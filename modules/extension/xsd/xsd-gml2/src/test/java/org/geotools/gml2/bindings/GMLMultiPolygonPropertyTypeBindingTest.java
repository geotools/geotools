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
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.picocontainer.defaults.DefaultPicoContainer;

public class GMLMultiPolygonPropertyTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance association;
    ElementInstance geometry;

    protected void setUp() throws Exception {
        super.setUp();

        association =
                createElement(
                        GML.NAMESPACE,
                        "myMultiPolygonProperty",
                        GML.MultiPolygonPropertyType,
                        null);
        geometry = createElement(GML.NAMESPACE, "myMultiPolygon", GML.MultiPointType, null);

        container = new DefaultPicoContainer();
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLGeometryAssociationTypeBinding.class);
        container.registerComponentImplementation(GMLMultiPolygonPropertyTypeBinding.class);
    }

    public void testWithGeometry() throws Exception {
        Polygon p1 =
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
                                null);
        Polygon p2 =
                new GeometryFactory()
                        .createPolygon(
                                new GeometryFactory()
                                        .createLinearRing(
                                                new Coordinate[] {
                                                    new Coordinate(2, 2),
                                                    new Coordinate(3, 3),
                                                    new Coordinate(4, 4),
                                                    new Coordinate(2, 2)
                                                }),
                                null);

        Node node =
                createNode(
                        association,
                        new ElementInstance[] {geometry},
                        new Object[] {
                            new GeometryFactory().createMultiPolygon(new Polygon[] {p1, p2})
                        },
                        null,
                        null);

        GMLGeometryAssociationTypeBinding s =
                (GMLGeometryAssociationTypeBinding)
                        container.getComponentInstanceOfType(
                                GMLGeometryAssociationTypeBinding.class);

        GMLMultiPolygonPropertyTypeBinding s1 =
                (GMLMultiPolygonPropertyTypeBinding)
                        container.getComponentInstanceOfType(
                                GMLMultiPolygonPropertyTypeBinding.class);

        MultiPolygon p =
                (MultiPolygon) s1.parse(association, node, s.parse(association, node, null));
        assertNotNull(p);
    }
}
