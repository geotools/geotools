/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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
 *    Portions of this file is adapted from Fortran code provided by NOAA.
 *    Programmed for CDC-6600 by LCDR L.Pfeifer NGS ROCKVILLE MD 18FEB75
 *    Modified for IBM SYSTEM 360 by John G.Gergen NGS ROCKVILLE MD 7507
 *    Source: ftp://ftp.ngs.noaa.gov/pub/pcsoft/for_inv.3d/source/
 */
package org.geotools.referencing;

import java.awt.Shape;
import java.awt.geom.GeneralPath;
import java.awt.geom.Point2D;
import java.text.Format;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import net.sf.geographiclib.GeodesicLine;
import net.sf.geographiclib.GeodesicMask;
import org.geotools.api.geometry.Position;
import org.geotools.api.referencing.crs.CompoundCRS;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.cs.AxisDirection;
import org.geotools.api.referencing.cs.CoordinateSystem;
import org.geotools.api.referencing.cs.CoordinateSystemAxis;
import org.geotools.api.referencing.datum.Datum;
import org.geotools.api.referencing.datum.Ellipsoid;
import org.geotools.api.referencing.datum.GeodeticDatum;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.GeneralPosition;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.TransformedPosition;
import org.geotools.measure.Angle;
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.i18n.Vocabulary;
import org.geotools.metadata.i18n.VocabularyKeys;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.util.CRSUtilities;
import org.geotools.util.TableWriter;
import si.uom.NonSI;

/**
 * Performs geodetic calculations on an {@linkplain Ellipsoid ellipsoid}. This class encapsulates a generic ellipsoid
 * and calculates the following properties:
 *
 * <p>
 *
 * <ul>
 *   <li>Distance and azimuth between two points.
 *   <li>Point located at a given distance and azimuth from an other point.
 * </ul>
 *
 * <p>The calculation uses the following information:
 *
 * <p>
 *
 * <ul>
 *   <li>The {@linkplain #setStartingPosition starting position}, which is always considered valid. It is initially set
 *       at (0,0) and can only be changed to another legitimate value.
 *   <li><strong>Only one</strong> of the following:
 *       <ul>
 *         <li>The {@linkplain #setDestinationPosition destination position}, or
 *         <li>An {@linkplain #setDirection azimuth and distance}.
 *       </ul>
 *       The last one set overrides the other and determines what will be calculated.
 * </ul>
 *
 * <p>Note: This class is not thread-safe. If geodetic calculations are needed in a multi-threads environment, create
 * one distinct instance of {@code GeodeticCalculator} for each thread.
 *
 * @since 2.1
 * @version $Id$
 * @author Daniele Franzoni
 * @author Martin Desruisseaux
 */
public class GeodeticCalculator {
    /**
     * The transform from user coordinates to geodetic coordinates used for computation, or {@code null} if no
     * transformations are required.
     */
    private final TransformedPosition userToGeodetic;

    /**
     * The coordinate reference system for all methods working on {@link Position} objects. If {@code null}, will be
     * created the first time {@link #getCoordinateReferenceSystem} is invoked.
     */
    private CoordinateReferenceSystem coordinateReferenceSystem;

    /**
     * The coordinate reference system for all methods working on {@link Point2D} objects. If {@code null}, will be
     * created the first time {@link #getGeographicCRS} is invoked.
     */
    private GeographicCRS geographicCRS;

    /** The encapsulated ellipsoid. */
    private final Ellipsoid ellipsoid;

    /*
     * The semi major axis of the reference ellipsoid.
     */
    private final double semiMajorAxis;

    /*
     * The flattening the reference ellipsoid.
     */
    private final double flattening;

    /**
     * The (<var>latitude</var>, <var>longitude</var>) coordinate of the first point <strong>in degrees</strong>. This
     * point is set by {@link #setStartingGeographicPoint}.
     */
    private double lat1, long1;

    /**
     * The (<var>latitude</var>, <var>longitude</var>) coordinate of the destination point <strong>in degrees</strong>.
     * This point is set by {@link #setDestinationGeographicPoint}.
     */
    private double lat2, long2;

    /**
     * The distance and azimuth (in degrees) from the starting point ({@link #long1}, {@link #lat1}) to the destination
     * point ({@link #long2}, {@link #lat2}).
     */
    private double distance, azimuth;

    /**
     * Tell if the destination point is valid. {@code false} if {@link #long2} and {@link #lat2} need to be computed.
     */
    private boolean destinationValid;

    /**
     * Tell if the azimuth and the distance are valids. {@code false} if {@link #distance} and {@link #azimuth} need to
     * be computed.
     */
    private boolean directionValid;

    /** The object that carries out the geodesic calculations. */
    private Geodesic geod;

    /** Constructs a new geodetic calculator associated with the WGS84 ellipsoid. */
    public GeodeticCalculator() {
        this(DefaultEllipsoid.WGS84);
    }

    /**
     * Constructs a new geodetic calculator associated with the specified ellipsoid. All calculations done by the new
     * instance are referenced to this ellipsoid.
     *
     * @param ellipsoid The ellipsoid onto which calculates distances and azimuths.
     */
    public GeodeticCalculator(final Ellipsoid ellipsoid) {
        this(ellipsoid, null);
    }

    /**
     * Constructs a new geodetic calculator expecting coordinates in the supplied CRS. The ellipsoid will be inferred
     * from the CRS.
     *
     * @param crs The reference system for the {@link Position} objects.
     * @since 2.2
     */
    public GeodeticCalculator(final CoordinateReferenceSystem crs) {
        this(CRS.getEllipsoid(crs), crs);
    }

    /** For internal use by public constructors only. */
    private GeodeticCalculator(final Ellipsoid ellipsoid, final CoordinateReferenceSystem crs) {
        if (ellipsoid == null) {
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.NULL_ARGUMENT_$1, "ellipsoid"));
        }
        this.ellipsoid = ellipsoid;
        semiMajorAxis = ellipsoid.getSemiMajorAxis();
        flattening = 1 / ellipsoid.getInverseFlattening();
        geod = new Geodesic(semiMajorAxis, flattening);
        if (crs != null) {
            coordinateReferenceSystem = crs;
            geographicCRS = getGeographicCRS(crs);
            /*
             * Note: there is no need to set Hints.LENIENT_DATUM_SHIFT to Boolean.TRUE here since
             *       the target CRS computed by our internal getGeographicCRS(crs) method should
             *       returns a CRS using the same datum than the specified CRS. If the factory
             *       fails with a "Bursa-Wolf parameters required" error message, then we probably
             *       have a bug somewhere.
             */
            userToGeodetic = new TransformedPosition(crs, geographicCRS, null);
        } else {
            userToGeodetic = null;
        }
    }

    ///////////////////////////////////////////////////////////
    ////////                                           ////////
    ////////        H E L P E R   M E T H O D S        ////////
    ////////                                           ////////
    ///////////////////////////////////////////////////////////

    /** Returns the first two-dimensional geographic CRS using standard axis, creating one if needed. */
    private static GeographicCRS getGeographicCRS(final CoordinateReferenceSystem crs) {
        if (crs instanceof GeographicCRS) {
            final CoordinateSystem cs = crs.getCoordinateSystem();
            if (cs.getDimension() == 2
                    && isStandard(cs.getAxis(0), AxisDirection.EAST)
                    && isStandard(cs.getAxis(1), AxisDirection.NORTH)) {
                return (GeographicCRS) crs;
            }
        }
        final Datum datum = CRSUtilities.getDatum(crs);
        if (datum instanceof GeodeticDatum) {
            return new DefaultGeographicCRS("Geodetic", (GeodeticDatum) datum, DefaultEllipsoidalCS.GEODETIC_2D);
        }
        if (crs instanceof CompoundCRS) {
            for (final CoordinateReferenceSystem component : ((CompoundCRS) crs).getCoordinateReferenceSystems()) {
                final GeographicCRS candidate = getGeographicCRS(component);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        throw new IllegalArgumentException(ErrorKeys.ILLEGAL_COORDINATE_REFERENCE_SYSTEM);
    }

    /**
     * Returns {@code true} if the specified axis is oriented toward the specified direction and uses decimal degrees
     * units.
     */
    private static boolean isStandard(final CoordinateSystemAxis axis, final AxisDirection direction) {
        return direction.equals(axis.getDirection()) && NonSI.DEGREE_ANGLE.equals(axis.getUnit());
    }

    /**
     * Checks the latitude validity. The argument {@code latitude} should be greater than or equal to -90 degrees and
     * less than or equal to +90 degrees.
     *
     * @param latitude The latitude value in <strong>decimal degrees</strong>.
     * @throws IllegalArgumentException if {@code latitude} is not between -90 and +90 degrees.
     */
    private static void checkLatitude(final double latitude) throws IllegalArgumentException {
        if (!(latitude >= Latitude.MIN_VALUE && latitude <= Latitude.MAX_VALUE)) {
            final Object arg0 = new Latitude(latitude);
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.LATITUDE_OUT_OF_RANGE_$1, arg0));
        }
    }

    /**
     * Checks the longitude validity. The argument {@code longitude} should be finite.
     *
     * @param longitude The longitude value in <strong>decimal degrees</strong>.
     * @throws IllegalArgumentException if {@code longitude} is not finite.
     */
    private static void checkLongitude(final double longitude) throws IllegalArgumentException {
        if (!(Math.abs(longitude) <= Double.MAX_VALUE)) {
            final Object arg1 = new Longitude(longitude);
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "longitude", arg1));
        }
    }

    /**
     * Checks the azimuth validity. The argument {@code azimuth} should be finite.
     *
     * @param azimuth The azimuth value in <strong>decimal degrees</strong>.
     * @throws IllegalArgumentException if {@code azimuth} is not finite.
     */
    private static void checkAzimuth(final double azimuth) throws IllegalArgumentException {
        if (!(Math.abs(azimuth) <= Double.MAX_VALUE)) {
            final Object arg1 = new Longitude(azimuth);
            throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "azimuth", arg1));
        }
    }

    /**
     * Checks the orthodromic distance validity. Arguments {@code orthodromicDistance} should be finite.
     *
     * @param distance The orthodromic distance value.
     * @throws IllegalArgumentException if {@code orthodromic distance} is not finite.
     */
    private static void checkOrthodromicDistance(final double distance) throws IllegalArgumentException {
        if (!(Math.abs(distance) <= Double.MAX_VALUE)) {
            throw new IllegalArgumentException(
                    MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "distance", distance));
        }
    }

    /**
     * Format the specified coordinates using the specified formatter, which should be an instance of
     * {@link CoordinateFormat}.
     */
    private static String format(final Format cf, final double longitude, final double latitude) {
        return cf.format(new GeneralPosition(longitude, latitude));
    }

    ///////////////////////////////////////////////////////////////
    ////////                                               ////////
    ////////        G E O D E T I C   M E T H O D S        ////////
    ////////                                               ////////
    ///////////////////////////////////////////////////////////////

    /**
     * Returns the coordinate reference system for all methods working on {@link Position} objects. This is the CRS
     * specified at {@linkplain #GeodeticCalculator(CoordinateReferenceSystem) construction time}.
     *
     * @return The CRS for all {@link Position}s.
     * @since 2.2
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        if (coordinateReferenceSystem == null) {
            coordinateReferenceSystem = getGeographicCRS();
        }
        return coordinateReferenceSystem;
    }

    /**
     * Returns the geographic coordinate reference system for all methods working on {@link Point2D} objects. This is
     * inferred from the CRS specified at {@linkplain #GeodeticCalculator(CoordinateReferenceSystem) construction time}.
     *
     * @return The CRS for {@link Point2D}s.
     * @since 2.3
     */
    public GeographicCRS getGeographicCRS() {
        if (geographicCRS == null) {
            final String name = Vocabulary.format(VocabularyKeys.GEODETIC_2D);
            geographicCRS = new DefaultGeographicCRS(
                    name,
                    new DefaultGeodeticDatum(name, getEllipsoid(), DefaultPrimeMeridian.GREENWICH),
                    DefaultEllipsoidalCS.GEODETIC_2D);
        }
        return geographicCRS;
    }

    /**
     * Returns the referenced ellipsoid.
     *
     * @return The referenced ellipsoid.
     */
    public Ellipsoid getEllipsoid() {
        return ellipsoid;
    }

    /**
     * Set the starting point in geographic coordinates. The {@linkplain #getAzimuth() azimuth}, the
     * {@linkplain #getOrthodromicDistance() orthodromic distance} and the {@linkplain #getDestinationGeographicPoint()
     * destination point} are discarted. They will need to be specified again.
     *
     * @param longitude The longitude in decimal degrees between -180 and +180°
     * @param latitude The latitude in decimal degrees between -90 and +90°
     * @throws IllegalArgumentException if the longitude or the latitude is out of bounds.
     * @since 2.3
     */
    public void setStartingGeographicPoint(double longitude, double latitude) throws IllegalArgumentException {
        // Check first in case an exception is raised
        // (in other words, we change all or nothing).
        checkLongitude(longitude);
        checkLatitude(latitude);
        // Check passed. Now performs the changes in this object.
        long1 = longitude;
        lat1 = latitude;
        destinationValid = false;
        directionValid = false;
    }

    /**
     * Set the starting point in geographic coordinates. The <var>x</var> and <var>y</var> coordinates must be the
     * longitude and latitude in decimal degrees, respectively.
     *
     * <p>This is a convenience method for <code>
     * {@linkplain #setStartingGeographicPoint(double,double)
     * setStartingGeographicPoint}(x,y)</code>.
     *
     * @param point The starting point.
     * @throws IllegalArgumentException if the longitude or the latitude is out of bounds.
     * @since 2.3
     */
    public void setStartingGeographicPoint(final Point2D point) throws IllegalArgumentException {
        setStartingGeographicPoint(point.getX(), point.getY());
    }

    /**
     * Set the starting position in user coordinates, which doesn't need to be geographic. The coordinate reference
     * system is the one specified to the {@linkplain #GeodeticCalculator(CoordinateReferenceSystem) constructor}.
     *
     * @param position The position in user coordinate reference system.
     * @throws TransformException if the position can't be transformed.
     * @since 2.3
     */
    public void setStartingPosition(Position position) throws TransformException {
        if (userToGeodetic != null) {
            userToGeodetic.transform(position);
            position = userToGeodetic;
        }
        setStartingGeographicPoint(position.getOrdinate(0), position.getOrdinate(1));
    }

    /**
     * Returns the starting point in geographic coordinates. The <var>x</var> and <var>y</var> coordinates are the
     * longitude and latitude in decimal degrees, respectively. If the starting point has never been set, then the
     * default value is (0,0).
     *
     * @return The starting point in geographic coordinates.
     * @since 2.3
     */
    public Point2D getStartingGeographicPoint() {
        return new Point2D.Double(long1, lat1);
    }

    /**
     * Returns the starting position in user coordinates, which doesn't need to be geographic. The coordinate reference
     * system is the one specified to the {@linkplain #GeodeticCalculator(CoordinateReferenceSystem) constructor}.
     *
     * @return The starting position in user CRS.
     * @throws TransformException if the position can't be transformed to user coordinates.
     * @since 2.3
     */
    public Position getStartingPosition() throws TransformException {
        Position position = userToGeodetic;
        if (position == null) {
            position = new Position2D();
        }
        position.setOrdinate(0, long1);
        position.setOrdinate(1, lat1);
        if (userToGeodetic != null) {
            position = userToGeodetic.inverseTransform();
        }
        return position;
    }

    /**
     * Set the destination point in geographic coordinates. The azimuth and distance values will be updated as a side
     * effect of this call. They will be recomputed the next time {@link #getAzimuth()} or
     * {@link #getOrthodromicDistance()} are invoked.
     *
     * @param longitude The longitude in decimal degrees
     * @param latitude The latitude in decimal degrees between -90 and +90°
     * @throws IllegalArgumentException if the longitude or the latitude is out of bounds.
     * @since 2.3
     */
    public void setDestinationGeographicPoint(double longitude, double latitude) throws IllegalArgumentException {
        // Check first in case an exception is raised
        // (in other words, we change all or nothing).
        checkLongitude(longitude);
        checkLatitude(latitude);
        // Check passed. Now performs the changes in this object.
        long2 = longitude;
        lat2 = latitude;
        destinationValid = true;
        directionValid = false;
    }

    /**
     * Set the destination point in geographic coordinates. The <var>x</var> and <var>y</var> coordinates must be the
     * longitude and latitude in decimal degrees, respectively.
     *
     * <p>This is a convenience method for <code>
     * {@linkplain #setDestinationGeographicPoint(double,double)
     * setDestinationGeographicPoint}(x,y)</code>.
     *
     * @param point The destination point.
     * @throws IllegalArgumentException if the longitude or the latitude is out of bounds.
     * @since 2.3
     */
    public void setDestinationGeographicPoint(final Point2D point) throws IllegalArgumentException {
        setDestinationGeographicPoint(point.getX(), point.getY());
    }

    /**
     * Set the destination position in user coordinates, which doesn't need to be geographic. The coordinate reference
     * system is the one specified to the {@linkplain #GeodeticCalculator(CoordinateReferenceSystem) constructor}.
     *
     * @param position The position in user coordinate reference system.
     * @throws TransformException if the position can't be transformed.
     * @since 2.2
     */
    public void setDestinationPosition(Position position) throws TransformException {
        if (userToGeodetic != null) {
            userToGeodetic.transform(position);
            position = userToGeodetic;
        }
        setDestinationGeographicPoint(position.getOrdinate(0), position.getOrdinate(1));
    }

    /**
     * Returns the destination point. This method returns the point set by the last call to a <code>
     * {@linkplain #setDestinationGeographicPoint(double,double)
     * setDestinationGeographicPoint}(...)</code> method, <strong>except</strong> if <code>
     * {@linkplain #setDirection(double,double) setDirection}(...)</code> has been invoked after. In this later case,
     * the destination point will be computed from the {@linkplain #getStartingGeographicPoint starting point} to the
     * azimuth and distance specified.
     *
     * @return The destination point. The <var>x</var> and <var>y</var> coordinates are the longitude and latitude in
     *     decimal degrees, respectively.
     * @throws IllegalStateException if the azimuth and the distance have not been set.
     * @since 2.3
     */
    public Point2D getDestinationGeographicPoint() throws IllegalStateException {
        if (!destinationValid) {
            computeDestinationPoint();
        }
        return new Point2D.Double(long2, lat2);
    }

    /**
     * Returns the destination position in user coordinates, which doesn't need to be geographic. The coordinate
     * reference system is the one specified to the {@linkplain #GeodeticCalculator(CoordinateReferenceSystem)
     * constructor}.
     *
     * @return The destination position in user CRS.
     * @throws TransformException if the position can't be transformed to user coordinates.
     * @since 2.2
     */
    public Position getDestinationPosition() throws TransformException {
        if (!destinationValid) {
            computeDestinationPoint();
        }
        Position position = userToGeodetic;
        if (position == null) {
            position = new Position2D();
        }
        position.setOrdinate(0, long2);
        position.setOrdinate(1, lat2);
        if (userToGeodetic != null) {
            position = userToGeodetic.inverseTransform();
        }
        return position;
    }

    /**
     * Set the azimuth and the distance from the {@linkplain #getStartingGeographicPoint starting point}. The
     * destination point will be updated as a side effect of this call. It will be recomputed the next time
     * {@link #getDestinationGeographicPoint()} is invoked.
     *
     * @param azimuth The azimuth in decimal degrees
     * @param distance The orthodromic distance in the same units as the {@linkplain #getEllipsoid ellipsoid} axis
     *     (meters by default)
     * @throws IllegalArgumentException if the azimuth or the distance is out of bounds.
     * @see #getAzimuth
     * @see #getOrthodromicDistance
     */
    public void setDirection(double azimuth, final double distance) throws IllegalArgumentException {
        // Check first in case an exception is raised
        // (in other words, we change all or nothing).
        checkAzimuth(azimuth);
        checkOrthodromicDistance(distance);
        // Check passed. Now performs the changes in this object.
        this.azimuth = azimuth;
        this.distance = distance;
        destinationValid = false;
        directionValid = true;
    }

    /**
     * Returns the azimuth. This method returns the value set by the last call to <code>
     * {@linkplain #setDirection(double,double) setDirection}(azimuth,distance)</code>, <strong>except</strong> if
     * <code>{@linkplain #setDestinationGeographicPoint(double,double)
     * setDestinationGeographicPoint}(...)</code> has been invoked after. In this later case, the azimuth will be
     * computed from the {@linkplain #getStartingGeographicPoint starting point} to the destination point.
     *
     * @return The azimuth, in decimal degrees from -180° to +180°.
     * @throws IllegalStateException if the destination point has not been set.
     * @todo Current implementation will provides an innacurate value for antipodal points. For now a warning is logged
     *     in such case. In a future version (if we have volunter time) we should provides a solution (search Internet
     *     for "<cite>azimuth antipodal points</cite>").
     */
    public double getAzimuth() throws IllegalStateException {
        if (!directionValid) {
            computeDirection();
        }
        return azimuth;
    }

    /**
     * Returns the orthodromic distance (expressed in meters). This method returns the value set by the last call to
     * <code>
     * {@linkplain #setDirection(double,double) setDirection}(azimuth,distance)</code>, <strong>except</strong> if
     * <code>{@linkplain #setDestinationGeographicPoint(double,double)
     * setDestinationGeographicPoint}(...)</code> has been invoked after. In this later case, the distance will be
     * computed from the {@linkplain #getStartingGeographicPoint starting point} to the destination point.
     *
     * @return The orthodromic distance, in the same units as the {@linkplain #getEllipsoid ellipsoid} axis.
     * @throws IllegalStateException if the destination point has not been set.
     */
    public double getOrthodromicDistance() throws IllegalStateException {
        if (!directionValid) {
            computeDirection();
        }
        return distance;
    }

    /**
     * Computes the destination point from the {@linkplain #getStartingGeographicPoint starting point}, the
     * {@linkplain #getAzimuth azimuth} and the {@linkplain #getOrthodromicDistance orthodromic distance}.
     *
     * @throws IllegalStateException if the azimuth and the distance have not been set.
     * @see #getDestinationGeographicPoint
     */
    private void computeDestinationPoint() throws IllegalStateException {
        if (!directionValid) {
            throw new IllegalStateException(ErrorKeys.DIRECTION_NOT_SET);
        }
        GeodesicData g = geod.Direct(lat1, long1, azimuth, distance);
        lat2 = g.lat2;
        long2 = g.lon2;
        destinationValid = true;
    }

    /**
     * Calculates the meridian arc length between two points in the same meridian in the referenced ellipsoid.
     *
     * @param latitude1 The latitude of the first point (in decimal degrees).
     * @param latitude2 The latitude of the second point (in decimal degrees).
     * @return Returned the meridian arc length between latitude1 and latitude2
     */
    public double getMeridianArcLength(final double latitude1, final double latitude2) {
        checkLatitude(latitude1);
        checkLatitude(latitude2);
        GeodesicData g = geod.Inverse(latitude1, 0, latitude2, 0, GeodesicMask.DISTANCE);
        return g.s12;
    }

    /**
     * Computes the azimuth and orthodromic distance from the {@linkplain #getStartingGeographicPoint starting point}
     * and the {@linkplain #getDestinationGeographicPoint destination point}.
     *
     * @throws IllegalStateException if the destination point has not been set.
     * @see #getAzimuth
     * @see #getOrthodromicDistance
     */
    private void computeDirection() throws IllegalStateException {
        if (!destinationValid) {
            throw new IllegalStateException(ErrorKeys.DESTINATION_NOT_SET);
        }
        GeodesicData g = geod.Inverse(lat1, long1, lat2, long2);
        azimuth = g.azi1;
        distance = g.s12;
        directionValid = true;
    }

    /**
     * Calculates the geodetic curve between two points in the referenced ellipsoid. A curve in the ellipsoid is a path
     * which points contain the longitude and latitude of the points in the geodetic curve. The geodetic curve is
     * computed from the {@linkplain #getStartingGeographicPoint starting point} to the
     * {@linkplain #getDestinationGeographicPoint destination point}.
     *
     * @param numberOfPoints The number of vertex in the geodetic curve. <strong>NOTE:</strong> This argument is only a
     *     hint and may be ignored in future version (if we compute a real curve rather than a list of line segments).
     * @return The path that represents the geodetic curve from the {@linkplain #getStartingGeographicPoint starting
     *     point} to the {@linkplain #getDestinationGeographicPoint destination point}.
     * @todo We should check for cases where the path cross the 90°N, 90°S, 90°E or 90°W boundaries.
     */
    public Shape getGeodeticCurve(final int numberOfPoints) {
        List<Point2D> points = getGeodeticPath(numberOfPoints);
        final GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, numberOfPoints + 1);
        Point2D start = points.get(0);
        path.moveTo(start.getX(), start.getY());
        for (int i = 1; i < points.size(); i++) {
            Point2D p = points.get(i);
            path.lineTo(p.getX(), p.getY());
        }

        return path;
    }

    /**
     * Calculates the geodetic curve between two points in the referenced ellipsoid. A curve in the ellipsoid is a path
     * which points contain the longitude and latitude of the points in the geodetic curve. The geodetic curve is
     * computed from the {@linkplain #getStartingGeographicPoint starting point} to the
     * {@linkplain #getDestinationGeographicPoint destination point}.
     *
     * @return The path that represents the geodetic curve from the {@linkplain #getStartingGeographicPoint starting
     *     point} to the {@linkplain #getDestinationGeographicPoint destination point}.
     */
    public Shape getGeodeticCurve() {
        return getGeodeticCurve(10);
    }

    /**
     * Calculates the geodetic curve between two points in the referenced ellipsoid. A curve in the ellipsoid is a path
     * which points contain the longitude and latitude of the points in the geodetic curve. The geodetic curve is
     * computed from the {@linkplain #getStartingGeographicPoint starting point} to the
     * {@linkplain #getDestinationGeographicPoint destination point}.
     *
     * @param numPoints The number of vertices <strong>between</strong> the start and destination points
     * @return vertices approximating the curve
     * @todo We should check for cases where the path cross the 90Â°N, 90Â°S, 90Â°E or 90Â°W boundaries.
     */
    public List<Point2D> getGeodeticPath(int numPoints) {
        if (numPoints < 0) {
            throw new IllegalArgumentException(
                    MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, "numPoints", numPoints));
        }

        List<Point2D> points = new ArrayList<>(numPoints + 2);

        if (!directionValid) {
            computeDirection();
        }

        if (!destinationValid) {
            computeDestinationPoint();
        }

        final double delta = distance / (numPoints + 1);

        points.add(new Point2D.Double(long1, lat1));
        GeodesicLine line = geod.Line(lat1, long1, azimuth);

        for (int i = 1; i <= numPoints + 1; i++) {
            GeodesicData g =
                    line.Position(i * delta, GeodesicMask.LATITUDE | GeodesicMask.LONGITUDE | GeodesicMask.LONG_UNROLL);
            points.add(new Point2D.Double(g.lon2, g.lat2));
        }

        return points;
    }

    /** Returns a string representation of the current state of this calculator. */
    @Override
    @SuppressWarnings("PMD.CloseResource")
    public String toString() {
        final Vocabulary resources = Vocabulary.getResources(null);
        final TableWriter buffer = new TableWriter(null, " ");
        if (coordinateReferenceSystem != null) {
            buffer.write(resources.getLabel(VocabularyKeys.COORDINATE_REFERENCE_SYSTEM));
            buffer.nextColumn();
            buffer.write(coordinateReferenceSystem.getName().getCode());
            buffer.nextLine();
        }
        if (ellipsoid != null) {
            buffer.write(resources.getLabel(VocabularyKeys.ELLIPSOID));
            buffer.nextColumn();
            buffer.write(ellipsoid.getName().getCode());
            buffer.nextLine();
        }
        final CoordinateFormat cf = new CoordinateFormat();
        final Format nf = cf.getFormat(0);
        buffer.write(resources.getLabel(VocabularyKeys.SOURCE_POINT));
        buffer.nextColumn();
        buffer.write(format(cf, long1, lat1));
        buffer.nextLine();
        if (destinationValid) {
            buffer.write(resources.getLabel(VocabularyKeys.TARGET_POINT));
            buffer.nextColumn();
            buffer.write(format(cf, long2, lat2));
            buffer.nextLine();
        }
        if (directionValid) {
            buffer.write(resources.getLabel(VocabularyKeys.AZIMUTH));
            buffer.nextColumn();
            buffer.write(nf.format(new Angle(azimuth)));
            buffer.nextLine();
        }
        if (directionValid) {
            buffer.write(resources.getLabel(VocabularyKeys.ORTHODROMIC_DISTANCE));
            buffer.nextColumn();
            buffer.write(nf.format(distance));
            if (ellipsoid != null) {
                buffer.write(' ');
                buffer.write(ellipsoid.getAxisUnit().toString());
            }
            buffer.nextLine();
        }
        return buffer.toString();
    }
}
