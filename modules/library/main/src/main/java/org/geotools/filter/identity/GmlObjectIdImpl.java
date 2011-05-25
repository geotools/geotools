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
package org.geotools.filter.identity;

import org.opengis.filter.identity.GmlObjectId;

/**
 * Implementation of {@link org.opengis.filter.identity.GmlObjectId}.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class GmlObjectIdImpl implements GmlObjectId {

	/** the object id */
	String gmlId;
	
	public GmlObjectIdImpl( String gmlId ) {
		this.gmlId = gmlId;
		if ( gmlId == null ) {
			throw new NullPointerException( "id can not be null" );
		}
	}
	
	public String getID() {
		return gmlId;
	}

	public boolean matches( Object object ) {
		if ( object instanceof org.opengis.feature.Feature ) {
			return new FeatureIdImpl( gmlId ).matches( (org.opengis.feature.Feature) object );
		}
		
		//TODO: geometries
		return false;
	}
	
	public String toString() {
		return gmlId;
	}
	
	public boolean equals(Object obj) {
		if ( obj instanceof GmlObjectIdImpl ) {
			GmlObjectIdImpl other = (GmlObjectIdImpl) obj;
			return gmlId.equals( other.gmlId );
		}
		
		return false;
	}
	
	public int hashCode() {
		return gmlId.hashCode();
	}
	

}
