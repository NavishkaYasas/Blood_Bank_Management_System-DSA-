package bloodbank.search;

import bloodbank.model.Donor;
import java.util.List;

/**
 * Member 1's component.
 * Implements Linear Search for donor lookup.
 * Complexity:
 * - Worst/Average case: O(n) (must scan entire list).
 * - Best case: O(1) (match found at index 0).
 */
public class LinearSearch {

    /**
     * Searches donors by name.
     * - Case-insensitive comparison.
     * - Currently requires exact name match (not true partial match).
     * - Returns null if no donor is found.
     */
    public static Donor searchByName(List<Donor> donors, String name) {
        for (Donor d : donors) {
            if (d.getName().equalsIgnoreCase(name)) {
                return d;
            }
        }
        return null;
    }

    /**
     * Searches donors by exact ID.
     * - Returns the donor if ID matches.
     * - Returns null if not found.
     */
    public static Donor searchById(List<Donor> donors, String id) {
        for (Donor d : donors) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }
}
