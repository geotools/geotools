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

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.geotools.xml.Parser;
import org.geotools.xml.StreamingParser;
import org.opengis.feature.simple.SimpleFeature;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Point;


public class GMLApplicationSchemaParsingTest extends TestCase {
    public void testStreamFeatureWithIncorrectSchemaLocation()
        throws Exception {
        InputStream in = getClass().getResourceAsStream("feature.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        Document document = factory.newDocumentBuilder().parse(in);

        //update hte schema location
        document.getDocumentElement().removeAttribute("xsi:schemaLocation");

        //reserialize the document
        File schemaFile = File.createTempFile("test", "xsd");
        schemaFile.deleteOnExit();

        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform(new DOMSource(document), new StreamResult(schemaFile));

        in.close();
        in = new FileInputStream(schemaFile);

        GMLConfiguration configuration = new GMLConfiguration();
        configuration.getProperties().add(Parser.Properties.IGNORE_SCHEMA_LOCATION);
        configuration.getProperties().add(Parser.Properties.PARSE_UNKNOWN_ELEMENTS);

        StreamingParser parser = new StreamingParser(configuration, in, "//TestFeature");

        for (int i = 0; i < 3; i++) {
            SimpleFeature f = (SimpleFeature) parser.parse();

            assertNotNull(f);
        }

        assertNull(parser.parse());

        try {
            in.close();
        } catch (IOException e) {
            // nothing to do, but this throws an exception under java 6
        }
    }

    public void testStreamPointWithIncorrectSchemaLocation()
        throws Exception {
        InputStream in = getClass().getResourceAsStream("feature.xml");

        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        Document document = factory.newDocumentBuilder().parse(in);

        //update hte schema location
        document.getDocumentElement().removeAttribute("xsi:schemaLocation");

        //reserialize the document
        File schemaFile = File.createTempFile("test", "xsd");
        schemaFile.deleteOnExit();

        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform(new DOMSource(document), new StreamResult(schemaFile));

        in.close();
        in = new FileInputStream(schemaFile);

        GMLConfiguration configuration = new GMLConfiguration();
        configuration.getProperties().add(Parser.Properties.IGNORE_SCHEMA_LOCATION);
        configuration.getProperties().add(Parser.Properties.PARSE_UNKNOWN_ELEMENTS);

        StreamingParser parser = new StreamingParser(configuration, in, "//Point");

        for (int i = 0; i < 3; i++) {
            Point p = (Point) parser.parse();

            assertNotNull(p);
            assertEquals(i, p.getX(), 0d);
            assertEquals(i, p.getY(), 0d);
        }

        assertNull(parser.parse());

        try {
            in.close();
        } catch (IOException e) {
            // nothing to do, but this throws an exception under java 6
        }
    }

    public void testWithCorrectSchemaLocation() throws Exception {
        InputStream in = getClass().getResourceAsStream("feature.xml");
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);

        Document document = factory.newDocumentBuilder().parse(in);

        //update hte schema location
        String schemaLocation = getClass().getResource("test.xsd").toString();
        document.getDocumentElement()
                .setAttribute("xsi:schemaLocation", TEST.NAMESPACE + " " + schemaLocation);

        //reserialize the document
        File schemaFile = File.createTempFile("test", "xsd");
        schemaFile.deleteOnExit();

        Transformer tx = TransformerFactory.newInstance().newTransformer();
        tx.transform(new DOMSource(document), new StreamResult(schemaFile));

        in.close();
        in = new FileInputStream(schemaFile);

        StreamingParser parser = new StreamingParser(new GMLConfiguration(), in, "//TestFeature");

        for (int i = 0; i < 3; i++) {
            SimpleFeature f = (SimpleFeature) parser.parse();
            assertNotNull(f);

            assertEquals(i + "", f.getID());
            assertEquals(i, ((Point) f.getDefaultGeometry()).getX(), 0d);
            assertEquals(i, ((Point) f.getDefaultGeometry()).getY(), 0d);
            assertEquals(i, ((Integer) f.getAttribute("count")).intValue());
        }

        assertNull(parser.parse());

        try {
            in.close();
        } catch (IOException e) {
            // nothing to do, but this throws an exception under java 6
        }
    }
}
