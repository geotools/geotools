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
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.picocontainer.defaults.DefaultPicoContainer;

public class GMLMultiPointTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance mp;
    ElementInstance point1;
    ElementInstance point2;
    GeometryFactory gf;

    protected void setUp() throws Exception {
        super.setUp();

        point1 = createElement(GML.NAMESPACE, "myPoint", GML.PointMemberType, null);
        point2 = createElement(GML.NAMESPACE, "myPoint", GML.PointMemberType, null);
        mp = createElement(GML.NAMESPACE, "myMultiPoint", GML.MultiPointType, null);

        container = new DefaultPicoContainer();
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLGeometryCollectionTypeBinding.class);
        container.registerComponentImplementation(GMLMultiPointTypeBinding.class);
    }

    public void test() throws Exception {
        Node node =
                createNode(
                        mp,
                        new ElementInstance[] {point1, point2},
                        new Object[] {
                            new GeometryFactory().createPoint(new Coordinate(0, 0)),
                            new GeometryFactory().createPoint(new Coordinate(1, 1))
                        },
                        null,
                        null);

        GMLGeometryCollectionTypeBinding s1 =
                (GMLGeometryCollectionTypeBinding)
                        container.getComponentInstanceOfType(
                                GMLGeometryCollectionTypeBinding.class);
        GMLMultiPointTypeBinding s2 =
                (GMLMultiPointTypeBinding)
                        container.getComponentInstanceOfType(GMLMultiPointTypeBinding.class);

        MultiPoint mpoint = (MultiPoint) s2.parse(mp, node, s1.parse(mp, node, null));

        assertNotNull(mpoint);
        assertEquals(mpoint.getNumGeometries(), 2);

        assertEquals(((Point) mpoint.getGeometryN(0)).getX(), 0d, 0d);
        assertEquals(((Point) mpoint.getGeometryN(0)).getY(), 0d, 0d);
        assertEquals(((Point) mpoint.getGeometryN(1)).getX(), 1d, 0d);
        assertEquals(((Point) mpoint.getGeometryN(1)).getY(), 1d, 0d);
    }
}
