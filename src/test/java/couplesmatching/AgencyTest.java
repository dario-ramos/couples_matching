package couplesmatching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgencyTest {

    private Agency agency;
    private Person john, smith, tony;
    private Person anna, bethany, rose;

    @BeforeEach
    void setUp() {
        agency = new Agency();
        john = new Person("John", true);
        smith = new Person("Smith", true);
        tony = new Person("Tony", true);
        anna = new Person("Anna", false);
        bethany = new Person("Bethany", false);
        rose = new Person("Rose", false);
        agency.addMan(john);
        agency.addMan(smith);
        agency.addMan(tony);
        agency.addWoman(anna);
        agency.addWoman(bethany);
        agency.addWoman(rose);
        setUpMenPreferences();
        setUpWomenPreferences();
    }

    void setUpMenPreferences() {
        john.setRanking(bethany, 0);
        john.setRanking(anna, 1);
        john.setRanking(rose, 2);
        smith.setRanking(bethany, 0);
        smith.setRanking(rose, 1);
        smith.setRanking(anna, 2);
        tony.setRanking(rose, 0);
        tony.setRanking(bethany, 1);
        tony.setRanking(anna, 2);
    }

    void setUpWomenPreferences() {
        anna.setRanking(smith, 0);
        anna.setRanking(tony, 1);
        anna.setRanking(john, 2);
        bethany.setRanking(tony, 0);
        bethany.setRanking(john, 1);
        bethany.setRanking(smith, 2);
        rose.setRanking(smith, 0);
        rose.setRanking(tony, 1);
        rose.setRanking(john, 2);
    }

    @Test
    void galeShapleySolution() {
        ArrayList couples = agency.assignUsingGaleShapley();

        assertCoupleEquality(new Couple(smith, rose), (Couple) couples.get(0));
        assertCoupleEquality(new Couple(tony, bethany), (Couple) couples.get(1));
        assertCoupleEquality(new Couple(john, anna), (Couple) couples.get(2));
    }

    @Test
    void backtrackingSolution() {
        ArrayList couples = agency.assignUsingBacktracking();

        assertCoupleEquality(new Couple(john, anna), (Couple) couples.get(0));
        assertCoupleEquality(new Couple(smith, rose), (Couple) couples.get(1));
        assertCoupleEquality(new Couple(tony, bethany), (Couple) couples.get(2));
    }

    private void assertCoupleEquality(Couple couple1, Couple couple2) {
        assertEquals(couple1.getMan().getName(), couple2.getMan().getName());
        assertEquals(couple1.getWoman().getName(), couple2.getWoman().getName());
    }
}
