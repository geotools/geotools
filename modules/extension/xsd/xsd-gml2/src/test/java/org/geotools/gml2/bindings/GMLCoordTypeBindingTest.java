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

import java.math.BigDecimal;

import org.geotools.gml2.GML;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xs.XS;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;


public class GMLCoordTypeBindingTest extends AbstractGMLBindingTest {
    ElementInstance x;
    ElementInstance y;
    ElementInstance z;
    ElementInstance coordinate;
    MutablePicoContainer container;

    protected void setUp() throws Exception {
        super.setUp();

        x = createElement(GML.NAMESPACE, "X", XS.DECIMAL, "12.34");
        y = createElement(GML.NAMESPACE, "Y", XS.DECIMAL, "56.78");
        z = createElement(GML.NAMESPACE, "Z", XS.DECIMAL, "910.11");
        coordinate = createElement(GML.NAMESPACE, "myCoordinate", GML.COORDTYPE, null);

        container = new DefaultPicoContainer();
        container.registerComponentInstance(CoordinateArraySequenceFactory.instance());
        container.registerComponentImplementation(GMLCoordTypeBinding.class);
    }

    public void testParse1D() throws Exception {
        Node node = createNode(coordinate, new ElementInstance[] { x },
                new Object[] { new BigDecimal(12.34) }, null, null);

        GMLCoordTypeBinding strategy = (GMLCoordTypeBinding) container.getComponentInstanceOfType(GMLCoordTypeBinding.class);

        Coordinate c = (Coordinate) strategy.parse(coordinate, node, null);
        assertNotNull(c);
        assertEquals(c.x, 12.34, 0d);
    }

    public void testParse2D() throws Exception {
        Node node = createNode(coordinate, new ElementInstance[] { x, y },
                new Object[] { new BigDecimal(12.34), new BigDecimal(56.78) }, null, null);

        GMLCoordTypeBinding strategy = (GMLCoordTypeBinding) container.getComponentInstanceOfType(GMLCoordTypeBinding.class);

        Coordinate c = (Coordinate) strategy.parse(coordinate, node, null);
        assertNotNull(c);
        assertEquals(c.x, 12.34, 0d);
        assertEquals(c.y, 56.78, 0d);
    }

    public void testParse3D() throws Exception {
        Node node = createNode(coordinate, new ElementInstance[] { x, y, z },
                new Object[] { new BigDecimal(12.34), new BigDecimal(56.78), new BigDecimal(910.11) },
                null, null);
        GMLCoordTypeBinding strategy = (GMLCoordTypeBinding) container.getComponentInstanceOfType(GMLCoordTypeBinding.class);

        Coordinate c = (Coordinate) strategy.parse(coordinate, node, null);
        assertNotNull(c);
        assertEquals(c.x, 12.34, 0d);
        assertEquals(c.y, 56.78, 0d);
        assertEquals(c.z, 910.11, 0d);
    }
}
