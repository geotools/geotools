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
import com.vividsolutions.jts.geom.MultiPolygon;


public class GMLMultiPolygonTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance mp;
    ElementInstance poly1;
    ElementInstance poly2;
    GeometryFactory gf;

    protected void setUp() throws Exception {
        super.setUp();

        poly1 = createElement(GML.NAMESPACE, "myPoly", GML.POLYGONMEMBERTYPE, null);
        poly2 = createElement(GML.NAMESPACE, "myPoly", GML.POLYGONMEMBERTYPE, null);
        mp = createElement(GML.NAMESPACE, "myPoly", GML.MULTIPOLYGONTYPE, null);

        container = new DefaultPicoContainer();
        container.registerComponentImplementation(GeometryFactory.class);
        container.registerComponentImplementation(GMLGeometryCollectionTypeBinding.class);
        container.registerComponentImplementation(GMLMultiPolygonTypeBinding.class);
    }

    public void test() throws Exception {
        Node node = createNode(mp, new ElementInstance[] { poly1, poly2 },
                new Object[] {
                    new GeometryFactory().createPolygon(new GeometryFactory().createLinearRing(
                            new Coordinate[] {
                                new Coordinate(0, 0), new Coordinate(1, 1), new Coordinate(2, 2),
                                new Coordinate(0, 0)
                            }), null),
                    new GeometryFactory().createPolygon(new GeometryFactory().createLinearRing(
                            new Coordinate[] {
                                new Coordinate(2, 2), new Coordinate(3, 3), new Coordinate(4, 4),
                                new Coordinate(2, 2)
                            }), null)
                }, null, null);

        GMLGeometryCollectionTypeBinding s1 = (GMLGeometryCollectionTypeBinding) container
            .getComponentInstanceOfType(GMLGeometryCollectionTypeBinding.class);
        GMLMultiPolygonTypeBinding s2 = (GMLMultiPolygonTypeBinding) container
            .getComponentInstanceOfType(GMLMultiPolygonTypeBinding.class);

        MultiPolygon mpoly = (MultiPolygon) s2.parse(mp, node, s1.parse(mp, node, null));

        assertNotNull(mpoly);
        assertEquals(mpoly.getNumGeometries(), 2);
    }
}
