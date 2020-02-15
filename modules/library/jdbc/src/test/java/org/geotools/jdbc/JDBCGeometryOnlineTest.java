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

import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Tests that geometry types can be properly created and retrieved back from the database. You might
 * need to override some of the tests method to fix the expectations for specific geometry class
 * types.
 */
public abstract class JDBCGeometryOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCGeometryTestSetup createTestSetup();

    public void testPoint() throws Exception {
        assertEquals(Point.class, checkGeometryType(Point.class));
    }

    public void testLineString() throws Exception {
        assertEquals(LineString.class, checkGeometryType(LineString.class));
    }

    public void testLinearRing() throws Exception {
        assertEquals(LinearRing.class, checkGeometryType(LinearRing.class));
    }

    public void testPolygon() throws Exception {
        assertEquals(Polygon.class, checkGeometryType(Polygon.class));
    }

    public void testMultiPoint() throws Exception {
        assertEquals(MultiPoint.class, checkGeometryType(MultiPoint.class));
    }

    public void testMultiLineString() throws Exception {
        assertEquals(MultiLineString.class, checkGeometryType(MultiLineString.class));
    }

    public void testMultiPolygon() throws Exception {
        assertEquals(MultiPolygon.class, checkGeometryType(MultiPolygon.class));
    }

    /** Sometimes the source cannot anticipate the geometry type, can we cope with this? */
    public void testGeometry() throws Exception {
        assertEquals(Geometry.class, checkGeometryType(Geometry.class));
    }

    /** Same goes for heterogeneous collections */
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
