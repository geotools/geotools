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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicDirectedGraphBuilder;
import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Node;
import org.junit.Test;

public class DirectedGraphSerializerTest extends BasicGraphSerializerTest {

    /**
     * Create a simple graph with no bifurcations and serialize, then deserialize <br>
     * <br>
     * Expected: 1. before and after graph should have same structure.
     */
    @Override
    @Test
    public void test_0() {
        final int nnodes = 100;
        GraphTestUtil.buildNoBifurcations(builder(), nnodes);

        try {
            // String filename = System.getProperty("user.dir") + File.sptmp.tmp";
            File victim = File.createTempFile("graph", null);
            victim.deleteOnExit();

            serializer().setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());

            serializer().write(builder().getGraph());

            Graph before = builder().getGraph();
            Graph after = serializer().read();

            // ensure same number of nodes and edges
            assertEquals(before.getNodes().size(), after.getNodes().size());
            assertEquals(before.getEdges().size(), after.getEdges().size());

            // ensure same graph structure
            GraphVisitor visitor = component -> {
                DirectedEdge e = (DirectedEdge) component;

                assertEquals(e.getInNode().getID(), e.getID());
                assertEquals(e.getOutNode().getID(), e.getID() + 1);

                return 0;
            };
            after.visitEdges(visitor);

            visitor = component -> {
                DirectedNode n = (DirectedNode) component;

                if (n.getDegree() == 1) {
                    assertTrue(n.getID() == 0 || n.getID() == nnodes - 1);
                } else {
                    assertEquals(2, n.getDegree());

                    Edge in = n.getInEdges().get(0);
                    Edge out = n.getOutEdges().get(0);

                    assertTrue(in.getID() == n.getID() - 1 && out.getID() == n.getID());
                }

                return 0;
            };
            after.visitNodes(visitor);

        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            fail();
        }
    }

    /**
     * Create a perfect binary tree, serialize it and deserialize it. <br>
     * <br>
     * Expected: 1. Same structure before and after.
     */
    @Override
    @Test
    public void test_1() {
        final int k = 5;
        Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
        final Map obj2node = (Map) obj[1];

        try {
            File victim = File.createTempFile("graph", null);
            victim.deleteOnExit();

            serializer().setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());

            serializer().write(builder().getGraph());

            Graph before = builder().getGraph();
            Graph after = serializer().read();

            // ensure same number of nodes and edges
            assertEquals(before.getNodes().size(), after.getNodes().size());
            assertEquals(before.getEdges().size(), after.getEdges().size());

            // ensure same structure
            GraphVisitor visitor = component -> {
                DirectedNode n = (DirectedNode) component;
                String id = (String) n.getObject();

                assertNotNull(obj2node.get(id));

                StringTokenizer st = new StringTokenizer(id, ".");

                if (st.countTokens() == 1) {
                    // root
                    assertEquals(2, n.getDegree());

                    Node n0 = n.getEdges().get(0).getOtherNode(n);
                    Node n1 = n.getEdges().get(1).getOtherNode(n);

                    assertTrue(n0.getObject().equals("0.0") && n1.getObject().equals("0.1")
                            || n0.getObject().equals("0.1") && n1.getObject().equals("0.0"));
                } else if (st.countTokens() == k + 1) {
                    // leaf
                    assertEquals(1, n.getDegree());

                    Node parent = ((DirectedEdge) n.getInEdges().get(0)).getInNode();
                    String parentid = (String) parent.getObject();

                    assertEquals(parentid, id.substring(0, id.length() - 2));
                } else {
                    // internal
                    assertEquals(3, n.getDegree());

                    String parent = ((DirectedEdge) n.getInEdges().get(0))
                            .getInNode()
                            .getObject()
                            .toString();
                    String c0 = ((DirectedEdge) n.getOutEdges().get(0))
                            .getOutNode()
                            .getObject()
                            .toString();
                    String c1 = ((DirectedEdge) n.getOutEdges().get(1))
                            .getOutNode()
                            .getObject()
                            .toString();

                    String parentid = id.substring(0, id.length() - 2);

                    assertTrue(parent.equals(parentid) && c0.equals(id + ".0") && c1.equals(id + ".1")
                            || parent.equals(parentid) && c1.equals(id + ".0") && c0.equals(id + ".1"));
                }

                return 0;
            };
            after.visitNodes(visitor);

        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            fail();
        }
    }

    /**
     * Create a simple graph and disconnect two nodes (remove all edges) then serialize and deserialize. <br>
     * <br>
     * Exepcted: 1. Same graph structure.
     */
    @Override
    @Test
    public void test_2() {
        final int nnodes = 100;
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);

        Set<Edge> toRemove = new HashSet<>();
        toRemove.addAll(ends[0].getEdges());
        toRemove.addAll(ends[1].getEdges());

        // disconnect end nodes
        builder().removeEdges(toRemove);

        assertEquals(builder().getGraph().getNodes().size(), nnodes);
        assertEquals(builder().getGraph().getEdges().size(), nnodes - 3);

        try {
            File victim = File.createTempFile("graph", null);
            victim.deleteOnExit();

            serializer().setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());

            serializer().write(builder().getGraph());

            Graph before = builder().getGraph();
            Graph after = serializer().read();

            // ensure same number of nodes and edges
            assertEquals(before.getNodes().size(), after.getNodes().size());
            assertEquals(before.getEdges().size(), after.getEdges().size());

            GraphVisitor visitor = component -> {
                Node n = (Node) component;
                if (n.getID() == 0 || n.getID() == nnodes - 1) assertEquals(0, n.getDegree());
                else if (n.getID() == 1 || n.getID() == nnodes - 2) assertEquals(1, n.getDegree());
                else assertEquals(2, n.getDegree());

                return 0;
            };
            after.visitNodes(visitor);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            fail();
        }
    }

    @Override
    protected GraphBuilder createBuilder() {
        return new BasicDirectedGraphBuilder();
    }

    @Override
    protected GraphBuilder createRebuilder() {
        return new BasicDirectedGraphBuilder();
    }
}
