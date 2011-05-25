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
package org.geotools.coverage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.geom.AffineTransform;
import java.awt.image.RenderedImage;

import javax.media.jai.iterator.RectIter;
import javax.media.jai.iterator.RectIterFactory;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.ViewType;
import org.geotools.coverage.grid.Viewer;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.test.TestData;
import org.opengis.coverage.Coverage;
import org.opengis.coverage.grid.GridCoverage;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.operation.MathTransform;


/**
 * Base class for tests on {@link AbstractCoverage} subclasses.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class CoverageTestBase {
    /**
     * {@code true} if the result of coverage operations should be displayed.
     * This is sometime useful for debugging purpose.
     */
    protected static boolean SHOW = TestData.isInteractiveTest();

    /**
     * Small value for comparaison of sample values. Since most grid coverage implementations in
     * Geotools 2 store geophysics values as {@code float} numbers, this {@code EPS} value must
     * be of the order of {@code float} relative precision, not {@code double}.
     */
    protected static final float EPS = 1E-5f;

    /**
     * Returns the "Sample to geophysics" transform as an affine transform, or {@code null}
     * if none. Note that the returned instance may be an immutable one, not necessarly the
     * default Java2D implementation.
     *
     * @param  coverage The coverage for which to get the "grid to CRS" affine transform.
     * @return The "grid to CRS" affine transform of the given coverage, or {@code null}
     *         if none or if the transform is not affine.
     */
    protected static AffineTransform getAffineTransform(final Coverage coverage) {
        if (coverage instanceof GridCoverage) {
            final GridGeometry geometry = ((GridCoverage) coverage).getGridGeometry();
            if (geometry != null) {
                final MathTransform gridToCRS;
                if (geometry instanceof GridGeometry2D) {
                    gridToCRS = ((GridGeometry2D) geometry).getGridToCRS();
                } else {
                    gridToCRS = geometry.getGridToCRS();
                }
                if (gridToCRS instanceof AffineTransform) {
                    return (AffineTransform) gridToCRS;
                }
            }
        }
        return null;
    }

    /**
     * Returns the scale of the "grid to CRS" transform, or {@link Double#NaN} if unknown.
     *
     * @param  coverage The coverage for which to get the "grid to CRS" scale, or {@code null}.
     * @return The "grid to CRS" scale, or {@code NaN} if none or if the transform is not affine.
     */
    protected static double getScale(final Coverage coverage) {
        final AffineTransform gridToCRS = getAffineTransform(coverage);
        return (gridToCRS != null) ? XAffineTransform.getScale(gridToCRS) : Double.NaN;
    }

    /**
     * Returns the envelope of the given coverage as a {@link GeneralEnvelope} implementation.
     *
     * @param  coverage The coverage for which to get the envelope.
     * @return The envelope of the given coverage (never {@code null}).
     */
    protected static GeneralEnvelope getGeneralEnvelope(final Coverage coverage) {
        final Envelope envelope = coverage.getEnvelope();
        assertNotNull(envelope);
        assertEquals(coverage.getCoordinateReferenceSystem(),
                     envelope.getCoordinateReferenceSystem());
        if (coverage instanceof GeneralEnvelope) {
            return (GeneralEnvelope) envelope;
        } else {
            return new GeneralEnvelope(envelope);
        }
    }

    /**
     * Compares the envelopes of two coverages for equality using the smallest
     * scale factor of their "grid to world" transform as the tolerance.
     *
     * @param expected The coverage having the expected envelope.
     * @param actual   The coverage having the actual envelope.
     */
    protected static void assertEnvelopeEquals(Coverage expected, Coverage actual) {
        final double scaleA = getScale(expected);
        final double scaleB = getScale(actual);
        final double tolerance;
        if (scaleA <= scaleB) {
            tolerance = scaleA;
        } else if (!Double.isNaN(scaleB)) {
            tolerance = scaleB;
        } else {
            tolerance = EPS;
        }
        assertTrue(getGeneralEnvelope(expected).equals(actual.getEnvelope(), tolerance, false));
    }

    /**
     * Compares two affine transforms for equality.
     *
     * @param expected The expected affine transform.
     * @param actual   The actual affine transform.
     */
    protected static void assertTransformEquals(AffineTransform expected, AffineTransform actual) {
        assertEquals("scaleX",     expected.getScaleX(),     actual.getScaleX(),     EPS);
        assertEquals("scaleY",     expected.getScaleY(),     actual.getScaleY(),     EPS);
        assertEquals("shearX",     expected.getShearX(),     actual.getShearX(),     EPS);
        assertEquals("shearY",     expected.getShearY(),     actual.getShearY(),     EPS);
        assertEquals("translateX", expected.getTranslateX(), actual.getTranslateX(), EPS);
        assertEquals("translateY", expected.getTranslateY(), actual.getTranslateY(), EPS);
    }

    /**
     * Compares the rendered view of two coverages for equality.
     *
     * @param expected The coverage containing the expected pixel values.
     * @param actual   The coverage containing the actual pixel values.
     */
    protected static void assertRasterEquals(final Coverage expected, final Coverage actual) {
        assertRasterEquals(expected.getRenderableImage(0,1).createDefaultRendering(),
                             actual.getRenderableImage(0,1).createDefaultRendering());
    }

    /**
     * Compares two rasters for equality.
     *
     * @param expected The image containing the expected pixel values.
     * @param actual   The image containing the actual pixel values.
     */
    protected static void assertRasterEquals(final RenderedImage expected, final RenderedImage actual) {
        final RectIter e = RectIterFactory.create(expected, null);
        final RectIter a = RectIterFactory.create(actual,   null);
        if (!e.finishedLines()) do {
            assertFalse(a.finishedLines());
            if (!e.finishedPixels()) do {
                assertFalse(a.finishedPixels());
                if (!e.finishedBands()) do {
                    assertFalse(a.finishedBands());
                    final float pe = e.getSampleFloat();
                    final float pa = a.getSampleFloat();
                    assertEquals(pe, pa, EPS);
                    a.nextBand();
                } while (!e.nextBandDone());
                assertTrue(a.finishedBands());
                a.nextPixel();
                a.startBands();
                e.startBands();
            } while (!e.nextPixelDone());
            assertTrue(a.finishedPixels());
            a.nextLine();
            a.startPixels();
            e.startPixels();
        } while (!e.nextLineDone());
        assertTrue(a.finishedLines());
    }

    /**
     * Show the default rendering of the specified coverage.
     * This is used for debugging only.
     *
     * @param coverage The coverage to display.
     */
    protected static void show(Coverage coverage) {
        if (coverage instanceof GridCoverage2D) {
            coverage = ((GridCoverage2D) coverage).view(ViewType.PACKED);
        }
        final RenderedImage image = coverage.getRenderableImage(0,1).createDefaultRendering();
        try {
            Class.forName("org.geotools.gui.swing.OperationTreeBrowser")
                 .getMethod("show", new Class[]{RenderedImage.class})
                 .invoke(null, new Object[]{image});
        } catch (RuntimeException e) {
            throw e;
        } catch (Exception e) {
            /*
             * The OperationTreeBrowser is not part of Geotools's core. It is optional and this
             * class should not fails if it is not presents. This is only a helper for debugging.
             */
            if (coverage instanceof GridCoverage2D) {
                Viewer.show((GridCoverage2D) coverage);
            } else {
                Viewer.show(image);
            }
        }
    }
}
