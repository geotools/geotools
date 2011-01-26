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

import org.geotools.caching.spatialindex.NodeIdentifier;

/**
 * Eviction policy interface.  Used to determine which items are removed
 * from the cache when the maximum cache size is reached.
 * 
 *
 * @source $URL$
 */
public interface EvictionPolicy {
   
	/**
	 * Removes the node from next node in the policy from the cache.
	 * @return false if nothing to evict; otherwise returns true
	 */
    public boolean evict();

    /**
     * Called when a node is accessed.
     * @param node	node accessed
     */
    public void access(NodeIdentifier node);
    
//    /**
//     * 
//     * @return false if nothing to evict; otherwise returns true
//     */
//    public boolean canEvict();
}
