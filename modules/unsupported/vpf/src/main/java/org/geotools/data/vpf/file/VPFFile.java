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

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.URI;
import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import org.geotools.data.vpf.VPFColumn;
import org.geotools.data.vpf.exc.VPFHeaderFormatException;
import org.geotools.data.vpf.ifc.DataTypesDefinition;
import org.geotools.data.vpf.ifc.FileConstants;
import org.geotools.data.vpf.io.TripletId;
import org.geotools.data.vpf.io.VPFInputStream;
import org.geotools.data.vpf.io.VariableIndexInputStream;
import org.geotools.data.vpf.io.VariableIndexRow;
import org.geotools.data.vpf.util.DataUtils;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.type.AnnotationFeatureType;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.LengthFunction;
import org.geotools.filter.LiteralExpression;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.util.InternationalString;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateList;
import com.vividsolutions.jts.geom.DefaultCoordinateSequenceFactory;
import com.vividsolutions.jts.geom.GeometryFactory;


/**
 * This class encapsulates VPF files. By implementing the <code>FeatureType</code> interface,
 * it serves as a factory for VPFColumns. Instances of this class should
 * be created by VPFFileFactory.
 *
 * @author <a href="mailto:jeff@ionicenterprise.com">Jeff Yutzler</a>
 *
 * @source $URL$
 */
public class VPFFile implements SimpleFeatureType, FileConstants, DataTypesDefinition {
    //    private final TableInputStream stream;
    private static String ACCESS_MODE = "r";

    /**
     * Variable <code>byteOrder</code> keeps value of  byte order in which
     * table is written:
     * 
     * <ul>
     * <li>
     * <b>L</b> - least-significant-first
     * </li>
     * <li>
     * <b>M</b> - most-significant-first
     * </li>
     * </ul>
     */
    private char byteOrder = LEAST_SIGNIF_FIRST;
    /**
     * The columns of the file. This list shall contain objects of type <code>VPFColumn</code>
     */
    private final List columns = new Vector();

    /**
     * Variable <code>description</code> keeps value of text description of the
     * table's contents.
     */
    private String description = null;
    /**
     * The contained Feature Type
     */
    private final SimpleFeatureType featureType;

    /**
     * Keeps value of length of ASCII header
     * string (i.e., the remaining information after this field)
     */
    private int headerLength = -0;

    /** The associated stream */
    private RandomAccessFile inputStream = null;

    /**
     * Variable <code>narrativeTable</code> keeps value of  an optional
     * narrative file which contains miscellaneous information about the
     * table.
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
     *
     * @throws IOException if the path or the file are invalid
     * @throws SchemaException if the contained feature type can not be
     *         constructed
     */
    public VPFFile(String cPathName) throws IOException, SchemaException {
        pathName = cPathName;
        inputStream = new RandomAccessFile(cPathName, ACCESS_MODE);
        readHeader();

        GeometryDescriptor gat = null;
        VPFColumn geometryColumn = null;

        Iterator iter = columns.iterator();
        while (iter.hasNext()) {
            geometryColumn = (VPFColumn) iter.next();

            if (geometryColumn.isGeometry()) {
                gat = geometryColumn.getGeometryAttributeType();

                break;
            }
        }

        SimpleFeatureType superType = null;
        // if it's a text geometry feature type add annotation as a super type
        if (pathName.endsWith(TEXT_PRIMITIVE)) {
            superType =  AnnotationFeatureType.ANNOTATION;
        }
         
        SimpleFeatureTypeBuilder b = new SimpleFeatureTypeBuilder();
        b.setName( cPathName );
        b.setNamespaceURI("VPF");
        b.setSuperType(superType);
        b.addAll( columns );
        b.setDefaultGeometry(gat.getLocalName());
        featureType = b.buildFeatureType();
    }

    /**
     * Gets the value of full length of ASCII header string including
     * <code>headerLength</code> field.
     *
     * @return the value of headerLength
     */
    private int getAdjustedHeaderLength() {
        return this.headerLength + 4;
    }

    /*
     *  (non-Javadoc)
     * @see org.geotools.feature.FeatureType#getAttributeCount()
     */
    public int getAttributeCount() {
        return featureType.getAttributeCount();
    }

    /**
     * Gets the value of byteOrder variable. Byte order in which table is
     * written:
     * 
     * <ul>
     * <li>
     * <b>L</b> - least-significant-first
     * </li>
     * <li>
     * <b>M</b> - most-significant-first
     * </li>
     * </ul>
     * 
     *
     * @return the value of byteOrder
     */
    public char getByteOrder() {
        return byteOrder;
    }

    /**
     * Gets the value of the description of table content. This is nice to
     * have, but I don't know how to make use of it.
     *
     * @return the value of description
     */
//    public String getDescription() {
//        return description;
//    }

    /**
     * Returns the directory name for this file by chopping off the file name
     * and the separator.
     *
     * @return the directory name for this file
     */
    public String getDirectoryName() {
        String result = new String();
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
        String result = pathName.substring(pathName.lastIndexOf(File.separator)
                + 1);

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
     * Method <code><code>getRecordSize</code></code> is used to return size in
     * bytes of records stored in this table. If table keeps variable length
     * records <code>-1</code> should be returned.
     *
     * @return an <code><code>int</code></code> value
     */
    protected int getRecordSize() {
        int size = 0;

        Iterator iter = columns.iterator();

        
        while (iter.hasNext()) {
            VPFColumn column = (VPFColumn) iter.next();
            int length = FeatureTypes.getFieldLength(column);
            if ( length > -1 ) {
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
     *
     * @return The first row which matches the ID
     *
     * @throws IllegalAttributeException The feature can not be created due to
     *         illegal attributes in the source file
     */
    public SimpleFeature getRowFromId(String idName, int id)
        throws IllegalAttributeException {
        SimpleFeature result = null;

        try {
            // This speeds things up mightily
            String firstColumnName = ((VPFColumn) columns.get(0)).getLocalName();

            if (idName.equals(firstColumnName)) {
                setPosition(id);
                result = readFeature();

                Number value = (Number) result.getAttribute(idName);

                // Check to make sure we got a primary key match
                if ((value == null) || (value.intValue() != id)) {
                    result = null;
                }
            }

            if (result == null) {
                Iterator joinedIter = readAllRows().iterator();
                result = getRowFromIterator(joinedIter, idName, id);
            }
        } catch (IOException exc) {
            exc.printStackTrace();
        }

        return result;
    }

    /**
     * Returns a single matching row from the Iterator,  ignoring rows that do
     * not match a particular id
     *
     * @param iter the iterator to examine
     * @param idName The name of the column to check
     * @param id The value of the column to check
     *
     * @return a TableRow that matches the other parameters
     */
    private SimpleFeature getRowFromIterator(Iterator iter, String idName, int id) {
        SimpleFeature result = null;
        SimpleFeature currentFeature;
        int value = -1;

        while (iter.hasNext()) {
            currentFeature = (SimpleFeature) iter.next();
            try {
                value = Integer.parseInt(currentFeature.getAttribute(idName).toString());

                if (id == value) {
                    result = currentFeature;

                    break;
                }
            } catch (NumberFormatException exc) {
                // If this happens, the data is invalid so dumping a stack trace seems reasonable
                exc.printStackTrace();
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
     * Determines if the stream contains storage for another object. Who knows
     * how well this will work on variable length objects?
     *
     * @return a <code>boolean</code> 
     */
    public boolean hasNext() {
        boolean result = false;

        try {
            int recordSize = getRecordSize();

            if (recordSize > 0) {
                result = inputStream.length() >= (inputStream.getFilePointer() + recordSize);
            } else {
                result = inputStream.length() >= (inputStream.getFilePointer() + 1);
            }
        } catch (IOException exc) {
            // No idea what to do if this happens
            exc.printStackTrace();
        }

        return result;
    }

    /*
     *  (non-Javadoc)
     * @see org.geotools.feature.FeatureType#isAbstract()
     */
    public boolean isAbstract() {
        return featureType.isAbstract();
    }

    /**
     * Generates a list containing all of the features in the file
     *
     * @return a <code>List</code> value containing Feature objects
     *
     * @exception IOException if an error occurs
     */
    public AbstractList readAllRows() throws IOException {
        AbstractList list = new LinkedList();

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
                if(hasNext()){
                    row = readFeature();
                }else{
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
     *
     * @exception IOException if an error occurs
     */
    protected char readChar() throws IOException {
        return (char) inputStream.read();
    }

    /**
     * Reads a column definition from the file header
     *
     * @return a <code>VPFColumn</code> value
     *
     * @exception VPFHeaderFormatException if an error occurs
     * @exception IOException if an error occurs
     * @exception NumberFormatException if an error occurs
     */
    private VPFColumn readColumn()
        throws VPFHeaderFormatException, IOException, NumberFormatException {
        char ctrl = readChar();

        if (ctrl == VPF_RECORD_SEPARATOR) {
            return null;
        }

        String name = ctrl + readString("=");
        char type = readChar();
        ctrl = readChar();

        if (ctrl != VPF_ELEMENT_SEPARATOR) {
            throw new VPFHeaderFormatException(
                "Header format does not fit VPF file definition.");
        }

        String elemStr = readString(new String() + VPF_ELEMENT_SEPARATOR).trim();

        if (elemStr.equals("*")) {
            elemStr = "-1";
        }

        int elements = Integer.parseInt(elemStr);
        char key = readChar();
        ctrl = readChar();

        if (ctrl != VPF_ELEMENT_SEPARATOR) {
            throw new VPFHeaderFormatException(
                "Header format does not fit VPF file definition.");
        }

        String colDesc = readString(new String() + VPF_ELEMENT_SEPARATOR
                + VPF_FIELD_SEPARATOR);
        String descTableName = readString(new String() + VPF_ELEMENT_SEPARATOR
                + VPF_FIELD_SEPARATOR);
        String indexFile = readString(new String() + VPF_ELEMENT_SEPARATOR
                + VPF_FIELD_SEPARATOR);
        String narrTable = readString(new String() + VPF_ELEMENT_SEPARATOR
                + VPF_FIELD_SEPARATOR);

        return new VPFColumn(name, type, elements, key, colDesc, descTableName,
            indexFile, narrTable);
    }
    /**
     * Constructs an object which is an instance of Geometry 
     * by reading values from the file.
     * @param instancesCount number of coordinates to read
     * @param dimensionality either 2 or 3
     * @param readDoubles true: read a double value; false: read a float value
     * @return the constructed object
     * @throws IOException on any file IO errors
     */
    protected Object readGeometry(int instancesCount, int dimensionality,
        boolean readDoubles) throws IOException {
        Object result = null;
        Coordinate coordinate = null;
        CoordinateList coordinates = new CoordinateList();
        GeometryFactory factory = new GeometryFactory();

        for (int inx = 0; inx < instancesCount; inx++) {
            switch (dimensionality) {
            case 2:
                coordinate = new Coordinate(readDoubles ? readDouble()
                                                        : readFloat(),
                        readDoubles ? readDouble() : readFloat());

                break;

            case 3:
                coordinate = new Coordinate(readDoubles ? readDouble()
                                                        : readFloat(),
                        readDoubles ? readDouble() : readFloat(),
                        readDoubles ? readDouble() : readFloat());

                break;

            default:
                //WTF???
            }

            coordinates.add(coordinate);
        }

        // Special handling for text primitives per the VPF spec.
        // The first 2 points are the endpoints of the line, the following
        // points fill in between the first 2 points.  
        if (pathName.endsWith(TEXT_PRIMITIVE) && coordinates.size() > 2) {
            Object o = coordinates.remove(1);
            coordinates.add(o);
        }

        if (instancesCount == 1) {
            result = factory.createPoint(coordinate);
        } else {
            result = factory.createLineString(DefaultCoordinateSequenceFactory.instance()
                                                                              .create(coordinates
                        .toCoordinateArray()));
        }

        return result;
    }

    /**
     * Retrieves a double from the file
     *
     * @return a <code>double</code> value
     *
     * @exception IOException if an error occurs
     */
    protected double readDouble() throws IOException {
        return DataUtils.decodeDouble(readNumber(DATA_LONG_FLOAT_LEN));
    }

    /**
     * Retrieves a feature from the file
     *
     * @return the retieved feature
     *
     * @throws IOException on any file IO errors
     * @throws IllegalAttributeException if any of the attributes retrieved are
     *         illegal
     */
    public SimpleFeature readFeature() throws IOException, IllegalAttributeException {
        SimpleFeature result = null;
        Iterator iter = columns.iterator();
        VPFColumn column;
        boolean textPrimitive = pathName.endsWith(TEXT_PRIMITIVE);
        int size = columns.size();
        if (textPrimitive) size++;
        Object[] values = new Object[size];
        
        try {
            for (int inx = 0; inx < columns.size(); inx++) {
                column = (VPFColumn) columns.get(inx);

                if ( column.getType().getRestrictions().isEmpty() || 
                        column.getType().getRestrictions().contains( org.opengis.filter.Filter.INCLUDE )) {
                    values[inx] = readVariableSizeData(column.getTypeChar());
                }
                else {
                    values[inx] = readFixedSizeData(column.getTypeChar(),
                            column.getElementsNumber());
                }
            }
            if (textPrimitive) {
                values[size-1] = "nam";
            }

            result = SimpleFeatureBuilder.build( featureType, values, null);
        } catch (EOFException exp) {
            // Should we be throwing an exception instead of eating it?
            exp.printStackTrace();
        }

        return result;
    }

    /**
     * Retrieves a fixed amount of data from the file
     *
     * @param dataType a <code>char</code> value indicating the data type
     * @param instancesCount an <code>int</code> value indicating the number 
     * of instances to retrieve.
     *
     * @return an <code>Object</code> value
     *
     * @exception IOException if an error occurs
     */
    protected Object readFixedSizeData(char dataType, int instancesCount)
        throws IOException {
        Object result = null;

        switch (dataType) {
        case DATA_TEXT:
        case DATA_LEVEL1_TEXT:
        case DATA_LEVEL2_TEXT:
        case DATA_LEVEL3_TEXT:

            byte[] dataBytes = new byte[instancesCount * DataUtils
                .getDataTypeSize(dataType)];
            inputStream.readFully(dataBytes);
            result = DataUtils.decodeData(dataBytes, dataType);

            break;

        case DATA_SHORT_FLOAT:
            result = new Float(readFloat());

            break;

        case DATA_LONG_FLOAT:
            result = new Double(readDouble());

            break;

        case DATA_SHORT_INTEGER:
            result = new Short(readShort());

            break;

        case DATA_LONG_INTEGER:
            result = new Integer(readInteger());

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
     *
     * @exception IOException if an error occurs
     */
    protected float readFloat() throws IOException {
        return DataUtils.decodeFloat(readNumber(DATA_SHORT_FLOAT_LEN));
    }

    /**
     * Retrieves a number of attributes from the file header
     *
     * @exception VPFHeaderFormatException if an error occurs
     * @exception IOException if an error occurs
     */
    protected void readHeader() throws VPFHeaderFormatException, IOException {
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
            throw new VPFHeaderFormatException(
                "Header format does not fit VPF file definition.");
        }

        description = readString(new String() + VPF_RECORD_SEPARATOR);
        narrativeTable = readString(new String() + VPF_RECORD_SEPARATOR);

        VPFColumn column = readColumn();

        while (column != null) {
            columns.add(column);
            ctrl = readChar();

            if (ctrl != VPF_FIELD_SEPARATOR) {
                throw new VPFHeaderFormatException(
                    "Header format does not fit VPF file definition.");
            }

            column = readColumn();
        }

        if (getRecordSize() < 0) {
            variableIndex = new VariableIndexInputStream(getVariableIndexFileName(),
                    getByteOrder());
        }
    }

    /**
     * Retrieves an integer value from the file
     *
     * @return an <code>int</code> value
     *
     * @exception IOException if an error occurs
     */
    protected int readInteger() throws IOException {
        return DataUtils.decodeInt(readNumber(DATA_LONG_INTEGER_LEN));
    }

    /**
     * Reads some byte data from the file
     *
     * @param cnt an <code>int</code> value indicating the number of bytes to retrieve
     *
     * @return a <code>byte[]</code> value
     *
     * @throws IOException if an error occurs
     */
    protected byte[] readNumber(int cnt) throws IOException {
        byte[] dataBytes = new byte[cnt];
        inputStream.readFully(dataBytes);

        if (byteOrder == LITTLE_ENDIAN_ORDER) {
            dataBytes = DataUtils.toBigEndian(dataBytes);
        }

        return dataBytes;
    }

    /**
     * Retrieves a short value from the file
     *
     * @return a <code>short</code> value
     *
     * @exception IOException if an error occurs
     */
    protected short readShort() throws IOException {
        return DataUtils.decodeShort(readNumber(DATA_SHORT_INTEGER_LEN));
    }

    /**
     * Reads a string value from the file
     *
     * @param terminators a <code>String</code> value indicating the terminators to look for
     *
     * @return a <code>String</code> value
     *
     * @exception IOException if an error occurs
     */
    protected String readString(String terminators) throws IOException {
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
     * 
     * @throws IOException on any IO errors
     */
    protected TripletId readTripletId() throws IOException {
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
     * Retrieves variable sized data from the file by first reading an integer
     * which indicates how many instances of the data type to retrieve
     *
     * @param dataType a <code>char</code> value indicating the data type
     *
     * @return an <code>Object</code> value
     *
     * @exception IOException if an error occurs
     */
    protected Object readVariableSizeData(char dataType)
        throws IOException {
        int instances = readInteger();

        return readFixedSizeData(dataType, instances);
    }

    /**
     * Resets the file stream by setting its pointer 
     * to the first position after the header.
     *
     */
    public void reset() {
        try {
            setPosition(1);
        } catch (IOException exc) {
            // This just means there is nothing in the table
        }
    }
    /**
     * Close the input stream pointed to by the object
     *  @throws IOException in some unlikely situation
     */
    public void close() throws IOException{
        inputStream.close();
        if (variableIndex != null) {
            variableIndex.close();
        }
    }
    /**
     * Sets the position in the stream
     *
     * @param pos A 1-indexed position
     *
     * @throws IOException on any IO failures
     */
    protected void setPosition(long pos) throws IOException {
        if (getRecordSize() < 0) {
            VariableIndexRow varRow = (VariableIndexRow) variableIndex.readRow((int) pos);
            inputStream.seek(varRow.getOffset());
        } else {
            inputStream.seek(getAdjustedHeaderLength()
                + ((pos - 1) * getRecordSize()));
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return featureType.toString();
    }

    /**
     * Back up a specified number of bytes in the file stream
     *
     * @param bytes a <code>long</code> value
     *
     * @exception IOException if an error occurs
     */
    protected void unread(long bytes) throws IOException {
        inputStream.seek(inputStream.getFilePointer() - bytes);
    }

    /**
     * Returns the full path of the variable index associated with the current file
     *
     * @return a <code>String</code> value
     */
    private String getVariableIndexFileName() {
        String result = null;
        String fileName = getFileName();

        if (fileName.toLowerCase().equals(FEATURE_CLASS_SCHEMA_TABLE)) {
            result = getDirectoryName().concat(File.separator).concat("fcz");
        } else {
            result = getDirectoryName().concat(File.separator).concat(fileName
                    .substring(0, fileName.length() - 1) + "x");
        }

        return result;
    }

	public List<AttributeDescriptor> getAttributeDescriptors() {
	    return featureType.getAttributeDescriptors();
	}
	
	public AttributeDescriptor getDescriptor(String name) {
		return featureType.getDescriptor(name);
	}
	
	public AttributeDescriptor getDescriptor(Name name) {
	    return featureType.getDescriptor(name);
	}

	public AttributeDescriptor getDescriptor(int index) {
		return featureType.getDescriptor(index);
	}
	
	public org.opengis.feature.type.AttributeType getType(Name name) {
		return featureType.getType( name );
	}

	public org.opengis.feature.type.AttributeType getType(String name) {
        return featureType.getType( name );
	}

	   
	public org.opengis.feature.type.AttributeType getType(int index) {
		return featureType.getType( index );
	}

	public List getTypes() {
		return featureType.getTypes();
	}

	public CoordinateReferenceSystem getCoordinateReferenceSystem() {
		return featureType.getCoordinateReferenceSystem();
	}

	public GeometryDescriptor getGeometryDescriptor() {
		return featureType.getGeometryDescriptor();
	}

	public Class getBinding() {
		return featureType.getBinding();
	}

	public Collection getDescriptors() {
		return featureType.getDescriptors();
	}

	public boolean isInline() {
		return featureType.isInline();
	}

	public List getRestrictions() {
		return featureType.getRestrictions();
	}

	public org.opengis.feature.type.AttributeType getSuper() {
		return featureType.getSuper();
	}

	public boolean isIdentified() {
		return featureType.isIdentified();
	}

	public InternationalString getDescription() {
		return featureType.getDescription();
	}

	public Name getName() {
		return featureType.getName();
	}

	public int indexOf(String name) {
		return featureType.indexOf(name);
	}

	public int indexOf(Name name) {
	    return featureType.indexOf(name);
	}
	
	public Map<Object, Object> getUserData() {
	    return featureType.getUserData();
	}
	
	
}
