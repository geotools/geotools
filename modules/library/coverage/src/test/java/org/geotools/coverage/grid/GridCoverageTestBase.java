/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import static java.awt.Color.decode;
import static javax.measure.unit.SI.CELSIUS;
import static javax.measure.unit.SI.CUBIC_METRE;
import static javax.measure.unit.SI.GRAM;
import static javax.measure.unit.SI.MILLI;
import static org.geotools.util.NumberRange.create;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.awt.Color;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.awt.image.WritableRaster;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.AbstractList;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.media.jai.RasterFactory;

import org.geotools.coverage.Category;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.CoverageTestBase;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.factory.Hints;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.ExponentialTransform1D;
import org.geotools.referencing.operation.transform.LinearTransform1D;
import org.geotools.test.TestData;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform1D;


/**
 * Base class for grid coverage tests. This base class provides factory methods for sample
 * {@link GridCoverage2D}, and {@code assertEqual} methods for comparing values.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class GridCoverageTestBase extends CoverageTestBase {
    /**
     * Random number generator for this test.
     */
    private static final Random random = new Random(684673898634768L);

    /**
     * Returns a grid coverage filled with random values. The coordinate
     * reference system default to {@link DefaultGeographicCRS#WGS84}.
     *
     * @return A random coverage.
     */
    protected static GridCoverage2D getRandomCoverage() {
        return getRandomCoverage(DefaultGeographicCRS.WGS84);
    }

    /**
     * Returns a grid coverage filled with random values.
     *
     * @param crs The coverage coordinate reference system.
     * @return A random coverage.
     */
    protected static GridCoverage2D getRandomCoverage(final CoordinateReferenceSystem crs) {
        /*
         * Some constants used for the construction and tests of the grid coverage.
         */
        final double      SCALE = 0.1; // Scale factor for pixel transcoding.
        final double     OFFSET = 5.0; // Offset factor for pixel transcoding.
        final double PIXEL_SIZE = .25; // Pixel size (in degrees). Used in transformations.
        final int   BEGIN_VALID = 3;   // The minimal valid index for quantitative category.
        /*
         * Constructs the grid coverage. We will assume that the grid coverage use
         * (longitude,latitude) coordinates, pixels of 0.25 degrees and a lower
         * left corner at 10°W 30°N.
         */
        final GridCoverage2D  coverage;  // The final grid coverage.
        final BufferedImage      image;  // The GridCoverage's data.
        final WritableRaster    raster;  // The image's data as a raster.
        final Rectangle2D       bounds;  // The GridCoverage's envelope.
        final GridSampleDimension band;  // The only image's band.
        band = new GridSampleDimension("Temperature", new Category[] {
            new Category("No data",     null, 0),
            new Category("Land",        null, 1),
            new Category("Cloud",       null, 2),
            new Category("Temperature", null, BEGIN_VALID, 256, SCALE, OFFSET)
        }, CELSIUS);
        image  = new BufferedImage(120, 80, BufferedImage.TYPE_BYTE_INDEXED);
        raster = image.getRaster();
        for (int i=raster.getWidth(); --i>=0;) {
            for (int j=raster.getHeight(); --j>=0;) {
                raster.setSample(i,j,0, random.nextInt(256));
            }
        }
        bounds = new Rectangle2D.Double(-10, 30, PIXEL_SIZE*image.getWidth(),
                                                 PIXEL_SIZE*image.getHeight());
        final GeneralEnvelope envelope = new GeneralEnvelope(crs);
        envelope.setRange(0, bounds.getMinX(), bounds.getMaxX());
        envelope.setRange(1, bounds.getMinY(), bounds.getMaxY());
        for (int i=envelope.getDimension(); --i>=2;) {
            final double min = 10 * i;
            envelope.setRange(i, min, min + 5);
        }
        final Hints hints = new Hints(Hints.TILE_ENCODING, "raw");
        final GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(hints);
        coverage = factory.create("Test", image, envelope, new GridSampleDimension[] {band}, null, null);
        assertEquals("raw", coverage.tileEncoding);
        /*
         * Grid coverage construction finished.  Now test it.  First we test the creation of a
         * "geophysics" view. This test make sure that the 'view(type)' method does not create
         * more grid coverages than needed.
         */
        assertSame(coverage.getRenderedImage(), coverage.getRenderableImage(0,1).createDefaultRendering());
        assertSame(image.getTile(0,0), coverage.getRenderedImage().getTile(0,0));
        GridCoverage2D geophysics = coverage.view(ViewType.GEOPHYSICS);
        assertSame(coverage,        coverage.view(ViewType.PACKED));
        assertSame(coverage,      geophysics.view(ViewType.PACKED));
        assertSame(geophysics,    geophysics.view(ViewType.GEOPHYSICS));
        assertFalse( coverage.equals(geophysics));
        assertFalse( coverage.getSampleDimension(0).getSampleToGeophysics().isIdentity());
        assertTrue(geophysics.getSampleDimension(0).getSampleToGeophysics().isIdentity());
        /*
         * Compares data.
         */
        final int bandN = 0; // Band to test.
        double[] bufferCov = null;
        double[] bufferGeo = null;
        final double left  = bounds.getMinX() + (0.5*PIXEL_SIZE); // Includes translation to center
        final double upper = bounds.getMaxY() - (0.5*PIXEL_SIZE); // Includes translation to center
        final Point2D.Double point = new Point2D.Double();        // Will maps to pixel center.
        for (int j=raster.getHeight(); --j>=0;) {
            for (int i=raster.getWidth(); --i>=0;) {
                point.x = left  + PIXEL_SIZE*i;
                point.y = upper - PIXEL_SIZE*j;
                double r = raster.getSampleDouble(i,j,bandN);
                bufferCov =   coverage.evaluate(point, bufferCov);
                bufferGeo = geophysics.evaluate(point, bufferGeo);
                assertEquals(r, bufferCov[bandN], EPS);

                // Compares transcoded samples.
                if (r < BEGIN_VALID) {
                    assertTrue(Double.isNaN(bufferGeo[bandN]));
                } else {
                    assertEquals(OFFSET + SCALE*r, bufferGeo[bandN], EPS);
                }
            }
        }
        return coverage;
    }

    /**
     * An immutable list of grid coverages to be used for testing purpose. Coverages are read
     * when a the {@code get(int)} method is invoked.
     */
    protected static final List<GridCoverage2D> EXAMPLES = new AbstractList<GridCoverage2D>() {
        /**
         * The coverages returned by previous invocations.
         */
        private final GridCoverage2D[] cached = new GridCoverage2D[6];

        /**
         * Returns the number of available coverages which may be used as example.
         */
        public int size() {
            return cached.length;
        }

        /**
         * Returns a {@link GridCoverage} which may be used as a "real world" example.
         *
         * @param  number The example number, from 0 inclusive to {@link #size()} exclusive.
         * @return The "real world" grid coverage.
         */
        public synchronized GridCoverage2D get(final int number) {
            GridCoverage2D coverage = cached[number];
            if (coverage == null) {
                cached[number] = coverage = load(number);
            }
            return coverage;
        }

        /**
         * Loads the image at the given index. This is invoked by {@link #get}
         * the first time a given coverage is requested.
         */
        private GridCoverage2D load(final int number) {
            final GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(null);
            final String                   path;
            final Category[]         categories;
            final CoordinateReferenceSystem crs;
            final Rectangle2D            bounds;
            final GridSampleDimension[]   bands;
            switch (number) {
                default: {
                    throw new IndexOutOfBoundsException(String.valueOf(number));
                }
                /* ------------------------------------------------------------
                 * Thematic           :  Sea Surface Temperature (SST) in °C
                 * Data packaging     :  Indexed 8-bits
                 * Nodata values      :  [0 .. 29] and [240 .. 255] inclusive.
                 * Conversion formula :  (°C) = (packed value)/10 + 10
                 * Geographic extent  :  (41°S, 35°E) - (5°N, 80°E)
                 * Image size         :  (450 x 460) pixels
                 *
                 * This is a raster from Earth observations using a relatively straightforward
                 * conversion formula to geophysics values (a linear transform using the usual
                 * scale and offset parameters, in this case 0.1 and 10 respectively).     The
                 * interesting part of this example is that it contains a lot of nodata values.
                 */
                case 0: {
                    path = "QL95209.png";
                    crs  = DefaultGeographicCRS.WGS84;
                    categories = new Category[] {
                        new Category("Coast line", decode("#000000"), create(  0,   0)),
                        new Category("Cloud",      decode("#C3C3C3"), create(  1,   9)),
                        new Category("Unused",     decode("#822382"), create( 10,  29)),
                        new Category("Sea Surface Temperature", null, create( 30, 219), 0.1, 10.0),
                        new Category("Unused",     decode("#A0505C"), create(220, 239)),
                        new Category("Land",       decode("#D2C8A0"), create(240, 254)),
                        new Category("No data",    decode("#FFFFFF"), create(255, 255)),
                    };
                    bounds = new Rectangle2D.Double(35, -41, 45, 46);
                    bands = new GridSampleDimension[] {
                        new GridSampleDimension("Measure", categories, CELSIUS)
                    };
                    break;
                }
                /* ------------------------------------------------------------
                 * Thematic           :  Chlorophyle-a concentration in mg/m³
                 * Data packaging     :  Indexed 8-bits
                 * Nodata values      :  0 and 255
                 * Conversion formula :  (mg/m³) = 10 ^ ((packed value)*0.015 - 1.985)
                 * Geographic extent  :  (34°N, 07°W) - (45°N, 12°E)
                 * Image size         :  (300 x 175) pixels
                 *
                 * This is a raster from Earth observations using a more complex conversion
                 * formula to geophysics values (an exponential one). The usual scale and
                 * offset parameters are not enough in this case.
                 */
                case 1: {
                    path = "CHL01195.png";
                    crs  = DefaultGeographicCRS.WGS84;
                    final MathTransform1D sampleToGeophysics = (MathTransform1D)
                            ConcatenatedTransform.create(LinearTransform1D.create(0.015, -1.985),
                                                         ExponentialTransform1D.create(10, 1));
                    categories = new Category[] {
                        new Category("Land",    decode("#000000"), create(255, 255)),
                        new Category("No data", decode("#FFFFFF"), create(  0,   0)),
                        new Category("Chl-a",   null,              create(  1, 254), sampleToGeophysics)
                    };
                    bounds = new Rectangle2D.Double(-7, 34, 19, 11);
                    bands = new GridSampleDimension[] {
                        new GridSampleDimension("Measure", categories, MILLI(GRAM).divide(CUBIC_METRE))
                    };
                    break;
                }
                /* ------------------------------------------------------------
                 * Thematic           :  World Digital Elevation Model (DEM)
                 * Geographic extent  :  (90°S, 180°W) - (90°N, 180°E)
                 */
                case 2: {
                    path   = "world_dem.gif";
                    bounds = new Rectangle2D.Double(-180, -90, 360, 180);
                    crs    = DefaultGeographicCRS.WGS84;
                    bands  = null;
                    break;
                }
                /* ------------------------------------------------------------
                 * Thematic           :  World Bathymetry (DEM)
                 * Geographic extent  :  (90°S, 180°W) - (90°N, 180°E)
                 */
                case 3:{
                    path   = "BATHY.png";
                    bounds = new Rectangle2D.Double(-180, -90, 360, 180);
                    crs    = DefaultGeographicCRS.WGS84;
                    bands  = null;
                    break;
                }
                /*
                 * A float coverage. Because we use only one tile with one band, the code below
                 * is pretty similar to the code we would have if we were just setting the values
                 * in a matrix.
                 */
                case 4: {
                    final int width  = 500;
                    final int height = 500;
                    WritableRaster raster =
                            RasterFactory.createBandedRaster(DataBuffer.TYPE_FLOAT, width, height, 1, null);
                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            raster.setSample(x, y, 0, x+y);
                        }
                    }
                    final Color[] colors = new Color[] {
                        Color.BLUE, Color.CYAN, Color.WHITE, Color.YELLOW, Color.RED
                    };
                    return factory.create("Float coverage", raster,
                            new Envelope2D(DefaultGeographicCRS.WGS84, 35, -41, 35+45, -41+46),
                                        null, null, null, new Color[][] {colors}, null);
                }
                /*
                 * A single band fictitious coverage with type UINT 16.
                 * 
                 */
                case 5: {
                    final int width  = 500;
                    final int height = 500;
                    final BufferedImage image= new BufferedImage(width, height, BufferedImage.TYPE_USHORT_GRAY);
                    final WritableRaster raster =(WritableRaster) image.getData();
                    for (int y=0; y<height; y++) {
                        for (int x=0; x<width; x++) {
                            raster.setSample(x, y, 0,(int)( 1+(x+y)*65534.0/1000.0));
                           
                        }
                    }
                    image.setData(raster);
                    return factory.create("UInt16 coverage", image,
                            new Envelope2D(DefaultGeographicCRS.WGS84, 35, -41, 35+45, -41+46));
                }
            }
            /*
             * Now creates the coverage from the informations selected in the above switch
             * statement.
             */
            final RenderedImage image;
            try {
                image = ImageIO.read(TestData.getResource(GridCoverageTestBase.class, path));
            } catch (IOException e) {
                throw new AssertionError(e);
            }
            final String filename = new File(path).getName();
            final GeneralEnvelope envelope = new GeneralEnvelope(bounds);
            envelope.setCoordinateReferenceSystem(crs);
            return factory.create(filename, image, envelope, bands, null, null);
        }
    };

    /**
     * Tests the serialization of the packed and geophysics views of a grid coverage.
     *
     * @param  coverage The coverage to serialize.
     * @return The deserialized grid coverage as packed view.
     * @throws IOException if an I/O operation was needed and failed.
     * @throws ClassNotFoundException Should never happen.
     */
    protected static GridCoverage2D serialize(GridCoverage2D coverage)
            throws IOException, ClassNotFoundException
    {
        coverage.tileEncoding = null;
        /*
         * The previous line is not something that we should do.
         * But we want to test the default GridCoverage2D encoding.
         */
        final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        final ObjectOutputStream out = new ObjectOutputStream(buffer);
        try {
            out.writeObject(coverage.view(ViewType.PACKED));
            out.writeObject(coverage.view(ViewType.GEOPHYSICS));
        } finally {
            out.close();
        }
        final ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(buffer.toByteArray()));
        GridCoverage2D read;
        try {
            read = (GridCoverage2D) in.readObject(); assertSame(read, read.view(ViewType.PACKED));
            read = (GridCoverage2D) in.readObject(); assertSame(read, read.view(ViewType.GEOPHYSICS));
        } finally {
            in.close();
        }
        coverage = read.view(ViewType.PACKED);
        assertNotSame(read, coverage);
        return coverage;
    }
}
