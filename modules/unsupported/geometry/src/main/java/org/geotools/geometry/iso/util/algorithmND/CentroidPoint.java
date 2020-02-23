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
package org.geotools.geometry.iso.util.algorithmND;

import java.util.Iterator;
import org.geotools.geometry.iso.aggregate.MultiPointImpl;
import org.geotools.geometry.iso.complex.CompositePointImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Computes the centroid of a point geometry.
 *
 * <h2>Algorithm</h2>
 *
 * Compute the average of all points.
 */
public class CentroidPoint {

    // private FeatGeomFactoryImpl factory = null;
    private int ptCount = 0;
    DirectPositionImpl centSum = null;

    /** Creates a new Centroid operation */
    public CentroidPoint(CoordinateReferenceSystem crs) {
        this.centSum =
                new DirectPositionImpl(
                        crs); // this.factory.getGeometryFactoryImpl().createDirectPosition();
    }

    /**
     * Adds the point(s) defined by a Geometry to the centroid total. If the geometry is not of
     * dimension 0 it does not contribute to the centroid.
     *
     * @param geom the geometry to add
     */
    public void add(GeometryImpl geom) {
        if (geom instanceof PointImpl) {
            this.add(((PointImpl) geom).getDirectPosition());
        } else if (geom instanceof MultiPointImpl) {
            Iterator<Point> points = ((MultiPointImpl) geom).getElements().iterator();
            while (points.hasNext()) {
                this.add((DirectPositionImpl) points.next().getDirectPosition());
            }
        } else if (geom instanceof CompositePointImpl) {
            this.add((DirectPositionImpl) ((CompositePointImpl) geom).getGenerators().get(0));
        }
    }

    private void add(DirectPositionImpl pt) {
        this.ptCount += 1;
        this.centSum.add(pt.getCoordinate());
    }

    /**
     * Returns the centroid of the added points
     *
     * @return Centroid position
     */
    public DirectPositionImpl getCentroid() {
        this.centSum.divideBy(this.ptCount);
        return this.centSum;
        //		DirectPositionImpl centroid =
        // this.factory.getCoordinateFactory().createDirectPosition();
        //		centroid.setCoordinate(thi);
        //		centroid.setX(this.centSumX / this.ptCount);
        //		centroid.setY(this.centSumY / this.ptCount);
        //		return centroid;
    }
}
