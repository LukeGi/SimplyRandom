package bluemonster.simplyrandom.util;

import java.util.Collection;
import java.util.Iterator;

public class Collections {
    public static <T> int indexOf(Collection<T> collection, T value) {
        if (collection.isEmpty() || value == null)
            return -1;
        Iterator<T> iterator = collection.iterator();
        for (int i = 0; i < collection.size(); i++) {
            T t = iterator.next();
            if (value.equals(t))
                return i;
        }
        return -1;
    }

    public static <T> T get(Collection<T> collection, int index) {
        if (index >= collection.size())
            return null;
        return ((T[])collection.toArray())[index];
    }
}
