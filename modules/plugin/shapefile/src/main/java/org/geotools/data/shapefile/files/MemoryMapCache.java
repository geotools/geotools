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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.NIOUtilities;
import org.geotools.util.SoftValueHashMap;
import org.geotools.util.SoftValueHashMap.ValueCleaner;
import org.geotools.util.URLs;
import org.geotools.util.logging.Logging;

/**
 * A cache for memory mapped buffers, used to avoid generating over and over read only memory mapped buffers. Mapping a
 * file is a synchronized operation, plus by generating light copies the same buffer can be shared by various threads
 *
 * @author Andrea Aime - OpenGeo
 */
class MemoryMapCache {

    static final Logger LOGGER = Logging.getLogger(MemoryMapCache.class);

    SoftValueHashMap<MappingKey, MappedByteBuffer> buffers = new SoftValueHashMap<>(0, new BufferCleaner());

    MappedByteBuffer map(FileChannel wrapped, URL url, MapMode mode, long position, long size) throws IOException {
        if (mode != MapMode.READ_ONLY) {
            return wrapped.map(mode, position, size);
        }

        File file = URLs.urlToFile(url).getCanonicalFile();
        MappingKey mk = new MappingKey(file, position, size);
        MappedByteBuffer buffer = buffers.get(mk);
        if (buffer == null) {
            synchronized (this) {
                buffer = buffers.get(mk);
                if (buffer == null) {
                    buffer = wrapped.map(mode, position, size);
                    buffers.put(mk, buffer);
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Mapping and caching " + file.getAbsolutePath());
                    }
                }
            }
        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Using cached map for " + file.getAbsolutePath());
            }
        }

        return (MappedByteBuffer) buffer.duplicate();
    }

    /**
     * Cleans up all memory mapped regions for a specified file. It is necessary to call this method before any attempt
     * to open a file for writing on Windows
     */
    void cleanFileCache(URL url) {
        try {
            final File rawFile = URLs.urlToFile(url);
            if (rawFile == null) {
                // not a local file
                return;
            }
            File file = rawFile.getCanonicalFile();
            List<MappingKey> keys = new ArrayList<>(buffers.keySet());
            for (MappingKey key : keys) {
                if (key.file.equals(file)) {
                    MappedByteBuffer buffer = buffers.remove(key);
                    NIOUtilities.clean(buffer, true);
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(Level.FINE, "Removed mapping for " + file.getAbsolutePath());
                    }
                }
            }
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "An error occurred while trying to clean the memory map cache", t);
        }
    }

    void clean() {
        List<MappingKey> keys = new ArrayList<>(buffers.keySet());
        for (MappingKey key : keys) {
            MappedByteBuffer buffer = buffers.remove(key);
            NIOUtilities.clean(buffer, true);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Removed mapping for " + key.file.getAbsolutePath());
            }
        }
    }

    /** Tracks a memory mapped region of a certain file */
    static class MappingKey {
        File file;
        long position;
        long size;

        public MappingKey(File file, long position, long size) {
            super();
            this.file = file;
            this.position = position;
            this.size = size;
        }

        @Override
        public int hashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((file == null) ? 0 : file.hashCode());
            result = prime * result + (int) (position ^ (position >>> 32));
            result = prime * result + (int) (size ^ (size >>> 32));
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (obj == null) return false;
            if (getClass() != obj.getClass()) return false;
            MappingKey other = (MappingKey) obj;
            if (file == null) {
                if (other.file != null) return false;
            } else if (!file.equals(other.file)) return false;
            if (position != other.position) return false;
            if (size != other.size) return false;
            return true;
        }
    }

    /**
     * Cleans up the buffers before the soft reference gets deallocated
     *
     * @author Andrea Aime
     */
    public class BufferCleaner implements ValueCleaner<MappingKey, MappedByteBuffer> {

        @Override
        public void clean(MappingKey key, MappedByteBuffer buffer) {
            NIOUtilities.clean(buffer, true);
        }
    }
}
