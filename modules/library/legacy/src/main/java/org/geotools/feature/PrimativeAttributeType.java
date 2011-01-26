/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import org.opengis.filter.Filter;


/**
 * Adds the ability to have restrictions on a particular data primitive
 * in a declarative manner.
 *
 * Standard expectations include internal validation using the specified
 * restrictions, and that the restrictions are immutable.
 *
 * @author dzwiers
 * @source $URL$
 *
 * @deprecated Will be removed in geotools 2.6
 */
public interface PrimativeAttributeType extends AttributeType {
    /**
     * This provides access to the immutable restriction for this attribute
     * type. This restriction should be applied when real data hits instances
     * of this class. This mapps to the idea of a Facet in xml schema, or
     * restrictions in a database.
     *
     * Examples may include Length <= 20 (VARCHAR 20)
     *
     * The Default value is Filter.EXCLUDE
     *
     * @return the restriction for applied to this attribute type, or
     * Filter.EXCLUDE. Mat not be null.
     *
     * @see Filter
     * @see Filter#ALL
     */
    Filter getRestriction();
}
