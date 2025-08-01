/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectortiles.store;

import io.tileverse.vectortile.model.VectorTile;
import io.tileverse.vectortile.model.VectorTile.Layer.Feature;
import java.util.AbstractList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.Name;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.filter.identity.Identifier;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.Types;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Converters;
import org.geotools.util.Utilities;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;

/**
 * {@link SimpleFeature} implementation directly backed by a vector tiles {@link Feature VectorTile.Layer.Feature}
 * without creating an additional {@code Object[]} to hold the values.
 */
class VectorTilesSimpleFeature implements SimpleFeature {

    private static final Geometry NULL_GEOM = new GeometryFactory().createEmpty(0);

    private final SimpleFeatureType featureType;
    private final VectorTile.Layer.Feature vtFeature;

    private Geometry defaultGeometry;
    private Map<Object, Object> featureUserData;
    private Map<String, Map<Object, Object>> attributesUserData;

    VectorTilesSimpleFeature(SimpleFeatureType featureType, VectorTile.Layer.Feature feature) {
        this.featureType = featureType;
        this.vtFeature = feature;
    }

    @Override
    public SimpleFeatureType getType() {
        return featureType;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    @Override
    public String getID() {
        return String.valueOf(vtFeature.getId());
    }

    @Override
    public FeatureId getIdentifier() {
        return new FeatureIdImpl(getID());
    }

    @Override
    public BoundingBox getBounds() {
        GeometryDescriptor gd = featureType.getGeometryDescriptor();
        if (gd == null) {
            return null;
        }
        Geometry geom = getDefaultGeometry();
        if (geom == null) {
            return null;
        }
        CoordinateReferenceSystem crs = gd.getCoordinateReferenceSystem();
        return new ReferencedEnvelope(geom.getEnvelopeInternal(), crs);
    }

    @Override
    public GeometryAttribute getDefaultGeometryProperty() {
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        if (geometryDescriptor != null) {
            return new GeometryAttributeImpl(vtFeature.getGeometry(), geometryDescriptor, null);
        }
        return null;
    }

    @Override
    public void setDefaultGeometryProperty(GeometryAttribute geometryAttribute) {
        setDefaultGeometry(geometryAttribute == null ? null : geometryAttribute.getValue());
    }

    @Override
    public void setDefaultGeometry(Object geometry) {
        if (geometry == null) {
            this.defaultGeometry = NULL_GEOM;
        } else {
            this.defaultGeometry = (Geometry) geometry;
        }
    }

    @Override
    public Geometry getDefaultGeometry() {
        if (defaultGeometry == NULL_GEOM) {
            return null;
        }
        if (defaultGeometry == null) {
            defaultGeometry = vtFeature.getGeometry();
        }
        return defaultGeometry;
    }

    @Override
    public void setValue(Object newValue) {
        @SuppressWarnings("unchecked")
        Collection<Property> converted = (Collection<Property>) newValue;
        setValue(converted);
    }

    @Override
    public void setValue(Collection<Property> values) {
        Map<String, Object> attributes = vtFeature.getAttributes();
        attributes.clear();
        values.forEach(prop -> setAttribute(prop.getName(), prop.getValue()));
    }

    @Override
    public Collection<? extends Property> getValue() {
        return getProperties();
    }

    @Override
    public Collection<Property> getProperties(Name name) {
        return getProperties(name.getLocalPart());
    }

    @Override
    public Collection<Property> getProperties(String name) {
        Property property = getProperty(name);
        return property == null ? List.of() : List.of(property);
    }

    @Override
    public Property getProperty(Name name) {
        return getProperty(name.getLocalPart());
    }

    @Override
    public Property getProperty(String name) {
        AttributeDescriptor descriptor = getType().getDescriptor(name);
        if (descriptor == null) {
            return null;
        }
        if (descriptor instanceof GeometryDescriptor geomd) {
            Object value = getAttribute(name);
            return new GeometryAttributeImpl(value, geomd, null);
        }
        return new Attribute(name);
    }

    @Override
    public Collection<Property> getProperties() {
        return new AttributeList();
    }

    @Override
    public void validate() throws IllegalAttributeException {
        for (int i = 0; i < getAttributeCount(); i++) {
            AttributeDescriptor descriptor = getType().getDescriptor(i);
            Types.validate(descriptor, getAttribute(descriptor.getLocalName()));
        }
    }

    @Override
    public AttributeDescriptor getDescriptor() {
        return new AttributeDescriptorImpl(featureType, featureType.getName(), 0, Integer.MAX_VALUE, true, null);
    }

    private Optional<AttributeDescriptor> getDescriptor(int index) {
        return Optional.ofNullable(getType().getDescriptor(index));
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
    public Map<Object, Object> getUserData() {
        if (this.featureUserData == null) {
            this.featureUserData = new HashMap<>();
        }
        return featureUserData;
    }

    @Override
    public void setAttributes(List<Object> values) {
        for (int i = 0; i < getAttributeCount(); i++) {
            AttributeDescriptor descriptor = featureType.getDescriptor(i);
            setAttribute(descriptor.getLocalName(), values.get(i));
        }
    }

    @Override
    public void setAttributes(Object[] values) {
        setAttributes(Arrays.asList(values));
    }

    @Override
    public List<Object> getAttributes() {
        return featureType.getDescriptors().stream()
                .map(PropertyDescriptor::getName)
                .map(this::getAttribute)
                .collect(Collectors.toList());
    }

    @Override
    public Object getAttribute(String name) {
        AttributeDescriptor descriptor = getType().getDescriptor(name);
        return getAttribute(descriptor);
    }

    @Override
    public Object getAttribute(int index) throws IndexOutOfBoundsException {
        AttributeDescriptor descriptor = getType().getDescriptor(index);
        return getAttribute(descriptor);
    }

    /**
     * Gets the attribute named after {@code descriptor}, converting it to the required binding if needed, to account
     * for potential type mismatch from the stored value in the vector tile and the declared attribute type in the layer
     * metadata (e.g. declared Double, parsed Long)
     */
    private Object getAttribute(AttributeDescriptor descriptor) {
        if (descriptor != null) {
            final String name = descriptor.getLocalName();
            final Class<?> binding = descriptor.getType().getBinding();
            Object value;
            if (descriptor instanceof GeometryDescriptor) {
                value = getDefaultGeometry();
            } else {
                value = vtFeature.getAttribute(name);
            }
            if (value != null && !binding.isInstance(value)) {
                value = Converters.convert(value, binding);
            }
            return value;
        }
        return null;
    }

    @Override
    public Object getAttribute(Name name) {
        return getAttribute(name.getLocalPart());
    }

    @Override
    public void setAttribute(String name, Object value) {
        GeometryDescriptor geomd = featureType.getGeometryDescriptor();
        if (geomd != null && geomd.getLocalName().equals(name)) {
            setDefaultGeometry(value);
        } else {
            vtFeature.getAttributes().put(name, value);
        }
    }

    @Override
    public void setAttribute(Name name, Object value) {
        setAttribute(name.getLocalPart(), value);
    }

    @Override
    public void setAttribute(int index, Object value) throws IndexOutOfBoundsException {
        getDescriptor(index).ifPresent(d -> setAttribute(d.getLocalName(), value));
    }

    @Override
    public int getAttributeCount() {
        return featureType.getAttributeCount();
    }

    /** Live collection backed directly on the value array */
    class AttributeList extends AbstractList<Property> {

        @Override
        public Property get(int index) {
            return getDescriptor(index)
                    .map(AttributeDescriptor::getLocalName)
                    .map(VectorTilesSimpleFeature.this::getProperty)
                    .orElse(null);
        }

        @Override
        public Attribute set(int index, Property element) {
            VectorTilesSimpleFeature.this.setAttribute(index, element.getValue());
            return null;
        }

        @Override
        public int size() {
            return VectorTilesSimpleFeature.this.getAttributeCount();
        }
    }
    /** Attribute that delegates directly to the value array */
    class Attribute implements org.geotools.api.feature.Attribute {
        String name;

        Attribute(String name) {
            this.name = name;
        }

        @Override
        public Identifier getIdentifier() {
            return null;
        }

        @Override
        public Name getName() {
            return getDescriptor().getName();
        }

        @Override
        public AttributeDescriptor getDescriptor() {
            return VectorTilesSimpleFeature.this.getType().getDescriptor(name);
        }

        @Override
        public AttributeType getType() {
            return VectorTilesSimpleFeature.this.getType().getType(name);
        }

        @Override
        @SuppressWarnings("unused")
        public Map<Object, Object> getUserData() {
            // lazily create the user data holder
            if (attributesUserData == null) {
                attributesUserData = new HashMap<>();
            }
            return attributesUserData.computeIfAbsent(name, n -> new HashMap<>());
        }

        @Override
        public Object getValue() {
            return VectorTilesSimpleFeature.this.getAttribute(name);
        }

        @Override
        public boolean isNillable() {
            return getDescriptor().isNillable();
        }

        @Override
        public void setValue(Object newValue) {
            VectorTilesSimpleFeature.this.setAttribute(name, newValue);
        }

        /**
         * Override of hashCode; uses descriptor name to agree with AttributeImpl
         *
         * @return hashCode for this object.
         */
        @Override
        public int hashCode() {
            return Objects.hash(getDescriptor(), getValue());
        }

        /**
         * Override of equals.
         *
         * @param obj the object to be tested for equality.
         * @return whether other is equal to this attribute Type.
         */
        @Override
        public boolean equals(Object obj) {
            return obj instanceof Attribute other
                    && (this == other
                            || (Utilities.equals(getDescriptor(), other.getDescriptor())
                                    && Utilities.deepEquals(getValue(), other.getValue())));
        }

        @Override
        public void validate() {
            Types.validate(getDescriptor(), getValue());
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder(getClass().getCanonicalName()).append(".Attribute: ");
            sb.append(getDescriptor().getName().getLocalPart());
            String id = getID();
            if (!getDescriptor()
                            .getName()
                            .getLocalPart()
                            .equals(getDescriptor().getType().getName().getLocalPart())
                    || id != null) {
                sb.append("<");
                sb.append(getDescriptor().getType().getName().getLocalPart());
                if (id != null) {
                    sb.append(" id=");
                    sb.append(id);
                }
                sb.append(">");
            }
            sb.append("=");
            sb.append(getValue());
            return sb.toString();
        }
    }
}
