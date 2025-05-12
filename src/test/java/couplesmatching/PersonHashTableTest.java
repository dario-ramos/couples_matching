package couplesmatching;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PersonHashTableTest {

    private TablaHash t;

    @BeforeEach
    void setUp() {
        t = new TablaHash();
    }

    @Test
    void singlePersonRetrieval() {
        final String personName = "John";
        Individuo i = new Individuo(personName, true);
        t.add(i);
        assertEquals(i, t.get(personName));
    }

    @Test
    void retrievingInexistentPersonReturnsNull() {
        assertNull(t.get("John"));
    }

    @Test
    void multiplePersonsRetrieval() {
        Individuo john = new Individuo("John", true);
        t.add(john);
        Individuo mary = new Individuo("Mary", false);
        t.add(mary);
        assertEquals(john, t.get(john.getNombre()));
        assertEquals(mary, t.get(mary.getNombre()));
    }

    @Test
    void initialSizeCanBeSurpassed() {
        TablaHash smallTable = new TablaHash();
        Individuo[] men = new Individuo[6];
        for (int i=1; i<=6; i++) {
            Individuo man = new Individuo("Hombre" + i, true);
            men[i-1] = man;
            smallTable.add(man);
        }
        for (int i=1; i<=6; i++) {
            Individuo man = smallTable.get("Hombre" + i);
            assertEquals(men[i-1], man);
        }
    }
}
