/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.image;

import junit.framework.TestCase;


/**
 * DOCUMENT ME!
 *
 * @author rgoulds
 * @source $URL$
 */
public class WorldImageFormatTest extends TestCase {
    private WorldImageFormatTest format;

    public WorldImageFormatTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        super.setUp();
        format = new WorldImageFormatTest("test");
		assertNotNull(format);
    }

    public void testGetWorldExtension() {
        assertTrue(WorldImageFormat.getWorldExtension("png").contains(".pgw"));
        assertTrue(WorldImageFormat.getWorldExtension("gif").contains( ".gfw"));
        assertTrue(WorldImageFormat.getWorldExtension("jpg").contains( ".jgw"));
        assertTrue(WorldImageFormat.getWorldExtension("jpeg").contains( ".jgw"));
        assertTrue(WorldImageFormat.getWorldExtension("tif").contains( ".tfw"));
        assertTrue(WorldImageFormat.getWorldExtension("tiff").contains( ".tfw"));
    }
}
