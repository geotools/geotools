/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.Point2D;
import java.awt.image.RenderedImage;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoordinates2D;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.factory.GeoTools;
import org.junit.Test;
import org.opengis.geometry.MismatchedDimensionException;

public class GridCoverageUtilTest {

    @Test
    public void testExactUpScale() {
        float[][] grid = {{1, 2}, {3, 4}};
        final GridCoverageFactory coverageFactory =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        final GridCoverage2D coverage =
                coverageFactory.create(
                        "geohashGridAgg", grid, new ReferencedEnvelope(0, 1, 0, 1, null));
        GridCoverage2D scaled = GridCoverageUtil.scale(coverage, 4, 4);
        final RenderedImage renderedImage = scaled.getRenderedImage();
        assertEquals(4, renderedImage.getWidth());
        assertEquals(4, renderedImage.getHeight());
        float[][] expected = {{1, 1, 2, 2}, {1, 1, 2, 2}, {3, 3, 4, 4}, {3, 3, 4, 4}};
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                float actual = scaled.evaluate(new GridCoordinates2D(j, i), new float[1])[0];
                assertEquals(expected[i][j], actual, 1e-10);
            }
        }
    }

    @Test
    public void testExactDownScale() {
        float[][] grid = {{1, 1, 2, 2}, {1, 1, 2, 2}, {3, 3, 4, 4}, {3, 3, 4, 4}};
        final GridCoverageFactory coverageFactory =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        final GridCoverage2D coverage =
                coverageFactory.create(
                        "geohashGridAgg", grid, new ReferencedEnvelope(0, 1, 0, 1, null));
        GridCoverage2D scaled = GridCoverageUtil.scale(coverage, 2, 2);
        final RenderedImage renderedImage = scaled.getRenderedImage();
        assertEquals(2, renderedImage.getWidth());
        assertEquals(2, renderedImage.getHeight());
        float[][] expected = {{1, 2}, {3, 4}};
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2; j++) {
                float actual = scaled.evaluate(new GridCoordinates2D(j, i), new float[1])[0];
                assertEquals(expected[i][j], actual, 1e-10);
            }
        }
    }

    @Test
    public void testInexactScale() {
        float[][] grid = {{1, 2}, {3, 4}};
        final GridCoverageFactory coverageFactory =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        final GridCoverage2D coverage =
                coverageFactory.create(
                        "geohashGridAgg", grid, new ReferencedEnvelope(0, 1, 0, 1, null));
        GridCoverage2D scaled = GridCoverageUtil.scale(coverage, 3, 7);
        final RenderedImage renderedImage = scaled.getRenderedImage();
        assertEquals(7, renderedImage.getWidth());
        assertEquals(3, renderedImage.getHeight());
    }

    @Test
    public void testSmallScale() {
        float[][] grid = new float[1500][1500];
        final GridCoverageFactory coverageFactory =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        final GridCoverage2D coverage =
                coverageFactory.create(
                        "geohashGridAgg", grid, new ReferencedEnvelope(0, 1, 0, 1, null));
        GridCoverage2D scaled = GridCoverageUtil.scale(coverage, 1501, 1499);
        final RenderedImage renderedImage = scaled.getRenderedImage();
        assertEquals(1499, renderedImage.getWidth());
        assertEquals(1501, renderedImage.getHeight());
    }

    @Test
    public void testLargeScale() {
        float[][] grid = new float[2][2];
        final GridCoverageFactory coverageFactory =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        final GridCoverage2D coverage =
                coverageFactory.create(
                        "geohashGridAgg", grid, new ReferencedEnvelope(0, 1, 0, 1, null));
        GridCoverage2D scaled = GridCoverageUtil.scale(coverage, 1501, 1499);
        final RenderedImage renderedImage = scaled.getRenderedImage();
        assertEquals(1499, renderedImage.getWidth());
        assertEquals(1501, renderedImage.getHeight());
    }

    @Test
    public void testCrop() throws MismatchedDimensionException {
        float[][] grid = {{3, 4}, {1, 2}};
        final GridCoverageFactory coverageFactory =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        final GridCoverage2D coverage =
                coverageFactory.create(
                        "geohashGridAgg", grid, new ReferencedEnvelope(0, 20, 0, 20, null));
        final ReferencedEnvelope envelope = new ReferencedEnvelope(10, 20, 10, 20, null);
        final GridCoverage2D croppedCoverage = GridCoverageUtil.crop(coverage, envelope);
        final RenderedImage renderedImage = croppedCoverage.getRenderedImage();
        assertEquals(1, renderedImage.getWidth());
        assertEquals(1, renderedImage.getHeight());
        assertEquals(
                4, croppedCoverage.evaluate(new Point2D.Double(15, 15), new float[1])[0], 1e-10);
    }
}
