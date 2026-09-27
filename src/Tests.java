public class Tests {

    public static void main(String[] args) {

        System.out.println("DYNAMIC ARRAY TESTS");

        testDynamicArray();

        System.out.println();

        System.out.println("LINKED LIST TESTS");

        testLinkedList();

        System.out.println();

        System.out.println("MIN HEAP TESTS");

        testMinHeap();

        System.out.println();
        System.out.println("ALL TESTS PASSED");
    }

    private static void testDynamicArray() {

        DynamicArray array = new DynamicArray();

        assert array.size() == 0;
        assert array.isEmpty();

        array.add(10);

        assert array.size() == 1;
        assert array.get(0) == 10;

        array.add(20);
        array.add(30);

        assert array.size() == 3;
        assert array.get(1) == 20;

        array.add(0, 5);

        assert array.get(0) == 5;
        assert array.get(1) == 10;

        array.add(2, 15);

        assert array.get(2) == 15;

        array.add(array.size(), 40);

        assert array.get(array.size() - 1) == 40;

        array.add(20);

        assert array.contains(20);

        int removed = array.remove(2);

        assert removed == 15;
        assert !array.contains(15);

        assert array.contains(10);
        assert !array.contains(999);

        boolean exceptionThrown = false;

        try {
            array.get(-1);
        } catch (IndexOutOfBoundsException e) {
            exceptionThrown = true;
        }

        assert exceptionThrown;

        exceptionThrown = false;

        try {
            array.remove(100);
        } catch (IndexOutOfBoundsException e) {
            exceptionThrown = true;
        }

        assert exceptionThrown;

        System.out.println("DynamicArray: PASS");
    }

    private static void testLinkedList() {

        LinkedList list = new LinkedList();

        assert list.size() == 0;
        assert list.isEmpty();

        list.add(10);

        assert list.size() == 1;
        assert list.get(0) == 10;

        list.add(20);
        list.add(30);

        assert list.size() == 3;

        list.add(0, 5);

        assert list.get(0) == 5;

        list.add(2, 15);

        assert list.get(2) == 15;

        list.add(list.size(), 40);

        assert list.get(list.size() - 1) == 40;

        list.add(20);
        assert list.contains(20);

        int removed = list.remove(0);

        assert removed == 5;
        assert list.get(0) == 10;

        removed = list.remove(1);

        assert removed == 15;

        assert list.contains(10);
        assert !list.contains(999);

        boolean exceptionThrown = false;

        try {
            list.get(-1);
        } catch (IndexOutOfBoundsException e) {
            exceptionThrown = true;
        }

        assert exceptionThrown;

        exceptionThrown = false;

        try {
            list.remove(100);
        } catch (IndexOutOfBoundsException e) {
            exceptionThrown = true;
        }

        assert exceptionThrown;

        System.out.println("LinkedList: PASS");
    }

    private static void testMinHeap() {

        MinHeap heap = new MinHeap();

        assert heap.size() == 0;
        assert heap.isEmpty();

        heap.insert(10);

        assert heap.peekMin() == 10;
        assert heap.isValidHeap();

        heap.insert(5);
        heap.insert(20);
        heap.insert(3);
        heap.insert(8);

        assert heap.peekMin() == 3;
        assert heap.isValidHeap();

        heap.insert(3);

        assert heap.peekMin() == 3;
        assert heap.isValidHeap();

        int previous = Integer.MIN_VALUE;

        while (!heap.isEmpty()) {

            int current = heap.extractMin();

            assert current >= previous;
            assert heap.isValidHeap();

            previous = current;
        }

        boolean exceptionThrown = false;

        try {
            heap.peekMin();
        } catch (IllegalStateException e) {
            exceptionThrown = true;
        }

        assert exceptionThrown;

        System.out.println("MinHeap: PASS");
    }
}