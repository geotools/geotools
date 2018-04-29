/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.tile.impl.bing;

import org.geotools.tile.TileIdentifier;
import org.geotools.tile.TileIdentifierTest;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.geotools.tile.impl.ZoomLevel;
import org.junit.Assert;
import org.junit.Test;

public class BingTileIdentifierTest extends TileIdentifierTest {

    @Test
    public void testGetId() {
        Assert.assertEquals("SomeService_03210", this.tileId.getId());
    }

    @Test
    public void testGetCode() {
        Assert.assertEquals("03210", this.tileId.getCode());
    }

    @Test
    public void testGetRightNeighbour() {
        BingTileIdentifier neighbour =
                new BingTileIdentifier(11, 12, new WebMercatorZoomLevel(5), "SomeService");

        Assert.assertEquals(neighbour, this.tileId.getRightNeighbour());
    }

    @Test
    public void testGetLowertNeighbour() {
        BingTileIdentifier neighbour =
                new BingTileIdentifier(10, 13, new WebMercatorZoomLevel(5), "SomeService");

        Assert.assertEquals(neighbour, this.tileId.getLowerNeighbour());
    }

    protected TileIdentifier createTestTileIdentifier(
            ZoomLevel zoomLevel, int x, int y, String name) {
        return new BingTileIdentifier(x, y, zoomLevel, name);
    }
}
