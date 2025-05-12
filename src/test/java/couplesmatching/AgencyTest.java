package couplesmatching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AgencyTest {

    private Agencia agency;
    private Individuo john, smith, tony;
    private Individuo anna, bethany, rose;

    @BeforeEach
    void setUp() {
        agency = new Agencia();
        john = new Individuo("John", true);
        smith = new Individuo("Smith", true);
        tony = new Individuo("Tony", true);
        anna = new Individuo("Anna", false);
        bethany = new Individuo("Bethany", false);
        rose = new Individuo("Rose", false);
        agency.addHombre(john);
        agency.addHombre(smith);
        agency.addHombre(tony);
        agency.addMujer(anna);
        agency.addMujer(bethany);
        agency.addMujer(rose);
        setUpMenPreferences();
        setUpWomenPreferences();
    }

    void setUpMenPreferences() {
        john.setPuesto(bethany, 0);
        john.setPuesto(anna, 1);
        john.setPuesto(rose, 2);
        smith.setPuesto(bethany, 0);
        smith.setPuesto(rose, 1);
        smith.setPuesto(anna, 2);
        tony.setPuesto(rose, 0);
        tony.setPuesto(bethany, 1);
        tony.setPuesto(anna, 2);
    }

    void setUpWomenPreferences() {
        anna.setPuesto(smith, 0);
        anna.setPuesto(tony, 1);
        anna.setPuesto(john, 2);
        bethany.setPuesto(tony, 0);
        bethany.setPuesto(john, 1);
        bethany.setPuesto(smith, 2);
        rose.setPuesto(smith, 0);
        rose.setPuesto(tony, 1);
        rose.setPuesto(john, 2);
    }

    @Test
    void galeShapleySolution() {
        ArrayList couples = agency.asignarConGS();

        assertCoupleEquality(new Pareja(smith, rose), (Pareja) couples.get(0));
        assertCoupleEquality(new Pareja(tony, bethany), (Pareja) couples.get(1));
        assertCoupleEquality(new Pareja(john, anna), (Pareja) couples.get(2));
    }

    @Test
    void backtrackingSolution() {
        ArrayList couples = agency.asignarConBT();

        assertCoupleEquality(new Pareja(john, anna), (Pareja) couples.get(0));
        assertCoupleEquality(new Pareja(smith, rose), (Pareja) couples.get(1));
        assertCoupleEquality(new Pareja(tony, bethany), (Pareja) couples.get(2));
    }

    private void assertCoupleEquality(Pareja couple1, Pareja couple2) {
        assertEquals(couple1.getHombre().getNombre(), couple2.getHombre().getNombre());
        assertEquals(couple1.getMujer().getNombre(), couple2.getMujer().getNombre());
    }
}
