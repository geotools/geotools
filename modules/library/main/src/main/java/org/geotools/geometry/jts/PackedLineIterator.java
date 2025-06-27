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
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.impl.PackedCoordinateSequence;
import org.locationtech.jts.geom.impl.PackedCoordinateSequence.Double;

/**
 * A path iterator for the LiteShape class, specialized to iterate over LineString object.
 *
 * @author Andrea Aime
 * @version $Id$
 */
public final class PackedLineIterator extends AbstractLiteIterator {
    /** Transform applied on the coordinates during iteration */
    private AffineTransform at;

    /** The array of coordinates that represents the line geometry */
    private PackedCoordinateSequence.Double coordinates = null;

    /** Current line coordinate */
    private int currentCoord = 0;

    /** The previous coordinate (during iteration) */
    private float oldX = Float.NaN;

    private float oldY = Float.NaN;

    /** True when the iteration is terminated */
    private boolean done = false;

    /** True if the line is a ring */
    private boolean isClosed;

    /** If true, apply simple distance based generalization */
    private boolean generalize = false;

    /** Maximum distance for point elision when generalizing */
    private float maxDistance = 1.0f;

    /** Horizontal scale, got from the affine transform and cached */
    private float xScale;

    /** Vertical scale, got from the affine transform and cached */
    private float yScale;

    private int coordinateCount;

    /**
     * Creates a new instance of LineIterator
     *
     * @param ls The line string the iterator will use
     * @param at The affine transform applied to coordinates during iteration
     */
    public PackedLineIterator(LineString ls, AffineTransform at, boolean generalize, float maxDistance) {
        if (at == null) {
            at = new AffineTransform();
        }

        this.at = at;
        xScale = (float) Math.sqrt(at.getScaleX() * at.getScaleX() + at.getShearX() * at.getShearX());
        yScale = (float) Math.sqrt(at.getScaleY() * at.getScaleY() + at.getShearY() * at.getShearY());

        coordinates = (Double) ls.getCoordinateSequence();
        coordinateCount = coordinates.size();
        isClosed = ls instanceof LinearRing;

        this.generalize = generalize;
        this.maxDistance = maxDistance;
    }

    /**
     * Creates a new instance of LineIterator
     *
     * @param ls The line string the iterator will use
     * @param at The affine transform applied to coordinates during iteration
     * @param generalize if true apply simple distance based generalization
     */
    //    public LineIterator(LineString ls, AffineTransform at, boolean generalize) {
    //        this(ls, at);
    //
    //    }

    /**
     * Creates a new instance of LineIterator
     *
     * @param ls The line string the iterator will use
     * @param at The affine transform applied to coordinates during iteration
     * @param generalize if true apply simple distance based generalization
     * @param maxDistance during iteration, a point will be skipped if it's distance from the previous is less than
     *     maxDistance
     */
    //    public LineIterator(
    //        LineString ls, AffineTransform at, boolean generalize,
    //        double maxDistance) {
    //        this(ls, at, generalize);
    //
    //    }

    /**
     * Sets the distance limit for point skipping during distance based generalization
     *
     * @param distance the maximum distance for point skipping
     */
    public void setMaxDistance(float distance) {
        maxDistance = distance;
    }

    /**
     * Returns the distance limit for point skipping during distance based generalization
     *
     * @return the maximum distance for distance based generalization
     */
    public double getMaxDistance() {
        return maxDistance;
    }

    /**
     * Returns the coordinates and type of the current path segment in the iteration. The return value is the
     * path-segment type: SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A double array of length 6 must
     * be passed in and can be used to store the coordinates of the point(s). Each point is stored as a pair of double
     * x,y coordinates. SEG_MOVETO and SEG_LINETO types returns one point, SEG_QUADTO returns two points, SEG_CUBICTO
     * returns 3 points and SEG_CLOSE does not return any points.
     *
     * @param coords an array that holds the data returned from this method
     * @return the path-segment type of the current path segment.
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     */
    @Override
    public int currentSegment(float[] coords) {
        if (currentCoord == 0) {
            coords[0] = (float) coordinates.getX(0);
            coords[1] = (float) coordinates.getY(0);
            at.transform(coords, 0, coords, 0, 1);

            return SEG_MOVETO;
        } else if (currentCoord == coordinateCount && isClosed) {
            return SEG_CLOSE;
        } else {
            coords[0] = (float) coordinates.getX(currentCoord);
            coords[1] = (float) coordinates.getY(currentCoord);
            at.transform(coords, 0, coords, 0, 1);

            return SEG_LINETO;
        }
    }

    //    /**
    //     * Returns the coordinates and type of the current path segment in the
    //     * iteration. The return value is the path-segment type: SEG_MOVETO,
    //     * SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A float array of
    //     * length 6 must be passed in and can be used to store the coordinates of
    //     * the point(s). Each point is stored as a pair of float x,y coordinates.
    //     * SEG_MOVETO and SEG_LINETO types returns one point, SEG_QUADTO returns
    //     * two points, SEG_CUBICTO returns 3 points and SEG_CLOSE does not return
    //     * any points.
    //     *
    //     * @param coords an array that holds the data returned from this method
    //     *
    //     * @return the path-segment type of the current path segment.
    //     *
    //     * @see #SEG_MOVETO
    //     * @see #SEG_LINETO
    //     * @see #SEG_QUADTO
    //     * @see #SEG_CUBICTO
    //     * @see #SEG_CLOSE
    //     */
    //    public int currentSegment(float[] coords) {
    //        double[] dcoords = new double[2];
    //        int result = currentSegment(dcoords);
    //        coords[0] = (float) dcoords[0];
    //        coords[1] = (float) dcoords[1];
    //
    //        return result;
    //    }

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
        if (currentCoord == coordinateCount - 1 && !isClosed || currentCoord == coordinateCount && isClosed) {
            done = true;
        } else {
            if (generalize) {
                if (Float.isNaN(oldX)) {
                    currentCoord++;
                    oldX = (float) coordinates.getX(currentCoord);
                    oldY = (float) coordinates.getY(currentCoord);
                } else {
                    float distx = 0;
                    float disty = 0;
                    float x = 0;
                    float y = 0;

                    do {
                        currentCoord++;
                        x = (float) coordinates.getX(currentCoord);
                        y = (float) coordinates.getY(currentCoord);

                        if (currentCoord < coordinateCount) {
                            distx = Math.abs(x - oldX);
                            disty = Math.abs(y - oldY);
                        }
                    } while (distx * xScale < maxDistance
                            && disty * yScale < maxDistance
                            && (!isClosed && currentCoord < coordinateCount - 1
                                    || isClosed && currentCoord < coordinateCount));

                    if (currentCoord < coordinateCount) {
                        oldX = x;
                        oldY = y;
                    } else {
                        oldX = Float.NaN;
                        oldY = Float.NaN;
                    }
                }
            } else {
                currentCoord++;
            }
        }
    }

    /** @see java.awt.geom.PathIterator#currentSegment(double[]) */
    @Override
    public int currentSegment(double[] coords) {
        return 0;
    }
}
