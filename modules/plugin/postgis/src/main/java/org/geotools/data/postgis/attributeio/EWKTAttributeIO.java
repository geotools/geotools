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
package org.geotools.data.postgis.attributeio;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.attributeio.AttributeIO;
import org.geotools.geometry.jts.WKTReader2;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;
import com.vividsolutions.jts.io.WKTWriter;


/**
 * An attribute IO object that can read and write geometries encoded into
 * EWKT format. 
 *
 * @since @2.4.1 Read/Write EWKT
 * @author jgarnett
 * @source $URL$
 */
public class EWKTAttributeIO implements AttributeIO {
    WKTReader reader;
    WKTWriter writer;

    /**
     * Lazily initialize the WKTReader
     */
    private WKTReader getWKTReader() {
        if (reader == null) {
            reader = new WKTReader2();
        }
        return reader;
    }

    /**
     * Lazily initialize the WKTWriter
     *
     */
    private WKTWriter getWKTWriter() {
        if (writer == null) {
            writer = new WKTWriter();
        }

        return writer;
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#read(java.sql.ResultSet,
     *      int)
     */
    public Object read(ResultSet rs, int position) throws IOException {
        try {
            String wkt = rs.getString(position);
            if (wkt == null || wkt.equals("")) {
            	return null;
            }
    		int srid = 0; // unknown!
    		// we must be in EWKT mode
			int split = wkt.indexOf(";");
			srid = Integer.parseInt( wkt.substring(5,split));
			wkt = wkt.substring(split+1);			
    		Geometry geom = getWKTReader().read(wkt);
    		geom.setSRID( srid );
    		return geom;
    		
        } catch (SQLException e) {
            throw new DataSourceException("Sql reading problem", e);
        } catch (ParseException e) {
            throw new DataSourceException("Could not parse WKT", e);
        }
    }

    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.ResultSet,
     *      int, java.lang.Object)
     */
    public void write(ResultSet rs, int position, Object value)
        throws IOException {
        try {
            if (value == null) {
                rs.updateNull(position);
            } else {
                Geometry g = (Geometry) value;
                String wkt = getWKTWriter().write(g);
                String ewkt = "SRID="+g.getSRID()+";"+wkt;                
                rs.updateString(position, ewkt);
            }
        } catch (Exception e) {
            throw new DataSourceException("Sql writing problem", e);
        }
    }
    
    /**
     * @see org.geotools.data.jdbc.attributeio.AttributeIO#write(java.sql.ResultSet,
     *      int, java.lang.Object)
     */
    public void write(PreparedStatement ps, int position, Object value)
        throws IOException {
        try {
            if (value == null) {
                ps.setNull(position, Types.VARCHAR);
            } else {
                Geometry g = (Geometry) value;
                String wkt = getWKTWriter().write(g);
                String ewkt = "SRID="+g.getSRID()+";"+wkt;
                ps.setString(position, ewkt);
            }
        } catch (Exception e) {
            throw new DataSourceException("Sql writing problem", e);
        }
    }
}
