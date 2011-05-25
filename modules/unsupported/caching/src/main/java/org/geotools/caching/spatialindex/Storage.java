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
package org.geotools.caching.spatialindex;

import java.util.Collection;
import java.util.Properties;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.type.FeatureType;

/**
 * Interface for cache storage options.
 * 
 *
 *
 * @source $URL$
 */
public interface Storage {
    
    public static final String STORAGE_TYPE_PROPERTY = "Storage.Type";

    /**
     * Adds a given node to the store.
     *
     * @param n
     */
    public void put(Node n);

    /**
     * Removes a node from the store given a node identifier
     *
     * @param id  identifier of the node to remove
     */
    public void remove(NodeIdentifier id);

    /**
     * Reads a node from the store given an node identifier
     *
     * @param id  identifier of the node to read
     * @return
     */
    public Node get(NodeIdentifier id);

    /**
     * Clears all information from the store.
     */
    public void clear();

    /**
     * Get properties about the given storage.  The actual
     * properties returned depend on the type of storage.
     *
     * @return
     */
    public Properties getPropertySet();

    /**
     * Flushes the store writing everything to the store.
     * <p>Currently this is really only used by the BufferedDiskStorage
     * to write everything in the buffer to the store.
     * </p>
     */
    public void flush();
    
    /**
     * Disposes of the store.
     */
    public void dispose();
    

    public NodeIdentifier findUniqueInstance(NodeIdentifier id);
        
    /**
     * @returns an unmodifiable collection of all the features types of the stored features
     */
    public Collection<FeatureType> getFeatureTypes();

    /**
     * Adds a feature type to the store.
     *
     * @param ft feature type to add
     */
    public void addFeatureType(FeatureType ft);
    
    /**
     * Removes all feature types associated with the store.
     */
    public void clearFeatureTypes();
    
    /**
     * Sets the bounds of the data in the cache.
     *
     * @param bounds
     */
    public void setBounds(ReferencedEnvelope bounds);
    
    /**
     * Gets the bounds of the cached data.
     *
     * @return
     */
    public ReferencedEnvelope getBounds();
}
