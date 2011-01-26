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

package org.geotools.data.jdbc.attributeio;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;

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
 * @since 2.4.1
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class WKBAttributeIO implements AttributeIO {
    WKBReader wkbr;
    ByteArrayInStream inStream = new ByteArrayInStream();

    public WKBAttributeIO() {
        wkbr = new WKBReader();
    }
    
    public WKBAttributeIO(Hints hints) {
        // setup the geometry factory according to the hints
        GeometryFactory gf = (GeometryFactory) hints.get(Hints.JTS_GEOMETRY_FACTORY);
        if(gf == null) {
            PrecisionModel pm =  (PrecisionModel) hints.get(Hints.JTS_PRECISION_MODEL);
            if(pm == null)
                pm = new PrecisionModel();
            Integer SRID = (Integer) hints.get(Hints.JTS_SRID);
            int srid = SRID == null ? 0 : SRID.intValue();
            CoordinateSequenceFactory csFactory = (CoordinateSequenceFactory) hints.get(Hints.JTS_COORDINATE_SEQUENCE_FACTORY);
            if(csFactory == null)
                csFactory = CoordinateArraySequenceFactory.instance();
            gf = new GeometryFactory(pm, srid, csFactory);
        }
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
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    public Object read(ResultSet rs, int position) throws IOException {
        try {
            byte bytes[] = rs.getBytes(position);
            if (bytes == null) // ie. its a null column -> return a null geometry!
                return null;
            return WKB2Geometry(bytes);
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }
    }

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
                ps.setBytes( position, new WKBWriter().write( (Geometry)value ));
            }
        } catch (SQLException e) {
            throw new DataSourceException("SQL exception occurred while reading the geometry.", e);
        }

    }
    
    /**
     * Accelerates data loading compared to the plain InStream shipped along with JTS
     * @author Andrea Aime - TOPP
     *
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
}

