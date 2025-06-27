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
package org.geotools.kml.bindings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.kml.v22.KML;
import org.geotools.kml.v22.KMLTestSupport;
import org.geotools.xsd.Binding;
import org.junit.Test;
import org.locationtech.jts.geom.Point;

public class PlacemarkTypeBindingTest extends KMLTestSupport {
    @Test
    public void testType() throws Exception {
        assertEquals(SimpleFeature.class, binding(KML.PlacemarkType).getType());
    }

    @Test
    public void testExecutionMode() throws Exception {
        assertEquals(Binding.AFTER, binding(KML.PlacemarkType).getExecutionMode());
    }

    private SimpleFeature parsePlacemark() throws Exception {
        SimpleFeature documentOrPlacemark = (SimpleFeature) parse();
        if (documentOrPlacemark.getName().getLocalPart().equalsIgnoreCase("placemark")) {
            return documentOrPlacemark;
        }
        @SuppressWarnings("unchecked")
        List<SimpleFeature> features = (List<SimpleFeature>) documentOrPlacemark.getAttribute("Feature");
        assert features.size() == 1;
        return features.get(0);
    }

    @Test
    public void testParsePlacemarkWithGeometry() throws Exception {
        String xml = "<Placemark>"
                + "<name>name</name>"
                + "<description>description</description>"
                + "<Point>"
                + "<coordinates>1,2</coordinates>"
                + "</Point>"
                + "</Placemark>";
        buildDocument(xml);

        SimpleFeature placemark = parsePlacemark();
        assertEquals("name", placemark.getAttribute("name"));
        assertEquals("description", placemark.getAttribute("description"));
        assertNotNull(placemark.getAttribute("Geometry"));
        assertTrue(placemark.getAttribute("Geometry") instanceof Point);

        Point p = (Point) placemark.getAttribute("Geometry");
        assertEquals(1d, p.getX(), 0.1);
        assertEquals(2d, p.getY(), 0.1);
    }

    @Test
    public void testParseWithUntypedData() throws Exception {
        String xml = "<Placemark>"
                + "<name>name</name>"
                + "<description>description</description>"
                + "<Point>"
                + "<coordinates>1,2</coordinates>"
                + "</Point>"
                + "<ExtendedData>"
                + "<Data name=\"foo\"><value>bar</value></Data>"
                + "</ExtendedData>"
                + "</Placemark>";
        buildDocument(xml);

        SimpleFeature placemark = parsePlacemark();
        Map<Object, Object> userData = placemark.getUserData();
        @SuppressWarnings("unchecked")
        Map<String, String> untypedData = (Map<String, String>) userData.get("UntypedExtendedData");
        assertEquals("bar", untypedData.get("foo"));
    }

    @Test
    public void testParseWithTypedData() throws Exception {
        String xml = "<kml>"
                + "<Schema name=\"foo\">"
                + "<SimpleField type=\"int\" name=\"quux\"></SimpleField>"
                + "</Schema>"
                + "<Placemark>"
                + "<name>name</name>"
                + "<description>description</description>"
                + "<Point>"
                + "<coordinates>1,2</coordinates>"
                + "</Point>"
                + "<ExtendedData>"
                + "<SchemaData schemaUrl=\"#foo\">"
                + "<SimpleData name=\"quux\">morx</SimpleData>"
                + "</SchemaData>"
                + "</ExtendedData>"
                + "</Placemark></kml>";
        buildDocument(xml);

        SimpleFeature placemark = parsePlacemark();
        SimpleFeatureType featureType = placemark.getFeatureType();
        assertEquals(Integer.class, featureType.getDescriptor("quux").getType().getBinding());
        assertEquals("morx", placemark.getAttribute("quux"));
    }

    // difference between this test and typed data test is schemaURL="foo" instead of
    // schemaURL="#foo"
    @Test
    public void testParseWithTypedDataSchemaURLNotFragment() throws Exception {
        String xml = "<kml>"
                + "<Schema name=\"foo\">"
                + "<SimpleField type=\"int\" name=\"quux\"></SimpleField>"
                + "</Schema>"
                + "<Placemark>"
                + "<name>name</name>"
                + "<description>description</description>"
                + "<Point>"
                + "<coordinates>1,2</coordinates>"
                + "</Point>"
                + "<ExtendedData>"
                + "<SchemaData schemaUrl=\"foo\">"
                + "<SimpleData name=\"quux\">morx</SimpleData>"
                + "</SchemaData>"
                + "</ExtendedData>"
                + "</Placemark></kml>";
        buildDocument(xml);

        SimpleFeature placemark = parsePlacemark();
        SimpleFeatureType featureType = placemark.getFeatureType();
        assertEquals(Integer.class, featureType.getDescriptor("quux").getType().getBinding());
        assertEquals("morx", placemark.getAttribute("quux"));
    }

    @Test
    public void testParseTypedAndUntypedData() throws Exception {
        String xml = "<kml>"
                + "<Schema name=\"foo\">"
                + "<SimpleField type=\"int\" name=\"quux\"></SimpleField>"
                + "</Schema>"
                + "<Placemark>"
                + "<name>name</name>"
                + "<description>description</description>"
                + "<Point>"
                + "<coordinates>1,2</coordinates>"
                + "</Point>"
                + "<ExtendedData>"
                + "<SchemaData schemaUrl=\"#foo\">"
                + "<SimpleData name=\"quux\">morx</SimpleData>"
                + "</SchemaData>"
                + "<Data name=\"foo\"><value>bar</value></Data>"
                + "</ExtendedData>"
                + "</Placemark></kml>";
        buildDocument(xml);

        SimpleFeature placemark = parsePlacemark();
        SimpleFeatureType featureType = placemark.getFeatureType();
        assertEquals(Integer.class, featureType.getDescriptor("quux").getType().getBinding());
        assertEquals("morx", placemark.getAttribute("quux"));
        Map<Object, Object> userData = placemark.getUserData();
        @SuppressWarnings("unchecked")
        Map<String, String> untypedData = (Map<String, String>) userData.get("UntypedExtendedData");
        assertEquals("bar", untypedData.get("foo"));
    }

    @Test
    public void testParseCustomElement() throws Exception {
        String xml = "<kml>"
                + "<Schema name=\"fooelement\">"
                + "<SimpleField type=\"int\" name=\"quux\"></SimpleField>"
                + "</Schema>"
                + "<fooelement>"
                + "<name>name</name>"
                + "<description>description</description>"
                + "<Point>"
                + "<coordinates>1,2</coordinates>"
                + "</Point>"
                + "<ExtendedData>"
                + "<SchemaData schemaUrl=\"#foo\">"
                + "<SimpleData name=\"quux\">morx</SimpleData>"
                + "</SchemaData>"
                + "<Data name=\"foo\"><value>bar</value></Data>"
                + "</ExtendedData>"
                + "</fooelement></kml>";
        buildDocument(xml);

        SimpleFeature placemark = parsePlacemark();
        SimpleFeatureType featureType = placemark.getFeatureType();
        assertEquals(Integer.class, featureType.getDescriptor("quux").getType().getBinding());
        assertEquals("morx", placemark.getAttribute("quux"));
        Map<Object, Object> userData = placemark.getUserData();
        @SuppressWarnings("unchecked")
        Map<String, String> untypedData = (Map<String, String>) userData.get("UntypedExtendedData");
        assertEquals("bar", untypedData.get("foo"));
    }
}
