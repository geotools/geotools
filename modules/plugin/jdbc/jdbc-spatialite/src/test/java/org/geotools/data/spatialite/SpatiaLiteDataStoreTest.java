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
package org.geotools.data.spatialite;

import org.geotools.jdbc.JDBCDataStoreTest;
import org.geotools.jdbc.JDBCTestSetup;

public class SpatiaLiteDataStoreTest extends JDBCDataStoreTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SpatiaLiteTestSetup();
    }
    
    @Override
    public void testCreateSchemaWithConstraints() throws Exception {
        //SQLite does not enforce length restrictions on strings
        //See FAQ (9) from http://www.sqlite.org/faq.html 
    }
    
    @Override
    public void testCreateSchema() throws Exception {
        //SQLite only has a few types, date not being one of them. So it is currently 
        // not really possible to create a feature attribute of type Date, and have that 
        // information round tripped
    }

}
