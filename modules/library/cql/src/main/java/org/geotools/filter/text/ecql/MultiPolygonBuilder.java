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

package org.geotools.filter.text.ecql;

import java.util.List;
import org.geotools.filter.text.commons.BuildResultStack;
import org.geotools.filter.text.cql2.CQLException;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Polygon;

/**
 * Builds the a {@link MultiPolygon} using the {@link Polygon} made in previous steps of parsing
 * process.
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since
 */
final class MultiPolygonBuilder extends GeometryBuilder {

    /** */
    public MultiPolygonBuilder(String statement, BuildResultStack resultStack) {
        super(statement, resultStack);
    }

    /**
     * Build a {@link MultiPolygon} using the polygon node made in previous steps of the parsing
     * process.
     */
    @Override
    public Geometry build(int polygonNode) throws CQLException {
        List<Geometry> polygonList = popGeometry(polygonNode);

        Polygon[] polygons = polygonList.toArray(new Polygon[polygonList.size()]);

        MultiPolygon multiPolygon = getGeometryFactory().createMultiPolygon(polygons);

        return multiPolygon;
    }
}
