package bloodbank.io;

import java.util.List;

/**
 * ABSTRACTION: any module's file manager implements this so the rest of the
 * system does not need to know how load/save is actually performed.
 */
public interface FileStorage<T> {
    // Load data of type T from a file at the given path
    List<T> loadFromFile(String path);

    // Save a list of data of type T into a file at the given path
    void saveToFile(String path, List<T> data);
}
