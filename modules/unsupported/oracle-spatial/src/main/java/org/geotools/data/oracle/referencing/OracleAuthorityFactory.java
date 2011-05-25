/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle.referencing;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.referencing.JDBCAuthorityFactory;
import org.geotools.referencing.CRS;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Access CRS information from the Oracle MD_SYS.CS_SRS view.
 * 
 * @author Andrea Aime - TOPP
 *
 * @source $URL$
 */
public class OracleAuthorityFactory extends JDBCAuthorityFactory {

    private String TABLE_NAME = "MDSYS.CS_SRS";
    private String WKT_COLUMN = "WKTEXT";
    private String SRID_COLUMN = "SRID";
    private String AUTH_SRID = "SRID";

    
    /**
     * Construct <code>PostgisAuthorityFactory</code>.
     * 
     * @param pool
     */
    public OracleAuthorityFactory( DataSource dataSource) {
        super(dataSource);
    }

    public CoordinateReferenceSystem createCRS( int srid ) throws FactoryException, IOException {
        Connection dbConnection = null;

        try {
            String sqlStatement = "SELECT * FROM " + TABLE_NAME + " WHERE " + SRID_COLUMN + " = "
                    + srid;
            dbConnection = dataSource.getConnection();

            Statement statement = dbConnection.createStatement();
            ResultSet result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                CoordinateReferenceSystem crs = null;
                try {
                    crs = createFromAuthority(result);
                } catch (Exception e) {
                    // do nothing
                }
                if (crs == null) {
                    crs=createFromWKT(result);
                }
                JDBCUtils.close(statement);

                return crs;
            } else {
                String mesg = "No row found for "+srid+" in table: " + TABLE_NAME;
                throw new FactoryException(mesg);
            }
        } catch (SQLException sqle) {
            String message = sqle.getMessage();

            throw new DataSourceException(message, sqle);
        } finally {
            JDBCUtils.close(dbConnection, Transaction.AUTO_COMMIT, null);
        }
    }

    protected CoordinateReferenceSystem createFromWKT( ResultSet result )
            throws DataSourceException, FactoryException {
        try {
            String wkt = result.getString(WKT_COLUMN);

            return factory.createFromWKT(wkt);

        } catch (SQLException sqle) {
            String message = sqle.getMessage();

            throw new DataSourceException(message, sqle);
        }
    }

    protected CoordinateReferenceSystem createFromAuthority( ResultSet result )
            throws DataSourceException, FactoryException {
        try {
            int id = result.getInt(AUTH_SRID);

            return CRS.decode("EPSG" + ":" + id);
        } catch (SQLException sqle) {
            String message = sqle.getMessage();

            throw new DataSourceException(message, sqle);
        }
    }

}
