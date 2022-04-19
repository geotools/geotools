/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import org.geotools.jdbc.JDBCJsonArrayContainsOnlineTest;
import org.geotools.jdbc.JDBCJsonArrayContainsTestSetup;

public class OracleJsonArrayContainsOnlineTest extends JDBCJsonArrayContainsOnlineTest {

    @Override
    protected OracleJsonArrayContainsTestSetup createTestSetup() {
        return new OracleJsonArrayContainsTestSetup();
    }

    public void testJsonArrayContainsStringClob() throws Exception {
        checkFunction("JSON_DATA_CLOB", "/arrayStr", "op2", 1);
    }

    public void testJsonArrayContainsStringVarchar() throws Exception {
        checkFunction("JSON_DATA_VARCHAR2", "/arrayStr", "op2", 1);
    }

    public void testJsonArrayContainsStringBlob() throws Exception {
        checkFunction("JSON_DATA_BLOB", "/arrayStr", "op2", 1);
    }

    public void testJsonArrayContainsNumberClob() throws Exception {
        checkFunction("JSON_DATA_CLOB", "/arrayNum", 2, 1);
    }

    public void testJsonArrayContainsNumberVarchar() throws Exception {
        checkFunction("JSON_DATA_VARCHAR2", "/arrayNum", 2, 1);
    }

    public void testJsonArrayContainsNumberBlob() throws Exception {
        checkFunction("JSON_DATA_BLOB", "/arrayNum", 2, 1);
    }
}

class OracleJsonArrayContainsTestSetup extends JDBCJsonArrayContainsTestSetup {

    protected OracleJsonArrayContainsTestSetup() {
        super(new OracleTestSetup());
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        runSafe("DROP TABLE json_data");

        run(
                "CREATE TABLE jsontest(id integer not null, json_data_clob CLOB, json_data_blob BLOB, json_data_varchar2 VARCHAR2(100),"
                        + "CONSTRAINT ensure_json_clob CHECK (json_data_clob IS JSON),"
                        + "CONSTRAINT ensure_json_blob CHECK (json_data_blob IS JSON),"
                        + "CONSTRAINT ensure_json_vchar CHECK (json_data_varchar2 IS JSON),"
                        + " CONSTRAINT json_data_pk PRIMARY KEY(id))");
        run(
                "INSERT INTO jsontest VALUES (1,"
                        + "'{ \"arrayStr\": [ \"op1\"]}', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"arrayStr\": [ \"op1\"]}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"arrayStr\": [ \"op1\" ] }')");
        run(
                "INSERT INTO jsontest VALUES (2,"
                        + "'{ \"arrayStr\": [ \"op1\", \"op2\"]}', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"arrayStr\": [ \"op1\", \"op2\"]}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"arrayStr\": [ \"op1\", \"op2\"] }')");
        run(
                "INSERT INTO jsontest VALUES (3,"
                        + "'{ \"arrayNum\": [ 1 ]}', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"arrayNum\": [ 1 ]}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"arrayStr\": [ 1 ] }')");
        run(
                "INSERT INTO jsontest VALUES (4,"
                        + "'{ \"arrayNum\": [ 1, 2 ]}', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"arrayNum\": [ 1, 2 ]}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"arrayNum\": [ 1, 2] }')");
        run(
                "INSERT INTO jsontest VALUES (5,"
                        + "' { \"onNestedObj\": { \"arrayNum\": [ 1, 2 ]} }', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"onNestedObj\": { \"arrayNum\": [ 1, 2 ]} }'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"onNestedObj\": { \"arrayNum\": [ 1, 2] } }')");
    }
}
