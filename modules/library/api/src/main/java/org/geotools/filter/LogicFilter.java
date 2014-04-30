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
package org.geotools.filter;

import java.util.List;

import org.opengis.filter.Filter;

/**
 * Maker interface for logic filters (the only filter type that contains other filters).
 * 
 * This filter holds one or more filters together and relates them logically with an internally defined type (AND, OR, NOT).
 * 
 * @author Rob Hranac, TOPP
 * @author Chris Holmes, TOPP
 * 
 * @source $URL$
 * @version $Id$
 * 
 * @deprecated use {@link org.opengis.filter.BinaryLogicOperator}
 */
public interface LogicFilter extends Filter {

    /**
     * Returns a list containing all of the child filters of this object.
     * <p>
     * This list will contain at least two elements, and each element will be an
     * instance of {@code Filter}.
     */
    List<org.opengis.filter.Filter> getChildren();

}
