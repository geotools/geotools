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
package org.geotools.gml2;

import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.geotools.xml.Configuration;
import org.geotools.xml.Parser;

import com.vividsolutions.jts.geom.GeometryCollection;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;


public class GMLGeometryTest extends TestCase {
    Parser parser;

    protected void setUp() throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();

        spf.setNamespaceAware(true);

        Configuration configuration = new GMLConfiguration();

        parser = new Parser(configuration, getClass().getResourceAsStream("geometry.xml"));
    }

    public void test() throws Exception {
        GeometryCollection gc = (GeometryCollection) parser.parse();

        assertEquals(gc.getNumGeometries(), 3);

        Object o = gc.getGeometryN(0);
        assertNotNull(o);
        assertTrue(o instanceof Point);

        o = gc.getGeometryN(1);
        assertNotNull(o);
        assertTrue(o instanceof LineString);

        o = gc.getGeometryN(2);
        assertNotNull(o);
        assertTrue(o instanceof Polygon);
    }
}
