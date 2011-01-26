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
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


public class GMLMultiGeometryPropertyTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance association;
    ElementInstance geometry;

    protected void setUp() throws Exception {
        super.setUp();

        association = createElement(GML.NAMESPACE, "myMultiGeometryProperty",
                GML.GEOMETRYPROPERTYTYPE, null);
        geometry = createElement(GML.NAMESPACE, "myGeometryCollection", GML.GEOMETRYCOLLECTIONTYPE,
                null);
    }

    public void testWithGeometry() throws Exception {
        Point p1 = new GeometryFactory().createPoint(new Coordinate(0, 0));
        Point p2 = new GeometryFactory().createPoint(new Coordinate(1, 1));

        Node node = createNode(association, new ElementInstance[] { geometry },
                new Object[] {
                    new GeometryFactory().createGeometryCollection(new Geometry[] { p1, p2 })
                }, null, null);

        GMLGeometryAssociationTypeBinding s = (GMLGeometryAssociationTypeBinding) getBinding(GML.GEOMETRYASSOCIATIONTYPE);
        GMLMultiGeometryPropertyTypeBinding s1 = (GMLMultiGeometryPropertyTypeBinding) getBinding(GML.MULTIGEOMETRYPROPERTYTYPE);

        GeometryCollection p = (GeometryCollection) s1.parse(association, node,
                s.parse(association, node, null));
        assertNotNull(p);
    }
}
