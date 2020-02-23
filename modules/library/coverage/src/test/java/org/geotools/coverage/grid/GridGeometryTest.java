/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2015, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.PixelTranslation;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.junit.Test;
import org.locationtech.jts.geom.Envelope;
import org.opengis.coverage.grid.GridEnvelope;
import org.opengis.coverage.grid.GridGeometry;
import org.opengis.geometry.DirectPosition;
import org.opengis.metadata.spatial.PixelOrientation;
import org.opengis.referencing.datum.PixelInCell;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.TransformException;

/**
 * Test the {@link GridGeometry} implementation.
 *
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public final class GridGeometryTest extends GridCoverageTestBase {

    static final double EPS = 1e-6;

    /** Tests the construction with an identity transform. */
    @Test
    public void testIdentity() {
        final int[] lower = new int[] {0, 0, 2};
        final int[] upper = new int[] {100, 200, 4};
        final MathTransform identity = IdentityTransform.create(3);
        GridGeometry2D gg;
        try {
            gg = new GridGeometry2D(new GeneralGridEnvelope(lower, upper, false), identity, null);
            fail();
        } catch (IllegalArgumentException e) {
            // This is the expected dimension.
        }
        upper[2] = 3;
        gg = new GridGeometry2D(new GeneralGridEnvelope(lower, upper, false), identity, null);
        assertTrue(identity.isIdentity());
        assertTrue(gg.getGridToCRS().isIdentity());
        assertTrue(gg.getGridToCRS2D().isIdentity());
        assertEquals(3, gg.getGridToCRS().getSourceDimensions());
        assertEquals(2, gg.getGridToCRS2D().getSourceDimensions());
        assertTrue(gg.getGridToCRS2D() instanceof AffineTransform);
        /*
         * Tests with a pixel orientation.
         */
        AffineTransform tr = (AffineTransform) gg.getGridToCRS2D(PixelOrientation.CENTER);
        assertTrue(tr.isIdentity());
        tr = (AffineTransform) gg.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
        assertFalse(tr.isIdentity());
        assertEquals(AffineTransform.TYPE_TRANSLATION, tr.getType());
        assertEquals(-0.5, tr.getTranslateX(), 0);
        assertEquals(-0.5, tr.getTranslateY(), 0);
        tr = (AffineTransform) gg.getGridToCRS2D(PixelOrientation.valueOf("LOWER"));
        assertEquals(AffineTransform.TYPE_TRANSLATION, tr.getType());
        assertEquals(0.0, tr.getTranslateX(), 0);
        assertEquals(0.5, tr.getTranslateY(), 0);
    }

    /** Tests the construction from an envelope. */
    @Test
    public void testEnvelope()
            throws InvalidGridGeometryException, NoninvertibleTransformException,
                    TransformException {
        final int[] lower = new int[] {0, 0, 4};
        final int[] upper = new int[] {90, 45, 5};
        final double[] minimum = new double[] {-180, -90, 9};
        final double[] maximum = new double[] {+180, +90, 10};
        final GridGeometry2D gg =
                new GridGeometry2D(
                        new GeneralGridEnvelope(lower, upper, false),
                        new GeneralEnvelope(minimum, maximum));
        final AffineTransform tr = (AffineTransform) gg.getGridToCRS2D();
        assertEquals(
                AffineTransform.TYPE_UNIFORM_SCALE
                        | AffineTransform.TYPE_TRANSLATION
                        | AffineTransform.TYPE_FLIP,
                tr.getType());

        assertEquals(4, tr.getScaleX(), 0);
        assertEquals(-4, tr.getScaleY(), 0);
        assertEquals(-178, tr.getTranslateX(), 0);
        assertEquals(88, tr.getTranslateY(), 0);

        final MathTransform transform =
                PixelTranslation.translate(
                        gg.getGridToCRS2D(), PixelInCell.CELL_CENTER, PixelInCell.CELL_CORNER);
        final GeneralEnvelope envelope = CRS.transform(transform.inverse(), gg.getEnvelope2D());
        final GeneralGridEnvelope ge =
                new GeneralGridEnvelope(envelope, PixelInCell.CELL_CORNER, true);
        assertEquals(0, ge.getLow(0), 0);
        assertEquals(0, ge.getLow(1), 0);
        assertEquals(90, ge.getHigh(0), 0);
        assertEquals(45, ge.getHigh(1), 0);
    }

    /** Tests construction with 0.5 pixel translations. */
    @Test
    public void testPixelInCell() {
        final MathTransform identity = IdentityTransform.create(4);
        final int[] lower = new int[] {100, 300, 3, 6};
        final int[] upper = new int[] {200, 400, 4, 7};
        final GeneralGridEnvelope range = new GeneralGridEnvelope(lower, upper, false);
        GridGeometry2D gg =
                new GridGeometry2D(range, PixelInCell.CELL_CORNER, identity, null, null);

        assertSame(identity, gg.getGridToCRS(PixelInCell.CELL_CORNER));
        assertFalse(identity.equals(gg.getGridToCRS(PixelInCell.CELL_CENTER)));
        assertFalse(identity.equals(gg.getGridToCRS(PixelOrientation.CENTER)));
        assertSame(
                gg.getGridToCRS(PixelInCell.CELL_CENTER), gg.getGridToCRS(PixelOrientation.CENTER));

        AffineTransform tr = (AffineTransform) gg.getGridToCRS2D(PixelOrientation.CENTER);
        assertFalse(tr.isIdentity());
        assertEquals(AffineTransform.TYPE_TRANSLATION, tr.getType());
        assertEquals(0.5, tr.getTranslateX(), 0);
        assertEquals(0.5, tr.getTranslateY(), 0);

        tr = (AffineTransform) gg.getGridToCRS2D(PixelOrientation.UPPER_LEFT);
        assertTrue(tr.isIdentity());
    }

    @Test
    public void testWorldToGridPoint() throws Exception {
        GridGeometry2D gg = getRandomCoverage().getGridGeometry();

        Envelope2D worldBounds = gg.getEnvelope2D();
        GridEnvelope2D gridBounds = gg.getGridRange2D();
        GridCoordinates2D gridExp = new GridCoordinates2D();

        DirectPosition worldPoint = worldBounds.getLowerCorner();
        GridCoordinates2D gridCalc = gg.worldToGrid(worldPoint);
        gridExp.setLocation(0, gridBounds.height - 1);
        assertTrue(gridExp.equals(gridCalc));

        worldPoint = worldBounds.getUpperCorner();
        gridCalc = gg.worldToGrid(worldPoint);
        gridExp.setLocation(gridBounds.width - 1, 0);
        assertTrue(gridExp.equals(gridCalc));

        // This is a specific example that previously broke this unit test
        Double minX = -78.523;
        Double minY = 38.010;
        Double maxX = -78.451;
        Double maxY = 38.069;
        int width = 400;
        int height = 300;

        ReferencedEnvelope bounds =
                new ReferencedEnvelope(
                        new Envelope(minX, maxX, minY, maxY), DefaultGeographicCRS.WGS84);
        Rectangle rect = new Rectangle(0, 0, width, height);
        GeneralGridEnvelope ggEnvelope = new GeneralGridEnvelope(rect, bounds.getDimension());
        GridGeometry2D gm = new GridGeometry2D(ggEnvelope, bounds);
        gridExp.setLocation(0, height - 1);
        GridCoordinates2D gridCalcException = gm.worldToGrid(bounds.getLowerCorner());
        assertTrue(gridCalcException.equals(gridExp));
    }

    @Test
    public void testWorldToGridEnvelope() throws Exception {
        GridGeometry2D gg = getRandomCoverage().getGridGeometry();

        Envelope2D worldBounds = gg.getEnvelope2D();
        GridEnvelope2D gridBounds = gg.getGridRange2D();

        GridEnvelope2D gridEnv = gg.worldToGrid(worldBounds);
        assertTrue(gridBounds.equals(gridEnv));

        // test sub-area conversion by creating an envelope that excludes
        // the first and last grid row and col centres
        double cellWidthX = worldBounds.getWidth() / gridBounds.getWidth();
        double cellWidthY = worldBounds.getHeight() / gridBounds.getHeight();

        Envelope2D subEnv =
                new Envelope2D(
                        gg.getCoordinateReferenceSystem2D(),
                        worldBounds.getMinX() + cellWidthX * 0.6,
                        worldBounds.getMinY() + cellWidthY * 0.6,
                        worldBounds.getWidth() - cellWidthX * 1.2,
                        worldBounds.getHeight() - cellWidthY * 1.2);

        gridEnv = gg.worldToGrid(subEnv);

        GridEnvelope2D expectedEnv =
                new GridEnvelope2D(
                        gridBounds.x + 1,
                        gridBounds.y + 1,
                        gridBounds.width - 2,
                        gridBounds.height - 2);

        assertTrue(gridEnv.equals(expectedEnv));
    }

    @Test
    public void testGridToWorldPoint() throws Exception {
        final double TOL = 1.0E-6;

        GridGeometry2D gg = getRandomCoverage().getGridGeometry();

        Envelope2D worldBounds = gg.getEnvelope2D();
        GridEnvelope2D gridBounds = gg.getGridRange2D();

        double cellWidthX = worldBounds.getWidth() / gridBounds.getWidth();
        double cellWidthY = worldBounds.getHeight() / gridBounds.getHeight();

        GridCoordinates2D low = gridBounds.getLow();
        DirectPosition2D dp = (DirectPosition2D) gg.gridToWorld(low);

        assertTrue(Math.abs(dp.x - (cellWidthX / 2) - worldBounds.getMinX()) < TOL);
        assertTrue(Math.abs(dp.y + (cellWidthY / 2) - worldBounds.getMaxY()) < TOL);

        GridCoordinates2D high = gridBounds.getHigh();
        dp = (DirectPosition2D) gg.gridToWorld(high);

        assertTrue(Math.abs(dp.x + (cellWidthX / 2) - worldBounds.getMaxX()) < TOL);
        assertTrue(Math.abs(dp.y - (cellWidthY / 2) - worldBounds.getMinY()) < TOL);
    }

    @Test
    public void testGridToWorldEnvelope() throws Exception {
        final double TOL = 1.0E-6;

        GridGeometry2D gg = getRandomCoverage().getGridGeometry();

        Envelope2D worldBounds = gg.getEnvelope2D();
        GridEnvelope2D gridBounds = gg.getGridRange2D();

        assertTrue(worldBounds.boundsEquals(gg.gridToWorld(gridBounds), 0, 1, TOL));

        // test sub-area conversion
        GridEnvelope2D subGrid =
                new GridEnvelope2D(
                        gridBounds.x + 1,
                        gridBounds.y + 1,
                        gridBounds.width - 2,
                        gridBounds.height - 2);

        Envelope2D subEnv = gg.gridToWorld(subGrid);

        double cellWidthX = worldBounds.getWidth() / gridBounds.getWidth();
        double cellWidthY = worldBounds.getHeight() / gridBounds.getHeight();

        Envelope2D expectedEnv =
                new Envelope2D(
                        gg.getCoordinateReferenceSystem2D(),
                        worldBounds.getMinX() + cellWidthX,
                        worldBounds.getMinY() + cellWidthY,
                        worldBounds.getWidth() - 2 * cellWidthX,
                        worldBounds.getHeight() - 2 * cellWidthY);

        assertTrue(expectedEnv.boundsEquals(subEnv, 0, 1, TOL));
    }

    @Test
    public void testCanonicalFromOrthogonal() throws Exception {
        Envelope2D bbox = new Envelope2D(DefaultGeographicCRS.WGS84, 150, 40, 10, 10);
        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(1000, 1000, 100, 100),
                        (org.opengis.geometry.Envelope) bbox);

        GridGeometry2D canonical = gg.toCanonical();
        assertEquivalentCanonical(gg, canonical);
    }

    @Test
    public void testCanonicalFromRotated() throws Exception {
        GridGeometry2D gg =
                new GridGeometry2D(
                        new GridEnvelope2D(1000, 1000, 100, 100),
                        new AffineTransform2D(0.001, -0.5, 0.5, 0.001, -10, -20),
                        DefaultGeographicCRS.WGS84);

        GridGeometry2D canonical = gg.toCanonical();
        assertEquivalentCanonical(gg, canonical);
    }

    private void assertEquivalentCanonical(GridGeometry2D original, GridGeometry2D canonical) {
        // check the grid range
        GridEnvelope canonicalRange = canonical.getGridRange();
        assertEquals(0, canonicalRange.getLow(0));
        assertEquals(0, canonicalRange.getLow(1));
        GridEnvelope originalRange = original.getGridRange();
        assertEquals(originalRange.getSpan(0), canonicalRange.getSpan(0));
        assertEquals(originalRange.getSpan(1), canonicalRange.getSpan(1));
        // check the envelope
        Envelope2D bbox = original.getEnvelope2D();
        Envelope2D canonicalBbox = canonical.getEnvelope2D();
        assertEquals(bbox.getMinX(), canonicalBbox.getMinX(), EPS);
        assertEquals(bbox.getMinY(), canonicalBbox.getMinY(), EPS);
        assertEquals(bbox.getMaxX(), canonicalBbox.getMaxX(), EPS);
        assertEquals(bbox.getMaxY(), canonicalBbox.getMaxY(), EPS);
    }
}
