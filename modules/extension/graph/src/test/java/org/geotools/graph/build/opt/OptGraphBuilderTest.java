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

import java.util.ArrayList;
import java.util.List;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.structure.opt.OptEdge;
import org.geotools.graph.structure.opt.OptNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptGraphBuilderTest {

    private OptGraphBuilder m_builder;

    @Before
    public void setUp() throws Exception {

        m_builder = createBuilder();
    }

    @Test
    public void test_buildNode() {
        Assert.assertTrue(m_builder.getNodes().isEmpty());

        Node n = m_builder.buildNode();

        Assert.assertNotNull(n);
        Assert.assertTrue(n instanceof OptNode);
    }

    @Test
    public void test_buildEdge() {
        Node n1 = m_builder.buildNode();
        Node n2 = m_builder.buildNode();

        Assert.assertTrue(m_builder.getEdges().isEmpty());

        Edge e = m_builder.buildEdge(n1, n2);

        Assert.assertNotNull(e);
        Assert.assertTrue(e instanceof OptEdge);
        Assert.assertTrue(e.getNodeA() == n1 || e.getNodeA() == n2);
        Assert.assertTrue(e.getNodeB() == n1 || e.getNodeB() == n2);
    }

    @Test
    public void test_addNode() {
        Node n1 = m_builder.buildNode();
        m_builder.addNode(n1);

        Assert.assertEquals(1, m_builder.getNodes().size());
        Assert.assertTrue(m_builder.getNodes().contains(n1));
    }

    @Test
    public void test_addEdge() {
        OptNode n1 = (OptNode) m_builder.buildNode();
        n1.setDegree(1);

        OptNode n2 = (OptNode) m_builder.buildNode();
        n2.setDegree(1);

        Edge e = m_builder.buildEdge(n1, n2);

        m_builder.addNode(n1);
        m_builder.addNode(n2);

        m_builder.addEdge(e);

        Assert.assertEquals(1, m_builder.getEdges().size());
        Assert.assertTrue(m_builder.getEdges().contains(e));

        Assert.assertTrue(n1.getEdges().contains(e));
        Assert.assertTrue(n2.getEdges().contains(e));
    }

    @Test
    public void test_removeNode() {
        OptNode n1 = (OptNode) m_builder.buildNode();
        n1.setDegree(0);

        m_builder.addNode(n1);

        Assert.assertTrue(m_builder.getNodes().contains(n1));

        m_builder.removeNode(n1);
        Assert.assertTrue(m_builder.getNodes().isEmpty());
    }

    @Test
    public void test_removeNodes() {
        OptNode n1 = (OptNode) m_builder.buildNode();
        n1.setDegree(0);

        OptNode n2 = (OptNode) m_builder.buildNode();
        n2.setDegree(0);

        m_builder.addNode(n1);
        m_builder.addNode(n2);

        Assert.assertEquals(2, m_builder.getNodes().size());
        Assert.assertTrue(m_builder.getNodes().contains(n1));
        Assert.assertTrue(m_builder.getNodes().contains(n2));

        List<Node> toRemove = new ArrayList<>();
        toRemove.add(n1);
        toRemove.add(n2);

        m_builder.removeNodes(toRemove);
        Assert.assertTrue(m_builder.getNodes().isEmpty());
    }

    @Test
    public void test_removeEdge() {
        OptNode n1 = (OptNode) m_builder.buildNode();
        n1.setDegree(1);

        OptNode n2 = (OptNode) m_builder.buildNode();
        n2.setDegree(1);

        Edge e = m_builder.buildEdge(n1, n2);

        m_builder.addNode(n1);
        m_builder.addNode(n2);
        m_builder.addEdge(e);

        Assert.assertTrue(n1.getEdges().contains(e));
        Assert.assertTrue(n2.getEdges().contains(e));

        try {
            m_builder.removeEdge(e);
            Assert.fail();
        } catch (UnsupportedOperationException uoe) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void test_removeEdges() {
        OptNode n1 = (OptNode) m_builder.buildNode();
        n1.setDegree(1);
        OptNode n2 = (OptNode) m_builder.buildNode();
        n2.setDegree(1);
        OptNode n3 = (OptNode) m_builder.buildNode();
        n3.setDegree(1);
        OptNode n4 = (OptNode) m_builder.buildNode();
        n4.setDegree(1);

        Edge e1 = m_builder.buildEdge(n1, n2);
        Edge e2 = m_builder.buildEdge(n3, n4);

        m_builder.addNode(n1);
        m_builder.addNode(n2);
        m_builder.addNode(n3);
        m_builder.addNode(n4);

        m_builder.addEdge(e1);
        m_builder.addEdge(e2);

        List<Edge> toRemove = new ArrayList<>();
        toRemove.add(e1);
        toRemove.add(e2);

        Assert.assertTrue(m_builder.getEdges().contains(e1));
        Assert.assertTrue(m_builder.getEdges().contains(e2));

        try {
            m_builder.removeEdges(toRemove);
            Assert.fail();
        } catch (UnsupportedOperationException uoe) {
            Assert.assertTrue(true);
        }
    }

    @Test
    public void test_getGraph() {
        Graph graph = m_builder.getGraph();
        Assert.assertNotNull(graph);
    }

    private OptGraphBuilder createBuilder() {
        return new OptGraphBuilder();
    }
}
