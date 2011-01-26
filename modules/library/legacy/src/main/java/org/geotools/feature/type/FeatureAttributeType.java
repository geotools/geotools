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
package org.geotools.feature.type;

import org.geotools.feature.AttributeType;
import org.geotools.feature.DefaultAttributeType;
import org.geotools.feature.Feature;
import org.geotools.feature.FeatureType;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.NameImpl;
import org.opengis.filter.Filter;

/**
 * Represents an ordered list of AttributeTypes. For SFS this will be a 
 * FeatureType. For GML this is the same as an element whose complexType 
 * contains a Sequence of Attributes.
 * 
 * @author dzwiers
 * @source $URL$
 */
public class FeatureAttributeType extends AttributeDescriptorImpl implements AttributeType {

	private final FeatureType featureType;
	
	
	/**
	 * @param copy
	 */
	public FeatureAttributeType(FeatureAttributeType copy) {
		super(copy.getType(),copy.getName(),copy.getMinOccurs(),copy.getMaxOccurs(),copy.isNillable(),copy.getDefaultValue());
        
		featureType = copy.getFeatureType();
	}

	// The field for 'Class type' should be added when GT has moved to java 1.5
    public FeatureAttributeType(String name,FeatureType featureType, boolean nillable, int min, int max) {
    	super(DefaultAttributeType.createAttributeType(name, Feature.class, Filter.INCLUDE),new NameImpl(name),min,max,nillable,null);
    	
    	this.featureType = featureType;
    }

    public FeatureAttributeType(String name,FeatureType featureType, boolean nillable){
    	this(name,featureType, nillable, 1, 1);
    }
    public Filter getRestriction(){
    	return DefaultAttributeType.getRestriction(this);
    }
    
    protected FeatureType getFeatureType(){return featureType;}
	
    /**
	 * {@inheritDoc}
	 */
	public String getLocalName() {
		return DefaultAttributeType.getLocalName(this);
	}

	/**
	 * {@inheritDoc}
	 */
	public Class getBinding() {
		return DefaultAttributeType.getBinding(this);
	}
	
	/* (non-Javadoc)
	 * @see org.geotools.feature.AttributeType#isGeometry()
	 */
	public boolean isGeometry() {
		return false;
	}

	/* (non-Javadoc)
	 * @see org.geotools.feature.AttributeType#parse(java.lang.Object)
	 */
	public Object parse(Object value) throws IllegalArgumentException {
	    if (value instanceof Feature) {
		return value;
	    }
	    if(value instanceof Object[])
			try {
				return featureType.create((Object[])value);
			} catch (IllegalAttributeException e) {
				IllegalArgumentException ee = new IllegalArgumentException(e.getMessage());
				ee.initCause(e);
				throw ee;
			}
		throw new IllegalArgumentException(getLocalName()+" needs to parse an array of Objects");
	}

	/* (non-Javadoc)
	 * @see org.geotools.feature.AttributeType#validate(java.lang.Object)
	 */
	public void validate(Object obj) throws IllegalArgumentException {
        if(obj instanceof Feature){
            try {
                featureType.duplicate((Feature)obj);
                return;
            } catch (IllegalAttributeException e) {
                IllegalArgumentException ee = new IllegalArgumentException(e.getMessage());
                ee.initCause(e);
                throw ee;
            }
        }
        //users should call parse first - ch.
        /*if(obj instanceof Object[]){
            try {
                featureType.create((Object[])obj);
                return;
            } catch (IllegalAttributeException e) {
                IllegalArgumentException ee = new IllegalArgumentException(e.getMessage());
                ee.initCause(e);
                throw ee;
            }
	    }*/
		throw new IllegalArgumentException("Not a Feature");
		
	}

	/* (non-Javadoc)
	 * @see org.geotools.feature.AttributeType#duplicate(java.lang.Object)
	 */
	public Object duplicate(Object src) throws IllegalAttributeException {
		if(src instanceof Feature){
			return featureType.duplicate((Feature)src);
		}
		throw new IllegalArgumentException("Not an Object []");
	}

	/* (non-Javadoc)
	 * @see org.geotools.feature.AttributeType#createDefaultValue()
	 */
	public Object createDefaultValue() {
		Object[] t = new Object[featureType.getAttributeCount()];
		for(int i=0;i<t.length;i++)
			t[i] = null;
		try {
			return featureType.create(t);
		} catch (IllegalAttributeException e) {
			// do nothing
			return null;
		}
	}

    /**
     * This is only used twice in the whole geotools code base, and  one of
     * those is for a test, so we're removing it from the interface. If
     * getAttributeType does not have the AttributeType it will just return
     * null.  Gets the number of occurrences of this attribute.
     *
     * @param xPath XPath pointer to attribute type.
     *
     * @return Number of occurrences.
     */
    public boolean hasAttributeType(String xPath) {
        return getAttributeType(xPath) != null;
    }

    /**
     * Returns the number of attributes at the first 'level' of the schema.
     *
     * @return equivalent value to getAttributeTypes().length
     */
    public int getAttributeCount(){
    	return featureType.getAttributeCount();
    }
    

    /**
     * Gets the attributeType at this xPath, if the specified attributeType
     * does not exist then null is returned.
     *
     * @param xPath XPath pointer to attribute type.
     *
     * @return True if attribute exists.
     */
    public AttributeType getAttributeType(String xPath) {        
        return featureType.getAttributeType(xPath);
    }

    /**
     * Find the position of a given AttributeType.
     *
     * @param type The type to search for.
     *
     * @return -1 if not found, a zero-based index if found.
     */
    public int find(AttributeType type) {return featureType.find(type);
    }
    
    /**
     * Find the position of an AttributeType which matches the given String.
     * @param attName the name to look for
     * @return -1 if not found, zero-based index otherwise
     */
    public int find(String attName) {
    	return featureType.find(attName);
    }

    /**
     * Gets the attributeType at the specified index.
     *
     * @param position the position of the attribute to check.
     *
     * @return The attribute type at the specified position.
     */
    public AttributeType getAttributeType(int position) {
    	return featureType.getAttributeType(position);
    }

    public AttributeType[] getAttributeTypes() {
    	return featureType.getAttributeTypes();
    }
}
