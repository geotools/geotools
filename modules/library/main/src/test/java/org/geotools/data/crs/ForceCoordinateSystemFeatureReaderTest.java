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
package org.geotools.data.crs;

import static org.junit.Assert.assertNotEquals;

import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.DataUtilities;
import org.geotools.api.data.FeatureReader;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class ForceCoordinateSystemFeatureReaderTest {

    private static final String FEATURE_TYPE_NAME = "testType";

    @Before
    public void setUp() throws Exception {}

    /**
     * create a feature collection with 1 feature in it.
     *
     * @param crs the CRS of the FeatureType
     * @param p the point to add, should be same CRS as crs
     */
    private SimpleFeatureCollection createTestFeatureCollection(
            CoordinateReferenceSystem crs, Point p) throws Exception {

        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName(FEATURE_TYPE_NAME);
        builder.setCRS(crs);
        builder.add("geom", Point.class);

        SimpleFeatureType ft = builder.buildFeatureType();

        SimpleFeatureBuilder b = new SimpleFeatureBuilder(ft);
        b.add(p);

        ListFeatureCollection features = new ListFeatureCollection(ft);
        features.add(b.buildFeature(null));

        return features;
    }

    @Test
    public void testSameCRS() throws Exception {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        GeometryFactory fac = new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10, 10));

        SimpleFeatureCollection features = createTestFeatureCollection(crs, p);
        try (FeatureReader<SimpleFeatureType, SimpleFeature> original =
                        DataUtilities.reader(features);
                ForceCoordinateSystemFeatureReader modified =
                        new ForceCoordinateSystemFeatureReader(
                                DataUtilities.reader(features), crs)) {
            SimpleFeature f1 = original.next();
            SimpleFeature f2 = modified.next();

            Assert.assertEquals(f1, f2);

            Assert.assertFalse(original.hasNext());
            Assert.assertFalse(modified.hasNext());

            Assert.assertSame(modified.builder.getFeatureType(), original.getFeatureType());
        }
    }

    @Test
    public void testDifferentCRS() throws Exception {
        CoordinateReferenceSystem srcCRS = DefaultGeographicCRS.WGS84;
        GeometryFactory fac = new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10, 10));

        SimpleFeatureCollection features = createTestFeatureCollection(srcCRS, p);
        CoordinateReferenceSystem destCRS = DefaultEngineeringCRS.CARTESIAN_2D;
        try (FeatureReader<SimpleFeatureType, SimpleFeature> original =
                        DataUtilities.reader(features);
                ForceCoordinateSystemFeatureReader modified =
                        new ForceCoordinateSystemFeatureReader(
                                DataUtilities.reader(features), destCRS); ) {
            SimpleFeature f1 = original.next();
            SimpleFeature f2 = modified.next();

            Assert.assertEquals(
                    ((Geometry) f1.getDefaultGeometry()).getCoordinate(),
                    ((Geometry) f2.getDefaultGeometry()).getCoordinate());
            SimpleFeatureType f1Type = f1.getFeatureType();
            SimpleFeatureType f2Type = f2.getFeatureType();
            assertNotEquals(
                    f1Type.getCoordinateReferenceSystem(), f2Type.getCoordinateReferenceSystem());
            Assert.assertEquals(srcCRS, f1Type.getCoordinateReferenceSystem());
            Assert.assertEquals(
                    srcCRS, f1Type.getGeometryDescriptor().getCoordinateReferenceSystem());
            Assert.assertEquals(destCRS, f2Type.getCoordinateReferenceSystem());
            Assert.assertEquals(
                    destCRS, f2Type.getGeometryDescriptor().getCoordinateReferenceSystem());

            Assert.assertFalse(original.hasNext());
            Assert.assertFalse(modified.hasNext());

            Assert.assertNotNull(modified.builder);
        }
    }

    @Test
    public void testNullDestination() throws Exception {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        GeometryFactory fac = new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10, 10));

        SimpleFeatureCollection features = createTestFeatureCollection(crs, p);

        try {
            new ForceCoordinateSystemFeatureReader(
                    DataUtilities.reader(features), (CoordinateReferenceSystem) null);
            Assert.fail(); // should throw a nullpointer exception.
        } catch (NullPointerException e) {
            // good
        }
    }

    @Test
    public void testNullSource() throws Exception {
        CoordinateReferenceSystem srcCRS = null;
        GeometryFactory fac = new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10, 10));

        SimpleFeatureCollection features = createTestFeatureCollection(srcCRS, p);
        CoordinateReferenceSystem destCRS = DefaultEngineeringCRS.CARTESIAN_2D;
        try (FeatureReader<SimpleFeatureType, SimpleFeature> original =
                        DataUtilities.reader(features);
                ForceCoordinateSystemFeatureReader modified =
                        new ForceCoordinateSystemFeatureReader(
                                DataUtilities.reader(features), destCRS)) {
            SimpleFeature f1 = original.next();
            SimpleFeature f2 = modified.next();

            Assert.assertEquals(
                    ((Geometry) f1.getDefaultGeometry()).getCoordinate(),
                    ((Geometry) f2.getDefaultGeometry()).getCoordinate());
            SimpleFeatureType f1Type = f1.getFeatureType();
            SimpleFeatureType f2Type = f2.getFeatureType();
            assertNotEquals(
                    f2Type.getCoordinateReferenceSystem(), f1Type.getCoordinateReferenceSystem());
            Assert.assertEquals(srcCRS, f1Type.getCoordinateReferenceSystem());
            Assert.assertEquals(
                    srcCRS, f1Type.getGeometryDescriptor().getCoordinateReferenceSystem());
            Assert.assertEquals(destCRS, f2Type.getCoordinateReferenceSystem());
            Assert.assertEquals(
                    destCRS, f2Type.getGeometryDescriptor().getCoordinateReferenceSystem());

            Assert.assertFalse(original.hasNext());
            Assert.assertFalse(modified.hasNext());

            Assert.assertNotNull(modified.builder);
        }
    }
}
