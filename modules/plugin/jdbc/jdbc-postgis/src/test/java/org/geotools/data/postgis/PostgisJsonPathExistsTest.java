/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.junit.Assert.assertEquals;

import java.io.StringWriter;
import org.geotools.api.feature.IllegalAttributeException;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Function;
import org.geotools.data.jdbc.SQLFilterTestSupport;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.SchemaException;
import org.geotools.util.Version;
import org.junit.Before;
import org.junit.Test;

public class PostgisJsonPathExistsTest extends SQLFilterTestSupport {

    private static FilterFactory ff;

    private PostGISDialect dialect;

    PostgisFilterToSQL filterToSql;

    StringWriter writer;

    @Override
    @Before
    public void setUp() throws IllegalAttributeException, SchemaException {
        ff = CommonFactoryFinder.getFilterFactory();
        dialect = new PostGISDialect(null);
        filterToSql = new PostgisFilterToSQL(dialect, new Version("12.0.0"));
        filterToSql.setFunctionEncodingEnabled(true);
        writer = new StringWriter();
        filterToSql.setWriter(writer);

        prepareFeatures();
    }

    @Test
    public void testFunctionJsonArrayContainsJsonPathExists() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer =
                ff.function(
                        "jsonArrayContains",
                        ff.property("OPERATIONS"),
                        ff.literal("/operations"),
                        ff.literal("OP1"));
        filterToSql.encode(pointer);
        String sql = writer.toString().trim();
        assertEquals("jsonb_path_exists(OPERATIONS::jsonb, '$ ? (@.operations == \"OP1\")')", sql);
    }

    @Test
    public void testFunctionJsonArrayContainsNumberJsonPathExists() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer =
                ff.function(
                        "jsonArrayContains",
                        ff.property("OPERATIONS"),
                        ff.literal("/operations"),
                        ff.literal(1));
        filterToSql.encode(pointer);
        String sql = writer.toString().trim();
        assertEquals("jsonb_path_exists(OPERATIONS::jsonb, '$ ? (@.operations == 1)')", sql);
    }

    @Test
    public void testNestedObjectJsonArrayContainsJsonPathExists() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer =
                ff.function(
                        "jsonArrayContains",
                        ff.property("OPERATIONS"),
                        ff.literal("/operations/parameters"),
                        ff.literal("P1"));
        filterToSql.encode(pointer);
        String sql = writer.toString().trim();
        assertEquals(
                "jsonb_path_exists(OPERATIONS::jsonb, '$.operations ? (@.parameters == \"P1\")')",
                sql);
    }

    @Test
    public void testFunctionJsonArrayContainsEscapingPointerJsonPathExists() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer =
                ff.function(
                        "jsonArrayContains",
                        ff.property("OPERATIONS"),
                        ff.literal("/\"'FOO"),
                        ff.literal("OP1"));
        filterToSql.encode(pointer);
        String sql = writer.toString().trim();
        assertEquals("jsonb_path_exists(OPERATIONS::jsonb, '$ ? (@.\\\"''FOO == \"OP1\")')", sql);
    }

    @Test
    public void testFunctionJsonArrayContainsEscapingExpectedJsonPathExists() throws Exception {
        filterToSql.setFeatureType(testSchema);
        Function pointer =
                ff.function(
                        "jsonArrayContains",
                        ff.property("OPERATIONS"),
                        ff.literal("/operations"),
                        ff.literal("\"'FOO"));
        filterToSql.encode(pointer);
        String sql = writer.toString().trim();
        assertEquals(
                "jsonb_path_exists(OPERATIONS::jsonb, '$ ? (@.operations == \"\"'FOO\")')", sql);
    }
}
