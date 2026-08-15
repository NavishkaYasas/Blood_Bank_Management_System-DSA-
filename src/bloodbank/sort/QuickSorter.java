package bloodbank.sort;

import java.util.Comparator;
import java.util.List;

// Quick Sort implementation using the SortStrategy interface
// Average time: O(n log n), Worst case: O(n²) with a poor pivot choice
public class QuickSorter<T> implements SortStrategy<T> {

    // Starts the Quick Sort algorithm for the complete list
    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        quickSort(list, 0, list.size() - 1, comparator);
    }

    // Recursively sorts the elements before and after the pivot
    private void quickSort(List<T> list, int low, int high, Comparator<T> comparator) {
        if (low < high) {

            // Partition the list and get the pivot position
            int pivotIndex = partition(list, low, high, comparator);

            // Sort the left side of the pivot
            quickSort(list, low, pivotIndex - 1, comparator);

            // Sort the right side of the pivot
            quickSort(list, pivotIndex + 1, high, comparator);
        }
    }

     // Places the pivot in its correct position
    private int partition(List<T> list, int low, int high, Comparator<T> comparator) {

        // Select the last element as the pivot
        T pivot = list.get(high);

        // Index for elements smaller than or equal to the pivot
        int i = low - 1;

        // Compare each element with the pivot
        for (int j = low; j < high; j++) {
            if (comparator.compare(list.get(j), pivot) <= 0) {
                i++;

                // Swap the current element with the element at index i
                T temp = list.get(i);
                list.set(i, list.get(j));
                list.set(j, temp);
            }
        }

        // Place the pivot in its correct position
        T temp = list.get(i + 1);
        list.set(i + 1, list.get(high));
        list.set(high, temp);
        return i + 1;
    }

    // Returns the name of this sorting algorithm
    @Override
    public String getName() { return "Quick Sort"; }
}
