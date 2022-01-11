/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.raster;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.grid.io.AbstractGridCoverage2DReader;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.gce.geotiff.GeoTiffReader;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.GridReaderLayer;
import org.geotools.map.MapContent;
import org.geotools.referencing.CRS;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.test.TestData;
import org.geotools.xml.styling.SLDParser;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.LineString;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Unit tests for ContourProcess.
 *
 * @author Michael Bedward
 * @since 8.0
 * @version $Id$
 */
public class ContourProcessTest {

    private static final double TOL = 1.0e-6;

    private static final GridCoverageFactory covFactory =
            CoverageFactoryFinder.getGridCoverageFactory(null);
    private ContourProcess process;

    @Before
    public void setup() throws FileNotFoundException, IOException {
        process = new ContourProcess();
        TestData.unzipFile(this, "contours.zip");
    }
    /**
     * Creates a coverage with just two rows where values are constant within rows and differ
     * between rows, then checks for correctly generated single contour between rows.
     */
    @Test
    public void singleContourInVerticalGradient() {
        final int COVERAGE_COLS = 10;
        final int COVERAGE_ROWS = 2;

        final double CELL_SIZE = 100;

        final ReferencedEnvelope WORLD =
                new ReferencedEnvelope(
                        1000,
                        1000 + COVERAGE_COLS * CELL_SIZE,
                        5000,
                        5000 + COVERAGE_ROWS * CELL_SIZE,
                        null);

        final float DATA_MIN = 100;
        final float DATA_MAX = 200;

        GridCoverage2D cov =
                createVerticalGradient(COVERAGE_ROWS, COVERAGE_COLS, WORLD, DATA_MIN, DATA_MAX);

        final double levelValue = (DATA_MIN + DATA_MAX) / 2;

        SimpleFeatureCollection fc =
                process.execute(cov, 0, new double[] {levelValue}, null, null, null, null, null);

        // Should be a single contour
        assertEquals(1, fc.size());

        SimpleFeature feature = DataUtilities.first(fc);

        // Check contour value
        Double value = (Double) feature.getAttribute("value");
        assertEquals(levelValue, value, TOL);

        LineString contour = (LineString) feature.getDefaultGeometry();
        Coordinate[] coords = contour.getCoordinates();

        // Contour should have had co-linear vertices removed by default
        assertEquals(2, coords.length);

        // Contour end-point X ordinates should be within half cell-width of
        // coverage X extrema
        double minX = Math.min(coords[0].x, coords[1].x);
        assertEquals(WORLD.getMinX(), minX, CELL_SIZE / 2 + TOL);
        double maxX = Math.max(coords[0].x, coords[1].x);
        assertEquals(WORLD.getMaxX(), maxX, CELL_SIZE / 2 + TOL);

        // Contour Y ordinate should be at mid-Y of coverage and
        // contour should be horizontal
        double expectedY = (WORLD.getMinY() + WORLD.getMaxY()) / 2;
        assertEquals(expectedY, coords[0].y, TOL);
        assertEquals(expectedY, coords[1].y, TOL);
    }

    /** Tests that the process doesn't blow up when there are no contours to return */
    @Test
    public void noContours() {
        // Coverage with values in range [0, 10]
        GridCoverage2D cov = createVerticalGradient(10, 10, null, 0, 10);

        // Run process asking for contours at level = 20
        SimpleFeatureCollection fc =
                process.execute(cov, 0, new double[] {20}, null, null, null, null, null);
        assertNotNull(fc);
        assertTrue(fc.isEmpty());
    }

    /**
     * Tests that the process doesn't blow up when the contour process provides invalid lineStrings
     */
    @Test
    public void invalidLinestrings()
            throws FileNotFoundException, IOException, NoSuchAuthorityCodeException,
                    FactoryException {
        File input = TestData.file(ContourProcessTest.class, "contoursample.tif");
        AbstractGridFormat format = GridFormatFinder.findFormat(input);
        AbstractGridCoverage2DReader reader = null;
        boolean success = true;
        try {
            reader = format.getReader(input);

            GridCoverage2D coverage = reader.read(null);
            CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);

            // Run process asking for contours at different levels
            double[] levels = new double[40];
            double start = -2.0;
            for (int i = 0; i < levels.length; i++) {
                levels[i] = start + (0.2 * i);
            }

            SimpleFeatureCollection fc =
                    process.execute(coverage, 0, levels, null, null, null, null, null);
            try (SimpleFeatureIterator features = fc.features()) {
                while (features.hasNext()) {
                    // Apply a set of transformations to the feature geometries to make sure
                    // no exceptions are thrown. (This would happen when dealing with invalid
                    // lineStrings, resulting into IllegalArgumentException)
                    SimpleFeature feature = features.next();
                    Geometry geometry = (Geometry) feature.getDefaultGeometry();
                    ReferencedEnvelope ge =
                            new ReferencedEnvelope(geometry.getEnvelopeInternal(), wgs84);
                    JTS.toGeometry((Envelope) ge);
                }
            }
        } catch (IllegalArgumentException iae) {
            success = false;
        } finally {
            if (reader != null) {
                try {
                    reader.dispose();
                } catch (Throwable t) {
                    // Ignore exception on dispose
                }
            }
        }
        assertTrue(success);
    }

    private GridCoverage2D createVerticalGradient(
            final int dataRows,
            final int dataCols,
            ReferencedEnvelope worldEnv,
            final float startValue,
            final float endValue) {

        if (dataRows < 2) {
            throw new IllegalArgumentException("dataRows must be >= 2");
        }
        if (dataCols < 1) {
            throw new IllegalArgumentException("dataCols must be positive");
        }

        if (worldEnv == null) {
            worldEnv = new ReferencedEnvelope(0, dataCols, 0, dataRows, null);
        }

        float[][] DATA = new float[dataRows][dataCols];
        float delta = (endValue - startValue) / (dataRows - 1);

        for (int iy = 0; iy < dataRows; iy++) {
            float value = startValue + iy * delta;
            for (int ix = 0; ix < dataCols; ix++) {
                DATA[iy][ix] = value;
            }
            value += delta;
        }

        return covFactory.create("coverage", DATA, worldEnv);
    }

    @Test
    public void testContourWithTransformAndReprojection_WithOpacitySingleFTS() throws Exception {
        // The style with a single FeatureTypeStyle will use plain rendering.
        // The style with opacity will not use the screenmap.
        assertWhiteSamples("contour_with_opacity.sld");
    }

    @Test
    public void testContourWithTransformAndReprojection_WithoutOpacitySingleFTS() throws Exception {
        // The style with a single FeatureTypeStyle will use plain rendering.
        // The style without opacity will use the screenmap.
        assertWhiteSamples("contour_without_opacity.sld");
    }

    @Test
    public void testContourWithTransformAndReprojection_WithOpacityMultiFTS() throws Exception {
        // The style with multiple FeatureTypeStyles will use optimized rendering.
        // The style with opacity will not use the screenmap.
        assertWhiteSamples("contour_multi_fts_with_opacity.sld");
    }

    @Test
    public void testContourWithTransformAndReprojection_WithoutOpacityMultiFTS() throws Exception {
        // The style with multiple FeatureTypeStyles will use optimized rendering.
        // The style without opacity will use the screenmap.
        assertWhiteSamples("contour_multi_fts_without_opacity.sld");
    }

    private void assertWhiteSamples(String styleName) throws Exception {
        StyleFactory factory = CommonFactoryFinder.getStyleFactory(null);
        URL url = TestData.getResource(this, styleName);
        Style style = new SLDParser(factory, url).readXML()[0];

        // load the sample coverage with a Mercator projection
        GeoTiffReader reader = new GeoTiffReader(TestData.file(this, "mer.tiff"));
        MapContent mc = new MapContent();
        mc.addLayer(new GridReaderLayer(reader, style));

        StreamingRenderer renderer = new StreamingRenderer();
        Map<Object, Object> rendererParams = new HashMap<>();
        rendererParams.put(StreamingRenderer.ADVANCED_PROJECTION_HANDLING_KEY, true);
        rendererParams.put(StreamingRenderer.CONTINUOUS_MAP_WRAPPING, true);
        renderer.setRendererHints(rendererParams);
        renderer.setMapContent(mc);

        // render the image with the EPSG:4326 projection
        int width = 256;
        int height = 256;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = image.createGraphics();
        renderer.paint(
                graphics,
                new Rectangle(image.getWidth(), image.getHeight()),
                new ReferencedEnvelope(-71, -68, 6, 9, CRS.decode("EPSG:4326", true)));
        graphics.dispose();
        Raster raster = image.getData();

        // count the number of white samples in the rendered image
        int whiteSamples = 0;
        for (int i = 0; i < 256; i++) {
            for (int j = 0; j < 256; j++) {
                whiteSamples += raster.getSample(i, j, 0) == 255 ? 1 : 0;
            }
        }

        // The white contour lines with the sample data and styles should cover about
        // 0.25% of the rendered image regardless of whether the screenmap is used.
        // The bug that this is testing would cause a large number of diagonal lines
        // to be drawn across the image so the percentage of white pixels with the
        // bug would be significantly higher.
        double area = width * height;
        assertEquals(0.0025, whiteSamples / area, 0.0005);
    }

    @Test
    public void testContourWithNoDataProperty() {
        // Create a coverage with a NoData value only in the properties.
        ReferencedEnvelope envelope = new ReferencedEnvelope(0, 3, 0, 3, null);
        float[][] matrix = {{0, 0, 0}, {0, 9999, 0}, {0, 0, 0}};
        GridCoverage2D cov = covFactory.create("coverage", matrix, envelope);
        Map<Object, Object> properties = new HashMap<>();
        properties.put(NoDataContainer.GC_NODATA, new NoDataContainer(9999));
        cov =
                covFactory.create(
                        cov.getName(),
                        cov.getRenderedImage(),
                        cov.getEnvelope(),
                        cov.getSampleDimensions(),
                        null,
                        properties);
        SimpleFeatureCollection fc =
                process.execute(cov, 0, new double[] {20}, null, null, null, null, null);
        // Before fix, the NoData value will be interpolated and create a contour.
        // After fix, the NoData value will be ignored so there will be no contours.
        assertNotNull(fc);
        assertTrue(fc.isEmpty());
    }
}
