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
package org.geotools.data.store;

import java.util.Iterator;

import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

/**
 * Iterator wrapper which re-types features on the fly based on a target 
 * feature type.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class ReTypingIterator implements Iterator {

	/**
	 * The delegate iterator
	 */
	Iterator delegate;
	
	/**
	 * The target feature type
	 */
	SimpleFeatureType target;
	
	/**
	 * The matching types from target 
	 */
	AttributeDescriptor[] types;
	
	SimpleFeatureBuilder builder;
	
	public ReTypingIterator( Iterator delegate, SimpleFeatureType source, SimpleFeatureType target ) {
		this.delegate = delegate;
		this.target = target;
		types = typeAttributes( source, target );
		this.builder = new SimpleFeatureBuilder(target);
	}
	
	public Iterator getDelegate() {
		return delegate;
	}

	public void remove() {
		delegate.remove();
	}

	public boolean hasNext() {
		return delegate.hasNext();
	}

	public Object next() {
		SimpleFeature next = (SimpleFeature) delegate.next();
        String id = next.getID();

        try {
			for (int i = 0; i < types.length; i++) {
			    final String xpath = types[i].getLocalName();
			    builder.add(next.getAttribute(xpath));
			}
			
			return builder.buildFeature(id);
		} 
        catch (IllegalAttributeException e) {
        	throw new RuntimeException( e );
		}
	}
	
	 /**
     * Supplies mapping from origional to target FeatureType.
     * 
     * <p>
     * Will also ensure that origional can cover target
     * </p>
     *
     * @param target Desired FeatureType
     * @param origional Origional FeatureType
     *
     * @return Mapping from originoal to target FeatureType
     *
     * @throws IllegalArgumentException if unable to provide a mapping
     */
    protected AttributeDescriptor[] typeAttributes(SimpleFeatureType original,
        SimpleFeatureType target) {
        if (target.equals(original)) {
            throw new IllegalArgumentException(
                "FeatureReader allready produces contents with the correct schema");
        }

        if (target.getAttributeCount() > original.getAttributeCount()) {
            throw new IllegalArgumentException(
                "Unable to retype  FeatureReader<SimpleFeatureType, SimpleFeature> (origional does not cover requested type)");
        }

        String xpath;
        AttributeDescriptor[] types = new AttributeDescriptor[target.getAttributeCount()];

        for (int i = 0; i < target.getAttributeCount(); i++) {
            AttributeDescriptor attrib = target.getDescriptor(i);
            xpath = attrib.getLocalName();
            types[i] = attrib;

            if (!attrib.equals(original.getDescriptor(xpath))) {
                throw new IllegalArgumentException(
                    "Unable to retype  FeatureReader<SimpleFeatureType, SimpleFeature> (origional does not cover "
                    + xpath + ")");
            }
        }

        return types;
    }

}
