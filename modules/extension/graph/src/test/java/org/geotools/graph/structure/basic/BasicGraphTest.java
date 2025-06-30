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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicGraphTest {

    private List<Node> m_nodes;
    private List<Edge> m_edges;
    private BasicGraph m_graph;

    @Before
    public void setUp() throws Exception {
        BasicNode n1 = new BasicNode();
        BasicNode n2 = new BasicNode();
        BasicNode n3 = new BasicNode();
        BasicNode n4 = new BasicNode();

        BasicEdge e1 = new BasicEdge(n1, n2);
        BasicEdge e2 = new BasicEdge(n2, n3);
        BasicEdge e3 = new BasicEdge(n3, n4);

        n1.add(e1);
        n2.add(e1);
        n2.add(e2);
        n3.add(e2);
        n3.add(e3);
        n4.add(e3);

        m_nodes = new ArrayList<>();
        m_nodes.add(n1);
        m_nodes.add(n2);
        m_nodes.add(n3);
        m_nodes.add(n4);

        m_edges = new ArrayList<>();
        m_edges.add(e1);
        m_edges.add(e2);
        m_edges.add(e3);

        m_graph = new BasicGraph(m_nodes, m_edges);
    }

    @Test
    public void test_getNodes() {
        Assert.assertSame(m_graph.getNodes(), m_nodes);
    }

    @Test
    public void test_getEdges() {
        Assert.assertSame(m_graph.getEdges(), m_edges);
    }

    @Test
    public void test_queryNodes() {
        GraphVisitor visitor = component -> {
            if (component == m_nodes.get(1) || component == m_nodes.get(2)) return BasicGraph.PASS_AND_CONTINUE;
            return BasicGraph.FAIL_QUERY;
        };
        List result = m_graph.queryNodes(visitor);

        Assert.assertEquals(2, result.size());
        Assert.assertSame(result.get(0), m_nodes.get(1));
        Assert.assertSame(result.get(1), m_nodes.get(2));
    }

    @Test
    public void test_queryEdges() {
        GraphVisitor visitor = component -> {
            if (component == m_edges.get(1) || component == m_edges.get(2)) return BasicGraph.PASS_AND_CONTINUE;
            return BasicGraph.FAIL_QUERY;
        };
        List result = m_graph.queryEdges(visitor);

        Assert.assertEquals(2, result.size());
        Assert.assertSame(result.get(0), m_edges.get(1));
        Assert.assertSame(result.get(1), m_edges.get(2));
    }

    @Test
    public void test_visitNodes() {
        final Set<Graphable> visited = new HashSet<>();
        GraphVisitor visitor = component -> {
            visited.add(component);
            return 0;
        };

        m_graph.visitNodes(visitor);

        for (Node m_node : m_nodes) {
            Assert.assertTrue(visited.contains(m_node));
        }
    }

    @Test
    public void test_visitEdges() {
        final Set<Graphable> visited = new HashSet<>();
        GraphVisitor visitor = component -> {
            visited.add(component);
            return 0;
        };

        m_graph.visitEdges(visitor);

        for (Edge m_edge : m_edges) {
            Assert.assertTrue(visited.contains(m_edge));
        }
    }

    @Test
    public void test_getNodesOfDegree() {
        List nodes = m_graph.getNodesOfDegree(1);
        Assert.assertTrue(nodes.contains(m_nodes.get(0)));
        Assert.assertTrue(nodes.contains(m_nodes.get(m_nodes.size() - 1)));

        nodes = m_graph.getNodesOfDegree(2);
        Assert.assertTrue(nodes.contains(m_nodes.get(1)));
        Assert.assertTrue(nodes.contains(m_nodes.get(2)));
    }

    @Test
    public void test_getVisitedNodes() {
        m_nodes.get(1).setVisited(true);
        m_nodes.get(2).setVisited(true);

        List visited = m_graph.getVisitedNodes(true);
        Assert.assertEquals(2, visited.size());
        Assert.assertTrue(visited.contains(m_nodes.get(1)));
        Assert.assertTrue(visited.contains(m_nodes.get(2)));

        visited = m_graph.getVisitedNodes(false);
        Assert.assertEquals(2, visited.size());
        Assert.assertTrue(visited.contains(m_nodes.get(0)));
        Assert.assertTrue(visited.contains(m_nodes.get(3)));
    }

    @Test
    public void test_getVisitedEdges() {
        m_edges.get(1).setVisited(true);

        List visited = m_graph.getVisitedEdges(true);
        Assert.assertEquals(1, visited.size());
        Assert.assertTrue(visited.contains(m_edges.get(1)));

        visited = m_graph.getVisitedEdges(false);
        Assert.assertEquals(2, visited.size());
        Assert.assertTrue(visited.contains(m_edges.get(0)));
        Assert.assertTrue(visited.contains(m_edges.get(2)));
    }

    @Test
    public void test_initNodes() {
        for (Node mNode : m_nodes) {
            BasicNode n = (BasicNode) mNode;
            n.setVisited(true);
            n.setCount(100);
        }

        m_graph.initNodes();

        for (Node m_node : m_nodes) {
            BasicNode n = (BasicNode) m_node;
            Assert.assertFalse(n.isVisited());
            Assert.assertEquals(0, n.getCount());
        }
    }

    @Test
    public void test_initEdges() {
        for (Edge mEdge : m_edges) {
            BasicEdge e = (BasicEdge) mEdge;
            e.setVisited(true);
            e.setCount(100);
        }

        m_graph.initEdges();

        for (Edge m_edge : m_edges) {
            BasicEdge e = (BasicEdge) m_edge;
            Assert.assertFalse(e.isVisited());
            Assert.assertEquals(0, e.getCount());
        }
    }
}
