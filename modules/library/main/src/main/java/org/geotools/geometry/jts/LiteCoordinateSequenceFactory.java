/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;

/**
 * @TODO class description
 *
 * @author jeichar
 * @since 2.1.x
 */
public class LiteCoordinateSequenceFactory implements CoordinateSequenceFactory {

    /* (non-Javadoc)
     * @see org.locationtech.jts.geom.CoordinateSequenceFactory#create(org.locationtech.jts.geom.Coordinate[])
     */
    @Override
    public CoordinateSequence create(Coordinate[] coordinates) {
        return new LiteCoordinateSequence(coordinates);
    }

    /* (non-Javadoc)
     * @see org.locationtech.jts.geom.CoordinateSequenceFactory#create(org.locationtech.jts.geom.CoordinateSequence)
     */
    @Override
    public CoordinateSequence create(CoordinateSequence coordSeq) {
        /* If copying a LiteCoordinateSequence, use the copy constructor to preserve dimensionality information. */
        if (coordSeq instanceof LiteCoordinateSequence)
            return new LiteCoordinateSequence((LiteCoordinateSequence) coordSeq);
        return new LiteCoordinateSequence(coordSeq.toCoordinateArray());
    }

    /* (non-Javadoc)
     * @see org.locationtech.jts.geom.CoordinateSequenceFactory#create(int, int)
     */
    @Override
    public CoordinateSequence create(int size, int dimension) {
        return new LiteCoordinateSequence(size, dimension);
    }

    /** @param points */
    public CoordinateSequence create(double[] points) {
        return new LiteCoordinateSequence(points);
    }

    /** @param points */
    public CoordinateSequence create(double[] points, int dimension) {
        return new LiteCoordinateSequence(points, dimension);
    }

    @Override
    public CoordinateSequence create(int size, int dimension, int measures) {
        return new LiteCoordinateSequence(size, dimension, measures);
    }

    /**
     * Cast to a {@link LiteCoordinateSequence}
     *
     * <p>This method first checks if <tt>cs</tt> is an instanceof {@link LiteCoordinateSequence}, if it is, itself is
     * returned. If not, <tt>cs</tt> is cloned into a new {@link LiteCoordinateSequence}
     *
     * <p>If cs is null, null is returned.
     *
     * @param cs The source {@link CoordinateSequence}. Can be null.
     * @return A LiteCoordinateSequence, or null if <tt>cs</tt> was null.
     */
    public static LiteCoordinateSequence lite(CoordinateSequence cs) {
        if (cs instanceof LiteCoordinateSequence) {
            return (LiteCoordinateSequence) cs;
        } else if (cs == null) {
            return null;
        } else {
            return new LiteCoordinateSequence(cs.toCoordinateArray());
        }
    }
}
