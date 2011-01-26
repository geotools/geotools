/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2004-2007 Open Geospatial Consortium Inc.
 *    
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.opengis.feature.type;

import java.util.Map;

import org.opengis.feature.ComplexAttribute;

/**
 * Describes a Property, and how it relates to its containing entity, which is
 * often a {@link ComplexAttribute}.
 * <br>
 * <p>
 * A property descriptor defines the following about the property:
 * <ul>
 *   <li>type of the property
 *   <li>the name of the property
 *   <li>number of allowable occurrences of the property
 *   <li>nilability of the property
 * </ul>
 * </p>
 * <br>
 * <p>
 * The concept of a descriptor is similar to that of a element declaration in
 * xml. Consider the following xml schema definition:
 * <pre>
 *   &lt;complexType name="someComplexType">
 *     &lt;sequence>
 *       &lt;element name="foo" minOccurs="2" maxOccurs="4" type="xs:string" nillable="false"/>
 *     &lt;sequence>
 *   &lt;complexType>
 * </pre>
 * <br>
 * In the above, the element declaration named "foo" maps to a property descriptor.
 * From the above schema, the following property descriptor would result:
 * <pre>
 *  //the complex type
 *  ComplexType complexType = ...;
 *
 *  //get the descriptor
 *  PropertyDescriptor descriptor = complexType.getProperty( "foo" );
 *
 *  //make the following assertions
 *  descriptor.getName().getLocalPart().equals( "foo" );
 *
 *  descriptor.getType().getName().getNamespaceURI().equals( "http://www.w3.org/2001/XMLSchema" )
 *  descriptor.getType().getName().getLocalPart().equals( "string" );
 *  descriptor.getMinOccurs() == 2;
 *  descriptor.getMaxOccurs() == 4;
 *  descriptor.isNillable() == true;
 *
 *  //the complex attribute
 *  ComplexAttribute complexAttribute = ...
 *  complexAttribute.getType() == complexType;
 *
 *  //get the properties
 *  Collection properties = complexAttribute.getProperties( "foo" );
 *
 *  //make assertions about properties
 *  properties.size() >= 2;  //minOccurs = 2
 *  properties.size() <= 4; //maxOccurs = 4
 *
 *  for ( Property p : properties ) {
 *      p.getDescriptor() == descriptor
 *
 *      p.getValue() != null; //nilable = false
 *      p.getType().getBinding() == String.class; //type = xs:string
 *      p.getValue() instanceof String; //type = xs:string
 *  }
 * </pre>
 * <p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface PropertyDescriptor {
    /**
     * The type of the property defined by the descriptor.
     * <p>
     * This value should never be <code>null</code>. The type contains information
     * about the value of the property such as its java class.
     * </p>
     */
    PropertyType getType();

    /**
     * The name of the property defined by the descriptor, with respect to its
     * containing type or entity..
     * <p>
     * This value may be <code>null</code> in some instances. Also note that this
     * is not the same name as <code>getType().getName()</code>. The former is
     * the name of the instance, the latter is the name of the type of the
     * instance.
     * </p>
     */
    Name getName();

    /**
     * The minimum number of occurrences of the property within its containing
     * entity.
     * <p>
     * This value is always an integer greater than or equal to zero.
     * </p>
     * @return An integer >= 0
     */
    int getMinOccurs();

    /**
     * The maximum number of occurrences of the property within its containing
     * entity.
     * <p>
     * This value is a positive integer. A value of <code>-1</code> means that
     * the max number of occurrences is unbounded.
     * </p>
     * @return An integer >= 0, or -1.
     */
    int getMaxOccurs();

    /**
     * Flag indicating if <code>null</code> is an allowable value for the
     * property.
     *
     * @return <code>true</code> if the property is allowed to be <code>null</code>,
     * otherwise <code>false</code>.
     */
    boolean isNillable();

    /**
     * A map of "user data" which enables applications to store "application-specific"
     * information against a property descriptor.
     *
     * @return A map of user data.
     */
    Map<Object,Object> getUserData();
}
