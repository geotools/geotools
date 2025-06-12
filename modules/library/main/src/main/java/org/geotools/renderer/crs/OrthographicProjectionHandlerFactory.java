/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.CENTRAL_MERIDIAN;
import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.LATITUDE_OF_ORIGIN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.Orthographic;
import org.geotools.util.SuppressFBWarnings;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;

@SuppressFBWarnings("FL_FLOATS_AS_LOOP_COUNTERS") // we actually need to loop on floats here
public class OrthographicProjectionHandlerFactory implements ProjectionHandlerFactory {

    private static final double EPS = 1e-3;
    private static final double SAFETY_BUFFER = -0.2;
    private static final int DENSIFY_DISTANCE = 1;

    @Override
    public ProjectionHandler getHandler(
            ReferencedEnvelope renderingEnvelope, CoordinateReferenceSystem sourceCRS, boolean wrap, int wrapLimit)
            throws FactoryException {
        if (renderingEnvelope == null) {
            return null;
        }
        CoordinateReferenceSystem targetCRS = renderingEnvelope.getCoordinateReferenceSystem();
        MapProjection mapProjection = CRS.getMapProjection(targetCRS);
        if (!(mapProjection instanceof Orthographic)) return null;

        ParameterValueGroup params = mapProjection.getParameterValues();
        double lo = params.parameter(LATITUDE_OF_ORIGIN.getName().getCode()).doubleValue();
        double cm = params.parameter(CENTRAL_MERIDIAN.getName().getCode()).doubleValue();

        Geometry validArea;
        if (Math.abs(lo) < 1E-6) {
            validArea = getEquatorialValidArea(cm);
        } else if (90 - Math.abs(lo) < 1E-6) {
            validArea = getPolarValidArea(lo);
        } else {
            try {
                validArea = getObliqueValidArea(cm, lo, targetCRS);
            } catch (TransformException e) {
                throw new RuntimeException("Failed to compute valid area for oblique orthographic", e);
            }
        }

        return new OrthographicProjectionHandler(sourceCRS, validArea, renderingEnvelope);
    }

    /** Either the norther or the southern hemisphere */
    private Geometry getPolarValidArea(double latitudeOrigin) {
        Geometry area;
        if (latitudeOrigin > 0) area = JTS.toGeometry(new Envelope(-180, 180, 0, 90));
        else area = JTS.toGeometry(new Envelope(-180, 180, -90, 0));
        return Densifier.densify(area, DENSIFY_DISTANCE).buffer(SAFETY_BUFFER);
    }

    /**
     * This one is a square, but it can be crossing the dateline, in which case, it needs to be split into two parts
     * (multi-polygon valid area)
     */
    private Geometry getEquatorialValidArea(double centralMeridian) {
        double minLat = -90 + EPS;
        double maxLat = 90 - EPS;
        Geometry validArea;
        GeometryFactory gf = new GeometryFactory();
        if (centralMeridian < -90) {
            // crossing the dateline, east
            Polygon p1 = JTS.toGeometry(new Envelope(centralMeridian - 90 + 360, 180 - EPS, minLat, maxLat));
            Polygon p2 = JTS.toGeometry(new Envelope(-180 - EPS, centralMeridian + 90, minLat, maxLat));
            validArea = gf.createMultiPolygon(new Polygon[] {p1, p2});

        } else if (centralMeridian > 90) {
            // crossing the dateline, west
            Polygon p1 = JTS.toGeometry(new Envelope(-180 + EPS, centralMeridian + 90 - 360, minLat, maxLat));
            Polygon p2 = JTS.toGeometry(new Envelope(centralMeridian - 90, 180 - EPS, minLat, maxLat));
            validArea = gf.createMultiPolygon(new Polygon[] {p1, p2});
        } else {
            validArea = JTS.toGeometry(new Envelope(centralMeridian - 90, centralMeridian + 90, minLat, maxLat));
        }
        // need to have a denser representation for reprojection, (buffer accounted above)
        return Densifier.densify(validArea, DENSIFY_DISTANCE);
    }

    /**
     * The border of the disc has a shape similar to a sinusoidal drawn between the datelines in plate carrée, but needs
     * to include everything up to the pole closer to the center point
     */
    private Geometry getObliqueValidArea(
            double centralMeridian, double latitudeOrigin, CoordinateReferenceSystem targetCRS)
            throws FactoryException, TransformException {
        MathTransform mt = CRS.findMathTransform(DefaultGeographicCRS.WGS84, targetCRS);
        double offset = latitudeOrigin > 0 ? -90 : 90;
        double[] src = {centralMeridian, latitudeOrigin + offset};
        double[] dst = new double[2];
        mt.transform(src, 0, dst, 0, 1);
        double radius = Math.abs(dst[1]);

        // stepping by 2 degrees of angle, but when getting close to the dateline,
        // doing a smaller step to get as close as possible to it (but not too close, or
        // the projection math will give us back invalid geometries...)
        List<Coordinate> coords = new ArrayList<>();
        MathTransform inverse = mt.inverse();
        Coordinate prev = null;
        final double NORMAL_STEP = 1;
        final double SMALL_STEP = 0.1;
        double step = NORMAL_STEP;
        boolean closingToDateline = false;
        double angle = 0;
        while (angle < 360) {
            src[0] = radius * Math.cos(Math.toRadians(angle));
            src[1] = radius * Math.sin(Math.toRadians(angle));
            inverse.transform(src, 0, dst, 0, 1);
            Coordinate curr = new Coordinate(dst[0], dst[1]);
            if (prev != null) {
                double diff = curr.x - prev.x;
                // dateline crossed?
                if (Math.abs(diff) > 90) {
                    join(latitudeOrigin, coords, prev, curr);
                    step = NORMAL_STEP;
                } else if (Math.abs(curr.x) > 177 && !closingToDateline) {
                    step = SMALL_STEP;
                    coords.add(curr); // this one last point we want to add, but not the next ones
                    closingToDateline = true;
                }
            }
            // do not add points while getting closer to the dateline cross
            if (step > SMALL_STEP) coords.add(curr);
            prev = curr;
            angle += step;
        }
        coords.add(coords.get(0));
        GeometryFactory gf = new GeometryFactory();
        Coordinate[] array = coords.toArray(new Coordinate[coords.size()]);
        LinearRing ring = gf.createLinearRing(new CoordinateArraySequence(array));
        Polygon polygon = gf.createPolygon(ring);
        // given the approximation with straight lines of a circular geometries, this
        // valid area goes sometimes outside of the actual mathematical valid area... and
        // the projection check throw. Reducing it a bit to stay on the safe side
        return polygon.buffer(SAFETY_BUFFER);
    }

    private void join(double latitudeOrigin, List<Coordinate> coords, Coordinate prev, Coordinate curr) {
        // adding points that include everything up to the pole, densified
        double sxp = Math.signum(prev.x);
        double sxc = Math.signum(curr.x);
        double sy = Math.signum(latitudeOrigin);
        coords.add(new Coordinate(prev.x, prev.y));
        double y = (prev.y + curr.y) / 2;
        if (sy > 0) {
            while (y < 90) {
                coords.add(new Coordinate(180 * sxp, y));
                y += 1;
            }
            coords.add(new Coordinate(180 * sxp, sy * 90));
            coords.add(new Coordinate(180 * sxc, sy * 90));
            y = y - 1;
            while (y > curr.y) {
                coords.add(new Coordinate(180 * sxc, y));
                y -= 1;
            }
        } else {
            while (y > -90) {
                coords.add(new Coordinate(180 * sxp, y));
                y -= 1;
            }
            coords.add(new Coordinate(180 * sxp, sy * 90));
            coords.add(new Coordinate(180 * sxc, sy * 90));
            y = y + 1;
            while (y < curr.y) {
                coords.add(new Coordinate(180 * sxc, y));
                y += 1;
            }
        }
    }

    private static class OrthographicProjectionHandler extends ProjectionHandler {

        /*
        The two parts of the valid area, in case of dateline crossing with equatorial projection
         */
        Geometry p1;
        Geometry p2;

        public OrthographicProjectionHandler(
                CoordinateReferenceSystem sourceCRS, Geometry validArea, ReferencedEnvelope renderingEnvelope)
                throws FactoryException {
            super(sourceCRS, validArea, renderingEnvelope, true);
            if (validArea instanceof MultiPolygon) {
                MultiPolygon mp = (MultiPolygon) validArea;
                p1 = mp.getGeometryN(0);
                p2 = mp.getGeometryN(1);
            }
        }

        /**
         * Orthographic is really finicky, step a bit out of the valid area and it will throw exceptions. So we need to
         * work strictly within the valid bounds, using JTS intersections, rather than working with envelopes
         */
        @Override
        protected ReferencedEnvelope transformEnvelope(ReferencedEnvelope envelope, CoordinateReferenceSystem targetCRS)
                throws TransformException, FactoryException {
            return transformEnvelope(envelope, targetCRS, validArea);
        }

        protected ReferencedEnvelope transformEnvelope(
                ReferencedEnvelope envelope, CoordinateReferenceSystem targetCRS, Geometry validArea)
                throws FactoryException, TransformException {
            // need to transform at all?
            CoordinateReferenceSystem envelopeCRS = envelope.getCoordinateReferenceSystem();
            if (CRS.equalsIgnoreMetadata(envelopeCRS, targetCRS)) return envelope;

            // reduce envelope to valid area
            Geometry validAreaInEnvelopeCRS =
                    JTS.transform(validArea, CRS.findMathTransform(DefaultGeographicCRS.WGS84, envelopeCRS));
            if (validArea instanceof MultiPolygon) {
                validAreaInEnvelopeCRS = validAreaInEnvelopeCRS.buffer(0);
            }
            Polygon geometry = JTS.toGeometry(envelope);
            Geometry intersection = geometry.intersection(validAreaInEnvelopeCRS);
            if (intersection == null || intersection.isEmpty()) return null;

            // need to densify or we'll loose a significant portion over reprojection,
            // the intersection created long straight lines and the validity areas in WGS84
            // are pretty curved
            Envelope ie = intersection.getEnvelopeInternal();
            double distance = (ie.getWidth() / 10 + ie.getHeight() / 10) / 2;
            intersection = Densifier.densify(intersection, distance);

            // transform to target CRS
            Geometry transformed = JTS.transform(intersection, CRS.findMathTransform(envelopeCRS, targetCRS));

            // does the target CRS have a strict notion of what's possible in terms of
            // valid coordinate ranges?
            ProjectionHandler handler = ProjectionHandlerFinder.getHandler(
                    new ReferencedEnvelope(targetCRS), DefaultGeographicCRS.WGS84, true, Collections.emptyMap());
            if (handler == null || handler instanceof WrappingProjectionHandler) {
                return new ReferencedEnvelope(transformed.getEnvelopeInternal(), targetCRS);
            }

            // if so, cut
            final ReferencedEnvelope validAreaBounds = handler.getValidAreaBounds();
            Geometry validAreaTarget = JTS.toGeometry(validAreaBounds.transform(targetCRS, true));
            Geometry reduced = transformed.intersection(validAreaTarget);
            if (reduced == null || reduced.isEmpty()) {
                return null;
            } else {
                return new ReferencedEnvelope(reduced.getEnvelopeInternal(), targetCRS);
            }
        }

        @Override
        public List<ReferencedEnvelope> getQueryEnvelopes() throws TransformException, FactoryException {
            // non equatorial case?
            if (p1 == null) return super.getQueryEnvelopes();

            // equatorial case, two separate valid areas
            ReferencedEnvelope e1 = transformEnvelope(renderingEnvelope, sourceCRS, p1);
            ReferencedEnvelope e2 = transformEnvelope(renderingEnvelope, sourceCRS, p2);

            if (e1 != null && e2 != null && e1.intersects((Envelope) e2)) {
                e1.expandToInclude(e2);
                return Collections.singletonList(e1);
            }

            List<ReferencedEnvelope> result = new ArrayList<>();
            if (e1 != null) result.add(e1);
            if (e2 != null) result.add(e2);
            return result;
        }
    }
}
