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
package org.geotools.coverage.processing.operation;

import static org.junit.Assert.assertEquals;

import java.awt.image.BufferedImage;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.CoordinateOperation;
import org.geotools.api.referencing.operation.CoordinateOperationFactory;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.processing.Operations;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.DefaultCoordinateOperationFactory;
import org.geotools.util.factory.FactoryIteratorProvider;
import org.geotools.util.factory.GeoTools;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Checks that coverage reprojection resolves coordinate operations through every registered factory, and so keeps the
 * EPSG defined transformation when the highest priority factory has no answer for a CRS pair. The resampler used to ask
 * for a single operation factory, so a maximum priority factory that knows nothing about the pair hid the EPSG
 * definitions and reprojection failed outright.
 */
public class ResampleAuthorityOperationTest {

    /** MGI (Ferro), datum without Bursa-Wolf parameters, reachable only through EPSG:1618. */
    private static final String FERRO = "EPSG:4805";

    /**
     * Bounds of the resampled coverage under EPSG:1618. Dropping the datum shift instead would give (14.73333333333333,
     * 15.033333333333335, 47.10059664535848, 47.40059614303223), about a thousandth of a degree away, some 100 m, so
     * the choice of operation is unmistakable.
     */
    private static final double[] EXPECTED_BOUNDS = {
        14.732411172381335, 15.032392129186864, 47.09957369039259, 47.39954850383027
    };

    private static final double TOL = 1e-9;

    /** PROJ reaches ETRS89 directly for the Belgian pair, GeoTools pivots through WGS84, 4 mm apart. */
    private static final double CENTIMETRE = 0.01;

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
    public void testResampleUsesAuthorityOperation() throws Exception {
        CoordinateReferenceSystem ferro = CRS.decode(FERRO, true);
        BufferedImage image = new BufferedImage(30, 30, BufferedImage.TYPE_BYTE_GRAY);
        GridCoverage2D coverage =
                new GridCoverageFactory().create("ferro", image, new ReferencedEnvelope(32.4, 32.7, 47.1, 47.4, ferro));

        GridCoverage2D resampled = (GridCoverage2D) Operations.DEFAULT.resample(coverage, DefaultGeographicCRS.WGS84);

        ReferencedEnvelope bounds = resampled.getEnvelope2D();
        assertEquals("minimum longitude", EXPECTED_BOUNDS[0], bounds.getMinX(), TOL);
        assertEquals("maximum longitude", EXPECTED_BOUNDS[1], bounds.getMaxX(), TOL);
        assertEquals("minimum latitude", EXPECTED_BOUNDS[2], bounds.getMinY(), TOL);
        assertEquals("maximum latitude", EXPECTED_BOUNDS[3], bounds.getMaxY(), TOL);
    }

    /**
     * The reprojection reported as broken, Belgian Lambert 72 to Belgian Lambert 2008, with a user configured factory
     * installed as it was on the reporting server.
     *
     * <p>Bounds from {@code cs2cs -f "%.6f" EPSG:31370 EPSG:3812} on the envelope corners.
     */
    @Test
    public void testReportedBelgianReprojection() throws Exception {
        CoordinateReferenceSystem lambert72 = CRS.decode("EPSG:31370", true);
        BufferedImage image = new BufferedImage(50, 50, BufferedImage.TYPE_BYTE_GRAY);
        GridCoverage2D coverage = new GridCoverageFactory()
                .create("l72", image, new ReferencedEnvelope(150000, 151000, 170000, 171000, lambert72));

        GridCoverage2D resampled =
                (GridCoverage2D) Operations.DEFAULT.resample(coverage, CRS.decode("EPSG:3812", true));

        ReferencedEnvelope bounds = resampled.getEnvelope2D();
        assertEquals("minimum easting", 649999.567705, bounds.getMinX(), CENTIMETRE);
        assertEquals("maximum easting", 650999.678808, bounds.getMaxX(), CENTIMETRE);
        assertEquals("minimum northing", 670000.411639, bounds.getMinY(), CENTIMETRE);
        assertEquals("maximum northing", 671000.522724, bounds.getMaxY(), CENTIMETRE);
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
}
