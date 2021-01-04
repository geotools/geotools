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
package org.geotools.graph.build.basic;

import junit.framework.TestCase;
import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedGraph;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;

public class BasicDirectedGraphBuilderTest extends TestCase {

    private BasicDirectedGraphBuilder m_builder;

    public BasicDirectedGraphBuilderTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();

        m_builder = new BasicDirectedGraphBuilder();
    }

    public void test_buildNode() {
        DirectedNode dn = (DirectedNode) m_builder.buildNode();
        assertNotNull(dn);
    }

    public void test_buildEdge() {
        Node n1 = m_builder.buildNode();
        Node n2 = m_builder.buildNode();

        DirectedEdge de = (DirectedEdge) m_builder.buildEdge(n1, n2);

        assertNotNull(de);
        assertSame(de.getInNode(), n1);
        assertSame(de.getOutNode(), n2);
    }

    public void test_addEdge_0() {
        DirectedNode n1 = (DirectedNode) m_builder.buildNode();
        DirectedNode n2 = (DirectedNode) m_builder.buildNode();

        DirectedEdge e = (DirectedEdge) m_builder.buildEdge(n1, n2);

        m_builder.addEdge(e);

        assertTrue(m_builder.getEdges().contains(e));
        assertTrue(n1.getOutEdges().contains(e));
        assertTrue(n2.getInEdges().contains(e));
    }

    public void test_addEdge_1() {
        // add a loop edge, in degree == 1, out degree = 1, degree == 2
        DirectedNode n1 = (DirectedNode) m_builder.buildNode();
        DirectedEdge e = (DirectedEdge) m_builder.buildEdge(n1, n1);

        m_builder.addNode(n1);
        m_builder.addEdge(e);

        assertEquals(1, n1.getInEdges().size());
        assertEquals(1, n1.getOutEdges().size());
        assertEquals(2, n1.getEdges().size());

        assertEquals(1, n1.getInDegree());
        assertEquals(1, n1.getOutDegree());
        assertEquals(2, n1.getDegree());
    }

    public void test_getGraph() {
        Graph graph = m_builder.getGraph();
        assertTrue(graph instanceof DirectedGraph);
    }
}
