/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.processing;

import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import javax.media.jai.OperationNode;

import org.opengis.coverage.grid.GridCoverage;
import org.geotools.coverage.grid.GridCoverage2D;
import static org.geotools.coverage.grid.ViewType.*;

import org.geotools.test.TestData;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests JAI operation wrapped as {@link OperatorJAI}.
 * <p>
 * <strong>NOTE:</strong>
 * This test may fails when executed on a machine without the <cite>mediaLib</cite> accelerator.
 * On Windows, the {@code mlib_jai.dll} and {@code mlib_jai_mmx.dll} files should exist in the
 * {@code jre/bin} directory, as well as {@code mlibwrapper_jai.jar} in {@code jre/lib/ext}.
 * Those {@code .dll} files should be there if JAI has been installed with the Sun standard
 * installation program ({@code jai-1_1_2_01-lib-windows-i586-jdk.exe}). With such installation,
 * everything should run fine. The {@code .dll} files are probably missing if JAI has been put in
 * the classpath by Maven, like our past attempt on the 2.1 branch.
 * <p>
 * This behavior looks like a JAI bug to me. In theory, the pure Java mode is supposed to produce
 * exactly the same result than the <cite>mediaLib</cite> native mode; just slower. This test
 * failure suggests that it is not always the case. The <cite>mediaLib</cite> native code seems
 * right in this case (the bug would be in the pure Java code).
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class OperationsTest extends GridProcessingTestBase {
    /**
     * Sample image.
     */
    private GridCoverage2D SST;

    /**
     * The grid coverage processor.
     */
    private Operations processor;

    /**
     * Fetch the processor before each test.
     */
    @Before
    public void setUp() {
        processor = Operations.DEFAULT;
        SST = EXAMPLES.get(0);
    }

    /**
     * Tests {@link Operations#subtract}.
     *
     * @todo Investigate why the color palette is lost.
     */
    @Test
    public void testSubtract() {
        double[]      constants      = new double[] {18.75};
        GridCoverage  sourceCoverage = SST.view(GEOPHYSICS);
        GridCoverage  targetCoverage = (GridCoverage) processor.subtract(sourceCoverage, constants);
        RenderedImage sourceImage    = sourceCoverage.getRenderableImage(0,1).createDefaultRendering();
        RenderedImage targetImage    = targetCoverage.getRenderableImage(0,1).createDefaultRendering();
        Raster        sourceRaster   = sourceImage.getData();
        Raster        targetRaster   = targetImage.getData();
        assertNotSame(sourceCoverage,                                targetCoverage);
        assertNotSame(sourceImage,                                   targetImage);
        assertNotSame(sourceRaster,                                  targetRaster);
        assertSame   (sourceCoverage.getCoordinateReferenceSystem(), targetCoverage.getCoordinateReferenceSystem());
        assertEquals (sourceCoverage.getEnvelope(),                  targetCoverage.getEnvelope());
        assertEquals (sourceCoverage.getGridGeometry(),              targetCoverage.getGridGeometry());
        assertEquals (sourceRaster  .getMinX(),                      targetRaster  .getMinX());
        assertEquals (sourceRaster  .getMinY(),                      targetRaster  .getMinY());
        assertEquals (sourceRaster  .getWidth(),                     targetRaster  .getWidth());
        assertEquals (sourceRaster  .getHeight(),                    targetRaster  .getHeight());
        assertEquals (0, sourceRaster.getMinX());
        assertEquals (0, sourceRaster.getMinY());
        assertEquals ("SubtractConst", ((OperationNode) targetImage).getOperationName());

        final boolean medialib = TestData.isMediaLibAvailable();
        for (int y=sourceRaster.getHeight(); --y>=0;) {
            for (int x=sourceRaster.getWidth(); --x>=0;) {
                final float s = sourceRaster.getSampleFloat(x, y, 0);
                final float t = targetRaster.getSampleFloat(x, y, 0);
                if (Float.isNaN(s)) {
                    /*
                     * For a mysterious reason (JAI bug?), the following test seems to fail when
                     * JAI is running in pure Java mode. If you get an assertion failure on this
                     * line, then make sure that "<your_jdk_path>/jre/bin/mlib_jai.dll" (Windows)
                     * or "lib/i386/libmlib_jai.so" (Linux) is presents in your JDK installation.
                     */
                    if (medialib) {
                        assertTrue(Float.isNaN(t));
                    }
                } else {
                    assertEquals(s - constants[0], t, 1E-3f);
                }
            }
        }
        if (SHOW) {
            show(targetCoverage);
        }
    }

    

    /**
     * Tests {@link Operations#gradientMagnitude}.
     *
     * @todo Investigate why the geophysics view is much more visible than the non-geophysics one.
     */
    @Test
    public void testGradientMagnitude() {
        GridCoverage  sourceCoverage = SST.view(GEOPHYSICS);
        GridCoverage  targetCoverage = (GridCoverage) processor.gradientMagnitude(sourceCoverage);
        RenderedImage sourceImage    = sourceCoverage.getRenderableImage(0,1).createDefaultRendering();
        RenderedImage targetImage    = targetCoverage.getRenderableImage(0,1).createDefaultRendering();
        Raster        sourceRaster   = sourceImage.getData();
        Raster        targetRaster   = targetImage.getData();
        assertNotSame(sourceCoverage,                                targetCoverage);
        assertNotSame(sourceImage,                                   targetImage);
        assertNotSame(sourceRaster,                                  targetRaster);
        assertSame   (sourceCoverage.getCoordinateReferenceSystem(), targetCoverage.getCoordinateReferenceSystem());
        assertEquals (sourceCoverage.getEnvelope(),                  targetCoverage.getEnvelope());
        assertEquals (sourceCoverage.getGridGeometry(),              targetCoverage.getGridGeometry());
        assertEquals (sourceRaster  .getMinX(),                      targetRaster  .getMinX());
        assertEquals (sourceRaster  .getMinY(),                      targetRaster  .getMinY());
        assertEquals (sourceRaster  .getWidth(),                     targetRaster  .getWidth());
        assertEquals (sourceRaster  .getHeight(),                    targetRaster  .getHeight());
        assertEquals (0, sourceRaster.getMinX());
        assertEquals (0, sourceRaster.getMinY());
        assertEquals ("GradientMagnitude", ((OperationNode) targetImage).getOperationName());

        assertEquals(3.95f, targetRaster.getSampleFloat(304, 310, 0), 1E-2f);
        assertEquals(1.88f, targetRaster.getSampleFloat(262, 357, 0), 1E-2f);

        if (SHOW) {
            show(targetCoverage);
        }
    }
}
