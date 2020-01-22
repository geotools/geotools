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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** @author ImranR */
public class ControlledHttpClientTest {

    static MockHttpClient mockHttpClient = new MockHttpClient();

    MockURLChecker urlChecker = new MockURLChecker();
    MockFileURIChecker fileUriChecker = new MockFileURIChecker();

    @BeforeClass
    public static void setUpOnce() throws Exception {
        mockHttpClient.expectGet(
                new URL("http://schemas.opengis.net/myschema.xml"),
                new MockHttpResponse("passed", "text/plain", null));
        mockHttpClient.expectGet(
                new URL("http://hacked.opengis.net/myschema.xml"),
                new MockHttpResponse("passed", "text/plain", null));
    }

    @Before
    public void setUp() throws Exception {
        urlChecker.setEnabled(true);
        fileUriChecker.setEnabled(true);
        URLCheckerFactory.addURLChecker(urlChecker);
        URLCheckerFactory.addURLChecker(fileUriChecker);
    }

    @After
    public void clean() throws Exception {
        URLCheckerFactory.removeURLChecker(urlChecker);
        URLCheckerFactory.removeURLChecker(fileUriChecker);
    }

    @Test
    public void TestFactoryEvaluation() throws IOException, URISyntaxException {
        // URL evaluation
        assertTrue(URLCheckerFactory.evaluate("http://schemas.opengis.net/myschema.xml"));
        assertTrue(URLCheckerFactory.evaluate(new URL("http://schemas.opengis.net/myschema.xml")));
        assertTrue(URLCheckerFactory.evaluate(new URI("http://schemas.opengis.net/myschema.xml")));

        try {
            assertFalse(URLCheckerFactory.evaluate("http://hacked.opengis.net/myschema.xml"));
            fail();
        } catch (Exception io) {
        }
        try {
            assertFalse(
                    URLCheckerFactory.evaluate(new URL("http://hacked.opengis.net/myschema.xml")));
            fail();
        } catch (Exception io) {
        }
        try {
            assertFalse(
                    URLCheckerFactory.evaluate(new URI("http://hacked.opengis.net/myschema.xml")));
            fail();
        } catch (Exception io) {
        }

        // FTP evaluation
        assertTrue(URLCheckerFactory.evaluate("ftp://user:pass@www.myserver.com/file.zip"));
        assertTrue(
                URLCheckerFactory.evaluate(new URL("ftp://user:pass@www.myserver.com/file.zip")));
        assertTrue(
                URLCheckerFactory.evaluate(new URI("ftp://user:pass@www.myserver.com/file.zip")));

        try {
            assertFalse(URLCheckerFactory.evaluate("ftp://user:pass@hacked.myserver.com/file.zip"));
            fail();
        } catch (Exception io) {
        }
        try {
            assertFalse(
                    URLCheckerFactory.evaluate(
                            new URL("ftp://user:pass@hacked.myserver.com/file.zip")));
            fail();
        } catch (Exception io) {
        }
        try {
            assertFalse(
                    URLCheckerFactory.evaluate(
                            new URI("ftp://user:pass@hacked.myserver.com/file.zip")));
            fail();
        } catch (Exception io) {
        }
    }

    @Test
    public void TestControlledHttpClient() throws IOException, URISyntaxException {
        // method test wrapping of an HTTPClient implementation
        ControlledHttpClient controlledClient = new ControlledHttpClient(mockHttpClient);

        HTTPResponse response =
                controlledClient.get(new URL("http://schemas.opengis.net/myschema.xml"));
        assertNotNull(response);

        try {
            controlledClient.get(new URL("http://hacked.opengis.net/myschema.xml"));
            // should not reach this line
            fail();
        } catch (Exception e) {
            assertTrue(e.getMessage().contains("did not pass security evaluation"));
        }
    }

    @Test
    public void TestControlledHttpClientAllDisabled() throws IOException, URISyntaxException {
        // this test checks how code behaves when all validators are disabled
        urlChecker.setEnabled(false);
        fileUriChecker.setEnabled(false);
        ControlledHttpClient controlledClient =
                new ControlledHttpClient(mockHttpClient, Arrays.asList(urlChecker, fileUriChecker));
        // everything should pass
        HTTPResponse response =
                controlledClient.get(new URL("http://schemas.opengis.net/myschema.xml"));
        assertNotNull(response);
        response = controlledClient.get(new URL("http://hacked.opengis.net/myschema.xml"));
        assertNotNull(response);
    }

    @Test
    public void TestControlledHttpClientEmpty() throws IOException, URISyntaxException {
        // Backward compatability, when factory has no url checkers
        ControlledHttpClient controlledClient =
                new ControlledHttpClient(mockHttpClient, Collections.EMPTY_LIST);
        // everything should pass
        HTTPResponse response =
                controlledClient.get(new URL("http://schemas.opengis.net/myschema.xml"));
        assertNotNull(response);
        response = controlledClient.get(new URL("http://hacked.opengis.net/myschema.xml"));
        assertNotNull(response);
    }
}
