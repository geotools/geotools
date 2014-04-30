/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter;

import java.util.Iterator;
import java.util.List;

import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.And;

/**
 * Direct implementation of And filter.
 *
 * @author jdeolive
 *
 *
 *
 * @source $URL$
 */
public class AndImpl extends LogicFilterImpl implements And {

    protected AndImpl(List<Filter> children) {
        super(children);
    }

    public boolean evaluate(Object object) {
        for (Iterator<Filter> itr = children.iterator(); itr.hasNext();) {
            Filter filter = itr.next();
            if (!filter.evaluate(object)) {
                return false; // short circuit
            }
        }
        return true;
    }

	public Object accept(FilterVisitor visitor, Object extraData) {
		return visitor.visit(this,extraData);
	}
}

