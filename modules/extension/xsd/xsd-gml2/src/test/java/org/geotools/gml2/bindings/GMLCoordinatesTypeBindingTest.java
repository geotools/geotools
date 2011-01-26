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
import org.geotools.xml.AttributeInstance;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xs.XS;
import org.picocontainer.MutablePicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;


public class GMLCoordinatesTypeBindingTest extends AbstractGMLBindingTest {
    AttributeInstance decimal;
    AttributeInstance ts;
    AttributeInstance cs;
    ElementInstance coordinates;
    MutablePicoContainer container;

    protected void setUp() throws Exception {
        super.setUp();

        decimal = createAtribute(GML.NAMESPACE, "decimal", XS.STRING, ".");
        ts = createAtribute(GML.NAMESPACE, "ts", XS.STRING, null);
        cs = createAtribute(GML.NAMESPACE, "cs", XS.STRING, null);
        coordinates = createElement(GML.NAMESPACE, "myCoordinates", GML.COORDTYPE, null);
        container = new DefaultPicoContainer();
        container.registerComponentInstance(CoordinateArraySequenceFactory.instance());
        container.registerComponentImplementation(GMLCoordinatesTypeBinding.class);
    }

    /*
     * Test method for 'org.geotools.gml2.strategies.GMLCoordinatesTypeBinding.parse(Element, Node[], Node[], Object)'
     */
    public void testParseDefaults() throws Exception {
        coordinates.setText("12.34,56.78 9.10,11.12 13.14,15.16");

        Node node = createNode(coordinates, null, null, null, null);
        GMLCoordinatesTypeBinding strategy = (GMLCoordinatesTypeBinding) container
            .getComponentInstanceOfType(GMLCoordinatesTypeBinding.class);

        CoordinateSequence c = (CoordinateSequence) strategy.parse(coordinates, node, null);
        assertNotNull(c);
        assertEquals(3, c.size());
        assertEquals(c.getCoordinate(0), new Coordinate(12.34, 56.78));
        assertEquals(c.getCoordinate(1), new Coordinate(9.10, 11.12));
        assertEquals(c.getCoordinate(2), new Coordinate(13.14, 15.16));
    }

    public void testParseNonDefaults() throws Exception {
        coordinates.setText("12.34:56.78;9.10:11.12;13.14:15.16");

        Node node = createNode(coordinates, null, null, new AttributeInstance[] { cs, ts },
                new String[] { ":", ";" });

        GMLCoordinatesTypeBinding strategy = (GMLCoordinatesTypeBinding) container
            .getComponentInstanceOfType(GMLCoordinatesTypeBinding.class);

        CoordinateSequence c = (CoordinateSequence) strategy.parse(coordinates, node, null);
        assertNotNull(c);
        assertEquals(3, c.size());
        assertEquals(c.getCoordinate(0), new Coordinate(12.34, 56.78));
        assertEquals(c.getCoordinate(1), new Coordinate(9.10, 11.12));
        assertEquals(c.getCoordinate(2), new Coordinate(13.14, 15.16));
    }

    /**
     * Data coming with multiple blanks or newlines shouldn't happen, but it does
     */
    public void testParseMultipleBlankCharacters() throws Exception {
        coordinates.setText("\n12.34,56.78\n 9.10,11.12\t\t\n 13.14,15.16\t\n  ");

        Node node = createNode(coordinates, null, null, null, null);
        GMLCoordinatesTypeBinding strategy = (GMLCoordinatesTypeBinding) container
            .getComponentInstanceOfType(GMLCoordinatesTypeBinding.class);

        CoordinateSequence c = (CoordinateSequence) strategy.parse(coordinates, node, null);
        assertNotNull(c);
        assertEquals(3, c.size());
        assertEquals(c.getCoordinate(0), new Coordinate(12.34, 56.78));
        assertEquals(c.getCoordinate(1), new Coordinate(9.10, 11.12));
        assertEquals(c.getCoordinate(2), new Coordinate(13.14, 15.16));
    }
}
