package com.etrade.eo.core.util;

import java.util.Arrays;
import java.util.Collection;

@SuppressWarnings("WeakerAccess")
public final class CollectionUtils {

    private CollectionUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Determines whether the collection is null or empty.
     * @param coll Collection to check
     * @return true if null or empty
     */
    public static boolean isNullOrEmpty(final Collection<?> coll) {
        return coll == null || coll.isEmpty();
    }

    /**
     * Checks an array for null or empty.
     * @param array Array to check
     * @return true is array is null or empty
     */
    public static boolean isNullOrEmpty(final Object[] array) {
        return array == null || array.length <= 0;
    }

    /**
     * Determines whether an array contains a specified item.
     * @param array Array to check
     * @param item Item to find
     * @return true if the array contains the item
     */
    public static <T> boolean contains(T[] array, T item) {
        Arrays.asList(array);
        return indexOf(array, item) >= 0;
    }

    /**
     * Determines the first index of an element within an array. Consider using
     * {@link java.util.Arrays#binarySearch(Object[], Object)} if sorted.
     * @param array Array to check
     * @param item Item to find the index of
     * @return Index of the first item if found. -1 if input array is null, or element not in array
     */
    public static int indexOf(Object[] array, Object item) {
        int index = -1;

        if (!isNullOrEmpty(array)) {
            for (int i = 0; i < array.length && index == -1; i++) {
                final Object itemAtIndex = array[i];

                if (itemAtIndex == item || (itemAtIndex != null && itemAtIndex.equals(item))) {
                    index = i;
                }
            }
        }
        return index;
    }
}
