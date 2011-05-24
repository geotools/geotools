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

import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.filter.identity.Identifier;

/**
 * An extension of Property for an attribute, or data.
 * <p>
 * The notion of an "attribute" is similar to that of an attribute in UML.
 * </p>
 * <p>
 * This interface is capable of modelling "primitive data", things like strings,
 * numerics, dates, etc... However for "complex data" (that is non-primitive
 * data types which are made up other primitive data types), a specific
 * sub-interface is used, see {@link ComplexAttribute}.
 * </p>
 * <p>
 * An analogy for an attribute is a "field" in a java object. A field also
 * brings together a field name, value and type.
 * </p>
 *
 * <p>
 * <h3>Identifiable</h3>
 *
 * When an attribute is identifiable the {@link #getID()} method returns a
 * unique identifier for the attribute. The type of the attribute is used to
 * determine identifiability.
 *
 * <pre>
 * Attribute attribute = ...;
 * if ( attribute.getType().isIdentified() ) {
 *   String id = attribute.getID();
 * }
 * </pre>
 * </p>
 * <h3>Validation</h3>
 * 
 * An attribute may hold any value at runtime; checking that the value meets the constraints
 * supplied by the AttributeType is the work of the validate() method.
 * 
 * @see Property
 *
 * @author Jody Garnett (Refractions Research)
 * @author Justin Deoliveira (The Open Planning Project)
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/modules/library/opengis/src/main/java/org/opengis/feature/Attribute.java $
 */
public interface Attribute extends Property {

    /**
     * Override of {@link Property#getDescriptor()} which type narrows to
     * {@link AttributeDescriptor}.
     *
     * @see Property#getDescriptor()
     * @return The attribute descriptor, may be null if this is a top level type
     */
    AttributeDescriptor getDescriptor();

    /**
     * Override of {@link Property#getType()} which type narrows to
     * {@link AttributeType}.
     *
     * @see Property#getType()
     * @return The attribute type.
     */
    AttributeType getType();

    /**
     * Unique Identifier for the attribute.
     * <p>
     * This value is non-null in the case that
     * <code>getType().isIdentifiable()</code> is <code>true</code>.
     * </p>
     *
     * @return A unique identifier for the attribute, or <code>null</code> if
     *         the attribute is non-identifiable.
     */
    Identifier getIdentifier();
    
    /**
     * Check the attribute value against the constraints provided by the AttributeDescriptor.
     * <p>
     * Please note this method checks the value only - it should have the correct java binding,
     * it should only be null if isNillable is true; and if a value is provided it should
     * satisfy all of the restrictions provided.
     * <p>
     * To check the the number of times an attribute is used (minOccurs and maxOccurs) please
     * use ComplexAttribute.validate().
     * 
     * @thorws IllegalAttributeException If value fails validation
     */
    void validate() throws IllegalAttributeException;
}
