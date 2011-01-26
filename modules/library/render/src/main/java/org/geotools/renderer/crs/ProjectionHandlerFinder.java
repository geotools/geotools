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

import java.util.ArrayList;
import java.util.List;

import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Looks up the {@link ProjectionHandler} for the specified rendering area.
 * WARNING: this API is not finalized and is meant to be used by StreamingRenderer only 
 * 
 * @author Andrea Aime - OpenGeo
 */
public class ProjectionHandlerFinder {

    static List<ProjectionHandlerFactory> factories = new ArrayList<ProjectionHandlerFactory>();

    static {
        factories.add(new GeographicHandlerFactory());
        factories.add(new MercatorHandlerFactory());
        factories.add(new TransverseMercatorHandlerFactory());
        factories.add(new PolarStereographicHandlerFactory());
    }

    /**
     * Returns a projection handler for the specified rendering area, or null if not found
     * @param renderingArea The area to be painted (mind, the CRS must be property set for projection handling to work)
     * @param wrap Enable continuous map wrapping if it's possible for the current projection
     */
    public static ProjectionHandler getHandler(ReferencedEnvelope renderingArea, boolean wrap) {
        if (renderingArea.getCoordinateReferenceSystem() == null)
            return null;

        for (ProjectionHandlerFactory factory : factories) {
            ProjectionHandler handler = factory.getHandler(renderingArea, wrap);
            if (handler != null)
                return handler;
        }

        return null;
    }

}
