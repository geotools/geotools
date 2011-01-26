/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.crs;

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.CoordinateSequenceFilter;

/**
 * Applies a static offset to the specified ordinate in all coordinates of the geometry
 * 
 * @author Andrea Aime - OpenGeo
 */
class OffsetOrdinateFilter implements CoordinateSequenceFilter {
    double offset;

    int ordinateIndex;

    public OffsetOrdinateFilter(int ordinateIndex, double offset) {
        this.ordinateIndex = ordinateIndex;
        this.offset = offset;
    }

    public void filter(CoordinateSequence seq, int i) {
        seq.setOrdinate(i, ordinateIndex, seq.getOrdinate(i, ordinateIndex) + offset);
    }

    public boolean isDone() {
        return false;
    }

    public boolean isGeometryChanged() {
        return true;
    }

}