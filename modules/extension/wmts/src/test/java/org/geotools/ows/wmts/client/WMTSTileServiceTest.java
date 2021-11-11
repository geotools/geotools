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

import java.util.Arrays;
import java.util.Set;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.MockHttpClient;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wmts.model.TileMatrix;
import org.geotools.ows.wmts.model.TileMatrixLimits;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.TileMatrixSetLink;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.junit.Assert;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;

public class WMTSTileServiceTest {

    /**
     * What's differentiates WMTS tile service from other is the usage of limits. Some of the global
     * tileset might not be available for a given layer, and should not be returned through a call
     * to findTilesInExtent.
     */
    @Test
    public void findTilesInExtentUsingLimits() throws Exception {
        final int MAX_TILES = 1000;

        double denominator = getDenominator();

        final WMTSTileService service = createWMTSTileService();

        double scale = denominator / 4;
        ReferencedEnvelope requestEnvelope =
                new ReferencedEnvelope(7.1, 12.1, 61.4, 64.6, CRS.decode("EPSG:4326", true));

        Set<Tile> tiles = service.findTilesInExtent(requestEnvelope, scale, false, MAX_TILES);
        Assert.assertFalse("Tiles shouldn't be empty.", tiles.isEmpty());
        Assert.assertEquals("There should be two tiles.", 2, tiles.size());
        Assert.assertFalse(
                "Tiles should be constrained to limits",
                tiles.stream()
                        .map(tile -> tile.getTileIdentifier())
                        .anyMatch(identifier -> identifier.getX() > 1 || identifier.getY() < 2));

        ReferencedEnvelope requestEnvelope2 =
                new ReferencedEnvelope(12.5, 15.3, 64.3, 66.0, CRS.decode("EPSG:4326", true));
        Set<Tile> tiles2 = service.findTilesInExtent(requestEnvelope2, scale, false, MAX_TILES);
        Assert.assertFalse("Tiles shouldn't be empty.", tiles2.isEmpty());
        Assert.assertEquals("There should be two tiles.", 2, tiles2.size());
    }

    private WMTSTileService createWMTSTileService() throws Exception {
        String templateUrl = "http://dummy.net/wmts";
        String tileMatrixSetIdentifier = "UTM33";
        GeometryFactory fact = JTSFactoryFinder.getGeometryFactory();
        Point topLeft = fact.createPoint(new Coordinate(-300000, 8000000));
        double denominator = getDenominator();

        TileMatrixSet tileMatrixSet = new TileMatrixSet();
        tileMatrixSet.setIdentifier(tileMatrixSetIdentifier);
        tileMatrixSet.setCRS("EPSG:32633");
        tileMatrixSet.setBbox(new CRSEnvelope("EPSG:32633", -300000, 6400000, 1300000, 8000000));
        tileMatrixSet.setMatrices(
                Arrays.asList(
                        createMatrix(
                                tileMatrixSetIdentifier + ":0",
                                tileMatrixSet,
                                topLeft,
                                denominator,
                                1,
                                1),
                        createMatrix(
                                tileMatrixSetIdentifier + ":1",
                                tileMatrixSet,
                                topLeft,
                                denominator / 2,
                                2,
                                2),
                        createMatrix(
                                tileMatrixSetIdentifier + ":2",
                                tileMatrixSet,
                                topLeft,
                                denominator / 4,
                                4,
                                4)));

        TileMatrixSetLink limits = new TileMatrixSetLink();
        limits.setIdentifier(tileMatrixSetIdentifier);
        limits.setLimits(
                Arrays.asList(
                        createMatrixLimits(tileMatrixSetIdentifier + ":0", 0, 0, 0, 0),
                        createMatrixLimits(tileMatrixSetIdentifier + ":1", 0, 0, 1, 1),
                        createMatrixLimits(tileMatrixSetIdentifier + ":2", 0, 1, 2, 3)));

        return new WMTSTileService(
                templateUrl,
                WMTSServiceType.KVP,
                createLayerWithLimits(
                        limits,
                        new CRSEnvelope(
                                new ReferencedEnvelope(
                                        4.4, 14.7, 57.8, 65.3, CRS.decode("CRS:84")))),
                "default",
                tileMatrixSet,
                new MockHttpClient());
    }

    private double getDenominator() {

        double pixelWidth = 0.00028;
        double tileSetSize = 1600000.0;
        double tileSize = 256.0;

        return tileSetSize / pixelWidth / tileSize;
    }

    private TileMatrixLimits createMatrixLimits(
            String identifier, long mincol, long maxcol, long minrow, long maxrow) {
        TileMatrixLimits limits = new TileMatrixLimits();
        limits.setTileMatix(identifier);
        limits.setMinCol(mincol);
        limits.setMaxCol(maxcol);
        limits.setMinRow(minrow);
        limits.setMaxRow(maxrow);
        return limits;
    }

    private TileMatrix createMatrix(
            String identifier,
            TileMatrixSet parentSet,
            Point topLeft,
            double denominator,
            int width,
            int height) {
        TileMatrix matrix = new TileMatrix();
        matrix.setIdentifier(identifier);
        matrix.setParent(parentSet);
        matrix.setTopLeft(topLeft);
        matrix.setDenominator(denominator);
        matrix.setMatrixWidth(width);
        matrix.setMatrixHeight(height);
        return matrix;
    }

    private WMTSLayer createLayerWithLimits(TileMatrixSetLink limitList, CRSEnvelope boundingBox) {
        WMTSLayer layer = new WMTSLayer("Top-layer");
        layer.setLatLonBoundingBox(boundingBox);
        layer.addTileMatrixLink(limitList);
        return layer;
    }
}
