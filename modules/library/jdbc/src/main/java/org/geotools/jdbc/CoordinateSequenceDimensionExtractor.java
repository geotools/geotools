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
package org.geotools.jdbc;

import org.geotools.geometry.jts.coordinatesequence.CoordinateSequences;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFilter;

/**
 * Extracts the maximum dimension value from the geometry coordinate sequences (assumes 2 as the
 * starting point)
 *
 * @author Andrea Aime - GeoSolutions
 */
class CoordinateSequenceDimensionExtractor implements CoordinateSequenceFilter {

    int dimension = 2;

    @Override
    public void filter(CoordinateSequence seq, int i) {
        int dim = CoordinateSequences.coordinateDimension(seq);
        dimension = Math.max(dimension, dim);
    }

    @Override
    public boolean isDone() {
        return false;
    }

    @Override
    public boolean isGeometryChanged() {
        return false;
    }

    public int getDimension() {
        return dimension;
    }
}
