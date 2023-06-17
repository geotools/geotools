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
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.PackedRTree;

final class ReadFidsIterable implements Iterable<SimpleFeature> {
    private final class IteratorImplementation implements Iterator<SimpleFeature> {
        int i = 0;
        long pos = 0;

        @Override
        public boolean hasNext() {
            return i < featureOffsets.length;
        }

        @Override
        public SimpleFeature next() {
            if (!hasNext()) throw new NoSuchElementException();
            try {
                long delta = featureOffsets[i] - pos;
                FlatGeobufFeatureReader.skipNBytes(data, delta);
                int featureSize = data.readInt();
                SimpleFeature feature =
                        FeatureConversions.deserialize(data, fb, headerMeta, fids[i], featureSize);
                pos += delta + 4 + featureSize;
                i++;
                return feature;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private final SimpleFeatureBuilder fb;
    private final long[] fids;
    private final long[] featureOffsets;
    private final HeaderMeta headerMeta;
    private final LittleEndianDataInputStream data;

    ReadFidsIterable(
            SimpleFeatureBuilder fb,
            long[] fids,
            HeaderMeta headerMeta,
            LittleEndianDataInputStream data)
            throws IOException {
        this.fb = fb;
        this.fids = fids;
        this.featureOffsets = PackedRTree.readFeatureOffsets(data, fids, headerMeta);
        this.headerMeta = headerMeta;
        this.data = data;
    }

    @Override
    public Iterator<SimpleFeature> iterator() {
        Iterator<SimpleFeature> it = new IteratorImplementation();
        return it;
    }
}
