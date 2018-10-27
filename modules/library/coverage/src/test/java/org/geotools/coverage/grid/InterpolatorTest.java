/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import it.geosolutions.jaiext.range.NoDataContainer;
import it.geosolutions.jaiext.range.Range;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.image.Raster;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import javax.media.jai.BorderExtender;
import javax.media.jai.Interpolation;
import javax.media.jai.PlanarImage;
import javax.media.jai.ROIShape;
import org.geotools.coverage.CoverageFactoryFinder;
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.util.factory.Hints;
import org.junit.Before;
import org.junit.Test;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.geometry.Envelope;

/**
 * Tests the {@link Interpolator2D} implementation.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class InterpolatorTest extends GridCoverageTestBase {
    /** The interpolators to use. */
    private Interpolation[] interpolations;

    /** Used to avoid errors if building on a system where hostname is not defined */
    private boolean hostnameDefined;

    /** Setup the {@linkplain #interpolations} values. */
    @Before
    public void setup() {
        final int[] types = {
            Interpolation.INTERP_BICUBIC,
            Interpolation.INTERP_BILINEAR,
            Interpolation.INTERP_NEAREST
        };
        interpolations = new Interpolation[types.length];
        for (int i = 0; i < interpolations.length; i++) {
            interpolations[i] = Interpolation.getInstance(types[i]);
        }

        try {
            InetAddress.getLocalHost();
            hostnameDefined = true;

        } catch (Exception ex) {
            hostnameDefined = false;
        }
    }

    /**
     * Tests bilinear intersection at pixel edges. It should be equals to the average of the four
     * pixels around.
     */
    @Test
    public void testInterpolationAtEdges() {
        // Following constant is pixel size (in degrees).
        // This constant must be identical to the one defined in 'getRandomCoverage()'
        GridCoverage2D coverage = getRandomCoverage();
        final double PIXEL_SIZE =
                XAffineTransform.getScale(
                        (AffineTransform)
                                ((GridGeometry2D) coverage.getGridGeometry()).getGridToCRS());
        final Interpolation interpolation =
                Interpolation.getInstance(Interpolation.INTERP_BILINEAR);
        coverage = Interpolator2D.create(coverage, new Interpolation[] {interpolation});
        final int band = 0; // Band to test.
        double[] buffer = null;
        final BorderExtender be = BorderExtender.createInstance(BorderExtender.BORDER_COPY);
        Rectangle rectangle =
                PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getBounds();
        rectangle =
                new Rectangle(
                        rectangle.x,
                        rectangle.y,
                        rectangle.width + interpolation.getWidth(),
                        rectangle.height + interpolation.getHeight());
        final Raster data =
                PlanarImage.wrapRenderedImage(coverage.getRenderedImage())
                        .getExtendedData(rectangle, be);
        final Envelope envelope = coverage.getEnvelope();
        final GridEnvelope range = coverage.getGridGeometry().getGridRange();
        final double left = envelope.getMinimum(0);
        final double upper = envelope.getMaximum(1);
        final Point2D.Double point = new Point2D.Double(); // Will maps to pixel upper-left corner
        for (int j = range.getSpan(1); j >= 0; --j) {
            for (int i = range.getSpan(0); i >= 0; --i) {
                point.x = left + PIXEL_SIZE * i;
                point.y = upper - PIXEL_SIZE * j;
                buffer = coverage.evaluate(point, buffer);
                double t = buffer[band];

                // Computes the expected value:
                double s00 = data.getSampleDouble(i + 0, j + 0, band);
                double s01 = data.getSampleDouble(i + 0, j + 1, band);
                double s10 = data.getSampleDouble(i + 1, j + 0, band);
                double s11 = data.getSampleDouble(i + 1, j + 1, band);
                double s = interpolation.interpolate(s00, s01, s10, s11, 0, 0);
                assertEquals(s, t, EPS);
            }
        }
    }

    /**
     * Tests bilinear intersection at pixel edges. It should be equals to the average of the four
     * pixels around.
     */
    @Test
    public void testInterpolationROINoData() {
        // Following constant is pixel size (in degrees).
        // This constant must be identical to the one defined in 'getRandomCoverage()'
        GridCoverage2D coverage = getRandomCoverage();
        final Hints hints = new Hints(Hints.TILE_ENCODING, "raw");
        final GridCoverageFactory factory = CoverageFactoryFinder.getGridCoverageFactory(hints);
        Map<String, Object> properties = new HashMap<>();
        RenderedImage src = coverage.getRenderedImage();
        // Setting ROI and NoData
        ROIShape roi =
                new ROIShape(
                        new Rectangle(
                                src.getMinX(),
                                src.getMinY(),
                                src.getWidth() / 2,
                                src.getHeight() / 2));
        CoverageUtilities.setROIProperty(properties, roi);
        NoDataContainer noDataContainer = new NoDataContainer(15);
        properties.put("GC_NODATA", noDataContainer);

        coverage =
                factory.create(
                        "Test2",
                        src,
                        coverage.getEnvelope(),
                        coverage.getSampleDimensions(),
                        null,
                        properties);
        final double PIXEL_SIZE =
                XAffineTransform.getScale(
                        (AffineTransform)
                                ((GridGeometry2D) coverage.getGridGeometry()).getGridToCRS());
        final Interpolation interpolation = Interpolation.getInstance(Interpolation.INTERP_NEAREST);
        coverage = Interpolator2D.create(coverage, new Interpolation[] {interpolation});
        final int band = 0; // Band to test.
        double[] buffer = null;
        final BorderExtender be = BorderExtender.createInstance(BorderExtender.BORDER_COPY);
        Rectangle rectangle =
                PlanarImage.wrapRenderedImage(coverage.getRenderedImage()).getBounds();
        rectangle =
                new Rectangle(
                        rectangle.x,
                        rectangle.y,
                        rectangle.width + interpolation.getWidth(),
                        rectangle.height + interpolation.getHeight());
        final Raster data =
                PlanarImage.wrapRenderedImage(coverage.getRenderedImage())
                        .getExtendedData(rectangle, be);
        final Envelope envelope = coverage.getEnvelope();
        final GridEnvelope range = coverage.getGridGeometry().getGridRange();
        final double left = envelope.getMinimum(0);
        final double upper = envelope.getMaximum(1);
        final Point2D.Double point = new Point2D.Double(); // Will maps to pixel upper-left corner

        // ROI and NOdata
        Range nodata = noDataContainer.getAsRange();
        double bkg = nodata.getMin(true).doubleValue();

        for (int j = range.getSpan(1); j >= 0; --j) {
            for (int i = range.getSpan(0); i >= 0; --i) {
                point.x = left + PIXEL_SIZE * i;
                point.y = upper - PIXEL_SIZE * j;
                buffer = coverage.evaluate(point, buffer);
                double t = buffer[band];
                if (!roi.contains(i, j)) {
                    assertEquals(bkg, t, EPS);
                } else {
                    // Computes the expected value:
                    double s00 = data.getSampleDouble(i, j, band);
                    if (nodata.contains(s00)) {
                        assertEquals(bkg, t, EPS);
                    } else {
                        double s = interpolation.interpolate(new double[][] {{s00}}, 0, 0);
                        assertEquals(s, t, EPS);
                    }
                }
            }
        }
    }

    /**
     * Tests the serialization of a grid coverage.
     *
     * @throws IOException if an I/O operation was needed and failed.
     * @throws ClassNotFoundException Should never happen.
     */
    @Test
    public void testSerialization() throws IOException, ClassNotFoundException {
        if (hostnameDefined) {
            GridCoverage2D coverage = EXAMPLES.get(0);
            coverage = Interpolator2D.create(coverage, interpolations);
            GridCoverage2D serial = serialize(coverage);
            assertNotSame(coverage, serial);
            assertEquals(Interpolator2D.class, serial.getClass());
            // conversions of NaN values which may be the expected ones.
            assertRasterEquals(coverage, serial);
        }
    }
}
