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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import org.geotools.api.data.DataAccessFactory;
import org.geotools.api.data.Parameter;
import org.junit.Test;

/** Tests for the ParamBuilder class. */
public class ParamBuilderTest {

    @Test
    public void testBasicParam() {
        DataAccessFactory.Param param = new ParamBuilder("test")
                .type(String.class)
                .description("Test parameter")
                .build();

        assertEquals("test", param.key);
        assertEquals(String.class, param.type);
        assertEquals("Test parameter", param.description.toString());
        assertFalse(param.required);
        assertEquals(0, param.getMinOccurs());
        assertEquals(1, param.getMaxOccurs());
    }

    @Test
    public void testRequiredParam() {
        DataAccessFactory.Param param = new ParamBuilder("test")
                .type(Integer.class)
                .description("Test parameter")
                .required(true)
                .build();

        assertEquals("test", param.key);
        assertEquals(Integer.class, param.type);
        assertTrue(param.required);
    }

    @Test
    public void testDefaultValue() {
        // We're only testing that the builder passes the default value to the constructor
        // Not testing lookUp functionality which belongs to the Param class, not our builder
        DataAccessFactory.Param param = new ParamBuilder("test")
                .type(Boolean.class)
                .description("Test parameter")
                .defaultValue(Boolean.TRUE)
                .build();

        assertEquals("test", param.key);
        assertEquals(Boolean.class, param.type);
    }

    @Test
    public void testMetadata() {
        DataAccessFactory.Param param = new ParamBuilder("test")
                .type(String.class)
                .description("Test parameter")
                .metadata("key1", "value1")
                .metadata("key2", 123)
                .build();

        assertEquals("test", param.key);
        assertEquals(String.class, param.type);
        assertEquals("value1", param.metadata.get("key1"));
        assertEquals(123, param.metadata.get("key2"));
    }

    @Test
    public void testMetadataMap() {
        Map<String, Object> metadata = Map.of("key1", "value1", "key2", 123);

        DataAccessFactory.Param param = new ParamBuilder("test")
                .type(String.class)
                .description("Test parameter")
                .metadata(metadata)
                .build();

        assertEquals("test", param.key);
        assertEquals(String.class, param.type);
        assertEquals("value1", param.metadata.get("key1"));
        assertEquals(123, param.metadata.get("key2"));
    }

    @Test
    public void testLevelShortcuts() {
        DataAccessFactory.Param userParam =
                new ParamBuilder("user").type(String.class).userLevel().build();

        DataAccessFactory.Param advancedParam =
                new ParamBuilder("advanced").type(String.class).advancedLevel().build();

        DataAccessFactory.Param programParam =
                new ParamBuilder("program").type(String.class).programLevel().build();

        assertEquals("user", userParam.metadata.get(Parameter.LEVEL));
        assertEquals("advanced", advancedParam.metadata.get(Parameter.LEVEL));
        assertEquals("program", programParam.metadata.get(Parameter.LEVEL));
    }

    @Test
    public void testOccurrences() {
        DataAccessFactory.Param param = new ParamBuilder("test")
                .type(String.class)
                .description("Test parameter")
                .minOccurs(2)
                .maxOccurs(5)
                .build();

        assertEquals("test", param.key);
        assertEquals(String.class, param.type);
        assertEquals(2, param.getMinOccurs());
        assertEquals(5, param.getMaxOccurs());
    }

    @Test(expected = IllegalStateException.class)
    public void testMissingType() {
        new ParamBuilder("test").description("Test parameter").build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNegativeMinOccurs() {
        new ParamBuilder("test")
                .type(String.class)
                .description("Test parameter")
                .minOccurs(-1)
                .build();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidMaxOccurs() {
        new ParamBuilder("test")
                .type(String.class)
                .description("Test parameter")
                .minOccurs(5)
                .maxOccurs(3)
                .build();
    }
}
