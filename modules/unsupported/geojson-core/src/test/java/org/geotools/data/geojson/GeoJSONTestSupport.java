/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.geojson;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class GeoJSONTestSupport {
    public static void assertEqualsIgnoreWhitespace(
            String message, String expected, String actual) {
        expected = removeWhitespace(expected);
        actual = removeWhitespace(actual);
        assertEquals(message, expected, actual);
    }

    private static String removeWhitespace(String actual) {
        if (actual == null) return "";
        StringBuffer crush = new StringBuffer(actual);
        int ch = 0;
        while (ch < crush.length()) {
            char chracter = crush.charAt(ch);
            if (Character.isWhitespace(chracter)) {
                crush.deleteCharAt(ch);
            } else {
                ch++;
            }
        }
        return crush.toString();
    }

    public static String getFileContents(File modified) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        Files.copy(modified.toPath(), baos);
        String contents = new String(baos.toByteArray(), StandardCharsets.UTF_8);
        return contents;
    }
}
