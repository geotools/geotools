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

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.referencing.operation.projection.LambertAzimuthalEqualArea;
import org.geotools.referencing.operation.projection.MapProjection;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Returns a {@link ProjectionHandler} for the {@link LambertAzimuthalEqualArea} projection that
 * will cut geometries 90° away from the projection center (at the moment it works only with with
 * the versions centered on poles and the equator)
 *
 * @author Andrea Aime - GeoSolutions
 */
public class LambertAzimuthalEqualAreaHandlerFactory implements ProjectionHandlerFactory {

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
        if (mapProjection instanceof LambertAzimuthalEqualArea) {
            ParameterValueGroup params = mapProjection.getParameterValues();
            double latitudeOfCenter =
                    params.parameter(
                                    LambertAzimuthalEqualArea.Provider.LATITUDE_OF_CENTRE
                                            .getName()
                                            .getCode())
                            .doubleValue();
            double longitudeOfCenter =
                    params.parameter(
                                    LambertAzimuthalEqualArea.Provider.LONGITUDE_OF_CENTRE
                                            .getName()
                                            .getCode())
                            .doubleValue();

            ReferencedEnvelope validArea;
            if (latitudeOfCenter > 0) {
                validArea = new ReferencedEnvelope(-180, 180, 0, 90, DefaultGeographicCRS.WGS84);
            } else if (latitudeOfCenter < 0) {
                validArea = new ReferencedEnvelope(-180, 180, -90, 0, DefaultGeographicCRS.WGS84);
            } else {
                validArea =
                        new ReferencedEnvelope(
                                longitudeOfCenter - 90,
                                longitudeOfCenter + 90,
                                -90,
                                90,
                                DefaultGeographicCRS.WGS84);
            }

            return new ProjectionHandler(sourceCrs, validArea, renderingEnvelope);
        }

        return null;
    }
}
