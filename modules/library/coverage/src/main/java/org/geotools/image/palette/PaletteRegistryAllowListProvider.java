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
package org.geotools.image.palette;

import java.util.Set;
import java.util.logging.Logger;
import org.eclipse.imagen.spi.RegistryAllowListProvider;

/** Contributes the GeoTools palette registry classes that ImageN may instantiate from this module registry file. */
public final class PaletteRegistryAllowListProvider implements RegistryAllowListProvider {

    private static final Logger LOGGER = Logger.getLogger(PaletteRegistryAllowListProvider.class.getName());

    private static final Set<String> ALLOWED_CLASSES = Set.of(
            ColorReductionDescriptor.class.getName(),
            ColorInversionDescriptor.class.getName(),
            ColorReductionCRIF.class.getName(),
            ColorInversionCRIF.class.getName());

    /**
     * Default constructor required by {@link java.util.ServiceLoader}.
     *
     * <p>Logs provider discovery at debug level so service-loading can be diagnosed when needed.
     */
    public PaletteRegistryAllowListProvider() {
        LOGGER.fine(() -> "Loaded palette ImageN allow-list provider with " + ALLOWED_CLASSES.size() + " classes");
    }

    /** Returns the exact registry classes contributed by the GeoTools coverage module. */
    @Override
    public Set<String> getAllowedRegistryClasses() {
        LOGGER.finest(() -> "Providing palette ImageN allow-list classes: " + ALLOWED_CLASSES);
        return ALLOWED_CLASSES;
    }
}
