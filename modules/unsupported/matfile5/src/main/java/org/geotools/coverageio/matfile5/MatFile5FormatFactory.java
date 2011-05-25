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
package org.geotools.coverageio.matfile5;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.opengis.coverage.grid.Format;


/**
 * Implementation of the {@link Format} service provider interface for Matlab Files version 5
 * files.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.7.x
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/matfile5/src/main/java/org/geotools/coverageio/matfile5/MatFile5FormatFactory.java $
 */
public final class MatFile5FormatFactory implements GridFormatFactorySpi {
    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
    		MatFile5FormatFactory.class.toString());

    /**
     * Tells me if the coverage plugin to access MatFile5 is available or not.
     *
     * @return {@code true} if the plugin is available, {@code false} otherwise.
     */
    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {
            Class.forName("javax.media.jai.JAI");
            Class.forName("com.sun.media.jai.operator.ImageReadDescriptor");
            Class.forName("it.geosolutions.imageio.matfile5.sas.SASTileImageReaderSpi");

            if (LOGGER.isLoggable(Level.FINE)) {
                if (available) 
                    LOGGER.fine(this.getClass().getName()+" is availaible.");
                else 
                    LOGGER.fine(this.getClass().getName()+" is not availaible.");
            }
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE)) 
                LOGGER.fine(this.getClass().getName()+" is not availaible.");

            available = false;
        }

        return available;
    }

    /**
     * Creating a {@link MatFile5Format}
     *
     * @return A {@link MatFile5Format}
     */
    public MatFile5Format createFormat() {
        return new MatFile5Format();
    }

    /**
     * Returns the implementation hints. The default implementation returns en
     * empty map.
     */
    public Map<RenderingHints.Key, ?>  getImplementationHints() {
        return Collections.emptyMap();
    }
}
