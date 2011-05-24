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
package org.geotools.geometry.jts;

import java.awt.Shape;
import java.awt.geom.IllegalPathStateException;
import java.awt.geom.PathIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.geometry.Envelope2D;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.referencing.CRS;
import org.geotools.referencing.GeodeticCalculator;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.TransformPathNotFoundException;
import org.geotools.referencing.operation.projection.PointOutsideEnvelopeException;
import org.geotools.resources.Classes;
import org.geotools.resources.geometry.ShapeUtilities;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.opengis.geometry.BoundingBox;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.CoordinateSystemAxis;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiLineString;
import com.vividsolutions.jts.geom.Polygon;


/**
 * JTS Geometry utility methods, bringing Geotools to JTS.
 * <p>
 * Offers geotools based services such as reprojection.
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>transformation</li>
 *   <li>coordinate sequence editing</li>
 *   <li>common coordinate sequence implementations for specific uses</li>
 * </ul>
 *
 * @since 2.2
 *
 * @source $URL$
 * @version $Id$
 * @author Jody Garnett
 * @author Martin Desruisseaux
 * @author Simone Giannecchini, GeoSolutions.
 */
public final class JTS {
    /**
     * A pool of direct positions for use in {@link #orthodromicDistance}.
     */
    private static final GeneralDirectPosition[] POSITIONS = new GeneralDirectPosition[4];

    static {
        for (int i = 0; i < POSITIONS.length; i++) {
            POSITIONS[i] = new GeneralDirectPosition(i);
        }
    }

    /**
     * Geodetic calculators already created for a given coordinate reference system.
     * For use in {@link #orthodromicDistance}.
     *
     * Note: We would like to use {@link org.geotools.util.CanonicalSet}, but we can't because
     *       {@link GeodeticCalculator} keep a reference to the CRS which is used as the key.
     */
    private static final Map<CoordinateReferenceSystem,GeodeticCalculator> CALCULATORS = new HashMap<CoordinateReferenceSystem,GeodeticCalculator>();

    /**
     * Do not allow instantiation of this class.
     */
    private JTS() {
    }

    /**
     * Makes sure that an argument is non-null.
     *
     * @param  name   Argument name.
     * @param  object User argument.
     * @throws IllegalArgumentException if {@code object} is null.
     */
    private static void ensureNonNull(final String name, final Object object)
        throws IllegalArgumentException {
        if (object == null) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.NULL_ARGUMENT_$1, name));
        }
    }

    /**
     * Transforms the envelope using the specified math transform.
     * Note that this method can not handle the case where the envelope contains the North or
     * South pole, or when it cross the &plusmn;180ï¿½ longitude, because {@linkplain MathTransform
     * math transforms} do not carry suffisient informations. For a more robust envelope
     * transformation, use {@link ReferencedEnvelope#transform(CoordinateReferenceSystem,
     * boolean)} instead.
     *
     * @param  envelope  The envelope to transform.
     * @param  transform The transform to use.
     * @return The transformed Envelope
     * @throws TransformException if at least one coordinate can't be transformed.
     */
    public static Envelope transform(final Envelope envelope, final MathTransform transform)
        throws TransformException {
        return transform(envelope, null, transform, 5);
    }

    /**
     * Transforms the densified envelope using the specified math transform.
     * The envelope is densified (extra points put around the outside edge)
     * to provide a better new envelope for high deformed situations.
     * <p>
     * If an optional target envelope is provided, this envelope will be
     * {@linkplain Envelope#expandToInclude expanded} with the transformation result. It will
     * <strong>not</strong> be {@linkplain Envelope#setToNull nullified} before the expansion.
     * <p>
     * Note that this method can not handle the case where the envelope contains the North or
     * South pole, or when it cross the &plusmn;180ï¿½ longitude, because {@linkplain MathTransform
     * math transforms} do not carry suffisient informations. For a more robust envelope
     * transformation, use {@link ReferencedEnvelope#transform(CoordinateReferenceSystem,
     * boolean, int)} instead.
     *
     * @param  sourceEnvelope  The envelope to transform.
     * @param  targetEnvelope  An envelope to expand with the transformation result, or {@code null}
     *                         for returning an new envelope.
     * @param  transform       The transform to use.
     * @param  npoints         Densification of each side of the rectange.
     * @return {@code targetEnvelope} if it was non-null, or a new envelope otherwise.
     *         In all case, the returned envelope fully contains the transformed envelope.
     * @throws TransformException if a coordinate can't be transformed.
     */
    public static Envelope transform(final Envelope sourceEnvelope, Envelope targetEnvelope,
        final MathTransform transform, int npoints) throws TransformException {
        ensureNonNull("sourceEnvelope", sourceEnvelope);
        ensureNonNull("transform", transform);

        if ((transform.getSourceDimensions() != 2) || (transform.getTargetDimensions() != 2)) {
            throw new MismatchedDimensionException(Errors.format(ErrorKeys.BAD_TRANSFORM_$1,
            		Classes.getShortClassName(transform)));
        }

        npoints++; // for the starting point.

        final double[] coordinates = new double[(4 * npoints) * 2];
        final double xmin = sourceEnvelope.getMinX();
        final double xmax = sourceEnvelope.getMaxX();
        final double ymin = sourceEnvelope.getMinY();
        final double ymax = sourceEnvelope.getMaxY();
        final double scaleX = (xmax - xmin) / npoints;
        final double scaleY = (ymax - ymin) / npoints;

        int offset = 0;

        for (int t = 0; t < npoints; t++) {
            final double dx = scaleX * t;
            final double dy = scaleY * t;
            coordinates[offset++] = xmin; // Left side, increasing toward top.
            coordinates[offset++] = ymin + dy;
            coordinates[offset++] = xmin + dx; // Top side, increasing toward right.
            coordinates[offset++] = ymax;
            coordinates[offset++] = xmax; // Right side, increasing toward bottom.
            coordinates[offset++] = ymax - dy;
            coordinates[offset++] = xmax - dx; // Bottom side, increasing toward left.
            coordinates[offset++] = ymin;
        }
        assert offset == coordinates.length;
        xform(transform, coordinates, coordinates);

        // Now find the min/max of the result
        if (targetEnvelope == null) {
            targetEnvelope = new Envelope();
        }

        for (int t = 0; t < offset;) {
            targetEnvelope.expandToInclude(coordinates[t++], coordinates[t++]);
        }

        return targetEnvelope;
    }

    /**
     * Transforms the geometry using the default transformer.
     *
     * @param  geom The geom to transform
     * @param  transform the transform to use during the transformation.
     * @return the transformed geometry.  It will be a new geometry.
     * @throws MismatchedDimensionException if the geometry doesn't have the expected dimension
     *         for the specified transform.
     * @throws TransformException if a point can't be transformed.
     */
    public static Geometry transform(final Geometry geom, final MathTransform transform)
        throws MismatchedDimensionException, TransformException {
        final GeometryCoordinateSequenceTransformer transformer = new GeometryCoordinateSequenceTransformer();
        transformer.setMathTransform(transform);

        return transformer.transform(geom);
    }

    /**
     * Transforms the coordinate using the provided math transform.
     *
     * @param source the source coordinate that will be transformed
     * @param dest the coordinate that will be set.  May be null or the source coordinate
     *        (or new coordinate of course).
     * @return the destination coordinate if not null or a new Coordinate.
     * @throws TransformException if the coordinate can't be transformed.
     */
    public static Coordinate transform(final Coordinate source, Coordinate dest,
        final MathTransform transform) throws TransformException {
        ensureNonNull("source", source);
        ensureNonNull("transform", transform);

        if (dest == null) {
            dest = new Coordinate();
        }

        final double[] array = new double[transform.getSourceDimensions()];
        copy(source, array);
        transform.transform(array, 0, array, 0, 1);

        switch (transform.getTargetDimensions()) {
        case 3:
            dest.z = array[2]; // Fall through

        case 2:
            dest.y = array[1]; // Fall through

        case 1:
            dest.x = array[0]; // Fall through

        case 0:
            break;
        }

        return dest;
    }

    /**
     * Transforms the envelope from its current crs to WGS84 coordinate reference system.
     * If the specified envelope is already in WGS84, then it is returned unchanged.
     *
     * @param envelope The envelope to transform.
     * @param crs The CRS the envelope is currently in.
     * @return The envelope transformed to be in WGS84 CRS.
     * @throws TransformException If at least one coordinate can't be transformed.
     */
    public static Envelope toGeographic(final Envelope envelope, final CoordinateReferenceSystem crs)
        throws TransformException {
        if (CRS.equalsIgnoreMetadata(crs, DefaultGeographicCRS.WGS84)) {
            return envelope;
        }

        final MathTransform transform;

        try {
            transform = CRS.findMathTransform(crs, DefaultGeographicCRS.WGS84, true);
        } catch (FactoryException exception) {
            throw new TransformPathNotFoundException(Errors.format(
                    ErrorKeys.CANT_TRANSFORM_ENVELOPE, exception));
        }

        return transform(envelope, transform);
    }

    /**
     * Like a transform but eXtreme!
     *
     * Transforms an array of coordinates using the provided math transform.
     * Each coordinate is transformed separately. In case of a transform exception then
     * the new value of the coordinate is the last coordinate correctly transformed.
     *
     * @param transform The math transform to apply.
     * @param src       The source coordinates.
     * @param dest      The destination array for transformed coordinates.
     * @throws TransformException if this method failed to transform any of the points.
     */
    public static void xform(final MathTransform transform, final double[] src, final double[] dest)
        throws TransformException {
        ensureNonNull("transform", transform);

        final int sourceDim = transform.getSourceDimensions();
        final int targetDim = transform.getTargetDimensions();

        if (targetDim != sourceDim) {
            throw new MismatchedDimensionException();
        }

        TransformException firstError = null;
        boolean startPointTransformed = false;

        for (int i = 0; i < src.length; i += sourceDim) {
            try {
                transform.transform(src, i, dest, i, 1);

                if (!startPointTransformed) {
                    startPointTransformed = true;

                    for (int j = 0; j < i; j++) {
                        System.arraycopy(dest, j, dest, i, targetDim);
                    }
                }
            } catch (TransformException e) {
                if (firstError == null) {
                    firstError = e;
                }

                if (startPointTransformed) {
                    System.arraycopy(dest, i - targetDim, dest, i, targetDim);
                }
            }
        }

        if (!startPointTransformed && (firstError != null)) {
            throw firstError;
        }
    }

    /**
     * Computes the orthodromic distance between two points. This method:
     * <p>
     * <ol>
     *   <li>Transforms both points to geographic coordinates
     *       (<var>latitude</var>,<var>longitude</var>).</li>
     *   <li>Computes the orthodromic distance between the two points using ellipsoidal
     *       calculations.</li>
     * </ol>
     * <p>
     * The real work is performed by {@link GeodeticCalculator}. This convenience method simply
     * manages a pool of pre-defined geodetic calculators for the given coordinate reference system
     * in order to avoid repetitive object creation. If a large amount of orthodromic distances
     * need to be computed, direct use of {@link GeodeticCalculator} provides better performance
     * than this convenience method.
     *
     * @param p1  First point
     * @param p2  Second point
     * @param crs Reference system the two points are in.
     * @return    Orthodromic distance between the two points, in meters.
     * @throws    TransformException if the coordinates can't be transformed from the specified
     *            CRS to a {@linkplain org.opengis.referencing.crs.GeographicCRS geographic CRS}.
     */
    public static synchronized double orthodromicDistance(final Coordinate p1, final Coordinate p2,
        final CoordinateReferenceSystem crs) throws TransformException {
        ensureNonNull("p1", p1);
        ensureNonNull("p2", p2);
        ensureNonNull("crs", crs);

        /*
         * Need to synchronize because we use a single instance of a Map (CALCULATORS) as well as
         * shared instances of GeodeticCalculator and GeneralDirectPosition (POSITIONS). None of
         * them are thread-safe.
         */
        GeodeticCalculator gc = (GeodeticCalculator) CALCULATORS.get(crs);

        if (gc == null) {
            gc = new GeodeticCalculator(crs);
            CALCULATORS.put(crs, gc);
        }
        assert crs.equals(gc.getCoordinateReferenceSystem()) : crs;

        final GeneralDirectPosition pos = POSITIONS[Math.min(POSITIONS.length - 1,
                crs.getCoordinateSystem().getDimension())];
        pos.setCoordinateReferenceSystem(crs);
        copy(p1, pos.ordinates);
        gc.setStartingPosition(pos);
        copy(p2, pos.ordinates);
        gc.setDestinationPosition(pos);

        return gc.getOrthodromicDistance();
    }

    /**
     * Copies the ordinates values from the specified JTS coordinates to the specified array. The
     * destination array can have any length. Only the relevant field of the source coordinate will
     * be copied. If the array length is greater than 3, then all extra dimensions will be set to
     * {@link Double#NaN NaN}.
     *
     * @param point The source coordinate.
     * @param ordinates The destination array.
     */
    public static void copy(final Coordinate point, final double[] ordinates) {
        ensureNonNull("point", point);
        ensureNonNull("ordinates", ordinates);

        switch (ordinates.length) {
        default:
            Arrays.fill(ordinates, 3, ordinates.length, Double.NaN); // Fall through

        case 3:
            ordinates[2] = point.z; // Fall through

        case 2:
            ordinates[1] = point.y; // Fall through

        case 1:
            ordinates[0] = point.x; // Fall through

        case 0:
            break;
        }
    }

    /**
     * Converts an arbitrary Java2D shape into a JTS geometry. The created JTS geometry
     * may be any of {@link LineString}, {@link LinearRing} or {@link MultiLineString}.
     *
     * @param  shape    The Java2D shape to create.
     * @param  factory  The JTS factory to use for creating geometry.
     * @return The JTS geometry.
     */
    public static Geometry shapeToGeometry(final Shape shape, final GeometryFactory factory) {
        ensureNonNull("shape", shape);
        ensureNonNull("factory", factory);

        final PathIterator iterator = shape.getPathIterator(null, ShapeUtilities.getFlatness(shape));
        final double[] buffer = new double[6];
        final List<Coordinate> coords = new ArrayList<Coordinate>();
        final List<LineString> lines = new ArrayList<LineString>();

        while (!iterator.isDone()) {
            switch (iterator.currentSegment(buffer)) {
            /*
             * Close the polygon: the last point is equal to
             * the first point, and a LinearRing is created.
             */
            case PathIterator.SEG_CLOSE: {
                if (!coords.isEmpty()) {
                    coords.add( coords.get(0) );
                    lines.add(factory.createLinearRing(
                            (Coordinate[]) coords.toArray(new Coordinate[coords.size()])));
                    coords.clear();
                }
                break;
            }

            /*
             * General case: A LineString is created from previous
             * points, and a new LineString begin for next points.
             */
            case PathIterator.SEG_MOVETO: {
                if (!coords.isEmpty()) {
                    lines.add(factory.createLineString(
                            (Coordinate[]) coords.toArray(new Coordinate[coords.size()])));
                    coords.clear();
                }

                // Fall through
            }

            case PathIterator.SEG_LINETO: {
                coords.add(new Coordinate(buffer[0], buffer[1]));

                break;
            }

            default:
                throw new IllegalPathStateException();
            }

            iterator.next();
        }

        /*
         * End of loops: create the last LineString if any, then create the MultiLineString.
         */
        if (!coords.isEmpty()) {
            lines.add(factory.createLineString(
                    (Coordinate[]) coords.toArray(new Coordinate[coords.size()])));
        }

        switch (lines.size()) {
        case 0:
            return null;

        case 1:
            return (LineString) lines.get(0);

        default:
            return factory.createMultiLineString(GeometryFactory.toLineStringArray(lines));
        }
    }

    /**
     * Converts a JTS 2D envelope in an {@link Envelope2D} for interoperability with the
     * referencing package.
     * <p>
     * If the provided envelope is a {@link ReferencedEnvelope} we check
     * that the provided CRS and the implicit CRS are similar.
     *
     * @param envelope The JTS envelope to convert.
     * @param crs The coordinate reference system for the specified envelope.
     * @return The GeoAPI envelope.
     * @throws MismatchedDimensionException if a two-dimensional envelope can't be created
     *         from an envelope with the specified CRS.
     *
     * @since 2.3
     */
    public static Envelope2D getEnvelope2D(final Envelope envelope,
        final CoordinateReferenceSystem crs) throws MismatchedDimensionException {
        // Initial checks
        ensureNonNull("envelope", envelope);
        ensureNonNull("crs", crs);

        if (envelope instanceof ReferencedEnvelope) {
            final ReferencedEnvelope referenced = (ReferencedEnvelope) envelope;
            final CoordinateReferenceSystem implicitCRS = referenced.getCoordinateReferenceSystem();

            if ((crs != null) && !CRS.equalsIgnoreMetadata(crs, implicitCRS)) {
                throw new IllegalArgumentException(Errors.format(
                        ErrorKeys.MISMATCHED_ENVELOPE_CRS_$2, crs.getName().getCode(),
                        implicitCRS.getName().getCode()));
            }
        }

        // Ensure the CRS is 2D and retrieve the new envelope
        final CoordinateReferenceSystem crs2D = CRS.getHorizontalCRS(crs);
		if(crs2D==null)
			throw new MismatchedDimensionException(
					Errors.format(
		                    ErrorKeys.CANT_SEPARATE_CRS_$1,crs));


        return new Envelope2D(crs2D, envelope.getMinX(), envelope.getMinY(), envelope.getWidth(),
            envelope.getHeight());
    }

    /**
     * Converts an envelope to a polygon.
     * <p>
     * The resulting polygon contains an outer ring with verticies:
     * (x1,y1),(x2,y1),(x2,y2),(x1,y2),(x1,y1)
     *
     * @param envelope The original envelope.
     * @return The envelope as a polygon.
     *
     * @since 2.4
     */
    public static Polygon toGeometry(Envelope e) {
        GeometryFactory gf = new GeometryFactory();

        return gf.createPolygon(gf.createLinearRing(
                new Coordinate[] {
                    new Coordinate(e.getMinX(), e.getMinY()),
                    new Coordinate(e.getMaxX(), e.getMinY()),
                    new Coordinate(e.getMaxX(), e.getMaxY()),
                    new Coordinate(e.getMinX(), e.getMaxY()),
                    new Coordinate(e.getMinX(), e.getMinY())
                }), null);
    }

    /**
     * Create a ReferencedEnvelope from the provided geometry, we will
     * do our best to guess the CoordinateReferenceSystem making use
     * of getUserData() and getSRID() as needed.
     * 
     * @param geom Provided Geometry
     * @return RefernecedEnveleope describing the bounds of the provided Geometry
     */
    public static ReferencedEnvelope toEnvelope( Geometry geom ){
        if( geom == null ){
            return null; //return new ReferencedEnvelope(); // very empty!
        }
        String srsName = null;
        Object userData = geom.getUserData();
        if( userData != null && userData instanceof String){
            srsName = (String) userData;    
        }
        else if (geom.getSRID() > 0){
            srsName = "EPSG:"+geom.getSRID();                                
        }
        CoordinateReferenceSystem crs = null;
        if( userData != null && userData instanceof CoordinateReferenceSystem){
            crs = (CoordinateReferenceSystem) userData;
        }
        else if( srsName != null ){
            try {
                crs = CRS.decode( srsName );
            } catch (NoSuchAuthorityCodeException e) {
                // e.printStackTrace();
            } catch (FactoryException e) {
                // e.printStackTrace();
            }
        }
        return new ReferencedEnvelope( geom.getEnvelopeInternal(), crs );
    }
    /**
     * Converts a {@link BoundingBox} to a polygon.
     * <p>
     * The resulting polygon contains an outer ring with verticies:
     * (x1,y1),(x2,y1),(x2,y2),(x1,y2),(x1,y1)
     *
     * @param envelope The original envelope.
     * @return The envelope as a polygon.
     *
     * @since 2.4
     */
    public static Polygon toGeometry(BoundingBox e) {
        GeometryFactory gf = new GeometryFactory();

        return gf.createPolygon(gf.createLinearRing(
                new Coordinate[] {
                    new Coordinate(e.getMinX(), e.getMinY()),
                    new Coordinate(e.getMaxX(), e.getMinY()),
                    new Coordinate(e.getMaxX(), e.getMaxY()),
                    new Coordinate(e.getMinX(), e.getMaxY()),
                    new Coordinate(e.getMinX(), e.getMinY())
                }), null);
    }

    /**
     * Checks a Geometry coordinates are within the area of validity of the
     * specified reference system. If a coordinate falls outside the area of
     * validity a {@link PointOutsideEnvelopeException} is thrown
     *
     * @param geom
     *            the geometry to check
     * @param the
     *            crs that defines the are of validity (must not be null)
     * @throws PointOutsideEnvelopeException
     * @since 2.4
     */
    public static void checkCoordinatesRange(Geometry geom, CoordinateReferenceSystem crs)
        throws PointOutsideEnvelopeException {
        // named x,y, but could be anything
        CoordinateSystemAxis x = crs.getCoordinateSystem().getAxis(0);
        CoordinateSystemAxis y = crs.getCoordinateSystem().getAxis(1);

        // check if unbounded, many projected systems are, in this case no check
        // is needed
        boolean xUnbounded = Double.isInfinite(x.getMinimumValue())
            && Double.isInfinite(x.getMaximumValue());
        boolean yUnbounded = Double.isInfinite(y.getMinimumValue())
            && Double.isInfinite(y.getMaximumValue());

        if (xUnbounded && yUnbounded) {
            return;
        }

        // check each coordinate
        Coordinate[] c = geom.getCoordinates();

        for (int i = 0; i < c.length; i++) {
            if (!xUnbounded && ((c[i].x < x.getMinimumValue()) || (c[i].x > x.getMaximumValue()))) {
                throw new PointOutsideEnvelopeException(c[i].x + " outside of ("
                    + x.getMinimumValue() + "," + x.getMaximumValue() + ")");
            }

            if (!yUnbounded && ((c[i].y < y.getMinimumValue()) || (c[i].y > y.getMaximumValue()))) {
                throw new PointOutsideEnvelopeException(c[i].y + " outside of ("
                    + y.getMinimumValue() + "," + y.getMaximumValue() + ")");
            }
        }
    }
}
