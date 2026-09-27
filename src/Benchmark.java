import java.util.Random;

public class Benchmark {

    private static final int[] SIZES = {
            100,
            1_000,
            10_000,
            100_000
    };

    private static final int REPETITIONS = 5;

    private static final int ACCESS_OPERATIONS = 10_000;
    private static final int SEARCH_OPERATIONS = 1_000;
    private static final int INSERT_REMOVE_OPERATIONS = 1_000;

    private static final long SEED = 42;

    public static void main(String[] args) {

        System.out.println("ASSIGNMENT 2 BENCHMARK");

        workload1RandomAccess();

        workload2Search();

        workload3InsertionRemoval();

        workload4MinHeap();
    }

    private static void workload1RandomAccess() {

        System.out.println();
        System.out.println("WORKLOAD 1 - RANDOM ACCESS");

        System.out.println(
                "n,Structure,AverageTimeNs,Accesses"
        );

        for (int n : SIZES) {

            long arrayTotalTime = 0;
            long arrayTotalAccesses = 0;

            long listTotalTime = 0;
            long listTotalAccesses = 0;

            for (int repetition = 0; repetition < REPETITIONS; repetition++) {

                Random random = new Random(SEED);

                int[] values = generateRandomValues(n, random);
                int[] indices = generateRandomIndices(
                        n,
                        ACCESS_OPERATIONS,
                        random
                );

                DynamicArray array = new DynamicArray(n);

                for (int value : values) {
                    array.add(value);
                }

                array.resetCounters();

                long start = System.nanoTime();

                for (int index : indices) {
                    array.get(index);
                }

                long end = System.nanoTime();

                arrayTotalTime += end - start;
                arrayTotalAccesses += array.getAccessCount();

                LinkedList list = new LinkedList();

                for (int value : values) {
                    list.add(value);
                }

                list.resetCounters();

                start = System.nanoTime();

                for (int index : indices) {
                    list.get(index);
                }

                end = System.nanoTime();

                listTotalTime += end - start;
                listTotalAccesses += list.getAccessCount();
            }

            long averageArrayTime =
                    arrayTotalTime / REPETITIONS;

            long averageArrayAccesses =
                    arrayTotalAccesses / REPETITIONS;

            long averageListTime =
                    listTotalTime / REPETITIONS;

            long averageListAccesses =
                    listTotalAccesses / REPETITIONS;

            System.out.println(
                    n + ",DynamicArray,"
                            + averageArrayTime + ","
                            + averageArrayAccesses
            );

            System.out.println(
                    n + ",LinkedList,"
                            + averageListTime + ","
                            + averageListAccesses
            );
        }
    }

    private static void workload2Search() {

        System.out.println();
        System.out.println("WORKLOAD 2 - SEARCH");
        System.out.println(
                "n,Structure,AverageTimeNs,Comparisons"
        );

        for (int n : SIZES) {

            long arrayTotalTime = 0;
            long arrayTotalComparisons = 0;

            long listTotalTime = 0;
            long listTotalComparisons = 0;

            for (int repetition = 0; repetition < REPETITIONS; repetition++) {

                Random random = new Random(SEED);

                int[] values = generateRandomValues(n, random);

                int[] searchValues =
                        generateSearchValues(
                                n,
                                SEARCH_OPERATIONS,
                                random
                        );

                DynamicArray array = new DynamicArray(n);

                for (int value : values) {
                    array.add(value);
                }

                array.resetCounters();

                long start = System.nanoTime();

                for (int value : searchValues) {
                    array.contains(value);
                }

                long end = System.nanoTime();

                arrayTotalTime += end - start;

                arrayTotalComparisons +=
                        array.getComparisonCount();

                LinkedList list = new LinkedList();

                for (int value : values) {
                    list.add(value);
                }

                list.resetCounters();

                start = System.nanoTime();

                for (int value : searchValues) {
                    list.contains(value);
                }

                end = System.nanoTime();

                listTotalTime += end - start;

                listTotalComparisons +=
                        list.getComparisonCount();
            }

            long averageArrayTime =
                    arrayTotalTime / REPETITIONS;

            long averageArrayComparisons =
                    arrayTotalComparisons / REPETITIONS;

            long averageListTime =
                    listTotalTime / REPETITIONS;

            long averageListComparisons =
                    listTotalComparisons / REPETITIONS;

            System.out.println(
                    n + ",DynamicArray,"
                            + averageArrayTime + ","
                            + averageArrayComparisons
            );

            System.out.println(
                    n + ",LinkedList,"
                            + averageListTime + ","
                            + averageListComparisons
            );
        }
    }

    private static void workload3InsertionRemoval() {

        System.out.println();
        System.out.println("WORKLOAD 3 - INSERTION AND REMOVAL");

        System.out.println(
                "n,Structure,Position,Operation,AverageTimeNs,Movements"
        );

        for (int n : SIZES) {

            runInsertionRemovalExperiment(n, 0, "Beginning");

            runInsertionRemovalExperiment(
                    n,
                    n / 2,
                    "Middle"
            );
        }
    }

    private static void runInsertionRemovalExperiment(
            int n,
            int index,
            String position
    ) {

        long arrayInsertTime = 0;
        long arrayInsertMovements = 0;

        long arrayRemoveTime = 0;
        long arrayRemoveMovements = 0;

        long listInsertTime = 0;
        long listInsertMovements = 0;

        long listRemoveTime = 0;
        long listRemoveMovements = 0;

        for (int repetition = 0;
             repetition < REPETITIONS;
             repetition++) {

            Random random = new Random(SEED);

            int[] values = generateRandomValues(n, random);

            // =====================================================
            // DYNAMIC ARRAY - INSERTION
            // =====================================================

            DynamicArray array = createDynamicArray(values);

            array.resetCounters();

            long start = System.nanoTime();

            for (int i = 0;
                 i < INSERT_REMOVE_OPERATIONS;
                 i++) {

                array.add(index, i);
            }

            long end = System.nanoTime();

            arrayInsertTime += end - start;

            arrayInsertMovements +=
                    array.getMovementCount();

            // =====================================================
            // DYNAMIC ARRAY - REMOVAL
            // =====================================================
            // Need at least 1000 elements so that 1000 removals
            // are possible even when n = 100.

            DynamicArray arrayForRemoval =
                    new DynamicArray(n + INSERT_REMOVE_OPERATIONS + 10);

            for (int i = 0;
                 i < n + INSERT_REMOVE_OPERATIONS;
                 i++) {

                arrayForRemoval.add(i);
            }

            arrayForRemoval.resetCounters();

            start = System.nanoTime();

            for (int i = 0;
                 i < INSERT_REMOVE_OPERATIONS;
                 i++) {

                arrayForRemoval.remove(index);
            }

            end = System.nanoTime();

            arrayRemoveTime += end - start;

            arrayRemoveMovements +=
                    arrayForRemoval.getMovementCount();

            // =====================================================
            // LINKED LIST - INSERTION
            // =====================================================

            LinkedList list = createLinkedList(values);

            list.resetCounters();

            start = System.nanoTime();

            for (int i = 0;
                 i < INSERT_REMOVE_OPERATIONS;
                 i++) {

                list.add(index, i);
            }

            end = System.nanoTime();

            listInsertTime += end - start;

            listInsertMovements +=
                    list.getMovementCount();

            // =====================================================
            // LINKED LIST - REMOVAL
            // =====================================================

            LinkedList listForRemoval = new LinkedList();

            for (int i = 0;
                 i < n + INSERT_REMOVE_OPERATIONS;
                 i++) {

                listForRemoval.add(i);
            }

            listForRemoval.resetCounters();

            start = System.nanoTime();

            for (int i = 0;
                 i < INSERT_REMOVE_OPERATIONS;
                 i++) {

                listForRemoval.remove(index);
            }

            end = System.nanoTime();

            listRemoveTime += end - start;

            listRemoveMovements +=
                    listForRemoval.getMovementCount();
        }

        // =========================================================
        // RESULTS
        // =========================================================

        System.out.println(
                n + ",DynamicArray,"
                        + position + ",Insertion,"
                        + arrayInsertTime / REPETITIONS + ","
                        + arrayInsertMovements / REPETITIONS
        );

        System.out.println(
                n + ",DynamicArray,"
                        + position + ",Removal,"
                        + arrayRemoveTime / REPETITIONS + ","
                        + arrayRemoveMovements / REPETITIONS
        );

        System.out.println(
                n + ",LinkedList,"
                        + position + ",Insertion,"
                        + listInsertTime / REPETITIONS + ","
                        + listInsertMovements / REPETITIONS
        );

        System.out.println(
                n + ",LinkedList,"
                        + position + ",Removal,"
                        + listRemoveTime / REPETITIONS + ","
                        + listRemoveMovements / REPETITIONS
        );
    }


    private static void workload4MinHeap() {

        System.out.println();
        System.out.println("WORKLOAD 4 - PRIORITY PROCESSING");

        System.out.println(
                "n,Operation,AverageTimeNs,Comparisons"
        );

        for (int n : SIZES) {

            long insertTotalTime = 0;
            long insertTotalComparisons = 0;

            long extractTotalTime = 0;
            long extractTotalComparisons = 0;

            for (int repetition = 0;
                 repetition < REPETITIONS;
                 repetition++) {

                Random random = new Random(SEED);

                int[] values =
                        generateRandomValues(n, random);

                MinHeap heap = new MinHeap(n);

                heap.resetCounters();

                long start = System.nanoTime();

                for (int value : values) {
                    heap.insert(value);
                }

                long end = System.nanoTime();

                insertTotalTime += end - start;

                insertTotalComparisons +=
                        heap.getComparisonCount();

                heap.resetCounters();

                start = System.nanoTime();

                int previous = Integer.MIN_VALUE;

                for (int i = 0; i < n; i++) {

                    int current = heap.extractMin();

                    if (current < previous) {
                        throw new RuntimeException(
                                "Heap extraction is not sorted!"
                        );
                    }

                    previous = current;
                }

                end = System.nanoTime();

                extractTotalTime += end - start;

                extractTotalComparisons +=
                        heap.getComparisonCount();
            }

            System.out.println(
                    n + ",Insert,"
                            + insertTotalTime / REPETITIONS + ","
                            + insertTotalComparisons / REPETITIONS
            );

            System.out.println(
                    n + ",ExtractMin,"
                            + extractTotalTime / REPETITIONS + ","
                            + extractTotalComparisons / REPETITIONS
            );
        }
    }


    private static int[] generateRandomValues(
            int n,
            Random random
    ) {

        int[] values = new int[n];

        for (int i = 0; i < n; i++) {
            values[i] = random.nextInt(1_000_000);
        }

        return values;
    }

    private static int[] generateRandomIndices(
            int n,
            int count,
            Random random
    ) {

        int[] indices = new int[count];

        for (int i = 0; i < count; i++) {
            indices[i] = random.nextInt(n);
        }

        return indices;
    }

    private static int[] generateSearchValues(
            int n,
            int count,
            Random random
    ) {

        int[] values = new int[count];

        for (int i = 0; i < count; i++) {

            values[i] = random.nextInt(1_000_000);
        }

        return values;
    }

    private static DynamicArray createDynamicArray(
            int[] values
    ) {

        DynamicArray array =
                new DynamicArray(values.length + 2000);

        for (int value : values) {
            array.add(value);
        }

        return array;
    }

    private static LinkedList createLinkedList(
            int[] values
    ) {

        LinkedList list = new LinkedList();

        for (int value : values) {
            list.add(value);
        }

        return list;
    }
}