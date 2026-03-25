/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.processing.jai;

import java.util.Set;
import java.util.logging.Logger;
import org.eclipse.imagen.spi.RegistryAllowListProvider;

/**
 * Contributes the GeoTools transparency-fill registry classes that ImageN may instantiate from this module registry
 * file.
 */
public final class TransparencyFillRegistryAllowListProvider implements RegistryAllowListProvider {

    private static final Logger LOGGER = Logger.getLogger(TransparencyFillRegistryAllowListProvider.class.getName());

    private static final Set<String> ALLOWED_CLASSES =
            Set.of(TransparencyFillDescriptor.class.getName(), TransparencyFillRIF.class.getName());

    /**
     * Default constructor required by {@link java.util.ServiceLoader}.
     *
     * <p>Logs provider discovery at debug level so service-loading can be diagnosed when needed.
     */
    public TransparencyFillRegistryAllowListProvider() {
        LOGGER.fine(() ->
                "Loaded transparency-fill ImageN allow-list provider with " + ALLOWED_CLASSES.size() + " classes");
    }

    /** Returns the exact registry classes contributed by the GeoTools process-raster module. */
    @Override
    public Set<String> getAllowedRegistryClasses() {
        LOGGER.finest(() -> "Providing transparency-fill ImageN allow-list classes: " + ALLOWED_CLASSES);
        return ALLOWED_CLASSES;
    }
}
