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
import org.geotools.referencing.operation.projection.MapProjection;
import org.geotools.referencing.operation.projection.MapProjection.AbstractProvider;
import org.geotools.referencing.operation.projection.TransverseMercator;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Returns a {@link ProjectionHandler} for the {@link TransverseMercator} projection that will cut
 * geometries 45° away from the projection central meridian
 *
 * @author Andrea Aime - OpenGeo
 */
public class TransverseMercatorHandlerFactory implements ProjectionHandlerFactory {

    public ProjectionHandler getHandler(
            ReferencedEnvelope renderingEnvelope,
            CoordinateReferenceSystem sourceCrs,
            boolean wrap,
            int maxWraps)
            throws FactoryException {
        if (renderingEnvelope == null) return null;
        MapProjection mapProjection =
                CRS.getMapProjection(renderingEnvelope.getCoordinateReferenceSystem());
        if (!(mapProjection instanceof TransverseMercator)) return null;

        double centralMeridian =
                mapProjection
                        .getParameterValues()
                        .parameter(AbstractProvider.CENTRAL_MERIDIAN.getName().getCode())
                        .doubleValue();

        ReferencedEnvelope validArea =
                new ReferencedEnvelope(
                        centralMeridian - 45,
                        centralMeridian + 45,
                        -85,
                        85,
                        DefaultGeographicCRS.WGS84);

        ProjectionHandler ph = new ProjectionHandler(sourceCrs, validArea, renderingEnvelope);
        if ((validArea.getMinX() < 180 && validArea.getMaxX() > 180)
                || (validArea.getMinX() < -180 && validArea.getMaxX() > -180)) {
            ph.computeDatelineX();
        }

        return ph;
    }
}
