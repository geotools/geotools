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
package org.geotools.tile.impl.osm;

import org.geotools.tile.TileIdentifier;
import org.geotools.tile.TileIdentifierTest;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.geotools.tile.impl.ZoomLevel;
import org.junit.Assert;
import org.junit.Test;

public class OSMTileIdentifierTest extends TileIdentifierTest {

    @Test
    public void testGetId() {
        // System.out.println(this.tileId.getId());
        Assert.assertEquals("SomeService_5_10_12", this.tileId.getId());
    }

    @Test
    public void testGetCode() {
        Assert.assertEquals("5/10/12", this.tileId.getCode());
    }

    @Test
    public void testGetRightNeighbour() {
        OSMTileIdentifier neighbour =
                new OSMTileIdentifier(11, 12, new WebMercatorZoomLevel(5), "SomeService");

        Assert.assertEquals(neighbour, this.tileId.getRightNeighbour());
    }

    @Test
    public void testGetLowertNeighbour() {
        OSMTileIdentifier neighbour =
                new OSMTileIdentifier(10, 13, new WebMercatorZoomLevel(5), "SomeService");

        Assert.assertEquals(neighbour, this.tileId.getLowerNeighbour());
    }

    protected TileIdentifier createTestTileIdentifier(
            ZoomLevel zoomLevel, int x, int y, String name) {
        return new OSMTileIdentifier(x, y, zoomLevel, name);
    }
}
