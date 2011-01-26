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

import org.geotools.gml2.GML;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


public class GMLPointTypeBindingTest extends AbstractGMLBindingTest {
    MutablePicoContainer container;
    ElementInstance point;
    ElementInstance coord;
    ElementInstance coords;

    protected void setUp() throws Exception {
        super.setUp();

        point = createElement(GML.NAMESPACE, "myPoint", GML.POINTTYPE, null);
        coord = createElement(GML.NAMESPACE, "coord", GML.COORDTYPE, null);
        coords = createElement(GML.NAMESPACE, "coordinates", GML.COORDINATESTYPE, null);

        container = new DefaultPicoContainer();
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLPointTypeBinding.class);
    }

    public void testParseCoordinate() throws Exception {
        Node node = createNode(point, new ElementInstance[] { coord },
                new Object[] { new Coordinate(12.34, 56.78) }, null, null);

        GMLPointTypeBinding strategy = (GMLPointTypeBinding) container.getComponentInstanceOfType(GMLPointTypeBinding.class);

        Point p = (Point) strategy.parse(point, node, null);
        assertNotNull(p);
        assertEquals(p.getX(), 12.34, 0d);
        assertEquals(p.getY(), 56.78, 0d);
    }

    public void testParseCoordinates() throws Exception {
        Node node = createNode(point, new ElementInstance[] { coords },
                new Object[] { createCoordinateSequence(new Coordinate(12.34, 56.78)) }, null, null);

        GMLPointTypeBinding strategy = (GMLPointTypeBinding) container.getComponentInstanceOfType(GMLPointTypeBinding.class);

        Point p = (Point) strategy.parse(point, node, null);
        assertNotNull(p);
        assertEquals(p.getX(), 12.34, 0d);
        assertEquals(p.getY(), 56.78, 0d);
    }

    public void testParseMultiCoordinates() throws Exception {
        Node node = createNode(point, new ElementInstance[] { coords },
                new Object[] {
                    createCoordinateSequence(
                        new Coordinate[] { new Coordinate(12.34, 56.78), new Coordinate(9.10, 11.12) })
                }, null, null);

        GMLPointTypeBinding strategy = (GMLPointTypeBinding) container.getComponentInstanceOfType(GMLPointTypeBinding.class);

        try {
            Point p = (Point) strategy.parse(point, node, null);
            fail("Should have thrown an exception");
        } catch (RuntimeException e) {
            //ok
        }
    }
}
