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
package org.geotools.referencing.operation;

import java.util.HashMap;
import java.util.Map;
import javax.measure.unit.Unit;

import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.InvalidParameterNameException;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValue;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationMethod;
import org.opengis.referencing.operation.Operation;
import org.opengis.referencing.operation.Projection;
import org.opengis.util.GenericName;

import org.geotools.parameter.DefaultParameterDescriptor;
import org.geotools.parameter.DefaultParameterDescriptorGroup;
import org.geotools.parameter.Parameters;
import org.geotools.resources.XArray;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.referencing.operation.transform.MathTransformProxy;


/**
 * An {@linkplain DefaultOperationMethod operation method} capable to creates a
 * {@linkplain MathTransform math transform} from set of
 * {@linkplain GeneralParameterValue parameter values}.
 * Implementations of this class should be listed in the following file:
 *
 * <blockquote>
 * <P>{@code META-INF/services/org.geotools.referencing.operation.MathTransformProvider}</P>
 * </blockquote>
 * <P>
 * The {@linkplain DefaultMathTransformFactory math transform factory} will parse this file in order
 * to gets all available providers on a system. If this file is bundle in many JAR files, the
 * {@linkplain DefaultCoordinateOperationFactory math transform factory} will read all of them.
 *
 * @since 2.0
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public abstract class MathTransformProvider extends DefaultOperationMethod {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 7530475536803158473L;

    /**
     * Constructs a math transform provider from a set of parameters. The provider
     * {@linkplain #getIdentifiers identifiers} will be the same than the parameter
     * ones.
     *
     * @param sourceDimensions Number of dimensions in the source CRS of this operation method.
     * @param targetDimensions Number of dimensions in the target CRS of this operation method.
     * @param parameters The set of parameters (never {@code null}).
     */
    public MathTransformProvider(final int sourceDimensions,
                                 final int targetDimensions,
                                 final ParameterDescriptorGroup parameters)
    {
        this(toMap(parameters), sourceDimensions, targetDimensions, parameters);
    }

    /**
     * Constructs a math transform provider from a set of properties.
     * The properties map is given unchanged to the
     * {@linkplain DefaultOperationMethod#DefaultOperationMethod(Map,int,int,ParameterDescriptorGroup)
     * super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param sourceDimensions Number of dimensions in the source CRS of this operation method.
     * @param targetDimensions Number of dimensions in the target CRS of this operation method.
     * @param parameters The set of parameters (never {@code null}).
     */
    public MathTransformProvider(final Map<String,?> properties,
                                 final int sourceDimensions,
                                 final int targetDimensions,
                                 final ParameterDescriptorGroup parameters)
    {
        super(properties, sourceDimensions, targetDimensions, parameters);
    }

    /**
     * Work around for RFE #4093999 in Sun's bug database
     * ("Relax constraint on placement of this()/super() call in constructors").
     */
    private static Map<String,Object> toMap(final IdentifiedObject parameters) {
        ensureNonNull("parameters", parameters);
        final Map<String,Object> properties = new HashMap<String,Object>(4);
        properties.put(NAME_KEY,        parameters.getName());
        properties.put(IDENTIFIERS_KEY, parameters.getIdentifiers().toArray(EMPTY_IDENTIFIER_ARRAY));
        properties.put(ALIAS_KEY,       parameters.getAlias()      .toArray(EMPTY_ALIAS_ARRAY));
        return properties;
    }

    /**
     * Returns the operation type. It may be
     * <code>{@linkplain org.opengis.referencing.operation.Operation}.class</code>,
     * <code>{@linkplain org.opengis.referencing.operation.Conversion}.class</code>,
     * <code>{@linkplain org.opengis.referencing.operation.Projection}.class</code>,
     * <cite>etc</cite>.
     * <p>
     * The default implementation returns {@code Operation.class}.
     * Subclass should overrides this methods and returns the appropriate
     * OpenGIS interface type (<strong>not</strong> the implementation type).
     *
     * @return The GeoAPI interface implemented by this operation.
     */
    @Override
    public Class<? extends Operation> getOperationType() {
        return Operation.class;
    }

    /**
     * Creates a math transform from the specified group of parameter values.
     * Subclasses can implements this method as in the example below:
     *
     * <blockquote><pre>
     * double semiMajor = values.parameter("semi_major").doubleValue(SI.METER);
     * double semiMinor = values.parameter("semi_minor").doubleValue(SI.METER);
     * // etc...
     * return new MyTransform(semiMajor, semiMinor, ...);
     * </pre></blockquote>
     *
     * @param  values The group of parameter values.
     * @return The created math transform.
     * @throws InvalidParameterNameException if the values contains an unknow parameter.
     * @throws ParameterNotFoundException if a required parameter was not found.
     * @throws InvalidParameterValueException if a parameter has an invalid value.
     * @throws FactoryException if the math transform can't be created for some other reason
     *         (for example a required file was not found).
     *
     * @see MathTransformProvider.Delegate
     */
    protected abstract MathTransform createMathTransform(ParameterValueGroup values)
            throws InvalidParameterNameException,
                   ParameterNotFoundException,
                   InvalidParameterValueException,
                   FactoryException;

    /**
     * Constructs a parameter descriptor from a set of alias. The parameter is
     * identified by codes provided by one or more authorities. Common authorities are
     * {@link Citations#OGC OGC} and {@link Citations#EPSG EPSG} for example.
     *
     * <P>The first entry in the {@code identifiers} array is both the
     * {@linkplain ParameterDescriptor#getName main name} and the
     * {@linkplain ParameterDescriptor#getIdentifiers identifiers}.
     * All others are {@linkplain ParameterDescriptor#getAlias aliases}.</P>
     *
     * @param identifiers  The parameter identifiers. Most contains at least one entry.
     * @param defaultValue The default value for the parameter, or {@link Double#NaN} if none.
     * @param minimum The minimum parameter value, or {@link Double#NEGATIVE_INFINITY} if none.
     * @param maximum The maximum parameter value, or {@link Double#POSITIVE_INFINITY} if none.
     * @param unit    The unit for default, minimum and maximum values.
     * @return The descriptor for the given identifiers.
     */
    protected static ParameterDescriptor<Double> createDescriptor(
            final ReferenceIdentifier[] identifiers, final double defaultValue,
            final double minimum, final double maximum, final Unit<?> unit)
    {
        return DefaultParameterDescriptor.create(toMap(identifiers), defaultValue,minimum, maximum, unit, true);
    }

    /**
     * Constructs an optional parameter descriptor from a set of alias.
     * The parameter is identified as with {@link #createDescriptor}.
     *
     * @param identifiers The parameter identifiers. Most contains at least one entry.
     * @param minimum The minimum parameter value, or {@link Double#NEGATIVE_INFINITY} if none.
     * @param maximum The maximum parameter value, or {@link Double#POSITIVE_INFINITY} if none.
     * @param unit    The unit for default, minimum and maximum values.
     * @return The descriptor for the given identifiers.
     */
    protected static ParameterDescriptor<Double> createOptionalDescriptor(
            final ReferenceIdentifier[] identifiers,
            final double minimum, final double maximum, final Unit<?> unit)
    {
        return DefaultParameterDescriptor.create(toMap(identifiers), Double.NaN,
                                              minimum, maximum, unit, false);
    }

    /**
     * Constructs a parameter group from a set of alias. The parameter group is
     * identified by codes provided by one or more authorities. Common authorities are
     * {@link Citations#OGC OGC} and {@link Citations#EPSG EPSG} for example.
     * <P>
     * Special rules:
     * <ul>
     *   <li>The first entry in the {@code identifiers} array is the
     *       {@linkplain ParameterDescriptorGroup#getName primary name}.</li>
     *   <li>If a an entry do not implements the {@link GenericName} interface, it is
     *       an {@linkplain ParameterDescriptorGroup#getIdentifiers identifiers}.</li>
     *   <li>All others are {@linkplain ParameterDescriptorGroup#getAlias aliases}.</li>
     * </ul>
     *
     * @param identifiers  The operation identifiers. Most contains at least one entry.
     * @param parameters   The set of parameters, or {@code null} or an empty array if none.
     * @return The descriptor for the given identifiers.
     */
    protected static ParameterDescriptorGroup createDescriptorGroup(
            final ReferenceIdentifier[] identifiers, final GeneralParameterDescriptor[] parameters)
    {
        return new DefaultParameterDescriptorGroup(toMap(identifiers), parameters);
    }

    /**
     * Put the identifiers into a properties map suitable for {@link IdentifiedObject}
     * constructor.
     */
    private static Map<String,Object> toMap(final ReferenceIdentifier[] identifiers) {
        ensureNonNull("identifiers", identifiers);
        if (identifiers.length == 0) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.EMPTY_ARRAY));
        }
        int idCount    = 0;
        int aliasCount = 0;
        ReferenceIdentifier[] id = new ReferenceIdentifier[identifiers.length];
        GenericName[]      alias = new GenericName        [identifiers.length];
        for (int i=0; i<identifiers.length; i++) {
            final ReferenceIdentifier candidate = identifiers[i];
            if (candidate instanceof GenericName) {
                alias[aliasCount++] = (GenericName) candidate;
            } else {
                id[idCount++] = candidate;
            }
        }
        id    = XArray.resize(id,       idCount);
        alias = XArray.resize(alias, aliasCount);
        final Map<String,Object> properties = new HashMap<String,Object>(4, 0.8f);
        properties.put(NAME_KEY,        identifiers[0]);
        properties.put(IDENTIFIERS_KEY, id);
        properties.put(ALIAS_KEY,       alias);
        return properties;
    }

    /**
     * Ensures that the given set of parameters contains only valid values.
     * This method compares all parameter names against the names declared in the
     * {@linkplain #getParameters operation method parameter descriptor}. If an unknow
     * parameter name is found, then an {@link InvalidParameterNameException} is thrown.
     * This method also ensures that all values are assignable to the
     * {@linkplain ParameterDescriptor#getValueClass expected class}, are between the
     * {@linkplain ParameterDescriptor#getMinimumValue minimum} and
     * {@linkplain ParameterDescriptor#getMaximumValue maximum} values and are one of the
     * {@linkplain ParameterDescriptor#getValidValues set of valid values}.
     * If the value fails any of those tests, then an
     * {@link InvalidParameterValueException} is thrown.
     *
     * @param  values The parameters values to check.
     * @return The parameter values to use for {@linkplain MathTransform math transform}
     *         construction. May be different than the supplied {@code values}
     *         argument if some missing values needed to be filled with default values.
     * @throws InvalidParameterNameException if a parameter name is unknow.
     * @throws InvalidParameterValueException if a parameter has an invalid value.
     */
    protected ParameterValueGroup ensureValidValues(final ParameterValueGroup values)
            throws InvalidParameterNameException, InvalidParameterValueException
    {
        final ParameterDescriptorGroup parameters = getParameters();
        final GeneralParameterDescriptor descriptor = values.getDescriptor();
        if (parameters.equals(descriptor)) {
            /*
             * Since the "official" parameter descriptor was used, the descriptor should
             * have already enforced argument validity. Consequently, there is no need to
             * performs the check and we will avoid it as a performance enhancement.
             */
            return values;
        }
        /*
         * Copy the all values from the user-supplied group to the provider-supplied group.
         * The provider group should performs all needed checks. Furthermore, it is suppliers
         * responsability to know about alias (e.g. OGC, EPSG, ESRI), while the user will
         * probably use the name from only one authority. With a copy, we gives a chances to
         * the provider-supplied parameters to uses its alias for understanding the user
         * parameter names.
         */
        final ParameterValueGroup copy = parameters.createValue();
        copy(values, copy);
        return copy;
    }

    /**
     * Implementation of {@code ensureValidValues}, to be invoked recursively
     * if the specified values contains sub-groups of values. This method copy all
     * values from the user-supplied parameter values into the provider-supplied
     * one. The provider one should understand alias, and performs name conversion
     * as well as argument checking on the fly.
     *
     * @param  values The parameters values to copy.
     * @param  copy   The parameters values where to put the copy.
     * @throws InvalidParameterNameException if a parameter name is unknow.
     * @throws InvalidParameterValueException if a parameter has an invalid value.
     */
    private static void copy(final ParameterValueGroup values,
                             final ParameterValueGroup copy)
            throws InvalidParameterNameException, InvalidParameterValueException
    {
        for (final GeneralParameterValue value : values.values()) {
            final String name = value.getDescriptor().getName().getCode();
            if (value instanceof ParameterValueGroup) {
                /*
                 * Contains sub-group - invokes 'copy' recursively.
                 */
                final GeneralParameterDescriptor descriptor;
                descriptor = copy.getDescriptor().descriptor(name);
                if (descriptor instanceof ParameterDescriptorGroup) {
                    final ParameterValueGroup groups = (ParameterValueGroup) descriptor.createValue();
                    copy((ParameterValueGroup) value, groups);
                    values.groups(name).add(groups);
                    continue;
                } else {
                    throw new InvalidParameterNameException(Errors.format(
                              ErrorKeys.UNEXPECTED_PARAMETER_$1, name), name);
                }
            }
            /*
             * Single parameter - copy the value, with special care for value with units.
             */
            final ParameterValue<?> source = (ParameterValue) value;
            final ParameterValue<?> target;
            try {
                target = copy.parameter(name);
            } catch (ParameterNotFoundException cause) {
                final InvalidParameterNameException exception =
                      new InvalidParameterNameException(Errors.format(
                          ErrorKeys.UNEXPECTED_PARAMETER_$1, name), name);
                exception.initCause(cause);
                throw exception;
            }
            final Object  v    = source.getValue();
            final Unit<?> unit = source.getUnit();
            if (unit == null) {
                target.setValue(v);
            } else if (v instanceof Number) {
                target.setValue(((Number) v).doubleValue(), unit);
            } else if (v instanceof double[]) {
                target.setValue((double[]) v, unit);
            } else {
                throw new InvalidParameterValueException(Errors.format(
                          ErrorKeys.ILLEGAL_ARGUMENT_$2, name, v), name, v);
            }
        }
    }

    /**
     * Returns the parameter value for the specified operation parameter.
     * This convenience method is used by subclasses for initializing
     * {@linkplain MathTransform math transform} from a set of parameters.
     *
     * @param  param The parameter to look for.
     * @param  group The parameter value group to search into.
     * @return The requested parameter value.
     * @throws ParameterNotFoundException if the parameter is not found.
     */
    protected static <T> ParameterValue<T> getParameter(final ParameterDescriptor<T> param,
                                                      final ParameterValueGroup    group)
            throws ParameterNotFoundException
    {
        /*
         * Search for an identifier matching the group's authority, if any.
         * This is needed if the parameter values group was created from an
         * EPSG database for example: we need to use the EPSG names instead
         * of the OGC ones.
         */
        String name = getName(param, group.getDescriptor().getName().getAuthority());
        if (name == null) {
            name = param.getName().getCode();
        }
        if (param.getMinimumOccurs() != 0) {
            return Parameters.cast(group.parameter(name), param.getValueClass());
        }
        /*
         * The parameter is optional. We don't want to invokes 'parameter(name)', because we don't
         * want to create a new parameter is the user didn't supplied one. Search the parameter
         * ourself (so we don't create any), and returns null if we don't find any.
         *
         * TODO: A simplier solution would be to add a 'isDefined' method in GeoAPI,
         *       or something similar.
         */
        final GeneralParameterDescriptor search;
        search = group.getDescriptor().descriptor(name);
        if (search instanceof ParameterDescriptor) {
            for (final GeneralParameterValue candidate : group.values()) {
                if (search.equals(candidate.getDescriptor())) {
                    return Parameters.cast((ParameterValue) candidate, param.getValueClass());
                }
            }
        }
        return null;
    }

    /**
     * Returns the parameter value for the specified operation parameter.
     * This convenience method is used by subclasses for initializing
     * {@linkplain MathTransform math transform} from a set of parameters.
     *
     * @param  <T> The type of parameter value.
     * @param  param The parameter to look for.
     * @param  group The parameter value group to search into.
     * @return The requested parameter value, or {@code null} if {@code param} is
     *         {@linkplain #createOptionalDescriptor optional} and the user didn't
     *         provided any value.
     * @throws ParameterNotFoundException if the parameter is not found.
     *
     * @todo Move to the {@link org.geotools.parameter.Parameters} class.
     */
    protected static <T> T value(final ParameterDescriptor<T> param,
                                 final ParameterValueGroup    group)
            throws ParameterNotFoundException
    {
        final ParameterValue<T> value = getParameter(param, group);
        return (value != null) ? value.getValue() : null;
    }

    /**
     * Returns the parameter value for the specified operation parameter.
     * This convenience method is used by subclasses for initializing
     * {@linkplain MathTransform math transform} from a set of parameters.
     *
     * @param  param The parameter to look for.
     * @param  group The parameter value group to search into.
     * @return The requested parameter value, or {@code null} if {@code param} is
     *         {@linkplain #createOptionalDescriptor optional} and the user didn't
     *         provided any value.
     * @throws ParameterNotFoundException if the parameter is not found.
     *
     * @todo Move to the {@link org.geotools.parameter.Parameters} class.
     */
    protected static String stringValue(final ParameterDescriptor<?> param,
                                        final ParameterValueGroup    group)
            throws ParameterNotFoundException
    {
        final ParameterValue<?> value = getParameter(param, group);
        return (value != null) ? value.stringValue() : null;
    }

    /**
     * Returns the parameter value for the specified operation parameter.
     * This convenience method is used by subclasses for initializing
     * {@linkplain MathTransform math transform} from a set of parameters.
     *
     * @param  param The parameter to look for.
     * @param  group The parameter value group to search into.
     * @return The requested parameter value, or {@code 0} if {@code param} is
     *         {@linkplain #createOptionalDescriptor optional} and the user didn't
     *         provided any value.
     * @throws ParameterNotFoundException if the parameter is not found.
     *
     * @todo Move to the {@link org.geotools.parameter.Parameters} class.
     */
    protected static int intValue(final ParameterDescriptor<?> param,
                                  final ParameterValueGroup    group)
            throws ParameterNotFoundException
    {
        final ParameterValue<?> value = getParameter(param, group);
        return (value != null) ? value.intValue() : 0;
    }

    /**
     * Returns the parameter value for the specified operation parameter.
     * Values are automatically converted into the standard units specified
     * by the supplied {@code param} argument.
     * This convenience method is used by subclasses for initializing
     * {@linkplain MathTransform math transform} from a set of parameters.
     *
     * @param  param The parameter to look for.
     * @param  group The parameter value group to search into.
     * @return The requested parameter value, or {@code NaN} if {@code param} is
     *         {@linkplain #createOptionalDescriptor optional} and the user didn't
     *         provided any value.
     * @throws ParameterNotFoundException if the parameter is not found.
     *
     * @todo Move to the {@link org.geotools.parameter.Parameters} class.
     */
    protected static double doubleValue(final ParameterDescriptor<?> param,
                                        final ParameterValueGroup    group)
            throws ParameterNotFoundException
    {
        final Unit<?> unit = param.getUnit();
        final ParameterValue<?> value = getParameter(param, group);
        return (value == null) ? Double.NaN :
                (unit != null) ? value.doubleValue(unit) : value.doubleValue();
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
        final Class type = getOperationType();
        if (Projection.class.isAssignableFrom(type)) {
            return super.formatWKT(formatter);
        }
        formatter.setInvalidWKT(OperationMethod.class);
        return "OperationMethod";
    }

    /**
     * The result of a call to {@link MathTransformProvider#createMathTransform createMathTransform}.
     * This class encapsulates a reference to the {@linkplain #method originating provider}
     * as well as the {@linkplain #transform created math transform}. This information is needed
     * when a provider delegates the work to an other provider according the parameter values.
     * For example a generic instance of
     * {@link org.geotools.referencing.operation.transform.ProjectiveTransform.ProviderAffine
     * ProviderAffine} may delegates the creation of an <cite>affine transform</cite> to an other
     * {@code ProviderAffine} instance with <cite>source</cite> and <cite>target</cite> dimensions
     * matching the supplied parameters, because those dimensions determine the set of legal
     * <code>"elt_<var>j</var>_<var>i</var>"</code> parameters.
     * <p>
     * Most {@linkplain MathTransformProvider math transform provider} do not delegate their work
     * to an other one, and consequently do not need this class.
     * <p>
     * Future Geotools version may extends this class for handling more information than just the
     * {@linkplain #transform transform} creator. This class is more convenient than adding new
     * methods right into {@link MathTransformProvider}, because it is sometime difficult for a
     * provider to infer all the conditions prevaling when
     * {@link MathTransformProvider#createMathTransform createMathTransform} was executed.
     * Furthermore, it avoid to pollute {@code MathTransformProvider} with methods unused
     * for the vast majority of providers.
     *
     * @version $Id$
     * @author Martin Desruisseaux (IRD)
     *
     * @since 2.2
     */
    protected static final class Delegate extends MathTransformProxy {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -3942740060970730790L;

        /**
         * The provider for the {@linkplain #transform transform}.
         */
        public final OperationMethod method;

        /**
         * Encapsulates the math transform created by the specified provider.
         *
         * @param transform The math transform created by provider.
         * @param method The provider, typically as an instance of {@link MathTransformProvider}.
         */
        public Delegate(final MathTransform transform, final OperationMethod method) {
            super(transform);
            this.method = method;
            ensureNonNull("transform", transform);
            ensureNonNull("method",    method);
        }
    }
}
