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
package org.geotools.tile;

import java.net.MalformedURLException;
import java.net.URL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.MockHttpClient;
import org.geotools.tile.impl.ZoomLevel;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Tests for implemented function's of TileService
 *
 * <p>Implementing mock classes representing a single tile
 *
 * @author Roar Brænden
 */
public class TileServiceTest {

    static final TileIdentifier MINE_TILE = new MockTileIdentifier();

    static final ReferencedEnvelope MINE_BOUNDS = new ReferencedEnvelope();

    @Test
    public void testObtainTileReturnsCachedTile() throws Exception {
        TileService service = new MockTileService();
        Tile firstTile = service.obtainTile(MINE_TILE);
        Assert.assertNotNull(firstTile);
        Tile secondTile = service.obtainTile(MINE_TILE);

        Assert.assertEquals("http://dummy.net/tiles/1", secondTile.getUrl().toString());
    }

    /**
     * A TileService representing a single tile.
     *
     * <p>Should'nt have any http calls.
     */
    private static class MockTileService extends TileService {

        private final TileFactory factory = new MockTileFactory();

        MockTileService() {
            super("TestTileService", "http://dummy.net/tiles", new MockHttpClient());
        }

        @Override
        public TileIdentifier identifyTileAtCoordinate(
                double lon, double lat, ZoomLevel zoomLevel) {
            return MINE_TILE;
        }

        @Override
        public double[] getScaleList() {
            return new double[] {1};
        }

        @Override
        public ReferencedEnvelope getBounds() {
            return MINE_BOUNDS;
        }

        @Override
        public CoordinateReferenceSystem getProjectedTileCrs() {
            return MINE_BOUNDS.getCoordinateReferenceSystem();
        }

        /** MockTileService must use the same factory. */
        @Override
        public TileFactory getTileFactory() {
            return factory;
        }
    }

    /** A TileFactory counting number of call's to create. */
    private static class MockTileFactory extends TileFactory {

        private int tilesCreated = 0;

        @Override
        public Tile create(TileIdentifier identifier, TileService service) {
            return new MockTile(identifier, ++tilesCreated);
        }

        @Override
        public Tile findTileAtCoordinate(
                double lon, double lat, ZoomLevel zoomLevel, TileService service) {
            return null;
        }

        @Override
        public ZoomLevel getZoomLevel(int zoomLevel, TileService service) {
            return null;
        }

        @Override
        public Tile findRightNeighbour(Tile tile, TileService service) {
            return null;
        }

        @Override
        public Tile findLowerNeighbour(Tile tile, TileService service) {
            return null;
        }
    }

    /** A TileIdentifier pointing at itself */
    private static class MockTileIdentifier extends TileIdentifier {
        MockTileIdentifier() {
            super(0, 0, new MockZoomLevel(), "TestTileService");
        }

        @Override
        public String getId() {
            return "TheOne";
        }

        @Override
        public String getCode() {
            return "0-0";
        }

        @Override
        public TileIdentifier getRightNeighbour() {
            return this;
        }

        @Override
        public TileIdentifier getLowerNeighbour() {
            return this;
        }
    }

    /** Representing a single tile at zoomLevel */
    private static class MockZoomLevel extends ZoomLevel {

        @Override
        public int calculateMaxTilePerRowNumber(int zoomLevel) {
            return 1;
        }

        @Override
        public int calculateMaxTilePerColNumber(int zoomLevel) {
            return 1;
        }
    }

    /** A Tile returning a url with it's number of creation. */
    private static class MockTile extends Tile {

        private final int tilesCreated;

        public MockTile(TileIdentifier tileId, int tilesCreated) {
            super(tileId, MINE_BOUNDS, 1);
            this.tilesCreated = tilesCreated;
        }

        @Override
        public URL getUrl() {
            try {
                return new URL("http://dummy.net/tiles/" + String.valueOf(tilesCreated));
            } catch (MalformedURLException e) {
                return null;
            }
        }
    }
}
