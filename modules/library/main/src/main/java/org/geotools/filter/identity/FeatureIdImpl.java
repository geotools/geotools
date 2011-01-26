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

import org.opengis.feature.Feature;
import org.opengis.filter.identity.FeatureId;

/**
 * Implementation of {@link org.opengis.filter.identity.FeatureId}
 * <p>
 * This class is mutable under one condition only; during a commit
 * a datastore can update the internal fid to reflect the real identify
 * assigned by the database or wfs.
 * <p>
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class FeatureIdImpl implements FeatureId {

	/** underlying fid */
	protected String fid;
	protected String origionalFid;
	public FeatureIdImpl( String fid ) {
		this.fid = fid;
		if ( fid == null ) {
			throw new NullPointerException( "fid must not be null" );
		}
	}
	
	public String getID() {
		return fid;
	}

	public void setID( String id ){
		if ( id == null ) {
			throw new NullPointerException( "fid must not be null" );
		}	
		if( origionalFid == null ){
                    origionalFid = fid;
                }
		fid = id;			
	}
	
	public boolean matches(Feature feature) {
		return feature != null && fid.equals( feature.getIdentifier().getID() );
	}

	public boolean matches(Object object) {
		if ( object instanceof Feature ) {
			return matches( (Feature) object );
		}	
		return false;
	}

	public String toString() {
		return fid;
	}
	
	public boolean equals(Object obj) {
		if ( obj instanceof FeatureId) {
			return fid.equals( ((FeatureId)obj).getID() );
		}
		
		return false;
	}
	
	public int hashCode() {
		return fid.hashCode();
	}

}
