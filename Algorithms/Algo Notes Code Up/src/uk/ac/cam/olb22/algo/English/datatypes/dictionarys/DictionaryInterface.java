package uk.ac.cam.olb22.algo.English.datatypes.dictionarys;

/**
 * Created by oliver on 03/05/17.
 */
public interface DictionaryInterface {
    void set(int key, Object value);
    Object get(int key);
    void delete(int key);
}
