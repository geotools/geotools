/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2025, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Envelope;

/**
 * Minimal quadtree for screen-space envelope collision detection with true early-exit support.
 *
 * <p>Unlike JTS {@code Quadtree}, {@link #anyIntersects} stops traversal immediately on the first matching item rather
 * than visiting all intersecting branches. Items that span quadrant boundaries are stored at the node where insertion
 * is attempted; items outside the tree bounds accumulate at the root.
 */
class LabelQuadtree {

    private static final int SPLIT_THRESHOLD = 16;
    private static final int MAX_DEPTH = 12;

    private final double minX, minY, maxX, maxY, midX, midY;
    private final int depth;
    private LabelQuadtree[] children;
    private final List<Envelope> items = new ArrayList<>();

    LabelQuadtree(double minX, double minY, double maxX, double maxY) {
        this(minX, minY, maxX, maxY, 0);
    }

    private LabelQuadtree(double minX, double minY, double maxX, double maxY, int depth) {
        this.minX = minX;
        this.minY = minY;
        this.maxX = maxX;
        this.maxY = maxY;
        this.midX = (minX + maxX) / 2;
        this.midY = (minY + maxY) / 2;
        this.depth = depth;
    }

    void insert(Envelope e) {
        if (children != null) {
            int idx = childIndex(e);
            if (idx >= 0) children[idx].insert(e);
            else items.add(e);
            return;
        }
        items.add(e);
        if (items.size() > SPLIT_THRESHOLD && depth < MAX_DEPTH) split();
    }

    /** Returns the first stored envelope intersecting {@code query}, or null if none. */
    Envelope findFirst(Envelope query) {
        for (Envelope e : items) {
            if (e.intersects(query)) return e;
        }
        if (children != null)
            for (LabelQuadtree child : children)
                if (child.nodeBoundsIntersect(query)) {
                    Envelope found = child.findFirst(query);
                    if (found != null) return found;
                }
        return null;
    }

    private boolean nodeBoundsIntersect(Envelope e) {
        return e.getMinX() <= maxX && e.getMaxX() >= minX && e.getMinY() <= maxY && e.getMaxY() >= minY;
    }

    /** Returns child index [0=SW,1=SE,2=NW,3=NE] if {@code e} fits entirely in that quadrant, else -1. */
    private int childIndex(Envelope e) {
        // keep at this node anything not fully inside its bounds, so every item lives in a node whose
        // envelope contains it; that is what makes findFirst's node-bounds pruning sound (mirrors the
        // JTS Quadtree invariant, which instead grows the tree upward to contain out-of-bounds items).
        if (e.getMinX() < minX || e.getMaxX() > maxX || e.getMinY() < minY || e.getMaxY() > maxY) return -1;
        boolean left = e.getMaxX() <= midX, right = e.getMinX() >= midX;
        boolean bottom = e.getMaxY() <= midY, top = e.getMinY() >= midY;
        if (left && bottom) return 0;
        if (right && bottom) return 1;
        if (left && top) return 2;
        if (right && top) return 3;
        return -1;
    }

    private void split() {
        children = new LabelQuadtree[4];
        children[0] = new LabelQuadtree(minX, minY, midX, midY, depth + 1);
        children[1] = new LabelQuadtree(midX, minY, maxX, midY, depth + 1);
        children[2] = new LabelQuadtree(minX, midY, midX, maxY, depth + 1);
        children[3] = new LabelQuadtree(midX, midY, maxX, maxY, depth + 1);
        List<Envelope> old = new ArrayList<>(items);
        items.clear();
        for (Envelope e : old) {
            int idx = childIndex(e);
            if (idx >= 0) children[idx].insert(e);
            else items.add(e);
        }
    }
}
