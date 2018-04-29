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
package org.geotools.coverage.io;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.util.DateRange;
import org.geotools.util.NumberRange;
import org.opengis.coverage.grid.GridCoverage;

/**
 * An interface implementing {@link GridCoverage} with add of temporal extent and vertical extent
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public interface GridCoverageResponse extends GridCoverage {

    /** Return the temporal extent of this gridcoverage */
    DateRange getTemporalExtent();

    /** Return the vertical extent of this gridcoverage */
    NumberRange<Double> getVerticalExtent();

    /** Return the underlying GridCoverage2D (responses are always 2D) */
    GridCoverage2D getGridCoverage2D();
}
