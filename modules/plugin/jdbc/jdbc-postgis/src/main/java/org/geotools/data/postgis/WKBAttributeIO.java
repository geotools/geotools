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

package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.geotools.data.DataSourceException;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ByteArrayInStream;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;



/**
 * An attribute IO implementation that can manage the WKB
 *
 * @author Andrea Aime
 *
 * @source $URL$
 * @since 2.4.1
 */
public class WKBAttributeIO {
    WKBReader wkbr;
    ByteArrayInStream inStream = new ByteArrayInStream(new byte[0]);
    GeometryFactory gf;

    public WKBAttributeIO() {
        this(new GeometryFactory());
    }
    
    public WKBAttributeIO(GeometryFactory gf) {
        wkbr = new WKBReader(gf);
    }
    
    public void setGeometryFactory(GeometryFactory gf) {
        wkbr = new WKBReader(gf);
    }

    /**
     * This method will convert a Well Known Binary representation to a
     * JTS  Geometry object.
     *
     * @param wkb te wkb encoded byte array
     *
     * @return a JTS Geometry object that is equivalent to the WTB
     *         representation passed in by param wkb
     *
     * @throws IOException if more than one geometry object was found in  the
     *         WTB representation, or if the parser could not parse the WKB
     *         representation.
     */
    private Geometry wkb2Geometry(byte[] wkbBytes)
        throws IOException {
        if (wkbBytes == null)  //DJB: null value from database --> null geometry (the same behavior as WKT).  NOTE: sending back a GEOMETRYCOLLECTION(EMPTY) is also a possibility, but this is not the same as NULL
            return null;
        try {
            inStream.setBytes(wkbBytes);
            return wkbr.read(inStream);
        } catch (Exception e) {
            throw new DataSourceException("An exception occurred while parsing WKB data", e);
        }
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    public Object read(ResultSet rs, String columnName) throws IOException {
        try {
            byte bytes[] = rs.getBytes(columnName);
            if (bytes == null) // ie. its a null column -> return a null geometry!
                return null;
            return wkb2Geometry(Base64.decode(bytes));
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }
    
    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    public Object read(ResultSet rs, int columnIndex) throws IOException {
        try {
            byte bytes[] = rs.getBytes(columnIndex);
            if (bytes == null) // ie. its a null column -> return a null geometry!
                return null;
            return wkb2Geometry(Base64.decode(bytes));
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.PreparedStatement, int, java.lang.Object)
     */
    public void write(PreparedStatement ps, int position, Object value) throws IOException {
        try {
            if(value == null) {
                ps.setNull(position, Types.OTHER);
            } else {
                ps.setBytes( position, new WKBWriter().write( (Geometry)value ));
            }
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }

    }
    
    /**
     * Turns a char that encodes four bits in hexadecimal notation into a byte
     *
     * @param c
     *
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
    
    
    
}

