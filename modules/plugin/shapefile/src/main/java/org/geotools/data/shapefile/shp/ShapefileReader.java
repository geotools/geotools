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

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.shapefile.FileReader;
import org.geotools.data.shapefile.ShpFileType;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.StreamLogging;
import org.geotools.renderer.ScreenMap;
import org.geotools.resources.NIOUtilities;
import org.geotools.util.logging.Logging;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.MultiPoint;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

/**
 * The general use of this class is: <CODE><PRE>
 * 
 * FileChannel in = new FileInputStream(&quot;thefile.dbf&quot;).getChannel();
 * ShapefileReader r = new ShapefileReader( in ) while (r.hasNext()) { Geometry
 * shape = (Geometry) r.nextRecord().shape() // do stuff } r.close();
 * 
 * </PRE></CODE> You don't have to immediately ask for the shape from the record. The
 * record will contain the bounds of the shape and will only read the shape when
 * the shape() method is called. This ShapefileReader.Record is the same object
 * every time, so if you need data from the Record, be sure to copy it.
 * 
 * @author jamesm
 * @author aaime
 * @author Ian Schneider
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/shapefile/src/main/java/org/geotools/data/shapefile/shp/ShapefileReader.java $
 */
public class ShapefileReader implements FileReader {
    private static final Logger LOGGER = Logging.getLogger(ShapefileReader.class);
    
    /**
     *  Used to mark the current shape is not known, either because someone moved the reader
     *  to a specific byte offset manually, or because the .shx could not be opened
     */
    private static final int UNKNOWN = Integer.MIN_VALUE;

    /**
     * The reader returns only one Record instance in its lifetime. The record
     * contains the current record information.
     */
    public final class Record {
        int length;

        public int number = 0;

        int offset; // Relative to the whole file

        int start = 0; // Relative to the current loaded buffer

        /** The minimum X value. */
        public double minX;

        /** The minimum Y value. */
        public double minY;

        /** The maximum X value. */
        public double maxX;

        /** The maximum Y value. */
        public double maxY;

        public ShapeType type;

        int end = 0; // Relative to the whole file

        Object shape = null;

        /** Fetch the shape stored in this record. */
        public Object shape() {
            if (shape == null) {
                buffer.position(start);
                buffer.order(ByteOrder.LITTLE_ENDIAN);
                shape = handler.read(buffer, type, flatGeometry);
            }
            return shape;
        }

        public int offset() {
            return offset;
        }

        /** A summary of the record. */
        public String toString() {
            return "Record " + number + " length " + length + " bounds " + minX
                    + "," + minY + " " + maxX + "," + maxY;
        }
        
        public Envelope envelope() {
            return new Envelope(minX, maxX, minY, maxY);
        }
        
        public Object getSimplifiedShape() {
            CoordinateSequenceFactory csf = geometryFactory.getCoordinateSequenceFactory();
            if(type.isPointType()) {
                CoordinateSequence cs = csf.create(1, 2);
                cs.setOrdinate(0, 0, (minX + maxX) / 2);
                cs.setOrdinate(0, 1, (minY + maxY) / 2);
                return geometryFactory.createMultiPoint(new Point[] {geometryFactory.createPoint(cs)});
            } else if(type.isLineType()) {
                CoordinateSequence cs = csf.create(2, 2);
                cs.setOrdinate(0, 0, minX);
                cs.setOrdinate(0, 1, minY);
                cs.setOrdinate(1, 0, maxX);
                cs.setOrdinate(1, 1, maxY);
                return geometryFactory.createMultiLineString(new LineString[] {geometryFactory.createLineString(cs)});
            } else if(type.isPolygonType()) {
                CoordinateSequence cs = csf.create(5, 2);
                cs.setOrdinate(0, 0, minX);
                cs.setOrdinate(0, 1, minY);
                cs.setOrdinate(1, 0, minX);
                cs.setOrdinate(1, 1, maxY);
                cs.setOrdinate(2, 0, maxX);
                cs.setOrdinate(2, 1, maxY);
                cs.setOrdinate(3, 0, maxX);
                cs.setOrdinate(3, 1, minY);
                cs.setOrdinate(4, 0, minX);
                cs.setOrdinate(4, 1, minY);
                LinearRing ring = geometryFactory.createLinearRing(cs);
                return geometryFactory.createMultiPolygon(new Polygon[] {geometryFactory.createPolygon(ring, null)});
            } else {
                return shape();
            }
        }
        
        public Object getSimplifiedShape(ScreenMap sm) {
            if(type.isPointType()) {
                return shape();
            }
            
            Class geomType = Geometry.class;
            if(type.isLineType()) {
                geomType = MultiLineString.class;
            } else if(type.isMultiPointType()) {
                geomType = MultiPoint.class;
            } else if(type.isPolygonType()) {
                geomType = MultiPolygon.class;
            }
            return sm.getSimplifiedShape(minX, minY, maxX, maxY, geometryFactory, geomType);
        }
    }

    private ShapeHandler handler;

    private ShapefileHeader header;

    private ReadableByteChannel channel;

    ByteBuffer buffer;

    private ShapeType fileShapeType = ShapeType.UNDEFINED;

    private ByteBuffer headerTransfer;

    private final Record record = new Record();

    private final boolean randomAccessEnabled;

    private boolean useMemoryMappedBuffer;

    private long currentOffset = 0L;
    
    private int currentShape = 0;
    
    private IndexFile shxReader;
    
    private StreamLogging streamLogger = new StreamLogging("Shapefile Reader");
    
    private GeometryFactory geometryFactory;

    private boolean flatGeometry;
    
    /**
     * @deprecated Use {@link #ShapefileReader(ShpFiles, boolean, boolean, GeometryFactory)} instead
     */
    public ShapefileReader(ShpFiles shapefileFiles, boolean strict,
            boolean useMemoryMapped) throws IOException, ShapefileException {
        this(shapefileFiles, strict, useMemoryMapped, new GeometryFactory());
    }

    /**
     * Creates a new instance of ShapeFile.
     * 
     * @param shapefileFiles
     *                The ReadableByteChannel this reader will use.
     * @param strict
     *                True to make the header parsing throw Exceptions if the
     *                version or magic number are incorrect.
     * @throws IOException
     *                 If problems arise.
     * @throws ShapefileException
     *                 If for some reason the file contains invalid records.
     */
    public ShapefileReader(ShpFiles shapefileFiles, boolean strict,
            boolean useMemoryMapped, GeometryFactory gf) throws IOException, ShapefileException {
        this.channel = shapefileFiles.getReadChannel(ShpFileType.SHP, this);
        this.useMemoryMappedBuffer = useMemoryMapped;
        streamLogger.open();
        randomAccessEnabled = channel instanceof FileChannel;
        try {
            shxReader = new IndexFile(shapefileFiles, this.useMemoryMappedBuffer);
        } catch(Exception e) {
            LOGGER.log(Level.WARNING, "Could not open the .shx file, continuing " +
            		"assuming the .shp file is not sparse", e);
            currentShape = UNKNOWN;
        }
        init(strict, gf);
    }
    
    /**
     * Creates a new instance of ShapeFile.
     * 
     * @param shapefileFiles
     *                The ReadableByteChannel this reader will use.
     * @param strict
     *                True to make the header parsing throw Exceptions if the
     *                version or magic number are incorrect.
     * @param useMemoryMapped Wheter to enable memory mapping or not
     * @param gf      The geometry factory used to build the geometries
     * @param onlyRandomAccess When true sets up the reader to do exclusively read driven by goTo(x)
     *                         and thus avoids opening the .shx file
     * @throws IOException
     *                 If problems arise.
     * @throws ShapefileException
     *                 If for some reason the file contains invalid records.
     */
    public ShapefileReader(ShpFiles shapefileFiles, boolean strict,
            boolean useMemoryMapped, GeometryFactory gf, boolean onlyRandomAccess) throws IOException, ShapefileException {
        this.channel = shapefileFiles.getReadChannel(ShpFileType.SHP, this);
        this.useMemoryMappedBuffer = useMemoryMapped;
        streamLogger.open();
        randomAccessEnabled = channel instanceof FileChannel;
        if(!onlyRandomAccess) {
            try {
                shxReader = new IndexFile(shapefileFiles, this.useMemoryMappedBuffer);
            } catch(Exception e) {
                LOGGER.log(Level.WARNING, "Could not open the .shx file, continuing " +
                        "assuming the .shp file is not sparse", e);
                currentShape = UNKNOWN;
            }
        }
        init(strict, gf);
    }
    
    /**
     * Disables .shx file usage. By doing so you drop support for sparse shapefiles, the 
     * .shp will have to be without holes, all the valid shapefile records will have to
     * be contiguous.
     * @throws IOException
     */
    public void disableShxUsage() throws IOException {
        if(shxReader != null) {
            shxReader.close();
            shxReader = null;
        }
        currentShape = UNKNOWN;
    }

    // ensure the capacity of the buffer is of size by doubling the original
    // capacity until it is big enough
    // this may be naiive and result in out of MemoryError as implemented...
    private ByteBuffer ensureCapacity(ByteBuffer buffer, int size,
            boolean useMemoryMappedBuffer) {
        // This sucks if you accidentally pass is a MemoryMappedBuffer of size
        // 80M
        // like I did while messing around, within moments I had 1 gig of
        // swap...
        if (buffer.isReadOnly() || useMemoryMappedBuffer) {
            return buffer;
        }

        int limit = buffer.limit();
        while (limit < size) {
            limit *= 2;
        }
        if (limit != buffer.limit()) {
            // clean up the old buffer and allocate a new one
            buffer = NIOUtilities.allocate(limit);
        }
        return buffer;
    }

    // for filling a ReadableByteChannel
    public static int fill(ByteBuffer buffer, ReadableByteChannel channel)
            throws IOException {
        int r = buffer.remaining();
        // channel reads return -1 when EOF or other error
        // because they a non-blocking reads, 0 is a valid return value!!
        while (buffer.remaining() > 0 && r != -1) {
            r = channel.read(buffer);
        }
        buffer.limit(buffer.position());
        return r;
    }

    private void init(boolean strict, GeometryFactory gf) throws IOException, ShapefileException {
        geometryFactory = gf;

        if (channel instanceof FileChannel && useMemoryMappedBuffer) {
            FileChannel fc = (FileChannel) channel;
            buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            buffer.position(0);
            this.currentOffset = 0;
        } else {
            // force useMemoryMappedBuffer to false
            this.useMemoryMappedBuffer = false;
            // start small
            buffer = NIOUtilities.allocate(1024);
            fill(buffer, channel);
            buffer.flip();
            this.currentOffset = 0;
        }
        header = new ShapefileHeader();
        header.read(buffer, strict);
        fileShapeType = header.getShapeType();
        handler = fileShapeType.getShapeHandler(gf);
        if (handler == null) {
            throw new IOException("Unsuported shape type:" + fileShapeType);
        }

        headerTransfer = ByteBuffer.allocate(8);
        headerTransfer.order(ByteOrder.BIG_ENDIAN);

        // make sure the record end is set now...
        record.end = this.toFileOffset(buffer.position());
    }

    /**
     * Get the header. Its parsed in the constructor.
     * 
     * @return The header that is associated with this file.
     */
    public ShapefileHeader getHeader() {
        return header;
    }

    // do important cleanup stuff.
    // Closes channel !
    /**
     * Clean up any resources. Closes the channel.
     * 
     * @throws IOException
     *                 If errors occur while closing the channel.
     */
    public void close() throws IOException {
        // don't throw NPE on double close
        if(channel == null)
            return;
        try {
            if (channel.isOpen()) {
                channel.close();
                streamLogger.close();
            }
            NIOUtilities.clean(buffer, useMemoryMappedBuffer);
        } finally {
            if(shxReader != null)
                shxReader.close();
        }
        shxReader = null;
        channel = null;
        header = null;
    }

    public boolean supportsRandomAccess() {
        return randomAccessEnabled;
    }

    /**
     * If there exists another record. Currently checks the stream for the
     * presence of 8 more bytes, the length of a record. If this is true and the
     * record indicates the next logical record number, there exists more
     * records.
     * 
     * @throws IOException
     * @return True if has next record, false otherwise.
     */
    public boolean hasNext() throws IOException {
        return this.hasNext(true);
    }

    /**
     * If there exists another record. Currently checks the stream for the
     * presence of 8 more bytes, the length of a record. If this is true and the
     * record indicates the next logical record number (if checkRecord == true),
     * there exists more records.
     * 
     * @param checkRecno
     *                If true then record number is checked
     * @throws IOException
     * @return True if has next record, false otherwise.
     */
    private boolean hasNext(boolean checkRecno) throws IOException {
        // don't read past the end of the file (provided currentShape accurately
        // represents the current position)
        if(currentShape > UNKNOWN && currentShape > shxReader.getRecordCount() - 1)
            return false;
        
        // mark current position
        int position = buffer.position();

        // ensure the proper position, regardless of read or handler behavior
        buffer.position(getNextOffset());

        // no more data left
        if (buffer.remaining() < 8)
            return false;

        // looks good
        boolean hasNext = true;
        if (checkRecno) {
            // record headers in big endian
            buffer.order(ByteOrder.BIG_ENDIAN);
            int declaredRecNo = buffer.getInt();
            hasNext = declaredRecNo == record.number + 1;
        }

        // reset things to as they were
        buffer.position(position);

        return hasNext;
    }
    
    private int getNextOffset() throws IOException {
        if(currentShape >= 0) {
            return this.toBufferOffset(shxReader.getOffsetInBytes(currentShape));
        } else {
            return this.toBufferOffset(record.end);
        }
    }
    
    /**
     * Transfer (by bytes) the data at the current record to the
     * ShapefileWriter.
     * 
     * @param bounds
     *                double array of length four for transfering the bounds
     *                into
     * @return The length of the record transfered in bytes
     */
    public int transferTo(ShapefileWriter writer, int recordNum, double[] bounds)
            throws IOException {

        buffer.position(this.toBufferOffset(record.end));
        buffer.order(ByteOrder.BIG_ENDIAN);

        buffer.getInt(); // record number
        int rl = buffer.getInt();
        int mark = buffer.position();
        int len = rl * 2;

        buffer.order(ByteOrder.LITTLE_ENDIAN);
        ShapeType recordType = ShapeType.forID(buffer.getInt());

        if (recordType.isMultiPoint()) {
            for (int i = 0; i < 4; i++) {
                bounds[i] = buffer.getDouble();
            }
        } else if (recordType != ShapeType.NULL) {
            bounds[0] = bounds[1] = buffer.getDouble();
            bounds[2] = bounds[3] = buffer.getDouble();
        }

        // write header to shp and shx
        headerTransfer.position(0);
        headerTransfer.putInt(recordNum).putInt(rl).position(0);
        writer.shpChannel.write(headerTransfer);
        headerTransfer.putInt(0, writer.offset).position(0);
        writer.offset += rl + 4;
        writer.shxChannel.write(headerTransfer);

        // reset to mark and limit at end of record, then write
        int oldLimit = buffer.limit();
        buffer.position(mark).limit(mark + len);
        writer.shpChannel.write(buffer);
        buffer.limit(oldLimit);

        record.end = this.toFileOffset(buffer.position());
        record.number++;

        return len;
    }

    /**
     * Fetch the next record information.
     * 
     * @throws IOException
     * @return The record instance associated with this reader.
     */
    public Record nextRecord() throws IOException {

        // need to update position
        buffer.position(getNextOffset());
        if(currentShape != UNKNOWN)
            currentShape++;

        // record header is big endian
        buffer.order(ByteOrder.BIG_ENDIAN);

        // read shape record header
        int recordNumber = buffer.getInt();
        // silly ESRI say contentLength is in 2-byte words
        // and ByteByffer uses bytes.
        // track the record location
        int recordLength = buffer.getInt() * 2;

        if (!buffer.isReadOnly() && !useMemoryMappedBuffer) {
            // capacity is less than required for the record
            // copy the old into the newly allocated
            if (buffer.capacity() < recordLength + 8) {
                this.currentOffset += buffer.position();
                ByteBuffer old = buffer;
                // ensure enough capacity for one more record header
                buffer = ensureCapacity(buffer, recordLength + 8,
                        useMemoryMappedBuffer);
                buffer.put(old);
                NIOUtilities.clean(old, useMemoryMappedBuffer);
                fill(buffer, channel);
                buffer.position(0);
            } else
            // remaining is less than record length
            // compact the remaining data and read again,
            // allowing enough room for one more record header
            if (buffer.remaining() < recordLength + 8) {
                this.currentOffset += buffer.position();
                buffer.compact();
                fill(buffer, channel);
                buffer.position(0);
            }
        }

        // shape record is all little endian
        buffer.order(ByteOrder.LITTLE_ENDIAN);

        // read the type, handlers don't need it
        ShapeType recordType = ShapeType.forID(buffer.getInt());

        // this usually happens if the handler logic is bunk,
        // but bad files could exist as well...
        if (recordType != ShapeType.NULL && recordType != fileShapeType) {
            throw new IllegalStateException("ShapeType changed illegally from "
                    + fileShapeType + " to " + recordType);
        }

        // peek at bounds, then reset for handler
        // many handler's may ignore bounds reading, but we don't want to
        // second guess them...
        buffer.mark();
        if (recordType.isMultiPoint()) {
            record.minX = buffer.getDouble();
            record.minY = buffer.getDouble();
            record.maxX = buffer.getDouble();
            record.maxY = buffer.getDouble();
        } else if (recordType != ShapeType.NULL) {
            record.minX = record.maxX = buffer.getDouble();
            record.minY = record.maxY = buffer.getDouble();
        }
        buffer.reset();

        record.offset = record.end;
        // update all the record info.
        record.length = recordLength;
        record.type = recordType;
        record.number = recordNumber;
        // remember, we read one int already...
        record.end = this.toFileOffset(buffer.position()) + recordLength - 4;
        // mark this position for the reader
        record.start = buffer.position();
        // clear any cached shape
        record.shape = null;

        return record;
    }

    /**
     * Moves the reader to the specified byte offset in the file. Mind that:
     * <ul>
     * <li>it's your responsibility to ensure the offset corresponds to the
     * actual beginning of a shape struct</li>
     * <li>once you call this, reading with hasNext/next on sparse shapefiles
     * will be broken (we don't know anymore at which shape we are)</li>
     * </ul>
     * 
     * @param offset
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    public void goTo(int offset) throws IOException,
            UnsupportedOperationException {
        disableShxUsage();
        if (randomAccessEnabled) {
            if (this.useMemoryMappedBuffer) {
                buffer.position(offset);
            } else {
                /*
                 * Check to see if requested offset is already loaded; ensure
                 * that record header is in the buffer
                 */
                if (this.currentOffset <= offset
                        && this.currentOffset + buffer.limit() >= offset + 8) {
                    buffer.position(this.toBufferOffset(offset));
                } else {
                    FileChannel fc = (FileChannel) this.channel;
                    fc.position(offset);
                    this.currentOffset = offset;
                    buffer.position(0);
                    buffer.limit(buffer.capacity());
                    fill(buffer, fc);
                    buffer.position(0);
                }
            }

            int oldRecordOffset = record.end;
            record.end = offset;
            try {
                hasNext(false); // don't check for next logical record equality
            } catch (IOException ioe) {
                record.end = oldRecordOffset;
                throw ioe;
            }
        } else {
            throw new UnsupportedOperationException("Random Access not enabled");
        }
    }

    /**
     * Returns the shape at the specified byte distance from the beginning of
     * the file. Mind that:
     * <ul>
     * <li>it's your responsibility to ensure the offset corresponds to the
     * actual beginning of a shape struct</li>
     * <li>once you call this, reading with hasNext/next on sparse shapefiles
     * will be broken (we don't know anymore at which shape we are)</li>
     * </ul>
     * 
     * 
     * @param offset
     * @throws IOException
     * @throws UnsupportedOperationException
     */
    public Object shapeAt(int offset) throws IOException,
            UnsupportedOperationException {
        disableShxUsage();
        if (randomAccessEnabled) {
            this.goTo(offset);
            return nextRecord().shape();
        }
        throw new UnsupportedOperationException("Random Access not enabled");
    }

    /**
     * Sets the current location of the byteStream to offset and returns the
     * next record. Usually used in conjuctions with the shx file or some other
     * index file. Mind that:
     * <ul>
     * <li>it's your responsibility to ensure the offset corresponds to the
     * actual beginning of a shape struct</li>
     * <li>once you call this, reading with hasNext/next on sparse shapefiles
     * will be broken (we don't know anymore at which shape we are)</li>
     * </ul>
     * 
     * 
     * 
     * @param offset
     *            If using an shx file the offset would be: 2 *
     *            (index.getOffset(i))
     * @return The record after the offset location in the bytestream
     * @throws IOException
     *             thrown in a read error occurs
     * @throws UnsupportedOperationException
     *             thrown if not a random access file
     */
    public Record recordAt(int offset) throws IOException,
            UnsupportedOperationException {
        if (randomAccessEnabled) {
            this.goTo(offset);
            return nextRecord();
        }
        throw new UnsupportedOperationException("Random Access not enabled");
    }

    /**
     * Converts file offset to buffer offset
     * 
     * @param offset
     *                The offset relative to the whole file
     * @return The offset relative to the current loaded portion of the file
     */
    private int toBufferOffset(int offset) {
        return (int) (offset - this.currentOffset);
    }

    /**
     * Converts buffer offset to file offset
     * 
     * @param offset
     *                The offset relative to the buffer
     * @return The offset relative to the whole file
     */
    private int toFileOffset(int offset) {
        return (int) (this.currentOffset + offset);
    }

    /**
     * Parses the shpfile counting the records.
     * 
     * @return the number of non-null records in the shapefile
     */
    public int getCount(int count) throws DataSourceException {
        try {
            if (channel == null)
                return -1;
            count = 0;
            long offset = this.currentOffset;
            try {
                goTo(100);
            } catch (UnsupportedOperationException e) {
                return -1;
            }
            while (hasNext()) {
                count++;
                nextRecord();
            }

            goTo((int) offset);

        } catch (IOException ioe) {
            count = -1;
            // What now? This seems arbitrarily appropriate !
            throw new DataSourceException("Problem reading shapefile record",
                    ioe);
        }
        return count;
    }

    /**
     * @param handler
     *                The handler to set.
     */
    public void setHandler(ShapeHandler handler) {
        this.handler = handler;
    }

    public String id() {
        return getClass().getName();
    }

    public void setFlatGeometry(boolean flatGeometry) {
        this.flatGeometry = flatGeometry;        
    }
}
