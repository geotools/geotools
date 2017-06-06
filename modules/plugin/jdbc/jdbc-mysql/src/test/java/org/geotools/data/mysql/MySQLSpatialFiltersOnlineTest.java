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
package org.geotools.data.mysql;

import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.geotools.jdbc.JDBCSpatialFiltersOnlineTest;

/**
 * 
 *
 * @source $URL$
 */
public class MySQLSpatialFiltersOnlineTest extends JDBCSpatialFiltersOnlineTest {

	
    @Override
	protected void connect() throws Exception {
		super.connect();
		if (dialect instanceof MySQLDialect) {
			System.out.println("MySQLDialect enhanced spatial support is:" + ((MySQLDialect)dialect).getUsePreciseSpatialOps());
		} else if (dialect instanceof MySQLDialectBasic) {
			System.out.println("MySQLDialectBasic enhanced spatial support is:" + ((MySQLDialectBasic)dialect).getUsePreciseSpatialOps());
		}
	}

	@Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new MySQLDataStoreAPITestSetup();
    }

    @Override
    public void testBboxFilter() throws Exception {
        //super.testBboxFilter();
    }
    
    @Override
    public void testBboxFilterDefault() throws Exception {
        //super.testBboxFilterDefault();
    }
    
    @Override
    public void testCrossesFilter() throws Exception {
        //super.testCrossesFilter();
    }
}
