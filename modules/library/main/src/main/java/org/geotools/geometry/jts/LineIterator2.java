/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.LineString;

/**
 * A path iterator for the LiteShape class, specialized to iterate over LineString object.
 *
 * @author Andrea Aime
 * @author simone giannecchini
 * @version $Id$
 */
public final class LineIterator2 implements PathIterator {

    private double[] allCoords;

    /** Transform applied on the coordinates during iteration */
    private AffineTransform at;

    /** Current line coordinate */
    private int currentCoord = 0;

    private int actualCoords; // numb coordinates

    /** True when the iteration is terminated */
    private boolean done = false;

    public LineIterator2() {}

    /** @see java.awt.geom.PathIterator#currentSegment(float[]) */
    @Override
    public int currentSegment(float[] coords) {
        if (currentCoord == 0) {
            coords[0] = (float) allCoords[0];
            coords[1] = (float) allCoords[1];
            if (at != null) at.transform(coords, 0, coords, 0, 1);
            return SEG_MOVETO;
        } else {
            coords[0] = (float) allCoords[currentCoord * 2];
            coords[1] = (float) allCoords[currentCoord * 2 + 1];
            if (at != null) at.transform(coords, 0, coords, 0, 1);
            return SEG_LINETO;
        }
    }

    /** */
    public void init(LineString ls, AffineTransform at) {

        if ((at == null) || at.isIdentity()) {
            this.at = null;
        } else {
            this.at = at;
        }

        CoordinateSequence coordinates = ls.getCoordinateSequence();
        if (coordinates instanceof LiteCoordinateSequence) {
            // array already there for us
            allCoords = ((LiteCoordinateSequence) coordinates).getXYArray();
            actualCoords = coordinates.size();
        } else {
            // build the array
            actualCoords = coordinates.size();
            allCoords = new double[actualCoords * 2];
            for (int t = 0; t < actualCoords; t++) {
                allCoords[t * 2] = coordinates.getOrdinate(t, 0);
                allCoords[t * 2 + 1] = coordinates.getOrdinate(t, 1);
            }
        }

        done = false;
        currentCoord = 0;
    }

    /**
     * Returns the winding rule for determining the interior of the path.
     *
     * @return the winding rule.
     * @see #WIND_EVEN_ODD
     * @see #WIND_NON_ZERO
     */
    @Override
    public int getWindingRule() {
        return WIND_NON_ZERO;
    }

    /**
     * Tests if the iteration is complete.
     *
     * @return <code>true</code> if all the segments have been read; <code>false</code> otherwise.
     */
    @Override
    public boolean isDone() {
        return done;
    }

    /**
     * Moves the iterator to the next segment of the path forwards along the primary direction of traversal as long as
     * there are more points in that direction.
     */
    @Override
    public void next() {
        if (currentCoord == (actualCoords - 1)) {
            done = true;
        } else {
            currentCoord++;
        }
    }

    /** @see java.awt.geom.PathIterator#currentSegment(double[]) */
    @Override
    public int currentSegment(double[] coords) {
        float[] fco = new float[6];
        int result = currentSegment(fco);
        coords[0] = fco[0];
        coords[1] = fco[1];
        coords[2] = fco[2];
        coords[3] = fco[3];
        return result;
    }
}
