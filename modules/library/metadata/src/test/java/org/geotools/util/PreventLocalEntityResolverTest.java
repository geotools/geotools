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
package org.geotools.util;

import static org.geotools.util.PreventLocalEntityResolver.ERROR_MESSAGE_BASE;
import static org.geotools.util.PreventLocalEntityResolver.INSTANCE;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import com.sun.net.httpserver.HttpServer;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

@SuppressWarnings("BadImport")
public class PreventLocalEntityResolverTest {

    private static HttpServer server;
    private static String baseUrl;

    @BeforeClass
    public static void startServer() throws IOException {
        server = HttpServer.create(new InetSocketAddress("localhost", 0), 0);
        server.createContext("/a.xsd", exchange -> respond(exchange, "<schema/>"));
        server.createContext("/redirect-to-allowed.xsd", exchange -> redirect(exchange, "/a.xsd"));
        server.createContext(
                "/redirect-to-disallowed.xsd", exchange -> redirect(exchange, "http://localhost/not-an-xsd"));
        server.createContext("/redirect-loop.xsd", exchange -> redirect(exchange, "/redirect-loop.xsd"));
        server.start();
        baseUrl = "http://localhost:" + server.getAddress().getPort();
    }

    @AfterClass
    public static void stopServer() {
        server.stop(0);
    }

    private static void respond(com.sun.net.httpserver.HttpExchange exchange, String body) throws IOException {
        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private static void redirect(com.sun.net.httpserver.HttpExchange exchange, String location) throws IOException {
        exchange.getResponseHeaders().add("Location", location);
        exchange.sendResponseHeaders(302, -1);
        exchange.close();
    }

    @Test
    public void testValidAbsoluteSystemIdWithoutBaseHTTP() throws Exception {
        InputSource source = INSTANCE.resolveEntity(null, baseUrl + "/a.xsd");
        assertNotNull(source);
        assertEquals(baseUrl + "/a.xsd", source.getSystemId());
    }

    @Test
    public void testValidAbsoluteSystemIdWithoutBaseHTTPS() throws Exception {
        try {
            INSTANCE.resolveEntity(null, "https://xyz/a.xsd");
            fail("expected an IOException");
        } catch (IOException expected) {
            // no local https test server; the ALLOWED_URIS check passed and a real connection
            // was attempted, as intended
        }
    }

    @Test
    public void testValidAbsoluteSystemIdWithoutBaseJAR() throws Exception {
        assertNull(INSTANCE.resolveEntity(null, "jar:file:/xyz/foo.jar!/bar/a.xsd"));
    }

    @Test
    public void testValidAbsoluteSystemIdWithoutBaseVFS() throws Exception {
        assertNull(INSTANCE.resolveEntity(null, "vfs:/xyz/foo.jar/bar/a.xsd"));
    }

    @Test
    public void testValidAbsoluteSystemIdNested() throws Exception {
        assertNull(
                INSTANCE.resolveEntity(
                        null,
                        "jar:nested:/home/spring/tailormap-api.jar/!BOOT-INF/lib/gt-xsd-gml3-33.1.jar!/org/geotools/gml3/gml.xsd"));
    }

    @Test
    public void testValidAbsoluteSystemIdWithBase() throws Exception {
        // systemId is absolute, so baseURI is ignored and never contacted
        InputSource source = INSTANCE.resolveEntity(null, null, baseUrl + "/ignored-base.xsd", baseUrl + "/a.xsd");
        assertNotNull(source);
        assertEquals(baseUrl + "/a.xsd", source.getSystemId());
    }

    @Test
    public void testValidRelativeSystemIdHTTP() throws Exception {
        InputSource source = INSTANCE.resolveEntity(null, null, baseUrl + "/", "a.xsd");
        assertNotNull(source);
        assertEquals(baseUrl + "/a.xsd", source.getSystemId());
    }

    @Test
    public void testValidRelativeSystemIdHTTPS() throws Exception {
        try {
            INSTANCE.resolveEntity(null, null, "https://xyz/a.xsd", "b.xsd");
            fail("expected an IOException");
        } catch (IOException expected) {
            // no local https test server; the ALLOWED_URIS check passed and a real connection
            // was attempted, as intended
        }
    }

    @Test
    public void testValidRelativeSystemIdJAR() throws Exception {
        assertNull(INSTANCE.resolveEntity(null, null, "jar:file:/xyz/foo.jar!/bar/a.xsd", "b.xsd"));
    }

    // Relative VFS URL is not unit tested because it requires a
    // custom URL stream handler that is a part of JBoss/WildFly.

    @Test
    public void testRelativeSystemIdWithoutBase() throws Exception {
        try {
            INSTANCE.resolveEntity(null, "a.xsd");
            fail("expected a SAXException");
        } catch (SAXException e) {
            assertEquals(ERROR_MESSAGE_BASE + "a.xsd", e.getMessage());
        }
    }

    @Test
    public void testAbsoluteSystemIdWithoutBaseInvalidProtocol() throws Exception {
        try {
            INSTANCE.resolveEntity(null, "file:///a/b/c.xsd");
            fail("expected a SAXException");
        } catch (SAXException e) {
            assertEquals(ERROR_MESSAGE_BASE + "file:///a/b/c.xsd", e.getMessage());
        }
    }

    @Test
    public void testAbsoluteSystemIdWithBaseInvalidProtocol() throws Exception {
        try {
            INSTANCE.resolveEntity(null, null, "http://xyz/a.xsd", "file:///a/b/c.xsd");
            fail("expected a SAXException");
        } catch (SAXException e) {
            assertEquals(ERROR_MESSAGE_BASE + "file:///a/b/c.xsd", e.getMessage());
        }
    }

    @Test
    public void testRelativeSystemIdInvalidExtension() throws Exception {
        try {
            INSTANCE.resolveEntity(null, null, "http://xyz/a.xsd", "b.txt");
            fail("expected a SAXException");
        } catch (SAXException e) {
            assertEquals(ERROR_MESSAGE_BASE + "b.txt", e.getMessage());
        }
    }

    @Test
    public void testRelativeSystemIdInvalidCharacter1() throws Exception {
        try {
            INSTANCE.resolveEntity(null, null, "http://xyz/a.xsd", "b.txt?.xsd");
            fail("expected a SAXException");
        } catch (SAXException e) {
            assertEquals(ERROR_MESSAGE_BASE + "b.txt?.xsd", e.getMessage());
        }
    }

    @Test
    public void testRelativeSystemIdInvalidCharacter2() throws Exception {
        try {
            INSTANCE.resolveEntity(null, null, "http://xyz/a.xsd", "b.txt#.xsd");
            fail("expected a SAXException");
        } catch (SAXException e) {
            assertEquals(ERROR_MESSAGE_BASE + "b.txt#.xsd", e.getMessage());
        }
    }

    @Test
    public void testRelativeSystemIdInvalidCharacter3() throws Exception {
        try {
            INSTANCE.resolveEntity(null, null, "http://xyz/a.xsd", "b.txt;.xsd");
            fail("expected a SAXException");
        } catch (SAXException e) {
            assertEquals(ERROR_MESSAGE_BASE + "b.txt;.xsd", e.getMessage());
        }
    }

    @Test
    public void testHttpRedirectToAllowedUriIsFollowed() throws Exception {
        InputSource source = INSTANCE.resolveEntity(null, baseUrl + "/redirect-to-allowed.xsd");
        assertNotNull(source);
        assertEquals(baseUrl + "/a.xsd", source.getSystemId());
    }

    @Test
    public void testHttpRedirectToDisallowedUriIsRejected() throws Exception {
        try {
            INSTANCE.resolveEntity(null, baseUrl + "/redirect-to-disallowed.xsd");
            fail("expected a SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().startsWith(ERROR_MESSAGE_BASE + "redirect to disallowed URL"));
        }
    }

    @Test
    public void testHttpTooManyRedirectsAreRejected() throws Exception {
        try {
            INSTANCE.resolveEntity(null, baseUrl + "/redirect-loop.xsd");
            fail("expected a SAXException");
        } catch (SAXException e) {
            assertTrue(e.getMessage().startsWith(ERROR_MESSAGE_BASE + "too many redirects"));
        }
    }
}
