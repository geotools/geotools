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
package org.geotools.gml2.bindings;

import javax.xml.namespace.QName;
import org.geotools.gml2.GML;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.picocontainer.defaults.DefaultPicoContainer;

public class GMLGeometryCollectionTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance gcol;
    ElementInstance point1;
    ElementInstance point2;
    ElementInstance line1;
    ElementInstance ring1;
    ElementInstance poly1;
    GeometryFactory gf;

    protected void setUp() throws Exception {
        super.setUp();

        point1 = createElement(GML.NAMESPACE, "myPoint", GML.PointMemberType, null);
        point2 = createElement(GML.NAMESPACE, "myPoint", GML.PointMemberType, null);
        line1 =
                createElement(
                        GML.NAMESPACE,
                        "myLine",
                        new QName("http://www.opengis.net/gml", "LineStringMemberType"),
                        null);
        ring1 = createElement(GML.NAMESPACE, "myLine", GML.LinearRingMemberType, null);
        poly1 = createElement(GML.NAMESPACE, "myPoly", GML.PolygonMemberType, null);
        gcol = createElement(GML.NAMESPACE, "myColl", GML.GeometryCollectionType, null);
        gf = new GeometryFactory();

        container = new DefaultPicoContainer();
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLGeometryCollectionTypeBinding.class);
    }

    public void testHomogeneous() throws Exception {
        Node node =
                createNode(
                        gcol,
                        new ElementInstance[] {point1, point2},
                        new Object[] {
                            gf.createPoint(new Coordinate(0, 0)),
                            gf.createPoint(new Coordinate(1, 1))
                        },
                        null,
                        null);

        GMLGeometryCollectionTypeBinding s =
                (GMLGeometryCollectionTypeBinding)
                        container.getComponentInstanceOfType(
                                GMLGeometryCollectionTypeBinding.class);

        GeometryCollection gc = (GeometryCollection) s.parse(gcol, node, null);
        assertNotNull(gc);
        assertEquals(gc.getNumGeometries(), 2);
        assertTrue(gc.getGeometryN(0) instanceof Point);
        assertTrue(gc.getGeometryN(1) instanceof Point);
        assertEquals(((Point) gc.getGeometryN(0)).getX(), 0d, 0d);
        assertEquals(((Point) gc.getGeometryN(0)).getY(), 0d, 0d);
        assertEquals(((Point) gc.getGeometryN(1)).getX(), 1d, 0d);
        assertEquals(((Point) gc.getGeometryN(1)).getY(), 1d, 0d);
    }

    public void testHeterogeneous() throws Exception {
        Node node =
                createNode(
                        gcol,
                        new ElementInstance[] {point1, point2, line1, ring1, poly1},
                        new Object[] {
                            gf.createPoint(new Coordinate(0, 0)),
                            gf.createPoint(new Coordinate(1, 1)),
                            gf.createLineString(
                                    new Coordinate[] {new Coordinate(0, 0), new Coordinate(1, 1)}),
                            gf.createLinearRing(
                                    new Coordinate[] {
                                        new Coordinate(0, 0),
                                        new Coordinate(1, 1),
                                        new Coordinate(2, 2),
                                        new Coordinate(0, 0)
                                    }),
                            gf.createPolygon(
                                    gf.createLinearRing(
                                            new Coordinate[] {
                                                new Coordinate(0, 0),
                                                new Coordinate(1, 1),
                                                new Coordinate(2, 2),
                                                new Coordinate(0, 0)
                                            }),
                                    null)
                        },
                        null,
                        null);

        GMLGeometryCollectionTypeBinding s =
                (GMLGeometryCollectionTypeBinding)
                        container.getComponentInstanceOfType(
                                GMLGeometryCollectionTypeBinding.class);

        GeometryCollection gc = (GeometryCollection) s.parse(gcol, node, null);
        assertNotNull(gc);
        assertEquals(gc.getNumGeometries(), 5);
        assertTrue(gc.getGeometryN(0) instanceof Point);
        assertTrue(gc.getGeometryN(1) instanceof Point);
        assertTrue(gc.getGeometryN(2) instanceof LineString);
        assertTrue(gc.getGeometryN(3) instanceof LinearRing);
        assertTrue(gc.getGeometryN(4) instanceof Polygon);
    }
}
