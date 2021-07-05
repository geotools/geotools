/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.client;

import org.junit.Assert;
import org.junit.Test;

/** @author Roar Brænden */
public class TileURLBuilderTest {

    @Test
    public void testTileURLBuilder() throws Exception {
        TileURLBuilder builder =
                new TileURLBuilder("http://dummy.net/wmts/{TileMatrix}/{TileCol}/{TileRow}");
        String result = builder.createURL("matrix", 54, 78);
        Assert.assertEquals("http://dummy.net/wmts/matrix/54/78", result);
    }
}
