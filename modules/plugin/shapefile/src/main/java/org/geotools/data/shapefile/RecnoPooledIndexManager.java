/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.shapefile;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.util.logging.Logging;

/**
 * Manages the optional RECNO field index on behalf of the {@link ShapefileDataStore} wrapped with a connection pool manager
 * 
 * @author Alvaro Huarte - Tracasa / ahuarte@tracasa.es
 */
public class RecnoPooledIndexManager extends RecnoIndexManager
{
    static final Logger LOGGER = Logging.getLogger(RecnoPooledIndexManager.class);
    
    // Hash of BasicDataSource's managed.
    private static HashMap<String,BasicDataSource> dataSourceHash = new HashMap<String,BasicDataSource>();
    
    // RecnoPooledIndexManager constructor.
    public RecnoPooledIndexManager(ShpFiles shpFiles, ShapefileDataStore store) 
    {
        super(shpFiles, store);
    }
    
    /**
     * Attempts to establish a connection to the given database URL.
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
    @Override
    protected java.sql.Connection getConnection(String connectionString, java.nio.charset.Charset charSet) throws ClassNotFoundException, SQLException
    {
        if (connectionString==null || connectionString.length()==0)
            return null;

        BasicDataSource dataSource = null;
        
        synchronized (this) {
            dataSource = dataSourceHash.get(connectionString);
            
            if (dataSource==null) {
                dataSource = new BasicDataSource();
                dataSource.setDriverClassName("sun.jdbc.odbc.JdbcOdbcDriver");
                dataSource.setUrl(connectionString);
                dataSource.setDefaultReadOnly(true);
                if (charSet!=null) dataSource.setConnectionProperties("charSet=" + charSet.name());
                dataSourceHash.put(connectionString, dataSource);
            }
        }
        
        java.sql.Connection connection = dataSource.getConnection();
        
        if (connection==null) {
            LOGGER.log(Level.SEVERE, "Error getting a shared Connection of '" + connectionString + "'.");
        }
        return connection;
    }
}
