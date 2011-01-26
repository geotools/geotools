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

import org.opengis.referencing.NoSuchAuthorityCodeException;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link URN_Parser} class.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class URN_ParserTest {
    /**
     * Tests the main types.
     */
    @Test
    public void testMainTypes() {
        assertEquals("crs",                 URN_Type.MAIN[0].name);
        assertEquals("datum",               URN_Type.MAIN[1].name);
        assertEquals("cs",                  URN_Type.MAIN[2].name);
        assertEquals("coordinateOperation", URN_Type.MAIN[3].name);
    }

    /**
     * Parses a valid URN.
     */
    @Test
    public void testParse() throws NoSuchAuthorityCodeException {
        final URN_Parser parser = new URN_Parser("urn:ogc:def:CRS:EPSG:6.11.2:4326");
        assertEquals("crs",       parser.type.name);
        assertEquals("EPSG",      parser.authority);
        assertEquals("6.11.2",    parser.version.toString());
        assertEquals("4326",      parser.code);
        assertEquals("EPSG:4326", parser.getAuthorityCode());
    }

    /**
     * Parses a valid URN without version.
     */
    @Test
    public void testParseWithoutVersion() throws NoSuchAuthorityCodeException {
        final URN_Parser parser = new URN_Parser("urn:ogc:def:CRS:EPSG:4326");
        assertEquals("crs",       parser.type.name);
        assertEquals("EPSG",      parser.authority);
        assertNull  (             parser.version);
        assertEquals("4326",      parser.code);
        assertEquals("EPSG:4326", parser.getAuthorityCode());
    }

    /**
     * Parses an invalid URN.
     */
    @Test
    public void testInvalidParse() {
        final String urn = "urn:ogcx:def:CRS:EPSG:6.8:4326";
        try {
            new URN_Parser(urn);
            fail();
        } catch (NoSuchAuthorityCodeException e) {
            // This is the expected exception.
            assertEquals(urn, e.getAuthorityCode());
        }
    }

    /**
     * Parses a URN with an unknow type.
     */
    @Test
    public void testInvalidType() {
        final String urn = "urn:ogc:def:dummy:EPSG:6.8:4326";
        try {
            new URN_Parser(urn);
            fail();
        } catch (NoSuchAuthorityCodeException e) {
            // This is the expected exception.
            assertEquals("dummy", e.getAuthorityCode());
        }
    }
}
