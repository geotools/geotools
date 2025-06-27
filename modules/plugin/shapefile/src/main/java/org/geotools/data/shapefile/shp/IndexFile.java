/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.shp;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Logger;
import org.geotools.data.shapefile.files.FileReader;
import org.geotools.data.shapefile.files.ShpFileType;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.files.StreamLogging;
import org.geotools.util.NIOUtilities;

/**
 * IndexFile parser for .shx files.<br>
 * For now, the creation of index files is done in the ShapefileWriter. But this can be used to access the index.<br>
 * For details on the index file, see <br>
 * <a href="http://www.esri.com/library/whitepapers/pdfs/shapefile.pdf"><b>"ESRI(r) Shapefile - A Technical
 * Description"</b><br>
 * * <i>'An ESRI White Paper . May 1997'</i></a>
 *
 * @author Ian Schneider
 */
public class IndexFile implements FileReader, AutoCloseable {
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(IndexFile.class);

    private static final int RECS_IN_BUFFER = 2000;

    private boolean useMemoryMappedBuffer;
    private FileChannel channel;
    private int channelOffset;
    private ByteBuffer buf = null;
    private int lastIndex = -1;
    private int recOffset;
    private int recLen;
    private ShapefileHeader header = null;
    private int[] content;
    private StreamLogging streamLogger = new StreamLogging("IndexFile");

    private volatile boolean closed = false;

    /**
     * Load the index file from the given channel.
     *
     * @param shpFiles The channel to read from.
     * @throws IOException If an error occurs.
     */
    @SuppressWarnings("PMD.CloseResource") // file channel managed as a resource
    public IndexFile(ShpFiles shpFiles, boolean useMemoryMappedBuffer) throws IOException {
        this.useMemoryMappedBuffer = useMemoryMappedBuffer;
        streamLogger.open();
        ReadableByteChannel byteChannel = shpFiles.getReadChannel(ShpFileType.SHX, this);
        try {
            if (byteChannel instanceof FileChannel) {
                this.channel = (FileChannel) byteChannel;
                if (useMemoryMappedBuffer) {
                    LOGGER.finest("Memory mapping file...");
                    this.buf = this.channel.map(FileChannel.MapMode.READ_ONLY, 0, this.channel.size());

                    this.channelOffset = 0;
                } else {
                    LOGGER.finest("Reading from file...");
                    this.buf = NIOUtilities.allocate(8 * RECS_IN_BUFFER);
                    channel.read(buf);
                    buf.flip();
                    this.channelOffset = 0;
                }

                header = new ShapefileHeader();
                header.read(buf, true);
            } else {
                LOGGER.finest("Loading all shx...");
                readHeader(byteChannel);
                readRecords(byteChannel);
                byteChannel.close();
            }
        } catch (Throwable e) {
            if (byteChannel != null) {
                byteChannel.close();
            }
            throw (IOException) new IOException(e.getLocalizedMessage()).initCause(e);
        }
    }

    /**
     * Get the header of this index file.
     *
     * @return The header of the index file.
     */
    public ShapefileHeader getHeader() {
        return header;
    }

    private void check() {
        if (closed) {
            throw new IllegalStateException("Index file has been closed");
        }
    }

    private void readHeader(ReadableByteChannel channel) throws IOException {
        ByteBuffer buffer = NIOUtilities.allocate(100);
        try {
            while (buffer.remaining() > 0) {
                channel.read(buffer);
            }
            buffer.flip();
            header = new ShapefileHeader();
            header.read(buffer, true);
        } finally {
            NIOUtilities.clean(buffer, false);
        }
    }

    private void readRecords(ReadableByteChannel channel) throws IOException {
        check();
        int remaining = header.getFileLength() * 2 - 100;
        ByteBuffer buffer = NIOUtilities.allocate(remaining);
        try {
            buffer.order(ByteOrder.BIG_ENDIAN);
            while (buffer.remaining() > 0) {
                channel.read(buffer);
            }
            buffer.flip();
            int records = remaining / 4;
            content = new int[records];
            IntBuffer ints = buffer.asIntBuffer();
            ints.get(content);
        } finally {
            NIOUtilities.clean(buffer, false);
        }
    }

    private void readRecord(int index) throws IOException {
        check();
        int pos = 100 + index * 8;
        if (!this.useMemoryMappedBuffer) {
            if (pos - this.channelOffset < 0 || this.channelOffset + buf.limit() <= pos || this.lastIndex == -1) {
                LOGGER.finest("Filling buffer...");
                this.channelOffset = pos;
                this.channel.position(pos);
                buf.clear();
                this.channel.read(buf);
                buf.flip();
            }
        }

        buf.position(pos - this.channelOffset);
        this.recOffset = buf.getInt();
        this.recLen = buf.getInt();
        this.lastIndex = index;
    }

    @Override
    public void close() throws IOException {
        closed = true;
        if (channel != null && channel.isOpen()) {
            channel.close();
            streamLogger.close();

            NIOUtilities.clean(buf, useMemoryMappedBuffer);
        }
        this.buf = null;
        this.content = null;
        this.channel = null;
    }

    /** @see java.lang.Object#finalize() */
    @Override
    @SuppressWarnings("deprecation") // finalize is deprecated in Java 9
    protected void finalize() throws Throwable {
        this.close();
        super.finalize();
    }

    /**
     * Get the number of records in this index.
     *
     * @return The number of records.
     */
    public int getRecordCount() {
        return (header.getFileLength() * 2 - 100) / 8;
    }

    /**
     * Get the offset of the record (in 16-bit words).
     *
     * @param index The index, from 0 to getRecordCount - 1
     * @return The offset in 16-bit words.
     */
    public int getOffset(int index) throws IOException {
        int ret = -1;

        if (this.channel != null) {
            if (this.lastIndex != index) {
                this.readRecord(index);
            }

            ret = this.recOffset;
        } else {
            ret = content[2 * index];
        }

        return ret;
    }

    /**
     * Get the offset of the record (in real bytes, not 16-bit words).
     *
     * @param index The index, from 0 to getRecordCount - 1
     * @return The offset in bytes.
     */
    public int getOffsetInBytes(int index) throws IOException {
        return this.getOffset(index) * 2;
    }

    /**
     * Get the content length of the given record in bytes, not 16 bit words.
     *
     * @param index The index, from 0 to getRecordCount - 1
     * @return The lengh in bytes of the record.
     */
    public int getContentLength(int index) throws IOException {
        int ret = -1;

        if (this.channel != null) {
            if (this.lastIndex != index) {
                this.readRecord(index);
            }

            ret = this.recLen;
        } else {
            ret = content[2 * index + 1];
        }

        return ret;
    }

    @Override
    public String id() {
        return getClass().getName();
    }
}
