/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.styling;

import java.util.Arrays;

public class LayerFeatureConstraintsImpl implements LayerFeatureConstraints {

	private FeatureTypeConstraint[] constraints;
	
	public FeatureTypeConstraint[] getFeatureTypeConstraints() {
		return constraints;
	}

	public void setFeatureTypeConstraints(FeatureTypeConstraint[] constraints) {
		this.constraints = constraints;
	}
	
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj instanceof FeatureTypeConstraintImpl) {
			LayerFeatureConstraintsImpl other = (LayerFeatureConstraintsImpl)obj;
			return Arrays.equals(constraints,other.constraints);
		}
		
		return false;
	}
	
	public int hashCode() {
		final int PRIME = 1000003;
		int result = 0;
		
		if (constraints != null) {
			 result = (PRIME * result) + constraints.hashCode();
		}
		
		return result;
	}

}
