package couplesmatching;

import couplesmatching.minheap.MinHeap;

/*
 * Out of all the information associated with a Person, we only care about the fields
 * required to solve the problem of stable couple matching.
 * TODO If created with the second constructor that specifies the couple count, Gale Shapley goes into
 *  an infinite loop.
 */
public class Person {
    private String name;
    private boolean isMale;
    private int ID;
    private Couple couple; // Couple to which this Person currently belongs.
    private int[] positions; // Relative to the ranking.
    private int amountOfPeopleRanked;
    private MinHeap intended; // IDs of all Persons pretended, sorted by ranking.
    private MinHeap suitors;  // IDs of all Persons that have proposed to this one.

    public Person(String name, boolean isMale) {
        this.name = name;
        this.isMale = isMale;
        couple = null;
        // TODO Don't assume: always solve the problem for the exact number of people given
        // Eliminate this assumption and leave a single constructor
        // Default max people: 1000.
        amountOfPeopleRanked = 1000;
        positions = new int[amountOfPeopleRanked];
        suitors = new MinHeap(1000, Integer.MIN_VALUE);
        intended = new MinHeap(1000, Integer.MIN_VALUE);
    }

    public Person(String name, boolean isMale, int peopleCount) {
        this.name = name;
        this.isMale = isMale;
        couple = null;
        amountOfPeopleRanked = peopleCount;
        positions = new int[amountOfPeopleRanked];
        suitors = new MinHeap(peopleCount, Integer.MAX_VALUE); // TODO Using MAX instead of MIN might be causing a bug
        intended = new MinHeap(peopleCount, Integer.MAX_VALUE);
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return ID;
    }

    public Couple getCouple() {
        return couple;
    }

    public boolean isSingle() {
        return (couple == null);
    }

    /*
     * Returns the ranking given to p by this person.
     * If this person hasn't ranked p yet, returns -1.
     */
    public int getRanking(Person p) {
        if (p.getID() < 0) {
            return -1;
        }
        if (p.getID() > amountOfPeopleRanked) {
            return -1;
        }
        return positions[p.getID()];
    }

    /*
     * Returns this Person's highest ranked intended.
     * This is a destructive read: for each call, the highest ranked is removed.
     * Therefore, calling this method N times will return all the intended Persons
     * in descending ranking order.
     */
    public Person extractFavoriteIntended() {
        return (Person) intended.extractRoot();
    }

    /*
     * Returns this Person's highest ranked intended, but without
     * extracting it from the ranking. This is a non-destructive read.
     * Multiple successive calls will always return the same Person.
     */
    public Person peekFavoriteIntended() {
        return (Person) suitors.peekRoot();
    }

    public void forgetSuitors() {
        suitors.removeAll();
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public void formCouple(Couple couple) {
        this.couple = couple;
    }

    public void breakUp() {
        couple = null;
    }

    /*
     * Assign a ranking to an intended Person. Zero will correspond to the favorite
     * intended, and N-1 to the least desired one.
     * If two Persons have the same ID, this method won't be able to tell them apart.
     * If a negative ranking value is provided, it will be capped to zero.
     */
    public void setRanking(Person intended, int ranking) {
        if (intended.ID <= amountOfPeopleRanked) {
            positions[intended.ID] = Math.max(ranking, 0);
            this.intended.insert(intended, ranking);
            return;
        }

        // Increase the "positions" list's size by 1.5.
        int newSize = Math.round(intended.ID * 1.5f);
        int[] newArray = new int[newSize];
        for (int j = 0; j < amountOfPeopleRanked; j++) {
            newArray[j] = positions[j];
        }
        amountOfPeopleRanked = newSize;
        positions = newArray;
    }

    public void addSuitor(Person suitor) {
        suitors.insert(suitor, getRanking(suitor));
    }
}
