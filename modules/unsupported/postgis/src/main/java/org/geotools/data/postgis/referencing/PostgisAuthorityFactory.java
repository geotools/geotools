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
package org.geotools.data.postgis.referencing;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.geotools.data.DataSourceException;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.referencing.JDBCAuthorityFactory;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Access CRS informationj from the PostGIS SPATIAL_REF_SYS table.
 * 
 * @author Jesse Eichar
 *
 * @source $URL$
 */
public class PostgisAuthorityFactory extends JDBCAuthorityFactory {
    
    private static final Logger LOGGER = Logging.getLogger(PostgisAuthorityFactory.class);

    private String TABLE_NAME = "SPATIAL_REF_SYS";
    private String WKT_COLUMN = "SRTEXT";
    private String SRID_COLUMN = "SRID";
    private String AUTH_NAME = "AUTH_NAME";
    private String AUTH_SRID = "AUTH_SRID";

    /**
//     * JD: Added this contstructor because the META_INF/services plugin
//     * mechanism requires it. 
//     */
//    public PostgisAuthorityFactory( ) {
//    	super(null);
//    }
    
    /**
     * Construct <code>PostgisAuthorityFactory</code>.
     * 
     * @param pool
     */
    public PostgisAuthorityFactory( DataSource pool ) {
        super(pool);
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
                    String name = result.getString(AUTH_NAME);
                    int id = result.getInt(AUTH_SRID);
                    crs = createFromAuthority(name, id);
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
            LOGGER.log(Level.FINE, "Error occurred while accessing the {0} table, switching to direct EPSG factory decode", TABLE_NAME);
            return createFromAuthority("EPSG", srid); 
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

    protected CoordinateReferenceSystem createFromAuthority( String authName, int srid )
            throws DataSourceException, FactoryException {
        return CRS.decode(authName + ":" + srid);
    }

}
