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
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.PrimativeAttributeType;
import org.geotools.util.Converters;
import org.opengis.filter.Filter;

import java.util.Date;

/**
 * A Default class that represents a Temporal attribute.
 * @source $URL$
 */
public class TemporalAttributeType extends DefaultAttributeType implements PrimativeAttributeType {
    // this might be right, maybe not, but anyway, its a default formatting
    static java.text.DateFormat format = java.text.DateFormat.getInstance();

    public TemporalAttributeType(String name, boolean nillable, int min,
        int max, Object defaultValue, Filter filter) {
        super(name, java.util.Date.class, nillable, min, max, defaultValue);
        this.filter = filter;
    }
    public TemporalAttributeType(String name, Class type, boolean nillable, int min, int max, 
		Object defaultValue, Filter filter ) {
    	super(name, type, nillable, min, max, defaultValue);
    	this.filter = filter;
    }
    
    private Filter filter;

    public Object duplicate(Object o) throws IllegalAttributeException {
        if (o == null) {
            return null;
        }

        if (o instanceof Date) {
            Date d = (Date) o;

            return new Date(d.getTime());
        }

        throw new IllegalAttributeException("Cannot duplicate "
            + o.getClass().getName());
    }

	/* (non-Javadoc)
	 * @see org.geotools.feature.PrimativeAttributeType#getRestriction()
	 */
	public Filter getRestriction() {
		return filter;
	}
}
