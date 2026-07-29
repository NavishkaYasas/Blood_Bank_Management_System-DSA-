package bloodbank.sort;

import java.util.Comparator;
import java.util.List;

public interface SortStrategy<T> {
    /** Sorts the given list IN PLACE using the provided comparator. */
    void sort(List<T> list, Comparator<T> comparator);

    /** Name of the algorithm, used for menu display and reporting. */
    String getName();
}
