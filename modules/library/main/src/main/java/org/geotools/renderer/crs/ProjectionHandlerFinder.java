/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.LazySet;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Looks up the {@link ProjectionHandler} for the specified rendering area. WARNING: this API is not
 * finalized and is meant to be used by StreamingRenderer only
 *
 * @author Andrea Aime - OpenGeo
 */
public class ProjectionHandlerFinder {

    private static FactoryRegistry registry;

    static final Logger LOGGER = Logging.getLogger(ProjectionHandlerFinder.class);

    public static final String WRAP_LIMIT_KEY = "org.geotools.render.wrapLimit";

    static int WRAP_LIMIT;

    static {
        if (registry == null) {
            registry =
                    new FactoryCreator(
                            Arrays.asList(new Class<?>[] {ProjectionHandlerFactory.class}));
        }

        String wrapLimit = System.getProperty(WRAP_LIMIT_KEY);
        int limit = 10;
        try {
            if (wrapLimit != null) {
                limit = Integer.valueOf(wrapLimit);
            }
        } catch (NumberFormatException e) {
            LOGGER.log(
                    Level.SEVERE,
                    WRAP_LIMIT_KEY
                            + " has invalid value, it should be an integer number but it was: "
                            + wrapLimit);
        }
        WRAP_LIMIT = limit;
    }

    /**
     * Programmatically sets the number of wraps per direction the wrapping projection handlers will
     * apply
     */
    public static void setWrapLimit(int wrapLimit) {
        ProjectionHandlerFinder.WRAP_LIMIT = wrapLimit;
    }

    private static LazySet<ProjectionHandlerFactory> getProjectionHandlerFactories() {
        Hints hints = GeoTools.getDefaultHints();
        return new LazySet<ProjectionHandlerFactory>(
                registry.getFactories(ProjectionHandlerFactory.class, null, hints));
    }

    /**
     * Returns a projection handler for the specified rendering area, or null if not found
     *
     * @param renderingArea The area to be painted (mind, the CRS must be property set for
     *     projection handling to work)
     * @param wrap Enable continuous map wrapping if it's possible for the current projection
     */
    public static ProjectionHandler getHandler(
            ReferencedEnvelope renderingArea, CoordinateReferenceSystem sourceCrs, boolean wrap)
            throws FactoryException {
        return getHandler(renderingArea, sourceCrs, wrap, new HashMap());
    }

    /**
     * Returns a projection handler for the specified rendering area, or null if not found
     *
     * @param renderingArea The area to be painted (mind, the CRS must be property set for
     *     projection handling to work)
     * @param wrap Enable continuous map wrapping if it's possible for the current projection
     * @param projectionParameters map of options for the projection handler, allows finer
     *     configuration of the handler from the final user of it
     */
    public static ProjectionHandler getHandler(
            ReferencedEnvelope renderingArea,
            CoordinateReferenceSystem sourceCrs,
            boolean wrap,
            Map projectionParameters)
            throws FactoryException {
        if (renderingArea.getCoordinateReferenceSystem() == null) return null;

        for (ProjectionHandlerFactory factory : getProjectionHandlerFactories()) {
            ProjectionHandler handler =
                    factory.getHandler(renderingArea, sourceCrs, wrap, WRAP_LIMIT);
            if (handler != null) {
                handler.setProjectionParameters(projectionParameters);
                return handler;
            }
        }

        return null;
    }
}
