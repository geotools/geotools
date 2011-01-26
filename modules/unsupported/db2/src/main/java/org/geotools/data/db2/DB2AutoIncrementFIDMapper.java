/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005-2007. All rights reserved.
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
 *
 */
package org.geotools.data.db2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.geotools.data.jdbc.fidmapper.AutoIncrementFIDMapper;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Overrides AutoIncrementFIDMapper methods for DB2-specific handling.
 *
 * @author David Adler - IBM Corporation
 *
 * @source $URL$
 */
public class DB2AutoIncrementFIDMapper extends AutoIncrementFIDMapper {

    /**
     * Default constructor.
     */
    public DB2AutoIncrementFIDMapper(String databaseSchemaName, String tableName, String colName, int dataType) {
        super(databaseSchemaName, tableName, colName, dataType);

    }
    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#createID(java.sql.Connection,
     *      org.geotools.feature.Feature, Statement)
     */
    public String createID(Connection conn, SimpleFeature feature, Statement statement)
        throws IOException {
    	if( tableName==null || getColumnName()==null )
    		return null;
    	try {
    		String sql="SELECT MAX(\"" + getColumnName() 
				+ "\") FROM \"" + this.tableSchemaName + "\"." 
				+ "\"" + tableName + "\"";
    		statement.execute(sql);
    		ResultSet resultSet = statement.getResultSet();
    		if( resultSet.next() )
    			return resultSet.getString(1);
    		else
    			return null;
		} catch (SQLException e) {
			throw (IOException) new IOException().initCause(e);
		}
    }    
}
