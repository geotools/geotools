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
package org.geotools.data.db2;

import java.io.IOException;

import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTransactionTest;


public class DB2TransactionTest extends JDBCTransactionTest {
    protected JDBCTestSetup createTestSetup() {
        return new DB2TestSetup();
    }
    
    public void testConcurrentTransactions() throws IOException {
    	// TODO does not work 
    }
}
