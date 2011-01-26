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

import org.geotools.feature.DefaultAttributeType;
import org.geotools.feature.PrimativeAttributeType;
import org.opengis.filter.Filter;


/**
 *
 * @source $URL$
 */
public class TextualAttributeType extends DefaultAttributeType implements PrimativeAttributeType {
    public TextualAttributeType(String name, boolean nillable, int min,
        int max, Object defaultValue, Filter filter) {
        super(name, String.class, nillable, min, max, defaultValue);
        this.filter = filter;
    }
    private Filter filter;

    /**
     * Duplicate as a String
     *
     * @param o DOCUMENT ME!
     *
     * @return a String obtained by calling toString or null.
     */
    public Object duplicate(Object o) {
        if (o == null) {
            return null;
        }

        return o.toString();
    }

	/* (non-Javadoc)
	 * @see org.geotools.feature.PrimativeAttributeType#getRestriction()
	 */
	public Filter getRestriction() {
		return filter;
	}
}
