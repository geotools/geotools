/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.Transaction;
import org.geotools.data.jdbc.JDBCUtils;

public class PostgisDBInfo {
    
    /** The logger for the postgis module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.postgis");

    
    String postgisVersion;
    int postgisMajorVersion;
    int postgisMinorVersion;

    String postgresVersion;
    int postgresMajorVersion;
    int postgresMinorVersion;
    
    boolean schemaEnabled = true;
    boolean byteaEnabled = false;
    boolean geosEnabled = false;
    
    /**
     * Creates a new DBInfo object and populates its values.
     */
    public PostgisDBInfo(Connection conn) {
        //get postgis version
        try {
            String sqlStatement = "SELECT postgis_version();";

            Statement statement = conn.createStatement();
            ResultSet result = statement.executeQuery(sqlStatement);

            if (result.next()) {
                postgisVersion = result.getString(1);
                LOGGER.fine("PostGIS version is " + postgisVersion);

                int[] versionNumbers;

                try {
                    String[] values = postgisVersion.trim().split(" ");
                    String[] versionNumbersStr = values[0].trim().split("\\.");
                    versionNumbers = new int[versionNumbersStr.length];

                    for (int i = 0; i < versionNumbers.length; i++) {
                        versionNumbers[i] = Integer.parseInt(versionNumbersStr[i]);
                    }
                    
                    postgisMajorVersion = versionNumbers[0];
                    postgisMinorVersion = versionNumbers[1];
                    
                    // bytea function has been introduced in 0.7.2
                    if ((postgisMajorVersion > 0) || (postgisMinorVersion > 7)
                            || ((versionNumbers.length > 2)
                            && (versionNumbers[2] >= 2))) {
                        byteaEnabled = true;
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.WARNING,
                        "Exception occurred while parsing the version number.",
                        e);
                }

                if (postgisVersion.indexOf("USE_GEOS=1") != -1) {
                    this.geosEnabled = true;
                } else {
                    //warn about not using GEOS
                    LOGGER.warning("GEOS is NOT enabled. This will result in limited functionality and performance.");
                }
                
                //check postgres version to determine if schemas should be 
                // enabled, pre 7.3 -> no
                postgresVersion = conn.getMetaData().getDatabaseProductVersion();
                LOGGER.fine("Postgres version is " + postgresVersion);
                postgresMajorVersion = conn.getMetaData().getDatabaseMajorVersion();
                postgresMinorVersion = conn.getMetaData().getDatabaseMinorVersion();
                
                if (postgresMajorVersion < 7 || (postgresMajorVersion == 7 && postgresMinorVersion < 3)) {
                    schemaEnabled = false; //pre 7.3
                }
            }
        } catch (SQLException sqle) {
            String message = sqle.getMessage();
            LOGGER.severe(message);
        } finally {
            JDBCUtils.close(conn, Transaction.AUTO_COMMIT, null);
        }

    }
    
    public String getVersion() {
        return postgisVersion;
    }
    
    public int getMajorVersion() {
        return postgisMajorVersion;
    }

    public int getMinorVersion() {
        return postgisMinorVersion;
    }

    public String getPostgresVersion() {
        return postgresVersion;
    }
    
    public int getPostgresMajorVersion() {
        return postgresMajorVersion;
    }

    public int getPostgresMinorVersion() {
        return postgresMinorVersion;
    }
    
    public boolean isSchemaEnabled() {
        return schemaEnabled;
    }
    
    public boolean isGeosEnabled() {
        return geosEnabled;
    }

    public boolean isByteaEnabled() {
        return byteaEnabled;
    }
}
