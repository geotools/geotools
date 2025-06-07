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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import org.junit.Test;
import org.xml.sax.SAXException;

@SuppressWarnings("BadImport")
public class PreventLocalEntityResolverTest {

    @Test
    public void testValidAbsoluteSystemIdWithoutBaseHTTP() throws Exception {
        assertNull(INSTANCE.resolveEntity(null, "http://xyz/a.xsd"));
    }

    @Test
    public void testValidAbsoluteSystemIdWithoutBaseHTTPS() throws Exception {
        assertNull(INSTANCE.resolveEntity(null, "https://xyz/a.xsd"));
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
    public void testValidAbsoluteSystemIdWithBase() throws Exception {
        assertNull(INSTANCE.resolveEntity(null, null, "http://xyz/a.xsd", "http://xyz/b.xsd"));
    }

    @Test
    public void testValidRelativeSystemIdHTTP() throws Exception {
        assertNull(INSTANCE.resolveEntity(null, null, "http://xyz/a.xsd", "b.xsd"));
    }

    @Test
    public void testValidRelativeSystemIdHTTPS() throws Exception {
        assertNull(INSTANCE.resolveEntity(null, null, "https://xyz/a.xsd", "b.xsd"));
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
}
