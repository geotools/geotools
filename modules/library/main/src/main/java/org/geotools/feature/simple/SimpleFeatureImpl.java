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
package org.geotools.feature.simple;

import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.api.filter.identity.Identifier;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.type.AttributeDescriptorImpl;
import org.geotools.feature.type.Types;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.geometry.jts.coordinatesequence.CoordinateSequences;
import org.geotools.util.Converters;
import org.geotools.util.SuppressFBWarnings;
import org.geotools.util.Utilities;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/**
 * An implementation of {@link SimpleFeature} geared towards speed and backed by an Object[].
 *
 * @author Justin
 * @author Andrea Aime
 */
public class SimpleFeatureImpl implements SimpleFeature {

    protected FeatureId id;
    protected SimpleFeatureType featureType;
    /** The actual values held by this feature */
    protected Object[] values;
    /** The attribute name -> position index */
    protected Map<String, Integer> index;
    /** The set of user data attached to the feature (lazily created) */
    protected Map<Object, Object> userData;
    /** The set of user data attached to each attribute (lazily created) */
    protected Map<Object, Object>[] attributeUserData;

    /** Whether this feature is self validating or not */
    protected boolean validating;

    /** Builds a new feature based on the provided values and feature type */
    public SimpleFeatureImpl(List<Object> values, SimpleFeatureType featureType, FeatureId id) {
        this(values.toArray(), featureType, id, false, index(featureType));
    }

    /**
     * Fast construction of a new feature.
     *
     * <p>The object takes ownership of the provided value array, do not modify after calling the constructor
     */
    public SimpleFeatureImpl(Object[] values, SimpleFeatureType featureType, FeatureId id, boolean validating) {
        this(values, featureType, id, validating, index(featureType));
    }

    /**
     * Fast construction of a new feature.
     *
     * <p>The object takes ownership of the provided value array, do not modify after calling the constructor
     *
     * @param index - attribute name to value index mapping
     */
    public SimpleFeatureImpl(
            Object[] values,
            SimpleFeatureType featureType,
            FeatureId id,
            boolean validating,
            Map<String, Integer> index) {
        this.id = id;
        this.featureType = featureType;
        this.values = values;
        this.validating = validating;
        this.index = index;

        // if we're self validating, do validation right now
        if (validating) validate();
    }

    /**
     * Generate (or lookup) an "index" mapping attribute to index for the provided FeatureType.
     *
     * <p>This method will use the following:
     *
     * <ul>
     *   <li>SimpleFeatureTypeImpl.index; or
     *   <li>Check getUserData().get("indexLookup");
     *   <li>or call {@link SimpleFeatureTypeImpl#buildIndex(SimpleFeatureType)} to generate the required index
     * </ul>
     *
     * @return mapping between attribute name to attribute index
     */
    @SuppressWarnings("unchecked")
    private static Map<String, Integer> index(SimpleFeatureType featureType) {
        // in the most common case reuse the map cached in the feature type
        if (featureType instanceof SimpleFeatureTypeImpl) {
            return ((SimpleFeatureTypeImpl) featureType).index;
        } else {
            synchronized (featureType) {
                // if we're not lucky, rebuild the index completely...
                Object cache = featureType.getUserData().get("indexLookup");
                if (cache instanceof Map) {
                    return (Map<String, Integer>) cache;
                } else {
                    Map<String, Integer> generatedIndex = SimpleFeatureTypeImpl.buildIndex(featureType);
                    featureType.getUserData().put("indexLookup", generatedIndex);
                    return generatedIndex;
                }
            }
        }
    }

    @Override
    public FeatureId getIdentifier() {
        return id;
    }

    @Override
    public String getID() {
        return id.getID();
    }

    public int getNumberOfAttributes() {
        return values.length;
    }

    @Override
    public Object getAttribute(int index) throws IndexOutOfBoundsException {
        return values[index];
    }

    @Override
    public Object getAttribute(String name) {
        Integer idx = index.get(name);
        if (idx != null) return getAttribute(idx);
        else return null;
    }

    @Override
    public Object getAttribute(Name name) {
        return getAttribute(name.getLocalPart());
    }

    @Override
    public int getAttributeCount() {
        return values.length;
    }

    @Override
    public List<Object> getAttributes() {
        return new ArrayList<>(Arrays.asList(values));
    }

    @Override
    public Object getDefaultGeometry() {
        // should be specified in the index as the default key (null)
        Integer idx = index.get(null);
        Object defaultGeometry = idx != null ? getAttribute(idx) : null;

        // not found? do we have a default geometry at all?
        if (defaultGeometry == null) {
            GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
            if (geometryDescriptor != null) {
                Integer defaultGeomIndex =
                        index.get(geometryDescriptor.getName().getLocalPart());
                defaultGeometry = getAttribute(defaultGeomIndex.intValue());
            }
        }

        return defaultGeometry;
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    @Override
    public SimpleFeatureType getType() {
        return featureType;
    }

    @Override
    public void setAttribute(int index, Object value) throws IndexOutOfBoundsException {
        // first do conversion
        Object converted = Converters.convert(
                value, getFeatureType().getDescriptor(index).getType().getBinding());
        // if necessary, validation too
        if (validating) Types.validate(featureType.getDescriptor(index), converted);
        // finally set the value into the feature
        values[index] = converted;
    }

    @Override
    public void setAttribute(String name, Object value) {
        final Integer idx = index.get(name);
        if (idx == null) throw new IllegalAttributeException("Unknown attribute " + name);
        setAttribute(idx.intValue(), value);
    }

    @Override
    public void setAttribute(Name name, Object value) {
        setAttribute(name.getLocalPart(), value);
    }

    @Override
    public void setAttributes(List<Object> values) {
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] = values.get(i);
        }
    }

    @Override
    public void setAttributes(Object[] values) {
        setAttributes(Arrays.asList(values));
    }

    @Override
    public void setDefaultGeometry(Object geometry) {
        Integer geometryIndex = index.get(null);
        if (geometryIndex != null) {
            setAttribute(geometryIndex, geometry);
        }
    }

    @Override
    public BoundingBox getBounds() {
        // TODO: cache this value
        CoordinateReferenceSystem crs = featureType.getCoordinateReferenceSystem();
        Envelope bounds = ReferencedEnvelope.create(crs);

        for (Object o : values) {
            if (o instanceof Geometry) {
                Geometry g = (Geometry) o;
                // TODO: check userData for crs... and ensure its of the same
                // crs as the feature type
                if (bounds.isNull()) {
                    bounds.init(JTS.bounds(g, crs));
                } else {
                    bounds.expandToInclude(JTS.bounds(g, crs));
                }
            }
        }

        return (BoundingBox) bounds;
    }

    @Override
    public GeometryAttribute getDefaultGeometryProperty() {
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        GeometryAttribute geometryAttribute = null;
        if (geometryDescriptor != null) {
            Object defaultGeometry = getDefaultGeometry();
            geometryAttribute = new GeometryAttributeImpl(defaultGeometry, geometryDescriptor, null);
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
        return new AttributeList();
    }

    @Override
    public Collection<Property> getProperties(Name name) {
        return getProperties(name.getLocalPart());
    }

    @Override
    public Collection<Property> getProperties(String name) {
        final Integer idx = index.get(name);
        if (idx != null) {
            // cast temporarily to a plain collection to avoid type problems with generics
            Collection<Property> c = Collections.singleton(new Attribute(idx));
            return c;
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public Property getProperty(Name name) {
        return getProperty(name.getLocalPart());
    }

    @Override
    public Property getProperty(String name) {
        final Integer idx = index.get(name);
        if (idx == null) {
            return null;
        } else {
            int index = idx.intValue();
            AttributeDescriptor descriptor = featureType.getDescriptor(index);
            if (descriptor instanceof GeometryDescriptor) {
                return new GeometryAttributeImpl(values[index], (GeometryDescriptor) descriptor, null);
            } else {
                return new Attribute(index);
            }
        }
    }

    @Override
    public Collection<? extends Property> getValue() {
        return getProperties();
    }

    @Override
    public void setValue(Collection<Property> values) {
        int i = 0;
        for (Property p : values) {
            this.values[i++] = p.getValue();
        }
    }

    @Override
    public void setValue(Object newValue) {
        @SuppressWarnings("unchecked")
        Collection<Property> converted = (Collection<Property>) newValue;
        setValue(converted);
    }

    /** @see org.geotools.api.feature.Attribute#getDescriptor() */
    @Override
    public AttributeDescriptor getDescriptor() {
        return new AttributeDescriptorImpl(featureType, featureType.getName(), 0, Integer.MAX_VALUE, true, null);
    }

    /**
     * @return same name than this feature's {@link SimpleFeatureType}
     * @see org.geotools.api.feature.Property#getName()
     */
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
        if (userData == null) userData = new HashMap<>();
        return userData;
    }

    @Override
    public boolean hasUserData() {
        return userData != null && !userData.isEmpty();
    }

    /**
     * returns a unique code for this feature
     *
     * @return A unique int
     */
    @Override
    public int hashCode() {
        return id.hashCode() * featureType.hashCode();
    }

    /**
     * override of equals. Returns if the passed in object is equal to this.
     *
     * @param obj the Object to test for equality.
     * @return <code>true</code> if the object is equal, <code>false</code> otherwise.
     */
    @Override
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (obj == this) {
            return true;
        }

        if (!(obj instanceof SimpleFeatureImpl)) {
            return false;
        }

        SimpleFeatureImpl feat = (SimpleFeatureImpl) obj;

        // this check shouldn't exist, by contract,
        // all features should have an ID.
        if (id == null) {
            if (feat.getIdentifier() != null) {
                return false;
            }
        }

        if (!id.equals(feat.getIdentifier())) {
            return false;
        }

        if (!feat.getFeatureType().equals(featureType)) {
            return false;
        }

        for (int i = 0, ii = values.length; i < ii; i++) {
            Object otherAtt = feat.getAttribute(i);

            if (values[i] == null) {
                if (otherAtt != null) {
                    return false;
                }
            } else {
                if (values[i] instanceof Geometry) {
                    if (!(otherAtt instanceof Geometry)) {
                        return false;
                    } else if (!CoordinateSequences.equalsND((Geometry) values[i], (Geometry) otherAtt)) {
                        return false;
                    }
                } else if (!values[i].equals(otherAtt)) {
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public void validate() {
        for (int i = 0; i < values.length; i++) {
            AttributeDescriptor descriptor = getType().getDescriptor(i);
            Types.validate(descriptor, values[i]);
        }
    }

    /** Live collection backed directly on the value array */
    class AttributeList extends AbstractList<Property> {

        @Override
        public Property get(int index) {
            AttributeDescriptor descriptor = featureType.getDescriptor(index);
            if (descriptor instanceof GeometryDescriptor) {
                return new SimpleGeometryAttribute(index);
            }
            return new Attribute(index);
        }

        @Override
        public Attribute set(int index, Property element) {
            values[index] = element.getValue();
            return null;
        }

        @Override
        public int size() {
            return values.length;
        }
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer("SimpleFeatureImpl:");
        sb.append(getType().getName().getLocalPart());
        sb.append("=");
        sb.append(getValue());
        return sb.toString();
    }

    /** Attribute that delegates directly to the value array */
    class Attribute implements org.geotools.api.feature.Attribute {
        int index;

        Attribute(int index) {
            this.index = index;
        }

        @Override
        public Identifier getIdentifier() {
            return null;
        }

        @Override
        public AttributeDescriptor getDescriptor() {
            return featureType.getDescriptor(index);
        }

        @Override
        public AttributeType getType() {
            return featureType.getType(index);
        }

        @Override
        public Name getName() {
            return getDescriptor().getName();
        }

        @Override
        @SuppressWarnings("unchecked")
        public Map<Object, Object> getUserData() {
            // lazily create the user data holder

            if (attributeUserData == null) attributeUserData = new HashMap[values.length];
            // lazily create the attribute user data
            if (attributeUserData[index] == null) attributeUserData[index] = new HashMap<>();
            return attributeUserData[index];
        }

        @Override
        public Object getValue() {
            return values[index];
        }

        @Override
        public boolean isNillable() {
            return getDescriptor().isNillable();
        }

        @Override
        public void setValue(Object newValue) {
            values[index] = newValue;
        }
        /**
         * Override of hashCode; uses descriptor name to agree with AttributeImpl
         *
         * @return hashCode for this object.
         */
        @Override
        public int hashCode() {
            return 37 * getDescriptor().hashCode()
                    + 37 * (getValue() == null ? 0 : getValue().hashCode());
        }

        /**
         * Override of equals.
         *
         * @param obj the object to be tested for equality.
         * @return whether other is equal to this attribute Type.
         */
        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof Attribute)) {
                return false;
            }
            Attribute other = (Attribute) obj;
            if (!Utilities.equals(getDescriptor(), other.getDescriptor())) {
                return false;
            }
            if (!Utilities.deepEquals(getValue(), other.getValue())) {
                return false;
            }
            return Utilities.equals(getIdentifier(), other.getIdentifier());
        }

        @Override
        public void validate() {
            Types.validate(getDescriptor(), values[index]);
        }

        @Override
        public String toString() {
            StringBuffer sb = new StringBuffer("SimpleFeatureImpl.Attribute: ");
            sb.append(getDescriptor().getName().getLocalPart());
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
            sb.append(values[index]);
            return sb.toString();
        }
    }

    class SimpleGeometryAttribute extends Attribute implements GeometryAttribute {

        SimpleGeometryAttribute(int index) {
            super(index);
        }

        @Override
        public GeometryType getType() {
            return (GeometryType) super.getType();
        }

        @Override
        public GeometryDescriptor getDescriptor() {
            return (GeometryDescriptor) super.getDescriptor();
        }

        @Override
        public BoundingBox getBounds() {
            ReferencedEnvelope bounds = new ReferencedEnvelope(featureType.getCoordinateReferenceSystem());
            Object value = getAttribute(index);
            if (value instanceof Geometry) {
                bounds.init(((Geometry) value).getEnvelopeInternal());
            }
            return bounds;
        }

        @Override
        public void setBounds(BoundingBox bounds) {
            // do nothing, this property is strictly derived. Shall throw unsupported operation
            // exception?
        }

        @Override
        public int hashCode() {
            return 17 * super.hashCode();
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) {
                return true;
            }

            if (!(obj instanceof SimpleGeometryAttribute)) {
                return false;
            }
            return super.equals(obj);
        }
    }
}
