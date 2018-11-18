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
import org.geotools.referencing.operation.projection.WorldVanDerGrintenI;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Returns a {@link ProjectionHandler} for the {@link WorldVanDerGrintenI} projection that will cut
 * geometries going above or below the poles, this projection will fail with latitudes out of range
 * (but not longitudes)
 *
 * @author Andrea Aime - GeoSolutions
 */
public class WorldVanDerGrintenIHandlerFactory implements ProjectionHandlerFactory {

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
        if (mapProjection instanceof WorldVanDerGrintenI) {
            ReferencedEnvelope validArea =
                    new ReferencedEnvelope(
                            -Integer.MAX_VALUE,
                            Integer.MAX_VALUE,
                            -90,
                            90,
                            DefaultGeographicCRS.WGS84);

            return new ProjectionHandler(sourceCrs, validArea, renderingEnvelope);
        }

        return null;
    }
}
