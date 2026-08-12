package bloodbank.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/** Member 5's component. Stable, O(n log n) in all cases; O(n) extra space. */
public class MergeSorter<T> implements SortStrategy<T> {

    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) return;
        List<T> sorted = mergeSort(list, comparator);
        for (int i = 0; i < sorted.size(); i++) {
            list.set(i, sorted.get(i));
        }
    }

    private List<T> mergeSort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) return list;
        int mid = list.size() / 2;
        List<T> left = mergeSort(new ArrayList<>(list.subList(0, mid)), comparator);
        List<T> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())), comparator);
        return merge(left, right, comparator);
    }

    private List<T> merge(List<T> left, List<T> right, Comparator<T> comparator) {
        List<T> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));
        return result;
    }

    @Override
    public String getName() { return "Merge Sort"; }
}
