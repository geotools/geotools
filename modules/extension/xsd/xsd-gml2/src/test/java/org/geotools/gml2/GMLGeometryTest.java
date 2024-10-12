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
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

public class GMLGeometryTest {
    Parser parser;

    @Before
    public void setUp() throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();

        spf.setNamespaceAware(true);

        Configuration configuration = new GMLConfiguration();

        parser = new Parser(configuration);
    }

    @Test
    public void test() throws Exception {
        GeometryCollection gc = (GeometryCollection) parser.parse(getClass().getResourceAsStream("geometry.xml"));

        Assert.assertEquals(gc.getNumGeometries(), 3);

        Object o = gc.getGeometryN(0);
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof Point);

        o = gc.getGeometryN(1);
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof LineString);

        o = gc.getGeometryN(2);
        Assert.assertNotNull(o);
        Assert.assertTrue(o instanceof Polygon);
    }
}
