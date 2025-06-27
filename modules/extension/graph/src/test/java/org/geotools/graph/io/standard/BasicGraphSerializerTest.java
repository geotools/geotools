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

import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InvalidClassException;
import java.io.ObjectOutputStream;
import java.util.HashSet;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.StringTokenizer;
import org.geotools.graph.GraphTestUtil;
import org.geotools.graph.build.GraphBuilder;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class BasicGraphSerializerTest {

    private GraphBuilder m_builder;
    private GraphBuilder m_rebuilder;
    private SerializedReaderWriter m_serializer;

    @Before
    public void setUp() throws Exception {

        m_builder = createBuilder();
        m_rebuilder = createRebuilder();
        m_serializer = new SerializedReaderWriter();
        m_serializer.setProperty(SerializedReaderWriter.BUILDER, rebuilder());
    }

    /**
     * Create a simple graph with no bifurcations and serialize, then deserialize <br>
     * <br>
     * Expected: 1. before and after graph should have same structure.
     */
    @Test
    public void test_0() throws Exception {
        final int nnodes = 100;
        GraphTestUtil.buildNoBifurcations(builder(), nnodes);
        File victim = File.createTempFile("graph", null);
        victim.deleteOnExit();

        m_serializer.setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());

        m_serializer.write(builder().getGraph());

        Graph before = builder().getGraph();
        Graph after = m_serializer.read();

        // ensure same number of nodes and edges
        Assert.assertEquals(before.getNodes().size(), after.getNodes().size());
        Assert.assertEquals(before.getEdges().size(), after.getEdges().size());

        // ensure same graph structure
        GraphVisitor visitor = component -> {
            Edge e = (Edge) component;

            Assert.assertEquals(e.getNodeA().getID(), e.getID());
            Assert.assertEquals(e.getNodeB().getID(), e.getID() + 1);

            return 0;
        };
        after.visitEdges(visitor);

        visitor = component -> {
            Node n = (Node) component;

            if (n.getDegree() == 1) {
                Assert.assertTrue(n.getID() == 0 || n.getID() == nnodes - 1);
            } else {
                Assert.assertEquals(2, n.getDegree());

                Edge e0 = n.getEdges().get(0);
                Edge e1 = n.getEdges().get(1);

                Assert.assertTrue(e0.getID() == n.getID() - 1 && e1.getID() == n.getID()
                        || e1.getID() == n.getID() - 1 && e0.getID() == n.getID());
            }

            return 0;
        };
        after.visitNodes(visitor);
    }

    /**
     * Create a perfect binary tree, serialize it and deserialize it. <br>
     * <br>
     * Expected: 1. Same structure before and after.
     */
    @Test
    public void test_1() {
        final int k = 5;
        Object[] obj = GraphTestUtil.buildPerfectBinaryTree(builder(), k);
        final Map obj2node = (Map) obj[1];

        try {
            File victim = File.createTempFile("graph", null);
            victim.deleteOnExit();
            m_serializer.setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());

            m_serializer.write(builder().getGraph());

            Graph before = builder().getGraph();
            Graph after = m_serializer.read();

            // ensure same number of nodes and edges
            Assert.assertEquals(before.getNodes().size(), after.getNodes().size());
            Assert.assertEquals(before.getEdges().size(), after.getEdges().size());

            // ensure same structure
            GraphVisitor visitor = component -> {
                Node n = (Node) component;
                String id = (String) n.getObject();

                Assert.assertNotNull(obj2node.get(id));

                StringTokenizer st = new StringTokenizer(id, ".");

                if (st.countTokens() == 1) {
                    // root
                    Assert.assertEquals(2, n.getDegree());

                    Node n0 = n.getEdges().get(0).getOtherNode(n);
                    Node n1 = n.getEdges().get(1).getOtherNode(n);

                    Assert.assertTrue(n0.getObject().equals("0.0")
                                    && n1.getObject().equals("0.1")
                            || n0.getObject().equals("0.1") && n1.getObject().equals("0.0"));
                } else if (st.countTokens() == k + 1) {
                    // leaf
                    Assert.assertEquals(1, n.getDegree());

                    Node parent = n.getEdges().get(0).getOtherNode(n);
                    String parentid = (String) parent.getObject();

                    Assert.assertEquals(parentid, id.substring(0, id.length() - 2));
                } else {
                    // internal
                    Assert.assertEquals(3, n.getDegree());

                    String id0 = n.getEdges().get(0).getOtherNode(n).getObject().toString();
                    String id1 = n.getEdges().get(1).getOtherNode(n).getObject().toString();
                    String id2 = n.getEdges().get(2).getOtherNode(n).getObject().toString();

                    String parentid = id.substring(0, id.length() - 2);

                    Assert.assertTrue(id0.equals(parentid) && id1.equals(id + ".0") && id2.equals(id + ".1")
                            || id0.equals(parentid) && id2.equals(id + ".0") && id1.equals(id + ".1")
                            || id1.equals(parentid) && id0.equals(id + ".0") && id2.equals(id + ".1")
                            || id1.equals(parentid) && id2.equals(id + ".0") && id0.equals(id + ".1")
                            || id2.equals(parentid) && id0.equals(id + ".0") && id1.equals(id + ".1")
                            || id2.equals(parentid) && id1.equals(id + ".0") && id0.equals(id + ".1"));
                }

                return 0;
            };
            after.visitNodes(visitor);

        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail();
        }
    }

    /**
     * Create a simple graph and disconnect two nodes (remove all edges) then serialize and deserialize. <br>
     * <br>
     * Exepcted: 1. Same graph structure.
     */
    @Test
    public void test_2() {
        final int nnodes = 100;
        Node[] ends = GraphTestUtil.buildNoBifurcations(builder(), nnodes);

        Set<Edge> toRemove = new HashSet<>();
        toRemove.addAll(ends[0].getEdges());
        toRemove.addAll(ends[1].getEdges());

        // disconnect end nodes
        builder().removeEdges(toRemove);

        Assert.assertEquals(builder().getGraph().getNodes().size(), nnodes);
        Assert.assertEquals(builder().getGraph().getEdges().size(), nnodes - 3);

        try {
            File victim = File.createTempFile("graph", null);
            victim.deleteOnExit();
            m_serializer.setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());

            m_serializer.write(builder().getGraph());

            Graph before = builder().getGraph();
            Graph after = m_serializer.read();

            // ensure same number of nodes and edges
            Assert.assertEquals(before.getNodes().size(), after.getNodes().size());
            Assert.assertEquals(before.getEdges().size(), after.getEdges().size());

            GraphVisitor visitor = component -> {
                Node n = (Node) component;
                if (n.getID() == 0 || n.getID() == nnodes - 1) Assert.assertEquals(0, n.getDegree());
                else if (n.getID() == 1 || n.getID() == nnodes - 2) Assert.assertEquals(1, n.getDegree());
                else Assert.assertEquals(2, n.getDegree());

                return 0;
            };
            after.visitNodes(visitor);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail();
        }
    }

    @Test
    public void testDeserializationValidation() throws Exception {
        File victim = File.createTempFile("graph", null);
        victim.deleteOnExit();
        m_serializer.setProperty(SerializedReaderWriter.FILENAME, victim.getAbsolutePath());
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(victim))) {
            out.writeInt(0);
            out.writeInt(1);
            out.writeObject(new PriorityQueue<>());
        }
        assertThrows(InvalidClassException.class, () -> m_serializer.read());
    }

    protected GraphBuilder createBuilder() {
        return new BasicGraphBuilder();
    }

    protected GraphBuilder createRebuilder() {
        return new BasicGraphBuilder();
    }

    protected GraphBuilder builder() {
        return m_builder;
    }

    protected GraphBuilder rebuilder() {
        return m_rebuilder;
    }

    protected SerializedReaderWriter serializer() {
        return m_serializer;
    }
}
