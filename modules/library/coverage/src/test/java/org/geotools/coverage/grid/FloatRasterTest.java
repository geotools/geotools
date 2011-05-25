/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid;

import java.awt.Color;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import javax.media.jai.RasterFactory;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Test;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * Tests the creation of a grid coverage using floating point value.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class FloatRasterTest extends GridCoverageTestBase {
    /**
     * Tests the creation of a floating point {@link WritableRaster}.
     */
    @Test
    public void testRaster() {
        /*
         * Set the pixel values.  Because we use only one tile with one band, the code below
         * is pretty similar to the code we would have if we were just setting the values in
         * a matrix.
         */
        final int width  = 500;
        final int height = 500;
        WritableRaster raster =
                RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                raster.setSample(x, y, 0, x+y);
            }
        }
        /*
         * Set some metadata (the CRS, the geographic envelope, etc.) and display the image.
         * The display may be slow, since the translation from floating-point values to some
         * color (or grayscale) is performed on the fly everytime the image is rendered.
         */
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        Envelope envelope = new Envelope2D(crs, 0, 0, 30, 30);
        GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        GridCoverage gc = factory.create("My grayscale coverage", raster, envelope);
        if (SHOW) {
            ((GridCoverage2D) gc).show(); // Convenience method specific to Geotools.
        }
        /*
         * The above example created a grayscale image. The example below creates a new grid
         * coverage for the same data, but using a specified color map. Note that the factory
         * used allows more details to be specified, for example units. Setting some of those
         * arguments to null (as in this example) lets GridCoverage computes automatically a
         * default value.
         */
        Color[] colors = new Color[] {Color.BLUE, Color.CYAN, Color.WHITE, Color.YELLOW, Color.RED};
        gc = factory.create("My colored coverage", raster, envelope,
                            null, null, null, new Color[][] {colors}, null);
        if (SHOW) {
            ((GridCoverage2D) gc).view(ViewType.RENDERED).show();
        }
    }

    /**
     * Tests the creation of a floating point matrix.
     */
    @Test
    public void testMatrix() {
        final int width  = 500;
        final int height = 500;
        final float[][] matrix = new float[height][width];
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                matrix[y][x] = x+y;
            }
        }
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        Envelope envelope = new Envelope2D(crs, 0, 0, 30, 30);
        GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
        GridCoverage gc = factory.create("My grayscale matrix", matrix, envelope);
        if (SHOW) {
            ((GridCoverage2D) gc).show(); // Convenience method specific to Geotools.
        }
    }
}
