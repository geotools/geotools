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
import org.geotools.referencing.operation.projection.Mercator;
import org.geotools.referencing.operation.projection.MapProjection.AbstractProvider;

/**
 * Returns a {@link ProjectionHandler} for the {@link Mercator} projection
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/render/src/main/java/org/geotools/renderer/crs/MercatorHandlerFactory.java $
 */
public class MercatorHandlerFactory implements ProjectionHandlerFactory {

    private static final ReferencedEnvelope VALID_AREA = new ReferencedEnvelope(-Double.MAX_VALUE, Double.MAX_VALUE, -89, 89,
            DefaultGeographicCRS.WGS84);

    public ProjectionHandler getHandler(ReferencedEnvelope renderingEnvelope, boolean wrap) {
        MapProjection mapProjection = CRS.getMapProjection(renderingEnvelope
                .getCoordinateReferenceSystem());
        if (renderingEnvelope != null && mapProjection instanceof Mercator) {
            if(wrap) {
                double centralMeridian = mapProjection.getParameterValues().parameter(
                        AbstractProvider.CENTRAL_MERIDIAN.getName().getCode()).doubleValue();
                return new WrappingProjectionHandler(renderingEnvelope, VALID_AREA, centralMeridian);
            } else {
                return new ProjectionHandler(renderingEnvelope, VALID_AREA);
            }
        }

        return null;
    }

}
