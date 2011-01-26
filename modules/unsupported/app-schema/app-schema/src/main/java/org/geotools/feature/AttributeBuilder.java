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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
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
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AssociationDescriptor;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Builder for attributes.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 * 
 *
 * @source $URL$
 */
public class AttributeBuilder {
    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(AttributeBuilder.class.getPackage().getName());

    /**
     * Factory used to create attributes
     */
    FeatureFactory attributeFactory;

    /**
     * Namespace context.
     */
    String namespace;

    /**
     * Type of complex attribute being built. This field is mutually exclusive with
     * {@link #descriptor}
     */
    AttributeType type;

    /**
     * Descriptor of complex attribute being built. This field is mutually exclusive with
     * {@link #type}
     */
    AttributeDescriptor descriptor;

    /**
     * Contained properties (associations + attributes)
     */
    List properties;

    /**
     * The crs of the attribute.
     */
    CoordinateReferenceSystem crs;

    /**
     * Default geometry of the feature.
     */
    Object defaultGeometry;

    public AttributeBuilder(FeatureFactory attributeFactory) {
        this.attributeFactory = attributeFactory;
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
     * Initializes the builder to its initial state, the same state it is in directly after being
     * instantiated.
     */
    public void init() {
        descriptor = null;
        type = null;
        properties = null;
        crs = null;
        defaultGeometry = null;
    }

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
     * Sets the descriptor of the attribute being built.
     * <p>
     * When building a complex attribute, this type is used a reference to obtain the types of
     * contained attributes.
     * </p>
     */
    public void setDescriptor(AttributeDescriptor descriptor) {
        this.descriptor = descriptor;
        this.type = (AttributeType) descriptor.getType();
    }

    /**
     * @return The type of the attribute being built.
     */
    public AttributeType getType() {
        return type;
    }

    // Feature specific methods
    /**
     * Sets the coordinate reference system of the built feature.
     */
    public void setCRS(CoordinateReferenceSystem crs) {
        this.crs = crs;
    }

    /**
     * @return The coordinate reference system of the feature, or null if not set.
     */
    public CoordinateReferenceSystem getCRS() {
        return crs;
    }

    /**
     * Sets the default geometry of the feature.
     */
    public void setDefaultGeometry(Object defaultGeometry) {
        this.defaultGeometry = defaultGeometry;
    }

    /**
     * @return The default geometry of the feature.
     */
    public Object getDefaultGeometry() {
        return defaultGeometry;
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
        AttributeDescriptor descriptor = attributeDescriptor(name, type);
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

        properties().add(association);
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

    /**
     * Adds an attribute to the complex attribute being built. <br>
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
     * 
     */
    public Attribute add(String id, Object value, Name name) {
        AttributeDescriptor descriptor = attributeDescriptor(name);
        Attribute attribute = create(value, null, descriptor, id);
        properties().add(attribute);
        return attribute;
    }

    /**
     * Convenience accessor for properties list which does the null check.
     */
    protected List properties() {
        if (properties == null) {
            properties = new ArrayList();
        }

        return properties;
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

    protected AttributeDescriptor attributeDescriptor(Name name) {
        PropertyDescriptor descriptor = Types.descriptor((ComplexType) type, name);

        if (descriptor == null) {
            String msg = "Could not locate attribute: " + name + " in type: " + type.getName();
            throw new IllegalArgumentException(msg);
        }

        if (!(descriptor instanceof AttributeDescriptor)) {
            String msg = name + " references a non attribute";
            throw new IllegalArgumentException(msg);
        }

        return (AttributeDescriptor) descriptor;
    }

    protected AttributeDescriptor attributeDescriptor(Name name, AttributeType actualType) {
        PropertyDescriptor descriptor = Types.descriptor((ComplexType) type, name, actualType);

        if (descriptor == null) {
            String msg = "Could not locate attribute: " + name + " in type: " + type.getName();
            throw new IllegalArgumentException(msg);
        }

        if (!(descriptor instanceof AttributeDescriptor)) {
            String msg = name + " references a non attribute";
            throw new IllegalArgumentException(msg);
        }

        return (AttributeDescriptor) descriptor;
    }

    /**
     * Factors out attribute creation code, needs to be called with either one of type or descriptor
     * null.
     */
    protected Attribute create(Object value, AttributeType type, AttributeDescriptor descriptor,
            String id) {
        if (descriptor != null) {
            type = (AttributeType) descriptor.getType();
        }
        // if (type instanceof FeatureCollectionType) {
        // attribute = descriptor != null ? attributeFactory.createFeatureCollection(
        // (Collection) value, descriptor, id) : attributeFactory.createFeatureCollection(
        // (Collection) value, (FeatureCollectionType) type, id);
        // } else
        if (type instanceof FeatureType) {
            return descriptor != null ? attributeFactory.createFeature((Collection) value,
                    descriptor, id) : attributeFactory.createFeature((Collection) value,
                    (FeatureType) type, id);
        } else if (type instanceof ComplexType) {
            return createComplexAttribute((Collection) value, (ComplexType) type, descriptor, id);
        } else if (type instanceof GeometryType) {
            return attributeFactory.createGeometryAttribute(value, (GeometryDescriptor) descriptor,
                    id, getCRS());
        } else {
            return attributeFactory.createAttribute(value, descriptor, id);
        }
    }

    /**
     * Create complex attribute
     * 
     * @param value
     * @param type
     * @param descriptor
     * @param id
     * @return
     */
    public ComplexAttribute createComplexAttribute(Object value, ComplexType type,
            AttributeDescriptor descriptor, String id) {
        return descriptor != null ? attributeFactory.createComplexAttribute((Collection) value,
                descriptor, id) : attributeFactory.createComplexAttribute((Collection) value, type,
                id);
    }
    
    /**
     * Builds the attribute.
     * <p>
     * The class of the attribute built is determined from its type set with
     * {@link #setType(AttributeType)}.
     * </p>
     * 
     * @return The build attribute.
     */
    public Attribute build() {
        return build(null);
    }

    /**
     * Builds the attribute.
     * <p>
     * The class of the attribute built is determined from its type set with
     * {@link #setType(AttributeType)}.
     * </p>
     * 
     * @param id
     *                The id of the attribute, or null.
     * 
     * @return The build attribute.
     */
    public Attribute build(String id) {
        Attribute built = create(properties(), type, descriptor, id);

        // FIXME
        // // if geometry, set the crs
        // if (built instanceof GeometryAttribute) {
        // ((GeometryAttribute) built).getDescriptor().setCRS(getCRS());
        // }

        // if feature, set crs and default geometry
        if (built instanceof Feature) {
            Feature feature = (Feature) built;
            // FIXME feature.setCRS(getCRS());
            if (defaultGeometry != null) {
                for (Iterator itr = feature.getProperties().iterator(); itr.hasNext();) {
                    Attribute att = (Attribute) itr.next();
                    if (att instanceof GeometryAttribute) {
                        if (defaultGeometry.equals(att.getValue())) {
                            feature.setDefaultGeometryProperty((GeometryAttribute) att);
                        }
                    }

                }
            }
        }
        properties().clear();
        return built;
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
        properties().add(attribute);
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
