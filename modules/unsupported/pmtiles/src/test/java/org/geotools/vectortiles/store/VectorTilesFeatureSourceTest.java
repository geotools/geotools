/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vectortiles.store;

import static io.tileverse.tiling.store.TileStore.Strategy.SPEED;
import static org.junit.Assert.assertEquals;

import io.tileverse.pmtiles.PMTilesReader;
import io.tileverse.pmtiles.PMTilesTestData;
import io.tileverse.tiling.matrix.TileMatrixSet;
import java.io.IOException;
import java.nio.file.Path;
import java.util.OptionalInt;
import org.geotools.api.data.Query;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.pmtiles.store.PMTilesDataStore;
import org.geotools.pmtiles.store.PMTilesDataStoreFactory;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

public class VectorTilesFeatureSourceTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    private PMTilesReader reader;
    private PMTilesDataStore store;

    @Before
    public void setUp() throws IOException {
        Path andorra = PMTilesTestData.andorra(tmpFolder.getRoot().toPath());
        reader = PMTilesReader.open(andorra.toUri());
        store = new PMTilesDataStore(new PMTilesDataStoreFactory(), reader);
        store.setNamespaceURI("http://test");
        store.setFeatureTypeFactory(CommonFactoryFinder.getFeatureTypeFactory(null));
        store.setFeatureFactory(CommonFactoryFinder.getFeatureFactory(null));
    }

    @After
    public void tearDown() throws IOException {
        if (reader != null) reader.close();
    }

    /**
     * The 256px WebMercatorQuad pyramid must be read as {@link VectorTilesDataStore#DEFAULT_DISPLAY_TILE_SIZE 512px}
     * display tiles, matching the MapLibre/Mapbox GL vector source convention, so a screen resolution that matches
     * matrix level z resolves to z-1, not z.
     */
    @Test
    public void testDisplaySizeShiftsZoomOneLevelShallower() throws IOException {
        // boundaries spans the whole pyramid, so it is visible at the shifted zoom (deterministic assertion)
        VectorTilesFeatureSource fs = (VectorTilesFeatureSource) store.getFeatureSource("boundaries");
        TileMatrixSet ms = fs.getMatrixSet();
        assertEquals("andorra pyramid is 256px", 256, ms.tileWidth());

        int rawZoom = 13;
        double resolution = ms.resolution(rawZoom);

        // the raw resolution matches rawZoom exactly against the 256px matrix
        int rawMatch = fs.getTileStore().findBestZoomLevel(resolution, SPEED, ms.minZoomLevel(), ms.maxZoomLevel());
        assertEquals(rawZoom, rawMatch);

        // reading tiles as 512px display shifts the pick one level shallower; the generalization-distance path
        // takes the screen resolution directly, so the test pins only the displayTileSize/tileWidth shift under test
        Query query = new Query("boundaries");
        query.setHints(new Hints(Hints.GEOMETRY_GENERALIZATION, resolution));
        assertEquals(OptionalInt.of(rawZoom - 1), fs.determineZoomLevel(query));
    }

    @Test
    public void testDisplayTileSizeIsConfigurablePerStore() throws IOException {
        store.setDisplayTileSize(256);

        VectorTilesFeatureSource fs = (VectorTilesFeatureSource) store.getFeatureSource("boundaries");
        TileMatrixSet ms = fs.getMatrixSet();

        int rawZoom = 13;
        double resolution = ms.resolution(rawZoom);

        // displayTileSize == tileWidth (256 == 256), no shift
        Query query = new Query("boundaries");
        query.setHints(new Hints(Hints.GEOMETRY_GENERALIZATION, resolution));
        assertEquals(OptionalInt.of(rawZoom), fs.determineZoomLevel(query));
    }

    /** A request on a zoom level resolution, with floating point noise, reads that level and not the coarser one. */
    @Test
    public void testZoomLevelResolutionNoise() throws IOException {
        assertEquals(OptionalInt.of(12), zoomLevelForResolution(13, 1 + 1e-9));
    }

    /** A request clearly coarser than a zoom level resolution still reads the coarser level. */
    @Test
    public void testZoomLevelResolutionOutsideTolerance() throws IOException {
        assertEquals(OptionalInt.of(11), zoomLevelForResolution(13, 1 + 1e-3));
    }

    /**
     * A request scale with floating point noise below a zoom level resolution reads that level and not the finer one. A
     * query without distance hints uses the {@code QUALITY} strategy, which picks the finer level on an inexact match.
     */
    @Test
    public void testRequestScaleResolutionNoise() throws IOException {
        VectorTilesFeatureSource fs = (VectorTilesFeatureSource) store.getFeatureSource("boundaries");
        double resolution = fs.getMatrixSet().resolution(13) * (1 - 1e-9);
        VectorTilesRequestScale.set(resolution / 0.00028);
        try {
            assertEquals(OptionalInt.of(12), fs.determineZoomLevel(new Query("boundaries")));
        } finally {
            VectorTilesRequestScale.clear();
        }
    }

    /** Zoom level picked for the resolution of the given matrix level, multiplied by a factor, on 512px tiles. */
    private OptionalInt zoomLevelForResolution(int matrixLevel, double factor) throws IOException {
        VectorTilesFeatureSource fs = (VectorTilesFeatureSource) store.getFeatureSource("boundaries");
        Query query = new Query("boundaries");
        double resolution = fs.getMatrixSet().resolution(matrixLevel) * factor;
        query.setHints(new Hints(Hints.GEOMETRY_GENERALIZATION, resolution));
        return fs.determineZoomLevel(query);
    }
}
