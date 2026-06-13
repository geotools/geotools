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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.locationtech.jts.geom.Envelope;

public class LabelQuadtreeTest {

    private LabelQuadtree tree() {
        return new LabelQuadtree(0, 0, 1000, 1000);
    }

    @Test
    public void testEmptyReturnsNull() {
        assertNull(tree().findFirst(new Envelope(10, 20, 10, 20)));
    }

    @Test
    public void testFindsIntersectingItem() {
        LabelQuadtree tree = tree();
        Envelope e = new Envelope(100, 200, 100, 200);
        tree.insert(e);
        assertSame(e, tree.findFirst(new Envelope(150, 160, 150, 160)));
    }

    @Test
    public void testDisjointReturnsNull() {
        LabelQuadtree tree = tree();
        tree.insert(new Envelope(100, 200, 100, 200));
        assertNull(tree.findFirst(new Envelope(500, 600, 500, 600)));
    }

    @Test
    public void testTouchingCountsAsHit() {
        // Envelope.intersects is inclusive on the shared edge
        LabelQuadtree tree = tree();
        Envelope e = new Envelope(100, 200, 100, 200);
        tree.insert(e);
        assertSame(e, tree.findFirst(new Envelope(200, 300, 100, 200)));
    }

    @Test
    public void testItemOutsideTreeBoundsStillFound() {
        // items outside the tree bounds accumulate at the root and must remain queryable
        LabelQuadtree tree = tree();
        Envelope e = new Envelope(-500, -400, -500, -400);
        tree.insert(e);
        assertSame(e, tree.findFirst(new Envelope(-450, -440, -450, -440)));
    }

    @Test
    public void testItemOutsideTreeBoundsStillFoundAfterSplit() {
        // regression: an out-of-bounds item inserted after a split must not be pushed into a child
        // quadrant that findFirst then prunes away. Force a split in the SW quadrant first.
        LabelQuadtree tree = tree();
        for (int i = 0; i < 20; i++) {
            double x = 10 + i;
            tree.insert(new Envelope(x, x + 0.4, 10, 10.4));
        }
        Envelope oob = new Envelope(-500, -400, -500, -400);
        tree.insert(oob);
        assertSame(oob, tree.findFirst(new Envelope(-450, -440, -450, -440)));
    }

    @Test
    public void testItemSpanningQuadrantBoundaryFoundFromEitherSide() {
        // straddles the root mid-lines (500,500): stays at the node, still found on both sides
        LabelQuadtree tree = tree();
        Envelope e = new Envelope(400, 600, 400, 600);
        tree.insert(e);
        assertSame(e, tree.findFirst(new Envelope(450, 460, 450, 460)));
        assertSame(e, tree.findFirst(new Envelope(550, 560, 550, 560)));
    }

    @Test
    public void testSplitPreservesAllItems() {
        // exceed the split threshold inside one quadrant to force a split; every item still found
        LabelQuadtree tree = tree();
        Envelope[] items = new Envelope[40];
        for (int i = 0; i < items.length; i++) {
            double x = 10 + i; // small boxes packed in the SW quadrant
            items[i] = new Envelope(x, x + 0.4, 10, 10.4);
            tree.insert(items[i]);
        }
        for (Envelope item : items) {
            Envelope hit = tree.findFirst(item);
            assertNotNull(hit);
            assertTrue(item.intersects(hit));
        }
    }

    @Test
    public void testFindFirstReturnsAnIntersectingItemWhenManyOverlap() {
        LabelQuadtree tree = tree();
        for (int i = 0; i < 30; i++) {
            tree.insert(new Envelope(100, 300, 100, 300));
        }
        Envelope hit = tree.findFirst(new Envelope(150, 160, 150, 160));
        assertNotNull(hit);
        assertTrue(hit.intersects(new Envelope(150, 160, 150, 160)));
    }
}
