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

import junit.framework.TestCase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.spatial.DWithin;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v1_0.OGCConfiguration;
import org.geotools.xml.Encoder;
import org.geotools.xml.Parser;
import org.geotools.xml.Parser.Properties;


public class OGCFilterTest extends TestCase {
    public void testEncode() throws Exception {
        FilterFactory f = CommonFactoryFinder.getFilterFactory(null);
        Filter filter = f.equal(f.property("testString"), f.literal(2), false);

        File file = File.createTempFile("filter", "xml");
        file.deleteOnExit();

        OutputStream output = new BufferedOutputStream(new FileOutputStream(file));
        Encoder encoder = new Encoder(new OGCConfiguration());

        encoder.encode(filter, OGC.PropertyIsEqualTo, output);
        output.flush();
        output.close();

        DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
        docFactory.setNamespaceAware(true);

        Document doc = docFactory.newDocumentBuilder().parse(file);

        assertEquals("ogc:PropertyIsEqualTo", doc.getDocumentElement().getNodeName());
        assertEquals(1, doc.getElementsByTagName("ogc:PropertyName").getLength());
        assertEquals(1, doc.getElementsByTagName("ogc:Literal").getLength());

        Element propertyName = (Element) doc.getElementsByTagName("ogc:PropertyName").item(0);
        Element literal = (Element) doc.getElementsByTagName("ogc:Literal").item(0);

        assertEquals("testString", propertyName.getFirstChild().getNodeValue());
        assertEquals("2", literal.getFirstChild().getNodeValue());
    }

    public void testParse() throws Exception {
        Parser parser = new Parser(new OGCConfiguration());
        InputStream in = getClass().getResourceAsStream("test1.xml");

        if (in == null) {
            throw new FileNotFoundException(getClass().getResource("test1.xml").toExternalForm());
        }

        Object thing = parser.parse(in);
        assertEquals(0, parser.getValidationErrors().size());

        assertNotNull(thing);
        assertTrue(thing instanceof PropertyIsEqualTo);

        PropertyIsEqualTo equal = (PropertyIsEqualTo) thing;
        assertTrue(equal.getExpression1() instanceof PropertyName);
        assertTrue(equal.getExpression2() instanceof Literal);

        PropertyName name = (PropertyName) equal.getExpression1();
        assertEquals("testString", name.getPropertyName());

        Literal literal = (Literal) equal.getExpression2();
        assertEquals("2", literal.toString());
    }
    
    public void testDWithinParse() throws Exception {

        String xml = "<Filter>" +
            "<DWithin>" +
              "<PropertyName>the_geom</PropertyName>" + 
              "<Point>" +  
                  "<coordinates>-74.817265,40.5296504</coordinates>" + 
               "</Point>" +
               "<Distance units=\"km\">200</Distance>" +
             "</DWithin>" +
           "</Filter>";
        
        OGCConfiguration configuration = new OGCConfiguration();
        configuration.getProperties().add(Properties.IGNORE_SCHEMA_LOCATION);

        Parser parser = new Parser(configuration);
        DWithin filter = (DWithin) parser.parse(new ByteArrayInputStream(xml.getBytes()));
        assertNotNull(filter);
        
        //Asserting the Property Name
        assertNotNull(filter.getExpression1());
        PropertyName propName = (PropertyName) filter.getExpression1();
        String name = propName.getPropertyName();
        assertEquals("the_geom", name);
        
        //Asserting the Geometry
        assertNotNull(filter.getExpression2());
        Literal geom = (Literal) filter.getExpression2();
        assertEquals("POINT (-74.817265 40.5296504)", geom.toString());
        
        //Asserting the Distance
        assertTrue(filter.getDistance() > 0 );
        Double dist = filter.getDistance();
        assertEquals(200.0, dist);
        
        //Asserting the Distance Units
        assertNotNull(filter.getDistanceUnits());
        String unit = filter.getDistanceUnits();
        assertEquals("km", unit);
     }
}
