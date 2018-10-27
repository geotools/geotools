package org.geotools.process.raster;

import static org.junit.Assert.assertEquals;

import it.geosolutions.jaiext.JAIExt;
import it.geosolutions.jaiext.range.NoDataContainer;
import java.awt.image.Raster;
import java.util.HashMap;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.GeoTools;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** Unit test for the 'Multiply' process */
public class MultiplyProcessTest {

    GridCoverageFactory covFactory;

    @BeforeClass
    public static void setupJaiExt() {
        JAIExt.initJAIEXT(true, true);
    }

    @AfterClass
    public static void teardownJaiExt() {
        JAIExt.initJAIEXT(false, false);
    }

    @Before
    public void setUp() {
        covFactory = CoverageFactoryFinder.getGridCoverageFactory(GeoTools.getDefaultHints());
    }

    private void doTestMultiply() {

        float[][] grid =
                new float[][] {
                    {1, 2, 3, 4},
                    {5, 6, 8, 9},
                    {10, 11, 12, 13},
                    {14, 15, 16, 17},
                };

        HashMap properties = new HashMap<>();
        CoverageUtilities.setNoDataProperty(properties, new NoDataContainer(2));
        GridCoverage2D cov =
                covFactory.create(
                        "test",
                        grid,
                        new ReferencedEnvelope(0, 10, 0, 10, DefaultGeographicCRS.WGS84));
        GridCoverage2D coverageNoData =
                covFactory.create(
                        "nodata",
                        cov.getRenderedImage(),
                        cov.getEnvelope(),
                        cov.getSampleDimensions(),
                        null,
                        properties);

        MultiplyCoveragesProcess p = new MultiplyCoveragesProcess();
        GridCoverage2D norm = p.execute(cov, cov, null);

        float[] data = data(norm);
        for (int i = 0; i < data.length; i++) {
            assertEquals(Math.pow(grid[i / grid.length][i % grid.length], 2.), data[i], 1E-9);
        }

        GridCoverage2D nodataResult = p.execute(cov, coverageNoData, null);
        if (JAIExt.isJAIExtOperation("algebric")) {
            // Only jai EXT takes nodata into account
            assertEquals(0., data(nodataResult)[1], 1E-9);
        } else {
            assertEquals(4., data(nodataResult)[1], 1E-9);
        }
    }

    /** @throws Exception */
    @Test
    public void testMultiplyJAIExt() throws Exception {
        doTestMultiply();
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
