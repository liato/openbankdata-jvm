package org.openbankdata.core.utils;

import java.util.Arrays;

/**
 *
 */
public class ObjectUtils {

    private ObjectUtils() {
    }

    public static boolean equal(Object obj, Object other) {
        return obj == other || (obj != null && obj.equals(other));

    }

    public static int hashCode(Object... pObjects) {
        return Arrays.hashCode(pObjects);
    }
}
