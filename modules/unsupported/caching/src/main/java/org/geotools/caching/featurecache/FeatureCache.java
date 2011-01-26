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

import java.io.IOException;

import org.geotools.caching.CacheOversizedException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.FeatureCollection;

import com.vividsolutions.jts.geom.Envelope;

/**
 * Interface for a feature cache. 
 *
 *
 * @source $URL$
 */
public interface FeatureCache extends SimpleFeatureSource {
    /**
     * Removes all features from the feature cache.
     */
    public void clear();

    /**
    *
    * @param fc the feature collection to add to the cache
    * @param e the envelope that encompasses the feature collection added
    *  
    * @throws CacheOversizedException
    */
    public void put(SimpleFeatureCollection fc, Envelope e) throws CacheOversizedException;

    /**
     *
     * @param fc the feature collection to add to the cache
     *  
     * @throws CacheOversizedException
     */
    public void put(SimpleFeatureCollection fc) throws CacheOversizedException;

    /**
     * Returns a feature collection of features within in the given 
     * envelope.
     *
     * <p>This function will look for features in the cache; if none are present it
     * will then look into the underlying feature source for the features.</p>
     * @param e
     * 
     * @return Collection of feature found in the given envelope (from the cache or feature source)
     * @throws IOException
     */
    public SimpleFeatureCollection get(Envelope e) throws IOException;

    /**
     * Looks in the cache for the features within a given
     * envelope.
     * <p>This function will only look in the cache for the features;
     * it will not look in the underlying datastore.</p>
     *
     * @param e
     * @return Collection of features found in the cache.
     */
    public SimpleFeatureCollection peek(Envelope e);

    /**
     * Removes all features from the cache
     * that lie within the given envelope.
     *
     * @param e
     */
    public void remove(Envelope e);
    
    /**
     * Disposes of the feature cache closing any open store
     * connections.
     */
    public void dispose();
    
}
