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
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.MultiLineString;

/**
 * Builds a {@link #MultiLineString} using the {@link LineString} made in the parsing process.
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since
 */
final class MultiLineStringBuilder extends GeometryBuilder {

    /** */
    public MultiLineStringBuilder(String statement, BuildResultStack resultStack) {
        super(statement, resultStack);
    }

    /**
     * Makes a {@link #MultiLineString} geometry using the {@link LineString} presents in the result
     * stack.
     */
    @Override
    public Geometry build(int linestringtextNode) throws CQLException {

        List<Geometry> lineList = popGeometry(linestringtextNode);

        LineString[] lineStrings = lineList.toArray(new LineString[lineList.size()]);

        MultiLineString multiLineString = getGeometryFactory().createMultiLineString(lineStrings);

        return multiLineString;
    }
}
