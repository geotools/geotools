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
package org.geotools.renderer.lite;

import java.awt.geom.Line2D;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFilter;
import org.locationtech.jts.geom.LineString;

/**
 * Class collecting unclosed straight lines from a geometry. If a non straight line is found, or any
 * other type of geometry is found, the "simple" property will return false. The class is stateful
 * and can be only used once
 *
 * @author Andrea Aime
 */
class LinesExtractor implements GeometryFilter {

    List<Line2D> lines = new ArrayList<>();
    boolean isSimple = true;

    @Override
    public void filter(Geometry geom) {
        if (geom instanceof LineString && ((LineString) geom).getCoordinateSequence().size() == 2) {
            CoordinateSequence cs = ((LineString) geom).getCoordinateSequence();
            lines.add(
                    new Line2D.Double(
                            cs.getOrdinate(0, 0),
                            cs.getOrdinate(0, 1),
                            cs.getOrdinate(1, 0),
                            cs.getOrdinate(1, 1)));
        } else if (!(geom instanceof GeometryCollection)) {
            // collections are not a problem
            isSimple = false;
        }
    }

    public List<Line2D> getLines() {
        return lines;
    }

    public boolean isSimple() {
        return isSimple;
    }
}
