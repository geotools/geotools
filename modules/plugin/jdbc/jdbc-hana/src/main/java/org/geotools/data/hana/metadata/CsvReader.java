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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple CSV file reader tailored for reading the embedded HANA spatial metadata.
 *
 * @author Stefan Uhrig, SAP SE
 */
class CsvReader {

    public CsvReader(InputStream is) {
        reader = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
    }

    private BufferedReader reader;

    public List<String> readNextRow() throws IOException {
        String line = reader.readLine();
        if (line == null) {
            return null;
        }

        Scanner scanner = new Scanner(line);
        ArrayList<String> ret = new ArrayList<>();
        while (true) {
            String entry = scanner.getNextEntry();
            if (entry == null) {
                break;
            }
            ret.add(entry);
        }
        return ret;
    }

    private static class Scanner {

        public Scanner(String line) {
            this.line = line;
            this.position = 0;
        }

        private String line;

        private int position;

        public String getNextEntry() {
            if (position == line.length()) {
                return null;
            }
            if (position > 0) {
                if (line.charAt(position) != ',') {
                    throw new AssertionError();
                }
                ++position;
            }
            if (position == line.length()) {
                return "";
            }

            StringBuilder sb = new StringBuilder();

            boolean inQuotes = false;
            if (line.charAt(position) == '"') {
                inQuotes = true;
                ++position;
            }
            while (position < line.length()) {
                char c = line.charAt(position);
                if (!inQuotes && c == ',') {
                    break;
                }
                if (inQuotes && c == '"') {
                    ++position;
                    if (position == line.length()) {
                        inQuotes = false;
                        break;
                    }
                    if (line.charAt(position) != '"') {
                        inQuotes = false;
                        break;
                    }
                    sb.append('"');
                    ++position;
                } else {
                    sb.append(c);
                    ++position;
                }
            }
            if (inQuotes) {
                throw new RuntimeException("CSV-file is malformed");
            }
            if (position < line.length() && line.charAt(position) != ',') {
                throw new RuntimeException("CSV-file is malformed");
            }
            return sb.toString();
        }
    }
}
