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

import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.List;
import org.locationtech.jts.geom.Envelope;

/**
 * Stores label envelopes and detects interference by overlap or proximity.
 *
 * <p>Backed by {@link LabelQuadtree} for O(log n) spatial pruning with true early-exit on first hit. Pass
 * {@code margin} large enough to contain any label that may be positioned outside the display area (e.g. with
 * {@code partialsEnabled}).
 *
 * @author Andrea Aime
 */
public class LabelIndex {

    private final LabelQuadtree index;
    private Envelope lastHit;

    /**
     * @param displayArea rendering area in screen coordinates
     * @param margin extra space beyond displayArea to accommodate labels outside the display area
     */
    public LabelIndex(Rectangle displayArea, int margin) {
        index = new LabelQuadtree(
                displayArea.getMinX() - margin,
                displayArea.getMinY() - margin,
                displayArea.getMaxX() + margin,
                displayArea.getMaxY() + margin);
    }

    /**
     * Returns true if any label in the index is within {@code distance} of {@code bounds}. The bounds are expanded by
     * the distance (no curved buffer).
     */
    public boolean labelsWithinDistance(Rectangle2D bounds, double distance) {
        if (distance < 0) return false;
        Envelope query = toEnvelope(bounds);
        query.expandBy(distance);
        if (lastHit != null && lastHit.intersects(query)) return true;
        Envelope found = index.findFirst(query);
        if (found != null) {
            lastHit = found;
            return true;
        }
        return false;
    }

    /** Adds a label into the index. */
    public void addLabel(LabelCacheItem item, Rectangle2D bounds) {
        index.insert(toEnvelope(bounds));
    }

    /** Reserves the areas indicated by these rectangles. */
    public void reserveArea(List<Rectangle2D> reserved) {
        for (Rectangle2D r : reserved) index.insert(toEnvelope(r));
    }

    private static Envelope toEnvelope(Rectangle2D r) {
        return new Envelope(r.getMinX(), r.getMaxX(), r.getMinY(), r.getMaxY());
    }
}
