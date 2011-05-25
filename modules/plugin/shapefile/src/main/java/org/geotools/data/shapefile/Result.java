/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

/**
 * 
 * @author jesse
 * @param <V>
 *                The type of value that the result is
 * @param <S>
 *                The state of the return for example this may be a an enum that
 *                Provides state values such as NONE, FAILURE, etc..
 *
 *
 * @source $URL$
 */
public class Result<V, S> {

    public final V value;
    public final S state;

    public Result(V value, S state) {
        this.value = value;
        this.state = state;
    }

    @Override
    public String toString() {
        return "State: " + state + " value: " + value;
    }
}
