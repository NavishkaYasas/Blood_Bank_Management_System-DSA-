package bloodbank.sort;

import java.util.Comparator;
import java.util.List;

/**
 * Member 2's component.
 * Implements Bubble Sort.
 * Complexity:
 * - Worst/Average case: O(n^2) (must compare and swap many times).
 * - Best case: O(n) (already sorted, early exit with swapped flag).
 */
public class BubbleSorter<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        int n = list.size();
        // Outer loop: runs n-1 passes
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false; // flag to detect if any swap happened
            // Inner loop: compare adjacent elements
            for (int j = 0; j < n - 1 - i; j++) {
                // If current element > next element, swap them
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            // If no swaps occurred, list is already sorted → exit early
            if (!swapped) break;
        }
    }

    @Override
    public String getName() {
        return "Bubble Sort";
    }
}
