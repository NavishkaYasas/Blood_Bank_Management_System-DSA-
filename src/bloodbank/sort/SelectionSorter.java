package bloodbank.sort;

import java.util.Comparator;
import java.util.List;

/**
 * Member 3's component.
 * Implements Selection Sort.
 * - Complexity: O(n^2) in all cases (always scans remaining list for minimum).
 * - Not stable (equal elements may swap order).
 */
public class SelectionSorter<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        int n = list.size();
        // Outer loop: iterate through each position in the list
        for (int i = 0; i < n - 1; i++) {
            int minIndex = i; // assume current index is minimum
            // Inner loop: find the smallest element in the unsorted portion
            for (int j = i + 1; j < n; j++) {
                if (comparator.compare(list.get(j), list.get(minIndex)) < 0) {
                    minIndex = j; // update index of minimum element
                }
            }
            // Swap if a smaller element was found
            if (minIndex != i) {
                T temp = list.get(i);
                list.set(i, list.get(minIndex));
                list.set(minIndex, temp);
            }
        }
    }

    @Override
    public String getName() {
        return "Selection Sort";
    }
}
