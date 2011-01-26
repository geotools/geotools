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
 *
 *    Portions of this file is adapted from Fortran code provided by NOAA.
 *    Programmed for CDC-6600 by LCDR L.Pfeifer NGS ROCKVILLE MD 18FEB75
 *    Modified for IBM SYSTEM 360 by John G.Gergen NGS ROCKVILLE MD 7507
 *    Source: ftp://ftp.ngs.noaa.gov/pub/pcsoft/for_inv.3d/source/
 */
package org.geotools.referencing;

import java.awt.Shape;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.GeneralPath;
import java.text.Format;
import javax.measure.unit.NonSI;
import static java.lang.Math.*;

import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.operation.TransformException;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.DirectPosition;

import org.geotools.measure.Angle;
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;
import org.geotools.measure.CoordinateFormat;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.TransformedDirectPosition;
import org.geotools.referencing.datum.DefaultEllipsoid;
import org.geotools.referencing.datum.DefaultPrimeMeridian;
import org.geotools.referencing.datum.DefaultGeodeticDatum;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.resources.CRSUtilities;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.io.TableWriter;
import org.geotools.util.logging.Logging;


/**
 * Performs geodetic calculations on an {@linkplain Ellipsoid ellipsoid}. This class encapsulates
 * a generic ellipsoid and calculates the following properties:
 * <p>
 * <ul>
 *   <li>Distance and azimuth between two points.</li>
 *   <li>Point located at a given distance and azimuth from an other point.</li>
 * </ul>
 * <p>
 * The calculation use the following informations:
 * <p>
 * <ul>
 *   <li>The {@linkplain #setStartingPosition starting position}, which is always considered valid.
 *       It is initially set at (0,0) and can only be changed to another legitimate value.</li>
 *   <li><strong>Only one</strong> of the following:
 *       <ul>
 *         <li>The {@linkplain #setDestinationPosition destination position}, or</li>
 *         <li>An {@linkplain #setDirection azimuth and distance}.</li>
 *       </ul>
 *       The latest one set overrides the other and determines what will be calculated.</li>
 * </ul>
 * <p>
 * Note: This class is not thread-safe. If geodetic calculations are needed in a multi-threads
 * environment, create one distinct instance of {@code GeodeticCalculator} for each thread.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Daniele Franzoni
 * @author Martin Desruisseaux
 */
public class GeodeticCalculator {
    /**
     * Tolerance factors from the strictest (<code>TOLERANCE_0</CODE>)
     * to the most relax one (<code>TOLERANCE_3</CODE>).
     */
    private static final double TOLERANCE_0 = 5.0e-15,  // tol0
                                TOLERANCE_1 = 5.0e-14,  // tol1
                                TOLERANCE_2 = 5.0e-13,  // tt
                                TOLERANCE_3 = 7.0e-3;   // tol2

    /**
     * Tolerance factor for assertions. It has no impact on computed values.
     */
    private static final double TOLERANCE_CHECK = 1E-8;

    /**
     * The transform from user coordinates to geodetic coordinates used for computation,
     * or {@code null} if no transformations are required.
     */
    private final TransformedDirectPosition userToGeodetic;

    /**
     * The coordinate reference system for all methods working on {@link Position} objects.
     * If {@code null}, will be created the first time {@link #getCoordinateReferenceSystem}
     * is invoked.
     */
    private CoordinateReferenceSystem coordinateReferenceSystem;

    /**
     * The coordinate reference system for all methods working on {@link Point2D} objects.
     * If {@code null}, will be created the first time {@link #getGeographicCRS} is invoked.
     */
    private GeographicCRS geographicCRS;

    /**
     * The encapsulated ellipsoid.
     */
    private final Ellipsoid ellipsoid;

    /*
     * The semi major axis of the refereced ellipsoid.
     */
    private final double semiMajorAxis;

    /*
     * The semi minor axis of the refereced ellipsoid.
     */
    private final double semiMinorAxis;

    /*
     * The eccenticity squared of the refereced ellipsoid.
     */
    private final double eccentricitySquared;

    /*
     * The maximum orthodromic distance that could be calculated onto the referenced ellipsoid.
     */
    private final double maxOrthodromicDistance;

    /**
     * GPNARC parameters computed from the ellipsoid.
     */
    private final double A, B, C, D, E, F;

    /**
     * GPNHRI parameters computed from the ellipsoid.
     *
     * {@code f} if the flattening of the referenced ellipsoid. {@code f2},
     * {@code f3} and {@code f4} are <var>f<sup>2</sup></var>,
     * <var>f<sup>3</sup></var> and <var>f<sup>4</sup></var> respectively.
     */
    private final double fo, f, f2, f3, f4;

    /**
     * Parameters computed from the ellipsoid.
     */
    private final double T1, T2, T4, T6;

    /**
     * Parameters computed from the ellipsoid.
     */
    private final double a01, a02, a03, a21, a22, a23, a42, a43, a63;

    /**
     * The (<var>latitude</var>, <var>longitude</var>) coordinate of the first point
     * <strong>in radians</strong>. This point is set by {@link #setStartingGeographicPoint}.
     */
    private double lat1, long1;

    /**
     * The (<var>latitude</var>, <var>longitude</var>) coordinate of the destination point
     * <strong>in radians</strong>. This point is set by {@link #setDestinationGeographicPoint}.
     */
    private double lat2, long2;

    /**
     * The distance and azimuth (in radians) from the starting point
     * ({@link #long1}, {@link #lat1}) to the destination point
     * ({@link #long2}, {@link #lat2}).
     */
    private double distance, azimuth;

    /**
     * Tell if the destination point is valid.
     * {@code false} if {@link #long2} and {@link #lat2} need to be computed.
     */
    private boolean destinationValid;

    /**
     * Tell if the azimuth and the distance are valids.
     * {@code false} if {@link #distance} and {@link #azimuth} need to be computed.
     */
    private boolean directionValid;

    /**
     * {@code true} if the source and destination points are almost antipodal. If {@code true},
     * then the distance and direction computed by {@link #computeDirection} are likely to be
     * innacurate.
     */
    private boolean antipodal;

    /**
     * Constructs a new geodetic calculator associated with the WGS84 ellipsoid.
     */
    public GeodeticCalculator() {
        this(DefaultEllipsoid.WGS84);
    }

    /**
     * Constructs a new geodetic calculator associated with the specified ellipsoid.
     * All calculations done by the new instance are referenced to this ellipsoid.
     *
     * @param ellipsoid The ellipsoid onto which calculates distances and azimuths.
     */
    public GeodeticCalculator(final Ellipsoid ellipsoid) {
        this(ellipsoid, null);
    }

    /**
     * Constructs a new geodetic calculator expecting coordinates in the supplied CRS.
     * The ellipsoid will be inferred from the CRS.
     *
     * @param  crs The reference system for the {@link Position} objects.
     *
     * @since 2.2
     */
    public GeodeticCalculator(final CoordinateReferenceSystem crs) {
        this(CRS.getEllipsoid(crs), crs);
    }

    /**
     * For internal use by public constructors only.
     */
    private GeodeticCalculator(final Ellipsoid ellipsoid, final CoordinateReferenceSystem crs) {
        if (ellipsoid == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, "ellipsoid"));
        }
        this.ellipsoid     = ellipsoid;
        this.semiMajorAxis = ellipsoid.getSemiMajorAxis();
        this.semiMinorAxis = ellipsoid.getSemiMinorAxis();
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
            userToGeodetic = new TransformedDirectPosition(crs, geographicCRS, null);
        } else {
            userToGeodetic = null;
        }

        /* calculation of GPNHRI parameters */
        f  = (semiMajorAxis-semiMinorAxis) / semiMajorAxis;
        fo = 1.0 - f;
        f2 = f*f;
        f3 = f*f2;
        f4 = f*f3;
        eccentricitySquared = f * (2.0-f);

        /* Calculation of GNPARC parameters */
        final double E2 = eccentricitySquared;
        final double E4 = E2*E2;
        final double E6 = E4*E2;
        final double E8 = E6*E2;
        final double EX = E8*E2;

        A =  1.0+0.75*E2+0.703125*E4+0.68359375 *E6+0.67291259765625*E8+0.6661834716796875 *EX;
        B =      0.75*E2+0.9375  *E4+1.025390625*E6+1.07666015625   *E8+1.1103057861328125 *EX;
        C =              0.234375*E4+0.41015625 *E6+0.538330078125  *E8+0.63446044921875   *EX;
        D =                          0.068359375*E6+0.15380859375   *E8+0.23792266845703125*EX;
        E =                                         0.01922607421875*E8+0.0528717041015625 *EX;
        F =                                                             0.00528717041015625*EX;

        maxOrthodromicDistance = semiMajorAxis * (1.0 - E2) * PI * A - 1.0;

        T1 = 1.0;
        T2 = -0.25*f*(1.0 + f + f2);
        T4 = 0.1875 * f2 * (1.0+2.25*f);
        T6 = 0.1953125 * f3;

        final double a = f3*(1.0+2.25*f);
        a01 = -f2*(1.0+f+f2)/4.0;
        a02 = 0.1875*a;
        a03 = -0.1953125*f4;
        a21 = -a01;
        a22 = -0.25*a;
        a23 = 0.29296875*f4;
        a42 = 0.03125*a;
        a43 = 0.05859375*f4;
        a63 = 5.0*f4/768.0;
    }





    ///////////////////////////////////////////////////////////
    ////////                                           ////////
    ////////        H E L P E R   M E T H O D S        ////////
    ////////                                           ////////
    ///////////////////////////////////////////////////////////

    /**
     * Returns the first two-dimensional geographic CRS using standard axis, creating one if needed.
     */
    private static GeographicCRS getGeographicCRS(final CoordinateReferenceSystem crs) {
        if (crs instanceof GeographicCRS) {
            final CoordinateSystem cs = crs.getCoordinateSystem();
            if (cs.getDimension() == 2 &&
                isStandard(cs.getAxis(0), AxisDirection.EAST) &&
                isStandard(cs.getAxis(1), AxisDirection.NORTH))
            {
                return (GeographicCRS) crs;
            }
        }
        final Datum datum = CRSUtilities.getDatum(crs);
        if (datum instanceof GeodeticDatum) {
            return new DefaultGeographicCRS("Geodetic", (GeodeticDatum) datum,
                    DefaultEllipsoidalCS.GEODETIC_2D);
        }
        if (crs instanceof CompoundCRS) {
            for (final CoordinateReferenceSystem component : ((CompoundCRS) crs).getCoordinateReferenceSystems()) {
                final GeographicCRS candidate = getGeographicCRS(component);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_COORDINATE_REFERENCE_SYSTEM));
    }

    /**
     * Returns {@code true} if the specified axis is oriented toward the specified direction and
     * uses decimal degrees units.
     */
    private static boolean isStandard(final CoordinateSystemAxis axis, final AxisDirection direction) {
        return direction.equals(axis.getDirection()) && NonSI.DEGREE_ANGLE.equals(axis.getUnit());
    }

    /**
     * Returns an angle between -{@linkplain Math#PI PI} and {@linkplain Math#PI PI}
     * equivalent to the specified angle in radians.
     *
     * @param  alpha An angle value in radians.
     * @return The angle between between -{@linkplain Math#PI PI} and {@linkplain Math#PI PI}.
     */
    private static double castToAngleRange(final double alpha) {
        return alpha - (2*PI) * floor(alpha / (2*PI) + 0.5);
    }

    /**
     * Checks the latidude validity. The argument {@code latidude} should be
     * greater or equal than -90 degrees and lower or equals than +90 degrees. As
     * a convenience, this method returns the latitude in radians.
     *
     * @param  latitude The latitude value in <strong>decimal degrees</strong>.
     * @return The latitude value in <strong>radians</strong>.
     * @throws IllegalArgumentException if {@code latitude} is not between -90 and +90 degrees.
     */
    private static double checkLatitude(final double latitude) throws IllegalArgumentException {
        if (latitude >= Latitude.MIN_VALUE && latitude <= Latitude.MAX_VALUE) {
            return toRadians(latitude);
        }
        throw new IllegalArgumentException(Errors.format(
                ErrorKeys.LATITUDE_OUT_OF_RANGE_$1, new Latitude(latitude)));
    }

    /**
     * Checks the longitude validity. The argument {@code longitude} should be
     * greater or equal than -180 degrees and lower or equals than +180 degrees. As
     * a convenience, this method returns the longitude in radians.
     *
     * @param  longitude The longitude value in <strong>decimal degrees</strong>.
     * @return The longitude value in <strong>radians</strong>.
     * @throws IllegalArgumentException if {@code longitude} is not between -180 and +180 degrees.
     */
    private static double checkLongitude(final double longitude) throws IllegalArgumentException {
        if (longitude >= Longitude.MIN_VALUE && longitude <= Longitude.MAX_VALUE) {
            return toRadians(longitude);
        }
        throw new IllegalArgumentException(Errors.format(
                ErrorKeys.LONGITUDE_OUT_OF_RANGE_$1, new Longitude(longitude)));
    }

    /**
     * Checks the azimuth validity. The argument {@code azimuth}  should be
     * greater or equal than -180 degrees and lower or equals than +180 degrees.
     * As a convenience, this method returns the azimuth in radians.
     *
     * @param  azimuth The azimuth value in <strong>decimal degrees</strong>.
     * @return The azimuth value in <strong>radians</strong>.
     * @throws IllegalArgumentException if {@code azimuth} is not between -180 and +180 degrees.
     */
    private static double checkAzimuth(final double azimuth) throws IllegalArgumentException {
        if (azimuth >= -180.0 && azimuth <= 180.0) {
            return toRadians(azimuth);
        }
        throw new IllegalArgumentException(Errors.format(
                ErrorKeys.AZIMUTH_OUT_OF_RANGE_$1, new Longitude(azimuth)));
    }

    /**
     * Checks the orthodromic distance validity. Arguments {@code orthodromicDistance}
     * should be greater or equal than 0 and lower or equals than the maximum orthodromic distance.
     *
     * @param  distance The orthodromic distance value.
     * @throws IllegalArgumentException if {@code orthodromic distance} is not between
     *                                  0 and the maximum orthodromic distance.
     */
    private void checkOrthodromicDistance(final double distance)
            throws IllegalArgumentException
    {
        if (!(distance >= 0.0 && distance <= maxOrthodromicDistance)) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.DISTANCE_OUT_OF_RANGE_$4,
                    distance, 0.0, maxOrthodromicDistance, ellipsoid.getAxisUnit()));
        }
    }

    /**
     * Checks the number of verteces in a curve. Arguments {@code numberOfPoints}
     * should be not negative.
     *
     * @param  numberOfPonits The number of verteces in a curve.
     * @throws IllegalArgumentException if {@code numberOfVerteces} is negative.
     */
    private static void checkNumberOfPoints(final int numberOfPoints)
            throws IllegalArgumentException
    {
        if (numberOfPoints < 0) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,
                        "numberOfPoints", numberOfPoints));
        }
    }

    /**
     * Returns a localized "No convergence" error message. The error message
     * includes informations about starting and destination points.
     */
    private String getNoConvergenceErrorMessage() {
        final CoordinateFormat cf = new CoordinateFormat();
        return Errors.format(ErrorKeys.NO_CONVERGENCE_$2,
                             format(cf, long1, lat1), format(cf, long2, lat2));
    }

    /**
     * Format the specified coordinates using the specified formatter, which should be an instance
     * of {@link CoordinateFormat}.
     */
    private static String format(final Format cf, final double longitude, final double latitude) {
        return cf.format(new GeneralDirectPosition(toDegrees(longitude), toDegrees(latitude)));
    }




    ///////////////////////////////////////////////////////////////
    ////////                                               ////////
    ////////        G E O D E T I C   M E T H O D S        ////////
    ////////                                               ////////
    ///////////////////////////////////////////////////////////////

    /**
     * Returns the coordinate reference system for all methods working on {@link Position} objects.
     * This is the CRS specified at {@linkplain #GeodeticCalculator(CoordinateReferenceSystem)
     * construction time}.
     *
     * @return The CRS for all {@link Position}s.
     *
     * @since 2.2
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        if (coordinateReferenceSystem == null) {
            coordinateReferenceSystem = getGeographicCRS();
        }
        return coordinateReferenceSystem;
    }

    /**
     * Returns the geographic coordinate reference system for all methods working
     * on {@link Point2D} objects. This is inferred from the CRS specified at
     * {@linkplain #GeodeticCalculator(CoordinateReferenceSystem) construction time}.
     *
     * @return The CRS for {@link Point2D}s.
     *
     * @since 2.3
     */
    public GeographicCRS getGeographicCRS() {
        if (geographicCRS == null) {
            final String name = Vocabulary.format(VocabularyKeys.GEODETIC_2D);
            geographicCRS = new DefaultGeographicCRS(name,
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
     * Set the starting point in geographic coordinates.
     * The {@linkplain #getAzimuth() azimuth},
     * the {@linkplain #getOrthodromicDistance() orthodromic distance} and
     * the {@linkplain #getDestinationGeographicPoint() destination point}
     * are discarted. They will need to be specified again.
     *
     * @param  longitude The longitude in decimal degrees between -180 and +180°
     * @param  latitude  The latitude  in decimal degrees between  -90 and  +90°
     * @throws IllegalArgumentException if the longitude or the latitude is out of bounds.
     *
     * @since 2.3
     */
    public void setStartingGeographicPoint(double longitude, double latitude)
            throws IllegalArgumentException
    {
        // Check first in case an exception is raised
        // (in other words, we change all or nothing).
        longitude = checkLongitude(longitude);
        latitude  = checkLatitude (latitude);
        // Check passed. Now performs the changes in this object.
        long1 = longitude;
        lat1  = latitude;
        destinationValid = false;
        directionValid   = false;
    }

    /**
     * Set the starting point in geographic coordinates. The <var>x</var> and <var>y</var>
     * coordinates must be the longitude and latitude in decimal degrees, respectively.
     *
     * This is a convenience method for
     * <code>{@linkplain #setStartingGeographicPoint(double,double)
     * setStartingGeographicPoint}(x,y)</code>.
     *
     * @param  point The starting point.
     * @throws IllegalArgumentException if the longitude or the latitude is out of bounds.
     *
     * @since 2.3
     */
    public void setStartingGeographicPoint(final Point2D point) throws IllegalArgumentException {
        setStartingGeographicPoint(point.getX(), point.getY());
    }

    /**
     * Set the starting position in user coordinates, which doesn't need to be geographic.
     * The coordinate reference system is the one specified to the
     * {@linkplain #GeodeticCalculator(CoordinateReferenceSystem) constructor}.
     *
     * @param  position The position in user coordinate reference system.
     * @throws TransformException if the position can't be transformed.
     *
     * @since 2.3
     */
    public void setStartingPosition(final Position position) throws TransformException {
        DirectPosition p = position.getDirectPosition();
        if (userToGeodetic != null) {
            userToGeodetic.transform(p);
            p = userToGeodetic;
        }
        setStartingGeographicPoint(p.getOrdinate(0), p.getOrdinate(1));
    }

    /**
     * Returns the starting point in geographic coordinates. The <var>x</var> and <var>y</var>
     * coordinates are the longitude and latitude in decimal degrees, respectively. If the
     * starting point has never been set, then the default value is (0,0).
     *
     * @return The starting point in geographic coordinates.
     *
     * @since 2.3
     */
    public Point2D getStartingGeographicPoint() {
        return new Point2D.Double(toDegrees(long1), toDegrees(lat1));
    }

    /**
     * Returns the starting position in user coordinates, which doesn't need to be geographic.
     * The coordinate reference system is the one specified to the
     * {@linkplain #GeodeticCalculator(CoordinateReferenceSystem) constructor}.
     *
     * @return The starting position in user CRS.
     * @throws TransformException if the position can't be transformed to user coordinates.
     *
     * @since 2.3
     */
    public DirectPosition getStartingPosition() throws TransformException {
        DirectPosition position = userToGeodetic;
        if (position == null) {
            position = new DirectPosition2D();
        }
        position.setOrdinate(0, toDegrees(long1));
        position.setOrdinate(1, toDegrees( lat1));
        if (userToGeodetic != null) {
            position = userToGeodetic.inverseTransform();
        }
        return position;
    }

    /**
     * Set the destination point in geographic coordinates. The azimuth and distance values
     * will be updated as a side effect of this call. They will be recomputed the next time
     * {@link #getAzimuth()} or {@link #getOrthodromicDistance()} are invoked.
     *
     * @param  longitude The longitude in decimal degrees between -180 and +180°
     * @param  latitude  The latgitude in decimal degrees between  -90 and  +90°
     * @throws IllegalArgumentException if the longitude or the latitude is out of bounds.
     *
     * @since 2.3
     */
    public void setDestinationGeographicPoint(double longitude, double latitude)
            throws IllegalArgumentException
    {
        // Check first in case an exception is raised
        // (in other words, we change all or nothing).
        longitude = checkLongitude(longitude);
        latitude  = checkLatitude (latitude);
        // Check passed. Now performs the changes in this object.
        long2 = longitude;
        lat2  = latitude;
        destinationValid = true;
        directionValid   = false;
    }

    /**
     * Set the destination point in geographic coordinates. The <var>x</var> and <var>y</var>
     * coordinates must be the longitude and latitude in decimal degrees, respectively.
     *
     * This is a convenience method for
     * <code>{@linkplain #setDestinationGeographicPoint(double,double)
     * setDestinationGeographicPoint}(x,y)</code>.
     *
     * @param  point The destination point.
     * @throws IllegalArgumentException if the longitude or the latitude is out of bounds.
     *
     * @since 2.3
     */
    public void setDestinationGeographicPoint(final Point2D point)
            throws IllegalArgumentException
    {
        setDestinationGeographicPoint(point.getX(), point.getY());
    }

    /**
     * Set the destination position in user coordinates, which doesn't need to be geographic.
     * The coordinate reference system is the one specified to the
     * {@linkplain #GeodeticCalculator(CoordinateReferenceSystem) constructor}.
     *
     * @param  position The position in user coordinate reference system.
     * @throws TransformException if the position can't be transformed.
     *
     * @since 2.2
     */
    public void setDestinationPosition(final Position position) throws TransformException {
        DirectPosition p = position.getDirectPosition();
        if (userToGeodetic != null) {
            userToGeodetic.transform(p);
            p = userToGeodetic;
        }
        setDestinationGeographicPoint(p.getOrdinate(0), p.getOrdinate(1));
    }

    /**
     * Returns the destination point. This method returns the point set by the last
     * call to a <code>{@linkplain #setDestinationGeographicPoint(double,double)
     * setDestinationGeographicPoint}(...)</code>
     * method, <strong>except</strong> if
     * <code>{@linkplain #setDirection(double,double) setDirection}(...)</code> has been
     * invoked after. In this later case, the destination point will be computed from the
     * {@linkplain #getStartingGeographicPoint starting point} to the azimuth and distance
     * specified.
     *
     * @return The destination point. The <var>x</var> and <var>y</var> coordinates
     *         are the longitude and latitude in decimal degrees, respectively.
     * @throws IllegalStateException if the azimuth and the distance have not been set.
     *
     * @since 2.3
     */
    public Point2D getDestinationGeographicPoint() throws IllegalStateException {
        if (!destinationValid) {
            computeDestinationPoint();
        }
        return new Point2D.Double(toDegrees(long2), toDegrees(lat2));
    }

    /**
     * Returns the destination position in user coordinates, which doesn't need to be geographic.
     * The coordinate reference system is the one specified to the
     * {@linkplain #GeodeticCalculator(CoordinateReferenceSystem) constructor}.
     *
     * @return The destination position in user CRS.
     * @throws TransformException if the position can't be transformed to user coordinates.
     *
     * @since 2.2
     */
    public DirectPosition getDestinationPosition() throws TransformException {
        if (!destinationValid) {
            computeDestinationPoint();
        }
        DirectPosition position = userToGeodetic;
        if (position == null) {
            position = new DirectPosition2D();
        }
        position.setOrdinate(0, toDegrees(long2));
        position.setOrdinate(1, toDegrees( lat2));
        if (userToGeodetic != null) {
            position = userToGeodetic.inverseTransform();
        }
        return position;
    }

    /**
     * Set the azimuth and the distance from the {@linkplain #getStartingGeographicPoint
     * starting point}. The destination point will be updated as a side effect of this call.
     * It will be recomputed the next time {@link #getDestinationGeographicPoint()} is invoked.
     *
     * @param  azimuth The azimuth in decimal degrees from -180° to 180°.
     * @param  distance The orthodromic distance in the same units as the
     *         {@linkplain #getEllipsoid ellipsoid} axis.
     * @throws IllegalArgumentException if the azimuth or the distance is out of bounds.
     *
     * @see #getAzimuth
     * @see #getOrthodromicDistance
     */
    public void setDirection(double azimuth, final double distance) throws IllegalArgumentException {
        // Check first in case an exception is raised
        // (in other words, we change all or nothing).
        azimuth = checkAzimuth(azimuth);
        checkOrthodromicDistance(distance);
        // Check passed. Now performs the changes in this object.
        this.azimuth  = azimuth;
        this.distance = distance;
        destinationValid = false;
        directionValid   = true;
    }

    /**
     * Returns the azimuth. This method returns the value set by the last call to
     * <code>{@linkplain #setDirection(double,double) setDirection}(azimuth,distance)</code>,
     * <strong>except</strong> if <code>{@linkplain #setDestinationGeographicPoint(double,double)
     * setDestinationGeographicPoint}(...)</code> has been invoked after. In this later case, the
     * azimuth will be computed from the {@linkplain #getStartingGeographicPoint starting point}
     * to the destination point.
     *
     * @return The azimuth, in decimal degrees from -180° to +180°.
     * @throws IllegalStateException if the destination point has not been set.
     *
     * @todo Current implementation will provides an innacurate value for antipodal points. For
     *       now a warning is logged in such case. In a future version (if we have volunter time)
     *       we should provides a solution (search Internet for "<cite>azimuth antipodal
     *       points</cite>").
     */
    public double getAzimuth() throws IllegalStateException {
        if (!directionValid) {
            computeDirection();
            if (antipodal) {
                Logging.getLogger(GeodeticCalculator.class).warning(
                        "Azimuth is innacurate for antipodal points.");
            }
        }
        return toDegrees(azimuth);
    }

    /**
     * Returns the orthodromic distance. This method returns the value set by the last call to
     * <code>{@linkplain #setDirection(double,double) setDirection}(azimuth,distance)</code>,
     * <strong>except</strong> if <code>{@linkplain #setDestinationGeographicPoint(double,double)
     * setDestinationGeographicPoint}(...)</code> has been invoked after. In this later case, the
     * distance will be computed from the {@linkplain #getStartingGeographicPoint starting point}
     * to the destination point.
     *
     * @return The orthodromic distance, in the same units as the
     *         {@linkplain #getEllipsoid ellipsoid} axis.
     * @throws IllegalStateException if the destination point has not been set.
     */
    public double getOrthodromicDistance() throws IllegalStateException {
        if (!directionValid) {
            computeDirection();
            if (antipodal) {
                // If we are at antipodes, DefaultEllipsoid will provides a better estimation.
                if (ellipsoid instanceof DefaultEllipsoid) {
                    return ((DefaultEllipsoid) ellipsoid).orthodromicDistance(
                            toDegrees(long1), toDegrees(lat1), toDegrees(long2), toDegrees(lat2));
                }
            } else {
                assert checkOrthodromicDistance() : this;
            }
        }
        return distance;
    }

    /**
     * Computes the orthodromic distance using the algorithm implemented in the Geotools's
     * ellipsoid class (if available), and check if the error is smaller than some tolerance
     * error.
     */
    private boolean checkOrthodromicDistance() {
        if (ellipsoid instanceof DefaultEllipsoid) {
            double check;
            final DefaultEllipsoid ellipsoid = (DefaultEllipsoid) this.ellipsoid;
            check = ellipsoid.orthodromicDistance(toDegrees(long1), toDegrees(lat1),
                                                  toDegrees(long2), toDegrees(lat2));
            check = abs(distance - check);
            return check <= (distance+1) * TOLERANCE_CHECK;
        }
        return true;
    }

    /**
     * Computes the destination point from the {@linkplain #getStartingGeographicPoint starting
     * point}, the {@linkplain #getAzimuth azimuth} and the {@linkplain #getOrthodromicDistance
     * orthodromic distance}.
     *
     * @throws IllegalStateException if the azimuth and the distance have not been set.
     *
     * @see #getDestinationGeographicPoint
     */
    private void computeDestinationPoint() throws IllegalStateException {
        if (!directionValid) {
            throw new IllegalStateException(Errors.format(ErrorKeys.DIRECTION_NOT_SET));
        }
        // Protect internal variables from changes
        final double lat1     = this.lat1;
        final double long1    = this.long1;
        final double azimuth  = this.azimuth;
        final double distance = this.distance;
        /*
         * Solution of the geodetic direct problem after T.Vincenty.
         * Modified Rainsford's method with Helmert's elliptical terms.
         * Effective in any azimuth and at any distance short of antipodal.
         *
         * Latitudes and longitudes in radians positive North and East.
         * Forward azimuths at both points returned in radians from North.
         *
         * Programmed for CDC-6600 by LCDR L.Pfeifer NGS ROCKVILLE MD 18FEB75
         * Modified for IBM SYSTEM 360 by John G.Gergen NGS ROCKVILLE MD 7507
         * Ported from Fortran to Java by Daniele Franzoni.
         *
         * Source: ftp://ftp.ngs.noaa.gov/pub/pcsoft/for_inv.3d/source/forward.for
         *         subroutine DIRECT1
         */
        double TU  = fo*sin(lat1) / cos(lat1);
        double SF  = sin(azimuth);
        double CF  = cos(azimuth);
        double BAZ = (CF!=0) ? atan2(TU,CF)*2.0 : 0;
        double CU  = 1 / sqrt(TU*TU + 1.0);
        double SU  = TU*CU;
        double SA  = CU*SF;
        double C2A = 1.0 - SA*SA;
        double X   = sqrt((1.0/fo/fo - 1) * C2A + 1.0) + 1.0;
        X   = (X - 2.0) / X;
        double C   = 1.0-X;
        C   = (X*X / 4.0 + 1.0) / C;
        double D   = (0.375 * X*X - 1.0) * X;
        TU   = distance / fo / semiMajorAxis / C;
        double Y   = TU;
        double SY, CY, CZ, E;
        do {
            SY = sin(Y);
            CY = cos(Y);
            CZ = cos(BAZ + Y);
            E  = CZ*CZ*2.0-1.0;
            C  = Y;
            X  = E*CY;
            Y  = E+E-1.0;
            Y  = (((SY*SY*4.0-3.0)*Y*CZ*D/6.0+X)*D/4.0-CZ)*SY*D+TU;
        } while (abs(Y-C) > TOLERANCE_1);
        BAZ  = CU*CY*CF - SU*SY;
        C    = fo * hypot(SA, BAZ);
        D    = SU*CY + CU*SY*CF;
        lat2 = atan2(D,C);
        C    = CU*CY - SU*SY*CF;
        X    = atan2(SY*SF, C);
        C    = ((-3.0 * C2A + 4.0) * f + 4.0) * C2A * f / 16.0;
        D    = ((E * CY * C + CZ) * SY * C + Y) * SA;
        long2 = long1+X - (1.0-C)*D*f;
        long2 = castToAngleRange(long2);
        destinationValid = true;
    }

    /**
     * Calculates the meridian arc length between two points in the same meridian
     * in the referenced ellipsoid.
     *
     * @param  latitude1 The latitude of the first  point (in decimal degrees).
     * @param  latitude2 The latitude of the second point (in decimal degrees).
     * @return Returned the meridian arc length between latitude1 and latitude2
     */
    public double getMeridianArcLength(final double latitude1, final double latitude2) {
        return getMeridianArcLengthRadians(checkLatitude(latitude1), checkLatitude(latitude2));
    }

    /**
     * Calculates the meridian arc length between two points in the same meridian
     * in the referenced ellipsoid.
     *
     * @param  P1 The latitude of the first  point (in radians).
     * @param  P2 The latitude of the second point (in radians).
     * @return Returned the meridian arc length between P1 and P2
     */
    private double getMeridianArcLengthRadians(final double P1, final double P2) {
        /*
         * Latitudes P1 and P2 in radians positive North and East.
         * Forward azimuths at both points returned in radians from North.
         *
         * Source: ftp://ftp.ngs.noaa.gov/pub/pcsoft/for_inv.3d/source/inverse.for
         *         subroutine GPNARC
         *         version    200005.26
         *         written by Robert (Sid) Safford
         *
         * Ported from Fortran to Java by Daniele Franzoni.
         */
        double S1 = abs(P1);
        double S2 = abs(P2);
        double DA = (P2-P1);
        // Check for a 90 degree lookup
        if (S1 > TOLERANCE_0 || S2 <= (PI/2 - TOLERANCE_0) || S2 >= (PI/2 + TOLERANCE_0)) {
            final double DB = sin(P2* 2.0) - sin(P1* 2.0);
            final double DC = sin(P2* 4.0) - sin(P1* 4.0);
            final double DD = sin(P2* 6.0) - sin(P1* 6.0);
            final double DE = sin(P2* 8.0) - sin(P1* 8.0);
            final double DF = sin(P2*10.0) - sin(P1*10.0);
            // Compute the S2 part of the series expansion
            S2 = -DB*B/2.0 + DC*C/4.0 - DD*D/6.0 + DE*E/8.0 - DF*F/10.0;
        }
        // Compute the S1 part of the series expansion
        S1 = DA * A;
        // Compute the arc length
        return abs(semiMajorAxis * (1.0-eccentricitySquared) * (S1+S2));
    }

    /**
     * Computes the azimuth and orthodromic distance from the
     * {@linkplain #getStartingGeographicPoint starting point} and the
     * {@linkplain #getDestinationGeographicPoint destination point}.
     *
     * @throws IllegalStateException if the destination point has not been set.
     *
     * @see #getAzimuth
     * @see #getOrthodromicDistance
     */
    private void computeDirection() throws IllegalStateException {
        if (!destinationValid) {
            throw new IllegalStateException(Errors.format(ErrorKeys.DESTINATION_NOT_SET));
        }
        // Protect internal variables from change.
        final double long1 = this.long1;
        final double lat1  = this.lat1;
        final double long2 = this.long2;
        final double lat2  = this.lat2;
        /*
         * Solution of the geodetic inverse problem after T.Vincenty.
         * Modified Rainsford's method with Helmert's elliptical terms.
         * Effective in any azimuth and at any distance short of antipodal.
         *
         * Latitudes and longitudes in radians positive North and East.
         * Forward azimuths at both points returned in radians from North.
         *
         * Programmed for CDC-6600 by LCDR L.Pfeifer NGS ROCKVILLE MD 18FEB75
         * Modified for IBM SYSTEM 360 by John G.Gergen NGS ROCKVILLE MD 7507
         * Ported from Fortran to Java by Daniele Franzoni.
         *
         * Source: ftp://ftp.ngs.noaa.gov/pub/pcsoft/for_inv.3d/source/inverse.for
         *         subroutine GPNHRI
         *         version    200208.09
         *         written by robert (sid) safford
         */
        final double dlon = castToAngleRange(long2 - long1);
        final double ss = abs(dlon);
        if (ss < TOLERANCE_1) {
            distance = getMeridianArcLengthRadians(lat1, lat2);
            azimuth = (lat2 > lat1) ? 0.0 : PI;
            directionValid = true;
            antipodal = false;
            return;
        }
        antipodal = (PI - ss < 2*TOLERANCE_3) && (abs(lat1 + lat2) < 2*TOLERANCE_3);
        /*
         * Computes the limit in longitude (alimit), it is equal
         * to twice  the distance from the equator to the pole,
         * as measured along the equator.
         */
        // tests for antinodal difference
        final double ESQP = eccentricitySquared / (1.0-eccentricitySquared);
        final double alimit = PI * fo;
        if (ss >= alimit &&
            lat1 < TOLERANCE_3 && lat1 > -TOLERANCE_3 &&
            lat2 < TOLERANCE_3 && lat2 > -TOLERANCE_3)
        {
            // Computes an approximate AZ
            final double CONS = (PI - ss) / (PI * f);
            double AZ = asin(CONS);
            double AZ_TEMP, S, AO;
            int iter = 0;
            do {
                if (++iter > 8) {
                    throw new ArithmeticException(getNoConvergenceErrorMessage());
                }
                S = cos(AZ);
                final double C2 = S*S;
                // Compute new AO
                AO = T1 + T2*C2 + T4*C2*C2 + T6*C2*C2*C2;
                final double CS = CONS/AO;
                S = asin(CS);
                AZ_TEMP = AZ;
                AZ = S;
            } while (abs(S - AZ_TEMP) >= TOLERANCE_2);

            final double AZ1 = (dlon < 0.0) ? 2.0*PI - S : S;
            azimuth = castToAngleRange(AZ1);
            S = cos(AZ1);

            // Equatorial - geodesic(S-s) SMS
            final double U2 = ESQP*S*S;
            final double U4 = U2*U2;
            final double U6 = U4*U2;
            final double U8 = U6*U2;
            final double BO =  1.0                 +
                               0.25            *U2 +
                               0.046875        *U4 +
                               0.01953125      *U6 +
                              -0.01068115234375*U8;
            S = sin(AZ1);
            final double SMS = semiMajorAxis*PI*(1.0 - f*abs(S)*AO - BO*fo);
            distance = semiMajorAxis*ss - SMS;
            directionValid = true;
            return;
        }

        // the reduced latitudes
        final double  u1 = atan(fo*sin(lat1) / cos(lat1));
        final double  u2 = atan(fo*sin(lat2) / cos(lat2));
        final double su1 = sin(u1);
        final double cu1 = cos(u1);
        final double su2 = sin(u2);
        final double cu2 = cos(u2);
        double xy, w, q2, q4, q6, r2, r3, sig, ssig, slon, clon, sinalf, ab=dlon;
        int kcount = 0;
        do {
            if (++kcount > 8) {
                throw new ArithmeticException(getNoConvergenceErrorMessage());
            }
            clon = cos(ab);
            slon = sin(ab);
            final double csig = su1*su2 + cu1*cu2*clon;
            ssig = hypot(slon*cu2, su2*cu1 - su1*cu2*clon);
            sig  = atan2(ssig, csig);
            sinalf = cu1*cu2*slon/ssig;
            w = (1.0 - sinalf*sinalf);
            final double t4 = w*w;
            final double t6 = w*t4;

            // the coefficents of type a
            final double ao = f+a01*w+a02*t4+a03*t6;
            final double a2 =   a21*w+a22*t4+a23*t6;
            final double a4 =         a42*t4+a43*t6;
            final double a6 =                a63*t6;

            // the multiple angle functions
            double qo  = 0.0;
            if (w > TOLERANCE_0) {
                qo = -2.0*su1*su2/w;
            }
            q2 = csig + qo;
            q4 = 2.0*q2*q2 - 1.0;
            q6 = q2*(4.0*q2*q2 - 3.0);
            r2 = 2.0*ssig*csig;
            r3 = ssig*(3.0 - 4.0*ssig*ssig);

            // the longitude difference
            final double s = sinalf*(ao*sig + a2*ssig*q2 + a4*r2*q4 + a6*r3*q6);
            double xz = dlon+s;
            xy = abs(xz - ab);
            ab = dlon+s;
        } while (xy >= TOLERANCE_1);

        final double z  = ESQP*w;
        final double bo = 1.0 + z*( 1.0/4.0 + z*(-3.0/  64.0 + z*(  5.0/256.0 - z*(175.0/16384.0))));
        final double b2 =       z*(-1.0/4.0 + z*( 1.0/  16.0 + z*(-15.0/512.0 + z*( 35.0/ 2048.0))));
        final double b4 =                   z*z*(-1.0/ 128.0 + z*(  3.0/512.0 - z*( 35.0/ 8192.0)));
        final double b6 =                                  z*z*z*(-1.0/1536.0 + z*(  5.0/ 6144.0));

        // The distance in ellispoid axis units.
        distance = semiMinorAxis * (bo*sig + b2*ssig*q2 + b4*r2*q4 + b6*r3*q6);
        double az1 = (dlon < 0) ? PI*(3.0/2.0) : PI/2;

        // now compute the az1 & az2 for latitudes not on the equator
        if ((abs(su1) >= TOLERANCE_0) || (abs(su2) >= TOLERANCE_0)) {
            final double tana1 = slon*cu2 / (su2*cu1 - clon*su1*cu2);
            final double sina1 = sinalf/cu1;

            // azimuths from north,longitudes positive east
            az1 = atan2(sina1, sina1/tana1);
        }
        azimuth = castToAngleRange(az1);
        directionValid = true;
        return;
    }

    /**
     * Calculates the geodetic curve between two points in the referenced ellipsoid.
     * A curve in the ellipsoid is a path which points contain the longitude and latitude
     * of the points in the geodetic curve. The geodetic curve is computed from the
     * {@linkplain #getStartingGeographicPoint starting point} to the
     * {@linkplain #getDestinationGeographicPoint destination point}.
     *
     * @param  numberOfPoints The number of vertex in the geodetic curve.
     *         <strong>NOTE:</strong> This argument is only a hint and may be ignored
     *         in future version (if we compute a real curve rather than a list of line
     *         segments).
     * @return The path that represents the geodetic curve from the
     *         {@linkplain #getStartingGeographicPoint starting point} to the
     *         {@linkplain #getDestinationGeographicPoint destination point}.
     *
     * @todo We should check for cases where the path cross the 90°N, 90°S, 90°E or 90°W boundaries.
     */
    public Shape getGeodeticCurve(final int numberOfPoints) {
        checkNumberOfPoints(numberOfPoints);
        if (!directionValid) {
            computeDirection();
        }
        if (!destinationValid) {
            computeDestinationPoint();
        }
        final double         long2 = this.long2;
        final double          lat2 = this.lat2;
        final double      distance = this.distance;
        final double deltaDistance = distance / numberOfPoints;
        final GeneralPath     path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, numberOfPoints+1);
        path.moveTo((float) toDegrees(long1), (float) toDegrees(lat1));
        for (int i=1; i<numberOfPoints; i++) {
            this.distance = i*deltaDistance;
            computeDestinationPoint();
            path.lineTo((float) toDegrees(this.long2), (float) toDegrees(this.lat2));
        }
        this.long2    = long2;
        this.lat2     = lat2;
        this.distance = distance;
        path.lineTo((float) toDegrees(long2), (float) toDegrees(lat2));
        return path;
    }

    /**
     * Calculates the geodetic curve between two points in the referenced ellipsoid.
     * A curve in the ellipsoid is a path which points contain the longitude and latitude
     * of the points in the geodetic curve. The geodetic curve is computed from the
     * {@linkplain #getStartingGeographicPoint starting point} to the
     * {@linkplain #getDestinationGeographicPoint destination point}.
     *
     * @return The path that represents the geodetic curve from the
     *         {@linkplain #getStartingGeographicPoint starting point} to the
     *         {@linkplain #getDestinationGeographicPoint destination point}.
     */
    public Shape getGeodeticCurve() {
        return getGeodeticCurve(10);
    }

    /**
     * Calculates the loxodromic curve between two points in the referenced ellipsoid.
     * The loxodromic curve between two points is a path that links together the two
     * points with a constant azimuth. The azimuth from every points of the loxodromic
     * curve and the second point is constant.
     *
     * @return The path that represents the loxodromic curve from the
     *         {@linkplain #getStartingGeographicPoint starting point} to the
     *         {@linkplain #getDestinationGeographicPoint destination point}.
     */
    private Shape getLoxodromicCurve() {
        if (true) {
            throw new UnsupportedOperationException();
        }
        /*************************************************************************************
        ** THE FOLLOWING IS CHECKED FOR COMPILER ERROR, BUT EXCLUDED FROM THE .class FILE.  **
        ** THIS CODE IS WRONG: LOXODROMIC CURVES ARE STRAIGHT LINES IN MERCATOR PROJECTION, **
        ** NOT IT PLAIN (longitude,latitude) SPACE. FURTHERMORE, THE "OUT OF BOUNDS" CHECK  **
        ** IS UNFINISHED: WHEN THE PATH CROSS THE 180° LONGITUDE, A +360° ADDITION NEED TO  **
        ** BE PERFORMED ON ONE OF THE SOURCE OR TARGET POINT  BEFORE TO COMPUTE THE LINEAR  **
        ** INTERPOLATION (OTHERWISE, THE SLOPE VALUE IS WRONG). FORMULAS FOR COMPUTING MID- **
        ** POINT ON A LOXODROMIC CURVE ARE AVAILABLE THERE:                                 **
        **                                                                                  **
        **              http://mathforum.org/discuss/sci.math/a/t/180912                    **
        **                                                                                  **
        ** LatM = (Lat0+Lat1)/2                                                             **
        **                                                                                  **
        **        (Lon1-Lon0)log(f(LatM)) + Lon0 log(f(Lat1)) - Lon1 log(f(Lat0))           **
        ** LonM = ---------------------------------------------------------------           **
        **                             log(f(Lat1)/f(Lat0))                                 **
        **                                                                                  **
        ** where log(f(x)) == log(sec(x)+tan(x)) is the inverse Gudermannian function.      **
        *************************************************************************************/
        if (!directionValid) {
            computeDirection();
        }
        if (!destinationValid) {
            computeDestinationPoint();
        }
        final double x1 = toDegrees(long1);
        final double y1 = toDegrees( lat1);
        final double x2 = toDegrees(long2);
        final double y2 = toDegrees( lat2);
        /*
         * Check if the azimuth is heading from P1 to P2 (TRUE) or in the opposite direction
         * (FALSE). Horizontal (X) and vertical (Y) components are checked separatly. A null
         * value means "don't know", because the path is perfectly vertical or horizontal or
         * because a coordinate is NaN.  If both components are not null (unknow), then they
         * must be consistent.
         */
        final Boolean xDirect = (x2>x1) ? Boolean.valueOf(azimuth >= 0) :
                                (x2<x1) ? Boolean.valueOf(azimuth <= 0) : null;
        final Boolean yDirect = (y2>y1) ? Boolean.valueOf(azimuth >= -90 && azimuth <= +90) :
                                (y2<y1) ? Boolean.valueOf(azimuth <= -90 || azimuth >= +90) : null;
        assert xDirect==null || yDirect==null || xDirect.equals(yDirect) : this;
        if (!Boolean.FALSE.equals(xDirect) && !Boolean.FALSE.equals(yDirect)) {
            return new Line2D.Double(x1, y1, x2, y2);
        }
        if (Boolean.FALSE.equals(yDirect)) {
            /*
             * Crossing North or South pole is more complicated than what we do for now: If we
             * follow the 0° longitude toward North, then we have to follow the 180° longitude
             * from North to South pole and follow the 0° longitude again toward North up to
             * the destination point.
             */
            throw new UnsupportedOperationException("Crossing pole is not yet implemented");
        }
        /*
         * The azimuth is heading in the opposite direction of the path from P1 to P2. Computes
         * the intersection points at the 90°N / 90°S boundaries, or the 180°E / 180°W boundaries.
         * (xout,yout) is the point where the path goes out (initialized to the corner where the
         * azimuth is heading); (xin,yin) is the point where the path come back in the opposite
         * hemisphere.
         */
        double xout = (x2 >= x1) ? -180 : +180;
        double yout = (y2 >= y1) ?  -90 :  +90;
        double xin  = -xout;
        double yin  = -yout;
        final double dx = x2-x1;
        final double dy = y2-y1;
        if (dx == 0) {
            xin = xout = x1;  // Vertical line.
        } else if (dy == 0) {
            yin = yout = y1;  // Horizontal line.
        } else {
            /*
             * The path is diagonal (neither horizontal or vertical). The following loop
             * is executed exactly twice:  the first pass computes the "out" point,  and
             * the second pass computes the "in" point.  Each pass computes actually two
             * points: the intersection point against the 180°W or 180°E boundary, and
             * the intersection point against the 90°N or 90°S boundary. Usually one of
             * those points will be out of range and the other one is selected.
             */
            boolean in = false;
            do {
                final double meridX, meridY; // The point where the path cross the +/-180° meridian.
                final double zonalX, zonalY; // The point where the path cross the +/- 90° parallel.
                meridX = in ? xin : xout;    meridY = dy/dx * (meridX-x1) + y1;
                zonalY = in ? yin : yout;    zonalX = dx/dy * (zonalY-y1) + x1;
                if (abs(meridY) < abs(zonalX)*0.5) {
                    if (in) {
                        xin = meridX;
                        yin = meridY;
                    } else {
                        xout = meridX;
                        yout = meridY;
                    }
                } else {
                    if (in) {
                        xin = zonalX;
                        yin = zonalY;
                    } else {
                        xout = zonalX;
                        yout = zonalY;
                    }
                }
            } while ((in = !in) == false);
        }
        final GeneralPath path = new GeneralPath(GeneralPath.WIND_EVEN_ODD, 4);
        path.moveTo((float)x1  , (float)y1  );
        path.lineTo((float)xout, (float)yout);
        path.moveTo((float)xin , (float)yin );
        path.lineTo((float)x2  , (float)y2  );
        return path;
    }

    /**
     * Returns a string representation of the current state of this calculator.
     */
    @Override
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
        final Format           nf = cf.getFormat(0);
        if (true) {
            buffer.write(resources.getLabel(VocabularyKeys.SOURCE_POINT));
            buffer.nextColumn();
            buffer.write(format(cf, long1, lat1));
            buffer.nextLine();
        }
        if (destinationValid) {
            buffer.write(resources.getLabel(VocabularyKeys.TARGET_POINT));
            buffer.nextColumn();
            buffer.write(format(cf, long2, lat2));
            buffer.nextLine();
        }
        if (directionValid) {
            buffer.write(resources.getLabel(VocabularyKeys.AZIMUTH));
            buffer.nextColumn();
            buffer.write(nf.format(new Angle(toDegrees(azimuth))));
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
