/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.util.logging.Logger;
import org.geotools.data.complex.config.NonFeatureTypeProxy;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.opengis.feature.Attribute;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;

/**
 * Builder for attributes.
 * 
 * @author Justin Deoliveira (The Open Planning Project)
 * 
 *
 *
 *
 *
 * @source $URL$
 */
public class AppSchemaAttributeBuilder extends AttributeBuilder {
    
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(AppSchemaAttributeBuilder.class.getPackage().getName());


    public AppSchemaAttributeBuilder(FeatureFactory attributeFactory) {
        super(attributeFactory);
    }

    /**
     * Adds an attribute to the complex attribute being built overriding the type of the declared
     * attribute descriptor by a subtype of it. <br>
     * <p>
     * This method uses the type supplied in {@link #setType(AttributeType)} in order to determine
     * the attribute type.
     * </p>
     * 
     * @param id
     *                the attribtue id
     * @param value
     *                The value of the attribute.
     * 
     * @param name
     *                The name of the attribute.
     * @param type
     *                the actual type of the attribute, which might be the same as the declared type
     *                for the given AttributeDescriptor or a derived type.
     * 
     */
    public Attribute add(final String id, final Object value, final Name name,
            final AttributeType type) {
        // existence check
        AttributeDescriptor descriptor = attributeDescriptor(name);
        AttributeType declaredType = (AttributeType) descriptor.getType();
        if (!declaredType.equals(type)) {
            boolean argIsSubType = Types.isSuperType(type, declaredType);
            if (!argIsSubType) {
                /*
                 * commented out since we got community schemas where the required instance type is
                 * not a subtype of the declared one throw new
                 * IllegalArgumentException(type.getName() + " is not a subtype of " +
                 * declaredType.getName());
                 */
                LOGGER.fine("Adding attribute " + name + " of type " + type.getName()
                        + " which is not a subtype of " + declaredType.getName());
            }
            int minOccurs = descriptor.getMinOccurs();
            int maxOccurs = descriptor.getMaxOccurs();
            boolean nillable = descriptor.isNillable();
            // TODO: handle default value
            Object defaultValue = null;
            if (type instanceof GeometryType) {
                descriptor = new GeometryDescriptorImpl((GeometryType) type, name, minOccurs,
                        maxOccurs, nillable, defaultValue);
            } else {
                descriptor = new AttributeDescriptorImpl(type, name, minOccurs, maxOccurs,
                        nillable, defaultValue);
            }
        }
        Attribute attribute;
        if (descriptor != null && descriptor.getType() instanceof NonFeatureTypeProxy) {
            // we don't want a feature. NonFeatureTypeProxy is used to make non feature types
            // a fake feature type, so it can be created as top level feature in app-schema
            // mapping file. When created inside other features, it should be encoded as
            // complex features though.
            attribute = createComplexAttribute(value, null, descriptor, id);
        } else {
            attribute = create(value, null, descriptor, id);
        }
        properties().add(attribute);
        return attribute;
    }
}
