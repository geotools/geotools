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

import junit.framework.TestCase;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class ForceCoordinateSystemFeatureIteratorTest extends TestCase {

    private static final String FEATURE_TYPE_NAME = "testType";

    protected void setUp() throws Exception {
        super.setUp();
    }

    /**
     * create a datastore with 1 feature in it.
     *
     * @param crs the CRS of the featuretype
     * @param p the point to add, should be same CRS as crs
     */
    private SimpleFeatureCollection createDatastore(CoordinateReferenceSystem crs, Point p)
            throws Exception {

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

    public void testSameCRS() throws Exception {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        GeometryFactory fac = new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10, 10));

        SimpleFeatureCollection collection = createDatastore(crs, p);

        SimpleFeatureIterator original = collection.features();

        ForceCoordinateSystemIterator modified =
                new ForceCoordinateSystemIterator(
                        collection.features(), collection.getSchema(), crs);

        SimpleFeature f1 = original.next();
        SimpleFeature f2 = modified.next();

        assertEquals(f1, f2);

        assertFalse(original.hasNext());
        assertFalse(modified.hasNext());
    }

    public void testDifferentCRS() throws Exception {
        CoordinateReferenceSystem srcCRS = DefaultGeographicCRS.WGS84;
        GeometryFactory fac = new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10, 10));

        SimpleFeatureCollection collection = createDatastore(srcCRS, p);
        SimpleFeatureIterator original = collection.features();
        CoordinateReferenceSystem destCRS = DefaultEngineeringCRS.CARTESIAN_2D;
        ForceCoordinateSystemIterator modified =
                new ForceCoordinateSystemIterator(
                        collection.features(), collection.getSchema(), destCRS);

        SimpleFeature f1 = original.next();
        SimpleFeature f2 = modified.next();

        assertEquals(
                ((Geometry) f1.getDefaultGeometry()).getCoordinate(),
                ((Geometry) f2.getDefaultGeometry()).getCoordinate());
        assertFalse(
                f1.getFeatureType()
                        .getCoordinateReferenceSystem()
                        .equals(f2.getFeatureType().getCoordinateReferenceSystem()));
        assertEquals(srcCRS, f1.getFeatureType().getCoordinateReferenceSystem());
        assertEquals(
                srcCRS, f1.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem());
        assertEquals(destCRS, f2.getFeatureType().getCoordinateReferenceSystem());
        assertEquals(
                destCRS,
                f2.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem());

        assertFalse(original.hasNext());
        assertFalse(modified.hasNext());

        assertNotNull(modified.builder);
    }

    public void testNullDestination() throws Exception {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        GeometryFactory fac = new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10, 10));

        SimpleFeatureCollection collection = createDatastore(crs, p);

        try {
            new ForceCoordinateSystemIterator(
                    collection.features(),
                    collection.getSchema(),
                    (CoordinateReferenceSystem) null);
            fail(); // should throw a nullpointer exception.
        } catch (NullPointerException e) {
            // good
        }
    }

    public void testNullSource() throws Exception {
        CoordinateReferenceSystem srcCRS = null;
        GeometryFactory fac = new GeometryFactory();
        Point p = fac.createPoint(new Coordinate(10, 10));

        SimpleFeatureCollection collection = createDatastore(srcCRS, p);

        SimpleFeatureIterator original = collection.features();
        CoordinateReferenceSystem destCRS = DefaultEngineeringCRS.CARTESIAN_2D;
        ForceCoordinateSystemIterator modified =
                new ForceCoordinateSystemIterator(
                        collection.features(), collection.getSchema(), destCRS);

        SimpleFeature f1 = original.next();
        SimpleFeature f2 = modified.next();

        assertEquals(
                ((Geometry) f1.getDefaultGeometry()).getCoordinate(),
                ((Geometry) f2.getDefaultGeometry()).getCoordinate());
        assertFalse(
                f2.getFeatureType()
                        .getCoordinateReferenceSystem()
                        .equals(f1.getFeatureType().getCoordinateReferenceSystem()));
        assertEquals(srcCRS, f1.getFeatureType().getCoordinateReferenceSystem());
        assertEquals(
                srcCRS, f1.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem());
        assertEquals(destCRS, f2.getFeatureType().getCoordinateReferenceSystem());
        assertEquals(
                destCRS,
                f2.getFeatureType().getGeometryDescriptor().getCoordinateReferenceSystem());

        assertFalse(original.hasNext());
        assertFalse(modified.hasNext());

        assertNotNull(modified.builder);
    }
}
