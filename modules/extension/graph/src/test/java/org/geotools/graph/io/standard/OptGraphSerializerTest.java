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
import org.geotools.graph.build.opt.OptGraphBuilder;
import org.geotools.graph.structure.Graph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OptGraphSerializerTest {
    private OptGraphBuilder m_builder;
    private OptGraphBuilder m_rebuilder;
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
            Assert.assertEquals(2, after.getNodesOfDegree(1).size());
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

            Graph after = serializer().read();

            Assert.assertEquals(1, after.getNodesOfDegree(2).size()); // root
            Assert.assertEquals(after.getNodesOfDegree(3).size(), (int) Math.pow(2, k) - 2); // internal
            Assert.assertEquals(after.getNodesOfDegree(1).size(), (int) Math.pow(2, k)); // leaves
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    protected OptGraphBuilder createBuilder() {
        return new OptGraphBuilder();
    }

    protected OptGraphBuilder builder() {
        return m_builder;
    }

    protected OptGraphBuilder createRebuilder() {
        return new OptGraphBuilder();
    }

    protected OptGraphBuilder rebuilder() {
        return m_rebuilder;
    }

    protected SerializedReaderWriter serializer() {
        return m_serializer;
    }
}
