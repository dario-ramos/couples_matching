package couplesmatching;

import java.util.ArrayList;
import java.util.Iterator;

/*
 * This class represents a matchmaking agency that contains the information of every man and woman,
 * including each of their rankings.
 * The agency is also in charge of assigning an identifier to each man and woman.
 */
public class Agency {
    private ArrayList men;
    private ArrayList women;
    private ArrayList couples; // Transitory solution.
    private ArrayList solution; // Definitive solution.
    private int coupleCount; // Amount of non-broken couples in the "couples" attribute. Used in GS algorithm.

    public Agency() {
        men = new ArrayList();
        women = new ArrayList();
        couples = new ArrayList();
        coupleCount = 0;
    }

    public Person getMan(int ID) {
        return (Person) men.get(ID);
    }

    public Person getWoman(int ID) {
        return (Person) women.get(ID);
    }

    public int getMenCount() {
        return men.size();
    }

    public int getWomenCount() {
        return women.size();
    }

    public void addMan(Person man) {
        man.setID(men.size());
        men.add(man);
    }

    public void addWoman(Person woman) {
        woman.setID(women.size());
        women.add(woman);
    }

    /*
     * PRE: All men must have a complete ranking of all women, and vice versa.
     */
    public ArrayList assignUsingGaleShapley() {
        couples.clear();
        coupleCount = 0;
        // First round for men: each man proposes to his favorite woman.
        for (int i = 0; i < this.getMenCount(); i++) {
            Person man = this.getMan(i);
            man.extractFavoriteIntended().addSuitor(man);
            // This man is now a suitor for his favorite woman.
        }
        // First round for women: each woman chooses the man she likes the most.
        for (int i = 0; i < this.getWomenCount(); i++) {
            Person woman = this.getWoman(i);
            Person man = woman.peekFavoriteIntended();
            if (man != null) { // If someone had already proposed
                Couple couple = new Couple(man, woman);
                couples.add(couple);
                coupleCount++;
            }
            woman.forgetSuitors();
        }

        // Remaining round
        while (!galeShapleyComplete()) {
            // Each man chooses his favorite woman, as long as he hasn't been already rejected.
            for (int i = 0; i < this.getMenCount(); i++) {
                Person man = this.getMan(i);
                if (man.isSingle()) {
                    Person woman = man.extractFavoriteIntended();
                    woman.addSuitor(man);
                    // Consider current couple as well.
                    if (!woman.isSingle()) {
                        woman.addSuitor(woman.getCouple().getMan());
                    }
                }
            }
            // Every woman must choose between her suitors and current couple, if she has the latter.
            for (int i = 0; i < this.getWomenCount(); i++) {
                Person woman = this.getWoman(i);
                Person man = woman.peekFavoriteIntended();
                if (man != null) {
                    // If someone proposed and she has a couple, then break up.
                    if (!woman.isSingle()) {
                        woman.getCouple().breakUp();
                        coupleCount--;
                    }
                    Couple couple = new Couple(man, woman);
                    couples.add(couple);
                    coupleCount++;
                    woman.forgetSuitors();
                }
            }
        }
        // It's simpler to do this only once at the end
        removeBrokenCouples(couples);
        solution = couples;
        return solution;
    }

    /*
     * The Gale Shapley algorithm is considered done if and only fine every man has been coupled.
     * This implies that every woman is also coupled.
     */
    private boolean galeShapleyComplete() {
        return coupleCount == this.getMenCount();
    }

    /*
     * A broken couple is one in which one of its members is null.
     */
    private void removeBrokenCouples(ArrayList couples) {
        Iterator it = couples.iterator();
        while (it.hasNext()) {
            Couple p = (Couple) it.next();
            if ((p.getMan() == null) || (p.getWoman() == null)) {
                it.remove();
            }
        }
    }

    /*
     * PRE: All men must have a complete ranking of all women, and vice versa.
     */
    public ArrayList assignUsingBacktracking() {
        couples.clear();
        coupleCount = 0;
        solution = null;
        extendBackTrackingSolution(0);
        return solution;
    }

    /*
     * Backtracking implementation via recursion.
     * Invariant: During the execution of this algorithm, coupleCount == couples.size()
     */
    private void extendBackTrackingSolution(int currentMan) {
        if (backtrackingComplete()) {
            solution = couples;
            return;
        }

        // Solution not complete, so try to extend it
        for (int woman = 0; woman < this.getWomenCount(); woman++) {
            // Try all women, one by one
            Couple newCouple = new Couple(this.getMan(currentMan), this.getWoman(woman));
            couples.add(newCouple);
            coupleCount++;
            if (solutionIsStable()) {
                extendBackTrackingSolution(currentMan + 1);
            }
            if (solution != null) {
                return;
            }
            forgetLastCouple();
        }

    }

    private void forgetLastCouple() {
        couples.remove(couples.size() - 1);
        coupleCount--;
    }

    /*
     * The backtracking algorithm is done if and only if the current couple assignment is stable
     * and everyone is paired.
     */
    private boolean backtrackingComplete() {
        return (solutionIsStable() && (couples.size() == this.getMenCount()));
    }

    /*
     * A solution is considered stable if and only if it has no blocking pairs.
     */
    private boolean solutionIsStable() {
        // An empty assignment or a single couple assignment is stable
        if (couples.isEmpty() || (couples.size() == 1)) {
            return true;
        }
        // Since the previous n-1 couples have been checked, only the new couple must
        // be checked against all the rest.
        Couple newestCouple = (Couple) couples.get(couples.size() - 1);
        for (int i = 0; (i + 1) < couples.size(); i++) {
            if (this.couplePairIsBlocking((Couple) couples.get(i), newestCouple)) {
                return false;
            }
        }
        return true;
    }

    /*
     * A pair of couples is considered a blocking pair if and only a man and a woman
     * from different couples would rather be together than with their current partners.
     */
    private boolean couplePairIsBlocking(Couple c1, Couple c2) {
        Person m1 = c1.getMan();
        Person w1 = c1.getWoman();
        Person m2 = c2.getMan();
        Person w2 = c2.getWoman();

        if ((m1 == m2) || (w1 == w2)) {
            return true;
        }
        // If m1 likes w2 more than his wife, w1, and w2 feels the same way, then the pair is blocking.
        if ((m1.getRanking(w2) < m1.getRanking(w1)) && (w2.getRanking(m1) < w2.getRanking(m2))) {
            return true;
        }

        // Symmetric blocking case: w1 likes m2 more than m1, and m2 feels the same way.
        if ((w1.getRanking(m2) < w1.getRanking(m1)) && (m2.getRanking(w1) < m2.getRanking(w2))) {
            return true;
        }

        // In any other case, the pair is not blocking.
        return false;
    }
}
