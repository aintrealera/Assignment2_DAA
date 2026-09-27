public class LinkedList {

    private static class Node {

        int data;
        Node next;

        Node(int data) {
            this.data = data;
            this.next = null;
        }
    }

    private Node head;
    private int size;

    // Counters for experimental analysis
    private long accessCount;
    private long movementCount;
    private long comparisonCount;

    public LinkedList() {
        head = null;
        size = 0;
    }

    // O(1)
    public void add(int x) {

        Node newNode = new Node(x);

        if (head == null) {
            head = newNode;
        } else {
            Node current = head;

            while (current.next != null) {
                current = current.next;
                accessCount++;
            }

            current.next = newNode;
            movementCount++;
        }

        size++;
    }

    // O(n)
    public void add(int index, int x) {

        checkPositionIndex(index);

        Node newNode = new Node(x);

        if (index == 0) {
            newNode.next = head;
            head = newNode;

            movementCount++;
            size++;
            return;
        }

        Node previous = getNode(index - 1);

        newNode.next = previous.next;
        previous.next = newNode;

        movementCount += 2;
        size++;
    }

    // O(n)
    public int remove(int index) {

        checkElementIndex(index);

        if (index == 0) {

            int removed = head.data;
            head = head.next;

            movementCount++;
            size--;

            return removed;
        }

        Node previous = getNode(index - 1);
        Node target = previous.next;

        int removed = target.data;

        previous.next = target.next;

        movementCount++;
        size--;

        return removed;
    }

    // O(n)
    public int get(int index) {

        checkElementIndex(index);

        Node node = getNode(index);

        return node.data;
    }

    // O(n)
    public boolean contains(int x) {

        Node current = head;

        while (current != null) {

            comparisonCount++;
            accessCount++;

            if (current.data == x) {
                return true;
            }

            current = current.next;
            movementCount++;
        }

        return false;
    }

    private Node getNode(int index) {

        Node current = head;

        for (int i = 0; i < index; i++) {
            current = current.next;

            accessCount++;
            movementCount++;
        }

        accessCount++;

        return current;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void resetCounters() {
        accessCount = 0;
        movementCount = 0;
        comparisonCount = 0;
    }

    public long getAccessCount() {
        return accessCount;
    }

    public long getMovementCount() {
        return movementCount;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    private void checkElementIndex(int index) {

        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }
    }

    private void checkPositionIndex(int index) {

        if (index < 0 || index > size) {
            throw new IndexOutOfBoundsException(
                    "Index: " + index + ", Size: " + size
            );
        }
    }
}