/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2006  Vivid Solutions
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
package org.geotools.geometry.iso.operation.relate;

import org.geotools.geometry.iso.UnsupportedDimensionException;
import org.geotools.geometry.iso.operation.GeometryGraphOperation;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.geometry.iso.topograph2D.IntersectionMatrix;
import org.opengis.geometry.Geometry;

/** Implements the relate() operation on {@link Geometry}s. */
public class RelateOp extends GeometryGraphOperation {

    public static IntersectionMatrix relate(GeometryImpl a, GeometryImpl b)
            throws UnsupportedDimensionException {

        RelateOp relOp = new RelateOp(a, b);
        IntersectionMatrix im = relOp.getIntersectionMatrix();
        return im;
    }

    private RelateComputer relate;

    /**
     * Creates a Relate Operation for the two given geometries and construct a noded graph between
     * those two geometry objects
     */
    public RelateOp(GeometryImpl g0, GeometryImpl g1) throws UnsupportedDimensionException {
        super(g0, g1);
        this.relate = new RelateComputer(super.arg);
    }

    /**
     * Returns the Dimension Extended 9 Intersection Matrix (DE-9-IM) for the two geometry objects
     *
     * @return Intersection Matrix
     */
    public IntersectionMatrix getIntersectionMatrix() {
        return this.relate.computeIM();
    }
}
