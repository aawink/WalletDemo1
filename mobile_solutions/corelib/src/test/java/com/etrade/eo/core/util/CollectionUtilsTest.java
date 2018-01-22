package com.etrade.eo.core.util;

import org.junit.Test;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CollectionUtilsTest {

    @Test(expected = UnsupportedOperationException.class)
    public void noInstantiation() throws Throwable {
        try {
            Constructor<CollectionUtils> instance = CollectionUtils.class.getDeclaredConstructor();
            instance.setAccessible(true);
            instance.newInstance();
        } catch (InvocationTargetException e) {
            throw e.getCause();
        }
    }

    @Test
    public void isNullOrEmptyCollection() throws Exception {
        final Collection nullCollection = null;
        assertTrue(CollectionUtils.isNullOrEmpty(nullCollection));

        assertTrue(CollectionUtils.isNullOrEmpty(new ArrayList<>()));

        assertFalse(CollectionUtils.isNullOrEmpty(Arrays.asList("one", "two")));
    }

    @Test
    public void isNullOrEmptyArray() throws Exception {
        final Object[] nullArray = null;
        assertTrue(CollectionUtils.isNullOrEmpty(nullArray));

        assertTrue(CollectionUtils.isNullOrEmpty(new String[] { }));

        assertFalse(CollectionUtils.isNullOrEmpty(new String[] { "one", "two" }));
    }

    @Test
    public void contains() throws Exception {
        final Integer[] integers = new Integer[] { 1, 2, 3 };

        assertTrue(CollectionUtils.contains(integers, 1));
        assertTrue(CollectionUtils.contains(integers, integers[0]));
        assertTrue(CollectionUtils.contains(integers, integers[1]));
        assertTrue(CollectionUtils.contains(integers, integers[2]));

        assertFalse(CollectionUtils.contains(integers, 4));
    }

    @SuppressWarnings("RedundantStringConstructorCall")
    @Test
    public void indexOf() throws Exception {
        final Integer[] integers = new Integer[] { 1, 2, 2, null };

        assertEquals(CollectionUtils.indexOf(integers, 1), 0);
        assertEquals(CollectionUtils.indexOf(integers, 2), 1);
        assertEquals(CollectionUtils.indexOf(integers, null), 3);

        assertEquals(CollectionUtils.indexOf(integers, 1337), -1);
        assertEquals(CollectionUtils.indexOf(null, 1), -1);

        final String[] strings = new String[] { "0", null, "2" };
        assertEquals(CollectionUtils.indexOf(strings, new String("0")), 0);
        assertEquals(CollectionUtils.indexOf(strings, null), 1);
        assertEquals(CollectionUtils.indexOf(strings, new String("2")), 2);
    }
}
