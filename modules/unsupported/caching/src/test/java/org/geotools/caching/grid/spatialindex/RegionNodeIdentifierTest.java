/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching.grid.spatialindex;

import java.util.HashMap;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.caching.spatialindex.Region;
import org.geotools.caching.spatialindex.RegionNodeIdentifier;


public class RegionNodeIdentifierTest extends TestCase {
    Region r1;
    Region r2;
    GridNode node1;
    GridNode node2;
    GridNode node3;
    RegionNodeIdentifier id1;
    RegionNodeIdentifier id2;
    RegionNodeIdentifier id3;

    protected void setUp() {
        GridSpatialIndex grid = new GridSpatialIndex();
        r1 = new Region(new double[] { 0, 0 }, new double[] { 1, 1 });
        r2 = new Region(new double[] { -1, -1 }, new double[] { -2, -2 });
        node1 = new GridNode(new RegionNodeIdentifier(r1));
        node2 = new GridNode(new RegionNodeIdentifier(r2));
        node3 = new GridNode(new RegionNodeIdentifier(r1));
        //id1 = new RegionNodeIdentifier(node1);
        //id2 = new RegionNodeIdentifier(node2);
        //id3 = new RegionNodeIdentifier(node3);
        id1 = (RegionNodeIdentifier)node1.getIdentifier();
        id2 = (RegionNodeIdentifier)node2.getIdentifier();
        id3 = (RegionNodeIdentifier)node3.getIdentifier();
    }

    public static Test suite() {
        return new TestSuite(RegionNodeIdentifierTest.class);
    }

    public void testhashCode() {
        assertEquals(id1.hashCode(), id3.hashCode());
    }

    public void testEquals() {
        assertTrue(node1.equals(node1));
        assertFalse(node1.equals(node2));
        assertFalse(id1.equals(id2));
        assertFalse(node1.equals(node3));
        assertTrue(id1.equals(id3));
        assertFalse(node2.equals(node3));
        assertFalse(id2.equals(id3));
        assertEquals(id1, id1);
        assertEquals(id2, id2);
        assertEquals(id3, id3);
        assertEquals(id1, id3);
    }

    public void testHashMap() {
        HashMap<RegionNodeIdentifier, GridNode> map = new HashMap<RegionNodeIdentifier, GridNode>();
        map.put(id1, node1);
        map.put(id2, node2);
        map.put(id3, node3);
        assertEquals(node1.getShape(), ((GridNode) map.get(id1)).getShape());
        assertEquals(node2.getShape(), ((GridNode) map.get(id2)).getShape());
        assertEquals(node3.getShape(), ((GridNode) map.get(id3)).getShape());
        assertTrue(node3.getShape().equals(((GridNode) map.get(id1)).getShape()));
    }
}
