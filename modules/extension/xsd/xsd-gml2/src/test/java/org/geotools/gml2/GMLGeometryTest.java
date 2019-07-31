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
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class GMLGeometryTest extends TestCase {
    Parser parser;

    protected void setUp() throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();

        spf.setNamespaceAware(true);

        Configuration configuration = new GMLConfiguration();

        parser = new Parser(configuration);
    }

    public void test() throws Exception {
        GeometryCollection gc =
                (GeometryCollection) parser.parse(getClass().getResourceAsStream("geometry.xml"));

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
