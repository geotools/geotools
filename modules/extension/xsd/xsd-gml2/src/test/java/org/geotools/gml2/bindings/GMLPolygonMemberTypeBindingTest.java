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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.gml2.GML;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Polygon;

public class GMLPolygonMemberTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance association;
    ElementInstance geometry;

    public void setUp() throws Exception {
        super.setUp();

        association = createElement(GML.NAMESPACE, "myAssociation", GML.PolygonMemberType, null);
        geometry = createElement(GML.NAMESPACE, "myGeometry", GML.PolygonType, null);
    }

    @Test
    public void testWithGeometry() throws Exception {
        Node node =
                createNode(
                        association,
                        new ElementInstance[] {geometry},
                        new Object[] {
                            new GeometryFactory()
                                    .createPolygon(
                                            new GeometryFactory()
                                                    .createLinearRing(
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
        GMLGeometryAssociationTypeBinding s1 =
                (GMLGeometryAssociationTypeBinding) getBinding(GML.GeometryAssociationType);
        Geometry g = (Geometry) s1.parse(association, node, null);

        GMLPolygonMemberTypeBinding s2 =
                (GMLPolygonMemberTypeBinding) getBinding(GML.PolygonMemberType);
        g = (Geometry) s2.parse(association, node, g);

        assertNotNull(g);
        assertTrue(g instanceof Polygon);
    }
}
