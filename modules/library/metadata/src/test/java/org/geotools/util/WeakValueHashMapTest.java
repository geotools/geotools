/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import org.junit.Test;

/**
 * Tests the {@link WeakHashSet}. A standard {@link HashMap} object is used for comparaison purpose.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class WeakValueHashMapTest {
    /** The size of the test sets to be created. */
    private static final int SAMPLE_SIZE = 500;

    /**
     * Tests the {@link WeakValueHashMap} using strong references. The tested {@link WeakValueHashMap} should behave
     * like a standard {@link Map} object.
     */
    @Test
    public void testStrongReferences() {
        final Random random = new Random();
        for (int pass = 0; pass < 4; pass++) {
            final WeakValueHashMap<Integer, Integer> weakMap = new WeakValueHashMap<>();
            final HashMap<Integer, Integer> strongMap = new HashMap<>();
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                final Integer key = random.nextInt(SAMPLE_SIZE);
                final Integer value = random.nextInt(SAMPLE_SIZE);
                assertEquals("containsKey:", strongMap.containsKey(key), weakMap.containsKey(key));
                assertSame("get:", strongMap.get(key), weakMap.get(key));
                if (random.nextBoolean()) {
                    // Test addition.
                    assertSame("put:", strongMap.put(key, value), weakMap.put(key, value));
                } else {
                    // Test remove
                    assertSame("remove:", strongMap.remove(key), weakMap.remove(key));
                }
                assertEquals("equals:", strongMap, weakMap);
            }
        }
    }

    /**
     * Tests the {@link WeakValueHashMap} using weak references. In this test, we have to keep in mind than some
     * elements in {@code weakMap} may disaspear at any time.
     */
    @Test
    public void testWeakReferences() throws InterruptedException {
        final Random random = new Random();
        for (int pass = 0; pass < 2; pass++) {
            final WeakValueHashMap<UUID, UUID> weakMap = new WeakValueHashMap<>();
            final HashMap<UUID, UUID> strongMap = new HashMap<>();
            for (int i = 0; i < SAMPLE_SIZE; i++) {
                // We really want new instances here.
                final UUID key = UUID.randomUUID();
                final UUID value = UUID.randomUUID();
                if (random.nextBoolean()) {
                    /*
                     * Tests addition.
                     */
                    final UUID weakPrevious = weakMap.put(key, value);
                    final UUID strongPrevious = strongMap.put(key, value);
                    if (weakPrevious == null) {
                        // If the element was not in the WeakValueHashMap (i.e. if the garbage
                        // collector has cleared it), then it must not been in HashMap neither
                        // (otherwise GC should not have cleared it).
                        assertNull("put:", strongPrevious);
                    } else {
                        assertNotSame(value, weakPrevious);
                    }
                    if (strongPrevious != null) {
                        // Note: If 'strongPrevious==null', 'weakPrevious' may not
                        //       be null if GC has not collected its entry yet.
                        assertSame("put:", strongPrevious, weakPrevious);
                    }
                } else {
                    /*
                     * Tests remove
                     */
                    final UUID weakPrevious = weakMap.get(key);
                    final UUID strongPrevious = strongMap.remove(key);
                    if (strongPrevious != null) {
                        assertSame("remove:", strongPrevious, weakPrevious);
                    }
                }
            }
            // Do our best to lets GC finish its work.
            for (int i = 0; i < 4; i++) {
                Thread.sleep(50);
                System.gc();
            }
            assertEquals("equals:", strongMap, weakMap);
        }
    }

    @Test
    public void testArrayIndexOutOfBounds() {
        // hard to reproduce bug, the sizes and actions here have been carefully crafted
        // to make it happen
        WeakValueHashMap<Integer, Integer> map = new WeakValueHashMap<>(10);
        @SuppressWarnings("ModifiedButNotUsed") // revisit?
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            Integer v = Integer.valueOf(i);
            values.add(v);
            map.put(v, v);
        }
        Integer last = Integer.valueOf(9);
        map.put(last, last);

        // reduce the size enough that we put the map on its threshold for shrinking
        for (int i = 0; i < 5; i++) {
            map.remove(Integer.valueOf(i));
        }

        // now replace the last element, which will force the map to shrink
        last = Integer.valueOf(9);
        values.add(last);
        map.put(last, last); // it used to throw the arrays out of bound here

        assertEquals(3, map.size());
        assertTrue(map.containsKey(Integer.valueOf(5)));
        assertTrue(map.containsKey(Integer.valueOf(6)));
        assertTrue(map.containsKey(Integer.valueOf(9)));
    }
}
