package org.geotools.data.geojson.store;

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
                continue;
            }
            ch++;
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
