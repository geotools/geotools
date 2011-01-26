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
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;


public class GMLLinearRingTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance ring;
    ElementInstance coord1;
    ElementInstance coord2;
    ElementInstance coord3;
    ElementInstance coord4;
    ElementInstance coord5;
    ElementInstance coords;
    MutablePicoContainer container;

    protected void setUp() throws Exception {
        super.setUp();

        ring = createElement(GML.NAMESPACE, "myLineString", GML.LINEARRINGTYPE, null);
        coord1 = createElement(GML.NAMESPACE, "coord", GML.COORDTYPE, null);
        coord2 = createElement(GML.NAMESPACE, "coord", GML.COORDTYPE, null);
        coord3 = createElement(GML.NAMESPACE, "coord", GML.COORDTYPE, null);
        coord4 = createElement(GML.NAMESPACE, "coord", GML.COORDTYPE, null);
        coord5 = createElement(GML.NAMESPACE, "coord", GML.COORDTYPE, null);

        coords = createElement(GML.NAMESPACE, "coordinates", GML.COORDINATESTYPE, null);

        container = new DefaultPicoContainer();
        container.registerComponentInstance(CoordinateArraySequenceFactory.instance());
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLLinearRingTypeBinding.class);
    }

    public void testCoordFour() throws Exception {
        Node node = createNode(ring, new ElementInstance[] { coord1, coord2, coord3, coord4 },
                new Object[] {
                    createCoordinateSequence(new Coordinate(1, 2)),
                    createCoordinateSequence(new Coordinate(3, 4)),
                    createCoordinateSequence(new Coordinate(5, 6)),
                    createCoordinateSequence(new Coordinate(1, 2))
                }, null, null);

        GMLLinearRingTypeBinding s = (GMLLinearRingTypeBinding) container.getComponentInstanceOfType(GMLLinearRingTypeBinding.class);
        LinearRing linearRing = (LinearRing) s.parse(ring, node, null);

        assertNotNull(linearRing);
        assertEquals(linearRing.getNumPoints(), 4);
        assertEquals(linearRing.getPointN(0).getX(), 1d, 0);
        assertEquals(linearRing.getPointN(0).getY(), 2d, 0);
        assertEquals(linearRing.getPointN(1).getX(), 3d, 0);
        assertEquals(linearRing.getPointN(1).getY(), 4d, 0);
        assertEquals(linearRing.getPointN(2).getX(), 5d, 0);
        assertEquals(linearRing.getPointN(2).getY(), 6d, 0);
        assertEquals(linearRing.getPointN(3).getX(), 1d, 0);
        assertEquals(linearRing.getPointN(3).getY(), 2d, 0);
    }

    public void testCoordLessThanFour() throws Exception {
        Node node = createNode(ring, new ElementInstance[] { coord1, coord2, coord3 },
                new Object[] {
                    createCoordinateSequence(new Coordinate(1, 2)),
                    createCoordinateSequence(new Coordinate(3, 4)),
                    createCoordinateSequence(new Coordinate(1, 2))
                }, null, null);

        GMLLinearRingTypeBinding s = (GMLLinearRingTypeBinding) container.getComponentInstanceOfType(GMLLinearRingTypeBinding.class);

        try {
            LinearRing linearRing = (LinearRing) s.parse(ring, node, null);
            fail("Should have thrown an exception");
        } catch (Exception e) {
            //ok
        }
    }

    public void testCoordMoreThanFour() throws Exception {
        Node node = createNode(ring,
                new ElementInstance[] { coord1, coord2, coord3, coord4, coord5 },
                new Object[] {
                    createCoordinateSequence(new Coordinate(1, 2)),
                    createCoordinateSequence(new Coordinate(3, 4)),
                    createCoordinateSequence(new Coordinate(5, 6)),
                    createCoordinateSequence(new Coordinate(7, 8)),
                    createCoordinateSequence(new Coordinate(1, 2))
                }, null, null);

        GMLLinearRingTypeBinding s = (GMLLinearRingTypeBinding) container.getComponentInstanceOfType(GMLLinearRingTypeBinding.class);
        LinearRing linearRing = (LinearRing) s.parse(ring, node, null);

        assertNotNull(linearRing);
        assertEquals(linearRing.getNumPoints(), 5);
        assertEquals(linearRing.getPointN(0).getX(), 1d, 0);
        assertEquals(linearRing.getPointN(0).getY(), 2d, 0);
        assertEquals(linearRing.getPointN(1).getX(), 3d, 0);
        assertEquals(linearRing.getPointN(1).getY(), 4d, 0);
        assertEquals(linearRing.getPointN(2).getX(), 5d, 0);
        assertEquals(linearRing.getPointN(2).getY(), 6d, 0);
        assertEquals(linearRing.getPointN(3).getX(), 7d, 0);
        assertEquals(linearRing.getPointN(3).getY(), 8d, 0);
        assertEquals(linearRing.getPointN(4).getX(), 1d, 0);
        assertEquals(linearRing.getPointN(4).getY(), 2d, 0);
    }

    public void testCoordinatesFour() throws Exception {
        Node node = createNode(ring, new ElementInstance[] { coords },
                new Object[] {
                    createCoordinateSequence(
                        new Coordinate[] {
                            new Coordinate(1, 2), new Coordinate(3, 4), new Coordinate(5, 6),
                            new Coordinate(1, 2)
                        }),
                }, null, null);

        GMLLinearRingTypeBinding s = (GMLLinearRingTypeBinding) container.getComponentInstanceOfType(GMLLinearRingTypeBinding.class);

        LinearRing linearRing = (LinearRing) s.parse(ring, node, null);
        assertNotNull(linearRing);
        assertEquals(linearRing.getNumPoints(), 4);
        assertEquals(linearRing.getPointN(0).getX(), 1d, 0);
        assertEquals(linearRing.getPointN(0).getY(), 2d, 0);
        assertEquals(linearRing.getPointN(1).getX(), 3d, 0);
        assertEquals(linearRing.getPointN(1).getY(), 4d, 0);
        assertEquals(linearRing.getPointN(2).getX(), 5d, 0);
        assertEquals(linearRing.getPointN(2).getY(), 6d, 0);
        assertEquals(linearRing.getPointN(3).getX(), 1d, 0);
        assertEquals(linearRing.getPointN(3).getY(), 2d, 0);
    }

    public void testCoordinatesLessThanFour() throws Exception {
        Node node = createNode(ring, new ElementInstance[] { coords },
                new Object[] {
                    createCoordinateSequence(
                        new Coordinate[] {
                            new Coordinate(1, 2), new Coordinate(3, 4), new Coordinate(1, 2)
                        }),
                }, null, null);

        GMLLinearRingTypeBinding s = (GMLLinearRingTypeBinding) container.getComponentInstanceOfType(GMLLinearRingTypeBinding.class);

        LinearRing linearRing;

        try {
            linearRing = (LinearRing) s.parse(ring, node, null);
            fail("Should have thrown an exception with less then 4 points");
        } catch (Exception e) {
            //ok
        }
    }

    public void testCoordinatesMoreThanFour() throws Exception {
        Node node = createNode(ring, new ElementInstance[] { coords },
                new Object[] {
                    createCoordinateSequence(
                        new Coordinate[] {
                            new Coordinate(1, 2), new Coordinate(3, 4), new Coordinate(5, 6),
                            new Coordinate(7, 8), new Coordinate(1, 2)
                        }),
                }, null, null);

        GMLLinearRingTypeBinding s = (GMLLinearRingTypeBinding) container.getComponentInstanceOfType(GMLLinearRingTypeBinding.class);

        LinearRing linearRing = (LinearRing) s.parse(ring, node, null);
        assertNotNull(linearRing);
        assertEquals(linearRing.getNumPoints(), 5);
        assertEquals(linearRing.getPointN(0).getX(), 1d, 0);
        assertEquals(linearRing.getPointN(0).getY(), 2d, 0);
        assertEquals(linearRing.getPointN(1).getX(), 3d, 0);
        assertEquals(linearRing.getPointN(1).getY(), 4d, 0);
        assertEquals(linearRing.getPointN(2).getX(), 5d, 0);
        assertEquals(linearRing.getPointN(2).getY(), 6d, 0);
        assertEquals(linearRing.getPointN(3).getX(), 7d, 0);
        assertEquals(linearRing.getPointN(3).getY(), 8d, 0);
        assertEquals(linearRing.getPointN(4).getX(), 1d, 0);
        assertEquals(linearRing.getPointN(4).getY(), 2d, 0);
    }
}
