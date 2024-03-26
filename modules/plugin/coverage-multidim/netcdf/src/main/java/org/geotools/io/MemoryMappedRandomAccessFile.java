/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.io;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.util.NIOUtilities;
import ucar.unidata.io.RandomAccessFile;
import ucar.unidata.io.spi.RandomAccessFileProvider;
import ucar.unidata.util.StringUtil2;

/**
 * A ReadOnly MemoryMapped RandomAccessFile. It will map the file in memory, using a
 * MappedByteBuffer.
 *
 * <p>Writing capability is not supported at the moment.
 */
public class MemoryMappedRandomAccessFile extends RandomAccessFile {

    private static final int BUFFER_MEMORY_LIMIT;

    private FileChannel channel;

    static {
        long limit = Integer.MAX_VALUE;
        String memoryMappingLimit =
                System.getProperty("org.geotools.coverage.io.netcdf.memorymaplimit");
        if (memoryMappingLimit != null && !memoryMappingLimit.isEmpty()) {
            limit = Long.parseLong(memoryMappingLimit);
            // The mapped byte buffer cannot be bigger than 2GB.
            if (limit < 0 || limit > Integer.MAX_VALUE) {
                limit = Integer.MAX_VALUE;
            }
        }
        BUFFER_MEMORY_LIMIT = (int) limit;
    }

    private MappedByteBuffer mappedByteBuffer;

    private static final int DEFAULT_BUFFER_SIZE = 1;

    private long currentOffset = 0;
    private FileChannel.MapMode mapMode;

    public MemoryMappedRandomAccessFile(String location, String mode) throws IOException {
        super(location, mode, DEFAULT_BUFFER_SIZE);
        if (!readonly) reportReadOnly();
        initDataBuffer();
    }

    private void initDataBuffer() throws IOException {
        channel = file.getChannel();
        mapMode = FileChannel.MapMode.READ_ONLY;
        long channelSize = channel.size();
        dataSize = (int) channelSize;
        dataEnd = channelSize;

        long initialMapSize =
                (channelSize - channel.position()) < (long) BUFFER_MEMORY_LIMIT
                        ? channelSize
                        : BUFFER_MEMORY_LIMIT;
        mappedByteBuffer = channel.map(mapMode, 0, initialMapSize);
        mappedByteBuffer.position((int) channel.position());
        bufferStart = 0;
        filePosition = 0;
        endOfFile = false;
    }

    @Override
    public void seek(long pos) throws IOException {
        long jumpSize = pos - filePosition;
        filePosition = pos;
        if (filePosition < dataEnd) {
            if (filePosition >= currentOffset + BUFFER_MEMORY_LIMIT
                    || filePosition < currentOffset) {
                // If we are going outside of the currently mapped portion
                // we need to remap the buffer
                bufferCheck(jumpSize);
            } else mappedByteBuffer.position((int) (filePosition - currentOffset));
        } else {
            endOfFile = true;
        }
    }

    @Override
    public int read() throws IOException {
        bufferCheck(1);
        // bufferCheck(dataEnd - filePosition);
        if (filePosition < dataEnd) {
            filePosition++;
            return mappedByteBuffer.get() & 0xff;
        } else {
            return -1;
        }
    }

    private void bufferCheck(long readSize) throws IOException {
        if (mappedByteBuffer.remaining() < readSize || readSize < 0) {
            long fcPosition = filePosition;
            if (dataEnd > fcPosition + BUFFER_MEMORY_LIMIT) {
                currentOffset = fcPosition;
            } else {
                currentOffset = dataEnd - BUFFER_MEMORY_LIMIT;
                // Can't go before the beginning of the file
                if (currentOffset < 0) {
                    currentOffset = 0;
                }
            }
            NIOUtilities.clean(mappedByteBuffer);
            mappedByteBuffer = channel.map(mapMode, currentOffset, BUFFER_MEMORY_LIMIT);
            // make sure to properly re-align the position
            mappedByteBuffer.position((int) (fcPosition - currentOffset));
        }
    }

    @Override
    public int readBytes(byte[] dst, int offset, int length) throws IOException {
        if (endOfFile) return -1;
        length = (int) Math.min(length, dataEnd - filePosition);
        bufferCheck(length);
        if (length > 0) {
            mappedByteBuffer.get(dst, offset, length);
            filePosition += length;
            if (filePosition == dataEnd) {
                endOfFile = true;
            }
        }
        return length;
    }

    @Override
    public void write(int b) {
        reportReadOnly();
    }

    @Override
    public void writeBytes(byte[] dst, int offset, int length) {
        reportReadOnly();
    }

    private void reportReadOnly() {
        throw new UnsupportedOperationException("Read Only MemoryMappedRandomAccessFile");
    }

    @Override
    public long length() {
        return dataEnd;
    }

    @Override
    public synchronized void close() throws IOException {
        super.close();
        if (channel != null && channel.isOpen()) {
            channel.close();
        }
        if (mappedByteBuffer != null) {
            NIOUtilities.clean(mappedByteBuffer, true);
        }

        mappedByteBuffer = null;
        channel = null;
    }

    /**
     * Hook into service provider interface to RandomAccessFileProvider. Register in
     * META-INF.services.ucar.unidata.io.spi.RandomAccessFileProvider
     */
    public static class Provider implements RandomAccessFileProvider {

        @Override
        public boolean isOwnerOf(String location) {
            return NetCDFUtilities.useMemoryMapping() && NetCDFUtilities.isFile(location);
        }

        /** Open a uriString that this Provider is the owner of. */
        @Override
        public RandomAccessFile open(String uriString) throws IOException {
            uriString = StringUtil2.replace(uriString, '\\', "/");
            if (uriString.startsWith("file:")) {
                uriString = StringUtil2.unescape(uriString.substring(5));
            }
            return new MemoryMappedRandomAccessFile(uriString, "r");
        }
    }
}
