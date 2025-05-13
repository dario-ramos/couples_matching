package couplesmatching.minheap;

public class Element {
    private Object data;
    private Comparable priority;

    public Element(Object data, Comparable priority) {
        this.data = data;
        this.priority = priority;
    }

    public Object getData() {
        return data;
    }

    public Comparable getPriority() {
        return priority;
    }

}
