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

import com.google.flatbuffers.FlatBufferBuilder;
import com.google.flatbuffers.FlatBufferBuilder.ByteBufferFactory;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import org.geotools.util.NIOUtilities;

/**
 * Utility class to create and release the internal byte buffers of a {@link FlatBufferBuilder}.
 *
 * <p>In order to either release or return to the cache the used buffers as soon as possible, use
 * {@link #newBuilder(int)} and {@link #release(FlatBufferBuilder)} as soon as possible, instead of
 * expecting the GC to do it.
 *
 * @implNote Currently, the returned {@link FlatBufferBuilder builders} use a {@link
 *     ByteBufferFactory} that uses GeoTools {@link NIOUtilities} cached buffers. This means that
 *     the buffers will be reused, but also that they'll be direct or heap buffers depending on
 *     {@link NIOUtilities#isDirectBuffersEnabled()}, hence the importance of calling {@link
 *     #release(FlatBufferBuilder)} as soon as the builder is to be disposed, to the GeoTools
 *     utility can take care of cleaning up the direct buffers when needed.
 */
public class FlatBuffers {

    private static class ReleasingByteBufferFactory extends ByteBufferFactory {

        private ByteBuffer lastBuffer;

        @Override
        public ByteBuffer newByteBuffer(int capacity) {
            ByteBuffer buff = NIOUtilities.allocate(capacity);
            buff.order(ByteOrder.LITTLE_ENDIAN);
            lastBuffer = buff;
            return lastBuffer;
        }

        @Override
        public void releaseByteBuffer(ByteBuffer bb) {
            NIOUtilities.returnToCache(bb);
        }

        public void release() {
            if (lastBuffer != null) {
                releaseByteBuffer(lastBuffer);
                lastBuffer = null;
            }
        }
    }
    ;

    private static class ReleasingFlatBufferBuilder extends FlatBufferBuilder {

        private ReleasingByteBufferFactory factory;

        public ReleasingFlatBufferBuilder(int initialSize, ReleasingByteBufferFactory factory) {
            super(initialSize, factory);
            this.factory = factory;
        }

        public void releaseBuffer() {
            factory.release();
        }
    }

    public static FlatBufferBuilder newBuilder(int minimumCapacity) {
        return new ReleasingFlatBufferBuilder(minimumCapacity, new ReleasingByteBufferFactory());
    }

    public static void release(FlatBufferBuilder builder) {
        if (builder instanceof ReleasingFlatBufferBuilder) {
            ((ReleasingFlatBufferBuilder) builder).releaseBuffer();
        }
    }
}
