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
package org.geotools.graph.build.polygon;

import java.util.StringTokenizer;
import org.geotools.graph.build.basic.BasicGraphBuilder;
import org.geotools.graph.structure.Graph;
import org.geotools.graph.structure.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

public class PolygonGraphGeneratorTest {

    static GeometryFactory gf = new GeometryFactory();
    PolygonGraphGenerator gg;

    @Before
    public void setUp() throws Exception {
        PolygonGraphGenerator.PolygonRelationship rel =
                new PolygonGraphGenerator.PolygonRelationship() {

                    @Override
                    public boolean related(Polygon p1, Polygon p2) {
                        return p1.intersects(p2);
                    }

                    @Override
                    public boolean equal(Polygon p1, Polygon p2) {
                        return p1.equalsTopo(p2);
                    }
                };

        gg = new PolygonGraphGenerator(new BasicGraphBuilder(), rel);
    }

    @Test
    public void testAdd() {
        Polygon p1 = createPolygon("0 0,1 1,2 2,0 0");
        Polygon p2 = createPolygon("3 3,4 4,5 5,3 3");
        Polygon p3 = createPolygon("6 6,7 7,8 8,6 6");

        Node n1 = (Node) gg.add(p1);
        Node n2 = (Node) gg.add(p2);
        Node n3 = (Node) gg.add(p3);

        Assert.assertNotNull(n1);
        Assert.assertEquals(n1.getObject(), p1);

        Assert.assertNotNull(n2);
        Assert.assertEquals(n2.getObject(), p2);

        Assert.assertNotNull(n3);
        Assert.assertEquals(n3.getObject(), p3);

        Graph g = gg.getGraph();
        Assert.assertEquals(3, g.getNodes().size());
        Assert.assertEquals(0, g.getEdges().size());
    }

    @Test
    public void testRelationships() {
        Polygon p1 = createPolygon("0 0,5 0,5 5,0 5,0 0");
        Polygon p2 = createPolygon("4 4,9 4,9 9,4 9,4 4");
        Polygon p3 = createPolygon("2 2,7 2,7 -3,2 -3,2 2");

        Node n1 = (Node) gg.add(p1);
        Node n2 = (Node) gg.add(p2);
        Node n3 = (Node) gg.add(p3);

        Assert.assertNotNull(n1.getEdge(n2));
        Assert.assertNotNull(n2.getEdge(n1));
        Assert.assertNotNull(n1.getEdge(n3));
        Assert.assertNotNull(n2.getEdge(n1));

        Assert.assertNull(n2.getEdge(n3));
        Assert.assertNull(n2.getEdge(n2));

        Graph g = gg.getGraph();
        Assert.assertEquals(3, g.getNodes().size());
        Assert.assertEquals(2, g.getEdges().size());
    }

    protected Polygon createPolygon(String coordinates) {
        StringTokenizer tokens = new StringTokenizer(coordinates, ",");
        Coordinate[] c = new Coordinate[tokens.countTokens()];

        int i = 0;
        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken();
            String[] oordinates = token.split(" ");
            c[i++] =
                    new Coordinate(
                            Double.parseDouble(oordinates[0]), Double.parseDouble(oordinates[1]));
        }

        return gf.createPolygon(gf.createLinearRing(c), null);
    }
}
