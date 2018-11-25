package uk.ac.cam.olb22.algo.English.datatypes.sets;

import uk.ac.cam.olb22.algo.English.datatypes.dictionarys.DictionaryInterface;

/**
 * Created by oliver on 03/05/17.
 */
public abstract class AbstractSet implements DictionaryInterface {
    public abstract boolean isEmpty();
    public abstract boolean hasKey(int x);
    public abstract int chooseAny();
    public abstract int min();
    public abstract int max();
    public abstract int predecessor(int x);
    public abstract int successor(int x);
    public abstract AbstractSet unionWith(AbstractSet s);
    public abstract String getName();
}
