/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf.tools;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

/** Synchronized writer dumping lines to a text file, helps writing multiple lines to files */
class LogWriter implements AutoCloseable {

    BufferedWriter writer;

    public LogWriter(File file) throws IOException {
        this.writer = new BufferedWriter(new FileWriter(file, StandardCharsets.UTF_8));
    }

    public synchronized void addLines(String... lines) throws IOException {
        if (lines == null) return;
        for (String line : lines) {
            writer.write(line);
            writer.newLine();
        }
    }

    @Override
    public void close() throws Exception {
        writer.flush();
        writer.close();
    }
}
