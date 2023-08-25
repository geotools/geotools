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
package org.geotools.kml;

import org.geotools.styling.FeatureTypeStyleImpl;

import java.net.URI;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Simple container for holding styles by uri.
 *
 * <p>This is lame as it is just a hash map in memory. It should really be an embedded db that
 * serializes / deserializes out to disk.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class StyleMap {
    protected Map<URI, FeatureTypeStyleImpl> map = Collections.synchronizedMap(new HashMap<>());

    public void put(URI uri, FeatureTypeStyleImpl style) {
        map.put(uri, style);
    }

    public FeatureTypeStyleImpl get(URI uri) {
        return map.get(uri);
    }
}
