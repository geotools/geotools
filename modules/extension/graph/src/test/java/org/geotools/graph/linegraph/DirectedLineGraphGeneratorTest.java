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
package org.geotools.graph.linegraph;

import org.geotools.graph.build.line.BasicDirectedLineGraphGenerator;
import org.geotools.graph.structure.DirectedEdge;
import org.geotools.graph.structure.DirectedNode;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

public class DirectedLineGraphGeneratorTest {

    private BasicDirectedLineGraphGenerator m_gen;

    @Before
    public void setUp() throws Exception {

        m_gen = createGenerator();
    }

    /**
     * Build a linear graph by adding a number of line segments that join at endpoints. <br>
     * Expected: 1. Number of edges = number of lines added. 2. Number of nodes = number of lines + 1
     */
    @Test
    public void test_0() {
        final Coordinate base = new Coordinate(0d, 0d);
        final int n = 100;
        for (int i = 1; i <= n; i++) {
            Edge e = (Edge) generator()
                    .add(new LineSegment(
                            new Coordinate(base.x + (i - 1), base.y + (i - 1)),
                            new Coordinate(base.x + i, base.y + i)));
            e.setID(i - 1);
            e.getNodeA().setID(i - 1);
            e.getNodeB().setID(i);
        }

        Graph built = generator().getGraph();

        // ensure correct graph structure
        Assert.assertEquals(built.getEdges().size(), n);
        Assert.assertEquals(built.getNodes().size(), n + 1);

        GraphVisitor visitor = component -> {
            DirectedNode node = (DirectedNode) component;
            Coordinate c = (Coordinate) node.getObject();

            if (node.getDegree() == 1) {
                Assert.assertTrue(node.getID() == 0 && node.getInDegree() == 0 && node.getOutDegree() == 1
                        || node.getID() == n && node.getInDegree() == 1 && node.getOutDegree() == 0);
            } else {
                Assert.assertTrue(node.getInDegree() == 1 && node.getOutDegree() == 1);
            }

            Assert.assertTrue(c.x == base.x + node.getID() && c.y == base.y + node.getID());
            return 0;
        };
        built.visitNodes(visitor);

        // ensure correct edge direction
        visitor = component -> {
            DirectedEdge e = (DirectedEdge) component;
            Coordinate c0 = (Coordinate) e.getInNode().getObject();
            Coordinate c1 = (Coordinate) e.getOutNode().getObject();
            LineSegment ls = (LineSegment) e.getObject();

            Assert.assertTrue(ls.p0.equals(c0) && ls.p1.equals(c1));

            return 0;
        };
    }

    /**
     * Build a circular graph of line segments that join at endpoints. <br>
     * <br>
     * Expected: 1. Number of edges = number of nodes = number of lines.
     */
    @Test
    public void test_1() {
        final Coordinate base = new Coordinate(0d, 0d);
        final int n = 100;
        for (int i = 1; i <= n; i++) {
            Edge e = (Edge) generator()
                    .add(new LineSegment(
                            new Coordinate(base.x + (i - 1), base.y + (i - 1)),
                            new Coordinate(base.x + i, base.y + i)));
            e.setID(i - 1);
            e.getNodeA().setID(i - 1);
            e.getNodeB().setID(i);
        }

        // complete the circle
        generator().add(new LineSegment(new Coordinate(base.x + n, base.y + n), base));

        Graph built = generator().getGraph();

        Assert.assertEquals(built.getEdges().size(), n + 1);
        Assert.assertEquals(built.getNodes().size(), n + 1);
    }

    protected BasicDirectedLineGraphGenerator createGenerator() {
        return new BasicDirectedLineGraphGenerator();
    }

    protected BasicDirectedLineGraphGenerator generator() {
        return m_gen;
    }
}
