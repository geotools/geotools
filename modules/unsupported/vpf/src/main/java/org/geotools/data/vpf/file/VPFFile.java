/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.vpf.file;

import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_2_COORD_F;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_2_COORD_R;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_3_COORD_F;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_3_COORD_R;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_LEVEL1_TEXT;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_LEVEL2_TEXT;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_LEVEL3_TEXT;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_LONG_FLOAT;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_LONG_FLOAT_LEN;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_LONG_INTEGER;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_LONG_INTEGER_LEN;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_NULL_FIELD;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_SHORT_FLOAT;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_SHORT_FLOAT_LEN;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_SHORT_INTEGER;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_SHORT_INTEGER_LEN;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_TEXT;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.DATA_TRIPLET_ID;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.LEAST_SIGNIF_FIRST;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.LITTLE_ENDIAN_ORDER;
import static org.geotools.data.vpf.ifc.DataTypesDefinition.STRING_NULL_VALUE;
import static org.geotools.data.vpf.ifc.FileConstants.FEATURE_CLASS_SCHEMA_TABLE;
import static org.geotools.data.vpf.ifc.FileConstants.TEXT_PRIMITIVE;
import static org.geotools.data.vpf.ifc.FileConstants.VPF_ELEMENT_SEPARATOR;
import static org.geotools.data.vpf.ifc.FileConstants.VPF_FIELD_SEPARATOR;
import static org.geotools.data.vpf.ifc.FileConstants.VPF_RECORD_SEPARATOR;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.AbstractList;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.data.vpf.VPFColumn;
import org.geotools.data.vpf.VPFLogger;
import org.geotools.data.vpf.exc.VPFHeaderFormatException;
import org.geotools.data.vpf.io.TripletId;
import org.geotools.data.vpf.io.VPFInputStream;
import org.geotools.data.vpf.io.VariableIndexInputStream;
import org.geotools.data.vpf.io.VariableIndexRow;
import org.geotools.data.vpf.util.DataUtils;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AnnotationFeatureType;
import org.geotools.text.Text;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateList;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

/**
 * This class encapsulates VPF files, serving as a factory for VPFColumns. Instances of this class should be created by
 * VPFFileFactory.
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 * @source $URL$
 */
public class VPFFile {

    //    private final TableInputStream stream;
    private static String ACCESS_MODE = "r";

    static final Logger LOGGER = Logging.getLogger(VPFFile.class);
    /**
     * Variable <code>byteOrder</code> keeps value of byte order in which table is written:
     *
     * <ul>
     *   <li><b>L</b> - least-significant-first
     *   <li><b>M</b> - most-significant-first
     * </ul>
     */
    private char byteOrder = LEAST_SIGNIF_FIRST;
    /** The columns of the file. This list shall contain objects of type <code>VPFColumn</code> */
    private final List<VPFColumn> columns = new ArrayList<>();

    /** Variable <code>description</code> keeps value of text description of the table's contents. */
    private String description = null;

    /** The contained Feature Type */
    private final SimpleFeatureType featureType;

    /** Keeps value of length of ASCII header string (i.e., the remaining information after this field) */
    private int headerLength = -0;

    /** The associated stream */
    private RandomAccessFile inputStream = null;

    /**
     * Variable <code>narrativeTable</code> keeps value of an optional narrative file which contains miscellaneous
     * information about the table.
     */
    private String narrativeTable = null;

    /** The path name */
    private final String pathName;

    /** Describe variable <code>variableIndex</code> here. */
    private VPFInputStream variableIndex = null;

    /**
     * Constructor.
     *
     * @param cPathName The path to this file
     * @throws IOException if the path or the file are invalid
     * @throws SchemaException if the contained feature type can not be constructed
     */
    public VPFFile(String cPathName) throws IOException, SchemaException {
        pathName = cPathName;
        inputStream = new RandomAccessFile(cPathName, ACCESS_MODE);
        readHeader();

        VPFColumn column = null;
        String geometryName = null;

        Iterator<VPFColumn> iter = columns.iterator();
        while (iter.hasNext()) {
            column = iter.next();

            if (column.isGeometry()) {
                geometryName = column.getName();
                break;
            }
        }

        SimpleFeatureType superType = null;
        // if it's a text geometry feature type add annotation as a super type
        if (pathName.endsWith(TEXT_PRIMITIVE)) {
            superType = AnnotationFeatureType.ANNOTATION;
        }

        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName(cPathName);
        b.setDescription(Text.text(description));
        b.setNamespaceURI("VPF");
        b.setSuperType(superType);
        if (columns != null) {
            for (VPFColumn ad : columns) {
                b.add(ad.getDescriptor());
            }
        }
        b.setDefaultGeometry(geometryName);
        featureType = b.buildFeatureType();

        featureType.getUserData().put(VPFFile.class, this);
    }

    /**
     * Gets the value of full length of ASCII header string including <code>headerLength</code> field.
     *
     * @return the value of headerLength
     */
    private int getAdjustedHeaderLength() {
        return this.headerLength + 4;
    }

    public SimpleFeatureType getFeatureType() {
        return featureType;
    }

    /*
     *  (non-Javadoc)
     * @see org.geotools.feature.FeatureType#getAttributeCount()
     */
    public int getAttributeCount() {
        return featureType.getAttributeCount();
    }

    /**
     * Gets the value of byteOrder variable. Byte order in which table is written:
     *
     * <ul>
     *   <li><b>L</b> - least-significant-first
     *   <li><b>M</b> - most-significant-first
     * </ul>
     *
     * @return the value of byteOrder
     */
    public char getByteOrder() {
        return byteOrder;
    }

    /**
     * Gets the value of the description of table content. This is nice to have, but I don't know how to make use of it.
     *
     * @return the value of description
     */
    //    public String getDescription() {
    //        return description;
    //    }

    /**
     * Returns the directory name for this file by chopping off the file name and the separator.
     *
     * @return the directory name for this file
     */
    public String getDirectoryName() {
        String result = "";
        int index = pathName.lastIndexOf(File.separator);

        if (index >= 0) {
            result = pathName.substring(0, index);
        }

        return result;
    }

    /**
     * Returns the file name (without path) for the file
     *
     * @return the file name for this file
     */
    public String getFileName() {
        String result = pathName.substring(pathName.lastIndexOf(File.separator) + 1);

        return result;
    }

    /**
     * Gets the value of narrativeTable variable file name.
     *
     * @return the value of narrativeTable
     */
    public String getNarrativeTable() {
        return narrativeTable;
    }

    /**
     * Gets the full path name for this file
     *
     * @return the path name for this file
     */
    public String getPathName() {
        return pathName;
    }

    /**
     * Method <code><code>getRecordSize</code></code> is used to return size in bytes of records stored in this table.
     * If table keeps variable length records <code>-1</code> should be returned.
     *
     * @return an <code><code>int</code></code> value
     */
    protected synchronized int getRecordSize() {
        int size = 0;

        Iterator<VPFColumn> iter = columns.iterator();

        while (iter.hasNext()) {
            VPFColumn column = iter.next();
            int length = FeatureTypes.getFieldLength(column.getDescriptor());
            if (length > -1) {
                size += length;
            }
        }

        return size;
    }

    /**
     * Returns a row with a matching value for the provided column
     *
     * @param idName The name of the column to look for, such as "id"
     * @param id An identifier for the requested row
     * @return The first row which matches the ID
     * @throws IllegalAttributeException The feature can not be created due to illegal attributes in the source file
     */
    public SimpleFeature getRowFromId(String idName, int id) {
        SimpleFeature result = null;

        Integer theId = id;

        try {
            this.reset();

            while (this.hasNext()) {
                SimpleFeature row = this.readFeature();

                if (row == null) break;

                Object rowId = row.getAttribute(idName);

                if (Objects.equals(theId, rowId)) {
                    result = row;
                    break;
                }
            }
        } catch (Exception e) {
            result = null;
        }
        return result;
    }

    public SimpleFeature getRowFromId0(String idName, int id) {
        SimpleFeature result = null;

        try {
            // This speeds things up mightily
            String firstColumnName = columns.get(0).getName();

            if (idName.equals(firstColumnName)) {
                setPosition(id);
                result = readFeature();

                Number value = (Number) result.getAttribute(idName);

                // Check to make sure we got a primary key match
                if (value == null || value.intValue() != id) {
                    result = null;
                }
            }

            if (result == null) {
                AbstractList<SimpleFeature> allRows = readAllRows();
                Iterator<SimpleFeature> joinedIter = allRows.iterator();
                result = getRowFromIterator(joinedIter, idName, id);
            }
        } catch (IOException exc) {
            LOGGER.log(Level.SEVERE, "", exc);
        }

        return result;
    }

    /**
     * Returns a single matching row from the Iterator, ignoring rows that do not match a particular id
     *
     * @param iter the iterator to examine
     * @param idName The name of the column to check
     * @param id The value of the column to check
     * @return a TableRow that matches the other parameters
     */
    private synchronized SimpleFeature getRowFromIterator(Iterator<SimpleFeature> iter, String idName, int id) {
        SimpleFeature result = null;
        SimpleFeature currentFeature;
        int value = -1;

        while (iter.hasNext()) {
            currentFeature = iter.next();
            try {
                value = Integer.parseInt(currentFeature.getAttribute(idName).toString());

                if (id == value) {
                    result = currentFeature;

                    break;
                }
            } catch (NumberFormatException exc) {
                // If this happens, the data is invalid so dumping a stack trace seems reasonable
                LOGGER.log(Level.SEVERE, "", exc);
            }
        }

        return result;
    }

    /*
     *  (non-Javadoc)
     * @see org.geotools.feature.FeatureType#getTypeName()
     */
    public String getTypeName() {
        return featureType.getTypeName();
    }

    /**
     * Determines if the stream contains storage for another object. Who knows how well this will work on variable
     * length objects?
     *
     * @return a <code>boolean</code>
     */
    public synchronized boolean hasNext() {
        boolean result = false;

        try {
            int recordSize = getRecordSize();

            if (recordSize > 0) {
                result = inputStream.length() >= inputStream.getFilePointer() + recordSize;
            } else {
                result = inputStream.length() >= inputStream.getFilePointer() + 1;
            }
        } catch (IOException exc) {
            // No idea what to do if this happens
            LOGGER.log(Level.SEVERE, "", exc);
        }

        return result;
    }

    /*
     *  (non-Javadoc)
     * @see org.geotools.feature.FeatureType#isAbstract()
     */
    public synchronized boolean isAbstract() {
        return featureType.isAbstract();
    }

    /**
     * Generates a list containing all of the features in the file
     *
     * @return a <code>List</code> value containing Feature objects
     * @exception IOException if an error occurs
     */
    public synchronized AbstractList<SimpleFeature> readAllRows() throws IOException {
        AbstractList<SimpleFeature> list = new LinkedList<>();

        try {
            setPosition(1);
        } catch (IOException exc) {
            // This indicates that there are no rows
            return list;
        }

        try {
            SimpleFeature row = readFeature();

            while (row != null) {
                list.add(row);
                if (hasNext()) {
                    row = readFeature();
                } else {
                    row = null;
                }
            }
        } catch (IllegalAttributeException exc1) {
            throw new IOException(exc1.getMessage());
        }

        return list;
    }

    /**
     * Reads a single byte as a character value
     *
     * @return a <code>char</code> value
     * @exception IOException if an error occurs
     */
    protected synchronized char readChar() throws IOException {
        return (char) inputStream.read();
    }

    /**
     * Reads a column definition from the file header
     *
     * @return a <code>VPFColumn</code> value
     * @exception VPFHeaderFormatException if an error occurs
     * @exception IOException if an error occurs
     * @exception NumberFormatException if an error occurs
     */
    private synchronized VPFColumn readColumn() throws VPFHeaderFormatException, IOException, NumberFormatException {
        char ctrl = readChar();

        if (ctrl == VPF_RECORD_SEPARATOR) {
            return null;
        }

        String name = ctrl + readString("=");
        char type = readChar();
        ctrl = readChar();

        if (ctrl != VPF_ELEMENT_SEPARATOR) {
            throw new VPFHeaderFormatException("Header format does not fit VPF file definition.");
        }

        String elemStr = readString(String.valueOf(VPF_ELEMENT_SEPARATOR)).trim();

        if (elemStr.equals("*")) {
            elemStr = "-1";
        }

        int elements = Integer.parseInt(elemStr);
        char key = readChar();
        ctrl = readChar();

        if (ctrl != VPF_ELEMENT_SEPARATOR) {
            throw new VPFHeaderFormatException("Header format does not fit VPF file definition.");
        }

        String colDesc = readString(String.valueOf(VPF_ELEMENT_SEPARATOR) + VPF_FIELD_SEPARATOR);
        String descTableName = readString(String.valueOf(VPF_ELEMENT_SEPARATOR) + VPF_FIELD_SEPARATOR);
        String indexFile = readString(String.valueOf(VPF_ELEMENT_SEPARATOR) + VPF_FIELD_SEPARATOR);
        String narrTable = readString(String.valueOf(VPF_ELEMENT_SEPARATOR) + VPF_FIELD_SEPARATOR);

        return new VPFColumn(name, type, elements, key, colDesc, descTableName, indexFile, narrTable);
    }
    /**
     * Constructs an object which is an instance of Geometry by reading values from the file.
     *
     * @param instancesCount number of coordinates to read
     * @param dimensionality either 2 or 3
     * @param readDoubles true: read a double value; false: read a float value
     * @return the constructed object
     * @throws IOException on any file IO errors
     */
    protected synchronized Object readGeometry(int instancesCount, int dimensionality, boolean readDoubles)
            throws IOException {
        Object result = null;
        Coordinate coordinate = null;
        CoordinateList coordinates = new CoordinateList();
        GeometryFactory factory = new GeometryFactory();

        for (int inx = 0; inx < instancesCount; inx++) {
            switch (dimensionality) {
                case 2:
                    coordinate = new Coordinate(
                            readDoubles ? readDouble() : readFloat(), readDoubles ? readDouble() : readFloat());

                    break;

                case 3:
                    coordinate = new Coordinate(
                            readDoubles ? readDouble() : readFloat(),
                            readDoubles ? readDouble() : readFloat(),
                            readDoubles ? readDouble() : readFloat());

                    break;

                default:
                    // WTF???
            }

            coordinates.add(coordinate);
        }

        // Special handling for text primitives per the VPF spec.
        // The first 2 points are the endpoints of the line, the following
        // points fill in between the first 2 points.
        if (pathName.endsWith(TEXT_PRIMITIVE) && coordinates.size() > 2) {
            Object o = coordinates.remove(1);
            coordinates.add((Coordinate) o);
        }

        if (instancesCount == 1) {
            result = factory.createPoint(coordinate);
        } else {
            result = factory.createLineString(new CoordinateArraySequence(coordinates.toCoordinateArray()));
        }

        return result;
    }

    /**
     * Retrieves a double from the file
     *
     * @return a <code>double</code> value
     * @exception IOException if an error occurs
     */
    protected synchronized double readDouble() throws IOException {
        return DataUtils.decodeDouble(readNumber(DATA_LONG_FLOAT_LEN));
    }

    /**
     * Retrieves a feature from the file
     *
     * @return the retieved feature
     * @throws IOException on any file IO errors
     * @throws IllegalAttributeException if any of the attributes retrieved are illegal
     */
    public synchronized SimpleFeature readFeature() throws IOException, IllegalAttributeException {
        SimpleFeature result = null;
        // Iterator<VPFColumn> iter = columns.iterator();
        VPFColumn column;
        boolean textPrimitive = pathName.endsWith(TEXT_PRIMITIVE);
        int size = columns.size();
        if (textPrimitive) size++;
        Object[] values = new Object[size];

        boolean debug = VPFLogger.isLoggable(Level.FINEST);

        try {
            for (int inx = 0; inx < columns.size(); inx++) {
                column = columns.get(inx);
                AttributeDescriptor descriptor = column.getDescriptor();

                if (descriptor.getType().getRestrictions().isEmpty()
                        || descriptor.getType().getRestrictions().contains(org.geotools.api.filter.Filter.INCLUDE)) {
                    values[inx] = readVariableSizeData(column.getTypeChar());
                } else {
                    values[inx] = readFixedSizeData(column.getTypeChar(), column.getElementsNumber());
                }
            }
            if (textPrimitive) {
                values[size - 1] = "nam";
            }

            result = SimpleFeatureBuilder.build(featureType, values, null);
        } catch (EOFException exp) {
            // Should we be throwing an exception instead of eating it?
            LOGGER.log(Level.SEVERE, "", exp);

            debug = true;

            result = null;
        }

        if (debug) {
            Integer recordSize = getRecordSize();
            Long streamLength = inputStream.length();
            Long filePointer = inputStream.getFilePointer();

            VPFLogger.log("************** EOFException");
            VPFLogger.log("recordSize: " + recordSize);
            VPFLogger.log("streamLength: " + streamLength);
            VPFLogger.log("filePointer: " + filePointer);
        }

        return result;
    }

    /**
     * Retrieves a fixed amount of data from the file
     *
     * @param dataType a <code>char</code> value indicating the data type
     * @param instancesCount an <code>int</code> value indicating the number of instances to retrieve.
     * @return an <code>Object</code> value
     * @exception IOException if an error occurs
     */
    protected synchronized Object readFixedSizeData(char dataType, int instancesCount) throws IOException {
        Object result = null;

        switch (dataType) {
            case DATA_TEXT:
            case DATA_LEVEL1_TEXT:
            case DATA_LEVEL2_TEXT:
            case DATA_LEVEL3_TEXT:
                byte[] dataBytes = new byte[instancesCount * DataUtils.getDataTypeSize(dataType)];
                inputStream.readFully(dataBytes);
                result = DataUtils.decodeData(dataBytes, dataType);

                break;

            case DATA_SHORT_FLOAT:
                result = Float.valueOf(readFloat());

                break;

            case DATA_LONG_FLOAT:
                result = Double.valueOf(readDouble());

                break;

            case DATA_SHORT_INTEGER:
                result = Short.valueOf(readShort());

                break;

            case DATA_LONG_INTEGER:
                result = Integer.valueOf(readInteger());

                break;

            case DATA_NULL_FIELD:
                result = "NULL";

                break;

            case DATA_TRIPLET_ID:
                result = readTripletId();

                break;

            case DATA_2_COORD_F:
                result = readGeometry(instancesCount, 2, false);

                break;

            case DATA_2_COORD_R:
                result = readGeometry(instancesCount, 2, true);

                break;

            case DATA_3_COORD_F:
                result = readGeometry(instancesCount, 3, false);

                break;

            case DATA_3_COORD_R:
                result = readGeometry(instancesCount, 3, true);

                break;

            default:
                break;
        } // end of switch (dataType)

        return result;
    }

    /**
     * Retrieves a floating point number from the file.
     *
     * @return a <code>float</code> value
     * @exception IOException if an error occurs
     */
    protected synchronized float readFloat() throws IOException {
        return DataUtils.decodeFloat(readNumber(DATA_SHORT_FLOAT_LEN));
    }

    /**
     * Retrieves a number of attributes from the file header
     *
     * @exception VPFHeaderFormatException if an error occurs
     * @exception IOException if an error occurs
     */
    protected synchronized void readHeader() throws VPFHeaderFormatException, IOException {
        byte[] fourBytes = new byte[4];
        inputStream.readFully(fourBytes);

        byteOrder = readChar();

        char ctrl = byteOrder;

        if (byteOrder == VPF_RECORD_SEPARATOR) {
            byteOrder = LITTLE_ENDIAN_ORDER;
        } else {
            ctrl = readChar();
        }

        if (byteOrder == LITTLE_ENDIAN_ORDER) {
            fourBytes = DataUtils.toBigEndian(fourBytes);
        }

        headerLength = DataUtils.decodeInt(fourBytes);

        if (ctrl != VPF_RECORD_SEPARATOR) {
            throw new VPFHeaderFormatException("Header format does not fit VPF file definition.");
        }

        description = readString(String.valueOf(VPF_RECORD_SEPARATOR));
        narrativeTable = readString(String.valueOf(VPF_RECORD_SEPARATOR));

        VPFColumn column = readColumn();

        while (column != null) {
            columns.add(column);
            ctrl = readChar();

            if (ctrl != VPF_FIELD_SEPARATOR && ctrl != VPF_RECORD_SEPARATOR) {
                throw new VPFHeaderFormatException("Header format does not fit VPF file definition.");
            }

            if (ctrl == VPF_RECORD_SEPARATOR) column = null;
            else column = readColumn();
        }

        if (getRecordSize() < 0) {
            variableIndex = new VariableIndexInputStream(getVariableIndexFileName(), getByteOrder());
        }
    }

    /**
     * Retrieves an integer value from the file
     *
     * @return an <code>int</code> value
     * @exception IOException if an error occurs
     */
    protected synchronized int readInteger() throws IOException {
        return DataUtils.decodeInt(readNumber(DATA_LONG_INTEGER_LEN));
    }

    /**
     * Reads some byte data from the file
     *
     * @param cnt an <code>int</code> value indicating the number of bytes to retrieve
     * @return a <code>byte[]</code> value
     * @throws IOException if an error occurs
     */
    protected synchronized byte[] readNumber(int cnt) throws IOException {
        byte[] dataBytes = new byte[cnt];

        boolean exception = false;
        boolean debug = false;

        EOFException e2 = null;

        try {
            inputStream.readFully(dataBytes);

            if (byteOrder == LITTLE_ENDIAN_ORDER) {
                dataBytes = DataUtils.toBigEndian(dataBytes);
            }
        } catch (EOFException e) {
            exception = true;
            debug = true;
            e2 = e;
        }

        if (debug) {
            Integer recordSize = getRecordSize();
            Long streamLength = inputStream.length();
            Long filePointer = inputStream.getFilePointer();
            Integer icnt = cnt;

            VPFLogger.log("************** EOFException");
            VPFLogger.log("cnt: " + icnt);
            VPFLogger.log("recordSize: " + recordSize);
            VPFLogger.log("streamLength: " + streamLength);
            VPFLogger.log("filePointer: " + filePointer);
        }

        if (exception) {
            throw e2;
        }

        return dataBytes;
    }

    /**
     * Retrieves a short value from the file
     *
     * @return a <code>short</code> value
     * @exception IOException if an error occurs
     */
    protected synchronized short readShort() throws IOException {
        return DataUtils.decodeShort(readNumber(DATA_SHORT_INTEGER_LEN));
    }

    /**
     * Reads a string value from the file
     *
     * @param terminators a <code>String</code> value indicating the terminators to look for
     * @return a <code>String</code> value
     * @exception IOException if an error occurs
     */
    protected synchronized String readString(String terminators) throws IOException {
        StringBuffer text = new StringBuffer();
        char ctrl = readChar();

        if (terminators.indexOf(ctrl) != -1) {
            if (ctrl == VPF_FIELD_SEPARATOR) {
                unread(1);
            }

            return null;
        }

        while (terminators.indexOf(ctrl) == -1) {
            text.append(ctrl);
            ctrl = readChar();
        }

        if (text.toString().equals(STRING_NULL_VALUE)) {
            return null;
        } else {
            return text.toString();
        }
    }

    /**
     * Retrieves a triplet object from the file
     *
     * @return a <code>TripletId</code> value
     * @throws IOException on any IO errors
     */
    protected synchronized TripletId readTripletId() throws IOException {
        // TODO: does this take into account byte order properly?
        byte tripletDef = (byte) inputStream.read();
        int dataSize = TripletId.calculateDataSize(tripletDef);
        byte[] tripletData = new byte[dataSize + 1];
        tripletData[0] = tripletDef;

        if (dataSize > 0) {
            inputStream.readFully(tripletData, 1, dataSize);
        }

        return new TripletId(tripletData);
    }

    /**
     * Retrieves variable sized data from the file by first reading an integer which indicates how many instances of the
     * data type to retrieve
     *
     * @param dataType a <code>char</code> value indicating the data type
     * @return an <code>Object</code> value
     * @exception IOException if an error occurs
     */
    protected synchronized Object readVariableSizeData(char dataType) throws IOException {
        int instances = readInteger();

        try {
            return readFixedSizeData(dataType, instances);
        } catch (EOFException e) {
            VPFLogger.log("************ readVariableSizeData error");
            VPFLogger.log("instance count: " + instances);
            throw e;
        }
    }

    /** Resets the file stream by setting its pointer to the first position after the header. */
    public synchronized void reset() {
        try {
            setPosition(1);
        } catch (IOException exc) {
            // This just means there is nothing in the table
        }
    }
    /**
     * Close the input stream pointed to by the object
     *
     * @throws IOException in some unlikely situation
     */
    public synchronized void close() throws IOException {
        inputStream.close();
        if (variableIndex != null) {
            variableIndex.close();
        }
    }
    /**
     * Sets the position in the stream
     *
     * @param pos A 1-indexed position
     * @throws IOException on any IO failures
     */
    protected synchronized void setPosition(long pos) throws IOException {
        if (getRecordSize() < 0) {
            VariableIndexRow varRow = (VariableIndexRow) variableIndex.readRow((int) pos);
            inputStream.seek(varRow.getOffset());
        } else {
            inputStream.seek(getAdjustedHeaderLength() + (pos - 1) * getRecordSize());
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return featureType.toString();
    }

    /**
     * Back up a specified number of bytes in the file stream
     *
     * @param bytes a <code>long</code> value
     * @exception IOException if an error occurs
     */
    protected synchronized void unread(long bytes) throws IOException {
        inputStream.seek(inputStream.getFilePointer() - bytes);
    }

    /**
     * Returns the full path of the variable index associated with the current file
     *
     * @return a <code>String</code> value
     */
    private synchronized String getVariableIndexFileName() {
        String result = null;
        String fileName = getFileName();

        if (fileName.equalsIgnoreCase(FEATURE_CLASS_SCHEMA_TABLE)) {
            result = getDirectoryName().concat(File.separator).concat("FCZ");
        } else {
            result = getDirectoryName()
                    .concat(File.separator)
                    .concat(fileName.substring(0, fileName.length() - 1) + "X");
        }

        return result;
    }

    public VPFColumn getColumn(int index) {
        return columns.get(index);
    }

    public int getColumnCount() {
        return columns.size();
    }

    public VPFColumn getColumn(String name) {
        if (name == null) return null;

        for (VPFColumn col : columns) {
            if (name.equals(col.getName())) {
                return col;
            }
        }
        return null; // not found
    }

    public AttributeDescriptor getDescriptor(String name) {
        VPFColumn col = getColumn(name);
        return col == null ? null : col.getDescriptor();
    }
}
