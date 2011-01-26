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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import junit.framework.TestCase;

import org.geotools.data.DataSourceException;
import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCUtils;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.data.postgis.PostgisDataStoreFactory;
import org.geotools.data.postgis.PostgisTests;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Test PostgisAuthorityFactory
 * 
 * @author Jesse Eichar, Refractions Research Inc.
 * @source $URL$
 */
public class PostgisAuthorityFactoryOnlineTest extends TestCase {
    
    
    private String TABLE_NAME="SPATIAL_REF_SYS";
    private String SRID_COLUMN="SRID";
    
    public int getSRIDs(DataSource pool) throws Exception{
        Connection dbConnection = null;

        try {
            String sqlStatement = "SELECT "+SRID_COLUMN+" FROM "+TABLE_NAME;
            dbConnection = pool.getConnection();

            Statement statement = dbConnection.createStatement();
            ResultSet result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                int srid = result.getInt("srid");
                JDBCUtils.close(statement);

                return srid;
            } else {
                String mesg = "No srid column row: "+ TABLE_NAME;
                throw new DataSourceException(mesg);
            }
        } catch (SQLException sqle) {
            String message = sqle.getMessage();

            throw new DataSourceException(message, sqle);
        } finally {
            JDBCUtils.close(dbConnection, Transaction.AUTO_COMMIT, null);
        }
    }
    
    public void testCreateCRS() throws Exception{
    	PostgisTests.Fixture f = PostgisTests.newFixture();
        String url = "jdbc:postgresql" + "://" + f.host + ":" + f.port + "/" + f.database;
        ManageableDataSource pool = PostgisDataStoreFactory.getDefaultDataSource(f.host, f.user, f.password, f.port.intValue(), f.database, 10, 2, false);
      PostgisAuthorityFactory factory=new PostgisAuthorityFactory(pool);
      CoordinateReferenceSystem crs=factory.createCRS(getSRIDs(pool));
      assertNotNull(crs);
  }

}
