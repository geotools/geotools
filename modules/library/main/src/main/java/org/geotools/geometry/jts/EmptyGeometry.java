/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateFilter;
import org.locationtech.jts.geom.CoordinateSequenceComparator;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.GeometryFilter;

/**
 * An empty geometry class. Best to use an empty collection to represent an empty geometry, this
 * class is left only for backwards compatibility, will eventually be removed.
 *
 * @deprecated
 */
@Deprecated
public class EmptyGeometry extends Geometry {

    public EmptyGeometry() {
        super(new GeometryFactory());
    }

    @Override
    public String getGeometryType() {
        return null;
    }

    @Override
    public Coordinate getCoordinate() {
        return null;
    }

    @Override
    public Coordinate[] getCoordinates() {
        return null;
    }

    @Override
    public int getNumPoints() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return true;
    }

    @Override
    public int getDimension() {
        return 0;
    }

    @Override
    public Geometry getBoundary() {
        return null;
    }

    @Override
    public int getBoundaryDimension() {
        return 0;
    }

    @Override
    public Geometry reverse() {
        return null;
    }

    @Override
    public boolean equalsExact(Geometry other, double tolerance) {
        return false;
    }

    @Override
    public void apply(CoordinateFilter filter) {}

    @Override
    public void apply(CoordinateSequenceFilter filter) {}

    @Override
    public void apply(GeometryFilter filter) {}

    @Override
    public void apply(GeometryComponentFilter filter) {}

    @Override
    public Geometry copy() {
        return null;
    }

    @Override
    protected Geometry copyInternal() {
        return null;
    }

    @Override
    public void normalize() {}

    @Override
    protected Envelope computeEnvelopeInternal() {
        return null;
    }

    @Override
    protected int compareToSameClass(Object o) {
        return 0;
    }

    @Override
    protected int compareToSameClass(Object o, CoordinateSequenceComparator comp) {
        return 0;
    }

    @Override
    protected int getSortIndex() {
        return 0;
    }

    @Override
    public String toString() {
        return "";
    }
}
