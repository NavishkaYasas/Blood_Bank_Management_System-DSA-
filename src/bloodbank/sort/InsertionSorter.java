package bloodbank.sort;

import java.util.Comparator;
import java.util.List;

/**
 * Best case : o(n)
 * worst case : o(n^2)
 * */
//insertion sort implementation

public class InsertionSorter<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        // take the number of elements in the list
        int n = list.size();
        for (int i = 1; i < n; i++) {
            T key = list.get(i);
            int j = i - 1;
            while (j >= 0 && comparator.compare(list.get(j), key) > 0) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    @Override
    // obtain the name of the sort algorithm
    public String getName() { return "Insertion Sort"; }
}