/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.flatgeobuf;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

public class FlatgeobufFeatureSource extends ContentFeatureSource {

    public FlatgeobufFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
    }

    @Override
    public FlatgeobufDataStore getDataStore() {
        DataStore dataStore = super.getDataStore();
        if (dataStore instanceof FlatgeobufDirectoryDataStore) {
            return ((FlatgeobufDirectoryDataStore) dataStore).getDataStore(entry.getTypeName());
        } else {
            return (FlatgeobufDataStore) dataStore;
        }
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return new FlatgeobufFeatureReader(getState(), query);
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        ReferencedEnvelope referencedEnvelope = null;
        try (InputStream in = new FileInputStream(getDataStore().getFile())) {
            FlatgeobufReader reader =
                    new FlatgeobufReader(
                            getState().getFeatureType().getTypeName(),
                            getState().getFeatureType().getGeometryDescriptor().getLocalName(),
                            in);
            SimpleFeature feature;
            while ((feature = reader.getNextFeature()) != null) {
                if (referencedEnvelope == null) {
                    referencedEnvelope = new ReferencedEnvelope(feature.getBounds());
                } else {
                    referencedEnvelope.expandToInclude(new ReferencedEnvelope(feature.getBounds()));
                }
            }
        }
        return referencedEnvelope;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        int count = -1;
        try (InputStream in = new FileInputStream(getDataStore().getFile())) {
            FlatgeobufReader reader =
                    new FlatgeobufReader(
                            getState().getFeatureType().getTypeName(),
                            getState().getFeatureType().getGeometryDescriptor().getLocalName(),
                            in);
            count = 0;
            while (reader.getNextFeature() != null) {
                count++;
            }
        }
        return count;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return getDataStore().getFeatureType();
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return super.handleVisitor(query, visitor);
    }
}
