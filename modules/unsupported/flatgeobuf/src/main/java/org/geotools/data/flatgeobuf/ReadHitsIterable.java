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

import com.google.common.io.LittleEndianDataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.PackedRTree.SearchHit;

final class ReadHitsIterable implements Iterable<SimpleFeature> {
    private final class IteratorImplementation implements Iterator<SimpleFeature> {
        int i = 0;
        long pos = featuresOffset;

        @Override
        public boolean hasNext() {
            return i < hits.size();
        }

        @Override
        public SimpleFeature next() {
            if (!hasNext()) throw new NoSuchElementException();
            SearchHit hit = hits.get(i);
            long skip = hit.offset - (pos - featuresOffset);
            try {
                FlatGeobufFeatureReader.skipNBytes(data, skip);
                pos += skip;
                int featureSize = data.readInt();
                pos += 4;
                SimpleFeature feature =
                        FeatureConversions.deserialize(
                                data, fb, headerMeta, hit.index, featureSize);
                pos += featureSize;
                i++;
                return feature;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final SimpleFeatureBuilder fb;
    private final ArrayList<SearchHit> hits;
    private final HeaderMeta headerMeta;
    private final int featuresOffset;
    private final LittleEndianDataInputStream data;

    ReadHitsIterable(
            SimpleFeatureBuilder fb,
            ArrayList<SearchHit> hits,
            HeaderMeta headerMeta,
            int featuresOffset,
            LittleEndianDataInputStream data) {
        this.fb = fb;
        this.hits = hits;
        this.headerMeta = headerMeta;
        this.featuresOffset = featuresOffset;
        this.data = data;
    }

    @Override
    public Iterator<SimpleFeature> iterator() {
        Iterator<SimpleFeature> it = new IteratorImplementation();
        return it;
    }
}
