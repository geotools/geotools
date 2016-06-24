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

import ucar.nc2.dataset.CoordinateAxis1D;

public class ClimatologicalTimeHandlerSpi implements CoordinateHandlerSpi {

    private static final String CLIMATOLOGICAL_UNITS = "CCYYMMDDHHMMSS";

    private final static Set<String> IGNORED;

    static {
        Set<String> set = new HashSet<String>();
        set.add("n_profiles");
        IGNORED = Collections.unmodifiableSet(set);
    }

    @Override
    public boolean canHandle(CoordinateAxis1D axis) {
        if (axis != null && "time".equalsIgnoreCase(axis.getShortName())
                && CLIMATOLOGICAL_UNITS.equalsIgnoreCase(axis.getUnitsString())) {
            return true;
        }
        return false;
    }

    @Override
    public CoordinateHandler createHandler() {
        return new ClimatologicalTimeHandler();
    }

    public class ClimatologicalTimeHandler implements CoordinateHandler {

        @Override
        public CoordinateVariable<Date> createCoordinateVariable(CoordinateAxis1D axis) {
            return new ClimatologicalTimeCoordinateVariable(axis);
        }
    }

    @Override
    public Set<String> getIgnoreSet() {
        return IGNORED;
    }
}
