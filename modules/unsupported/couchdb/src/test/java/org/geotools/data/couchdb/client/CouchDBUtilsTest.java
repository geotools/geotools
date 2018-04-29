/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.couchdb.client;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * @author Ian Schneider (OpenGeo)
 * @source $URL$
 */
public class CouchDBUtilsTest {

    @Test
    public void testStripComments() {
        String jzunk = "/* a header\nwith info*/\nstuff\nstuff\n/*a comment*//*another*/stuff";
        String stripped = CouchDBUtils.stripComments(jzunk);
        assertEquals("\nstuff\nstuff\nstuff", stripped);
    }
}
