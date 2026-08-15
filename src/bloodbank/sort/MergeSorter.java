package bloodbank.sort;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

// Merge Sort implementation - sorts a list by dividing, sorting halves, and merging
public class MergeSorter<T> implements SortStrategy<T> {

    // Sort the list using merge sort
    @Override
    public void sort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) return; // Already sorted
        List<T> sorted = mergeSort(list, comparator);
        for (int i = 0; i < sorted.size(); i++) { // Copy sorted result back into original list
            list.set(i, sorted.get(i));
        }
    }

    // Recursively split the list into halves and merge them
    private List<T> mergeSort(List<T> list, Comparator<T> comparator) {
        if (list.size() <= 1) return list; // Base case
        int mid = list.size() / 2; // Find the middle
        List<T> left = mergeSort(new ArrayList<>(list.subList(0, mid)), comparator); // Sort left half
        List<T> right = mergeSort(new ArrayList<>(list.subList(mid, list.size())), comparator); // Sort right half
        return merge(left, right, comparator); // Merge both halves
    }

    // Merge two sorted lists into one sorted list
    private List<T> merge(List<T> left, List<T> right, Comparator<T> comparator) {
        List<T> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) { // Compare elements from both lists
            if (comparator.compare(left.get(i), right.get(j)) <= 0) {
                result.add(left.get(i++)); // Left is smaller, add it
            } else {
                result.add(right.get(j++)); // Right is smaller, add it
            }
        }
        while (i < left.size()) result.add(left.get(i++)); // Add remaining left elements
        while (j < right.size()) result.add(right.get(j++)); // Add remaining right elements
        return result;
    }

    // Return the name of this sorting algorithm
    @Override
    public String getName() { return "Merge Sort"; }
}
