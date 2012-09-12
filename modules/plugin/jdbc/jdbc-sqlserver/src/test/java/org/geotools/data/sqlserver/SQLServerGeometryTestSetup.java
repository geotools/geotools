/*
 *  GeoBatch - Open Source geospatial batch processing system
 *  https://github.com/nfms4redd/nfms-geobatch
 *  Copyright (C) 2007-2012 GeoSolutions S.A.S.
 *  http://www.geo-solutions.it
 *
 *  GPLv3 + Classpath exception
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCGeometryTestSetup;

/**
 * @author DamianoG
 * 
 */
public class SQLServerGeometryTestSetup extends JDBCGeometryTestSetup {

    protected SQLServerGeometryTestSetup() {
        super(new SQLServerTestSetup());
    }

    public void setUp() throws Exception {
        super.setUp();
        runSafe("DROP TABLE GEOMETRY_COLUMNS");

        // create the geometry columns
        run("CREATE TABLE GEOMETRY_COLUMNS(" + "F_TABLE_SCHEMA VARCHAR(30) NOT NULL,"
                + "F_TABLE_NAME VARCHAR(30) NOT NULL," + "F_GEOMETRY_COLUMN VARCHAR(30) NOT NULL,"
                + "COORD_DIMENSION INTEGER," + "SRID INTEGER NOT NULL,"
                + "TYPE VARCHAR(30) NOT NULL," + ");");

    }

    @Override
    protected void dropSpatialTable(String tableName) throws Exception {
        runSafe("DELETE FROM GEOMETRY_COLUMNS WHERE F_TABLE_NAME = '" + tableName + "'");
        runSafe("DROP TABLE " + tableName);
    }

    public void setupMetadataTable(JDBCDataStore dataStore) {
        ((SQLServerDialect) dataStore.getSQLDialect()).setGeometryMetadataTable("GEOMETRY_COLUMNS");

    }

}
