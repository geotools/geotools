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
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.Tile;
import org.geotools.tile.TileFactory;
import org.geotools.tile.TileFactoryTest;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.WebMercatorTileFactory;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.junit.Assert;
import org.junit.Test;

public class BingTileFactoryTest extends TileFactoryTest {

    @Test
    public void testGetTileFromCoordinate() {

        Tile tile =
                factory.findTileAtCoordinate(51, 7, new WebMercatorZoomLevel(5), createService());

        TileService service = createService();
        BingTile expectedTile = new BingTile(20, 15, new WebMercatorZoomLevel(5), service);
        Assert.assertEquals(expectedTile, tile);
    }

    @Test
    public void testFindRightNeighbour() {

        TileService service = createService();
        BingTile tile = new BingTile(20, 15, new WebMercatorZoomLevel(5), service);

        Tile neighbour = factory.findRightNeighbour(tile, service);

        BingTile expectedNeighbour = new BingTile(21, 15, new WebMercatorZoomLevel(5), service);

        Assert.assertEquals(expectedNeighbour, neighbour);
    }

    @Test
    public void testFindLowerNeighbour() {

        TileService service = createService();
        BingTile tile = new BingTile(20, 15, new WebMercatorZoomLevel(5), service);

        Tile neighbour = factory.findLowerNeighbour(tile, service);

        BingTile expectedNeighbour = new BingTile(20, 16, new WebMercatorZoomLevel(5), service);

        Assert.assertEquals(expectedNeighbour, neighbour);
    }

    @Test
    public void testGetExtentFromTileName() {

        BingTileIdentifier tileId =
                new BingTileIdentifier(10, 12, new WebMercatorZoomLevel(5), "SomeName");
        BingTile tile = new BingTile(tileId, new BingService("2", "d"));

        ReferencedEnvelope env = WebMercatorTileFactory.getExtentFromTileName(tileId);

        Assert.assertEquals(tile.getExtent(), env);

        ReferencedEnvelope expectedEnv =
                new ReferencedEnvelope(
                        -67.5, -56.25, 31.9521622380, 40.9798980, DefaultGeographicCRS.WGS84);

        Assert.assertEquals(env.getMinX(), expectedEnv.getMinX(), 0.000001);
        Assert.assertEquals(env.getMinY(), expectedEnv.getMinY(), 0.000001);
        Assert.assertEquals(env.getMaxX(), expectedEnv.getMaxX(), 0.000001);
        Assert.assertEquals(env.getMaxY(), expectedEnv.getMaxY(), 0.000001);

        // Assert.assertEquals(envRaw, env);

    }

    private TileService createService() {
        String baseURL =
                "http://ak.dynamic.t2.tiles.virtualearth.net/comp/ch/${code}?mkt=de-de&it=G,VE,BX,L,LA&shading=hill&og=78&n=z";
        return new BingService("Road", baseURL);
    }

    protected TileFactory createFactory() {
        return new BingTileFactory();
    }
}
