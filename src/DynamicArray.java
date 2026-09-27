public class DynamicArray {

    private int[] data;
    private int size;

    // Counters for experimental analysis
    private long accessCount;
    private long movementCount;
    private long comparisonCount;

    public DynamicArray() {
        data = new int[10];
        size = 0;
    }

    public DynamicArray(int capacity) {
        if (capacity <= 0) {
            capacity = 10;
        }

        data = new int[capacity];
        size = 0;
    }

    // O(1) amortized, O(n) when resizing
    public void add(int x) {
        if (size == data.length) {
            resize();
        }

        data[size] = x;
        accessCount++;
        size++;
    }

    // O(n)
    public void add(int index, int x) {
        checkPositionIndex(index);

        if (size == data.length) {
            resize();
        }

        for (int i = size; i > index; i--) {
            data[i] = data[i - 1];

            accessCount += 2;
            movementCount++;
        }

        data[index] = x;
        accessCount++;
        size++;
    }

    // O(n)
    public int remove(int index) {
        checkElementIndex(index);

        int removed = data[index];
        accessCount++;

        for (int i = index; i < size - 1; i++) {
            data[i] = data[i + 1];

            accessCount += 2;
            movementCount++;
        }

        size--;

        return removed;
    }

    // O(1)
    public int get(int index) {
        checkElementIndex(index);

        accessCount++;
        return data[index];
    }

    // O(n)
    public boolean contains(int x) {

        for (int i = 0; i < size; i++) {
            comparisonCount++;
            accessCount++;

            if (data[i] == x) {
                return true;
            }
        }

        return false;
    }

    private void resize() {
        int[] newData = new int[data.length * 2];

        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];

            accessCount += 2;
            movementCount++;
        }

        data = newData;
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