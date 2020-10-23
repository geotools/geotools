/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import org.junit.Before;
import org.junit.Test;

/** @author ImranR */
public class URLCheckerFactoryBackwardTest {

    @Before
    public void setUp() throws Exception {
        // empty the list
        List<URLChecker> urlCheckerList = URLCheckers.getEnabledURLCheckerList();
        for (URLChecker urlChecker : urlCheckerList) {
            URLCheckers.removeURLChecker(urlChecker);
        }
    }

    @Test
    public void URLCheckerFactoryBackwardTest() throws IOException, URISyntaxException {
        assertTrue(URLCheckers.getURLCheckerList().size() == 0);

        // when no URLCheckers are available, all evaluations should by default pass
        assertTrue(URLCheckers.evaluate("http://schemas.opengis.net/myschema.xml"));
        assertTrue(URLCheckers.evaluate(new URL("http://schemas.opengis.net/myschema.xml")));
        assertTrue(URLCheckers.evaluate(new URI("http://schemas.opengis.net/myschema.xml")));

        assertTrue(URLCheckers.evaluate("ftp://user:pass@www.myserver.com/file.zip"));
        assertTrue(URLCheckers.evaluate(new URL("ftp://user:pass@www.myserver.com/file.zip")));
        assertTrue(URLCheckers.evaluate(new URI("ftp://user:pass@www.myserver.com/file.zip")));
    }
}
