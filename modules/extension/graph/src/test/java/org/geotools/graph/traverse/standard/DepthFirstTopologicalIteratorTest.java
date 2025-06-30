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

public class DepthFirstTopologicalIteratorTest {
    private GraphBuilder m_builder;

    @Before
    public void setUp() throws Exception {

        m_builder = createBuilder();
    }

    /**
     * Create a graph with no bifurcations and do a full traversal. <br>
     * <br>
     * Expected: 1. Nodes should be visited from one end to other in order.
     */
    @Test
    public void test_0() {
        int nnodes = 100;
        GraphTestUtil.buildNoBifurcations(builder(), nnodes);

        CountingWalker walker = new CountingWalker() {
            @Override
            public int visit(Graphable element, GraphTraversal traversal) {
                element.setCount(getCount());
                return super.visit(element, traversal);
            }
        };
        DepthFirstTopologicalIterator iterator = createIterator();
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

        for (Node node : builder().getGraph().getNodes()) {
            if (flip) Assert.assertEquals(node.getCount(), 100 - 1 - node.getID());
            else Assert.assertEquals(node.getCount(), node.getID());
        }
    }

    /**
     * Create a circular graph and do a full traversal. <br>
     * <br>
     * Expected: 1. No nodes should be visited.
     */
    @Test
    public void test_1() {
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

    protected DepthFirstTopologicalIterator createIterator() {
        return new DepthFirstTopologicalIterator();
    }
}
