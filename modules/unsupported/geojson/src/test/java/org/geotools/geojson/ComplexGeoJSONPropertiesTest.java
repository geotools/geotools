/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson;

import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.io.WKTReader;

public class ComplexGeoJSONPropertiesTest extends GeoJSONTestSupport {

    FeatureJSON fjson = new FeatureJSON();
    SimpleFeatureType featureType;
    SimpleFeatureBuilder fb;

    @Before
    public void setUp() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("feature");
        tb.setSRS("EPSG:4326");
        tb.add("int", Integer.class);
        tb.add("double", Double.class);
        tb.add("string", String.class);
        tb.add("list", List.class);
        tb.add("map", Map.class);
        tb.add("geometry", Geometry.class);

        featureType = tb.buildFeatureType();
        fb = new SimpleFeatureBuilder(featureType);
    }

    @Test
    public void testFeatureWrite() throws Exception {
        StringWriter writer = new StringWriter();
        fjson.writeFeature(feature(1), writer);

        assertEquals(strip(featureText(1)), writer.toString());
    }

    @Test
    public void testWriteReadNoProperties() throws Exception {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Point.class, CRS.decode("EPSG:4326"));
        tb.add("name", String.class);
        tb.add("quantity", Integer.class);
        tb.setName("outbreak");
        SimpleFeatureType schema = tb.buildFeatureType();

        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);
        fb.add(new WKTReader().read("POINT(10 20)"));
        SimpleFeature feature = fb.buildFeature("outbreak.1");

        FeatureJSON fj = new FeatureJSON();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        fj.writeFeature(feature, os);

        String json = os.toString();

        // here it would break because the written json was incorrect
        SimpleFeature feature2 = fj.readFeature(json);
        assertNotNull(feature2);
        assertEquals(feature.getID(), feature2.getID());
        assertEquals(feature.getDefaultGeometry(), feature2.getDefaultGeometry());
    }

    @Test
    public void testFeatureRead() throws Exception {
        SimpleFeature f1 = feature(1);
        SimpleFeature f2 = fjson.readFeature(reader(strip(featureText(1))));
        assertEqualsLax(f1, f2);
    }

    @Test
    public void testFeatureWithGeometryCollectionRead() throws Exception {
        String json = strip("{"
                + "  'type':'Feature',"
                + "  'geometry': {"
                + "    'type':'GeometryCollection',"
                + "    'geometries':[{"
                + "        'type':'Point','coordinates':[4,6]"
                + "      },{"
                + "        'type':'LineString','coordinates':[[4,6],[7,10]]"
                + "      }"
                + "     ]"
                + "  },"
                + "  'properties':{"
                + "    'name':'Name123',"
                + "    'label':'Label321',"
                + "    'roles':'[1,2,3]'"
                + "  },"
                + "  'id':'fid-7205cfc1_138e7ce8900_-7ffe'"
                + "}");

        SimpleFeature f1 = fjson.readFeature(json);
        assertNotNull(f1.getDefaultGeometry());

        GeometryCollection gc = (GeometryCollection) f1.getDefaultGeometry();
        assertEquals(2, gc.getNumGeometries());

        WKTReader wkt = new WKTReader();
        assertEquals(wkt.read("POINT (4 6)"), gc.getGeometryN(0));
        assertEquals(wkt.read("LINESTRING (4 6, 7 10)"), gc.getGeometryN(1));
        assertEquals("fid-7205cfc1_138e7ce8900_-7ffe", f1.getID());
        assertEquals("Name123", f1.getAttribute("name"));
        assertEquals("Label321", f1.getAttribute("label"));
        assertEquals("[1,2,3]", f1.getAttribute("roles"));
    }

    @Test
    public void testFeatureWithGeometryCollectionRead2() throws Exception {
        String json = strip("{"
                + "   'type':'Feature',"
                + "   'geometry':{"
                + "      'type':'GeometryCollection',"
                + "      'geometries':["
                + "         {"
                + "            'type':'Polygon',"
                + "            'coordinates':[[[-28.1107, 142.998], [-28.1107, 148.623], [-30.2591, 148.623], [-30.2591, 142.998], [-28.1107, 142.998]]]"
                + "         },"
                + "         {"
                + "            'type':'Polygon',"
                + "            'coordinates':[[[-27.1765, 142.998], [-25.6811, 146.4258], [-27.1765, 148.5352], [-27.1765, 142.998]]]"
                + "         }"
                + "     ]"
                + "   },"
                + "   'properties':{"
                + "      'name':'',"
                + "      'caseSN':'x_2000a',"
                + "      'siteNum':2"
                + "   },"
                + "   'id':'fid-397164b3_13880d348b9_-7a5c'"
                + "}");

        SimpleFeature f1 = fjson.readFeature(json);
        assertNotNull(f1.getDefaultGeometry());

        GeometryCollection gc = (GeometryCollection) f1.getDefaultGeometry();
        assertEquals(2, gc.getNumGeometries());

        WKTReader wkt = new WKTReader();
        assertEquals(
                wkt.read(
                        "POLYGON ((-28.1107 142.998, -28.1107 148.623, -30.2591 148.623, -30.2591 142.998, -28.1107 142.998))"),
                gc.getGeometryN(0));
        assertEquals(
                wkt.read("POLYGON((-27.1765 142.998, -25.6811 146.4258, -27.1765 148.5352, -27.1765 142.998))"),
                gc.getGeometryN(1));

        assertEquals("fid-397164b3_13880d348b9_-7a5c", f1.getID());
        assertEquals("", f1.getAttribute("name"));
        assertEquals("x_2000a", f1.getAttribute("caseSN"));
        assertEquals(2L, f1.getAttribute("siteNum"));
    }

    @Test
    public void testFeatureWithRegularGeometryAttributeRead() throws Exception {
        String jsonIn = strip("{"
                + "   'type': 'Feature',"
                + "   'geometry': {"
                + "     'type': 'Point',"
                + "     'coordinates': [0.1, 0.1]"
                + "   },"
                + "   'properties': {"
                + "     'int': 1,"
                + "     'double': 0.1,"
                + "     'string': 'one',"
                + "     'otherGeometry': {"
                + "        'type': 'LineString',"
                + "        'coordinates': [[1.1, 1.2], [1.3, 1.4]]"
                + "     }"
                + "   },"
                + "   'id': 'feature.0'"
                + " }");
        SimpleFeature f = fjson.readFeature(reader(jsonIn));

        assertNotNull(f);
        assertTrue(f.getDefaultGeometry() instanceof Point);

        Point p = (Point) f.getDefaultGeometry();
        assertEquals(0.1, p.getX(), 0.1);
        assertEquals(0.1, p.getY(), 0.1);

        assertTrue(f.getAttribute("otherGeometry") instanceof LineString);
        assertEquals(
                new GeometryFactory()
                        .createLineString(new Coordinate[] {new Coordinate(1.1, 1.2), new Coordinate(1.3, 1.4)}),
                f.getAttribute("otherGeometry"));

        assertEquals(1, ((Number) f.getAttribute("int")).intValue());
        assertEquals(0.1, ((Number) f.getAttribute("double")).doubleValue(), 0.0f);
        assertEquals("one", f.getAttribute("string"));

        assertEquals(jsonIn, fjson.toString(f));
    }

    @Test
    public void testFeatureWithDefaultGeometryEqualsNullRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip("{"
                + "   'type': 'Feature',"
                + "   'geometry': null,"
                + "   'properties': {"
                + "     'int': 1,"
                + "     'double': 0.1,"
                + "     'string': 'one',"
                + "     'list': [1, [ 2 ] ],"
                + "     'map': {'key': 1}"
                + "   },"
                + "   'id': 'feature.0'"
                + " }")));

        assertNotNull(f);
        assertNull(f.getDefaultGeometry());

        assertEquals(1, ((Number) f.getAttribute("int")).intValue());
        assertEquals(0.1, ((Number) f.getAttribute("double")).doubleValue(), 0.0f);
        assertEquals("one", f.getAttribute("string"));
    }

    @Test
    public void testFeatureWithRegularGeometryAttributeNoDefaultGeometryRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip("{"
                + "   'type': 'Feature',"
                + "   'properties': {"
                + "     'int': 1,"
                + "     'double': 0.1,"
                + "     'string': 'one',"
                + "     'list': [1, [ 2 ] ],"
                + "     'map': {'key': 1},"
                + "     'otherGeometry': {"
                + "        'type': 'LineString',"
                + "        'coordinates': [[1.1, 1.2], [1.3, 1.4]]"
                + "     }"
                + "   },"
                + "   'id': 'feature.0'"
                + " }")));

        assertNotNull(f);
        assertTrue(f.getDefaultGeometry() instanceof LineString);

        LineString l = (LineString) f.getDefaultGeometry();
        assertEquals(
                new GeometryFactory()
                        .createLineString(new Coordinate[] {new Coordinate(1.1, 1.2), new Coordinate(1.3, 1.4)}),
                l);

        assertTrue(f.getAttribute("otherGeometry") instanceof LineString);
        assertEquals(
                new GeometryFactory()
                        .createLineString(new Coordinate[] {new Coordinate(1.1, 1.2), new Coordinate(1.3, 1.4)}),
                f.getAttribute("otherGeometry"));

        assertEquals(1, ((Number) f.getAttribute("int")).intValue());
        assertEquals(0.1, ((Number) f.getAttribute("double")).doubleValue(), 0.0f);
        assertEquals("one", f.getAttribute("string"));
    }

    @Test
    public void testFeatureWithBoundsWrite() throws Exception {
        String json = "{"
                + "   'type': 'Feature',"
                + "   'bbox': [1.1, 1.1, 1.1, 1.1], "
                + "   'geometry': {"
                + "     'type': 'Point',"
                + "     'coordinates': [1.1, 1.1]"
                + "   },"
                + "   'properties': {"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one',"
                + "     'list': [1, [ 2 ] ],"
                + "     'map': {'key': 1}"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";

        fjson.setEncodeFeatureBounds(true);
        assertEquals(strip(json), fjson.toString(feature(1)));
    }

    @Test
    public void testFeatureWithCRSWrite() throws Exception {
        fjson.setEncodeFeatureCRS(true);
        assertEquals(strip(featureWithCRSText()), fjson.toString(feature(1)));
    }

    @Test
    public void testFeatureNoGeometryWrite() throws Exception {
        String json = "{"
                + "   'type': 'Feature',"
                + "   'properties': {"
                + "     'foo': 'FOO'"
                + "   },"
                + "   'id': 'feature.foo'"
                + " }";

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("nogeom");
        tb.add("foo", String.class);

        SimpleFeatureType ft = tb.buildFeatureType();
        SimpleFeatureBuilder b = new SimpleFeatureBuilder(ft);
        b.add("FOO");

        SimpleFeature f = b.buildFeature("feature.foo");
        assertEquals(strip(json), fjson.toString(f));
    }

    String featureWithCRSText() {
        String json = "{"
                + "   'type': 'Feature',"
                + "   'crs': {"
                + "     'type': 'name',"
                + "     'properties': {"
                + "       'name': 'EPSG:4326'"
                + "     }"
                + "   }, "
                + "   'geometry': {"
                + "     'type': 'Point',"
                + "     'coordinates': [1.1, 1.1]"
                + "   },"
                + "   'properties': {"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one',"
                + "     'list': [1, [ 2 ] ],"
                + "     'map': {'key': 1}"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

    @Test
    public void testFeatureWithCRSRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip(featureWithCRSText())));
        assertTrue(CRS.equalsIgnoreMetadata(
                CRS.decode("EPSG:4326"), f.getFeatureType().getCoordinateReferenceSystem()));
    }

    String featureWithBBOXText() {
        String json = "{"
                + "   'type': 'Feature',"
                + "   'bbox': [1.1, 1.1, 1.1, 1.1],"
                + "   'geometry': {"
                + "     'type': 'Point',"
                + "     'coordinates': [1.1, 1.1]"
                + "   },"
                + "   'properties': {"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one',"
                + "     'list': [1, [ 2 ] ],"
                + "     'map': {'key': 1}"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

    @Test
    public void testFeatureWithBBOXRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip(featureWithBBOXText())));
        assertEquals(1.1, f.getBounds().getMinX(), 0.1d);
        assertEquals(1.1, f.getBounds().getMaxX(), 0.1d);
        assertEquals(1.1, f.getBounds().getMinY(), 0.1d);
        assertEquals(1.1, f.getBounds().getMaxY(), 0.1d);
    }

    String featureWithBoundedByAttributeText() {
        String json = "{"
                + "   'type': 'Feature',"
                + "   'geometry': {"
                + "     'type': 'Point',"
                + "     'coordinates': [1.1, 1.1]"
                + "   },"
                + "   'properties': {"
                + "     'boundedBy': [-1.2, -1.3, 1.2, 1.3],"
                + "     'int': 1,"
                + "     'double': 1.1,"
                + "     'string': 'one'"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

    SimpleFeature featureWithBoundedByAttribute() {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("feature");
        tb.add("geometry", Point.class);
        tb.add("boundedBy", Envelope.class);
        tb.add("int", Integer.class);
        tb.add("double", Double.class);
        tb.add("string", String.class);
        tb.add("list", List.class);
        tb.add("Map", Map.class);

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());
        b.add(new GeometryFactory().createPoint(new Coordinate(1.1, 1.1)));
        b.add(new Envelope(-1.2, 1.2, -1.3, 1.3));
        b.add(1);
        b.add(1.1);
        b.add("one");
        return b.buildFeature("feature.1");
    }

    @Test
    public void testFeatureWithBoundedByAttributeRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip(featureWithBoundedByAttributeText())));
        List l = (List) f.getAttribute("boundedBy");

        assertEquals(-1.2, (Double) l.get(0), 0.1d);
        assertEquals(-1.3, (Double) l.get(1), 0.1d);
        assertEquals(1.2, (Double) l.get(2), 0.1d);
        assertEquals(1.3, (Double) l.get(3), 0.1d);
    }

    @Test
    public void testFeatureWithoutPropertiesRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip(featureWithoutPropertiesText())));
        assertEquals(1, f.getFeatureType().getAttributeCount());
        assertEquals("geometry", f.getFeatureType().getDescriptor(0).getLocalName());

        assertEquals(1.2, ((Point) f.getDefaultGeometry()).getX(), 0.0f);
        assertEquals(3.4, ((Point) f.getDefaultGeometry()).getY(), 0.0f);
    }

    String featureWithoutPropertiesText() {
        String json = "{"
                + "   'type': 'Feature',"
                + "   'geometry': {"
                + "     'type': 'Point',"
                + "     'coordinates': [1.2, 3.4]"
                + "   },"
                + "   'id': 'feature.1'"
                + " }";
        return json;
    }

    @Test
    public void testFeatureWithGeometryAfterPropertiesRead() throws Exception {
        SimpleFeature f1 = feature(1);
        SimpleFeature f2 = fjson.readFeature(reader(strip(featureTextWithGeometryAfterProperties(1))));
        assertEqualsLax(f1, f2);
    }

    String featureTextWithGeometryAfterProperties(int val) {
        String text = "{"
                + "  'type': 'Feature',"
                + "'  properties': {"
                + "     'int': "
                + val
                + ","
                + "     'double': "
                + (val + 0.1)
                + ","
                + "     'string': '"
                + toString(val)
                + "'"
                + "     'list': ["
                + val
                + ", [  2 ] ]"
                + "     'map': {'key': "
                + val
                + "}"
                + "   },"
                + "  'geometry': {"
                + "     'type': 'Point',"
                + "     'coordinates': ["
                + (val + 0.1)
                + ","
                + (val + 0.1)
                + "]"
                + "   }, "
                + "   'id':'feature."
                + val
                + "'"
                + "}";

        return text;
    }

    @Test
    public void testFeatureWithBoundedByAttributeWrite() throws Exception {
        StringWriter writer = new StringWriter();
        fjson.writeFeature(featureWithBoundedByAttribute(), writer);
        assertEquals(strip(featureWithBoundedByAttributeText()), writer.toString());
    }

    @Test
    public void testFeatureCollectionConflictingTypesSchemaRead() throws Exception {
        String json = strip("{"
                + "  'type': 'FeatureCollection',"
                + "  'features': ["
                + "    {"
                + "      'type': 'Feature',"
                + "      'properties': {"
                + "         'prop': 1"
                + "      }"
                + "    },"
                + "    {"
                + "      'type': 'Feature',"
                + "      'properties': {"
                + "        'prop': {'xyz': 'fortytwo' }"
                + "      }"
                + "    }"
                + "  ]"
                + "}");

        try {
            fjson.readFeatureCollectionSchema(json, false);
            fail("Should have thrown IllegalStateException");
        } catch (IllegalStateException e) {
        }
    }

    @Test
    public void testFeatureCollectionConflictingButInterchangeableTypesSchemaRead() throws Exception {
        String json = strip("{"
                + "  'type': 'FeatureCollection',"
                + "  'features': ["
                + "    {"
                + "      'type': 'Feature',"
                + "      'properties': {"
                + "         'prop': null"
                + "      }"
                + "    },"
                + "    {"
                + "      'type': 'Feature',"
                + "      'properties': {"
                + "         'prop': [1.0]"
                + "      }"
                + "    }"
                + "  ]"
                + "}");

        SimpleFeatureType type = fjson.readFeatureCollectionSchema(json, false);
        assertEquals(List.class, type.getDescriptor("prop").getType().getBinding());
    }

    SimpleFeature feature(int val) {
        fb.add(val);
        fb.add(val + 0.1);
        fb.add(toString(val));
        fb.add(new LinkedList<>(asList((long) val, singletonList(2L))));
        fb.add(new HashMap<>(Collections.singletonMap("key", (long) val)));
        fb.add(new GeometryFactory().createPoint(new Coordinate(val + 0.1, val + 0.1)));

        return fb.buildFeature("feature." + val);
    }

    String featureText(int val) {
        return featureText(val, false, false);
    }

    String featureText(int val, boolean missingAttribute, boolean nullAttribute) {
        if (missingAttribute && nullAttribute) {
            throw new IllegalArgumentException("For tests, use only one of either missingAttribute or nullAttribute");
        }
        String text = "{"
                + "  'type': 'Feature',"
                + "  'geometry': {"
                + "     'type': 'Point',"
                + "     'coordinates': ["
                + (val + 0.1)
                + ","
                + (val + 0.1)
                + "]"
                + "   }, "
                + "'  properties': {"
                + "     'int': "
                + val
                + ","
                + (missingAttribute
                        ? ""
                        : nullAttribute ? "     'double': null," : "     'double': " + (val + 0.1) + ",")
                + "     'string': '"
                + toString(val)
                + "',"
                + "     'list': ["
                + val
                + ", [ 2 ] ],"
                + "     'map': {'key': "
                + val
                + "}"
                + "   },"
                + "   'id':'feature."
                + val
                + "'"
                + "}";

        return text;
    }

    FeatureCollection collection() {
        DefaultFeatureCollection collection = new DefaultFeatureCollection(null, featureType);
        for (int i = 0; i < 3; i++) {
            collection.add(feature(i));
        }
        return collection;
    }

    String collectionText() {
        return collectionText(false, false);
    }

    String collectionText(boolean withBounds, boolean withCRS) {
        return collectionText(withBounds, withCRS, false, false, false, false);
    }

    String collectionText(
            boolean withBounds,
            boolean withCRS,
            boolean crsAfter,
            boolean missingFirstFeatureAttribute,
            boolean nullFirstFeatureAttribute) {
        return collectionText(
                withBounds, withCRS, crsAfter, missingFirstFeatureAttribute, nullFirstFeatureAttribute, false);
    }

    String collectionText(
            boolean withBounds,
            boolean withCRS,
            boolean crsAfter,
            boolean missingFirstFeatureAttribute,
            boolean nullFirstFeatureAttribute,
            boolean nullAttributeAllFeatures) {
        StringBuffer sb = new StringBuffer();
        sb.append("{'type':'FeatureCollection',");
        if (withBounds) {
            FeatureCollection features = collection();
            ReferencedEnvelope bbox = features.getBounds();
            sb.append("'bbox': [");
            sb.append(bbox.getMinX())
                    .append(",")
                    .append(bbox.getMinY())
                    .append(",")
                    .append(bbox.getMaxX())
                    .append(",")
                    .append(bbox.getMaxY());
            sb.append("],");
        }
        if (withCRS && !crsAfter) {
            sb.append("'crs': {");
            sb.append("  'type': 'name',");
            sb.append("  'properties': {");
            sb.append("    'name': 'EPSG:4326'");
            sb.append("   }");
            sb.append("},");
        }
        sb.append("'features':[");
        if (nullAttributeAllFeatures) {
            // creates all features with a null attribute
            for (int i = 0; i < 3; i++) {
                sb.append(featureText(i, false, true)).append(",");
            }
        } else {
            // only the first feature will have a null or missing attribute
            sb.append(featureText(0, missingFirstFeatureAttribute, nullFirstFeatureAttribute))
                    .append(",");
            for (int i = 1; i < 3; i++) {
                sb.append(featureText(i, false, false)).append(",");
            }
        }
        sb.setLength(sb.length() - 1);
        sb.append("]");
        if (withCRS && crsAfter) {
            sb.append(",'crs': {");
            sb.append("  'type': 'name',");
            sb.append("  'properties': {");
            sb.append("    'name': 'EPSG:4326'");
            sb.append("   }");
            sb.append("}");
        }
        sb.append("}");
        return sb.toString();
    }
}
