/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.sql.SQLException;

/**
 *  
 * @author Andrea Aime
 *
 * @source $URL$
 */
public abstract class JDBCGeometrylessTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCGeometrylessTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    protected final void setUpData() throws Exception {
        //kill all the data
        try {
            dropPersonTable();
        } catch (SQLException e) {
        }

        try {
            dropZipCodeTable();
        } catch (SQLException e) {
        }

        //create all the data
        createPersonTable();
    }

    /**
     * Creates a table with the following schema:
     * <p>
     * person( id:Integer; name:String; age:Integer )
     * </p>
     * <p>
     * The table should be populated with the following data:
     *  0 | "Paul" | 32
     *  1 | "Anne" | 40
     * </p>
     */
    protected abstract void createPersonTable() throws Exception;

    /**
     * Drops the "person" table previously created
     */
    protected abstract void dropPersonTable() throws Exception;
    
    /**
     * Drops the "zipcode" table that has been created during the test
     */
    protected abstract void dropZipCodeTable() throws Exception;

    

}
