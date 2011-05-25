/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile.indexed;

import static org.geotools.data.shapefile.ShpFileType.FIX;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

import org.geotools.data.shapefile.FileWriter;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.StorageFile;
import org.geotools.data.shapefile.StreamLogging;
import org.geotools.resources.NIOUtilities;

/**
 * The Writer writes out the fid and record number of features to the fid index file.
 * 
 * @author Jesse
 *
 *
 * @source $URL$
 */
public class IndexedFidWriter implements FileWriter {
    public static final int HEADER_SIZE = 13;
    public static final int RECORD_SIZE = 12;
    private FileChannel channel;
    private ByteBuffer writeBuffer;
    private IndexedFidReader reader;
    long fidIndex;
    private int recordIndex;
    private boolean closed;

    private long current;

    private long position;
    private int removes;
    StreamLogging streamLogger = new StreamLogging("IndexedFidReader");
    private StorageFile storageFile;

    /**
     * Creates a new instance and writes the fids to a storage file which is replaces the original
     * on close().
     * 
     * @param shpFiles The shapefiles to used
     * @throws IOException
     */
    public IndexedFidWriter( ShpFiles shpFiles ) throws IOException {
        storageFile = shpFiles.getStorageFile(FIX);
        init(shpFiles, storageFile);
    }

    /**
     * Create a new instance<br>
     * Note: {@link StorageFile#replaceOriginal()} is NOT called.  Call {@link #IndexedFidWriter(ShpFiles)} for that 
     * behaviour.  
     * @param shpFiles The shapefiles to used
     * @param storageFile the storage file that will be written to.  It will NOT be closed.
     * @throws IOException
     */
    public IndexedFidWriter( ShpFiles shpFiles, StorageFile storageFile ) throws IOException {
        // Note do NOT assign storageFile so that it is closed because this method method requires that
        // the caller close the storage file.
        // Call the single argument constructor instead
        init(shpFiles, storageFile);
    }

    private void init( ShpFiles shpFiles, StorageFile storageFile ) throws IOException {
        if (!shpFiles.isLocal()) {
            throw new IllegalArgumentException(
                    "Currently only local files are supported for writing");
        }


        try {
            reader = new IndexedFidReader(shpFiles);
        } catch (FileNotFoundException e) {
            reader = new IndexedFidReader(shpFiles, storageFile.getWriteChannel());
        }

        this.channel = storageFile.getWriteChannel();
        streamLogger.open();
        allocateBuffers();
        removes = reader.getRemoves();
        writeBuffer.position(HEADER_SIZE);
        closed = false;
        position = 0;
        current = -1;
        recordIndex = 0;
        fidIndex = 0;
    }

    private IndexedFidWriter() {
    }

    /**
     * Allocate some buffers for writing.
     */
    private void allocateBuffers() {
        writeBuffer = NIOUtilities.allocate(HEADER_SIZE + RECORD_SIZE * 1024);
    }

    /**
     * Drain internal buffers into underlying channels.
     * 
     * @throws IOException DOCUMENT ME!
     */
    private void drain() throws IOException {
        writeBuffer.flip();

        int written = 0;

        while( writeBuffer.remaining() > 0 )
            written += channel.write(writeBuffer, position);

        position += written;

        writeBuffer.flip().limit(writeBuffer.capacity());
    }

    private void writeHeader() throws IOException {
        ByteBuffer buffer = NIOUtilities.allocate(HEADER_SIZE);
        
        try {
            buffer.put((byte) 1);
    
            buffer.putLong(recordIndex);
            buffer.putInt(removes);
            buffer.flip();
            channel.write(buffer, 0);
        } finally {
            NIOUtilities.clean(buffer, false);
        }
    }

    public boolean hasNext() throws IOException {
        return reader.hasNext();
    }

    public long next() throws IOException {

        if (current != -1)
            write();

        if (reader.hasNext()) {
            reader.next();
            fidIndex = reader.getCurrentFIDIndex();
        } else {
            fidIndex++;
        }

        current = fidIndex;

        return fidIndex;
    }

    public void close() throws IOException {
        if (closed) {
            return;
        }

        try {

            finishLastWrite();
        } finally {
            try {
                reader.close();
            } finally {
                closeWriterChannels();
            }
            if (storageFile != null) {
                storageFile.replaceOriginal();
            }
        }

        closed = true;
    }

    private void closeWriterChannels() throws IOException {
        if (channel.isOpen())
            channel.close();
        streamLogger.close();
        if (writeBuffer != null) {
            NIOUtilities.clean(writeBuffer, false);
            writeBuffer = null;
        }

    }

    private void finishLastWrite() throws IOException {
        while( hasNext() )
            next();

        if (current != -1)
            write();

        drain();
        writeHeader();
    }

    /**
     * Increments the fidIndex by 1. Indicates that a feature was removed from the location. This is
     * intended to ensure that FIDs stay constant over time. Consider the following case of 5
     * features. feature 1 has fid typename.0 feature 2 has fid typename.1 feature 3 has fid
     * typename.2 feature 4 has fid typename.3 feature 5 has fid typename.4 when feature 3 is
     * removed/deleted the following usage of the write should take place: next(); (move to feature
     * 1) next(); (move to feature 2) next(); (move to feature 3) remove();(delete feature 3)
     * next(); (move to feature 4) // optional write(); (write feature 4) next(); (move to feature
     * 5) write(); (write(feature 5)
     * 
     * @throws IOException
     */
    public void remove() throws IOException {
        if (current == -1)
            throw new IOException("Current fid index is null, next must be called before remove");
        if (hasNext()) {
            removes++;
            current = -1;

            // reader.next();
        }
    }

    /**
     * Writes the current fidIndex. Writes to the same place in the file each time. Only
     * {@link #next()} moves forward in the file.
     * 
     * @throws IOException
     * @see #next()
     * @see #remove()
     */
    public void write() throws IOException {
        if (current == -1)
            throw new IOException("Current fid index is null, next must be called before write()");

        if (writeBuffer == null) {
            allocateBuffers();
        }

        if (writeBuffer.remaining() < RECORD_SIZE) {
            drain();
        }

        writeBuffer.putLong(current);
        writeBuffer.putInt(recordIndex);

        recordIndex++;
        current = -1;
    }

    public boolean isClosed() {
        return closed;
    }

    public String id() {
        return getClass().getName();
    }

    public static final IndexedFidWriter EMPTY_WRITER = new IndexedFidWriter(){
        @Override
        public void close() throws IOException {
        }
        @Override
        public boolean hasNext() throws IOException {
            return false;
        }
        @Override
        public boolean isClosed() {
            return false;
        }
        @Override
        public void write() throws IOException {
        }
        @Override
        public long next() throws IOException {
            return 0;
        }
        @Override
        public void remove() throws IOException {
        }
    };
}
