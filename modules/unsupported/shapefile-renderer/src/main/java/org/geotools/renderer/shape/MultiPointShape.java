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
package org.geotools.renderer.shape;

import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;


/**
 * A shape for drawing on a graphics2d
 *
 * @author jeichar
 *
 * @since 2.1.x
 *
 * @source $URL$
 */
public class MultiPointShape extends AbstractShape implements Shape {
    public MultiPointShape(SimpleGeometry geom) {
        super(geom);
    }

    /**
     * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform)
     */
    public PathIterator getPathIterator(final AffineTransform at) {
        return new PathIterator() {
                int currentPart = 0;

                public int getWindingRule() {
                    return WIND_NON_ZERO;
                }

                public boolean isDone() {
                    return currentPart == geom.coords.length;
                }

                public void next() {
                    if (isDone()) {
                        return;
                    }

                    currentPart++;
                }

                public int currentSegment(float[] coords) {
                    coords[0] = (float) geom.coords[currentPart][0];
                    coords[1] = (float) geom.coords[currentPart][1];

                    if (at != null) {
                        at.transform(coords, 0, coords, 0, 1);
                    }

                    return SEG_MOVETO;
                }

                public int currentSegment(double[] coords) {
                    coords[0] = (float) geom.coords[currentPart][0];
                    coords[1] = (float) geom.coords[currentPart][1];

                    if (at != null) {
                        at.transform(coords, 0, coords, 0, 1);
                    }

                    return SEG_MOVETO;
                }
            };
    }

    /*
     * (non-Javadoc)
     *
     * @see java.awt.Shape#getPathIterator(java.awt.geom.AffineTransform,
     *      double)
     */
    public PathIterator getPathIterator(AffineTransform at, double flatness) {
        return getPathIterator(at);
    }
}
