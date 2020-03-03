/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import java.util.Set;

/**
 * Mapping of names to ZoomContexts
 *
 * @author Kevin Smith, Boundless
 */
public interface ZoomContextFinder {

    /**
     * Get a named ZoomContext
     *
     * @return The named context, or null if it is not present
     */
    public ZoomContext get(String name);

    /** Get all valid names */
    public Set<String> getNames();

    /** Get one name for each available ZoomContext. */
    public Set<String> getCanonicalNames();
}
