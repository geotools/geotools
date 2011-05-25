/*$************************************************************************************************
 **
 ** $Id$
 **
 ** $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/primitive/BearingImpl.java,v $
 **
 ** Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved. http://www.opengis.org/Legal/
 **
 *************************************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry.primitive;

import org.opengis.geometry.primitive.Bearing;


/**
 * Represents direction in the coordinate reference system. In a 2D coordinate reference
 * system, this can be accomplished using a "angle measured from true north" or a 2D vector
 * point in that direction. In a 3D coordinate reference system, two angles or any 3D vector
 * is possible. If both a set of angles and a vector are given, then they shall be consistent
 * with one another.
 *
 * @UML datatype Bearing
 * @author ISO/DIS 19107
 * @author <A HREF="http://www.opengis.org">OpenGIS&reg; consortium</A>
 *
 *
 * @source $URL$
 * @version 2.0
 *
 * @revisit Should we move this interface elsewhere (e.g. in some kind of units package)?
 */
public class BearingImpl implements Bearing {
    
    //*************************************************************************
    //  fields
    //*************************************************************************
    
    private double[] angles;
    
    private double[] direction;
    
    //*************************************************************************
    //  Constructor
    //*************************************************************************
    
    public BearingImpl(double[] angles, double[] direction) {
        this.angles = angles;
        this.direction = direction;
    }
    
    //*************************************************************************
    //
    //*************************************************************************
    
    /**
     * Returns the azimuth and (optionnaly) the altitude.
     * In this variant of bearing usually used for 2D coordinate systems, the first angle (azimuth)
     * is measured from the first coordinate axis (usually north) in a counterclockwise fashion
     * parallel to the reference surface tangent plane. If two angles are given, the second angle
     * (altitude) usually represents the angle above (for positive angles) or below (for negative
     * angles) a local plane parallel to the tangent plane of the reference surface.
     *
     * @return An array of length 0, 1 or 2 containing the azimuth and altitude angles.
     * @UML operation angle
     *
     * @revisit Should we split this method in {@code getAzimuth()} and
     *          {@code getAltitude()} methods instead? Should we provides
     *          a {@code getDimension()} method too?
     */
    public double[] getAngles() {
        return angles;
    }

    /**
     * Returns the direction as a vector.
     * In this variant of bearing usually used for 3D coordinate systems, the direction is
     * express as an arbitrary vector, in the coordinate system.
     *
     * @return The direction.
     * @UML operation direction
     */
    public double[] getDirection() {
        return direction;
    }
}
