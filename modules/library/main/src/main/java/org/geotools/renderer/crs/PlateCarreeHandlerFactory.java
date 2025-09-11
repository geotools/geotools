/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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

import java.util.logging.Logger;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.EquidistantCylindrical;
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.MapProjection.AbstractProvider;
import org.geotools.referencing.operation.projection.PlateCarree;

/** Returns a {@link ProjectionHandler} for the {@link PlateCarree} projection */
public class PlateCarreeHandlerFactory implements ProjectionHandlerFactory {

    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(PlateCarreeHandlerFactory.class);

    private static final ReferencedEnvelope VALID_AREA =
            new ReferencedEnvelope(-1800, 1800, -90, 90, DefaultGeographicCRS.WGS84);

    @Override
    public ProjectionHandler getHandler(
            ReferencedEnvelope renderingEnvelope, CoordinateReferenceSystem sourceCrs, boolean wrap, int maxWraps)
            throws FactoryException {

        if (renderingEnvelope == null) return null;
        CoordinateReferenceSystem renderingCrs = renderingEnvelope.getCoordinateReferenceSystem();
        if (renderingCrs == null) return null;
        MapProjection mapProjection = CRS.getMapProjection(renderingCrs);
        boolean isPlateCarree = false;
        if (mapProjection != null) {
            isPlateCarree = mapProjection instanceof PlateCarree;
            if (!isPlateCarree && mapProjection instanceof EquidistantCylindrical) {
                double stdParallel = 0;
                try {
                    stdParallel = mapProjection
                            .getParameterValues()
                            .parameter(EquidistantCylindrical.Provider.STANDARD_PARALLEL_1
                                    .getName()
                                    .getCode())
                            .doubleValue();
                } catch (Exception ignore) {
                    LOGGER.finest("Unable to read standard_parallel_1 parameter, assuming 0");
                }
                isPlateCarree = Math.abs(stdParallel) < 1e-12;
            }
        }

        if (!isPlateCarree) return null;
        ProjectionHandler handler;
        double centralMeridian = 0;
        ParameterValueGroup params = mapProjection.getParameterValues();
        if (params != null) {
            try {
                centralMeridian = params.parameter(
                                AbstractProvider.CENTRAL_MERIDIAN.getName().getCode())
                        .doubleValue();
            } catch (Exception ignore) {
                LOGGER.finest("Unable to read central_meridian parameter, assuming 0");
            }
        }
        centralMeridian = ((centralMeridian + 180) % 360 + 360) % 360 - 180; // normalize

        if (wrap && maxWraps > 0) {
            handler =
                    new WrappingProjectionHandler(renderingEnvelope, VALID_AREA, sourceCrs, centralMeridian, maxWraps);
        } else {
            handler = new ProjectionHandler(sourceCrs, VALID_AREA, renderingEnvelope);
            handler.setCentralMeridian(centralMeridian);
            handler.queryAcrossDateline = false;
        }
        return handler;
    }
}
