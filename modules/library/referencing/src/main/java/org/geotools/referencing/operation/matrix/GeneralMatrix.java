/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.matrix;

import java.awt.geom.AffineTransform;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Serializable;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Locale;
import org.ejml.UtilEjml;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.CommonOps_DDRM;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Errors;
import org.geotools.util.ContentFormatException;
import org.geotools.util.LineFormat;
import org.geotools.util.Utilities;
import org.geotools.util.XArray;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.Matrix;

/**
 * A two dimensional array of numbers. Row and column numbering begins with zero.
 *
 * @since 2.2
 * @version 14.0
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini
 * @see java.awt.geom.AffineTransform
 */
public class GeneralMatrix implements XMatrix, Serializable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = 8447482612423035361L;

    DMatrixRMaj mat;

    /**
     * Constructs a square identity matrix of size {@code size}&nbsp;&times;&nbsp;{@code size}.
     *
     * @param size The number of rows and columns.
     */
    public GeneralMatrix(final int size) {
        mat = new DMatrixRMaj(size, size);
        setIdentity();
    }

    /**
     * Creates a matrix of size {@code numRow}&nbsp;&times;&nbsp;{@code numCol}. Elements on the
     * diagonal <var>j==i</var> are set to 1.
     *
     * @param numRow Number of rows.
     * @param numCol Number of columns.
     */
    public GeneralMatrix(final int numRow, final int numCol) {
        mat = new DMatrixRMaj(numRow, numCol);
        setIdentity();
    }

    /**
     * Constructs a {@code numRow}&nbsp;&times;&nbsp;{@code numCol} matrix initialized to the values
     * in the {@code matrix} array. The array values are copied in one row at a time in row major
     * fashion. The array should be exactly <code>numRow*numCol</code> in length. Note that because
     * row and column numbering begins with zero, {@code numRow} and {@code numCol} will be one
     * larger than the maximum possible matrix index values.
     *
     * @param numRow Number of rows.
     * @param numCol Number of columns.
     * @param matrix Initial values in row order
     */
    public GeneralMatrix(final int numRow, final int numCol, final double... matrix) {
        mat = new DMatrixRMaj(numRow, numCol, true, matrix);
        if (numRow * numCol != matrix.length) {
            throw new IllegalArgumentException(String.valueOf(matrix.length));
        }
    }

    /**
     * Constructs a {@code numRow}&nbsp;&times;&nbsp;{@code numCol} matrix initialized to the values
     * in the {@code matrix} array. The array values are copied in one row at a time in row major
     * fashion. The array should be exactly <code>numRow*numCol</code> in length. Note that because
     * row and column numbering begins with zero, {@code numRow} and {@code numCol} will be one
     * larger than the maximum possible matrix index values.
     *
     * @param numRow Number of rows.
     * @param numCol Number of columns.
     * @param matrix Initial values in row order
     */
    public GeneralMatrix(final int numRow, final int numCol, final Matrix matrix) {
        mat = new DMatrixRMaj(numRow, numCol);
        if (matrix.getNumRow() != numRow || matrix.getNumCol() != numCol) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_MATRIX_SIZE));
        }
        for (int j = 0; j < numRow; j++) {
            for (int i = 0; i < numCol; i++) {
                setElement(j, i, matrix.getElement(j, i));
            }
        }
    }

    /**
     * Constructs a new matrix from a two-dimensional array of doubles.
     *
     * @param matrix Array of rows. Each row must have the same length.
     * @throws IllegalArgumentException if the specified matrix is not regular (i.e. if all rows
     *     doesn't have the same length).
     */
    public GeneralMatrix(final double[][] matrix) throws IllegalArgumentException {
        mat = new DMatrixRMaj(matrix);
        final int numRow = getNumRow();
        final int numCol = getNumCol();
        for (int j = 0; j < numRow; j++) {
            if (matrix[j].length != numCol) {
                throw new IllegalArgumentException(Errors.format(ErrorKeys.MATRIX_NOT_REGULAR));
            }
            for (int i = 0; i < numCol; i++) {
                mat.set(j, i, matrix[j][i]);
            }
        }
    }

    /**
     * Constructs a new matrix and copies the initial values from the parameter matrix.
     *
     * @param matrix The matrix to copy.
     */
    public GeneralMatrix(final Matrix matrix) {
        if (matrix instanceof GeneralMatrix) {
            mat = new DMatrixRMaj(((GeneralMatrix) matrix).mat);
        } else {
            mat = new DMatrixRMaj(matrix.getNumRow(), matrix.getNumCol());

            final int height = getNumRow();
            final int width = getNumCol();
            for (int j = 0; j < height; j++) {
                for (int i = 0; i < width; i++) {
                    mat.set(j, i, matrix.getElement(j, i));
                }
            }
        }
    }

    /**
     * Constructs a new matrix and copies the initial values from the parameter matrix.
     *
     * @param matrix The matrix to copy.
     */
    public GeneralMatrix(final GeneralMatrix matrix) {
        mat = new DMatrixRMaj(matrix.mat);
    }

    /**
     * Constructs a 3&times;3 matrix from the specified affine transform.
     *
     * @param transform The matrix to copy.
     */
    public GeneralMatrix(final AffineTransform transform) {
        mat =
                new DMatrixRMaj(
                        3,
                        3,
                        true,
                        new double[] {
                            transform.getScaleX(),
                            transform.getShearX(),
                            transform.getTranslateX(),
                            transform.getShearY(),
                            transform.getScaleY(),
                            transform.getTranslateY(),
                            0,
                            0,
                            1
                        });
        assert isAffine() : this;
    }

    /**
     * Constructs a transform that maps a source region to a destination region. Axis order and
     * direction are left unchanged.
     *
     * <p>If the source dimension is equals to the destination dimension, then the transform is
     * affine. However, the following special cases are also handled:
     *
     * <UL>
     *   <LI>If the target dimension is smaller than the source dimension, then extra dimensions are
     *       dropped.
     *   <LI>If the target dimension is greater than the source dimension, then the coordinates in
     *       the new dimensions are set to 0.
     * </UL>
     *
     * @param srcRegion The source region.
     * @param dstRegion The destination region.
     */
    public GeneralMatrix(final Envelope srcRegion, final Envelope dstRegion) {
        mat = new DMatrixRMaj(dstRegion.getDimension() + 1, srcRegion.getDimension() + 1);

        // Next lines should be first if only Sun could fix RFE #4093999 (sigh...)
        final int srcDim = srcRegion.getDimension();
        final int dstDim = dstRegion.getDimension();
        for (int i = Math.min(srcDim, dstDim); --i >= 0; ) {
            double scale = dstRegion.getSpan(i) / srcRegion.getSpan(i);
            double translate = dstRegion.getMinimum(i) - srcRegion.getMinimum(i) * scale;
            setElement(i, i, scale);
            setElement(i, srcDim, translate);
        }
        setElement(dstDim, srcDim, 1);
        assert (srcDim != dstDim) || isAffine() : this;
    }

    /**
     * Constructs a transform changing axis order and/or direction. For example, the transform may
     * converts (NORTH,WEST) coordinates into (EAST,NORTH). Axis direction can be inversed only. For
     * example, it is illegal to transform (NORTH,WEST) coordinates into (NORTH,DOWN).
     *
     * <p>If the source dimension is equals to the destination dimension, then the transform is
     * affine. However, the following special cases are also handled: <br>
     *
     * <UL>
     *   <LI>If the target dimension is smaller than the source dimension, extra axis are dropped.
     *       An exception is thrown if the target contains some axis not found in the source.
     * </UL>
     *
     * @param srcAxis The set of axis direction for source coordinate system.
     * @param dstAxis The set of axis direction for destination coordinate system.
     * @throws IllegalArgumentException If {@code dstAxis} contains some axis not found in {@code
     *     srcAxis}, or if some colinear axis were found.
     */
    public GeneralMatrix(final AxisDirection[] srcAxis, final AxisDirection[] dstAxis) {
        this(null, srcAxis, null, dstAxis, false);
    }

    /**
     * Constructs a transform mapping a source region to a destination region. Axis order and/or
     * direction can be changed during the process. For example, the transform may convert
     * (NORTH,WEST) coordinates into (EAST,NORTH). Axis direction can be inversed only. For example,
     * it is illegal to transform (NORTH,WEST) coordinates into (NORTH,DOWN).
     *
     * <p>If the source dimension is equals to the destination dimension, then the transform is
     * affine. However, the following special cases are also handled: <br>
     *
     * <UL>
     *   <LI>If the target dimension is smaller than the source dimension, extra axis are dropped.
     *       An exception is thrown if the target contains some axis not found in the source.
     * </UL>
     *
     * @param srcRegion The source region.
     * @param srcAxis Axis direction for each dimension of the source region.
     * @param dstRegion The destination region.
     * @param dstAxis Axis direction for each dimension of the destination region.
     * @throws MismatchedDimensionException if the envelope dimension doesn't matches the axis
     *     direction array length.
     * @throws IllegalArgumentException If {@code dstAxis} contains some axis not found in {@code
     *     srcAxis}, or if some colinear axis were found.
     */
    public GeneralMatrix(
            final Envelope srcRegion,
            final AxisDirection[] srcAxis,
            final Envelope dstRegion,
            final AxisDirection[] dstAxis) {
        this(srcRegion, srcAxis, dstRegion, dstAxis, true);
    }

    /**
     * Implementation of constructors expecting envelope and/or axis directions.
     *
     * @param validRegions {@code true} if source and destination regions must be taken in account.
     *     If {@code false}, then source and destination regions will be ignored and may be null.
     */
    private GeneralMatrix(
            final Envelope srcRegion,
            final AxisDirection[] srcAxis,
            final Envelope dstRegion,
            final AxisDirection[] dstAxis,
            final boolean validRegions) {
        this(dstAxis.length + 1, srcAxis.length + 1);
        if (validRegions) {
            ensureDimensionMatch("srcRegion", srcRegion, srcAxis.length);
            ensureDimensionMatch("dstRegion", dstRegion, dstAxis.length);
        }
        /*
         * Map source axis to destination axis.  If no axis is moved (for example if the user
         * want to transform (NORTH,EAST) to (SOUTH,EAST)), then source and destination index
         * will be equal.   If some axis are moved (for example if the user want to transform
         * (NORTH,EAST) to (EAST,NORTH)),  then ordinates at index {@code srcIndex} will
         * have to be moved at index {@code dstIndex}.
         */
        setZero();
        for (int dstIndex = 0; dstIndex < dstAxis.length; dstIndex++) {
            boolean hasFound = false;
            final AxisDirection dstAxe = dstAxis[dstIndex];
            final AxisDirection search = dstAxe.absolute();
            for (int srcIndex = 0; srcIndex < srcAxis.length; srcIndex++) {
                final AxisDirection srcAxe = srcAxis[srcIndex];
                if (search.equals(srcAxe.absolute())) {
                    if (hasFound) {
                        // TODO: Use the localized version of 'getName' in GeoAPI 2.1
                        throw new IllegalArgumentException(
                                Errors.format(
                                        ErrorKeys.COLINEAR_AXIS_$2, srcAxe.name(), dstAxe.name()));
                    }
                    hasFound = true;
                    /*
                     * Set the matrix elements. Some matrix elements will never
                     * be set. They will be left to zero, which is their wanted
                     * value.
                     */
                    final boolean normal = srcAxe.equals(dstAxe);
                    double scale = (normal) ? +1 : -1;
                    double translate = 0;
                    if (validRegions) {
                        translate =
                                (normal)
                                        ? dstRegion.getMinimum(dstIndex)
                                        : dstRegion.getMaximum(dstIndex);
                        scale *= dstRegion.getSpan(dstIndex) / srcRegion.getSpan(srcIndex);
                        translate -= srcRegion.getMinimum(srcIndex) * scale;
                    }
                    setElement(dstIndex, srcIndex, scale);
                    setElement(dstIndex, srcAxis.length, translate);
                }
            }
            if (!hasFound) {
                // TODO: Use the localized version of 'getName' in GeoAPI 2.1
                throw new IllegalArgumentException(
                        Errors.format(ErrorKeys.NO_SOURCE_AXIS_$1, dstAxis[dstIndex].name()));
            }
        }
        setElement(dstAxis.length, srcAxis.length, 1);
        assert (srcAxis.length != dstAxis.length) || isAffine() : this;
    }

    //
    // In-place operations
    //
    /**
     * Cast (or convert) Matrix to internal DMatrixRMaj representation required for CommonOps_DDRM.
     */
    private DMatrixRMaj internal(Matrix matrix) {
        if (matrix instanceof GeneralMatrix) {
            return ((GeneralMatrix) matrix).mat;
        } else {
            DMatrixRMaj a = new DMatrixRMaj(matrix.getNumRow(), matrix.getNumCol());
            for (int j = 0; j < a.numRows; j++) {
                for (int i = 0; i < a.numCols; i++) {
                    a.set(j, i, matrix.getElement(j, i));
                }
            }
            return a;
        }
    }

    /**
     * Convenience method for checking object dimension validity. This method is usually invoked for
     * argument checking.
     *
     * @param name The name of the argument to check.
     * @param envelope The envelope to check.
     * @param dimension The expected dimension for the object.
     * @throws MismatchedDimensionException if the envelope doesn't have the expected dimension.
     */
    private static void ensureDimensionMatch(
            final String name, final Envelope envelope, final int dimension)
            throws MismatchedDimensionException {
        final int dim = envelope.getDimension();
        if (dimension != dim) {
            throw new MismatchedDimensionException(
                    Errors.format(ErrorKeys.MISMATCHED_DIMENSION_$3, name, dim, dimension));
        }
    }

    /**
     * Retrieves the specifiable values in the transformation matrix into a 2-dimensional array of
     * double precision values. The values are stored into the 2-dimensional array using the row
     * index as the first subscript and the column index as the second. Values are copied; changes
     * to the returned array will not change this matrix.
     *
     * @param matrix The matrix to extract elements from.
     * @return The matrix elements.
     */
    public static double[][] getElements(final Matrix matrix) {
        if (matrix instanceof GeneralMatrix) {
            return ((GeneralMatrix) matrix).getElements();
        }
        final int numCol = matrix.getNumCol();
        final double[][] rows = new double[matrix.getNumRow()][];
        for (int j = 0; j < rows.length; j++) {
            final double[] row;
            rows[j] = row = new double[numCol];
            for (int i = 0; i < row.length; i++) {
                row[i] = matrix.getElement(j, i);
            }
        }
        return rows;
    }

    /**
     * Retrieves the specifiable values in the transformation matrix into a 2-dimensional array of
     * double precision values. The values are stored into the 2-dimensional array using the row
     * index as the first subscript and the column index as the second. Values are copied; changes
     * to the returned array will not change this matrix.
     *
     * @return The matrix elements.
     */
    public final double[][] getElements() {
        final int numCol = getNumCol();
        final double[][] rows = new double[getNumRow()][];
        for (int j = 0; j < rows.length; j++) {
            getRow(j, rows[j] = new double[numCol]);
        }
        return rows;
    }

    /** {@inheritDoc} */
    public final boolean isAffine() {
        int dimension = getNumRow();
        if (dimension != getNumCol()) {
            return false;
        }
        dimension--;
        for (int i = 0; i <= dimension; i++) {
            if (getElement(dimension, i) != (i == dimension ? 1 : 0)) {
                return false;
            }
        }
        return true;
    }

    /** Changes the sign of each element in the matrix. */
    @Override
    public void negate() {
        // JNH: This seems the most aggressive approach.
        CommonOps_DDRM.changeSign(mat);
    }

    @Override
    public void negate(Matrix matrix) {
        DMatrixRMaj a = internal(matrix);
        CommonOps_DDRM.changeSign(a);
        this.mat = a;
    }

    /** Transposes the matrix. */
    @Override
    public void transpose() {
        CommonOps_DDRM.transpose(mat);
    }

    @Override
    public void transpose(Matrix matrix) {
        DMatrixRMaj a = internal(matrix);
        CommonOps_DDRM.transpose(a, mat);
    }

    @Override
    public void invert() {
        boolean success = CommonOps_DDRM.invert(mat);
        if (!success) {
            throw new SingularMatrixException("Could not invert, possible singular matrix?");
        }
    }

    @Override
    public void invert(Matrix matrix) throws SingularMatrixException {
        DMatrixRMaj a;
        if (matrix instanceof GeneralMatrix) {
            a = new DMatrixRMaj(((GeneralMatrix) matrix).mat);
        } else {
            a = new DMatrixRMaj(matrix.getNumRow(), matrix.getNumCol());
            for (int j = 0; j < mat.numRows; j++) {
                for (int i = 0; i < mat.numCols; i++) {
                    mat.set(j, i, matrix.getElement(j, i));
                }
            }
        }
        boolean success = CommonOps_DDRM.invert(a);
        if (!success) {
            throw new SingularMatrixException("Could not invert, possible singular matrix?");
        }
        this.mat = a;
    }

    /**
     * Gets the number of rows in the matrix.
     *
     * @return The number of rows in the matrix.
     */
    @Override
    public int getNumRow() {
        return mat.getNumRows();
    }

    /**
     * Gets the number of columns in the matrix.
     *
     * @return The number of columns in the matrix.
     */
    @Override
    public int getNumCol() {
        return mat.getNumCols();
    }

    /**
     * Returns the value at the row, column position in the matrix.
     *
     * @return Matrix value at the given row and column.
     */
    @Override
    public double getElement(int row, int column) {
        return mat.get(row, column);
    }

    public void setColumn(int column, double... values) {
        if (values.length != mat.getNumCols()) {
            throw new IllegalArgumentException(
                    "Call setRow received an array of length "
                            + values.length
                            + ".  "
                            + "The dimensions of the matrix is "
                            + mat.getNumRows()
                            + " by "
                            + mat.getNumCols()
                            + ".");
        }
        for (int i = 0; i < values.length; i++) {
            mat.set(i, column, values[i]);
        }
    }

    public void setRow(int row, double... values) {
        if (values.length != mat.getNumCols()) {
            throw new IllegalArgumentException(
                    "Call setRow received an array of length "
                            + values.length
                            + ".  "
                            + "The dimensions of the matrix is "
                            + mat.getNumRows()
                            + " by "
                            + mat.getNumCols()
                            + ".");
        }

        for (int i = 0; i < values.length; i++) {
            mat.set(row, i, values[i]);
        }
    }

    /** Sets the value of the row, column position in the matrix. */
    @Override
    public void setElement(int row, int column, double value) {
        mat.set(row, column, value);
    }

    /** Sets each value of the matrix to 0.0. */
    @Override
    public void setZero() {
        mat.zero();
    }

    /** Sets the main diagonal of this matrix to be 1.0. */
    @Override
    public void setIdentity() {
        CommonOps_DDRM.setIdentity(mat);
    }

    /** Returns {@code true} if this matrix is an identity matrix. */
    public final boolean isIdentity() {
        final int numRow = getNumRow();
        final int numCol = getNumCol();
        if (numRow != numCol) {
            return false;
        }
        for (int j = 0; j < numRow; j++) {
            for (int i = 0; i < numCol; i++) {
                if (getElement(j, i) != (i == j ? 1.0 : 0.0)) {
                    return false;
                }
            }
        }
        assert isAffine() : this;
        assert isIdentity(0) : this;
        return true;
    }

    /**
     * {@inheritDoc}
     *
     * @since 2.3.1
     */
    public final boolean isIdentity(double tolerance) {
        return isIdentity(this, tolerance);
    }

    /** Returns {@code true} if the matrix is an identity matrix using the provided tolerance. */
    static boolean isIdentity(final Matrix matrix, double tolerance) {
        tolerance = Math.abs(tolerance);
        final int numRow = matrix.getNumRow();
        final int numCol = matrix.getNumCol();
        if (numRow != numCol) {
            return false;
        }
        for (int j = 0; j < numRow; j++) {
            for (int i = 0; i < numCol; i++) {
                double e = matrix.getElement(j, i);
                if (i == j) {
                    e--;
                }
                if (!(Math.abs(e) <= tolerance)) { // Uses '!' in order to catch NaN values.
                    return false;
                }
            }
        }
        // Note: we can't assert matrix.isAffine().
        return true;
    }

    /** {@inheritDoc} */
    public final void multiply(final Matrix matrix) {
        mul(matrix);
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        if (mat == null) {
            return prime * result;
        }
        result = prime * result + mat.numRows;
        result = prime * result + mat.numCols;
        for (double d : mat.data) {
            long bits = Double.doubleToRawLongBits(d);
            result = prime * result + ((int) (bits ^ (bits >>> 32)));
        }
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        GeneralMatrix other = (GeneralMatrix) obj;
        return equals(other, 0);
    }

    public boolean equals(final Matrix matrix, final double tolerance) {
        return epsilonEquals(this, matrix, tolerance);
    }

    /** Compares the element values. */
    static boolean epsilonEquals(final Matrix m1, final Matrix m2, final double tolerance) {
        final int numRow = m1.getNumRow();
        if (numRow != m2.getNumRow()) {
            return false;
        }
        final int numCol = m1.getNumCol();
        if (numCol != m2.getNumCol()) {
            return false;
        }
        for (int j = 0; j < numRow; j++) {
            for (int i = 0; i < numCol; i++) {
                final double v1 = m1.getElement(j, i);
                final double v2 = m2.getElement(j, i);
                if (!(Math.abs(v1 - v2) <= tolerance)) {
                    if (Double.doubleToLongBits(v1) == Double.doubleToLongBits(v2)) {
                        // Special case for NaN and infinite values.
                        continue;
                    }
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Returns an affine transform for this matrix. This is a convenience method for
     * interoperability with Java2D.
     *
     * @return The affine transform for this matrix.
     * @throws IllegalStateException if this matrix is not 3&times;3, or if the last row is not
     *     {@code [0 0 1]}.
     */
    public final AffineTransform toAffineTransform2D() throws IllegalStateException {
        int check;
        if ((check = getNumRow()) != 3 || (check = getNumCol()) != 3) {
            throw new IllegalStateException(
                    Errors.format(ErrorKeys.NOT_TWO_DIMENSIONAL_$1, check - 1));
        }
        if (isAffine()) {
            return new AffineTransform(
                    getElement(0, 0),
                    getElement(1, 0),
                    getElement(0, 1),
                    getElement(1, 1),
                    getElement(0, 2),
                    getElement(1, 2));
        }
        throw new IllegalStateException(Errors.format(ErrorKeys.NOT_AN_AFFINE_TRANSFORM));
    }

    /**
     * Loads data from the specified file until the first blank line or end of file.
     *
     * @param file The file to read.
     * @return The matrix parsed from the file.
     * @throws IOException if an error occured while reading the file.
     * @since 2.2
     */
    public static GeneralMatrix load(final File file) throws IOException {
        final BufferedReader in = new BufferedReader(new FileReader(file));
        try {
            return load(in, Locale.US);
        } finally {
            in.close();
        }
    }

    /**
     * Loads data from the specified streal until the first blank line or end of stream.
     *
     * @param in The stream to read.
     * @param locale The locale for the numbers to be parsed.
     * @return The matrix parsed from the stream.
     * @throws IOException if an error occured while reading the stream.
     * @since 2.2
     */
    public static GeneralMatrix load(final BufferedReader in, final Locale locale)
            throws IOException {
        final LineFormat parser = new LineFormat(locale);
        double[] data = null;
        double[] row = null;
        int numRow = 0;
        int numData = 0;
        String line;
        while ((line = in.readLine()) != null) {
            if ((line = line.trim()).length() == 0) {
                if (numRow == 0) {
                    continue;
                } else {
                    break;
                }
            }
            try {
                parser.setLine(line);
                row = parser.getValues(row);
            } catch (ParseException exception) {
                throw new ContentFormatException(exception.getLocalizedMessage(), exception);
            }
            final int upper = numData + row.length;
            if (data == null) {
                // Assumes a square matrix.
                data = new double[numData * numData];
            }
            if (upper > data.length) {
                data = XArray.resize(data, upper * 2);
            }
            System.arraycopy(row, 0, data, numData, row.length);
            numData = upper;
            numRow++;
            assert numData % numRow == 0 : numData;
        }
        data = (data != null) ? XArray.resize(data, numData) : new double[0];
        return new GeneralMatrix(numRow, numData / numRow, data);
    }

    /**
     * Returns a string representation of this matrix. The returned string is implementation
     * dependent. It is usually provided for debugging purposes only.
     */
    @Override
    public String toString() {
        return toString(this);
    }

    /**
     * Returns a string representation of the specified matrix. The returned string is
     * implementation dependent. It is usually provided for debugging purposes only.
     */
    static String toString(final Matrix matrix) {
        final int numRow = matrix.getNumRow();
        final int numCol = matrix.getNumCol();
        StringBuffer buffer = new StringBuffer();
        final int columnWidth = 12;
        final String lineSeparator = System.getProperty("line.separator", "\n");
        final FieldPosition dummy = new FieldPosition(0);
        final NumberFormat format = NumberFormat.getNumberInstance();
        format.setGroupingUsed(false);
        format.setMinimumFractionDigits(6);
        format.setMaximumFractionDigits(6);
        for (int j = 0; j < numRow; j++) {
            for (int i = 0; i < numCol; i++) {
                final int position = buffer.length();
                buffer = format.format(matrix.getElement(j, i), buffer, dummy);
                final int spaces = Math.max(columnWidth - (buffer.length() - position), 1);
                buffer.insert(position, Utilities.spaces(spaces));
            }
            buffer.append(lineSeparator);
        }
        return buffer.toString();
    }

    // Method Compatibility
    /** Returns a clone of this matrix. */
    @Override
    public GeneralMatrix clone() {
        try {
            super.clone();
        } catch (CloneNotSupportedException e) {
            // should not happen
            throw new RuntimeException(e);
        }
        return new GeneralMatrix(this);
    }

    /** Extract a subMatrix to the provided target */
    public void copySubMatrix(
            int rowSource,
            int colSource,
            int numRows,
            int numCol,
            int rowDest,
            int colDest,
            GeneralMatrix target) {
        int rowLimit = rowSource + numRows;
        int colLimit = colSource + numCol;
        CommonOps_DDRM.extract(
                mat, rowSource, rowLimit, colSource, colLimit, target.mat, rowDest, colDest);
    }

    /** Extract col to provided array. */
    public void getColumn(int col, double[] array) {
        for (int j = 0; j < array.length; j++) {
            array[j] = mat.get(j, col);
        }
    }

    @Override
    public void mul(double scalar) {
        CommonOps_DDRM.scale(scalar, this.mat);
    }

    @Override
    public void mul(double scalar, Matrix matrix) {
        DMatrixRMaj a = new DMatrixRMaj(matrix.getNumRow(), matrix.getNumCol());
        CommonOps_DDRM.scale(scalar, internal(matrix), a);
        mat = a;
    }

    /** Extract row to provided array */
    public void getRow(int row, double[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = mat.get(row, i);
        }
    }

    /** In-place multiply with provided matrix. */
    public final void mul(Matrix matrix) {
        DMatrixRMaj b = internal(matrix);
        DMatrixRMaj ret = new DMatrixRMaj(mat.numRows, b.numCols);
        CommonOps_DDRM.mult(mat, b, ret);
        mat = ret;
    }

    /** In-place update from matrix1 * matrix2. */
    public void mul(Matrix matrix1, Matrix matrix2) {
        DMatrixRMaj a = internal(matrix1);
        DMatrixRMaj b = internal(matrix2);
        if (a == mat || b == mat) {
            mat = new DMatrixRMaj(a.numRows, b.numCols);
        } else {
            mat.reshape(a.numRows, b.numCols, false);
        }
        CommonOps_DDRM.mult(a, b, mat);
    }

    @Override
    public void sub(double scalar) {
        CommonOps_DDRM.subtract(mat, scalar, mat);
    }

    @Override
    public void sub(double scalar, Matrix matrix) {
        DMatrixRMaj a = internal(matrix);
        mat.reshape(a.numRows, a.numCols, false);
        CommonOps_DDRM.subtract(scalar, a, mat);
    }

    public void sub(Matrix matrix) {
        CommonOps_DDRM.subtract(mat, internal(matrix), mat);
    }

    public void sub(Matrix matrix1, Matrix matrix2) {
        DMatrixRMaj a = internal(matrix1);
        DMatrixRMaj b = internal(matrix2);
        mat.reshape(a.numRows, a.numCols, false);
        CommonOps_DDRM.subtract(a, b, mat);
    }

    /** Update in place to the provided matrix (row-order). */
    public void set(double[] matrix) {
        mat.setData(matrix);
    }

    /**
     * Resize the matrix to the specified number of rows and columns (preserving remaining
     * elements).
     *
     * @param numRows The new number of rows in the matrix.
     * @param numCols The new number of columns in the matrix.
     */
    public void setSize(int numRows, int numCols) {
        if (numRows != mat.numCols || numCols != mat.numCols) {
            // grow or shrink
            DMatrixRMaj ret = new DMatrixRMaj(numRows, numCols);
            CommonOps_DDRM.extract(mat, 0, numRows, 0, numCols, ret, 0, 0);
            mat = ret;
        }
    }

    @Override
    public void add(double scalar) {
        CommonOps_DDRM.add(mat, scalar, mat);
    }

    @Override
    public void add(double scalar, XMatrix matrix) {
        DMatrixRMaj a = internal(matrix);
        mat.reshape(a.numRows, a.numCols, false);
        CommonOps_DDRM.add(a, scalar, mat);
    }

    @Override
    public void add(XMatrix matrix) {
        CommonOps_DDRM.add(mat, internal(matrix), mat);
    }

    @Override
    public void add(XMatrix matrix1, XMatrix matrix2) {
        DMatrixRMaj a = internal(matrix1);
        DMatrixRMaj b = internal(matrix2);
        mat.reshape(a.numRows, a.numCols, false);
        CommonOps_DDRM.add(a, b, mat);
    }

    @Override
    public double determinate() {
        double det = CommonOps_DDRM.det(mat);
        // if the decomposition silently failed then the matrix is most likely singular
        if (UtilEjml.isUncountable(det)) return 0;
        return det;
    }
}
