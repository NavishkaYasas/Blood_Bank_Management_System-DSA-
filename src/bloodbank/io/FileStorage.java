package bloodbank.io;

import java.util.List;

/**
 * ABSTRACTION: any module's file manager implements this so the rest of the
 * system does not need to know how load/save is actually performed.
 */
public interface FileStorage<T> {
    List<T> loadFromFile(String path);
    void saveToFile(String path, List<T> data);
}
