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
package org.geotools.graph.traverse.standard;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicDirectedGraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.path.AStarShortestPathFinder;
import org.geotools.graph.path.Path;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.GraphTraversal;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.basic.CountingWalker;
import org.geotools.graph.traverse.standard.AStarIterator.AStarNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AStarIteratorTest {
    public GraphBuilder m_builder;
    public GraphBuilder m_directed_builder;

    @Before
    public void setUp() throws Exception {

        m_builder = createBuilder();
        m_directed_builder = createDirectedBuilder();
    }
    /**
     * Test 0: Graph with no bifurcations. 100 nodes. G = 1 for all edges H = TargetsID - currentID Expected: 1. Every
     * node should be visited 2. ParentID = SonsID - 1
     */
    @Test
    public void test_0() {
        final int nnodes = 100;
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);

        CountingWalker walker = new CountingWalker();

        final AStarIterator iterator = createIterator(ends[0], ends[ends.length - 1]);

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            Assert.assertTrue(component.isVisited());
            if (component.getID() == 0) Assert.assertNull(iterator.getParent((Node) component));
            else
                Assert.assertEquals(
                        component.getID(), iterator.getParent((Node) component).getID() + 1);
            return 0;
        };
        builder().getGraph().visitNodes(visitor);

        Assert.assertEquals(walker.getCount(), nnodes);
    }

    /**
     * Create a graph with no bifurcations and start a traversal from start node, then suspend, and resume. <br>
     * <br>
     * Expected: After suspend: 1. Nodes from 0 to suspend node should be visted, others not.
     *
     * <p>After resume: 1. Next node visited should have id suspend node id + 1 2. Every node should have a parent with
     * id node id + 1
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

        final AStarIterator iterator = createIterator(ends[0], ends[ends.length - 1]);

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
            if (component.getID() == 0) Assert.assertNull(iterator.getParent((Node) component));
            else Assert.assertEquals(iterator.getParent((Node) component).getID(), component.getID() - 1);

            return 0;
        };
        builder().getGraph().visitNodes(visitor);
        Assert.assertEquals(walker.getCount(), nnodes);
    }

    /**
     * Create a balanced binary tree and do a normal traversal starting at root. <br>
     * <br>
     * Expected: 1. #nodes_visited <= #nodes 2. The parent of each node should be the same as the parent of the tree. 3.
     * G = depth. H = infinity if the target is not in any subtree of this node or depth difference between target node
     * and current node otherwise.
     */
    @Test
    @SuppressWarnings({"unchecked", "PMD.AvoidUsingHardCodedIP"}) // refusing to clean up this unholy mess
    public void test_3() {
        int k = 4;
        Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
        final Node root = (Node) obj[0];
        final HashMap map = (HashMap) obj[1];
        HashMap rmap = new HashMap<>();
        Map.Entry[] set = new Map.Entry[map.size()];
        map.entrySet().toArray(set);
        for (Map.Entry entry : set) {
            rmap.put(entry.getValue(), entry.getKey());
        }
        final HashMap hashmap = rmap;

        class Factory {
            public AStarIterator.AStarFunctions createFunctions(Node target) {
                return new AStarIterator.AStarFunctions(target) {
                    @Override
                    public double cost(AStarNode n1, AStarNode n2) {
                        return 1;
                    }

                    @Override
                    public double h(Node n) {
                        String dest = hashmap.get(this.getDest()).toString();
                        String current = hashmap.get(n).toString();
                        if (dest.startsWith(current)) {
                            // n under dest
                            dest = dest.replaceAll("\\D", "");
                            current = current.replaceAll("\\D", "");
                            return dest.length() - current.length();
                        } else {
                            return Double.POSITIVE_INFINITY;
                        }
                    }
                };
            }
        }
        Factory f = new Factory();

        AStarShortestPathFinder walker = new AStarShortestPathFinder(
                builder().getGraph(), root, (Node) map.get("0.1.0.1"), f.createFunctions((Node) map.get("0.1.0.1")));

        walker.calculate();
        MyVisitor visitor = new MyVisitor();
        builder().getGraph().visitNodes(visitor);
        // #1
        Assert.assertTrue(visitor.count > 0);
        Assert.assertTrue(visitor.count < map.size() + 1);
        Path p = null;
        try {
            p = walker.getPath();
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
        p.getEdges();
        Assert.assertEquals(4, p.size());
        // #2
        for (int j = 0; j < p.size() - 1; j++) {
            Node n = p.get(j);
            Node parent = p.get(j + 1);
            String n_id = rmap.get(n).toString();
            String parent_id = rmap.get(parent).toString();
            Assert.assertTrue(n_id.startsWith(parent_id));
        }
    }

    /**
     * Create an undirected graph with no bifurcations and start a full traversal from start node. Test getRelated at
     * each node.<br>
     * <br>
     * Expected: 1. The first and last node should have one related node. 2. All other nodes should have two related
     * nodes 3. Related nodes should have an id one greater or one less than the current node
     */
    @Test
    public void test_4() {
        final int nnodes = 100;
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);

        CountingWalker walker = new CountingWalker();

        final AStarIterator iterator = createIterator(ends[0], ends[ends.length - 1]);

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
    public void test_5() {
        final int nnodes = 100;
        Node[] ends = GraphTestUtil.buildNoBifurcations(directedBuilder(), nnodes);

        CountingWalker walker = new CountingWalker();

        final AStarIterator iterator = createIterator(ends[0], ends[ends.length - 1]);

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

    private static class MyVisitor implements GraphVisitor {
        public int count = 0;

        @Override
        public int visit(Graphable component) {
            if (component.isVisited()) {
                count++;
            }
            return 0;
        }
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

    protected AStarIterator createIterator(Node source, Node target) {
        return new AStarIterator(source, new AStarIterator.AStarFunctions(target) {

            @Override
            public double cost(AStarNode n1, AStarNode n2) {
                return 1;
            }

            @Override
            public double h(Node n) {
                return getDest().getID() - n.getID();
            }
        });
    }
}
