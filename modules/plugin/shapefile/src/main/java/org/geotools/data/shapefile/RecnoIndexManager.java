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

import static org.geotools.data.shapefile.files.ShpFileType.SHP;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.util.logging.Logging;
import org.geotools.data.shapefile.files.ShpFiles;
import org.geotools.data.shapefile.index.CloseableIterator;
import org.geotools.data.shapefile.index.Data;
import org.geotools.data.shapefile.index.DataDefinition;
import org.geotools.data.shapefile.shp.IndexFile;
import org.geotools.data.jdbc.FilterToSQLException;

import org.geotools.filter.FilterCapabilities;
import org.opengis.filter.ExcludeFilter;
import org.opengis.filter.Filter;
import org.opengis.filter.IncludeFilter;
import org.opengis.filter.PropertyIsLike;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Add;
import org.opengis.filter.expression.Divide;
import org.opengis.filter.expression.Multiply;
import org.opengis.filter.expression.Subtract;

/**
 * Manages the optional RECNO field index on behalf of the {@link ShapefileDataStore}
 * It only works for Windows SO's.
 * 
 * @author Alvaro Huarte - Tracasa / ahuarte@tracasa.es
 */
public class RecnoIndexManager 
{
    static final Logger LOGGER = Logging.getLogger(RecnoIndexManager.class);

    // The Microsoft Visual FoxPro Driver is available!
    static boolean MICROSOFT_FOXPRO_DRIVER_INSTALLED = false;
    // The Advantage StreamlineSQL ODBC is available!
    static boolean ADVANTAGE_ODBC_DRIVER_INSTALLED = false;
    
    // Describes the allowed filters we support for alphanumeric Dbase queries.
    static final FilterCapabilities filterCapabilities = new FilterCapabilities();
    
    ShpFiles shpFiles;
    ShapefileDataStore store;

    // Static constructor of RecnoIndexManager class.
    static 
    {
        boolean runningWindows = System.getProperty("os.name").toUpperCase().contains("WINDOWS");
        
        /* TODO: Now, it only works for two ODBC drivers running in Windows SO's: 
         *  - Microsoft ODBC FoxPro Driver (x86).
         *  - Advantage StreamlineSQL ODBC driver (x86/x64).
         *
         * It is feasible use the 'Advantage StreamlineSQL ODBC' in Linux platforms. 
         * See... 
         *  http://devzone.advantagedatabase.com/dz/content.aspx?Key=20&Release=16&Product=14
         *  http://scn.sap.com/docs/DOC-39207
         */
        if (runningWindows)
        {
            java.sql.Connection connection = null;
            
            String connectionString = null;
            String tablePath = System.getProperty("user.dir");
            
            // Get if available two 'superfast' JDBC driver of Windows for DBF tables.
            // 1) Microsoft Visual FoxPro Driver:
            try
            {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                connectionString = "jdbc:odbc:Driver={Microsoft Visual FoxPro Driver};SourceType=DBF;SourceDB="+tablePath+";";
                
                if ((connection = java.sql.DriverManager.getConnection(connectionString, "", ""))!=null)
                {
                    MICROSOFT_FOXPRO_DRIVER_INSTALLED = true;
                    connection.close();

                    LOGGER.info("The 'Microsoft Visual FoxPro Driver' is available!");
                }
            }
            catch (Exception error)
            {
                LOGGER.info("The 'Microsoft Visual FoxPro Driver' is not available!");
            }
            // 2) Advantage StreamlineSQL ODBC Driver:
            try
            {
                Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
                connectionString = "jdbc:odbc:Driver={Advantage StreamlineSQL ODBC};DataDirectory="+tablePath+";DefaultType=FoxPro;ServerTypes=1;AdvantageLocking=OFF;Pooling=FALSE;ShowDeleted=FALSE;";
    			
                if ((connection = java.sql.DriverManager.getConnection(connectionString, "", ""))!=null)
                {
                    ADVANTAGE_ODBC_DRIVER_INSTALLED = true;
                    connection.close();
            		
                    LOGGER.info("The 'Advantage StreamlineSQL ODBC Driver' is available!");
                }
            }
            catch (Exception error)	
            {
                LOGGER.info("The 'Advantage StreamlineSQL ODBC Driver' is not available!");
            }
            
            // Common alphanumeric filter capabilities of the JDBC providers.
            filterCapabilities.addAll(FilterCapabilities.LOGICAL_OPENGIS);
            filterCapabilities.addAll(FilterCapabilities.SIMPLE_COMPARISONS_OPENGIS);
            filterCapabilities.addType(Add.class);
            filterCapabilities.addType(Subtract.class);
            filterCapabilities.addType(Multiply.class);
            filterCapabilities.addType(Divide.class);
            filterCapabilities.addType(PropertyIsNull.class);
            filterCapabilities.addType(IncludeFilter.class);
            filterCapabilities.addType(ExcludeFilter.class);
            filterCapabilities.addType(PropertyIsLike.class);
        }
    }
    
    // RecnoIndexManager constructor.
    public RecnoIndexManager(ShpFiles shpFiles, ShapefileDataStore store) 
    {
        this.shpFiles = shpFiles;
        this.store = store;
    }
    
    /**
     * Attempts to establish a connection to the given database URL.
     * @throws ClassNotFoundException 
     * @throws SQLException 
     */
    protected java.sql.Connection getConnection(String connectionString, java.nio.charset.Charset charSet) throws ClassNotFoundException, SQLException
    {
        java.util.Properties info = new java.util.Properties();
        info.put("charSet", charSet.name());
        info.put("user", "");
        info.put("password", "");
        
        Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
        return java.sql.DriverManager.getConnection(connectionString, info);
    }
    /**
     * Attempts to close the specified connection managed.
     */
    protected void closeConnection(java.sql.Connection connection)
    {
        org.geotools.data.jdbc.JDBCUtils.close(connection, null, null);
    }
    
    /**
     * Returns the record index collection that matches with the specified filter using one super fast ODBC Driver.
     */
    private List<Integer> queryRecnoIndex(String shapeFileName, Filter filter, java.nio.charset.Charset charSet, int maxFeatures) throws SQLException, ClassNotFoundException, IOException, FilterToSQLException
    {
        java.sql.Connection connection = null;
        java.sql.Statement stmt = null;
        java.sql.ResultSet rs = null;
        
        List<Integer> recnoList = new ArrayList<Integer>();
        try
        {
            File file = org.geotools.data.DataUtilities.urlToFile(new java.net.URL(shapeFileName));
            if (file==null) return null;
            
            String connectionString = null;
            String tablePath = file.getParentFile().getPath();
            String tableName = file.getName().substring(0, file.getName().lastIndexOf("."));    		
            String whereFilter = new RecnoFilterToSQL(filterCapabilities).encodeToString(filter);
            
            // TableName is valid ?
            if (tableName.indexOf('-')!=-1 || tableName.indexOf('(')!=-1)
                return null;
    		
            if (ADVANTAGE_ODBC_DRIVER_INSTALLED)
            {
                connectionString = "jdbc:odbc:Driver={Advantage StreamlineSQL ODBC};DataDirectory="+tablePath+";DefaultType=FoxPro;ServerTypes=1;AdvantageLocking=OFF;Pooling=FALSE;ShowDeleted=FALSE;";
                
                // Read the Fid's from the specified Query with this Driver.
                if ((connection = getConnection(connectionString, charSet))!=null)
                {
                    if ((stmt = connection.createStatement())!=null)
                    {
                        String sql = maxFeatures!=-1 && maxFeatures<Integer.MAX_VALUE ?
                            String.format("SELECT TOP %d ROWID FROM [%s] %s;", maxFeatures, tableName, whereFilter) :
                            String.format("SELECT ROWID FROM [%s] %s;", tableName, whereFilter);
                        
                        if ((rs = stmt.executeQuery(sql))!=null)
                        {
                            while (rs.next())
                            {
                                Integer id = RecnoIndexManager.ConvertRowidToRecno(rs.getString(1));
                                recnoList.add(id - 1);
                            }
                        }
                    }
                }
            }
            else
            if (MICROSOFT_FOXPRO_DRIVER_INSTALLED)
            {
                connectionString = "jdbc:odbc:Driver={Microsoft Visual FoxPro Driver};SourceType=DBF;SourceDB="+tablePath+";";
                
                // Read the Fid's from the specified Query with this Driver.
                if ((connection = getConnection(connectionString, charSet))!=null)
                {
                    if ((stmt = connection.createStatement())!=null)
                    {
                        String sql = maxFeatures!=-1 && maxFeatures<Integer.MAX_VALUE ?
                            String.format("SELECT TOP %d recno() FROM [%s] %s;", maxFeatures, tableName, whereFilter) :
                            String.format("SELECT recno() FROM [%s] %s;", tableName, whereFilter);
                        
                        if ((rs = stmt.executeQuery(sql))!=null)
                        {
                            while (rs.next())
                            {
                                Integer id = rs.getInt(1);
                                recnoList.add(id - 1);
                            }
                        }
                    }
                }
            }
        }
        finally
        {
            org.geotools.data.jdbc.JDBCUtils.close(rs);
            org.geotools.data.jdbc.JDBCUtils.close(stmt);
            
            closeConnection(connection);            
        }
        return recnoList;
    }
    
    /**
     * Convert the specified Advantage StreamlineSQL ROWID to the DBF RECNO value.
     */
    private static int ConvertRowidToRecno(String rowID)
    {
        final String BASE64 = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/";

        // The RecNo is the last 6 characters of the ROWID.
        int recno = 0;
        recno += BASE64.indexOf(rowID.charAt(12)) * 1073741824;
        recno += BASE64.indexOf(rowID.charAt(13)) * 16777216;
        recno += BASE64.indexOf(rowID.charAt(14)) * 262144;
        recno += BASE64.indexOf(rowID.charAt(15)) * 4096;
        recno += BASE64.indexOf(rowID.charAt(16)) * 64;
        recno += BASE64.indexOf(rowID.charAt(17));		
        return recno;
    }
    
    /**
     * Returns if the RECNO field index can be used.
     */
    public boolean hasRecnoIndex() 
    {
        return MICROSOFT_FOXPRO_DRIVER_INSTALLED || ADVANTAGE_ODBC_DRIVER_INSTALLED;
    }    
    /**
     * Uses the optional Recno field to quickly lookup the shp offset and the record number for the list of fids.
     * Now it only works for two ODBC drivers running in Windows SO's:
     *  - Microsoft ODBC FoxPro Driver (x86).
     *  - Advantage StreamlineSQL ODBC driver (x86/x64).
     * 
     * @todo It is feasible use the 'Advantage StreamlineSQL ODBC' in Linux platforms.
     */
    public CloseableIterator<Data> queryRecnoIndex(Filter filter, int maxFeatures, CloseableIterator<Data> goodRecs) throws SQLException, ClassNotFoundException, IOException, FilterToSQLException
    {
        if (hasRecnoIndex() && filter!=null && !Filter.INCLUDE.equals(filter) && !Filter.EXCLUDE.equals(filter) && filterCapabilities.fullySupports(filter))
        {
            String shapeFileName = store.shpFiles.get(SHP);
            
            List<Integer> recnoList = queryRecnoIndex(shapeFileName, filter, store.charset, maxFeatures);
            List<Data> records = new ArrayList<Data>();
            if (recnoList==null) return goodRecs;

            if (recnoList.size()>0)
            {
                IndexFile shx = store.shpManager.openIndexFile();
    				
                try
                {
                    DataDefinition def = new DataDefinition("US-ASCII");
                    def.addField(Integer.class);
                    def.addField(Long.class);
    					
                    // Filter the already good records from a previous spatial indexing.
                    if (goodRecs!=null)
                    {
                        HashMap<Integer,Integer> recnoHash = new HashMap<Integer,Integer>();
        					
                        for (int i = 0, icount = recnoList.size(); i < icount; i++)
                        {
                            int recno = recnoList.get(i);
                            recnoHash.put(recno+1,recno);
                        }
                        while (goodRecs.hasNext())
                        {
                            Data data = goodRecs.next();
                            if (recnoHash.containsKey(data.getValue(0))) records.add(data);
                        }
                        recnoHash.clear();
                        goodRecs.close();
                    }
                    else
                    {
                        for (int i = 0, icount = recnoList.size(); i < icount; i++)
                        {
                            int recno = recnoList.get(i);
        					
                            Data data = new Data(def);
                            data.addValue(new Integer(recno + 1));
                            data.addValue(new Long(shx.getOffsetInBytes(recno)));
                            
                            records.add(data);
                        }
                    }
                    recnoList.clear();
                }
                finally
                {
                    shx.close();
                }
            }
            return new CloseableIteratorWrapper<Data>(records.iterator());
        }
        return goodRecs;
    }
}
