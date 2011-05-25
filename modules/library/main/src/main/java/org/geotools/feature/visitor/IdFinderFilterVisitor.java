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
package org.geotools.feature.visitor;

import org.geotools.filter.visitor.AbstractFinderFilterVisitor;
import org.opengis.filter.Id;

/**
 * Quick check to see if an ID filter is found.
 * <p>
 * Example:<code>found = (Boolean) filter.accept( new IdFinderFilter(), null )</code>
 *
 *
 * @source $URL$
 */
public class IdFinderFilterVisitor extends AbstractFinderFilterVisitor {

    public Object visit( Id filter, Object data ) {
        found = true;
        return found;
    }
        
}
