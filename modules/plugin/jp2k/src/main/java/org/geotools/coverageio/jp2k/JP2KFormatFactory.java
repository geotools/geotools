/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.jp2k;

import it.geosolutions.imageio.plugins.jp2k.JP2KKakaduImageReaderSpi;
import it.geosolutions.util.KakaduUtilities;
import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.spi.ImageReaderSpi;
import org.geotools.api.coverage.grid.Format;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;

/**
 * Implementation of the {@link Format} service provider interface for JP2K files.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 */
public final class JP2KFormatFactory implements GridFormatFactorySpi {
    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(JP2KFormatFactory.class);

    static ImageReaderSpi cachedSpi;

    public static ImageReaderSpi getCachedSpi() {
        return cachedSpi;
    }

    static {
        boolean hasKakaduSpi = false;
        try {
            // check if our jp2k plugin is in the path
            Class.forName("it.geosolutions.imageio.plugins.jp2k.JP2KKakaduImageReaderSpi");
            hasKakaduSpi = true;
        } catch (ClassNotFoundException e) {
            if (LOGGER.isLoggable(Level.WARNING))
                LOGGER.log(Level.WARNING, "Unable to load specific JP2K reader spi", e);
        }

        if (hasKakaduSpi && KakaduUtilities.isKakaduAvailable()) {
            cachedSpi = new JP2KKakaduImageReaderSpi();
        }
    }

    /**
     * Tells me if the coverage plugin to access JP2K is available or not.
     *
     * @return <code>true</code> if the plugin is available, <code>false</code> otherwise.
     */
    @Override
    public boolean isAvailable() {
        boolean available = false;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {
            Class.forName("org.eclipse.imagen.JAI");
            Class.forName("org.eclipse.imagen.media.imageread.ImageReadDescriptor");
            if (cachedSpi != null) available = true;
            //
            // Class.forName("it.geosolutions.imageio.plugins.jp2k.JP2KKakaduImageReaderSpi");
            //            available = KakaduUtilities.isKakaduAvailable();

            if (LOGGER.isLoggable(Level.FINE)) {
                if (available) {
                    LOGGER.fine("JP2KFormatFactory is available.");
                } else {
                    LOGGER.fine("JP2KFormatFactory is not available.");
                }
            }
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("JP2KFormatFactory is not available.");
            }

            available = false;
        }

        return available;
    }

    /**
     * Creating a {@link JP2KFormat}.
     *
     * @return A {@link JP2KFormat}.;
     */
    @Override
    public JP2KFormat createFormat() {
        return new JP2KFormat();
    }

    /**
     * Returns the implementation hints. The default implementation returns an empty map.
     *
     * @return An empty map.
     */
    @Override
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
