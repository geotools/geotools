/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LinearRing;
import org.geotools.data.jdbc.FilterToSQL;
import org.opengis.filter.expression.Literal;

import java.io.IOException;

public class TeradataFilterToSQL extends FilterToSQL {

    public TeradataFilterToSQL(TeradataGISDialect dialect) {
    }

    @Override
    protected void visitLiteralGeometry(Literal expression) throws IOException {
        // evaluate the literal and store it for later
        Geometry geom = (Geometry) evaluateLiteral(expression, Geometry.class);

        if (geom instanceof LinearRing) {
            //teradata does not handle linear rings, convert to just a line string
            geom = geom.getFactory().createLineString(((LinearRing) geom).getCoordinateSequence());
        }

        out.write("'");
        out.write(geom.toText());
        out.write("'");
    }
}
