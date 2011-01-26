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
package org.geotools.index.quadtree;

import org.geotools.data.shapefile.shp.IndexFile;

/**
 * DOCUMENT ME!
 * 
 * @author Tommaso Nolli
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/shapefile/src/main/java/org/geotools/index/quadtree/IndexStore.java $
 */
public interface IndexStore {
    /**
     * Stores a <code>QuadTree</code>
     * 
     * @param tree
     *                the <code>QuadTree</code> to store
     * 
     * @throws StoreException
     */
    public void store(QuadTree tree) throws StoreException;

    /**
     * Loads a <code>QuadTree</code>
     * 
     * @return the loaded <code>QuadTree</code>
     * 
     * @throws StoreException
     */
    public QuadTree load(IndexFile indexfile, boolean useMemoryMapping) throws StoreException;
}
