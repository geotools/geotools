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
import java.io.InputStream;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.geotools.xsd.StreamingParser;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.geotools.api.feature.simple.SimpleFeature;
import org.w3c.dom.Document;

public class GMLApplicationSchemaParsingTest {
    @Test
    public void testStreamFeatureWithIncorrectSchemaLocation() throws Exception {
        File schemaFile = File.createTempFile("test", "xsd");
        schemaFile.deleteOnExit();

        try (InputStream in = getClass().getResourceAsStream("feature.xml")) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            Document document = factory.newDocumentBuilder().parse(in);

            // update hte schema location
            document.getDocumentElement().removeAttribute("xsi:schemaLocation");

            // reserialize the document
            Transformer tx = TransformerFactory.newInstance().newTransformer();
            tx.transform(new DOMSource(document), new StreamResult(schemaFile));
        }
        try (FileInputStream in = new FileInputStream(schemaFile)) {

            GMLConfiguration configuration = new GMLConfiguration();
            StreamingParser parser = new StreamingParser(configuration, in, "//TestFeature");

            for (int i = 0; i < 3; i++) {
                SimpleFeature f = (SimpleFeature) parser.parse();

                Assert.assertNotNull(f);
            }

            Assert.assertNull(parser.parse());
        }
    }

    @Test
    public void testStreamPointWithIncorrectSchemaLocation() throws Exception {
        File schemaFile = File.createTempFile("test", "xsd");
        schemaFile.deleteOnExit();
        try (InputStream in = getClass().getResourceAsStream("feature.xml")) {

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            Document document = factory.newDocumentBuilder().parse(in);

            // update hte schema location
            document.getDocumentElement().removeAttribute("xsi:schemaLocation");

            // reserialize the document
            Transformer tx = TransformerFactory.newInstance().newTransformer();
            tx.transform(new DOMSource(document), new StreamResult(schemaFile));
        }
        try (InputStream in = new FileInputStream(schemaFile)) {

            GMLConfiguration configuration = new GMLConfiguration();
            StreamingParser parser = new StreamingParser(configuration, in, "//Point");

            for (int i = 0; i < 3; i++) {
                Point p = (Point) parser.parse();

                Assert.assertNotNull(p);
                Assert.assertEquals(i, p.getX(), 0d);
                Assert.assertEquals(i, p.getY(), 0d);
            }

            Assert.assertNull(parser.parse());
        }
    }

    @Test
    public void testWithCorrectSchemaLocation() throws Exception {
        File schemaFile = File.createTempFile("test", "xsd");
        schemaFile.deleteOnExit();
        try (InputStream in = getClass().getResourceAsStream("feature.xml")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);

            Document document = factory.newDocumentBuilder().parse(in);

            // update hte schema location
            String schemaLocation = getClass().getResource("test.xsd").toString();
            document.getDocumentElement()
                    .setAttribute("xsi:schemaLocation", TEST.NAMESPACE + " " + schemaLocation);

            // reserialize the document
            Transformer tx = TransformerFactory.newInstance().newTransformer();
            tx.transform(new DOMSource(document), new StreamResult(schemaFile));
        }
        try (InputStream in = new FileInputStream(schemaFile)) {

            StreamingParser parser =
                    new StreamingParser(new GMLConfiguration(), in, "//TestFeature");

            for (int i = 0; i < 3; i++) {
                SimpleFeature f = (SimpleFeature) parser.parse();
                Assert.assertNotNull(f);

                Assert.assertEquals(i + "", f.getID());
                Assert.assertEquals(i, ((Point) f.getDefaultGeometry()).getX(), 0d);
                Assert.assertEquals(i, ((Point) f.getDefaultGeometry()).getY(), 0d);
                Assert.assertEquals(i, ((Integer) f.getAttribute("count")).intValue());
            }

            Assert.assertNull(parser.parse());
        }
    }
}
