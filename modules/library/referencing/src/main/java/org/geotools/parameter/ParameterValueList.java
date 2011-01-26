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
 */
package org.geotools.parameter;

import java.io.Serializable;
import java.util.AbstractList;
import java.util.List;
import java.util.RandomAccess;

import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterCardinalityException;
import org.opengis.parameter.InvalidParameterNameException;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValue;

import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;


/**
 * The list to be returned by {@link Parameter#values}.
 * This class performs check on the parameter value to be added or removed.
 * This implementation supports {@link #add} and {@link #remove} operations.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
final class ParameterValueList extends AbstractList<GeneralParameterValue>
        implements RandomAccess, Serializable
{
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -7446077551686135264L;

    /**
     * The descriptor.
     */
    private final ParameterDescriptorGroup descriptor;

    /**
     * The parameter values for this list.
     */
    private final List<GeneralParameterValue> values;

    /**
     * Constructs a parameter list.
     *
     * @param descriptor The descriptor for this list.
     * @param values The parameter values for this list.
     */
    public ParameterValueList(final ParameterDescriptorGroup descriptor,
                              final List<GeneralParameterValue> values)
    {
        this.descriptor = descriptor;
        this.values     = values;
    }

    /*
     * CAUTION: Some methods are NOT forwarded to 'values', and this is on purpose!
     *          This include all modification methods (add, set, remove, etc.).
     *          We must rely on the default AbstractList implementation for them.
     */
    @Override public boolean                isEmpty    ()         {return values.isEmpty    ( );}
              public int                    size       ()         {return values.size       ( );}
              public GeneralParameterValue  get        (int i)    {return values.get        (i);}
    @Override public int                    indexOf    (Object o) {return values.indexOf    (o);}
    @Override public int                    lastIndexOf(Object o) {return values.lastIndexOf(o);}
    @Override public boolean                equals     (Object o) {return values.equals     (o);}
    @Override public int                    hashCode   ()         {return values.hashCode   ( );}
    @Override public String                 toString   ()         {return values.toString   ( );}

    /**
     * Adds a {@linkplain ParameterValue parameter value} or an other
     * {@linkplain ParameterGroup parameter group} to this group. If an existing parameter
     * is already included for the same {@linkplain ParameterDescriptor#getName identifier},
     * then there is a choice:
     * <UL>
     *   <LI>For <code>{@linkplain GeneralParameterDescriptor#getMaximumOccurs maximumOccurs}
     *       == 1</code>, the new parameter will replace the existing parameter.</LI>
     *   <LI>For <code>{@linkplain GeneralParameterDescriptor#getMaximumOccurs maximumOccurs}
     *       &gt; 1</code>, the new parameter will be added. If adding the new parameter will
     *       increase the number past what is allowable by {@code maximumOccurs}, then
     *       an {@link IllegalStateException} will be thrown.</LI>
     * </UL>
     *
     * @param  parameter New parameter to be added to this group.
     * @return {@code true} if this object changed as a result of this call.
     * @throws IllegalArgumentException if the specified parameter is not allowable by the
     *         groups descriptor.
     * @throws InvalidParameterCardinalityException if adding this parameter would result in
     *         more parameters than allowed by {@code maximumOccurs}.
     */
    @Override
    public boolean add(final GeneralParameterValue parameter) {
        modCount++;
        final GeneralParameterDescriptor type = parameter.getDescriptor();
        final List<GeneralParameterDescriptor> descriptors = descriptor.descriptors();
        final String name = type.getName().getCode();
        if (!descriptors.contains(type)) {
            /*
             * For a more accurate error message, check if the operation failed because
             * the parameter name was not found, or the parameter descriptor doesn't matches.
             */
            for (final GeneralParameterDescriptor descriptor : descriptors) {
                if (AbstractIdentifiedObject.nameMatches(descriptor, name)) {
                    /*
                     * Found a matching name. Consequently, the operation failed because
                     * the descriptor was illegal.
                     */
                    throw new IllegalArgumentException(Errors.format(
                              ErrorKeys.ILLEGAL_DESCRIPTOR_FOR_PARAMETER_$1, name));
                }
            }
            /*
             * Found no matching name. Consequently, the operation failed because the name
             * was invalid.
             */
            final Object value;
            if (parameter instanceof ParameterValue) {
                value = ((ParameterValue) parameter).getValue();
            } else {
                value = "(group)";
            }
            throw new InvalidParameterNameException(Errors.format(
                      ErrorKeys.ILLEGAL_ARGUMENT_$2, name, value), name);
        }
        final int max = type.getMaximumOccurs();
        if (max == 1) {
            /*
             * Optional or mandatory parameter - we will simply replace what is there.
             */
            for (int i=values.size(); --i>=0;) {
                final GeneralParameterValue oldValue = values.get(i);
                final GeneralParameterDescriptor oldDescriptor = oldValue.getDescriptor();
                if (type.equals(oldDescriptor)) {
                    assert AbstractIdentifiedObject.nameMatches(oldDescriptor, name) : parameter;
                    final boolean same = parameter.equals(oldValue);
                    values.set(i, parameter);
                    return !same;
                }
            }
            // Value will be added at the end of this method.
        } else {
            /*
             * Parameter added (usually a group). Check the cardinality.
             */
            int count = 0;
            for (final GeneralParameterValue value : values) {
                if (AbstractIdentifiedObject.nameMatches(value.getDescriptor(), name)) {
                    count++;
                }
            }
            if (count >= max) {
                throw new InvalidParameterCardinalityException(Errors.format(
                          ErrorKeys.TOO_MANY_OCCURENCES_$2, name, count), name);
            }
        }
        values.add(parameter);
        return true;
    }

    /**
     * Remove the value at the specified index.
     *
     * @param index The index of the value to remove.
     */
    @Override
    public GeneralParameterValue remove(final int index) {
        return remove(values.get(index).getDescriptor(), index);
    }

    /**
     * Remove the value at the specified index.
     *
     * @param type  The descriptor of the value to remove.
     * @param index The index of the value to remove.
     */
    private GeneralParameterValue remove(final GeneralParameterDescriptor type, final int index) {
        modCount++;
        int count = 0;
        final String name = type.getName().getCode();
        for (final GeneralParameterValue value : values) {
            if (AbstractIdentifiedObject.nameMatches(value.getDescriptor(), name)) {
                count++;
            }
        }
        final int min = type.getMinimumOccurs();
        if (count <= min) {
            final int max = type.getMaximumOccurs();
            throw new InvalidParameterCardinalityException(Errors.format(
                      ErrorKeys.ILLEGAL_OCCURS_FOR_PARAMETER_$4, name, count-1, min, max), name);
        }
        final GeneralParameterValue value = values.remove(index);
        assert value!=null && type.equals(value.getDescriptor()) : value;
        return value;
    }
}
