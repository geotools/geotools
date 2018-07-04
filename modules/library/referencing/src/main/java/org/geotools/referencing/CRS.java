/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.util.HashSet;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.geotools.factory.Factory;
import org.geotools.factory.FactoryNotFoundException;
import org.geotools.factory.FactoryRegistryException;
import org.geotools.factory.GeoTools;
import org.geotools.factory.Hints;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.iso.extent.GeographicBoundingBoxImpl;
import org.geotools.referencing.crs.DefaultEngineeringCRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.cs.DefaultCoordinateSystemAxis;
import org.geotools.referencing.cs.DefaultEllipsoidalCS;
import org.geotools.referencing.factory.AbstractAuthorityFactory;
import org.geotools.referencing.factory.IdentifiedObjectFinder;
import org.geotools.referencing.operation.DefaultMathTransformFactory;
import org.geotools.referencing.operation.projection.LambertAzimuthalEqualArea;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.PolarStereographic;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.IdentityTransform;
import org.geotools.referencing.wkt.Formattable;
import org.geotools.resources.CRSUtilities;
import org.geotools.resources.geometry.XRectangle2D;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.util.GenericName;
import org.geotools.util.UnsupportedImplementationException;
import org.geotools.util.Version;
import org.geotools.util.logging.Logging;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.metadata.citation.Citation;
import org.opengis.metadata.extent.BoundingPolygon;
import org.opengis.metadata.extent.Extent;
import org.opengis.metadata.extent.GeographicBoundingBox;
import org.opengis.metadata.extent.GeographicExtent;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.parameter.ParameterValue;
import org.opengis.referencing.AuthorityFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.IdentifiedObject;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CompoundCRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeneralDerivedCRS;
import org.opengis.referencing.crs.GeographicCRS;
import org.opengis.referencing.crs.ProjectedCRS;
import org.opengis.referencing.crs.SingleCRS;
import org.opengis.referencing.crs.TemporalCRS;
import org.opengis.referencing.crs.VerticalCRS;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CartesianCS;
import org.opengis.referencing.cs.CoordinateSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.cs.EllipsoidalCS;
import org.opengis.referencing.datum.Datum;
import org.opengis.referencing.datum.Ellipsoid;
import org.opengis.referencing.datum.GeodeticDatum;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.MathTransform2D;
import org.opengis.referencing.operation.NoninvertibleTransformException;
import org.opengis.referencing.operation.Projection;
import org.opengis.referencing.operation.TransformException;

/**
 * Simple utility class for making use of the {@linkplain CoordinateReferenceSystem coordinate
 * reference system} and associated {@linkplain org.opengis.referencing.Factory} implementations.
 * This utility class is made up of static final functions. This class is not a factory or a
 * builder. It makes use of the GeoAPI factory interfaces provided by {@link
 * ReferencingFactoryFinder}.
 *
 * <p>The following methods may be added in a future version:
 *
 * <ul>
 *   <li>{@code CoordinateReferenceSystem parseXML(String)}
 * </ul>
 *
 * <p>When using {@link CoordinateReferenceSystem} matching methods of this class ({@link
 * #equalsIgnoreMetadata(Object, Object)},{@link #lookupIdentifier(IdentifiedObject, boolean)},
 * {@link #lookupEpsgCode(CoordinateReferenceSystem, boolean)}, {@link
 * #lookupIdentifier(IdentifiedObject, boolean)}, {@link #lookupIdentifier(Citation,
 * CoordinateReferenceSystem, boolean)}) against objects derived from a database other than the
 * official EPSG one it may be advisable to set a non zero comparison tolerance with {@link
 * Hints#putSystemDefault(java.awt.RenderingHints.Key, Object)} using the {@link
 * Hints#COMPARISON_TOLERANCE} key. A value of 10e-9 has proved to give satisfactory results with
 * definitions commonly found in .prj files accompaining shapefiles and georeferenced images.<br>
 * <b>Warning</b>: the tolerance value is used in all internal comparison, this will also change the
 * way math transforms are setup. Use with care.
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett (Refractions Research)
 * @author Martin Desruisseaux
 * @author Andrea Aime
 * @tutorial
 *     http://docs.codehaus.org/display/GEOTOOLS/Coordinate+Transformation+Services+for+Geotools+2.1
 */
public final class CRS {

    static final Logger LOGGER = Logging.getLogger(CRS.class);

    static volatile AtomicBoolean FORCED_LON_LAT = null;

    /** Enumeration describing axis order for geographic coordinate reference systems. */
    public static enum AxisOrder {
        /**
         * Ordering in which longitude comes before latitude, commonly referred to as x/y ordering.
         */
        EAST_NORTH,

        NORTH_EAST,

        /** Indicates axis ordering is not applicable to the coordinate reference system. */
        INAPPLICABLE;

        /**
         * Ordering in which longitude comes before latitude, commonly referred to as x/y ordering.
         *
         * @deprecated use {@link #EAST_NORTH}
         */
        public static AxisOrder LON_LAT = EAST_NORTH;

        /**
         * Ordering in which latitude comes before longitude, commonly referred to as y/x ordering.
         *
         * @deprecated use {@link #NORTH_EAST}
         */
        public static AxisOrder LAT_LON = NORTH_EAST;
    };

    /** A map with {@link Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER} set to {@link Boolean#TRUE}. */
    private static final Hints FORCE_LONGITUDE_FIRST_AXIS_ORDER =
            new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);

    /**
     * A factory for CRS creation with (<var>latitude</var>, <var>longitude</var>) axis order
     * (unless otherwise specified in system property). Will be created only when first needed.
     */
    private static CRSAuthorityFactory defaultFactory;

    /**
     * A factory for CRS creation with (<var>longitude</var>, <var>latitude</var>) axis order. Will
     * be created only when first needed.
     */
    private static CRSAuthorityFactory xyFactory;

    /** A factory for default (non-lenient) operations. */
    private static volatile CoordinateOperationFactory strictFactory;

    /** A factory for default lenient operations. */
    private static volatile CoordinateOperationFactory lenientFactory;

    /** Registers a listener automatically invoked when the system-wide configuration changed. */
    static {
        GeoTools.addChangeListener(
                new ChangeListener() {
                    public void stateChanged(ChangeEvent e) {
                        synchronized (CRS.class) {
                            defaultFactory = null;
                            xyFactory = null;
                            strictFactory = null;
                            lenientFactory = null;
                        }
                    }
                });
    }

    /** Do not allow instantiation of this class. */
    private CRS() {}

    //////////////////////////////////////////////////////////////
    ////                                                      ////
    ////        FACTORIES, CRS CREATION AND INSPECTION        ////
    ////                                                      ////
    //////////////////////////////////////////////////////////////

    /**
     * Returns the CRS authority factory used by the {@link #decode(String,boolean) decode} methods.
     * This factory is {@linkplain org.geotools.referencing.factory.BufferedAuthorityFactory
     * buffered}, scans over {@linkplain org.geotools.referencing.factory.AllAuthoritiesFactory all
     * factories} and uses additional factories as {@linkplain
     * org.geotools.referencing.factory.FallbackAuthorityFactory fallbacks} if there is more than
     * one {@linkplain ReferencingFactoryFinder#getCRSAuthorityFactories registered factory} for the
     * same authority.
     *
     * <p>This factory can be used as a kind of <cite>system-wide</cite> factory for all
     * authorities. However for more determinist behavior, consider using a more specific factory
     * (as returned by {@link ReferencingFactoryFinder#getCRSAuthorityFactory} when the authority in
     * known.
     *
     * @param longitudeFirst {@code true} if axis order should be forced to
     *     (<var>longitude</var>,<var>latitude</var>). Note that {@code false} means "<cite>use
     *     default</cite>", <strong>not</strong> "<cite>latitude first</cite>".
     * @return The CRS authority factory.
     * @throws FactoryRegistryException if the factory can't be created.
     * @since 2.3
     */
    public static synchronized CRSAuthorityFactory getAuthorityFactory(final boolean longitudeFirst)
            throws FactoryRegistryException {
        CRSAuthorityFactory factory = (longitudeFirst) ? xyFactory : defaultFactory;
        if (factory == null)
            try {
                // what matters is the value of the flag when the factories are created,. do updated
                updateForcedLonLat();
                factory = new DefaultAuthorityFactory(longitudeFirst);
                if (longitudeFirst) {
                    xyFactory = factory;
                } else {
                    defaultFactory = factory;
                }
            } catch (NoSuchElementException exception) {
                // No factory registered in FactoryFinder.
                throw new FactoryNotFoundException(null, exception);
            }
        return factory;
    }

    private static void updateForcedLonLat() {
        boolean forcedLonLat = false;
        try {
            forcedLonLat =
                    Boolean.getBoolean("org.geotools.referencing.forceXY")
                            || Boolean.TRUE.equals(
                                    Hints.getSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER));
        } catch (Exception e) {
            // all right it was a best effort attempt
            LOGGER.log(Level.FINE, "Failed to determine if we are in forced lon/lat mode", e);
        }
        FORCED_LON_LAT = new AtomicBoolean(forcedLonLat);
    }

    private static boolean isForcedLonLat() {
        if (FORCED_LON_LAT == null) {
            updateForcedLonLat();
        }
        return FORCED_LON_LAT.get();
    }

    /**
     * Returns the coordinate operation factory used by {@link
     * #findMathTransform(CoordinateReferenceSystem, CoordinateReferenceSystem) findMathTransform}
     * convenience methods.
     *
     * @param lenient {@code true} if the coordinate operations should be created even when there is
     *     no information available for a datum shift.
     * @since 2.4
     */
    public static CoordinateOperationFactory getCoordinateOperationFactory(final boolean lenient) {
        CoordinateOperationFactory factory = (lenient) ? lenientFactory : strictFactory;
        if (factory == null) {
            synchronized (CRS.class) {
                factory = (lenient) ? lenientFactory : strictFactory;
                if (factory == null) {
                    final Hints hints = GeoTools.getDefaultHints();
                    if (lenient) {
                        hints.put(Hints.LENIENT_DATUM_SHIFT, Boolean.TRUE);
                    }
                    factory = ReferencingFactoryFinder.getCoordinateOperationFactory(hints);
                    if (lenient) {
                        lenientFactory = factory;
                    } else {
                        strictFactory = factory;
                    }
                }
            }
        }
        return factory;
    }

    /**
     * Returns the version number of the specified authority database, or {@code null} if not
     * available.
     *
     * @param authority The authority name (typically {@code "EPSG"}).
     * @return The version number of the authority database, or {@code null} if unknown.
     * @throws FactoryRegistryException if no {@link CRSAuthorityFactory} implementation was found
     *     for the specified authority.
     * @since 2.4
     */
    public static Version getVersion(final String authority) throws FactoryRegistryException {
        Object candidate = ReferencingFactoryFinder.getCRSAuthorityFactory(authority, null);
        final Set<Factory> guard = new HashSet<Factory>();
        while (candidate instanceof Factory) {
            final Factory factory = (Factory) candidate;
            if (!guard.add(factory)) {
                break; // Safety against never-ending recursivity.
            }
            final Map hints = factory.getImplementationHints();
            final Object version = hints.get(Hints.VERSION);
            if (version instanceof Version) {
                return (Version) version;
            }
            candidate = hints.get(Hints.CRS_AUTHORITY_FACTORY);
        }
        return null;
    }

    /**
     * Get the list of the codes that are supported by the given authority. For example {@code
     * getSupportedCodes("EPSG")} may returns {@code "EPSG:2000"}, {@code "EPSG:2001"}, {@code
     * "EPSG:2002"}, <cite>etc</cite>. It may also returns {@code "2000"}, {@code "2001"}, {@code
     * "2002"}, <cite>etc.</cite> without the {@code "EPSG:"} prefix. Whatever the authority name is
     * prefixed or not is factory implementation dependent.
     *
     * <p>If there is more than one factory for the given authority, then this method merges the
     * code set of all of them. If a factory fails to provide a set of supported code, then this
     * particular factory is ignored. Please be aware of the following potential issues:
     *
     * <p>
     *
     * <ul>
     *   <li>If there is more than one EPSG databases (for example an {@linkplain
     *       org.geotools.referencing.factory.epsg.AccessDataSource Access} and a {@linkplain
     *       org.geotools.referencing.factory.epsg.PostgreDataSource PostgreSQL} ones), then this
     *       method will connect to all of them even if their content are identical.
     *   <li>If two factories format their codes differently (e.g. {@code "4326"} and {@code
     *       "EPSG:4326"}), then the returned set will contain a lot of synonymous codes.
     *   <li>For any code <var>c</var> in the returned set, there is no warranty that <code>
     *       {@linkplain #decode decode}(c)</code> will use the same authority factory than the one
     *       that formatted <var>c</var>.
     *   <li>This method doesn't report connection problems since it doesn't throw any exception.
     *       {@link FactoryException}s are logged as warnings and otherwise ignored.
     * </ul>
     *
     * <p>If a more determinist behavior is wanted, consider the code below instead. The following
     * code exploit only one factory, the "preferred" one.
     *
     * <blockquote>
     *
     * <code>
     * {@linkplain CRSAuthorityFactory} factory = FactoryFinder.{@linkplain
     * ReferencingFactoryFinder#getCRSAuthorityFactory getCRSAuthorityFactory}(authority, null);<br>
     * Set&lt;String&gt; codes = factory.{@linkplain CRSAuthorityFactory#getAuthorityCodes
     * getAuthorityCodes}(CoordinateReferenceSystem.class);<br>
     * String code = <cite>...choose a code here...</cite><br>
     * {@linkplain CoordinateReferenceSystem} crs = factory.createCoordinateReferenceSystem(code);
     * </code>
     *
     * </blockquote>
     *
     * @param authority The authority name (for example {@code "EPSG"}).
     * @return The set of supported codes. May be empty, but never null.
     */
    public static Set<String> getSupportedCodes(final String authority) {
        return DefaultAuthorityFactory.getSupportedCodes(authority);
    }

    /**
     * Returns the set of the authority identifiers supported by registered authority factories.
     * This method search only for {@linkplain CRSAuthorityFactory CRS authority factories}.
     *
     * @param returnAliases If {@code true}, the set will contain all identifiers for each
     *     authority. If {@code false}, only the first one
     * @return The set of supported authorities. May be empty, but never null.
     * @since 2.3.1
     */
    public static Set<String> getSupportedAuthorities(final boolean returnAliases) {
        return DefaultAuthorityFactory.getSupportedAuthorities(returnAliases);
    }

    /**
     * Return a Coordinate Reference System for the specified code. Note that the code needs to
     * mention the authority. Examples:
     *
     * <blockquote>
     *
     * <pre>
     * EPSG:1234
     * AUTO:42001, ..., ..., ...
     * </pre>
     *
     * </blockquote>
     *
     * If there is more than one factory implementation for the same authority, then all additional
     * factories are {@linkplain org.geotools.referencing.factory.FallbackAuthorityFactory
     * fallbacks} to be used only when the first acceptable factory failed to create the requested
     * CRS object.
     *
     * <p>CRS objects created by previous calls to this method are {@linkplain
     * org.geotools.referencing.factory.BufferedAuthorityFactory cached in a buffer} using
     * {@linkplain java.lang.ref.WeakReference weak references}. Subsequent calls to this method
     * with the same authority code should be fast, unless the CRS object has been garbage
     * collected.
     *
     * @param code The Coordinate Reference System authority code.
     * @return The Coordinate Reference System for the provided code.
     * @throws NoSuchAuthorityCodeException If the code could not be understood.
     * @throws FactoryException if the CRS creation failed for an other reason.
     * @see #getSupportedCodes
     * @see org.geotools.referencing.factory.AllAuthoritiesFactory#createCoordinateReferenceSystem
     */
    public static CoordinateReferenceSystem decode(final String code)
            throws NoSuchAuthorityCodeException, FactoryException {
        /*
         * Do not use Boolean.getBoolean(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER).
         * The boolean argument should be 'false', which means "use system default"
         * (not "latitude first").
         */
        return decode(code, false);
    }

    /**
     * Return a Coordinate Reference System for the specified code, maybe forcing the axis order to
     * (<var>longitude</var>, <var>latitude</var>). The {@code code} argument value is parsed as in
     * "<code>{@linkplain #decode(String) decode}(code)</code>". The {@code longitudeFirst} argument
     * value controls the hints to be given to the {@linkplain ReferencingFactoryFinder factory
     * finder} as in the following pseudo-code:
     *
     * <p>
     *
     * <blockquote>
     *
     * <pre>
     * if (longitudeFirst) {
     *     hints.put({@linkplain Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER}, {@linkplain Boolean#TRUE});
     * } else {
     *     // Do not set the FORCE_LONGITUDE_FIRST_AXIS_ORDER hint to FALSE.
     *     // Left it unset, which means "use system default".
     * }
     * </pre>
     *
     * </blockquote>
     *
     * The following table compare this method {@code longitudeFirst} argument with the hint
     * meaning:
     *
     * <p>
     *
     * <table border='1'>
     * <tr>
     *   <th>This method argument</th>
     *   <th>{@linkplain Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER Hint} value</th>
     *   <th>Meaning</th>
     * </tr>
     * <tr>
     *   <td>{@code true}</td>
     *   <td>{@link Boolean#TRUE TRUE}</td>
     *   <td>All coordinate reference systems are forced to
     *       (<var>longitude</var>, <var>latitude</var>) axis order.</td>
     * </tr>
     * <tr>
     *   <td>{@code false}</td>
     *   <td>{@code null}</td>
     *   <td>Coordinate reference systems may or may not be forced to
     *       (<var>longitude</var>, <var>latitude</var>) axis order. The behavior depends on user
     *       setting, for example the value of the <code>{@value
     *       org.geotools.referencing.factory.epsg.LongitudeFirstFactory#SYSTEM_DEFAULT_KEY}</code>
     *       system property.</td>
     * </tr>
     * <tr>
     *   <td></td>
     *   <td>{@link Boolean#FALSE FALSE}</td>
     *   <td>Forcing (<var>longitude</var>, <var>latitude</var>) axis order is not allowed,
     *       no matter the value of the <code>{@value
     *       org.geotools.referencing.factory.epsg.LongitudeFirstFactory#SYSTEM_DEFAULT_KEY}</code>
     *       system property.</td>
     * </tr>
     * </table>
     *
     * @param code The Coordinate Reference System authority code.
     * @param longitudeFirst {@code true} if axis order should be forced to (<var>longitude</var>,
     *     <var>latitude</var>). Note that {@code false} means "<cite>use default</cite>",
     *     <strong>not</strong> "<cite>latitude first</cite>".
     * @return The Coordinate Reference System for the provided code.
     * @throws NoSuchAuthorityCodeException If the code could not be understood.
     * @throws FactoryException if the CRS creation failed for an other reason.
     * @see #getSupportedCodes
     * @see Hints#FORCE_LONGITUDE_FIRST_AXIS_ORDER
     * @since 2.3
     */
    public static CoordinateReferenceSystem decode(String code, final boolean longitudeFirst)
            throws NoSuchAuthorityCodeException, FactoryException {
        // @deprecated: 'toUpperCase()' is required only for epsg-wkt.
        // Remove after we deleted the epsg-wkt module.
        code = code.trim().toUpperCase();
        return getAuthorityFactory(longitudeFirst).createCoordinateReferenceSystem(code);
    }

    /**
     * Parses a <A
     * HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite></A> (WKT) into a CRS object. This convenience method is a shorthand for the
     * following:
     *
     * <blockquote>
     *
     * <code>
     * FactoryFinder.{@linkplain ReferencingFactoryFinder#getCRSFactory getCRSFactory}(null).{@linkplain
     * org.opengis.referencing.crs.CRSFactory#createFromWKT createFromWKT}(wkt);
     * </code>
     *
     * </blockquote>
     */
    public static CoordinateReferenceSystem parseWKT(final String wkt) throws FactoryException {
        return ReferencingFactoryFinder.getCRSFactory(null).createFromWKT(wkt);
    }

    /**
     * Returns the domain of validity for the specified coordinate reference system, or {@code null}
     * if unknown.
     *
     * <p>This method fetchs the {@linkplain CoordinateReferenceSystem#getDomainOfValidity domain of
     * validity} associated with the given CRS. Only {@linkplain GeographicExtent geographic
     * extents} of kind {@linkplain BoundingPolygon bounding polygon} are taken in account. If none
     * are found, then the {@linkplain #getGeographicBoundingBox geographic bounding boxes} are used
     * as a fallback.
     *
     * <p>The returned envelope is expressed in terms of the specified CRS.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @return The envelope in terms of the specified CRS, or {@code null} if none.
     * @see #getGeographicBoundingBox
     * @see org.geotools.geometry.GeneralEnvelope#normalize
     * @since 2.2
     */
    public static Envelope getEnvelope(final CoordinateReferenceSystem crs) {
        Envelope envelope = null;
        GeneralEnvelope merged = null;
        if (crs != null) {
            final Extent domainOfValidity = crs.getDomainOfValidity();
            if (domainOfValidity != null) {
                for (final GeographicExtent extent : domainOfValidity.getGeographicElements()) {
                    if (Boolean.FALSE.equals(extent.getInclusion())) {
                        continue;
                    }
                    if (extent instanceof BoundingPolygon) {
                        for (final Geometry geometry : ((BoundingPolygon) extent).getPolygons()) {
                            final Envelope candidate = geometry.getEnvelope();
                            if (candidate != null) {
                                final CoordinateReferenceSystem sourceCRS =
                                        candidate.getCoordinateReferenceSystem();
                                if (sourceCRS == null || equalsIgnoreMetadata(sourceCRS, crs)) {
                                    if (envelope == null) {
                                        envelope = candidate;
                                    } else {
                                        if (merged == null) {
                                            envelope = merged = new GeneralEnvelope(envelope);
                                        }
                                        merged.add(envelope);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        /*
         * If no envelope was found, uses the geographic bounding box as a fallback. We will
         * need to transform it from WGS84 to the supplied CRS. This step was not required in
         * the previous block because the later selected only envelopes in the right CRS.
         */
        if (envelope == null) {
            final GeographicBoundingBox bounds = getGeographicBoundingBox(crs);
            if (bounds != null && !Boolean.FALSE.equals(bounds.getInclusion())) {
                envelope =
                        merged =
                                new GeneralEnvelope(
                                        new double[] {
                                            bounds.getWestBoundLongitude(),
                                            bounds.getSouthBoundLatitude()
                                        },
                                        new double[] {
                                            bounds.getEastBoundLongitude(),
                                            bounds.getNorthBoundLatitude()
                                        });
                /*
                 * We do not assign WGS84 inconditionnaly to the geographic bounding box, because
                 * it is not defined to be on a particular datum; it is only approximative bounds.
                 * We try to get the GeographicCRS from the user-supplied CRS and fallback on WGS
                 * 84 only if we found none.
                 */
                final SingleCRS targetCRS = getHorizontalCRS(crs);
                final GeographicCRS sourceCRS = CRSUtilities.getStandardGeographicCRS2D(targetCRS);
                merged.setCoordinateReferenceSystem(sourceCRS);
                try {
                    envelope = transform(envelope, targetCRS);
                } catch (TransformException exception) {
                    /*
                     * The envelope is probably outside the range of validity for this CRS.
                     * It should not occurs, since the envelope is supposed to describe the
                     * CRS area of validity. Logs a warning and returns null, since it is a
                     * legal return value according this method contract.
                     */
                    envelope = null;
                    unexpectedException("getEnvelope", exception);
                }
                /*
                 * If transform(...) created a new envelope, its CRS is already targetCRS so it
                 * doesn't matter if 'merged' is not anymore the right instance. If 'transform'
                 * returned the envelope unchanged, the 'merged' reference still valid and we
                 * want to ensure that it have the user-supplied CRS.
                 */
                merged.setCoordinateReferenceSystem(targetCRS);
            }
        }
        return envelope;
    }

    /**
     * Returns the valid geographic area for the specified coordinate reference system, or {@code
     * null} if unknown.
     *
     * <p>This method fetchs the {@linkplain CoordinateReferenceSystem#getDomainOfValidity domain of
     * validity} associated with the given CRS. Only {@linkplain GeographicExtent geographic
     * extents} of kind {@linkplain GeographicBoundingBox geographic bounding box} are taken in
     * account.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @return The geographic area, or {@code null} if none.
     * @see #getEnvelope
     * @since 2.3
     */
    public static GeographicBoundingBox getGeographicBoundingBox(
            final CoordinateReferenceSystem crs) {
        GeographicBoundingBox bounds = null;
        GeographicBoundingBoxImpl merged = null;
        if (crs != null) {
            final Extent domainOfValidity = crs.getDomainOfValidity();
            if (domainOfValidity != null) {
                for (final GeographicExtent extent : domainOfValidity.getGeographicElements()) {
                    if (extent instanceof GeographicBoundingBox) {
                        final GeographicBoundingBox candidate = (GeographicBoundingBox) extent;
                        if (bounds == null) {
                            bounds = candidate;
                        } else {
                            if (merged == null) {
                                bounds = merged = new GeographicBoundingBoxImpl(bounds);
                            }
                            merged.add(candidate);
                        }
                    }
                }
            }
        }
        return bounds;
    }

    /**
     * Returns the first horizontal coordinate reference system found in the given CRS, or {@code
     * null} if there is none. A horizontal CRS is usually a two-dimensional {@linkplain
     * GeographicCRS geographic} or {@linkplain ProjectedCRS projected} CRS.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @return The horizontal CRS, or {@code null} if none.
     * @since 2.4
     */
    public static SingleCRS getHorizontalCRS(final CoordinateReferenceSystem crs) {
        if (crs instanceof SingleCRS) {
            final CoordinateSystem cs = crs.getCoordinateSystem();
            final int dimension = cs.getDimension();
            if (dimension == 2) {
                /*
                 * For two-dimensional CRS, returns the CRS directly if it is either a
                 * GeographicCRS, or any kind of derived CRS having a GeographicCRS as
                 * its base.
                 */
                CoordinateReferenceSystem base = crs;
                while (base instanceof GeneralDerivedCRS) {
                    base = ((GeneralDerivedCRS) base).getBaseCRS();
                }
                // No need to test for ProjectedCRS, since the code above unwrap it.
                if (base instanceof GeographicCRS) {
                    return (SingleCRS) crs; // Really returns 'crs', not 'base'.
                }
                // cartesian are certainly valid horizontal CRS
                if (base.getCoordinateSystem() instanceof CartesianCS) {
                    return (SingleCRS) crs; // Really returns 'crs', not 'base'.
                }
            } else if (dimension >= 3 && crs instanceof GeographicCRS) {
                /*
                 * For three-dimensional Geographic CRS, extracts the axis having a direction
                 * like "North", "North-East", "East", etc. If we find exactly two of them,
                 * we can build a new GeographicCRS using them.
                 */
                CoordinateSystemAxis axis0 = null, axis1 = null;
                int count = 0;
                for (int i = 0; i < dimension; i++) {
                    final CoordinateSystemAxis axis = cs.getAxis(i);
                    search:
                    if (DefaultCoordinateSystemAxis.isCompassDirection(axis.getDirection())) {
                        switch (count++) {
                            case 0:
                                axis0 = axis;
                                break;
                            case 1:
                                axis1 = axis;
                                break;
                            default:
                                break search;
                        }
                    }
                }
                if (count == 2) {
                    final GeodeticDatum datum = ((GeographicCRS) crs).getDatum();
                    Map<String, ?> properties = CRSUtilities.changeDimensionInName(cs, "3D", "2D");
                    EllipsoidalCS horizontalCS;
                    try {
                        horizontalCS =
                                ReferencingFactoryFinder.getCSFactory(null)
                                        .createEllipsoidalCS(properties, axis0, axis1);
                    } catch (FactoryException e) {
                        Logging.recoverableException(CRS.class, "getHorizontalCRS", e);
                        horizontalCS = new DefaultEllipsoidalCS(properties, axis0, axis1);
                    }
                    properties = CRSUtilities.changeDimensionInName(crs, "3D", "2D");
                    GeographicCRS horizontalCRS;
                    try {
                        horizontalCRS =
                                ReferencingFactoryFinder.getCRSFactory(null)
                                        .createGeographicCRS(properties, datum, horizontalCS);
                    } catch (FactoryException e) {
                        Logging.recoverableException(CRS.class, "getHorizontalCRS", e);
                        horizontalCRS = new DefaultGeographicCRS(properties, datum, horizontalCS);
                    }
                    return horizontalCRS;
                }
            }
        }
        if (crs instanceof CompoundCRS) {
            final CompoundCRS cp = (CompoundCRS) crs;
            for (final CoordinateReferenceSystem c : cp.getCoordinateReferenceSystems()) {
                final SingleCRS candidate = getHorizontalCRS(c);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        return null;
    }

    /**
     * Returns the first projected coordinate reference system found in a the given CRS, or {@code
     * null} if there is none.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @return The projected CRS, or {@code null} if none.
     * @since 2.4
     */
    public static ProjectedCRS getProjectedCRS(final CoordinateReferenceSystem crs) {
        if (crs instanceof ProjectedCRS) {
            return (ProjectedCRS) crs;
        }
        if (crs instanceof CompoundCRS) {
            final CompoundCRS cp = (CompoundCRS) crs;
            for (final CoordinateReferenceSystem c : cp.getCoordinateReferenceSystems()) {
                final ProjectedCRS candidate = getProjectedCRS(c);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        return null;
    }

    /**
     * Returns the {@link MapProjection} driving the specified crs, or {@code null} if none could be
     * found.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @return The {@link MapProjection}, or {@code null} if none.
     */
    public static MapProjection getMapProjection(final CoordinateReferenceSystem crs) {
        ProjectedCRS projectedCRS = CRS.getProjectedCRS(crs);
        if (projectedCRS == null) return null;

        Projection conversion = projectedCRS.getConversionFromBase();
        MathTransform mt = conversion.getMathTransform();
        return unrollProjection(mt);
    }

    private static MapProjection unrollProjection(MathTransform mt) {
        if (mt instanceof MapProjection) {
            return (MapProjection) mt;
        } else if (mt instanceof ConcatenatedTransform) {
            ConcatenatedTransform ct = (ConcatenatedTransform) mt;
            MapProjection result = unrollProjection(ct.transform1);
            if (result == null) {
                result = unrollProjection(ct.transform2);
            }
            return result;
        } else {
            return null;
        }
    }

    /**
     * Returns the first vertical coordinate reference system found in a the given CRS, or {@code
     * null} if there is none.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @return The vertical CRS, or {@code null} if none.
     * @since 2.4
     */
    public static VerticalCRS getVerticalCRS(final CoordinateReferenceSystem crs) {
        if (crs instanceof VerticalCRS) {
            return (VerticalCRS) crs;
        }
        if (crs instanceof CompoundCRS) {
            final CompoundCRS cp = (CompoundCRS) crs;
            for (final CoordinateReferenceSystem c : cp.getCoordinateReferenceSystems()) {
                final VerticalCRS candidate = getVerticalCRS(c);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        return null;
    }

    /**
     * Returns the first temporal coordinate reference system found in the given CRS, or {@code
     * null} if there is none.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @return The temporal CRS, or {@code null} if none.
     * @since 2.4
     */
    public static TemporalCRS getTemporalCRS(final CoordinateReferenceSystem crs) {
        if (crs instanceof TemporalCRS) {
            return (TemporalCRS) crs;
        }
        if (crs instanceof CompoundCRS) {
            final CompoundCRS cp = (CompoundCRS) crs;
            for (final CoordinateReferenceSystem c : cp.getCoordinateReferenceSystems()) {
                final TemporalCRS candidate = getTemporalCRS(c);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        return null;
    }

    /**
     * Returns the first ellipsoid found in a coordinate reference system, or {@code null} if there
     * is none.
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @return The ellipsoid, or {@code null} if none.
     * @since 2.4
     */
    public static Ellipsoid getEllipsoid(final CoordinateReferenceSystem crs) {
        final Datum datum = CRSUtilities.getDatum(crs);
        if (datum instanceof GeodeticDatum) {
            return ((GeodeticDatum) datum).getEllipsoid();
        }
        if (crs instanceof CompoundCRS) {
            final CompoundCRS cp = (CompoundCRS) crs;
            for (final CoordinateReferenceSystem c : cp.getCoordinateReferenceSystems()) {
                final Ellipsoid candidate = getEllipsoid(c);
                if (candidate != null) {
                    return candidate;
                }
            }
        }
        return null;
    }

    /**
     * Compares the specified objects for equality. If both objects are Geotools implementations of
     * class {@link AbstractIdentifiedObject}, then this method will ignore the metadata during the
     * comparaison.
     *
     * @param object1 The first object to compare (may be null).
     * @param object2 The second object to compare (may be null).
     * @return {@code true} if both objects are equals.
     * @since 2.2
     */
    public static boolean equalsIgnoreMetadata(final Object object1, final Object object2) {
        if (object1 == object2) {
            return true;
        }
        if (object1 instanceof AbstractIdentifiedObject
                && object2 instanceof AbstractIdentifiedObject) {
            return ((AbstractIdentifiedObject) object1)
                    .equals(((AbstractIdentifiedObject) object2), false);
        }
        return object1 != null && object1.equals(object2);
    }

    /**
     * Checks if a transformation is required to go from source to target. This method returns true
     * if:
     *
     * <ul>
     *   <li>source or target are null
     *   <li>source or target are a wildcard CRS, see {@link DefaultEngineeringCRS#isWildcard()}
     *   <li>source and target test positively to {@link #equalsIgnoreMetadata(Object, Object)}
     *   <li>the transformation between source and target is nevertheless and identity, e.g., same
     *       projected CRS based on a geographic one that has straight and flipped axis
     * </ul>
     *
     * @param source The source {@link CoordinateReferenceSystem}
     * @param target the target {@link CoordinateReferenceSystem}
     * @return
     */
    public static boolean isTransformationRequired(
            CoordinateReferenceSystem source, CoordinateReferenceSystem target)
            throws FactoryException {
        // null cases, no transformation possible
        if (source == null || target == null) {
            return false;
        }

        // wildcard cases
        if (source instanceof DefaultEngineeringCRS
                && ((DefaultEngineeringCRS) source).isWildcard()) {
            return false;
        }
        if (target instanceof DefaultEngineeringCRS
                && ((DefaultEngineeringCRS) target).isWildcard()) {
            return false;
        }

        // checking equality
        if (equalsIgnoreMetadata(source, target)) {
            return false;
        }

        // last attempt, compute the transform and check if identity
        MathTransform mathTransform = findMathTransform(source, target);
        return !mathTransform.isIdentity();
    }

    /**
     * Returns the <cite>Spatial Reference System</cite> identifier, or {@code null} if none. OGC
     * Web Services have the concept of a Spatial Reference System identifier used to communicate
     * CRS information between systems.
     *
     * <p>Spatial Reference System (ie SRS) values:
     *
     * <ul>
     *   <li>{@code EPSG:4326} - this is the usual format understood to mean <cite>forceXY</cite>
     *       order prior to WMS 1.3.0. Note that the axis order is <em>not necessarly</em>
     *       (<var>longitude</var>, <var>latitude</var>), but this is the common behavior we observe
     *       in practice.
     *   <li>{@code AUTO:43200} -
     *   <li>{@code CRS:84} - similar to {@link DefaultGeographicCRS#WGS84} (formally defined by
     *       CRSAuthorityFactory)
     *   <li>{@code ogc:uri:.....} - understood to match the EPSG database axis order.
     *   <li>Well Known Text (WKT)
     * </ul>
     *
     * @param crs The coordinate reference system, or {@code null}.
     * @return SRS represented as a string for communication between systems, or {@code null}.
     * @since 2.5
     */
    public static String toSRS(final CoordinateReferenceSystem crs) {
        if (crs == null) {
            return null;
        }
        if (isForcedLonLat() && CRS.getAxisOrder(crs, false) == AxisOrder.NORTH_EAST) {
            try {
                // not usual axis order, check if we can have a EPSG code
                Integer code = CRS.lookupEpsgCode(crs, false);
                if (code != null) {
                    return "urn:ogc:def:crs:EPSG::" + code;
                }
            } catch (Exception e) {
                // all right it was a best effort attempt
                LOGGER.log(Level.FINE, "Failed to determine EPSG code", e);
            }
        }
        // special case DefaultGeographic.WGS84 to prevent SRS="WGS84(DD)"
        if (crs == DefaultGeographicCRS.WGS84) {
            // if( forcedLonLat ) return "EPSG:4326"; <-- this is a bad idea for interoperability
            // WMS 1.3.0
            return "CRS:84"; // WMS Authority definition DefaultGeographicCRS.WGS84
        }
        Set<ReferenceIdentifier> identifiers = crs.getIdentifiers();
        if (identifiers.isEmpty()) {
            // we got nothing to work with ... unfortunately this often does not work
            final ReferenceIdentifier name = crs.getName();
            if (name != null) {
                return name.toString();
            }
            return null;
        } else {
            // check for an identifier to use as an srsName
            for (ReferenceIdentifier identifier : crs.getIdentifiers()) {
                String srs = identifier.toString();
                if (srs.contains("EPSG:") || srs.contains("CRS:")) {
                    return srs; // handles prj files that supply EPSG code
                }
            }
            // fallback unfortunately this often does not work
            ReferenceIdentifier name = crs.getName();
            if (name != null
                    && (name.toString().contains("EPSG:") || name.toString().contains("CRS:"))) {
                return name.toString();
            }
            // nothing was obviously an identifier .. so we grab the first
            return identifiers.iterator().next().toString();
        }
    }
    /**
     * Returns the <cite>Spatial Reference System</cite> identifier, or {@code null} if none.
     *
     * <p>Some older web services are unable to deal with the full ogc:uri syntax, set simple to
     * true for force a very simple representation that is just based on the code portion.
     *
     * @param crs
     * @param codeOnly Set to true to force generation of a simple srsName using only the code
     *     portion
     * @return srsName
     */
    public static String toSRS(final CoordinateReferenceSystem crs, boolean codeOnly) {
        if (crs == null) return null;
        String srsName = toSRS(crs);
        if (codeOnly && srsName != null) {
            // Some Server implementations using older versions of this
            // library barf on a fully qualified CRS name with messages
            // like : "couldnt decode SRS - EPSG:EPSG:4326. currently
            // only supporting EPSG #"; looks like they only needs the
            // SRID. adjust
            final int index = srsName.lastIndexOf(':');
            if (index > 0) {
                // LOGGER.info("Reducing CRS name [" + srsName+ "] to its SRID");
                srsName = srsName.substring(index + 1).trim();
            }
            return srsName;
        } else {
            return srsName;
        }
    }
    /**
     * Looks up an identifier for the specified object. This method searchs in registered factories
     * for an object {@linkplain #equalsIgnoreMetadata equals, ignoring metadata}, to the specified
     * object. If such object is found, then its identifier is returned. Otherwise this method
     * returns {@code null}.
     *
     * <p>This convenience method delegates its work to {@link IdentifiedObjectFinder}. Consider
     * using the later if more control are wanted, for example if the search shall be performed only
     * on some {@linkplain AuthorityFactory authority factories} instead of all registered onez, or
     * if the full {@linkplain IdentifiedObject identified object} is wanted instead of only its
     * identifier.
     *
     * @param object The object (usually a {@linkplain CoordinateReferenceSystem coordinate
     *     reference system}) looked up.
     * @param fullScan If {@code true}, an exhaustive full scan against all registered objects will
     *     be performed (may be slow). Otherwise only a fast lookup based on embedded identifiers
     *     and names will be performed.
     * @return The identifier, or {@code null} if not found.
     * @throws FactoryException if an unexpected failure occured during the search.
     * @see AbstractAuthorityFactory#getIdentifiedObjectFinder
     * @see IdentifiedObjectFinder#find
     * @since 2.4
     */
    public static String lookupIdentifier(final IdentifiedObject object, final boolean fullScan)
            throws FactoryException {
        /*
         * We perform the search using the 'xyFactory' because our implementation of
         * IdentifiedObjectFinder should be able to inspect both the (x,y) and (y,x)
         * axis order using this factory.
         */
        final AbstractAuthorityFactory xyFactory =
                (AbstractAuthorityFactory) getAuthorityFactory(true);
        final IdentifiedObjectFinder finder =
                xyFactory.getIdentifiedObjectFinder(object.getClass());
        finder.setFullScanAllowed(fullScan);
        return finder.findIdentifier(object);
    }

    /**
     * Looks up an identifier of the specified authority for the given {@linkplain
     * CoordinateReferenceSystem coordinate reference system}). This method is similar to <code>
     * {@linkplain #lookupIdentifier(IdentifiedObject, boolean) lookupIdentifier}(object,
     * fullScan)</code> except that the search is performed only among the factories of the given
     * authority.
     *
     * <p>If the CRS does not have an {@linkplain ReferenceIdentifier identifier} which corresponds
     * to the {@linkplain Citations#EPSG EPSG} authority, then:
     *
     * <ul>
     *   <li>if {@code fullScan} is {@code true}, then this method scans the factories in search for
     *       an object {@linkplain #equalsIgnoreMetadata equals, ignoring metadata}, to the given
     *       object. If one is found, its identifier is returned.
     *   <li>Otherwise (if {@code fullScan} is {@code false} or if no identifier was found in the
     *       previous step), this method returns {@code null}.
     * </ul>
     *
     * @param authority The authority for the code to search.
     * @param crs The coordinate reference system instance, or {@code null}.
     * @return The CRS identifier, or {@code null} if none was found.
     * @throws FactoryException if an error occured while searching for the identifier.
     * @since 2.5
     */
    public static String lookupIdentifier(
            final Citation authority, final CoordinateReferenceSystem crs, final boolean fullScan)
            throws FactoryException {
        ReferenceIdentifier id = AbstractIdentifiedObject.getIdentifier(crs, authority);
        if (id != null) {
            return id.getCode();
        }
        for (final CRSAuthorityFactory factory :
                ReferencingFactoryFinder.getCRSAuthorityFactories(
                        FORCE_LONGITUDE_FIRST_AXIS_ORDER)) {
            if (!Citations.identifierMatches(factory.getAuthority(), authority)) {
                continue;
            }
            if (factory == null || !(factory instanceof AbstractAuthorityFactory)) {
                continue;
            }
            final AbstractAuthorityFactory f = (AbstractAuthorityFactory) factory;
            final IdentifiedObjectFinder finder = f.getIdentifiedObjectFinder(crs.getClass());
            finder.setFullScanAllowed(fullScan);
            final String code = finder.findIdentifier(crs);
            if (code != null) {
                return code;
            }
        }
        return null;
    }

    /**
     * Looks up an EPSG code for the given {@linkplain CoordinateReferenceSystem coordinate
     * reference system}). This is a convenience method for <code>{@linkplain
     * #lookupIdentifier(Citations, IdentifiedObject, boolean) lookupIdentifier}({@linkplain
     * Citations#EPSG}, crs, fullScan)</code> except that code is parsed as an integer.
     *
     * @param crs The coordinate reference system instance, or {@code null}.
     * @return The CRS identifier, or {@code null} if none was found.
     * @throws FactoryException if an error occured while searching for the identifier.
     * @since 2.5
     */
    public static Integer lookupEpsgCode(
            final CoordinateReferenceSystem crs, final boolean fullScan) throws FactoryException {
        final String identifier = lookupIdentifier(Citations.EPSG, crs, fullScan);
        if (identifier != null) {
            final int split = identifier.lastIndexOf(GenericName.DEFAULT_SEPARATOR);
            final String code = identifier.substring(split + 1);
            // The above code works even if the separator was not found, since in such case
            // split == -1, which implies a call to substring(0) which returns 'identifier'.
            try {
                return Integer.parseInt(code);
            } catch (NumberFormatException e) {
                throw new FactoryException(
                        Errors.format(ErrorKeys.ILLEGAL_IDENTIFIER_$1, identifier), e);
            }
        }
        return null;
    }

    /////////////////////////////////////////////////
    ////                                         ////
    ////          COORDINATE OPERATIONS          ////
    ////                                         ////
    /////////////////////////////////////////////////

    /**
     * Grab a transform between two Coordinate Reference Systems. This convenience method is a
     * shorthand for the following:
     *
     * <blockquote>
     *
     * <code>FactoryFinder.{@linkplain ReferencingFactoryFinder#getCoordinateOperationFactory
     * getCoordinateOperationFactory}(null).{@linkplain CoordinateOperationFactory#createOperation
     * createOperation}(sourceCRS, targetCRS).{@linkplain CoordinateOperation#getMathTransform
     * getMathTransform}();</code>
     *
     * </blockquote>
     *
     * Note that some metadata like {@linkplain CoordinateOperation#getPositionalAccuracy positional
     * accuracy} are lost by this method. If those metadata are wanted, use the {@linkplain
     * CoordinateOperationFactory coordinate operation factory} directly.
     *
     * <p>Sample use:
     *
     * <blockquote>
     *
     * <code>
     * {@linkplain MathTransform} transform = CRS.findMathTransform(
     * CRS.{@linkplain #decode decode}("EPSG:42102"),
     * CRS.{@linkplain #decode decode}("EPSG:4326") );
     * </blockquote></code>
     *
     * @param sourceCRS The source CRS.
     * @param targetCRS The target CRS.
     * @return The math transform from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If no math transform can be created for the specified source and
     *     target CRS.
     */
    public static MathTransform findMathTransform(
            final CoordinateReferenceSystem sourceCRS, final CoordinateReferenceSystem targetCRS)
            throws FactoryException {
        return findMathTransform(sourceCRS, targetCRS, false);
    }

    /**
     * Grab a transform between two Coordinate Reference Systems. This method is similar to <code>
     * {@linkplain #findMathTransform(CoordinateReferenceSystem, CoordinateReferenceSystem)
     * findMathTransform}(sourceCRS, targetCRS)</code>, except that it can optionally tolerate
     * <cite>lenient datum shift</cite>. If the {@code lenient} argument is {@code true}, then this
     * method will not throw a "<cite>Bursa-Wolf parameters required</cite>" exception during datum
     * shifts if the Bursa-Wolf paramaters are not specified. Instead it will assume a no datum
     * shift.
     *
     * @param sourceCRS The source CRS.
     * @param targetCRS The target CRS.
     * @param lenient {@code true} if the math transform should be created even when there is no
     *     information available for a datum shift. The default value is {@code false}.
     * @return The math transform from {@code sourceCRS} to {@code targetCRS}.
     * @throws FactoryException If no math transform can be created for the specified source and
     *     target CRS.
     * @see Hints#LENIENT_DATUM_SHIFT
     */
    public static MathTransform findMathTransform(
            final CoordinateReferenceSystem sourceCRS,
            final CoordinateReferenceSystem targetCRS,
            boolean lenient)
            throws FactoryException {
        if (equalsIgnoreMetadata(sourceCRS, targetCRS)) {
            // Slight optimization in order to avoid the overhead of loading the full referencing
            // engine.
            return IdentityTransform.create(sourceCRS.getCoordinateSystem().getDimension());
        }
        CoordinateOperationFactory operationFactory = getCoordinateOperationFactory(lenient);
        return operationFactory.createOperation(sourceCRS, targetCRS).getMathTransform();
    }

    /**
     * Transforms the given envelope to the specified CRS. If the given envelope is null, or the
     * {@linkplain Envelope#getCoordinateReferenceSystem envelope CRS} is null, or the given target
     * CRS is null, or the transform {@linkplain MathTransform#isIdentity is identity}, then the
     * envelope is returned unchanged. Otherwise a new transformed envelope is returned.
     *
     * <p><strong>Don't use this method if there is many envelopes to transform.</strong> This
     * method is provided as a convenience when there is only one envelope to transform between CRS
     * that can't be known in advance. If there is many of them or if the CRS are restricted to
     * known values, get the {@linkplain CoordinateOperation coordinate operation} or {@linkplain
     * MathTransform math transform} once for ever and invoke one of the methods below instead
     * (unless if performance is not a concern).
     *
     * @param envelope The envelope to transform (may be {@code null}).
     * @param targetCRS The target CRS (may be {@code null}).
     * @return A new transformed envelope, or directly {@code envelope} if no transformation was
     *     required.
     * @throws TransformException If a transformation was required and failed.
     * @since 2.5
     */
    public static GeneralEnvelope transform(
            Envelope envelope, final CoordinateReferenceSystem targetCRS)
            throws TransformException {
        if (envelope != null && targetCRS != null) {
            final CoordinateReferenceSystem sourceCRS = envelope.getCoordinateReferenceSystem();
            if (sourceCRS != null) {
                if (!equalsIgnoreMetadata(sourceCRS, targetCRS)) {
                    final CoordinateOperationFactory factory = getCoordinateOperationFactory(true);
                    final CoordinateOperation operation;
                    try {
                        operation = factory.createOperation(sourceCRS, targetCRS);
                    } catch (FactoryException exception) {
                        throw new TransformException(
                                Errors.format(ErrorKeys.CANT_TRANSFORM_ENVELOPE), exception);
                    }
                    if (!operation.getMathTransform().isIdentity()) {
                        envelope = transform(operation, envelope);
                    } else if (!equalsIgnoreMetadata(
                            envelope.getCoordinateReferenceSystem(), targetCRS)) {
                        GeneralEnvelope tx = new GeneralEnvelope(envelope);
                        tx.setCoordinateReferenceSystem(targetCRS);
                        envelope = tx;
                    }
                }
                assert equalsIgnoreMetadata(envelope.getCoordinateReferenceSystem(), targetCRS);
            }
        }
        return GeneralEnvelope.toGeneralEnvelope(envelope);
    }

    /**
     * Transforms an envelope using the given {@linkplain MathTransform math transform}. The
     * transformation is only approximative. Note that the returned envelope may not have the same
     * number of dimensions than the original envelope.
     *
     * <p>Note that this method can not handle the case where the envelope contains the North or
     * South pole, or when it cross the &plusmn;180° longitude, because {@linkplain MathTransform
     * math transforms} do not carry suffisient informations. For a more robust envelope
     * transformation, use {@link #transform(CoordinateOperation, Envelope)} instead.
     *
     * @param transform The transform to use.
     * @param envelope Envelope to transform, or {@code null}. This envelope will not be modified.
     * @return The transformed envelope, or {@code null} if {@code envelope} was null.
     * @throws TransformException if a transform failed.
     * @since 2.4
     * @see #transform(CoordinateOperation, Envelope)
     */
    public static GeneralEnvelope transform(final MathTransform transform, final Envelope envelope)
            throws TransformException {
        return transform(transform, envelope, null);
    }

    /**
     * Implementation of {@link #transform(MathTransform, Envelope)} with the opportunity to save
     * the projected center coordinate. If {@code targetPt} is non-null, then this method will set
     * it to the center of the source envelope projected to the target CRS.
     */
    private static GeneralEnvelope transform(
            final MathTransform transform, final Envelope envelope, GeneralDirectPosition targetPt)
            throws TransformException {
        if (envelope == null) {
            return null;
        }
        if (transform.isIdentity()) {
            /*
             * Slight optimisation: Just copy the envelope. Note that we need to set the CRS
             * to null because we don't know what the target CRS was supposed to be. Even if
             * an identity transform often imply that the target CRS is the same one than the
             * source CRS, it is not always the case. The metadata may be differents, or the
             * transform may be a datum shift without Bursa-Wolf parameters, etc.
             */
            final GeneralEnvelope e = new GeneralEnvelope(envelope);
            e.setCoordinateReferenceSystem(null);
            if (targetPt != null) {
                for (int i = envelope.getDimension(); --i >= 0; ) {
                    targetPt.setOrdinate(i, e.getMedian(i));
                }
            }
            return e;
        }
        /*
         * Checks argument validity: envelope and math transform dimensions must be consistent.
         */
        final int sourceDim = transform.getSourceDimensions();
        if (envelope.getDimension() != sourceDim) {
            throw new MismatchedDimensionException(
                    Errors.format(
                            ErrorKeys.MISMATCHED_DIMENSION_$2, sourceDim, envelope.getDimension()));
        }
        int coordinateNumber = 0;
        GeneralEnvelope transformed = null;
        if (targetPt == null) {
            targetPt = new GeneralDirectPosition(transform.getTargetDimensions());
        }
        /*
         * Before to run the loops, we must initialize the coordinates to the minimal values.
         * This coordinates will be updated in the 'switch' statement inside the 'while' loop.
         */
        final GeneralDirectPosition sourcePt = new GeneralDirectPosition(sourceDim);
        for (int i = sourceDim; --i >= 0; ) {
            sourcePt.setOrdinate(i, envelope.getMinimum(i));
        }
        loop:
        while (true) {
            /*
             * Transform a point and add the transformed point to the destination envelope.
             * Note that the very last point to be projected must be the envelope center.
             */
            if (targetPt != transform.transform(sourcePt, targetPt)) {
                throw new UnsupportedImplementationException(transform.getClass());
            }
            if (transformed != null) {
                transformed.add(targetPt);
            } else {
                transformed = new GeneralEnvelope(targetPt, targetPt);
            }
            /*
             * Get the next point's coordinates.  The 'coordinateNumber' variable should
             * be seen as a number in base 5 where the number of digits is equals to the
             * number of dimensions. For example, a 4-D space would have numbers ranging
             * from "0000" to "4444" (numbers in base 4). The digits are then translated
             * into minimal, central or maximal ordinates. The outer loop stops when the
             * counter roll back to "0000".  Note that 'targetPt' must keep the value of
             * the last projected point, which must be the envelope center identified by
             * "4444" in the 4-D case.
             */
            int n = ++coordinateNumber;
            for (int i = sourceDim; --i >= 0; ) {
                switch (n % 5) {
                    case 0:
                        sourcePt.setOrdinate(i, envelope.getMinimum(i));
                        n /= 5;
                        break;
                    case 1:
                        sourcePt.setOrdinate(i, envelope.getMaximum(i));
                        continue loop;
                    case 2:
                        sourcePt.setOrdinate(
                                i, (envelope.getMinimum(i) + envelope.getMedian(i)) / 2);
                        continue loop;
                    case 3:
                        sourcePt.setOrdinate(
                                i, (envelope.getMedian(i) + envelope.getMaximum(i)) / 2);
                        continue loop;
                    case 4:
                        sourcePt.setOrdinate(i, envelope.getMedian(i));
                        continue loop;
                    default:
                        throw new AssertionError(n); // Should never happen
                }
            }
            break;
        }
        return transformed;
    }

    /**
     * Transforms an envelope using the given {@linkplain CoordinateOperation coordinate operation}.
     * The transformation is only approximative. It may be bigger than the smallest possible
     * bounding box, but should not be smaller. Note that the returned envelope may not have the
     * same number of dimensions than the original envelope.
     *
     * <p>This method can handle the case where the envelope contains the North or South pole, or
     * when it cross the &plusmn;180° longitude.
     *
     * @param operation The operation to use. Source and target dimension must be 2.
     * @param envelope Envelope to transform, or {@code null}. This envelope will not be modified.
     * @return The transformed envelope, or {@code null} if {@code envelope} was null.
     * @throws TransformException if a transform failed.
     * @since 2.4
     * @see #transform(MathTransform, Envelope)
     */
    public static GeneralEnvelope transform(
            final CoordinateOperation operation, final Envelope envelope)
            throws TransformException {
        if (envelope == null) {
            return null;
        }
        final CoordinateReferenceSystem sourceCRS = operation.getSourceCRS();
        if (sourceCRS != null) {
            final CoordinateReferenceSystem crs = envelope.getCoordinateReferenceSystem();
            if (crs != null && !equalsIgnoreMetadata(crs, sourceCRS)) {
                throw new MismatchedReferenceSystemException(
                        Errors.format(ErrorKeys.MISMATCHED_COORDINATE_REFERENCE_SYSTEM));
            }
        }
        MathTransform mt = operation.getMathTransform();
        final GeneralDirectPosition centerPt = new GeneralDirectPosition(mt.getTargetDimensions());
        final GeneralEnvelope transformed = transform(mt, envelope, centerPt);
        /*
         * If the source envelope crosses the expected range of valid coordinates, also projects
         * the range bounds as a safety. Example: if the source envelope goes from 150 to 200°E,
         * some map projections will interpret 200° as if it was -160°, and consequently produce
         * an envelope which do not include the 180°W extremum. We will add those extremum points
         * explicitly as a safety. It may leads to bigger than necessary target envelope, but the
         * contract is to include at least the source envelope, not to returns the smallest one.
         */
        if (sourceCRS != null) {
            final CoordinateSystem cs = sourceCRS.getCoordinateSystem();
            if (cs != null) { // Should never be null, but check as a paranoiac safety.
                DirectPosition sourcePt = null;
                DirectPosition targetPt = null;
                final int dimension = cs.getDimension();
                for (int i = 0; i < dimension; i++) {
                    final CoordinateSystemAxis axis = cs.getAxis(i);
                    if (axis == null) { // Should never be null, but check as a paranoiac safety.
                        continue;
                    }
                    final double min = envelope.getMinimum(i);
                    final double max = envelope.getMaximum(i);
                    final double v1 = axis.getMinimumValue();
                    final double v2 = axis.getMaximumValue();
                    final boolean b1 = (v1 > min && v1 < max);
                    final boolean b2 = (v2 > min && v2 < max);
                    if (!b1 && !b2) {
                        continue;
                    }
                    if (sourcePt == null) {
                        sourcePt = new GeneralDirectPosition(dimension);
                        for (int j = 0; j < dimension; j++) {
                            sourcePt.setOrdinate(j, envelope.getMedian(j));
                        }
                    }
                    if (b1) {
                        sourcePt.setOrdinate(i, v1);
                        transformed.add(targetPt = mt.transform(sourcePt, targetPt));
                    }
                    if (b2) {
                        sourcePt.setOrdinate(i, v2);
                        transformed.add(targetPt = mt.transform(sourcePt, targetPt));
                    }
                    sourcePt.setOrdinate(i, envelope.getMedian(i));
                }
            }
        }

        // check the target CRSS
        /*
         * Special case for polar stereographic, if the envelope contains the origin, then
         * the whole set of longitudes should be included
         */
        final CoordinateReferenceSystem targetCRS = operation.getTargetCRS();
        if (targetCRS == null) {
            return transformed;
        }
        GeneralEnvelope generalEnvelope = toGeneralEnvelope(envelope);
        MapProjection sourceProjection = CRS.getMapProjection(sourceCRS);
        if (sourceProjection instanceof PolarStereographic
                || (sourceProjection instanceof LambertAzimuthalEqualArea)) {
            ParameterValue<?> fe =
                    sourceProjection
                            .getParameterValues()
                            .parameter(
                                    MapProjection.AbstractProvider.FALSE_EASTING
                                            .getName()
                                            .getCode());
            double originX = fe.doubleValue();
            ParameterValue<?> fn =
                    sourceProjection
                            .getParameterValues()
                            .parameter(
                                    MapProjection.AbstractProvider.FALSE_NORTHING
                                            .getName()
                                            .getCode());
            double originY = fn.doubleValue();
            DirectPosition2D origin = new DirectPosition2D(originX, originY);
            if (isPole(origin, sourceCRS)) {
                if (generalEnvelope.contains(origin)) {
                    if (targetCRS instanceof GeographicCRS) {
                        DirectPosition lowerCorner = transformed.getLowerCorner();
                        if (getAxisOrder(targetCRS) == AxisOrder.NORTH_EAST) {
                            lowerCorner.setOrdinate(1, -180);
                            transformed.add(lowerCorner);
                            lowerCorner.setOrdinate(1, 180);
                            transformed.add(lowerCorner);
                        } else {
                            lowerCorner.setOrdinate(0, -180);
                            transformed.add(lowerCorner);
                            lowerCorner.setOrdinate(0, 180);
                            transformed.add(lowerCorner);
                        }
                    } else {
                        // there is no guarantee that the whole range of longitudes will make
                        // sense for the target projection. We do a 1deg sampling as a compromise
                        // between
                        // speed and accuracy
                        DirectPosition lc = transformed.getLowerCorner();
                        DirectPosition uc = transformed.getUpperCorner();
                        for (int j = -180; j < 180; j++) {
                            expandEnvelopeByLongitude(j, lc, transformed, targetCRS);
                            expandEnvelopeByLongitude(j, uc, transformed, targetCRS);
                        }
                    }
                } else {
                    // check where the point closes to the origin is, make sure it's included
                    // in the tranformation points
                    if (generalEnvelope.getMinimum(0) < originX
                            && generalEnvelope.getMaximum(0) > originX) {
                        DirectPosition lc = generalEnvelope.getLowerCorner();
                        lc.setOrdinate(0, originX);
                        mt.transform(lc, lc);
                        transformed.add(lc);
                        DirectPosition uc = generalEnvelope.getUpperCorner();
                        uc.setOrdinate(0, originX);
                        mt.transform(uc, uc);
                        transformed.add(uc);
                    }
                    if (generalEnvelope.getMinimum(1) < originY
                            && generalEnvelope.getMaximum(1) > originY) {
                        DirectPosition lc = generalEnvelope.getLowerCorner();
                        lc.setOrdinate(1, originY);
                        mt.transform(lc, lc);
                        transformed.add(lc);
                        DirectPosition uc = generalEnvelope.getUpperCorner();
                        uc.setOrdinate(1, originY);
                        mt.transform(uc, uc);
                        transformed.add(uc);
                    }
                }
            }
        }
        MapProjection targetProjection = CRS.getMapProjection(targetCRS);
        if (targetProjection instanceof PolarStereographic && sourceCRS instanceof GeographicCRS) {
            final CoordinateSystem sourceCS = sourceCRS.getCoordinateSystem();
            for (int i = 0; i < sourceCS.getDimension(); i++) {
                final CoordinateSystemAxis axis = sourceCS.getAxis(i);
                if (equalsIgnoreMetadata(DefaultCoordinateSystemAxis.LONGITUDE, axis)) {
                    double minLon = envelope.getMinimum(i);
                    double maxLon = envelope.getMaximum(i);
                    // world spanning longitude? add points around the globe quadrants then
                    if ((maxLon - minLon) >= 360) {
                        DirectPosition lower = generalEnvelope.getLowerCorner();
                        DirectPosition upper = generalEnvelope.getUpperCorner();
                        DirectPosition dest = new DirectPosition2D();
                        for (int lon = -180; lon <= 180; lon += 90) {
                            lower.setOrdinate(i, lon);
                            mt.transform(lower, dest);
                            transformed.add(dest);
                            upper.setOrdinate(i, lon);
                            mt.transform(upper, dest);
                            transformed.add(dest);
                        }
                    }
                }
            }
        }

        /*
         * Now takes the target CRS in account...
         */
        transformed.setCoordinateReferenceSystem(targetCRS);
        final CoordinateSystem targetCS = targetCRS.getCoordinateSystem();
        if (targetCS == null) {
            // It should be an error, but we keep this method tolerant.
            return transformed;
        }
        /*
         * Checks for singularity points. For example the south pole is a singularity point in
         * geographic CRS because we reach the maximal value allowed on one particular geographic
         * axis, namely latitude. This point is not a singularity in the stereographic projection,
         * where axis extends toward infinity in all directions (mathematically) and south pole
         * has nothing special apart being the origin (0,0).
         *
         * Algorithm:
         *
         * 1) Inspect the target axis, looking if there is any bounds. If bounds are found, get
         *    the coordinates of singularity points and project them from target to source CRS.
         *
         *    Example: if the transformed envelope above is (80°S to 85°S, 10°W to 50°W), and if
         *             target axis inspection reveal us that the latitude in target CRS is bounded
         *             at 90°S, then project (90°S,30°W) to source CRS. Note that the longitude is
         *             set to the the center of the envelope longitude range (more on this later).
         *
         * 2) If the singularity point computed above is inside the source envelope, add that
         *    point to the target (transformed) envelope.
         *
         * Note: We could choose to project the (-180, -90), (180, -90), (-180, 90), (180, 90)
         * points, or the (-180, centerY), (180, centerY), (centerX, -90), (centerX, 90) points
         * where (centerX, centerY) are transformed from the source envelope center. It make
         * no difference for polar projections because the longitude is irrelevant at pole, but
         * may make a difference for the 180° longitude bounds.  Consider a Mercator projection
         * where the transformed envelope is between 20°N and 40°N. If we try to project (-180,90),
         * we will get a TransformException because the Mercator projection is not supported at
         * pole. If we try to project (-180, 30) instead, we will get a valid point. If this point
         * is inside the source envelope because the later overlaps the 180° longitude, then the
         * transformed envelope will be expanded to the full (-180 to 180) range. This is quite
         * large, but at least it is correct (while the envelope without expansion is not).
         */
        DirectPosition sourcePt = null;
        DirectPosition targetPt = null;
        final int dimension = targetCS.getDimension();
        for (int i = 0; i < dimension; i++) {
            final CoordinateSystemAxis axis = targetCS.getAxis(i);
            if (axis == null) { // Should never be null, but check as a paranoiac safety.
                continue;
            }
            boolean testMax = false; // Tells if we are testing the minimal or maximal value.
            do {
                final double extremum = testMax ? axis.getMaximumValue() : axis.getMinimumValue();
                if (Double.isInfinite(extremum) || Double.isNaN(extremum)) {
                    /*
                     * The axis is unbounded. It should always be the case when the target CRS is
                     * a map projection, in which case this loop will finish soon and this method
                     * will do nothing more (no object instantiated, no MathTransform inversed...)
                     */
                    continue;
                }
                if (targetPt == null) {
                    try {
                        mt = mt.inverse();
                    } catch (NoninvertibleTransformException exception) {
                        /*
                         * If the transform is non invertible, this method can't do anything. This
                         * is not a fatal error because the envelope has already be transformed by
                         * the caller. We lost the check for singularity points performed by this
                         * method, but it make no difference in the common case where the source
                         * envelope didn't contains any of those points.
                         *
                         * Note that this exception is normal if target dimension is smaller than
                         * source dimension, since the math transform can not reconstituate the
                         * lost dimensions. So we don't log any warning in this case.
                         */
                        if (dimension >= mt.getSourceDimensions()) {
                            unexpectedException("transform", exception);
                        }
                        return transformed;
                    }
                    targetPt = new GeneralDirectPosition(mt.getSourceDimensions());
                    for (int j = 0; j < dimension; j++) {
                        targetPt.setOrdinate(j, centerPt.getOrdinate(j));
                    }
                }
                targetPt.setOrdinate(i, extremum);
                try {
                    sourcePt = mt.transform(targetPt, sourcePt);
                } catch (Exception e) {
                    /*
                     * This exception may be normal. For example we are sure to get this exception
                     * when trying to project the latitude extremums with a cylindrical Mercator
                     * projection. Do not log any message and try the other points.
                     */
                    continue;
                }
                if (generalEnvelope.contains(sourcePt)) {
                    transformed.add(targetPt);
                }
            } while ((testMax = !testMax) == true);
            if (targetPt != null) {
                targetPt.setOrdinate(i, centerPt.getOrdinate(i));
            }
        }

        if (targetProjection != null) {
            // the points intersecting the rays emanating from the center of the projection in polar
            // stereographic
            // and other projections is a maximum deformation point, add those to the envelope too
            getProjectionCenterLonLat(targetCRS, centerPt);
            // now try to intesect the source envelope with the center point
            if (isPole(centerPt, DefaultGeographicCRS.WGS84)) {
                try {
                    MathTransform geoToTarget;
                    Envelope geoEnvelope;
                    if (sourceCRS instanceof GeographicCRS) {
                        // this is a simplification to avoid dateline flips due to datum differences
                        geoToTarget = findMathTransform(sourceCRS, targetCRS);
                        geoEnvelope = envelope;
                    } else {
                        MathTransform mtWgs84 =
                                findMathTransform(sourceCRS, DefaultGeographicCRS.WGS84);
                        geoToTarget = findMathTransform(DefaultGeographicCRS.WGS84, targetCRS);
                        geoEnvelope = transform(mtWgs84, envelope, null);
                    }
                    expandEnvelopeOnExtremePoints(centerPt, transformed, geoToTarget, geoEnvelope);
                    if (targetProjection instanceof PolarStereographic
                            || targetProjection instanceof LambertAzimuthalEqualArea) {
                        // sample quadrant points too
                        centerPt.setOrdinate(0, rollLongitude(centerPt.getOrdinate(0) - 90));
                        expandEnvelopeOnExtremePoints(
                                centerPt, transformed, geoToTarget, geoEnvelope);
                        centerPt.setOrdinate(0, rollLongitude(centerPt.getOrdinate(0) - 90));
                        expandEnvelopeOnExtremePoints(
                                centerPt, transformed, geoToTarget, geoEnvelope);
                        centerPt.setOrdinate(0, rollLongitude(centerPt.getOrdinate(0) - 90));
                        expandEnvelopeOnExtremePoints(
                                centerPt, transformed, geoToTarget, geoEnvelope);
                    }
                } catch (FactoryException | TransformException e) {
                    LOGGER.log(
                            Level.FINE,
                            "Failed to transform from source to WGS84 to further enlarge the envelope on extreme points, proceeding without expansion",
                            e);
                }
            }
        }

        return transformed;
    }

    private static double rollLongitude(final double x) {
        double rolled = x - (((int) (x + Math.signum(x) * 180)) / 360) * 360.0;
        return rolled;
    }

    private static void expandEnvelopeOnExtremePoints(
            GeneralDirectPosition centerPt,
            GeneralEnvelope transformed,
            MathTransform geoToTarget,
            Envelope geoEnvelope)
            throws TransformException {
        GeneralDirectPosition workPoint = new GeneralDirectPosition(centerPt.getDimension());
        double centerLon = centerPt.getOrdinate(0);
        double minLon = geoEnvelope.getMinimum(0);
        double maxLon = geoEnvelope.getMaximum(0);
        double minLat = geoEnvelope.getMinimum(1);
        double maxLat = geoEnvelope.getMaximum(1);
        if (minLon <= centerLon && centerLon <= maxLon) {
            // intersection at boundaries, south
            includeTransformedPoint(transformed, geoToTarget, workPoint, centerLon, minLat);
            // intersection at boundaries, north
            includeTransformedPoint(transformed, geoToTarget, workPoint, centerLon, maxLat);
        }
        double centerLat = centerPt.getOrdinate(1);
        if (minLat <= centerLat && centerLat <= maxLat) {
            // intersection at boundaries, west
            includeTransformedPoint(transformed, geoToTarget, workPoint, minLon, centerLat);
            // intersection at boundaries, east
            includeTransformedPoint(transformed, geoToTarget, workPoint, maxLon, centerLat);
        }
    }

    private static void includeTransformedPoint(
            GeneralEnvelope envelope,
            MathTransform mt,
            GeneralDirectPosition workPoint,
            double x,
            double y)
            throws TransformException {
        workPoint.setOrdinate(0, x);
        workPoint.setOrdinate(1, y);
        mt.transform(workPoint, workPoint);
        envelope.add(workPoint);
    }

    private static GeneralDirectPosition getProjectionCenterLonLat(
            CoordinateReferenceSystem crs, GeneralDirectPosition centerPt) {
        // set defaults
        centerPt.setOrdinate(0, 0);
        centerPt.setOrdinate(1, 0);

        MapProjection projection = getMapProjection(crs);
        if (projection == null) {
            return centerPt;
        }

        for (GeneralParameterValue gpv : projection.getParameterValues().values()) {
            // for safety
            if (!(gpv instanceof ParameterValue)) {
                continue;
            }
            ParameterValue pv = (ParameterValue) gpv;
            ReferenceIdentifier pvName = pv.getDescriptor().getName();
            if (MapProjection.AbstractProvider.LATITUDE_OF_ORIGIN.getName().equals(pvName)) {
                centerPt.setOrdinate(1, pv.doubleValue());
            } else if (MapProjection.AbstractProvider.LATITUDE_OF_CENTRE.getName().equals(pvName)) {
                centerPt.setOrdinate(1, pv.doubleValue());
            } else if (MapProjection.AbstractProvider.LONGITUDE_OF_CENTRE
                    .getName()
                    .equals(pvName)) {
                centerPt.setOrdinate(0, pv.doubleValue());
            } else if (MapProjection.AbstractProvider.CENTRAL_MERIDIAN.getName().equals(pvName)) {
                centerPt.setOrdinate(0, pv.doubleValue());
            }
        }

        return centerPt;
    }

    private static boolean isPole(DirectPosition point, CoordinateReferenceSystem crs) {
        DirectPosition result = new DirectPosition2D();
        GeographicCRS geographic;
        try {
            ProjectedCRS projectedCRS = getProjectedCRS(crs);
            if (projectedCRS != null) {
                geographic = projectedCRS.getBaseCRS();
                MathTransform mt = CRS.findMathTransform(projectedCRS, geographic);
                mt.transform(point, result);
            } else if (crs instanceof GeographicCRS) {
                result = point;
                geographic = (GeographicCRS) crs;
            } else {
                return false;
            }
        } catch (MismatchedDimensionException | TransformException | FactoryException e) {
            return false;
        }

        final double EPS = 1e-6;
        if (getAxisOrder(geographic) == AxisOrder.NORTH_EAST) {
            return Math.abs(result.getOrdinate(0) - 90) < EPS
                    || Math.abs(result.getOrdinate(0) + 90) < EPS;
        } else {
            return Math.abs(result.getOrdinate(1) - 90) < EPS
                    || Math.abs(result.getOrdinate(1) + 90) < EPS;
        }
    }

    private static void expandEnvelopeByLongitude(
            double longitude,
            DirectPosition input,
            GeneralEnvelope transformed,
            CoordinateReferenceSystem sourceCRS) {
        try {
            MathTransform mt = findMathTransform(sourceCRS, DefaultGeographicCRS.WGS84);
            DirectPosition2D pos = new DirectPosition2D(sourceCRS);
            mt.transform(input, pos);
            pos.setOrdinate(0, longitude);
            mt.inverse().transform(pos, pos);
            transformed.add(pos);
        } catch (Exception e) {
            LOGGER.log(
                    Level.FINER,
                    "Tried to expand target envelope to include longitude "
                            + longitude
                            + " but failed. This is not necesseraly and issue, "
                            + "this is a best effort attempt to handle the polar stereographic "
                            + "pole singularity during reprojection",
                    e);
        }
    }

    private static GeneralEnvelope toGeneralEnvelope(final Envelope envelope) {
        // TODO: avoid the hack below if we provide a contains(DirectPosition)
        // method in GeoAPI Envelope interface.
        GeneralEnvelope generalEnvelope;
        if (envelope instanceof GeneralEnvelope) {
            generalEnvelope = (GeneralEnvelope) envelope;
        } else {
            generalEnvelope = new GeneralEnvelope(envelope);
        }
        return generalEnvelope;
    }

    /**
     * Transforms a rectangular envelope using the given {@linkplain MathTransform math transform}.
     * The transformation is only approximative. Invoking this method is equivalent to invoking the
     * following:
     *
     * <p>
     *
     * <pre>transform(transform, new GeneralEnvelope(envelope)).toRectangle2D()</pre>
     *
     * <p>Note that this method can not handle the case where the rectangle contains the North or
     * South pole, or when it cross the &plusmn;180° longitude, because {@linkplain MathTransform
     * math transforms} do not carry suffisient informations. For a more robust rectangle
     * transformation, use {@link #transform(CoordinateOperation, Rectangle2D, Rectangle2D)}
     * instead.
     *
     * @param transform The transform to use. Source and target dimension must be 2.
     * @param envelope The rectangle to transform (may be {@code null}).
     * @param destination The destination rectangle (may be {@code envelope}). If {@code null}, a
     *     new rectangle will be created and returned.
     * @return {@code destination}, or a new rectangle if {@code destination} was non-null and
     *     {@code envelope} was null.
     * @throws TransformException if a transform failed.
     * @since 2.4
     * @see #transform(CoordinateOperation, Rectangle2D, Rectangle2D)
     * @see org.geotools.referencing.operation.matrix.XAffineTransform#transform(
     *     java.awt.geom.AffineTransform, Rectangle2D, Rectangle2D)
     */
    public static Rectangle2D transform(
            final MathTransform2D transform, final Rectangle2D envelope, Rectangle2D destination)
            throws TransformException {
        return transform(transform, envelope, destination, new Point2D.Double());
    }

    /**
     * Implementation of {@link #transform(MathTransform, Rectangle2D, Rectangle2D)} with the
     * opportunity to save the projected center coordinate. This method sets {@code point} to the
     * center of the source envelope projected to the target CRS.
     */
    @SuppressWarnings("fallthrough")
    private static Rectangle2D transform(
            final MathTransform2D transform,
            final Rectangle2D envelope,
            Rectangle2D destination,
            final Point2D.Double point)
            throws TransformException {
        if (envelope == null) {
            return null;
        }
        double xmin = Double.POSITIVE_INFINITY;
        double ymin = Double.POSITIVE_INFINITY;
        double xmax = Double.NEGATIVE_INFINITY;
        double ymax = Double.NEGATIVE_INFINITY;
        for (int i = 0; i <= 8; i++) {
            /*
             *   (0)----(5)----(1)
             *    |             |
             *   (4)    (8)    (7)
             *    |             |
             *   (2)----(6)----(3)
             *
             * (note: center must be last)
             */
            point.x = (i & 1) == 0 ? envelope.getMinX() : envelope.getMaxX();
            point.y = (i & 2) == 0 ? envelope.getMinY() : envelope.getMaxY();
            switch (i) {
                case 5: // fall through
                case 6:
                    point.x = envelope.getCenterX();
                    break;
                case 8:
                    point.x = envelope.getCenterX(); // fall through
                case 7: // fall through
                case 4:
                    point.y = envelope.getCenterY();
                    break;
            }
            if (point != transform.transform(point, point)) {
                throw new UnsupportedImplementationException(transform.getClass());
            }
            if (point.x < xmin) xmin = point.x;
            if (point.x > xmax) xmax = point.x;
            if (point.y < ymin) ymin = point.y;
            if (point.y > ymax) ymax = point.y;
        }
        if (destination != null) {
            destination.setRect(xmin, ymin, xmax - xmin, ymax - ymin);
        } else {
            destination = XRectangle2D.createFromExtremums(xmin, ymin, xmax, ymax);
        }
        // Attempt the 'equalsEpsilon' assertion only if source and destination are not same and
        // if the target envelope is Float or Double (this assertion doesn't work with integers).
        assert (destination == envelope
                                || !(destination instanceof Rectangle2D.Double
                                        || destination instanceof Rectangle2D.Float))
                        || XRectangle2D.equalsEpsilon(
                                destination,
                                transform(transform, new Envelope2D(null, envelope))
                                        .toRectangle2D())
                : destination;
        return destination;
    }

    /**
     * Transforms a rectangular envelope using the given {@linkplain CoordinateOperation coordinate
     * operation}. The transformation is only approximative. Invoking this method is equivalent to
     * invoking the following:
     *
     * <p>
     *
     * <pre>transform(operation, new GeneralEnvelope(envelope)).toRectangle2D()</pre>
     *
     * <p>This method can handle the case where the rectangle contains the North or South pole, or
     * when it cross the &plusmn;180° longitude.
     *
     * @param operation The operation to use. Source and target dimension must be 2.
     * @param envelope The rectangle to transform (may be {@code null}).
     * @param destination The destination rectangle (may be {@code envelope}). If {@code null}, a
     *     new rectangle will be created and returned.
     * @return {@code destination}, or a new rectangle if {@code destination} was non-null and
     *     {@code envelope} was null.
     * @throws TransformException if a transform failed.
     * @since 2.4
     * @see #transform(MathTransform2D, Rectangle2D, Rectangle2D)
     * @see org.geotools.referencing.operation.matrix.XAffineTransform#transform(
     *     java.awt.geom.AffineTransform, Rectangle2D, Rectangle2D)
     */
    public static Rectangle2D transform(
            final CoordinateOperation operation,
            final Rectangle2D envelope,
            Rectangle2D destination)
            throws TransformException {
        if (envelope == null) {
            return null;
        }

        GeneralEnvelope result = transform(operation, new GeneralEnvelope(envelope));
        if (destination == null) {
            return result.toRectangle2D();
        } else {
            destination.setFrame(
                    result.getMinimum(0),
                    result.getMinimum(1),
                    result.getSpan(0),
                    result.getSpan(1));
            return destination;
        }
    }

    /**
     * Invoked when an unexpected exception occured. Those exceptions must be non-fatal, i.e. the
     * caller <strong>must</strong> have a raisonable fallback (otherwise it should propagate the
     * exception).
     */
    static void unexpectedException(final String methodName, final Exception exception) {
        Logging.unexpectedException(CRS.class, methodName, exception);
    }

    /**
     * Resets some aspects of the referencing system. The aspects to be reset are specified by a
     * space or comma delimited string, which may include any of the following elements:
     *
     * <p>
     *
     * <ul>
     *   <li>{@code "plugins"} for {@linkplain ReferencingFactoryFinder#scanForPlugins searching the
     *       classpath for new plugins}.
     *   <li>{@code "warnings"} for {@linkplain MapProjection#resetWarnings re-enabling the warnings
     *       to be issued when a coordinate is out of projection valid area}.
     * </ul>
     *
     * @param aspects The aspects to reset, or {@code "all"} for all of them. Unknown aspects are
     *     silently ignored.
     * @since 2.5
     */
    public static void reset(final String aspects) {
        final StringTokenizer tokens = new StringTokenizer(aspects, ", \t\n\r\f");
        while (tokens.hasMoreTokens()) {
            final String aspect = tokens.nextToken().trim();
            final boolean all = aspect.equalsIgnoreCase("all");
            if (all || aspect.equalsIgnoreCase("plugins")) {
                ReferencingFactoryFinder.reset();
                ReferencingFactoryFinder.scanForPlugins();
            }
            if (all || aspect.equalsIgnoreCase("warnings")) {
                MapProjection.resetWarnings();
            }
        }
        FORCED_LON_LAT = null;
        defaultFactory = null;
        xyFactory = null;
        strictFactory = null;
        lenientFactory = null;
    }

    /**
     * Cleans up the thread local set in this thread. They can prevent web applications from proper
     * shutdown
     */
    public static void cleanupThreadLocals() {
        DefaultMathTransformFactory.cleanupThreadLocals();
        Formattable.cleanupThreadLocals();
    }

    /**
     * Determines the axis ordering of a specified crs object.
     *
     * @param crs The coordinate reference system.
     * @return One of {@link AxisOrder#EAST_NORTH}, {@link AxisOrder@NORTH_EAST}, or {@link
     *     AxisOrder#INAPPLICABLE}
     * @see AxisOrder
     * @since 2.7
     */
    public static AxisOrder getAxisOrder(CoordinateReferenceSystem crs) {
        return getAxisOrder(crs, false);
    }

    /**
     * Determines the axis ordering of a specified crs object.
     *
     * <p>The <tt>useBaseGeoCRS</tt> parameter is used to control behaviour for projected crs. When
     * set to <tt>true</tt> the comparison will use the coordinate system for the underlying
     * geographic crs object for the comparison. When set to false the comparison will use the
     * coordinate system from projected crs itself.
     *
     * @param crs The coordinate reference system.
     * @param useBaseGeoCRS Controls whether the base geo crs is used for projected crs.
     * @return One of {@link AxisOrder#EAST_NORTH}, {@link AxisOrder@NORTH_EAST}, or {@link
     *     AxisOrder#INAPPLICABLE}
     */
    public static AxisOrder getAxisOrder(CoordinateReferenceSystem crs, boolean useBaseGeoCRS) {
        CoordinateSystem cs = null;

        if (crs instanceof ProjectedCRS) {
            cs =
                    !useBaseGeoCRS
                            ? crs.getCoordinateSystem()
                            : ((ProjectedCRS) crs).getBaseCRS().getCoordinateSystem();
        } else if (crs instanceof GeographicCRS) {
            cs = crs.getCoordinateSystem();
        } else {
            return AxisOrder.INAPPLICABLE;
        }

        int dimension = cs.getDimension();
        int longitudeDim = -1;
        int latitudeDim = -1;

        for (int i = 0; i < dimension; i++) {
            AxisDirection dir = cs.getAxis(i).getDirection().absolute();

            if (dir.equals(AxisDirection.EAST)) {
                longitudeDim = i;
            }

            if (dir.equals(AxisDirection.NORTH)) {
                latitudeDim = i;
            }
        }

        if ((longitudeDim >= 0) && (latitudeDim >= 0)) {
            if (longitudeDim < latitudeDim) {
                return AxisOrder.EAST_NORTH;
            } else {
                return AxisOrder.NORTH_EAST;
            }
        }

        return AxisOrder.INAPPLICABLE;
    }

    /**
     * Prints to the {@linkplain System#out standard output stream} some information about
     * {@linkplain CoordinateReferenceSystem coordinate reference systems} specified by their
     * authority codes. This method can be invoked from the command line in order to test the
     * {@linkplain #getAuthorityFactory authority factory} content for some specific CRS.
     *
     * <p>By default, this method prints all enumerated objects as <cite>Well Known Text</cite>.
     * However this method can prints different kind of information if an option such as {@code
     * -factories}, {@code -codes} or {@code -bursawolfs} is provided.
     *
     * <p><b>Usage:</b> {@code java org.geotools.referencing.CRS [options] [codes]}<br>
     * <b>Options:</b>
     *
     * <blockquote>
     *
     * <p><b>{@code -authority}=<var>name</var></b><br>
     * Uses the specified authority factory, for example {@code "EPSG"}. The authority name can be
     * any of the authorities listed by the {@code -factories} option. If this option is not
     * specified, then the default is all factories.
     *
     * <p><b>{@code -bursawolfs} <var>codes</var></b><br>
     * Lists the Bursa-Wolf parameters for the specified CRS ou datum objects. For some
     * transformations, there is more than one set of Bursa-Wolf parameters available. The standard
     * <cite>Well Known Text</cite> format prints only what look like the "main" one. This option
     * display all Bursa-Wolf parameters in a table for a given object.
     *
     * <p><b>{@code -codes}</b><br>
     * Lists all available authority codes. Use the {@code -authority} option if the list should be
     * restricted to a single authority.
     *
     * <p><b>{@code -colors}</b><br>
     * Enable syntax coloring on <A HREF="http://en.wikipedia.org/wiki/ANSI_escape_code">ANSI
     * X3.64</A> compatible (aka ECMA-48 and ISO/IEC 6429) terminal. This option tries to highlight
     * most of the elements relevant to the {@link #equalsIgnoreMetadata equalsIgnoreMetadata}
     * method, with the addition of Bursa-Wolf parameters.
     *
     * <p><b>{@code -encoding}=<var>charset</var></b><br>
     * Sets the console encoding for this application output. This value has no impact on data, but
     * may improve the output quality. This is not needed on Linux terminal using UTF-8 encoding
     * (tip: the <cite>terminus font</cite> gives good results).
     *
     * <p><b>{@code -dependencies}</b><br>
     * Lists authority factory dependencies as a tree.
     *
     * <p><b>{@code -factories}</b><br>
     * Lists all availables CRS authority factories.
     *
     * <p><b>{@code -forcexy}</b><br>
     * Force "longitude first" axis order.
     *
     * <p><b>{@code -help}</b><br>
     * Prints the list of options.
     *
     * <p><b>{@code -locale}=<var>name</var></b><br>
     * Formats texts in the specified {@linkplain java.util.Locale locale}.
     *
     * <p><b>{@code -operations} <var>sourceCRS</var> <var>targetCRS</var></b><br>
     * Prints all available coordinate operations between a pair of CRS. This option prints only the
     * operations explicitly defined in a database like EPSG. There is sometime many such
     * operations, and sometime none (in which case this option prints nothing - it doesn't try to
     * find an operation by itself).
     *
     * <p><b>{@code -transform} <var>sourceCRS</var> <var>targetCRS</var></b><br>
     * Prints the preferred math transform between a pair of CRS. At the difference of the {@code
     * "-operations"} option, this option pick up only one operation (usually the most accurate
     * one), inferring it if none were explicitly specified in the database.
     *
     * </blockquote>
     *
     * <strong>Examples</strong> (assuming that {@code "CRS"} is a shortcut for {@code "java
     * org.geotools.referencing.CRS"}):
     *
     * <blockquote>
     *
     * <p><b>{@code CRS EPSG:4181 EPSG:4326 CRS:84 AUTO:42001,30,0}</b><br>
     * Prints the "Luxembourg 1930" CRS, the "WGS 84" CRS (from EPSG database), the ""WGS84" CRS
     * (from the <cite>Web Map Service</cite> specification) and a UTM projection in WKT format.
     *
     * <p><b>{@code CRS -authority=EPSG 4181 4326}</b><br>
     * Prints the "Luxembourg 1930" and "WGS 84" CRS, looking only in the EPSG database (so there is
     * no need to prefix the codes with {@code "EPSG"}).
     *
     * <p><b>{@code CRS -colors EPSG:7411}</b><br>
     * Prints the "NTF (Paris) / Lambert zone II + NGF Lallemand" CRS with syntax coloring enabled.
     *
     * <p><b>{@code CRS -bursawolfs EPSG:4230}</b><br>
     * Prints three set of Bursa-Wolf parameters for a CRS based on "European Datum 1950".
     *
     * <p><b>{@code CRS -authority=EPSG -operations 4230 4326}</b><br>
     * Prints all operations declared in the EPSG database from "ED50" to "WGS 84" geographic CRS.
     * Note that for this particular pair of CRS, there is close to 40 operations declared in the
     * EPSG database. This method prints only the ones that Geotools can handle.
     *
     * <p><b>{@code CRS -transform EPSG:4230 EPSG:4326}</b><br>
     * Prints the math transform that Geotools would use by default for coordinate transformation
     * from "ED50" to "WGS 84".
     *
     * </blockquote>
     *
     * @param args Options and list of object codes to display.
     * @since 2.4
     */
    public static void main(final String[] args) {
        Command.execute(args);
    }
}
