/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.wololo.flatgeobuf.HeaderMeta;

public class FlatGeobufFeatureSource extends ContentFeatureSource {

    public FlatGeobufFeatureSource(ContentEntry entry, Query query) {
        super(entry, query);
    }

    @Override
    public FlatGeobufDataStore getDataStore() {
        DataStore dataStore = super.getDataStore();
        if (dataStore instanceof FlatGeobufDirectoryDataStore) {
            return ((FlatGeobufDirectoryDataStore) dataStore).getDataStore(entry.getTypeName());
        } else {
            return (FlatGeobufDataStore) dataStore;
        }
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        return new FlatGeobufFeatureReader(getState(), query, getDataStore().getHeaderMeta());
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        if (query.getFilter() != Filter.INCLUDE) {
            return null;
        }

        HeaderMeta headerMeta = getDataStore().getHeaderMeta();
        CoordinateReferenceSystem crs;
        try {
            crs = headerMeta.srid > 0 ? CRS.decode("EPSG:" + headerMeta.srid) : null;
        } catch (FactoryException e) {
            throw new IOException(e);
        }
        Envelope env = headerMeta.envelope;
        if (env != null) {
            return new ReferencedEnvelope(env, crs);
        }

        // NOTE: header does not contain envelope will enumerate features to calculate
        try (FlatGeobufFeatureReader reader = (FlatGeobufFeatureReader) getReaderInternal(query)) {
            SimpleFeature feature;
            ReferencedEnvelope referencedEnvelope = null;
            while (reader.hasNext()) {
                feature = reader.next();
                if (referencedEnvelope == null) {
                    referencedEnvelope = new ReferencedEnvelope(feature.getBounds());
                } else {
                    referencedEnvelope.expandToInclude(new ReferencedEnvelope(feature.getBounds()));
                }
            }
            return referencedEnvelope;
        }
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        int count = -1;
        if (query.getFilter() != Filter.INCLUDE) {
            return count;
        }

        count = (int) getDataStore().getHeaderMeta().featuresCount;

        if (count > 0) {
            return count;
        }

        // NOTE: header does not contain feature count will enumerate features to calculate
        try (FlatGeobufFeatureReader reader = (FlatGeobufFeatureReader) getReaderInternal(query)) {
            count = 0;
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
            return count;
        }
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        return getDataStore().getFeatureType(getEntry().getName());
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        return super.handleVisitor(query, visitor);
    }
}
