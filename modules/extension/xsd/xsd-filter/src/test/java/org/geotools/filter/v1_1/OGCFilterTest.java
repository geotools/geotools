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
package org.geotools.filter.v1_1;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringReader;
import java.nio.charset.StandardCharsets;
import javax.xml.parsers.DocumentBuilderFactory;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.PropertyIsEqualTo;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Parser;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class OGCFilterTest {
    @Test
    public void testEncode() throws Exception {
        FilterFactory f = CommonFactoryFinder.getFilterFactory(null);
        Filter filter = f.equal(f.property("testString"), f.literal(2), false);

        File file = File.createTempFile("filter", "xml");
        file.deleteOnExit();

        try (OutputStream output = new BufferedOutputStream(new FileOutputStream(file))) {
            Encoder encoder = new Encoder(new OGCConfiguration());

            encoder.encode(filter, OGC.PropertyIsEqualTo, output);
            output.flush();
        }

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        Document doc = docFactory.newDocumentBuilder().parse(file);

        Assert.assertEquals("ogc:PropertyIsEqualTo", doc.getDocumentElement().getNodeName());
        Assert.assertEquals(1, doc.getElementsByTagName("ogc:PropertyName").getLength());
        Assert.assertEquals(1, doc.getElementsByTagName("ogc:Literal").getLength());

        Element propertyName =
                (Element) doc.getElementsByTagName("ogc:PropertyName").item(0);
        Element literal = (Element) doc.getElementsByTagName("ogc:Literal").item(0);

        Assert.assertEquals("testString", propertyName.getFirstChild().getNodeValue());
        Assert.assertEquals("2", literal.getFirstChild().getNodeValue());
    }

    @Test
    public void testParse() throws Exception {
        Parser parser = new Parser(new OGCConfiguration());
        try (InputStream in = getClass().getResourceAsStream("test1.xml")) {

            if (in == null) {
                throw new FileNotFoundException(
                        getClass().getResource("test1.xml").toExternalForm());
            }

            Object thing = parser.parse(in);
            Assert.assertEquals(0, parser.getValidationErrors().size());

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

    @Test
    public void testDWithinWithoutUnitsParse() throws Exception {

        String xml = "<Filter>"
                + "<DWithin>"
                + "<PropertyName>the_geom</PropertyName>"
                + "<Point>"
                + "<coordinates>-74.817265,40.5296504</coordinates>"
                + "</Point>"
                + "<Distance>200</Distance>"
                + "</DWithin>"
                + "</Filter>";

        OGCConfiguration configuration = new OGCConfiguration();

        Parser parser = new Parser(configuration);
        DWithin filter = (DWithin) parser.parse(new ByteArrayInputStream(xml.getBytes(StandardCharsets.UTF_8)));
        Assert.assertNotNull(filter);

        // Asserting the Distance
        Assert.assertTrue(filter.getDistance() > 0);
        Double dist = filter.getDistance();
        Assert.assertEquals(200.0, dist, 0d);

        // Asserting the Distance Units
        Assert.assertNull(filter.getDistanceUnits());
    }

    @Test
    public void testBBOXValidateWithoutPropertyName() throws Exception {
        String xml = "<ogc:Filter xmlns:ogc='http://www.opengis.net/ogc'>"
                + "<ogc:BBOX>"
                + "<gml:Envelope xmlns:gml='http://www.opengis.net/gml'>"
                + "<gml:lowerCorner>36.986771000000005 -91.516129</gml:lowerCorner>"
                + "<gml:upperCorner>42.50936100000001 -87.507889</gml:upperCorner>"
                + "</gml:Envelope>"
                + "</ogc:BBOX>"
                + "</ogc:Filter>";

        Parser p = new Parser(new OGCConfiguration());
        p.validate(new StringReader(xml));

        Assert.assertTrue(p.getValidationErrors().isEmpty());
    }
}
