package bloodbank.sort;

import java.util.Comparator;
import java.util.List;

/**
 * Member 6's component.
 * Implements Quick Sort.
 * - Average case: O(n log n).
 * - Worst case: O(n^2) (poor pivot choice, e.g. always picking min/max).
 */
public class QuickSorter<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        quickSort(list, 0, list.size() - 1, comparator);
    }

    // Recursive quicksort
    private void quickSort(List<T> list, int low, int high, Comparator<T> comparator) {
        if (low < high) {
            // Partition the list and get pivot index
            int pivotIndex = partition(list, low, high, comparator);
            // Recursively sort left and right sublists
            quickSort(list, low, pivotIndex - 1, comparator);
            quickSort(list, pivotIndex + 1, high, comparator);
        }
    }

    // Partition method: places pivot in correct position
    private int partition(List<T> list, int low, int high, Comparator<T> comparator) {
        T pivot = list.get(high); // choose last element as pivot
        int i = low - 1;          // index of smaller element
        for (int j = low; j < high; j++) {
            // If current element <= pivot, swap it into correct side
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }
        // Place pivot in correct position
        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1; // return pivot index
    }

    @Override
    public String getName() {
        return "Quick Sort";
    }
}
