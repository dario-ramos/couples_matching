package couplesmatching.minheap;

/**
 * Min Heap: at any given time, the element in the root is the minimum.
 * This allows using this data structure as a priority queue. If a lower
 * numerical value means a higher priority, then the root will always be
 * the element with the highest priority.
 * In spite of the size being required as an argument, this structure can
 * grow dynamically to accommodate more elements.
 */
public class MinHeap {
    private Element[] elements;
    private int currentSize;

    public MinHeap(int initialCapacity, Comparable minusInfinity) {
        elements = new Element[initialCapacity + 1];
        currentSize = 0;
        elements[0] = new Element(null, minusInfinity);
        // The first element is always a fake parent for the actual root.
        // It's a value lower than all possible values, hence the name "minusInfinity".
        // For example, for int/Integer, it should be Integer.MIN_VALUE.
        // This simplifies the implementation.
        for (int i = 1; i <= initialCapacity; i++) {
            elements[i] = null;
        }
    }

    public boolean isEmpty() {
        return currentSize == 0;
    }

    public void removeAll() {
        for (int i = 1; i < currentSize; i++) {
            elements[i] = null;
        }
        currentSize = 0;
    }

    /**
     * @return The object associated to the current root, without removing it.
     * If the heap is empty, returns null.
     */
    public Object peekRoot() {
        if (elements[1] == null) {
            return null;
        }
        return elements[1].getData();
    }

    public void insert(Object o, Comparable prior) {
        if (currentSize == elements.length - 1) {
            duplicateCapacity();
        }

        // Start from the end and go up as long as the new element has a higher priority than the parent.
        int hole = currentSize + 1;
        while (((elements[hole / 2].getPriority()).compareTo(prior)) > 0) {
            elements[hole] = elements[hole / 2];
            hole /= 2;
        }
        elements[hole] = new Element(o, prior);
        currentSize++;
    }

    private void duplicateCapacity() {
        Element[] newElementsArray = new Element[elements.length * 2];
        System.arraycopy(elements, 0, newElementsArray, 0, elements.length);
        elements = newElementsArray;
    }

    public Object extractRoot() {
        if (elements[1] == null) {
            return null;
        }
        Element e = elements[1];
        elements[1] = elements[currentSize];
        // The last element is now the root.
        elements[currentSize] = null;
        currentSize--;
        // Now, restablish the heap's order. This might require rebalancing.
        swapDown(1);
        return e.getData();
    }

    /**
     * Sends an element down the heap, keeping its order.
     * This is done by swapping elements around.
     */
    private void swapDown(int hole) {
        int child = hole * 2;
        if (child > currentSize) {
            return; // The element to sink has no children
        }
        if (child + 1 <= currentSize) { // If there is more than one child, choose the one with the highest priority
            if (elements[child].getPriority().compareTo(elements[child + 1].getPriority()) > 0) {
                child++;
            }
        }
        if (elements[child].getPriority().compareTo(elements[hole].getPriority()) < 0) {
            // Now, swap the node with the child chosen in the previous steps and continue recursively.
            Element aux = elements[child];
            elements[child] = elements[hole];
            elements[hole] = aux;
            swapDown(child);
        }
    }

}
