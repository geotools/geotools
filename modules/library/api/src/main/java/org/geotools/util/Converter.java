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
package org.geotools.util;


/**
 * Converts values of one type into another.
 *
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @since 2.4
 *
 * @source $URL$
 */
public interface Converter {
    /**
     * Converts an object to an object of another type.
     * <p>
     * If the converstion supplied is not supported this method can either throw an exception or
     * return <code>null</code>.
     * </p>
     *
     * @param source The original object, never <code>null</code>
     * @param target The type of the converted object.
     *
     * @return An instance of target, or <code>null</code> if the conversion could not take place.
     *
     * @throws Exception If the conversion can not take place.
     */
    <T> T convert(Object source, Class<T> target) throws Exception;
}
