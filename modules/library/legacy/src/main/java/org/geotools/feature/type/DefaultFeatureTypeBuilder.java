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

import org.geotools.feature.AttributeType;
import org.geotools.feature.DefaultFeatureType;
import org.geotools.feature.DefaultFeatureTypeFactory2;
import org.geotools.feature.GeometryAttributeType;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Feature Type Builder which creates instances of the old model.
 * <p>
 * This class should not be used outside of geotools itself by client code. 
 * Client code should be using {@link SimpleFeatureTypeBuilder}.
 * </p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @deprecated 
 */
@Deprecated
public class DefaultFeatureTypeBuilder extends SimpleFeatureTypeBuilder {
    
	public DefaultFeatureTypeBuilder() {
		super( new DefaultFeatureTypeFactory2());
		attributeBuilder = new DefaultAttributeTypeBuilder();
		
		//sets the default namespace to gml
		setNamespaceURI((String)null);
	}
	
	public void setNamespaceURI(String namespaceURI) {
	    if ( namespaceURI != null ) {
	        super.setNamespaceURI(namespaceURI);
	    }
	    else {
	        super.setNamespaceURI("http://www.opengis.net/gml");
	    }
	    
	}
	
	public void add(String name, Class binding) {
		if ( Geometry.class.isAssignableFrom(binding)) {
			//TODO: check user data
			add( name, binding, (CoordinateReferenceSystem) null );
		}
		else {
			super.add( name, binding );
		}
	}
	
	/**
	 * Additional api for adding an AttributeType directly.
	 * 
	 */
	public void add(AttributeType type) {
	    attributes().add(type);
	}
	public void add(AttributeType[] types) {
	    if( types == null ) 
	        return;
	    
	    for ( int i = 0; i < types.length; i++ ) {
	        add(types[i]);
	    }
	}
	
	public void setDefaultGeometry(AttributeType defaultGeomtetry) {
	    setDefaultGeometry(defaultGeomtetry.getLocalName());
	}
	
	/**
	 * Override to type narror to DefaultFeautreType.
	 */
	public DefaultFeatureType buildFeatureType() {
	    return (DefaultFeatureType) super.buildFeatureType();
	}
}
