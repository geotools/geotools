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
import static org.junit.Assert.assertThrows;

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
        URLCheckers.register(fileChecker);
        URLCheckers.register(gtChecker);
        URLCheckers.register(gsChecker);

        // invalid references
        assertThrows(URLCheckerException.class, () -> URLCheckers.confirm("http://google.com"));
        assertThrows(URLCheckerException.class, () -> URLCheckers.confirm("http://nyt.com"));
        assertThrows(URLCheckerException.class, () -> URLCheckers.confirm("file:///tmp"));

        // valid references
        URLCheckers.confirm("https://www.geoserver.org/logo.png");
        URLCheckers.confirm("http://www.geotools.org/sld.xsd");
        URLCheckers.confirm("file:///data/dem.tif");
    }
}
