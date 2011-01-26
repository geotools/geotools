/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2003-2005, Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.parameter;

import java.util.List;
import org.opengis.metadata.Identifier;
import org.opengis.annotation.UML;
import org.opengis.annotation.Extension;

import static org.opengis.annotation.Obligation.*;
import static org.opengis.annotation.Specification.*;


/**
 * A group of related parameter values. The same group can be repeated more than once in an
 * {@linkplain org.opengis.referencing.operation.Operation operation} or higher level {@code ParameterValueGroup},
 * if those instances contain different values of one or more {@link ParameterValue}s which suitably
 * distinquish among those groups.
 *
 * @version <A HREF="http://portal.opengeospatial.org/files/?artifact_id=6716">Abstract specification 2.0</A>
 * @author  Martin Desruisseaux (IRD)
 * @author  Jody Garnett (Refractions Research)
 * @since   GeoAPI 1.0
 *
 * @see ParameterDescriptorGroup
 * @see ParameterValue
 */
@UML(identifier="CC_ParameterValueGroup", specification=ISO_19111)
public interface ParameterValueGroup extends GeneralParameterValue {
    /**
     * The abstract definition of this group of parameters.
     *
     * @departure
     *   The ISO name was {@code "valuesOfGroup"}. GeoAPI uses {@code "descriptor"} instead in order
     *   to override the {@linkplain GeneralParameterValue#getDescriptor generic method provided in
     *   the parent interface}. The "descriptor" name make more apparent that this method returns an
     *   abstract definition of parameters - not their actual values - and is consistent with usage
     *   in other Java libraries like {@link javax.media.jai.ParameterList#getParameterListDescriptor
     *   ParameterList}.
     */
    @UML(identifier="valuesOfGroup", obligation=MANDATORY, specification=ISO_19111)
    ParameterDescriptorGroup getDescriptor();

    /**
     * Returns the values in this group. The returned list may or may not be unmodifiable;
     * this is implementation-dependent. However, if some aspects of this list are modifiable,
     * then any modification shall be reflected back into this {@code ParameterValueGroup}.
     * More specifically:
     *
     * <UL>
     *   <LI><P>If the list supports the {@link List#add(Object) add} operation, then it should
     *       ensure that the added {@linkplain GeneralParameterValue general parameter value} is
     *       valid and can be added to this group.
     *       An {@link InvalidParameterCardinalityException} (or any other appropriate exception)
     *       shall be thrown if it is not the case.</P></LI>
     *   <LI><P>The list may also supports the {@link List#remove(Object) remove} operation as a
     *       way to remove parameter created by the {@link #parameter} method.</P></LI>
     * </UL>
     *
     * @return The values in this group.
     */
    @UML(identifier="includesValue", obligation=MANDATORY, specification=ISO_19111)
    List<GeneralParameterValue> values();

    /**
     * Returns the value in this group for the specified {@linkplain Identifier#getCode
     * identifier code}. If no {@linkplain ParameterValue parameter value} is found but
     * a {@linkplain ParameterDescriptor parameter descriptor} is found (which may occurs
     * if the parameter is optional, i.e. <code>{@linkplain ParameterDescriptor#getMinimumOccurs
     * minimumOccurs} == 0</code>), then a {@linkplain ParameterValue parameter value} is
     * automatically created and initialized to its {@linkplain ParameterDescriptor#getDefaultValue
     * default value} (if any).
     * <p>
     * This convenience method provides a way to get and set parameter values by name. For
     * example the following idiom fetches a floating point value for the
     * {@code "false_easting"} parameter:
     *
     * <blockquote><code>
     * double value = parameter("false_easting").{@linkplain ParameterValue#doubleValue() doubleValue()};
     * </code></blockquote>
     *
     * This method do not search recursively in subgroups. This is because more than one
     * subgroup may exist for the same {@linkplain ParameterDescriptorGroup descriptor}.
     * The user must {@linkplain #groups query all subgroups} and select explicitly the
     * appropriate one to use.
     *
     * @param  name The case insensitive {@linkplain Identifier#getCode identifier code} of the
     *              parameter to search for.
     * @return The parameter value for the given identifier code.
     * @throws ParameterNotFoundException if there is no parameter value for the given identifier code.
     */
    @Extension
    ParameterValue<?> parameter(String name) throws ParameterNotFoundException;

    /**
     * Returns all subgroups with the specified name. This method do not create new groups.
     * If the requested group is optional (i.e.
     * <code>{@linkplain ParameterDescriptor#getMinimumOccurs minimumOccurs} == 0</code>)
     * and no value were defined previously, then this method returns an empty set.
     *
     * @param  name The case insensitive {@linkplain Identifier#getCode identifier code} of the
     *              parameter group to search for.
     * @return The set of all parameter group for the given identifier code.
     * @throws ParameterNotFoundException if no {@linkplain ParameterDescriptorGroup descriptor}
     *         was found for the given name.
     */
    @Extension
    List<ParameterValueGroup> groups(String name) throws ParameterNotFoundException;

    /**
     * Creates a new group of the specified name. The specified name must be the
     * {@linkplain Identifier#getCode identifier code} of a {@linkplain ParameterDescriptorGroup
     * descriptor group}.
     *
     * @param  name The case insensitive {@linkplain Identifier#getCode identifier code} of the
     *              parameter group to create.
     * @return A newly created parameter group for the given identifier code.
     * @throws ParameterNotFoundException if no {@linkplain ParameterDescriptorGroup descriptor}
     *         was found for the given name.
     * @throws IllegalStateException if this parameter group already contains the
     *         {@linkplain ParameterDescriptorGroup#getMaximumOccurs maximum number of occurences}
     *         of subgroups of the given name.
     */
    @Extension
    ParameterValueGroup addGroup(String name) throws ParameterNotFoundException, IllegalStateException;

    /**
     * Returns a copy of this group of parameter values.
     * Included parameter values and subgroups are cloned recursively.
     *
     * @return A copy of this group of parameter values.
     */
    ParameterValueGroup clone();
}
