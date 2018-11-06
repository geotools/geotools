/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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

import java.io.ByteArrayOutputStream;
import java.io.StringWriter;
import java.util.List;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.io.WKTReader;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class FeatureJSONTest extends GeoJSONTestSupport {

    FeatureJSON fjson = new FeatureJSON();
    SimpleFeatureType featureType;
    SimpleFeatureBuilder fb;
    SimpleFeatureType featureTypeArray;
    SimpleFeatureBuilder fbArray;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        setupNonGeometricAttributes(tb);
        tb.add("geometry", Geometry.class);

        featureType = tb.buildFeatureType();
        fb = new SimpleFeatureBuilder(featureType);

        setupNonGeometricAttributes(tb);
        tb.add("array", String[].class);
        tb.add("geometry", Geometry.class);
        featureTypeArray = tb.buildFeatureType();

        fbArray = new SimpleFeatureBuilder(featureTypeArray);
    }

    private void setupNonGeometricAttributes(SimpleFeatureTypeBuilder tb) {
        tb.setName("feature");
        tb.setSRS("EPSG:4326");
        tb.add("int", Integer.class);
        tb.add("double", Double.class);
        tb.add("string", String.class);
    }

    public void testFeatureWrite() throws Exception {

        StringWriter writer = new StringWriter();
        fjson.writeFeature(feature(1), writer);

        assertEquals(strip(featureText(1)), writer.toString());
    }

    public void testFeatureArrayWrite() throws Exception {

        StringWriter writer = new StringWriter();
        fjson.writeFeature(featureArray(1), writer);

        assertEquals(strip(featureArrayText(1, false, false)), strip(writer.toString()));
    }

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
        // assertEquals(feature.getID(), feature2.getID());
    }

    public void testFeatureRead() throws Exception {
        SimpleFeature f1 = feature(1);
        SimpleFeature f2 = fjson.readFeature(reader(strip(featureText(1))));
        assertEqualsLax(f1, f2);
    }

    public void testFeatureArrayRead() throws Exception {
        SimpleFeature f1 = featureArray(1);
        SimpleFeature f2 = fjson.readFeature(reader(strip(featureArrayText(1, false, false))));
        assertEqualsLax(f1, f2);
    }

    public void testFeatureWithGeometryCollectionRead() throws Exception {
        String json =
                strip(
                        "{"
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
        assertTrue(wkt.read("POINT (4 6)").equals(gc.getGeometryN(0)));
        assertTrue(wkt.read("LINESTRING (4 6, 7 10)").equals(gc.getGeometryN(1)));

        assertEquals("fid-7205cfc1_138e7ce8900_-7ffe", f1.getID());
        assertEquals("Name123", f1.getAttribute("name"));
        assertEquals("Label321", f1.getAttribute("label"));
        assertEquals("[1,2,3]", f1.getAttribute("roles"));
    }

    public void testFeatureWithGeometryCollectionRead2() throws Exception {
        String json =
                strip(
                        "{"
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
        assertTrue(
                wkt.read(
                                "POLYGON ((-28.1107 142.998, -28.1107 148.623, -30.2591 148.623, -30.2591 142.998, -28.1107 142.998))")
                        .equals(gc.getGeometryN(0)));
        assertTrue(
                wkt.read(
                                "POLYGON((-27.1765 142.998, -25.6811 146.4258, -27.1765 148.5352, -27.1765 142.998))")
                        .equals(gc.getGeometryN(1)));

        assertEquals("fid-397164b3_13880d348b9_-7a5c", f1.getID());
        assertEquals("", f1.getAttribute("name"));
        assertEquals("x_2000a", f1.getAttribute("caseSN"));
        assertEquals(2l, f1.getAttribute("siteNum"));
    }

    public void testFeatureWithRegularGeometryAttributeRead() throws Exception {
        SimpleFeature f =
                fjson.readFeature(
                        reader(
                                strip(
                                        "{"
                                                + "   'type': 'Feature',"
                                                + "   'geometry': {"
                                                + "     'type': 'Point',"
                                                + "     'coordinates': [0.1, 0.1],"
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
                                                + " }")));

        assertNotNull(f);
        assertTrue(f.getDefaultGeometry() instanceof Point);

        Point p = (Point) f.getDefaultGeometry();
        assertEquals(0.1, p.getX(), 0.1);
        assertEquals(0.1, p.getY(), 0.1);

        assertTrue(f.getAttribute("otherGeometry") instanceof LineString);
        assertTrue(
                new GeometryFactory()
                        .createLineString(
                                new Coordinate[] {
                                    new Coordinate(1.1, 1.2), new Coordinate(1.3, 1.4)
                                })
                        .equals((LineString) f.getAttribute("otherGeometry")));

        assertEquals(1, ((Number) f.getAttribute("int")).intValue());
        assertEquals(0.1, ((Number) f.getAttribute("double")).doubleValue());
        assertEquals("one", f.getAttribute("string"));
    }

    public void testFeatureWithDefaultGeometryEqualsNullRead() throws Exception {
        SimpleFeature f =
                fjson.readFeature(
                        reader(
                                strip(
                                        "{"
                                                + "   'type': 'Feature',"
                                                + "   'geometry': null,"
                                                + "   'properties': {"
                                                + "     'int': 1,"
                                                + "     'double': 0.1,"
                                                + "     'string': 'one'"
                                                + "   },"
                                                + "   'id': 'feature.0'"
                                                + " }")));

        assertNotNull(f);
        assertTrue(f.getDefaultGeometry() == null);

        assertEquals(1, ((Number) f.getAttribute("int")).intValue());
        assertEquals(0.1, ((Number) f.getAttribute("double")).doubleValue());
        assertEquals("one", f.getAttribute("string"));
    }

    public void testFeatureWithRegularGeometryAttributeNoDefaultGeometryRead() throws Exception {
        SimpleFeature f =
                fjson.readFeature(
                        reader(
                                strip(
                                        "{"
                                                + "   'type': 'Feature',"
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
                                                + " }")));

        assertNotNull(f);
        assertTrue(f.getDefaultGeometry() instanceof LineString);

        LineString l = (LineString) f.getDefaultGeometry();
        assertTrue(
                new GeometryFactory()
                        .createLineString(
                                new Coordinate[] {
                                    new Coordinate(1.1, 1.2), new Coordinate(1.3, 1.4)
                                })
                        .equals(l));

        assertTrue(f.getAttribute("otherGeometry") instanceof LineString);
        assertTrue(
                new GeometryFactory()
                        .createLineString(
                                new Coordinate[] {
                                    new Coordinate(1.1, 1.2), new Coordinate(1.3, 1.4)
                                })
                        .equals((LineString) f.getAttribute("otherGeometry")));

        assertEquals(1, ((Number) f.getAttribute("int")).intValue());
        assertEquals(0.1, ((Number) f.getAttribute("double")).doubleValue());
        assertEquals("one", f.getAttribute("string"));
    }

    public void testFeatureWithBoundsWrite() throws Exception {
        String json =
                "{"
                        + "   'type': 'Feature',"
                        + "   'bbox': [1.1, 1.1, 1.1, 1.1], "
                        + "   'geometry': {"
                        + "     'type': 'Point',"
                        + "     'coordinates': [1.1, 1.1]"
                        + "   },"
                        + "   'properties': {"
                        + "     'int': 1,"
                        + "     'double': 1.1,"
                        + "     'string': 'one'"
                        + "   },"
                        + "   'id': 'feature.1'"
                        + " }";

        fjson.setEncodeFeatureBounds(true);
        assertEquals(strip(json), fjson.toString(feature(1)));
    }

    public void testFeatureWithCRSWrite() throws Exception {
        fjson.setEncodeFeatureCRS(true);
        assertEquals(strip(featureWithCRSText()), fjson.toString(feature(1)));
    }

    public void testFeatureNoGeometryWrite() throws Exception {
        String json =
                "{"
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
        String json =
                "{"
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
                        + "     'string': 'one'"
                        + "   },"
                        + "   'id': 'feature.1'"
                        + " }";
        return json;
    }

    public void testFeatureWithCRSRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip(featureWithCRSText())));
        assertTrue(
                CRS.equalsIgnoreMetadata(
                        CRS.decode("EPSG:4326"),
                        f.getFeatureType().getCoordinateReferenceSystem()));
    }

    String featureWithBBOXText() {
        String json =
                "{"
                        + "   'type': 'Feature',"
                        + "   'bbox': [1.1, 1.1, 1.1, 1.1],"
                        + "   'geometry': {"
                        + "     'type': 'Point',"
                        + "     'coordinates': [1.1, 1.1]"
                        + "   },"
                        + "   'properties': {"
                        + "     'int': 1,"
                        + "     'double': 1.1,"
                        + "     'string': 'one'"
                        + "   },"
                        + "   'id': 'feature.1'"
                        + " }";
        return json;
    }

    public void testFeatureWithBBOXRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip(featureWithBBOXText())));
        assertEquals(1.1, f.getBounds().getMinX(), 0.1d);
        assertEquals(1.1, f.getBounds().getMaxX(), 0.1d);
        assertEquals(1.1, f.getBounds().getMinY(), 0.1d);
        assertEquals(1.1, f.getBounds().getMaxY(), 0.1d);
    }

    String featureWithBoundedByAttributeText() {
        String json =
                "{"
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

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(tb.buildFeatureType());
        b.add(new GeometryFactory().createPoint(new Coordinate(1.1, 1.1)));
        b.add(new Envelope(-1.2, 1.2, -1.3, 1.3));
        b.add(1);
        b.add(1.1);
        b.add("one");
        return b.buildFeature("feature.1");
    }

    public void testFeatureWithBoundedByAttributeRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip(featureWithBoundedByAttributeText())));
        List l = (List) f.getAttribute("boundedBy");

        assertEquals(-1.2, (Double) l.get(0), 0.1d);
        assertEquals(-1.3, (Double) l.get(1), 0.1d);
        assertEquals(1.2, (Double) l.get(2), 0.1d);
        assertEquals(1.3, (Double) l.get(3), 0.1d);
    }

    public void testFeatureWithoutPropertiesRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip(featureWithoutPropertiesText())));
        assertEquals(1, f.getFeatureType().getAttributeCount());
        assertEquals("geometry", f.getFeatureType().getDescriptor(0).getLocalName());

        assertEquals(1.2, ((Point) f.getDefaultGeometry()).getX());
        assertEquals(3.4, ((Point) f.getDefaultGeometry()).getY());
    }

    String featureWithoutPropertiesText() {
        String json =
                "{"
                        + "   'type': 'Feature',"
                        + "   'geometry': {"
                        + "     'type': 'Point',"
                        + "     'coordinates': [1.2, 3.4]"
                        + "   },"
                        + "   'id': 'feature.1'"
                        + " }";
        return json;
    }

    public void testFeatureWithGeometryAfterPropertiesRead() throws Exception {
        SimpleFeature f1 = feature(1);
        SimpleFeature f2 =
                fjson.readFeature(reader(strip(featureTextWithGeometryAfterProperties(1))));
        assertEqualsLax(f1, f2);
    }

    String featureTextWithGeometryAfterProperties(int val) {
        String text =
                "{"
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

    public void testFeatureWithBoundedByAttributeWrite() throws Exception {
        StringWriter writer = new StringWriter();
        fjson.writeFeature(featureWithBoundedByAttribute(), writer);
        assertEquals(strip(featureWithBoundedByAttributeText()), writer.toString());
    }

    public void testFeatureCollectionWrite() throws Exception {
        StringWriter writer = new StringWriter();
        fjson.writeFeatureCollection(collection(), writer);
        assertEquals(strip(collectionText()), writer.toString());
    }

    public void testFeatureCollectionRead() throws Exception {

        FeatureCollection actual = fjson.readFeatureCollection(reader(strip(collectionText())));
        assertNotNull(actual);

        FeatureCollection expected = collection();
        assertEquals(expected.size(), actual.size());

        FeatureIterator a = actual.features();
        FeatureIterator e = expected.features();

        while (e.hasNext()) {
            assertTrue(a.hasNext());
            assertEqualsLax((SimpleFeature) e.next(), (SimpleFeature) a.next());
        }
        a.close();
        e.close();
    }

    public void testFeatureCollectionStreamBasic() throws Exception {
        testFeatureCollectionStream(false, false);
    }

    public void testFeatureCollectionStreamFull() throws Exception {
        testFeatureCollectionStream(true, true);
    }

    void testFeatureCollectionStream(boolean withBounds, boolean withCRS) throws Exception {
        FeatureIterator<SimpleFeature> features =
                fjson.streamFeatureCollection(reader(strip(collectionText(withBounds, withCRS))));

        FeatureCollection expected = collection();
        FeatureIterator e = expected.features();

        while (e.hasNext()) {
            features.hasNext(); // ensure that hasNext() does not skip features
            assertTrue(features.hasNext());
            assertEqualsLax((SimpleFeature) e.next(), features.next());
        }

        features.close();
        e.close();
    }

    public void testFeatureCollectionWithBoundsWrite() throws Exception {
        fjson.setEncodeFeatureCollectionBounds(true);
        assertEquals(strip(collectionText(true, false)), fjson.toString(collection()));
    }

    public void testFeatureCollectionWithCRSWrite() throws Exception {
        fjson.setEncodeFeatureCollectionCRS(true);
        assertEquals(strip(collectionText(false, true)), fjson.toString(collection()));
    }

    public void testFeatureCollectionWithNonWGS84CRSWrite() throws Exception {
        String json =
                "{"
                        + "  'type': 'FeatureCollection',"
                        + "  'crs': {"
                        + "    'type': 'name',"
                        + "    'properties': {"
                        + "      'name': 'EPSG:3857'"
                        + "    }"
                        + "  },"
                        + "  'features': ["
                        + "    {"
                        + "      'type': 'Feature',"
                        + "      'geometry': {"
                        + "        'type': 'Point', "
                        + "        'coordinates': [2.003750834E7, 2.003750834E7]"
                        + "      },"
                        + "      'properties': {"
                        + "      },"
                        + "      'id': 'xyz.1'"
                        + "    }"
                        + "  ]"
                        + "}";

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.add("geom", Point.class, CRS.decode("EPSG:3857"));
        tb.add("name", String.class);
        tb.setName("xyz");
        SimpleFeatureType schema = tb.buildFeatureType();

        DefaultFeatureCollection fc = new DefaultFeatureCollection();

        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(schema);
        fb.add(new WKTReader().read("POINT(20037508.34 20037508.34)"));
        fc.add(fb.buildFeature("xyz.1"));

        FeatureJSON fj = new FeatureJSON();
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        fj.writeFeatureCollection(fc, os);

        assertEquals(strip(json), os.toString());
    }

    public void testFeatureCollectionWithCRSRead() throws Exception {
        String json = collectionText(true, true);
        FeatureCollection fcol = fjson.readFeatureCollection(strip(collectionText(true, true)));
        assertNotNull(fcol.getSchema().getCoordinateReferenceSystem());

        FeatureIterator it = fcol.features();
        while (it.hasNext()) {
            assertNotNull(it.next().getType().getCoordinateReferenceSystem());
        }
    }

    public void testFeatureCollectionWithMissingAttributeRead() throws Exception {
        String collectionText = collectionText(true, true, false, true, false);
        SimpleFeatureType ftype = fjson.readFeatureCollectionSchema((strip(collectionText)), false);

        assertNotNull(ftype.getDescriptor("double"));
        assertEquals(Double.class, ftype.getDescriptor("double").getType().getBinding());
        assertNotNull(ftype.getDescriptor("int"));
        assertEquals(Long.class, ftype.getDescriptor("int").getType().getBinding());
        assertNotNull(ftype.getDescriptor("string"));
        assertEquals(String.class, ftype.getDescriptor("string").getType().getBinding());

        assertNotNull(ftype.getCoordinateReferenceSystem());

        fjson.setFeatureType(ftype);
        SimpleFeatureCollection fcol =
                (SimpleFeatureCollection) fjson.readFeatureCollection((strip(collectionText)));

        assertEquals(ftype, fcol.getSchema());

        FeatureIterator it = fcol.features();
        while (it.hasNext()) {
            assertEquals(ftype, it.next().getType());
        }
    }

    @Test
    public void testFeatureCollectionWithNullAttributeRead() throws Exception {
        String collectionText = collectionText(true, true, false, false, true);
        SimpleFeatureType ftype = fjson.readFeatureCollectionSchema((strip(collectionText)), true);

        // System.out.println("type: " + ftype);

        assertEquals(4, ftype.getAttributeCount());

        assertNotNull(ftype.getDescriptor("int"));
        assertEquals(Long.class, ftype.getDescriptor(1).getType().getBinding());
        assertNotNull(ftype.getDescriptor("double"));
        assertEquals(Double.class, ftype.getDescriptor(2).getType().getBinding());
        assertNotNull(ftype.getDescriptor("string"));
        assertEquals(String.class, ftype.getDescriptor(3).getType().getBinding());

        assertNotNull(ftype.getCoordinateReferenceSystem());

        fjson.setFeatureType(ftype);
        SimpleFeatureCollection fcol =
                (SimpleFeatureCollection) fjson.readFeatureCollection((strip(collectionText)));

        assertEquals(ftype, fcol.getSchema());

        FeatureIterator it = fcol.features();
        while (it.hasNext()) {
            assertEquals(ftype, it.next().getType());
        }
    }

    public void testFeatureCollectionWithNullAttributeAllFeaturesRead() throws Exception {
        String collectionText = collectionText(true, true, false, false, false, true);
        SimpleFeatureType ftype = fjson.readFeatureCollectionSchema((strip(collectionText)), false);

        assertNotNull(ftype.getDescriptor("double"));
        // type defaults to String as all values were null
        assertEquals(String.class, ftype.getDescriptor("double").getType().getBinding());
        assertNotNull(ftype.getDescriptor("int"));
        assertEquals(Long.class, ftype.getDescriptor("int").getType().getBinding());
        assertNotNull(ftype.getDescriptor("string"));
        assertEquals(String.class, ftype.getDescriptor("string").getType().getBinding());

        assertNotNull(ftype.getCoordinateReferenceSystem());

        fjson.setFeatureType(ftype);
        SimpleFeatureCollection fcol =
                (SimpleFeatureCollection) fjson.readFeatureCollection((strip(collectionText)));

        assertEquals(ftype, fcol.getSchema());

        FeatureIterator it = fcol.features();
        while (it.hasNext()) {
            assertEquals(ftype, it.next().getType());
        }
    }

    public void testFeatureCollectionWithCRSPostFeaturesRead() throws Exception {
        String json = collectionText(true, true);
        FeatureCollection fcol =
                fjson.readFeatureCollection(strip(collectionText(true, true, true, false, false)));
        assertNotNull(fcol.getSchema().getCoordinateReferenceSystem());

        FeatureIterator it = fcol.features();
        while (it.hasNext()) {
            assertNotNull(it.next().getType().getCoordinateReferenceSystem());
        }
    }

    public void testFeatureCollectionWithTypePostFeaturesRead() throws Exception {
        String json =
                strip(
                        "{ "
                                + "  'features' : [{ "
                                + "     'geometry' : { 'coordinates' : [ 17.633333, 59.85 ], 'type' : 'Point' },"
                                + "     'type' : 'Feature',"
                                + "     'properties' : { 'name' : 'Station' }"
                                + "  }],"
                                + "  'type' : 'FeatureCollection'"
                                + "}");
        FeatureCollection fcol = fjson.readFeatureCollection(json);
        FeatureIterator it = fcol.features();
        assertTrue(it.hasNext());

        SimpleFeature f = (SimpleFeature) it.next();
        assertTrue(
                new WKTReader()
                        .read("POINT (17.633333 59.85)")
                        .equals((Geometry) f.getDefaultGeometry()));
        assertEquals("Station", f.getAttribute("name"));
        it.close();
    }

    public void testEmptyFeatureCollection() throws Exception {
        String json = strip("{'type':'FeatureCollection','features':[]}");
        FeatureCollection fcol = fjson.readFeatureCollection(json);
        assertNull(fcol.getSchema());
        assertTrue(fcol.isEmpty());
    }

    public void testCRSWrite() throws Exception {
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        StringWriter writer = new StringWriter();
        fjson.writeCRS(crs, writer);

        assertEquals(strip(crsText()), writer.toString());
    }

    public void testCRSRead() throws Exception {
        Object crs = fjson.readCRS(reader(strip(crsText())));
        assertTrue(CRS.equalsIgnoreMetadata(CRS.decode("epsg:4326"), crs));
    }

    public void testFeatureCollectionWithNullBoundsWrite() throws Exception {
        DefaultFeatureCollection features =
                new DefaultFeatureCollection() {
                    @Override
                    public ReferencedEnvelope getBounds() {
                        return null;
                    }
                };
        features.add(feature(0));

        String json = fjson.toString(features);
    }

    public void testFeatureCollectionWithNullGeometrySchemaRead() throws Exception {
        String json =
                strip(
                        "{"
                                + "  'type': 'FeatureCollection',"
                                + "  'features': ["
                                + "    {"
                                + "      'type': 'Feature',"
                                + "      'geometry': null,"
                                + "      'properties': {"
                                + "      },"
                                + "      'id': 'xyz.1'"
                                + "    }"
                                + "  ]"
                                + "}");

        SimpleFeatureType type = fjson.readFeatureCollectionSchema(json, true);
        assertNull(type.getGeometryDescriptor());
    }

    public void testFeatureCollectionWithoutGeometrySchemaRead() throws Exception {
        String json =
                strip(
                        "{"
                                + "  'type': 'FeatureCollection',"
                                + "  'features': ["
                                + "    {"
                                + "      'type': 'Feature',"
                                + "      'properties': {"
                                + "      },"
                                + "      'id': 'xyz.1'"
                                + "    }"
                                + "  ]"
                                + "}");

        SimpleFeatureType type = fjson.readFeatureCollectionSchema(json, true);
        assertNull(type.getGeometryDescriptor());
    }

    public void testFeatureCollectionConflictingTypesSchemaRead() throws Exception {
        String json =
                strip(
                        "{"
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
                                + "        'prop': 'xyz'"
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

    public void testFeatureCollectionWithoutGeometryReadWriteFromFeatureSource() throws Exception {
        String json =
                strip(
                        "{"
                                + "  'type': 'FeatureCollection',"
                                + "  'features': ["
                                + "    {"
                                + "      'type': 'Feature',"
                                + "      'properties': {"
                                + "      },"
                                + "      'id': 'xyz.1'"
                                + "    }"
                                + "  ]"
                                + "}");

        SimpleFeatureSource fs = DataUtilities.source(fjson.readFeatureCollection(json));
        fjson.toString(fs.getFeatures());
    }

    public void testFeatureCollectionConflictingButInterchangeableTypesSchemaRead()
            throws Exception {
        String json =
                strip(
                        "{"
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
                                + "        'prop': 1.0"
                                + "      }"
                                + "    }"
                                + "  ]"
                                + "}");

        SimpleFeatureType type = fjson.readFeatureCollectionSchema(json, false);
        assertEquals(Double.class, type.getDescriptor("prop").getType().getBinding());
    }

    public void testFeatureCollectionWithIdPropertyReadWrite() throws Exception {

        String json =
                strip(
                        "{"
                                + "  'type': 'FeatureCollection',"
                                + "  'features': ["
                                + "    {"
                                + "      'type': 'Feature',"
                                + "      'properties': {"
                                + "         'id': 'one'"
                                + "      },"
                                + "      'id': 'xyz.1'"
                                + "    }"
                                + "  ]"
                                + "}");

        FeatureCollection fc = fjson.readFeatureCollection(json);
        assertNotNull(fc.getSchema().getDescriptor("id"));
        Feature feat = fc.features().next();
        assertEquals("one", feat.getProperty("id").getValue());
        assertEquals("xyz.1", feat.getIdentifier().getID());

        ByteArrayOutputStream os = new ByteArrayOutputStream();
        fjson.writeFeatureCollection(fc, os);

        assertEquals(json, os.toString());
    }

    String crsText() {
        return "{"
                + "    'type': 'name',"
                + "    'properties': {"
                + "       'name': 'EPSG:4326'"
                + "     }"
                + "}";
    }

    SimpleFeature feature(int val) {
        fb.add(val);
        fb.add(val + 0.1);
        fb.add(toString(val));
        fb.add(new GeometryFactory().createPoint(new Coordinate(val + 0.1, val + 0.1)));

        return fb.buildFeature("feature." + val);
    }

    SimpleFeature featureArray(int val) {
        fbArray.add(val);
        fbArray.add(val + 0.1);
        fbArray.add(toString(val));
        fbArray.add(new String[] {toString(val), toString(val + 1)});
        fbArray.add(new GeometryFactory().createPoint(new Coordinate(val + 0.1, val + 0.1)));

        return fbArray.buildFeature("feature." + val);
    }

    SimpleFeature featureMissingAttribute(int val) {
        fb.add(val);
        fb.add(val + 0.1);
        fb.add(new GeometryFactory().createPoint(new Coordinate(val + 0.1, val + 0.1)));

        return fb.buildFeature("feature." + val);
    }

    SimpleFeature featureNullAttribute(int val) {
        fb.add(val);
        fb.add(null);
        fb.add(toString(val));
        fb.add(new GeometryFactory().createPoint(new Coordinate(val + 0.1, val + 0.1)));

        return fb.buildFeature("feature." + val);
    }

    String featureText(int val) {
        return featureText(val, false, false);
    }

    String featureText(int val, boolean missingAttribute, boolean nullAttribute) {
        if (missingAttribute && nullAttribute) {
            throw new IllegalArgumentException(
                    "For tests, use only one of either missingAttribute or nullAttribute");
        }
        String text =
                "{"
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
                                : (nullAttribute
                                        ? ("     'double': null,")
                                        : ("     'double': " + (val + 0.1) + ",")))
                        + "     'string': '"
                        + toString(val)
                        + "'"
                        + "   },"
                        + "   'id':'feature."
                        + val
                        + "'"
                        + "}";

        return text;
    }

    String featureArrayText(int val, boolean missingAttribute, boolean nullAttribute) {
        if (missingAttribute && nullAttribute) {
            throw new IllegalArgumentException(
                    "For tests, use only one of either missingAttribute or nullAttribute");
        }
        String text =
                "{"
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
                                : (nullAttribute
                                        ? ("     'double': null,")
                                        : ("     'double': " + (val + 0.1) + ",")))
                        + "     'string': '"
                        + toString(val)
                        + "',"
                        + "'array': ['"
                        + toString(val)
                        + "','"
                        + toString(val + 1)
                        + "']"
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
                withBounds,
                withCRS,
                crsAfter,
                missingFirstFeatureAttribute,
                nullFirstFeatureAttribute,
                false);
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

    public void testKeyOrderInFeatureCollectionParsing() throws Exception {
        /* Test parsing of three variations of the same GeoJSON object. */

        /* input1 tests parsing when "type" occurs at the top of each sub-object */
        String input1 =
                "{"
                        + " \"type\": \"FeatureCollection\","
                        + " \"features\": [{"
                        + "  \"type\": \"Feature\","
                        + "  \"geometry\": {"
                        + "   \"type\": \"GeometryCollection\","
                        + "   \"geometries\": [{"
                        + "    \"type\": \"Polygon\","
                        + "    \"coordinates\": [[[100.0, 1.0],[101.0, 1.0],[100.5, 1.5],[100.0, 1.0]]]"
                        + "   }]"
                        + "  },"
                        + "  \"properties\": {}"
                        + " }]"
                        + "}";

        /* input2 tests parsing when "type" in a geometry of the geom collection occurs after "coordinates". */
        String input2 =
                "{"
                        + " \"type\": \"FeatureCollection\","
                        + " \"features\": [{"
                        + "  \"type\": \"Feature\","
                        + "  \"geometry\": {"
                        + "   \"type\": \"GeometryCollection\","
                        + "   \"geometries\": [{"
                        + "    \"coordinates\": [[[100.0, 1.0],[101.0, 1.0],[100.5, 1.5],[100.0, 1.0]]],"
                        + "    \"type\": \"Polygon\""
                        + "   }]"
                        + "  },"
                        + "  \"properties\": {}"
                        + " }]"
                        + "}";

        /* input3 is similar to input 2 but also tests parsing when "type" for the feature collection occurs after
         * "features".
         */
        String input3 =
                "{"
                        + " \"features\": [{"
                        + "  \"type\": \"Feature\","
                        + "  \"geometry\": {"
                        + "   \"type\": \"GeometryCollection\","
                        + "   \"geometries\": [{"
                        + "    \"coordinates\": [[[100.0, 1.0],[101.0, 1.0],[100.5, 1.5],[100.0, 1.0]]],"
                        + "    \"type\": \"Polygon\""
                        + "   }]"
                        + "  },"
                        + "  \"properties\": {}"
                        + " }],"
                        + " \"type\": \"FeatureCollection\""
                        + "}";

        GeometryFactory factory = new GeometryFactory();
        Point expectedLastPoint = factory.createPoint(new Coordinate(100.0, 1.0));

        /* test input 1 */
        FeatureCollection featureCollection = fjson.readFeatureCollection(input1);
        testKeyOrderInFeatureCollectionParsing_VerifyContents(featureCollection, expectedLastPoint);

        /* test input 2 */
        featureCollection = fjson.readFeatureCollection(input2);
        testKeyOrderInFeatureCollectionParsing_VerifyContents(featureCollection, expectedLastPoint);

        /* test input 3 */
        featureCollection = fjson.readFeatureCollection(input3);
        testKeyOrderInFeatureCollectionParsing_VerifyContents(featureCollection, expectedLastPoint);
    }

    /*
     * Helper function that specifically supports test case testParseFeatureCollectionKeyOrder
     */
    private final void testKeyOrderInFeatureCollectionParsing_VerifyContents(
            FeatureCollection featureCollection, Point expectedLastPoint) {
        assertNotNull(featureCollection);
        assertNotNull(expectedLastPoint);
        assertEquals(1, featureCollection.size(), 0);
        FeatureIterator fiter = featureCollection.features();
        assertTrue(fiter.hasNext());
        Feature feature = fiter.next();
        GeometryAttribute geomAttrib = feature.getDefaultGeometryProperty();
        Object collectionObj = geomAttrib.getValue();
        assertTrue(collectionObj instanceof GeometryCollection);
        GeometryCollection geomCollection = (GeometryCollection) collectionObj;
        assertEquals(1, geomCollection.getNumGeometries());
        Object geomObj = geomCollection.getGeometryN(0);
        assertTrue(geomObj instanceof Polygon);
        Polygon polygon = (Polygon) geomObj;
        assertEquals(4, polygon.getNumPoints());
        assertEquals(1, polygon.getNumGeometries(), 0);
        LineString outerBoundary = polygon.getExteriorRing();
        assertEquals(4, outerBoundary.getNumPoints());
        Point lastPoint = outerBoundary.getPointN(3);
        assertTrue(lastPoint.equalsExact(expectedLastPoint));
        assertFalse(fiter.hasNext());
    }

    public void testParseCrsAttribute() throws Exception {
        String json =
                strip(
                        "{\n"
                                + "  \"type\": \"Feature\",\n"
                                + "  \"geometry\": {\n"
                                + "      \"type\":\"Polygon\",\n"
                                + "      \"coordinates\":[[[-180,-90],[180,-90],[180,90],[-180,90],[-180, -90]]]\n"
                                + "  },\n"
                                + "  \"properties\": {\n"
                                + "    \"eop:identifier\" : \"S2A_OPER_MSI_L1C_TL_SGS__20180101T000000_A006640_T32TPP_N02.04\",\n"
                                + "    \"timeStart\" : \"2018-01-01T00:00:00Z\",\n"
                                + "    \"timeEnd\" : \"2018-01-01T00:00:00Z\",\n"
                                + "    \"originalPackageLocation\" : \"/var/data/sentinel2/2017/04/23/S2A_OPER_MSI_L1C_TL_SGS__20180101T000000_A006640_T32TPP_N02.04.zip\",\n"
                                + "    \"thumbnailURL\" : null,\n"
                                + "    \"quicklookURL\" : null,\n"
                                + "    \"crs\" : \"EPSG:32632\",\n"
                                + "  }\n"
                                + "}");
        SimpleFeature sf = fjson.readFeature(json);
        assertNotNull(sf);
        assertEquals("EPSG:32632", sf.getAttribute("crs"));
    }
}
