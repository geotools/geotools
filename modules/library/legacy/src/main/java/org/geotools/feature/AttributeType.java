/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;


/**
 * <p>
 * Stores metadata about a single attribute object.
 *
 * <ol>
 * <li>
 * Name: A string that is used to reference the attribute.
 * </li>
 * <li>
 * Nillable: if nulls are allowed as this attribute.
 * </li>
 * <li>
 * Type: The expected Java class of this attribute.
 * </li>
 * </ol>
 * </p>
 *
 * <p>
 * AttributeTypes must also provide the <code>validate(Object obj)</code>
 * method, which determines whether a given object matches the constraints
 * imposed by the AttributeType.  In a default attribute this will simply be
 * that it is of the correct class and non-null (or null if isNillable is
 * true).  More complex AttributeTypes can impose any restrictions that they
 * like.  Nested FeatureTypes are an example of this, as they need to check
 * that the Feature object matches all its constraints, not that it is  just
 * of class Feature.
 * </p>
 *
 * <p>
 * Additionally, implementors may use the parse method to convert an object to
 * its preferred storage type.  If an implementor does not choose to provide
 * any functionality for this method they should simple return the object
 * passed in.  If parsing is attempted and not successful, then an exception
 * should be thrown.  This method is primarily used by FeatureType to try to
 * convert objects to the correct storage type, such as a string of a double
 * when the AttributeType requires a Double.
 * </p>
 *
 * @author Rob Hranac, VFNY
 * @author Chris Holmes, TOPP
 * @source $URL$
 * @version $Id$
 *
 * @deprecated use {@link AttributeDescriptor}.
 */
public interface AttributeType extends AttributeDescriptor {
    /** Represents any number of elements. Same '' in a reg-ex */
    public static int UNBOUNDED = Integer.MAX_VALUE;

    /* feature-exp2 redesign notes:
     * 1) removed isNested() from the api as are going to incorporating
     * more complex attribute sequences ... including sets and choices.
     * isNested() was used very little, and was never fully thought through
     * So killing it should not have too much of an effect.
     *
     * 2) Added minOccurs and maxOccurs since we are going to get into
     * multiplicity, where this matters.  The isNilleable is not enough
     *
     * 3) deprecated isGeometry() as it is a convenience method that can
     * be accomplished in different ways, and it means nothing for a
     * featureType
     *
     * 4) We are planning on killing getFieldLength, as it is extremely
     * impercise, has no general meaning.  We are going to replace it
     * with the concept of xml facets, hopefully we will have time to.
     *
     */

    /**
     * Gets the name of this attribute.
     *
     * @return Name.
     * @deprecated use {@link #getLocalName()}
     */

    //String getLocalName();

    /**
     * Returns the unqualified name of this attribute.
     * <p>
     * This method is a replacement for {@link #getName()} in order to resolve
     * a naming conflict with the geoapi feature model.
     * </p>
     * @see PropertyDescriptor#getName()
     * @since 2.4
     */
    String getLocalName();

    /**
     * Gets the type of this attribute.
     *
     * @return Type.
     * @deprecated use {@link #getBinding()}
     */

    //Class getBinding();

    /**
     * Gets the class/type/binding for this attribute.
     * <p>
     * This method is a replacement for {@link #getType()} in order to resolve
     * a naming conflict with the geoapi feature model.
     * </p>
     *
     * @see AttributeDescriptor#getType()
     * @since 2.4
     */
    Class getBinding();

    /**
     * This represents a Facet in XML schema ... for example can be used to
     * represent the max length of 20 for a string.
     *
     * @return Filter, or Filter.INCLUDE if no restriction is needed.
     */
    Filter getRestriction();

    /**
     * Returns whether nulls are allowed for this attribute.
     *
     * @return true if nulls are permitted, false otherwise.
     */
    boolean isNillable();

    /**
     * Returns the Min number of occurences ...
     *
     */
    int getMinOccurs();

    /**
     * Returns the Max number of occurences ...
     *
     */
    int getMaxOccurs();

    /**
     * Allows this AttributeType to convert an argument to its prefered storage
     * type. If no parsing is possible, returns the original value. If a parse
     * is attempted, yet fails (i.e. a poor decimal format) throw the
     * Exception. This is mostly for use internally in Features, but
     * implementors should simply follow the rules to be safe.
     *
     * @param value the object to attempt parsing of.
     *
     * @return <code>value</code> converted to the preferred storage of this
     *         <code>AttributeType</code>.  If no parsing was possible then
     *         the same object is returned.
     *
     * @throws IllegalArgumentException if parsing is attempted and is
     *         unsuccessful.
     */
    Object parse(Object value) throws IllegalArgumentException;

    /**
     * Whether the tested object passes the validity constraints of  this
     * AttributeType.  At a minimum it should be of the correct class
     * specified by {@link #getBinding()}, non-null if isNillable is
     * <tt>false</tt>, and a geometry if isGeometry is <tt>true</tt>.  If The
     * object does not validate then an IllegalArgumentException reporting the
     * error in validation should be thrown.
     *
     * @param obj The object to be tested for validity.
     *
     * @throws IllegalArgumentException if the object does not validate.
     */
    void validate(Object obj) throws IllegalArgumentException;

    /**
     * Create a duplicate value of the passed Object. For immutable Objects, it
     * is not neccessary to create a new Object.
     *
     * @param src The Object to duplicate.
     *
     * @return Duplicate of provided object
     *
     * @throws IllegalAttributeException If the src Object is not the correct
     *         type.
     */
    Object duplicate(Object src) throws IllegalAttributeException;

    /**
     * Create a default value for this AttributeType. If the type is nillable,
     * the Object may or may not be null.
     *
     * @return Default value, note may be null if isNillable is true
     */
    Object createDefaultValue();
}
