/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Read access to feature content held in memory.
 *
 * @author Jody Garnett (Boundless)
 */
public class MemoryFeatureSource extends ContentFeatureSource {

    public MemoryFeatureSource(ContentEntry entry) {
        this(entry, Query.ALL);
    }

    public MemoryFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
    }

    /** Access parent MemoryDataStore. */
    public MemoryDataStore getDataStore() {
        return (MemoryDataStore) super.getDataStore();
    }

    public MemoryState getState() {
        return (MemoryState) super.getState();
    }
    /** The entry for the feature source. */
    public MemoryEntry getEntry() {
        return (MemoryEntry) super.getEntry();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) { // filtering not implemented
            FeatureReader<SimpleFeatureType, SimpleFeature> featureReader =
                    getReaderInternal(query);
            CoordinateReferenceSystem crs =
                    featureReader.getFeatureType().getCoordinateReferenceSystem();
            ReferencedEnvelope bounds = ReferencedEnvelope.create(crs);
            try {
                while (featureReader.hasNext()) {
                    SimpleFeature feature = featureReader.next();
                    bounds.include(feature.getBounds());
                }
            } finally {
                featureReader.close();
            }
            return bounds;
        }
        return null; // feature by feature scan required to count records
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        if (query.getFilter() == Filter.INCLUDE) {
            MemoryEntry entry = getEntry();
            return entry.getMemory().size();
        }
        // feature by feature count required
        return -1;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return new MemoryFeatureReader(getState(), query);
    }

    @Override
    protected SimpleFeatureType buildFeatureType() {
        return getState()
                .getEntry()
                .schema; // cache schema unchanged (as we do not retype/reproject)
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return super.handleVisitor(query, visitor);
    }
}
