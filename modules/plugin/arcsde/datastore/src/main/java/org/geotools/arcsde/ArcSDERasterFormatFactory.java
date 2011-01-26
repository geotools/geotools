/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.geotools.arcsde;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.arcsde.raster.gce.ArcSDERasterFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.util.logging.Logging;

import com.esri.sde.sdk.client.SeConnection;
import com.esri.sde.sdk.pe.PeCoordinateSystem;

/**
 * Implementation of the GridCoverageFormat service provider interface for ArcSDE Databases. Based
 * on the Arc Grid implementation.
 * 
 * @author Saul Farber (saul.farber)
 * @author aaime
 * @author Simone Giannecchini (simboss)
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/arcsde/datastore/src/main/java
 *         /org/geotools/arcsde/ArcSDERasterFormatFactory.java $
 */
public class ArcSDERasterFormatFactory implements GridFormatFactorySpi {

    /** package's logger */
    protected static final Logger LOGGER = Logging.getLogger(ArcSDERasterFormatFactory.class
            .getName());

    /**
     * @see GridFormatFactorySpi#isAvailable()
     */
    public boolean isAvailable() {
        LOGGER.fine("Checking availability of ArcSDE Jars.");
        try {
            LOGGER.fine(SeConnection.class.getName() + " is in place.");
            LOGGER.fine(PeCoordinateSystem.class.getName() + " is in place.");
        } catch (Throwable t) {
            LOGGER.log(Level.WARNING, "ArcSDE Java API seems to not be on your classpath. Please"
                    + " verify that all needed jars are. ArcSDE data stores"
                    + " will not be available.", t);
            return false;
        }

        return true;
    }

    /**
     * @see GridFormatFactorySpi#createFormat()
     */
    public ArcSDERasterFormat createFormat() {
        return ArcSDERasterFormat.getInstance();
    }

    /**
     * Returns the implementation hints. The default implementation returns en empty map.
     * 
     * @return the empty map, this factory make no use of any implementation hint so far
     */
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
