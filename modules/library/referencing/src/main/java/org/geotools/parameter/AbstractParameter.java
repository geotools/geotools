/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.parameter;

import java.io.Writer;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.Iterator;

import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.GeneralParameterDescriptor;

import org.geotools.util.Utilities;
import org.geotools.io.TableWriter;
import org.geotools.referencing.wkt.Formattable;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Abstract parameter value or group of parameter values.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @see AbstractParameterDescriptor
 */
public abstract class AbstractParameter extends Formattable
           implements GeneralParameterValue, Serializable
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 8458179223988766398L;

    /**
     * The abstract definition of this parameter or group of parameters.
     */
    final GeneralParameterDescriptor descriptor;

    /**
     * Constructs a parameter value from the specified descriptor.
     *
     * @param descriptor The abstract definition of this parameter or group of parameters.
     */
    protected AbstractParameter(final GeneralParameterDescriptor descriptor) {
        this.descriptor = descriptor;
        ensureNonNull("descriptor", descriptor);
    }

    /**
     * Returns the abstract definition of this parameter or group of parameters.
     */
    public GeneralParameterDescriptor getDescriptor() {
        return descriptor;
    }

    /**
     * Makes sure that an argument is non-null. This method was already defined in
     * {@link org.geotools.referencing.AbstractIdentifiedObject}, but is defined here again
     * in order to get a more appropriate stack trace, and for access by class which do not
     * inherit from {@link org.geotools.referencing.AbstractIdentifiedObject}.
     *
     * @param  name   Argument name.
     * @param  object User argument.
     * @throws IllegalArgumentException if {@code object} is null.
     */
    static void ensureNonNull(final String name, final Object object)
            throws IllegalArgumentException
    {
        if (object == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }

    /**
     * Makes sure an array element is non-null. This is
     * a convenience method for subclass constructors.
     *
     * @param  name  Argument name.
     * @param  array The array to look at.
     * @param  index Index of the element to check.
     * @throws IllegalArgumentException if {@code array[i]} is null.
     */
    static void ensureNonNull(final String name, final Object[] array, final int index)
        throws IllegalArgumentException
    {
        if (array[index] == null) {
            throw new IllegalArgumentException(Errors.format(
                      ErrorKeys.NULL_ARGUMENT_$1, name+'['+index+']'));
        }
    }

    /**
     * Verify that the specified value is of the specified class.
     *
     * @param  expectedClass the expected class.
     * @param  value The expected value, or {@code null}.
     * @throws IllegalArgumentException if {@code value} is non-null and has a non-assignable
     *         class.
     */
    static <T> void ensureValidClass(final Class<?> expectedClass, final Object value)
            throws IllegalArgumentException
    {
        if (value != null) {
            final Class<?> valueClass = value.getClass();
            if (!expectedClass.isAssignableFrom(valueClass)) {
                throw new IllegalArgumentException(Errors.format(
                        ErrorKeys.ILLEGAL_CLASS_$2, valueClass, expectedClass));
            }
        }
    }

    /**
     * Returns an exception initialized with a "Unitless parameter" error message for the
     * specified descriptor.
     */
    static IllegalStateException unitlessParameter(final GeneralParameterDescriptor descriptor) {
        return new IllegalStateException(Errors.format(ErrorKeys.UNITLESS_PARAMETER_$1,
                                                       getName(descriptor)));
    }

    /**
     * Convenience method returning the name of the specified descriptor. This method is used
     * mostly for output to be read by human, not for processing. Consequently, we may consider
     * to returns a localized name in a future version.
     */
    static String getName(final GeneralParameterDescriptor descriptor) {
        return descriptor.getName().getCode();
    }

    /**
     * Returns a copy of this parameter value or group.
     */
    @Override
    public AbstractParameter clone() {
        try {
            return (AbstractParameter) super.clone();
        } catch (CloneNotSupportedException exception) {
            // Should not happen, since we are cloneable
            throw new AssertionError(exception);
        }
    }

    /**
     * Compares the specified object with this parameter for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final Object object) {
        if (object!=null && object.getClass().equals(getClass())) {
            final AbstractParameter that = (AbstractParameter) object;
            return Utilities.equals(this.descriptor, that.descriptor);
        }
        return false;
    }

    /**
     * Returns a hash value for this parameter. This value doesn't need
     * to be the same in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        return descriptor.hashCode() ^ (int)serialVersionUID;
    }

    /**
     * Returns a string representation of this parameter. The default implementation
     * delegates the work to {@link #write}, which should be overridden by subclasses.
     */
    @Override
    public final String toString() {
        final TableWriter table = new TableWriter(null, 1);
        table.setMultiLinesCells(true);
        try {
            write(table);
        } catch (IOException exception) {
            // Should never happen, since we write to a StringWriter.
            throw new AssertionError(exception);
        }
        return table.toString();
    }

    /**
     * Write the content of this parameter to the specified table. This method make it easier
     * to align values properly than overriding the {@link #toString} method. The table's columns
     * are defined as below:
     * <ol>
     *   <li>The parameter name</li>
     *   <li>The separator</li>
     *   <li>The parameter value</li>
     * </ol>
     *
     * <P>The default implementation is suitable for most cases. However, subclasses are free to
     * override this method with the following idiom:</P>
     *
     * <blockquote><pre>
     * table.{@linkplain TableWriter#write(String) write}("<var>parameter name</var>");
     * table.{@linkplain TableWriter#nextColumn() nextColumn}()
     * table.{@linkplain TableWriter#write(String) write}('=');
     * table.{@linkplain TableWriter#nextColumn() nextColumn}()
     * table.{@linkplain TableWriter#write(String) write}("<var>parameter value</var>");
     * table.{@linkplain TableWriter#nextLine() nextLine}()
     * </pre></blockquote>
     *
     * @param  table The table where to format the parameter value.
     * @throws IOException if an error occurs during output operation.
     */
    protected void write(final TableWriter table) throws IOException {
        table.write(getName(descriptor));
        table.nextColumn();
        if (this instanceof ParameterValue) {
            /*
             * Provides a default implementation for parameter value. This implementation doesn't
             * need to be a Geotools's one. Putting a default implementation here avoid duplication
             * in all subclasses implementing the same interface.
             */
            table.write('=');
            table.nextColumn();
            append(table, ((ParameterValue) this).getValue());
        } else if (this instanceof ParameterValueGroup) {
            /*
             * Provides a default implementation for parameter value group, for the same reasons
             * then the previous block. Reminder: the above 'instanceof' check for interface, not
             * for subclass. This explain why we use it instead of method overriding.
             */
            table.write(':');
            table.nextColumn();
            TableWriter inner = null;
            for (final Iterator it=((ParameterValueGroup) this).values().iterator(); it.hasNext();) {
                final GeneralParameterValue value = (GeneralParameterValue) it.next();
                if (value instanceof AbstractParameter) {
                    if (inner == null) {
                        inner = new TableWriter(table, 1);
                    }
                    ((AbstractParameter) value).write(inner);
                } else {
                    // Unknow implementation. It will break the formatting. Too bad...
                    if (inner != null) {
                        inner.flush();
                        inner = null;
                    }
                    table.write(value.toString());
                    table.write(System.getProperty("line.separator", "\r"));
                }
            }
            if (inner != null) {
                inner.flush();
            }
        } else {
            /*
             * No know parameter value for this default implementation.
             */
        }
        table.nextLine();
    }

    /**
     * Append the specified value to a stream. If the value is an array, then
     * the array element are appended recursively (i.e. the array may contains
     * sub-array).
     */
    private static void append(final Writer buffer, final Object value) throws IOException {
        if (value == null) {
            buffer.write("null");
        } else if (value.getClass().isArray()) {
            buffer.write('{');
            final int length = Array.getLength(value);
            final int limit = Math.min(5, length);
            for (int i=0; i<limit; i++) {
                if (i != 0) {
                    buffer.write(", ");
                }
                append(buffer, Array.get(value, i));
            }
            if (length > limit) {
                buffer.write(", ...");
            }
            buffer.write('}');
        } else {
            final boolean isNumeric = (value instanceof Number);
            if (!isNumeric) {
                buffer.write('"');
            }
            buffer.write(value.toString());
            if (!isNumeric) {
                buffer.write('"');
            }
        }
    }

    /**
     * Format the inner part of this parameter as
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A>. This method doesn't need to be overridden, since the formatter
     * already know how to {@linkplain Formatter#append(GeneralParameterValue) format parameters}.
     */
    @Override
    protected final String formatWKT(final Formatter formatter) {
        return "PARAMETER";
    }
}
