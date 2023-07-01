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
package org.geotools.kml;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.kml.bindings.DocumentTypeBinding;
import org.geotools.styling.FeatureTypeStyle;
import org.geotools.xsd.Encoder;
import org.geotools.xsd.Parser;
import org.geotools.xsd.StreamingParser;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.geotools.api.feature.simple.SimpleFeature;
import org.w3c.dom.Document;

public class KMLParsingTest {

    @Test
    public void testParse() throws Exception {
        Parser parser = new Parser(new KMLConfiguration());
        SimpleFeature f =
                (SimpleFeature) parser.parse(getClass().getResourceAsStream("states.kml"));
        Assert.assertNotNull(f);

        Assert.assertEquals("topp:states", f.getAttribute("name"));

        Collection placemarks = (Collection) f.getAttribute("Feature");
        Assert.assertEquals(49, placemarks.size());
    }

    @Test
    public void testStream() throws Exception {
        StreamingParser parser =
                new StreamingParser(
                        new KMLConfiguration(),
                        getClass().getResourceAsStream("states.kml"),
                        KML.Placemark);
        int count = 0;
        SimpleFeature f = null;

        while ((f = (SimpleFeature) parser.parse()) != null) {
            FeatureTypeStyle style = (FeatureTypeStyle) f.getAttribute("Style");
            Assert.assertNotNull(style);

            Assert.assertEquals(3, style.rules().get(0).symbolizers().size());

            count++;
        }

        Assert.assertEquals(49, count);
    }

    @Test
    public void testEncodeFeatureCollection() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("foo");
        tb.add("name", String.class);
        tb.add("description", String.class);
        tb.add("geometry", Geometry.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder sb = new SimpleFeatureBuilder(tb.buildFeatureType());
        DefaultFeatureCollection features = new DefaultFeatureCollection();

        sb.add("one");
        sb.add("the first feature");
        sb.add(gf.createPoint(new Coordinate(1, 1)));
        features.add(sb.buildFeature("1"));

        sb.add("two");
        sb.add("the second feature");
        sb.add(gf.createPoint(new Coordinate(2, 2)));
        features.add(sb.buildFeature("2"));

        Encoder encoder = new Encoder(new KMLConfiguration());
        encoder.setIndenting(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encoder.encode(features, KML.kml, out);
        // System.out.println(new String(out.toByteArray()));

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document d = db.parse(new ByteArrayInputStream(out.toByteArray()));
        Assert.assertEquals("kml:kml", d.getDocumentElement().getNodeName());
        Assert.assertEquals(2, d.getElementsByTagName("kml:Placemark").getLength());
    }

    @Test
    @Ignore
    public void testEncodeFeature() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("foo");
        tb.add("name", String.class);
        tb.add("description", String.class);
        tb.add("geometry", Geometry.class);

        GeometryFactory gf = new GeometryFactory();
        SimpleFeatureBuilder sb = new SimpleFeatureBuilder(tb.buildFeatureType());
        List<SimpleFeature> features = new ArrayList<>();

        sb.add("one");
        sb.add("the first feature");
        sb.add(gf.createPoint(new Coordinate(1, 1)));
        features.add(sb.buildFeature("1"));

        sb.add("two");
        sb.add("the second feature");
        sb.add(gf.createPoint(new Coordinate(2, 2)));
        features.add(sb.buildFeature("2"));

        sb = new SimpleFeatureBuilder(DocumentTypeBinding.FeatureType);
        sb.set("Feature", features);
        SimpleFeature f = sb.buildFeature("kml");

        Encoder encoder = new Encoder(new KMLConfiguration());
        encoder.setIndenting(true);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        encoder.encode(f, KML.kml, out);

        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document d = db.parse(new ByteArrayInputStream(out.toByteArray()));
        Assert.assertEquals("kml:kml", d.getDocumentElement().getNodeName());
        Assert.assertEquals(2, d.getElementsByTagName("kml:Placemark").getLength());
    }
}
