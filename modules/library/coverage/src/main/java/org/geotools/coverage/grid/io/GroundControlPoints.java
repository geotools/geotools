/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;

import java.util.List;
import org.geotools.coverage.grid.io.imageio.geotiff.TiePoint;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Image ground control points, as a list of {@link TiePoint} and their CRS
 *
 * @author Andrea Aime - GeoSolutions
 */
public class GroundControlPoints {
    List<TiePoint> tiePoints;

    CoordinateReferenceSystem crs;

    public GroundControlPoints(List<TiePoint> tiePoints, CoordinateReferenceSystem crs) {
        super();
        this.tiePoints = tiePoints;
        this.crs = crs;
    }

    /**
     * The tie points
     *
     * @return the tiePoints
     */
    public List<TiePoint> getTiePoints() {
        return tiePoints;
    }

    /**
     * The tie points coordinate reference system
     *
     * @return the crs
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /*
     * (non-Javadoc)
     *
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "GroundControlPoints [tiePoints=" + tiePoints + ", crs=" + crs + "]";
    }
}
