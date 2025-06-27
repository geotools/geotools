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
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;

/**
 * A path iterator for the LiteShape class, specialized to iterate over a geometry collection. It can be seen as a
 * composite, since uses in fact other, simpler iterator to carry on its duties.
 *
 * @author Andrea Aime
 * @version $Id$
 */
public final class GeomCollectionIterator extends AbstractLiteIterator {
    /** Transform applied on the coordinates during iteration */
    private AffineTransform at;

    /** The set of geometries that we will iterate over */
    private GeometryCollection gc;

    /** The current geometry */
    private int currentGeom;

    /** The current sub-iterator */
    private PathIterator currentIterator;

    /** True when the iterator is terminate */
    private boolean done = false;

    /** If true, apply simple distance based generalization */
    private boolean generalize = false;

    /** Maximum distance for point elision when generalizing */
    private double maxDistance = 1.0;

    public GeomCollectionIterator() {}

    /** */
    public void init(GeometryCollection gc, AffineTransform at, boolean generalize, double maxDistance) {
        this.gc = gc;
        this.at = at == null ? new AffineTransform() : at;
        this.generalize = generalize;
        this.maxDistance = maxDistance;
        currentGeom = 0;
        done = false;
        currentIterator = gc.isEmpty() ? EmptyIterator.INSTANCE : getIterator(gc.getGeometryN(0));
    }

    /**
     * Creates a new instance of GeomCollectionIterator
     *
     * @param gc The geometry collection the iterator will use
     * @param at The affine transform applied to coordinates during iteration
     * @param generalize if true apply simple distance based generalization
     * @param maxDistance during iteration, a point will be skipped if it's distance from the previous is less than
     *     maxDistance
     */
    public GeomCollectionIterator(GeometryCollection gc, AffineTransform at, boolean generalize, double maxDistance) {
        init(gc, at, generalize, maxDistance);
    }

    /**
     * Sets the distance limit for point skipping during distance based generalization
     *
     * @param distance the maximum distance for point skipping
     */
    public void setMaxDistance(double distance) {
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
     * Returns the specific iterator for the geometry passed.
     *
     * @param g The geometry whole iterator is requested
     * @return the specific iterator for the geometry passed.
     */
    private AbstractLiteIterator getIterator(Geometry g) {
        AbstractLiteIterator pi = null;

        if (g.isEmpty()) return EmptyIterator.INSTANCE;
        if (g instanceof Polygon) {
            Polygon p = (Polygon) g;
            pi = new PolygonIterator(p, at, generalize, maxDistance);
        } else if (g instanceof GeometryCollection) {
            GeometryCollection gc = (GeometryCollection) g;
            pi = new GeomCollectionIterator(gc, at, generalize, maxDistance);
        } else if (g instanceof LineString || g instanceof LinearRing) {
            LineString ls = (LineString) g;
            pi = new LineIterator(ls, at, generalize, (float) maxDistance);
        } else if (g instanceof Point) {
            Point p = (Point) g;
            pi = new PointIterator(p, at);
        }

        return pi;
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
    public int currentSegment(double[] coords) {
        return currentIterator.currentSegment(coords);
    }

    /**
     * Returns the coordinates and type of the current path segment in the iteration. The return value is the
     * path-segment type: SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE. A float array of length 6 must
     * be passed in and can be used to store the coordinates of the point(s). Each point is stored as a pair of float
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
        return currentIterator.currentSegment(coords);
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
        // try to move the current iterator forward
        if (!currentIterator.isDone()) {
            currentIterator.next();
        }
        // if the iterator is finished, let's move to the next one (and if
        // the next one, should the next one be empty)
        while (currentIterator.isDone() && !done) {
            if (currentGeom < gc.getNumGeometries() - 1) {
                currentGeom++;
                currentIterator = getIterator(gc.getGeometryN(currentGeom));
            } else {
                done = true;
            }
        }
    }
}
