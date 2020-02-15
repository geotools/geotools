/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.memory;

import org.geotools.data.store.ContentState;

/**
 * State for MemoryDataStore providing FeatureType and stored features.
 *
 * @author Jody Garnett (Boundless)
 */
public class MemoryState extends ContentState {
    /** State for MemoryDataStore providing FeatureType and stored features. */
    public MemoryState(MemoryEntry entry) {
        super(entry);
    }

    /** Creates the state from an existing one. */
    public MemoryState(MemoryState state) {
        this(state.getEntry());
    }

    @Override
    public MemoryEntry getEntry() {
        return (MemoryEntry) super.getEntry();
    }

    public MemoryState copy() {
        return new MemoryState(this);
    }
}
