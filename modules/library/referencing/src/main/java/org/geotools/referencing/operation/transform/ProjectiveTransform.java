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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.operation.transform;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import javax.vecmath.MismatchedSizeException;
import javax.vecmath.SingularMatrixException;
import javax.measure.unit.NonSI;

import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.Conversion;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.Matrix;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.geometry.DirectPosition;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.parameter.MatrixParameterDescriptors;
import org.geotools.parameter.MatrixParameters;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.LinearTransform;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.referencing.operation.matrix.MatrixFactory;
import org.geotools.referencing.operation.matrix.GeneralMatrix;
import org.geotools.referencing.operation.matrix.XMatrix;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * A usually affine, or otherwise a projective transform. A projective transform is capable of
 * mapping an arbitrary quadrilateral into another arbitrary quadrilateral, while preserving the
 * straightness of lines. In the special case where the transform is affine, the parallelism of
 * lines in the source is preserved in the output.
 * <p>
 * Such a coordinate transformation can be represented by a square {@linkplain GeneralMatrix matrix}
 * of an arbitrary size. Point coordinates must have a dimension equals to
 * <code>{@linkplain Matrix#getNumCol}-1</code>. For example, for square matrix of size 4&times;4,
 * coordinate points are three-dimensional. The transformed points <code>(x',y',z')</code> are
 * computed as below (note that this computation is similar to
 * {@link javax.media.jai.PerspectiveTransform} in <cite>Java Advanced Imaging</cite>):
 *
 * <blockquote><pre>
 * [ u ]     [ m<sub>00</sub>  m<sub>01</sub>  m<sub>02</sub>  m<sub>03</sub> ] [ x ]
 * [ v ]  =  [ m<sub>10</sub>  m<sub>11</sub>  m<sub>12</sub>  m<sub>13</sub> ] [ y ]
 * [ w ]     [ m<sub>20</sub>  m<sub>21</sub>  m<sub>22</sub>  m<sub>23</sub> ] [ z ]
 * [ t ]     [ m<sub>30</sub>  m<sub>31</sub>  m<sub>32</sub>  m<sub>33</sub> ] [ 1 ]
 *
 *   x' = u/t
 *   y' = v/t
 *   y' = w/t
 * </pre></blockquote>
 *
 * In the special case of an affine transform, the last row contains only zero
 * values except in the last column, which contains 1.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see javax.media.jai.PerspectiveTransform
 * @see java.awt.geom.AffineTransform
 * @see <A HREF="http://mathworld.wolfram.com/AffineTransformation.html">Affine transformation on MathWorld</A>
 */
public class ProjectiveTransform extends AbstractMathTransform implements LinearTransform, Serializable {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -2104496465933824935L;

    /**
     * The number of rows.
     */
    private final int numRow;

    /**
     * The number of columns.
     */
    private final int numCol;

    /**
     * Elements of the matrix. Column indice vary fastest.
     */
    private final double[] elt;

    /**
     * The inverse transform. Will be created only when first needed.
     */
    private transient ProjectiveTransform inverse;

    /**
     * Constructs a transform from the specified matrix.
     * The matrix should be affine, but it will not be verified.
     *
     * @param matrix The matrix.
     */
    protected ProjectiveTransform(final Matrix matrix) {
        numRow = matrix.getNumRow();
        numCol = matrix.getNumCol();
        elt = new double[numRow*numCol];
        int index = 0;
        for (int j=0; j<numRow; j++) {
            for (int i=0; i<numCol; i++) {
                elt[index++] = matrix.getElement(j,i);
            }
        }
    }

    /**
     * Creates a transform for the specified matrix.
     * The matrix should be affine, but it is not be verified.
     *
     * @param matrix The affine transform as a matrix.
     * @return The transform for the given matrix.
     */
    public static LinearTransform create(final Matrix matrix) {
        final int dimension = matrix.getNumRow()-1;
        if (dimension == matrix.getNumCol()-1) {
            if (matrix.isIdentity()) {
                return IdentityTransform.create(dimension);
            }
            final GeneralMatrix m = toGMatrix(matrix);
            if (m.isAffine()) {
                switch (dimension) {
                    case 1: return LinearTransform1D.create(m.getElement(0,0), m.getElement(0,1));
                    case 2: return create(m.toAffineTransform2D());
                }
            }
        }
        switch (dimension) {
            case 2:  return new ProjectiveTransform2D(matrix);
            default: return new ProjectiveTransform  (matrix);
        }
    }

    /**
     * Creates a transform for the specified matrix as a Java2D object.
     * This method is provided for interoperability with
     * <A HREF="http://java.sun.com/products/java-media/2D/index.jsp">Java2D</A>.
     *
     * @param matrix The affine transform as a matrix.
     * @return The transform for the given matrix.
     */
    public static LinearTransform create(final AffineTransform matrix) {
        if (matrix.isIdentity()) {
            return IdentityTransform.create(2);
        }
        return new AffineTransform2D(matrix);
    }

    /**
     * Creates a transform that apply a uniform scale along all axis.
     *
     * @param dimension The input and output dimensions.
     * @param scale The scale factor.
     * @return The scale transform.
     *
     * @since 2.3
     */
    public static LinearTransform createScale(final int dimension, final double scale) {
        if (scale == 1) {
            return IdentityTransform.create(dimension);
        }
        final Matrix matrix = new GeneralMatrix(dimension + 1);
        for (int i=0; i<dimension; i++) {
            matrix.setElement(i, i, scale);
        }
        return create(matrix);
    }

    /**
     * Creates a transform that apply the same translation along all axis.
     *
     * @param dimension The input and output dimensions.
     * @param offset The translation.
     * @return The offset transform.
     *
     * @since 2.3
     */
    public static LinearTransform createTranslation(final int dimension, final double offset) {
        if (offset == 0) {
            return IdentityTransform.create(dimension);
        }
        final Matrix matrix = new GeneralMatrix(dimension + 1);
        for (int i=0; i<dimension; i++) {
            matrix.setElement(i, dimension, offset);
        }
        return create(matrix);
    }

    /**
     * Creates a matrix that keep only a subset of the ordinate values.
     * The dimension of source coordinates is {@code sourceDim} and
     * the dimension of target coordinates is {@code toKeep.length}.
     *
     * @param  sourceDim the dimension of source coordinates.
     * @param  toKeep the indices of ordinate values to keep.
     * @return The matrix to give to the {@link #create(Matrix)}
     *         method in order to create the transform.
     * @throws IndexOutOfBoundsException if a value of {@code toKeep}
     *         is lower than 0 or not smaller than {@code sourceDim}.
     */
    public static Matrix createSelectMatrix(final int sourceDim, final int[] toKeep)
            throws IndexOutOfBoundsException
    {
        final int targetDim = toKeep.length;
        final XMatrix matrix = MatrixFactory.create(targetDim+1, sourceDim+1);
        matrix.setZero();
        for (int j=0; j<targetDim; j++) {
            matrix.setElement(j, toKeep[j], 1);
        }
        matrix.setElement(targetDim, sourceDim, 1);
        return matrix;
    }

    /**
     * Returns the parameter descriptors for this math transform.
     */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return ProviderAffine.PARAMETERS;
    }

    /**
     * Returns the matrix elements as a group of parameters values. The number of parameters
     * depends on the matrix size. Only matrix elements different from their default value
     * will be included in this group.
     *
     * @param  matrix The matrix to returns as a group of parameters.
     * @return A copy of the parameter values for this math transform.
     */
    static ParameterValueGroup getParameterValues(final Matrix matrix) {
        final MatrixParameters values;
        values = (MatrixParameters) ProviderAffine.PARAMETERS.createValue();
        values.setMatrix(matrix);
        return values;
    }

    /**
     * Returns the matrix elements as a group of parameters values. The number of parameters
     * depends on the matrix size. Only matrix elements different from their default value
     * will be included in this group.
     *
     * @return A copy of the parameter values for this math transform.
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        return getParameterValues(getMatrix());
    }

    /**
     * Transforms an array of floating point coordinates by this matrix. Point coordinates
     * must have a dimension equals to <code>{@link Matrix#getNumCol}-1</code>. For example,
     * for square matrix of size 4&times;4, coordinate points are three-dimensional and
     * stored in the arrays starting at the specified offset ({@code srcOff}) in the order
     * <code>[x<sub>0</sub>, y<sub>0</sub>, z<sub>0</sub>,
     *        x<sub>1</sub>, y<sub>1</sub>, z<sub>1</sub>...,
     *        x<sub>n</sub>, y<sub>n</sub>, z<sub>n</sub>]</code>.
     *
     * @param srcPts The array containing the source point coordinates.
     * @param srcOff The offset to the first point to be transformed in the source array.
     * @param dstPts The array into which the transformed point coordinates are returned.
     * @param dstOff The offset to the location of the first transformed point that is stored
     *               in the destination array. The source and destination array sections can
     *               be overlaps.
     * @param numPts The number of points to be transformed
     */
    @Override
    public void transform(float[] srcPts, int srcOff,
                          final float[] dstPts, int dstOff, int numPts)
    {
        final int  inputDimension = numCol-1; // The last ordinate will be assumed equals to 1.
        final int outputDimension = numRow-1;
        final double[]     buffer = new double[numRow];
        if (srcPts == dstPts) {
            // We are going to write in the source array. Checks if
            // source and destination sections are going to clash.
            final int upperSrc = srcOff + numPts*inputDimension;
            if (upperSrc > dstOff) {
                if (inputDimension >= outputDimension ? dstOff > srcOff :
                            dstOff + numPts*outputDimension > upperSrc) {
                    // If source overlaps destination, then the easiest workaround is
                    // to copy source data. This is not the most efficient however...
                    srcPts = new float[numPts*inputDimension];
                    System.arraycopy(dstPts, srcOff, srcPts, 0, srcPts.length);
                    srcOff = 0;
                }
            }
        }
        while (--numPts >= 0) {
            int mix = 0;
            for (int j=0; j<numRow; j++) {
                double sum=elt[mix + inputDimension];
                for (int i=0; i<inputDimension; i++) {
                    sum += srcPts[srcOff+i]*elt[mix++];
                }
                buffer[j] = sum;
                mix++;
            }
            final double w = buffer[outputDimension];
            for (int j=0; j<outputDimension; j++) {
                // 'w' is equals to 1 if the transform is affine.
                dstPts[dstOff++] = (float) (buffer[j]/w);
            }
            srcOff += inputDimension;
        }
    }

    /**
     * Transforms an array of floating point coordinates by this matrix. Point coordinates
     * must have a dimension equals to <code>{@link Matrix#getNumCol}-1</code>. For example,
     * for square matrix of size 4&times;4, coordinate points are three-dimensional and
     * stored in the arrays starting at the specified offset ({@code srcOff}) in the order
     * <code>[x<sub>0</sub>, y<sub>0</sub>, z<sub>0</sub>,
     *        x<sub>1</sub>, y<sub>1</sub>, z<sub>1</sub>...,
     *        x<sub>n</sub>, y<sub>n</sub>, z<sub>n</sub>]</code>.
     *
     * @param srcPts The array containing the source point coordinates.
     * @param srcOff The offset to the first point to be transformed in the source array.
     * @param dstPts The array into which the transformed point coordinates are returned.
     * @param dstOff The offset to the location of the first transformed point that is stored
     *               in the destination array. The source and destination array sections can
     *               be overlaps.
     * @param numPts The number of points to be transformed
     */
    public void transform(double[] srcPts, int srcOff,
                          final double[] dstPts, int dstOff, int numPts)
    {
        final int  inputDimension = numCol-1; // The last ordinate will be assumed equals to 1.
        final int outputDimension = numRow-1;
        final double[]     buffer = new double[numRow];
        if (srcPts == dstPts) {
            // We are going to write in the source array. Checks if
            // source and destination sections are going to clash.
            final int upperSrc = srcOff + numPts*inputDimension;
            if (upperSrc > dstOff) {
                if (inputDimension >= outputDimension ? dstOff > srcOff :
                            dstOff + numPts*outputDimension > upperSrc) {
                    // If source overlaps destination, then the easiest workaround is
                    // to copy source data. This is not the most efficient however...
                    srcPts = new double[numPts*inputDimension];
                    System.arraycopy(dstPts, srcOff, srcPts, 0, srcPts.length);
                    srcOff = 0;
                }
            }
        }
        while (--numPts >= 0) {
            int mix = 0;
            for (int j=0; j<numRow; j++) {
                double sum=elt[mix + inputDimension];
                for (int i=0; i<inputDimension; i++) {
                    sum += srcPts[srcOff+i]*elt[mix++];
                }
                buffer[j] = sum;
                mix++;
            }
            final double w = buffer[outputDimension];
            for (int j=0; j<outputDimension; j++) {
                // 'w' is equals to 1 if the transform is affine.
                dstPts[dstOff++] = buffer[j]/w;
            }
            srcOff += inputDimension;
        }
    }

    /**
     * Gets the derivative of this transform at a point.
     * For a matrix transform, the derivative is the
     * same everywhere.
     */
    @Override
    public Matrix derivative(final Point2D point) {
        return derivative((DirectPosition)null);
    }

    /**
     * Gets the derivative of this transform at a point.
     * For a matrix transform, the derivative is the
     * same everywhere.
     */
    @Override
    public Matrix derivative(final DirectPosition point) {
        final GeneralMatrix matrix = getGeneralMatrix();
        matrix.setSize(numRow-1, numCol-1);
        return matrix;
    }

    /**
     * Returns a copy of the matrix.
     */
    public Matrix getMatrix() {
        return getGeneralMatrix();
    }

    /**
     * Returns a copy of the matrix.
     */
    private GeneralMatrix getGeneralMatrix() {
        return new GeneralMatrix(numRow, numCol, elt);
    }

    /**
     * Gets the dimension of input points.
     */
    public int getSourceDimensions() {
        return numCol - 1;
    }

    /**
     * Gets the dimension of output points.
     */
    public int getTargetDimensions() {
        return numRow - 1;
    }

    /**
     * Tests whether this transform does not move any points.
     */
    @Override
    public boolean isIdentity() {
        if (numRow != numCol) {
            return false;
        }
        int index=0;
        for (int j=0; j<numRow; j++) {
            for (int i=0; i<numCol; i++) {
                if (elt[index++] != (i==j ? 1 : 0)) {
                    return false;
                }
            }
        }
        assert isIdentity(0);
        return true;
    }

    /**
     * Tests whether this transform does not move any points by using the provided tolerance.
     * This method work in the same way than
     * {@link org.geotools.referencing.operation.matrix.XMatrix#isIdentity(double)}.
     *
     * @since 2.4
     */
    public boolean isIdentity(double tolerance) {
        tolerance = Math.abs(tolerance);
        if (numRow != numCol) {
            return false;
        }
        int index=0;
        for (int j=0; j<numRow; j++) {
            for (int i=0; i<numCol; i++) {
                double e = elt[index++];
                if (i == j) {
                    e--;
                }
                // Uses '!' in order to catch NaN values.
                if (!(Math.abs(e) <= tolerance)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Creates the inverse transform of this object.
     */
    @Override
    public synchronized MathTransform inverse() throws NoninvertibleTransformException {
        if (inverse == null) {
            if (isIdentity()) {
                inverse = this;
            } else {
                final XMatrix matrix = getGeneralMatrix();
                try {
                    matrix.invert();
                } catch (SingularMatrixException exception) {
                    throw new NoninvertibleTransformException(Errors.format(
                              ErrorKeys.NONINVERTIBLE_TRANSFORM), exception);
                } catch (MismatchedSizeException exception) {
                    // This exception is thrown if the matrix is not square.
                    throw new NoninvertibleTransformException(Errors.format(
                              ErrorKeys.NONINVERTIBLE_TRANSFORM), exception);
                }
                inverse = new ProjectiveTransform(matrix);
                inverse.inverse = this;
            }
        }
        return inverse;
    }

    /**
     * Creates an inverse transform using the specified matrix.
     * To be overridden by {@link GeocentricTranslation}.
     */
    MathTransform createInverse(final Matrix matrix) {
        return new ProjectiveTransform(matrix);
    }

    /**
     * Returns a hash value for this transform.
     * This value need not remain consistent between
     * different implementations of the same class.
     */
    @Override
    public int hashCode() {
        long code = serialVersionUID;
        for (int i=elt.length; --i>=0;) {
            code = code*37 + Double.doubleToLongBits(elt[i]);
        }
        return (int)(code >>> 32) ^ (int)code;
    }

    /**
     * Compares the specified object with
     * this math transform for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final ProjectiveTransform that = (ProjectiveTransform) object;
            return this.numRow == that.numRow &&
                   this.numCol == that.numCol &&
                   Arrays.equals(this.elt, that.elt);
        }
        return false;
    }

    /**
     * The provider for the "<cite>Affine general parametric transformation</cite>" (EPSG 9624).
     * The OGC's name is {@code "Affine"}. The default matrix size is
     * {@value org.geotools.parameter.MatrixParameterDescriptors#DEFAULT_MATRIX_SIZE}&times;{@value
     * org.geotools.parameter.MatrixParameterDescriptors#DEFAULT_MATRIX_SIZE}.
     * <p>
     * Note that affine transform is a special case of projective transform.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static final class ProviderAffine extends MathTransformProvider {
        /**
         * Serial number for interoperability with different versions.
         */
        private static final long serialVersionUID = 649555815622129472L;

        /**
         * The set of predefined providers.
         */
        private static final ProviderAffine[] methods = new ProviderAffine[8];

        /**
         * The parameters group.
         *
         * @todo We should register EPSG parameter identifiers (A0, A1, A2, B0, B1, B2) as well.
         */
        static final ParameterDescriptorGroup PARAMETERS;
        static {
            final NamedIdentifier name = new NamedIdentifier(Citations.OGC, "Affine");
            final Map<String,Object> properties = new HashMap<String,Object>(4, 0.8f);
            properties.put(NAME_KEY,        name);
            properties.put(IDENTIFIERS_KEY, name);
            properties.put(ALIAS_KEY, new NamedIdentifier[] {name,
                new NamedIdentifier(Citations.EPSG, "Affine general parametric transformation"),
                new NamedIdentifier(Citations.EPSG, "9624"),
                new NamedIdentifier(Citations.GEOTOOLS,
                    Vocabulary.formatInternational(VocabularyKeys.AFFINE_TRANSFORM))
            });
            PARAMETERS = new MatrixParameterDescriptors(properties);
        }

        /**
         * Creates a provider for affine transform with a default matrix size.
         */
        public ProviderAffine() {
            this(MatrixParameterDescriptors.DEFAULT_MATRIX_SIZE-1,
                 MatrixParameterDescriptors.DEFAULT_MATRIX_SIZE-1);
            methods[MatrixParameterDescriptors.DEFAULT_MATRIX_SIZE-2] = this;
        }

        /**
         * Creates a provider for affine transform with the specified dimensions.
         */
        private ProviderAffine(final int sourceDimensions, final int targetDimensions) {
            super(sourceDimensions, targetDimensions, PARAMETERS);
        }

        /**
         * Returns the operation type.
         */
        @Override
        public Class<Conversion> getOperationType() {
            return Conversion.class;
        }

        /**
         * Creates a projective transform from the specified group of parameter values.
         *
         * @param  values The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup values)
                throws ParameterNotFoundException
        {
            final MathTransform transform;
            transform = create(((MatrixParameterDescriptors) getParameters()).getMatrix(values));
            return new Delegate(transform, getProvider(transform.getSourceDimensions(),
                                                       transform.getTargetDimensions()));
        }

        /**
         * Returns the operation method for the specified source and target dimensions.
         * This method provides different methods for different matrix sizes.
         *
         * @param sourceDimensions The number of source dimensions.
         * @param targetDimensions The number of target dimensions.
         * @return The provider for transforms of the given source and target dimensions.
         */
        public static ProviderAffine getProvider(final int sourceDimensions,
                                                 final int targetDimensions)
        {
            if (sourceDimensions == targetDimensions) {
                final int i = sourceDimensions - 1;
                if (i>=0 && i<methods.length) {
                    ProviderAffine method = methods[i];
                    if (method == null) {
                        methods[i] = method = new ProviderAffine(sourceDimensions, targetDimensions);
                    }
                    return method;
                }
            }
            return new ProviderAffine(sourceDimensions, targetDimensions);
        }
    }

    /**
     * The provider for the "<cite>Longitude rotation</cite>" (EPSG 9601).
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     */
    public static final class ProviderLongitudeRotation extends MathTransformProvider {
        /**
         * Serial number for interoperability with different versions.
         */
        private static final long serialVersionUID = -2104496465933824935L;

        /**
         * The longitude offset.
         */
        public static final ParameterDescriptor OFFSET = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.EPSG, "Longitude offset")
                },
                Double.NaN, -180, +180, NonSI.DEGREE_ANGLE);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                    new NamedIdentifier(Citations.EPSG, "Longitude rotation"),
                    new NamedIdentifier(Citations.EPSG, "9601")
                }, new ParameterDescriptor[] {
                    OFFSET
                });

        /**
         * Constructs a provider with default parameters.
         */
        public ProviderLongitudeRotation() {
            super(2, 2, PARAMETERS);
        }

        /**
         * Returns the operation type.
         */
        @Override
        public Class<Conversion> getOperationType() {
            return Conversion.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  values The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup values)
                throws ParameterNotFoundException
        {
            final double offset = doubleValue(OFFSET, values);
            return create(AffineTransform.getTranslateInstance(offset, 0));
        }
    }
}
