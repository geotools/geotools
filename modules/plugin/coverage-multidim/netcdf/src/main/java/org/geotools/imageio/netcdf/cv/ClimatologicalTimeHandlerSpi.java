/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf.cv;

import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import ucar.nc2.dataset.CoordinateAxis;

public class ClimatologicalTimeHandlerSpi implements CoordinateHandlerSpi {

    private static final String CLIMATOLOGICAL_UNITS = "CCYYMMDDHHMMSS";

    private static final Logger LOGGER = Logging.getLogger(ClimatologicalTimeHandlerSpi.class);

    private static final Set<String> IGNORED;

    static {
        Set<String> set = new HashSet<>();
        set.add("n_profiles");
        IGNORED = Collections.unmodifiableSet(set);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("ClimatologicalTimeHandlerSpi being initialized");
        }
    }

    @Override
    public boolean canHandle(CoordinateAxis axis) {
        if (axis != null
                && "time".equalsIgnoreCase(axis.getShortName())
                && CLIMATOLOGICAL_UNITS.equalsIgnoreCase(axis.getUnitsString())) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("Climatological Time Handler Spi can handle the axis");
            }
            return true;
        }
        return false;
    }

    @Override
    public CoordinateHandler createHandler() {
        return new ClimatologicalTimeHandler();
    }

    public static class ClimatologicalTimeHandler implements CoordinateHandler {

        @Override
        public CoordinateVariable<Date> createCoordinateVariable(CoordinateAxis axis) {
            if (LOGGER.isLoggable(Level.FINEST)) {
                LOGGER.finest("Creating ClimatologicalTimeCoordinateVariable");
            }
            return new ClimatologicalTimeCoordinateVariable(axis);
        }
    }

    @Override
    public Set<String> getIgnoreSet() {
        return IGNORED;
    }
}
