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
package org.geotools.data.postgis.attributeio;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;

import com.vividsolutions.jts.geom.CoordinateSequenceFactory;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.PrecisionModel;
import com.vividsolutions.jts.geom.impl.CoordinateArraySequenceFactory;
import com.vividsolutions.jts.io.InStream;
import com.vividsolutions.jts.io.WKBReader;
import com.vividsolutions.jts.io.WKBWriter;



/**
 * An attribute IO implementation that can manage the WKB
 *
 * @author Andrea Aime
 * @source $URL$
 */
public class PgWKBAttributeIO implements AttributeIO {
    private boolean useByteArray;
//    private GeometryFactory gf;
    WKBReader wkbr;
    ByteArrayInStream inStream = new ByteArrayInStream();

    public PgWKBAttributeIO(boolean useByteArray) {
        this.useByteArray = useByteArray;
        wkbr = new WKBReader();
    }
    
    public PgWKBAttributeIO(boolean useByteArray, Hints hints) {
        this.useByteArray = useByteArray;
        // setup the geometry factory according to the hints
        GeometryFactory gf = (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY);
        if(gf == null) {
            PrecisionModel pm =  (PrecisionModel) hints.get(Hints.JTS_PRECISION_MODEL);
            if(pm == null)
                pm = new PrecisionModel();
            Integer SRID = (Integer) hints.get(Hints.JTS_SRID);
            int srid = SRID == null ? 0 : SRID.intValue();
            Integer dimension = (Integer) hints.get(Hints.COORDINATE_DIMENSION);
            CoordinateSequenceFactory csFactory = (CoordinateSequenceFactory) hints.get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);
            if(csFactory == null) {
                if(dimension == null || dimension <= 3) {
                    csFactory = CoordinateArraySequenceFactory.instance();
                } else {
                    csFactory = new LiteCoordinateSequenceFactory();
                }
            } 
            gf = new GeometryFactory(pm, srid, csFactory);
        }
        wkbr = new WKBReader(gf);
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

    /**
     * This method will convert a String of hex characters that represent  the
     * hexadecimal form of a Geometry in Well Known Binary representation to a
     * JTS  Geometry object.
     *
     * @param wkb a String of hex characters where each character  represents a
     *        hex value. In particular, each character is a value of 0-9, A, B
     *        ,C, D, E, or F.
     *
     * @return a JTS Geometry object that is equivalent to the WTB
     *         representation passed in by param wkb
     *
     * @throws IOException if more than one geometry object was found in  the
     *         WTB representation, or if the parser could not parse the WKB
     *         representation.
     */
    private Geometry WKB2Geometry(byte[] wkbBytes)
        throws IOException {
        // convert the byte[] to a JTS Geometry object

    	if (wkbBytes == null)  //DJB: null value from database --> null geometry (the same behavior as WKT).  NOTE: sending back a GEOMETRYCOLLECTION(EMPTY) is also a possibility, but this is not the same as NULL
    		return null;
    	try {
    	    inStream.setBytes(wkbBytes);
    		return wkbr.read(inStream);
    	} catch (Exception e) {
    		throw new DataSourceException("An exception occurred while parsing WKB data", e);
    	}

//        JTSFactory factory = new JTSFactory();
//        WKBParser parser = new WKBParser(factory);
//        try {
//            parser.parseData(wkbBytes, 42102);
//        } catch (Exception e) {
//            throw new DataSourceException("An exception occurred while parsing WKB data", e);
//        }

//        ArrayList geoms = factory.getGeometries();
//
//        if (geoms.size() > 0) {
//            return (Geometry) geoms.get(0);
//        } else if (geoms.size() > 1) {
//            throw new IOException(
//                "Found more than one Geometry in WKB representation ");
//        } else {
//            throw new IOException(
//                "Could not parse WKB representations -  found no Geometries ");
//        }
    }

    private byte[] hexToBytes(String wkb) {
      // convert the String of hex values to a byte[]
      byte[] wkbBytes = new byte[wkb.length() / 2];

      for (int i = 0; i < wkbBytes.length; i++) {
          byte b1 = getFromChar(wkb.charAt(i * 2));
          byte b2 = getFromChar(wkb.charAt((i * 2) + 1));
          wkbBytes[i] = (byte) ((b1 << 4) | b2);
      }

      return wkbBytes;
    }

    /*
     private byte[] byteaToBytes(byte[] bytes) {
        for(int i = 0; i < bytes.length; i++) {
            if(bytes[i] >= 'A')
                bytes[i] -= ('A' + 10);
            else
                bytes[i] -= '0';
        }
        return bytes;
    }
    */
    public void compare(byte[] b1, byte[] b2)
    {
    	int len = b2.length;
    	for (int t=0;t<len;t++)
    	{
    		if (b1[t] != b2[t])
    			System.out.println("differ at position "+t+ "("+b1[t]+" vs "+b2[t]+")");
    	}
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    public Object read(ResultSet rs, int position) throws IOException {
        try {
            if(useByteArray) {
            	byte bytes[] = rs.getBytes(position);
//            	byte b[] = Base64.decode(bytes);
//            	byte b2[] =  Base64.decode(bytes,0,bytes.length);
//            	compare(b,b2);
            	if (bytes == null) // ie. its a null column -> return a null geometry!
            		return null;
                return WKB2Geometry(Base64.decode(bytes));
//                return WKB2Geometry(bytes);
            } else {
                return WKB2Geometry(hexToBytes(rs.getString(position)));
            }
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }

    /**
     * @param metaData
     * @throws SQLException
     *
    private void printMetadata(ResultSetMetaData md) throws SQLException {
        for (int i = 1; i <= md.getColumnCount(); i++) {
            System.out.println(i + " " + md.getColumnName(i) + " " + md.getColumnTypeName(i));
        }

    }*/

    /**
     * Unsupported, will throw an UnsupportedOperationException
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.ResultSet,
     *      int, java.lang.Object)
     */
    public void write(ResultSet rs, int position, Object value)
        throws IOException {
        try {
            if(value == null) {
                rs.updateNull(position);
            } else {
                //rs.updateString(position, WKBEncoder.encodeGeometryHex((Geometry) value));
                rs.updateBytes( position, new WKBWriter().write( (Geometry)value ));
            }
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
                //ps.setString(position, WKBEncoder.encodeGeometryHex((Geometry) value));
                ps.setBytes( position, new WKBWriter().write( (Geometry)value ));
            }
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }

    }
    
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
}
