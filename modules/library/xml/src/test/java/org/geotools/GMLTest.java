/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.namespace.QName;
import org.geotools.GML.Version;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.TestData;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * Check GML abilities
 *
 * @source $URL$
 */
public class GMLTest {
    /** Check if we can encode a SimpleFeatureType using GML2 */
    @Test
    public void testEncodeGML2XSD() throws Exception {
        SimpleFeatureType TYPE = DataUtilities.createType("Location", "geom:Point,name:String");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GML encode = new GML(Version.GML2);
        encode.setBaseURL(new URL("http://localhost/"));
        encode.encode(out, TYPE);

        out.close();

        String xsd = out.toString();
        assertTrue(xsd.indexOf("gml/2.1.2/feature.xsd") != -1);
    }

    @Test
    public void testEncodeGML2Legacy() throws Exception {
        SimpleFeatureType TYPE = DataUtilities.createType("Location", "geom:Point,name:String");

        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        WKTReader2 wkt = new WKTReader2();

        collection.add(
                SimpleFeatureBuilder.build(
                        TYPE, new Object[] {wkt.read("POINT (1 2)"), "name1"}, null));
        collection.add(
                SimpleFeatureBuilder.build(
                        TYPE, new Object[] {wkt.read("POINT (4 4)"), "name2"}, null));

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GML encode = new GML(Version.GML2);
        encode.setNamespace("Location", "http://localhost/Location.xsd");
        encode.setLegacy(true);
        encode.encode(out, collection);

        out.close();

        String gml = out.toString();
        assertTrue(gml.indexOf("<gml:Point>") != -1);
    }

    public void testEncodeGML2() throws Exception {
        // step one write out xsd file
        SimpleFeatureType TYPE = DataUtilities.createType("location", "geom:Point,name:String");
        File locationFile = new File("location.xsd");
        locationFile = locationFile.getCanonicalFile();

        locationFile.deleteOnExit();
        if (locationFile.exists()) {
            locationFile.delete();
        }
        locationFile.createNewFile();

        URL locationURL = locationFile.toURI().toURL();
        URL baseURL = locationFile.getParentFile().toURI().toURL();

        FileOutputStream out = new FileOutputStream(locationFile);

        GML encode = new GML(Version.GML2);
        encode.setBaseURL(baseURL);
        encode.setNamespace("location", locationURL.toExternalForm());
        encode.encode(out, TYPE);

        out.close();

        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        WKTReader2 wkt = new WKTReader2();

        collection.add(
                SimpleFeatureBuilder.build(
                        TYPE, new Object[] {wkt.read("POINT (1 2)"), "name1"}, null));
        collection.add(
                SimpleFeatureBuilder.build(
                        TYPE, new Object[] {wkt.read("POINT (4 4)"), "name2"}, null));

        ByteArrayOutputStream out2 = new ByteArrayOutputStream();
        GML encode2 = new GML(Version.GML2);
        encode2.setBaseURL(baseURL);
        encode2.setNamespace("location", "location.xsd");
        encode2.encode(out2, collection);

        out.close();

        String gml = out.toString();

        assertTrue(gml.indexOf("<gml:Point>") != -1);
    }

    @Test
    public void testEncodeWFS1_0FeatureCollection() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("feature");
        tb.setNamespaceURI("http://geotools.org");
        tb.add("geometry", Point.class);
        tb.add("name", String.class);

        SimpleFeatureType TYPE = tb.buildFeatureType();

        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        WKTReader2 wkt = new WKTReader2();
        collection.add(
                SimpleFeatureBuilder.build(
                        TYPE, new Object[] {wkt.read("POINT (1 2)"), "name1"}, null));
        collection.add(
                SimpleFeatureBuilder.build(
                        TYPE, new Object[] {wkt.read("POINT (4 4)"), "name2"}, null));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GML encode = new GML(Version.WFS1_0);
        encode.setNamespace("geotools", "http://geotools.org");

        encode.encode(out, collection);

        out.close();
        String gml = out.toString();
        assertTrue(gml.indexOf("<gml:Point>") != -1);
    }

    @Test
    public void testEncodeWFS1_1FeatureCollection() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("feature");
        tb.setNamespaceURI("http://geotools.org");
        tb.add("geometry", Point.class);
        tb.add("name", String.class);

        SimpleFeatureType TYPE = tb.buildFeatureType();

        DefaultFeatureCollection collection = new DefaultFeatureCollection();
        WKTReader2 wkt = new WKTReader2();
        collection.add(
                SimpleFeatureBuilder.build(
                        TYPE, new Object[] {wkt.read("POINT (1 2)"), "name1"}, null));
        collection.add(
                SimpleFeatureBuilder.build(
                        TYPE, new Object[] {wkt.read("POINT (4 4)"), "name2"}, null));

        ByteArrayOutputStream out = new ByteArrayOutputStream();

        GML encode = new GML(Version.WFS1_1);
        encode.setNamespace("geotools", "http://geotools.org");

        encode.encode(out, collection);

        out.close();
        String gml = out.toString();
        assertTrue(gml.indexOf("<gml:Point") != -1);
    }

    //
    // GML3
    //
    @Test
    public void testEncodeGML3XSD() throws Exception {
        SimpleFeatureType TYPE = DataUtilities.createType("location", "geom:Point,name:String");

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        GML encode = new GML(Version.GML3);
        encode.setBaseURL(new URL("http://localhost/"));
        encode.setNamespace("location", "http://localhost/location.xsd");
        encode.encode(out, TYPE);

        out.close();

        String xsd = out.toString();

        assertTrue(xsd.indexOf("gml/3.1.1/base/gml.xsd") != -1);
    }

    // WFS 1.1
    @Test
    public void testGML3ParseSimpleFeatureType() throws IOException {
        URL schemaLocation = TestData.getResource(this, "states.xsd");

        GML gml = new GML(Version.WFS1_1);
        gml.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);

        SimpleFeatureType featureType =
                gml.decodeSimpleFeatureType(
                        schemaLocation, new NameImpl("http://www.openplans.org/topp", "states"));

        assertNotNull(featureType);
        assertSame(DefaultGeographicCRS.WGS84, featureType.getCoordinateReferenceSystem());

        List<AttributeDescriptor> attributes = featureType.getAttributeDescriptors();
        List<String> names = new ArrayList<String>(attributes.size());
        for (AttributeDescriptor desc : attributes) {
            names.add(desc.getLocalName());
        }
        assertEquals("Expected number of Attributes", 23, names.size());
    }

    @Test
    public void testGML2ParseSimpleFeatureType() throws IOException {
        URL schemaLocation = TestData.getResource(this, "states_gml2.xsd");

        GML gml = new GML(Version.WFS1_0);
        gml.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);

        SimpleFeatureType featureType =
                gml.decodeSimpleFeatureType(
                        schemaLocation, new NameImpl("http://www.openplans.org/topp", "states"));

        assertNotNull(featureType);
        assertSame(DefaultGeographicCRS.WGS84, featureType.getCoordinateReferenceSystem());

        List<AttributeDescriptor> attributes = featureType.getAttributeDescriptors();
        List<String> names = new ArrayList<String>(attributes.size());
        for (AttributeDescriptor desc : attributes) {
            names.add(desc.getLocalName());
        }
        assertEquals("Expected number of Attributes", 23, names.size());
    }

    @Test
    public void testWFS1_0FeatureCollection() throws Exception {
        URL url = TestData.getResource(this, "states_gml2.xml");
        InputStream in = url.openStream();

        GML gml = new GML(Version.WFS1_0);
        SimpleFeatureCollection featureCollection = gml.decodeFeatureCollection(in);

        assertNotNull(featureCollection);
        assertEquals(49, featureCollection.size());
    }

    @Test
    public void testGML3FeatureCollection() throws Exception {
        Logger log = org.geotools.util.logging.Logging.getLogger("org.geotools.xml");
        Level level = log.getLevel();
        try {
            log.setLevel(Level.ALL);

            URL url = TestData.getResource(this, "states.gml");
            InputStream in = url.openStream();

            GML gml = new GML(Version.GML3);
            SimpleFeatureCollection featureCollection = gml.decodeFeatureCollection(in);

            assertNotNull(featureCollection);
            assertEquals(2, featureCollection.size());
        } finally {
            log.setLevel(level);
        }
    }

    @Test
    public void testWFS1_2FeatureCollection() throws Exception {
        URL url = TestData.getResource(this, "states.xml");
        InputStream in = url.openStream();

        GML gml = new GML(Version.WFS1_1);
        SimpleFeatureCollection featureCollection = gml.decodeFeatureCollection(in);

        assertNotNull(featureCollection);
        assertEquals(2, featureCollection.size());
    }

    @Test
    public void testGML3FeatureIterator() throws Exception {
        URL url = TestData.getResource(this, "states.xml");
        InputStream in = url.openStream();

        GML gml = new GML(Version.GML3);
        SimpleFeatureIterator iter = gml.decodeFeatureIterator(in);
        assertTrue(iter.hasNext());
        int count = 0;
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            assertNotNull(feature);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testGML3FeatureIteratorGeometryMorph() throws Exception {
        URL url = TestData.getResource(this, "states.xml");
        InputStream in = url.openStream();

        QName name = new QName("http://www.opengis.net/gml", "MultiSurface");

        GML gml = new GML(Version.GML3);
        SimpleFeatureIterator iter = gml.decodeFeatureIterator(in, name);
        assertTrue(iter.hasNext());
        int count = 0;
        while (iter.hasNext()) {
            SimpleFeature feature = iter.next();
            assertNotNull(feature);
            count++;
        }
        assertEquals(2, count);
    }

    @Test
    public void testGMLNoSchemaUnrelated() throws Exception {
        URL url = TestData.getResource(this, "states_noschema_unrelated_atts.xml");
        InputStream in = url.openStream();

        GML gml = new GML(Version.GML3);
        SimpleFeatureCollection fc = gml.decodeFeatureCollection(in, true);
        assertEquals(2, fc.size());
        SimpleFeatureType schema = fc.getSchema();
        assertNotNull(schema.getGeometryDescriptor());
        assertEquals(9, schema.getAttributeDescriptors().size());
        Map<String, Integer> attributePositions = getAttributePositionsMap(schema);
        // System.out.println(attributePositions);
        assertTrue(
                attributePositions
                        .keySet()
                        .containsAll(
                                Arrays.asList(
                                        "the_geom",
                                        "P_MALE",
                                        "STATE_NAME",
                                        "P_FEMALE",
                                        "STATE_FIPS",
                                        "SAMP_POP")));
        assertTrue(attributePositions.get("STATE_NAME") < attributePositions.get("STATE_FIPS"));
        assertTrue(attributePositions.get("P_MALE") < attributePositions.get("P_FEMALE"));
        assertTrue(attributePositions.get("P_FEMALE") < attributePositions.get("SAMP_POP"));
    }

    @Test
    public void testGMLNoSchemaRelated() throws Exception {
        URL url = TestData.getResource(this, "states_noschema_linked_atts.xml");
        InputStream in = url.openStream();

        GML gml = new GML(Version.GML3);
        SimpleFeatureCollection fc = gml.decodeFeatureCollection(in, true);
        assertEquals(2, fc.size());
        SimpleFeatureType schema = fc.getSchema();
        assertNotNull(schema.getGeometryDescriptor());
        Map<String, Integer> attributePositions = getAttributePositionsMap(schema);
        // System.out.println(attributePositions);
        assertTrue(
                attributePositions
                        .keySet()
                        .containsAll(
                                Arrays.asList("the_geom", "P_MALE", "STATE_NAME", "P_FEMALE")));
        assertTrue(attributePositions.get("STATE_NAME") < attributePositions.get("STATE_FIPS"));
        assertTrue(attributePositions.get("STATE_FIPS") < attributePositions.get("SUB_REGION"));
        assertTrue(attributePositions.get("STATE_NAME") < attributePositions.get("P_MALE"));
        assertTrue(attributePositions.get("P_MALE") < attributePositions.get("P_FEMALE"));
    }

    Map<String, Integer> getAttributePositionsMap(SimpleFeatureType schema) {
        Map<String, Integer> map = new LinkedHashMap<>();
        int idx = 0;
        for (AttributeDescriptor ad : schema.getAttributeDescriptors()) {
            map.put(ad.getLocalName(), idx++);
        }
        return map;
    }
}
