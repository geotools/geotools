/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.junit.Test;

public class PartiallyOrderedSetTest {

    @Test
    public void testIterationOverEmptySet() {
        PartiallyOrderedSet<String> poset = new PartiallyOrderedSet<>();
        for (String s : poset) {
            fail();
        }
    }

    @Test
    public void testNoOrder() {
        PartiallyOrderedSet<String> poset = new PartiallyOrderedSet<>();
        poset.add("a");
        poset.add("b");
        poset.add("c");

        assertEquals(3, poset.size());
        assertTrue(poset.contains("a"));
        assertTrue(poset.contains("b"));
        assertTrue(poset.contains("c"));

        // leverage the backing linkedhashmap for predictable order
        List<String> entries = toList(poset);
        assertEquals(3, entries.size());
        assertEquals("a", entries.get(0));
        assertEquals("b", entries.get(1));
        assertEquals("c", entries.get(2));
    }

    @Test
    public void testRemoveNoOrder() {
        PartiallyOrderedSet<String> poset = new PartiallyOrderedSet<>();
        poset.add("a");
        poset.add("b");
        poset.add("c");

        assertEquals(3, poset.size());
        poset.remove("b");
        assertEquals(2, poset.size());
        assertTrue(poset.contains("a"));
        assertTrue(poset.contains("c"));

        // leverage the backing linkedhashmap for predictable order
        List<String> entries = toList(poset);
        assertEquals(2, entries.size());
        assertEquals("a", entries.get(0));
        assertEquals("c", entries.get(1));
    }

    @Test
    public void testFullyOrdered() {
        PartiallyOrderedSet<String> poset = new PartiallyOrderedSet<>();
        poset.add("a");
        poset.add("b");
        poset.add("c");
        poset.setOrder("c", "b");
        poset.setOrder("b", "a");

        // check the full ordering
        List<String> entries = toList(poset);
        assertEquals(3, entries.size());
        assertEquals("c", entries.get(0));
        assertEquals("b", entries.get(1));
        assertEquals("a", entries.get(2));
    }

    @Test
    public void testPartiallyOrdered() {
        PartiallyOrderedSet<String> poset = new PartiallyOrderedSet<>();
        poset.add("a");
        poset.add("b");
        poset.add("c");
        poset.add("d");
        poset.setOrder("c", "b");
        poset.setOrder("d", "a");

        // check the partial order relation
        List<String> entries = toList(poset);
        // System.out.println(entries);
        assertEquals(4, entries.size());
        assertTrue(entries.indexOf("c") < entries.indexOf("b"));
        assertTrue(entries.indexOf("d") < entries.indexOf("a"));
    }

    @Test
    public void testPartiallyOrdered2() {
        PartiallyOrderedSet<String> poset = creatBowTieGraph();

        // check the partial order relation
        List<String> entries = toList(poset);
        // System.out.println(entries);
        assertEquals(5, entries.size());
        assertTrue(entries.indexOf("a") < entries.indexOf("b"));
        assertTrue(entries.indexOf("d") < entries.indexOf("b"));
        assertTrue(entries.indexOf("b") < entries.indexOf("c"));
        assertTrue(entries.indexOf("b") < entries.indexOf("e"));
    }

    @Test
    public void testModifyOrder() {
        PartiallyOrderedSet<String> poset = creatBowTieGraph();
        poset.clearOrder("a", "b");
        poset.setOrder("e", "a");

        // check the partial order relation
        List<String> entries = toList(poset);
        // System.out.println(entries);
        assertEquals(5, entries.size());
        assertTrue(entries.indexOf("e") < entries.indexOf("a"));
        assertTrue(entries.indexOf("b") < entries.indexOf("a"));
        assertTrue(entries.indexOf("d") < entries.indexOf("b"));
        assertTrue(entries.indexOf("b") < entries.indexOf("c"));
        assertTrue(entries.indexOf("b") < entries.indexOf("e"));
    }

    private PartiallyOrderedSet<String> creatBowTieGraph() {
        PartiallyOrderedSet<String> poset = new PartiallyOrderedSet<>();
        poset.add("a");
        poset.add("b");
        poset.add("c");
        poset.add("d");
        poset.add("e");
        poset.setOrder("a", "b");
        poset.setOrder("d", "b");
        poset.setOrder("b", "c");
        poset.setOrder("b", "e");
        return poset;
    }

    @Test(expected = IllegalStateException.class)
    public void testLoop() {
        PartiallyOrderedSet<String> poset = new PartiallyOrderedSet<>();
        poset.add("a");
        poset.add("b");
        poset.add("c");
        poset.setOrder("a", "b");
        poset.setOrder("b", "c");
        poset.setOrder("c", "a");

        // should throw an exception right away
        poset.iterator();
    }

    @Test(expected = IllegalStateException.class)
    public void testIsolatedLoop() {
        PartiallyOrderedSet<String> poset = new PartiallyOrderedSet<>();
        poset.add("a");
        poset.add("b");
        poset.add("c");
        poset.add("d");
        poset.setOrder("a", "b");
        poset.setOrder("b", "c");
        poset.setOrder("c", "a");

        // get the iterator, should throw after the first item
        Iterator<String> it = poset.iterator();
        it.next();
        it.next();
    }

    @Test
    public void testRepeatedOrder() {
        PartiallyOrderedSet<String> poset = new PartiallyOrderedSet<>();
        poset.add("a");
        poset.add("b");
        poset.setOrder("a", "b");
        poset.setOrder("a", "b");

        // leverage the backing linkedhashmap for predictable order
        List<String> entries = toList(poset);
        assertEquals(2, entries.size());
        assertEquals("a", entries.get(0));
        assertEquals("b", entries.get(1));
    }

    @Test
    public void testRepeatedAddThenRemove() {
        PartiallyOrderedSet<String> poset = new PartiallyOrderedSet<>();
        poset.add("a");
        poset.add("b");
        poset.setOrder("a", "b");
        poset.remove("a");
        poset.add("a");
        poset.setOrder("a", "b");
        poset.remove("a");
        poset.add("a");
        poset.setOrder("a", "b");

        // verify that repeated adding and removal does not break the set
        List<String> entries = toList(poset);
        assertEquals(2, entries.size());
        assertEquals("a", entries.get(0));
        assertEquals("b", entries.get(1));
    }

    public <E> List<E> toList(PartiallyOrderedSet<E> poset) {
        List<E> result = new ArrayList<E>();
        for (E e : poset) {
            result.add(e);
        }
        return result;
    }
}
