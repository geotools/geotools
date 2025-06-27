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
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.data.FeatureWriter;
import org.geotools.api.data.Query;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.identity.FeatureId;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.Hints;

/** Update contents of MemoryDataStore. */
public class MemoryFeatureWriter implements FeatureWriter<SimpleFeatureType, SimpleFeature> {
    MemoryState state;
    SimpleFeatureType featureType;
    Name typeName;

    Iterator<SimpleFeature> iterator;

    SimpleFeature live = null;
    SimpleFeature current = null; // current Feature returned to user

    public MemoryFeatureWriter(MemoryState state, Query query) throws IOException {
        this.state = state;
        this.typeName = state.getEntry().getName();
        this.featureType = state.getFeatureType();

        MemoryEntry entry = state.getEntry();
        iterator = entry.getMemory().values().iterator();
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return state.getFeatureType();
    }

    @Override
    public SimpleFeature next() throws IOException, NoSuchElementException {
        if (hasNext()) {
            // existing content
            live = iterator.next();

            try {
                current = SimpleFeatureBuilder.copy(live);
            } catch (IllegalAttributeException e) {
                throw new DataSourceException("Unable to edit " + live.getID() + " of " + typeName);
            }
        } else {
            // new content
            live = null;

            try {
                current = SimpleFeatureBuilder.template(featureType, null);
            } catch (IllegalAttributeException e) {
                throw new DataSourceException("Unable to add additional Features of " + typeName);
            }
        }

        return current;
    }

    @Override
    public void remove() throws IOException {
        if (iterator == null) {
            throw new IOException("FeatureWriter has been closed");
        }

        if (current == null) {
            throw new IOException("No feature available to remove");
        }

        if (live != null) {
            // remove existing content
            iterator.remove();
            live = null;
            current = null;
        } else {
            // cancel add new content
            current = null;
        }
    }

    @Override
    public void write() throws IOException {
        if (iterator == null) {
            throw new IOException("FeatureWriter has been closed");
        }

        if (current == null) {
            throw new IOException("No feature available to write");
        }
        // preserve FeatureIDs during insert feature
        if (Boolean.TRUE.equals(current.getUserData().get(Hints.USE_PROVIDED_FID))) {
            if (current.getUserData().containsKey(Hints.PROVIDED_FID)) {
                String fid = (String) current.getUserData().get(Hints.PROVIDED_FID);
                FeatureId id = new FeatureIdImpl(fid);
                current = new SimpleFeatureImpl(current.getAttributes(), current.getFeatureType(), id);
            }
        }

        if (live != null) {
            if (live.equals(current)) {
                // no modifications made to current
                //
                live = null;
                current = null;
            } else {
                // accept modifications
                //
                try {
                    live.setAttributes(current.getAttributes());
                } catch (Exception e) {
                    throw new DataSourceException(
                            "Unable to accept modifications to " + live.getID() + " on " + typeName);
                }

                ReferencedEnvelope bounds = new ReferencedEnvelope();
                bounds.expandToInclude(new ReferencedEnvelope(live.getBounds()));
                bounds.expandToInclude(new ReferencedEnvelope(current.getBounds()));
                live = null;
                current = null;
            }
        } else {
            // add new content
            MemoryEntry entry = state.getEntry();
            entry.addFeature(current);
            current = null;
        }
    }

    @Override
    public boolean hasNext() throws IOException {
        if (iterator == null) {
            throw new IOException("FeatureWriter has been closed");
        }
        return iterator != null && iterator.hasNext();
    }

    @Override
    public void close() {
        if (iterator != null) {
            iterator = null;
        }

        if (featureType != null) {
            featureType = null;
        }
        current = null;
        live = null;
    }
}
