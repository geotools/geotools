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

public class VectorTilesZoomSelectionTest {

    @Rule
    public TemporaryFolder tmpFolder = new TemporaryFolder();

    private PMTilesReader reader;
    private PMTilesDataStore store;

    @Before
    public void setUp() throws IOException {
        Path andorra = PMTilesTestData.andorra(tmpFolder.getRoot().toPath());
        reader = new PMTilesReader(andorra);
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
     * The 256px WebMercatorQuad pyramid must be read as 512px display tiles (Mapbox/MapLibre/protomaps convention), so
     * a screen resolution that matches matrix level z resolves to z-1, not z.
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
        // takes the screen resolution directly, so the test pins only the 512/tileWidth shift under test
        Query query = new Query("boundaries");
        query.setHints(new Hints(Hints.GEOMETRY_GENERALIZATION, resolution));
        assertEquals(OptionalInt.of(rawZoom - 1), fs.determineZoomLevel(query));
    }
}
