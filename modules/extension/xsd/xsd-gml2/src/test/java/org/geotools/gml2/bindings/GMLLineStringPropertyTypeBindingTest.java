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

import org.geotools.gml2.GML;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public class GMLLineStringPropertyTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance association;
    ElementInstance geometry;

    @Override
    @Before
    public void setUp() throws Exception {
        super.setUp();

        association = createElement(GML.NAMESPACE, "myLineStringProperty", GML.LineStringPropertyType, null);
        geometry = createElement(GML.NAMESPACE, "myLineString", GML.LineStringType, null);
    }

    @Test
    public void testWithGeometry() throws Exception {
        Node node = createNode(
                association,
                new ElementInstance[] {geometry},
                new Object[] {
                    new GeometryFactory().createLinearRing(new Coordinate[] {
                        new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2), new Coordinate(0, 0),
                    })
                },
                null,
                null);
        GMLGeometryAssociationTypeBinding s =
                (GMLGeometryAssociationTypeBinding) getBinding(GML.GeometryAssociationType);
        GMLLineStringPropertyTypeBinding s1 = (GMLLineStringPropertyTypeBinding) getBinding(GML.LineStringPropertyType);
        LineString p = (LineString) s1.parse(association, node, s.parse(association, node, null));
        assertNotNull(p);
    }
}
