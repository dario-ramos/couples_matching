package couplesmatching.minheap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MinHeapTest {

    private MinHeap p;

    @BeforeEach
    void setUp() {
        p = new MinHeap(5, Integer.MIN_VALUE);
    }

    @Test
    void isCreatedEmpty() {
        assertTrue(p.isEmpty());
    }

    @Test
    void firstElementOfEmptyHeapIsNull() {
        assertNull(p.peekRoot());
    }

    @Test
    void firstInsertedElementIsMin() {
        final String elementData = "elementData";
        p.insert(elementData, 1);
        assertEquals(elementData, p.peekRoot());
    }

    @Test
    void deletingLeavesHeapEmpty() {
        p.insert("element1", 1);
        p.insert("element2", 2);
        assertFalse(p.isEmpty());
        p.removeAll();
        assertTrue(p.isEmpty());
    }

    @Test
    void initialSizeCanBeSurpassed() {
        for (int i=1; i<=6; i++) {
            p.insert("element" + i, i);
        }
        assertEquals("element1", p.peekRoot());
    }

    @Test
    void insertingLowerPriorityFirstMaintainsOrder() {
        for (int i=5; i>=1; i--) {
            p.insert("element" + i, i);
        }
        assertEquals("element1", p.peekRoot());
    }

    @Test
    void retrievingMinFromEmptyHeapReturnsNull() {
        assertNull(p.extractRoot());
    }

    @Test
    void retrievingSingleMinElementEmptiesHeap() {
        assertNull(p.extractRoot());
        final String element = "element1";
        p.insert(element, 1);
        assertEquals(element, p.extractRoot());
        assertTrue(p.isEmpty());
    }

    @Test
    void priorityIsRespected() {
        p.insert("element3", 3);
        p.insert("element5", 5);
        p.insert("element1", 1);
        p.insert("element4", 4);
        p.insert("element2", 2);
        assertEquals("element1", p.extractRoot());
        assertEquals("element2", p.extractRoot());
        assertEquals("element3", p.extractRoot());
        assertEquals("element4", p.extractRoot());
        assertEquals("element5", p.extractRoot());
    }
}
