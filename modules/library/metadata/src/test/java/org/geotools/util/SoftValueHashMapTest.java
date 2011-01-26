/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;
import java.util.ConcurrentModificationException;
import java.util.logging.Logger;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link SoftValueHashMap}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class SoftValueHashMapTest {
    /**
     * The size of the test sets to be created.
     */
    private static final int SAMPLE_SIZE = 200;

    /**
     * Tests the {@link SoftValueHashMap} using strong references. The tested
     * {@link SoftValueHashMap} should behave like a standard {@link Map} object.
     */
    @Test
    public void testStrongReferences() {
        final Random random = getRandom();
        for (int pass=0; pass<4; pass++) {
            // make sure we can keep as many strong references as the sample, since there is no guarantee
            // the distribution of random.nextBoolean() is uniform in the short term 
            final SoftValueHashMap<Integer,Integer> softMap = new SoftValueHashMap<Integer,Integer>(SAMPLE_SIZE);
            final HashMap<Integer,Integer>        strongMap = new HashMap<Integer,Integer>();
            for (int i=0; i<SAMPLE_SIZE; i++) {
                final Integer key   = random.nextInt(SAMPLE_SIZE);
                final Integer value = random.nextInt(SAMPLE_SIZE);
                assertEquals("containsKey:",   strongMap.containsKey(key),
                                                 softMap.containsKey(key));
                assertEquals("containsValue:", strongMap.containsValue(value),
                                                 softMap.containsValue(value));
                assertSame  ("get:",           strongMap.get(key),
                                                 softMap.get(key));
                assertEquals("equals:", strongMap, softMap);
                if (random.nextBoolean()) {
                    // Test addition.
                    assertSame("put:", strongMap.put(key, value),
                                         softMap.put(key, value));
                } else {
                    // Test remove
                    assertSame("remove:", strongMap.remove(key),
                                            softMap.remove(key));
                }
                assertEquals("equals:", strongMap, softMap);
            }
        }
    }

    /**
     * Tests the {@link SoftValueHashMap} using soft references.
     * In this test, we have to keep in mind than some elements
     * in {@code softMap} may disaspear at any time.
     */
    @Test
    public void testSoftReferences() throws InterruptedException {
        final Random random = getRandom();
        final SoftValueHashMap<Integer,Integer> softMap = new SoftValueHashMap<Integer,Integer>();
        final HashMap<Integer,Integer> strongMap = new HashMap<Integer,Integer>();
        for (int pass=0; pass<2; pass++) {
            int count = 0;
            softMap.clear();
            strongMap.clear();
            for (int i=0; i<SAMPLE_SIZE; i++) {
                // We really want new instances below.
                final Integer key   = new Integer(random.nextInt(SAMPLE_SIZE));
                final Integer value = new Integer(random.nextInt(SAMPLE_SIZE));
                if (random.nextBoolean()) {
                    /*
                     * Test addition.
                     */
                    final Integer   softPrevious = softMap  .put(key, value);
                    final Integer strongPrevious = strongMap.put(key, value);
                    if (softPrevious == null) {
                        // If the element was not in the SoftValueHashMap (i.e. if the garbage
                        // collector has cleared it), then it must not been in HashMap neither
                        // (otherwise GC should not have cleared it).
                        assertNull("put:", strongPrevious);
                        count++; // Count only the new values.
                    } else {
                        assertNotSame(value, softPrevious);
                    }
                    if (strongPrevious != null) {
                        // Note: If 'strongPrevious==null', 'softPrevious' may not
                        //       be null if GC has not collected its entry yet.
                        assertSame("put:", strongPrevious, softPrevious);
                    }
                } else {
                    /*
                     * Test remove
                     */
                    final Integer   softPrevious = softMap.get(key);
                    final Integer strongPrevious = strongMap.remove(key);
                    if (strongPrevious != null) {
                        assertSame("remove:", strongPrevious, softPrevious);
                    }
                }
                assertTrue("containsAll:", softMap.entrySet().containsAll(strongMap.entrySet()));
            }
            // Do our best to lets GC finish its work.
            for (int i=0; i<20; i++) {
                Runtime.getRuntime().gc();
                Runtime.getRuntime().runFinalization();
            }
            assertTrue(softMap.isValid());
            assertTrue("size:", softMap.size() <= count);
            /*
             * Make sure that all values are of the correct type. More specifically, we
             * want to make sure that we didn't forget to convert some Reference object.
             */
            for (final Iterator it=softMap.values().iterator(); it.hasNext();) {
                final Object value;
                try {
                    value = it.next();
                } catch (ConcurrentModificationException e) {
                    break;
                }
                assertTrue(value instanceof Integer);
                assertNotNull(value);
            }
        }
    }
    
    private Random getRandom() {
    	long seed = System.currentTimeMillis() + hashCode();
    	Random random = new Random(seed);
    	Logger.getLogger(this.getClass().getName()).info("Using Random Seed: "+seed);
    	return random;
    }
}
