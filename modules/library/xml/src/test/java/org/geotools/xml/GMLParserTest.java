/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.io.File;
import java.net.URI;
import java.util.HashMap;
import java.util.logging.Level;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import org.geotools.TestData;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.xml.gml.GMLFeatureCollection;
import org.geotools.xml.gml.GMLSchema;
import org.geotools.xml.schema.Schema;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

/** @author dzwiers www.refractions.net */
public class GMLParserTest {
    @Test
    public void testSchema() {
        Schema s = SchemaFactory.getInstance(GMLSchema.NAMESPACE);
        Assert.assertNotNull(s);
    }

    @Test
    public void testParseEmptyCollectionFeatures() throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        SAXParser parser = spf.newSAXParser();

        String path = "xml/fme/empty-collection/lakes.xml";
        File f = TestData.copy(this, path);
        TestData.copy(this, "xml/fme/empty-collection/lakes.xsd");
        URI u = f.toURI();

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u, null);
        XMLSAXHandler.setLogLevel(Level.WARNING);
        XSISAXHandler.setLogLevel(Level.WARNING);
        XMLElementHandler.setLogLevel(Level.WARNING);
        XSIElementHandler.setLogLevel(Level.WARNING);

        parser.parse(f, xmlContentHandler);

        Object doc = xmlContentHandler.getDocument();
        Assert.assertNotNull("Document missing", doc);

        SimpleFeatureCollection collection = (SimpleFeatureCollection) doc;
        Assert.assertEquals(0, collection.size());
    }

    @Test
    @Ignore
    public void testOneFeature() throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        SAXParser parser = spf.newSAXParser();

        String path = "xml/geoserver/oneFeature.xml";
        File f = TestData.copy(this, path);
        TestData.copy(this, "xml/geoserver/roadSchema.xsd");
        TestData.copy(this, "xml/wfs/WFS-basic.xsd");
        URI u = f.toURI();

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u, null);
        XMLSAXHandler.setLogLevel(Level.FINEST);
        XSISAXHandler.setLogLevel(Level.FINEST);
        XMLElementHandler.setLogLevel(Level.FINEST);
        XSIElementHandler.setLogLevel(Level.FINEST);

        parser.parse(f, xmlContentHandler);

        Object doc = xmlContentHandler.getDocument();
        Assert.assertNotNull("Document missing", doc);
        //            System.out.println(doc);

        checkFeatureCollection((SimpleFeatureCollection) doc);
    }

    @Test
    @Ignore
    public void testMoreFeatures() {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            spf.setValidating(false);

            SAXParser parser = spf.newSAXParser();

            String path = "xml/geoserver/roads.xml";
            File f = TestData.copy(this, path);
            TestData.copy(this, "xml/geoserver/roadSchema.xsd");
            TestData.copy(this, "xml/wfs/WFS-basic.xsd");
            URI u = f.toURI();

            XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u, null);
            XMLSAXHandler.setLogLevel(Level.WARNING);
            XSISAXHandler.setLogLevel(Level.WARNING);
            XMLElementHandler.setLogLevel(Level.WARNING);
            XSIElementHandler.setLogLevel(Level.WARNING);

            parser.parse(f, xmlContentHandler);

            Object doc = xmlContentHandler.getDocument();
            Assert.assertNotNull("Document missing", doc);
            //            System.out.println(doc);

            checkFeatureCollection((SimpleFeatureCollection) doc);

        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testPatternSchema() {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            spf.setValidating(false);

            SAXParser parser = spf.newSAXParser();

            String path = "xml/patternfacet/states.xml";
            File f = TestData.copy(this, path);
            TestData.copy(this, "xml/patternfacet/states.xsd");
            TestData.copy(this, "xml/wfs/WFS-basic.xsd");
            URI u = f.toURI();

            XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u, null);
            XMLSAXHandler.setLogLevel(Level.WARNING);
            XSISAXHandler.setLogLevel(Level.WARNING);
            XMLElementHandler.setLogLevel(Level.WARNING);
            XSIElementHandler.setLogLevel(Level.WARNING);

            parser.parse(f, xmlContentHandler);

            Object doc = xmlContentHandler.getDocument();
            Assert.assertNotNull("Document missing", doc);

            checkFeatureCollection((SimpleFeatureCollection) doc);

        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testFMERoadsFeatures() throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        SAXParser parser = spf.newSAXParser();

        String path = "xml/fme/roads/roads.xml";
        File f = TestData.copy(this, path);
        TestData.copy(this, "xml/fme/roads/roads.xsd");
        URI u = f.toURI();

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u, null);
        XMLSAXHandler.setLogLevel(Level.WARNING);
        XSISAXHandler.setLogLevel(Level.WARNING);
        XMLElementHandler.setLogLevel(Level.WARNING);
        XSIElementHandler.setLogLevel(Level.WARNING);

        parser.parse(f, xmlContentHandler);

        Object doc = xmlContentHandler.getDocument();
        Assert.assertNotNull("Document missing", doc);
        //            System.out.println(doc);

        checkFeatureCollection((SimpleFeatureCollection) doc);
    }

    @Test
    public void testFMELakesFeatures() throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        SAXParser parser = spf.newSAXParser();

        String path = "xml/fme/lakes/lakes.xml";
        File f = TestData.copy(this, path);
        TestData.copy(this, "xml/fme/lakes/lakes.xsd");
        URI u = f.toURI();

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u, null);
        XMLSAXHandler.setLogLevel(Level.WARNING);
        XSISAXHandler.setLogLevel(Level.WARNING);
        XMLElementHandler.setLogLevel(Level.WARNING);
        XSIElementHandler.setLogLevel(Level.WARNING);

        parser.parse(f, xmlContentHandler);

        Object doc = xmlContentHandler.getDocument();
        Assert.assertNotNull("Document missing", doc);
        //            System.out.println(doc);

        checkFeatureCollection((SimpleFeatureCollection) doc);
    }

    private void checkFeatureCollection(SimpleFeatureCollection doc) {

        // remaining slot (s) should be feature(s)
        Assert.assertTrue("Requires atleast one feature", doc.size() > 0); // bbox + feature
        try (SimpleFeatureIterator i = doc.features()) {
            int j = 1;
            while (i.hasNext()) {
                SimpleFeature ft = i.next();
                Assert.assertNotNull("Feature #" + j + " is null", ft);
                //            assertNotNull("Feature #"+j+" missing crs
                // ",ft.getFeatureType().getDefaultGeometry().getCoordinateSystem());
                //            System.out.println("Feature "+j+" : "+ft);
                j++;
            }
        }
    }

    @Test
    @Ignore
    public void testOneFeatureWrite() {

        try {
            String path = "xml/geoserver/oneFeature.xml";
            File f = TestData.copy(this, path);
            TestData.copy(this, "xml/geoserver/roadSchema.xsd");
            TestData.copy(this, "xml/wfs/WFS-basic.xsd");

            GMLFeatureCollection doc =
                    (GMLFeatureCollection) DocumentFactory.getInstance(f.toURI(), null, Level.WARNING);
            Assert.assertNotNull("Document missing", doc);

            Schema s = SchemaFactory.getInstance(new URI("http://www.openplans.org/topp"));

            path = "oneFeature_out.xml";
            f = new File(f.getParentFile(), path);
            if (f.exists()) f.delete();
            f.createNewFile();

            Assert.assertNotNull("Bounds exists", doc.getBounds());
            DocumentWriter.writeDocument(doc, s, f, null);

            //        doc =
            // (GMLFeatureCollection)DocumentFactory.getInstance(f.toURI(),null,Level.WARNING);
            //        assertNotNull("New Document missing", doc);
            //
            //        assertTrue("file was not created +f",f.exists());
            // System.out.println(f);
        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(Level.INFO, "", e);
            Assert.fail(e.toString());
        }
    }

    @Test
    @Ignore
    public void testOneFeatureWriteWithHints() {

        try {
            String path = "xml/geoserver/oneFeature.xml";

            File f = TestData.copy(this, path);
            TestData.copy(this, "xml/geoserver/roadSchema.xsd");
            TestData.copy(this, "xml/wfs/WFS-basic.xsd");

            GMLFeatureCollection doc =
                    (GMLFeatureCollection) DocumentFactory.getInstance(f.toURI(), null, Level.WARNING);
            Assert.assertNotNull("Document missing", doc);

            Schema s = SchemaFactory.getInstance(new URI("http://www.openplans.org/topp"));

            path = "oneFeature_out_hints.xml";
            f = new File(f.getParentFile(), path);
            if (f.exists()) f.delete();
            f.createNewFile();

            HashMap<String, Object> hints = new HashMap<>();
            hints.put(
                    DocumentWriter.SCHEMA_ORDER,
                    new String[] {"http://www.opengis.net/wfs", "http://www.openplans.org/topp"});
            Assert.assertNotNull("Bounds exists", doc.getBounds());
            DocumentWriter.writeDocument(doc, s, f, hints);

            //        doc =
            // (GMLFeatureCollection)DocumentFactory.getInstance(f.toURI(),null,Level.WARNING);
            //        assertNotNull("New Document missing", doc);
            //
            //        assertTrue("file was not created +f",f.exists());
            // System.out.println(f);
        } catch (Throwable e) {
            java.util.logging.Logger.getGlobal().log(Level.INFO, "", e);
            Assert.fail(e.toString());
        }
    }

    @Test
    public void testProblemFeatures() {
        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            spf.setNamespaceAware(true);
            spf.setValidating(false);

            SAXParser parser = spf.newSAXParser();

            String path = "xml/iba-gml-bad.xml";
            File f = TestData.copy(this, path);
            URI u = f.toURI();

            XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u, null);
            XMLSAXHandler.setLogLevel(Level.WARNING);
            XSISAXHandler.setLogLevel(Level.WARNING);
            XMLElementHandler.setLogLevel(Level.WARNING);
            XSIElementHandler.setLogLevel(Level.WARNING);

            parser.parse(f, xmlContentHandler);

            Object doc = xmlContentHandler.getDocument();
            Assert.assertNotNull("Document missing", doc);
            //           System.out.println(doc);

            checkFeatureCollection((SimpleFeatureCollection) doc);
            Assert.fail("Didn't catch an exception :(");
        } catch (Exception e) {
            // fine, they were expected
        }
    }
}
