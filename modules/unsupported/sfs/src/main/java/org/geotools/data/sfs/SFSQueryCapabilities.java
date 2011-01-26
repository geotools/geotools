/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.sfs;

import org.geotools.data.QueryCapabilities;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.sort.SortBy;

/**
 * OpenDataStore supports offset, and sorting only on explicit attributes 
 */

class SFSQueryCapabilities extends QueryCapabilities {

    SimpleFeatureType schema;

    /**
     *
     * @param schema
     */
    public SFSQueryCapabilities(SimpleFeatureType schema) {
    	if(schema == null)
    		throw new NullPointerException("Provided schema is null");
        this.schema = schema;
    }

    /**
     * is offset supported
     * @return boolean
     */
    @Override
    public boolean isOffsetSupported() {
        return true;
    }
    
    /**
     * Is Sorting supported ?
     * @param sortAttributes
     * @return true/false
     */
    @Override
    public boolean supportsSorting(SortBy[] sortAttributes) {
        // natural sorting, we don't support it
        if ((sortAttributes == null) || (sortAttributes.length == 0)) {
            return false;
        }

        // sort by attribute, we do unless there is a geometric one
        for (SortBy sortBy : sortAttributes) {
            AttributeDescriptor att = schema.getDescriptor(sortBy.getPropertyName().getPropertyName());
            if(att == null || att instanceof GeometryDescriptor) {
                return false;
            }
        }
        return true;
    }
}
