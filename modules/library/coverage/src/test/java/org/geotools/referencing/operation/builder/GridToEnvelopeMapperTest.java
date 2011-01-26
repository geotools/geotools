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
package org.geotools.referencing.operation.builder;

import java.util.Arrays;
import java.awt.geom.Point2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

import org.opengis.geometry.MismatchedDimensionException;

import org.geotools.coverage.grid.GeneralGridEnvelope;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.operation.matrix.XAffineTransform;
import org.geotools.geometry.GeneralEnvelope;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link GridToEnvelopeMapper}. This test appears in the coverage module instead
 * of the referencing module because it need an implementation of {@link GridRange}.
 *
 * @since 2.3
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class GridToEnvelopeMapperTest {
    /**
     * Tolerance factor for the comparaison of floating point numbers.
     */
    private static final double EPS = 1E-10;

    /**
     * Various tests.
     *
     * @throws NoninvertibleTransformException If the attempt to inverse a transform failed.
     */
    @Test
    public void testMapper() throws NoninvertibleTransformException {
        ///////////////////////////////////////////////////////////////
        ///  Tests the initial state.
        ///
        final GridToEnvelopeMapper mapper = new GridToEnvelopeMapper();
        assertTrue (mapper.isAutomatic(GridToEnvelopeMapper.SWAP_XY));
        assertTrue (mapper.isAutomatic(GridToEnvelopeMapper.REVERSE_AXIS));
        assertFalse(mapper.getSwapXY());
        assertNull (mapper.getReverseAxis());
        try {
            mapper.getGridRange();
            fail();
        } catch (IllegalStateException e) {
            // This is the expected exception.
        }
        try {
            mapper.getEnvelope();
            fail();
        } catch (IllegalStateException e) {
            // This is the expected exception.
        }
        try {
            mapper.createTransform();
            fail();
        } catch (IllegalStateException e) {
            // This is the expected exception.
        }

        ///////////////////////////////////////////////////////////////
        ///  Tests the setting of grid range and envelope.
        ///
        Point2D.Double point = new Point2D.Double();
        GeneralGridEnvelope gridRange;
        GeneralEnvelope envelope;
        gridRange = new GeneralGridEnvelope(new int[] {10, 20}, new int[] {110, 220}, false);
        envelope  = new GeneralEnvelope(new double[] {1, 4, 6}, new double[] {11, 44, 66});
        mapper.setGridRange(gridRange);
        assertSame(gridRange, mapper.getGridRange());
        try {
            mapper.getEnvelope();
            fail();
        } catch (IllegalStateException e) {
            // This is the expected exception.
        }
        try {
            mapper.setEnvelope(envelope);
            fail();
        } catch (MismatchedDimensionException e) {
            // This is the expected exception.
        }
        try {
            new GridToEnvelopeMapper(gridRange, envelope);
            fail();
        } catch (MismatchedDimensionException e) {
            // This is the expected exception.
        }
        envelope = envelope.getSubEnvelope(0, 2);
        mapper.setEnvelope(envelope);
        assertSame(envelope, mapper.getEnvelope());


        ///////////////////////////////////////////////////////////////
        ///  Tests the creation when no CRS is available.
        ///
        assertFalse(mapper.getSwapXY());
        boolean[] reverse = mapper.getReverseAxis();
        assertNotNull(reverse);
        assertEquals (2, reverse.length);
        assertFalse  (reverse[0]);
        assertTrue   (reverse[1]);
        final AffineTransform tr1 = mapper.createAffineTransform();
        assertEquals(AffineTransform.TYPE_GENERAL_SCALE |
                     AffineTransform.TYPE_TRANSLATION   |
                     AffineTransform.TYPE_FLIP, tr1.getType());
        assertEquals( 0.1, tr1.getScaleX(),     EPS);
        assertEquals(-0.2, tr1.getScaleY(),     EPS);
        assertEquals(0.05, tr1.getTranslateX(), EPS);
        assertEquals(47.9, tr1.getTranslateY(), EPS);
        assertSame("Transform should be cached", tr1, mapper.createAffineTransform());

        // Tests a coordinate transformation.
        point.x = 10 - 0.5;
        point.y = 20 - 0.5;
        assertSame(point, tr1.transform(point, point));
        assertEquals( 1, point.x, EPS);
        assertEquals(44, point.y, EPS);


        ///////////////////////////////////////////////////////////////
        ///  Tests the creation when a CRS is available.
        ///
        envelope = envelope.clone();
        envelope.setCoordinateReferenceSystem(DefaultGeographicCRS.WGS84);
        mapper.setEnvelope(envelope);
        assertFalse(mapper.getSwapXY());
        assertTrue (Arrays.equals(new boolean[] {false, true}, mapper.getReverseAxis()));
        final AffineTransform tr2 = mapper.createAffineTransform();
        assertNotSame("Should be a new transform", tr1, tr2);
        assertEquals(AffineTransform.TYPE_GENERAL_SCALE |
                     AffineTransform.TYPE_TRANSLATION   |
                     AffineTransform.TYPE_FLIP, tr2.getType());
        assertEquals( 0.1, tr2.getScaleX(), EPS);
        assertEquals(-0.2, tr2.getScaleY(), EPS);
        assertSame("Transform should be cached", tr2, mapper.createAffineTransform());

        // Tests a coordinate transformation.
        point.x = 10 - 0.5;
        point.y = 20 - 0.5;
        assertSame(point, tr2.transform(point, point));
        assertEquals( 1, point.x, EPS);
        assertEquals(44, point.y, EPS);


        ///////////////////////////////////////////////////////////////
        ///  Tests the creation with a (latitude, longitude) CRS.
        ///
        envelope = envelope.clone();
        envelope.setCoordinateReferenceSystem(new DefaultGeographicCRS("WGS84",
                DefaultGeodeticDatum.WGS84, new DefaultEllipsoidalCS("WGS84",
                DefaultCoordinateSystemAxis.LATITUDE,
                DefaultCoordinateSystemAxis.LONGITUDE)));
        mapper.setEnvelope(envelope);
        assertTrue (mapper.getSwapXY());
        assertTrue (Arrays.equals(new boolean[] {true, false}, mapper.getReverseAxis()));
        final AffineTransform tr3 = mapper.createAffineTransform();
        assertNotSame("Should be a new transform", tr2, tr3);
        assertEquals(AffineTransform.TYPE_QUADRANT_ROTATION |
                     AffineTransform.TYPE_GENERAL_SCALE     |
                     AffineTransform.TYPE_TRANSLATION, tr3.getType());
        assertEquals( 0.0,  tr3.getScaleX(), EPS);
        assertEquals( 0.0,  tr3.getScaleY(), EPS);
        assertEquals(-0.05, tr3.getShearX(), EPS);
        assertEquals( 0.4,  tr3.getShearY(), EPS);
        assertEquals( 0.05, XAffineTransform.getScaleX0(tr3), EPS);
        assertEquals( 0.4,  XAffineTransform.getScaleY0(tr3), EPS);
        assertSame("Transform should be cached", tr3, mapper.createAffineTransform());

        // Tests a coordinate transformation.
        point.x = 10 - 0.5;
        point.y = 20 - 0.5;
        assertSame(point, tr3.transform(point, point));
        assertEquals( 4, point.y, EPS);
        assertEquals(11, point.x, EPS);

        // Tests matrix inversion. Note that compared to the 'tr3' transform, the
        // factors are not only inversed (1/0.05 = 20, 1/0.4 = 2.5). In addition,
        // shearX and shearY are interchanged.
        final AffineTransform tr3i = tr3.createInverse();
        assertEquals(  0.0, tr3i.getScaleX(), EPS);
        assertEquals(  0.0, tr3i.getScaleY(), EPS);
        assertEquals(  2.5, tr3i.getShearX(), EPS);
        assertEquals(-20,   tr3i.getShearY(), EPS);
        assertEquals(  2.5, XAffineTransform.getScaleX0(tr3i), EPS);
        assertEquals( 20,   XAffineTransform.getScaleY0(tr3i), EPS);


        ///////////////////////////////////////////////////////////////
        ///  Tests explicit axis reversal and swapping
        ///
        assertTrue (mapper.isAutomatic(GridToEnvelopeMapper.SWAP_XY));
        assertTrue (mapper.isAutomatic(GridToEnvelopeMapper.REVERSE_AXIS));
        assertTrue (mapper.getSwapXY());
        mapper.setSwapXY(false);
        assertFalse(mapper.isAutomatic(GridToEnvelopeMapper.SWAP_XY));
        assertTrue (mapper.isAutomatic(GridToEnvelopeMapper.REVERSE_AXIS));
        assertFalse(mapper.getSwapXY());
        assertNotSame(tr3, mapper.createAffineTransform());
        mapper.setReverseAxis(null);
        mapper.reverseAxis(1);
        assertFalse(mapper.isAutomatic(GridToEnvelopeMapper.SWAP_XY));
        assertFalse(mapper.isAutomatic(GridToEnvelopeMapper.REVERSE_AXIS));
        assertEquals(tr1, mapper.createAffineTransform());
    }
}
