/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.crs;

import static java.lang.String.format;
import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import org.geotools.api.metadata.extent.GeographicBoundingBox;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.api.referencing.crs.SingleCRS;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.GeocentricTransform;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.prep.PreparedGeometry;
import org.locationtech.jts.geom.prep.PreparedGeometryFactory;
import org.locationtech.jts.precision.EnhancedPrecisionOp;
import org.locationtech.jts.precision.GeometryPrecisionReducer;

/**
 * A class that can perform transformations on geometries to handle the singularity of the rendering CRS, deal with
 * geometries that are crossing the dateline, and eventually wrap them around to produce a seamless continuous map
 * effect.
 *
 * <p>This basic implementation will cut the geometries that get outside of the area of validity of the projection (as
 * provided by the constructor)
 *
 * <p>WARNING: this API is not finalized and is meant to be used by StreamingRenderer only
 *
 * @author Andrea Aime - OpenGeo
 */
public class ProjectionHandler {

    public static final String ADVANCED_PROJECTION_DENSIFY = "advancedProjectionDensify";

    protected static final double EPS = 1e-6;

    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ProjectionHandler.class);

    /**
     * The default densification limit. A {@link org.locationtech.jts.geom.Coordinate} object is 40 bytes, this number
     * allows the allocation of at most 10MB worth of Coordinate objects
     */
    public static final int DEFAULT_DENSIFICATION_LIMIT = 262144;

    /** System variable that can be used to override the default densification limit. */
    public static final String DENSIFICATION_LIMIT_KEY = "org.geotools.render.densificationLimit";

    /** The maximum number of coordinates to densify a geometry to. */
    private static final int DENSIFICATION_LIMIT = initDensificationLimit();

    protected CRS.AxisOrder sourceAxisOrder;
    protected CRS.AxisOrder targetAxisOrder;

    /** Parsing and validating the densification limit. */
    private static int initDensificationLimit() {
        String property = System.getProperty(DENSIFICATION_LIMIT_KEY);
        if (property != null) {
            // parse and validate, prepare error message to re-use its setup
            String errorMessage = format(
                    "Invalid value for %s, it should be a positive integer greater than 2. Will use the default value.",
                    DENSIFICATION_LIMIT_KEY);
            try {
                int limit = Integer.parseInt(property);
                if (limit > 2) {
                    return limit;
                }
                LOGGER.severe(errorMessage);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.SEVERE, errorMessage, e);
            }
        }
        return DEFAULT_DENSIFICATION_LIMIT;
    }

    protected ReferencedEnvelope renderingEnvelope;

    protected final ReferencedEnvelope validAreaBounds;

    protected final Geometry validArea;

    protected final PreparedGeometry validaAreaTester;

    protected final CoordinateReferenceSystem sourceCRS;

    protected final CoordinateReferenceSystem targetCRS;

    protected double datelineX = Double.NaN;

    protected double targetHalfCircle = Double.NaN;

    protected boolean queryAcrossDateline;

    protected SingleCRS geometryCRS;

    protected boolean noReprojection;

    protected double densify = 0.0;

    Map<String, Object> projectionParameters;

    /**
     * Initializes a projection handler
     *
     * @param sourceCRS The source CRS
     * @param validAreaBounds The valid area (used to cut geometries that go beyond it)
     * @param renderingEnvelope The target rendering area and target CRS
     */
    public ProjectionHandler(
            CoordinateReferenceSystem sourceCRS, Envelope validAreaBounds, ReferencedEnvelope renderingEnvelope)
            throws FactoryException {
        this.renderingEnvelope = renderingEnvelope;
        this.sourceCRS = CRS.getHorizontalCRS(sourceCRS);
        this.targetCRS = renderingEnvelope.getCoordinateReferenceSystem();
        this.validAreaBounds =
                validAreaBounds != null ? new ReferencedEnvelope(validAreaBounds, DefaultGeographicCRS.WGS84) : null;
        this.validArea = null;
        this.validaAreaTester = null;
        checkReprojection();
    }

    /**
     * Initializes a projection handler
     *
     * @param sourceCRS The source CRS
     * @param validArea The valid area (used to cut geometries that go beyond it)
     * @param renderingEnvelope The target rendering area and target CRS
     */
    public ProjectionHandler(
            CoordinateReferenceSystem sourceCRS, Geometry validArea, ReferencedEnvelope renderingEnvelope)
            throws FactoryException {
        this(sourceCRS, validArea, renderingEnvelope, false);
    }

    /**
     * Initializes a projection handler
     *
     * @param sourceCRS The source CRS
     * @param validArea The valid area (used to cut geometries that go beyond it)
     * @param renderingEnvelope The target rendering area and target CRS
     * @param keepGeometry Can be used to force the valid area to be treated as a polygon, even when it's a rectangle
     */
    public ProjectionHandler(
            CoordinateReferenceSystem sourceCRS,
            Geometry validArea,
            ReferencedEnvelope renderingEnvelope,
            boolean keepGeometry)
            throws FactoryException {
        if (validArea.isRectangle() && !keepGeometry) {
            this.renderingEnvelope = renderingEnvelope;
            this.sourceCRS = sourceCRS;
            this.targetCRS = renderingEnvelope.getCoordinateReferenceSystem();
            this.validAreaBounds = new ReferencedEnvelope(validArea.getEnvelopeInternal(), DefaultGeographicCRS.WGS84);
            this.validArea = null;
            this.validaAreaTester = null;
        } else {
            this.renderingEnvelope = renderingEnvelope;
            this.sourceCRS = sourceCRS;
            this.targetCRS = renderingEnvelope.getCoordinateReferenceSystem();
            this.validAreaBounds = new ReferencedEnvelope(validArea.getEnvelopeInternal(), DefaultGeographicCRS.WGS84);
            this.validArea = validArea;
            this.validaAreaTester = PreparedGeometryFactory.prepare(validArea);
        }
        checkReprojection();
    }

    /**
     * Set one of the supported projection parameters: - advancedProjectionDensify (double) if > 0 enables densification
     * on preprocessing with the given distance between points.
     */
    public void setProjectionParameters(Map<String, Object> projectionParameters) {
        if (projectionParameters.containsKey(ADVANCED_PROJECTION_DENSIFY)) {
            densify = (Double) projectionParameters.get(ADVANCED_PROJECTION_DENSIFY);
        }
        this.projectionParameters = projectionParameters;
    }

    private void checkReprojection() throws FactoryException {
        geometryCRS = CRS.getHorizontalCRS(sourceCRS);
        CoordinateReferenceSystem renderingCRS = renderingEnvelope.getCoordinateReferenceSystem();
        try {
            noReprojection = !CRS.isTransformationRequired(geometryCRS, renderingCRS);
        } catch (Exception e) {
            LOGGER.log(Level.FINE, "Failed to determine is reprojection is required, assumming it is", e);
            noReprojection = false;
        }
        // query across dateline only in case of reprojection, Oracle won't use the spatial index
        // with two or-ed bboxes and fixing the issue at the store level requires more
        // time/resources than we presently have
        this.queryAcrossDateline =
                !CRS.equalsIgnoreMetadata(sourceCRS, renderingEnvelope.getCoordinateReferenceSystem());

        this.sourceAxisOrder = CRS.getAxisOrder(sourceCRS);
        this.targetAxisOrder = CRS.getAxisOrder(targetCRS);
    }

    /** Returns the current rendering envelope */
    public ReferencedEnvelope getRenderingEnvelope() {
        return renderingEnvelope;
    }

    public CoordinateReferenceSystem getSourceCRS() {
        return this.sourceCRS;
    }

    /**
     * Returns a set of envelopes that will be used to query the data given the specified rendering envelope and the
     * current query envelope
     */
    public List<ReferencedEnvelope> getQueryEnvelopes() throws TransformException, FactoryException {
        CoordinateReferenceSystem renderingCRS = renderingEnvelope.getCoordinateReferenceSystem();
        if (!queryAcrossDateline) {
            ReferencedEnvelope envelope = transformEnvelope(renderingEnvelope, sourceCRS);
            if (envelope == null) return Collections.emptyList();
            return Collections.singletonList(envelope);
        }
        if (renderingCRS instanceof GeographicCRS && !CRS.equalsIgnoreMetadata(renderingCRS, WGS84)) {
            // special case, if we just transform the coordinates are going to be wrapped by the
            // referencing
            // subsystem directly
            ReferencedEnvelope re = renderingEnvelope;
            List<ReferencedEnvelope> envelopes = new ArrayList<>();
            addTransformedEnvelope(re, envelopes);

            if (targetAxisOrder == CRS.AxisOrder.NORTH_EAST) {
                if (re.getMinY() >= -180.0 && re.getMaxY() <= 180) {
                    return envelopes;
                }
                // We need to split reprojected envelope and normalize it. To be lenient with
                // situations in which the data is just broken (people saying 4326 just because they
                // have no idea at all) we don't actually split, but add elements
                adjustEnvelope(re, envelopes, true);
            } else {
                if (re.getMinX() >= -180.0 && re.getMaxX() <= 180) {
                    ReferencedEnvelope envelope = transformEnvelope(renderingEnvelope, sourceCRS);
                    if (envelope == null) return Collections.emptyList();
                    return Collections.singletonList(envelope);
                }
                // We need to split reprojected envelope and normalize it. To be lenient with
                // situations in which the data is just broken (people saying 4326 just because they
                // have no idea at all) we don't actually split, but add elements
                adjustEnvelope(re, envelopes, true);
            }
            mergeEnvelopes(envelopes);
            return envelopes;
        } else {
            if (!Double.isNaN(datelineX)
                    && renderingEnvelope.getMinX() < datelineX
                    && renderingEnvelope.getMaxX() > datelineX
                    && renderingEnvelope.getWidth() < targetHalfCircle) {
                double minX = renderingEnvelope.getMinX();
                double minY = renderingEnvelope.getMinY();
                double maxX = renderingEnvelope.getMaxX();
                double maxY = renderingEnvelope.getMaxY();
                ReferencedEnvelope re1 = new ReferencedEnvelope(minX, datelineX - EPS, minY, maxY, renderingCRS);
                List<ReferencedEnvelope> result = new ArrayList<>();
                ReferencedEnvelope tx1 = transformEnvelope(re1, WGS84);
                if (tx1 != null) {
                    tx1.expandToInclude(180, tx1.getMinY());
                    addTransformedEnvelope(tx1, result);
                }
                ReferencedEnvelope re2 = new ReferencedEnvelope(datelineX + EPS, maxX, minY, maxY, renderingCRS);
                ReferencedEnvelope tx2 = transformEnvelope(re2, WGS84);
                if (tx2 != null) {
                    if (tx2.getMinX() > 180) {
                        tx2.translate(-360, 0);
                    }
                    tx2.expandToInclude(-180, tx1.getMinY());
                    addTransformedEnvelope(tx2, result);
                }

                mergeEnvelopes(result);
                return result;
            } else {
                return getSourceEnvelopes(renderingEnvelope);
            }
        }
    }

    private void addTransformedEnvelope(ReferencedEnvelope re, List<ReferencedEnvelope> envelopes)
            throws TransformException, FactoryException {
        ReferencedEnvelope transformed = transformEnvelope(re, sourceCRS);
        if (transformed != null) {
            envelopes.add(transformed);
        }
    }

    protected List<ReferencedEnvelope> getSourceEnvelopes(ReferencedEnvelope renderingEnvelope)
            throws TransformException, FactoryException {
        // check if we are crossing the dateline
        ReferencedEnvelope re = transformEnvelope(renderingEnvelope, WGS84);
        if (re == null) {
            return Collections.emptyList();
        }
        if (re.getMinX() >= -180.0 && re.getMaxX() <= 180) {
            final ReferencedEnvelope result = transformEnvelope(renderingEnvelope, sourceCRS);
            if (result != null) {
                return Collections.singletonList(result);
            } else {
                return Collections.emptyList();
            }
        }
        // We need to split reprojected envelope and normalize it. To be lenient with
        // situations in which the data is just broken (people saying 4326 just because they
        // have no idea at all) we don't actually split, but add elements
        List<ReferencedEnvelope> envelopes = new ArrayList<>();
        envelopes.add(re);
        adjustEnvelope(re, envelopes, false);
        mergeEnvelopes(envelopes);
        reprojectEnvelopes(sourceCRS, envelopes);
        return envelopes.stream().filter(e -> e != null).collect(Collectors.toList());
    }

    /**
     * Adjust the envelope by taking into account dateline wrapping as well as multiple spans of the whole world extent.
     * When transform flag is true, the envelopes will be transformed before being returned
     */
    private void adjustEnvelope(ReferencedEnvelope re, List<ReferencedEnvelope> envelopes, boolean transform)
            throws TransformException, FactoryException {
        CoordinateReferenceSystem crs = re.getCoordinateReferenceSystem();
        boolean isLatLon = CRS.getAxisOrder(crs) == CRS.AxisOrder.NORTH_EAST;
        double minX = isLatLon ? re.getMinY() : re.getMinX();
        double maxX = isLatLon ? re.getMaxY() : re.getMaxX();
        double minY = isLatLon ? re.getMinX() : re.getMinY();
        double maxY = isLatLon ? re.getMaxX() : re.getMaxY();
        double extent = maxX - minX;
        List<ReferencedEnvelope> envelopesToBeAdded = new ArrayList<>();
        if (extent > 360) {
            // at least one whole world use case -> requested data covers the full world:
            // let's set a -180,180 bbox.
            // the wrapping projectionHandler and the gridCoverageReaders
            // will do proper clones / intersections afterwards
            minX = -180;
            maxX = 180;
            // Create a whole world envelope taking into account latLon/lonLat
            ReferencedEnvelope envelope = new ReferencedEnvelope(
                    isLatLon ? minY : minX,
                    isLatLon ? maxY : maxX,
                    isLatLon ? minX : minY,
                    isLatLon ? maxX : maxY,
                    crs);
            envelopesToBeAdded.add(envelope);
        } else {
            // Note that the extent won't be > 360 at this point
            // let's do some adjustments to "shift" the request around -180, 180 interval:
            // we basically add or subtract 360° N times
            // 1) let's count how many halfCircles (a 180° span) we are away from the zero
            // Using half circles allow to understand if there is a dateline cross

            // 2) add/subtract 360° N times to move forward/backward the request, also
            // keeping into account whether we are crossing the dateline or not,
            // by using (halfCircles % 2).
            // An odd number of halfCircles means dateline crossing.
            // An even number of halfCircles means no dateline crossing.
            // i.e. minX = 371 -> halfCircles = 2 -> no dateline crossing (371° = 11°)
            // i.e. minX = 908 -> halfCircles = 5 -> dateline crossing (908° = 188°)

            // 3) add/subtract the original extent to get the other value of the interval
            // in order to move the whole window (Note that the extent won't be > 360°
            // since we are inside the "else")

            int halfCircles = 0;
            if (minX < -180) {
                halfCircles = (int) (Math.abs(minX) / 180);
                minX += (360 * ((halfCircles / 2) + (halfCircles % 2)));
                maxX = minX + extent;
            } else if (minX > 180) {
                halfCircles = (int) (minX / 180);
                minX -= (360 * ((halfCircles / 2) + (halfCircles % 2)));
                maxX = minX + extent;
            } else if (maxX < -180) {
                halfCircles = (int) (Math.abs(maxX) / 180);
                maxX += (360 * ((halfCircles / 2) + (halfCircles % 2)));
                minX = maxX - extent;
            } else if (maxX > 180) {
                halfCircles = (int) (Math.abs(maxX) / 180);
                maxX -= (360 * ((halfCircles / 2) + (halfCircles % 2)));
                minX = maxX - extent;
            }

            if ((int) (minX / 180) < (int) (maxX / 180)) {
                // Dateline crossing check.
                // Examples of [min, max] and how this IF will work
                // a case like [-91, 91] will be 0 < 0 -> False: no Dateline cross
                // a case like [-1, 181] will be 0 < 1 -> True: Dateline cross.
                // a case like [-181, 1] will be -1 < 0 -> True: Dateline cross.

                // Need to use 2 separate envelopes when crossing the dateline.
                // Let's prepare 8 coordinates, 4 for each side of the dateline
                // (left and right), keeping also into account latLon vs lonLat
                double coord1L, coord2L, coord3L, coord4L;
                double coord1R, coord2R, coord3R, coord4R;
                if (minX < -180) {
                    coord1L = isLatLon ? minY : -180;
                    coord2L = isLatLon ? maxY : Math.min(maxX, 180);
                    coord3L = isLatLon ? -180 : minY;
                    coord4L = isLatLon ? Math.min(maxX, 180) : maxY;

                    coord1R = isLatLon ? minY : minX + 360;
                    coord2R = isLatLon ? maxY : 180;
                    coord3R = isLatLon ? minX + 360 : minY;
                    coord4R = isLatLon ? 180 : maxY;
                } else {
                    // maxX will be greater than 180 since we crossed the dateline
                    // so we need to put it back of a -360 factor
                    coord1L = isLatLon ? minY : -180;
                    coord2L = isLatLon ? maxY : maxX - 360;
                    coord3L = isLatLon ? -180 : minY;
                    coord4L = isLatLon ? maxX - 360 : maxY;

                    coord1R = isLatLon ? minY : minX;
                    coord2R = isLatLon ? maxY : 180;
                    coord3R = isLatLon ? minX : minY;
                    coord4R = isLatLon ? 180 : maxY;
                }

                envelopesToBeAdded.add(new ReferencedEnvelope(coord1L, coord2L, coord3L, coord4L, crs));
                envelopesToBeAdded.add(new ReferencedEnvelope(coord1R, coord2R, coord3R, coord4R, crs));

            } else {
                // No dateline has been crossed. One envelope would be enough
                envelopesToBeAdded.add(new ReferencedEnvelope(
                        isLatLon ? minY : minX,
                        isLatLon ? maxY : maxX,
                        isLatLon ? minX : minY,
                        isLatLon ? maxX : maxY,
                        crs));
            }
        }
        for (ReferencedEnvelope env : envelopesToBeAdded) {
            if (transform) {
                addTransformedEnvelope(env, envelopes);
            } else {
                envelopes.add(env);
            }
        }
    }

    /**
     * Reprojects the given envelope to the target CRS, taking into account the ProjectionHandler constraints (valid
     * area bounds, etc.).
     *
     * @param envelope envelope to reproject
     * @param targetCRS target CRS
     * @return reprojected envelope
     */
    public ReferencedEnvelope getProjectedEnvelope(ReferencedEnvelope envelope, CoordinateReferenceSystem targetCRS)
            throws TransformException, FactoryException {
        return transformEnvelope(envelope, targetCRS);
    }

    protected ReferencedEnvelope transformEnvelope(ReferencedEnvelope envelope, CoordinateReferenceSystem targetCRS)
            throws TransformException, FactoryException {
        if (CRS.equalsIgnoreMetadata(envelope.getCoordinateReferenceSystem(), targetCRS)) {
            return envelope;
        }
        try {
            if (validAreaBounds != null) {
                ReferencedEnvelope validAreaInTargetCRS =
                        validAreaBounds.transform(envelope.getCoordinateReferenceSystem(), true);
                envelope = envelope.intersection(validAreaInTargetCRS);
                if (envelope.isEmpty()) {
                    return null;
                }
            }

            ReferencedEnvelope transformed = envelope.transform(targetCRS, true, 10);
            ProjectionHandler handler = ProjectionHandlerFinder.getHandler(
                    new ReferencedEnvelope(targetCRS), DefaultGeographicCRS.WGS84, true, projectionParameters);
            // does the target CRS have a strict notion of what's possible in terms of
            // valid coordinate ranges?
            if (handler == null || handler instanceof WrappingProjectionHandler) {
                return transformed;
            }

            // if so, cut
            final ReferencedEnvelope validAreaBounds = handler.getValidAreaBounds();
            ReferencedEnvelope validArea = validAreaBounds.transform(targetCRS, true);
            ReferencedEnvelope reduced = transformed.intersection(validArea);
            if (reduced.isNull()) {
                return null;
            } else {
                return reduced;
            }
        } catch (Exception e) {
            LOGGER.fine("Failed to reproject the envelope "
                    + envelope
                    + " to "
                    + targetCRS
                    + " trying an area restriction");

            ReferencedEnvelope envWGS84 = envelope.transform(DefaultGeographicCRS.WGS84, true);

            // do we have restrictions on the target CRS?
            ProjectionHandler handler = ProjectionHandlerFinder.getHandler(
                    new ReferencedEnvelope(targetCRS), DefaultGeographicCRS.WGS84, false);
            if (handler != null && handler.validAreaBounds != null) {
                ReferencedEnvelope validAreaBounds = handler.validAreaBounds;
                envWGS84 = envWGS84.intersection(validAreaBounds);
            }

            // let's see if we can restrict the area we're reprojecting back using a projection
            // handler for the source CRS
            handler = ProjectionHandlerFinder.getHandler(envelope, envelope.getCoordinateReferenceSystem(), false);
            if (handler != null && handler.validAreaBounds != null) {
                ReferencedEnvelope validAreaBounds = handler.validAreaBounds;
                envWGS84 = envWGS84.intersection(validAreaBounds);
            }

            // try to reproject
            if (envWGS84.isNull()) {
                return null;
            } else {
                try {
                    return ReferencedEnvelope.reference(envWGS84).transform(targetCRS, true);
                } catch (Exception e2) {
                    LOGGER.fine("Failed to reproject the restricted envelope " + envWGS84 + " to " + targetCRS);
                }
            }

            // ok, let's see if we have an area of validity then
            GeographicBoundingBox bbox = CRS.getGeographicBoundingBox(targetCRS);
            if (bbox != null) {
                ReferencedEnvelope restriction = new ReferencedEnvelope(
                        bbox.getEastBoundLongitude(),
                        bbox.getWestBoundLongitude(),
                        bbox.getSouthBoundLatitude(),
                        bbox.getNorthBoundLatitude(),
                        DefaultGeographicCRS.WGS84);
                Envelope intersection = envWGS84.intersection(restriction);
                if (intersection.isNull()) {
                    return null;
                } else {
                    try {
                        return ReferencedEnvelope.reference(intersection).transform(targetCRS, true);
                    } catch (Exception e2) {
                        LOGGER.fine("Failed to reproject the restricted envelope " + intersection + " to " + targetCRS);
                    }
                }
            }

            throw new TransformException(
                    "All attemptsto reproject the envelope " + envelope + " to " + targetCRS + " failed");
        }
    }

    protected void reprojectEnvelopes(CoordinateReferenceSystem queryCRS, List<ReferencedEnvelope> envelopes)
            throws TransformException, FactoryException {
        // reproject the surviving envelopes
        for (int i = 0; i < envelopes.size(); i++) {
            final ReferencedEnvelope envelope = transformEnvelope(envelopes.get(i), queryCRS);
            if (envelope != null) {
                envelopes.set(i, envelope);
            }
        }
    }

    protected void mergeEnvelopes(List<ReferencedEnvelope> envelopes) {
        // the envelopes generated might overlap, check and merge if necessary, we
        // don't want the data backend to deal with ORs against the spatial index
        // unless necessary
        boolean merged = true;
        while (merged && envelopes.size() > 1) {
            merged = false;
            for (int i = 0; i < envelopes.size() - 1; i++) {
                ReferencedEnvelope curr = envelopes.get(i);
                for (int j = i + 1; j < envelopes.size(); ) {
                    ReferencedEnvelope next = envelopes.get(j);
                    if (curr.intersects((Envelope) next)) {
                        curr.expandToInclude(next);
                        envelopes.remove(j);
                        merged = true;
                    } else {
                        j++;
                    }
                }
            }
        }
    }

    /** Returns true if the geometry needs special handling */
    public boolean requiresProcessing(Geometry geometry) {
        // if there is no valid area, no cutting is required
        if (validAreaBounds == null) return false;

        // if not reprojection is going on, we don't need to cut
        if (noReprojection) {
            return false;
        }

        return true;
    }

    /**
     * Pre processes the geometry, e.g. cuts it, splits it, etc. in its native srs. May return null if the geometry is
     * not to be drawn
     */
    public Geometry preProcess(Geometry geometry) throws TransformException, FactoryException {
        // if there is no valid area, no cutting is required either
        if (validAreaBounds == null && validArea == null) return densify(geometry, true);

        // if not reprojection is going on, we don't need to cut
        if (noReprojection) {
            return densify(geometry, false);
        }

        Geometry mask;
        ReferencedEnvelope ge = new ReferencedEnvelope(geometry.getEnvelopeInternal(), geometryCRS);
        ReferencedEnvelope geWGS84 = ge.transform(WGS84, true);
        // if the size of the envelope is less than 1 meter (1e-6 in degrees) expand it a bit
        // to make intersection tests work
        geWGS84.expandBy(EPS);
        if (validArea == null) {

            // if the geometry is within the valid area for this projection
            // just skip expensive cutting
            if (validAreaBounds.contains((Envelope) geWGS84)) {
                return densify(geometry, true);
            }

            // we need to cut, first thing, we intersect the geometry envelope
            // and the valid area in WGS84, which is a neutral, everything can
            // be turned into it, and then turn back the intersection into
            // the origin SRS
            ReferencedEnvelope envIntWgs84 = new ReferencedEnvelope(validAreaBounds.intersection(geWGS84), WGS84);

            // if the intersection is empty the geometry is completely outside of the valid area,
            // skip it
            if (envIntWgs84.getHeight() <= 0 || envIntWgs84.getWidth() <= 0) {
                // valid area is crossing dateline?
                if (validAreaBounds.contains(180, (validAreaBounds.getMinY() + validAreaBounds.getMaxY()) / 2)) {
                    ReferencedEnvelope translated = new ReferencedEnvelope(validAreaBounds);
                    translated.translate(-360, 0);
                    if (translated.contains((Envelope) geWGS84)) {
                        return densify(geometry, false);
                    }
                    envIntWgs84 = translated.intersection(geWGS84);
                } else if (validAreaBounds.contains(
                        -180, (validAreaBounds.getMinY() + validAreaBounds.getMaxY()) / 2)) {
                    ReferencedEnvelope translated = new ReferencedEnvelope(validAreaBounds);
                    translated.translate(360, 0);
                    if (translated.contains((Envelope) geWGS84)) {
                        return densify(geometry, false);
                    }
                    envIntWgs84 = translated.intersection(geWGS84);
                }
                if (envIntWgs84.getHeight() <= 0 || envIntWgs84.getWidth() <= 0) {
                    return null;
                }
            }

            ReferencedEnvelope envInt = envIntWgs84.transform(geometryCRS, true);
            mask = JTS.toGeometry((Envelope) envInt);
        } else {
            // if the geometry is within the valid area for this projection
            // just skip expensive cutting
            if (validaAreaTester.contains(JTS.toGeometry(geWGS84))) {
                return densify(geometry, false);
            }

            // we need to cut, first thing, we intersect the geometry envelope
            // and the valid area in WGS84, which is a neutral, everything can
            // be turned into it, and then turn back the intersection into
            // the origin SRS
            ReferencedEnvelope envIntWgs84 = new ReferencedEnvelope(validAreaBounds.intersection(geWGS84), WGS84);

            // if the intersection is empty the geometry is completely outside of the valid area,
            // skip it
            if (envIntWgs84.isEmpty()) {
                return null;
            }

            Polygon polyIntWgs84 = JTS.toGeometry(envIntWgs84);
            Geometry maskWgs84 = intersect(validArea, polyIntWgs84, geometryCRS);
            if (maskWgs84 == null || maskWgs84.isEmpty()) {
                return null;
            }
            mask = JTS.transform(maskWgs84, CRS.findMathTransform(WGS84, geometryCRS));
        }

        return densify(intersect(geometry, mask, geometryCRS), false);
    }

    /**
     * Densifies the given geometry using the current densification configuration.
     *
     * <p>It returns the original geometry if densification is not enabled.
     */
    protected Geometry densify(Geometry geometry, boolean validate) {
        if (geometry != null && densify > 0.0 && !(geometry instanceof Point) && !(geometry instanceof MultiPoint)) {
            try {
                // does it make sense to densify to start with?
                double length = geometry.getLength();
                double expectedCount = length / densify;
                if (expectedCount < 3) {
                    return geometry;
                }

                double localDensify = densify;
                if (expectedCount > DENSIFICATION_LIMIT) {
                    LOGGER.log(
                            Level.FINE,
                            "Geometry densification would lead to at least {0} points, above limit, computing a smaller densification value",
                            new Object[] {expectedCount});
                    // limit is at least 3, so this loop cannot continue forever
                    // even starting with 1 billion expected coordinates, they would be reduced
                    // down to 2 in 30 iterations
                    while (expectedCount > DENSIFICATION_LIMIT) {
                        localDensify = localDensify * 2;
                        expectedCount = length / localDensify;
                    }
                    LOGGER.log(
                            Level.FINE,
                            "Re-computed densification value to {0}, expect to generate {1} points",
                            new Object[] {localDensify, expectedCount});
                }

                Densifier densifier = new Densifier(geometry);
                densifier.setDistanceTolerance(localDensify);
                densifier.setValidate(validate);
                return densifier.getResultGeometry();
            } catch (Throwable t) {
                LOGGER.warning("Cannot densify geometry");
            }
        }
        return geometry;
    }

    protected Geometry intersect(Geometry geometry, Geometry mask, CoordinateReferenceSystem geometryCRS) {
        // this seems to cause issues to JTS, reduce to
        // single geometry when possible (http://jira.codehaus.org/browse/GEOS-6570)
        if (geometry instanceof GeometryCollection) {
            int numGeometries = geometry.getNumGeometries();
            if (numGeometries == 1) {
                geometry = geometry.getGeometryN(0);
            } else {
                // go piecewise, the JTS intersection can be pretty fragile in these cases
                // and take a lot of time
                List<Geometry> elements = new ArrayList<>();
                String geometryType =
                        numGeometries > 0 ? geometry.getGeometryN(0).getGeometryType() : null;
                for (int i = 0; i < numGeometries; i++) {
                    Geometry g = geometry.getGeometryN(i);
                    if (g.getEnvelopeInternal().intersects(mask.getEnvelopeInternal())) {
                        Geometry intersected = intersect(g, mask, geometryCRS);
                        if (intersected != null) {
                            if (intersected.getGeometryType().equals(geometryType)) {
                                elements.add(intersected);
                            } else if (intersected instanceof GeometryCollection) {
                                addGeometries(elements, (GeometryCollection) intersected, geometryType);
                            }
                        }
                    }
                }

                if (elements.isEmpty()) {
                    return null;
                }

                if (geometry instanceof MultiPoint) {
                    Point[] array = elements.toArray(new Point[elements.size()]);
                    return geometry.getFactory().createMultiPoint(array);
                } else if (geometry instanceof MultiLineString) {
                    LineString[] array = elements.toArray(new LineString[elements.size()]);
                    return geometry.getFactory().createMultiLineString(array);
                } else if (geometry instanceof MultiPolygon) {
                    Polygon[] array = elements.toArray(new Polygon[elements.size()]);
                    return geometry.getFactory().createMultiPolygon(array);
                } else {
                    Geometry[] array = elements.toArray(new Geometry[elements.size()]);
                    return geometry.getFactory().createGeometryCollection(array);
                }
            }
        }
        Geometry result = null;
        try {
            result = intersection(geometry, mask);
        } catch (Exception e1) {
            // JTS versions lower than 1.18.0 included a call to buffer(0) in the reduce call.
            // We add it here to ensure that inputs are suitably clean.
            geometry = geometry.buffer(0);

            // try a precision reduction approach, starting from mm and scaling up to km
            double precision;
            if (CRS.getProjectedCRS(geometryCRS) != null) {
                precision = 1e-3;
            } else {
                precision = 1e-3 / 100000; // 1 degree roughly 100km
            }
            // from mm to km
            for (int i = 0; i < 6; i++) {
                GeometryPrecisionReducer reducer = new GeometryPrecisionReducer(new PrecisionModel(1 / precision));
                Geometry reduced = reducer.reduce(geometry);
                try {
                    if (LOGGER.isLoggable(Level.FINE)) {
                        LOGGER.log(
                                Level.FINE,
                                "Failed to intersect the geometry with the projection area of "
                                        + "validity mask, trying a precision reduction approach with a precision of "
                                        + precision);
                    }
                    result = intersection(reduced, mask);
                    break;
                } catch (Exception e3) {
                    precision *= 10;
                }
            }

            if (result == null) {
                LOGGER.log(
                        Level.WARNING,
                        "Failed to intersect the geometry with the projection area of "
                                + "validity mask, returning the original geometry: "
                                + geometry);
                result = geometry;
            }
        }

        // clean up lower dimensional elements
        GeometryDimensionCollector collector = new GeometryDimensionCollector(geometry.getDimension());
        result.apply(collector);
        result = collector.collect();

        // handle in special way empty intersections
        if (result == null || result.isEmpty()) {
            return null;
        } else {
            return result;
        }
    }

    private Geometry intersection(Geometry geometry, Geometry mask) {
        Geometry result = geometry.intersection(mask);

        // workaround for a JTS bug, sometimes it returns empty results
        // even if the two geometries are indeed intersecting
        if (result.isEmpty() && geometry.intersects(mask)) {
            try {
                result = EnhancedPrecisionOp.intersection(geometry, mask);
            } catch (Exception e2) {
                result = geometry;
            }
        }

        return result;
    }

    /** Can modify/wrap the transform to handle specific projection issues */
    public MathTransform getRenderingTransform(MathTransform mt) throws FactoryException {
        List<MathTransform> elements = new ArrayList<>();
        accumulateTransforms(mt, elements);

        List<MathTransform> wrapped = new ArrayList<>();
        List<MathTransform> datumShiftChain = null;
        boolean datumShiftDetected = false;
        for (MathTransform element : elements) {
            if (datumShiftChain != null) {
                datumShiftChain.add(element);
                if (element.getClass().getName().equals(GeocentricTransform.class.getName() + "$Inverse")) {
                    datumShiftDetected = true;
                    MathTransform combined = concatenateTransforms(datumShiftChain);
                    GeographicOffsetWrapper wrapper = new GeographicOffsetWrapper(combined);
                    wrapped.add(wrapper);
                    datumShiftChain = null;
                }
            } else if (element instanceof GeocentricTransform) {
                datumShiftChain = new ArrayList<>();
                datumShiftChain.add(element);
            } else {
                wrapped.add(element);
            }
        }

        if (datumShiftDetected) {
            if (datumShiftChain != null) {
                wrapped.addAll(datumShiftChain);
            }
            return concatenateTransforms(wrapped);
        } else {
            return mt;
        }
    }

    protected MathTransform concatenateTransforms(List<MathTransform> datumShiftChain) {
        if (datumShiftChain.size() == 1) {
            return datumShiftChain.get(0);
        } else {
            MathTransform mt = ConcatenatedTransform.create(datumShiftChain.get(0), datumShiftChain.get(1));
            for (int i = 2; i < datumShiftChain.size(); i++) {
                MathTransform curr = datumShiftChain.get(i);
                mt = ConcatenatedTransform.create(mt, curr);
            }

            return mt;
        }
    }

    protected void accumulateTransforms(MathTransform mt, List<MathTransform> elements) {
        if (mt instanceof ConcatenatedTransform) {
            ConcatenatedTransform ct = (ConcatenatedTransform) mt;
            accumulateTransforms(ct.transform1, elements);
            accumulateTransforms(ct.transform2, elements);
        } else {
            elements.add(mt);
        }
    }

    /**
     * Processes the geometry already projected to the target SRS. May return null if the geometry is not to be drawn.
     *
     * @param mt optional reverse transformation to facilitate unwrapping
     */
    public Geometry postProcess(MathTransform mt, Geometry geometry) {
        return geometry;
    }

    /**
     * Returns the area where the transformation from source to target is valid, expressed in the source coordinate
     * reference system, or null if there is no limit
     */
    public ReferencedEnvelope getValidAreaBounds() {
        return validAreaBounds;
    }

    /**
     * Returns the valid area as a JTS geometry, if it's a complex area (otherwise use {@link #getValidAreaBounds()})
     *
     * @return
     */
    public Geometry getValidArea() {
        return validArea;
    }

    protected void setCentralMeridian(double centralMeridian) {
        // compute the earth half circle in target CRS coordinates
        try {
            CoordinateReferenceSystem targetCRS = renderingEnvelope.getCoordinateReferenceSystem();
            MathTransform mt = CRS.findMathTransform(WGS84, targetCRS, true);
            double[] src = {centralMeridian, 0, 180 + centralMeridian, 0};
            double[] dst = new double[4];
            mt.transform(src, 0, dst, 0, 2);

            if (CRS.getAxisOrder(targetCRS) == CRS.AxisOrder.NORTH_EAST) {
                targetHalfCircle = Math.abs(dst[3] - dst[1]);
            } else {
                targetHalfCircle = Math.abs(dst[2] - dst[0]);
            }

            if (targetHalfCircle <= 0) {
                throw new RuntimeException("Computed Earth half circle is 0, what is going on?");
            }
        } catch (Exception e) {
            throw new RuntimeException("Unexpected error computing the Earth half circle in the current projection", e);
        }

        computeDatelineX();
    }

    protected void computeDatelineX() {
        // compute the x of the dateline in the rendering CRS
        try {
            double[] ordinates = {180, -80, 180, 80};
            MathTransform mt = CRS.findMathTransform(
                    DefaultGeographicCRS.WGS84, renderingEnvelope.getCoordinateReferenceSystem(), true);
            mt.transform(ordinates, 0, ordinates, 0, 2);
            datelineX = ordinates[0];
        } catch (Exception e) {
            // should never happen...
            throw new RuntimeException(e);
        }
    }

    /**
     * Private method for adding to the input List only the {@link Geometry} objects of the input
     * {@link GeometryCollection} which belongs to the defined geometryType
     */
    protected void addGeometries(List<Geometry> geoms, GeometryCollection collection, String geometryType) {
        // Check if the list exists
        if (geoms == null) {
            return;
        }
        // Check the Geometry type
        if (geometryType == null || geometryType.isEmpty()) {
            return;
        }
        // Check the collection
        if (collection == null || collection.getNumGeometries() <= 0) {
            return;
        }
        // Get the number of Geometries
        int numGeometries = collection.getNumGeometries();
        // Cycle on the Geometries
        for (int i = 0; i < numGeometries; i++) {
            // get the Geometry
            Geometry geo = collection.getGeometryN(i);
            // If it belongs to the correct Geometry type, it is added to the Liats
            if (geo.getGeometryType().equals(geometryType)) {
                geoms.add(geo);
                // Otherwise if it is a collection we try to iterate on it (recursion)
            } else if (geo instanceof GeometryCollection) {
                addGeometries(geoms, (GeometryCollection) geo, geometryType);
            }
        }
    }
}
