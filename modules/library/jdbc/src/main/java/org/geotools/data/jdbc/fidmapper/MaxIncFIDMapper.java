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
/*
 * Created on 18-apr-2004
 * 26-may-2005 D. Adler Removed returnIDAsAttribute variable and method.
 * 12-jul-2006 D. Adler GEOT-728 Refactor FIDMapper classes
 */
package org.geotools.data.jdbc.fidmapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;


/**
 * A FID mapper that uses a single integer column as the primary key and that
 * does a <code>SELECT MAX(fixColumn) + 1</code> to generate new ones. This is
 * a fragile generation strategy, better use a sequence or a serial to get
 * reliable results.
 *
 * @author aaime
 * @source $URL$
 * @deprecated scheduled for removal in 2.7, use classes in org.geotools.jdbc
 */
public class MaxIncFIDMapper extends AbstractFIDMapper {
	private static final long serialVersionUID = 5719859796485477701L;

	/** A logger */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc.fidmapper");
    
    /**
     * Creates a new MaxIncFIDMapper object.
     *
     * @param tableName the table name
     * @param FIDColumn the name of the FID column
     * @param FIDColumnType The SQL type of the column - must be a numeric type
     */
    public MaxIncFIDMapper(String tableName, String FIDColumn, int FIDColumnType) {
        this(null, tableName, FIDColumn, FIDColumnType, false);
    }

    /**
     * Creates a new MaxIncFIDMapper object that will return the FID columns as
     * business attributes.
     *
     * @param tableSchemaName the schema of this table
     * @param tableName the table name
     * @param FIDColumn the name of the FID column
     * @param FIDColumnType The SQL type of the column - must be a numeric type
     * @param returnFIDColumnsAsAttributes true to return FID columns as
     *        attributes.
     */
    public MaxIncFIDMapper(String tableSchemaName, String tableName, String FIDColumn,
        int FIDColumnType, boolean returnFIDColumnsAsAttributes) {
    	super(tableSchemaName, tableName);
        this.returnFIDColumnsAsAttributes = returnFIDColumnsAsAttributes;
        setInfo(FIDColumn, FIDColumnType, 0, 0, false);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getID(java.lang.Object[])
     */
    public String getID(Object[] attributes) {
        return String.valueOf(attributes[0]);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#getPKAttributes(java.lang.String)
     */
    public Object[] getPKAttributes(String FID) {
    	try{
        return new Object[] { new Long(Long.parseLong(FID)) };
    	}catch (NumberFormatException e) {
			return new Object[]{new Long(-1)};
		}
    }



    /**
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object object) {
        if (!(object instanceof TypedFIDMapper)) {
            return false;
        }

        MaxIncFIDMapper other = (MaxIncFIDMapper) object;

        return (other.getColumnName() == this.getColumnName())
        && (other.getColumnType() == this.getColumnType())
        && (other.returnFIDColumnsAsAttributes == this.returnFIDColumnsAsAttributes);
    }

    /**
     * @see org.geotools.data.jdbc.fidmapper.FIDMapper#createID(java.sql.Connection,
     *      Feature, Statement)
     */
    public String createID(Connection conn, SimpleFeature feature, Statement statement)
        throws IOException {
    	Statement stmt = null;
    	ResultSet rs =null;
        try {
            stmt = conn.createStatement();
            rs = stmt.executeQuery("Select MAX(" + getColumnName()
                    + ") from " + tableName);

            if (rs.next()) {
                long maxFid = rs.getLong(1);

                return String.valueOf(maxFid + 1);
            } else {
                throw new DataSourceException("Could not get MAX for "
                    + tableName + "." + getColumnName()
                    + ": No result returned from query");
            }
        } catch (SQLException e) {
            throw new DataSourceException("An sql problem occurred. Are the table and the fid column there?",
                e);
        }
        finally {
        	if( stmt != null ) {
        		try {
					stmt.close();
				} catch (SQLException e) {
					LOGGER.log(Level.WARNING, "MaxIncFidMapper could not close statement:"+e, e );
				}
        	}
        	if( rs != null ) {
        		try {
					rs.close();
				} catch (SQLException e) {
					LOGGER.log(Level.WARNING, "MaxIncFidMapper could not close resultset:"+e, e );
				}
        	}
        }
    }
    
    /**
     * @return {@code true} if fid can be parsed as a long, as the fids generated by this FIDMapper
     *         can, {@code false} othwerwise
     * @see FIDMapper#isValid(String)
     */
    public boolean isValid(String fid) {
        try {
            //explicitly parse with radix 10 as Long.toString uses it
            Long.parseLong(fid, 10);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }

}
