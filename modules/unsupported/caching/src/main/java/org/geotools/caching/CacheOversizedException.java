/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.caching;

import org.geotools.caching.featurecache.FeatureCacheException;

/**
 * An Exception that is thrown when the
 * size of the features being added to the cache exceeds the
 * size of the cache.
 *
 *
 * @source $URL$
 */
public class CacheOversizedException extends FeatureCacheException {
    /**
     *
     */
    private static final long serialVersionUID = 3498526657089279151L;

    public CacheOversizedException() {
        super();
    }

    public CacheOversizedException(Throwable t) {
        super(t);
    }

    public CacheOversizedException(String message) {
        super(message);
    }
}
