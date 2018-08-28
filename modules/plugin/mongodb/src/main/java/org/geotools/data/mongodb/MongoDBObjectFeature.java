/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import com.mongodb.DBObject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class MongoDBObjectFeature implements SimpleFeature {

    private final SimpleFeatureType featureType;
    private final DBObject featureDBO;
    private final CollectionMapper mapper;

    private Map<Object, Object> userData;

    public MongoDBObjectFeature(
            DBObject dbo, SimpleFeatureType featureType, CollectionMapper mapper) {
        this.featureDBO = dbo;
        this.featureType = featureType;
        this.mapper = mapper;
    }

    public DBObject getObject() {
        return featureDBO;
    }

    @Override
    public SimpleFeatureType getType() {
        return featureType;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return getType();
    }

    @Override
    public FeatureId getIdentifier() {
        String id = getID();
        return id != null ? new FeatureIdImpl(id) : null;
    }

    @Override
    public String getID() {
        Object id = featureDBO.get("_id");
        return id != null ? id.toString() : null;
    }

    @Override
    public BoundingBox getBounds() {
        Object o = getDefaultGeometry();
        if (o instanceof Geometry) {
            CoordinateReferenceSystem crs = featureType.getCoordinateReferenceSystem();
            if (crs == null) {
                crs = DefaultGeographicCRS.WGS84;
            }
            Envelope bounds = ReferencedEnvelope.create(crs);
            bounds.init(JTS.bounds((Geometry) o, crs));
            return (BoundingBox) bounds;
        }
        return null;
    }

    @Override
    public Object getDefaultGeometry() {
        return mapper.getGeometry(featureDBO);
    }

    @Override
    public void setDefaultGeometry(Object geometry) {
        MongoUtil.setDBOValue(
                featureDBO, mapper.getGeometryPath(), mapper.toObject((Geometry) geometry));
    }

    @Override
    public Object getAttribute(Name name) {
        return doGetAttribute(featureType.getDescriptor(name));
    }

    @Override
    public void setAttribute(Name name, Object value) {
        doSetAttribute(featureType.getDescriptor(name), value);
    }

    @Override
    public Object getAttribute(String name) {
        return doGetAttribute(featureType.getDescriptor(name));
    }

    @Override
    public void setAttribute(String name, Object value) {
        doSetAttribute(featureType.getDescriptor(name), value);
    }

    @Override
    public Object getAttribute(int index) throws IndexOutOfBoundsException {
        return doGetAttribute(featureType.getDescriptor(index));
    }

    @Override
    public void setAttribute(int index, Object value) throws IndexOutOfBoundsException {
        doSetAttribute(featureType.getDescriptor(index), value);
    }

    private Object doGetAttribute(AttributeDescriptor d) throws IndexOutOfBoundsException {
        if (d instanceof GeometryDescriptor) {
            Object o = getDBOValue(mapper.getGeometryPath());
            return o instanceof DBObject ? mapper.getGeometry((DBObject) o) : null;
        }
        return getDBOValue(mapper.getPropertyPath(d.getLocalName()));
    }

    private void doSetAttribute(AttributeDescriptor d, Object o) {
        if (d instanceof GeometryDescriptor) {
            MongoUtil.setDBOValue(
                    featureDBO, mapper.getGeometryPath(), mapper.toObject((Geometry) o));
        } else {
            MongoUtil.setDBOValue(
                    featureDBO,
                    mapper.getPropertyPath(d.getLocalName()),
                    Converters.convert(o, d.getType().getBinding()));
        }
    }

    Object getDBOValue(String path) {
        return MongoUtil.getDBOValue(featureDBO, path);
    }

    @Override
    public int getAttributeCount() {
        return featureType.getAttributeCount();
    }

    @Override
    public List<Object> getAttributes() {
        int aCount = getAttributeCount();
        List<Object> values = new ArrayList<Object>(aCount);
        for (int aIndex = 0; aIndex < aCount; aIndex++) {
            values.add(getAttribute(aIndex));
        }
        return values;
    }

    @Override
    public void setAttributes(List<Object> values) {
        int index = 0;
        for (Object value : values) {
            setAttribute(index++, value);
        }
    }

    @Override
    public void setAttributes(Object[] values) {
        int index = 0;
        for (Object value : values) {
            setAttribute(index++, value);
        }
    }

    @Override
    public Map<Object, Object> getUserData() {
        if (userData == null) {
            userData = new HashMap<Object, Object>();
        }
        return userData;
    }

    @Override
    public GeometryAttribute getDefaultGeometryProperty() {
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        GeometryAttribute geometryAttribute = null;
        if (geometryDescriptor != null) {
            Object defaultGeometry = getDefaultGeometry();
            geometryAttribute =
                    new GeometryAttributeImpl(defaultGeometry, geometryDescriptor, null);
        }
        return geometryAttribute;
    }

    @Override
    public void setDefaultGeometryProperty(GeometryAttribute geometryAttribute) {
        if (geometryAttribute != null) setDefaultGeometry(geometryAttribute.getValue());
        else setDefaultGeometry(null);
    }

    @Override
    public Collection<Property> getProperties() {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Property> getProperties(Name name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<Property> getProperties(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Property getProperty(Name name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Property getProperty(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Collection<? extends Property> getValue() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setValue(Collection<Property> value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public AttributeDescriptor getDescriptor() {
        return new AttributeDescriptorImpl(
                featureType, featureType.getName(), 0, Integer.MAX_VALUE, true, null);
    }

    @Override
    public Name getName() {
        return featureType.getName();
    }

    @Override
    public boolean isNillable() {
        return true;
    }

    @Override
    public void setValue(Object value) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void validate() {}
}
