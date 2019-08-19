/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.image.Raster;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.GeoTools;
import org.junit.Before;
import org.junit.Test;

public class NormalizeCoverageProcessTest {

    GridCoverageFactory covFactory;

    @Before
    public void setUp() {
        covFactory = CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
    }

    @Test
    public void test() throws Exception {
        float[][] grid =
                new float[][] {
                    {1, 2, 3, 4},
                    {5, 6, 8, 9},
                    {10, 11, 12, 13},
                    {14, 15, 16, 17},
                };

        GridCoverage2D cov =
                covFactory.create(
                        "test",
                        grid,
                        new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84));
        NormalizeCoverageProcess p = new NormalizeCoverageProcess();
        GridCoverage2D norm = p.execute(cov);

        float[] data = data(norm);
        for (int i = 0; i < data.length; i++) {
            assertEquals(grid[i / grid.length][i % grid.length] / 17f, data[i], 1E-9);
        }
    }

    @Test
    public void testZeroCoverage() throws Exception {
        float[][] grid =
                new float[][] {
                    {0, 0},
                    {0, 0},
                };

        GridCoverage2D cov =
                covFactory.create(
                        "test",
                        grid,
                        new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84));
        NormalizeCoverageProcess p = new NormalizeCoverageProcess();
        GridCoverage2D norm = p.execute(cov);

        float[] data = data(norm);
        for (int i = 0; i < data.length; i++) {
            assertEquals(0f, data[i], 1E-9);
        }
    }

    float[] data(GridCoverage2D cov) {
        Raster data = cov.getRenderedImage().getData();
        int w = data.getWidth();
        int h = data.getHeight();

        float[] grid = new float[w * h];
        data.getDataElements(0, 0, w, h, grid);

        return grid;
    }
}
