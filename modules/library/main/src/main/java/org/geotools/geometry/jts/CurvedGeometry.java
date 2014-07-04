/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *   (C) 2009, Open Source Geospatial Foundation (OSGeo)
 *   (C) 2001, Vivid Solutions
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
 *    
 *    This is a port of the JTS WKTReader to handle SQL MM types such as Curve.
 *    We have subclassed so that our implementation can be used anywhere 
 *    a WKTReader is needed. We would of tried for more code reuse  except
 *    the base class has reduced everything to privatee methods.
 *    
 *    This class also contains code written by Mark Leslie for PostGIS while working
 *    at Refractions Reserach with whom we have a code contribution agreement.
 */
package org.geotools.geometry.jts;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Provides linearization control for curved geometries
 * 
 * @author Andrea Aime - GeoSolutions
 */
public interface CurvedGeometry<T extends Geometry> {

    /**
     * Linearizes the geometry using the built-in linearization tolerance
     * 
     * @return
     */
    T linearize();

    /**
     * Linearizes the geometry using the provided tolerance, the result is guaranteed to be less
     * than tolerance away from the curved geometry unless the number of points needed to linearize
     * the geometry exceeds the build-in per quadrant maximum, see
     * {@link CircularArc#MAX_SEGMENTs_QUADRANT}
     * 
     * @param tolerance Linearization tolerance, should be zero or positive. When zero is used, the
     *        maximum number of allowed linearization points will be used, see
     *        {@link CircularArc#MAX_SEGMENTs_QUADRANT}
     * @return
     */
    T linearize(double tolerance);

    /**
     * Parallel method to {@link Geometry#toText()} that will output the geometry as curved instead
     * of as linear
     * 
     * @return
     */
    String toCurvedText();
}
