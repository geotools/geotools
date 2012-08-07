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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.complex.ComplexFeatureConstants;
import org.geotools.data.complex.config.NonFeatureTypeProxy;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.GeometryDescriptorImpl;
import org.opengis.feature.Association;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.Property;
import org.opengis.feature.type.AssociationDescriptor;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

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
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(AppSchemaAttributeBuilder.class.getPackage().getName());
    
    public AppSchemaAttributeBuilder(FeatureFactory attributeFactory) {
        super(attributeFactory);
    }

    //
    // Injection
    //
    // Used to inject dependencies we need during construction time.
    //
    /**
     * Returns the underlying attribute factory.
     */
    public FeatureFactory getFeatureFactory() {
        return attributeFactory;
    }

    /**
     * Sets the underlying attribute factory.
     */
    public void setFeatureFactory(FeatureFactory attributeFactory) {
        this.attributeFactory = attributeFactory;
    }

    //
    // State
    //
    /**
     * Initializes the state of the builder based on a previously built attribute.
     * <p>
     * This method is useful when copying another attribute.
     * </p>
     */
    public void init(Attribute attribute) {
        init();

        descriptor = attribute.getDescriptor();
        type = attribute.getType();

        if (attribute instanceof ComplexAttribute) {
            ComplexAttribute complex = (ComplexAttribute) attribute;
            Collection properties = (Collection) complex.getValue();
            for (Iterator itr = properties.iterator(); itr.hasNext();) {
                Property property = (Property) itr.next();
                if (property instanceof Attribute) {
                    Attribute att = (Attribute) property;
                    add(att.getIdentifier() == null ? null : att.getIdentifier().toString(), att
                            .getValue(), att.getName());
                } else if (property instanceof Association) {
                    Association assoc = (Association) property;
                    associate(assoc.getValue(), assoc.getName());
                }
            }
        }

        if (attribute instanceof Feature) {
            Feature feature = (Feature) attribute;

            if (feature.getDefaultGeometryProperty() != null) {
                if (crs == null) {
                    crs = feature.getDefaultGeometryProperty().getType()
                            .getCoordinateReferenceSystem();
                }

                defaultGeometry = feature.getDefaultGeometryProperty().getValue();
            }
        }

    }

    /**
     * This namespace will be used when constructing attribute names.
     */
    public void setNamespaceURI(String namespace) {
        this.namespace = namespace;
    }

    /**
     * This namespace will be used when constructing attribute names.
     * 
     * @return namespace will be used when constructing attribute names.
     */
    public String getNamespaceURI() {
        return namespace;
    }

    /**
     * Sets the type of the attribute being built.
     * <p>
     * When building a complex attribute, this type is used a reference to obtain the types of
     * contained attributes.
     * </p>
     */
    public void setType(AttributeType type) {
        this.type = type;
        this.descriptor = null;
    }

    /**
     * @return The type of the attribute being built.
     */
    public AttributeType getType() {
        return type;
    }

    //
    // Complex attribute specific methods
    //
    /**
     * Adds an attribute to the complex attribute being built. <br>
     * <p>
     * This method uses the result of {@link #getNamespaceURI()} to build a qualified attribute
     * name.
     * </p>
     * <p>
     * This method uses the type supplied in {@link #setType(AttributeType)} in order to determine
     * the attribute type.
     * </p>
     * 
     * @param name
     *                The name of the attribute.
     * @param value
     *                The value of the attribute.
     * 
     */
    public Attribute add(Object value, String name) {
        return add(null, value, name);
    }

    /**
     * Adds an association to the complex attribute being built. <br>
     * <p>
     * This method uses the result of {@link #getNamespaceURI()} to build a qualified attribute
     * name.
     * </p>
     * <p>
     * This method uses the type supplied in {@link #setType(AttributeType)} in order to determine
     * the association type.
     * </p>
     * 
     * @param value
     *                The value of the association, an attribute.
     * @param name
     *                The name of the association.
     */
    public void associate(Attribute value, String name) {
        associate(value, name, namespace);
    }

    /**
     * Adds an attribute to the complex attribute being built. <br>
     * <p>
     * This method uses the type supplied in {@link #setType(AttributeType)} in order to determine
     * the attribute type.
     * </p>
     * 
     * @param value
     *                The value of the attribute.
     * @param name
     *                The name of the attribute.
     * @param namespaceURI
     *                The namespace of the attribute.
     */
    public Attribute add(Object value, String name, String namespaceURI) {
        return add(null, value, name, namespaceURI);
    }

    /**
     * Adds an association to the complex attribute being built. <br>
     * <p>
     * This method uses the type supplied in {@link #setType(AttributeType)} in order to determine
     * the association type.
     * </p>
     * 
     * @param value
     *                The value of the association, an attribute.
     * @param name
     *                The name of the association.
     * @param namespaceURI
     *                The namespace of the association
     */
    public void associate(Attribute attribute, String name, String namespaceURI) {
        associate(attribute, Types.typeName(namespaceURI, name));
    }

    /**
     * Adds an attribute to the complex attribute being built, overriding the type of the declared
     * attribute descriptor by a subtype of it. <br>
     * <p>
     * This method uses the type supplied in {@link #setType(AttributeType)} in order to determine
     * the attribute type.
     * </p>
     * 
     * @param id
     *                the attribute id
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
        AttributeDescriptor descriptor = getAttributeDescriptorFor(name);
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
        getProperties().add(attribute);
        return attribute;
    }

    /**
     * Adds an attribute to the complex attribute being built. <br>
     * <p>
     * This method uses the type supplied in {@link #setType(AttributeType)} in order to determine
     * the attribute type.
     * </p>
     * 
     * @param name
     *                The name of the attribute.
     * @param value
     *                The value of the attribute.
     * 
     */
    public Attribute add(Object value, Name name) {
        return add(null, value, name);
    }

    /**
     * Adds an association to the complex attribute being built. <br>
     * <p>
     * This method uses the type supplied in {@link #setType(AttributeType)} in order to determine
     * the association type.
     * </p>
     * 
     * @param value
     *                The value of the association, an attribute.
     * @param name
     *                The name of the association.
     * @param namespaceURI
     *                The namespace of the association
     */
    public void associate(Attribute value, Name name) {
        AssociationDescriptor descriptor = associationDescriptor(name);
        Association association = attributeFactory.createAssociation(value, descriptor);

        getProperties().add(association);
    }

    /**
     * Adds an attribute to the complex attribute being built. <br>
     * <p>
     * The result of {@link #getNamespaceURI()} to build a qualified attribute name.
     * </p>
     * <p>
     * This method uses the type supplied in {@link #setType(AttributeType)} in order to determine
     * the attribute type.
     * </p>
     * 
     * @param id
     *                The id of the attribute.
     * @param name
     *                The name of the attribute.
     * @param value
     *                The value of the attribute.
     */
    public Attribute add(String id, Object value, String name) {
        return add(id, value, name, namespace);
    }

    /**
     * Adds an attribute to the complex attribute being built. <br>
     * <p>
     * This method uses the type supplied in {@link #setType(AttributeType)} in order to determine
     * the attribute type.
     * </p>
     * 
     * @param id
     *                The id of the attribute.
     * @param value
     *                The value of the attribute.
     * @param name
     *                The name of the attribute.
     * @param namespaceURI
     *                The namespace of the attribute.
     */
    public Attribute add(String id, Object value, String name, String namespaceURI) {
        return add(id, value, Types.typeName(namespaceURI, name));
    }

    protected AssociationDescriptor associationDescriptor(Name name) {
        PropertyDescriptor descriptor = Types.descriptor((ComplexType) type, name);

        if (descriptor == null) {
            String msg = "Could not locate association: " + name + " in type: " + type.getName();
            throw new IllegalArgumentException(msg);
        }

        if (!(descriptor instanceof AssociationDescriptor)) {
            String msg = name + " references a non association";
            throw new IllegalArgumentException(msg);
        }

        return (AssociationDescriptor) descriptor;
    }

    /**
     * Special case for any type. Skip validating existence in the schema, since anyType legally can
     * be casted into anything.
     * 
     * @param value
     *            the value to be set
     * @param type
     *            the type of the value
     * @param descriptor
     *            the attribute descriptor of anyType type
     * @param id
     * @return
     */
    public Attribute addAnyTypeValue(Object value, AttributeType type, 
            AttributeDescriptor descriptor, String id) {
        Attribute attribute = create(value, type, descriptor, id);
        getProperties().add(attribute);
        return attribute;
    }
    
    /**
     * Create a complex attribute for XS.AnyType, since it's defined as a simple type. We need a
     * complex attribute so we can set xlink:href in it.
     * 
     * @param value
     * @param descriptor
     * @param id
     * @return
     */
    public Attribute addComplexAnyTypeAttribute(Object value, AttributeDescriptor descriptor,
            String id) {
        // need to create a complex attribute for any type, so we can have client properties
        // for xlink:href and so we chain features etc.
        Map<Object, Object> userData = descriptor.getUserData();
        descriptor = new AttributeDescriptorImpl(ComplexFeatureConstants.ANYTYPE_TYPE, descriptor
                .getName(), descriptor.getMinOccurs(), descriptor.getMaxOccurs(), descriptor
                .isNillable(), descriptor.getDefaultValue());
        descriptor.getUserData().putAll(userData);
        return createComplexAttribute(value, ComplexFeatureConstants.ANYTYPE_TYPE, descriptor, id);
    }
}
