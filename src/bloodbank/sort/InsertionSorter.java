package bloodbank.sort;

import java.util.Comparator;
import java.util.List;

/**
 * Member 4's component.
 * Implements Insertion Sort.
 * Complexity:
 * - Best case: O(n) (already sorted).
 * - Average/Worst case: O(n^2).
 */
public class InsertionSorter<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        int n = list.size();
        // Start from index 1 (first element is trivially sorted)
        for (int i = 1; i < n; i++) {
            T key = list.get(i);   // element to insert
            int j = i - 1;
            // Shift elements greater than key one position ahead
            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            // Place key at its correct position
            list.set(j + 1, key);
        }
    }

    @Override
    public String getName() {
        return "Insertion Sort";
    }
}
