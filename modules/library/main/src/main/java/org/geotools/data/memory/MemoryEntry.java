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

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import org.geotools.data.store.ContentEntry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Entry used to store features (of a single FeatureType).
 *
 * <p>
 *
 * <p>Please be sure to synchronize on entry before access:
 *
 * <pre><code> synchronize ( entry ){
 *     entry.memory.put( feature.getID(), feature );
 * }</code></pre>
 *
 * @author Jody Garnett (Boundless)
 */
public class MemoryEntry extends ContentEntry {

    /** Schema of managed content. */
    final SimpleFeatureType schema;

    /**
     * Memory storage for features (addressed by fid).
     *
     * <p>Please be sure to synchronize on entry before access:
     *
     * <pre><code> synchronize ( entry ){
     *     entry.memory.put( feature.getID(), feature );
     * }</code></pre>
     */
    private final Map<String, SimpleFeature> memory;

    /** Entry to store content of the provided SimpleFeatureType. */
    MemoryEntry(MemoryDataStore store, SimpleFeatureType schema) {
        super(store, schema.getName());
        this.schema = schema;
        memory = Collections.synchronizedMap(new LinkedHashMap<String, SimpleFeature>());
    }

    protected MemoryState createContentState(ContentEntry entry) {
        return new MemoryState((MemoryEntry) entry);
    }

    /**
     * Access the {@link #memory} field used to store feature content.
     *
     * @return the memory
     */
    public Map<String, SimpleFeature> getMemory() {
        return memory;
    }

    public String toString() {
        return "MemoryEntry '" + getTypeName() + "': " + getMemory().size() + " features";
    }

    /**
     * Safely add feature to {@link #memory}.
     *
     * <p>Feature is required to be non-null, and of the expected {@link #schema}.
     */
    void addFeature(SimpleFeature feature) {
        if (feature == null) {
            throw new IllegalArgumentException("Provided Feature is empty");
        } else if (!feature.getFeatureType().equals(schema)) {
            throw new IllegalArgumentException(
                    "addFeatures expected "
                            + schema.getTypeName()
                            + "(but was "
                            + feature.getFeatureType().getTypeName()
                            + ")");
        }
        getMemory().put(feature.getID(), feature);
    }
}
