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
package org.geotools.feature;

/**
 * A simple feature is one that does not have any nested attributes, and  that
 * has no multiplicity for each attribute.  In non xml speak this means that
 * the attributes returned are guaranteed to be the Objects you would expect -
 * not Lists as is the case when Features are non-simple.  This is  thus a
 * constraining extension - it essentially allows you to make a few more
 * assumptions about the nature of the {@link Feature} you are getting back.
 * 
 * <p>
 * The notion of a Simple Feature is drawn from the OGC's Simple Features for
 * SQL specification - where a simple feature represents a single row in  a
 * database table.  This extends beyond databases though, to flat files, for
 * example.  A database does not necessarily only return simple features -
 * indeed by relying on foreign keys much more complex structures can be
 * created.  But at the time of the creation of this class all GeoTools
 * datastores return Simple Features - they just were not explicitly called
 * that.  Making explicit that they are Simple should hopefully encourage more
 * complex Features to be returned.
 * </p>
 * 
 * <p>
 * The assumptions one can make with Simple Features are as follows:
 * </p>
 * 
 * <ul>
 * <li>
 * If  {@link #getAttribute(int)}  is called then it will always return an
 * actual object, instead of a List,  as is common in the parent Feature
 * class.  That is to say a Simple Feature will never have more than one
 * attribute in any of its positions, so the interface just assumes that you
 * want the actual object, instead of a List containing just the object.
 * </li>
 * <li>
 * If {@link #setAttribute(int, Object)} is called then a similar assumption is
 * made about the object being set - it need be a List, will default to
 * setting the attribute itself.
 * </li>
 * <li>
 * {@link #getAttribute(String)} and {@link #setAttribute(String, Object)}
 * implicitly append a [0], as that's the behavior implementors expect -  to
 * name an attribute and get it back.
 * </li>
 * </ul>
 * 
 * <p>
 * To figure out if a Feature is a SimpleFeature one may call instanceof.  For
 * a number of Features returned from a DataStore it will save much energy if
 * instanceof is called on the FeatureType, to check if it is a {@link
 * SimpleFeatureType}.  And in the future we should have FeatureCollections
 * that know their types.
 * </p>
 *
 * @author David Zwiers, Refractions
 * @author Chris Holmes, TOPP
 * @source $URL$
 * @version $Id$
 *
 * @task REVISIT: I am not sure that I like getAttribute returning the  object
 *       straight away.  It might be better to have a getFirstAttribute()
 *       method in Feature.java, and move people to get used to calling that,
 *       or  else to expect a List (which in a SimpleFeature would always only
 *       contain one Object).  This would seem to make the api a bit cleaner
 *       in my mind.
 * @since 2.1
 * 
 * @deprecated use {@link org.opengis.feature.simple.SimpleFeature}
 */
public interface SimpleFeature extends Feature {
    /**
     * Gets a reference to the schema for this feature. This method should
     * always return DefaultFeatureType Object.  This will be explicitly
     * posible in Java 1.5 (dz)
     *
     * @return A reference to this simple feature's schema.
     */
    FeatureType getFeatureType();

    /**
     * Sets all attributes for this feature, passed as a complex object array.
     * Note that this array must conform to the internal schema for this
     * feature, or it will throw an exception.  Checking this is, of course,
     * left to the feature to do internally.  Well behaved features should
     * always fully check the passed attributes against thier schema before
     * adding them.  Since this is a SimpleFeature, the number of attributes
     * will be exactly  the same as the number of attribute types. Attribute
     * values will be paired  with attribute types based on array indexes.
     *
     * @param attributes All feature attributes.
     *
     * @throws IllegalAttributeException Passed attributes do not match schema.
     */
    void setAttributes(Object[] attributes) throws IllegalAttributeException;

    /**
     * This is the same as the parent declaration, except that when the
     * instance  is not specified for the xPath, [0] will be added as there is
     * only ever one  Attribute value for an AttributeType
     *
     * @param xPath XPath representation of attribute location.
     *
     * @return A copy of the requested attribute, null if the requested xpath
     *         is not found, or NULL_ATTRIBUTE.
     *
     * @see Feature#getAttribute(String)
     */
    Object getAttribute(String xPath);

    /**
     * Gets an attribute by the given zero-based index. Unlike the parent
     * interface,  this index is guaranteed to match the index of
     * AttributeType in the FeatureType.
     *
     * @param index The requested index. Must be 0 &lt;= idx &lt;
     *        getNumberOfAttributes().
     *
     * @return A copy of the requested attribute, or NULL_ATTRIBUTE.
     */
    Object getAttribute(int index);

    /**
     * Sets an attribute by the given zero-based index. Unlike the parent
     * interface,  this index is guaranteed to match the index of
     * AttributeType in the FeatureType.
     *
     * @param position The requested index. Must be 0 &lt;= idx &lt;
     *        getNumberOfAttributes()
     * @param val An object representing the attribute being set
     *
     * @throws IllegalAttributeException if the passed in val does not validate
     *         against the AttributeType at that position.
     * @throws ArrayIndexOutOfBoundsException if an invalid position is given
     */
    void setAttribute(int position, Object val)
        throws IllegalAttributeException, IndexOutOfBoundsException;

    /**
     * Allows this feature to turn itself to a Complex Feature - that is one
     * with multiplicity.  This is used so that clients can choose to deal
     * with all Complex Features if they would like - always getting lists
     * back when they ask for objects.  Of course when a SimpleFeature turns
     * itself into a Complex Feature then all its lists will be of length  1.
     * Am leaving this commented out since it's approved in the api yet -ch
     */

    //Feature toComplex();
}
