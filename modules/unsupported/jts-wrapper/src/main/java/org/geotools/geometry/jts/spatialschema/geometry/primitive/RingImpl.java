/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/primitive/RingImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

import org.geotools.geometry.jts.spatialschema.geometry.JTSUtils;
import org.geotools.geometry.jts.spatialschema.geometry.complex.CompositeCurveImpl;
import org.locationtech.jts.geom.LineString;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Shell;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Represent a single connected component of a {@linkplain SurfaceBoundary surface boundary}. It
 * consists of a number of references to {@linkplain OrientableCurve orientable curves} connected in
 * a cycle (an object whose boundary is empty). A {@code Ring} is structurally similar to a
 * {@linkplain CompositeCurve composite curve} in that the end point of each {@linkplain
 * OrientableCurve orientable curve} in the sequence is the start point of the next {@linkplain
 * OrientableCurve orientable curve} in the sequence. Since the sequence is circular, there is no
 * exception to this rule. Each ring, like all boundaries is a cycle and each ring is simple. <br>
 * <br>
 * Even though each {@code Ring} is simple, the boundary need not be simple. The easiest case of
 * this is where one of the interior rings of a surface is tangent to its exterior ring.
 * Implementations may enforce stronger restrictions on the interaction of boundary elements.
 *
 * <p>This implementation does not automatically close itself. The isValid method returns false if
 * the curve is either not closed or crosses itself. @UML type GM_Ring
 *
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 * @version 2.0
 * @see SurfaceBoundary
 * @see Shell
 */
public class RingImpl extends CompositeCurveImpl implements Ring {
    /** Constructs a new Ring instance with no CRS and no parent curve. */
    public RingImpl() {
        this(null, null);
    }

    /** Constructs a new Ring instance with the given CRS and no parent curve. */
    public RingImpl(CoordinateReferenceSystem crs) {
        this(null, crs);
    }

    /**
     * Constructs a new Ring instance with the given curve as its parent and with the CRS retrieved
     * from parentCurve.
     */
    public RingImpl(CompositeCurve parentCurve) {
        this(parentCurve, parentCurve.getCoordinateReferenceSystem());
    }

    public RingImpl(CompositeCurve parentCurve, CoordinateReferenceSystem crs) {
        super(parentCurve, crs);
    }

    /**
     * This implementation returns true if the curve does not cross itself, false otherwise. It does
     * not test for closure or disconnects. To check for disconnects, call the superclass method.
     *
     * @return True if this object's coordinates are a valid Ring.
     */
    public boolean isValid() {
        // Verify that the line doesn't cross itself
        org.locationtech.jts.geom.Coordinate[] coords = computeJTSPeer().getCoordinates();
        int count = coords.length;
        // System.err.println("RingImpl.isValid -- coord count " + count);
        if (count > 2) {
            if (!coords[0].equals(coords[count - 1])) {
                // System.err.println("  Adding closure coord");
                // Close the set of coordinates for the validation test
                // if it isn't already closed
                org.locationtech.jts.geom.Coordinate[] tmp =
                        new org.locationtech.jts.geom.Coordinate[count + 1];
                System.arraycopy(coords, 0, tmp, 0, count);
                tmp[count] = coords[0];
                coords = tmp;
            }
            LineString jtsLineString = JTSUtils.GEOMETRY_FACTORY.createLineString(coords);
            /*
            // System.err.println("  Created JTS LineString, point count " + jtsLineString.getNumPoints()
            	+ ", length " + jtsLineString.getLength() + "\n  simple? " + jtsLineString.isSimple()
            	+ "; valid? " + jtsLineString.isValid() + "; closed? " + jtsLineString.isClosed());
            	*/
            return jtsLineString.isRing();
        }
        return false;
    }
}
