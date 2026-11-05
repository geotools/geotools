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
package org.geotools.referencing.operation.transform;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertSame;

import java.awt.geom.AffineTransform;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.builder.LocalizationGrid;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.projection.MapProjection;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

/** Checks the cached inverse of the transforms that compute it lazily. */
@RunWith(Parameterized.class)
public class InverseCacheTest {

    private static final double TOLERANCE = 1E-6;

    /** Not the shared factory, as its pool would hand out transforms that other tests already inverted. */
    private static final DefaultMathTransformFactory FACTORY = new DefaultMathTransformFactory();

    private static final double WGS84_A = 6378137, WGS84_B = 6356752.314245;
    private static final double CLARKE_A = 6378206.4, CLARKE_B = 6356583.8;

    private final Class<?> type;
    private final MathTransform transform;
    private final double[] point;

    public InverseCacheTest(String name, Class<?> type, MathTransform transform, double[] point) {
        this.type = type;
        this.transform = transform;
        this.point = point;
    }

    @Parameters(name = "{0}")
    public static List<Object[]> transforms() throws Exception {
        NTv2Transform ntv2 = new NTv2Transform(new URI("BALR2009.gsb"));
        NADCONTransform nadcon = new NADCONTransform(new URI("stpaul.las"), new URI("stpaul.los"));
        MathTransform passThrough = PassThroughTransform.create(1, geocentric(), 0);
        MathTransform concatenated = ConcatenatedTransform.create(scale2D(), geocentric());
        MathTransform exponential = ExponentialTransform1D.create(10, 2);
        MathTransform logarithmic = LogarithmicTransform1D.create(10, 0);
        Class<?> as2D = Class.forName(MolodenskiTransform.class.getName() + "$As2D");
        Class<?> gridType = Class.forName(LocalizationGrid.class.getName() + "Transform2D");
        return List.of(
                testCase("ConcatenatedTransform", ConcatenatedTransform.class, concatenated, 5, 15),
                testCase("ProjectiveTransform", ProjectiveTransform.class, scale3D(), 5, 15, 1),
                testCase("AffineTransform2D", AffineTransform2D.class, scale2D(), 10, 45),
                testCase("PassThroughTransform", PassThroughTransform.class, passThrough, 7, 10, 45),
                testCase("GeocentricTransform", GeocentricTransform.class, geocentric(), 10, 45),
                testCase("NTv2Transform", NTv2Transform.class, ntv2, 3.084896111, 39.592654167),
                testCase("NADCONTransform", NADCONTransform.class, nadcon, -170.25, 57.125),
                testCase("LinearTransform1D", LinearTransform1D.class, LinearTransform1D.create(2, 3), 5),
                testCase("ExponentialTransform1D", ExponentialTransform1D.class, exponential, 1.5),
                testCase("LogarithmicTransform1D", LogarithmicTransform1D.class, logarithmic, 100),
                testCase("MolodenskiTransform", MolodenskiTransform.class, molodenski(), 10, 45),
                testCase("MolodenskiTransform 2D", as2D, molodenski2D(), 10, 45),
                testCase("MapProjection", MapProjection.class, mercator(), 10, 45),
                testCase("LocalizationGridTransform2D", gridType, localizationGrid(), 0.5, 0.5));
    }

    private static MathTransform molodenski() {
        return new MolodenskiTransform(false, WGS84_A, WGS84_B, false, CLARKE_A, CLARKE_B, false, -8, 160, 176);
    }

    private static MathTransform molodenski2D() throws Exception {
        ParameterValueGroup parameters = FACTORY.getDefaultParameters("Molodenski");
        parameters.parameter("dim").setValue(2);
        parameters.parameter("dx").setValue(-8.0);
        parameters.parameter("dy").setValue(160.0);
        parameters.parameter("dz").setValue(176.0);
        parameters.parameter("src_semi_major").setValue(WGS84_A);
        parameters.parameter("src_semi_minor").setValue(WGS84_B);
        parameters.parameter("tgt_semi_major").setValue(CLARKE_A);
        parameters.parameter("tgt_semi_minor").setValue(CLARKE_B);
        return FACTORY.createParameterizedTransform(parameters);
    }

    private static MathTransform mercator() throws Exception {
        ParameterValueGroup parameters = FACTORY.getDefaultParameters("Mercator_1SP");
        parameters.parameter("semi_major").setValue(WGS84_A);
        parameters.parameter("semi_minor").setValue(WGS84_B);
        return FACTORY.createParameterizedTransform(parameters);
    }

    private static MathTransform localizationGrid() {
        LocalizationGrid grid = new LocalizationGrid(2, 2);
        grid.setLocalizationPoint(0, 0, 10, 20);
        grid.setLocalizationPoint(1, 0, 12, 20);
        grid.setLocalizationPoint(0, 1, 10, 23);
        grid.setLocalizationPoint(1, 1, 12, 23);
        return grid.getMathTransform();
    }

    /** Builds a new instance for each row, so that each row computes its own inverse. */
    private static GeocentricTransform geocentric() {
        // without height, as the inverse computes the height with millimeter errors
        return new GeocentricTransform(DefaultEllipsoid.WGS84, false);
    }

    private static MathTransform scale2D() {
        return ProjectiveTransform.create(AffineTransform.getScaleInstance(2, 3));
    }

    private static MathTransform scale3D() {
        GeneralMatrix matrix = new GeneralMatrix(4);
        matrix.setElement(0, 0, 2);
        matrix.setElement(1, 1, 3);
        return ProjectiveTransform.create(matrix);
    }

    private static Object[] testCase(String name, Class<?> type, MathTransform transform, double... point) {
        return new Object[] {name, type, transform, point};
    }

    /** Repeated calls return the same inverse, and the inverse of the inverse is the original transform. */
    @Test
    public void testInverseCached() throws Exception {
        assertThat(transform, instanceOf(type));
        MathTransform inverse = transform.inverse();
        assertSame(inverse, transform.inverse());
        assertSame(transform, inverse.inverse());
    }

    @Test
    public void testRoundTrip() throws Exception {
        double[] transformed = new double[transform.getTargetDimensions()];
        transform.transform(point, 0, transformed, 0, 1);
        double[] back = new double[point.length];
        transform.inverse().transform(transformed, 0, back, 0, 1);
        assertArrayEquals(point, back, TOLERANCE);
    }

    /** Returns the cached inverse while another thread holds the monitor of the transform. */
    @Test
    public void testInverseWithoutLock() throws Exception {
        MathTransform inverse = transform.inverse();
        CountDownLatch locked = new CountDownLatch(1);
        CountDownLatch release = new CountDownLatch(1);
        ExecutorService executor = Executors.newFixedThreadPool(2);
        try {
            executor.submit(() -> {
                synchronized (transform) {
                    locked.countDown();
                    release.await();
                }
                return null;
            });
            locked.await();
            Future<MathTransform> cached = executor.submit(transform::inverse);
            assertSame(inverse, cached.get(5, SECONDS));
        } finally {
            release.countDown();
            executor.shutdown();
        }
    }
}
