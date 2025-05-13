package couplesmatching;

/*
 * Represents a particular man-woman pairing.
 */
public class Couple {
    private Person man;
    private Person woman;

    public Couple(Person man, Person woman) {
        this.man = man;
        this.woman = woman;
        man.formCouple(this);
        woman.formCouple(this);
    }

    public Person getMan() {
        return man;
    }

    public Person getWoman() {
        return woman;
    }

    public void breakUp() {
        man.breakUp();
        woman.breakUp();
        man = null;
        woman = null;
    }
}
