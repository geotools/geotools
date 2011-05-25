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
 *
 * @source $URL$
 */
public abstract class JDBCSkipColumnTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCSkipColumnTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    protected final void setUpData() throws Exception {
        //kill all the data
        try {
            dropSkipColumnTable();
        } catch (SQLException e) {
        }

        //create all the data
        createSkipColumnTable();
    }

    /**
     * Creates a table with the following schema:
     * <p>
     * skipcolumn( id:Integer; geom: Point; weird: unrecognizedType, name: String )
     * </p>
     * <p>
     * The table should be populated with the following data:<br>
     *  0 | Point(0,0) | null | GeoTools<br>
     * <p>
     * UnrecognizedType can be any type the datastore is not able to 
     * recognize (array, blob, whatever will force the column to be ignored)
     * </p>
     */
    protected abstract void createSkipColumnTable() throws Exception;

    /**
     * Drops the "skipcolumn" table previously created
     */
    protected abstract void dropSkipColumnTable() throws Exception;
    

}
