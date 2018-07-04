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

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Assert;
import org.junit.Test;

public class BingTileUtilTest {

    @Test
    public void testLonLatToPixelXY() {

        int[] pixelXY = BingTileUtil.lonLatToPixelXY(7, 51, 5);
        Assert.assertArrayEquals(new int[] {4255, 2742}, pixelXY);
    }

    @Test
    public void testLonLatToPixelXYAndBack() {

        double[] coords = {7, 51};

        int levelOfDetail = 5;

        int[] pixelXY = BingTileUtil.lonLatToPixelXY(coords[0], coords[1], levelOfDetail);

        Assert.assertArrayEquals(new int[] {4255, 2742}, pixelXY);

        double[] calculatedCoords =
                BingTileUtil.pixelXYToLonLat(pixelXY[0], pixelXY[1], levelOfDetail);

        double delta = 0.000001;
        Assert.assertEquals(calculatedCoords[0], 6.9873046875, delta);
        Assert.assertEquals(calculatedCoords[1], 51.013754657, delta);
    }

    @Test
    public void testLonLatToTileQuadRaw() {

        double[] coords = {7, 51};

        int levelOfDetail = 5;

        int[] pixelXY = BingTileUtil.lonLatToPixelXY(coords[0], coords[1], levelOfDetail);
        int[] tileXY = BingTileUtil.pixelXYToTileXY(pixelXY[0], pixelXY[1]);

        String quadKey = BingTileUtil.tileXYToQuadKey(tileXY[0], tileXY[1], levelOfDetail);

        Assert.assertEquals("12020", quadKey);
    }

    @Test
    public void testLonLatZoomToTileQuad() {

        double[] coords = {7, 51};
        int levelOfDetail = 5;

        String quadKey = BingTileUtil.lonLatToQuadKey(coords[0], coords[1], levelOfDetail);

        Assert.assertEquals("12020", quadKey);

        String quadKey2 = BingTileUtil.lonLatToQuadKey(coords[0], coords[1], 12);
        Assert.assertEquals("120203023133", quadKey2);
    }

    @Test
    public void testGetTileBoundingBox() {

        double[] coords = {7, 51};
        int levelOfDetail = 8;

        ReferencedEnvelope env =
                BingTileUtil.getTileBoundingBox(coords[0], coords[1], levelOfDetail);

        double delta = 0.000001;
        Assert.assertEquals(5.625, env.getMinX(), delta);
        Assert.assertEquals(50.7364551370, env.getMinY(), delta);
        Assert.assertEquals(7.03125, env.getMaxX(), delta);
        Assert.assertEquals(51.6180165487, env.getMaxY(), delta);
    }

    @Test
    public void testMapSize() {

        Assert.assertEquals(256, BingTileUtil.mapSize(0));
        Assert.assertEquals(BingTile.DEFAULT_TILE_SIZE, BingTileUtil.mapSize(0));

        Assert.assertEquals(512, BingTileUtil.mapSize(1));
        Assert.assertEquals(BingTile.DEFAULT_TILE_SIZE * 2, BingTileUtil.mapSize(1));

        Assert.assertEquals(1024, BingTileUtil.mapSize(2));
    }

    @Test
    public void pixelXYToTileXY() {

        int[] tileXY = BingTileUtil.pixelXYToTileXY(250, 250);
        Assert.assertArrayEquals(new int[] {0, 0}, tileXY);

        tileXY = BingTileUtil.pixelXYToTileXY(250, 256);
        Assert.assertArrayEquals(new int[] {0, 1}, tileXY);

        tileXY = BingTileUtil.pixelXYToTileXY(511, 512);
        Assert.assertArrayEquals(new int[] {1, 2}, tileXY);

        tileXY = BingTileUtil.pixelXYToTileXY(1024, 1024);
        Assert.assertArrayEquals(new int[] {4, 4}, tileXY);
    }
}
