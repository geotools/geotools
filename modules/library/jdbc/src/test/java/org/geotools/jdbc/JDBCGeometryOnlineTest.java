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
package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.DataUtilities;
import org.geotools.data.store.ContentFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.WKTReader2;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * Tests that geometry types can be properly created and retrieved back from the database. You might
 * need to override some of the tests method to fix the expectations for specific geometry class
 * types.
 */
public abstract class JDBCGeometryOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCGeometryTestSetup createTestSetup();

    @Test
    public void testPoint() throws Exception {
        assertEquals(Point.class, checkGeometryType(Point.class));
    }

    @Test
    public void testLineString() throws Exception {
        assertEquals(LineString.class, checkGeometryType(LineString.class));
    }

    @Test
    public void testLinearRing() throws Exception {
        assertEquals(LinearRing.class, checkGeometryType(LinearRing.class));
    }

    @Test
    public void testPolygon() throws Exception {
        assertEquals(Polygon.class, checkGeometryType(Polygon.class));
    }

    @Test
    public void testMultiPoint() throws Exception {
        assertEquals(MultiPoint.class, checkGeometryType(MultiPoint.class));
    }

    @Test
    public void testMultiLineString() throws Exception {
        assertEquals(MultiLineString.class, checkGeometryType(MultiLineString.class));
    }

    @Test
    public void testMultiPolygon() throws Exception {
        assertEquals(MultiPolygon.class, checkGeometryType(MultiPolygon.class));
    }

    @Test
    public void testMultiSurfaceLinearized() throws Exception {
        String featureTypeName = "tMultiPolygon";

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName("tMultiPolygon");
        ftb.add(aname("id"), Integer.class);
        ftb.add(aname("name"), String.class);
        ftb.add(aname("geom"), MultiPolygon.class, CRS.decode("EPSG:4326"));

        SimpleFeatureType ft = ftb.buildFeatureType();
        dataStore.createSchema(ft);

        SimpleFeatureBuilder sfb = new SimpleFeatureBuilder(ft);
        WKTReader2 reader = new WKTReader2();
        sfb.set("name", "the name");
        sfb.set("geom", reader.read("MultiSurface (((1 0, 2 0, 2 1, 1 1, 1 0)))"));

        ContentFeatureStore store =
                (ContentFeatureStore) dataStore.getFeatureSource(featureTypeName);
        store.addFeatures(DataUtilities.collection(sfb.buildFeature("1")));
    }

    /** Sometimes the source cannot anticipate the geometry type, can we cope with this? */
    @Test
    public void testGeometry() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(Geometry.class));
    }

    /** Same goes for heterogeneous collections */
    @Test
    public void testGeometryCollection() throws Exception {
        assertEquals(GeometryCollection.class, checkGeometryType(GeometryCollection.class));
    }

    protected Class checkGeometryType(Class geomClass) throws Exception {
        // we just prefix the table name with "t" otherwise we go beyond
        // the Oracle identifier max length limit (oh my my...)
        String featureTypeName = tname("t" + geomClass.getSimpleName());

        // create a featureType and write it to PostGIS
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");

        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setName(featureTypeName);
        ftb.add(aname("id"), Integer.class);
        ftb.add(aname("name"), String.class);
        ftb.add(aname("geom"), geomClass, crs);

        SimpleFeatureType newFT = ftb.buildFeatureType();
        dataStore.createSchema(newFT);

        SimpleFeatureType newSchema = dataStore.getSchema(featureTypeName);
        assertNotNull(newSchema);
        assertEquals(3, newSchema.getAttributeCount());
        return newSchema.getGeometryDescriptor().getType().getBinding();
    }
}
