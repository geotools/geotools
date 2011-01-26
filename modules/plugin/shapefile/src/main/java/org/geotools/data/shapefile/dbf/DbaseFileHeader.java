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

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.resources.NIOUtilities;

/**
 * Class to represent the header of a Dbase III file. Creation date: (5/15/2001
 * 5:15:30 PM)
 * 
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/shapefile/src/main/java/org/geotools/data/shapefile/dbf/DbaseFileHeader.java $
 */
public class DbaseFileHeader {
    // Constant for the size of a record
    private static final int FILE_DESCRIPTOR_SIZE = 32;

    // type of the file, must be 03h
    private static final byte MAGIC = 0x03;

    private static final int MINIMUM_HEADER = 33;

    // Date the file was last updated.
    private Date date = new Date();

    private int recordCnt = 0;

    private int fieldCnt = 0;

    // set this to a default length of 1, which is enough for one "space"
    // character which signifies an empty record
    private int recordLength = 1;

    // set this to a flagged value so if no fields are added before the write,
    // we know to adjust the headerLength to MINIMUM_HEADER
    private int headerLength = -1;

    private int largestFieldSize = 0;

    private Logger logger = org.geotools.util.logging.Logging
            .getLogger("org.geotools.data.shapefile");

    /**
     * Returns the number of millis at January 1st 4713 BC
     * 
     *  Calendar refCal = (Calendar) new GregorianCalendar(TimeZone.getTimeZone("UTC"));
     *   refCal.set(Calendar.ERA, GregorianCalendar.BC);
     *   refCal.set(Calendar.YEAR, 4713);
     *   refCal.set(Calendar.MONTH, Calendar.JANUARY);
     *   refCal.set(Calendar.DAY_OF_MONTH, 1);
     *   refCal.set(Calendar.HOUR, 12);
     *   refCal.set(Calendar.MINUTE, 0);
     *   refCal.set(Calendar.SECOND, 0);
     *   refCal.set(Calendar.MILLISECOND, 0);
     *   MILLIS_SINCE_4713 = refCal.getTimeInMillis() - 43200000L; 
     *   //(43200000L: 12 hour correction factor taken from DBFViewer2000)
     */
    public static long MILLIS_SINCE_4713 = -210866803200000L;
    
    /**
     * Class for holding the information associated with a record.
     */
    class DbaseField {

        // Field Name
        String fieldName;

        // Field Type (C N L D @ or M)
        char fieldType;

        // Field Data Address offset from the start of the record.
        int fieldDataAddress;

        // Length of the data in bytes
        int fieldLength;

        // Field decimal count in Binary, indicating where the decimal is
        int decimalCount;

    }

    // Collection of header records.
    // lets start out with a zero-length array, just in case
    private DbaseField[] fields = new DbaseField[0];

    private void read(ByteBuffer buffer, ReadableByteChannel channel)
            throws IOException {
        while (buffer.remaining() > 0) {
            if (channel.read(buffer) == -1) {
                throw new EOFException("Premature end of file");
            }
        }
    }

    /**
     * Determine the most appropriate Java Class for representing the data in
     * the field.
     * 
     * <PRE>
     * All packages are java.lang unless otherwise specified.
     * C (Character) -&gt; String
     * N (Numeric)   -&gt; Integer or Long or Double (depends on field's decimal count and fieldLength)
     * F (Floating)  -&gt; Double
     * L (Logical)   -&gt; Boolean
     * D (Date)      -&gt; java.util.Date (Without time)
     * @ (Timestamp) -&gt; java.sql.Timestamp (With time)
     * Unknown       -&gt; String
     * </PRE>
     * 
     * @param i
     *                The index of the field, from 0 to
     *                <CODE>getNumFields() - 1</CODE> .
     * @return A Class which closely represents the dbase field type.
     */
    public Class getFieldClass(int i) {
        Class typeClass = null;

        switch (fields[i].fieldType) {
        case 'C':
            typeClass = String.class;
            break;

        case 'N':
            if (fields[i].decimalCount == 0) {
                if (fields[i].fieldLength < 10) {
                    typeClass = Integer.class;
                } else {
                    typeClass = Long.class;
                }
            } else {
                typeClass = Double.class;
            }
            break;

        case 'F':
            typeClass = Double.class;
            break;

        case 'L':
            typeClass = Boolean.class;
            break;

        case 'D':
            typeClass = Date.class;
            break;
            
        case '@':
            typeClass = Timestamp.class;
            break;
            
        default:
            typeClass = String.class;
            break;
        }

        return typeClass;
    }

    /**
     * Add a column to this DbaseFileHeader. The type is one of (C N L or D)
     * character, number, logical(true/false), or date. The Field length is the
     * total length in bytes reserved for this column. The decimal count only
     * applies to numbers(N), and floating point values (F), and refers to the
     * number of characters to reserve after the decimal point. <B>Don't expect
     * miracles from this...</B>
     * 
     * <PRE>
     * Field Type MaxLength
     * ---------- ---------
     * C          254
     * D          8
     * @          8
     * F          20
     * N          18
     * </PRE>
     * 
     * @param inFieldName
     *                The name of the new field, must be less than 10 characters
     *                or it gets truncated.
     * @param inFieldType
     *                A character representing the dBase field, ( see above ).
     *                Case insensitive.
     * @param inFieldLength
     *                The length of the field, in bytes ( see above )
     * @param inDecimalCount
     *                For numeric fields, the number of decimal places to track.
     * @throws DbaseFileException
     *                 If the type is not recognized.
     */
    public void addColumn(String inFieldName, char inFieldType,
            int inFieldLength, int inDecimalCount) throws DbaseFileException {
        if (inFieldLength <= 0) {
            throw new DbaseFileException("field length <= 0");
        }
        if (fields == null) {
            fields = new DbaseField[0];
        }
        int tempLength = 1; // the length is used for the offset, and there is a
                            // * for deleted as the first byte
        DbaseField[] tempFieldDescriptors = new DbaseField[fields.length + 1];
        for (int i = 0; i < fields.length; i++) {
            fields[i].fieldDataAddress = tempLength;
            tempLength = tempLength + fields[i].fieldLength;
            tempFieldDescriptors[i] = fields[i];
        }
        tempFieldDescriptors[fields.length] = new DbaseField();
        tempFieldDescriptors[fields.length].fieldLength = inFieldLength;
        tempFieldDescriptors[fields.length].decimalCount = inDecimalCount;
        tempFieldDescriptors[fields.length].fieldDataAddress = tempLength;

        // set the field name
        String tempFieldName = inFieldName;
        if (tempFieldName == null) {
            tempFieldName = "NoName";
        }
        // Fix for GEOT-42, ArcExplorer will not handle field names > 10 chars
        // Sorry folks.
        if (tempFieldName.length() > 10) {
            tempFieldName = tempFieldName.substring(0, 10);
            if (logger.isLoggable(Level.WARNING)) {
                logger.warning("FieldName " + inFieldName
                        + " is longer than 10 characters, truncating to "
                        + tempFieldName);
            }
        }
        tempFieldDescriptors[fields.length].fieldName = tempFieldName;

        // the field type
        if ((inFieldType == 'C') || (inFieldType == 'c')) {
            tempFieldDescriptors[fields.length].fieldType = 'C';
            if (inFieldLength > 254) {
                if (logger.isLoggable(Level.FINE)) {
                    logger
                            .fine("Field Length for "
                                    + inFieldName
                                    + " set to "
                                    + inFieldLength
                                    + " Which is longer than 254, not consistent with dbase III");
                }
            }
        } else if ((inFieldType == 'S') || (inFieldType == 's')) {
            tempFieldDescriptors[fields.length].fieldType = 'C';
            if (logger.isLoggable(Level.WARNING)) {
                logger
                        .warning("Field type for "
                                + inFieldName
                                + " set to S which is flat out wrong people!, I am setting this to C, in the hopes you meant character.");
            }
            if (inFieldLength > 254) {
                if (logger.isLoggable(Level.FINE)) {
                    logger
                            .fine("Field Length for "
                                    + inFieldName
                                    + " set to "
                                    + inFieldLength
                                    + " Which is longer than 254, not consistent with dbase III");
                }
            }
            tempFieldDescriptors[fields.length].fieldLength = 8;
        } else if ((inFieldType == 'D') || (inFieldType == 'd')) {
            tempFieldDescriptors[fields.length].fieldType = 'D';
            if (inFieldLength != 8) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Field Length for " + inFieldName + " set to "
                            + inFieldLength + " Setting to 8 digits YYYYMMDD");
                }
            }
            tempFieldDescriptors[fields.length].fieldLength = 8;
        } else if (inFieldType == '@') {
            tempFieldDescriptors[fields.length].fieldType = '@';
            if (inFieldLength != 8) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Field Length for " + inFieldName + " set to "
                            + inFieldLength + " Setting to 8 digits - two longs,"
                            + "one long for date and one long for time");
                }
            }
            tempFieldDescriptors[fields.length].fieldLength = 8;   
        } else if ((inFieldType == 'F') || (inFieldType == 'f')) {
            tempFieldDescriptors[fields.length].fieldType = 'F';
            if (inFieldLength > 20) {
                if (logger.isLoggable(Level.FINE)) {
                    logger
                            .fine("Field Length for "
                                    + inFieldName
                                    + " set to "
                                    + inFieldLength
                                    + " Preserving length, but should be set to Max of 20 not valid for dbase IV, and UP specification, not present in dbaseIII.");
                }
            }
        } else if ((inFieldType == 'N') || (inFieldType == 'n')) {
            tempFieldDescriptors[fields.length].fieldType = 'N';
            if (inFieldLength > 18) {
                if (logger.isLoggable(Level.FINE)) {
                    logger
                            .fine("Field Length for "
                                    + inFieldName
                                    + " set to "
                                    + inFieldLength
                                    + " Preserving length, but should be set to Max of 18 for dbase III specification.");
                }
            }
            if (inDecimalCount < 0) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Field Decimal Position for " + inFieldName
                            + " set to " + inDecimalCount
                            + " Setting to 0 no decimal data will be saved.");
                }
                tempFieldDescriptors[fields.length].decimalCount = 0;
            }
            if (inDecimalCount > inFieldLength - 1) {
                if (logger.isLoggable(Level.WARNING)) {
                    logger.warning("Field Decimal Position for " + inFieldName
                            + " set to " + inDecimalCount + " Setting to "
                            + (inFieldLength - 1)
                            + " no non decimal data will be saved.");
                }
                tempFieldDescriptors[fields.length].decimalCount = inFieldLength - 1;
            }
        } else if ((inFieldType == 'L') || (inFieldType == 'l')) {
            tempFieldDescriptors[fields.length].fieldType = 'L';
            if (inFieldLength != 1) {
                if (logger.isLoggable(Level.FINE)) {
                    logger.fine("Field Length for " + inFieldName + " set to "
                            + inFieldLength
                            + " Setting to length of 1 for logical fields.");
                }
            }
            tempFieldDescriptors[fields.length].fieldLength = 1;
        } else {
            throw new DbaseFileException("Undefined field type " + inFieldType
                    + " For column " + inFieldName);
        }
        // the length of a record
        tempLength = tempLength
                + tempFieldDescriptors[fields.length].fieldLength;

        // set the new fields.
        fields = tempFieldDescriptors;
        fieldCnt = fields.length;
        headerLength = MINIMUM_HEADER + 32 * fields.length;
        recordLength = tempLength;
    }

    /**
     * Remove a column from this DbaseFileHeader.
     * 
     * @todo This is really ugly, don't know who wrote it, but it needs fixing...
     * @param inFieldName
     *                The name of the field, will ignore case and trim.
     * @return index of the removed column, -1 if no found
     */
    public int removeColumn(String inFieldName) {

        int retCol = -1;
        int tempLength = 1;
        DbaseField[] tempFieldDescriptors = new DbaseField[fields.length - 1];
        for (int i = 0, j = 0; i < fields.length; i++) {
            if (!inFieldName.equalsIgnoreCase(fields[i].fieldName.trim())) {
                // if this is the last field and we still haven't found the
                // named field
                if (i == j && i == fields.length - 1) {
                    System.err.println("Could not find a field named '"
                            + inFieldName + "' for removal");
                    return retCol;
                }
                tempFieldDescriptors[j] = fields[i];
                tempFieldDescriptors[j].fieldDataAddress = tempLength;
                tempLength += tempFieldDescriptors[j].fieldLength;
                // only increment j on non-matching fields
                j++;
            } else {
                retCol = i;
            }
        }

        // set the new fields.
        fields = tempFieldDescriptors;
        headerLength = 33 + 32 * fields.length;
        recordLength = tempLength;

        return retCol;
    }

    // Retrieve the length of the field at the given index
    /**
     * Returns the field length in bytes.
     * 
     * @param inIndex
     *                The field index.
     * @return The length in bytes.
     */
    public int getFieldLength(int inIndex) {
        return fields[inIndex].fieldLength;
    }

    // Retrieve the location of the decimal point within the field.
    /**
     * Get the decimal count of this field.
     * 
     * @param inIndex
     *                The field index.
     * @return The decimal count.
     */
    public int getFieldDecimalCount(int inIndex) {
        return fields[inIndex].decimalCount;
    }

    // Retrieve the Name of the field at the given index
    /**
     * Get the field name.
     * 
     * @param inIndex
     *                The field index.
     * @return The name of the field.
     */
    public String getFieldName(int inIndex) {
        return fields[inIndex].fieldName;
    }

    // Retrieve the type of field at the given index
    /**
     * Get the character class of the field.
     * 
     * @param inIndex
     *                The field index.
     * @return The dbase character representing this field.
     */
    public char getFieldType(int inIndex) {
        return fields[inIndex].fieldType;
    }

    /**
     * Get the date this file was last updated.
     * 
     * @return The Date last modified.
     */
    public Date getLastUpdateDate() {
        return date;
    }

    /**
     * Return the number of fields in the records.
     * 
     * @return The number of fields in this table.
     */
    public int getNumFields() {
        return fields.length;
    }

    /**
     * Return the number of records in the file
     * 
     * @return The number of records in this table.
     */
    public int getNumRecords() {
        return recordCnt;
    }

    /**
     * Get the length of the records in bytes.
     * 
     * @return The number of bytes per record.
     */
    public int getRecordLength() {
        return recordLength;
    }

    /**
     * Get the length of the header
     * 
     * @return The length of the header in bytes.
     */
    public int getHeaderLength() {
        return headerLength;
    }
    
    /**
     * Read the header data from the DBF file.
     * 
     * @param channel
     *                A readable byte channel. If you have an InputStream you
     *                need to use, you can call
     *                java.nio.Channels.getChannel(InputStream in).
     * @throws IOException
     *                 If errors occur while reading.
     */
    public void readHeader(ReadableByteChannel channel) throws IOException {
        readHeader(channel, Charset.defaultCharset());
    }

    /**
     * Read the header data from the DBF file.
     * 
     * @param channel
     *                A readable byte channel. If you have an InputStream you
     *                need to use, you can call
     *                java.nio.Channels.getChannel(InputStream in).
     * @throws IOException
     *                 If errors occur while reading.
     */
    public void readHeader(ReadableByteChannel channel, Charset charset) throws IOException {
        // we'll read in chunks of 1K
        ByteBuffer in = NIOUtilities.allocate(1024);
        try {
            // do this or GO CRAZY
            // ByteBuffers come preset to BIG_ENDIAN !
            in.order(ByteOrder.LITTLE_ENDIAN);
    
            // only want to read first 10 bytes...
            in.limit(10);
    
            read(in, channel);
            in.position(0);
    
            // type of file.
            byte magic = in.get();
            if (magic != MAGIC) {
                throw new IOException("Unsupported DBF file Type "
                        + Integer.toHexString(magic));
            }
    
            // parse the update date information.
            int tempUpdateYear = in.get();
            int tempUpdateMonth = in.get();
            int tempUpdateDay = in.get();
            // ouch Y2K uncompliant
            if (tempUpdateYear > 90) {
                tempUpdateYear = tempUpdateYear + 1900;
            } else {
                tempUpdateYear = tempUpdateYear + 2000;
            }
            Calendar c = Calendar.getInstance();
            c.set(Calendar.YEAR, tempUpdateYear);
            c.set(Calendar.MONTH, tempUpdateMonth - 1);
            c.set(Calendar.DATE, tempUpdateDay);
            date = c.getTime();
    
            // read the number of records.
            recordCnt = in.getInt();
    
            // read the length of the header structure.
            // ahhh.. unsigned little-endian shorts
            // mask out the byte and or it with shifted 2nd byte
            headerLength = (in.get() & 0xff) | ((in.get() & 0xff) << 8);
    
            // if the header is bigger than our 1K, reallocate
            if (headerLength > in.capacity()) {
                NIOUtilities.clean(in, false);
                in = NIOUtilities.allocate(headerLength - 10);
            }
            in.limit(headerLength - 10);
            in.position(0);
            read(in, channel);
            in.position(0);
    
            // read the length of a record
            // ahhh.. unsigned little-endian shorts
            recordLength = (in.get() & 0xff) | ((in.get() & 0xff) << 8);
    
            // skip the reserved bytes in the header.
            in.position(in.position() + 20);
    
            // calculate the number of Fields in the header
            fieldCnt = (headerLength - FILE_DESCRIPTOR_SIZE - 1)
                    / FILE_DESCRIPTOR_SIZE;
    
            // read all of the header records
            List lfields = new ArrayList();
            for (int i = 0; i < fieldCnt; i++) {
                DbaseField field = new DbaseField();
    
                // read the field name
                byte[] buffer = new byte[11];
                in.get(buffer);
                String name = new String(buffer, charset.name());
                int nullPoint = name.indexOf(0);
                if (nullPoint != -1) {
                    name = name.substring(0, nullPoint);
                }
                field.fieldName = name.trim();
    
                // read the field type
                field.fieldType = (char) in.get();
    
                // read the field data address, offset from the start of the record.
                field.fieldDataAddress = in.getInt();
    
                // read the field length in bytes
                int length = (int) in.get();
                if (length < 0) {
                    length = length + 256;
                }
                field.fieldLength = length;
    
                if (length > largestFieldSize) {
                    largestFieldSize = length;
                }
    
                // read the field decimal count in bytes
                field.decimalCount = (int) in.get();
    
                // reserved bytes.
                // in.skipBytes(14);
                in.position(in.position() + 14);
    
                // some broken shapefiles have 0-length attributes. The reference
                // implementation
                // (ArcExplorer 2.0, built with MapObjects) just ignores them.
                if (field.fieldLength > 0) {
                    lfields.add(field);
                }
            }
    
            // Last byte is a marker for the end of the field definitions.
            // in.skipBytes(1);
            in.position(in.position() + 1);
    
            fields = new DbaseField[lfields.size()];
            fields = (DbaseField[]) lfields.toArray(fields);
        } finally {
            NIOUtilities.clean(in, false);
        }
    }
    
    /**
     * Read the header data from the DBF file.
     * 
     * @param channel
     *                A readable byte channel. If you have an InputStream you
     *                need to use, you can call
     *                java.nio.Channels.getChannel(InputStream in).
     * @throws IOException
     *                 If errors occur while reading.
     */
    public void readHeader(ByteBuffer in) throws IOException {
        // do this or GO CRAZY
        // ByteBuffers come preset to BIG_ENDIAN !
        in.order(ByteOrder.LITTLE_ENDIAN);

        // type of file.
        byte magic = in.get();
        if (magic != MAGIC) {
            throw new IOException("Unsupported DBF file Type "
                    + Integer.toHexString(magic));
        }

        // parse the update date information.
        int tempUpdateYear = in.get();
        int tempUpdateMonth = in.get();
        int tempUpdateDay = in.get();
        // ouch Y2K uncompliant
        if (tempUpdateYear > 90) {
            tempUpdateYear = tempUpdateYear + 1900;
        } else {
            tempUpdateYear = tempUpdateYear + 2000;
        }
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, tempUpdateYear);
        c.set(Calendar.MONTH, tempUpdateMonth - 1);
        c.set(Calendar.DATE, tempUpdateDay);
        date = c.getTime();

        // read the number of records.
        recordCnt = in.getInt();

        // read the length of the header structure.
        // ahhh.. unsigned little-endian shorts
        // mask out the byte and or it with shifted 2nd byte
        headerLength = (in.get() & 0xff) | ((in.get() & 0xff) << 8);

        // if the header is bigger than our 1K, reallocate
        if (headerLength > in.capacity()) {
            throw new IllegalArgumentException("The contract says the buffer should be long enough to fit all the header!");
        }

        // read the length of a record
        // ahhh.. unsigned little-endian shorts
        recordLength = (in.get() & 0xff) | ((in.get() & 0xff) << 8);

        // skip the reserved bytes in the header.
        in.position(in.position() + 20);

        // calculate the number of Fields in the header
        fieldCnt = (headerLength - FILE_DESCRIPTOR_SIZE - 1)
                / FILE_DESCRIPTOR_SIZE;

        // read all of the header records
        List lfields = new ArrayList();
        for (int i = 0; i < fieldCnt; i++) {
            DbaseField field = new DbaseField();

            // read the field name
            byte[] buffer = new byte[11];
            in.get(buffer);
            String name = new String(buffer);
            int nullPoint = name.indexOf(0);
            if (nullPoint != -1) {
                name = name.substring(0, nullPoint);
            }
            field.fieldName = name.trim();

            // read the field type
            field.fieldType = (char) in.get();

            // read the field data address, offset from the start of the record.
            field.fieldDataAddress = in.getInt();

            // read the field length in bytes
            int length = (int) in.get();
            if (length < 0) {
                length = length + 256;
            }
            field.fieldLength = length;

            if (length > largestFieldSize) {
                largestFieldSize = length;
            }

            // read the field decimal count in bytes
            field.decimalCount = (int) in.get();

            // reserved bytes.
            // in.skipBytes(14);
            in.position(in.position() + 14);

            // some broken shapefiles have 0-length attributes. The reference
            // implementation
            // (ArcExplorer 2.0, built with MapObjects) just ignores them.
            if (field.fieldLength > 0) {
                lfields.add(field);
            }
        }

        // Last byte is a marker for the end of the field definitions.
        // in.skipBytes(1);
        in.position(in.position() + 1);

        fields = new DbaseField[lfields.size()];
        fields = (DbaseField[]) lfields.toArray(fields);
    }

    /**
     * Get the largest field size of this table.
     * 
     * @return The largest field size in bytes.
     */
    public int getLargestFieldSize() {
        return largestFieldSize;
    }

    /**
     * Set the number of records in the file
     * 
     * @param inNumRecords
     *                The number of records.
     */
    public void setNumRecords(int inNumRecords) {
        recordCnt = inNumRecords;
    }

    /**
     * Write the header data to the DBF file.
     * 
     * @param out
     *                A channel to write to. If you have an OutputStream you can
     *                obtain the correct channel by using
     *                java.nio.Channels.newChannel(OutputStream out).
     * @throws IOException
     *                 If errors occur.
     */
    public void writeHeader(WritableByteChannel out) throws IOException {
        // take care of the annoying case where no records have been added...
        if (headerLength == -1) {
            headerLength = MINIMUM_HEADER;
        }
        ByteBuffer buffer = NIOUtilities.allocate(headerLength);
        try {
            buffer.order(ByteOrder.LITTLE_ENDIAN);
    
            // write the output file type.
            buffer.put((byte) MAGIC);
    
            // write the date stuff
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            buffer.put((byte) (c.get(Calendar.YEAR) % 100));
            buffer.put((byte) (c.get(Calendar.MONTH) + 1));
            buffer.put((byte) (c.get(Calendar.DAY_OF_MONTH)));
    
            // write the number of records in the datafile.
            buffer.putInt(recordCnt);
    
            // write the length of the header structure.
            buffer.putShort((short) headerLength);
    
            // write the length of a record
            buffer.putShort((short) recordLength);
    
            // // write the reserved bytes in the header
            // for (int i=0; i<20; i++) out.writeByteLE(0);
            buffer.position(buffer.position() + 20);
    
            // write all of the header records
            int tempOffset = 0;
            for (int i = 0; i < fields.length; i++) {
    
                // write the field name
                for (int j = 0; j < 11; j++) {
                    if (fields[i].fieldName.length() > j) {
                        buffer.put((byte) fields[i].fieldName.charAt(j));
                    } else {
                        buffer.put((byte) 0);
                    }
                }
    
                // write the field type
                buffer.put((byte) fields[i].fieldType);
                // // write the field data address, offset from the start of the
                // record.
                buffer.putInt(tempOffset);
                tempOffset += fields[i].fieldLength;
    
                // write the length of the field.
                buffer.put((byte) fields[i].fieldLength);
    
                // write the decimal count.
                buffer.put((byte) fields[i].decimalCount);
    
                // write the reserved bytes.
                // for (in j=0; jj<14; j++) out.writeByteLE(0);
                buffer.position(buffer.position() + 14);
            }
    
            // write the end of the field definitions marker
            buffer.put((byte) 0x0D);
    
            buffer.position(0);
    
            int r = buffer.remaining();
            while ((r -= out.write(buffer)) > 0) {
                ; // do nothing
            }
        } finally {
            NIOUtilities.clean(buffer, false);
        }
    }

    /**
     * Get a simple representation of this header.
     * 
     * @return A String representing the state of the header.
     */
    public String toString() {
        StringBuffer fs = new StringBuffer();
        for (int i = 0, ii = fields.length; i < ii; i++) {
            DbaseField f = fields[i];
            fs.append(f.fieldName + " " + f.fieldType + " " + f.fieldLength
                    + " " + f.decimalCount + " " + f.fieldDataAddress + "\n");
        }

        return "DB3 Header\n" + "Date : " + date + "\n" + "Records : "
                + recordCnt + "\n" + "Fields : " + fieldCnt + "\n" + fs;

    }

}
