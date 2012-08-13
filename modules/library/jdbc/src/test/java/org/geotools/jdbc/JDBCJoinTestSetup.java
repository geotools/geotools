/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

public abstract class JDBCJoinTestSetup extends JDBCDelegatingTestSetup {

    protected JDBCJoinTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected final void setUpData() throws Exception {
        delegate.setUpData();
        
        //kill all the data
        try {
            dropJoinTable();
        } catch (SQLException e) {
        }

        //create all the data
        createJoinTable();
    }

    /**
     * Creates two tables with the following schema:
     * <p>
     * ftjoin( id:Integer; name:String; geom:POLYGON; join1intProperty:Integer )
     * </p>
     * The table should be populated with the following data:
     * <pre>
     * 0 | 'zero' | POLYGON ((-0.1 -0.1, -0.1 0.1, 0.1 0.1, 0.1 -0.1, -0.1 -0.1)) | 0
     * 1 | 'one' | POLYGON ((-1.1 -1.1, -1.1 1.1, 1.1 1.1, 1.1 -1.1, -1.1 -1.1)) | 1
     * 2 | 'two' | POLYGON ((-10 -10, -10 10, 10 10, 10 -10, -10 -10)) | 2
     * 3 | 'three' | NULL | 3
     * </pre>
     * <p>
     * ftjoin2( id:Integer; join2intProperty:Integer; stringProperty2:String )
     * </p>
     * The table should be populated with the following data:
     * <pre>
     * 0 | 0 | '2nd zero'
     * 1 | 1 | '2nd one'
     * 2 | 2 | '2nd two'
     * 3 | 3 | '2nd three'
     * </pre>
     */
    protected abstract void createJoinTable() throws Exception;

    protected abstract void dropJoinTable() throws Exception;

}
