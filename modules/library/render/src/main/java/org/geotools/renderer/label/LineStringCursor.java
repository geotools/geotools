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
package org.geotools.renderer.label;

import java.util.Arrays;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.LineString;

/**
 * Allows to move a point cursor along the path of a LineString using a
 * curvilinear coordinate system and either absolute distances (from the start
 * of the line) or offsets relative to the current position, to return the
 * absolute position of the cursor as a Point, and to get the orientation of the
 * current segment.
 * 
 * @author Andrea Aime
 * 
 *
 *
 * @source $URL$
 */
public class LineStringCursor {
    
    /**
     * Tolerance used for angle comparisons
     */
    static final double ONE_DEGREE = Math.PI / 180.0;

    /**
     * The LineString being walked
     */
    LineString lineString;

    /**
     * The sequence making up the line string
     */
    CoordinateSequence coords;

    /**
     * The current segment
     */
    int segment;

    /**
     * The current positions's offset from the start of the current segment
     */
    double offsetDistance;

    /**
     * All the segments lengths
     */
    double[] segmentLenghts;

    /**
     * The distance from the start of the line string to the first point in the
     * segment
     */
    double[] segmentStartOrdinate;

    /**
     * A cache for the orientation of each segment (we use it a lot)
     */
    double[] segmentAngles;

    /**
     * Builds a new cursor
     * 
     * @param ls
     */
    public LineStringCursor(LineString ls) {
        this.lineString = ls;
        coords = ls.getCoordinateSequence();

        // reset (it's not really necessary, but still...)
        segment = 0;
        offsetDistance = 0.0;

        // Allocate the length and ordinate caches
        final int coordsCount = coords.size();
        segmentLenghts = new double[coordsCount - 1];
        segmentStartOrdinate = new double[coordsCount - 1];
        segmentStartOrdinate[0] = 0;

        // prepare the coordinates used for distance computation
        Coordinate c1 = new Coordinate();
        Coordinate c2 = new Coordinate();
        c2.x = coords.getX(0);
        c2.y = coords.getY(0);
        for (int i = 1; i < coordsCount; i++) {
            c1.x = c2.x;
            c1.y = c2.y;
            c2.x = coords.getX(i);
            c2.y = coords.getY(i);

            final double distance = c1.distance(c2);
            segmentLenghts[i - 1] = distance;
            if (i < coords.size() - 1)
                segmentStartOrdinate[i] = segmentStartOrdinate[i - 1] + distance;
        }

        // fill up the segment angles cache with placeholders
        segmentAngles = new double[segmentLenghts.length];
        Arrays.fill(segmentAngles, Double.NaN);
    }

    /**
     * Copy constructor
     * 
     * @param cursor
     */
    public LineStringCursor(LineStringCursor cursor) {
        this.lineString = cursor.lineString;
        this.coords = cursor.coords;
        this.segment = cursor.segment;
        this.offsetDistance = cursor.offsetDistance;
        this.segmentLenghts = cursor.segmentLenghts;
        this.segmentStartOrdinate = cursor.segmentStartOrdinate;
        this.segmentAngles = cursor.segmentAngles;
    }

    /**
     * Returns the line string length
     * 
     * @return
     */
    public double getLineStringLength() {
        return segmentStartOrdinate[coords.size() - 2] + segmentLenghts[coords.size() - 2];
    }

    /**
     * Moves the current position to the
     * 
     * @param ordinate
     */
    public void moveTo(double ordinate) {
        double position = 0;

        if (ordinate < 0) {
            // before start
            segment = 0;
            offsetDistance = 0;
        } else if (ordinate > getLineStringLength()) {
            // after end
            segment = segmentLenghts.length - 1;
            offsetDistance = segmentLenghts[segment];
        } else {
            // find the segment and the offset within the segment
            for (int i = 0; i < segmentLenghts.length; i++) {
                double length = segmentLenghts[i];
                if (ordinate < (length + position)) {
                    segment = i;
                    offsetDistance = ordinate - position;
                    break;
                } else {
                    position += length;
                }
            }
        }
    }

    /**
     * Moves of the specified distance from the current position.
     * 
     * @param offset
     * @return true if it was possible to move to the desired offset, false if
     *         the movement stopped because the start or end of the LineString
     *         was reached
     */
    public boolean moveRelative(double offset) {
        if (offset == 0) {
            return true;
        } else if (offset > 0) {
            // move forward until you get to the desired offset, or end up
            // into the end of the line
            while (offset > 0) {
                if ((offsetDistance + offset) <= segmentLenghts[segment]) {
                    // move within the current segment and we're done
                    offsetDistance += offset;
                    return true;
                } else if (segment == (segmentLenghts.length - 1)) {
                    // ops, reached the end of the linestring
                    offsetDistance = segmentLenghts[segment];
                    return false;
                } else {
                    // move to the end of the current segment
                    offset = offset - (segmentLenghts[segment] - offsetDistance);
                    offsetDistance = 0;
                    segment++;
                }
            }
        } else {
            // move backwards until you get to the desired offset, or end up
            // into the end of the line
            while (offset < 0.0) {
                if ((offsetDistance + offset) >= 0.0) {
                    // move within the current segment and we're done
                    offsetDistance += offset;
                    return true;
                } else if (segment == 0) {
                    // ops, reached the start of the linestring
                    offsetDistance = 0;
                    return false;
                } else {
                    // move to the end of the previous segment
                    offset = offset + offsetDistance;
                    segment--;
                    offsetDistance = segmentLenghts[segment];
                }
            }
        }
        throw new RuntimeException("You have stumbled into a software bug, "
                + "the code should never get here. Please report with a reproducable test case");
    }

    /**
     * Returns the Point representing the current position along the LineString
     */
    public Coordinate getCurrentPosition() {
        return getCurrentPosition(new Coordinate());

    }

    /**
     * Returns the Point representing the current position along the LineString
     */
    public Coordinate getCurrentPosition(Coordinate c) {
        c.setCoordinate(coords.getCoordinate(segment));
        if (offsetDistance > 0) {
            final double angle = getCurrentAngle();
            c.x += offsetDistance * Math.cos(angle);
            c.y += offsetDistance * Math.sin(angle);
        }
        return c;

    }

    public double getCurrentOrdinate() {
        return segmentStartOrdinate[segment] + offsetDistance;
    }

    /**
     * Returns the current segment direction as an angle expressed in radians
     * 
     * @return
     */
    public double getCurrentAngle() {
        return getSegmentAngle(segment);
    }

    protected double getSegmentAngle(int segmentIdx) {
        if (Double.isNaN(segmentAngles[segmentIdx])) {
            double dx = (coords.getX(segmentIdx + 1) - coords.getX(segmentIdx));
            double dy = (coords.getY(segmentIdx + 1) - coords.getY(segmentIdx));
            segmentAngles[segmentIdx] = Math.atan2(dy, dx);
        }
        return segmentAngles[segmentIdx];
    }

    /**
     * Returns the current segment direction as an angle expressed in radians
     * 
     * @return
     */
    public double getLabelOrientation() {
        double dx = (coords.getX(segment + 1) - coords.getX(segment));
        double dy = (coords.getY(segment + 1) - coords.getY(segment));
        double slope = dy / dx;
        double angle = Math.atan(slope);
        // make sure we turn PI/2 into -PI/2, we don't want some labels looking straight up
        // and some others straight down, when almost vertical they should all be orianted
        // on the same side
        if(Math.abs(angle - Math.PI / 2) < ONE_DEGREE) {
            angle = -Math.PI / 2 + Math.abs(angle - Math.PI / 2);
        }
        return angle;
    }

    /**
     * Returns the maximum angle change (in radians) between two subsequent
     * segments between the specified curvilinear coordinates.
     * 
     * @param startOrdinate
     * @param endOrdinate
     * @return
     */
    public double getMaxAngleChange(double startOrdinate, double endOrdinate) {
        if (startOrdinate > endOrdinate)
            throw new IllegalArgumentException("Invalid arguments, endOrdinate < starOrdinate");

        // compute the begin and end segments
        LineStringCursor delegate = new LineStringCursor(this);
        delegate.moveTo(startOrdinate);
        int startSegment = delegate.segment;
        delegate.moveTo(endOrdinate);
        int endSegment = delegate.segment;

        // everything inside the same segment
        if (startSegment == endSegment)
            return 0;

        double maxDifference = 0;
        double prevAngle = getSegmentAngle(startSegment);
        for (int i = startSegment + 1; i <= endSegment; i++) {
            double currAngle = getSegmentAngle(i);
            double difference = Math.abs(currAngle - prevAngle);
            if (difference > maxDifference)
                maxDifference = difference;
            prevAngle = currAngle;
        }

        return maxDifference;
    }

    /**
     * Returns a line string cursor based on the opposite walking direction.
     * 
     * @return
     */
    public LineStringCursor reverse() {
        return new LineStringCursor((LineString) lineString.reverse());
    }

    /**
     * The linestrings wrapped by this cursor
     * 
     * @return
     */
    public LineString getLineString() {
        return lineString;
    }

    /**
     * Returns the linestring that starts and ends at the specified curvilinear
     * coordinates.
     * 
     * @param startOrdinate
     * @param endOrdinate
     * @return
     */
    public LineString getSubLineString(double startOrdinate, double endOrdinate) {
        LineStringCursor clone = new LineStringCursor(this);
        clone.moveTo(startOrdinate);
        int startSegment = clone.segment;
        Coordinate start = clone.getCurrentPosition();
        clone.moveTo(endOrdinate);
        int endSegment = clone.segment;
        Coordinate end = clone.getCurrentPosition();

        Coordinate[] subCoords = new Coordinate[endSegment - startSegment + 2];
        subCoords[0] = start;
        for (int i = startSegment; i < endSegment; i++) {
            subCoords[i - startSegment + 1] = coords.getCoordinate(i + 1);
        }
        subCoords[subCoords.length - 1] = end;
        return lineString.getFactory().createLineString(subCoords);
    }
}
