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

package org.geotools.data.sqlserver;

import static org.junit.Assume.assumeTrue;

import java.util.logging.Logger;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCJNDIDataSourceOnlineTest;
import org.geotools.jdbc.JDBCJNDIDataStoreFactory;
import org.geotools.jdbc.JDBCJNDITestSetup;
import org.geotools.util.logging.Logging;
import org.junit.Before;

public class SQLServerJNDIDataSourceOnlineTest extends JDBCJNDIDataSourceOnlineTest {

    static final Logger LOGGER = Logging.getLogger(SQLServerJNDIDataSourceOnlineTest.class);

    @Override
    protected JDBCJNDITestSetup createTestSetup() {
        return new JDBCJNDITestSetup(new SQLServerTestSetup());
    }

    @Override
    protected JDBCJNDIDataStoreFactory getJNDIStoreFactory() {
        return new SQLServerJNDIDataStoreFactory();
    }

    @Override
    protected JDBCDataStoreFactory getDataStoreFactory() {
        return new SQLServerDataStoreFactory();
    }

    @Before
    public void checkMicrosoftDriverAvailable() {
        assumeTrue(isMicrosoftDriverAvailable());
    }

    /** Returns true if the Microsoft JDBC Driver is available in the classpath, false otherwise */
    private boolean isMicrosoftDriverAvailable() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return true;
        } catch (ClassNotFoundException e) {
            LOGGER.info("Skipping SQLServerJNDIDataSourceOnlineTest test as the Microsoft driver is not available");
            return false;
        }
    }
}
