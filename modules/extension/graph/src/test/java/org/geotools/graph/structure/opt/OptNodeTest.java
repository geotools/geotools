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
package org.geotools.graph.structure.opt;

import java.util.Iterator;
import org.geotools.graph.structure.Edge;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptNodeTest {
    private OptNode m_node;
    private OptNode m_otherNode;
    private Edge m_edge;
    private Edge m_otherEdge;

    @Before
    public void setUp() throws Exception {
        m_node = new OptNode();
        m_otherNode = new OptNode();

        m_edge = new OptEdge(m_node, m_otherNode);
        m_otherEdge = new OptEdge(m_node, m_otherNode);
    }

    @Test
    public void test_add() {
        // test addition of single edge
        m_node.setDegree(1);
        m_node.add(m_edge);
        Assert.assertTrue(m_node.getEdges().contains(m_edge));

        // test addition of multiple edges, same edge
        m_node.setDegree(2);
        m_node.add(m_edge);
        m_node.add(m_edge);
        Assert.assertEquals(2, m_node.getEdges().size());

        // test addition of multiple edges, different
        m_node.setDegree(2);

        Edge other = new OptEdge(m_node, m_otherNode);
        m_node.add(m_edge);
        m_node.add(other);
        Assert.assertEquals(2, m_node.getEdges().size());
        Assert.assertTrue(m_node.getEdges().contains(m_edge));
        Assert.assertTrue(m_node.getEdges().contains(other));
    }

    @Test
    public void test_remove() {
        m_node.setDegree(1);
        m_node.add(m_edge);

        try {
            m_node.remove(m_edge);
            Assert.fail();
        } catch (UnsupportedOperationException uoe) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void test_getDegree() {
        // intially degree should be zero
        Assert.assertEquals(0, m_node.getDegree());

        // add single edge making degree 1
        m_node.setDegree(1);
        m_node.add(m_edge);
        Assert.assertEquals(1, m_node.getDegree());

        // add the same edge, should be degree 2
        m_node.setDegree(2);
        m_node.add(m_edge);
        m_node.add(m_edge);
        Assert.assertEquals(2, m_node.getDegree());

        // add different edge, should be degree 3
        m_node.setDegree(3);
        m_node.add(m_edge);
        m_node.add(m_edge);
        m_node.add(m_otherEdge);
        Assert.assertEquals(3, m_node.getDegree());
    }

    @Test
    public void test_getEdge() {
        m_node.setDegree(2);

        m_node.add(m_edge);
        Assert.assertSame(m_edge, m_node.getEdge(m_otherNode));

        // add another edge that has the same other node, since the underlying
        // structure is an array, first one should be returned
        m_node.add(m_otherEdge);
        Assert.assertSame(m_edge, m_node.getEdge(m_otherNode));
    }

    @Test
    public void test_getEdges() {
        m_node.setDegree(1);
        m_node.add(m_edge);
        Assert.assertTrue(m_node.getEdges(m_otherNode).contains(m_edge));

        // add the same edge
        m_node.setDegree(2);
        m_node.add(m_edge);
        m_node.add(m_edge);
        Assert.assertEquals(2, m_node.getEdges(m_otherNode).size());

        // add a different edge
        m_node.setDegree(3);
        m_node.add(m_edge);
        m_node.add(m_edge);
        m_node.add(m_otherEdge);

        Assert.assertEquals(3, m_node.getEdges(m_otherNode).size());
        Assert.assertTrue(m_node.getEdges(m_otherNode).contains(m_edge));
        Assert.assertTrue(m_node.getEdges(m_otherNode).contains(m_otherEdge));
    }

    @Test
    public void test_getRelated() {
        // no edges should be empty
        Assert.assertFalse(m_node.getRelated().hasNext());

        // single edge
        m_node.setDegree(1);
        m_node.add(m_edge);

        Iterator itr = m_node.getRelated();
        Assert.assertSame(itr.next(), m_otherNode);
        Assert.assertFalse(itr.hasNext());

        // multiple edges, same, same node should be returned twice
        m_node.setDegree(2);
        m_node.add(m_edge);
        m_node.add(m_edge);

        itr = m_node.getRelated();
        Assert.assertSame(itr.next(), m_otherNode);
        Assert.assertSame(itr.next(), m_otherNode);
        Assert.assertFalse(itr.hasNext());
    }
}
