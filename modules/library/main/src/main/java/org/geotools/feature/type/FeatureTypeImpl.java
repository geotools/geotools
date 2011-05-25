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

import org.geotools.util.Utilities;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

/**
 * 
 * Base implementation of FeatureType.
 * 
 * @author gabriel
 *
 *
 *
 * @source $URL$
 */
public class FeatureTypeImpl extends ComplexTypeImpl implements FeatureType {
	
	private GeometryDescriptor defaultGeometry;
	private CoordinateReferenceSystem crs;

	public FeatureTypeImpl(
		Name name, Collection<PropertyDescriptor> schema, GeometryDescriptor defaultGeometry, 
		boolean isAbstract, List<Filter> restrictions, AttributeType superType, 
		InternationalString description
	) {
		super(name, schema, true, isAbstract, restrictions, superType, description);
		this.defaultGeometry = defaultGeometry;
		
		if ( defaultGeometry != null && 
		        !(defaultGeometry.getType() instanceof GeometryType ) )  {
		    throw new IllegalArgumentException( "defaultGeometry must have a GeometryType");
		}
        
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
	    if(crs == null) {
    	    if ( getGeometryDescriptor() != null && getGeometryDescriptor().getType().getCoordinateReferenceSystem() != null) {
                crs = defaultGeometry.getType().getCoordinateReferenceSystem();
            }
    	    if(crs == null) {
        	    for (PropertyDescriptor property : getDescriptors()) {
                    if ( property instanceof GeometryDescriptor ) {
                        GeometryDescriptor geometry = (GeometryDescriptor) property;
                        if ( geometry.getType().getCoordinateReferenceSystem() != null ) {
                            crs = geometry.getType().getCoordinateReferenceSystem();
                            break;
                        }
                    }
                }
    	    }
	    }
        
        return crs;
	}
	
	public GeometryDescriptor getGeometryDescriptor() {
	    if (defaultGeometry == null) {
            for (PropertyDescriptor property : getDescriptors()) {
                if (property instanceof GeometryDescriptor ) {
                    defaultGeometry = (GeometryDescriptor) property; 
                    break;
                }
            }
        }
        return defaultGeometry;
	}
	
	public boolean equals(Object o) {
	    if(this == o) {
	        return true;
	    }
	    if(!super.equals(o)){
	        return false;
	    }
	    if (getClass() != o.getClass()) {
            return false;
        }
	    FeatureType other = (FeatureType) o;
	    if (!Utilities.equals( getGeometryDescriptor(), other.getGeometryDescriptor())) {
	        return false;
	    }
	    return true;
	}
	
	public int hashCode() {
		int hashCode = super.hashCode();
		
		if ( defaultGeometry != null ) {
			hashCode = hashCode ^ defaultGeometry.hashCode();
		}
		
		return hashCode;
	}
}
