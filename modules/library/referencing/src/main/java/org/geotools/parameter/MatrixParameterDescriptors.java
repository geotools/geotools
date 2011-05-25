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
package org.geotools.parameter;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.measure.unit.Unit;

import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterNameException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.Matrix;

import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.operation.matrix.MatrixFactory;
import org.geotools.resources.UnmodifiableArrayList;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.Utilities;


/**
 * A parameter group for {@linkplain Matrix matrix} elements.  The amount of
 * {@linkplain ParameterValue parameter values} is extensible, i.e. it can grown or
 * shrink according the value of  <code>"num_row"</code> and <code>"num_col"</code>
 * parameters. The parameters format may vary according the information provided to
 * the constructor, but it is typically as below:
 * <blockquote><pre>
 * num_row
 * num_col
 * elt_0_0
 * elt_0_1
 * ...
 * elt_0_<var>&lt;num_col-1&gt;</var>
 * elt_1_0
 * elt_1_1
 * ...
 * elt_<var>&lt;num_row-1&gt;</var>_<var>&lt;num_col-1&gt;</var>
 * </pre></blockquote>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see MatrixParameters
 */
public class MatrixParameterDescriptors extends DefaultParameterDescriptorGroup {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7386537348359343836L;

    /**
     * The default matrix size for the
     * {@linkplain #MatrixParameterDescriptors(Map) one-argument constructor}.
     */
    public static final int DEFAULT_MATRIX_SIZE = 3;

    /**
     * The height and weight of the matrix of {@link #parameters} to cache. Descriptors
     * for row or column indices greater than or equals to this value will not be cached.
     */
    private static final int CACHE_SIZE = 8;

    /**
     * The cached descriptors for each elements in a matrix. Descriptors do not depends
     * on matrix element values. Consequently, the same descriptors can be reused for all
     * {@link MatrixParameters} instances.
     */
    @SuppressWarnings("unchecked")
    private final ParameterDescriptor<Double>[] parameters =
            new ParameterDescriptor[CACHE_SIZE * CACHE_SIZE];

    /**
     * The descriptor for the {@code "num_row"} parameter.
     */
    protected final ParameterDescriptor<Integer> numRow;

    /**
     * The descriptor for the {@code "num_col"} parameter.
     */
    protected final ParameterDescriptor<Integer> numCol;

    /**
     * The prefix to insert in front of parameter name for each matrix elements.
     */
    protected final String prefix;

    /**
     * The separator between the row and the column index in parameter names.
     */
    protected final char separator;

    /**
     * Constructs a parameter group with default name format matching
     * <cite><A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html">Well
     * Known Text</A></cite> usages.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     */
    public MatrixParameterDescriptors(final Map<String,?> properties) {
        /*
         * Note: the upper limit given in the operation parameters is arbitrary. A high
         *       value doesn't make much sense anyway  since matrix size for projective
         *       transform will usually not be much more than 5, and the storage scheme
         *       used in this implementation is inefficient  for large amount of matrix
         *       elements.
         */
        this(properties, new ParameterDescriptor[] {
        		DefaultParameterDescriptor.create("num_row", DEFAULT_MATRIX_SIZE, 2, 50),
        		DefaultParameterDescriptor.create("num_col", DEFAULT_MATRIX_SIZE, 2, 50)
        }, "elt_", '_');
    }

    /**
     * Constructs a parameter group. The properties map is given unchanged to the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     * The {@code parameters} array should contains parameters <strong>other</strong>
     * than matrix elements. The first parameter is assumed to be the number of rows, and
     * the second parameter the number of columns. All extra parameters are ignored.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param parameters The {@code "num_row"} and {@code "num_col"} parameters.
     * @param prefix     The prefix to insert in front of parameter name for each matrix elements.
     * @param separator  The separator between the row and the column index in parameter names.
     */
    public MatrixParameterDescriptors(final Map<String,?>      properties,
                                      ParameterDescriptor<?>[] parameters,
                                      final String             prefix,
                                      final char               separator)
    {
        super(properties, parameters);
        if (parameters.length < 2) {
            // TODO: provide a localized message
            throw new IllegalArgumentException();
        }
        numRow = Parameters.cast(parameters[0], Integer.class);
        numCol = Parameters.cast(parameters[1], Integer.class);
        ensureNonNull("prefix", prefix);
        this.prefix    = prefix;
        this.separator = separator;
    }

    /**
     * Verify that the specified index is included in the expected range of values.
     *
     * @param  name  The parameter name. To be used for formatting error message.
     * @param  index The indice to check.
     * @param  upper The upper range value, exclusive.
     * @throws IndexOutOfBoundsException if {@code index} is outside the expected range.
     */
    static void checkIndice(final String name, final int index, final int upper)
            throws IndexOutOfBoundsException
    {
        if (index<0 || index>=upper) {
            throw new IndexOutOfBoundsException(Errors.format(
                      ErrorKeys.ILLEGAL_ARGUMENT_$2, name, index));
        }
    }

    /**
     * Returns the parameter in this group for the specified name. The name can be a matrix element
     * if it uses the following syntax: <code>"elt_<var>row</var>_<var>col</var>"</code> where
     * {@code "elt_"} is the {@linkplain #prefix} for all matrix elements, and <var>row</var>
     * and <var>col</var> are row and column indices respectively. For example {@code "elt_2_1"}
     * is the element name for the value at line 2 and row 1. The row and column index are 0 based.
     *
     * @param  name The case insensitive name of the parameter to search for.
     * @return The parameter for the given name.
     * @throws ParameterNotFoundException if there is no parameter for the given name.
     */
    @Override
    @SuppressWarnings("unchecked")
    public final GeneralParameterDescriptor descriptor(final String name)
            throws ParameterNotFoundException
    {
        return descriptor(name, ((Number) numRow.getMaximumValue()).intValue(),
                                ((Number) numCol.getMaximumValue()).intValue());
    }

    /**
     * Implementation of the {@link #descriptor(String)} method.
     *
     * @param  name   The case insensitive name of the parameter to search for.
     * @param  numRow The maximum number of rows.
     * @param  numCol The maximum number of columns.
     * @return The parameter for the given name.
     * @throws ParameterNotFoundException if there is no parameter for the given name.
     */
    final GeneralParameterDescriptor descriptor(String name, final int numRow, final int numCol)
            throws ParameterNotFoundException
    {
        ensureNonNull("name", name);
        name = name.trim();
        RuntimeException cause = null;
        if (name.regionMatches(true, 0, prefix, 0, prefix.length())) {
            final int split = name.indexOf(separator, prefix.length());
            if (split >= 0) try {
                final int row = Integer.parseInt(name.substring(prefix.length(), split));
                final int col = Integer.parseInt(name.substring(split+1));
                return descriptor(row, col, numRow, numCol);
            } catch (NumberFormatException exception) {
                cause = exception;
            } catch (IndexOutOfBoundsException exception) {
                cause = exception;
            }
        }
        /*
         * The parameter name is not a matrix element name. Search in the super
         * class for other parameters, especially "num_row" and "num_col".
         */
        try {
            return super.descriptor(name);
        } catch (ParameterNotFoundException exception) {
            if (cause != null) try {
                exception.initCause(cause);
            } catch (IllegalStateException ignore) {
                // A cause has already be given to the exception. Forget the cause then.
            }
            throw exception;
        }
    }

    /**
     * Returns the parameter in this group for a matrix element at the specified
     * index. row and column indices are 0 based. Indices must be lower that the
     * {@linkplain ParameterDescriptor#getMaximumValue maximum values}
     * given to the {@link #numRow} and {@link #numCol} parameters.
     *
     * @param  row    The row indice.
     * @param  column The column indice
     * @return The parameter descriptor for the specified matrix element.
     * @throws IndexOutOfBoundsException if {@code row} or {@code column} is out of bounds.
     */
    @SuppressWarnings("unchecked")
    public final ParameterDescriptor<Double> descriptor(final int row, final int column)
            throws IndexOutOfBoundsException
    {
        return descriptor(row, column, ((Number) numRow.getMaximumValue()).intValue(),
                                       ((Number) numCol.getMaximumValue()).intValue());
    }

    /**
     * Implementation of the {@link #descriptor(int,int)} method.
     *
     * @param  row    The row indice.
     * @param  column The column indice
     * @param  numRow The maximum number of rows.
     * @param  numCol The maximum number of columns.
     * @return The parameter descriptor for the specified matrix element.
     * @throws IndexOutOfBoundsException if {@code row} or {@code column} is out of bounds.
     */
    final ParameterDescriptor<Double> descriptor(final int row,    final int column,
                                                 final int numRow, final int numCol)
            throws IndexOutOfBoundsException
    {
        checkIndice("row",    row,    numRow);
        checkIndice("column", column, numCol);
        int index = -1;
        ParameterDescriptor<Double> param;
        if (row<CACHE_SIZE && column<CACHE_SIZE) {
            index = row*CACHE_SIZE + column;
            param = parameters[index];
            if (param != null) {
                return param;
            }
        }
        /*
         * Parameter not found in the cache. Create a new one and cache it for future reuse.
         * Note that this cache is shared by all MatrixParameterDescriptors instance. There
         * is no need to synchronize since it is not a big deal if the same parameter is
         * constructed twice.
         */
        param = new DefaultParameterDescriptor<Double>(
                Collections.singletonMap(NAME_KEY, prefix + row + separator + column),
                Double.class, null, (row == column) ? 1.0 : 0.0,
                null, null, Unit.ONE, true);
        if (index >= 0) {
            parameters[index] = param;
        }
        return param;
    }

    /**
     * Returns the parameters in this group. The number or elements is inferred from the
     * {@linkplain ParameterDescriptor#getDefaultValue default values}
     * given to the {@link #numRow} and {@link #numCol} parameters.
     *
     * @return The matrix parameters, including all elements.
     */
    @Override
    public final List<GeneralParameterDescriptor> descriptors() {
        return descriptors(this.numRow.getDefaultValue().intValue(),
                           this.numCol.getDefaultValue().intValue());
    }

    /**
     * Implementation of the {@link #descriptors()} method.
     * Returns the parameters in this group for a matrix of the specified size.
     *
     * @param numRow The number of rows.
     * @param numCol The number of columns.
     * @return The matrix parameters, including all elements.
     */
    final List<GeneralParameterDescriptor> descriptors(final int numRow, final int numCol) {
        final GeneralParameterDescriptor[] parameters = new GeneralParameterDescriptor[numRow*numCol + 2];
        int k = 0;
        parameters[k++] = this.numRow;
        parameters[k++] = this.numCol;
        for (int j=0; j<numRow; j++) {
            for (int i=0; i<numCol; i++) {
                parameters[k++] = descriptor(j,i, numRow, numCol);
            }
        }
        assert k == parameters.length : k;
        return UnmodifiableArrayList.wrap(parameters);
    }

    /**
     * Creates a new instance of {@linkplain MatrixParameters parameter values} with
     * elements initialized to the 1 on the diagonal, and 0 everywere else. The returned
     * parameter group is extensible, i.e. the number of elements will depends upon the
     * value associated to the {@link #numRow} and {@link #numCol numCol} parameters.
     *
     * @return A new parameter initialized to the default value.
     */
    @Override
    public ParameterValueGroup createValue() {
        return new MatrixParameters(this);
    }

    /**
     * Constructs a matrix from a group of parameters.
     *
     * @param  parameters The group of parameters.
     * @return A matrix constructed from the specified group of parameters.
     * @throws InvalidParameterNameException if a parameter name was not recognized.
     */
    public Matrix getMatrix(final ParameterValueGroup parameters)
            throws InvalidParameterNameException
    {
        if (parameters instanceof MatrixParameters) {
            // More efficient implementation
            return ((MatrixParameters) parameters).getMatrix();
        }
        // Fallback on the general case (others implementations)
        final ParameterValue numRowParam = parameters.parameter(numRow.getName().toString());
        final ParameterValue numColParam = parameters.parameter(numCol.getName().toString());
        final int numRow = numRowParam.intValue();
        final int numCol = numColParam.intValue();
        final Matrix matrix = MatrixFactory.create(numRow, numCol);
        final List<GeneralParameterValue> params = parameters.values();
        if (params != null) {
            for (final GeneralParameterValue param : params) {
                if (param==numRowParam || param==numColParam) {
                    continue;
                }
                RuntimeException cause = null;
                final String name = param.getDescriptor().getName().toString();
                if (name.regionMatches(true, 0, prefix, 0, prefix.length())) {
                    final int split = name.indexOf(separator, prefix.length());
                    if (split >= 0) try {
                        final int row = Integer.parseInt(name.substring(prefix.length(), split));
                        final int col = Integer.parseInt(name.substring(split+1));
                        matrix.setElement(row, col, ((ParameterValue) param).doubleValue());
                        continue;
                    } catch (NumberFormatException exception) {
                        cause = exception;
                    } catch (IndexOutOfBoundsException exception) {
                        cause = exception;
                    }
                }
                final InvalidParameterNameException exception;
                exception = new InvalidParameterNameException(Errors.format(
                                ErrorKeys.UNEXPECTED_PARAMETER_$1, name), name);
                if (cause != null) {
                    exception.initCause(cause);
                }
                throw exception;
            }
        }
        return matrix;
    }

    /**
     * Compares the specified object with this parameter group for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (super.equals(object, compareMetadata)) {
            final MatrixParameterDescriptors that = (MatrixParameterDescriptors) object;
            return this.separator == that.separator && Utilities.equals(this.prefix, that.prefix);
        }
        return false;
    }

    /**
     * Returns a hash value for this parameter.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return super.hashCode() ^ prefix.hashCode() ^ 37*separator;
    }
}
