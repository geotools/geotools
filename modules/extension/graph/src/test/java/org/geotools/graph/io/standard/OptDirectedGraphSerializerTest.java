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
package org.geotools.graph.io.standard;

import java.io.File;
import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.opt.OptDirectedGraphBuilder;
import org.geotools.graph.build.opt.OptGraphBuilder;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptDirectedGraphSerializerTest {
    private OptDirectedGraphBuilder m_builder;
    private OptDirectedGraphBuilder m_rebuilder;
    private SerializedReaderWriter m_serializer;

    @Before
    public void setUp() throws Exception {

        m_builder = createBuilder();
        m_rebuilder = createBuilder();
        m_serializer = new SerializedReaderWriter();
        m_serializer.setProperty(SerializedReaderWriter.BUILDER, rebuilder());
    }

    /**
     * Create a simple graph with no bifurcations and serialize, then deserialize <br>
     * <br>
     * Expected: 1. before and after graph should have same structure.
     */
    @Test
    public void test_0() {
        final int nnodes = 100;
        GraphTestUtil.buildNoBifurcations(builder(), nnodes);
        try {
            File victim = File.createTempFile("graph", null);
            victim.deleteOnExit();
            serializer().setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());

            serializer().write(builder().getGraph());

            Graph before = builder().getGraph();
            Graph after = serializer().read();

            // ensure same number of nodes and edges
            Assert.assertEquals(before.getNodes().size(), after.getNodes().size());
            Assert.assertEquals(before.getEdges().size(), after.getEdges().size());

            // ensure two nodes of degree 1, and nnodes-2 nodes of degree 2
            GraphVisitor visitor = component -> {
                DirectedNode node = (DirectedNode) component;
                if (node.getInDegree() == 0 || node.getOutDegree() == 0) return Graph.PASS_AND_CONTINUE;
                return Graph.FAIL_QUERY;
            };
            Assert.assertEquals(2, after.queryNodes(visitor).size());

            visitor = component -> {
                DirectedNode node = (DirectedNode) component;
                if (node.getInDegree() == 1 || node.getOutDegree() == 1) return Graph.PASS_AND_CONTINUE;
                return Graph.FAIL_QUERY;
            };

            Assert.assertEquals(after.getNodesOfDegree(2).size(), nnodes - 2);

        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail();
        }
    }

    /**
     * Create a perfect binary tree, serialize it and deserialize it. <br>
     * <br>
     * Expected: 1. Same structure before and after.
     */
    @Test
    public void test_1() {
        final int k = 5;
        GraphTestUtil.buildPerfectBinaryTree(builder(), k);

        try {
            File victim = File.createTempFile("graph", null);
            victim.deleteOnExit();
            serializer().setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());

            serializer().write(builder().getGraph());

            Graph before = builder().getGraph();
            Graph after = serializer().read();

            // ensure same number of nodes and edges
            Assert.assertEquals(before.getNodes().size(), after.getNodes().size());
            Assert.assertEquals(before.getEdges().size(), after.getEdges().size());

            GraphVisitor visitor = component -> {
                DirectedNode node = (DirectedNode) component;
                if (node.getInDegree() == 0 && node.getOutDegree() == 2) return Graph.PASS_AND_CONTINUE;
                return Graph.FAIL_QUERY;
            };
            Assert.assertEquals(1, after.queryNodes(visitor).size()); // root

            visitor = component -> {
                DirectedNode node = (DirectedNode) component;
                if (node.getInDegree() == 1 && node.getOutDegree() == 2) return Graph.PASS_AND_CONTINUE;
                return Graph.FAIL_QUERY;
            };
            Assert.assertEquals(after.queryNodes(visitor).size(), (int) Math.pow(2, k) - 2); // internal

            visitor = component -> {
                DirectedNode node = (DirectedNode) component;
                if (node.getInDegree() == 1 && node.getOutDegree() == 0) return Graph.PASS_AND_CONTINUE;
                return Graph.FAIL_QUERY;
            };
            Assert.assertEquals(after.queryNodes(visitor).size(), (int) Math.pow(2, k)); // leaves
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail();
        }
    }

    protected OptDirectedGraphBuilder createBuilder() {
        return new OptDirectedGraphBuilder();
    }

    protected OptDirectedGraphBuilder builder() {
        return m_builder;
    }

    protected OptGraphBuilder createRebuilder() {
        return new OptGraphBuilder();
    }

    protected OptDirectedGraphBuilder rebuilder() {
        return m_rebuilder;
    }

    protected SerializedReaderWriter serializer() {
        return m_serializer;
    }
}
