/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.geotools.graph.build.line.BasicLineGraphGenerator;
import org.geotools.graph.path.DijkstraShortestPathFinder;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.geotools.graph.traverse.standard.DijkstraIterator;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.LineSegment;

public class DijkstraShortestPathFinderWithTurnCostsTest {

    private Graph graph;

    @Before
    public void setUp() throws Exception {

        LineSegment ld1 = new LineSegment();
        ld1.setCoordinates(new Coordinate(0, 0), new Coordinate(1, 1));

        LineSegment ld2 = new LineSegment();
        ld2.setCoordinates(new Coordinate(1, 1), new Coordinate(0, 2));

        LineSegment ld3 = new LineSegment();
        ld3.setCoordinates(new Coordinate(1, 1), new Coordinate(1, 0));

        // we have some line segments
        LineSegment[] lines = {ld1, ld2, ld3};

        // create the graph generator
        BasicLineGraphGenerator graphGen = new BasicLineGraphGenerator();

        // add the lines to the graph
        for (LineSegment line : lines) {
            graphGen.add(line);
        }

        this.graph = graphGen.getGraph();

        // System.out.println(graph);
    }

    @Test
    public void test1() {

        List<Double> gotArray = new ArrayList<>(4);

        Iterator it = graph.getNodes().iterator();

        Node source = (Node) it.next();

        // create the path finder
        DijkstraShortestPathFinder pf = new DijkstraShortestPathFinder(graph, source, costFunction(), tcostFunction());

        pf.calculate();

        Iterator it1 = graph.getNodes().iterator();

        while (it1.hasNext()) {
            Node d = (Node) it1.next();

            gotArray.add(pf.getCost(d));
        }
    }

    protected DijkstraIterator.EdgeWeighter costFunction() {
        return e -> 1;
    }

    protected DijkstraIterator.NodeWeighter tcostFunction() {
        return (n, e1, e2) -> 1.0;
    }
}
