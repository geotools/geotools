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

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.operation.Matrix;
import org.opengis.util.InternationalString;
import org.opengis.util.GenericName;

import org.geotools.io.TableWriter;
import org.geotools.referencing.operation.matrix.MatrixFactory;
import org.geotools.resources.UnmodifiableArrayList;
import org.geotools.resources.XArray;
import org.geotools.util.Utilities;


/**
 * The values for a group of {@linkplain MatrixParameterDescriptors matrix parameters}. This value
 * group is extensible, i.e. the number of <code>"elt_<var>row</var>_<var>col</var>"</code>
 * parameters depends on the <code>"num_row"</code> and <code>"num_col"</code> parameter values.
 * Consequently, this {@linkplain ParameterGroup parameter value group} is also its own mutable
 * {@linkplain ParameterDescriptorGroup operation parameter group}.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see MatrixParameterDescriptors
 */
public class MatrixParameters extends ParameterGroup implements ParameterDescriptorGroup {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7747712999115044943L;

    /**
     * The parameter values. Will be constructed only when first requested.
     */
    private ParameterValue<Double>[][] matrixValues;

    /**
     * The value for the {@link MatrixParameterDescriptors#numRow} parameter.
     * Consider this field as final. It is not only for {@link #clone} implementation.
     */
    private ParameterValue<Integer> numRow;

    /**
     * The value for the {@link MatrixParameterDescriptors#numCol} parameter.
     * Consider this field as final. It is not only for {@link #clone} implementation.
     */
    private ParameterValue<Integer> numCol;

    /**
     * Constructs default values for the specified
     * {@linkplain MatrixParameterDescriptors matrix parameter descriptors}.
     *
     * @param descriptor The descriptor for this group of parameters.
     */
    public MatrixParameters(final MatrixParameterDescriptors descriptor) {
        super(descriptor);
        numRow = Parameters.cast((ParameterValue) parameter(0), Integer.class);
        numCol = Parameters.cast((ParameterValue) parameter(1), Integer.class);
    }

    /**
     * Returns a description of this parameter value group. Returns always {@code this},
     * since the description depends on <code>"num_row"</code> and <code>"num_col"</code>
     * parameter values.
     */
    @Override
    public ParameterDescriptorGroup getDescriptor() {
        return this;
    }

    /**
     * Forward the call to the {@linkplain MatrixParameterDescriptors matrix parameter descriptors}
     * specified at construction time.
     */
    public ReferenceIdentifier getName() {
        return descriptor.getName();
    }

    /**
     * Forward the call to the {@linkplain MatrixParameterDescriptors matrix parameter descriptors}
     * specified at construction time.
     */
    public Collection<GenericName> getAlias() {
        return descriptor.getAlias();
    }

    /**
     * Forward the call to the {@linkplain MatrixParameterDescriptors matrix parameter descriptors}
     * specified at construction time.
     */
    public Set<ReferenceIdentifier> getIdentifiers() {
        return descriptor.getIdentifiers();
    }

    /**
     * Forward the call to the {@linkplain MatrixParameterDescriptors matrix parameter descriptors}
     * specified at construction time.
     */
    public InternationalString getRemarks() {
        return descriptor.getRemarks();
    }

    /**
     * Forward the call to the {@linkplain MatrixParameterDescriptors matrix parameter descriptors}
     * specified at construction time.
     */
    public int getMinimumOccurs() {
        return descriptor.getMinimumOccurs();
    }

    /**
     * Forward the call to the {@linkplain MatrixParameterDescriptors matrix parameter descriptors}
     * specified at construction time.
     */
    public int getMaximumOccurs() {
        return descriptor.getMaximumOccurs();
    }

    /**
     * Returns the parameter in this group for the specified name. The name can be a matrix element
     * if it uses the following syntax: <code>"elt_<var>row</var>_<var>col</var>"</code> where
     * <code>"elt_"</code> is the {@linkplain MatrixParameterDescriptors#prefix prefix} for all
     * matrix elements, and <var>row</var> and <var>col</var> are row and column indices
     * respectively. For example <code>"elt_2_1"</code> is the element name for the value at line 2
     * and row 1. The row and column index are 0 based.
     *
     * @param  name The case insensitive name of the parameter to search for.
     * @return The parameter for the given name.
     * @throws ParameterNotFoundException if there is no parameter for the given name.
     */
    public GeneralParameterDescriptor descriptor(final String name)
            throws ParameterNotFoundException
    {
        return ((MatrixParameterDescriptors) descriptor).descriptor(name,
                numRow.intValue(), numCol.intValue());
    }

    /**
     * Returns the value in this group for the specified name. The name can be a matrix element
     * if it uses the following syntax: <code>"elt_<var>row</var>_<var>col</var>"</code> where
     * <code>"elt_"</code> is the {@linkplain MatrixParameterDescriptors#prefix prefix} for all
     * matrix elements, and <var>row</var> and <var>col</var> are row and column indices
     * respectively. For example <code>"elt_2_1"</code> is the element name for the value at line
     * 2 and row 1. The row and column index are 0 based.
     *
     * @param  name The case insensitive name of the parameter to search for.
     * @return The parameter value for the given name.
     * @throws ParameterNotFoundException if there is no parameter for the given name.
     */
    @Override
    public ParameterValue<?> parameter(String name) throws ParameterNotFoundException {
        ensureNonNull("name", name);
        name = name.trim();
        final MatrixParameterDescriptors descriptor = ((MatrixParameterDescriptors) this.descriptor);
        final String prefix = descriptor.prefix;
        RuntimeException cause = null;
        if (name.regionMatches(true, 0, prefix, 0, prefix.length())) {
            final int split = name.indexOf(descriptor.separator, prefix.length());
            if (split >= 0) try {
                final int row = Integer.parseInt(name.substring(prefix.length(), split));
                final int col = Integer.parseInt(name.substring(split + 1));
                return parameter(row, col);
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
            return super.parameter(name);
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
     * Returns the value in this group for a matrix element at the specified index.
     * Row and column index are 0 based.
     *
     * @param  row    The row indice.
     * @param  column The column indice
     * @return The parameter value for the specified matrix element (never {@code null}).
     * @throws IndexOutOfBoundsException if {@code row} or {@code column} is out of bounds.
     */
    public final ParameterValue<Double> parameter(final int row, final int column)
            throws IndexOutOfBoundsException
    {
        return parameter(row, column, numRow.intValue(), numCol.intValue());
    }

    /**
     * Implementation of {@link #parameter(int,int)}.
     *
     * @param  row    The row indice.
     * @param  column The column indice
     * @param  numRow The maximum number of rows.
     * @param  numCol The maximum number of columns.
     * @return The parameter value for the specified matrix element.
     * @throws IndexOutOfBoundsException if {@code row} or {@code column} is out of bounds.
     */
    @SuppressWarnings("unchecked") // Because of array creation
    private ParameterValue<Double> parameter(final int row,    final int column,
                                             final int numRow, final int numCol)
            throws IndexOutOfBoundsException
    {
        MatrixParameterDescriptors.checkIndice("row",    row,    numRow);
        MatrixParameterDescriptors.checkIndice("column", column, numCol);
        if (matrixValues == null) {
            matrixValues = new ParameterValue[numRow][];
        }
        if (row >= matrixValues.length) {
            matrixValues = XArray.resize(matrixValues, numRow);
        }
        ParameterValue<Double>[] rowValues = matrixValues[row];
        if (rowValues == null) {
            matrixValues[row] = rowValues = new ParameterValue[numCol];
        }
        if (column >= rowValues.length) {
            matrixValues[row] = rowValues = XArray.resize(rowValues, numCol);
        }
        ParameterValue<Double> param = rowValues[column];
        if (param == null) {
            rowValues[column] = param = new FloatParameter(
                ((MatrixParameterDescriptors) descriptor).descriptor(row, column, numRow, numCol));
        }
        return param;
    }

    /**
     * Returns the parameters descriptors in this group. The amount of parameters depends
     * on the value of <code>"num_row"</code> and <code>"num_col"</code> parameters.
     */
    public List<GeneralParameterDescriptor> descriptors() {
        return ((MatrixParameterDescriptors) descriptor).descriptors(
                numRow.intValue(), numCol.intValue());
    }

    /**
     * Returns the parameters values in this group. The amount of parameters depends
     * on the value of <code>"num_row"</code> and <code>"num_col"</code> parameters.
     * The parameter array will contains only matrix elements which have been requested at
     * least once by one of {@code parameter(...)} methods. Never requested elements
     * are left to their default value and omitted from the returned array.
     */
    @Override
    public List<GeneralParameterValue> values() {
        final int numRow = this.numRow.intValue();
        final int numCol = this.numCol.intValue();
        final ParameterValue[] parameters = new ParameterValue[numRow*numCol + 2];
        int k = 0;
        parameters[k++] = this.numRow;
        parameters[k++] = this.numCol;
        if (matrixValues != null) {
            final int maxRow = Math.min(numRow, matrixValues.length);
            for (int j=0; j<maxRow; j++) {
                final ParameterValue[] rowValues = matrixValues[j];
                if (rowValues != null) {
                    final int maxCol = Math.min(numCol, rowValues.length);
                    for (int i=0; i<maxCol; i++) {
                        final ParameterValue value = rowValues[i];
                        if (value != null) {
                            parameters[k++] = value;
                        }
                    }
                }
            }
        }
        return UnmodifiableArrayList.wrap((GeneralParameterValue[]) XArray.resize(parameters, k));
    }

    /**
     * Forwards the call to the {@linkplain MatrixParameterDescriptors matrix parameter descriptors}
     * specified at construction time.
     */
    public ParameterValueGroup createValue() {
        return (ParameterValueGroup) descriptor.createValue();
    }

    /**
     * Creates a matrix from this group of parameters.
     *
     * @return A matrix created from this group of parameters.
     */
    public Matrix getMatrix() {
        final int numRow = this.numRow.intValue();
        final int numCol = this.numCol.intValue();
        final Matrix matrix = MatrixFactory.create(numRow, numCol);
        if (matrixValues != null) {
            for (int j=0; j<numRow; j++) {
                final ParameterValue[] row = matrixValues[j];
                if (row != null) {
                    for (int i=0; i<numCol; i++) {
                        final ParameterValue element = row[i];
                        if (element != null) {
                            matrix.setElement(j, i, element.doubleValue());
                        }
                    }
                }
            }
        }
        return matrix;
    }

    /**
     * Sets all parameter values to the element value in the specified matrix.
     * After this method call, {@link #values} will returns only the elements
     * different from the default value.
     *
     * @param matrix The matrix to copy in this group of parameters.
     */
    @SuppressWarnings("unchecked") // Because of array creation
    public void setMatrix(final Matrix matrix) {
        final MatrixParameterDescriptors matrixDescriptor =
            ((MatrixParameterDescriptors) this.descriptor);
        final int numRow = matrix.getNumRow();
        final int numCol = matrix.getNumCol();
        this.numRow.setValue(numRow);
        this.numCol.setValue(numCol);
        for (int row=0; row<numRow; row++) {
            for (int col=0; col<numCol; col++) {
                final double element = matrix.getElement(row,col);
                ParameterDescriptor<Double> descriptor = matrixDescriptor.descriptor(row, col);
                final double value = descriptor.getDefaultValue();
                if (Double.doubleToLongBits(element) == Double.doubleToLongBits(value)) {
                    /*
                     * Value matches the default value, so there is no need to keep it.
                     * Remove entry to keep things sparse.
                     */
                    if (matrixValues != null  &&  matrixValues[row] != null) {
                        matrixValues[row][col] = null;
                    }
                    continue;
                }
                if (matrixValues == null) {
                    matrixValues = new ParameterValue[numRow][];
                }
                if (matrixValues[row] == null ){
                    matrixValues[row] = new ParameterValue[numCol];
                }
                matrixValues[row][col] = new FloatParameter(descriptor, element);
            }
        }
    }

    /**
     * Compare this object with the specified one for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            return true; // Slight optimization.
        }
        if (super.equals(object)) {
            final MatrixParameters that = (MatrixParameters) object;
            final int numRow = this.numRow.intValue();
            final int numCol = this.numCol.intValue();
            for (int j=0; j<numRow; j++) {
                for (int i=0; i<numCol; i++) {
                    if (!Utilities.equals(this.parameter(j,i, numRow, numCol),
                                          that.parameter(j,i, numRow, numCol)))
                    {
                        return false;
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Returns a clone of this parameter group.
     */
    @Override
    @SuppressWarnings("unchecked")
    public MatrixParameters clone() {
        final MatrixParameters copy = (MatrixParameters) super.clone();
        if (copy.matrixValues != null) {
            copy.numRow       = (ParameterValue) copy.parameter(0);
            copy.numCol       = (ParameterValue) copy.parameter(1);
            copy.matrixValues =                  copy.matrixValues.clone();
            for (int j=0; j<copy.matrixValues.length; j++) {
                ParameterValue[] array = copy.matrixValues[j];
                if (array != null) {
                    copy.matrixValues[j] = array = array.clone();
                    for (int i=0; i<array.length; i++) {
                        if (array[i] != null) {
                            array[i] = array[i].clone();
                        }
                    }
                }
            }
        }
        return copy;
    }

    /**
     * Writes the content of this parameter to the specified table.
     *
     * @param  table The table where to format the parameter value.
     * @throws IOException if an error occurs during output operation.
     */
    @Override
    protected void write(final TableWriter table) throws IOException {
        table.write(getName(descriptor));
        table.nextColumn();
        table.write('=');
        table.nextColumn();
        table.write(getMatrix().toString());
        table.nextLine();
    }
}
