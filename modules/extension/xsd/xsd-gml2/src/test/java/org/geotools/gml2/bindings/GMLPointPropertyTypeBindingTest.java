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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


public class GMLPointPropertyTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance association;
    ElementInstance geometry;

    protected void setUp() throws Exception {
        super.setUp();

        association = createElement(GML.NAMESPACE, "myPointProperty", GML.POINTPROPERTYTYPE, null);
        geometry = createElement(GML.NAMESPACE, "myPoint", GML.POINTTYPE, null);
    }

    public void testWithGeometry() throws Exception {
        Node node = createNode(association, new ElementInstance[] { geometry },
                new Object[] { new GeometryFactory().createPoint(new Coordinate(0, 0)) }, null, null);
        GMLGeometryAssociationTypeBinding s = (GMLGeometryAssociationTypeBinding) getBinding(GML.GEOMETRYASSOCIATIONTYPE);
        GMLPointPropertyTypeBinding s1 = (GMLPointPropertyTypeBinding) getBinding(GML.POINTPROPERTYTYPE);
        Point p = (Point) s1.parse(association, node, s.parse(association, node, null));
        assertNotNull(p);
    }
}
