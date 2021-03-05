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
import java.util.List;
import java.util.Map;
import org.locationtech.jts.geom.Geometry;
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
 * Base class for feature decorators.
 *
 * <p>Subclasses should override those methods which are relevant to the decorator.
 *
 * @author Justin Deoliveira, The Open Planning Project
 * @since 2.5
 */
public class DecoratingFeature implements SimpleFeature {

    protected SimpleFeature delegate;

    public DecoratingFeature(SimpleFeature delegate) {
        this.delegate = delegate;
    }

    @Override
    public Object getAttribute(int index) {
        return delegate.getAttribute(index);
    }

    @Override
    public Object getAttribute(Name arg0) {
        return delegate.getAttribute(arg0);
    }

    @Override
    public Object getAttribute(String path) {
        return delegate.getAttribute(path);
    }

    @Override
    public int getAttributeCount() {
        return delegate.getAttributeCount();
    }

    @Override
    public List<Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public BoundingBox getBounds() {
        return delegate.getBounds();
    }

    @Override
    public Object getDefaultGeometry() {
        return delegate.getDefaultGeometry();
    }

    @Override
    public GeometryAttribute getDefaultGeometryProperty() {
        return delegate.getDefaultGeometryProperty();
    }

    @Override
    public AttributeDescriptor getDescriptor() {
        return delegate.getDescriptor();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return delegate.getFeatureType();
    }

    @Override
    public FeatureId getIdentifier() {
        return delegate.getIdentifier();
    }

    @Override
    public String getID() {
        return delegate.getID();
    }

    @Override
    public Name getName() {
        return delegate.getName();
    }

    @Override
    public Collection<Property> getProperties() {
        return delegate.getProperties();
    }

    @Override
    public Collection<Property> getProperties(Name arg0) {
        return delegate.getProperties(arg0);
    }

    @Override
    public Collection<Property> getProperties(String arg0) {
        return delegate.getProperties(arg0);
    }

    @Override
    public Property getProperty(Name arg0) {
        return delegate.getProperty(arg0);
    }

    @Override
    public Property getProperty(String arg0) {
        return delegate.getProperty(arg0);
    }

    @Override
    public SimpleFeatureType getType() {
        return delegate.getType();
    }

    @Override
    public Map<Object, Object> getUserData() {
        return delegate.getUserData();
    }

    @Override
    public Collection<? extends Property> getValue() {
        return delegate.getValue();
    }

    @Override
    public boolean isNillable() {
        return delegate.isNillable();
    }

    @Override
    public void setAttribute(int position, Object val) {
        delegate.setAttribute(position, val);
    }

    @Override
    public void setAttribute(Name arg0, Object arg1) {
        delegate.setAttribute(arg0, arg1);
    }

    @Override
    public void setAttribute(String path, Object attribute) {
        delegate.setAttribute(path, attribute);
    }

    @Override
    public void setAttributes(List<Object> arg0) {
        delegate.setAttributes(arg0);
    }

    @Override
    public void setAttributes(Object[] arg0) {
        delegate.setAttributes(arg0);
    }

    @Override
    public void setDefaultGeometry(Object arg0) {
        delegate.setDefaultGeometry(arg0);
    }

    @Override
    public void setDefaultGeometryProperty(GeometryAttribute arg0) {
        delegate.setDefaultGeometryProperty(arg0);
    }

    public void setDefaultGeometry(Geometry geometry) throws IllegalAttributeException {
        delegate.setDefaultGeometry(geometry);
    }

    @Override
    public void setValue(Collection<Property> arg0) {
        delegate.setValue(arg0);
    }

    @Override
    public void setValue(Object arg0) {
        delegate.setValue(arg0);
    }

    @Override
    public boolean equals(Object obj) {
        return delegate.equals(obj);
    }

    @Override
    public int hashCode() {
        return delegate.hashCode();
    }

    @Override
    public String toString() {
        return "<" + getClass().getCanonicalName() + ">" + delegate.toString();
    }

    @Override
    public void validate() {
        delegate.validate();
    }
}
