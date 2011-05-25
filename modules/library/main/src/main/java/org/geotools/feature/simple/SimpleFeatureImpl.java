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

import org.geotools.feature.AttributeImpl;
import org.geotools.feature.GeometryAttributeImpl;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.PropertyImpl;
import org.geotools.feature.type.Types;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.Converters;
import org.geotools.util.Utilities;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.identity.Identifier;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Geometry;

/**
 * An implementation of {@link SimpleFeature} geared towards speed and backed by an Object[].
 * 
 * @author Justin
 * @author Andrea Aime
 *
 *
 * @source $URL$
 */
public class SimpleFeatureImpl implements SimpleFeature {
    
    protected FeatureId id;
    protected SimpleFeatureType featureType;
    /**
     * The actual values held by this feature
     */
    protected Object[] values;
    /**
     * The attribute name -> position index
     */
    protected Map<String,Integer> index;
    /**
     * The set of user data attached to the feature (lazily created)
     */
    protected Map<Object, Object> userData;
    /**
     * The set of user data attached to each attribute (lazily created)
     */
    protected Map<Object, Object>[] attributeUserData;
    
    /**
     * Whether this feature is self validating or not
     */
    protected  boolean validating;
    
    /**
     * Builds a new feature based on the provided values and feature type
     * @param values
     * @param featureType
     * @param id
     */
    public SimpleFeatureImpl( List<Object> values, SimpleFeatureType featureType, FeatureId id) {
        this(values.toArray(), featureType, id, false);
    }
    
    /**
     * Fast construction of a new feature. The object takes owneship of the provided value array,
     * do not modify after calling the constructor
     * @param values
     * @param featureType
     * @param id
     * @param validating
     */
    public SimpleFeatureImpl(Object[] values, SimpleFeatureType featureType, FeatureId id, boolean validating) {
        this.id = id;
        this.featureType = featureType;
        this.values = values;
        this.validating = validating;
        
        // in the most common case reuse the map cached in the feature type
        if(featureType instanceof SimpleFeatureTypeImpl) {
            index = ((SimpleFeatureTypeImpl) featureType).index;
        } else {
            // if we're not lucky, rebuild the index completely... 
            // TODO: create a separate cache for this case?
            this.index = SimpleFeatureTypeImpl.buildIndex(featureType);
        }
        
        // if we're self validating, do validation right now
        if(validating)
            validate();
    }
    
    public FeatureId getIdentifier() {
        return id;
    }
    public String getID() {
    	return id.getID();
    }
    
    public int getNumberOfAttributes() {
        return values.length;
    }
    
    public Object getAttribute(int index) throws IndexOutOfBoundsException {
        return values[ index ];
    }
    
    public Object getAttribute(String name) {
        Integer idx = index.get(name);
        if(idx != null)
            return getAttribute(idx);
        else
            return null;
    }

    public Object getAttribute(Name name) {
        return getAttribute( name.getLocalPart() );
    }

    public int getAttributeCount() {
        return values.length;
    }

    public List<Object> getAttributes() {
        return new ArrayList(Arrays.asList( values ));
    }

    public Object getDefaultGeometry() {
        // should be specified in the index as the default key (null)
        Integer idx = index.get(null);
        Object defaultGeometry = idx != null ? getAttribute( idx ) : null;
           
       // not found? do we have a default geometry at all?
       if(defaultGeometry == null){
           GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
           if(geometryDescriptor != null){
               Integer defaultGeomIndex = index.get(geometryDescriptor.getName().getLocalPart());
               defaultGeometry = getAttribute(defaultGeomIndex.intValue());
           }
       }
    
       return defaultGeometry;
    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    public SimpleFeatureType getType() {
        return featureType;
    }

    public void setAttribute(int index, Object value)
        throws IndexOutOfBoundsException {
        // first do conversion
        Object converted = Converters.convert(value, getFeatureType().getDescriptor(index).getType().getBinding());
        // if necessary, validation too
        if(validating)
            Types.validate(featureType.getDescriptor(index), converted);
        // finally set the value into the feature
        values[index] = converted;
    }
    
    public void setAttribute(String name, Object value) {
        final Integer idx = index.get(name);
        if(idx == null)
            throw new IllegalAttributeException("Unknown attribute " + name);
        setAttribute( idx.intValue(), value );
    }

    public void setAttribute(Name name, Object value) {
        setAttribute( name.getLocalPart(), value );
    }

    public void setAttributes(List<Object> values) {
        for (int i = 0; i < this.values.length; i++) {
            this.values[i] = values.get(i);
        }
    }

    public void setAttributes(Object[] values) {
        setAttributes( Arrays.asList( values ) );
    }

    public void setDefaultGeometry(Object geometry) {
        Integer geometryIndex = index.get( null );
        if ( geometryIndex != null ) {
            setAttribute( geometryIndex, geometry );
        }
    }

    public BoundingBox getBounds() {
        //TODO: cache this value
        ReferencedEnvelope bounds = new ReferencedEnvelope( featureType.getCoordinateReferenceSystem() );
        for ( Object o : values ) {
            if ( o instanceof Geometry ) {
                Geometry g = (Geometry) o;
                //TODO: check userData for crs... and ensure its of the same 
                // crs as the feature type
                if ( bounds.isNull() ) {
                    bounds.init(g.getEnvelopeInternal());
                }
                else {
                    bounds.expandToInclude(g.getEnvelopeInternal());
                }
            }
        }
        
        return bounds;
    }

    public GeometryAttribute getDefaultGeometryProperty() {
        GeometryDescriptor geometryDescriptor = featureType.getGeometryDescriptor();
        GeometryAttribute geometryAttribute = null;
        if(geometryDescriptor != null){
            Object defaultGeometry = getDefaultGeometry();
            geometryAttribute = new GeometryAttributeImpl(defaultGeometry, geometryDescriptor, null);            
        }
        return geometryAttribute;
    }

    public void setDefaultGeometryProperty(GeometryAttribute geometryAttribute) {
        if(geometryAttribute != null)
            setDefaultGeometry(geometryAttribute.getValue());
        else
            setDefaultGeometry(null);
    }

    public Collection<Property> getProperties() {
        return new AttributeList();
    }

    public Collection<Property> getProperties(Name name) {
        return getProperties( name.getLocalPart() );
    }

    public Collection<Property> getProperties(String name) {
        final Integer idx = index.get(name);
        if(idx != null) {
            // cast temporarily to a plain collection to avoid type problems with generics
            Collection c = Collections.singleton( new Attribute( idx ) );
            return c;
        } else {
            return Collections.emptyList();
        }
    }

    public Property getProperty(Name name) {
        return getProperty( name.getLocalPart() );
    }

    public Property getProperty(String name) {
        final Integer idx = index.get(name);
        if(idx == null){
            return null;
        } else {
            int index = idx.intValue();
            AttributeDescriptor descriptor = featureType.getDescriptor(index);
            if(descriptor instanceof GeometryDescriptor){
                return new GeometryAttributeImpl(values[index], (GeometryDescriptor) descriptor, null); 
            }else{
                return new Attribute( index );
            }
        }
    }

    public Collection<? extends Property> getValue() {
        return getProperties();
    }

    public void setValue(Collection<Property> values) {
        int i = 0;
        for ( Property p : values ) {
            this.values[i] = p.getValue();
        }
    }

    public void setValue(Object newValue) {
        setValue( (Collection<Property>) newValue );
    }
    
    public AttributeDescriptor getDescriptor() {
        return null;
    }

    public Name getName() {
        return null;
    }

    public boolean isNillable() {
        return true;
    }

    public Map<Object, Object> getUserData() {
        if(userData == null)
            userData = new HashMap<Object, Object>();
        return userData;
    }
    
    /**
     * returns a unique code for this feature
     *
     * @return A unique int
     */
    public int hashCode() {
        return id.hashCode() * featureType.hashCode();
    }

    /**
     * override of equals.  Returns if the passed in object is equal to this.
     *
     * @param obj the Object to test for equality.
     *
     * @return <code>true</code> if the object is equal, <code>false</code>
     *         otherwise.
     */
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
        //all features should have an ID.
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
                if (!values[i].equals(otherAtt)) {
                    if (values[i] instanceof Geometry
                            && otherAtt instanceof Geometry) {
                        // we need to special case Geometry
                        // as JTS is broken Geometry.equals( Object ) 
                        // and Geometry.equals( Geometry ) are different 
                        // (We should fold this knowledge into AttributeType...)
                        if (!((Geometry) values[i]).equals(
                                    (Geometry) otherAtt)) {
                            return false;
                        }
                    } else {
                        return false;
                    }
                }
            }
        }

        return true;
    }
    
    public void validate() {
        for (int i = 0; i < values.length; i++) {
            AttributeDescriptor descriptor = getType().getDescriptor(i);
            Types.validate(descriptor, values[i]);
        }
    }

    /**
     * Live collection backed directly on the value array
     */
    class AttributeList extends AbstractList<Property> {

        public Attribute get(int index) {
            return new Attribute( index );
        }
        
        public Attribute set(int index, Property element) {
            values[index] =  element.getValue();
            return null;
        }
        
        public int size() {
            return values.length;
        }
    }
    
    public String toString() {
        StringBuffer sb = new StringBuffer("SimpleFeatureImpl:");
        sb.append( getType().getName().getLocalPart());
        sb.append("=");
        sb.append( getValue() );
        return sb.toString();
    }
    

    /**
     * Attribute that delegates directly to the value array
     */
    class Attribute implements org.opengis.feature.Attribute {
        int index;
        
        Attribute( int index ) {
            this.index = index;
        }
        
        public Identifier getIdentifier() {
            return null;
        }

        public AttributeDescriptor getDescriptor() {
            return featureType.getDescriptor(index);
        }

        public AttributeType getType() {
            return featureType.getType(index);
        }

        public Name getName() {
            return getDescriptor().getName();
        }

        public Map<Object, Object> getUserData() {
            // lazily create the user data holder
            if(attributeUserData == null)
                attributeUserData = new HashMap[values.length];
            // lazily create the attribute user data
            if(attributeUserData[index] == null)
                attributeUserData[index] = new HashMap<Object, Object>();
            return attributeUserData[index];
        }

        public Object getValue() {
            return values[index];
        }

        public boolean isNillable() {
            return getDescriptor().isNillable();
        }

        public void setValue(Object newValue) {
            values[index] = newValue;
        }
        /**
         * Override of hashCode; uses descriptor name to agree with AttributeImpl
         * 
         * @return hashCode for this object.
         */
        public int hashCode() {
            return 37 * getDescriptor().hashCode()
                    + (37 * (getValue() == null ? 0 : getValue().hashCode()));
        }

        /**
         * Override of equals.
         * 
         * @param other
         *            the object to be tested for equality.
         * 
         * @return whether other is equal to this attribute Type.
         */
        public boolean equals(Object obj) {
            if ( this == obj ) {
                return true;
            }
            
            if (!(obj instanceof Attribute)) {
                return false;
            }
            Attribute other = (Attribute) obj;
            if (!Utilities.equals(getDescriptor(), other.getDescriptor())){
                return false;
            }
            if (!Utilities.deepEquals(getValue(), other.getValue())){
                return false;   
            }
            return Utilities.equals( getIdentifier(), other.getIdentifier());
        }
        
        public void validate() {
            Types.validate(getDescriptor(), values[index]);
        }

        public String toString() {
            StringBuffer sb = new StringBuffer("SimpleFeatureImpl.Attribute: ");
            sb.append(getDescriptor().getName().getLocalPart());
            if (!getDescriptor().getName().getLocalPart().equals(
                    getDescriptor().getType().getName().getLocalPart())
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
            sb.append( values[index] );
            return sb.toString();
        }
    }
    
    
}
