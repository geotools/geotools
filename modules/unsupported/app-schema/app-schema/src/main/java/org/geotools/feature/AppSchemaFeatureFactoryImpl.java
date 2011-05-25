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

import org.geotools.feature.type.GeometryDescriptorImpl;
import org.geotools.feature.type.GeometryTypeImpl;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureFactory;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.GmlObjectId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * {@link FeatureFactory} that:
 * 
 * <ul>
 * <li>does not choke on null ids
 * <li>constructs containers for complex attributes with null values
 * </ul>
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.6
 */
public class AppSchemaFeatureFactoryImpl extends ValidatingFeatureFactoryImpl {

    /**
     * Create an attribute, even for null id.
     * 
     * @see org.geotools.feature.AbstractFeatureFactoryImpl#createAttribute(java.lang.Object,
     *      org.opengis.feature.type.AttributeDescriptor, java.lang.String)
     */
    @Override
    public Attribute createAttribute(Object value, AttributeDescriptor descriptor, String id) {
        return new AttributeImpl(value, descriptor, buildSafeGmlObjectId(id));
    }

    /**
     * Create a new geometry attribute, even for null id.
     * 
     * @see org.geotools.feature.AbstractFeatureFactoryImpl#createGeometryAttribute(java.lang.Object,
     *      org.opengis.feature.type.GeometryDescriptor, java.lang.String,
     *      org.opengis.referencing.crs.CoordinateReferenceSystem)
     */
    @Override
    public GeometryAttribute createGeometryAttribute(Object value, GeometryDescriptor descriptor,
            String id, CoordinateReferenceSystem crs) {
        if (crs != null && !(crs.equals(descriptor.getCoordinateReferenceSystem()))) {
            // update CRS
            GeometryType origType = (GeometryType) descriptor.getType();
            GeometryType geomType = new GeometryTypeImpl(origType.getName(), origType.getBinding(),
                    crs, origType.isIdentified(), origType.isAbstract(),
                    origType.getRestrictions(), origType.getSuper(), origType.getDescription());
            geomType.getUserData().putAll(origType.getUserData());

            descriptor = new GeometryDescriptorImpl(geomType, descriptor.getName(), descriptor
                    .getMinOccurs(), descriptor.getMaxOccurs(), descriptor.isNillable(),
                    ((GeometryDescriptor) descriptor).getDefaultValue());
            descriptor.getUserData().putAll(descriptor.getUserData());
        }
        return new GeometryAttributeImpl(value, descriptor, buildSafeGmlObjectId(id));
    }

    /**
     * Create a new complex attribute, even for null value or id.
     * 
     * @see org.geotools.feature.AbstractFeatureFactoryImpl#createComplexAttribute(java.util.Collection,
     *      org.opengis.feature.type.AttributeDescriptor, java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public ComplexAttribute createComplexAttribute(Collection value,
            AttributeDescriptor descriptor, String id) {
        return new ComplexAttributeImpl(buildCollectionIfNull(value), descriptor,
                buildSafeGmlObjectId(id));
    }

    /**
     * Create a new complex attribute, even for null value or id.
     * 
     * @see org.geotools.feature.AbstractFeatureFactoryImpl#createComplexAttribute(java.util.Collection,
     *      org.opengis.feature.type.ComplexType, java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public ComplexAttribute createComplexAttribute(Collection value, ComplexType type, String id) {
        return new ComplexAttributeImpl(buildCollectionIfNull(value), type,
                buildSafeGmlObjectId(id));
    }

    /**
     * Create a new feature, even for null value or id.
     * 
     * @see org.geotools.feature.AbstractFeatureFactoryImpl#createFeature(java.util.Collection,
     *      org.opengis.feature.type.AttributeDescriptor, java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Feature createFeature(Collection value, AttributeDescriptor descriptor, String id) {
        return new FeatureImpl(buildCollectionIfNull(value), descriptor, buildSafeFeatureId(id));
    }

    /**
     * Create a new feature, even for null value or id.
     * 
     * @see org.geotools.feature.AbstractFeatureFactoryImpl#createFeature(java.util.Collection,
     *      org.opengis.feature.type.FeatureType, java.lang.String)
     */
    @Override
    @SuppressWarnings("unchecked")
    public Feature createFeature(Collection value, FeatureType type, String id) {
        return new FeatureImpl(buildCollectionIfNull(value), type, buildSafeFeatureId(id));
    }

    /**
     * Construct a gml object id from a string, or return null if the string is null.
     * 
     * @param id
     * @return null if id is null
     */
    private GmlObjectId buildSafeGmlObjectId(String id) {
        if (id == null) {
            return null;
        } else {
            return ff.gmlObjectId(id);
        }
    }

    /**
     * Construct a feature id, or return null if the string is null.
     * 
     * @param id
     * @return null if id is null
     */
    private FeatureId buildSafeFeatureId(String id) {
        if (id == null) {
            return null;
        } else {
            return ff.featureId(id);
        }
    }

    /**
     * If the value collection is null, construct and return a new empty collection. If value
     * collection is not null, it is returned.
     * 
     * @param value
     * @return a non-null collection
     */
    private Collection<Property> buildCollectionIfNull(Collection<Property> value) {
        if (value == null) {
            return new ArrayList<Property>();
        } else {
            return value;
        }
    }

}
