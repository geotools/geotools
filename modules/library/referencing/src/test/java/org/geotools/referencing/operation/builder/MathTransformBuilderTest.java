/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.Position2D;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.junit.Assert;
import org.junit.Test;

/**
 * A test for the MathTransformBuilders.
 *
 * @version $Id$
 * @author Jan Jezek
 * @author Adrian Custer
 */
public final class MathTransformBuilderTest {
    /**
     * Coordinates List generator.
     *
     * @param numberOfVertices count of generated points
     * @param seed for random generating.
     * @return points
     */
    private List<MappedPosition> generateCoords(int numberOfVertices, long seed) {
        CoordinateReferenceSystem crs = DefaultEngineeringCRS.CARTESIAN_2D;
        return generateCoordsWithCRS(numberOfVertices, crs, seed, true);
    }

    /**
     * Coordinates List generator.
     *
     * @param numberOfVertices count of generated points
     * @param seed for random generating.
     * @param includeAccuracy set true to generate points with accuracy.
     */
    private List<MappedPosition> generateCoords(int numberOfVertices, long seed, boolean includeAccuracy) {
        CoordinateReferenceSystem crs = DefaultEngineeringCRS.CARTESIAN_2D;
        return generateCoordsWithCRS(numberOfVertices, crs, seed, includeAccuracy);
    }

    /**
     * Coordinates List generator.
     *
     * @param numberOfVertices count of generated points
     * @param crs Coordinate Reference System of generated points
     * @param seed seed for generate random numbers
     * @param includeAccuracy set true to generate points with accuracy.
     */
    private List<MappedPosition> generateCoordsWithCRS(
            int numberOfVertices, CoordinateReferenceSystem crs, long seed, boolean includeAccuracy) {
        List<MappedPosition> vert = new ArrayList<>();
        Random randomCoord = new Random(seed);
        for (int i = 0; i < numberOfVertices; i++) {
            double xs = randomCoord.nextDouble() * 1000;
            double ys = randomCoord.nextDouble() * 1000;
            double xd = randomCoord.nextDouble() * 1000;
            double yd = randomCoord.nextDouble() * 1000;
            MappedPosition p = new MappedPosition(new Position2D(crs, xs, ys), new Position2D(crs, xd, yd));
            if (includeAccuracy) {
                p.setAccuracy(randomCoord.nextDouble());
            }
            vert.add(p);
        }
        return vert;
    }

    /**
     * Test expected values against transformed values.
     *
     * @param mt mathTransform that will be tested
     * @param pts MappedPositions of source and target values.
     */
    private void transformTest(MathTransform mt, List<MappedPosition> pts) throws FactoryException, TransformException {
        double[] points = new double[pts.size() * 2];
        double[] ptCalculated = new double[pts.size() * 2];
        for (int i = 0; i < pts.size(); i++) {
            points[2 * i] = pts.get(i).getSource().getCoordinate()[0];
            points[2 * i + 1] = pts.get(i).getSource().getCoordinate()[1];
        }
        mt.transform(points, 0, ptCalculated, 0, pts.size());
        for (int i = 0; i < pts.size(); i++) {
            assertEquals(pts.get(i).getTarget().getCoordinate()[0], ptCalculated[2 * i], 0.001);
            assertEquals(pts.get(i).getTarget().getCoordinate()[1], ptCalculated[2 * i + 1], 0.001);
        }
    }

    /** Test for {@linkplain RubberSheetBuilder RubberSheetBuilder}. */
    @Test
    public void testRubberBuilder() throws FactoryException, TransformException {
        List<MappedPosition> pts = generateCoords(20, 8324);
        CoordinateReferenceSystem crs = DefaultEngineeringCRS.CARTESIAN_2D;
        List<Position> dpl = new ArrayList<>();
        dpl.add(new Position2D(crs, 1000, 0));
        dpl.add(new Position2D(crs, 0, 0));
        dpl.add(new Position2D(crs, 0, 1000));
        dpl.add(new Position2D(crs, 1000, 1000));
        MathTransformBuilder ppc = new RubberSheetBuilder(pts, dpl);
        transformTest(ppc.getMathTransform(), pts);
        assertTrue(ppc.getErrorStatistics().rms() < 0.00001);

        // Tests the formatting, but do not display to console for not polluting
        // the output device. We just check that at least one axis title is
        // there.
        assertTrue(ppc.toString().indexOf(" x ") >= 0);
    }

    /** Test for {@linkplain ProjectiveTransformBuilder ProjectiveTransformBuilder}. */
    @Test
    public void testProjectiveBuilder() throws FactoryException, TransformException {
        List<MappedPosition> pts = generateCoords(4, 312243);
        MathTransformBuilder ppc = new ProjectiveTransformBuilder(pts);
        transformTest(ppc.getMathTransform(), pts);
        assertTrue(ppc.getErrorStatistics().rms() < 0.0001);
    }

    /** Test that all Matrixes are filled properly. */
    @Test
    public void testLSMCalculation() throws FactoryException, TransformException {
        List<MappedPosition> pts = generateCoords(15, 3121123);
        LSMTester buildTester = new LSMTester(pts);
        buildTester.includeWeights(true);
        buildTester.testLSM();
    }

    /** Test for {@linkplain AffineTransformBuilder AffineTransformBuilder}. */
    @Test
    public void testAffineBuilder() throws FactoryException, TransformException {
        List<MappedPosition> pts = generateCoords(3, 2345);
        MathTransformBuilder ppc = new AffineTransformBuilder(pts);
        transformTest(ppc.getMathTransform(), pts);
        assertTrue(ppc.getErrorStatistics().rms() < 0.00001);
    }

    /** Test for {@linkplain SimilarTransformBuilder SimilarTransformBuilder}. */
    @Test
    public void testSimilarBuilder() throws FactoryException, TransformException {
        List<MappedPosition> pts = generateCoords(2, 24535);
        MathTransformBuilder ppc = new SimilarTransformBuilder(pts);
        transformTest(ppc.getMathTransform(), pts);
        assertTrue(ppc.getErrorStatistics().rms() < 0.00001);
    }

    @Test
    public void testAdvancedAffineBuilder() {
        try {
            List<MappedPosition> pts = generateCoords(3, 1245);
            AdvancedAffineBuilder aab = new AdvancedAffineBuilder(pts);
            aab.setConstrain(AdvancedAffineBuilder.SXY, 0);
            AffineToGeometric a2g = new AffineToGeometric((AffineTransform2D) aab.getMathTransform());
            Assert.assertEquals(a2g.getSkew(), 0, 0.000000001);
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }
    /** Test for MismatchedSizeException. */
    @Test(expected = IllegalArgumentException.class)
    public void testMismatchedSizeException() throws TransformException {
        // The exception should be thrown when the number of points is less than necessary
        List<MappedPosition> pts = generateCoords(2, 2453655);
        new AffineTransformBuilder(pts);
    }

    /** Test for MissingInfoException. */
    @Test(expected = MissingInfoException.class)
    public void testMissingInfoException() throws FactoryException {
        // The exception should be thrown when the number of points is less than necessary
        List<MappedPosition> pts = generateCoords(5, 2434765, false);
        AffineTransformBuilder builder = new AffineTransformBuilder(pts);
        builder.includeWeights(true);
    }

    /**
     * Implements the method for testing the calculation of least square method. The main requirement of least square
     * method is that A<sup>T<sup>PAx + A<sup>T<sup>PX = 0.
     *
     * @author jezekjas
     */
    static final class LSMTester extends ProjectiveTransformBuilder {
        LSMTester(List<MappedPosition> pts) {
            super(pts);
        }

        @Test
        public void testLSM() {
            // fill Matrix by calculateLSM()
            this.calculateLSM();

            GeneralMatrix AT = A.clone();
            AT.transpose();

            GeneralMatrix ATP = new GeneralMatrix(AT.getNumRow(), P.getNumCol());
            GeneralMatrix ATPA = new GeneralMatrix(AT.getNumRow(), A.getNumCol());
            GeneralMatrix ATPX = new GeneralMatrix(AT.getNumRow(), 1);
            GeneralMatrix x = new GeneralMatrix(A.getNumCol(), 1);
            ATP.mul(AT, P); // ATP
            ATPA.mul(ATP, A); // ATPA
            ATPX.mul(ATP, X); // ATPX

            GeneralMatrix ATPAI = ATPA.clone();
            ATPAI.invert();

            x.mul(ATPAI, ATPX);

            // assert the ATPAx + ATPX should be 0;.
            x.mul(ATPA, x);
            x.sub(ATPX);

            double[] tx = new double[x.getNumRow()];
            x.getColumn(0, tx);

            for (double v : tx) {
                assertTrue(v < 0.001);
            }
        }
    }
}
