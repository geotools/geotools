/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.operation.projection.EquidistantConic;
import org.geotools.referencing.operation.projection.LambertConformal;
import org.geotools.referencing.operation.projection.LambertConformal1SP;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.MapProjection.AbstractProvider;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Returns a {@link ProjectionHandler} for the {@link LambertConformal} projection that will cut
 * geometries too close to pole on the open ended side of the cone.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ConicHandlerFactory implements ProjectionHandlerFactory {

    static final double EPS = 0.1;
    static final double MAX_DISTANCE = 44;

    public ProjectionHandler getHandler(
            ReferencedEnvelope renderingEnvelope,
            CoordinateReferenceSystem sourceCrs,
            boolean wrap,
            int maxWraps)
            throws FactoryException {
        if (renderingEnvelope == null) {
            return null;
        }

        MapProjection mapProjection =
                CRS.getMapProjection(renderingEnvelope.getCoordinateReferenceSystem());
        if (mapProjection instanceof LambertConformal
                || mapProjection instanceof EquidistantConic) {
            ParameterValueGroup params = mapProjection.getParameterValues();
            double centralMeridian =
                    params.parameter(AbstractProvider.CENTRAL_MERIDIAN.getName().getCode())
                            .doubleValue();
            double minLat, maxLat;

            double latitudeOrigin;
            if (mapProjection instanceof LambertConformal1SP) {
                latitudeOrigin =
                        params.parameter(AbstractProvider.LATITUDE_OF_ORIGIN.getName().getCode())
                                .doubleValue();
            } else {
                double stdParallel1 =
                        params.parameter(AbstractProvider.STANDARD_PARALLEL_1.getName().getCode())
                                .doubleValue();
                double stdParallel2 =
                        params.parameter(AbstractProvider.STANDARD_PARALLEL_2.getName().getCode())
                                .doubleValue();
                latitudeOrigin = (stdParallel1 + stdParallel2) / 2;
            }
            if (latitudeOrigin > 0) {
                minLat = Math.max(-89, latitudeOrigin - MAX_DISTANCE);
                maxLat = 90;
            } else {
                minLat = -90;
                maxLat = Math.min(89, latitudeOrigin + MAX_DISTANCE);
            }

            if (centralMeridian != 0) {
                // we need to divide geometries crossing the central antimeridian in two
                double antiMeridian =
                        centralMeridian > 0 ? centralMeridian - 180 : centralMeridian + 180;
                Polygon beforeAntiMeridian =
                        JTS.toGeometry(new Envelope(-180, antiMeridian - EPS, minLat, maxLat));
                Polygon afterAntiMeridian =
                        JTS.toGeometry(new Envelope(antiMeridian + EPS, 180, minLat, maxLat));
                MultiPolygon mask =
                        beforeAntiMeridian
                                .getFactory()
                                .createMultiPolygon(
                                        new Polygon[] {beforeAntiMeridian, afterAntiMeridian});

                return new ProjectionHandler(sourceCrs, mask, renderingEnvelope);
            } else {
                // for this case we just need to cut on the valid area, which is the whole world
                // minus a tiny area around the dateline
                Envelope validArea = new Envelope(-180 + EPS, 180 - EPS, minLat, maxLat);
                return new ProjectionHandler(sourceCrs, validArea, renderingEnvelope);
            }
        }

        return null;
    }
}
