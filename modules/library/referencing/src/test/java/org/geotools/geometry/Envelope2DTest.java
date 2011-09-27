/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.geom.Rectangle2D;

import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.junit.Before;
import org.junit.Test;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

/**
 * Test case for Envelope2D; mostly added to ensure it correctly meets the BoundingBox
 * API contract.
 * 
 * @author Paul Pfeiffer
 *
 * @source $URL$
 */
public class Envelope2DTest {

    private Envelope2D envelope2D;

    private Envelope2D australia;

    private Envelope2D newZealand;

    private Envelope2D somewhereIntersectingAustralia;

    private Envelope2D somewhereInAustralia;

    final private CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;

    @Before
    public void setupTest() {
        Rectangle2D bounds = new Rectangle2D.Double(-20.0, -20.0, 40.0, 40.0);
        this.envelope2D = new Envelope2D(crs, bounds);

        this.australia = new Envelope2D(this.crs);
        australia.include(40, 110);
        australia.include(10, 150);

        this.newZealand = new Envelope2D(DefaultEngineeringCRS.CARTESIAN_2D);
        newZealand.include(50, 165);
        newZealand.include(33, 180);

        this.somewhereIntersectingAustralia = new Envelope2D(this.crs);
        somewhereIntersectingAustralia.include(50, 145);
        somewhereIntersectingAustralia.include(33, 180);

        this.somewhereInAustralia = new Envelope2D(this.crs);
        somewhereInAustralia.include(35, 140);
        somewhereInAustralia.include(40, 145);

    }

    @Test
    public void testSetBounds() {

        Envelope2D testEnvelope = new Envelope2D();
        testEnvelope.setBounds(envelope2D);

        assertNotNull("envelope2d is null", testEnvelope);
        assertEquals("setbounds envelopes do not match", envelope2D, testEnvelope);
    }

    @Test
    public void testInclude() throws Exception {

        assertEquals("unexpected bounds x min after include", 10, australia.x, 0);
        assertEquals("unexpected bounds y min after include", 110, australia.y, 0);
        assertEquals("unexpected bounds width after include", 30, australia.width, 0);
        assertEquals("unexpected bounds height after include", 40, australia.height, 0);

        try {
            australia.include(newZealand);
            fail("Expected a missmatch of CoordianteReferenceSystem");
        } catch (MismatchedReferenceSystemException t) {
            // expected
        }
        try {
            australia.include(this.envelope2D);
            // expected
            assertEquals("unexpected bounds x min after include", -20, australia.x, 0);
            assertEquals("unexpected bounds y min after include", -20, australia.y, 0);
            assertEquals("unexpected bounds width after include", 60, australia.width, 0);
            assertEquals("unexpected bounds height after include", 170, australia.height, 0);
        } catch (MismatchedReferenceSystemException t) {
            fail("Expected a match of CoordianteReferenceSystem");
        }
    }

    @Test
    public void testIntersectsBoundingBox() {
        boolean testIntersects;

        try {
            australia.intersects((BoundingBox) newZealand);
            fail("Expected a missmatch of CoordianteReferenceSystem");
        } catch (MismatchedReferenceSystemException t) {
            // expected
        }
        try {
            testIntersects = australia.intersects((BoundingBox) envelope2D);
            // expected
            assertFalse(testIntersects);
        } catch (MismatchedReferenceSystemException t) {
            fail("Expected a match of CoordianteReferenceSystem");
        }

        try {
            testIntersects = australia.intersects((BoundingBox) somewhereIntersectingAustralia);
            // expected
            assertTrue(testIntersects);
        } catch (MismatchedReferenceSystemException t) {
            fail("Expected a match of CoordianteReferenceSystem");
        }
    }

    @Test
    public void testContainsBoundingBox() {
        boolean testContains;

        try {
            testContains = australia.contains((BoundingBox) envelope2D);
            // expected
            assertFalse(testContains);
        } catch (MismatchedReferenceSystemException t) {
            fail("Expected a match of CoordianteReferenceSystem");
        }

        try {
            testContains = australia.contains((BoundingBox) somewhereInAustralia);
            // expected
            assertTrue(testContains);
        } catch (MismatchedReferenceSystemException t) {
            fail("Expected a match of CoordianteReferenceSystem");
        }
    }

    @Test
    public void testContainsDirectPosition() {
        boolean testContains;
        DirectPosition2D positionInAustralia = new DirectPosition2D(crs, 30, 120);
        DirectPosition2D positionOutsideAustralia = new DirectPosition2D(crs, 30, 170);

        try {
            testContains = australia.contains((DirectPosition) positionOutsideAustralia);
            // expected
            assertFalse(testContains);
        } catch (MismatchedReferenceSystemException t) {
            fail("Expected a match of CoordianteReferenceSystem");
        }

        try {
            testContains = australia.contains((DirectPosition) positionInAustralia);
            // expected
            assertTrue(testContains);
        } catch (MismatchedReferenceSystemException t) {
            fail("Expected a match of CoordianteReferenceSystem");
        }
    }

    @Test
    public void testToBounds() {
        BoundingBox testBox;

        try {
            newZealand.toBounds(crs);
            fail("Expected a missmatch of CoordianteReferenceSystem");
        } catch (TransformException t) {
            // expected
        }
        try {
            testBox = australia.toBounds(crs);
            // expected
            assertEquals("unexpected bounds x min after toBounds", 10, testBox.getMinX(), 0);
            assertEquals("unexpected bounds y min after toBounds", 110, testBox.getMinY(), 0);
            assertEquals("unexpected bounds x max after toBounds", 40, testBox.getMaxX(), 0);
            assertEquals("unexpected bounds y max after toBounds", 150, testBox.getMaxY(), 0);

        } catch (TransformException t) {
            fail("Missmatch of CoordianteReferenceSystem");
        }
    }

}
