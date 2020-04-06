/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2014, Open Source Geospatial Foundation (OSGeo)
 * (C) 2014 TOPP - www.openplans.org.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.process.raster;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.referencing.GeodeticCalculator;
import org.opengis.geometry.DirectPosition;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;

/**
 * Used to calculate an estimate for the Grid Convergence Angle at a point within a 2D Coordinate
 * Reference System. The Grid Convergence Angle at any point on a projected 2D map is the difference
 * between "up" or "grid north" on the map at that point and True North. Since the map is projected,
 * the Grid Convergence Angle can change at each point on the map; True North can be in a different
 * Cartesian direction on the flat map for every point. One example impact of this is that vectors
 * cannot be accurately drawn on the screen by simply rotating them a certain amount in "screen
 * degrees." This class, then, is used to estimate the Grid Convergence Angle at those points.
 * Though the underlying meaning is the same, different mapping conventions define the angle's
 * direction (+/-) and beginning point / ending point differently. Therefore, for the purposes of
 * this code, the Grid Convergence Angle is defined as the angle (C) FROM true north TO grid north
 * with 0 deg up and angles increasing positively clockwise. So, for the example below, C would be
 * approximately -33 deg, since the angle FROM true north TO grid north is Counter-Clockwise.
 *
 * <pre>
 *
 *                         Up
 *                     Grid North
 *                         0 deg    True North
 *                         |       .
 *                         |  C  .
 *                         |   .
 *                         | .
 *       270 deg-----------O--------------90 deg
 *                         |
 *                         |
 *                         |
 *                         |
 *                         |
 *                         180 deg
 *
 * </pre>
 *
 * This class uses the GeoTools GeodeticCalculator for performing underlying calculations. <br>
 * <br>
 * Some literature (don't have a link) says that the Grid Covergence Angle is really the angle
 * between a line extending toward grid north and one extending toward true north THAT HAVE BEEN
 * PROJECTED into the map projection, but suggests the difference between this angle and the way the
 * angle is being estimated here is small. May want some verification of that. <br>
 * <br>
 *
 * @author Mike Grogan, WeatherFlow, Inc., Synoptic <br>
 *     <br>
 * @see http://www.threelittlemaids.co.uk/magdec/explain.html
 * @see http://www.bluemarblegeo.com/knowledgebase/calculator/Scale_Factor_and_Convergence.htm
 */
final class GridConvergenceAngleCalc {

    /** Input Coverage CRS */
    private final CoordinateReferenceSystem crs;

    /** Operator used for calculating the GridConvergence angle */
    private GeodeticCalculator geoCalc;

    /** Index associated to the "up" axis */
    private final int upAxisDimension;

    /**
     * Constructs a new GridConvergenceAngleCalc for a given CoordinateReferenceSystem
     *
     * @param crs CoordinateReferenceSystem for which to construct a new GridConvergenceAngleCalc.
     */
    public GridConvergenceAngleCalc(CoordinateReferenceSystem crs) throws Exception {
        this.crs = crs;
        this.geoCalc = new GeodeticCalculator(this.crs);
        this.upAxisDimension = determineUpAxisDimension();
        //
        // If we could not find the "up" axis ... meaning up on the map/screen
        // not in the vertical ... then throw an exception
        //

        if (upAxisDimension < 0) {
            throw new Exception("Up Axis can not be determined.");
        }
    }

    /**
     * Estimates the grid convergence angle at a position within a Coordinate Reference System. The
     * angle returned is as described in the documentation for the class. The Coordinate Reference
     * System of the supplied position must be the same as was used when constructing the
     * calculator, because using anything else would not make sense as convergence angle depends on
     * projection.
     *
     * @param position DirectPosition2D at which we want to estimate the grid convergence angle
     * @return double containing grid convergence angle, as described in documentation for the
     *     class.
     */
    public double getConvergenceAngle(DirectPosition2D position) throws Exception {

        //
        // Check to make sure the coordinate reference system for the
        // argument is the same as the calculator.
        //

        CoordinateReferenceSystem positionCRS = position.getCoordinateReferenceSystem();

        if (!positionCRS.equals(crs)) {
            throw new Exception("Position CRS does not match Calculator CRS");
        }

        //
        // We will use the Geotools Geodetic calculator to estimate the
        // convergence angle. We estimate this by taking the supplied point,
        // moving "upward" along the proper upward map axis by 1 unit, and
        // then having the Geodetic calculator tell us the azimuth from the
        // starting point to the ending point. Since the azimuth is relative
        // to true north ... and we are "walking" along a grid north
        // parallel, the azimuth then essentially tells us the angle from
        // true north to grid north, or the local grid convergence angle.
        //

        //
        // Get the "up" axis
        //

        CoordinateSystemAxis upAxis = crs.getCoordinateSystem().getAxis(upAxisDimension);

        //
        // Need to make sure we're not going to go out of bounds along the
        // axis by going up a little bit.
        //
        // Determine the maximum value along that axis
        //

        double upAxisMax = upAxis.getMaximumValue();

        //
        // Get the starting value along the up axis
        //

        double startValueUp = position.getOrdinate(upAxisDimension);

        //
        // If adding 1 to the up axis is going to push us out of bounds, then
        // first subtract 1 from the starting position ... the estimate should
        // still be close if units are close.
        //

        if ((startValueUp + 1) > upAxisMax) {
            position.setOrdinate(upAxisDimension, position.getOrdinate(upAxisDimension) - 1);
        }

        //
        // Set the starting position for the geodetic calculator to position.
        //

        geoCalc.setStartingPosition(position);

        //
        // Set the ending position to be the same as the starting position,
        // except move "up" 1 unit along the "up" axis.
        //

        DirectPosition2D endingPosition = new DirectPosition2D((DirectPosition) position);
        endingPosition.setOrdinate(upAxisDimension, position.getOrdinate(upAxisDimension) + 1);
        geoCalc.setDestinationPosition(endingPosition);

        //
        // Now just ask for the azimuth, which is our convergence angle
        // estimate.
        //
        return geoCalc.getAzimuth();
    }

    /**
     * Determines which axis in the calculator's Coordinate Reference System is "up"
     *
     * @return int with up axis dimension, or -1 if up axis cannot be found.
     */
    private int determineUpAxisDimension() {
        //
        // Grab the number of dimensions. We only can deal with a 2D
        // projection here. Set to -1 if not a 2D system ... and let
        // other code throw errors.
        //

        int numDimensions = crs.getCoordinateSystem().getDimension();

        if (numDimensions > 2) {
            return -1;
        }

        //
        // Loop through all of the axes until you find the one that is
        // probably the upward axis.
        //

        for (int i = 0; i < numDimensions; i++) {
            CoordinateSystemAxis axis = crs.getCoordinateSystem().getAxis(i);
            AxisDirection axisDirection = axis.getDirection();

            if (axisDirection.equals(AxisDirection.DISPLAY_UP)
                    || axisDirection.equals(AxisDirection.EAST_NORTH_EAST)
                    || axisDirection.equals(AxisDirection.NORTH)
                    || axisDirection.equals(AxisDirection.NORTH_EAST)
                    || axisDirection.equals(AxisDirection.NORTH_NORTH_EAST)
                    || axisDirection.equals(AxisDirection.NORTH_NORTH_WEST)
                    || axisDirection.equals(AxisDirection.NORTH_WEST)
                    || axisDirection.equals(AxisDirection.ROW_POSITIVE)
                    || axisDirection.equals(AxisDirection.UP)
                    || axisDirection.equals(AxisDirection.WEST_NORTH_WEST)) {
                return i;
            }
        }

        //
        // If not found yet, find one with name Northing or y and assume
        // it is up.
        //

        for (int i = 0; i < numDimensions; i++) {
            CoordinateSystemAxis axis = crs.getCoordinateSystem().getAxis(i);
            String axisName = axis.getName().toString().toUpperCase();
            if (axisName.equals("Y")
                    || axisName.equals("NORTHING")
                    || axisName.contains("NORTHING")) {
                return i;
            }
        }

        //
        // If the up axis still hasn't been found, then signify we can't
        // find it with a -1.
        //

        return -1;
    }
}
