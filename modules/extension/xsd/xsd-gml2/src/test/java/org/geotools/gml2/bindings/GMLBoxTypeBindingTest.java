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
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;

public class GMLBoxTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance box;
    ElementInstance coord1;
    ElementInstance coord2;
    ElementInstance coord3;
    ElementInstance coords;

    protected void setUp() throws Exception {
        super.setUp();

        box = createElement(GML.NAMESPACE, "myBox", GML.BoxType, null);
        coord1 = createElement(GML.NAMESPACE, "coord", GML.CoordType, null);
        coord2 = createElement(GML.NAMESPACE, "coord", GML.CoordType, null);
        coord3 = createElement(GML.NAMESPACE, "coord", GML.CoordType, null);
        coords = createElement(GML.NAMESPACE, "coordinates", GML.CoordinatesType, null);
    }

    public void testTwoCoord() throws Exception {
        Node node =
                createNode(
                        box,
                        new ElementInstance[] {coord1, coord2},
                        new Object[] {new Coordinate(1, 2), new Coordinate(3, 4)},
                        null,
                        null);

        GMLBoxTypeBinding s = (GMLBoxTypeBinding) getBinding(GML.BoxType);
        Envelope e = (Envelope) s.parse(box, node, null);
        assertNotNull(e);
        assertEquals(e.getMinX(), 1d, 0d);
        assertEquals(e.getMinY(), 2d, 0d);
        assertEquals(e.getMaxX(), 3d, 0d);
        assertEquals(e.getMaxY(), 4d, 0d);
    }

    public void testSingleCoord() throws Exception {
        Node node =
                createNode(
                        box,
                        new ElementInstance[] {coord1},
                        new Object[] {createCoordinateSequence(new Coordinate(1, 2))},
                        null,
                        null);

        GMLBoxTypeBinding s = (GMLBoxTypeBinding) getBinding(GML.BoxType);

        try {
            Envelope e = (Envelope) s.parse(box, node, null);
            fail("< 2 coordinate envelope should have thrown exception");
        } catch (Exception e) {
            // ok
        }
    }

    public void testMultiCoord() throws Exception {
        Node node =
                createNode(
                        box,
                        new ElementInstance[] {coord1, coord2, coord3},
                        new Object[] {
                            createCoordinateSequence(new Coordinate(1, 2)),
                            createCoordinateSequence(new Coordinate(3, 4)),
                            createCoordinateSequence(new Coordinate(5, 6))
                        },
                        null,
                        null);

        GMLBoxTypeBinding s = (GMLBoxTypeBinding) getBinding(GML.BoxType);

        try {
            Envelope e = (Envelope) s.parse(box, node, null);
            fail("> 2 coordinate envelope should have thrown exception");
        } catch (Exception e) {
            // ok
        }
    }

    public void testTwoCoordinates() throws Exception {
        Node node =
                createNode(
                        box,
                        new ElementInstance[] {coords},
                        new Object[] {
                            createCoordinateSequence(
                                    new Coordinate[] {new Coordinate(1, 2), new Coordinate(3, 4)})
                        },
                        null,
                        null);

        GMLBoxTypeBinding s = (GMLBoxTypeBinding) getBinding(GML.BoxType);
        Envelope e = (Envelope) s.parse(box, node, null);
        assertNotNull(e);
        assertEquals(e.getMinX(), 1d, 0d);
        assertEquals(e.getMinY(), 2d, 0d);
        assertEquals(e.getMaxX(), 3d, 0d);
        assertEquals(e.getMaxY(), 4d, 0d);
    }

    public void testSingleCoordinates() throws Exception {
        Node node =
                createNode(
                        box,
                        new ElementInstance[] {coords},
                        new Object[] {createCoordinateSequence(new Coordinate(1, 2))},
                        null,
                        null);

        GMLBoxTypeBinding s = (GMLBoxTypeBinding) getBinding(GML.BoxType);

        try {
            Envelope e = (Envelope) s.parse(box, node, null);
            fail("< 2 coordinate envelope should have thrown exception");
        } catch (Exception e) {
            // ok
        }
    }

    public void testMultiCoordinates() throws Exception {
        Node node =
                createNode(
                        box,
                        new ElementInstance[] {coords},
                        new Object[] {
                            createCoordinateSequence(
                                    new Coordinate[] {
                                        new Coordinate(1, 2),
                                        new Coordinate(3, 4),
                                        new Coordinate(5, 6)
                                    })
                        },
                        null,
                        null);

        GMLBoxTypeBinding s = (GMLBoxTypeBinding) getBinding(GML.BoxType);

        try {
            Envelope e = (Envelope) s.parse(box, node, null);
            fail("> 2 coordinate envelope should have thrown exception");
        } catch (Exception e) {
            // ok
        }
    }
}
