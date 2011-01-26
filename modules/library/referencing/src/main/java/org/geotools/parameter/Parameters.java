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

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.LogRecord;
import javax.measure.unit.Unit;

import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.InvalidParameterTypeException;

import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.util.logging.Logging;
import org.geotools.resources.Classes;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Utility class for methods helping implementing, and working with the
 * parameter API from {@link org.opengis.parameter} package.
 * <p>
 * <h3>Design note</h3>
 * This class contains some methods working on a specific parameter in a group (e.g.
 * {@linkplain #search searching}, {@linkplain #ensureSet setting a value}, <cite>etc.</cite>).
 * Parameters are identified by their {@linkplain ParameterDescriptor#getName name} instead of
 * their full {@linkplain ParameterDescriptor descriptor} object, because:
 * <ul>
 *   <li>The parameter descriptor may not be always available. For example a user may looks for
 *       the {@code "semi_major"} axis length (because it is documented in OGC specification under
 *       that name) but doesn't know and doesn't care about who is providing the implementation. In
 *       such case, he doesn't have the parameter's descriptor. He only have the parameter's name,
 *       and creating a descriptor from that name (a descriptor independent of any implementation)
 *       is tedious.</li>.
 *   <li>Parameter descriptors are implementation-dependent. For example if a user searchs for
 *       the above-cited {@code "semi_major"} axis length using the {@linkplain
 *       org.geotools.referencing.operation.projection.MapProjection.AbstractProvider#SEMI_MAJOR
 *       Geotools's descriptor} for this parameter, we will fail to find this parameter in any
 *       alternative {@link ParameterValueGroup} implementations. This is against GeoAPI's
 *       inter-operability goal.</li>
 * </ul>
 * <p>
 * The above doesn't mean that parameter's descriptor should not be used. They are used for
 * inspecting meta-data about parameters, not as a key for searching parameters in a group.
 * Since each parameter's name should be unique in a given parameter group (because
 * {@linkplain ParameterDescriptor#getMaximumOccurs maximum occurs} is always 1 for single
 * parameter), the parameter name is a suffisient key.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett (Refractions Research)
 * @author Martin Desruisseaux
 */
public final class Parameters {
    /**
     * Small number for floating point comparaisons.
     */
    private static final double EPS = 1E-8;

    /**
     * An empty parameter group. This group contains no parameters.
     */
    public static ParameterDescriptorGroup EMPTY_GROUP =
            new DefaultParameterDescriptorGroup("empty", // TODO: localize
            new GeneralParameterDescriptor[0]);

    /**
     * Do not allows instantiation of this utility class.
     */
    private Parameters() {
    }

    /**
     * Casts the given parameter descriptor to the given type. An exception is thrown
     * immediately if the parameter does not have the expected value class. This
     * is a helper method for type safety when using Java 5 parameterized types.
     *
     * @param <T> The expected value class.
     * @param  descriptor The descriptor to cast.
     * @param  type The expected value class.
     * @return The descriptor casted to the given type.
     * @throws ClassCastException if the given descriptor doesn't have the expected value class.
     *
     * @since 2.5
     */
    @SuppressWarnings("unchecked")
    public static <T> ParameterDescriptor<T> cast(ParameterDescriptor<?> descriptor, Class<T> type)
            throws ClassCastException
    {
        if (descriptor != null) {
            final Class<?> actual = descriptor.getValueClass();
            // We require a strict equality - not type.isAssignableFrom(actual) - because in
            // the later case we could have (to be strict) to return a <? extends T> type.
            if (!type.equals(actual)) {
                throw new ClassCastException(Errors.format(ErrorKeys.BAD_PARAMETER_TYPE_$2,
                        descriptor.getName().getCode(), actual));
            }
        }
        return (ParameterDescriptor) descriptor;
    }

    /**
     * Casts the given parameter value to the given type. An exception is thrown
     * immediately if the parameter does not have the expected value class. This
     * is a helper method for type safety when using Java 5 parameterized types.
     *
     * @param <T> The expected value class.
     * @param  value The value to cast.
     * @param  type The expected value class.
     * @return The value casted to the given type.
     * @throws ClassCastException if the given value doesn't have the expected value class.
     *
     * @since 2.5
     */
    @SuppressWarnings("unchecked")
    public static <T> ParameterValue<T> cast(final ParameterValue<?> value, final Class<T> type)
            throws ClassCastException
    {
        if (value != null) {
            final ParameterDescriptor descriptor = value.getDescriptor();
            final Class<?> actual = descriptor.getValueClass();
            if (!type.equals(actual)) { // Same comment than cast(ParameterDescriptor)...
                throw new ClassCastException(Errors.format(ErrorKeys.BAD_PARAMETER_TYPE_$2,
                        descriptor.getName().getCode(), actual));
            }
        }
        return (ParameterValue) value;
    }

    /**
     * Checks a parameter value against its {@linkplain ParameterDescriptor parameter descriptor}.
     * This method takes care of handling checking arrays and collections against parameter
     * descriptor.
     * <p>
     * When the {@linkplain ParameterDescriptor#getValueClass value class} is an array (like
     * {@code double[].class}) or a {@linkplain Collection collection} (like {@code List.class}),
     * the descriptor
     * {@linkplain ParameterDescriptor#getMinimumValue minimum value},
     * {@linkplain ParameterDescriptor#getMaximumValue maximum value} and
     * {@linkplain ParameterDescriptor#getValidValues valid values}
     * will be used to check the elements.
     *
     * @param parameter The parameter to test.
     * @return true if parameter is valid.
     *
     * @see Parameter#ensureValidValue
     */
    public static boolean isValid(final ParameterValue<?> parameter) {
        final ParameterDescriptor<?> descriptor = parameter.getDescriptor();
        final Object value = parameter.getValue();
        if (value == null) {
            // Accepts null values only if explicitly authorized.
            final Set<?> validValues = descriptor.getValidValues();
            return validValues != null && validValues.contains(value);
        }
        final Class<?> type = Classes.primitiveToWrapper(value.getClass());
        final Class<?> expected = Classes.primitiveToWrapper(descriptor.getValueClass());
        if (expected.isAssignableFrom(type)) {
            return false; // value not of the correct type
        }
        if (type.isArray()) {
            // handle checking elements in an aray
            final int length = Array.getLength(value);
            for (int i=0; i<length; i++) {
                if (!isValidValue(Array.get(value, i), descriptor)) {
                    return false;
                }
            }
        } else if (value instanceof Collection) {
            // handle checking elements in a collection
            for (final Object element : (Collection) value) {
                if (!isValidValue(element, descriptor)) {
                    return false;
                }
            }
        } else {
            if (!isValidValue(value, descriptor)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Called on a single {@linkplain ParameterValue parameter value},
     * or on elements of a parameter value. This method ensures that
     * {@linkplain ParameterDescriptor#getMinimumValue minimum value},
     * {@linkplain ParameterDescriptor#getMaximumValue maximum value} and
     * {@linkplain ParameterDescriptor#getValidValues valid values}
     * all think the provided value is okay.
     *
     * @param  value The value to test.
     * @param  descriptor The descriptor for the value.
     * @return true if parameter is valid.
     *
     * @see Parameter#ensureValidValue
     */
    private static boolean isValidValue(final Object value, final ParameterDescriptor<?> descriptor) {
        final Set<?> validValues = descriptor.getValidValues();
        if (validValues != null && !validValues.contains(value)) {
            return false;
        }
        @SuppressWarnings("unchecked") // Type has been verified by the caller.
        final Comparable<Object> min = (Comparable) descriptor.getMinimumValue();
        if (min!=null && min.compareTo(value) > 0) {
            return false;
        }
        @SuppressWarnings("unchecked")
        final Comparable<Object> max = (Comparable) descriptor.getMaximumValue();
        if (max!=null && max.compareTo(value) < 0) {
            return false;
        }
        return true;
    }

    /**
     * Searchs all parameters with the specified name. The given {@code name} is
     * compared against parameter {@link GeneralParameterDescriptor#getName name} and
     * {@link GeneralParameterDescriptor#getAlias alias}. This method search recursively
     * in subgroups up to the specified depth:
     * <p>
     * <ul>
     *   <li>If {@code maxDepth} is equals to 0, then this method returns {@code param}
     *       if and only if it matches the specified name.</li>
     *   <li>If {@code maxDepth} is equals to 1 and {@code param} is an instance of
     *       {@link ParameterDescriptorGroup}, then this method checks all elements
     *       in this group but not in subgroups.</li>
     *   <li>...</li>
     *   <li>If {@code maxDepth} is a high number (e.g. 100), then this method checks all elements
     *       in all subgroups up to the specified depth, which is likely to be never reached. In
     *       this case, {@code maxDepth} can be seen as a safeguard against never ending loops, for
     *       example if parameters graph contains cyclic entries.</li>
     * </ul>
     *
     * @param  param The parameter to inspect.
     * @param  name  The name of the parameter to search for. See the class javadoc
     *               for a rational about the usage of name as a key instead of
     *               {@linkplain ParameterDescriptor descriptor}.
     * @param maxDepth The maximal depth while descending down the parameter tree.
     * @return The set (possibly empty) of parameters with the given name.
     */
    public static List<Object> search(final GeneralParameterValue param, final String name, int maxDepth) {
        final List<Object> list = new ArrayList<Object>();
        search(param, name, maxDepth, list);
        return list;
    }

    /**
     * Implementation of the search algorithm. The result is stored in the supplied set.
     */
    private static void search(final GeneralParameterValue param, final String name,
                               final int maxDepth, final Collection<Object> list)
    {
        if (maxDepth >= 0) {
            if (AbstractIdentifiedObject.nameMatches(param.getDescriptor(), name)) {
                list.add(param);
            }
            if ((maxDepth != 0) && (param instanceof ParameterValueGroup)) {
                for (final GeneralParameterValue value : ((ParameterValueGroup) param).values()) {
                    search(value, name, maxDepth-1, list);
                }
            }
        }
    }

    /**
     * Copies all parameter values from {@code source} to {@code target}. A typical usage of
     * this method is for transfering values from an arbitrary implementation to some specific
     * implementation (e.g. a parameter group implementation backed by a
     * {@link java.awt.image.renderable.ParameterBlock} for image processing operations).
     *
     * @param source The parameters to copy.
     * @param target Where to copy the source parameters.
     *
     * @since 2.2
     */
    public static void copy(final ParameterValueGroup source, final ParameterValueGroup target) {
        for (final GeneralParameterValue param : source.values()) {
            final String name = param.getDescriptor().getName().getCode();
            if (param instanceof ParameterValueGroup) {
                copy((ParameterValueGroup) param, target.addGroup(name));
            } else {
                target.parameter(name).setValue(((ParameterValue) param).getValue());
            }
        }
    }

    /**
     * Gets a flat view of
     * {@linkplain ParameterDescriptor#getName name}-{@linkplain ParameterValue#getValue value}
     * pairs. This method copies all parameter values into the supplied {@code destination} map.
     * Keys are parameter names as {@link String} objects, and values are parameter values as
     * arbitrary objects. All subgroups (if any) are extracted recursively.
     *
     * @param  parameters  The parameters to extract values from.
     * @param  destination The destination map, or {@code null} for a default one.
     * @return {@code destination}, or a new map if {@code destination} was null.
     */
    public static Map<String,Object> toNameValueMap(final GeneralParameterValue parameters,
                                                    Map<String,Object> destination)
    {
        if (destination == null) {
            destination = new LinkedHashMap<String,Object>();
        }
        if (parameters instanceof ParameterValue) {
            final ParameterValue param = (ParameterValue) parameters;
            final Object value = param.getValue();
            final Object old = destination.put(param.getDescriptor().getName().getCode(), value);
            if (old!=null && !old.equals(value)) {
                // TODO: This code will fails to detect if a null value was explicitly supplied
                //       previously. We assume that this case should be uncommon and not a big deal.
                throw new IllegalArgumentException("Inconsistent value"); // TODO: localize.
            }
        }
        if (parameters instanceof ParameterValueGroup) {
            final ParameterValueGroup group = (ParameterValueGroup) parameters;
            for (final GeneralParameterValue value : group.values()) {
                destination = toNameValueMap(value, destination);
            }
        }
        return destination;
    }

    /**
     * Ensures that the specified parameter is set. The {@code value} is set if and only if
     * no value were already set by the user for the given {@code name}.
     * <p>
     * The {@code force} argument said what to do if the named parameter is already set. If the
     * value matches, nothing is done in all case. If there is a mismatch and {@code force} is
     * {@code true}, then the parameter is overridden with the specified {@code value}. Otherwise,
     * the parameter is left unchanged but a warning is logged with the {@link Level#FINE FINE}
     * level.
     *
     * @param parameters The set of projection parameters.
     * @param name       The parameter name to set.
     * @param value      The value to set, or to expect if the parameter is already set.
     * @param unit       The value unit.
     * @param force      {@code true} for forcing the parameter to the specified {@code value}
     *                   is case of mismatch.
     * @return {@code true} if the were a mismatch, or {@code false} if the parameters can be
     *         used with no change.
     */
    public static boolean ensureSet(final ParameterValueGroup parameters,
                                    final String name, final double value, final Unit<?> unit,
                                    final boolean force)
    {
        final ParameterValue<?> parameter;
        try {
            parameter = parameters.parameter(name);
        } catch (ParameterNotFoundException ignore) {
            /*
             * Parameter not found. This exception should not occurs most of the time.
             * If it occurs, we will not try to set the parameter here, but the same
             * exception is likely to occurs at MathTransform creation time. The later
             * is the expected place for this exception, so we will let it happen there.
             */
            return false;
        }
        try {
            if (Math.abs(parameter.doubleValue(unit) / value - 1) <= EPS) {
                return false;
            }
        } catch (InvalidParameterTypeException exception) {
            /*
             * The parameter is not a floating point value. Don't try to set it. An exception is
             * likely to be thrown at MathTransform creation time, which is the expected place.
             */
            return false;
        } catch (IllegalStateException exception) {
            /*
             * No value were set for this parameter, and there is no default value.
             */
            parameter.setValue(value, unit);
            return true;
        }
        /*
         * A value was set, but is different from the expected value.
         */
        if (force) {
            parameter.setValue(value, unit);
        } else {
            // TODO: localize
            final LogRecord record = new LogRecord(Level.FINE, "Axis length mismatch.");
            record.setSourceClassName(Parameters.class.getName());
            record.setSourceMethodName("ensureSet");
            final Logger logger = Logging.getLogger(Parameters.class);
            record.setLoggerName(logger.getName());
            logger.log(record);
        }
        return true;
    }
}
