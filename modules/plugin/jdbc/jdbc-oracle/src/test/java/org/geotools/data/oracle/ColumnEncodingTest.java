/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test for GEOT-5231 Oracle Dialect doesn't encode column names correctly.
 *
 * @author Ian Turton
 */
public class ColumnEncodingTest {

    @Test
    public void test() {
        StringBuffer buffer = new StringBuffer();
        OracleDialect dialect = new OracleDialect(null);
        dialect.encodeColumnName("name", buffer);
        assertEquals("NAME", buffer.toString());
    }

    /** test for GEOT-5176 Oracle can't handle spaces in column names */
    @Test
    public void testspaces() {
        StringBuffer buffer = new StringBuffer();
        OracleDialect dialect = new OracleDialect(null);
        dialect.encodeColumnName("name with space", buffer);
        assertEquals("\"NAME WITH SPACE\"", buffer.toString());
    }
}
