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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.GeostationarySatellite;
import org.geotools.referencing.operation.projection.MapProjection;
import org.locationtech.jts.densify.Densifier;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

/** Projection handler for {@link org.geotools.referencing.operation.projection.GeostationarySatellite} */
public class GeosProjectionHandlerFactory implements ProjectionHandlerFactory {

    public static final Polygon WORLD_LEFT = JTS.toGeometry(new Envelope(-360, -180, -90, 90));
    public static final Polygon WORLD = JTS.toGeometry(new Envelope(-180, 180, -90, 90));
    public static final Polygon WORLD_RIGHT = JTS.toGeometry(new Envelope(180, 360, -90, 90));

    private static final GeosValidAreaCache VALID_AREA_CACHE = new GeosValidAreaCache();

    @Override
    public ProjectionHandler getHandler(
            ReferencedEnvelope renderingEnvelope, CoordinateReferenceSystem sourceCRS, boolean wrap, int wrapLimit)
            throws FactoryException {
        CoordinateReferenceSystem targetCRS = renderingEnvelope.getCoordinateReferenceSystem();
        MapProjection mapProjection = CRS.getMapProjection(targetCRS);
        if (!(mapProjection instanceof GeostationarySatellite)) return null;
        try {
            Geometry validArea = VALID_AREA_CACHE.getValidAreaInGeosCrs(targetCRS);
            return new GeosProjectionHandler(sourceCRS, validArea, renderingEnvelope);
        } catch (TransformException e) {
            throw new FactoryException("Failed to calculate valid area for GeostationarySatellite projection", e);
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
            if (validArea instanceof MultiPolygon mp) {
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
