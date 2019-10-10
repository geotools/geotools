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
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AssociationDescriptor;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Factory for creation of attributes, associations, and features.
 *
 * <p>Implementations of this interface should not contain any "special logic" for creating
 * attributes and features. Method implementations should be straight through calls to a
 * constructor.
 *
 * @author Gabriel Roldan (Axios Engineering)
 * @author Justin Deoliveira (The Open Planning Project)
 * @since 2.2
 */
public interface FeatureFactory {

    /**
     * Creates an association.
     *
     * @param value The value of the association, an attribute.
     * @param descriptor The association descriptor.
     */
    Association createAssociation(Attribute value, AssociationDescriptor descriptor);

    /**
     * Creates an attribute.
     *
     * @param value The value of the attribute, may be <code>null</code>.
     * @param descriptor The attribute descriptor.
     * @param id The id of the attribute, may be <code>null</code>.
     */
    Attribute createAttribute(Object value, AttributeDescriptor descriptor, String id);

    /**
     * Creates a geometry attribute.
     *
     * <p><code>descriptor.getType()</code> must be an instance of {@link GeometryType}.
     *
     * @param geometry The value of the attribute, may be <code>null</code>.
     * @param descriptor The attribute descriptor.
     * @param id The id of the attribute, may be <code>null</code>.
     * @param crs The coordinate reference system of the attribute, may be <code>null</code>.
     */
    GeometryAttribute createGeometryAttribute(
            Object geometry,
            GeometryDescriptor descriptor,
            String id,
            CoordinateReferenceSystem crs);

    /**
     * Creates a complex attribute.
     *
     * <p><code>descriptor.getType()</code> must be an instance of {@link ComplexType}.
     *
     * @param value The value of the attribute, a collection of properties.
     * @param descriptor The attribute descriptor.
     * @param id The id of the attribute, may be <code>null</code>.
     */
    ComplexAttribute createComplexAttribute(
            Collection<Property> value, AttributeDescriptor descriptor, String id);

    /**
     * Creates a complex attribute.
     *
     * @param value The value of the attribute, a collection of properties.
     * @param type The type of the attribute.
     * @param id The id of the attribute, may be <code>null</code>.
     */
    ComplexAttribute createComplexAttribute(
            Collection<Property> value, ComplexType type, String id);

    /**
     * Creates a feature.
     *
     * <p><code>descriptor.getType()</code> must be an instance of {@link FeatureType}.
     *
     * @param value The value of the feature, a collection of properties.
     * @param descriptor The attribute descriptor.
     * @param id The id of the feature.
     */
    Feature createFeature(Collection<Property> value, AttributeDescriptor descriptor, String id);

    /**
     * Creates a feature.
     *
     * @param value The value of the feature, a collection of properties.
     * @param type The type of the feature.
     * @param id The id of the feature.
     */
    Feature createFeature(Collection<Property> value, FeatureType type, String id);

    /**
     * Create a SimpleFeature from an array of objects.
     *
     * <p>Please note that the provided array may be used directly by an implementation.
     *
     * @param array Object array of values; this array may beused directly.
     * @param type The type of the simple feature.
     * @param id The id of the feature.
     */
    SimpleFeature createSimpleFeature(Object[] array, SimpleFeatureType type, String id);

    /**
     * Creates a simple feature.
     *
     * <p><code>descriptor.getType()</code> must be an instance of {@link SimpleFeatureType}.
     *
     * @param array Object array of values; this array may be used directly.
     * @param descriptor The attribute descriptor.
     * @param id The id of the feature.
     */
    SimpleFeature createSimpleFeautre(Object[] array, AttributeDescriptor descriptor, String id);
}
