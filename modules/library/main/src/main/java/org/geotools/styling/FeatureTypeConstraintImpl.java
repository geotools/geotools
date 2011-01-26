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
package org.geotools.styling;

import java.util.Arrays;

import org.geotools.util.Utilities;
import org.opengis.filter.Filter;


/**
 *
 * @source $URL$
 */
public class FeatureTypeConstraintImpl implements FeatureTypeConstraint, Cloneable {
    /** the feature type name */
    String featureTypeName;

    /** the filter */
    Filter filter;

    /** the extents */
    Extent[] extents;

    public String getFeatureTypeName() {
        return featureTypeName;
    }

    public void setFeatureTypeName(String name) {
        this.featureTypeName = name;
    }

    public Filter getFilter() {
        return filter;
    }

    public void setFilter(Filter filter) {
        this.filter = filter;
    }

    public Extent[] getExtents() {
        return extents;
    }

    public void setExtents(Extent[] extents) {
        this.extents = extents;
    }

    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }
    
    public int hashCode() {
    	final int PRIME = 1000003;
		int result = 0;
		
		if (featureTypeName != null) {
			result = (PRIME * result) + featureTypeName.hashCode();
		}
		
		if (filter != null) {
			result =  (PRIME * result) + filter.hashCode();
		}
		
		if (extents != null) {
			result =  (PRIME * result) + extents.hashCode();
	
		}
		return result;
    }
    
    public boolean equals(Object obj) {
    	if (this == obj) {
    		return true;
    	}
    	
    	if (obj instanceof FeatureTypeConstraintImpl) {
    		FeatureTypeConstraintImpl other = (FeatureTypeConstraintImpl)obj;
    		return Utilities.equals(featureTypeName,other.featureTypeName) && 
    			Utilities.equals(filter,other.filter) && 
    			Arrays.equals(extents,other.extents);
    	}
    	
    	return false;
    		
    }
}
