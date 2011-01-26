/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.coverage;

// J2SE and JAI dependencies
import java.awt.Color;
import java.awt.image.DataBuffer;
import java.awt.image.WritableRaster;

import javax.media.jai.RasterFactory;

import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridCoverageFactory;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * A simple demo computing a {@linkplain WritableRaster raster} and displaying it. The raster uses
 * {@code float} data type with arbitrary sample values. This demo consider the image as one and
 * only one tile. Consequently, sample values are set directly in the raster (no need to deal for
 * multi-tiles).
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class FloatRasterDemo extends junit.framework.TestCase {
    /**
     * Run the demo.
     */
    public static void main(String[] args) {
        /*
         * Set the pixel values.  Because we use only one tile with one band, the code below
         * is pretty similar to the code we would have if we were just setting the values in
         * a matrix.
         */
        final int width  = 500;
        final int height = 500;
        WritableRaster raster = RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT,
                                                                 width, height, 1, null);
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
        ((GridCoverage2D) gc).show(); // Convenience method specific to Geotools.
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
        ((GridCoverage2D) gc).geophysics(false).show();
    }

    public void testCoverage() {
        main(null);
    }
}
