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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.geotools.tile.impl.ZoomLevel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

public class TileTest {

    protected Tile tile;

    @Before
    public void beforeTest() {
        this.tile = createTestTile();
    }

    @After
    public void afterTest() {
        this.tile = null;
    }

    @Test
    public void testContructor() {
        Assert.assertNotNull(this.tile);
    }

    @Test
    public void testEquals() {

        Assert.assertEquals(this.tile, this.tile);
        Assert.assertNotEquals(null, this.tile);
        Assert.assertNotEquals("Blah", this.tile);

        Tile otherTile = createTestTile();
        Assert.assertEquals(this.tile, otherTile);
        Assert.assertEquals(otherTile, this.tile);
    }

    @Test
    public void testCreateErrorImage() {
        BufferedImage img = this.tile.createErrorImage("Failed:" + this.tile.getId());
        Assert.assertNotNull(img);
        Assert.assertEquals(256, img.getHeight());
        Assert.assertEquals(256, img.getWidth());
        int pix = img.getRGB(20, 10);
        Assert.assertTrue(checkColor(Color.white.getRGB(), pix));
        System.setProperty(Tile.DEBUG_FLAG, "no");
        img = this.tile.createErrorImage("Failed:" + this.tile.getId());
        Assert.assertNotNull(img);
        Assert.assertEquals(256, img.getHeight());
        Assert.assertEquals(256, img.getWidth());
        pix = img.getRGB(20, 10);
        Assert.assertFalse(checkColor(Color.white.getRGB(), pix));
    }

    private boolean checkColor(int expected, int observed) {
        int blueExpected = expected & 0xff;
        int greenExpected = (expected & 0xff00) >> 8;
        int redExpected = (expected & 0xff0000) >> 16;

        int blue = observed & 0xff;
        int green = (observed & 0xff00) >> 8;
        int red = (observed & 0xff0000) >> 16;
        return blue == blueExpected && red == redExpected && green == greenExpected;
    }

    @Test
    public void testGetExtent() {
        ReferencedEnvelope env = new ReferencedEnvelope(new Envelope(6, 15, 47, 55), DefaultGeographicCRS.WGS84);
        Assert.assertEquals(env, this.tile.getExtent());
    }

    @Test
    public void testGetId() {
        Assert.assertEquals("SomeService_01234xy56789", this.tile.getId());
    }

    protected Tile createTestTile() {

        ReferencedEnvelope env = new ReferencedEnvelope(new Envelope(6, 15, 47, 55), DefaultGeographicCRS.WGS84);
        ZoomLevel zoomLevel = new WebMercatorZoomLevel(5);

        return new Tile(TileIdentifierTest.createTileIdentifierPrototype(zoomLevel, 10, 12, "SomeService"), env, 256) {

            @Override
            public URL getUrl() {
                try {
                    return new URL("http://localhost/service/tile123");
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
