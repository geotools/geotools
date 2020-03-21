/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import org.locationtech.jts.geom.Geometry;

/**
 * Provides linearization control for curved geometries
 *
 * @author Andrea Aime - GeoSolutions
 */
public interface CurvedGeometry<T extends Geometry> {

    /** Linearizes the geometry using the built-in linearization tolerance */
    T linearize();

    /**
     * Linearizes the geometry using the provided tolerance, the result is guaranteed to be less
     * than tolerance away from the curved geometry unless the number of points needed to linearize
     * the geometry exceeds the build-in per quadrant maximum, see {@link
     * CircularArc#MAX_SEGMENTS_QUADRANT}
     *
     * @param tolerance Linearization tolerance, should be zero or positive. When zero is used, the
     *     maximum number of allowed linearization points will be used, see {@link
     *     CircularArc#MAX_SEGMENTS_QUADRANT}
     */
    T linearize(double tolerance);

    /**
     * Parallel method to {@link Geometry#toText()} that will output the geometry as curved instead
     * of as linear
     */
    String toCurvedText();

    /** The default linearization tolerance */
    double getTolerance();

    /** Returns the dimension of the geometry without forcing access to the coordinate sequence */
    int getCoordinatesDimension();
}
