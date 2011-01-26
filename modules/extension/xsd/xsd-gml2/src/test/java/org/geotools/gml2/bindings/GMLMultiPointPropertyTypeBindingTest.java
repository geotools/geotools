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
import org.picocontainer.defaults.DefaultPicoContainer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.Point;


public class GMLMultiPointPropertyTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance association;
    ElementInstance geometry;

    protected void setUp() throws Exception {
        super.setUp();

        association = createElement(GML.NAMESPACE, "myMultiPointProperty",
                GML.MULTIPOINTPROPERTYTYPE, null);
        geometry = createElement(GML.NAMESPACE, "myMultiPoint", GML.MULTIPOINTTYPE, null);

        container = new DefaultPicoContainer();
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLGeometryAssociationTypeBinding.class);
        container.registerComponentImplementation(GMLMultiPointPropertyTypeBinding.class);
    }

    public void testWithGeometry() throws Exception {
        Point p1 = new GeometryFactory().createPoint(new Coordinate(0, 0));
        Point p2 = new GeometryFactory().createPoint(new Coordinate(1, 1));

        Node node = createNode(association, new ElementInstance[] { geometry },
                new Object[] { new GeometryFactory().createMultiPoint(new Point[] { p1, p2 }) },
                null, null);

        GMLGeometryAssociationTypeBinding s = (GMLGeometryAssociationTypeBinding) container
            .getComponentInstanceOfType(GMLGeometryAssociationTypeBinding.class);

        GMLMultiPointPropertyTypeBinding s1 = (GMLMultiPointPropertyTypeBinding) container
            .getComponentInstanceOfType(GMLMultiPointPropertyTypeBinding.class);

        MultiPoint p = (MultiPoint) s1.parse(association, node, s.parse(association, node, null));
        assertNotNull(p);
    }
}
