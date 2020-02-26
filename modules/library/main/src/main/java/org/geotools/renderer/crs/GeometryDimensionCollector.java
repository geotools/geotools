/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.*;

/** Collects component of a given geometry that have the desired dimension */
public class GeometryDimensionCollector implements GeometryComponentFilter {

    int targetDimension;
    List<Geometry> geometries = new ArrayList<>();

    public GeometryDimensionCollector(int targetDimension) {
        this.targetDimension = targetDimension;
    }

    @Override
    public void filter(Geometry geom) {
        if (!(geom instanceof GeometryCollection)
                && geom.getDimension() == targetDimension
                && !geom.isEmpty()) {
            geometries.add(geom);
        }
    }

    /**
     * Returns the collected elements as either:
     *
     * <ul>
     *   <li>null: in case the colletion is empty
     *   <li>single geometry: in case there is a single item
     *   <li>multi-geometry: in all other cases
     * </ul>
     */
    public Geometry collect() {
        if (geometries.isEmpty()) {
            return null;
        } else if (geometries.size() == 1) {
            return geometries.get(0);
        } else {
            GeometryFactory factory = geometries.get(0).getFactory();
            if (targetDimension == 0) {
                return factory.createMultiPoint(geometries.toArray(new Point[geometries.size()]));
            } else if (targetDimension == 1) {
                return factory.createMultiLineString(
                        geometries.toArray(new LineString[geometries.size()]));
            } else if (targetDimension == 2) {
                return factory.createMultiPolygon(
                        geometries.toArray(new Polygon[geometries.size()]));
            }
        }

        return null;
    }
}
