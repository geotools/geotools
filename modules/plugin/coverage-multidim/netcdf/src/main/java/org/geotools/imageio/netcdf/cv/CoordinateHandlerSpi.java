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

import java.util.Set;
import ucar.nc2.dataset.CoordinateAxis;

/**
 * Constructs a live CoordinateHandler
 *
 * <p>In addition to implementing this interface, this service file should be defined:
 *
 * <p><code>META-INF/services/org.geotools.imageio.netcdf.cv.CoordinateHandlerSpi</code>
 *
 * <p>The file should contain a single line which gives the full name of the implementing class.
 *
 * <p>example:<br>
 * <code>e.g.
 * org.geotools.imageio.netcdf.cv.ClimatologicalTimeHandlerSPI</code>
 *
 * <p>The factories are never called directly by users, instead the CoordinateHandlerFinder class is
 * used.
 */
public interface CoordinateHandlerSpi {

    /**
     * Check if the specified spi can handle the provided axis through a {@link CoordinateHandler}
     */
    public boolean canHandle(CoordinateAxis axis);

    /** Create a {@link CoordinateHandler} */
    public CoordinateHandler createHandler();

    /** Return a set of dimensions to be ignored */
    public Set<String> getIgnoreSet();

    public interface CoordinateHandler {

        public CoordinateVariable<?> createCoordinateVariable(CoordinateAxis axis);
    }
}
