/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.ows;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import java.io.File;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;

public class URLCheckersTest {

    @After
    public void tearDown() throws Exception {
        URLCheckers.reset();
    }

    @Test
    public void testEmptyChecker() {
        assertThat(URLCheckers.getEnabledURLCheckers(), Matchers.empty());
        URLCheckers.confirm("myFakeLocation");
        URLCheckers.confirm("http://www.geoserver.org");
        URLCheckers.confirm("ftp://www.sf.net");
    }

    @Test
    public void testDisabledChecker() {
        URLCheckers.register(new MockURLChecker("disabled", false, s -> false));
        assertThat(URLCheckers.getEnabledURLCheckers(), Matchers.empty());
    }

    @Test
    public void testReset() {
        // initial state, empty
        assertThat(URLCheckers.getEnabledURLCheckers(), Matchers.empty());

        // register
        MockURLChecker mockChecker = new MockURLChecker("enabled", true, s -> true);
        URLCheckers.register(mockChecker);
        assertThat(URLCheckers.getEnabledURLCheckers(), Matchers.hasItems(mockChecker));

        // reset and check has been removed
        URLCheckers.reset();
        assertThat(URLCheckers.getEnabledURLCheckers(), Matchers.empty());
    }

    @Test
    public void testMultipleCheckers() {
        // the registry cannot have multiple instances of the same class. Curly brace trick here,
        // creating anonymous inner classes for each checker, so that they can all be added at
        // the same time
        MockURLChecker fileChecker =
                new MockURLChecker("enabled", true, s -> s.matches("file:///data/.*")) {};
        MockURLChecker gtChecker =
                new MockURLChecker(
                        "enabled", true, s -> s.matches("https?://www.geotools.org/.*")) {};
        MockURLChecker gsChecker =
                new MockURLChecker(
                        "enabled", true, s -> s.matches("https?://www.geoserver.org/.*")) {};

        MockURLChecker apiChecker =
                new MockURLChecker(
                        "enabled",
                        true,
                        s -> s.matches("^https?://localhost:8080/geoserver/ows\\?.*$")) {};

        MockURLChecker owsChecker =
                new MockURLChecker(
                        "enabled",
                        true,
                        s ->
                                s.matches(
                                        "^https?://localhost:8080/geoserver/(\\w+/)?ows((\\?\\!\\\\.\\\\./).)*(\\\\?.*)?$")) {};

        URLCheckers.register(fileChecker);
        URLCheckers.register(gtChecker);
        URLCheckers.register(gsChecker);
        URLCheckers.register(apiChecker);
        URLCheckers.register(owsChecker);

        // invalid references
        assertThrows(URLCheckerException.class, () -> URLCheckers.confirm("http://google.com"));
        assertThrows(URLCheckerException.class, () -> URLCheckers.confirm("http://nyt.com"));
        assertThrows(URLCheckerException.class, () -> URLCheckers.confirm("file:///tmp"));
        assertThrows(
                URLCheckerException.class,
                () -> URLCheckers.confirm("http://localhost/geoserver/styles/grass_mark.png"));
        assertThrows(
                URLCheckerException.class,
                () -> URLCheckers.confirm("http://localhost:8080/geoserver/ows/.."));

        // valid references
        URLCheckers.confirm("https://www.geoserver.org/logo.png");
        URLCheckers.confirm("http://www.geotools.org/sld.xsd");
        URLCheckers.confirm("file:///data/dem.tif");
        URLCheckers.confirm("http://localhost:8080/geoserver/ows?");
        URLCheckers.confirm("http://localhost:8080/geoserver/styles/../ows?");
        URLCheckers.confirm(
                "http://localhost:8080/geoserver/ows?SERVICE=WMS&REQUEST=GetCapabilities&VERSION=1.3.0");
        URLCheckers.confirm(
                "https://localhost:8080/geoserver/ne/ows?SERVICE=WFS&REQUEST=GetCapabilities&VERSION=1.1.0");

        // valid normalization
        URLCheckers.confirm("file:///data/../data/dem.tif");
        URLCheckers.confirm("file:/data/../data/dem.tif");
        URLCheckers.confirm("file:///data/./dem.tif");
        URLCheckers.confirm("file:/data/./dem.tif");

        // invalid normalization
        assertThrows(URLCheckerException.class, () -> URLCheckers.confirm("file:///tmp"));
        assertThrows(URLCheckerException.class, () -> URLCheckers.confirm("file:///data/../tmp"));
    }

    @Test
    public void testNormalization() {
        assertEquals("file:///data/dem.tif", URLCheckers.normalize("file:///data/../data/dem.tif"));

        assertEquals("file:///data/dem.tif", URLCheckers.normalize("file:/data/dem.tif"));

        assertEquals("file:///base/file", URLCheckers.normalize("file:///base/folder/../file"));

        assertEquals(
                "http://localhost:8080/geoserver/styles/icon.png",
                URLCheckers.normalize("http://localhost:8080/geoserver/styles/icon.png"));

        assertEquals(
                "http://localhost:8080/geoserver/www/icon.png",
                URLCheckers.normalize("http://localhost:8080/geoserver/styles/../www/icon.png"));

        assertEquals(
                "http://localhost:8080/geoserver/styles/icon.png",
                URLCheckers.normalize("http://localhost:8080/geoserver/styles/./icon.png"));

        assertEquals(
                String.join(File.separator, "", "base", "folder", "file"),
                URLCheckers.normalize("/base/folder/./file"));

        assertEquals(
                String.join(File.separator, "", "file"),
                URLCheckers.normalize("/base/folder/../../file"));

        assertEquals("file", URLCheckers.normalize("folder/../file"));

        assertEquals(
                String.join(File.separator, "..", "file"),
                URLCheckers.normalize("folder/../../file"));

        assertEquals(
                "http://example.net/api?ignore=..",
                URLCheckers.normalize("http://example.net/api?ignore=.."));

        assertEquals("C:\\directory\\file", URLCheckers.normalize("C:\\directory\\file"));
    }
}
