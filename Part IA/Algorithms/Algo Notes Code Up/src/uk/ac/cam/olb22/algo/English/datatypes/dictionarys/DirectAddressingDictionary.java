package uk.ac.cam.olb22.algo.English.datatypes.dictionarys;

/**
 * Created by oliver on 03/05/17.
 */
public class DirectAddressingDictionary implements DictionaryInterface {
    Object[] data;

    @Override
    public void set(int key, Object value) {
        if (key >= data.length) {
            expandArray(key);
        }
        data[key] = value;
    }

    private void expandArray(int key) {
        int newSize = key*2;
        Object[] newData = new Object[newSize];
        for (int i = 0; i < data.length; i++) {
            newData[i] = data[i];
        }
        data = newData;
    }

    @Override
    public Object get(int key) {
        if (key >= data.length) {
            return null;
        } else {
            return data[key];
        }
    }

    @Override
    public void delete(int key) {
        if (key >= data.length) {

        } else {
            data[key] = null;
        }
    }

    public DirectAddressingDictionary(int initialMaxKey) {
        data = new Object[initialMaxKey+1];
    }
}
