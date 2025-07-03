/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg.hsql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;

/**
 * Reads a multiline SQL file extracting each command separately. Skips empty lines, assumes comments start with "--"
 * and are on their own line
 */
public class SqlScriptReader {
    boolean fetched = true;
    StringBuilder builder = new StringBuilder();
    BufferedReader reader;

    public SqlScriptReader(Reader reader) {
        this.reader = new BufferedReader(reader);
    }

    public boolean hasNext() throws IOException {
        // do we have an un-fetched command?
        if (!fetched) {
            return builder.length() > 0;
        }

        builder.setLength(0);
        String line = null;
        boolean insideString = false;
        while ((line = reader.readLine()) != null) {
            line = line.trim();

            // Skip empty and comment lines
            if (!"".equals(line) && !line.startsWith("--")) {
                // Handle string literals and prevent line splits inside strings
                // EPSG 11.0.31 version contained a value like this, on 2 separated lines:
                // 'National Land Survey of Finland;
                //        http://www.maanmittauslaitos.fi'
                if (line.contains("'")) {
                    int quoteCount = line.length() - line.replace("'", "").length();
                    if (quoteCount % 2 != 0) { // Odd number of quotes means we are inside a string
                        insideString = !insideString; // Toggle the insideString flag
                    }
                }

                // If we're not inside a string, add a newline at the end of the line
                if (!insideString && line.endsWith(";")) {
                    builder.append(line).append("\n");
                    fetched = false;
                    break;
                } else {
                    // Add the line without breaking it
                    builder.append(line);
                }
            }
        }

        if (line == null && builder.length() > 0) {
            throw new IOException("The file ends with a non ; terminated command");
        }

        return line != null;
    }

    public String next() throws IOException {
        if (fetched) throw new IOException("hasNext was not called, or was called and it returned false");

        fetched = true;
        return builder.toString();
    }

    public void dispose() {
        try {
            reader.close();
        } catch (IOException e) {
            // never mind
        }
    }
}
