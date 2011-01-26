/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *    (C) Copyright IBM Corporation, 2005. All rights reserved.
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
 *
 */
package org.geotools.data.db2;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;

import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapper;
import org.geotools.data.jdbc.fidmapper.FIDMapperFactory;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;


/**
 * Exercise DB2FIDMapperFactory.
 *
 * @author David Adler - IBM Corporation
 * @source $URL$
 */
public class DB2FIDMapperFactoryOnlineTest extends AbstractDB2OnlineTestCase {
    public void testMappers() throws Exception {
        String catalog = null;
        String schema = "Test";
        String tableName = null;
        Connection conn = null;
        FIDMapper fm;
        String wrapperDesc = null;
        FIDMapperFactory fmFact = new DB2FIDMapperFactory("Test");

        DB2DataStore ds = getDataStore();
        FIDMapper fm1 = ds.getFIDMapper("FIDVCHARPRIKEY");
        FIDMapper fm2 = new TypedFIDMapper(new BasicFIDMapper("IDCOL", 12), "FIDVCHARPRIKEY");
        fm1.equals(fm2);
//        assertEquals(new TypedFIDMapper(new BasicFIDMapper("IDCOL", 12), "FIDVCHARPRIKEY"), ds.getFIDMapper("FIDVCHARPRIKEY"));

        conn = getConnection();
        tableName = "FIDAUTOINC";
        fm = fmFact.getMapper(catalog, schema, tableName, conn);
        wrapperDesc = fm.toString();
        fm2 = ds.getFIDMapper(tableName);
        assertEquals(tableName, wrapperDesc,
            "Wrapped:class org.geotools.data.db2.DB2AutoIncrementFIDMapper:1:IDCOL:4:0:0:false:true:");

        tableName = "FIDCHARPRIKEY";
        fm = fmFact.getMapper(catalog, schema, tableName, conn);
        wrapperDesc = fm.toString();
        assertEquals(tableName, wrapperDesc,
            "Wrapped:class org.geotools.data.jdbc.fidmapper.BasicFIDMapper:1:IDCOL:12:15:0:true:false:");

        tableName = "FIDNOPRIKEY";
        fm = fmFact.getMapper(catalog, schema, tableName, conn);
        wrapperDesc = fm.toString();
        assertEquals(tableName, wrapperDesc,
            "Wrapped:class org.geotools.data.db2.DB2NullFIDMapper:0::false:false:");

        tableName = "FIDINTPRIKEY";
        fm = fmFact.getMapper(catalog, schema, tableName, conn);
        wrapperDesc = fm.toString();
        assertEquals(tableName, wrapperDesc,
            "Wrapped:class org.geotools.data.jdbc.fidmapper.MaxIncFIDMapper:1:IDCOL:4:0:0:true:false:");

        tableName = "FIDVCHARPRIKEY";
        fm = fmFact.getMapper(catalog, schema, tableName, conn);
        wrapperDesc = fm.toString();
        assertEquals(tableName, wrapperDesc,
            "Wrapped:class org.geotools.data.jdbc.fidmapper.BasicFIDMapper:1:IDCOL:12:17:0:true:false:");

        tableName = "FIDMCOLPRIKEY";
        fm = fmFact.getMapper(catalog, schema, tableName, conn);
        wrapperDesc = fm.toString();
        assertEquals(tableName, wrapperDesc,
            "Wrapped:class org.geotools.data.jdbc.fidmapper.MultiColumnFIDMapper:2:IDCOL1:1:11:0:true:false:");

        try {
            tableName = "NoTable";
            fm = fmFact.getMapper(catalog, schema, tableName, conn);
            fail("Didn't get exception on invalid tableName");
        } catch (SchemaNotFoundException e) {
            assertEquals("Unexpected exception", e.getMessage(),
                "Feature type could not be found for NoTable");
        } catch (IOException e) {
            fail("Unexpected exception: " + e);
        }

        try {
            conn.close();
        } catch (SQLException e1) {
            e1.printStackTrace();
        }
    }

    String toString(FIDMapper fm) {
        String mapperName = ((TypedFIDMapper) fm).getWrappedMapper().getClass()
                             .toString();
        String colInfo = "";

        if (fm.getColumnCount() > 0) {
            colInfo = fm.getColumnName(0) + ":" + fm.getColumnType(0) + ":"
                + fm.getColumnSize(0) + ":" + fm.getColumnDecimalDigits(0);
        }

        String s = mapperName + ":" + fm.getColumnCount() + ":" + colInfo + ":"
            + fm.returnFIDColumnsAsAttributes() + ":"
            + fm.hasAutoIncrementColumns() + ":" + "";

        return s;
    }
}
