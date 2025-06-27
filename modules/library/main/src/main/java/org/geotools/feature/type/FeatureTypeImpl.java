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
package org.geotools.feature.type;

import java.util.Collection;
import java.util.List;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.FeatureType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.Filter;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.util.InternationalString;
import org.geotools.util.Utilities;

/**
 * Base implementation of FeatureType.
 *
 * @author gabriel
 */
public class FeatureTypeImpl extends ComplexTypeImpl implements FeatureType {

    private GeometryDescriptor defaultGeometry;
    private CoordinateReferenceSystem crs;

    public FeatureTypeImpl(
            Name name,
            Collection<PropertyDescriptor> schema,
            GeometryDescriptor defaultGeometry,
            boolean isAbstract,
            List<Filter> restrictions,
            AttributeType superType,
            InternationalString description) {
        super(name, schema, true, isAbstract, restrictions, superType, description);
        this.defaultGeometry = defaultGeometry;

        if (defaultGeometry != null && defaultGeometry.getType() == null) {
            throw new IllegalArgumentException("defaultGeometry must have a GeometryType");
        }
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        if (crs == null) {
            if (getGeometryDescriptor() != null
                    && getGeometryDescriptor().getType().getCoordinateReferenceSystem() != null) {
                crs = defaultGeometry.getType().getCoordinateReferenceSystem();
            }
            if (crs == null) {
                for (PropertyDescriptor property : getDescriptors()) {
                    if (property instanceof GeometryDescriptor) {
                        GeometryDescriptor geometry = (GeometryDescriptor) property;
                        if (geometry.getType().getCoordinateReferenceSystem() != null) {
                            crs = geometry.getType().getCoordinateReferenceSystem();
                            break;
                        }
                    }
                }
            }
        }

        return crs;
    }

    @Override
    public GeometryDescriptor getGeometryDescriptor() {
        if (defaultGeometry == null) {
            for (PropertyDescriptor property : getDescriptors()) {
                if (property instanceof GeometryDescriptor) {
                    defaultGeometry = (GeometryDescriptor) property;
                    break;
                }
            }
        }
        return defaultGeometry;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!super.equals(o)) {
            return false;
        }
        if (getClass() != o.getClass()) {
            return false;
        }
        FeatureType other = (FeatureType) o;
        if (!Utilities.equals(getGeometryDescriptor(), other.getGeometryDescriptor())) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hashCode = super.hashCode();

        if (defaultGeometry != null) {
            hashCode = hashCode ^ defaultGeometry.hashCode();
        }

        return hashCode;
    }
}
