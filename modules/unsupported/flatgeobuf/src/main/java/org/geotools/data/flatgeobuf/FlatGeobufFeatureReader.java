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

        Envelope bbox = new ReferencedEnvelope();
        if (q != null && q.getFilter() != null) {
            bbox = (Envelope) q.getFilter().accept(ExtractBoundsFilterVisitor.BOUNDS_VISITOR, bbox);
        }
        if (bbox.isNull()
                || Double.isInfinite(bbox.getWidth())
                || Double.isInfinite(bbox.getHeight())) {
            bbox = null;
        }

        it =
                FeatureCollectionConversions.deserialize(inputStream, headerMeta, featureType, bbox)
                        .iterator();
    }

    public static void skipNBytes(InputStream stream, long skip) throws IOException {
        long actual = 0;
        long remaining = skip;
        while (actual < remaining) {
            remaining -= stream.skip(remaining);
        }
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
            if (isEmpty) {
                return false;
            }
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
