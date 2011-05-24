/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature;

import java.util.Collection;

import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;

/**
 * An instance of {@link ComplexType} which is composed of other properties.
 * <p>
 * A complex attribute is a container for other properties (attributes +
 * associations). The value of a complex attribute is a collection of those
 * contained properties.
 * </p>
 * <br/>
 * <p>
 * <h3>Property Access</h3>
 * The {@link #getValue()} method returns a collection of the properties
 * contained by the complex attribute.
 *
 * <pre>
 *    ComplexAttribute attribute = ...;
 *
 *    //loop through all the properties
 *    for (Property p : attribute.getValue(); ) {
 *        // do something with the property
 *    }
 * </pre>
 *
 * <br>
 * Contained properties can also be fetched by name by {@link Name} with the
 * {@link #getProperties(Name)} and {@link #getProperties(String)} methods.
 *
 * <pre>
 *    ComplexAttribute attribute = ...;
 *
 *    //loop through all the &quot;foo&quot; attributes
 *    for ( Property p : attribute.getProperties( &quot;foo&quot; ) ) {
 *        p.getName().getLocalPart() == &quot;foo&quot;;
 *    }
 * </pre>
 *
 * <br>
 * Often it is known in advance that a single instance of a particular property
 * exists. When this is the case the {@link #getProperty(Name)} and
 * {@link #getProperty(String)} methods can be used to get direct access to the
 * property.
 *
 * <pre>
 *    ComplexAttribute attribute = ...;
 *
 *    //get the single foo attribute
 *    Property foo = attribute.getProperty( &quot;foo&quot; );
 * </pre>
 *
 * </p>
 * <br>
 * <p>
 * <h3>Xpath and Query Language Access</h3>
 * The above property access methods perform an exact match on property name
 * against the name passed in. However, often it is necesary to access
 * properties via a query language such as xpath.
 * </p>
 * <br>
 * <p>
 * For instance.the expression <code>"//foo"</code> should return all the
 * properties named "foo". Or the expression <code>"foo/bar"</code> should
 * return the "bar" property nested inside of the "foo" property. In these
 * cases, an {@link Expression} must be used:
 *
 * <pre>
 *   ComplexAttribute attribute = ...;
 *
 *   //get the 'foo/bar' property
 *   FilterFactory factory = ...;
 *   PropertyName xpath = factory.property( &quot;foo/bar&quot; );
 *   Property bar = xpath.evaluate( attribute );
 * </pre>
 *
 * </p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Gabriel Roldan, Axios Engineering
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/feature/ComplexAttribute.java $
 */
public interface ComplexAttribute extends Attribute {

    /**
     * Override of {@link Attribute#getType()} which type narrows to
     * {@link ComplexType}.
     *
     * @see Attribute#getType()
     */
    ComplexType getType();

    /**
     * Sets the contained properties of the complex attribute.
     * <p>
     * The <tt>values</tt> should match the structure defined by
     * <code>getDescriptor()</code>.
     * </p>
     */
    void setValue(Collection<Property> values);

    /**
     * Override of {@link Property#getValue()} which returns the collection of
     * {@link Property} which make up the value of the complex attribute.
     */
    Collection<? extends Property> getValue();

    /**
     * Returns a subset of the properties of the complex attribute which match
     * the specified name.
     * <p>
     * The <tt>name</tt> parameter is matched against each contained
     * {@link Property#getName()}, those that are equal are returned.
     * </p>
     *
     * @param name
     *            The name of the properties to return.
     *
     * @return The collection of properties which match the specified name, or
     *         an empty collection if no such properties match.
     */
    Collection<Property> getProperties(Name name);

    /**
     * Returns single property of the complex attribute which matches the
     * specified name.
     * <p>
     * Note: This method is a convenience and care should be taken when calling
     * it if more then a single property matches <tt>name</tt>. In such a
     * case the first encountered property in which {@link Property#getName()}
     * is equal to <tt>name</tt> is returned, and no order is guaranteed.
     * </p>
     * <p>
     * This method is a safe convenience for:
     *
     * <code>getProperties(name).iterator().next()</code>.
     *
     * In the event that no property matches the specified name
     * <code>null</code> is returned.
     * </p>
     *
     * @param name
     *            The name of the property to return.
     *
     * @return The property matching the specified name, or <code>null</code>.
     */
    Property getProperty(Name name);

    /**
     * Returns a subset of the properties of the complex attribute which match
     * the specified name.
     * <p>
     * This method is a convenience for {@link #getProperties(Name)} in which
     * {@link Name#getNamespaceURI()} is <code>null</code>.
     * </p>
     * <p>
     * Note: Special care should be taken when using this method in the case
     * that two properties with the same local name but different namespace uri
     * exist. For this reason using {@link #getProperties(Name)} is safer.
     * </p>
     *
     * @param name
     *            The local name of the properties to return.
     *
     * @return The collection of properties which match the specified name, or
     *         an empty collection if no such properties match.
     *
     * @see #getProperties(Name)
     */
    Collection<Property> getProperties(String name);

    /**
     * Complete collection of properties.
     * <p>
     * This method is a convenience method for calling (Collection<Property>) getValue().
     * </p>
     * @return The complete collection of properties.
     */
    Collection<Property> getProperties();

    /**
     * Returns single property of the complex attribute which matches the
     * specified name.
     * <p>
     * This method is a convenience for {@link #getProperty(Name)} in which
     * {@link Name#getNamespaceURI()} is <code>null</code>.
     * </p>
     * <p>
     * Note: This method is a convenience and care should be taken when calling
     * it if more then a single property matches <tt>name</tt>. In such a
     * case the first encountered property in which {@link Property#getName()}
     * is matches <tt>name</tt> is returned, and no order is guaranteed.
     * </p>
     * <p>
     * Note: Special care should be taken when using this method in the case
     * that two properties with the same local name but different namespace uri
     * exist. For this reason using {@link #getProperties(Name)} is safer.
     * </p>
     *
     * @param name
     *            The local name of the property to return.
     *
     * @return The property matching the specified name, or <code>null</code>.
     */
    Property getProperty(String name);
    
    /**
     * Check the properties against the constraints provided by their AttributeDescriptors.
     * <p>
     * Please note this method checks minOccurs and maxOccurs information; and calls each Attribute.validate
     * on each entry in turn (in order to check isNillable, binding and restrictions).
     * @throws IllegalAttributeException If any attribute fails validation
     */
    void validate() throws IllegalAttributeException;
}
