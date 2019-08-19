/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory;

import static org.junit.Assert.*;

import org.junit.*;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Tests the {@link URN_Parser} class.
 *
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class URN_ParserTest {

    /** Parses a valid URN. */
    @Test
    public void testParse() throws NoSuchAuthorityCodeException {
        final URN_Parser parser = URN_Parser.buildParser("urn:ogc:def:CRS:EPSG:6.11.2:4326");
        assertEquals("crs", parser.type.name);
        assertEquals("EPSG", parser.authority);
        assertEquals("6.11.2", parser.version.toString());
        assertEquals("4326", parser.code);
        assertEquals("EPSG:4326", parser.getAuthorityCode());
    }

    /** Parses a valid URN without version. */
    @Test
    public void testParseWithoutVersion() throws NoSuchAuthorityCodeException {
        final URN_Parser parser = URN_Parser.buildParser("urn:ogc:def:CRS:EPSG:4326");
        assertEquals("crs", parser.type.name);
        assertEquals("EPSG", parser.authority);
        assertNull(parser.version);
        assertEquals("4326", parser.code);
        assertEquals("EPSG:4326", parser.getAuthorityCode());
    }

    /** Parses an invalid URN. */
    @Test
    public void testInvalidParse() {
        final String urn = "urn:ogcx:def:CRS:EPSG:6.8:4326";
        try {
            URN_Parser.buildParser(urn);
            fail();
        } catch (NoSuchAuthorityCodeException e) {
            // This is the expected exception.
            assertEquals(urn, e.getAuthorityCode());
        }
    }

    /** Parses a URN with an unknow type. */
    @Test
    public void testInvalidType() {
        final String urn = "urn:ogc:def:dummy:EPSG:6.8:4326";
        try {
            URN_Parser.buildParser(urn);
            fail();
        } catch (NoSuchAuthorityCodeException e) {
            // This is the expected exception.
            assertEquals("dummy", e.getAuthorityCode());
        }
    }

    @Test
    public void testOgcCRS84() throws NoSuchAuthorityCodeException {
        final String urn_crs84 = "urn:ogc:def:crs:OGC:1.3:CRS84";
        final URN_Parser parser = URN_Parser.buildParser(urn_crs84);
        assertEquals("crs", parser.type.name);
        assertEquals("CRS", parser.authority);
        assertEquals("1.3", parser.version.toString());
        assertEquals("84", parser.code);
        assertEquals("CRS:84", parser.getAuthorityCode());
    }

    @Test
    public void testOgcCRS84NoVersion() throws NoSuchAuthorityCodeException {
        final String urn_crs84 = "urn:ogc:def:crs:OGC:CRS84";
        final URN_Parser parser = URN_Parser.buildParser(urn_crs84);
        assertEquals("crs", parser.type.name);
        assertEquals("CRS", parser.authority);
        assertNull(parser.version);
        assertEquals("84", parser.code);
        assertEquals("CRS:84", parser.getAuthorityCode());
    }

    @Test
    public void testAutoLatLon() throws NoSuchAuthorityCodeException {
        final String urn_auto = "urn:ogc:def:crs:OGC:1.3:AUTO42002:180:90";
        final URN_Parser parser = URN_Parser.buildParser(urn_auto);
        assertEquals("crs", parser.type.name);
        assertEquals("AUTO", parser.authority);
        assertEquals("1.3", parser.version.toString());
        assertEquals("42002,180,90", parser.code);
        assertEquals("AUTO:42002,180,90", parser.getAuthorityCode());
    }

    @Test
    public void testAuto() throws NoSuchAuthorityCodeException {
        final String urn_auto = "urn:ogc:def:crs:OGC:1.3:AUTO42002";
        final URN_Parser parser = URN_Parser.buildParser(urn_auto);
        assertEquals("crs", parser.type.name);
        assertEquals("AUTO", parser.authority);
        assertEquals("1.3", parser.version.toString());
        assertEquals("42002", parser.code);
        assertEquals("AUTO:42002", parser.getAuthorityCode());
    }

    @Test
    public void testAutoNoVersion() throws NoSuchAuthorityCodeException {
        final String urn_auto = "urn:ogc:def:crs:OGC:AUTO42002";
        final URN_Parser parser = URN_Parser.buildParser(urn_auto);
        assertEquals("crs", parser.type.name);
        assertEquals("AUTO", parser.authority);
        assertNull(parser.version);
        assertEquals("42002", parser.code);
        assertEquals("AUTO:42002", parser.getAuthorityCode());
    }
}
