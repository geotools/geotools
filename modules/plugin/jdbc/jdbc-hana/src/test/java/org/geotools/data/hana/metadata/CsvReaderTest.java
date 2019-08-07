/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana.metadata;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import junit.framework.TestCase;

/** @author Stefan Uhrig, SAP SE */
public class CsvReaderTest extends TestCase {

    public void testValidCsv() throws IOException {
        String csv = "a,\"b\",\"c\"\"d\"\n\",\",\u00e4,";
        InputStream is = new ByteArrayInputStream(csv.getBytes("UTF-8"));
        CsvReader reader = new CsvReader(is);
        List<String> row;

        row = reader.readNextRow();
        assertEquals(3, row.size());
        assertEquals("a", row.get(0));
        assertEquals("b", row.get(1));
        assertEquals("c\"d", row.get(2));

        row = reader.readNextRow();
        assertEquals(3, row.size());
        assertEquals(",", row.get(0));
        assertEquals("\u00e4", row.get(1));
        assertEquals("", row.get(2));

        row = reader.readNextRow();
        assertNull(row);
    }

    public void testNoMatchingQuote() throws IOException {
        String csv = "\"abc";
        InputStream is = new ByteArrayInputStream(csv.getBytes("UTF-8"));
        CsvReader reader = new CsvReader(is);
        try {
            reader.readNextRow();
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
        }
    }

    public void testPartiallyQuoted() throws IOException {
        String csv = "\"abc\"xyz";
        InputStream is = new ByteArrayInputStream(csv.getBytes("UTF-8"));
        CsvReader reader = new CsvReader(is);
        try {
            reader.readNextRow();
            fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
        }
    }
}
