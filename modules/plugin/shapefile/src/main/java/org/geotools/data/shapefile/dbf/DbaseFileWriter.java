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

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.text.FieldPosition;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.geotools.data.shapefile.StreamLogging;
import org.geotools.resources.NIOUtilities;

/**
 * A DbaseFileReader is used to read a dbase III format file. The general use of
 * this class is: <CODE><PRE>
 * DbaseFileHeader header = ...
 * WritableFileChannel out = new FileOutputStream(&quot;thefile.dbf&quot;).getChannel();
 * DbaseFileWriter w = new DbaseFileWriter(header,out);
 * while ( moreRecords ) {
 *   w.write( getMyRecord() );
 * }
 * w.close();
 * </PRE></CODE> You must supply the <CODE>moreRecords</CODE> and
 * <CODE>getMyRecord()</CODE> logic...
 * 
 * @author Ian Schneider
 *
 * @source $URL$
 */
public class DbaseFileWriter {
    private DbaseFileHeader header;
    private DbaseFileWriter.FieldFormatter formatter;
    WritableByteChannel channel;
    private ByteBuffer buffer;
    /**
     * The null values to use for each column. This will be accessed only when
     * null values are actually encountered, but it is allocated in the ctor
     * to save time and memory.
     */
    private final byte[][] nullValues;
    private StreamLogging streamLogger = new StreamLogging("Dbase File Writer");
    private Charset charset;
    private TimeZone timeZone;
    
    /**
     * Create a DbaseFileWriter using the specified header and writing to the
     * given channel.
     * 
     * @param header
     *                The DbaseFileHeader to write.
     * @param out
     *                The Channel to write to.
     * @throws IOException
     *                 If errors occur while initializing.
     */
    public DbaseFileWriter(DbaseFileHeader header, WritableByteChannel out)
            throws IOException {
        this(header, out, null, null);
    }
    
    /**
     * Create a DbaseFileWriter using the specified header and writing to the
     * given channel.
     * 
     * @param header
     *                The DbaseFileHeader to write.
     * @param out
     *                The Channel to write to.
     * @throws IOException
     *                 If errors occur while initializing.
     */
    public DbaseFileWriter(DbaseFileHeader header, WritableByteChannel out, Charset charset)
            throws IOException {
        this(header, out, charset, null);
    }
    

    /**
     * Create a DbaseFileWriter using the specified header and writing to the
     * given channel.
     * 
     * @param header
     *                The DbaseFileHeader to write.
     * @param out
     *                The Channel to write to.
     * @param charset The charset the dbf is (will be) encoded in
     * @throws IOException
     *                 If errors occur while initializing.
     */
    public DbaseFileWriter(DbaseFileHeader header, WritableByteChannel out, Charset charset, TimeZone timeZone)
            throws IOException {
        header.writeHeader(out);
        this.header = header;
        this.channel = out;
        this.charset = charset == null ? Charset.defaultCharset() : charset;
        this.timeZone = timeZone == null ? TimeZone.getDefault() : timeZone;
        this.formatter = new DbaseFileWriter.FieldFormatter(this.charset, this.timeZone);
        streamLogger.open();

        // As the 'shapelib' osgeo project does, we use specific values for
        // null cells. We can set up these values for each column once, in
        // the constructor, to save time and memory.
        nullValues = new byte[header.getNumFields()][];
        for (int i = 0; i < nullValues.length; i++) {
            char nullChar;
            switch (header.getFieldType(i)) {
            case 'C':
            case 'c':
            case 'M':
            case 'G':
                nullChar = '\0';
                break;
            case 'L':
            case 'l':
                nullChar = '?';
                break;
            case 'N':
            case 'n':
            case 'F':
            case 'f':
                nullChar = '*';
                break;
            case 'D':
            case 'd':
                nullChar = '0';
                break;
            case '@':
                // becomes day 0 time 0.
                nullChar = '\0';
                break;
            default:
                // catches at least 'D', and 'd'
                nullChar = '0';
                break;
            }
            nullValues[i] = new byte[header.getFieldLength(i)];
            Arrays.fill(nullValues[i], (byte)nullChar);
        }
        buffer = NIOUtilities.allocate(header.getRecordLength());
    }

    private void write() throws IOException {
        buffer.position(0);
        int r = buffer.remaining();
        while ((r -= channel.write(buffer)) > 0) {
            ; // do nothing
        }
    }
    
    /**
     * Write a single dbase record.
     * 
     * @param record
     *                The entries to write.
     * @throws IOException
     *                 If IO error occurs.
     * @throws DbaseFileException
     *                 If the entry doesn't comply to the header.
     */
    public void write(Object[] record) throws IOException, DbaseFileException {
        if (record.length != header.getNumFields()) {
            throw new DbaseFileException("Wrong number of fields "
                    + record.length + " expected " + header.getNumFields());
        }

        buffer.position(0);

        // put the 'not-deleted' marker
        buffer.put((byte) ' ');

        byte[] bytes;
        for (int i = 0; i < header.getNumFields(); i++) {
            // convert this column to bytes
            if (record[i] == null) {
                bytes = nullValues[i];
            } else {
                bytes = fieldBytes(record[i], i);
                // if the returned array is not the proper length
                // write a null instead; this will only happen
                // when the formatter handles a value improperly.
                if (bytes.length != nullValues[i].length) {
                    bytes = nullValues[i];
                }
            }
            buffer.put(bytes);
        }

        write();
    }

    
    /**
     * Called to convert the given object to bytes.
     * 
     * @param obj
     *            The value to convert; never null.
     * @param col
     *            The column this object will be encoded into.
     * @return The bytes of a string representation of the given object in the
     *         current character encoding.
     * @throws UnsupportedEncodingException Thrown if the current charset is unsupported. 
     */
    private byte[] fieldBytes(Object obj, final int col)
            throws UnsupportedEncodingException {
        String o;
        final int fieldLen = header.getFieldLength(col);
        switch (header.getFieldType(col)) {
        case 'C':
        case 'c':
            o = formatter.getFieldString(fieldLen, obj.toString());
            break;
        case 'L':
        case 'l':
            if (obj instanceof Boolean) {
                o = ((Boolean)obj).booleanValue() ? "T" : "F";
            } else {
                o = "?";
            }
            break;
        case 'M':
        case 'G':
            o = formatter.getFieldString(fieldLen, obj.toString());
            break;
        case 'N':
        case 'n':
            // int?
            if (header.getFieldDecimalCount(col) == 0) {
                o = formatter.getFieldString(fieldLen, 0, (Number)obj);
                break;
            }
        case 'F':
        case 'f':
            o = formatter.getFieldString(fieldLen,
                    header.getFieldDecimalCount(col),
                    (Number)obj);
            break;
        case 'D':
        case 'd':
            if (obj instanceof java.util.Calendar) {
                o = formatter.getFieldString(((Calendar) obj).getTime());

            } else {
                o = formatter.getFieldString((Date) obj);
            }
            break;
        case '@':
            o = formatter.getFieldStringDateTime((Date)obj);
            if (Boolean.getBoolean("org.geotools.shapefile.datetime")) {
                // Adding the charset to getBytes causes the output to
                // get altered for the '@: Timestamp' field.
                // And using String.getBytes returns a different array
                // in 64-bit platforms so we get chars and cast to byte
                // one element at a time.
                char[] carr = o.toCharArray();
                byte[] barr = new byte[carr.length];
                for (int i = 0; i < carr.length; i++) {
                    barr[i] = (byte)carr[i];
                }                            
                return barr;
            }
            break;   
        default:
            throw new RuntimeException("Unknown type "
                    + header.getFieldType(col));
        }

        // convert the string to bytes with the given charset.
        return o.getBytes(charset.name());
    }

    /**
     * Release resources associated with this writer. <B>Highly recommended</B>
     * 
     * @throws IOException
     *                 If errors occur.
     */
    public void close() throws IOException {
        // IANS - GEOT 193, bogus 0x00 written. According to dbf spec, optional
        // eof 0x1a marker is, well, optional. Since the original code wrote a
        // 0x00 (which is wrong anyway) lets just do away with this :)
        // - produced dbf works in OpenOffice and ArcExplorer java, so it must
        // be okay.
        // buffer.position(0);
        // buffer.put((byte) 0).position(0).limit(1);
        // write();
        if (channel != null && channel.isOpen()) {
            channel.close();
            streamLogger.close();
        }
        if(buffer != null) {
            NIOUtilities.clean(buffer, false);
        }
        buffer = null;
        channel = null;
        formatter = null;
    }

    /** Utility for formatting Dbase fields. */
    public static class FieldFormatter {
        private StringBuffer buffer = new StringBuffer(255);
        private NumberFormat numFormat = NumberFormat.getNumberInstance(Locale.US);
        private Calendar calendar;
        private final long MILLISECS_PER_DAY = 24*60*60*1000;

        private String emptyString;
        private static final int MAXCHARS = 255;
        private Charset charset;

        public FieldFormatter(Charset charset, TimeZone timeZone) {
            // Avoid grouping on number format
            numFormat.setGroupingUsed(false);

            // build a 255 white spaces string
            StringBuffer sb = new StringBuffer(MAXCHARS);
            sb.setLength(MAXCHARS);
            for (int i = 0; i < MAXCHARS; i++) {
                sb.setCharAt(i, ' ');
            }
            
            this.charset = charset;
            
            this.calendar = Calendar.getInstance(timeZone, Locale.US);

            emptyString = sb.toString();
        }

        public String getFieldString(int size, String s) {
            try {
                buffer.replace(0, size, emptyString);
                buffer.setLength(size);
                // international characters must be accounted for so size != length.
                int maxSize = size;
                if (s != null) {
                    buffer.replace(0, size, s);
                    int currentBytes = s.substring(0, Math.min(size, s.length()))
                            .getBytes(charset.name()).length;
                    if (currentBytes > size) {
                        char[] c = new char[1];
                        for (int index = size - 1; currentBytes > size; index--) {
                            c[0] = buffer.charAt(index);
                            String string = new String(c);
                            buffer.deleteCharAt(index);
                            currentBytes -= string.getBytes().length;
                            maxSize--;
                        }
                    } else {
                        if (s.length() < size) {
                            maxSize = size - (currentBytes - s.length());
                            for (int i = s.length(); i < size; i++) {
                                buffer.append(' ');
                            }
                        }
                    }
                }

                buffer.setLength(maxSize);
    
                return buffer.toString();
            } catch(UnsupportedEncodingException e) {
                throw new RuntimeException("This error should never occurr", e);
            }
        }

        public String getFieldString(Date d) {

            if (d != null) {
                buffer.delete(0, buffer.length());
                
                calendar.setTime(d);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH) + 1; // returns 0
                                                                // based month?
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                if (year < 1000) {
                    if (year >= 100) {
                        buffer.append("0");
                    } else if (year >= 10) {
                        buffer.append("00");
                    } else {
                        buffer.append("000");
                    }
                }
                buffer.append(year);

                if (month < 10) {
                    buffer.append("0");
                }
                buffer.append(month);

                if (day < 10) {
                    buffer.append("0");
                }
                buffer.append(day);
            } else {
                buffer.setLength(8);
                buffer.replace(0, 8, emptyString);
            }

            buffer.setLength(8);
            return buffer.toString();
        }

        public String getFieldStringDateTime(Date d) {
              
            // Sanity check
            if (d == null) return null;
            
            final long difference = d.getTime() - DbaseFileHeader.MILLIS_SINCE_4713;
            
            final int days = (int) (difference / MILLISECS_PER_DAY);
            final int time = (int) (difference % MILLISECS_PER_DAY);
            
            try{
                ByteArrayOutputStream o_bytes = new ByteArrayOutputStream();
                DataOutputStream o_stream;
                o_stream = new DataOutputStream(new BufferedOutputStream(o_bytes));
                o_stream.writeInt(days);
                o_stream.writeInt(time);
                o_stream.flush();                       
                byte[] bytes = o_bytes.toByteArray();
                // Cast the byte values to char as a workaround for erroneous byte
                // array retrieval in 64-bit machines
                char[] out = {
                    // Days, after reverse.
                    (char) bytes[3], (char) bytes[2],(char)  bytes[1], (char) bytes[0], 
                    // Time in millis, after reverse.
                    (char) bytes[7], (char) bytes[6], (char) bytes[5], (char) bytes[4],
                };

                return  new String(out);   
            }catch(IOException e){
                // This is always just a int serialization, 
                // there is no way to recover from here.
                return null;
            }       
        }
        
        public String getFieldString(int size, int decimalPlaces, Number n) {
            buffer.delete(0, buffer.length());

            if (n != null) {
                numFormat.setMaximumFractionDigits(decimalPlaces);
                numFormat.setMinimumFractionDigits(decimalPlaces);
                numFormat.format(n, buffer, new FieldPosition(
                        NumberFormat.INTEGER_FIELD));
            }

            int diff = size - buffer.length();
            if (diff >= 0) {
                while (diff-- > 0) {
                    buffer.insert(0, ' ');
                }
            } else {
                buffer.setLength(size);
            }
            return buffer.toString();
        }
    }

}
