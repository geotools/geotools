/*
 *    OSGeom -- Geometry Collab
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2009 Department of Geography, University of Bonn
 *    (C) 2001-2009 lat/lon GmbH
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
package org.osgeo.geometry.primitive.segments;


/**
 * A clothoid, or Cornu's spiral, is a plane {@link CurveSegment} whose curvature is a fixed function of its length.
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
public interface Clothoid extends CurveSegment {

    /**
     * Returns the affine mapping that places the curve defined by the Fresnel Integrals into the coordinate reference
     * system of this object.
     *
     * @return the affine mapping
     */
    public AffinePlacement getReferenceLocation();

    /**
     * Returns the value for the constant in the Fresnel's integrals.
     *
     * @return the value for the constant in the Fresnel's integrals
     */
    public double getScaleFactor();

    /**
     * Returns the arc length distance from the inflection point that will be the start point for this curve segment.
     *
     * @return the arc length distance that defines the start point
     */
    public double getStartParameter();

    /**
     * Returns the arc length distance from the inflection point that will be the end point for this curve segment.
     *
     * @return the arc length distance that defines the end point
     */
    public double getEndParameter();
}
