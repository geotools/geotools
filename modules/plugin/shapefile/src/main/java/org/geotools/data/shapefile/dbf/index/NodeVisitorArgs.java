/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.dbf.index;

import org.opengis.filter.Filter;

/**
 * Argument class of a Visitor with {@code visit} methods to be called by {@link Node#accept Node.accept(...)}.
 * 
 * @author Alvaro Huarte
 */
public interface NodeVisitorArgs {
    
    /**
     * Returns the current filter being evaluated.
     */
    Filter filter();
    
    /**
     * Returns {@code true} if this job is cancelled.
     */
    boolean isCanceled();
    
    /**
     * Indicates that task should be cancelled.
     */
    void setCanceled(boolean cancel);
    
    /**
     * Returns a shared buffer to efficiently visit nodes.
     */
    byte[] sharedBuffer(int bufferLength);
}
