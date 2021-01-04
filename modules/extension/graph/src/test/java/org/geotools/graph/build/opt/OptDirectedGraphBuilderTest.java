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
package org.geotools.graph.build.opt;

import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedGraph;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.opt.OptDirectedEdge;
import org.geotools.graph.structure.opt.OptDirectedNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptDirectedGraphBuilderTest {

    private OptDirectedGraphBuilder m_builder;

    @Before
    public void setUp() throws Exception {

        m_builder = new OptDirectedGraphBuilder();
    }

    @Test
    public void test_buildNode() {
        DirectedNode dn = (DirectedNode) m_builder.buildNode();

        Assert.assertNotNull(dn);
        Assert.assertTrue(dn instanceof OptDirectedNode);
    }

    @Test
    public void test_buildEdge() {
        Node n1 = m_builder.buildNode();
        Node n2 = m_builder.buildNode();

        DirectedEdge de = (DirectedEdge) m_builder.buildEdge(n1, n2);

        Assert.assertNotNull(de);
        Assert.assertTrue(de instanceof OptDirectedEdge);
        Assert.assertSame(de.getInNode(), n1);
        Assert.assertSame(de.getOutNode(), n2);
    }

    @Test
    public void test_addEdge() {
        OptDirectedNode n1 = (OptDirectedNode) m_builder.buildNode();
        n1.setInDegree(0);
        n1.setOutDegree(1);

        OptDirectedNode n2 = (OptDirectedNode) m_builder.buildNode();
        n2.setInDegree(1);
        n2.setOutDegree(0);

        DirectedEdge e = (DirectedEdge) m_builder.buildEdge(n1, n2);

        m_builder.addEdge(e);

        Assert.assertTrue(m_builder.getEdges().contains(e));
        Assert.assertTrue(n1.getOutEdges().contains(e));
        Assert.assertTrue(n2.getInEdges().contains(e));
    }

    @Test
    public void test_getGraph() {
        Graph graph = m_builder.getGraph();
        Assert.assertTrue(graph instanceof DirectedGraph);
    }
}
