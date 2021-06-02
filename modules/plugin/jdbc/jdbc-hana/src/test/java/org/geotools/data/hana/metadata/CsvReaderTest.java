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

import static java.nio.charset.StandardCharsets.UTF_8;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import org.junit.Assert;
import org.junit.Test;

/** @author Stefan Uhrig, SAP SE */
public class CsvReaderTest {

    @Test
    public void testValidCsv() throws IOException {
        String csv = "a,\"b\",\"c\"\"d\"\n\",\",\u00e4,";
        InputStream is = new ByteArrayInputStream(csv.getBytes(UTF_8));
        CsvReader reader = new CsvReader(is);

        List<String> row = reader.readNextRow();
        Assert.assertEquals(3, row.size());
        Assert.assertEquals("a", row.get(0));
        Assert.assertEquals("b", row.get(1));
        Assert.assertEquals("c\"d", row.get(2));

        row = reader.readNextRow();
        Assert.assertEquals(3, row.size());
        Assert.assertEquals(",", row.get(0));
        Assert.assertEquals("\u00e4", row.get(1));
        Assert.assertEquals("", row.get(2));

        row = reader.readNextRow();
        Assert.assertNull(row);
    }

    @Test
    public void testNoMatchingQuote() throws IOException {
        String csv = "\"abc";
        InputStream is = new ByteArrayInputStream(csv.getBytes(UTF_8));
        CsvReader reader = new CsvReader(is);
        try {
            reader.readNextRow();
            Assert.fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
        }
    }

    @Test
    public void testPartiallyQuoted() throws IOException {
        String csv = "\"abc\"xyz";
        InputStream is = new ByteArrayInputStream(csv.getBytes(UTF_8));
        CsvReader reader = new CsvReader(is);
        try {
            reader.readNextRow();
            Assert.fail("Expected an exception to be thrown");
        } catch (RuntimeException e) {
        }
    }
}
