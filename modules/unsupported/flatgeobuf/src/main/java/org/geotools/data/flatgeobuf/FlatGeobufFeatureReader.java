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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentState;
import org.geotools.filter.visitor.ExtractBoundsFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.identity.Identifier;
import org.wololo.flatgeobuf.HeaderMeta;

public class FlatGeobufFeatureReader implements FeatureReader<SimpleFeatureType, SimpleFeature> {

    final Iterator<SimpleFeature> it;
    final SimpleFeatureType featureType;
    final boolean isEmpty;

    InputStream inputStream;
    SimpleFeature nextFeature;

    FlatGeobufFeatureReader(ContentState state, Query q) throws IOException {
        this(state, q, null);
    }

    FlatGeobufFeatureReader(ContentState state, Query q, HeaderMeta headerMeta) throws IOException {
        this.featureType = state.getFeatureType();

        DataStore dataStore = state.getEntry().getDataStore();
        File file;
        URL url;
        if (dataStore instanceof FlatGeobufDirectoryDataStore) {
            file =
                    ((FlatGeobufDirectoryDataStore) dataStore)
                            .getDataStore(featureType.getTypeName())
                            .getFile();
            url =
                    ((FlatGeobufDirectoryDataStore) dataStore)
                            .getDataStore(featureType.getTypeName())
                            .getURL();
        } else {
            file = ((FlatGeobufDataStore) dataStore).getFile();
            url = ((FlatGeobufDataStore) dataStore).getURL();
        }

        if (file != null && !file.exists()) {
            isEmpty = true;
            it = null;
            return;
        } else {
            isEmpty = false;
        }

        inputStream = url.openStream();

        if (headerMeta == null) {
            headerMeta = HeaderMeta.read(inputStream);
        } else {
            skipNBytes(inputStream, headerMeta.offset);
        }

        Filter filter = q.getFilter();
        Envelope bbox = new ReferencedEnvelope();
        Id id = null;
        if (q != null && filter != null) {
            bbox = (Envelope) filter.accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);
            if (filter instanceof Id) id = (Id) filter;
        }
        if (bbox == null
                || bbox.isNull()
                || Double.isInfinite(bbox.getWidth())
                || Double.isInfinite(bbox.getHeight())) {
            bbox = null;
        }
        if (bbox != null)
            it =
                    FeatureCollectionConversions.deserialize(
                                    inputStream, headerMeta, featureType, bbox)
                            .iterator();
        else if (id != null) {
            long featuresCount = headerMeta.featuresCount;
            long[] fids =
                    id.getIdentifiers().stream()
                            .mapToLong(i -> extractFid(i))
                            .filter(l -> l >= 0 && l < featuresCount)
                            .toArray();
            Arrays.sort(fids);
            it =
                    FeatureCollectionConversions.deserialize(
                                    inputStream, headerMeta, featureType, fids)
                            .iterator();
        } else {
            it =
                    FeatureCollectionConversions.deserialize(inputStream, headerMeta, featureType)
                            .iterator();
        }
    }

    private static long extractFid(Identifier i) {
        long fid = -1;
        String idStr = i.getID().toString();
        int dotIndex = idStr.indexOf(".", 0);
        String idPart = idStr.substring(dotIndex + 1);
        try {
            fid = Long.parseLong(idPart);
        } catch (NumberFormatException e) {
        }
        return fid;
    }

    public static void skipNBytes(InputStream stream, long skip) throws IOException {
        long actual = 0;
        long remaining = skip;
        while (actual < remaining) remaining -= stream.skip(remaining);
    }

    @Override
    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    @Override
    public SimpleFeature next()
            throws IOException, IllegalArgumentException, NoSuchElementException {
        SimpleFeature feature = null;
        if (nextFeature != null) {
            feature = nextFeature;
            nextFeature = null;
        } else {
            if (isEmpty) throw new NoSuchElementException();
            feature = it.next();
        }
        return feature;
    }

    @Override
    public boolean hasNext() throws IOException {
        if (nextFeature != null) {
            return true;
        } else {
            if (isEmpty) return false;
            if (it.hasNext()) {
                nextFeature = it.next();
                return true;
            } else {
                return false;
            }
        }
    }

    @Override
    public void close() throws IOException {
        if (inputStream != null) inputStream.close();
    }
}
