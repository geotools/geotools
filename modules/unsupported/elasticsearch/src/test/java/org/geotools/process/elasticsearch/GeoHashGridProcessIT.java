/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.elasticsearch;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.awt.image.DataBuffer;
import java.awt.image.DataBufferFloat;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.stream.IntStream;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.data.elasticsearch.ElasticLayerConfiguration;
import org.geotools.data.elasticsearch.ElasticTestSupport;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Before;
import org.junit.Test;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GeoHashGridProcessIT extends ElasticTestSupport {

    @Test
    public void testAutomaticAggregation() throws Exception {
        init();

        ContentFeatureCollection features = featureSource.getFeatures();
        String aggregationDefinition = null;
        GridCoverage2D grid =
                new GeoHashGridProcess()
                        .execute(
                                features,
                                "Basic",
                                null,
                                null,
                                null,
                                null,
                                false,
                                new ReferencedEnvelope(
                                        -180, 180, -90, 90, DefaultGeographicCRS.WGS84),
                                360,
                                180,
                                aggregationDefinition /* agg definition */,
                                null,
                                null);
        // automatic aggregation should have created a geohash with precision
        assertNotNull(grid);
        // precision = 3 has been chosen, level 4 would have too many cells
        RenderedImage ri = grid.getRenderedImage();
        assertEquals(256, ri.getWidth());
        assertEquals(128, ri.getHeight());
    }

    @Before
    @Override
    public void init() throws Exception {
        super.init();
    }

    @Test
    public void testValues() throws Exception {
        checkAutomaticGrid(featureSource);
    }

    @Test
    public void testScrollingEnabled() throws Exception {
        dataStore.setScrollEnabled(true);
        dataStore.setScrollSize(100l);

        try {
            checkAutomaticGrid(featureSource);
        } finally {
            dataStore.setScrollEnabled(false);
            dataStore.setScrollSize(null);
        }
    }

    private void checkAutomaticGrid(ContentFeatureSource fs) throws IOException {
        // force it to a small grid that's easy to check
        ContentFeatureCollection features = fs.getFeatures();
        String aggregationDefinition = null;
        GridCoverage2D grid =
                new GeoHashGridProcess()
                        .execute(
                                features,
                                "Basic",
                                null,
                                null,
                                null,
                                null,
                                false,
                                new ReferencedEnvelope(
                                        -180, 180, -90, 90, DefaultGeographicCRS.WGS84),
                                8,
                                4,
                                aggregationDefinition /* agg definition */,
                                null,
                                null);
        // automatic aggregation should have created a geohash with precision
        assertNotNull(grid);
        // precision = 1 has been chosen
        RenderedImage ri = grid.getRenderedImage();
        assertEquals(8, ri.getWidth());
        assertEquals(4, ri.getHeight());
        // check data type and extract array
        assertEquals(DataBuffer.TYPE_FLOAT, ri.getData().getTransferType());
        float[] data = ((DataBufferFloat) ri.getData().getDataBuffer()).getData();
        // all 11 values are in geohash "s", fifth cell of the second row
        for (int i = 0; i < data.length; i++) {
            if (i == 12) assertEquals(11, data[i], 0f);
            else assertEquals(0, data[i], 0f);
        }
    }

    @Test
    public void testReprojected() throws Exception {
        ReferencedEnvelope base =
                new ReferencedEnvelope(-50, 50, -50, 50, DefaultGeographicCRS.WGS84);
        CoordinateReferenceSystem webMercator = CRS.decode("EPSG:3857", true);
        ReferencedEnvelope transformed = base.transform(webMercator, true);

        ContentFeatureCollection features = featureSource.getFeatures();
        String aggregationDefinition = null;
        GridCoverage2D grid =
                new GeoHashGridProcess()
                        .execute(
                                features,
                                "Basic",
                                null,
                                null,
                                null,
                                null,
                                false,
                                transformed,
                                12,
                                12,
                                aggregationDefinition /* agg definition */,
                                null,
                                null);
        assertNotNull(grid);
        // the output is a GeoHash grid, always in WGS84
        assertEquals(DefaultGeographicCRS.WGS84, grid.getCoordinateReferenceSystem2D());
        // Locked on precision 2 containing the data (with a bit of padding due to reprojection)
        // At precision 2 cells are 11.25 by 5.625 degrees
        Envelope envelope = grid.getEnvelope();
        assertEquals(-67.5, envelope.getMinimum(0), 0d);
        assertEquals(-61.875, envelope.getMinimum(1), 0d);
        assertEquals(56.25, envelope.getMaximum(0), 0d);
        assertEquals(61.875, envelope.getMaximum(1), 0d);
        // smaller area, smaller grid (at precision 2 cells are rectangular)
        RenderedImage ri = grid.getRenderedImage();
        assertEquals(11, ri.getWidth());
        assertEquals(22, ri.getHeight());
        // check data type and extract array
        assertEquals(DataBuffer.TYPE_FLOAT, ri.getData().getTransferType());
        float[] data = ((DataBufferFloat) ri.getData().getDataBuffer()).getData();
        // at this precision data is spread out in various cells, let's check the total
        int matches = (int) IntStream.range(0, data.length).mapToDouble(i -> data[i]).sum();
        assertEquals(11, matches);
    }

    @Test
    public void testRenamedGeometry() throws Exception {
        ElasticLayerConfiguration renaming = new ElasticLayerConfiguration(config);
        renaming.getAttributes().stream()
                .filter(a -> a.isDefaultGeometry())
                .forEach(a -> a.setCustomName("myGeometry"));
        final String RENAMED_TYPE_NAME = "renamed";
        renaming.setLayerName(RENAMED_TYPE_NAME);

        dataStore.setLayerConfiguration(renaming);
        ContentFeatureSource fs = dataStore.getFeatureSource(RENAMED_TYPE_NAME);
        checkAutomaticGrid(fs);
    }
}
