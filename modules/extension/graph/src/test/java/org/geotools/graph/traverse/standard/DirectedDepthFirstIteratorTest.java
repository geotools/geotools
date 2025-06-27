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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicDirectedGraphBuilder;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.basic.BasicGraphTraversal;
import org.geotools.graph.traverse.basic.DummyGraphWalker;
import org.junit.Test;

public class DirectedDepthFirstIteratorTest extends DepthFirstIteratorTest {

    /**
     * Create a graph with no bifurcations and do a full traversal from "last" node in graph. <br>
     * <br>
     * Expected: 1. Only last node should be visited.
     */
    @Test
    public void test_7() {
        final Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), 100);

        DummyGraphWalker walker = new DummyGraphWalker();
        DirectedDepthFirstIterator iterator = new DirectedDepthFirstIterator();
        BasicGraphTraversal traversal = new BasicGraphTraversal(builder().getGraph(), walker, iterator);
        traversal.init();

        iterator.setSource(ends[1]);
        traversal.traverse();

        // ensure only last node visited
        GraphVisitor visitor = component -> {
            if (component == ends[1]) assertTrue(component.isVisited());
            else assertFalse(component.isVisited());
            return 0;
        };
        builder().getGraph().visitNodes(visitor);
    }

    @Override
    protected DepthFirstIterator createIterator() {
        return new DirectedDepthFirstIterator();
    }

    @Override
    protected GraphBuilder createBuilder() {
        return new BasicDirectedGraphBuilder();
    }
}
