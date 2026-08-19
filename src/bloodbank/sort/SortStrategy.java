package bloodbank.sort;

import java.util.Comparator;
import java.util.List;

/**
 * Common interface for all sorting strategies.
 * - Defines a contract: every sorter must implement `sort()` and `getName()`.
 * - Enables polymorphism: client code can use any sorter interchangeably.
 */
public interface SortStrategy<T> {
    /** Sorts the given list IN PLACE using the provided comparator. */
    void sort(List<T> list, Comparator<T> comparator);

    /** Name of the algorithm, used for menu display and reporting. */
    String getName();
}
