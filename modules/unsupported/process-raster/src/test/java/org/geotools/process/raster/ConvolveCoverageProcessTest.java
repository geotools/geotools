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
import static org.junit.Assert.assertNotEquals;

import java.awt.image.Raster;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.GeoTools;
import org.junit.Before;
import org.junit.Test;

public class ConvolveCoverageProcessTest {

    float[][] covData;
    GridCoverage2D cov;

    @Before
    public void setUp() {
        covData =
                new float[][] {
                    {1, 2, 3, 4},
                    {5, 6, 8, 9},
                    {8, 7, 6, 5},
                    {4, 3, 2, 1},
                };

        GridCoverageFactory covFactory =
                CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
        cov =
                covFactory.create(
                        "test",
                        covData,
                        new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84));
    }

    @Test
    public void testConvolveRect() throws Exception {
        ConvolveCoverageProcess p = new ConvolveCoverageProcess();

        // identity
        GridCoverage2D result = p.execute(cov, null, 0, 1, 1);
        float[] grid = data(result);
        assertEquals(covData[0][0], grid[0], 0.1);

        // do something
        result = p.execute(cov, null, 0, 2, 2);
        grid = data(result);

        assertNotEquals(covData[0][0], grid[0], 0.1);
    }

    @Test
    public void testConvolveCircle() throws Exception {
        ConvolveCoverageProcess p = new ConvolveCoverageProcess();
        GridCoverage2D result = p.execute(cov, null, 1, 0, 0);

        float[] grid = data(result);

        assertNotEquals(0d, grid[5], 0.1);
        assertNotEquals(covData[1][0], grid[5], 0.1);
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
