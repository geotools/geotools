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
package org.geotools.data.shapefile;

import static org.geotools.data.shapefile.shp.ShapeType.POINT;
import static org.geotools.data.shapefile.shp.ShapeType.POINTM;
import static org.geotools.data.shapefile.shp.ShapeType.POINTZ;

import java.io.Closeable;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.DoubleBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import java.nio.file.FileStore;
import java.nio.file.FileSystemException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.shapefile.index.quadtree.QuadTree;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.shapefile.shp.ShapeType;
import org.geotools.data.shapefile.shp.ShapefileReader;
import org.geotools.data.shapefile.shp.ShapefileReader.Record;
import org.geotools.util.NIOUtilities;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.index.quadtree.Quadtree;

/**
 * Utility class to assist {@link ShapeFileIndexer} in speeding up the {@link QuadTree} optimization phase through a
 * {@link BoundsReader strategy object} that provides quick access to each shapefile record envelope, potentially
 * avoiding an immense amount of random disk I/O calls through {@link ShapefileReader}, as the quad tree internal nodes
 * get split/shrank.
 *
 * <p>Since the {@link QuadTree} leaf nodes hold only the shapefile record ids, and not their bounds, the tree layout
 * optimization phase may incur into too much random disk reads on the {@code .shp} file.
 *
 * <p>The strategy object {@link #create provided} is meant to avoid that to the extent possible.
 *
 * <p>To a given point, record bounds will be stored in heap memory (up to 1MiB, accounting for 32K records, or 64K
 * records if it's a points shapefile).
 *
 * <p>For a bigger number of {@link IndexFile#getRecordCount() shapefile records}, the strategy is to store the bounds
 * in a temporary file (named {@code GeoTools_shp_qix_bounds_<random number>.tmp} under {@code java.io.tmpdir}), which
 * is memory mapped and deleted at {@link BoundsReader#close()}. This leverages the Operating System's native paging,
 * and due to the reduced size of the bounds file compared to the actual {@code.shp} and avoiding the parsing performed
 * by {@link ShapefileReader#nextRecord()}, results in dramatically less random I/O and computing.
 *
 * <p>Note, however, that if there's not enough temporary space in the file system where the {@code java.io.tmpdir}
 * directory resides, a fall back strategy that reads directly from the {@link ShapefileReader} will be used. This
 * should be a very edge case though, since with a bounds record size of 32 bytes, the required temporary storage is
 * 30.1MiB per million features.
 */
class ShapefileIndexerBoundsHelper {

    private static final Logger LOGGER = Logging.getLogger(ShapefileIndexerBoundsHelper.class);

    /**
     * A record count threshold past which bounds storage falls back to a temporary file.
     *
     * <p>Set to the equivalent of 1MB of heap memory, accounting for 32768 records (for a 32 byte serialized envelope
     * size), or 65536 records if the shapefile geometry type is POINT (using 16 bytes per envelope).
     */
    private static final int FALLBACK_TO_FILE_REC_COUNT_THRESHOLD = 1024 * 1024 / 32;

    /**
     * Returns a bounds reader strategy following the logic explained at the class' javadocs.
     *
     * @param reader the shapefile reader used to build the QuadTree
     * @param shpIndex provided shapefile record count, and file offsets if necesasry
     * @return a reader optimized to quickly store and serve the shapefile records bounds, may fall back to random disk
     *     I/O on the {@code .shp} itself through {@link ShapefileReader}.
     */
    static BoundsReader createBoundsReader(final ShapefileReader reader, final IndexFile shpIndex) {
        final int numRecs = shpIndex.getRecordCount();
        final ShapeType shapeType = reader.getHeader().getShapeType();
        final boolean pointBounds = shapeType == POINT || shapeType == POINTM || shapeType == POINTZ;
        final int fileRecCountThreshold = (pointBounds ? 2 : 1) * FALLBACK_TO_FILE_REC_COUNT_THRESHOLD;

        if (numRecs <= fileRecCountThreshold) {
            return new HeapBoundsReader(numRecs, pointBounds);
        }
        try {
            return new FileBoundsReader(numRecs, pointBounds);
        } catch (IOException e) {
            LOGGER.log(
                    Level.INFO,
                    "Unable to create a temporary file backed bounds reader, falling back to slower ShapefileReader random access",
                    e);
            return new ShapefileReaderBoundsReader(reader, shpIndex);
        }
    }

    /**
     * Strategy to save the {@code .shp} records bounds while building the {@link QuadTree} and quickly access them when
     * optimizing its layout.
     *
     * <p>While building the in memory {@link Quadtree}, the {@link ShapefileReader} is traversed sequentially. At that
     * point, {@link #insert(int, Envelope)} should be called with each record's index and bounds, for this strategy
     * object to save the bounds in its internal storage.
     *
     * <p>While optimizing the {@link QuadTree} layout, {@link #read(int, Envelope)} and {@link #expandEnvelope(int,
     * Envelope)} provided quick access to each record bounds.
     *
     * <p>Finally, {@link #close()} must be called to release any internal resources being used.
     */
    static interface BoundsReader extends Closeable {

        /** Records the bounds of the given shapefile record to be read after. */
        void insert(int recNumber, Envelope env);

        /** Initializes the provided envelope to the bounds of the requested shapefile record {@code recNumber}. */
        void read(int recNumber, Envelope env) throws IOException;

        /** Expands the provided envelope to cover the bounds of the shapefile record {@code recNumber}. */
        void expandEnvelope(int recNumber, Envelope env) throws IOException;

        /** Releases the underlying bounds storage and any other resource being used to such effect. */
        @Override
        default void close() throws IOException {}
    }

    /**
     * A fallback strategy to use {@link ShapefileReader}'s random access to records through (e.g.
     * {@link ShapefileReader#recordAt(int)}).
     *
     * <p>Note {@link #insert(int, Envelope)} is a no-op operation, {@link #read} and {@link #expand} will incur into
     * random disk reads as they position on the {@code .shp} record index and read them to obtain each record bounds.
     */
    private static class ShapefileReaderBoundsReader implements BoundsReader {

        private ShapefileReader reader;
        private IndexFile shpIndex;

        ShapefileReaderBoundsReader(ShapefileReader reader, IndexFile shpIndex) {
            this.reader = reader;
            this.shpIndex = shpIndex;
        }

        @Override
        public void insert(int recno, Envelope env) {
            // no-op, bounds reading is performed directly against the .shp
        }

        @Override
        public void read(int recNumber, Envelope env) throws IOException {
            int offset = shpIndex.getOffsetInBytes(recNumber);
            reader.goTo(offset);
            Record rec = reader.nextRecord();
            env.init(rec.minX, rec.maxX, rec.minY, rec.maxY);
        }

        @Override
        public void expandEnvelope(int recNumber, Envelope env) throws IOException {
            int offset = shpIndex.getOffsetInBytes(recNumber);
            reader.goTo(offset);
            Record rec = reader.nextRecord();
            env.expandToInclude(rec.minX, rec.minY);
            env.expandToInclude(rec.maxX, rec.maxY);
        }
    }

    /**
     * Stores record bounds in a {@link DoubleBuffer}, in {@code minx,miny[,maxx,maxy]} order; point bounds are stored
     * as a single ordinate pair.
     */
    private static class BufferBoundsReader implements BoundsReader {

        protected DoubleBuffer buff;
        private final boolean pointBounds;
        private final int ordinatesPerRec;

        BufferBoundsReader(DoubleBuffer buff, boolean pointBounds) {
            this.buff = buff;
            this.pointBounds = pointBounds;
            this.ordinatesPerRec = pointBounds ? 2 : 4;
        }

        private int offsetOf(int recNumber) {
            return ordinatesPerRec * recNumber;
        }

        @Override
        public void close() {
            buff = null;
        }

        @Override
        public void insert(int recno, Envelope env) {
            final int offset = offsetOf(recno);
            buff.put(offset, env.getMinX());
            buff.put(offset + 1, env.getMinY());
            if (!pointBounds) {
                buff.put(offset + 2, env.getMaxX());
                buff.put(offset + 3, env.getMaxY());
            }
        }

        @Override
        public void read(int recNumber, Envelope env) throws IOException {
            buff.position(offsetOf(recNumber));
            double minx = buff.get();
            double miny = buff.get();
            if (pointBounds) {
                env.init(minx, minx, miny, miny);
            } else {
                double maxx = buff.get();
                double maxy = buff.get();
                env.init(minx, maxx, miny, maxy);
            }
        }

        @Override
        public void expandEnvelope(int recNumber, Envelope env) throws IOException {
            buff.position(offsetOf(recNumber));
            env.expandToInclude(buff.get(), buff.get());
            if (!pointBounds) {
                env.expandToInclude(buff.get(), buff.get());
            }
        }
    }

    /** Uses a heap memory backed {@link DoubleBuffer} */
    private static class HeapBoundsReader extends BufferBoundsReader {
        HeapBoundsReader(int recCount, boolean pointBounds) {
            super(DoubleBuffer.allocate((pointBounds ? 2 : 4) * recCount), pointBounds);
        }
    }

    /**
     * Uses a memory mapped temporary file as the source for the {@link DoubleBuffer} where to store the record bounds.
     *
     * <p>{@link #close()} releases the memory mapped byte buffer and deletes the temporary file.
     */
    private static class FileBoundsReader implements BoundsReader {

        private static final String TMP_FILE_NAME_PREFIX = "GeoTools_shp_qix_bounds_";
        private final BufferBoundsReader reader;
        private final Path path;
        private final RandomAccessFile file;
        private final MappedByteBuffer mappedBuffer;

        FileBoundsReader(final int recCount, final boolean isPoint) throws IOException {
            final int fileSize = recCount * Double.BYTES * (isPoint ? 2 : 4);
            path = Files.createTempFile(TMP_FILE_NAME_PREFIX, ".tmp");
            try {
                checkAvailableSpace(path, fileSize);
                file = new RandomAccessFile(path.toFile(), "rw");
                mappedBuffer = file.getChannel().map(MapMode.READ_WRITE, 0L, fileSize);
                reader = new BufferBoundsReader(mappedBuffer.asDoubleBuffer(), isPoint);
            } catch (IOException | RuntimeException e) {
                Files.delete(path);
                throw e;
            }
        }

        @Override
        public void close() {
            reader.close();
            NIOUtilities.clean(mappedBuffer, true);
            try {
                file.close();
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error closing temporary file " + path, e);
            }
            try {
                Files.delete(path);
            } catch (IOException e) {
                LOGGER.log(Level.WARNING, "Error deleting temporary file " + path, e);
            }
        }

        @Override
        public void insert(int recno, Envelope env) {
            reader.insert(recno, env);
        }

        @Override
        public void read(int recNumber, Envelope env) throws IOException {
            reader.read(recNumber, env);
        }

        @Override
        public void expandEnvelope(int recNumber, Envelope env) throws IOException {
            reader.expandEnvelope(recNumber, env);
        }

        private void checkAvailableSpace(final Path path, final int fileSize) throws IOException {
            final FileStore fileStore = Files.getFileStore(path);
            final long usableSpace = fileStore.getUsableSpace();
            if (usableSpace < fileSize) {
                throw new FileSystemException(String.format(
                        "Not enough disk space. Required %,d bytes, available: %,d ", fileSize, usableSpace));
            }
        }
    }
}
