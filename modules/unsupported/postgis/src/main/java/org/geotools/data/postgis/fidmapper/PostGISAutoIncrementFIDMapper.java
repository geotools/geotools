/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis.fidmapper;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

import org.geotools.data.jdbc.fidmapper.AutoIncrementFIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;

/**
 * Generate FID based on an auto increment function, the most stable approach for
 * use with editing.
 * 
 * @author Jesse Eichar, Refractions Research, Inc.
 * @author Cory Horner, Refractions Research, Inc.
 *
 * @source $URL$
 */
public class PostGISAutoIncrementFIDMapper extends AutoIncrementFIDMapper
                implements FIDMapper {

        private static final long serialVersionUID = -6082930630426171079L;

        /** Indicates that the pg_get_serial_sequence function exists, and works for this table */ 
        boolean can_usepg_get_serial_sequence = true;

        /** Flag to indicate when we can't find the table's sequence */
        boolean hasSerialSequence = true;
        
        /** The actual name of the sequence, if we have found it */
        String sequenceName = null;
        
        public PostGISAutoIncrementFIDMapper(String tableSchemaName, String tableName, String colName, int dataType) {
                super(tableSchemaName, tableName, colName, dataType);
        }
        
        public PostGISAutoIncrementFIDMapper(String tableName, String colName, int dataType, 
                boolean returnFIDColumnsAsAttributes) {
            super(tableName, colName, dataType);
            this.returnFIDColumnsAsAttributes = returnFIDColumnsAsAttributes; 
        }
        
        public String createID( Connection conn, SimpleFeature feature, Statement statement ) throws IOException {
            String id = retriveId(conn, feature, statement);
            if(id != null && returnFIDColumnsAsAttributes) {
                // we have to udpate the attribute in the feature too
                try {
                    feature.setAttribute(colNames[0], id);
                } catch(IllegalAttributeException e) {
                    throw new IOException("Could not set generated key " + id + " into attribute " + colNames[0]);
                }
            }
            return id;
        }

        /**
         * Attempts to determine the FID after it was inserted, using four techniques:
         * 1. SELECT currval(pg_get_serial_sequence(...))
         * 2. SELECT currval(sequence name) <-- using other methods to get name
         * 3. SELECT fid ... ORDER BY fid DESC LIMIT 1
         */
        public String retriveId( Connection conn, SimpleFeature feature, Statement statement )
            throws IOException {
            //this approach works when using prepared statements through PostgisPSFeatureWriter, 
            //commenting out until it becomes supported
//            try {
//                //may the insert statement being done with "RETURNING pk"?. In that case statement.getResultset() contains the needed information
//                ResultSet generatedKeys = statement.getResultSet();
//                if(generatedKeys != null && generatedKeys.next()){
//                    Object key = generatedKeys.getObject(1);
//                    return String.valueOf(key);
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
            
            ResultSet rs = null;
            if (can_usepg_get_serial_sequence) {
                try {
                    //use pg_get_serial_sequence('"table name"','"column name"')
                    String sql = "SELECT currval(pg_get_serial_sequence('\"";
                    String schema = getTableSchemaName();
                    if (schema != null && !schema.equals("")) {
                        sql = sql + schema + "\".\""; 
                    }
                    sql = sql + getTableName() + "\"','" + getColumnName() + "'))";
                    rs = statement.executeQuery(sql); 
                    if (rs.next() && rs.getString("currval") != null){
                        return rs.getString("currval");
                    }else {
                        can_usepg_get_serial_sequence = false;
                    }
                } catch (Exception e) {
                    can_usepg_get_serial_sequence = false;
                } finally {
                    try {
                        if (rs != null)
                            rs.close();
                    } catch (Exception e) {
                        //oh well
                    }
                }
            }
            //TODO: add logging
            if (hasSerialSequence) {
                if (sequenceName == null) {
                    // try to find the sequence (this makes the assumption that
                    // the sequence name contains "tableName_columnName")
                    String sql = "SELECT relname FROM pg_catalog.pg_class WHERE relkind = 'S' AND relname LIKE '"
                        + getTableName() + "_" + getColumnName() + "_seq'";
                    try {
                        rs = statement.executeQuery(sql);
                        if (rs.next() && rs.getString(1) != null) {
                            sequenceName = rs.getString(1);
                        } else {
                            hasSerialSequence = false;
                        }
                    } catch (Exception e) {
                        hasSerialSequence = false;
                    } finally {
                        try {
                            if (rs != null)
                                rs.close();
                        } catch (Exception e) {
                            //oh well
                        }
                    }
                }
                
                if (sequenceName != null) {
                    //get the sequence value
                    String sql = "SELECT currval('\"" + sequenceName + "\"')";
                    try {
                        rs = statement.executeQuery(sql); 
                        if (rs.next() && rs.getString("currval") != null)
                            return rs.getString("currval");
                        else {
                            hasSerialSequence = false;
                        }
                    } catch (Exception e) {
                        hasSerialSequence = false;
                    } finally {
                        try {
                            if (rs != null)
                                rs.close();
                        } catch (Exception e) {
                            //oh well
                        }
                    }
                }
            }
            return findInsertedFID(conn, feature, statement);
        }

        /**
         * Our last resort method for getting the FID. 
         */
        private String findInsertedFID( Connection conn, SimpleFeature feature, Statement statement )
            throws IOException {
            String sql = "SELECT \"" + getColumnName() + "\" FROM \"";
            String schema = getTableSchemaName();
            if (schema != null && !schema.equals("")) {
                sql = sql + schema + "\".\""; 
            }
            sql = sql + getTableName() + "\" ORDER BY \"" + getColumnName()
                + "\" DESC LIMIT 1;"; 
            ResultSet rs = null;
            try {
                statement.execute(sql); 
                rs = statement.getResultSet();
                rs.next();
                return rs.getString(getColumnName());
            } catch (Exception e) { //i surrender
                return super.createID(conn, feature, statement);
            } finally {
                try {
                    if (rs != null)
                        rs.close();
                } catch (Exception e) {
                    //oh well
                }
            }
        }
}
