package couplesmatching.heap;

import couplesmatching.parva.Parva;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {

    private Parva p;

    @BeforeEach
    void setUp() {
        p = new Parva(5, Integer.MIN_VALUE);
    }

    @Test
    void isCreatedEmpty() {
        assertTrue(p.esVacia());
    }

    @Test
    void firstElementOfEmptyHeapIsNull() {
        assertNull(p.primero());
    }

    @Test
    void firstInsertedElementIsMin() {
        final String elementData = "elementData";
        p.insertar(elementData, 1);
        assertEquals(elementData, p.primero());
    }

    @Test
    void deletingLeavesHeapEmpty() {
        p.insertar("element1", 1);
        p.insertar("element2", 2);
        assertFalse(p.esVacia());
        p.borrar();
        assertTrue(p.esVacia());
    }

    @Test
    void initialSizeCanBeSurpassed() {
        for (int i=1; i<=6; i++) {
            p.insertar("element" + i, i);
        }
        assertEquals("element1", p.primero());
    }

    @Test
    void insertingLowerPriorityFirstMaintainsOrder() {
        for (int i=5; i>=1; i--) {
            p.insertar("element" + i, i);
        }
        assertEquals("element1", p.primero());
    }

    @Test
    void retrievingMinFromEmptyHeapReturnsNull() {
        assertNull(p.getMaximo());
    }

    @Test
    void retrievingSingleMinElementEmptiesHeap() {
        assertNull(p.getMaximo());
        final String element = "element1";
        p.insertar(element, 1);
        assertEquals(element, p.getMaximo());
        assertTrue(p.esVacia());
    }

    @Test
    void priorityIsRespected() {
        p.insertar("element3", 3);
        p.insertar("element5", 5);
        p.insertar("element1", 1);
        p.insertar("element4", 4);
        p.insertar("element2", 2);
        assertEquals("element1", p.getMaximo());
        assertEquals("element2", p.getMaximo());
        assertEquals("element3", p.getMaximo());
        assertEquals("element4", p.getMaximo());
        assertEquals("element5", p.getMaximo());
    }
}
