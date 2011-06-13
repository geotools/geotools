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
package org.geotools.data.shapefile.indexed.attribute;

import java.io.DataInputStream;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;

import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.StreamLogging;
import org.geotools.data.shapefile.dbf.DbaseFileHeader;
import org.geotools.data.shapefile.dbf.DbaseFileReader;
import org.geotools.resources.NIOUtilities;

/**
 * Class used to create an index for an dbf attribute 
 * @author Manuele Ventoruzzo
 *
 *
 * @source $URL$
 */
public class AttributeIndexWriter {

    public static final int HEADER_SIZE = 9;
    /** Number of bytes to be cached into memory (then it will be written to temporary file) */
    private int cacheSize;
    private int record_size;
    private FileChannel writeChannel;
    private FileChannel currentChannel;
    private DbaseFileReader reader;
    private StreamLogging streamLogger = new StreamLogging("AttributeIndexWriter");
    private String attribute;
    private int numRecords;
    private int attributeColumn;
    private Class attributeClass;
    private char attributeType;
    private File[] tempFiles;
    private ArrayList buffer;
    private long current;
    private long position;
    private int curFile;
    private ByteBuffer writeBuffer;
    
    /**
     * Create a new instance of AttributeIndexWriter 
     * @param attribute Attribute to be indexed
     * @param writeChannel Channel used to write the index
     * @param readChannel Channel used to read attributes file
     */
    public AttributeIndexWriter(String attribute, FileChannel writeChannel,
                                 ReadableByteChannel readChannel, int cacheSize) throws IOException {
        this.writeChannel = writeChannel;
        this.attribute = attribute;
        this.cacheSize = cacheSize;
        reader = new DbaseFileReader(readChannel, false, ShapefileDataStore.DEFAULT_STRING_CHARSET, null);
        if (!retrieveAttributeInfos()) {
            throw new IOException("Attribute " + attribute + " not found in dbf file");
        }
        streamLogger.open();
        tempFiles = new File[getNumFiles()];
        buffer = new ArrayList(getCacheSize());
        current = 0;
        curFile = 0;
    }

    /**
     * Build index, caching data in chucks and sorting it.
     */
    public void buildIndex() throws IOException {
        while (hasNext()) {
            position = 0;
            readBuffer();
            saveBuffer();
        }
        reader.close();
        merge();
        deleteTempFiles();
        streamLogger.close();
    }

    /** 
     * Returns the number of attributes indexed
     */
    public int getCount() {
        return numRecords;
    }

    private boolean hasNext() {
        return reader.hasNext();
    }

    private void deleteTempFiles() {
        for (int i = 0; i < tempFiles.length; i++) {
            if (!tempFiles[i].delete()) {
                tempFiles[i].deleteOnExit();
            }
        }
    }

    private void merge() throws IOException {
        DataInputStream[] in = new DataInputStream[tempFiles.length];
        try {
            IndexRecord[] recs = new IndexRecord[tempFiles.length];
            for (int i = 0; i < tempFiles.length; i++) {
                in[i] = new DataInputStream(new FileInputStream(tempFiles[i]));
                recs[i] = null;
            }
            currentChannel = writeChannel; //to write to the ultimate destination
            allocateBuffers();
            writeBuffer.position(HEADER_SIZE);
            position = 0;
            int streamsReady;
            IndexRecord min;
            int mpos;
            do {
                min = null;
                mpos = -1;
                streamsReady = recs.length;
                for (int j = 0; j < recs.length; j++) {
                    if (recs[j] == null) {
                        try {
                            recs[j] = readRecord(in[j]);
                        } catch (EOFException e) {
                            streamsReady--;
                            continue;
                        }
                    }
                    if (min==null || (min.compareTo(recs[j])>0)) {
                        min = recs[j];
                        mpos = j;
                    }
                }
                if (mpos!=-1)
                    recs[mpos] = null;
                write(min);
            } while (streamsReady>0);
        } finally {
            //close input streams
            for (int i = 0; i < in.length; i++) {
                if (in[i]!=null)
                    in[i].close();
            }
            //close output stream
            drain();
            writeHeader();
            close();
        }
    }

    /** Loads next part of file into cache */
    private void readBuffer() throws IOException {
        buffer.clear();
        int n = getCacheSize();
        Comparable o;
        IndexRecord r;
        for (int i = 0; hasNext() && i < n; i++) {
            o = getAttribute();
            r = new IndexRecord(o,current+1);
            buffer.add(r);
            current++;
        }
    }

    /** Saves buffer on temporary file */
    private void saveBuffer() throws IOException {
        RandomAccessFile raf = null;
        try {
            if (buffer.size() == 0) {
                return;
            }
            try {
                Collections.sort(buffer);
            } catch (OutOfMemoryError err) {
                throw new IOException(err.getMessage()+". Try to lower memory load parameter.");
            }
            File file = File.createTempFile("attind", null);
            tempFiles[curFile++] = file;
            Iterator it = buffer.iterator();
            raf = new RandomAccessFile(file, "rw");
            currentChannel = raf.getChannel();
            currentChannel.lock();
            allocateBuffers();
            writeBuffer.position(0);
            while (it.hasNext()) {
                write((IndexRecord) it.next());
            }
        } finally {
            close();
            if (raf != null) {
                raf.close();
            }
        }
    }


    private int getNumFiles() throws IOException {
        int maxRec = getCacheSize();
        int n = (numRecords / maxRec);
        return ((numRecords % maxRec)==0) ? n : n+1;
    }

    private int getCacheSize() {
        return ((numRecords * record_size) > cacheSize) ? cacheSize / record_size : numRecords;
    }

    private Comparable getAttribute() throws IOException {
        DbaseFileReader.Row row = reader.readRow();
        Object o = row.read(attributeColumn);
        if (o instanceof Date) {
            //use ms from 1/1/70
            return new Long(((Date)o).getTime());
        }
        return (Comparable)o;
    }

    private IndexRecord readRecord(DataInputStream in) throws IOException {
        Comparable obj = null;
        switch (attributeType) {
            case 'N':
            case 'D':
                if (attributeClass.isInstance(new Integer(0))) {
                    obj = new Integer(in.readInt());
                } else {
                    obj = new Long(in.readLong());
                }
                break;
            case 'F':
                obj = new Double(in.readDouble());
                break;
            case 'L':
                obj = new Boolean(in.readBoolean());
                break;
            case 'C':
            default:
                byte[] b = new byte[record_size - 8];
                in.read(b);
                obj = (new String(b, "ISO-8859-1")).trim();
            }
        long id = in.readLong();
        return new IndexRecord(obj, id);
    }

    private void write(IndexRecord r) throws IOException {
        try {
            if (r == null)
                return;
            if (writeBuffer == null)
                allocateBuffers();
            if (writeBuffer.remaining() < record_size)
                drain();
            switch (attributeType) {
                case 'N':
                case 'D':
                    Object obj = r.getAttribute();
                    //sometimes DbaseFileReader reads an attribute as Integer, even if it's described as Long in the header
                    if (attributeClass.isInstance(new Integer(0))) {
                        int i = (obj instanceof Integer) ? ((Number) obj).intValue() : (int) ((Number) obj).longValue();
                        writeBuffer.putInt(i);
                    } else {
                        long l = (obj instanceof Integer) ? (long) ((Number) obj).intValue() : ((Number) obj).longValue();
                        writeBuffer.putLong(l);
                    }
                    break;
                case 'F':
                    writeBuffer.putDouble(((Double) r.getAttribute()).doubleValue());
                    break;
                case 'L':
                    boolean b = ((Boolean) r.getAttribute()).booleanValue();
                    writeBuffer.put((byte)(b?1:0));
                    break;
                case 'C':
                default:
                    byte[] btemp = r.getAttribute().toString().getBytes("ISO-8859-1");
                    byte[] bres = new byte[record_size-8];
                    for (int i = 0; i < bres.length; i++) {
                        bres[i] = (i<btemp.length) ? btemp[i] : (byte)0;
                    }
                    writeBuffer.put(bres);
            }
            writeBuffer.putLong(r.getFeatureID());
        } catch (UnsupportedEncodingException ex) {
            throw new IOException(ex.getMessage());
        }
    }

    private void writeHeader() throws IOException {
        ByteBuffer buf = ByteBuffer.allocate(HEADER_SIZE);
        buf.put((byte)attributeType);
        buf.putInt(record_size); //record size in buffer
        buf.putInt(numRecords); //number of records in this index
        buf.flip();
        writeChannel.write(buf, 0);
    }

    private void allocateBuffers() throws IOException {
        writeBuffer = NIOUtilities.allocate(HEADER_SIZE+record_size * 1024);
    }

    private void drain() throws IOException {
        if (writeBuffer==null)
            return;
        writeBuffer.flip();
        int written = 0;
        while (writeBuffer.remaining() > 0) {
            written += currentChannel.write(writeBuffer, position);
        }
        position += written;
        writeBuffer.flip().limit(writeBuffer.capacity());
    }

    private boolean retrieveAttributeInfos() {
        DbaseFileHeader header = reader.getHeader();
        for (int i = 0; i < header.getNumFields(); i++) {
            if (header.getFieldName(i).equals(attribute)) {
                attributeColumn = i;
                attributeClass = header.getFieldClass(i);
                numRecords = header.getNumRecords();
                attributeType = header.getFieldType(i);
                switch (attributeType) {
                    case 'C': //Character
                        record_size = header.getFieldLength(i);
                        break;
                    case 'N': //Numeric
                        if (attributeClass.isInstance(new Integer(0)))
                            record_size = 4;
                        else
                            record_size = 8; //Long and Double are represented using 64 bits
                        break;
                    case 'F': //Float
                        record_size = 8;
                        break;
                    case 'D': //Date
                        record_size = 8; //stored in ms from 1/1/70
                        break;
                    case 'L': //Logic
                        record_size = 1; //of course index on boolean feature doesn't have any meaning
                        break;
                    default:
                        record_size = header.getFieldLength(i);                            
                }
                record_size += 8; //fid index
                return true;
            }
        }
        return false;
    }

    private void close() throws IOException {
        try {
            drain();
        } finally {
            if (writeBuffer != null) {
                if (writeBuffer instanceof MappedByteBuffer) {
                    NIOUtilities.clean(writeBuffer);
                }
            }
            if (currentChannel!=null && currentChannel.isOpen()) {
                currentChannel.close();
            }
        }
    }

}
