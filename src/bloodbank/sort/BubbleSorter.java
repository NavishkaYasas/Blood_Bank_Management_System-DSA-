package bloodbank.sort;

import java.util.Comparator;
import java.util.List;

/** Member 2's component. O(n^2) worst/average, O(n) best (already sorted, with early-exit flag). */
public class BubbleSorter<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        int n = list.size();
        for (int i = 0; i < n - 1; i++) {
            boolean swapped = false;
            for (int j = 0; j < n - 1 - i; j++) {
                if (comparator.compare(list.get(j), list.get(j + 1)) > 0) {
                    T temp = list.get(j);
                    list.set(j, list.get(j + 1));
                    list.set(j + 1, temp);
                    swapped = true;
                }
            }
            if (!swapped) break; // already sorted -> best case O(n)
        }
    }

    @Override
    public String getName() { return "Bubble Sort"; }
}
