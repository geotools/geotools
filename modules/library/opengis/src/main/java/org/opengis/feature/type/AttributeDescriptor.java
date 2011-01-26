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

/**
 * Describes an instance of an Attribute.
 * <p>
 * An AttributeDescriptor is an extension of {@link PropertyDescriptor} which
 * defines some additional information:
 * <ul>
 *   <li>A default value for an attribute
 * </ul>
 * </p>
 * <p>
 *
 * @author Jody Garnett, Refractions Research
 * @author Justin Deoliveira, The Open Planning Project
 */
public interface AttributeDescriptor extends PropertyDescriptor {

    /**
     * Override of {@link PropertyDescriptor#getType()} which type narrows to
     * {@link AttributeType}.
     *
     *  @see PropertyDescriptor#getType()
     */
    AttributeType getType();

    /**
     * The local name for this AttributeDescriptor.
     * Specifically this returns <code>getName().getLocalPart</code>().
     * @return The local name for this attribute descriptor.
     */
    String getLocalName();

    /**
     * The default value for the attribute.
     * <p>
     * This value is used when an attribute is created and no value for it is
     * specified.
     * </p>
     * <p>
     * This value may be <code>null</code>. If it is non-null it should be an
     * instance of of the class specified by <code>getType().getBinding()</code>.
     * </p>
     */
    Object getDefaultValue();
}
