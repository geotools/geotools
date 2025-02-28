/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.data.DataAccessFactory.Param;
import org.geotools.api.data.Parameter;
import org.junit.Test;

/** Integration tests for the ParamBuilder with DuckDB datastores. */
public class ParamBuilderIntegrationTest {

    /** Test creating a param that looks like what's in DuckDBDataStoreFactory. */
    @Test
    public void testCreateFactoryParam() {
        Param inMemory = new ParamBuilder("memory")
                .type(Boolean.class)
                .description("Use in-memory DuckDB database")
                .defaultValue(Boolean.TRUE)
                .build();

        assertEquals("memory", inMemory.key);
        assertEquals(Boolean.class, inMemory.type);
        assertNotNull(inMemory.description);

        // Now create a parameter similar to what's in actual usage
        Param dbPath = new ParamBuilder("database")
                .type(String.class)
                .description("Path to DuckDB database file")
                .required(false)
                .build();

        assertEquals("database", dbPath.key);
        assertEquals(String.class, dbPath.type);
    }

    /** Test creating a param with program level metadata. */
    @Test
    public void testCreateProgramLevelParam() {
        Param dbType = new ParamBuilder("dbtype")
                .type(String.class)
                .description("Type")
                .required(true)
                .defaultValue("geoparquet")
                .programLevel()
                .build();

        assertEquals("dbtype", dbType.key);
        assertEquals(String.class, dbType.type);
        assertEquals("program", dbType.metadata.get(Parameter.LEVEL));
    }

    /** Tests creating a parameter with additional custom metadata. */
    @Test
    public void testCreateParamWithCustomMetadata() {
        // Create a param with custom metadata
        Param param = new ParamBuilder("custom")
                .type(String.class)
                .description("Custom parameter")
                .metadata("experimental", true)
                .metadata("since", "2.0")
                .build();

        assertEquals("custom", param.key);
        assertEquals(String.class, param.type);
        assertTrue((Boolean) param.metadata.get("experimental"));
        assertEquals("2.0", param.metadata.get("since"));
    }

    /** Tests creating a parameter with multiple occurrences. */
    @Test
    public void testCreateMultiOccurrenceParam() {
        // Create a param that can occur multiple times
        Param param = new ParamBuilder("include")
                .type(String.class)
                .description("Patterns to include")
                .minOccurs(0)
                .maxOccurs(10)
                .build();

        assertEquals("include", param.key);
        assertEquals(String.class, param.type);
        assertEquals(0, param.getMinOccurs());
        assertEquals(10, param.getMaxOccurs());
    }
}
