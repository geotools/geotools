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
package org.geotools.xml.schema;

/**
 * <p>
 * This interface is intended to represent a data structure to pass resolved
 * Child elements to the parent element. This is of particular importance as
 * this enable the programmer to reverse the direction of the recursion on the
 * parse tree, hopefully reducing the impact on the heap and overall memory.
 * </p>
 *
 * @author dzwiers www.refractions.net
 * @source $URL$
 */
public interface ElementValue {
    /**
     * <p>
     * Returns the type which generated the associated value. The type is
     * important because it allows easy access to the xml element inheritance
     * tree, allowing the user to test whether it is a valid data entry.
     * </p>
     *
     * @return Type
     */

    //    public Type getType();
    public Element getElement();

    /**
     * <p>
     * This returns the realized value of an element which was associated with
     * this type. We recommend that this value be realized prior to the first
     * request for the value (use this object to cache the result). If you do
     * chose to implement this method lazily, consider caching the result as
     * it may be called more than once, expecting the same result both times.
     * </p>
     *
     * @return Object (may be null)
     */
    public Object getValue();
}
