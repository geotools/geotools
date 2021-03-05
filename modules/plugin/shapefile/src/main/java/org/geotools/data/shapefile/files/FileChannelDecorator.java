/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.files;

import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * A FileChannel that delegates all calls to the underlying FileChannel but for {@link
 * #implCloseChannel()} it also calls ShapefileFiles.unlock method to release the lock on the URL.
 *
 * @author jesse
 */
public class FileChannelDecorator extends FileChannel implements ReadableByteChannel {

    private final FileChannel wrapped;
    private final ShpFiles shapefileFiles;
    private final URL url;
    private final FileReader reader;
    private final FileWriter writer;
    private boolean closed;

    public FileChannelDecorator(
            FileChannel channel, ShpFiles shapefileFiles, URL url, FileReader requestor) {
        this.wrapped = channel;
        this.shapefileFiles = shapefileFiles;
        this.url = url;
        this.reader = requestor;
        writer = null;
        this.closed = false;
    }

    public FileChannelDecorator(
            FileChannel channel, ShpFiles shapefileFiles, URL url, FileWriter requestor) {
        this.wrapped = channel;
        this.shapefileFiles = shapefileFiles;
        this.url = url;
        this.writer = requestor;
        reader = null;
    }

    @Override
    public void force(boolean metaData) throws IOException {
        wrapped.force(metaData);
    }

    @Override
    public FileLock lock(long position, long size, boolean shared) throws IOException {
        return wrapped.lock(position, size, shared);
    }

    @Override
    public MappedByteBuffer map(MapMode mode, long position, long size) throws IOException {
        //    	return wrapped.map(mode, position, size)
        return shapefileFiles.map(wrapped, url, mode, position, size);
    }

    @Override
    public long position() throws IOException {
        return wrapped.position();
    }

    @Override
    public FileChannel position(long newPosition) throws IOException {
        return wrapped.position(newPosition);
    }

    @Override
    public int read(ByteBuffer dst, long position) throws IOException {
        return wrapped.read(dst, position);
    }

    @Override
    public int read(ByteBuffer dst) throws IOException {
        return wrapped.read(dst);
    }

    @Override
    public long read(ByteBuffer[] dsts, int offset, int length) throws IOException {
        return wrapped.read(dsts, offset, length);
    }

    @Override
    public long size() throws IOException {
        return wrapped.size();
    }

    @Override
    public long transferFrom(ReadableByteChannel src, long position, long count)
            throws IOException {
        return wrapped.transferFrom(src, position, count);
    }

    @Override
    public long transferTo(long position, long count, WritableByteChannel target)
            throws IOException {
        return wrapped.transferTo(position, count, target);
    }

    @Override
    public FileChannel truncate(long size) throws IOException {
        return wrapped.truncate(size);
    }

    @Override
    public FileLock tryLock(long position, long size, boolean shared) throws IOException {
        return wrapped.tryLock(position, size, shared);
    }

    @Override
    public int write(ByteBuffer src, long position) throws IOException {
        return wrapped.write(src, position);
    }

    @Override
    public int write(ByteBuffer src) throws IOException {
        return wrapped.write(src);
    }

    @Override
    public long write(ByteBuffer[] srcs, int offset, int length) throws IOException {
        return wrapped.write(srcs, offset, length);
    }

    @Override
    protected void implCloseChannel() throws IOException {
        try {
            wrapped.close();
        } finally {
            if (!closed) {
                closed = true;
                if (reader != null) {
                    shapefileFiles.unlockRead(url, reader);
                } else {
                    shapefileFiles.unlockWrite(url, writer);
                }
            }
        }
    }
}
