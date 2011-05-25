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
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.crs.GeographicCRS;

/**
 * Returns a {@link ProjectionHandler} for any {@link GeographicCRS}
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/render/src/main/java/org/geotools/renderer/crs/GeographicHandlerFactory.java $
 */
public class GeographicHandlerFactory implements ProjectionHandlerFactory {
    
    public ProjectionHandler getHandler(ReferencedEnvelope renderingEnvelope, boolean wrap) {
        CoordinateReferenceSystem crs = renderingEnvelope.getCoordinateReferenceSystem();
        if (renderingEnvelope != null  && crs instanceof GeographicCRS) {
            GeographicCRS  geogCrs = (GeographicCRS) crs;
            if(wrap) {
                double centralMeridian = geogCrs.getDatum().getPrimeMeridian().getGreenwichLongitude();
                return new WrappingProjectionHandler(renderingEnvelope, null, centralMeridian);
            } else {
                return new ProjectionHandler(renderingEnvelope, null);
            }
        }

        return null;
    }

}
