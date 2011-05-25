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

package org.geotools.data.teradata;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.geotools.data.DataSourceException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.InStream;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;

/**
 * An attribute IO implementation that can manage the WKB
 * 
 * @author Andrea Aime
 * @source $URL$
 * @since 2.4.1
 */
public class WKBAttributeIO {
    WKBReader wkbr;
    ByteArrayInStream byteArrayInStream = new ByteArrayInStream();
    InputStreamInStream inputStreamInStream = new InputStreamInStream();

    public WKBAttributeIO() {
        wkbr = new WKBReader();
    }

    public WKBAttributeIO(GeometryFactory gf) {
        wkbr = new WKBReader(gf);
    }

    /**
     * This method will convert a Well Known Binary representation to a JTS
     * Geometry object.
     * 
     * @return a JTS Geometry object that is equivalent to the WTB
     *         representation passed in by param wkb
     * @throws IOException
     *             if more than one geometry object was found in the WTB
     *             representation, or if the parser could not parse the WKB
     *             representation.
     */
    public Geometry wkb2Geometry(byte[] wkbBytes) throws IOException {
        if (wkbBytes == null) // DJB: null value from database --> null geometry
                              // (the same behavior as WKT). NOTE: sending back
                              // a GEOMETRYCOLLECTION(EMPTY) is also a
                              // possibility, but this is not the same as NULL
            return null;
        try {
            byteArrayInStream.setBytes(wkbBytes);
            return wkbr.read(byteArrayInStream);
        } catch (Exception e) {
            throw new DataSourceException("An exception occurred while parsing WKB data", e);
        }
    }

    public Geometry wkb2Geometry(InputStream inputStream) throws IOException {
        if (inputStream == null) // DJB: null value from database --> null
                                 // geometry (the same behavior as WKT). NOTE:
                                 // sending back a GEOMETRYCOLLECTION(EMPTY) is
                                 // also a possibility, but this is not the same
                                 // as NULL
            return null;
        try {
            inputStreamInStream.setIn(inputStream);
            return wkbr.read(inputStreamInStream);
        } catch (Exception e) {
            throw new DataSourceException("An exception occurred while parsing WKB data", e);
        }
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    public Geometry read(ResultSet rs, String columnName) throws IOException {
        try {
            return read(rs, rs.findColumn(columnName));
        } catch (SQLException e) {
            throw new IllegalArgumentException("columnName " + e + " is not a column in result set");
        }
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    public Geometry read(ResultSet rs, int columnIndex) throws IOException {
        try {
            switch (rs.getMetaData().getColumnType(columnIndex)) {
            case Types.BLOB:
                return readFromBlob(rs, columnIndex);
            default:
                return readFromBytes(rs, columnIndex);
            }
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    private Geometry readFromBytes(ResultSet rs, int columnIndex) throws IOException {
        try {
            return wkb2Geometry(rs.getBytes(columnIndex));
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }

    private Geometry doReadFromBlob(Blob blob) throws IOException {
        if (blob == null) {
            return null;
        }
        InputStream is = null;
        try {
            is = blob.getBinaryStream();
            if (is == null || is.available() == 0) // ie. its a 0 length column
                                                   // -> return a null geometry!
                return null;
            return wkb2Geometry(is);
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    private Geometry readFromBlob(ResultSet rs, int columnIndex) throws IOException {
        try {
            return doReadFromBlob(rs.getBlob(columnIndex));
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.PreparedStatement,
     *      int, java.lang.Object)
     */
    public void write(PreparedStatement ps, int position, Object value) throws IOException {
        try {
            if (value == null) {
                ps.setNull(position, Types.OTHER);
            } else {
                ps.setBytes(position, new WKBWriter().write((Geometry) value));
            }
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }

    }

    /**
     * Turns a char that encodes four bits in hexadecimal notation into a byte
     * 
     * @param c
     */
    public static byte getFromChar(char c) {
        if (c <= '9') {
            return (byte) (c - '0');
        } else if (c <= 'F') {
            return (byte) (c - 'A' + 10);
        } else {
            return (byte) (c - 'a' + 10);
        }
    }

    /**
     * Accelerates data loading compared to the plain InStream shipped along
     * with JTS
     * 
     * @author Andrea Aime - TOPP
     */
    private static class ByteArrayInStream implements InStream {

        byte[] buffer;
        int position;

        public void setBytes(final byte[] buffer) {
            this.buffer = buffer;
            this.position = 0;
        }

        public void read(final byte[] buf) throws IOException {
            final int size = buf.length;
            System.arraycopy(buffer, position, buf, 0, size);
            position += size;
        }

    }

    private static class InputStreamInStream implements InStream {
        private InputStream in;

        public void setIn(InputStream in) {
            this.in = in;
        }

        public void read(final byte[] buf) throws IOException {
            in.read(buf);
        }

    }
}
