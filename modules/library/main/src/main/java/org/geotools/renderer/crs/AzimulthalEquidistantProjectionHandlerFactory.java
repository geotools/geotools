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
package org.geotools.renderer.crs;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValue;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.AzimuthalEquidistant;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.MapProjection.AbstractProvider;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.PolygonExtracter;
import org.locationtech.jts.operation.buffer.BufferParameters;
import org.locationtech.jts.simplify.DouglasPeuckerSimplifier;

public class AzimulthalEquidistantProjectionHandlerFactory implements ProjectionHandlerFactory {

    static final Logger LOGGER = Logging.getLogger(AzimulthalEquidistantProjectionHandlerFactory.class);

    // longitude range goes beyond valid area to allow reading data that crosses the dateline,
    // extending for example between 0 and 360 (as used by GridCoverageReaderHelper)
    static final ReferencedEnvelope AZEQ_VALID_AREA =
            new ReferencedEnvelope(Integer.MIN_VALUE, Integer.MAX_VALUE, -90, 90, DefaultGeographicCRS.WGS84);

    static final double EPS = 1e-3;

    static GeometryFactory GF = new GeometryFactory();

    @Override
    public ProjectionHandler getHandler(
            final ReferencedEnvelope renderingEnvelope,
            final CoordinateReferenceSystem sourceCRS,
            boolean wrap,
            int wrapLimit)
            throws FactoryException {
        MapProjection mapProjection = CRS.getMapProjection(renderingEnvelope.getCoordinateReferenceSystem());
        if (renderingEnvelope != null && mapProjection instanceof AzimuthalEquidistant.Abstract) {
            try {

                ProjectionHandler ph =
                        new AzimuthalEquidistantProjectionHandler(sourceCRS, AZEQ_VALID_AREA, renderingEnvelope);
                return ph;
            } catch (Exception e) {
                LOGGER.log(
                        Level.WARNING,
                        "Could not create an azimuthal equidistant projection handler, " + "rendering without it",
                        e);
                return null;
            }
        }

        return null;
    }

    private static class AzimuthalEquidistantProjectionHandler extends ProjectionHandler {

        Geometry simplifiedDateline;

        Geometry bufferedDateline;

        GeometryFactory gf = new GeometryFactory();

        Polygon renderingGeometry;

        boolean renderingGeometryReduced;

        final Point north;
        final Point south;

        public AzimuthalEquidistantProjectionHandler(
                CoordinateReferenceSystem sourceCRS, Envelope validAreaBounds, ReferencedEnvelope renderingEnvelope)
                throws FactoryException, MismatchedDimensionException, TransformException {
            super(sourceCRS, validAreaBounds, renderingEnvelope);

            CoordinateReferenceSystem crs = renderingEnvelope.getCoordinateReferenceSystem();
            Point2D.Double center = getCenter();
            initializeDatelineCutter(crs, center);
            double radius = getRadius(crs, center);
            renderingGeometry = JTS.toGeometry(renderingEnvelope);
            if (!checkRenderingWithinRadius(center, radius)) {
                renderingGeometryReduced = true;
                Polygon azeqProjectedExtents = getAzeqProjectedExtents(radius);
                Geometry intersection = renderingGeometry.intersection(azeqProjectedExtents);
                if (intersection.isEmpty()) {
                    renderingGeometry = null;
                } else {
                    List polygons = PolygonExtracter.getPolygons(intersection);
                    renderingGeometry = (Polygon) polygons.get(0);
                }
            }
            if (renderingGeometry != null) {
                // reprojecting the geometry to compute query envelopes incurs in heavy deformation,
                // have at least 10 points per side
                Envelope envelope = renderingGeometry.getEnvelopeInternal();
                double tolerance = Math.max(envelope.getWidth(), envelope.getHeight()) / 20;
                renderingGeometry = (Polygon) Densifier.densify(renderingGeometry, tolerance);
            }

            // get north and south pole, to avoid holes in the output
            MathTransform mt = CRS.findMathTransform(DefaultGeographicCRS.WGS84, crs);
            north = getAzeqPosition(mt, 0, 90);
            south = getAzeqPosition(mt, 0, -90);
        }

        private Point getAzeqPosition(MathTransform mt, double lon, double lat) throws TransformException {
            double[] source = {lon, lat};
            double[] target = new double[2];
            try {
                mt.transform(source, 0, target, 0, 1);
                return GF.createPoint(new Coordinate(target[0], target[1]));
            } catch (TransformException e) {
                LOGGER.log(Level.FINE, "Could not transform lon/lat: " + lon + ", " + lat, e);
                return null;
            }
        }

        private double getRadius(CoordinateReferenceSystem crs, Point2D.Double center)
                throws TransformException, FactoryException {
            // the extreme points are sitting around 180 - center.x, -center.y, but each
            // need to be perturbated a bit to create the 4 cardinal points...
            double eps = 1e-3;
            double x = center.x > 0 ? (-180 + center.x) : (180 + center.x);
            double y = -center.y;
            final double[] line = {
                center.x,
                center.y, //
                x,
                y, //
                rollLongitude(x - eps),
                validLat(y - eps), //
                rollLongitude(x - eps),
                validLat(y + eps), //
                rollLongitude(x + eps),
                validLat(y - eps), //
                rollLongitude(x + eps),
                validLat(y + eps)
            };
            CRS.findMathTransform(DefaultGeographicCRS.WGS84, crs).transform(line, 0, line, 0, 6);
            double radius = 0;
            for (int i = 1; i <= 5; i++) {
                final double dx = line[i * 2] - line[0];
                final double dy = line[i * 2 + 1] - line[1];
                final double r = Math.sqrt(dx * dx + dy * dy);
                if (r > radius) radius = r;
            }
            return radius;
        }

        private double validLat(double lat) {
            if (lat < -90) return -90;
            if (lat > 90) return 90;
            return lat;
        }

        private double rollLongitude(final double x) {
            return x - (((int) (x + Math.signum(x) * 180)) / 360.0) * 360.0;
        }

        private void initializeDatelineCutter(CoordinateReferenceSystem crs, Point2D.Double center)
                throws TransformException, FactoryException {
            Geometry dateLine = getDateLine(center);
            Geometry datelineAzeq = JTS.transform(dateLine, CRS.findMathTransform(DefaultGeographicCRS.WGS84, crs));
            simplifiedDateline = DouglasPeuckerSimplifier.simplify(datelineAzeq, 100);
            bufferedDateline = simplifiedDateline.buffer(100, 1, BufferParameters.CAP_SQUARE);
        }

        @Override
        public List<ReferencedEnvelope> getQueryEnvelopes() throws TransformException, FactoryException {
            if (renderingGeometry == null) {
                return Collections.emptyList();
            }
            List<ReferencedEnvelope> results = new ArrayList<>();
            if (simplifiedDateline.intersects(renderingGeometry)) {
                Geometry difference = renderingGeometry.difference(bufferedDateline);
                @SuppressWarnings("unchecked")
                List<Polygon> polygons = PolygonExtracter.getPolygons(difference);
                for (Polygon p : polygons) {
                    Geometry transformed =
                            transformGeometry(p, renderingEnvelope.getCoordinateReferenceSystem(), sourceCRS);
                    // the back-transform can literally make the inner rings bigger than the
                    // outer ones, be careful computing the envelope
                    Envelope envelope = getFullEnvelope(transformed);
                    results.add(new ReferencedEnvelope(envelope, sourceCRS));
                }
            } else if (renderingGeometryReduced) {
                MathTransform mt = CRS.findMathTransform(renderingEnvelope.getCoordinateReferenceSystem(), sourceCRS);
                Geometry transformed = JTS.transform(renderingGeometry, mt);
                // the back-transform can literally make the inner rings bigger than the
                // outer ones, be careful computing the envelope
                Envelope envelope = getFullEnvelope(transformed);
                ReferencedEnvelope re = new ReferencedEnvelope(envelope, sourceCRS);
                results.add(re);
            } else {
                results = super.getQueryEnvelopes();
            }

            results.forEach(e -> {
                expandIfIncluded(e, north, 0, 90);
                expandIfIncluded(e, south, 0, -90);
            });

            mergeEnvelopes(results);
            return results;
        }

        public void expandIfIncluded(ReferencedEnvelope e, Point point, double lon, double lat) {
            if (renderingGeometry.contains(point)) {
                expandToIncludeGeographic(e, lon, lat);
            }
        }

        private Geometry transformGeometry(
                Geometry g, CoordinateReferenceSystem sourceCRS, CoordinateReferenceSystem targetCRS)
                throws FactoryException, TransformException {
            Geometry transformed = null;
            try {
                transformed = JTS.transform(g, CRS.findMathTransform(sourceCRS, targetCRS));
            } catch (TransformException e) {
                // uh oh, got into a special case of the target projection... go step by step
                Geometry polygonWGS84 = JTS.transform(
                        g,
                        CRS.findMathTransform(
                                renderingEnvelope.getCoordinateReferenceSystem(), DefaultGeographicCRS.WGS84));
                ProjectionHandler handler = ProjectionHandlerFinder.getHandler(
                        new ReferencedEnvelope(sourceCRS), DefaultGeographicCRS.WGS84, false);
                if (handler == null) throw e;
                Geometry preProcessed = handler.preProcess(polygonWGS84);
                transformed = JTS.transform(
                        preProcessed,
                        CRS.findMathTransform(renderingEnvelope.getCoordinateReferenceSystem(), sourceCRS));
            }
            return transformed;
        }

        private void expandToIncludeGeographic(ReferencedEnvelope env, double lon, double lat) {
            Point sourcepoint = GF.createPoint(new Coordinate(lon, lat));
            try {
                Point transformed = (Point)
                        transformGeometry(sourcepoint, DefaultGeographicCRS.WGS84, env.getCoordinateReferenceSystem());
                env.expandToInclude(transformed.getX(), transformed.getY());
            } catch (Exception e) {
                LOGGER.log(
                        Level.FINE,
                        "Failed to transform lon/lat point " + lon + ", " + lat + ", might not be a problem per se",
                        e);
            }
        }

        private Envelope getFullEnvelope(Geometry transformed) {
            final Envelope envelope = transformed.getEnvelopeInternal();
            transformed.apply((GeometryComponentFilter) geom -> envelope.expandToInclude(geom.getEnvelopeInternal()));

            return envelope;
        }

        /** Returns true if the rendering envelope is fully within the AZEQ projected max extension */
        public boolean checkRenderingWithinRadius(Point2D.Double center, double radius) {
            boolean renderingWithinRadius = true;
            double radiusSquared = radius * radius;
            for (int i = 0; i < 4; i++) {
                final double x, y;
                switch (i) {
                    case 0:
                        x = renderingEnvelope.getMinX();
                        y = renderingEnvelope.getMinY();
                        break;
                    case 1:
                        x = renderingEnvelope.getMinX();
                        y = renderingEnvelope.getMaxY();
                        break;
                    case 2:
                        x = renderingEnvelope.getMaxX();
                        y = renderingEnvelope.getMinY();
                        break;
                    case 3:
                        x = renderingEnvelope.getMaxX();
                        y = renderingEnvelope.getMaxY();
                        break;
                    default:
                        throw new IllegalStateException();
                }
                final double dx = x - center.x;
                final double dy = y - center.y;
                final double distanceSquared = dx * dx + dy * dy;
                renderingWithinRadius &= distanceSquared < radiusSquared;
            }
            return renderingWithinRadius;
        }

        private Point2D.Double getCenter() {
            MapProjection mapProjection = CRS.getMapProjection(renderingEnvelope.getCoordinateReferenceSystem());
            ParameterValue<?> centralMeridian = getParameter(mapProjection, AbstractProvider.CENTRAL_MERIDIAN);
            ParameterValue<?> latitudeOfOrigin = getParameter(mapProjection, AbstractProvider.LATITUDE_OF_ORIGIN);
            double centerLon = centralMeridian != null ? centralMeridian.doubleValue() : 0;
            double centerLat = latitudeOfOrigin != null ? latitudeOfOrigin.doubleValue() : 0;
            return new Point2D.Double(centerLon, centerLat);
        }

        private Geometry getDateLine(Point2D.Double center) {
            if (Math.abs(center.x) < EPS) {
                LineString ls1 = sampleDateLineBetweenLatitudes(gf, -90, center.y - EPS);
                LineString ls2 = sampleDateLineBetweenLatitudes(gf, center.y + EPS, 90);
                MultiLineString mls = gf.createMultiLineString(new LineString[] {ls1, ls2});
                return mls;
            } else {
                LineString ls = sampleDateLineBetweenLatitudes(gf, -90, 90);
                return ls;
            }
        }

        private ParameterValue<?> getParameter(MapProjection mapProjection, ParameterDescriptor<?> pd) {
            ParameterValue<?> centralMeridian = null;
            try {
                centralMeridian = mapProjection
                        .getParameterValues()
                        .parameter(pd.getName().getCode());
            } catch (ParameterNotFoundException e) {
                // ignore
            }
            return centralMeridian;
        }

        private LineString sampleDateLineBetweenLatitudes(GeometryFactory gf, double start, double end) {
            List<Coordinate> coordinates = new ArrayList<>();
            for (double lat = start; lat < end; lat++) {
                coordinates.add(new Coordinate(180, lat));
                if (lat + 1 > end) {
                    coordinates.add(new Coordinate(180, end));
                }
            }

            Coordinate[] array = coordinates.toArray(new Coordinate[coordinates.size()]);
            LineString ls = gf.createLineString(array);
            return ls;
        }

        public Polygon getAzeqProjectedExtents(double radius) {
            // might have false easting and northing
            MapProjection mapProjection = CRS.getMapProjection(renderingEnvelope.getCoordinateReferenceSystem());
            ParameterValue<?> falseEasting = getParameter(mapProjection, AbstractProvider.FALSE_EASTING);
            ParameterValue<?> falseNorthing = getParameter(mapProjection, AbstractProvider.FALSE_NORTHING);
            double centerX = falseEasting != null ? falseEasting.doubleValue() : 0;
            double centerY = falseNorthing != null ? falseNorthing.doubleValue() : 0;

            final int POINTS = 180;
            Coordinate[] coordinates = new Coordinate[POINTS + 1];
            // need to keep a bit of distance, the external radius collapses to a single point
            // when we project back to WGS84
            double distance = radius - 100;
            for (int i = 0; i < POINTS; i++) {
                Coordinate c = new Coordinate();
                c.x = centerX + Math.cos(Math.toRadians(360.0 / POINTS * i)) * distance;
                c.y = centerY + Math.sin(Math.toRadians(360.0 / POINTS * i)) * distance;
                coordinates[i] = new Coordinate(c.x, c.y);
            }
            coordinates[POINTS] = coordinates[0];

            LinearRing ring = gf.createLinearRing(coordinates);
            return gf.createPolygon(ring);
        }
    }
}
