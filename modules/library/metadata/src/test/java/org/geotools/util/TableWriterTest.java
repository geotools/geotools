/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.util;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;
import org.junit.*;

/**
 * Minimal testing for TableWriter class.
 *
 * @author Frank Warmerdam
 */
public final class TableWriterTest {
    private static final String LINE_SEPARATOR = System.getProperty("line.separator", "\n");

    /** Format a very simple table as shown in the example. */
    @Test
    public void testExample() throws IOException {
        String expectedTable =
                "Prénom      Nom    "
                        + LINE_SEPARATOR
                        + "-------------------"
                        + LINE_SEPARATOR
                        + "Idéphonse   Laporte"
                        + LINE_SEPARATOR
                        + "Sarah       Coursi "
                        + LINE_SEPARATOR
                        + "Yvan        Dubois "
                        + LINE_SEPARATOR;

        StringWriter writer = new StringWriter();
        TableWriter out = new TableWriter(writer, 3);
        out.write("Prénom\tNom" + LINE_SEPARATOR);
        out.nextLine('-');
        out.write(
                "Idéphonse\tLaporte"
                        + LINE_SEPARATOR
                        + "Sarah\tCoursi"
                        + LINE_SEPARATOR
                        + "Yvan\tDubois");
        out.flush();
        out.close();

        assertEquals(expectedTable, writer.toString());
    }

    /**
     * Ensure that toString() gets the whole table even if the caller didn't explicitly mark the end
     * of the last column value.
     */
    @Test
    public void testToStringWithoutFlush() throws IOException {
        final TableWriter table = new TableWriter(null, " ");
        table.write("Source Point");
        table.write(':');

        table.nextColumn();
        table.write("1.234");

        table.nextLine();
        table.write("Target Point");
        table.write(':');
        table.nextColumn();
        table.write("2.345");

        assertEquals(
                "Source Point: 1.234" + LINE_SEPARATOR + "Target Point: 2.345" + LINE_SEPARATOR,
                table.toString());
        table.close();
    }
}
