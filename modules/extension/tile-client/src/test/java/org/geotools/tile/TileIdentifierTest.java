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
package org.geotools.tile;

import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.geotools.tile.impl.ZoomLevel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TileIdentifierTest {

    protected TileIdentifier tileId;

    @Before
    public void beforeTest() {
        this.tileId = createTestTileIdentifier(5, 10, 12, "SomeService");
    }

    @After
    public void afterTest() {
        this.tileId = null;
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(this.tileId);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalZ() {
        createTestTileIdentifier(-1, 10, 12, "SomeService");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullZ() {
        createTestTileIdentifier(null, 10, 12, "SomeService");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalX() {
        createTestTileIdentifier(1, -10, 12, "SomeService");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalY() {
        createTestTileIdentifier(1, 10, -12, "SomeService");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalServiceName() {
        createTestTileIdentifier(1, 10, 12, null);
    }

    @Test
    public void testX() {
        Assert.assertEquals(10, this.tileId.getX());
    }

    @Test
    public void testY() {
        Assert.assertEquals(12, this.tileId.getY());
    }

    @Test
    public void testZ() {
        Assert.assertEquals(5, this.tileId.getZ());
    }

    @Test
    public void testGetId() {
        Assert.assertEquals("SomeService_01234xy56789", this.tileId.getId());
    }

    @Test
    public void testGetCode() {
        Assert.assertEquals("01234xy56789", this.tileId.getCode());
    }

    @Test
    public void testGetServiceName() {
        Assert.assertEquals("SomeService", this.tileId.getServiceName());
    }

    protected TileIdentifier createTestTileIdentifier(int z, int x, int y, String name) {
        return createTestTileIdentifier(new WebMercatorZoomLevel(z), x, y, name);
    };

    @Test
    public void testEquals() {
        Assert.assertTrue(this.tileId.equals(this.tileId));
        Assert.assertFalse(this.tileId.equals(null));
        Assert.assertFalse(this.tileId.equals("Blah"));

        TileIdentifier otherTile = createTestTileIdentifier(5, 10, 12, "SomeService");
        Assert.assertTrue(this.tileId.equals(otherTile));
        Assert.assertTrue(otherTile.equals(this.tileId));
    }

    protected TileIdentifier createTestTileIdentifier(
            ZoomLevel zoomLevel, int x, int y, String name) {
        return createTileIdentifierPrototype(zoomLevel, x, y, name);
    }

    public static final TileIdentifier createTileIdentifierPrototype(
            ZoomLevel zoomLevel, int x, int y, String name) {
        return new TileIdentifier(x, y, zoomLevel, name) {

            @Override
            public String getId() {
                return getServiceName() + "_" + getCode();
            }

            @Override
            public String getCode() {
                return "01234xy56789";
            }

            @Override
            public TileIdentifier getRightNeighbour() {
                return null;
            }

            @Override
            public TileIdentifier getLowerNeighbour() {
                return null;
            }
        };
    }
}
