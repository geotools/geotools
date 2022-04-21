/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCDelegatingTestSetup;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

public class OracleJsonArrayContainsOnlineTest extends JDBCTestSupport {

    private OracleJsonArrayContainsTestSetup testSetup;
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    @Override
    protected JDBCJsonArrayContainsTestSetup createTestSetup() {
        testSetup = new OracleJsonArrayContainsTestSetup();
        return testSetup;
    }

    public void testJsonArrayContainsStringClob() throws Exception {

        ContentFeatureSource featureSource =
                dataStore.getFeatureSource(tname("json_data"));
        Function function = ff.function(
                "jsonArrayContains",
                ff.property("JSON_DATA_CLOB"),
                ff.literal("/arrayStr"),
                ff.literal("op2"));
        Filter filter = ff.equals(function, ff.literal(true));
        try (SimpleFeatureIterator iterator = featureSource.getFeatures(filter).features()) {
            List<SimpleFeature> features = new ArrayList<>();
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
            // check that we retrieved the necessary features
            assertEquals(features.size(), 1);
        }
    }

    public void testJsonArrayContainsStringVarchar() throws Exception {

        ContentFeatureSource featureSource =
                dataStore.getFeatureSource(tname("json_data"));
        Function function = ff.function(
                "jsonArrayContains",
                ff.property("JSON_DATA_VARCHAR2"),
                ff.literal("/arrayStr"),
                ff.literal("op2"));
        Filter filter = ff.equals(function, ff.literal(true));
        try (SimpleFeatureIterator iterator = featureSource.getFeatures(filter).features()) {
            List<SimpleFeature> features = new ArrayList<>();
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
            // check that we retrieved the necessary features
            assertEquals(features.size(), 1);
        }
    }

    public void testJsonArrayContainsStringBlob() throws Exception {

        ContentFeatureSource featureSource =
                dataStore.getFeatureSource(tname("json_data"));
        Function function = ff.function(
                "jsonArrayContains",
                ff.property("JSON_DATA_BLOB"),
                ff.literal("/arrayStr"),
                ff.literal("op2"));
        Filter filter = ff.equals(function, ff.literal(true));
        try (SimpleFeatureIterator iterator = featureSource.getFeatures(filter).features()) {
            List<SimpleFeature> features = new ArrayList<>();
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
            // check that we retrieved the necessary features
            assertEquals(features.size(), 1);
        }
    }
}

abstract class JDBCJsonArrayContainsTestSetup extends JDBCDelegatingTestSetup {
    protected JDBCJsonArrayContainsTestSetup(JDBCTestSetup delegate) {
        super(delegate);
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
                "CREATE TABLE json_data(id integer not null, json_data_clob CLOB, json_data_blob BLOB, json_data_varchar2 VARCHAR2(100),"
                        + "CONSTRAINT ensure_json_clob CHECK (json_data_clob IS JSON),"
                        + "CONSTRAINT ensure_json_blob CHECK (json_data_blob IS JSON),"
                        + "CONSTRAINT ensure_json_vchar CHECK (json_data_varchar2 IS JSON)," +
                        " CONSTRAINT json_data_pk PRIMARY KEY(id))");
        run(
                "INSERT INTO json_data VALUES (1,"
                        + "'{ \"arrayStr\": [ \"op1\"]}', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"arrayStr\": [ \"op1\"]}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"arrayStr\": [ \"op1\", \"op2\"] }')");
        run(
                "INSERT INTO json_data VALUES (2,"
                        + "'{ \"arrayStr\": [ \"op1\", \"op2\"]}', "
                        + "UTL_RAW.convert(UTL_RAW.cast_to_raw('{ \"arrayStr\": [ \"op1\", \"op2\"]}'), 'AL32UTF8', 'WE8MSWIN1252'),"
                        + "'{ \"arrayStr\": [ \"op1\", \"op2\"] }')");
    }
}