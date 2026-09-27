public class MinHeap {

    private int[] heap;
    private int size;

    // Counters for experimental analysis
    private long comparisonCount;
    private long movementCount;
    private long accessCount;

    public MinHeap() {
        heap = new int[10];
        size = 0;
    }

    public MinHeap(int capacity) {

        if (capacity <= 0) {
            capacity = 10;
        }

        heap = new int[capacity];
        size = 0;
    }

    // O(log n)
    public void insert(int x) {

        ensureCapacity();

        int index = size;
        heap[index] = x;

        accessCount++;
        size++;

        // Bubble up
        while (index > 0) {

            int parent = (index - 1) / 2;

            comparisonCount++;
            accessCount += 2;

            if (heap[parent] <= heap[index]) {
                break;
            }

            swap(parent, index);

            index = parent;
        }
    }

    // O(1)
    public int peekMin() {

        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        accessCount++;

        return heap[0];
    }

    // O(log n)
    public int extractMin() {

        if (size == 0) {
            throw new IllegalStateException("Heap is empty");
        }

        int min = heap[0];
        accessCount++;

        heap[0] = heap[size - 1];
        accessCount++;
        movementCount++;

        size--;

        if (size > 0) {
            heapifyDown(0);
        }

        return min;
    }

    private void heapifyDown(int index) {

        while (true) {

            int left = 2 * index + 1;
            int right = 2 * index + 2;

            int smallest = index;

            if (left < size) {

                comparisonCount++;
                accessCount += 2;

                if (heap[left] < heap[smallest]) {
                    smallest = left;
                }
            }

            if (right < size) {

                comparisonCount++;
                accessCount += 2;

                if (heap[right] < heap[smallest]) {
                    smallest = right;
                }
            }

            if (smallest == index) {
                break;
            }

            swap(index, smallest);

            index = smallest;
        }
    }

    private void swap(int i, int j) {

        int temp = heap[i];
        heap[i] = heap[j];
        heap[j] = temp;

        accessCount += 3;
        movementCount += 3;
    }

    private void ensureCapacity() {

        if (size == heap.length) {

            int[] newHeap = new int[heap.length * 2];

            for (int i = 0; i < heap.length; i++) {

                newHeap[i] = heap[i];

                accessCount += 2;
                movementCount++;
            }

            heap = newHeap;
        }
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void resetCounters() {
        comparisonCount = 0;
        movementCount = 0;
        accessCount = 0;
    }

    public long getComparisonCount() {
        return comparisonCount;
    }

    public long getMovementCount() {
        return movementCount;
    }

    public long getAccessCount() {
        return accessCount;
    }
    public boolean isValidHeap() {

        for (int i = 0; i < size; i++) {

            int left = 2 * i + 1;
            int right = 2 * i + 2;

            if (left < size && heap[i] > heap[left]) {
                return false;
            }

            if (right < size && heap[i] > heap[right]) {
                return false;
            }
        }

        return true;
    }
}