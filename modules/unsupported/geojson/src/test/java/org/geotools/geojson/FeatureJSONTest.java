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
import java.util.Iterator;
import java.util.List;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.io.WKTReader;

public class FeatureJSONTest extends GeoJSONTestSupport {

    FeatureJSON fjson = new FeatureJSON();
    SimpleFeatureType featureType;
    SimpleFeatureBuilder fb;
    
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("feature");
        tb.setSRS("EPSG:4326");
        tb.add("int", Integer.class);
        tb.add("double", Double.class);
        tb.add("string", String.class);
        tb.add("geometry", Geometry.class);
        
        featureType = tb.buildFeatureType();
        fb = new SimpleFeatureBuilder(featureType);
    }
        
    public void testFeatureWrite() throws Exception {
        
        StringWriter writer = new StringWriter();
        fjson.writeFeature(feature(1), writer);
        
        assertEquals(strip(featureText(1)), writer.toString());
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
        assertEquals(feature.getID(), feature2.getID());
    }
    
    public void testFeatureRead() throws Exception {
        SimpleFeature f1 = feature(1);
        SimpleFeature f2 = fjson.readFeature(reader(strip(featureText(1)))); 
        assertEqualsLax(f1, f2);
    }
    
    public void testFeatureWithRegularGeometryAttributeRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip("{" + 
        "   'type': 'Feature'," +
        "   'geometry': {" +
        "     'type': 'Point'," +
        "     'coordinates': [0.1, 0.1]," +
        "   }," +
        "   'properties': {" +
        "     'int': 1," +
        "     'double': 0.1," +
        "     'string': 'one'," +
        "     'otherGeometry': {" +
        "        'type': 'LineString'," +
        "        'coordinates': [[1.1, 1.2], [1.3, 1.4]]" +
        "     }"+
        "   }," +
        "   'id': 'feature.0'" +
        " }")));
        
        assertNotNull(f);
        assertTrue(f.getDefaultGeometry() instanceof Point);
        
        Point p = (Point) f.getDefaultGeometry();
        assertEquals(0.1, p.getX(), 0.1);
        assertEquals(0.1, p.getY(), 0.1);
        
        assertTrue(f.getAttribute("otherGeometry") instanceof LineString);
        assertTrue(new GeometryFactory().createLineString(new Coordinate[]{
            new Coordinate(1.1, 1.2), new Coordinate(1.3, 1.4)}).equals((LineString)f.getAttribute("otherGeometry")));
        
        assertEquals(1, ((Number)f.getAttribute("int")).intValue());
        assertEquals(0.1, ((Number)f.getAttribute("double")).doubleValue());
        assertEquals("one", f.getAttribute("string"));
    }
    
    public void testFeatureWithRegularGeometryAttributeNoDefaultGeometryRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip("{" + 
        "   'type': 'Feature'," +
        "   'properties': {" +
        "     'int': 1," +
        "     'double': 0.1," +
        "     'string': 'one'," +
        "     'otherGeometry': {" +
        "        'type': 'LineString'," +
        "        'coordinates': [[1.1, 1.2], [1.3, 1.4]]" +
        "     }"+
        "   }," +
        "   'id': 'feature.0'" +
        " }")));
        
        assertNotNull(f);
        assertTrue(f.getDefaultGeometry() instanceof LineString);
        
        LineString l = (LineString) f.getDefaultGeometry();
        assertTrue(new GeometryFactory().createLineString(new Coordinate[]{
                new Coordinate(1.1, 1.2), new Coordinate(1.3, 1.4)}).equals(l));
        
        assertTrue(f.getAttribute("otherGeometry") instanceof LineString);
        assertTrue(new GeometryFactory().createLineString(new Coordinate[]{
            new Coordinate(1.1, 1.2), new Coordinate(1.3, 1.4)}).equals((LineString)f.getAttribute("otherGeometry")));
        
        assertEquals(1, ((Number)f.getAttribute("int")).intValue());
        assertEquals(0.1, ((Number)f.getAttribute("double")).doubleValue());
        assertEquals("one", f.getAttribute("string"));
    }
    
    
    
    public void testFeatureWithBoundsWrite() throws Exception {
        String json = 
            "{" + 
            "   'type': 'Feature'," +
            "   'bbox': [1.1, 1.1, 1.1, 1.1], " + 
            "   'geometry': {" +
            "     'type': 'Point'," +
            "     'coordinates': [1.1, 1.1]" +
            "   }," +
            "   'properties': {" +
            "     'int': 1," +
            "     'double': 1.1," +
            "     'string': 'one'" +
            "   }," +
            "   'id': 'feature.1'" +
            " }";
        
        fjson.setEncodeFeatureBounds(true);
        assertEquals(strip(json), fjson.toString(feature(1)));
    }
    
    public void testFeatureWithCRSWrite() throws Exception {
        fjson.setEncodeFeatureCRS(true);
        assertEquals(strip(featureWithCRSText()), fjson.toString(feature(1)));
    }

    public void testFeatureNoGeometryWrite() throws Exception {
        String json = 
            "{" + 
            "   'type': 'Feature'," +
            "   'properties': {" +
            "     'foo': 'FOO'" +
            "   }," +
            "   'id': 'feature.foo'" +
            " }";
        
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
            "{" + 
            "   'type': 'Feature'," +
            "   'crs': {" +
            "     'type': 'name'," +
            "     'properties': {" +
            "       'name': 'EPSG:4326'" + 
            "     }" +
            "   }, " + 
            "   'geometry': {" +
            "     'type': 'Point'," +
            "     'coordinates': [1.1, 1.1]" +
            "   }," +
            "   'properties': {" +
            "     'int': 1," +
            "     'double': 1.1," +
            "     'string': 'one'" +
            "   }," +
            "   'id': 'feature.1'" +
            " }";
        return json;
    }

    public void testFeatureWithCRSRead() throws Exception {
        SimpleFeature f = fjson.readFeature(reader(strip(featureWithCRSText())));
        assertTrue(CRS.equalsIgnoreMetadata(CRS.decode("EPSG:4326"), 
            f.getFeatureType().getCoordinateReferenceSystem()));
    }
    
    String featureWithBBOXText() {
        String json = 
            "{" + 
            "   'type': 'Feature'," +
            "   'bbox': [1.1, 1.1, 1.1, 1.1]," +
            "   'geometry': {" +
            "     'type': 'Point'," +
            "     'coordinates': [1.1, 1.1]" +
            "   }," +
            "   'properties': {" +
            "     'int': 1," +
            "     'double': 1.1," +
            "     'string': 'one'" +
            "   }," +
            "   'id': 'feature.1'" +
            " }";
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
            "{" + 
            "   'type': 'Feature'," +
            "   'geometry': {" +
            "     'type': 'Point'," +
            "     'coordinates': [1.1, 1.1]" +
            "   }," +
            "   'properties': {" +
            "     'boundedBy': [-1.2, -1.3, 1.2, 1.3]," +
            "     'int': 1," +
            "     'double': 1.1," +
            "     'string': 'one'" +
            "   }," +
            "   'id': 'feature.1'" +
            " }";
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
        b.add(new GeometryFactory().createPoint(new Coordinate(1.1,1.1)));
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
        
        FeatureCollection actual = 
            fjson.readFeatureCollection(reader(strip(collectionText())));
        assertNotNull(actual);
        
        FeatureCollection expected = collection();
        assertEquals(expected.size(), actual.size());
        
        Iterator a = actual.iterator();
        Iterator e = expected.iterator();
        
        while(e.hasNext()) {
            assertTrue(a.hasNext());
            assertEqualsLax((SimpleFeature)e.next(), (SimpleFeature) a.next());
        }
        
        actual.close(a);
        expected.close(e);
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
        Iterator e = expected.iterator();
        
        while(e.hasNext()) {
            features.hasNext(); //ensure that hasNext() does not skip features
            assertTrue(features.hasNext());
            assertEqualsLax((SimpleFeature)e.next(), features.next());
        }
        
        features.close();
        expected.close(e);
    }

    public void testFeatureCollectionWithBoundsWrite() throws Exception {
        fjson.setEncodeFeatureCollectionBounds(true);
        assertEquals(strip(collectionText(true, false)), fjson.toString(collection()));
    }
    
    public void testFeatureCollectionWithCRSWrite() throws Exception {
        fjson.setEncodeFeatureCollectionCRS(true);
        assertEquals(strip(collectionText(false, true)), fjson.toString(collection()));
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
    
    String crsText() {
        return 
            "{" + 
            "    'type': 'name',"+
            "    'properties': {"+
            "       'name': 'EPSG:4326'"+
            "     }"+
            "}";
    }
    
    SimpleFeature feature(int val) {
        fb.add(val);
        fb.add(val + 0.1);
        fb.add(toString(val));
        fb.add(new GeometryFactory().createPoint(new Coordinate(val+0.1,val+0.1)));
        
        return fb.buildFeature("feature." + val);
    }
    
    String featureText(int val) {
        String text = 
        "{" +
        "  'type': 'Feature'," +
        "  'geometry': {" +
        "     'type': 'Point'," +
        "     'coordinates': [" + (val+0.1) + "," + (val+0.1) + "]" +
        "   }, " +
        "'  properties': {" +
        "     'int': " + val + "," +
        "     'double': " + (val + 0.1) + "," +
        "     'string': '" + toString(val) + "'" + 
        "   }," +
        "   'id':'feature." + val + "'" +
        "}";
        
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
        return collectionText(false,false);
    }
    
    String collectionText(boolean withBounds, boolean withCRS) {
        StringBuffer sb = new StringBuffer();
        sb.append("{'type':'FeatureCollection',");
        if (withBounds) {
            FeatureCollection features = collection();
            ReferencedEnvelope bbox = features.getBounds();
            sb.append("'bbox': [");
            sb.append(bbox.getMinX()).append(",").append(bbox.getMinY()).append(",")
                .append(bbox.getMaxX()).append(",").append(bbox.getMaxY());
            sb.append("],");
        }
        if (withCRS) {
            sb.append("'crs': {");
            sb.append("  'type': 'name',");
            sb.append("  'properties': {");
            sb.append("    'name': 'EPSG:4326'");
            sb.append("   }");
            sb.append("},");
        }
        sb.append("'features':[");
        for (int i = 0; i < 3; i++) {
            sb.append(featureText(i)).append(",");
        }
        sb.setLength(sb.length()-1);
        sb.append("]}");
        return sb.toString();
    }
    
}
