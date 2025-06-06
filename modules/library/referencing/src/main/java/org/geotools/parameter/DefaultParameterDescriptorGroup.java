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

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.api.metadata.citation.Citation;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.InvalidParameterNameException;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.util.UnmodifiableArrayList;

/**
 * The definition of a group of related parameters used by an operation method.
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 * @see ParameterGroup
 * @see DefaultParameterDescriptor
 */
public class DefaultParameterDescriptorGroup extends AbstractParameterDescriptor implements ParameterDescriptorGroup {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -4613190550542423839L;

    /** The maximum number of times that values for this parameter group or parameter are required. */
    private final int maximumOccurs;

    /** The {@linkplain #descriptors() parameter descriptors} for this group. */
    private final GeneralParameterDescriptor[] parameters;

    /** A view of {@link #parameters} as an immutable list. Will be constructed only when first needed. */
    private transient List<GeneralParameterDescriptor> asList;

    /**
     * Constructs a group with the same values than the specified one. This copy constructor may be used in order to
     * wraps an arbitrary implementation into a Geotools one.
     *
     * @since 2.2
     */
    public DefaultParameterDescriptorGroup(final ParameterDescriptorGroup group) {
        super(group);
        maximumOccurs = group.getMaximumOccurs();
        final List<GeneralParameterDescriptor> c = group.descriptors();
        parameters = c.toArray(new GeneralParameterDescriptor[c.size()]);
    }

    /**
     * Constructs a parameter group from a name. This parameter group will be required exactly once.
     *
     * @param name The parameter group name.
     * @param parameters The {@linkplain #descriptors() parameter descriptors} for this group.
     */
    public DefaultParameterDescriptorGroup(final String name, final GeneralParameterDescriptor... parameters) {
        this(Collections.singletonMap(NAME_KEY, name), parameters);
    }

    /**
     * Constructs a parameter group from a name and an authority. This parameter group will be required exactly once.
     *
     * @param authority The authority (e.g. {@link org.geotools.metadata.iso.citation.Citations#OGC OGC}).
     * @param name The parameter group name.
     * @param parameters The {@linkplain #descriptors() parameter descriptors} for this group.
     * @since 2.2
     */
    public DefaultParameterDescriptorGroup(
            final Citation authority, final String name, final GeneralParameterDescriptor... parameters) {
        this(Collections.singletonMap(NAME_KEY, new NamedIdentifier(authority, name)), parameters);
    }

    /**
     * Constructs a parameter group from a set of properties. This parameter group will be required exactly once. The
     * properties map is given unchanged to the {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map)
     * super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param parameters The {@linkplain #descriptors() parameter descriptors} for this group.
     */
    public DefaultParameterDescriptorGroup(
            final Map<String, ?> properties, final GeneralParameterDescriptor... parameters) {
        this(properties, 1, 1, parameters);
    }

    /**
     * Constructs a parameter group from a set of properties. The properties map is given unchanged to the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param minimumOccurs The {@linkplain #getMinimumOccurs minimum number of times} that values for this parameter
     *     group are required.
     * @param maximumOccurs The {@linkplain #getMaximumOccurs maximum number of times} that values for this parameter
     *     group are required.
     * @param parameters The {@linkplain #descriptors() parameter descriptors} for this group.
     */
    public DefaultParameterDescriptorGroup(
            final Map<String, ?> properties,
            final int minimumOccurs,
            final int maximumOccurs,
            GeneralParameterDescriptor... parameters) {
        super(properties, minimumOccurs, maximumOccurs);
        this.maximumOccurs = maximumOccurs;
        ensureNonNull("parameters", parameters);
        this.parameters = new GeneralParameterDescriptor[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            this.parameters[i] = parameters[i];
            ensureNonNull("parameters", parameters, i);
        }
        /*
         * Ensure there is no conflict in parameter names.
         */
        parameters = this.parameters;
        for (int i = 0; i < parameters.length; i++) {
            final String name = parameters[i].getName().getCode();
            for (int j = 0; j < parameters.length; j++) {
                if (i != j) {
                    if (nameMatches(parameters[j], name)) {
                        final Object arg0 = parameters[j].getName().getCode();
                        throw new InvalidParameterNameException(
                                MessageFormat.format(ErrorKeys.PARAMETER_NAME_CLASH_$4, arg0, j, name, i), name);
                    }
                }
            }
        }
    }

    /**
     * The maximum number of times that values for this parameter group are required.
     *
     * @see #getMinimumOccurs
     */
    @Override
    public int getMaximumOccurs() {
        return maximumOccurs;
    }

    /**
     * Creates a new instance of {@linkplain ParameterGroup parameter value group} initialized with the
     * {@linkplain ParameterDescriptor#getDefaultValue default values}. The
     * {@linkplain org.geotools.api.parameter.ParameterValueGroup#getDescriptor parameter value descriptor} for the
     * created group will be {@code this} object.
     */
    @Override
    public ParameterValueGroup createValue() {
        return new ParameterGroup(this);
    }

    /**
     * A view of {@link #parameters} as an unmodifiable list. This class overides {@link #contains} with a faster
     * implementation based on {@link HashSet}. It can help for map projection implementations (among other), which test
     * often for a parameter validity.
     */
    private static final class AsList extends UnmodifiableArrayList<GeneralParameterDescriptor> {
        /** For compatibility with different versions. */
        private static final long serialVersionUID = -2116304004367396735L;

        /** The element as a set. Will be constructed only when first needed. */
        private transient Set<GeneralParameterDescriptor> asSet;

        /** Constructs a list for the specified array. */
        public AsList(final GeneralParameterDescriptor... array) {
            super(array);
        }

        /** Tests for the inclusion of the specified descriptor. */
        @Override
        public boolean contains(final Object object) {
            if (asSet == null) {
                asSet = new HashSet<>(this);
            }
            return asSet.contains(object);
        }
    }

    /** Returns the parameters in this group. */
    @Override
    public List<GeneralParameterDescriptor> descriptors() {
        if (asList == null) {
            if (parameters == null) {
                asList = Collections.emptyList();
            } else
                switch (parameters.length) {
                    case 0:
                        asList = Collections.emptyList();
                        break;
                    case 1:
                        asList = Collections.singletonList(parameters[0]);
                        break;
                    case 2: // fall through
                    case 3:
                        asList = UnmodifiableArrayList.wrap(parameters);
                        break;
                    default:
                        asList = new AsList(parameters);
                        break;
                }
        }
        return asList;
    }

    /**
     * Returns the first parameter in this group for the specified
     * {@linkplain org.geotools.api.metadata.Identifier#getCode identifier code}.
     *
     * @param name The case insensitive identifier code of the parameter to search for.
     * @return The parameter for the given identifier code.
     * @throws ParameterNotFoundException if there is no parameter for the given identifier code.
     */
    @Override
    public GeneralParameterDescriptor descriptor(String name) throws ParameterNotFoundException {
        ensureNonNull("name", name);
        name = name.trim();
        List<DefaultParameterDescriptorGroup> subgroups = null;
        List<GeneralParameterDescriptor> parameters = descriptors();
        while (parameters != null) {
            for (final GeneralParameterDescriptor param : parameters) {
                if (param instanceof ParameterDescriptor) {
                    if (nameMatches(param, name)) {
                        return param;
                    }
                } else if (param instanceof DefaultParameterDescriptorGroup) {
                    if (subgroups == null) {
                        subgroups = new LinkedList<>();
                    }
                    assert !subgroups.contains(param) : param;
                    subgroups.add((DefaultParameterDescriptorGroup) param);
                }
            }
            /*
             * Looks in subgroups only after all parameters in the current group have been verified.
             * Search in a "first in, first out" basis.
             */
            if (subgroups == null || subgroups.isEmpty()) {
                break;
            }
            parameters = subgroups.remove(0).descriptors();
        }
        throw new ParameterNotFoundException(MessageFormat.format(ErrorKeys.MISSING_PARAMETER_$1, name), name);
    }

    /**
     * Compares the specified object with this parameter group for equality.
     *
     * @param object The object to compare to {@code this}.
     * @param compareMetadata {@code true} for performing a strict comparaison, or {@code false} for comparing only
     *     properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object, compareMetadata)) {
            final DefaultParameterDescriptorGroup that = (DefaultParameterDescriptorGroup) object;
            return Arrays.equals(this.parameters, that.parameters);
        }
        return false;
    }

    /**
     * Returns a hash value for this parameter.
     *
     * @return The hash code value. This value doesn't need to be the same in past or future versions of this class.
     */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        int code = super.hashCode();
        // TODO: We should use Arrays.deepHashCode instead in J2SE 1.5.
        for (GeneralParameterDescriptor parameter : parameters) {
            code = code * 37 + parameter.hashCode();
        }
        return code;
    }
}
