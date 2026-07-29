package bloodbank.search;

import bloodbank.model.Donor;
import java.util.List;

/** Member 1's component. O(n) worst/average, O(1) best case (match at index 0). */
public class LinearSearch {

    /** Searches by donor name (case-insensitive, partial match allowed). Returns null if not found. */
    public static Donor searchByName(List<Donor> donors, String name) {
        for (Donor d : donors) {
            if (d.getName().equalsIgnoreCase(name)) {
                return d;
            }
        }
        return null;
    }

    /** Searches by exact donor ID. */
    public static Donor searchById(List<Donor> donors, String id) {
        for (Donor d : donors) {
            if (d.getId().equals(id)) {
                return d;
            }
        }
        return null;
    }
}
