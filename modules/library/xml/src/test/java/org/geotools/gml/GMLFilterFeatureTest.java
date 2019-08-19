/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gml;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.TransformerException;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLUnit;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.gml.producer.FeatureTransformer;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLFilterImpl;

/**
 * {@link GMLFilterFeature} modifies whitespaces in attribute values, this way tampering with input
 * data.
 *
 * <p>{@link SubHandlerPolygon} twists rings of {@link Polygon}s, reverses CCW shells and CW holes,
 * this way tampering with input data.
 *
 * <p>It is not the job of a GML-parser to modify input data.
 *
 * @author Burkhard Strauss
 */
public class GMLFilterFeatureTest {

    @Before
    public void before() {

        final Map<String, String> namespaces = new HashMap<String, String>();
        namespaces.put("xlink", "http://www.w3.org/1999/xlink");
        namespaces.put("wfs", "http://www.opengis.net/wfs");
        namespaces.put("gml", "http://www.opengis.net/gml");
        namespaces.put("gt", "http://www.geotools.org");
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(namespaces));
    }

    @Test
    public void test()
            throws TransformerException, SAXException, IOException, ParserConfigurationException {

        final String gml = createTestGMLInput();
        // System.out.format("%s\n", gml);
        final DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        class Handler extends XMLFilterImpl implements GMLHandlerFeature {
            @Override
            public void feature(final SimpleFeature feature) {
                featureCollection.add(feature);
            }
        }
        final Handler handler = new Handler();
        final GMLFilterFeature filterFeature = new GMLFilterFeature(handler);
        final GMLFilterGeometry filterGeometry = new GMLFilterGeometry(filterFeature);
        final GMLFilterDocument filterDocument = new GMLFilterDocument(filterGeometry);
        final SAXParserFactory parserFactory = SAXParserFactory.newInstance();
        parserFactory.setNamespaceAware(true);
        final SAXParser parser = parserFactory.newSAXParser();
        final XMLReader reader = parser.getXMLReader();
        reader.setContentHandler(filterDocument);
        reader.parse(new InputSource(new StringReader(gml)));
        assertTrue(featureCollection.size() == 2);
        final Iterator<SimpleFeature> it = featureCollection.iterator();
        {
            final SimpleFeature feature = it.next();
            final Polygon polygon = (Polygon) feature.getAttribute("geometry");
            final String a = polygon.toString();
            final String b = polygonA().toString();
            assertTrue(a.equals(b));
            final String value = (String) feature.getAttribute("my_string_attribute");
            assertTrue(value.equals(helloWorldA));
        }
        {
            final SimpleFeature feature = it.next();
            final Polygon polygon = (Polygon) feature.getAttribute("geometry");
            final String a = polygon.toString();
            final String b = polygonB().toString();
            assertTrue(a.equals(b));
            final String value = (String) feature.getAttribute("my_string_attribute");
            assertTrue(value.equals(helloWorldB));
        }
    }

    private String createTestGMLInput() throws TransformerException {

        final FeatureTransformer tx = new FeatureTransformer();
        tx.setIndentation(2);
        tx.setCollectionBounding(true);
        tx.setFeatureBounding(true);
        final ByteArrayOutputStream bos = new ByteArrayOutputStream();
        tx.transform(createFeatureCollection(), bos);
        return bos.toString();
    }

    private FeatureCollection createFeatureCollection() {

        final SimpleFeatureTypeBuilder featureTypeBuilder = new SimpleFeatureTypeBuilder();
        featureTypeBuilder.setName("test_feature_type");
        featureTypeBuilder.add("geometry", Geometry.class);
        featureTypeBuilder.add("my_double_attribute", double.class);
        featureTypeBuilder.add("my_string_attribute", String.class);
        final SimpleFeatureType featureType = featureTypeBuilder.buildFeatureType();
        final DefaultFeatureCollection featureCollection = new DefaultFeatureCollection();
        {
            final SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            featureBuilder.add(polygonA());
            featureBuilder.add(Math.PI);
            featureBuilder.add(helloWorldA);
            featureCollection.add(featureBuilder.buildFeature("id-000"));
        }
        {
            final SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
            featureBuilder.add(polygonB());
            featureBuilder.add(0.5 * Math.PI);
            featureBuilder.add(helloWorldB);
            featureCollection.add(featureBuilder.buildFeature("id-001"));
        }
        return featureCollection;
    }

    private static Polygon polygonA() {

        final Coordinate[] coors = new Coordinate[5];
        coors[0] = new Coordinate(0, 0);
        coors[1] = new Coordinate(1, 0);
        coors[2] = new Coordinate(1, 1);
        coors[3] = new Coordinate(0, 1);
        coors[4] = coors[0];
        final LinearRing shell = geometryFactory.createLinearRing(coors);
        return geometryFactory.createPolygon(shell);
    }

    private static Polygon polygonB() {

        final LinearRing shell;
        {
            final Coordinate[] coors = new Coordinate[5];
            coors[0] = new Coordinate(0, 0);
            coors[1] = new Coordinate(1, 0);
            coors[2] = new Coordinate(1, 1);
            coors[3] = new Coordinate(0, 1);
            coors[4] = coors[0];
            shell = geometryFactory.createLinearRing(coors);
        }
        final LinearRing hole;
        {
            final Coordinate[] coors = new Coordinate[5];
            coors[0] = new Coordinate(0.1, 0.1);
            coors[1] = new Coordinate(0.9, 0.1);
            coors[2] = new Coordinate(0.9, 0.9);
            coors[3] = new Coordinate(0.1, 0.9);
            coors[4] = coors[0];
            hole = geometryFactory.createLinearRing(coors);
        }
        return geometryFactory.createPolygon(shell, new LinearRing[] {hole});
    }

    private static final GeometryFactory geometryFactory = new GeometryFactory();

    private static final String helloWorldA = "hello,\nworld\nA\n";

    private static final String helloWorldB = "hello,\nworld\nB\n";
}
