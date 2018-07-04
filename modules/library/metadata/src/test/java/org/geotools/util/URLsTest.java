/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import org.junit.Test;

/** Tests for {@link URLs}. */
public class URLsTest {

    private void assertURL(String expectedFilePath, String urlString) throws MalformedURLException {
        URL url = new URL(urlString);
        File file = URLs.urlToFile(url);
        if (URLs.IS_WINDOWS_OS) {
            assertEquals(expectedFilePath.replaceAll("/", "\\\\"), file.getPath());
        } else {
            if (expectedFilePath.endsWith("/")) {
                expectedFilePath = expectedFilePath.substring(0, expectedFilePath.length() - 1);
            }
            assertEquals(expectedFilePath, file.getPath());
        }
    }

    private void handleFile(String path) throws Exception {
        File file = new File(path);
        URI uri = file.toURI();
        URL url = file.toURI().toURL();
        URL url2 = file.toURI().toURL();
        assertEquals("jdk contract", file.getAbsoluteFile(), new File(uri));
        File toFile = URLs.urlToFile(url);
        assertEquals(path + ":url", file.getAbsoluteFile(), toFile);
        File toFile2 = URLs.urlToFile(url2);
        assertEquals(path + ":url2", file.getAbsoluteFile(), toFile2);
    }

    private String replaceSlashes(String string) {
        return string.replaceAll("\\\\", "/");
    }

    @Test
    public void testFileToUrl() {
        String url = URLs.fileToUrl(new File("file café")).toString();
        assertTrue("Expected '" + url + "' to start with 'file:'", url.startsWith("file:"));
        assertTrue(
                "Expected '" + url + "' to end with 'file%20caf%C3%A9'",
                url.endsWith("file%20caf%C3%A9"));
    }

    @Test
    public void testUrlToFile() throws Exception {
        handleFile(System.getProperty("user.home"));
        handleFile(System.getProperty("user.dir"));
        if (URLs.IS_WINDOWS_OS) {
            handleFile("C:\\");
            handleFile("C:\\one");
            handleFile("C:\\one\\two");
            handleFile("C:\\one\\two\\and three");
            handleFile("D:\\");
            // Single slash keeps rooted path
            assertURL("\\one", "file:/one");
            assertURL("\\.\\one", "file:/./one");
            // Double slash makes it relative if non existent.
            assertURL("one", "file://one");
            assertURL("./one", "file://./one");
            handleFile("\\\\host\\share\\file");
            // from GEOT-3300 urlToFile doesn't handle network paths correctly
            URL url = new URL("file", "////oehhwsfs09", "/some/path/on/the/server/filename.nds");
            File windowsShareFile = URLs.urlToFile(url);
            assertNotNull(windowsShareFile);
        } else {
            handleFile("/one");
            handleFile("one");
            handleFile("/one/two/and three");
            handleFile("/hello world/this++().file");
        }
        assertURL("one", "file:one");
        assertURL("/one", "file:///one");
        assertURL(replaceSlashes("C:\\"), "file://C:/");
        assertURL(replaceSlashes("C:\\one"), "file://C:/one");
        assertURL(replaceSlashes("C:\\one\\two"), "file://C:/one/two");
        assertURL(replaceSlashes("C:\\one\\two\\and three"), "file://C:/one/two/and three");
        assertURL("sample", "file:sample?query");
        assertURL("sample", "file:sample#ref");
        File file = File.createTempFile("hello", "world");
        handleFile(file.getAbsolutePath());
        handleFile(file.getPath());
        // from GEOT-3300 urlToFile doesn't handle network paths correctly
        URL url = new URL("file", "////oehhwsfs09", "/some/path/on/the/server/filename.nds");
        File windowsShareFile = URLs.urlToFile(url);
        assertNotNull(windowsShareFile);
        assertURL("file café", "file:file%20caf%C3%A9");
        assertURL("/file café", "file:/file%20caf%C3%A9");
        assertURL("file café", "file://file%20caf%C3%A9");
    }
}
