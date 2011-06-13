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
 *
 *    This file is based on an origional contained in the GISToolkit project:
 *    http://gistoolkit.sourceforge.net/
 */
package org.geotools.data.shapefile.dbf;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.nio.channels.ReadableByteChannel;
import java.nio.charset.Charset;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import org.geotools.data.shapefile.FileReader;
import org.geotools.data.shapefile.ShpFileType;
import org.geotools.data.shapefile.ShpFiles;
import org.geotools.data.shapefile.StreamLogging;
import org.geotools.resources.NIOUtilities;

/**
 * A DbaseFileReader is used to read a dbase III format file. <br>
 * The general use of this class is: <CODE><PRE>
 * 
 * FileChannel in = new FileInputStream(&quot;thefile.dbf&quot;).getChannel();
 * DbaseFileReader r = new DbaseFileReader( in ) Object[] fields = new
 * Object[r.getHeader().getNumFields()]; while (r.hasNext()) {
 * r.readEntry(fields); // do stuff } r.close();
 * 
 * </PRE></CODE> For consumers who wish to be a bit more selective with their reading
 * of rows, the Row object has been added. The semantics are the same as using
 * the readEntry method, but remember that the Row object is always the same.
 * The values are parsed as they are read, so it pays to copy them out (as each
 * call to Row.read() will result in an expensive String parse). <br>
 * <b>EACH CALL TO readEntry OR readRow ADVANCES THE FILE!</b><br>
 * An example of using the Row method of reading: <CODE><PRE>
 * 
 * FileChannel in = new FileInputStream(&quot;thefile.dbf&quot;).getChannel();
 * DbaseFileReader r = new DbaseFileReader( in ) int fields =
 * r.getHeader().getNumFields(); while (r.hasNext()) { DbaseFileReader.Row row =
 * r.readRow(); for (int i = 0; i &lt; fields; i++) { // do stuff Foo.bar(
 * row.read(i) ); } } r.close();
 * 
 * </PRE></CODE>
 * 
 * @author Ian Schneider, Andrea Aaime
 *
 * @source $URL$
 */
public class DbaseFileReader implements FileReader {

    public final class Row {
        public Object read(final int column) throws IOException {
            final int offset = fieldOffsets[column];
            return readObject(offset, column);
        }

        public String toString() {
            final StringBuffer ret = new StringBuffer("DBF Row - ");
            for (int i = 0; i < header.getNumFields(); i++) {
                ret.append(header.getFieldName(i)).append(": \"");
                try {
                    ret.append(this.read(i));
                } catch (final IOException ioe) {
                    ret.append(ioe.getMessage());
                }
                ret.append("\" ");
            }
            return ret.toString();
        }
    }

    DbaseFileHeader header;

    ByteBuffer buffer;

    ReadableByteChannel channel;

    byte[] bytes;

    char[] fieldTypes;

    int[] fieldLengths;
    
    int[] fieldOffsets;

    int cnt = 1;

    Row row;

    protected boolean useMemoryMappedBuffer;

    protected boolean randomAccessEnabled;

    protected long currentOffset = 0;
    private final StreamLogging streamLogger = new StreamLogging("Dbase File Reader");

    private Charset stringCharset;
    
    private boolean oneBytePerChar;

    private Calendar calendar;

    private final long MILLISECS_PER_DAY = 24*60*60*1000;

    
    /**
     * Creates a new instance of DBaseFileReader
     * 
     * @param shapefileFiles.
     *                The readable channel to use.
     * @throws IOException
     *                 If an error occurs while initializing.
     */
    public DbaseFileReader(final ShpFiles shapefileFiles,
            final boolean useMemoryMappedBuffer, final Charset charset, final TimeZone timeZone) throws IOException {
        final ReadableByteChannel dbfChannel = shapefileFiles.getReadChannel(ShpFileType.DBF, this);
        init(dbfChannel, useMemoryMappedBuffer, charset, timeZone);
    }
    
    public DbaseFileReader(final ShpFiles shapefileFiles,
            final boolean useMemoryMappedBuffer, final Charset charset) throws IOException {
        final ReadableByteChannel dbfChannel = shapefileFiles.getReadChannel(ShpFileType.DBF, this);
        init(dbfChannel, useMemoryMappedBuffer, charset, null);
    }
    
    public DbaseFileReader(final ReadableByteChannel readChannel, final boolean useMemoryMappedBuffer, 
    		final Charset charset) throws IOException {
        init(readChannel, useMemoryMappedBuffer, charset, null);
    }

    public DbaseFileReader(final ReadableByteChannel readChannel, final boolean useMemoryMappedBuffer, 
    		final Charset charset, final TimeZone timeZone) throws IOException {
        init(readChannel, useMemoryMappedBuffer, charset, timeZone);
    }

    private void init(final ReadableByteChannel dbfChannel, final boolean useMemoryMappedBuffer,
            final Charset charset, final TimeZone timeZone) throws IOException {
        this.channel = dbfChannel;
        this.stringCharset = charset == null ? Charset.defaultCharset() : charset;
        TimeZone calTimeZone = timeZone == null ? TimeZone.getDefault() : timeZone;
        this.calendar = Calendar.getInstance(calTimeZone, Locale.US);

        this.useMemoryMappedBuffer = useMemoryMappedBuffer;
        this.randomAccessEnabled = (channel instanceof FileChannel);
        streamLogger.open();
        header = new DbaseFileHeader();

        // create the ByteBuffer
        // if we have a FileChannel, lets map it
        if (channel instanceof FileChannel && this.useMemoryMappedBuffer) {
            final FileChannel fc = (FileChannel) channel;
            if((fc.size() - fc.position()) < (long) Integer.MAX_VALUE) {
                buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, fc.size());
            } else {
                buffer = fc.map(FileChannel.MapMode.READ_ONLY, 0, Integer.MAX_VALUE);
            }
            buffer.position((int) fc.position());
            header.readHeader(buffer);
            
            this.currentOffset = 0;
        } else {
            // Force useMemoryMappedBuffer to false
            this.useMemoryMappedBuffer = false;
            header.readHeader(channel, charset);
            // Some other type of channel
            // size the buffer so that we can read 4 records at a time (and make the buffer cacheable)
            //int size = (int) Math.pow(2, Math.ceil(Math.log(header.getRecordLength()) / Math.log(2)));
            buffer = NIOUtilities.allocate(header.getRecordLength());
            // fill it and reset
            fill(buffer, channel);
            buffer.flip();
            this.currentOffset = header.getHeaderLength();
        }
        
        // The entire file is in little endian
        buffer.order(ByteOrder.LITTLE_ENDIAN);
        
        // Set up some buffers and lookups for efficiency
        fieldTypes = new char[header.getNumFields()];
        fieldLengths = new int[header.getNumFields()];
        fieldOffsets = new int[header.getNumFields()];
        for (int i = 0, ii = header.getNumFields(); i < ii; i++) {
            fieldTypes[i] = header.getFieldType(i);
            fieldLengths[i] = header.getFieldLength(i);
            if(i > 0)
                fieldOffsets[i] = fieldOffsets[i -1] + header.getFieldLength(i - 1);
        }
        bytes = new byte[header.getRecordLength() - 1];
        

        // check if we working with a latin-1 char Charset
        final String cname = stringCharset.name();
        oneBytePerChar = "ISO-8859-1".equals(cname) || "US-ASCII".equals(cname);
        
        row = new Row();
    }

    protected int fill(final ByteBuffer buffer, final ReadableByteChannel channel)
            throws IOException {
        int r = buffer.remaining();
        // channel reads return -1 when EOF or other error
        // because they a non-blocking reads, 0 is a valid return value!!
        while (buffer.remaining() > 0 && r != -1) {
            r = channel.read(buffer);
        }
        if (r == -1) {
            buffer.limit(buffer.position());
        }
        return r;
    }

    private void bufferCheck() throws IOException {
        // remaining is less than record length
        // compact the remaining data and read again
        if(useMemoryMappedBuffer) {
            if(buffer.remaining() < header.getRecordLength()) {
                // ops, we're dealing with a DBF whose size is > 2GB (and < 4 normally?)
                FileChannel fc = (FileChannel) channel;
                int position = buffer.position();
                if(fc.size() > position + Integer.MAX_VALUE) {
                    currentOffset = position;
                } else {
                    currentOffset = fc.size() - Integer.MAX_VALUE;
                }
                NIOUtilities.clean(buffer);
                buffer = fc.map(MapMode.READ_ONLY, currentOffset, Integer.MAX_VALUE);
                
                buffer = ((FileChannel) channel).map(MapMode.READ_ONLY, buffer.position(), Integer.MAX_VALUE);
            }
        } else if (buffer.remaining() < header.getRecordLength()) {
            this.currentOffset += buffer.position();
            buffer.compact();
            fill(buffer, channel);
            buffer.position(0);
        }
    }

    /**
     * Get the header from this file. The header is read upon instantiation.
     * 
     * @return The header associated with this file or null if an error
     *         occurred.
     */
    public DbaseFileHeader getHeader() {
        return header;
    }

    /**
     * Clean up all resources associated with this reader.<B>Highly recomended.</B>
     * 
     * @throws IOException
     *                 If an error occurs.
     */
    public void close() throws IOException {
        if (channel != null && channel.isOpen()) {
            channel.close();
            streamLogger.close();
        }
        if(buffer != null) {
            NIOUtilities.clean(buffer, useMemoryMappedBuffer);
        }

        buffer = null;
        channel = null;
        bytes= null;
        header = null;
        row = null;
    }

    /**
     * Query the reader as to whether there is another record.
     * 
     * @return True if more records exist, false otherwise.
     */
    public boolean hasNext() {
        return cnt < header.getNumRecords() + 1;
    }

    /**
     * Get the next record (entry). Will return a new array of values.
     * 
     * @throws IOException
     *                 If an error occurs.
     * @return A new array of values.
     */
    public Object[] readEntry() throws IOException {
        return readEntry(new Object[header.getNumFields()]);
    }

    public Row readRow() throws IOException {
        read();
        return row;
    }

    /**
     * Skip the next record.
     * 
     * @throws IOException
     *                 If an error occurs.
     */
    public void skip() throws IOException {
        boolean foundRecord = false;
        while (!foundRecord) {

            bufferCheck();

            // read the deleted flag
            final char tempDeleted = (char) buffer.get();

            // skip the next bytes
            buffer.position(buffer.position() + header.getRecordLength() - 1); // the
            // 1 is
            // for
            // the
            // deleted
            // flag
            // just
            // read.

            // add the row if it is not deleted.
            if (tempDeleted != '*') {
                foundRecord = true;
            }
        }
        cnt++;
    }

    /**
     * Copy the next record into the array starting at offset.
     * 
     * @param entry
     *                Th array to copy into.
     * @param offset
     *                The offset to start at
     * @throws IOException
     *                 If an error occurs.
     * @return The same array passed in.
     */
    public Object[] readEntry(final Object[] entry, final int offset)
            throws IOException {
        if (entry.length - offset < header.getNumFields()) {
            throw new ArrayIndexOutOfBoundsException();
        }

        read();

        // retrieve the record length
        final int numFields = header.getNumFields();

        for (int j = 0; j < numFields; j++) {
            entry[j + offset] = readObject(fieldOffsets[j], j);
        }

        return entry;
    }
    
    /**
     * Reads a single field from the current record and returns it. Remember to call {@link #read()} before
     * starting to read fields from the dbf, and call it every time you need to move to the next record.
     * @param fieldNum The field number to be read (zero based)
     * @throws IOException
     *                 If an error occurs.
     * @return The value of the field
     */
    public Object readField(final int fieldNum)
            throws IOException {
        return readObject(fieldOffsets[fieldNum], fieldNum);
    }

    /**
     * Transfer, by bytes, the next record to the writer.
     */
    public void transferTo(final DbaseFileWriter writer) throws IOException {
        bufferCheck();
        buffer.limit(buffer.position() + header.getRecordLength());
        writer.channel.write(buffer);
        buffer.limit(buffer.capacity());

        cnt++;
    }

    /**
     * Reads the next record into memory. You need to use this directly when reading only
     * a subset of the fields using {@link #readField(int)}. 
     * @throws IOException
     */
    public void read() throws IOException {
        boolean foundRecord = false;
        while (!foundRecord) {

            bufferCheck();

            // read the deleted flag
            final char deleted = (char) buffer.get();
            if (deleted == '*') {
                continue;
            }

            buffer.limit(buffer.position() + header.getRecordLength() - 1);
            buffer.get(bytes); // SK: There is a side-effect here!!!
            buffer.limit(buffer.capacity());

            foundRecord = true;
        }

        cnt++;
    }

    /**
     * Copy the next entry into the array.
     * 
     * @param entry
     *                The array to copy into.
     * @throws IOException
     *                 If an error occurs.
     * @return The same array passed in.
     */
    public Object[] readEntry(final Object[] entry) throws IOException {
        return readEntry(entry, 0);
    }
    private Object readObject(final int fieldOffset, final int fieldNum)
            throws IOException {
        final char type = fieldTypes[fieldNum];
        final int fieldLen = fieldLengths[fieldNum];
        Object object = null;
        if (fieldLen > 0) {
            switch (type) {
            // (L)logical (T,t,F,f,Y,y,N,n)
            case 'l':
            case 'L':
                final char c = (char) bytes[fieldOffset];
                switch (c) {
                case 't':
                case 'T':
                case 'Y':
                case 'y':
                    object = Boolean.TRUE;
                    break;
                case 'f':
                case 'F':
                case 'N':
                case 'n':
                    object = Boolean.FALSE;
                    break;
                default:
                    // 0x20 should be interpreted as null, but we're going to be a bit more lax
                    object = null;
                }
                break;
            // (C)character (String)
            case 'c':
            case 'C':
                // if the string begins with a null terminator, the value is null
                if (bytes[fieldOffset] != '\0') {
                    // remember we need to skip trailing and leading spaces
                    if(oneBytePerChar) {
                        object = fastParse(bytes, fieldOffset, fieldLen).trim();
                    } else {
                        object = new String(bytes, fieldOffset, fieldLen, stringCharset.name()).trim();
                    }
                }
                break;
            // (D)date (Date)
            case 'd':
            case 'D':
                // If the first 8 characters are '0', this is a null date
                for (int i = 0; i < 8; i++) {
                    if (bytes[fieldOffset+i] != '0') {
                        try {
                            String tempString = fastParse(bytes,fieldOffset,4); 
                            final int tempYear = Integer.parseInt(tempString);
                            tempString =  fastParse(bytes,fieldOffset + 4,2);
                            final int tempMonth = Integer.parseInt(tempString) - 1;
                            tempString = fastParse(bytes,fieldOffset + 6,2); 
                            final int tempDay = Integer.parseInt(tempString);
                            calendar.clear();
                            calendar.set(Calendar.YEAR, tempYear);
                            calendar.set(Calendar.MONTH, tempMonth);
                            calendar.set(Calendar.DAY_OF_MONTH, tempDay);
                            object = calendar.getTime();
                        } catch (final NumberFormatException nfe) {
                            // todo: use progresslistener, this isn't a grave error.
                        }
                        break;
                    }
                }
                break;
            // (@) Timestamp (Date)
            case '@':
                try {      
                    //TODO: Find a smarter way to do this. 
                    //timestampBytes = bytes[fieldOffset:fieldOffset+7]
                    byte[] timestampBytes = {
                        // Time in millis, after reverse.
                        bytes[fieldOffset+7], bytes[fieldOffset+6], bytes[fieldOffset+5], bytes[fieldOffset+4],
                        // Days, after reverse.
                        bytes[fieldOffset+3], bytes[fieldOffset+2], bytes[fieldOffset+1], bytes[fieldOffset]                    
                    };
                       
                    ByteArrayInputStream i_bytes = new ByteArrayInputStream(timestampBytes);
                    DataInputStream i_stream = new DataInputStream(new BufferedInputStream(i_bytes));

                    int time = i_stream.readInt();
                    int days = i_stream.readInt();
                              
                    calendar.setTimeInMillis(days * MILLISECS_PER_DAY + DbaseFileHeader.MILLIS_SINCE_4713 + time);

                    object = calendar.getTime();

                } catch (final NumberFormatException nfe) {
                   // todo: use progresslistener, this isn't a grave error.
                }
                break;                
            // (N)umeric (Integer, Long or Fallthrough to Double)
            case 'n':
            case 'N':
                // numbers that begin with '*' are considered null
                if (bytes[fieldOffset] == '*') {
                    break;
                } else {
                    final String string = fastParse(bytes,fieldOffset,fieldLen).trim();
                    Class clazz = header.getFieldClass(fieldNum);
                    if (clazz == Integer.class) {
                        try {
                            object = Integer.parseInt(string);
                            break;
                        } catch (NumberFormatException e) {
                            // try to parse as long... 
                            clazz = Long.class; 
                        }
                    } 
                    if (clazz == Long.class) {
                        try {
                            object = Long.parseLong(string);
                            break;
                        } catch (final NumberFormatException e2) {
                            // fall through to the floating point number
                        }
                    }
                }
                // do not break, fall through to the 'f' case

            // (F)loating point number
            case 'f':
            case 'F': 
                if (bytes[fieldOffset] != '*') {
                    try {
                        object = Double.parseDouble(fastParse(bytes,fieldOffset,fieldLen));
                    } catch (final NumberFormatException e) {
                        // okay, now whatever we got was truly indigestible. Lets go
                        // with a zero Double.
                        object = new Double(0.0);
                    }
                }
                break;
            default:
                throw new IOException("Invalid field type : " + type);
            }

        }
        return object;
    }
    
    /**
     * Performs a faster byte[] to String conversion under the assumption the content
     * is represented with one byte per char 
     * @param fieldLen
     * @param fieldOffset
     * @return
     */
    String fastParse(final byte[] bytes, final int fieldOffset, final int fieldLen) {
        // faster reading path, the decoder is for some reason slower,
        // probably because it has to make extra checks to support multibyte chars
        final char[] chars = new char[fieldLen]; 
        for (int i = 0; i < fieldLen; i++) {
            // force the byte to a positive integer interpretation before casting to char
            chars[i] = ((char) (0x00FF & bytes[fieldOffset+i]));
        }
        return new String(chars);
    }

    public static void main(final String[] args) throws Exception {
        final DbaseFileReader reader = new DbaseFileReader(new ShpFiles(args[0]),
                false, Charset.forName("ISO-8859-1"), null);
        System.out.println(reader.getHeader());
        int r = 0;
        while (reader.hasNext()) {
            System.out.println(++r + ","
                    + java.util.Arrays.asList(reader.readEntry()));
        }
        reader.close();
    }

    public String id() {
        return getClass().getName();
    }

}
