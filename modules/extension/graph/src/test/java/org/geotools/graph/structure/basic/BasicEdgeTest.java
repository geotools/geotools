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
import org.geotools.graph.structure.Edge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicEdgeTest {

    private BasicNode m_nodeA;
    private BasicNode m_nodeB;
    private BasicNode m_otherNode1;
    private BasicNode m_otherNode2;
    private BasicNode m_otherNode3;
    private BasicNode m_otherNode4;

    private BasicEdge m_edge;
    private BasicEdge m_other1;
    private BasicEdge m_other2;
    private BasicEdge m_other3;
    private BasicEdge m_other4;

    private BasicEdge m_same;
    private BasicEdge m_opp;
    private BasicEdge m_loopA;
    private BasicEdge m_loopB;

    @Before
    public void setUp() throws Exception {
        m_nodeA = new BasicNode();
        m_nodeB = new BasicNode();
        m_otherNode1 = new BasicNode();
        m_otherNode2 = new BasicNode();
        m_otherNode3 = new BasicNode();
        m_otherNode4 = new BasicNode();

        m_edge = new BasicEdge(m_nodeA, m_nodeB);
        m_nodeA.add(m_edge);
        m_nodeB.add(m_edge);

        m_other1 = new BasicEdge(m_nodeA, m_otherNode1);
        m_nodeA.add(m_other1);
        m_otherNode1.add(m_other1);

        m_other2 = new BasicEdge(m_nodeB, m_otherNode2);
        m_nodeB.add(m_other2);
        m_otherNode2.add(m_other2);

        m_other3 = new BasicEdge(m_otherNode3, m_nodeA);
        m_otherNode3.add(m_other3);
        m_nodeA.add(m_other3);

        m_other4 = new BasicEdge(m_otherNode4, m_nodeB);
        m_otherNode4.add(m_other4);
        m_nodeB.add(m_other4);

        // dont add these to the graph yet
        m_same = new BasicEdge(m_nodeA, m_nodeB);
        m_opp = new BasicEdge(m_nodeB, m_nodeA);
        m_loopA = new BasicEdge(m_nodeA, m_nodeA);
        m_loopB = new BasicEdge(m_nodeB, m_nodeB);
    }

    @Test
    public void test_getNodeA() {
        Assert.assertSame(m_edge.getNodeA(), m_nodeA);
    }

    @Test
    public void test_getNodeB() {
        Assert.assertSame(m_edge.getNodeB(), m_nodeB);
    }

    @Test
    public void test_getOtherNode() {
        Assert.assertSame(m_edge.getOtherNode(m_nodeA), m_nodeB);
        Assert.assertSame(m_edge.getOtherNode(m_nodeB), m_nodeA);
        Assert.assertSame(m_edge.getOtherNode(new BasicNode()), null);
    }

    @Test
    public void test_reverse() {
        Assert.assertSame(m_nodeA, m_edge.getNodeA());
        Assert.assertSame(m_nodeB, m_edge.getNodeB());

        m_edge.reverse();

        Assert.assertSame(m_nodeA, m_edge.getNodeB());
        Assert.assertSame(m_nodeB, m_edge.getNodeA());
    }

    @Test
    public void test_getRelated() {

        // nodes share single edge
        Iterator<Edge> itr = m_edge.getRelated();
        Assert.assertTrue(itr.hasNext());

        BasicEdge be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1) || be.equals(m_other2) || be.equals(m_other3) || be.equals(m_other4));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1) || be.equals(m_other2) || be.equals(m_other3) || be.equals(m_other4));
        Assert.assertTrue(itr.hasNext());
        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1) || be.equals(m_other2) || be.equals(m_other3) || be.equals(m_other4));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1) || be.equals(m_other2) || be.equals(m_other3) || be.equals(m_other4));
        Assert.assertFalse(itr.hasNext());

        // nodes share multiple edges (same direction)
        m_nodeA.add(m_same);
        m_nodeB.add(m_same);

        itr = m_edge.getRelated();
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_same));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_same));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_same));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_same));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_same));
        Assert.assertFalse(itr.hasNext());
        m_nodeA.remove(m_same);
        m_nodeB.remove(m_same);

        // nodes share multiple edges (different direction)
        m_nodeB.add(m_opp);
        m_nodeA.add(m_opp);

        itr = m_edge.getRelated();
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_opp));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_opp));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_opp));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_opp));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_opp));
        Assert.assertFalse(itr.hasNext());

        m_nodeA.remove(m_opp);
        m_nodeB.remove(m_opp);

        // loop on one of nodes
        m_nodeA.add(m_loopA);

        itr = m_edge.getRelated();
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopA));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopA));

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopA));

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopA));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopA));
        Assert.assertFalse(itr.hasNext());

        m_nodeA.remove(m_loopA);

        // test loop on other node
        m_nodeB.add(m_loopB);

        itr = m_edge.getRelated();
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));
        Assert.assertFalse(itr.hasNext());

        // test loop on both nodes
        m_nodeA.add(m_loopA);
        itr = m_edge.getRelated();
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_loopA)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_loopA)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_loopA)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_loopA)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_loopA)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));
        Assert.assertTrue(itr.hasNext());

        be = (BasicEdge) itr.next();
        Assert.assertTrue(be.equals(m_other1)
                || be.equals(m_other2)
                || be.equals(m_loopA)
                || be.equals(m_other3)
                || be.equals(m_other4)
                || be.equals(m_loopB));
        Assert.assertFalse(itr.hasNext());
    }

    @Test
    public void test_compareTo() {
        BasicEdge same = new BasicEdge(m_nodeA, m_nodeB);
        BasicEdge opp = new BasicEdge(m_nodeB, m_nodeA);

        Assert.assertEquals(m_edge.compareNodes(same), Edge.EQUAL_NODE_ORIENTATION);
        Assert.assertEquals(m_edge.compareNodes(opp), Edge.OPPOSITE_NODE_ORIENTATION);
        Assert.assertEquals(m_edge.compareNodes(m_other1), Edge.UNEQUAL_NODE_ORIENTATION);
    }
}
