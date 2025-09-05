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
import com.google.flatbuffers.FlatBufferBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Envelope;
import org.wololo.flatgeobuf.Constants;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.PackedRTree;
import org.wololo.flatgeobuf.PackedRTree.SearchResult;

public class FeatureCollectionConversions {

    public static void serialize(
            SimpleFeatureCollection featureCollection, long featuresCount, OutputStream outputStream)
            throws IOException {
        SimpleFeatureType featureType = featureCollection.getSchema();
        FlatBufferBuilder builder = FlatBuffers.newBuilder(16 * 1024);
        try {
            HeaderMeta headerMeta = HeaderMetaUtil.fromFeatureType(featureType, featuresCount);
            outputStream.write(Constants.MAGIC_BYTES);
            HeaderMeta.write(headerMeta, outputStream, builder);
            builder.clear();
            try (FeatureIterator<SimpleFeature> iterator = featureCollection.features()) {
                while (iterator.hasNext()) {
                    SimpleFeature feature = iterator.next();
                    FeatureConversions.serialize(feature, headerMeta, outputStream, builder);
                    builder.clear();
                }
            }
        } finally {
            FlatBuffers.release(builder);
        }
    }

    public static SimpleFeatureCollection deserializeSFC(InputStream stream) throws IOException {
        HeaderMeta headerMeta = HeaderMeta.read(stream);
        SimpleFeatureType featureType = HeaderMetaUtil.toFeatureType(headerMeta, "unknown");
        return deserializeSFC(stream, headerMeta, featureType);
    }

    public static Iterable<SimpleFeature> deserialize(InputStream stream) throws IOException {
        HeaderMeta headerMeta = HeaderMeta.read(stream);
        SimpleFeatureType featureType = HeaderMetaUtil.toFeatureType(headerMeta, "unknown");
        return deserialize(stream, headerMeta, featureType);
    }

    public static Iterable<SimpleFeature> deserialize(InputStream stream, Envelope rect) throws IOException {
        HeaderMeta headerMeta = HeaderMeta.read(stream);
        SimpleFeatureType featureType = HeaderMetaUtil.toFeatureType(headerMeta, "unknown");
        return deserialize(stream, headerMeta, featureType, rect);
    }

    public static SimpleFeatureCollection deserializeSFC(
            InputStream stream, HeaderMeta headerMeta, SimpleFeatureType ft) throws IOException {
        Iterator<SimpleFeature> it = deserialize(stream, headerMeta, ft).iterator();
        MemoryFeatureCollection fc = new MemoryFeatureCollection(ft);
        while (it.hasNext()) fc.add(it.next());
        return fc;
    }

    public static Iterable<SimpleFeature> deserialize(InputStream stream, long[] fids) throws IOException {
        HeaderMeta headerMeta = HeaderMeta.read(stream);
        SimpleFeatureType featureType = HeaderMetaUtil.toFeatureType(headerMeta, "unknown");
        return deserialize(stream, headerMeta, featureType, fids);
    }

    public static Iterable<SimpleFeature> deserialize(
            InputStream stream, HeaderMeta headerMeta, SimpleFeatureType ft, long[] fids) throws IOException {
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        LittleEndianDataInputStream data = new LittleEndianDataInputStream(stream);
        Iterable<SimpleFeature> it = new ReadFidsIterable(fb, fids, headerMeta, data);
        return it;
    }

    private static long getTreeSize(HeaderMeta headerMeta) {
        long treeSize = headerMeta.featuresCount > 0 && headerMeta.indexNodeSize > 0
                ? PackedRTree.calcSize((int) headerMeta.featuresCount, headerMeta.indexNodeSize)
                : 0;
        return treeSize;
    }

    public static Iterable<SimpleFeature> deserialize(InputStream stream, HeaderMeta headerMeta, SimpleFeatureType ft)
            throws IOException {
        long treeSize = getTreeSize(headerMeta);
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        LittleEndianDataInputStream data = new LittleEndianDataInputStream(stream);

        Iterable<SimpleFeature> iterable;
        if (treeSize > 0) FlatGeobufFeatureReader.skipNBytes(data, treeSize);
        iterable = new ReadAllInterable(headerMeta, data, fb, 0);

        return iterable;
    }

    public static Iterable<SimpleFeature> deserialize(
            InputStream stream, HeaderMeta headerMeta, SimpleFeatureType ft, int startIndex) throws IOException {
        long treeSize = getTreeSize(headerMeta);
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        LittleEndianDataInputStream data = new LittleEndianDataInputStream(stream);
        if (treeSize > 0) {
            if (startIndex >= headerMeta.featuresCount) throw new IndexOutOfBoundsException();
            long[] offsets = PackedRTree.readFeatureOffsets(data, new long[] {startIndex}, headerMeta);
            FlatGeobufFeatureReader.skipNBytes(data, offsets[0]);
        } else {
            startIndex = 0;
        }
        Iterable<SimpleFeature> iterable = new ReadAllInterable(headerMeta, data, fb, startIndex);
        return iterable;
    }

    public static Iterable<SimpleFeature> deserialize(
            InputStream stream, HeaderMeta headerMeta, SimpleFeatureType ft, Envelope rect) throws IOException {
        long treeSize = getTreeSize(headerMeta);
        long featuresOffset = headerMeta.offset + treeSize;
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        LittleEndianDataInputStream data = new LittleEndianDataInputStream(stream);
        Iterable<SimpleFeature> iterable;
        if (headerMeta.indexNodeSize > 1) {
            SearchResult result = PackedRTree.search(
                    data, headerMeta.offset, (int) headerMeta.featuresCount, headerMeta.indexNodeSize, rect);
            long skip = treeSize - result.pos;
            if (skip > 0) FlatGeobufFeatureReader.skipNBytes(data, skip);
            iterable = new ReadHitsIterable(fb, result.hits, headerMeta, featuresOffset, data);
        } else {
            iterable = new ReadAllInterable(headerMeta, data, fb, 0);
        }
        return iterable;
    }
}
