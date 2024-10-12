/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.api.feature.Association;
import org.geotools.api.feature.Attribute;
import org.geotools.api.feature.ComplexAttribute;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.FeatureFactory;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AssociationDescriptor;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.ComplexType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.referencing.crs.CRSFactory;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureImpl;

/**
 * Factory for creating instances of the Attribute family of classes.
 *
 * @author Ian Schneider
 * @author Gabriel Roldan
 * @author Justin Deoliveira
 * @version $Id$
 */
public abstract class AbstractFeatureFactoryImpl implements FeatureFactory {

    /** Factory used to create CRS objects */
    CRSFactory crsFactory;

    public FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

    /**
     * Whether the features to be built should be self validating on construction and value setting,
     * or not. But default, not, subclasses do override this value
     */
    boolean validating = false;

    public CRSFactory getCRSFactory() {
        return crsFactory;
    }

    public void setCRSFactory(CRSFactory crsFactory) {
        this.crsFactory = crsFactory;
    }

    @Override
    public Association createAssociation(Attribute related, AssociationDescriptor descriptor) {
        return new AssociationImpl(related, descriptor);
    }

    @Override
    public Attribute createAttribute(Object value, AttributeDescriptor descriptor, String id) {
        return new AttributeImpl(value, descriptor, id == null ? null : ff.gmlObjectId(id));
    }

    @Override
    public GeometryAttribute createGeometryAttribute(
            Object value, GeometryDescriptor descriptor, String id, CoordinateReferenceSystem crs) {

        return new GeometryAttributeImpl(value, descriptor, id == null ? null : ff.gmlObjectId(id));
    }

    @Override
    public ComplexAttribute createComplexAttribute(
            Collection<Property> value, AttributeDescriptor descriptor, String id) {
        return new ComplexAttributeImpl(value, descriptor, id == null ? null : ff.gmlObjectId(id));
    }

    @Override
    public ComplexAttribute createComplexAttribute(Collection<Property> value, ComplexType type, String id) {
        return new ComplexAttributeImpl(value, type, id == null ? null : ff.gmlObjectId(id));
    }

    @Override
    public Feature createFeature(Collection<Property> value, AttributeDescriptor descriptor, String id) {
        return new FeatureImpl(value, descriptor, ff.featureId(id));
    }

    @Override
    public Feature createFeature(Collection<Property> value, FeatureType type, String id) {
        return new FeatureImpl(value, type, ff.featureId(id));
    }

    @Override
    public SimpleFeature createSimpleFeature(Object[] array, SimpleFeatureType type, String id) {
        if (type.isAbstract()) {
            throw new IllegalArgumentException(
                    "Cannot create an feature of an abstract FeatureType " + type.getTypeName());
        }
        return new SimpleFeatureImpl(array, type, ff.featureId(id), validating);
    }

    @Override
    public SimpleFeature createSimpleFeautre(Object[] array, AttributeDescriptor descriptor, String id) {
        return createSimpleFeature(array, (SimpleFeatureType) descriptor, id);
    }
}
