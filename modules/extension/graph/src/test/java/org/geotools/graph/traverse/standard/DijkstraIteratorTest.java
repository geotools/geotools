/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.graph.traverse.standard;

import java.util.Iterator;
import java.util.Map;
import java.util.StringTokenizer;
import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicDirectedGraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.basic.CountingWalker;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DijkstraIteratorTest {

    public GraphBuilder m_builder;
    public GraphBuilder m_directed_builder;

    @Before
    public void setUp() throws Exception {

        m_builder = createBuilder();
        m_directed_builder = createDirectedBuilder();
    }

    /**
     * Create a graph with no bifurcations and start a full traversal from start node. <br>
     * <br>
     * Expected: 1. Every node should be visited in order. 2. Every node should have a cost associated with == id 3.
     * Every node should havea parent with id node id + 1
     */
    @Test
    public void test_0() {
        int nnodes = 100;
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);

        CountingWalker walker = new CountingWalker();

        final DijkstraIterator iterator = createIterator();
        iterator.setSource(ends[0]);

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            Assert.assertTrue(component.isVisited());
            Assert.assertEquals(iterator.getCost(component), (double) component.getID(), 0d);
            if (component.getID() == 0) Assert.assertNull(iterator.getParent(component));
            else Assert.assertEquals(iterator.getParent(component).getID(), component.getID() - 1);

            return 0;
        };
        builder().getGraph().visitNodes(visitor);

        Assert.assertEquals(walker.getCount(), nnodes);
    }

    /**
     * Create a graph with no bifurcations and start a traversal from start node, then suspend, and resume. <br>
     * <br>
     * Expected: After supsend: 1. Nodes from 0 to suspend node should be visted, others not.
     *
     * <p>After resume: 1. Next node visited should have id suspend node id + 1 2. Every node should have a cost
     * associated with it == id 3. Every node should have a parent with id node id + 1
     */
    @Test
    public void test_1() {
        int nnodes = 100;
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);
        final int suspend = 50;

        CountingWalker walker = new CountingWalker() {
            int m_mode = 0;

            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                super.visit(element, traversal);
                if (m_mode == 0) {
                    if (element.getID() == suspend) {
                        m_mode++;
                        return GraphTraversal.SUSPEND;
                    }
                } else if (m_mode == 1) {
                    Assert.assertEquals(element.getID(), suspend + 1);
                    m_mode++;
                }
                return GraphTraversal.CONTINUE;
            }
        };

        final DijkstraIterator iterator = createIterator();
        iterator.setSource(ends[0]);

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            if (component.getID() <= suspend) Assert.assertTrue(component.isVisited());
            else Assert.assertFalse(component.isVisited());
            return 0;
        };
        builder().getGraph().visitNodes(visitor);
        Assert.assertEquals(walker.getCount(), nnodes - suspend + 1);

        // resume
        traversal.traverse();

        visitor = component -> {
            Assert.assertTrue(component.isVisited());
            Assert.assertEquals(iterator.getCost(component), (double) component.getID(), 0d);
            if (component.getID() == 0) Assert.assertNull(iterator.getParent(component));
            else Assert.assertEquals(iterator.getParent(component).getID(), component.getID() - 1);

            return 0;
        };
        builder().getGraph().visitNodes(visitor);
        Assert.assertEquals(walker.getCount(), nnodes);
    }

    /**
     * Create a graph with no bifurcations and start a traversal from start node, then kill branch, and resume. <br>
     * <br>
     * Expected: After kill branch: 1. Traversal ends After resume: 2. No more nodes visited.
     */
    @Test
    public void test_2() {
        int nnodes = 100;
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);
        final int kill = 50;

        CountingWalker walker = new CountingWalker() {
            int m_mode = 0;

            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                super.visit(element, traversal);
                if (m_mode == 0) {
                    if (element.getID() == kill) {
                        m_mode++;
                        return GraphTraversal.KILL_BRANCH;
                    }
                }

                return GraphTraversal.CONTINUE;
            }
        };

        final DijkstraIterator iterator = createIterator();
        iterator.setSource(ends[0]);

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            if (component.getID() <= kill) Assert.assertTrue(component.isVisited());
            else Assert.assertFalse(component.isVisited());

            return 0;
        };
        builder().getGraph().visitNodes(visitor);
        Assert.assertEquals(walker.getCount(), nnodes - kill + 1);

        // resume
        traversal.traverse();
        builder().getGraph().visitNodes(visitor);
        Assert.assertEquals(walker.getCount(), nnodes - kill + 1);
    }

    /**
     * Create a balanced binary tree and do a normal traversal starting at root. <br>
     * <br>
     * Expected: 1. Every node should be visited. 2. The dijsktra parent of each node should be the same as the parent
     * of the tree. 3. The cost of each node should be equal to its depth.
     */
    @Test
    public void test_3() {
        int k = 4;
        Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
        final Node root = (Node) obj[0];

        CountingWalker walker = new CountingWalker();
        final DijkstraIterator iterator = createIterator();
        iterator.setSource((Node) obj[0]);

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            Assert.assertTrue(component.isVisited());
            String id = component.getObject().toString();
            StringTokenizer st = new StringTokenizer(id, ".");

            Assert.assertEquals(iterator.getCost(component), (double) st.countTokens() - 1, 0d);
            if (component != root) {
                String parentid = id.substring(0, id.length() - 2);
                Assert.assertEquals(iterator.getParent(component).getObject().toString(), parentid);
            }

            return 0;
        };
        builder().getGraph().visitNodes(visitor);
        Assert.assertEquals(walker.getCount(), (int) Math.pow(2, k + 1) - 1);
    }

    /**
     * Create a balanced binary tree and do a normal traversal starting at root and then suspend at right right child of
     * root, then resume <br>
     * <br>
     * Expected: After suspend: 1. Not every node should be visited After resume: 1. Every node should be visited.
     */
    @Test
    public void test_4() {
        int k = 4;
        Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
        Map id2node = (Map) obj[1];

        final Node root = (Node) obj[0];
        final Node lc = (Node) id2node.get(root.getObject().toString() + ".0");
        final Node rc = (Node) id2node.get(root.getObject().toString() + ".1");

        CountingWalker walker = new CountingWalker() {
            private int m_mode = 0;

            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                super.visit(element, traversal);
                if (m_mode == 0) {
                    if (element == rc) {
                        m_mode++;
                        return GraphTraversal.SUSPEND;
                    }
                }
                return GraphTraversal.CONTINUE;
            }
        };

        final DijkstraIterator iterator = createIterator();
        iterator.setSource((Node) obj[0]);

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            if (component != root && component != lc && component != rc) Assert.assertFalse(component.isVisited());

            return 0;
        };
        builder().getGraph().visitNodes(visitor);

        // resume
        traversal.traverse();

        visitor = component -> {
            Assert.assertTrue(component.isVisited());
            return 0;
        };
        builder().getGraph().visitNodes(visitor);
        Assert.assertEquals(walker.getCount(), (int) Math.pow(2, k + 1) - 1);
    }

    /**
     * Create a balanced binary tree and do a normal traversal starting at root and then kill at right right child of
     * root, then resume <br>
     * <br>
     * Expected: 1. Every node in left subtree should be visited 2. Every node in right substree (minus right child of
     * root) should not be visited
     */
    @Test
    public void test_5() {
        int k = 4;
        Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
        Map id2node = (Map) obj[1];

        final Node root = (Node) obj[0];
        final Node lc = (Node) id2node.get(root.getObject().toString() + ".0");
        final Node rc = (Node) id2node.get(root.getObject().toString() + ".1");

        CountingWalker walker = new CountingWalker() {
            private int m_mode = 0;

            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                super.visit(element, traversal);
                if (m_mode == 0) {
                    if (element == rc) {
                        m_mode++;
                        return GraphTraversal.KILL_BRANCH;
                    }
                }
                return GraphTraversal.CONTINUE;
            }
        };

        final DijkstraIterator iterator = createIterator();
        iterator.setSource((Node) obj[0]);

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            if (component == root || component == lc || component == rc) Assert.assertTrue(component.isVisited());
            else {
                String id = component.getObject().toString();
                if (id.startsWith("0.1.")) Assert.assertFalse(component.isVisited());
                else Assert.assertTrue(component.isVisited());
            }

            return 0;
        };
        builder().getGraph().visitNodes(visitor);

        Assert.assertEquals(walker.getCount(), (int) Math.pow(2, k) + 1);
    }

    /**
     * Create a circular graph and perform a full traversal. <br>
     * <br>
     * Expected : 1. For nodes with id < (total # of nodes) / 2: a. parent should be node with id - 1 b. cost == id 2.
     * For nodes with id > (total # of nodes) / 2; a. parent should be node with id + 1 b. cost == total # of nodes - id
     */
    @Test
    public void test_6() {
        int nnodes = 100;
        Node[] ends = GraphTestUtil.buildCircular(builder(), nnodes);

        CountingWalker walker = new CountingWalker();

        final DijkstraIterator iterator = createIterator();
        iterator.setSource(ends[0]);

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            Graphable parent = iterator.getParent(component);
            if (component.getID() < 50 && component.getID() > 0) {
                Assert.assertEquals(iterator.getCost(component), (double) component.getID(), 0d);
                Assert.assertEquals(parent.getID(), component.getID() - 1, 0d);
            } else if (component.getID() > 50 && component.getID() < 99) {
                Assert.assertEquals(iterator.getCost(component), (double) 100 - component.getID(), 0d);
                Assert.assertEquals(parent.getID(), component.getID() + 1, 0d);
            } else if (component.getID() == 0) {
                Assert.assertNull(parent);
                Assert.assertEquals(0d, iterator.getCost(component), 0d);
            } else if (component.getID() == 99) {
                Assert.assertEquals(0, parent.getID());
                Assert.assertEquals(1d, iterator.getCost(component), 0d);
            }

            return 0;
        };

        builder().getGraph().visitNodes(visitor);
        Assert.assertEquals(walker.getCount(), nnodes);
    }

    protected DijkstraIterator createIterator() {
        return new DijkstraIterator(e -> 1);
    }

    /**
     * Create an undirected graph with no bifurcations and start a full traversal from start node. Test getRelated at
     * each node.<br>
     * <br>
     * Expected: 1. The first and last node should have one related node. 2. All other nodes should have two related
     * nodes 3. Related nodes should have an id one greater or one less than the current node
     */
    @Test
    public void test_7() {
        final int nnodes = 100;
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);

        CountingWalker walker = new CountingWalker();

        final DijkstraIterator iterator = createIterator();
        iterator.setSource(ends[0]);

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            Iterator related = iterator.getRelated(component);
            int count = 0;
            int expectedCount = 2;
            if (component.getID() == 0 || component.getID() == nnodes - 1) {
                expectedCount = 1;
            }
            while (related.hasNext()) {
                Graphable relatedComponent = (Graphable) related.next();
                Assert.assertTrue(component.getID() == relatedComponent.getID() - 1
                        || component.getID() == relatedComponent.getID() + 1);
                count++;
            }
            Assert.assertEquals(expectedCount, count);

            return 0;
        };
        builder().getGraph().visitNodes(visitor);

        Assert.assertEquals(walker.getCount(), nnodes);
    }

    /**
     * Create a directed graph with no bifurcations and start a full traversal from start node. Test getRelated at each
     * node.<br>
     * <br>
     * Expected: 1. The last node should have no related nodes 2. All other nodes should have one related node 3.
     * Related nodes should have an id one greater than the current node
     */
    @Test
    public void test_8() {
        final int nnodes = 100;
        Node[] ends = GraphTestUtil.buildNoBifurcations(directedBuilder(), nnodes);

        CountingWalker walker = new CountingWalker();

        final DijkstraIterator iterator = createIterator();
        iterator.setSource(ends[0]);

        BasicGraphTraversal traversal =
                new BasicGraphTraversal(directedBuilder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            Iterator related = iterator.getRelated(component);
            int count = 0;
            int expectedCount = 1;
            if (component.getID() == nnodes - 1) {
                expectedCount = 0;
            }
            while (related.hasNext()) {
                Graphable relatedComponent = (Graphable) related.next();
                Assert.assertEquals(component.getID(), relatedComponent.getID() - 1);
                count++;
            }
            Assert.assertEquals(expectedCount, count);

            return 0;
        };
        directedBuilder().getGraph().visitNodes(visitor);

        Assert.assertEquals(walker.getCount(), nnodes);
    }

    protected GraphBuilder createBuilder() {
        return new BasicGraphBuilder();
    }

    protected GraphBuilder builder() {
        return m_builder;
    }

    protected GraphBuilder createDirectedBuilder() {
        return new BasicDirectedGraphBuilder();
    }

    protected GraphBuilder directedBuilder() {
        return m_directed_builder;
    }
}
