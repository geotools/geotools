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

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
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

public class BreadthFirstTopologicalIteratorTest {
    private GraphBuilder m_builder;

    @Before
    public void setUp() throws Exception {

        m_builder = createBuilder();
    }

    /**
     * Create a graph with no bifurcations and do a full traversal. <br>
     * <br>
     * Expected: 1. Nodes should be visited in order (1,n,2,n-2,...n/2). This order can be reversed.
     */
    @Test
    public void test_0() {
        int nnodes = 9;
        // odd number so there is a middle node
        GraphTestUtil.buildNoBifurcations(builder(), nnodes);

        // note: traversal uses count and we are changing the count value here.
        // but it shouldn't matter because at the time of visitation, the traversal
        // no longer needs the counter value.
        CountingWalker walker = new CountingWalker() {
            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                element.setCount(getCount());
                return super.visit(element, traversal);
            }
        };

        BreadthFirstTopologicalIterator iterator = createIterator();
        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        Assert.assertEquals(walker.getCount(), nnodes);

        boolean flip = false;
        for (Node node : builder().getGraph().getNodes()) {
            if (node.getID() == 0 && node.getCount() != 0) {
                flip = true;
                break;
            }
        }
        final int size = builder().getGraph().getNodes().size();

        for (Node node : builder().getGraph().getNodes()) {
            int id = node.getID();
            int expected = -1;

            if (id == size / 2) expected = size - 1;
            else if (id < size / 2) {
                if (!flip) {
                    expected = id * 2;
                } else {
                    expected = id * 2 + 1;
                }
            } else {
                if (!flip) {
                    expected = (size - 1 - id) * 2 + 1;
                } else {
                    expected = (size - 1 - id) * 2;
                }
            }

            Assert.assertEquals(expected, node.getCount());
        }
    }

    /**
     * Create a balanced binary tree and do a full traversal. <br>
     * <br>
     * Expected: 1. Nodes in a lower level of the tree should be visited before nodes in a higher level.
     */
    @Test
    public void test_1() {
        int k = 4;
        GraphTestUtil.buildPerfectBinaryTree(builder(), k);

        CountingWalker walker = new CountingWalker() {
            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                element.setCount(getCount());
                return super.visit(element, traversal);
            }
        };

        BreadthFirstTopologicalIterator iterator = createIterator();

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        // ensure that each node in lower level visited before node in higher level
        GraphVisitor visitor = component -> {
            String id = component.getObject().toString();

            for (Node other : builder().getGraph().getNodes()) {
                if (other.getObject().toString().length() < id.length()) {
                    Assert.assertTrue(other.getCount() > component.getCount());
                }
            }
            return 0;
        };
        builder().getGraph().visitNodes(visitor);

        Assert.assertEquals(walker.getCount(), (int) Math.pow(2, k + 1) - 1);
    }

    /**
     * Create a circular graph and do a full traversal. <br>
     * <br>
     * Expected: 1. No nodes should be visited.
     */
    @Test
    public void test_2() {
        int nnodes = 100;
        GraphTestUtil.buildCircular(builder(), nnodes);

        CountingWalker walker = new CountingWalker();
        BreadthFirstTopologicalIterator iterator = createIterator();

        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();
        traversal.traverse();

        GraphVisitor visitor = component -> {
            Assert.assertFalse(component.isVisited());
            return 0;
        };
        builder().getGraph().visitNodes(visitor);

        Assert.assertEquals(0, walker.getCount());
    }

    protected GraphBuilder createBuilder() {
        return new BasicGraphBuilder();
    }

    protected GraphBuilder builder() {
        return m_builder;
    }

    protected BreadthFirstTopologicalIterator createIterator() {
        return new BreadthFirstTopologicalIterator();
    }
}
