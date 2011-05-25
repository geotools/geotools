/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.AffineTransform;
import java.util.Arrays;
import java.util.Random;

import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.DirectPosition;

import org.geotools.geometry.DirectPosition1D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.WKT;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.matrix.MatrixFactory;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.matrix.XMatrix;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests various classes of {@link MathTransform}, including {@link ConcatenatedTransform}.
 * Actually, there is many {@link ConcatenatedTransform}, each optimized for special cases.
 * This test tries to test a wide range of subclasses.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class MathTransformTest {
    /**
     * Random numbers generator.
     */
    private Random random;

    /**
     * The default math transform factory.
     */
    private DefaultMathTransformFactory factory;

    /**
     * Expected accuracy for tests on JTS objects.
     */
    private static final double ACCURACY = 0.03;

    /**
     * Set up common objects used for all tests.
     */
    @Before
    public void setUp() {
        random  = new Random(-3531834320875149028L);
        factory = new DefaultMathTransformFactory();
    }

    /**
     * Tests a transformation on a {@link DirectPosition} object.
     */
    @Test
    public void testDirectPositionTransform() throws FactoryException, TransformException {
        CoordinateReferenceSystem crs = ReferencingFactoryFinder.getCRSFactory(null).createFromWKT(WKT.UTM_10N);
        MathTransform t = ReferencingFactoryFinder.getCoordinateOperationFactory(null).
                createOperation(DefaultGeographicCRS.WGS84, crs).getMathTransform();
        DirectPosition position = new GeneralDirectPosition(-123, 55);
        position = t.          transform(position, position);
        position = t.inverse().transform(position, position);
        assertEquals(-123, position.getOrdinate(0), 1E-6);
        assertEquals(  55, position.getOrdinate(1), 1E-6);
    }

    /**
     * Tests the {@link ProjectiveTransform} implementation.
     */
    @Test
    public void testAffineTransform() throws FactoryException, TransformException {
        for (int pass=0; pass<10; pass++) {
            final AffineTransform transform = new AffineTransform();
            transform.rotate(Math.PI*random.nextDouble(), 100*random.nextDouble(), 100*random.nextDouble());
            transform.scale       (2*random.nextDouble(),   2*random.nextDouble());
            transform.shear       (2*random.nextDouble(),   2*random.nextDouble());
            transform.translate (100*random.nextDouble(), 100*random.nextDouble());
            compareTransforms("AffineTransform", new MathTransform[] {
                new ProjectiveTransform(new GeneralMatrix(transform)),
                new AffineTransform2D(transform)
            });
        }

        AffineTransform at = new AffineTransform(23.157082917424454,  0.0, 3220.1613428464952,
                                                 0.0, -23.157082917424457, 1394.4593259871676);
        MathTransform mt = factory.createAffineTransform(new GeneralMatrix(at));

        final double[] points = new double[] {
                -129.992589135802,    55.9226692948365, -129.987254340541,
                  55.9249676996729, -129.982715772093,    55.9308988434656,
                -129.989772198265,    55.9289277997662, -129.992589135802, 55.9226692948365
        };
        final double[] transformedPoints = new double[points.length];
        mt.transform(points, 0, transformedPoints, 0, points.length/2);
        at.transform(points, 0, points, 0, points.length/2);
        for (int i=0; i<transformedPoints.length; i++) {
            assertEquals(points[i], transformedPoints[i], ACCURACY);
        }
    }

    /**
     * Test various linear transformations. We test for many differents dimensions.
     * The factory class should have created specialized classes for 1D and 2D cases.
     * This test is used in order to ensure that specialized case produces the same
     * results than general cases.
     */
    @Test
    public void testSubAffineTransform() throws FactoryException, TransformException {
        for (int pass=0; pass<5; pass++) {
            /*
             * Construct the reference matrix.
             */
            final int dimension = 10;
            final GeneralMatrix matrix = new GeneralMatrix(dimension+1, dimension+1);
            for (int i=0; i<dimension; i++) {
                matrix.setElement(i,i,         400*Math.random()-200);
                matrix.setElement(i,dimension, 400*Math.random()-200);
            }
            assertTrue(matrix.isAffine());
            /*
             * Construct all math transforms.
             */
            final MathTransform[] transforms = new MathTransform[dimension];
            for (int i=1; i<=dimension; i++) {
                final GeneralMatrix sub = new GeneralMatrix(i+1, i+1);
                matrix.copySubMatrix(0, 0, i, i, 0, 0, sub);         // Scale terms
                matrix.copySubMatrix(0, dimension, i, 1, 0, i, sub); // Translation terms
                final MathTransform transform = transforms[i-1] = factory.createAffineTransform(sub);
                assertTrue  (sub.isAffine());
                assertEquals(sub, new GeneralMatrix(((LinearTransform) transform).getMatrix()));
                assertInterfaced(transform);
                assertTrue(i ==  transform.getSourceDimensions());
            }
            /*
             * Check transformations and the inverse transformations.
             */
            assertTrue("MathTransform1D", transforms[0] instanceof MathTransform1D);
            assertTrue("MathTransform2D", transforms[1] instanceof MathTransform2D);
            assertEquals(matrix, ((LinearTransform) transforms[dimension-1]).getMatrix());
            compareTransforms("SubAffineTransform", transforms);
            for (int i=0; i<transforms.length; i++) {
                transforms[i] = transforms[i].inverse();
            }
            compareTransforms("SubAffineTransform.inverse", transforms);
        }
    }

    /**
     * Test the concatenation of linear transformations. Concatenation of linear
     * transformations should involve only matrix multiplication.  However, this
     * test will also create {@link ConcatenatedTransform} objects in order to
     * compare their results.
     */
    @Test
    public void testAffineTransformConcatenation() throws FactoryException, TransformException {
        final MathTransform[] transforms = new MathTransform[2];
        final int numDim = 4;
        final int numPts = 200;
        for (int pass=0; pass<100; pass++) {
            final int     dimSource = random.nextInt(numDim)+1;
            final int     dimTarget = random.nextInt(numDim)+1;
            final int     dimInterm = random.nextInt(numDim)+1;
            final Matrix    matrix1 = getRandomMatrix(dimSource, dimInterm);
            final Matrix    matrix2 = getRandomMatrix(dimInterm, dimTarget);
            final MathTransform tr1 = factory.createAffineTransform(matrix1);
            final MathTransform tr2 = factory.createAffineTransform(matrix2);
            final double[] sourcePt = new double[dimSource * numPts];
            final double[] intermPt = new double[dimInterm * numPts];
            final double[] targetPt = new double[dimTarget * numPts];
            final double[]  compare = new double[dimTarget * numPts];
            final double[]    delta = new double[dimTarget];
            for (int i=0; i<numPts; i++) {
                sourcePt[i] = 100*random.nextDouble()-50;
            }
            tr1.transform(sourcePt, 0, intermPt, 0, numPts);
            tr2.transform(intermPt, 0, targetPt, 0, numPts);
            Arrays.fill(delta, 1E-6);
            /*
             * Create two set of concatenated transform: the first one computed from matrix
             * multiplication;   the second one is forced to a ConcatenatedTransform object
             * for testing purpose.
             */
            transforms[0] = factory.createConcatenatedTransform  (tr1, tr2);
            transforms[1] = ConcatenatedTransform.createConcatenatedTransform(tr1, tr2);
            assertTrue (transforms[0] instanceof LinearTransform);
            assertFalse(transforms[1] instanceof LinearTransform);
            for (int i=0; i<transforms.length; i++) {
                final MathTransform transform = transforms[i];
                assertInterfaced(transform);
                assertEquals("dimSource["+i+']', dimSource, transform.getSourceDimensions());
                assertEquals("dimTarget["+i+']', dimTarget, transform.getTargetDimensions());
                transform.transform(sourcePt, 0, compare, 0, numPts);
                String name = "transform["+i+"]("+dimSource+" -> "+dimInterm+" -> "+dimTarget+')';
                assertPointsEqual(name, targetPt, compare, delta);
            }
        }
    }

    /**
     * Make sure that linear transformation preserve NaN values.
     * This is required for {@link org.geotools.coverage.Category}.
     */
    @Test
    public void testNaN() throws FactoryException, TransformException {
        final XMatrix matrix = MatrixFactory.create(2);
        matrix.setElement(0,0,0);
        for (int i=0; i<200; i++) {
            final int rawBits = 0x7FC00000 + random.nextInt(100);
            final float value = Float.intBitsToFloat(rawBits);
            assertTrue("isNaN", Float.isNaN(value));
            matrix.setElement(0,1, value);
            final MathTransform1D tr = (MathTransform1D) factory.createAffineTransform(matrix);
            assertTrue("ConstantTransform1D", tr instanceof ConstantTransform1D);
            final float compare = (float) tr.transform(0);
            assertEquals("rawBits", rawBits, Float.floatToRawIntBits(compare));
        }
    }

    /**
     * Test the {@link ExponentialTransform1D} and {@link LogarithmicTransform1D} classes
     * using simple know values.
     */
    @Test
    public void testLogarithmicTransform() throws FactoryException, TransformException {
        final double[] POWER_2  = {0, 1, 2, 3,  4,  5,  6,   7,   8,   9,   10,   11,   12,   13};
        final double[] VALUE_2  = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192};
        final double[] POWER_10 = {  -5,   -4,   -3,   -2,   -1, 0,   +1,   +2,   +3,   +4,   +5};
        final double[] VALUE_10 = {1E-5, 1E-4, 1E-3, 1E-2, 1E-1, 1, 1E+1, 1E+2, 1E+3, 1E+4, 1E+5};
        compareTransform1D("Exponential",  2, POWER_2,  VALUE_2);
        compareTransform1D("Exponential", 10, POWER_10, VALUE_10);
        compareTransform1D("Logarithmic",  2, VALUE_2,  POWER_2);
        compareTransform1D("Logarithmic", 10, VALUE_10, POWER_10);
    }

    /**
     * Test the concatenation of {@link LinearTransform1D}, {@link ExponentialTransform1D}
     * and {@link LogarithmicTransform1D}.
     */
    @Test
    public void testLogarithmicAndExponentialConcatenation() throws FactoryException, TransformException {
        final int numPts = 200;
        final double[] sourcePt = new double[numPts];
        final double[] targetPt = new double[numPts];
        final double[]  compare = new double[numPts];
        final double[]    delta = new double[numPts];
        for (int pass=0; pass<100; pass++) {
            for (int i=0; i<numPts; i++) {
                sourcePt[i] = 20*random.nextDouble()+0.1;
            }
            MathTransform ctr = getRandomTransform1D();
            ctr.transform(sourcePt, 0, targetPt,  0, numPts);
            for (int i=random.nextInt(2)+1; --i>=0;) {
                final MathTransform1D step = getRandomTransform1D();
                ctr = (MathTransform1D) factory.createConcatenatedTransform(ctr, step);
                step.transform(targetPt, 0, targetPt, 0, numPts);
            }
            ctr.transform(sourcePt, 0, compare,  0, numPts);
            final double EPS = Math.pow(10, -5+countNonlinear(ctr));
            for (int i=0; i<numPts; i++) {
                delta[i] = Math.max(1E-9, Math.abs(targetPt[i]*EPS));
                if (targetPt[i] >= +1E+300) targetPt[i] = Double.POSITIVE_INFINITY;
                if (targetPt[i] <= -1E+300) targetPt[i] = Double.NEGATIVE_INFINITY;
            }
            assertPointsEqual("transform["+ctr+']', targetPt, compare, delta);
            /*
             * Test the inverse transform. It is difficult to get back the exact original value,
             * since expressions like  'pow(b1, pow(b2,x))'  tend to overflow or underflow very
             * fast. We are very tolerant for this test because of this (exponential expression
             * give exponential error). The 'testLogarithmicTransform' method tested the inverse
             * transform in a more sever way.
             */
            try {
                final MathTransform inv = ctr.inverse();
                Arrays.fill(delta, Math.pow(10, countNonlinear(inv)));
                inv.transform(targetPt, 0, compare,  0, numPts);
                for (int i=0; i<numPts; i++) {
                    if (!isReal(targetPt[i]) || !isReal(compare[i])) {
                        // Ignore all input points that produced NaN or infinity
                        // A succession of 2 "Exponentional" operation produces
                        // infinities pretty fast, so ignore it.
                        sourcePt[i] = Double.NaN;
                    }
                }
                assertPointsEqual("inverse["+inv+']', sourcePt, compare, delta);
            } catch (NoninvertibleTransformException exception) {
                // Some transforms may not be invertible. Ignore...
            }
        }
    }








    ///////////////////////////////////////////////////////////////////////////////////
    ////////////                                                           ////////////
    ////////////               U T I L I T Y   M E T H O D S               ////////////
    ////////////                                                           ////////////
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns a random matrix.
     *
     * @param dimSource Number of dimension for input points.
     * @param dimTarget Number of dimension for outout points.
     */
    private Matrix getRandomMatrix(final int dimSource, final int dimTarget) {
        final XMatrix matrix = MatrixFactory.create(dimTarget+1, dimSource+1);
        for (int j=0; j<dimTarget; j++) { // Don't touch to the last row!
            for (int i=0; i<=dimSource; i++) {
                matrix.setElement(j,i, 10*random.nextDouble()-5);
            }
            if (j <= dimSource) {
                matrix.setElement(j,j, 40*random.nextDouble()+10);
            }
            matrix.setElement(j,dimSource, 80*random.nextDouble()-40);
        }
        if (dimSource == dimTarget) {
            assertTrue("Affine", matrix.isAffine());
        }
        return matrix;
    }

    /**
     * Gets a random one-dimensional transform.
     */
    private MathTransform1D getRandomTransform1D() throws FactoryException {
        final String[] candidates = {
            "Logarithmic",
            "Exponential",
            "Affine"
        };
        final String classification = candidates[random.nextInt(candidates.length)];
        final ParameterValueGroup parameters = factory.getDefaultParameters(classification);
        if (classification.equalsIgnoreCase("Affine")) {
            parameters.parameter("num_row").setValue(2);
            parameters.parameter("num_col").setValue(2);
            parameters.parameter("elt_0_0").setValue(random.nextDouble()*2+0.1); // scale
            parameters.parameter("elt_0_1").setValue(random.nextDouble()*1 - 2); // offset
        } else {
            parameters.parameter("base").setValue(random.nextDouble()*4 + 0.1);
        }
        return (MathTransform1D) factory.createParameterizedTransform(parameters);
    }

    /**
     * Compare the transformation performed by many math transforms. If some transforms
     * don't have the same number of input or output dimension, only the first input or
     * output dimensions will be taken in account.
     *
     * @throws TransformException if a transformation failed.
     */
    private void compareTransforms(final String name, final MathTransform[] transforms)
            throws TransformException
    {
        /*
         * Initialisation...
         */
        final GeneralDirectPosition[] sources = new GeneralDirectPosition[transforms.length];
        final GeneralDirectPosition[] targets = new GeneralDirectPosition[transforms.length];
        int maxDimSource = 0;
        int maxDimTarget = 0;
        for (int i=0; i<transforms.length; i++) {
            final int dimSource = transforms[i].getSourceDimensions();
            final int dimTarget = transforms[i].getTargetDimensions();
            if (dimSource > maxDimSource) maxDimSource = dimSource;
            if (dimTarget > maxDimTarget) maxDimTarget = dimTarget;
            sources[i] = new GeneralDirectPosition(dimSource);
            targets[i] = new GeneralDirectPosition(dimTarget);
        }
        /*
         * Test with an arbitrary number of randoms points.
         */
        for (int pass=0; pass<200; pass++) {
            for (int j=0; j<maxDimSource; j++) {
                final double ord = 100*random.nextDouble();
                for (int i=0; i<sources.length; i++) {
                    final GeneralDirectPosition source = sources[i];
                    if (j < source.ordinates.length) {
                        source.ordinates[j] = ord;
                    }
                }
            }
            for (int j=0; j<transforms.length; j++) {
                assertSame(transforms[j].transform(sources[j], targets[j]), targets[j]);
            }
            /*
             * Compare all target points.
             */
            final StringBuilder buffer = new StringBuilder(name);
            buffer.append(": Compare transform[");
            final int lengthJ = buffer.length();
            for (int j=0; j<targets.length; j++) {
                buffer.setLength(lengthJ);
                buffer.append(j).append("] with [");
                final int lengthI = buffer.length();
                final GeneralDirectPosition targetJ = targets[j];
                for (int i=j+1; i<targets.length; i++) {
                    buffer.setLength(lengthI);
                    buffer.append(i).append(']');
                    final String label = buffer.toString();
                    final GeneralDirectPosition targetI = targets[i];
                    assertTrue(targetJ.ordinates != targetI.ordinates);
                    for (int k=Math.min(targetJ.ordinates.length, targetI.ordinates.length); --k>=0;) {
                        assertEquals(label, targetJ.ordinates[k], targetI.ordinates[k], 1E-6);
                    }
                }
            }
        }
    }

     /**
      * Compare the result of "Logarithmic" or "Exponential" transform with the expected results.
      *
      * @param classification The identifier name (e.g. "Exponential" or "Logarithmic").
      * @param base           The value for the "base" parameter.
      * @param input          Array of input values.
      * @param expected       Array of expected output values.
      */
    private void compareTransform1D(final String   classification,
                                    final double   base,
                                    final double[] input,
                                    final double[] expected)
            throws FactoryException, TransformException
    {
        assertEquals(input.length, expected.length);
        final ParameterValueGroup parameters = factory.getDefaultParameters(classification);
        parameters.parameter("base").setValue(base);
        final MathTransform1D direct =
             (MathTransform1D) factory.createParameterizedTransform(parameters);
        final MathTransform1D inverse = direct.inverse();
        final DirectPosition1D point = new DirectPosition1D();
        for (int i=0; i<expected.length; i++) {
            final double x = input[i];
            final double y = direct.transform(x);
            assertEquals("transform[x="+x+']', expected[i], y,          1E-6);
            assertEquals("inverse  [y="+y+']', x, inverse.transform(y), 1E-6);
            point.setOrdinate(0, x);
            assertSame(direct.transform(point, point), point);
            assertEquals(y, point.getOrdinate(0), 1E-9);
        }
    }

    /**
     * Count the number of non-linear steps in a {@link MathTransform}.
     */
    private static int countNonlinear(final MathTransform transform) {
        if ((transform instanceof ExponentialTransform1D) ||
            (transform instanceof LogarithmicTransform1D))
        {
            return 1;
        }
        if (transform instanceof ConcatenatedTransform) {
            final ConcatenatedTransform ct = (ConcatenatedTransform) transform;
            return countNonlinear(ct.transform1) +
                   countNonlinear(ct.transform2);
        }
        return 0;
    }








    ///////////////////////////////////////////////////////////////////////////////////
    ////////////                                                           ////////////
    ////////////             A S S E R T I O N   M E T H O D S             ////////////
    ////////////                                                           ////////////
    ///////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns {@code true} if the specified number is real (neither NaN or infinite).
     */
    private static boolean isReal(final double value) {
        return !Double.isNaN(value) && !Double.isInfinite(value);
    }

    /**
     * Verify that the specified transform implements {@link MathTransform1D}
     * or {@link MathTransform2D} as needed.
     *
     * @param transform The transform to test.
     */
    private static void assertInterfaced(final MathTransform transform) {
        if (transform instanceof LinearTransform) {
            final Matrix matrix = ((LinearTransform) transform).getMatrix();
            if (!((XMatrix) matrix).isAffine()) {
                // Special case: Non-affine transforms not yet declared as a 1D or 2D transform.
                return;
            }
        }
        int dim = transform.getSourceDimensions();
        if (transform.getTargetDimensions() != dim) {
            dim = 0;
        }
        assertTrue("MathTransform1D", (dim==1) == (transform instanceof MathTransform1D));
        assertTrue("MathTransform2D", (dim==2) == (transform instanceof MathTransform2D));
    }

    /**
     * Compare two arrays of points.
     *
     * @param name      The name of the comparaison to be performed.
     * @param expected  The expected array of points.
     * @param actual    The actual array of points.
     * @param delta     The maximal difference tolerated in comparaisons for each dimension.
     *                  This array length must be equal to coordinate dimension (usually 1, 2 or 3).
     */
    private static void assertPointsEqual(final String   name,
                                          final double[] expected,
                                          final double[] actual,
                                          final double[] delta)
    {
        final int dimension = delta.length;
        final int stop = Math.min(expected.length, actual.length)/dimension * dimension;
        assertEquals("Array length for expected points", stop, expected.length);
        assertEquals("Array length for actual points",   stop,   actual.length);
        final StringBuffer buffer = new StringBuffer(name);
        buffer.append(": point[");
        final int start = buffer.length();
        for (int i=0; i<stop; i++) {
            buffer.setLength(start);
            buffer.append(i/dimension);
            buffer.append(", dimension ");
            buffer.append(i % dimension);
            buffer.append(" of ");
            buffer.append(dimension);
            buffer.append(']');
            if (isReal(expected[i])) {
                // The "two steps" method in ConcatenatedTransformTest sometime produces
                // random NaN numbers. This "two steps" is used only for comparaison purpose;
                // the "real" (tested) method work better.
                assertEquals(buffer.toString(), expected[i], actual[i], delta[i % dimension]);
            }
        }
    }
}
