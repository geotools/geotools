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

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.Tile;
import org.geotools.tile.TileFactory;
import org.geotools.tile.TileFactoryTest;
import org.geotools.tile.TileService;
import org.geotools.tile.impl.WebMercatorTileFactory;
import org.geotools.tile.impl.WebMercatorTileService;
import org.geotools.tile.impl.WebMercatorZoomLevel;
import org.geotools.tile.impl.bing.BingService;
import org.junit.Assert;
import org.junit.Test;

public class OSMTileFactoryTest extends TileFactoryTest {

    @Test
    public void testGetTileFromCoordinate() {

        Tile tile =
                factory.findTileAtCoordinate(51, 7, new WebMercatorZoomLevel(5), createService());

        TileService service = createService();
        OSMTile expectedTile = new OSMTile(20, 15, new WebMercatorZoomLevel(5), service);
        Assert.assertEquals(expectedTile, tile);
    }

    @Test
    public void testGetTileFromTopLeftCoordinate() {

        Tile tile =
                factory.findTileAtCoordinate(
                        WebMercatorTileService.MIN_LONGITUDE,
                        WebMercatorTileService.MAX_LATITUDE,
                        new WebMercatorZoomLevel(5),
                        createService());

        TileService service = createService();
        OSMTile expectedTile = new OSMTile(0, 0, new WebMercatorZoomLevel(5), service);
        Assert.assertEquals(expectedTile, tile);
    }

    @Test
    public void testFindRightNeighbour() {

        TileService service = createService();
        OSMTile tile = new OSMTile(20, 15, new WebMercatorZoomLevel(5), service);

        Tile neighbour = factory.findRightNeighbour(tile, service);

        OSMTile expectedNeighbour = new OSMTile(21, 15, new WebMercatorZoomLevel(5), service);

        Assert.assertEquals(expectedNeighbour, neighbour);
    }

    @Test
    public void testFindLowerNeighbour() {

        TileService service = createService();
        OSMTile tile = new OSMTile(20, 15, new WebMercatorZoomLevel(5), service);

        Tile neighbour = factory.findLowerNeighbour(tile, service);

        OSMTile expectedNeighbour = new OSMTile(20, 16, new WebMercatorZoomLevel(5), service);

        Assert.assertEquals(expectedNeighbour, neighbour);
    }

    @Test
    public void testGetExtentFromTileName() {

        OSMTileIdentifier tileId =
                new OSMTileIdentifier(10, 12, new WebMercatorZoomLevel(5), "SomeName");
        OSMTile tile = new OSMTile(tileId, new BingService("2", "d"));

        ReferencedEnvelope env = WebMercatorTileFactory.getExtentFromTileName(tileId);

        Assert.assertEquals(tile.getExtent(), env);

        ReferencedEnvelope expectedEnv =
                new ReferencedEnvelope(
                        -67.5, -56.25, 31.9521622380, 40.9798980, DefaultGeographicCRS.WGS84);

        Assert.assertEquals(env.getMinX(), expectedEnv.getMinX(), 0.000001);
        Assert.assertEquals(env.getMinY(), expectedEnv.getMinY(), 0.000001);
        Assert.assertEquals(env.getMaxX(), expectedEnv.getMaxX(), 0.000001);
        Assert.assertEquals(env.getMaxY(), expectedEnv.getMaxY(), 0.000001);
    }

    private TileService createService() {
        String baseURL = "http://tile.openstreetmap.org/";
        return new OSMService("OSM", baseURL);
    }

    protected TileFactory createFactory() {
        return new OSMTileFactory();
    }
}
