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
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

public class GMLLineStringMemberTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance association;
    ElementInstance geometry;

    protected void setUp() throws Exception {
        super.setUp();

        association =
                createElement(
                        GML.NAMESPACE,
                        "myAssociation",
                        new QName("http://www.opengis.net/gml", "LineStringMemberType"),
                        null);
        geometry = createElement(GML.NAMESPACE, "myGeometry", GML.LineStringType, null);
    }

    public void testWithGeometry() throws Exception {
        Node node =
                createNode(
                        association,
                        new ElementInstance[] {geometry},
                        new Object[] {
                            new GeometryFactory()
                                    .createLineString(
                                            new Coordinate[] {
                                                new Coordinate(0, 0), new Coordinate(1, 1)
                                            })
                        },
                        null,
                        null);
        GMLGeometryAssociationTypeBinding s1 =
                (GMLGeometryAssociationTypeBinding) getBinding(GML.GeometryAssociationType);
        Geometry g = (Geometry) s1.parse(association, node, null);

        GMLLineStringMemberTypeBinding s2 =
                (GMLLineStringMemberTypeBinding)
                        getBinding(new QName("http://www.opengis.net/gml", "LineStringMemberType"));
        g = (Geometry) s2.parse(association, node, g);

        assertNotNull(g);
        assertTrue(g instanceof LineString);
    }
}
