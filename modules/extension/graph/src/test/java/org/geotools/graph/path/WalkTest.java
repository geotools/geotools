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
package org.geotools.graph.path;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.GraphWalker;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.standard.NoBifurcationIterator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WalkTest {

    private GraphBuilder m_builder;

    @Before
    public void setUp() throws Exception {
        m_builder = createBuilder();
    }

    @Test
    public void test_add() {
        Node n = builder().buildNode();
        Walk walk = new Walk();

        walk.add(n);

        Assert.assertEquals(1, walk.size());
        Assert.assertEquals(walk.get(0), n);
    }

    @Test
    public void test_remove() {
        Node n = builder().buildNode();
        Walk walk = new Walk();

        walk.add(n);
        Assert.assertFalse(walk.isEmpty());

        walk.remove(n);
        Assert.assertTrue(walk.isEmpty());
    }

    @Test
    public void test_reverse() {
        List<Node> nodes = new ArrayList<>();
        Walk walk = new Walk();

        for (int i = 0; i < 10; i++) {
            Node n = builder().buildNode();
            nodes.add(n);
            walk.add(n);
        }

        Iterator itr = walk.iterator();
        for (Node n1 : nodes) {
            Node n2 = (Node) itr.next();

            Assert.assertSame(n1, n2);
        }

        walk.reverse();
        itr = walk.iterator();

        for (int i = nodes.size() - 1; i >= 0; i--) {
            Node n1 = nodes.get(i);
            Node n2 = (Node) itr.next();

            Assert.assertSame(n1, n2);
        }
    }

    @Test
    public void test_isClosed() {
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 10);
        final Walk walk = new Walk();

        NoBifurcationIterator iterator = new NoBifurcationIterator();
        iterator.setSource(ends[0]);

        GraphWalker walker = new GraphWalker() {
            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                walk.add((Node) element);
                return GraphTraversal.CONTINUE;
            }

            @Override
            public void finish() {}
        };

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        Assert.assertEquals(walk.size(), builder().getGraph().getNodes().size());
        Assert.assertTrue(walk.isValid() && !walk.isClosed());

        // create a new edges in the graph making the graph a cycle
        Edge e = builder().buildEdge(ends[0], ends[1]);
        builder().addEdge(e);

        walk.add(ends[0]);
        Assert.assertTrue(walk.isValid() && walk.isClosed());
    }

    @Test
    public void test_getEdges() {
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 10);
        final Walk walk = new Walk();

        NoBifurcationIterator iterator = new NoBifurcationIterator();
        iterator.setSource(ends[0]);

        GraphWalker walker = new GraphWalker() {
            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                walk.add((Node) element);
                return GraphTraversal.CONTINUE;
            }

            @Override
            public void finish() {}
        };

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        Assert.assertNotNull(walk.getEdges());
        Assert.assertTrue(walk.isValid());
    }

    @Test
    public void test_truncate_0() {
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 10);
        final Walk walk = new Walk();

        NoBifurcationIterator iterator = new NoBifurcationIterator();
        iterator.setSource(ends[0]);

        GraphWalker walker = new GraphWalker() {
            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                walk.add((Node) element);
                return GraphTraversal.CONTINUE;
            }

            @Override
            public void finish() {}
        };

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        walk.truncate(0);
        Assert.assertTrue(walk.isEmpty());
        Assert.assertTrue(walk.isValid());
    }

    @Test
    public void test_truncate_1() {
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 10);
        final Walk walk = new Walk();

        NoBifurcationIterator iterator = new NoBifurcationIterator();
        iterator.setSource(ends[0]);

        GraphWalker walker = new GraphWalker() {
            int count = 0;

            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                walk.add((Node) element);
                return GraphTraversal.CONTINUE;
            }

            @Override
            public void finish() {}
        };

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        int size = walk.size();
        walk.truncate(size / 2);
        Assert.assertEquals(walk.size(), size / 2);
    }

    @Test
    public void test_truncate_2() {
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 11);
        final Walk walk = new Walk();

        NoBifurcationIterator iterator = new NoBifurcationIterator();
        iterator.setSource(ends[0]);

        GraphWalker walker = new GraphWalker() {
            int count = 0;

            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                walk.add((Node) element);
                return GraphTraversal.CONTINUE;
            }

            @Override
            public void finish() {}
        };

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        int size = walk.size();
        walk.truncate(size / 2);
        Assert.assertEquals(walk.size(), size / 2);
    }

    protected GraphBuilder createBuilder() {
        return new BasicGraphBuilder();
    }

    protected GraphBuilder builder() {
        return m_builder;
    }
}
