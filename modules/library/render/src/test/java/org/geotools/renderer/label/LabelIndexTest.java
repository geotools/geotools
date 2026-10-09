/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.label;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Arrays;
import org.junit.Test;

public class LabelIndexTest {

    private LabelIndex index() {
        return new LabelIndex(new Rectangle(0, 0, 256, 256), 256);
    }

    @Test
    public void testEmptyHasNoConflict() {
        assertFalse(index().labelsWithinDistance(new Rectangle2D.Double(10, 10, 5, 5), 0));
    }

    @Test
    public void testDetectsOverlap() {
        LabelIndex index = index();
        index.addLabel(null, new Rectangle2D.Double(10, 10, 20, 20));
        assertTrue(index.labelsWithinDistance(new Rectangle2D.Double(15, 15, 5, 5), 0));
    }

    @Test
    public void testNoConflictWhenDisjoint() {
        LabelIndex index = index();
        index.addLabel(null, new Rectangle2D.Double(10, 10, 20, 20));
        assertFalse(index.labelsWithinDistance(new Rectangle2D.Double(100, 100, 5, 5), 0));
    }

    @Test
    public void testDistanceExpandsTheQuery() {
        LabelIndex index = index();
        index.addLabel(null, new Rectangle2D.Double(10, 10, 10, 10)); // covers up to (20, 20)
        Rectangle2D candidate = new Rectangle2D.Double(30, 30, 5, 5); // 10px gap from the stored label
        assertFalse(index.labelsWithinDistance(candidate, 5));
        assertTrue(index.labelsWithinDistance(candidate, 15));
    }

    @Test
    public void testNegativeDistanceNeverConflicts() {
        LabelIndex index = index();
        index.addLabel(null, new Rectangle2D.Double(10, 10, 20, 20));
        assertFalse(index.labelsWithinDistance(new Rectangle2D.Double(15, 15, 5, 5), -1));
    }

    @Test
    public void testReservedAreaConflicts() {
        LabelIndex index = index();
        index.reserveArea(Arrays.asList(new Rectangle2D.Double(50, 50, 30, 30)));
        assertTrue(index.labelsWithinDistance(new Rectangle2D.Double(60, 60, 5, 5), 0));
    }

    @Test
    public void testLabelOutsideDisplayAreaStillDetected() {
        // labels placed outside the display area (beyond the margin too) must still be found
        LabelIndex index = index();
        index.addLabel(null, new Rectangle2D.Double(-2000, -2000, 20, 20));
        assertTrue(index.labelsWithinDistance(new Rectangle2D.Double(-1995, -1995, 5, 5), 0));
    }
}
