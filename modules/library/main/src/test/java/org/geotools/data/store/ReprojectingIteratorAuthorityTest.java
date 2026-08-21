/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.store;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.GeometryCoordinateSequenceTransformer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.geotools.util.factory.FactoryIteratorProvider;
import org.geotools.util.factory.GeoTools;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

/**
 * Checks that the reprojecting iterators resolve the coordinate operation through every registered factory, and so keep
 * the EPSG defined transformation when the highest priority factory has no answer for a CRS pair.
 *
 * <p>These iterators used to ask for a single operation factory, so a maximum priority factory that knows nothing about
 * the pair hid the EPSG operation, leaving the datum shift out of the reprojection.
 */
public class ReprojectingIteratorAuthorityTest {

    /** MGI (Ferro), datum without Bursa-Wolf parameters, reachable only through EPSG:1618. */
    private static final String FERRO = "EPSG:4805";

    private static final double LONGITUDE = 32.54;
    private static final double LATITUDE = 47.24;

    /** Result of EPSG:1618, as opposed to (14.873333333333331, 47.240596419075324) with the shift dropped. */
    private static final double EXPECTED_LONGITUDE = 14.872403673695622;

    private static final double EXPECTED_LATITUDE = 47.2395619253438;

    /** Tolerance in degrees, three orders of magnitude below the distance to the shift-less answer. */
    private static final double TOL = 1e-7;

    private static FactoryIteratorProvider factoryProvider;

    /** Registers a factory that outranks the default one and answers nothing, like a user configured one. */
    @BeforeClass
    public static void registerBlockingFactory() {
        factoryProvider = new FactoryIteratorProvider() {
            @Override
            @SuppressWarnings("unchecked")
            public <T> Iterator<T> iterator(Class<T> category) {
                if (CoordinateOperationFactory.class == category) {
                    return List.of((T) new BlockingOperationFactory()).iterator();
                }
                return null;
            }
        };
        GeoTools.addFactoryIteratorProvider(factoryProvider);
        CRS.reset("all");
    }

    @AfterClass
    public static void deregisterBlockingFactory() {
        GeoTools.removeFactoryIteratorProvider(factoryProvider);
        CRS.reset("all");
    }

    @Test
    public void testFeatureIteratorUsesAuthorityOperation() throws Exception {
        SimpleFeature source = feature();
        try (SimpleFeatureIterator iterator = new ReprojectingFeatureIterator(
                new SingleFeatureIterator(source),
                source.getFeatureType().getCoordinateReferenceSystem(),
                DefaultGeographicCRS.WGS84,
                targetType(source.getFeatureType()),
                new GeometryCoordinateSequenceTransformer())) {
            assertReprojected(iterator.next());
        }
    }

    @Test
    public void testIteratorUsesAuthorityOperation() throws Exception {
        SimpleFeature source = feature();
        ReprojectingIterator iterator = new ReprojectingIterator(
                List.of(source).iterator(),
                source.getFeatureType().getCoordinateReferenceSystem(),
                DefaultGeographicCRS.WGS84,
                targetType(source.getFeatureType()),
                new GeometryCoordinateSequenceTransformer());
        assertReprojected(iterator.next());
    }

    private static void assertReprojected(SimpleFeature reprojected) {
        Point point = (Point) reprojected.getDefaultGeometry();
        assertEquals("longitude", EXPECTED_LONGITUDE, point.getX(), TOL);
        assertEquals("latitude", EXPECTED_LATITUDE, point.getY(), TOL);
    }

    private static SimpleFeature feature() throws Exception {
        CoordinateReferenceSystem ferro = CRS.decode(FERRO, true);
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("ferro");
        builder.setCRS(ferro);
        builder.add("geom", Point.class);
        SimpleFeatureType type = builder.buildFeatureType();

        Point point = new GeometryFactory().createPoint(new Coordinate(LONGITUDE, LATITUDE));
        return SimpleFeatureBuilder.build(type, new Object[] {point}, "ferro.1");
    }

    private static SimpleFeatureType targetType(SimpleFeatureType sourceType) throws SchemaException {
        return SimpleFeatureTypeBuilder.retype(sourceType, DefaultGeographicCRS.WGS84);
    }

    /** Highest priority factory with no operations of its own, forcing a fall through to the EPSG authority. */
    private static class BlockingOperationFactory extends DefaultCoordinateOperationFactory {

        BlockingOperationFactory() {
            super(null, MAXIMUM_PRIORITY);
        }

        @Override
        public Set<CoordinateOperation> findFromDatabase(
                CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS, int limit) {
            return Set.of();
        }

        @Override
        protected CoordinateOperation createFromDatabase(
                CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS) {
            return null;
        }
    }

    /** Minimal iterator over one feature, to avoid pulling in a data store. */
    private static class SingleFeatureIterator implements SimpleFeatureIterator {

        private SimpleFeature feature;

        SingleFeatureIterator(SimpleFeature feature) {
            this.feature = feature;
        }

        @Override
        public boolean hasNext() {
            return feature != null;
        }

        @Override
        public SimpleFeature next() {
            SimpleFeature next = feature;
            feature = null;
            return next;
        }

        @Override
        public void close() {
            feature = null;
        }
    }
}
