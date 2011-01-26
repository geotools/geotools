/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

public abstract class JDBCUDTTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCUDTTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void setUpData() throws Exception {
        try {
            dropUdtTable();
        }
        catch(Exception e) {}
        
        createUdtTable();
    }
    

    /**
     * Creates a table name "udt" with the following schema:
     * <p>
     * udt( id:Integer PRIMARY KEY; ut: String[] )
     * </p>
     * <p>
     * The "ut" column is a user defined column that is text based but restricts input to 
     * 2 numbers follows by two letters. Example: '12ab', '34CD'. 
     * <p>
     * <p>
     * The table should be populated with the following data:
     * <pre>
     * 0 | '12ab'
     * </pre>
     * </p>
     */
    protected abstract void createUdtTable() throws Exception;
    
    /**
     * Drops the "udt" table.
     */
    protected abstract void dropUdtTable() throws Exception;
}
