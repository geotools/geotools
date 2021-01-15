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

import org.geotools.graph.build.line.OptLineGraphGenerator;
import org.geotools.graph.structure.Edge;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.GraphVisitor;
import org.geotools.graph.structure.Graphable;
import org.geotools.graph.structure.line.XYNode;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

public class OptLineGraphGeneratorTest {

    private OptLineGraphGenerator m_gen;

    @Before
    public void setUp() throws Exception {

        m_gen = createGenerator();
    }

    /**
     * Build a linear graph by adding a number of line segments that join at endpoints. <br>
     * Expected: 1. Number of edges = number of lines added. 2. Number of nodes = number of lines +
     * 1
     */
    @Test
    public void test_0() {
        final Coordinate base = new Coordinate(0d, 0d);
        final int n = 100;
        for (int i = 1; i <= n; i++) {
            Edge e =
                    (Edge)
                            generator()
                                    .add(
                                            new LineSegment(
                                                    new Coordinate(
                                                            base.x + (i - 1), base.y + (i - 1)),
                                                    new Coordinate(base.x + i, base.y + i)));
        }
        generator().generate();
        Graph built = generator().getGraph();

        // ensure correct graph structure
        Assert.assertEquals(built.getEdges().size(), n);
        Assert.assertEquals(built.getNodes().size(), n + 1);

        Assert.assertEquals(2, built.getNodesOfDegree(1).size());
        Assert.assertEquals(built.getNodesOfDegree(2).size(), n - 1);

        // ensure coordinates
        GraphVisitor visitor =
                new GraphVisitor() {
                    public int visit(Graphable component) {
                        Edge e = (Edge) component;
                        XYNode a = (XYNode) e.getNodeA();
                        XYNode b = (XYNode) e.getNodeB();

                        // coordinats should be a distance of sqrt(2)
                        Assert.assertTrue(
                                Math.abs(a.getCoordinate().x - b.getCoordinate().x) == 1
                                        && Math.abs(a.getCoordinate().y - b.getCoordinate().y)
                                                == 1);

                        //        assertTrue(
                        //            Math.abs(a.getX() - b.getX()) == 1
                        //         && Math.abs(a.getY() - b.getY()) == 1
                        //        );
                        return (0);
                    }
                };
        built.visitEdges(visitor);
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
            Edge e =
                    (Edge)
                            generator()
                                    .add(
                                            new LineSegment(
                                                    new Coordinate(
                                                            base.x + (i - 1), base.y + (i - 1)),
                                                    new Coordinate(base.x + i, base.y + i)));
        }

        // complete the circle
        generator().add(new LineSegment(new Coordinate(base.x + n, base.y + n), base));

        generator().generate();
        Graph built = generator().getGraph();

        Assert.assertEquals(built.getEdges().size(), n + 1);
        Assert.assertEquals(built.getNodes().size(), n + 1);

        // all nodes should be of degree 2
        Assert.assertEquals(built.getNodesOfDegree(2).size(), built.getNodes().size());

        // ensure coordinates
        GraphVisitor visitor =
                new GraphVisitor() {
                    public int visit(Graphable component) {
                        Edge e = (Edge) component;
                        XYNode a = (XYNode) e.getNodeA();
                        XYNode b = (XYNode) e.getNodeB();

                        if (b.getCoordinate().equals(base)) {
                            Assert.assertEquals(a.getCoordinate(), new Coordinate(n, n));
                        } else {
                            Assert.assertTrue(
                                    Math.abs(a.getCoordinate().x - b.getCoordinate().x) == 1
                                            && Math.abs(a.getCoordinate().y - b.getCoordinate().y)
                                                    == 1);
                        }

                        return (0);
                    }
                };
        built.visitEdges(visitor);
    }

    protected OptLineGraphGenerator createGenerator() {
        return (new OptLineGraphGenerator());
    }

    protected OptLineGraphGenerator generator() {
        return (m_gen);
    }
}
