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
package org.geotools.graph.util;

import java.util.List;
import java.util.Map;
import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Node;
import org.geotools.graph.util.graph.GraphPartitioner;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GraphPartitionerTest {

    private GraphBuilder m_builder;

    @Before
    public void setUp() throws Exception {

        m_builder = createBuilder();
    }

    /**
     * Create a graph in which every node is connected and partition.
     *
     * <p>Expected: 1. There should only be one partition.
     */
    @Test
    public void test_0() {
        int nnodes = 100;
        GraphTestUtil.buildNoBifurcations(builder(), nnodes);

        GraphPartitioner partitioner = new GraphPartitioner(builder().getGraph());
        partitioner.partition();

        List partitions = partitioner.getPartitions();

        Assert.assertEquals(1, partitions.size());

        // ensure every node in the original graph is in the new graph
        final Graph g = (Graph) partitions.get(0);
        Assert.assertEquals(g.getNodes().size(), builder().getGraph().getNodes().size());
        Assert.assertEquals(g.getEdges().size(), builder().getGraph().getEdges().size());

        GraphVisitor visitor = component -> {
            Assert.assertTrue(g.getNodes().contains(component));
            return 0;
        };
        builder().getGraph().visitNodes(visitor);
    }

    /**
     * Create a balanced binary tree and then remove the root node and partition. <br>
     * <br>
     * Expected: 1. Two graphs should be created. One for each subtree of original.
     */
    @Test
    public void test_1() {
        int k = 4;
        Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
        Node root = (Node) obj[0];
        Map id2node = (Map) obj[1];

        Node lc = (Node) id2node.get("0.0");
        Node rc = (Node) id2node.get("0.1");

        builder().removeNode(root);

        GraphPartitioner parter = new GraphPartitioner(builder().getGraph());
        parter.partition();

        List partitions = parter.getPartitions();

        Assert.assertEquals(2, partitions.size());

        Graph left = (Graph) partitions.get(0);
        Graph right = (Graph) partitions.get(1);

        if (!left.getNodes().contains(lc)) {
            // swap
            left = (Graph) partitions.get(1);
            right = (Graph) partitions.get(0);
        }

        Assert.assertTrue(left.getNodes().contains(lc));
        Assert.assertTrue(right.getNodes().contains(rc));

        Assert.assertEquals(left.getNodes().size(), (int) Math.pow(2, k) - 1);
        Assert.assertEquals(left.getEdges().size(), (int) Math.pow(2, k) - 2);
        Assert.assertEquals(right.getNodes().size(), (int) Math.pow(2, k) - 1);
        Assert.assertEquals(right.getEdges().size(), (int) Math.pow(2, k) - 2);

        GraphVisitor visitor = component -> {
            Assert.assertTrue(component.getObject().toString().startsWith("0.0"));
            return 0;
        };
        left.visitNodes(visitor);

        visitor = component -> {
            Assert.assertTrue(component.getObject().toString().startsWith("0.1"));
            return 0;
        };
        right.visitNodes(visitor);
    }

    protected GraphBuilder createBuilder() {
        return new BasicGraphBuilder();
    }

    protected GraphBuilder builder() {
        return m_builder;
    }
}
