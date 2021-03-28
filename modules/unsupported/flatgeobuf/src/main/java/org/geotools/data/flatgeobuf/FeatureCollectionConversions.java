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
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.NoSuchElementException;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.wololo.flatgeobuf.Constants;
import org.wololo.flatgeobuf.HeaderMeta;
import org.wololo.flatgeobuf.PackedRTree;
import org.wololo.flatgeobuf.PackedRTree.SearchHit;
import org.wololo.flatgeobuf.PackedRTree.SearchResult;
import org.wololo.flatgeobuf.generated.Feature;

public class FeatureCollectionConversions {

    private static final class ReadHitsIterable implements Iterable<SimpleFeature> {
        private final SimpleFeatureBuilder fb;
        private final ArrayList<SearchHit> hits;
        private final HeaderMeta headerMeta;
        private final int featuresOffset;
        private final LittleEndianDataInputStream data;

        private ReadHitsIterable(
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
            Iterator<SimpleFeature> it =
                    new Iterator<SimpleFeature>() {
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
                                byte[] bytes = new byte[featureSize];
                                data.readFully(bytes);
                                pos += featureSize;
                                ByteBuffer bb = ByteBuffer.wrap(bytes);
                                Feature f = Feature.getRootAsFeature(bb);
                                SimpleFeature feature =
                                        FeatureConversions.deserialize(
                                                f, fb, headerMeta, hit.index);
                                i++;
                                return feature;
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    };
            return it;
        }
    }

    private static final class ReadAllInterable implements Iterable<SimpleFeature> {
        private final HeaderMeta headerMeta;
        private final LittleEndianDataInputStream data;
        private final SimpleFeatureBuilder fb;

        private ReadAllInterable(
                HeaderMeta headerMeta, LittleEndianDataInputStream data, SimpleFeatureBuilder fb) {
            this.headerMeta = headerMeta;
            this.data = data;
            this.fb = fb;
        }

        @Override
        public Iterator<SimpleFeature> iterator() {
            Iterator<SimpleFeature> it =
                    new Iterator<SimpleFeature>() {
                        long count = 0;
                        SimpleFeature feature;
                        SimpleFeature nextFeature;
                        boolean done = false;

                        @Override
                        public boolean hasNext() {
                            if (done) return false;
                            if (nextFeature != null) {
                                feature = nextFeature;
                                nextFeature = null;
                            } else {
                                try {
                                    nextFeature = next();
                                } catch (NoSuchElementException e) {
                                    done = true;
                                    return false;
                                }
                                return true;
                            }
                            return true;
                        }

                        @Override
                        public SimpleFeature next() {
                            if (nextFeature != null) {
                                feature = nextFeature;
                                nextFeature = null;
                            } else {
                                int featureSize;
                                try {
                                    featureSize = data.readInt();
                                    byte[] bytes = new byte[featureSize];
                                    data.readFully(bytes);
                                    ByteBuffer bb = ByteBuffer.wrap(bytes);
                                    Feature f = Feature.getRootAsFeature(bb);
                                    feature =
                                            FeatureConversions.deserialize(
                                                    f, fb, headerMeta, count++);
                                } catch (IOException e) {
                                    throw new NoSuchElementException();
                                }
                            }
                            return feature;
                        }
                    };
            return it;
        }
    }

    public static void serialize(
            SimpleFeatureCollection featureCollection,
            long featuresCount,
            OutputStream outputStream)
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

    public static SimpleFeatureCollection deserialize(InputStream stream) throws IOException {
        HeaderMeta headerMeta = HeaderMeta.read(stream);
        SimpleFeatureType featureType = HeaderMetaUtil.toFeatureType(headerMeta, "unknown");
        return deserialize(stream, headerMeta, featureType);
    }

    public static Iterable<SimpleFeature> deserialize(InputStream stream, Envelope rect)
            throws IOException {
        HeaderMeta headerMeta = HeaderMeta.read(stream);
        SimpleFeatureType featureType = HeaderMetaUtil.toFeatureType(headerMeta, "unknown");
        return deserialize(stream, headerMeta, featureType, rect);
    }

    public static SimpleFeatureCollection deserialize(
            InputStream stream, HeaderMeta headerMeta, SimpleFeatureType ft) throws IOException {
        Iterator<SimpleFeature> it = deserialize(stream, headerMeta, ft, null).iterator();
        MemoryFeatureCollection fc = new MemoryFeatureCollection(ft);
        while (it.hasNext()) fc.add(it.next());
        return fc;
    }

    public static Iterable<SimpleFeature> deserialize(
            InputStream stream, HeaderMeta headerMeta, SimpleFeatureType ft, Envelope rect)
            throws IOException {
        int treeSize =
                headerMeta.featuresCount > 0 && headerMeta.indexNodeSize > 0
                        ? (int)
                                PackedRTree.calcSize(
                                        (int) headerMeta.featuresCount, headerMeta.indexNodeSize)
                        : 0;
        int featuresOffset = headerMeta.offset + treeSize;
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(ft);
        LittleEndianDataInputStream data = new LittleEndianDataInputStream(stream);

        Iterable<SimpleFeature> iterable;
        if (rect == null) {
            if (treeSize > 0) {
                FlatGeobufFeatureReader.skipNBytes(data, treeSize);
            }
            iterable = new ReadAllInterable(headerMeta, data, fb);
        } else {
            SearchResult result =
                    PackedRTree.search(
                            data,
                            headerMeta.offset,
                            (int) headerMeta.featuresCount,
                            headerMeta.indexNodeSize,
                            rect);
            int skip = treeSize - result.pos;
            if (skip > 0) {
                FlatGeobufFeatureReader.skipNBytes(data, treeSize - result.pos);
            }
            iterable = new ReadHitsIterable(fb, result.hits, headerMeta, featuresOffset, data);
        }

        return iterable;
    }
}
