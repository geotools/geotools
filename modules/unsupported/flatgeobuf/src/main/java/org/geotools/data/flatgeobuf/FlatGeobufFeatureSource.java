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
import java.io.InputStream;
import java.net.URL;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.feature.FeatureVisitor;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.PackedRTree;

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
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query) throws IOException {
        return new FlatGeobufFeatureReader(getState(), query, getDataStore().getHeaderMeta());
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        if (query.getFilter() != Filter.INCLUDE && !(query.getFilter() instanceof BBOX)) {
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

        if (env == null) return null;
        ReferencedEnvelope result = new ReferencedEnvelope(env, crs);

        if (query.getFilter() == Filter.INCLUDE) {
            return result;
        } else if (query.getFilter() instanceof BBOX) {
            ReferencedEnvelope bbox = ReferencedEnvelope.reference(((BBOX) query.getFilter()).getBounds());
            if (bbox.contains(env)) {
                return result;
            }
        }

        // if there is no fast way, let the caller decide wheter to enumerate or not
        return null;
    }

    @Override
    protected boolean canOffset(Query query) {
        try {
            return query.getFilter() == Filter.INCLUDE && getDataStore().hasIndex();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected boolean canSort(Query query) {
        try {
            return query.getFilter() == Filter.INCLUDE && getDataStore().hasIndex();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        // if we cannot get the header, there is no point
        HeaderMeta headerMeta = getDataStore().getHeaderMeta();
        if (headerMeta == null) return -1;

        // all features count, pick from header if possible
        Filter filter = query.getFilter();
        if (filter == null || filter == Filter.INCLUDE) {
            int count = (int) getDataStore().getHeaderMeta().featuresCount;
            if (count > 0) {
                return count;
            }
        }

        // bbox filter
        if (filter instanceof BBOX && headerMeta.indexNodeSize > 0) {
            ReferencedEnvelope bounds = ReferencedEnvelope.reference(((BBOX) filter).getBounds());
            if (bounds != null) {
                // first check, if the file bbox is withing the query bbox, then all features match
                Envelope headerEnvelope = headerMeta.envelope;
                if (headerEnvelope != null && bounds.contains(headerEnvelope)) {
                    int count = (int) getDataStore().getHeaderMeta().featuresCount;
                    if (count > 0) {
                        return count;
                    }
                }

                // otherwise scan the index (might be fooled by features crossing the dateline)
                URL url = getDataStore().getURL();
                try (InputStream is = url.openStream()) {
                    FlatGeobufFeatureReader.skipNBytes(is, headerMeta.offset);
                    PackedRTree.SearchResult result = PackedRTree.search(
                            is,
                            headerMeta.offset,
                            (int) headerMeta.featuresCount,
                            headerMeta.indexNodeSize,
                            ReferencedEnvelope.reference(bounds));
                    return result.hits.size();
                }
            }
        }

        // no other optimized paths, give up
        return -1;
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
