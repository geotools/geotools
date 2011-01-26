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
package org.geotools.referencing.operation.matrix;

import java.awt.geom.AffineTransform;
import java.text.ParseException;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Locale;
import java.io.File;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;
import javax.vecmath.GMatrix;

import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.operation.Matrix;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;

import org.geotools.io.LineFormat;
import org.geotools.io.ContentFormatException;
import org.geotools.util.Utilities;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * A two dimensional array of numbers. Row and column numbering begins with zero.
 *
 * @since 2.2
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @author Simone Giannecchini
 *
 * @see javax.vecmath.GMatrix
 * @see java.awt.geom.AffineTransform
 * @see javax.media.jai.PerspectiveTransform
 * @see javax.media.j3d.Transform3D
 * @see <A HREF="http://math.nist.gov/javanumerics/jama/">Jama matrix</A>
 * @see <A HREF="http://jcp.org/jsr/detail/83.jsp">JSR-83 Multiarray package</A>
 */
public class GeneralMatrix extends GMatrix implements XMatrix {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8447482612423035360L;

    /**
     * Constructs a square identity matrix of size {@code size}&nbsp;&times;&nbsp;{@code size}.
     *
     * @param size The number of rows and columns.
     */
    public GeneralMatrix(final int size) {
        super(size, size);
    }

    /**
     * Creates a matrix of size {@code numRow}&nbsp;&times;&nbsp;{@code numCol}.
     * Elements on the diagonal <var>j==i</var> are set to 1.
     *
     * @param numRow Number of rows.
     * @param numCol Number of columns.
     */
    public GeneralMatrix(final int numRow, final int numCol) {
        super(numRow, numCol);
    }

    /**
     * Constructs a {@code numRow}&nbsp;&times;&nbsp;{@code numCol} matrix
     * initialized to the values in the {@code matrix} array. The array values
     * are copied in one row at a time in row major fashion. The array should be
     * exactly <code>numRow*numCol</code> in length. Note that because row and column
     * numbering begins with zero, {@code numRow} and {@code numCol} will be
     * one larger than the maximum possible matrix index values.
     *
     * @param numRow Number of rows.
     * @param numCol Number of columns.
     * @param matrix Initial values.
     */
    public GeneralMatrix(final int numRow, final int numCol, final double[] matrix) {
        super(numRow, numCol, matrix);
        if (numRow*numCol != matrix.length) {
            throw new IllegalArgumentException(String.valueOf(matrix.length));
        }
    }

    /**
     * Constructs a new matrix from a two-dimensional array of doubles.
     *
     * @param  matrix Array of rows. Each row must have the same length.
     * @throws IllegalArgumentException if the specified matrix is not regular
     *         (i.e. if all rows doesn't have the same length).
     */
    public GeneralMatrix(final double[][] matrix) throws IllegalArgumentException {
        super(matrix.length, (matrix.length!=0) ? matrix[0].length : 0);
        final int numRow = getNumRow();
        final int numCol = getNumCol();
        for (int j=0; j<numRow; j++) {
            if (matrix[j].length!=numCol) {
                throw new IllegalArgumentException(Errors.format(ErrorKeys.MATRIX_NOT_REGULAR));
            }
            setRow(j, matrix[j]);
        }
    }

    /**
     * Constructs a new matrix and copies the initial values from the parameter matrix.
     *
     * @param matrix The matrix to copy.
     */
    public GeneralMatrix(final Matrix matrix) {
        this(matrix.getNumRow(), matrix.getNumCol());
        final int height = getNumRow();
        final int width  = getNumCol();
        for (int j=0; j<height; j++) {
            for (int i=0; i<width; i++) {
                setElement(j, i, matrix.getElement(j, i));
            }
        }
    }

    /**
     * Constructs a new matrix and copies the initial values from the parameter matrix.
     *
     * @param matrix The matrix to copy.
     */
    public GeneralMatrix(final GMatrix matrix) {
        super(matrix);
    }

    /**
     * Constructs a 3&times;3 matrix from the specified affine transform.
     *
     * @param transform The matrix to copy.
     */
    public GeneralMatrix(final AffineTransform transform) {
        super(3,3, new double[] {
            transform.getScaleX(), transform.getShearX(), transform.getTranslateX(),
            transform.getShearY(), transform.getScaleY(), transform.getTranslateY(),
            0,                     0,                     1
        });
        assert isAffine() : this;
    }

    /**
     * Constructs a transform that maps a source region to a destination region.
     * Axis order and direction are left unchanged.
     *
     * <P>If the source dimension is equals to the destination dimension,
     * then the transform is affine. However, the following special cases
     * are also handled:</P>
     *
     * <UL>
     *   <LI>If the target dimension is smaller than the source dimension,
     *       then extra dimensions are dropped.</LI>
     *   <LI>If the target dimension is greater than the source dimension,
     *       then the coordinates in the new dimensions are set to 0.</LI>
     * </UL>
     *
     * @param srcRegion The source region.
     * @param dstRegion The destination region.
     */
    public GeneralMatrix(final Envelope srcRegion,
                         final Envelope dstRegion)
    {
        super(dstRegion.getDimension()+1, srcRegion.getDimension()+1);
        // Next lines should be first if only Sun could fix RFE #4093999 (sigh...)
        final int srcDim = srcRegion.getDimension();
        final int dstDim = dstRegion.getDimension();
        for (int i=Math.min(srcDim, dstDim); --i>=0;) {
            double scale     = dstRegion.getLength (i) / srcRegion.getLength (i);
            double translate = dstRegion.getMinimum(i) - srcRegion.getMinimum(i)*scale;
            setElement(i, i,         scale);
            setElement(i, srcDim, translate);
        }
        setElement(dstDim, srcDim, 1);
        assert (srcDim != dstDim) || isAffine() : this;
    }

    /**
     * Constructs a transform changing axis order and/or direction.
     * For example, the transform may converts (NORTH,WEST) coordinates
     * into (EAST,NORTH). Axis direction can be inversed only. For example,
     * it is illegal to transform (NORTH,WEST) coordinates into (NORTH,DOWN).
     *
     * <P>If the source dimension is equals to the destination dimension,
     * then the transform is affine. However, the following special cases
     * are also handled:</P>
     * <BR>
     * <UL>
     *   <LI>If the target dimension is smaller than the source dimension,
     *       extra axis are dropped. An exception is thrown if the target
     *       contains some axis not found in the source.</LI>
     * </UL>
     *
     * @param  srcAxis The set of axis direction for source coordinate system.
     * @param  dstAxis The set of axis direction for destination coordinate system.
     * @throws IllegalArgumentException If {@code dstAxis} contains some axis
     *         not found in {@code srcAxis}, or if some colinear axis were found.
     */
    public GeneralMatrix(final AxisDirection[] srcAxis,
                         final AxisDirection[] dstAxis)
    {
        this(null, srcAxis, null, dstAxis, false);
    }

    /**
     * Constructs a transform mapping a source region to a destination region.
     * Axis order and/or direction can be changed during the process.
     * For example, the transform may convert (NORTH,WEST) coordinates
     * into (EAST,NORTH). Axis direction can be inversed only. For example,
     * it is illegal to transform (NORTH,WEST) coordinates into (NORTH,DOWN).
     *
     * <P>If the source dimension is equals to the destination dimension,
     * then the transform is affine. However, the following special cases
     * are also handled:</P>
     * <BR>
     * <UL>
     *   <LI>If the target dimension is smaller than the source dimension,
     *       extra axis are dropped. An exception is thrown if the target
     *       contains some axis not found in the source.</LI>
     * </UL>
     *
     * @param srcRegion The source region.
     * @param srcAxis   Axis direction for each dimension of the source region.
     * @param dstRegion The destination region.
     * @param dstAxis   Axis direction for each dimension of the destination region.
     * @throws MismatchedDimensionException if the envelope dimension doesn't
     *         matches the axis direction array length.
     * @throws IllegalArgumentException If {@code dstAxis} contains some axis
     *         not found in {@code srcAxis}, or if some colinear axis were found.
     */
    public GeneralMatrix(final Envelope srcRegion, final AxisDirection[] srcAxis,
                         final Envelope dstRegion, final AxisDirection[] dstAxis)
    {
        this(srcRegion, srcAxis, dstRegion, dstAxis, true);
    }

    /**
     * Implementation of constructors expecting envelope and/or axis directions.
     *
     * @param validRegions   {@code true} if source and destination regions must
     *        be taken in account. If {@code false}, then source and destination
     *        regions will be ignored and may be null.
     */
    private GeneralMatrix(final Envelope srcRegion, final AxisDirection[] srcAxis,
                          final Envelope dstRegion, final AxisDirection[] dstAxis,
                          final boolean validRegions)
    {
        super(dstAxis.length+1, srcAxis.length+1);
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
        for (int dstIndex=0; dstIndex<dstAxis.length; dstIndex++) {
            boolean hasFound = false;
            final AxisDirection dstAxe = dstAxis[dstIndex];
            final AxisDirection search = dstAxe.absolute();
            for (int srcIndex=0; srcIndex<srcAxis.length; srcIndex++) {
                final AxisDirection srcAxe = srcAxis[srcIndex];
                if (search.equals(srcAxe.absolute())) {
                    if (hasFound) {
                        // TODO: Use the localized version of 'getName' in GeoAPI 2.1
                        throw new IllegalArgumentException(Errors.format(ErrorKeys.COLINEAR_AXIS_$2,
                                                           srcAxe.name(), dstAxe.name()));
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
                        translate  = (normal) ? dstRegion.getMinimum(dstIndex)
                                              : dstRegion.getMaximum(dstIndex);
                        scale     *= dstRegion.getLength(dstIndex) /
                                     srcRegion.getLength(srcIndex);
                        translate -= srcRegion.getMinimum(srcIndex) * scale;
                    }
                    setElement(dstIndex, srcIndex,       scale);
                    setElement(dstIndex, srcAxis.length, translate);
                }
            }
            if (!hasFound) {
                // TODO: Use the localized version of 'getName' in GeoAPI 2.1
                throw new IllegalArgumentException(Errors.format(
                            ErrorKeys.NO_SOURCE_AXIS_$1, dstAxis[dstIndex].name()));
            }
        }
        setElement(dstAxis.length, srcAxis.length, 1);
        assert (srcAxis.length != dstAxis.length) || isAffine() : this;
    }

    /**
     * Convenience method for checking object dimension validity.
     * This method is usually invoked for argument checking.
     *
     * @param  name      The name of the argument to check.
     * @param  envelope  The envelope to check.
     * @param  dimension The expected dimension for the object.
     * @throws MismatchedDimensionException if the envelope doesn't have the expected dimension.
     */
    private static void ensureDimensionMatch(final String   name,
					     final Envelope envelope,
                                             final int      dimension)
        throws MismatchedDimensionException
    {
        final int dim = envelope.getDimension();
        if (dimension != dim) {
            throw new MismatchedDimensionException(Errors.format(
                        ErrorKeys.MISMATCHED_DIMENSION_$3, name, dim, dimension));
        }
    }

    /**
     * Retrieves the specifiable values in the transformation matrix into a
     * 2-dimensional array of double precision values. The values are stored
     * into the 2-dimensional array using the row index as the first subscript
     * and the column index as the second. Values are copied; changes to the
     * returned array will not change this matrix.
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
        for (int j=0; j<rows.length; j++) {
            final double[] row;
            rows[j] = row = new double[numCol];
            for (int i=0; i<row.length; i++) {
                row[i] = matrix.getElement(j, i);
            }
        }
        return rows;
    }

    /**
     * Retrieves the specifiable values in the transformation matrix into a
     * 2-dimensional array of double precision values. The values are stored
     * into the 2-dimensional array using the row index as the first subscript
     * and the column index as the second. Values are copied; changes to the
     * returned array will not change this matrix.
     *
     * @return The matrix elements.
     */
    public final double[][] getElements() {
        final int numCol = getNumCol();
        final double[][] rows = new double[getNumRow()][];
        for (int j=0; j<rows.length; j++) {
            getRow(j, rows[j]=new double[numCol]);
        }
        return rows;
    }

    /**
     * {@inheritDoc}
     */
    public final boolean isAffine() {
        int dimension  = getNumRow();
        if (dimension != getNumCol()) {
            return false;
        }
        dimension--;
        for (int i=0; i<=dimension; i++) {
            if (getElement(dimension, i) != (i==dimension ? 1 : 0)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Returns {@code true} if this matrix is an identity matrix.
     */
    public final boolean isIdentity() {
        final int numRow = getNumRow();
        final int numCol = getNumCol();
        if (numRow != numCol) {
            return false;
        }
        for (int j=0; j<numRow; j++) {
            for (int i=0; i<numCol; i++) {
                if (getElement(j,i) != (i==j ? 1 : 0)) {
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

    /**
     * Returns {@code true} if the matrix is an identity matrix using the provided tolerance.
     */
    static boolean isIdentity(final Matrix matrix, double tolerance) {
    	tolerance = Math.abs(tolerance);
        final int numRow = matrix.getNumRow();
        final int numCol = matrix.getNumCol();
        if (numRow != numCol) {
            return false;
        }
        for (int j=0; j<numRow; j++) {
            for (int i=0; i<numCol; i++) {
                double e = matrix.getElement(j,i);
                if (i == j) {
                    e--;
                }
                if (!(Math.abs(e) <= tolerance)) {  // Uses '!' in order to catch NaN values.
                    return false;
                }
            }
        }
        // Note: we can't assert matrix.isAffine().
        return true;
    }

    /**
     * {@inheritDoc}
     */
    public final void multiply(final Matrix matrix) {
        final GMatrix m;
        if (matrix instanceof GMatrix) {
            m = (GMatrix) matrix;
        } else {
            m = new GeneralMatrix(matrix);
        }
        mul(m);
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(final Matrix matrix, final double tolerance) {
        return epsilonEquals(this, matrix, tolerance);
    }

    /**
     * Compares the element values.
     */
    static boolean epsilonEquals(final Matrix m1, final Matrix m2, final double tolerance) {
        final int numRow = m1.getNumRow();
        if (numRow != m2.getNumRow()) {
            return false;
        }
        final int numCol = m1.getNumCol();
        if (numCol != m2.getNumCol()) {
            return false;
        }
        for (int j=0; j<numRow; j++) {
            for (int i=0; i<numCol; i++) {
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
     * Returns an affine transform for this matrix.
     * This is a convenience method for interoperability with Java2D.
     *
     * @return The affine transform for this matrix.
     * @throws IllegalStateException if this matrix is not 3&times;3,
     *         or if the last row is not {@code [0 0 1]}.
     */
    public final AffineTransform toAffineTransform2D() throws IllegalStateException {
        int check;
        if ((check=getNumRow())!=3 || (check=getNumCol())!=3) {
            throw new IllegalStateException(Errors.format(
                        ErrorKeys.NOT_TWO_DIMENSIONAL_$1, check-1));
        }
        if (isAffine()) {
            return new AffineTransform(getElement(0,0), getElement(1,0),
            getElement(0,1), getElement(1,1),
            getElement(0,2), getElement(1,2));
        }
        throw new IllegalStateException(Errors.format(ErrorKeys.NOT_AN_AFFINE_TRANSFORM));
    }

    /**
     * Loads data from the specified file until the first blank line or end of file.
     *
     * @param  file The file to read.
     * @return The matrix parsed from the file.
     * @throws IOException if an error occured while reading the file.
     *
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
     * @param  in The stream to read.
     * @param  locale The locale for the numbers to be parsed.
     * @return The matrix parsed from the stream.
     * @throws IOException if an error occured while reading the stream.
     *
     * @since 2.2
     */
    public static GeneralMatrix load(final BufferedReader in, final Locale locale)
            throws IOException
    {
        final LineFormat parser = new LineFormat(locale);
        double[] data = null;
        double[] row  = null;
        int   numRow  = 0;
        int   numData = 0;
        String line;
        while ((line=in.readLine()) != null) {
            if ((line=line.trim()).length() == 0) {
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
                data = XArray.resize(data, upper*2);
            }
            System.arraycopy(row, 0, data, numData, row.length);
            numData = upper;
            numRow++;
            assert numData % numRow == 0 : numData;
        }
        data = (data!=null) ? XArray.resize(data, numData) : new double[0];
        return new GeneralMatrix(numRow, numData/numRow, data);
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
        final int    numRow = matrix.getNumRow();
        final int    numCol = matrix.getNumCol();
        StringBuffer buffer = new StringBuffer();
        final int      columnWidth = 12;
        final String lineSeparator = System.getProperty("line.separator", "\n");
        final FieldPosition  dummy = new FieldPosition(0);
        final NumberFormat  format = NumberFormat.getNumberInstance();
        format.setGroupingUsed(false);
        format.setMinimumFractionDigits(6);
        format.setMaximumFractionDigits(6);
        for (int j=0; j<numRow; j++) {
            for (int i=0; i<numCol; i++) {
                final int position = buffer.length();
                buffer = format.format(matrix.getElement(j,i), buffer, dummy);
                final int spaces = Math.max(columnWidth - (buffer.length() - position), 1);
                buffer.insert(position, Utilities.spaces(spaces));
            }
            buffer.append(lineSeparator);
        }
        return buffer.toString();
    }

    /**
     * Returns a clone of this matrix.
     */
    @Override
    public GeneralMatrix clone() {
        return (GeneralMatrix) super.clone();
    }
}
