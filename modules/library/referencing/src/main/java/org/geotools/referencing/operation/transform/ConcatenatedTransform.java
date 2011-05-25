/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.geom.Point2D;
import java.io.Serializable;

import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform1D;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.DirectPosition;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.operation.matrix.XMatrix;
import org.geotools.referencing.operation.matrix.Matrix3;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.util.Utilities;


/**
 * Base class for concatenated transform. Concatenated transforms are
 * serializable if all their step transforms are serializables.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class ConcatenatedTransform extends AbstractMathTransform implements Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 5772066656987558634L;

    /**
     * Small number for floating point comparaisons.
     */
    private static final double EPSILON = 1E-10;

    /**
     * Maximum length of temporary {@double[]} arrays to be created, used for performing
     * transformations in batch. A value of 256 will consumes 2 kilobytes of memory. It is
     * better to avoid too high values since allocating and initializing the array elements
     * to zero have a cost.
     */
    private static final int TEMPORARY_ARRAY_LENGTH = 256;

    /**
     * The first math transform.
     */
    public final MathTransform transform1;

    /**
     * The second math transform.
     */
    public final MathTransform transform2;

    /**
     * The inverse transform. This field will be computed only when needed.
     * But it is serialized in order to avoid rounding error if the inverse
     * transform is serialized instead of the original one.
     */
    private ConcatenatedTransform inverse;

    /**
     * Constructs a concatenated transform. This constructor is for subclasses only. To
     * create a concatenated transform, use the factory method {@link #create} instead.
     *
     * @param transform1 The first math transform.
     * @param transform2 The second math transform.
     */
    protected ConcatenatedTransform(final MathTransform transform1,
                                    final MathTransform transform2)
    {
        this.transform1 = transform1;
        this.transform2 = transform2;
        if (!isValid()) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.CANT_CONCATENATE_TRANSFORMS_$2,
                      getName(transform1), getName(transform2)));
        }
    }

    /**
     * Returns the underlying matrix for the specified transform,
     * or {@code null} if the matrix is unavailable.
     */
    private static XMatrix getMatrix(final MathTransform transform) {
        if (transform instanceof LinearTransform) {
            return toXMatrix(((LinearTransform) transform).getMatrix());
        }
        if (transform instanceof AffineTransform) {
            return new Matrix3((AffineTransform) transform);
        }
        return null;
    }

    /**
     * Tests if one math transform is the inverse of the other. This implementation
     * can't detect every case. It just detect the case when {@code tr2} is an
     * instance of {@link AbstractMathTransform.Inverse}.
     *
     * @todo We could make this test more general (just compare with tr2.inverse(),
     *       no matter if it is an instance of AbstractMathTransform.Inverse or not,
     *       and catch the exception if one is thrown). Would it be too expensive to
     *       create inconditionnaly the inverse transform?
     */
    private static boolean areInverse(final MathTransform tr1, final MathTransform tr2) {
        if (tr2 instanceof AbstractMathTransform.Inverse) {
            return tr1.equals(((AbstractMathTransform.Inverse) tr2).inverse());
        }
        return false;
    }

    /**
     * Constructs a concatenated transform.  This factory method checks for step transforms
     * dimension. The returned transform will implements {@link MathTransform2D} if source and
     * target dimensions are equal to 2.  Likewise, it will implements {@link MathTransform1D}
     * if source and target dimensions are equal to 1.  {@link MathTransform} implementations
     * are available in two version: direct and non-direct. The "non-direct" version use an
     * intermediate buffer when performing transformations; they are slower and consume more
     * memory. They are used only as a fallback when a "direct" version can't be created.
     *
     * @param tr1 The first math transform.
     * @param tr2 The second math transform.
     * @return    The concatenated transform.
     *
     * @todo We could add one more optimisation: if one transform is a matrix and the
     *       other transform is a PassThroughTransform, and if the matrix as 0 elements
     *       for all rows matching the PassThrough sub-transform, then we can get ride
     *       of the whole PassThroughTransform object.
     */
    public static MathTransform create(MathTransform tr1, MathTransform tr2) {
        final int dim1 = tr1.getTargetDimensions();
        final int dim2 = tr2.getSourceDimensions();
        if (dim1 != dim2) {
            throw new IllegalArgumentException(
                      Errors.format(ErrorKeys.CANT_CONCATENATE_TRANSFORMS_$2,
                                    getName(tr1), getName(tr2)) + ' ' +
                      Errors.format(ErrorKeys.MISMATCHED_DIMENSION_$2, dim1, dim2));
        }
        MathTransform mt = createOptimized(tr1, tr2);
        if (mt != null) {
            return mt;
        }
        /*
         * If at least one math transform is an instance of ConcatenatedTransform and assuming
         * that MathTransforms are associatives, tries the following arrangements and select
         * one one with the fewest amount of steps:
         *
         *   Assuming :  tr1 = (A * B)
         *               tr2 = (C * D)
         *
         *   Current  :  (A * B) * (C * D)     Will be the selected one if nothing better.
         *   Try k=0  :  A * (B * (C * D))     Implies A * ((B * C) * D) through recursivity.
         *   Try k=1  :  ((A * B) * C) * D     Implies (A * (B * C)) * D through recursivity.
         *   Try k=2  :                        Tried only if try k=1 changed something.
         *
         * TODO: The same combinaison may be computed more than once (e.g. (B * C) above).
         *       Should not be a big deal if there is not two many steps. In the even where
         *       it would appears a performance issue, we could maintains a Map of combinaisons
         *       already computed. The map would be local to a "create" method execution.
         */
        int stepCount = getStepCount(tr1) + getStepCount(tr2);
        boolean tryAgain = true; // Really 'true' because we want at least 2 iterations.
        for (int k=0; ; k++) {
            MathTransform c1 = tr1;
            MathTransform c2 = tr2;
            final boolean first = (k & 1) == 0;
            MathTransform candidate = first ? c1 : c2;
            while (candidate instanceof ConcatenatedTransform) {
                final ConcatenatedTransform ctr = (ConcatenatedTransform) candidate;
                if (first) {
                    c1 = candidate = ctr.transform1;
                    c2 = create(ctr.transform2, c2);
                } else {
                    c1 = create(c1, ctr.transform1);
                    c2 = candidate = ctr.transform2;
                }
                final int c = getStepCount(c1) + getStepCount(c2);
                if (c < stepCount) {
                    tr1 = c1;
                    tr2 = c2;
                    stepCount = c;
                    tryAgain = true;
                }
            }
            if (!tryAgain) break;
            tryAgain = false;
        }
        /*
         * Tries again the check for optimized cases (identity, etc.), because a
         * transform may have been simplified to identity as a result of the above.
         */
        mt = createOptimized(tr1, tr2);
        if (mt != null) {
            return mt;
        }
        /*
         * Can't avoid the creation of a ConcatenatedTransform object.
         * Check for the type to create (1D, 2D, general case...)
         */
        return createConcatenatedTransform(tr1, tr2);
    }

    /**
     * Tries to returns an optimized concatenation, for example by merging to affine transforms
     * into a single one. If no optimized cases has been found, returns {@code null}. In the later
     * case, the caller will need to create a more heavy {@link ConcatenatedTransform} instance.
     */
    private static MathTransform createOptimized(final MathTransform tr1, final MathTransform tr2) {
        /*
         * Trivial - but actually essential!! - check for the identity cases.
         */
        if (tr1.isIdentity()) return tr2;
        if (tr2.isIdentity()) return tr1;
        /*
         * If both transforms use matrix, then we can create
         * a single transform using the concatenated matrix.
         */
        final XMatrix matrix1 = getMatrix(tr1);
        if (matrix1 != null) {
            final XMatrix matrix2 = getMatrix(tr2);
            if (matrix2 != null) {
                // Compute "matrix = matrix2 * matrix1". Reuse an existing matrix object
                // if possible, which is always the case when both matrix are square.
                final int numRow = matrix2.getNumRow();
                final int numCol = matrix1.getNumCol();
                final XMatrix matrix;
                if (numCol == matrix2.getNumCol()) {
                    matrix = matrix2;
                    matrix2.multiply(matrix1);
                } else {
                    final GeneralMatrix m = new GeneralMatrix(numRow, numCol);
                    m.mul(toGMatrix(matrix2), toGMatrix(matrix1));
                    matrix = m;
                }
                if (matrix.isIdentity(EPSILON)) {
                    matrix.setIdentity();
                }
                // May not be really affine, but work anyway...
                // This call will detect and optimize the special
                // case where an 'AffineTransform' can be used.
                return ProjectiveTransform.create(matrix);
            }
        }
        /*
         * If one transform is the inverse of the
         * other, returns the identity transform.
         */
        if (areInverse(tr1, tr2) || areInverse(tr2, tr1)) {
            assert tr1.getSourceDimensions() == tr2.getTargetDimensions();
            assert tr1.getTargetDimensions() == tr2.getSourceDimensions();
            return IdentityTransform.create(tr1.getSourceDimensions());
        }
        /*
         * Gives a chance to AbstractMathTransform to returns an optimized object.
         * The main use case is Logarithmic vs Exponential transforms.
         */
        if (tr1 instanceof AbstractMathTransform) {
            final MathTransform optimized = ((AbstractMathTransform) tr1).concatenate(tr2, false);
            if (optimized != null) {
                return optimized;
            }
        }
        if (tr2 instanceof AbstractMathTransform) {
            final MathTransform optimized = ((AbstractMathTransform) tr2).concatenate(tr1, true);
            if (optimized != null) {
                return optimized;
            }
        }
        // No optimized case found.
        return null;
    }

    /**
     * Continue the construction started by {@link #create}. The construction step is available
     * separatly for testing purpose (in a JUnit test), and for {@link #inverse()} implementation.
     */
    static ConcatenatedTransform createConcatenatedTransform(final MathTransform tr1,
                                                             final MathTransform tr2)
    {
        final int dimSource = tr1.getSourceDimensions();
        final int dimTarget = tr2.getTargetDimensions();
        /*
         * Checks if the result need to be a MathTransform1D.
         */
        if (dimSource == 1 && dimTarget == 1) {
            if (tr1 instanceof MathTransform1D && tr2 instanceof MathTransform1D) {
                return new ConcatenatedTransformDirect1D((MathTransform1D) tr1,
                                                         (MathTransform1D) tr2);
            } else {
                return new ConcatenatedTransform1D(tr1, tr2);
            }
        } else
        /*
         * Checks if the result need to be a MathTransform2D.
         */
        if (dimSource == 2 && dimTarget == 2) {
            if (tr1 instanceof MathTransform2D && tr2 instanceof MathTransform2D) {
                return new ConcatenatedTransformDirect2D((MathTransform2D) tr1,
                                                         (MathTransform2D) tr2);
            } else {
                return new ConcatenatedTransform2D(tr1, tr2);
            }
        } else
        /*
         * Checks for the general case.
         */
        if (dimSource == tr1.getTargetDimensions() && tr2.getSourceDimensions() == dimTarget) {
            return new ConcatenatedTransformDirect(tr1, tr2);
        } else {
            return new ConcatenatedTransform(tr1, tr2);
        }
    }

    /**
     * Returns a name for the specified math transform.
     */
    private static final String getName(final MathTransform transform) {
        if (transform instanceof AbstractMathTransform) {
            ParameterValueGroup params = ((AbstractMathTransform) transform).getParameterValues();
            if (params != null) {
                String name = params.getDescriptor().getName().getCode();
                if (name!=null && (name=name.trim()).length()!=0) {
                    return name;
                }
            }
        }
        return Classes.getShortClassName(transform);
    }

    /**
     * Checks if transforms are compatibles. The default
     * implementation check if transfert dimension match.
     */
    boolean isValid() {
        return transform1.getTargetDimensions() == transform2.getSourceDimensions();
    }

    /**
     * Gets the dimension of input points.
     */
    public final int getSourceDimensions() {
        return transform1.getSourceDimensions();
    }

    /**
     * Gets the dimension of output points.
     */
    public final int getTargetDimensions() {
        return transform2.getTargetDimensions();
    }

    /**
     * Returns the number of {@linkplain MathTransform math transform} steps performed by this
     * concatenated transform.
     *
     * @return The number of transform steps.
     *
     * @since 2.5
     */
    public final int getStepCount() {
        return getStepCount(transform1) + getStepCount(transform2);
    }

    /**
     * Returns the number of {@linkplain MathTransform math transform} steps performed by the
     * given transform. As a special case, we returns 0 for the identity transform since it
     * should be omitted from the final chain.
     */
    private static int getStepCount(final MathTransform transform) {
        if (transform.isIdentity()) {
            return 0;
        }
        if (!(transform instanceof ConcatenatedTransform)) {
            return 1;
        }
        return ((ConcatenatedTransform) transform).getStepCount();
    }

    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}.
     */
    @Override
    public DirectPosition transform(final DirectPosition ptSrc, final DirectPosition ptDst)
            throws TransformException
    {
        assert isValid();
        //  Note: If we know that the transfert dimension is the same than source
        //        and target dimension, then we don't need to use an intermediate
        //        point. This optimization is done in ConcatenatedTransformDirect.
        return transform2.transform(transform1.transform(ptSrc, null), ptDst);
    }

    /**
     * Transforms a list of coordinate point ordinal values. The source points are first
     * transformed by {@link #transform1}, then the intermediate points are transformed
     * by {@link #transform2}. The transformations are performed without intermediate
     * buffer if it can be avoided.
     */
    public void transform(final double[] srcPts, int srcOff,
                          final double[] dstPts, int dstOff, int numPts)
            throws TransformException
    {
        assert isValid();
        final int intermDim = transform1.getTargetDimensions();
        final int targetDim = getTargetDimensions();
        /*
         * If the transfert dimension is not greater than the target dimension, then we
         * don't need to use an intermediate buffer. Note that this optimization is done
         * inconditionnaly in ConcatenatedTransformDirect.
         */
        if (intermDim <= targetDim) {
            transform1.transform(srcPts, srcOff, dstPts, dstOff, numPts);
            transform2.transform(dstPts, dstOff, dstPts, dstOff, numPts);
            return;
        }
        if (numPts <= 0) {
            return;
        }
        /*
         * Creates a temporary array for the intermediate result. The array may be smaller than
         * the length necessary for containing every coordinates. In such case the concatenated
         * transform will need to be applied piecewise.
         */
        int numTmp = numPts;
        int length = numTmp * intermDim;
        if (length > TEMPORARY_ARRAY_LENGTH) {
            numTmp = Math.max(1, TEMPORARY_ARRAY_LENGTH / intermDim);
            length = numTmp * intermDim;
        }
        final double[] tmp = new double[length];
        final int sourceDim = getSourceDimensions();
        do {
            if (numTmp > numPts) {
                numTmp = numPts;
            }
            transform1.transform(srcPts, srcOff, tmp, 0, numTmp);
            transform2.transform(tmp, 0, dstPts, dstOff, numTmp);
            srcOff += numTmp * sourceDim;
            dstOff += numTmp * targetDim;
            numPts -= numTmp;
        } while (numPts != 0);
    }

    /**
     * Transforms a list of coordinate point ordinal values. The source points are first copied
     * in a temporary array of type {@code double[]}, transformed by {@link #transform1} first,
     * then by {@link #transform2} and finally the result is casted to {@code float} primitive
     * type and stored in the destination array. The use of {@code double} primitive type for
     * intermediate results is necesssary for reducing rounding errors.
     */
    @Override
    public void transform(final float[] srcPts, int srcOff,
                          final float[] dstPts, int dstOff, int numPts)
            throws TransformException
    {
        assert isValid();
        if (numPts <= 0) {
            return;
        }
        final int sourceDim = getSourceDimensions();
        final int targetDim = getTargetDimensions();
        final int intermDim = transform1.getTargetDimensions();
        final int dimension = Math.max(Math.max(sourceDim, targetDim), intermDim);
        int numTmp = numPts;
        int length = numTmp * dimension;
        if (length > TEMPORARY_ARRAY_LENGTH) {
            numTmp = Math.max(1, TEMPORARY_ARRAY_LENGTH / dimension);
            length = numTmp * dimension;
        }
        final double[] tmp = new double[length];
        do {
            if (numTmp > numPts) {
                numTmp = numPts;
            }
            length = numTmp * sourceDim;
            for (int i=0; i<length; i++) {
                tmp[i] = srcPts[srcOff++];
            }
            transform1.transform(tmp, 0, tmp, 0, numTmp);
            transform2.transform(tmp, 0, tmp, 0, numTmp);
            length = numTmp * targetDim;
            for (int i=0; i<length; i++) {
                dstPts[dstOff++] = (float) tmp[i];
            }
            numPts -= numTmp;
        } while (numPts != 0);
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public synchronized MathTransform inverse() throws NoninvertibleTransformException {
        assert isValid();
        if (inverse == null) {
            inverse = createConcatenatedTransform(transform2.inverse(), transform1.inverse());
            inverse.inverse = this;
        }
        return inverse;
    }

    /**
     * Gets the derivative of this transform at a point. This method delegates to the
     * {@link #derivative(DirectPosition)} method because the transformation steps
     * {@link #transform1} and {@link #transform2} may not be instances of
     * {@link MathTransform2D}.
     *
     * @param  point The coordinate point where to evaluate the derivative.
     * @return The derivative at the specified point as a 2&times;2 matrix.
     * @throws TransformException if the derivative can't be evaluated at the specified point.
     */
    @Override
    public Matrix derivative(final Point2D point) throws TransformException {
        return derivative(new GeneralDirectPosition(point));
    }

    /**
     * Gets the derivative of this transform at a point.
     *
     * @param  point The coordinate point where to evaluate the derivative.
     * @return The derivative at the specified point (never {@code null}).
     * @throws TransformException if the derivative can't be evaluated at the specified point.
     */
    @Override
    public Matrix derivative(final DirectPosition point) throws TransformException {
        final Matrix matrix1 = transform1.derivative(point);
        final Matrix matrix2 = transform2.derivative(transform1.transform(point, null));
        // Compute "matrix = matrix2 * matrix1". Reuse an existing matrix object
        // if possible, which is always the case when both matrix are square.
        final int numRow = matrix2.getNumRow();
        final int numCol = matrix1.getNumCol();
        final XMatrix matrix;
        if (numCol == matrix2.getNumCol()) {
            matrix = toXMatrix(matrix2);
            matrix.multiply(matrix1);
        } else {
            final GeneralMatrix m = new GeneralMatrix(numRow, numCol);
            m.mul(toGMatrix(matrix2), toGMatrix(matrix1));
            matrix = m;
        }
        return matrix;
    }

    /**
     * Tests whether this transform does not move any points.
     * Default implementation check if the two transforms are
     * identity.
     */
    @Override
    public final boolean isIdentity() {
        return transform1.isIdentity() && transform2.isIdentity();
    }

    /**
     * Returns a hash value for this transform.
     */
    @Override
    public final int hashCode() {
        return transform1.hashCode() + 37*transform2.hashCode();
    }

    /**
     * Compares the specified object with this math transform for equality.
     */
    @Override
    public final boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final ConcatenatedTransform that = (ConcatenatedTransform) object;
            return Utilities.equals(this.transform1, that.transform1) &&
                   Utilities.equals(this.transform2, that.transform2);
        }
        return false;
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name.
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        addWKT(formatter, transform1);
        addWKT(formatter, transform2);
        return "CONCAT_MT";
    }

    /**
     * Append to a string buffer the WKT for the specified math transform.
     */
    private static void addWKT(final Formatter formatter,
                               final MathTransform transform)
    {
        if (transform instanceof ConcatenatedTransform) {
            final ConcatenatedTransform concat = (ConcatenatedTransform) transform;
            addWKT(formatter, concat.transform1);
            addWKT(formatter, concat.transform2);
        } else {
            formatter.append(transform);
        }
    }
}
