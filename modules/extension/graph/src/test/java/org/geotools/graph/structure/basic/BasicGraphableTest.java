/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.structure.basic;

import java.util.Iterator;
import org.geotools.graph.structure.Graphable;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicGraphableTest {

    private BasicGraphable m_graphable;

    @Before
    public void setUp() throws Exception {

        m_graphable =
                new BasicGraphable() {
                    @Override
                    public Iterator<? extends Graphable> getRelated() {
                        return null;
                    }
                };
    }

    /**
     * Test BasicGraphable#setVisited(boolean) method. <br>
     * <br>
     * Test: Clear visited flag, then reset.<br>
     * Expected: Visited flag should be set.
     */
    @Test
    public void test_setVisited() {
        Assert.assertFalse(m_graphable.isVisited());
        m_graphable.setVisited(true);
        Assert.assertTrue(m_graphable.isVisited());
    }

    /**
     * Test BasicGraphable#setObject(Object). <br>
     * <br>
     * Test: Create a new object and set underlying object. Expected: Underlying object should be
     * equal to created object.
     */
    @Test
    public void test_setObject() {
        Object obj = Integer.valueOf(1);

        Assert.assertNull(m_graphable.getObject());
        m_graphable.setObject(obj);

        Assert.assertSame(m_graphable.getObject(), obj);
    }

    /**
     * Test BasicGraphable#setCount(int). <br>
     * <br>
     * Test: Change value of counter.<br>
     * Expected: Counter equal to new value.
     */
    @Test
    public void test_setCount() {
        Assert.assertEquals(m_graphable.getCount(), -1);
        m_graphable.setCount(10);

        Assert.assertEquals(m_graphable.getCount(), 10);
    }

    /**
     * Test BasicGraphable#setID(int). <br>
     * <br>
     * Test: Change id.<br>
     * Expected: Id equal to new value.
     */
    @Test
    public void test_setID() {
        m_graphable.setID(10);
        Assert.assertEquals(m_graphable.getID(), 10);
    }
}
