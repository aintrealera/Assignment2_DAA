# Assignment 2 — Algorithmic Analysis, Correctness and Performance Trade-offs

## 1. Overview

This project implements three data structures from scratch in Java:

- Dynamic Array
- Linked List
- Min-Heap

The purpose of the assignment is to implement the required operations, analyze their asymptotic complexity, prove correctness with loop invariants, perform controlled experiments, and compare theoretical predictions with measured performance.

Required operations:

### Dynamic Array
- `add(x)`
- `add(index, x)`
- `remove(index)`
- `get(index)`
- `contains(x)`

### Linked List
- `add(x)`
- `add(index, x)`
- `remove(index)`
- `get(index)`
- `contains(x)`

### Min-Heap
- `insert(x)`
- `peekMin()`
- `extractMin()`

The implementation is analyzed as written. In particular, this Linked List implementation does not maintain a tail reference, so `add(x)` traverses to the end of the list.

---

## 2. Complexity Analysis

### 2.1 Dynamic Array

| Operation | Best | Average | Worst | Auxiliary Space |
|---|---|---|---|---|
| `add(x)` | Ω(1) | Θ(1) amortized | O(n) | O(1), O(n) during resize |
| `add(i,x)` | Ω(1) | Θ(n) | O(n) | O(1), O(n) during resize |
| `remove(i)` | Ω(1) | Θ(n) | O(n) | O(1) |
| `get(i)` | Θ(1) | Θ(1) | Θ(1) | O(1) |
| `contains(x)` | Ω(1) | Θ(n) | O(n) | O(1) |

`get(i)` is constant time because the index maps directly to an array position. Insertion and removal may require shifting elements, which takes linear time. Appending is constant time when capacity is available; a resize makes an individual append O(n), but repeated appends are Θ(1) amortized.

### 2.2 Linked List

| Operation | Best | Average | Worst | Auxiliary Space |
|---|---|---|---|---|
| `add(x)` | Ω(1) | Θ(n) | O(n) | O(1) |
| `add(i,x)` | Ω(1) | Θ(n) | O(n) | O(1) |
| `remove(i)` | Ω(1) | Θ(n) | O(n) | O(1) |
| `get(i)` | Ω(1) | Θ(n) | O(n) | O(1) |
| `contains(x)` | Ω(1) | Θ(n) | O(n) | O(1) |

For this implementation, `add(x)` traverses from the head to the last node, so it is linear rather than constant time. Indexed operations require traversal to the target position. Once the target node is reached, insertion/removal itself only changes a small number of references.

### 2.3 Min-Heap

| Operation | Best | Average | Worst | Auxiliary Space |
|---|---|---|---|---|
| `insert(x)` | Ω(1) | O(log n) | O(log n) | O(1), O(n) during resize |
| `peekMin()` | Θ(1) | Θ(1) | Θ(1) | O(1) |
| `extractMin()` | Ω(1) | O(log n) | O(log n) | O(1) |

`insert(x)` may move a new item upward through the heap. `peekMin()` reads the root directly. `extractMin()` replaces the root and may move the replacement downward through a logarithmic-height heap.

### 2.4 Important differences

Two operations can have the same Big-O complexity but noticeably different running times because of constant factors, memory access patterns, object/reference traversal, cache locality, JVM JIT optimization, and implementation details.

---

## 3. Correctness

## 3.1 Loop Invariant — Dynamic Array Insertion

Operation:

```java
add(int index, int x)
```

The shifting loop is:

```java
for (int i = size; i > index; i--) {
    data[i] = data[i - 1];
}
```

### Invariant

At the start of each loop iteration with current position `i`, every original element in positions `i, i+1, ..., size-1` has already been moved one position to the right, so positions `i+1, ..., size` contain the required shifted values. Positions from `index` through `i` have not yet all been shifted.

### Initialization

Before the first iteration, `i = size`. The set of elements that must already have been shifted is empty. Therefore the invariant is true.

### Maintenance

During an iteration, the algorithm executes:

```java
data[i] = data[i - 1];
```

The element originally at `i-1` is moved to `i`. Thus, after the iteration, one additional position at the right side satisfies the required shifted arrangement. The loop then decrements `i`, so the invariant remains true for the next iteration.

### Termination

The loop terminates when `i == index`. At this point, every original element from `index` through `size-1` has been moved one position to the right. Therefore position `index` is free for the new value.

The algorithm then executes:

```java
data[index] = x;
size++;
```

The final array therefore contains the original elements in the same order with `x` inserted at the requested index. Hence `add(index, x)` is correct.

---

## 3.2 Loop Invariant — Min-Heap Extraction

Operation:

```java
extractMin()
```

After replacing the root with the last element, the algorithm uses `heapifyDown(index)`:

```java
while (true) {
    ...
    if (smallest == index) {
        break;
    }
    swap(index, smallest);
    index = smallest;
}
```

### Invariant

At the start of every `heapifyDown` iteration, the heap property is valid everywhere except possibly at the current index. The two subtrees below the current index are already valid min-heaps, and the current node is the only possible violation.

### Initialization

After extraction, the root is replaced by the former last element. All nodes below the root keep their original parent-child relationships, so their heap properties remain valid. Therefore the only possible violation is at the root, which is the initial `index`.

### Maintenance

The algorithm compares the current node with its children and selects the smallest value.

- If the current node is already smallest, there is no violation and the loop terminates.
- Otherwise, the current node is swapped with the smallest child.

After the swap, the new parent is no greater than either child, while any possible violation has moved downward to the child's previous position. The invariant is therefore preserved with the new `index`.

### Termination

The loop ends when the current node is smaller than both children, or when it is a leaf. There is then no remaining heap-property violation. Since all other subtrees were already valid, the entire structure satisfies the min-heap property.

Therefore `extractMin()` restores a valid min-heap after removing the minimum element.

---

## 4. Experimental Setup

### Input sizes

The experiments use:

```text
n = 100
n = 1,000
n = 10,000
n = 100,000
```

The same workload definition is used as `n` changes.

### Repetitions

Each experiment is run 5 times and the reported execution time is the arithmetic average.

### Timing

Timing uses:

```java
System.nanoTime()
```

Input generation and printing are outside the timed section.

### Random seed

```java
new Random(42)
```

### Workload 1 — Random Access

For each `n`:

- create a structure containing `n` random integers;
- generate 10,000 random indices;
- execute `get(index)` 10,000 times;
- record average execution time;
- record element accesses.

### Workload 2 — Search

For each `n`:

- create a structure containing `n` random integers;
- generate 1,000 search values;
- execute `contains(value)` 1,000 times;
- record average execution time;
- count comparisons.

### Workload 3 — Insertion and Removal

For each `n`:

- perform 1,000 insertions at index `0`;
- perform 1,000 removals at index `0`;
- repeat at index `n / 2`;
- record time and movement/access instrumentation.

#### Removal note

The assignment specifies 1,000 removals for every `n`, including `n = 100`. Removing 1,000 items from an initially 100-element structure is impossible. Therefore the benchmark initializes a separate removal structure with `n + 1,000` elements so that all 1,000 removal operations can be executed without changing the requested `n` values used for the experimental scale.

### Workload 4 — Priority Processing

For each `n`:

- insert `n` random integers into an empty Min-Heap;
- measure all insertions;
- extract the minimum `n` times;
- measure all extractions;
- count comparisons;
- verify non-decreasing extraction order.

---

## 5. Results

CSV result files are stored in `results/tables/` in this package.

### Workload 1 — Random Access

| n | Dynamic Array time (ns) | Linked List time (ns) | Dynamic Array accesses | Linked List accesses |
|---:|---:|---:|---:|---:|
| 100 | 432,180 | 1,889,940 | 10,000 | 511,327 |
| 1,000 | 105,980 | 10,812,800 | 10,000 | 5,021,262 |
| 10,000 | 16,780 | 108,452,520 | 10,000 | 50,188,951 |
| 100,000 | 38,080 | 1,071,465,560 | 10,000 | 504,940,938 |

The number of Dynamic Array accesses remains constant because each `get` directly indexes the array. Linked List accesses grow roughly linearly with `n`, matching the theoretical O(n) traversal cost.

The measured Dynamic Array execution times do not increase monotonically at every size. This is consistent with practical effects such as JVM warm-up, cache behavior, and measurement noise. The access-count metric provides the clearer theoretical relationship.

### Workload 2 — Search

| n | Dynamic Array time (ns) | Linked List time (ns) | Comparisons |
|---:|---:|---:|---:|
| 100 | 663,240 | 672,520 | 99,996 |
| 1,000 | 1,784,740 | 2,402,460 | 998,832 |
| 10,000 | 7,776,780 | 21,598,340 | 9,948,508 |
| 100,000 | 76,794,500 | 211,904,220 | 95,047,696 |

Both structures perform the same linear search algorithm, so their comparison counts are identical for the same generated workload. Execution time differs because an array provides contiguous memory access while a linked list requires pointer traversal.

### Workload 3 — Insertion and Removal

The beginning-position experiment produces constant-size Linked List movement counts because insertion/removal at index 0 only changes a small number of references.

At `n = 100,000`:

| Structure | Position | Operation | Time (ns) | Movements |
|---|---|---|---:|---:|
| DynamicArray | Beginning | Insertion | 8,934,760 | 100,499,500 |
| DynamicArray | Beginning | Removal | 8,742,700 | 100,499,500 |
| LinkedList | Beginning | Insertion | 6,040 | 1,000 |
| LinkedList | Beginning | Removal | 2,500 | 1,000 |
| DynamicArray | Middle | Insertion | 4,394,140 | 50,499,500 |
| DynamicArray | Middle | Removal | 4,623,480 | 50,499,500 |
| LinkedList | Middle | Insertion | 104,806,080 | 50,001,000 |
| LinkedList | Middle | Removal | 104,952,280 | 50,000,000 |

These results illustrate why the physical organization matters: arrays move contiguous elements, whereas linked lists traverse nodes and then change references.

### Workload 4 — Priority Processing

| n | Insert time (ns) | ExtractMin time (ns) | Insert comparisons | ExtractMin comparisons |
|---:|---:|---:|---:|---:|
| 100 | 38,000 | 138,920 | 194 | 841 |
| 1,000 | 97,460 | 269,460 | 2,232 | 14,994 |
| 10,000 | 689,700 | 1,808,760 | 22,593 | 216,736 |
| 100,000 | 3,383,460 | 16,258,560 | 227,662 | 2,831,463 |

The comparison counts and execution times increase with `n`, with extraction requiring more work than insertion because each extraction may move an element down the heap.

### Plots

The `results/plots/` directory contains plots for all four workloads:

- workload 1 execution time and accesses;
- workload 2 execution time and comparisons;
- workload 3 beginning execution time and movements;
- workload 3 middle execution time and movements;
- workload 4 execution time and comparisons.

---

## 6. Discussion

### 6.1 Effect of increasing n

Increasing `n` affects workloads according to the operation performed.

- Random access strongly separates the structures because array access is constant time while linked-list access requires traversal.
- Search grows approximately linearly for both structures because `contains` scans elements sequentially.
- Insertion/removal at the beginning favors linked-list pointer changes over array shifting.
- Insertion/removal in the middle requires linked-list traversal, while the array shifts a contiguous block.
- Heap insertion and extraction grow according to heap height, which is logarithmic per individual operation.

### 6.2 Agreement with theoretical complexity

The strongest agreement appears in the operation counts:

- Dynamic Array `get`: constant number of array accesses.
- Linked List `get`: approximately linear increase in accesses.
- Search: linear comparison growth.
- Beginning Linked List insertion/removal: approximately constant movement count.
- Middle Linked List operations: movement/traversal work grows with `n`.
- Heap operations: comparison counts increase roughly in line with logarithmic per-operation cost.

Measured wall-clock times generally follow the same patterns, but are noisier because runtime depends on hardware, JVM optimization, memory hierarchy, allocation, and other constant factors.

### 6.3 Why same Big-O can still have different running times

Big-O describes asymptotic growth rather than exact execution time. Two O(n) operations can have different constants. For example, an array search performs simple contiguous accesses, while a linked-list search repeatedly follows references.

### 6.4 Constant factors and implementation details

Practical performance is affected by:

- contiguous versus non-contiguous memory;
- pointer/reference traversal;
- object allocation;
- cache locality;
- branch behavior;
- JVM JIT compilation;
- resizing/copying;
- instrumentation overhead.

Therefore measured time should be interpreted together with operation counts.

---

## 7. Design Recommendations

The data structure should be chosen according to the workload.

### Dynamic Array

A Dynamic Array is suitable when:

- random indexed access is frequent;
- compact contiguous storage is useful;
- searches are acceptable as linear scans;
- insertions/removals are not frequently performed near the beginning or middle.

### Linked List

A Linked List is useful when:

- insertions/removals occur at known nodes or at the beginning;
- constant-time pointer changes are more important than random access;
- indexed access is relatively uncommon.

This particular implementation is less efficient for appending at the end because it does not maintain a tail reference.

### Min-Heap

A Min-Heap is appropriate for priority-based processing because it provides direct access to the minimum element and supports insertion/extraction in logarithmic time.

### Workload matters

There is no single data structure that minimizes every workload. The appropriate choice depends on which operation dominates the workload.

---

## 8. Conclusion

The implemented Dynamic Array, Linked List, and Min-Heap demonstrate different trade-offs.

The Dynamic Array provides constant-time indexed access and good locality but may require many element movements for indexed insertion/removal. The Linked List avoids large element shifts for beginning operations but pays traversal costs for indexed access and middle operations. The Min-Heap provides an efficient structure for repeated minimum-priority processing.

The experimental data broadly agrees with the theoretical analysis. Operation counters show the expected asymptotic behavior especially clearly, while execution times show additional practical effects caused by implementation and runtime factors.

The main result of the experiment is that data-structure performance depends on both asymptotic complexity and the actual workload.

---

## Project structure

```text
assignment-2/
├── src/
│   ├── DynamicArray.java
│   ├── LinkedList.java
│   ├── MinHeap.java
│   ├── Benchmark.java
│   └── Tests.java
├── results/
│   ├── tables/
│   │   ├── workload1_random_access.csv
│   │   ├── workload2_search.csv
│   │   ├── workload3_insertion_removal.csv
│   │   └── workload4_priority_processing.csv
│   └── plots/
│       ├── workload1_execution_time_vs_n.png
│       ├── workload1_accesses_vs_n.png
│       ├── workload2_execution_time_vs_n.png
│       ├── workload2_comparisons_vs_n.png
│       ├── workload3_beginning_execution_time_vs_n.png
│       ├── workload3_beginning_movements_vs_n.png
│       ├── workload3_middle_execution_time_vs_n.png
│       ├── workload3_middle_movements_vs_n.png
│       ├── workload4_execution_time_vs_n.png
│       └── workload4_comparisons_vs_n.png
└── README.md
```
