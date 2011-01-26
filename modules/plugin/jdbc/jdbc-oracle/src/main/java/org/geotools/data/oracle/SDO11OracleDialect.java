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
package org.geotools.data.oracle;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;

import org.geotools.jdbc.JDBCDataStore;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * This dialect uses the Oracle 11 specific sdoapi.jar API, which is touted to
 * be faster than then standard JGeometry one. Benchmarking shows that it's no
 * match for the standard GeometryConverter thought, and only adds a nasty
 * dependency that cannot be redistributed, so it lays here inactive only
 * for historical purposes. Uncomment here and in OracleDialect if you 
 * want to try it out.
 * @author Andrea Aime
 */
class SDO11OracleDialect extends OracleDialect {

    protected SDO11OracleDialect(JDBCDataStore dataStore) {
        super(dataStore);
    }

    @Override
    Geometry readGeometry(ResultSet rs, String column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        return readGeometry(rs.getBytes(column), factory, cx);
    }

    @Override
    Geometry readGeometry(ResultSet rs, int column, GeometryFactory factory, Connection cx)
            throws IOException, SQLException {
        return readGeometry(rs.getBytes(column), factory, cx);
    }

    private Geometry readGeometry(byte[] rawGeometry, GeometryFactory factory, Connection cx) throws SQLException {
        if(rawGeometry == null)
            return null;
        
        throw new UnsupportedOperationException("Due to licensing issues the code depending to " +
        		"JGeometry has been commented out");
//        JGeometry geom = null;
//        try {
//            geom = JGeometry.load(rawGeometry);
//        } catch(Exception e) {
//            throw (SQLException) new SQLException("Could not parse the geometry struct").initCause(e);
//        }
//        return JGeometryConverter.toJTS(factory, geom);
    }

    @Override
    public void setGeometryValue(Geometry g, int srid, Class binding, PreparedStatement ps,
            int column) throws SQLException {
        // Handle the null geometry case.
        // Surprisingly, using setNull(column, Types.OTHER) does not work...
        if (g == null) {
            ps.setNull(column, Types.STRUCT, "MDSYS.SDO_GEOMETRY");
            return;
        }
        
        throw new UnsupportedOperationException("Due to licensing issues the code depending to " +
        "JGeometry has been commented out");


//        JGeometry geom = JGeometryConverter.toJGeometry(g, srid);
//        try {
//            OracleConnection ocx = unwrapConnection(ps.getConnection());
//            // not to be mistaken for store(geom, connection), this is the faster 11G version
//            // (no kidding, they created a new method by inverting the types in the signature... man!!!!)
//            STRUCT obj = JGeometry.store(ocx, geom);
//            ps.setObject(column, obj);
//        } catch(Exception e) {
//            throw (SQLException) new SQLException("Could not turn the JGeometry " +
//            		"into a raw byte[]").initCause(e);
//        }
//
//        if (LOGGER.isLoggable(Level.FINE)) {
//            String sdo = SDOSqlDumper.toSDOGeom(g, srid);
//            LOGGER.fine("Setting paramtetr " + column + " as " + sdo);
//        }
    }

}
