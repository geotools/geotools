/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;

/**
 * This is a workaround for shapefiles having a limit on attribute names at 10 chars length.
 *
 * @author Andrea Aime - GeoSolutions
 */
class ShapefileCompatibleFeature implements SimpleFeature {

    SimpleFeature delegate;

    public ShapefileCompatibleFeature(SimpleFeature delegate) {
        this.delegate = delegate;
    }

    @Override
    public void setAttribute(String name, Object value) {
        name = fixPropertyName(name);
        delegate.setAttribute(name, value);
    }

    private String fixPropertyName(String name) {
        if (name.length() > 10 && delegate.getFeatureType().getDescriptor(name) == null) {
            String reduced = name.substring(0, 10);
            if (delegate.getFeatureType().getDescriptor(name) == null) {
                return reduced;
            }
        }
        return name;
    }

    @Override
    public Object getAttribute(String name) {
        name = fixPropertyName(name);
        return delegate.getAttribute(name);
    }

    @Override
    public FeatureId getIdentifier() {
        return delegate.getIdentifier();
    }

    @Override
    public AttributeDescriptor getDescriptor() {
        return delegate.getDescriptor();
    }

    @Override
    public BoundingBox getBounds() {
        return delegate.getBounds();
    }

    @Override
    public String getID() {
        return delegate.getID();
    }

    @Override
    public SimpleFeatureType getType() {
        return delegate.getType();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return delegate.getFeatureType();
    }

    @Override
    public void setValue(Object newValue) {
        delegate.setValue(newValue);
    }

    @Override
    public List<Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public GeometryAttribute getDefaultGeometryProperty() {
        return delegate.getDefaultGeometryProperty();
    }

    @Override
    public void setValue(Collection<Property> values) {
        delegate.setValue(values);
    }

    @Override
    public void setAttributes(List<Object> values) {
        delegate.setAttributes(values);
    }

    @Override
    public void setDefaultGeometryProperty(GeometryAttribute geometryAttribute) {
        delegate.setDefaultGeometryProperty(geometryAttribute);
    }

    @Override
    public Collection<? extends Property> getValue() {
        return delegate.getValue();
    }

    @Override
    public Collection<Property> getProperties(Name name) {
        return delegate.getProperties(name);
    }

    @Override
    public void setAttributes(Object[] values) {
        delegate.setAttributes(values);
    }

    @Override
    public Name getName() {
        return delegate.getName();
    }

    @Override
    public Property getProperty(Name name) {
        return delegate.getProperty(name);
    }

    @Override
    public boolean isNillable() {
        return delegate.isNillable();
    }

    @Override
    public Map<Object, Object> getUserData() {
        return delegate.getUserData();
    }

    @Override
    public Collection<Property> getProperties(String name) {
        return delegate.getProperties(name);
    }

    @Override
    public Object getAttribute(Name name) {
        return delegate.getAttribute(name);
    }

    @Override
    public void setAttribute(Name name, Object value) {
        delegate.setAttribute(name, value);
    }

    @Override
    public Collection<Property> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public Property getProperty(String name) {
        return delegate.getProperty(name);
    }

    @Override
    public Object getAttribute(int index) throws IndexOutOfBoundsException {
        return delegate.getAttribute(index);
    }

    @Override
    public void setAttribute(int index, Object value) throws IndexOutOfBoundsException {
        delegate.setAttribute(index, value);
    }

    @Override
    public void validate() throws IllegalAttributeException {
        delegate.validate();
    }

    @Override
    public int getAttributeCount() {
        return delegate.getAttributeCount();
    }

    @Override
    public Object getDefaultGeometry() {
        return delegate.getDefaultGeometry();
    }

    @Override
    public void setDefaultGeometry(Object geometry) {
        delegate.setDefaultGeometry(geometry);
    }

    @Override
    public String toString() {
        return "ShapefileCompatibleFeature{" + "delegate=" + delegate + '}';
    }
}
