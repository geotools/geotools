/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectormosaic;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/** Class used to track the number of times a granule is accessed and the number of unique granules */
public class GranuleTracker {
    private Set<String> granuleNames = new HashSet<>();
    private AtomicInteger count = new AtomicInteger(0);

    /** Increment the access count */
    public void incrementAccessCount() {
        count.incrementAndGet();
    }

    /**
     * Get the number of unique granules
     *
     * @return
     */
    public int getCount() {
        return count.get();
    }

    /**
     * Add a granule names
     *
     * @param params the granule name
     */
    public void add(String params) {
        granuleNames.add(params);
    }

    public Set<String> getGranuleNames() {
        return granuleNames;
    }

    public void setGranuleNames(Set<String> granuleNames) {
        this.granuleNames = granuleNames;
    }

    public void setCount(AtomicInteger count) {
        this.count = count;
    }
}
