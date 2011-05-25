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
package org.geotools.caching.featurecache;

/**
 * Exception thrown by the FeatureCache when unexpected error occurs.
 *
 *
 * @source $URL$
 */
public class FeatureCacheException extends Exception {

    private static final long serialVersionUID = 5016587341837058296L;

    public FeatureCacheException() {
        super();
    }

    public FeatureCacheException(Throwable t) {
        super();
        initCause(t);
    }

    public FeatureCacheException(String comment) {
        super(comment);
    }
}
