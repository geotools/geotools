/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.referencing.operation.projection.GeostationarySatellite.Provider.SATELLITE_HEIGHT;
import static org.geotools.referencing.operation.projection.MapProjection.AbstractProvider.CENTRAL_MERIDIAN;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.ProjectedCRS;
import org.geotools.api.referencing.datum.Ellipsoid;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.GeostationarySatellite;
import org.geotools.referencing.operation.projection.MapProjection;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.util.AffineTransformation;

/** Projection handler for {@link org.geotools.referencing.operation.projection.GeostationarySatellite} */
public class GeosProjectionHandlerFactory implements ProjectionHandlerFactory {

    public static final Polygon WORLD_LEFT = JTS.toGeometry(new Envelope(-360, -180, -90, 90));
    public static final Polygon WORLD = JTS.toGeometry(new Envelope(-180, 180, -90, 90));
    public static final Polygon WORLD_RIGHT = JTS.toGeometry(new Envelope(180, 360, -90, 90));

    @Override
    public ProjectionHandler getHandler(
            ReferencedEnvelope renderingEnvelope, CoordinateReferenceSystem sourceCRS, boolean wrap, int wrapLimit)
            throws FactoryException {
        CoordinateReferenceSystem targetCRS = renderingEnvelope.getCoordinateReferenceSystem();
        MapProjection mapProjection = CRS.getMapProjection(targetCRS);
        if (!(mapProjection instanceof GeostationarySatellite)) return null;

        ParameterValueGroup params = mapProjection.getParameterValues();
        double cm = params.parameter(CENTRAL_MERIDIAN.getName().getCode()).doubleValue();
        double height = params.parameter(SATELLITE_HEIGHT.getName().getCode()).doubleValue();
        Ellipsoid ellipsoid = ((ProjectedCRS) targetCRS).getBaseCRS().getDatum().getEllipsoid();
        double radius = (ellipsoid.getSemiMajorAxis() + ellipsoid.getSemiMajorAxis()) / 2;
        double angle = getInnerAngle(height, radius);

        Geometry validArea = getValidAreaLatLon(cm, angle);

        return new GeosProjectionHandler(sourceCRS, validArea, renderingEnvelope);
    }

    /**
     * Returns the inner angle between the hypotenuse and the catethus. In the context of this class, the hypotenuse is
     * the height of the satellite, and the catethus is the radius of the Earth.
     */
    static double getInnerAngle(double hypotenuse, double cathetus) {
        double opposite = Math.sqrt(hypotenuse * hypotenuse - cathetus * cathetus);
        return Math.toDegrees(Math.atan2(opposite, cathetus));
    }

    /**
     * Returns the valid area of the projection in lat/lon coordinates.
     *
     * @param centralMeridian the central meridian of the projection
     * @param angle the inner angle of the projection
     * @return the valid area of the projection
     */
    static Geometry getValidAreaLatLon(double centralMeridian, double angle) {
        // compute "circle" around the center, one segment every 2 degrees
        Point center = new GeometryFactory().createPoint(new Coordinate(centralMeridian, 0));
        Geometry circle = center.buffer(angle, 45);

        // check if crossing the dateline, or not
        Envelope env = circle.getEnvelopeInternal();
        if (env.getMinX() < -180) {
            Geometry left = circle.intersection(WORLD_LEFT);
            Geometry leftTranslated =
                    AffineTransformation.translationInstance(360, 0).transform(left);
            Geometry right = circle.intersection(WORLD);

            return right.union(leftTranslated);
        } else if (env.getMaxX() > 180) {
            Geometry left = circle.intersection(WORLD);
            Geometry right = circle.intersection(WORLD_RIGHT);
            Geometry rightTranslated =
                    AffineTransformation.translationInstance(-360, 0).transform(right);
            return left.union(rightTranslated);
        } else {
            return circle;
        }
    }

    /** Projection handler for {@link GeostationarySatellite} projection */
    static class GeosProjectionHandler extends ProjectionHandler {

        /*
        The two parts of the valid area, in case of dateline crossing with equatorial projection
         */
        Geometry p1;
        Geometry p2;

        public GeosProjectionHandler(
                CoordinateReferenceSystem sourceCRS, Geometry validArea, ReferencedEnvelope renderingEnvelope)
                throws FactoryException {
            super(sourceCRS, validArea, renderingEnvelope);
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

            // two separate valid areas
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
