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
package org.geotools.filter.v1_0;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.xsd.Configuration;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class OGCFilterTest {
    Parser parser;

    @Before
    public void setUp() throws Exception {

        Configuration configuration = new OGCConfiguration();
        parser = new Parser(configuration);
    }

    @Test
    public void testRun() throws Exception {
        Object thing = parser.parse(getClass().getResourceAsStream("test1.xml"));
        Assert.assertNotNull(thing);
        Assert.assertTrue(thing instanceof PropertyIsEqualTo);

        PropertyIsEqualTo equal = (PropertyIsEqualTo) thing;
        Assert.assertTrue(equal.getExpression1() instanceof PropertyName);
        Assert.assertTrue(equal.getExpression2() instanceof Literal);

        PropertyName name = (PropertyName) equal.getExpression1();
        Assert.assertEquals("testString", name.getPropertyName());

        Literal literal = (Literal) equal.getExpression2();
        Assert.assertEquals("2", literal.toString());
    }

    @Test
    public void testLax() throws Exception {
        String xml = "<Filter>"
                + "  <PropertyIsEqualTo>"
                + "    <PropertyName>foo</PropertyName>"
                + "    <Literal>bar</Literal>"
                + "  </PropertyIsEqualTo>"
                + "</Filter>";

        Parser parser = new Parser(new OGCConfiguration());
        parser.setStrict(false);
        Filter filter = (Filter) parser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        Assert.assertNotNull(filter);
    }

    @Test
    public void testLiteralWithEntity() throws Exception {
        String xml = "<Filter>"
                + "  <PropertyIsEqualTo>"
                + "    <PropertyName>foo</PropertyName>"
                + "    <Literal>bar &gt; 10 and &lt; 20</Literal>"
                + "  </PropertyIsEqualTo>"
                + "</Filter>";

        Parser parser = new Parser(new OGCConfiguration());
        parser.setStrict(false);
        Filter filter = (Filter) parser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        Assert.assertNotNull(filter);
        PropertyIsEqualTo equal = (PropertyIsEqualTo) filter;
        PropertyName pn = (PropertyName) equal.getExpression1();
        Assert.assertEquals("foo", pn.getPropertyName());
        Literal literal = (Literal) equal.getExpression2();
        Assert.assertEquals("bar > 10 and < 20", literal.getValue());
    }

    @Test
    public void testDWithinParse() throws Exception {

        String xml = "<Filter>"
                + "<DWithin>"
                + "<PropertyName>the_geom</PropertyName>"
                + "<Point>"
                + "<coordinates>-74.817265,40.5296504</coordinates>"
                + "</Point>"
                + "<Distance units=\"km\">200</Distance>"
                + "</DWithin>"
                + "</Filter>";

        OGCConfiguration configuration = new OGCConfiguration();

        Parser parser = new Parser(configuration);
        DWithin filter = (DWithin) parser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        Assert.assertNotNull(filter);

        // Asserting the Property Name
        Assert.assertNotNull(filter.getExpression1());
        PropertyName propName = (PropertyName) filter.getExpression1();
        String name = propName.getPropertyName();
        Assert.assertEquals("the_geom", name);

        // Asserting the Geometry
        Assert.assertNotNull(filter.getExpression2());
        Literal geom = (Literal) filter.getExpression2();
        Assert.assertEquals("POINT (-74.817265 40.5296504)", geom.toString());

        // Asserting the Distance
        Assert.assertTrue(filter.getDistance() > 0);
        Double dist = filter.getDistance();
        Assert.assertEquals(200.0, dist, 0d);

        // Asserting the Distance Units
        Assert.assertNotNull(filter.getDistanceUnits());
        String unit = filter.getDistanceUnits();
        Assert.assertEquals("km", unit);
    }
}
