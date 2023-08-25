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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

public class OracleJsonOnlineTest extends JDBCTestSupport {

    private OracleJsonOnlineTestSetup testSetup;
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    @Override
    protected OracleJsonOnlineTestSetup createTestSetup() {
        testSetup = new OracleJsonOnlineTestSetup();
        return testSetup;
    }

    @Test
    public void testJsonArrayContainsStringClob() throws Exception {
        checkJsonArrayFunction("JSON_DATA_CLOB", "/arrayStr", "op2", 1);
    }

    @Test
    public void testJsonArrayContainsStringVarchar() throws Exception {
        checkJsonArrayFunction("JSON_DATA_VARCHAR2", "/arrayStr", "op2", 1);
    }

    @Test
    public void testJsonArrayContainsStringBlob() throws Exception {
        checkJsonArrayFunction("JSON_DATA_BLOB", "/arrayStr", "op2", 1);
    }

    @Test
    public void testJsonArrayContainsNumberClob() throws Exception {
        checkJsonArrayFunction("JSON_DATA_CLOB", "/arrayNum", 2, 1);
    }

    @Test
    public void testJsonArrayContainsNumberVarchar() throws Exception {
        checkJsonArrayFunction("JSON_DATA_VARCHAR2", "/arrayNum", 2, 1);
    }

    @Test
    public void testJsonArrayContainsNumberBlob() throws Exception {
        checkJsonArrayFunction("JSON_DATA_BLOB", "/arrayNum", 2, 1);
    }

    @Test
    public void testJsonPointerFunction2Matches() throws Exception {
        checkJsonPointerFunction("JSON_DATA_CLOB", "/character/name", "Mario", 2);
        checkJsonPointerFunction("JSON_DATA_VARCHAR2", "/character/name", "Mario", 2);
        checkJsonPointerFunction("JSON_DATA_BLOB", "/character/name", "Mario", 2);
    }

    @Test
    public void testJsonPointerFunction1Match() throws Exception {
        checkJsonPointerFunction("JSON_DATA_CLOB", "/character/name", "Luigi", 1);
        checkJsonPointerFunction("JSON_DATA_VARCHAR2", "/character/name", "Luigi", 1);
        checkJsonPointerFunction("JSON_DATA_BLOB", "/character/name", "Luigi", 1);
    }

    @Test
    public void testJsonPointerFunctionNoMatches() throws Exception {
        checkJsonPointerFunction("JSON_DATA_CLOB", "/character/name", "Bowser", 0);
        checkJsonPointerFunction("JSON_DATA_VARCHAR2", "/character/name", "Bowser", 0);
        checkJsonPointerFunction("JSON_DATA_BLOB", "/character/name", "Bowser", 0);
    }

    @Test
    public void testJsonPointerFunctionOnArray2ElementStr() throws Exception {
        checkJsonPointerFunction("JSON_DATA_CLOB", "/arrayStr/1", "op2", 1);
        checkJsonPointerFunction("JSON_DATA_VARCHAR2", "/arrayStr/1", "op2", 1);
        checkJsonPointerFunction("JSON_DATA_BLOB", "/arrayStr/1", "op2", 1);
    }

    @Test
    public void testJsonPointerFunctionOnArray1ElementStr() throws Exception {
        checkJsonPointerFunction("JSON_DATA_CLOB", "/arrayStr/0", "op1", 2);
        checkJsonPointerFunction("JSON_DATA_VARCHAR2", "/arrayStr/0", "op1", 2);
        checkJsonPointerFunction("JSON_DATA_BLOB", "/arrayStr/0", "op1", 2);
    }

    @Test
    public void testJsonPointerFunctionOnArray2ElementNum() throws Exception {
        checkJsonPointerFunction("JSON_DATA_CLOB", "/arrayNum/1", 2, 1);
        checkJsonPointerFunction("JSON_DATA_VARCHAR2", "/arrayNum/1", 2, 1);
        checkJsonPointerFunction("JSON_DATA_BLOB", "/arrayNum/1", 2, 1);
    }

    @Test
    public void testJsonPointerFunctionOnArray1ElementNum() throws Exception {
        checkJsonPointerFunction("JSON_DATA_CLOB", "/arrayNum/0", 1, 2);
        checkJsonPointerFunction("JSON_DATA_VARCHAR2", "/arrayNum/0", 1, 2);
        checkJsonPointerFunction("JSON_DATA_BLOB", "/arrayNum/0", 1, 2);
    }

    private void checkJsonArrayFunction(
            String columnName, String pointer, Object expected, int countResult) throws Exception {

        ContentFeatureSource featureSource = dataStore.getFeatureSource(tname("json_data"));
        Function function =
                ff.function(
                        "jsonArrayContains",
                        ff.property(columnName),
                        ff.literal(pointer),
                        ff.literal(expected));
        Filter filter = ff.equals(function, ff.literal(true));
        try (SimpleFeatureIterator iterator = featureSource.getFeatures(filter).features()) {
            List<SimpleFeature> features = new ArrayList<>();
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
            assertEquals(features.size(), countResult);
        }
    }

    private void checkJsonPointerFunction(
            String columnName, String pointer, Object expected, int countResult) throws Exception {
        ContentFeatureSource featureSource = dataStore.getFeatureSource(tname("json_data"));
        Function function =
                ff.function("jsonPointer", ff.property(columnName), ff.literal(pointer));
        Filter filter = ff.equals(function, ff.literal(expected));
        try (SimpleFeatureIterator iterator = featureSource.getFeatures(filter).features()) {
            List<SimpleFeature> features = new ArrayList<>();
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
            assertEquals(countResult, features.size());
        }
    }
}

class OracleJsonOnlineTestSetup extends JDBCDelegatingTestSetup {

    protected OracleJsonOnlineTestSetup() {
        super(new OracleTestSetup());
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        runSafe("DROP TABLE json_data");

        run(
                "CREATE TABLE json_data(id integer not null, json_data_clob CLOB, json_data_blob BLOB, json_data_varchar2 VARCHAR2(100),"
                        + "CONSTRAINT ensure_json_clob CHECK (json_data_clob IS JSON),"
                        + "CONSTRAINT ensure_json_blob CHECK (json_data_blob IS JSON),"
                        + "CONSTRAINT ensure_json_vchar CHECK (json_data_varchar2 IS JSON),"
                        + " CONSTRAINT json_data_pk PRIMARY KEY(id))");
        run(
                "INSERT INTO json_data VALUES (1,"
                        + "'{ \"arrayStr\": [ \"op1\"]}', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"arrayStr\": [ \"op1\"]}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"arrayStr\": [ \"op1\" ] }')");
        run(
                "INSERT INTO json_data VALUES (2,"
                        + "'{ \"arrayStr\": [ \"op1\", \"op2\"]}', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"arrayStr\": [ \"op1\", \"op2\"]}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"arrayStr\": [ \"op1\", \"op2\"] }')");
        run(
                "INSERT INTO json_data VALUES (3,"
                        + "'{ \"arrayNum\": [ 1 ]}', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"arrayNum\": [ 1 ]}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"arrayNum\": [ 1 ] }')");
        run(
                "INSERT INTO json_data VALUES (4,"
                        + "'{ \"arrayNum\": [ 1, 2 ]}', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"arrayNum\": [ 1, 2 ]}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"arrayNum\": [ 1, 2] }')");
        run(
                "INSERT INTO json_data VALUES (5,"
                        + "' { \"onNestedObj\": { \"arrayNum\": [ 1, 2 ]} }', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"onNestedObj\": { \"arrayNum\": [ 1, 2 ]} }'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"onNestedObj\": { \"arrayNum\": [ 1, 2] } }')");
        run(
                "INSERT INTO json_data VALUES (6,"
                        + "' { \"character\": { \"name\": \"Mario\"} }', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"character\": { \"name\": \"Mario\"}}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"character\": { \"name\": \"Mario\"} }')");
        run(
                "INSERT INTO json_data VALUES (7,"
                        + "' { \"character\": { \"name\": \"Luigi\"} }', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"character\": { \"name\": \"Luigi\"}}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"character\": { \"name\": \"Luigi\"} }')");
        run(
                "INSERT INTO json_data VALUES (8,"
                        + "' { \"character\": { \"name\": \"Mario\"} }', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"character\": { \"name\": \"Mario\"}}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"character\": { \"name\": \"Mario\"} }')");
    }
}
