/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.geom;

import java.io.IOException;
import java.sql.SQLException;
import org.sqlite.Function;

/**
 * An sqlite function that operates on a Geopackage Geometry BLOB.
 *
 * @author Justin Deoliveira
 * @author Niels Charlier
 * @deprecated The implementation forces object wrapping for all primitives, causing generation of lots of short lived
 *     objects.
 */
@Deprecated
public abstract class GeometryFunction extends Function {

    public abstract Object execute(GeoPkgGeomReader reader) throws IOException;

    @Override
    protected void xFunc() throws SQLException {
        if (args() != 1) {
            throw new SQLException("Geometry Function expects one argument.");
        }

        Object res;
        try {
            res = execute(new GeoPkgGeomReader(value_blob(0)));
        } catch (IOException e) {
            throw new SQLException(e);
        }

        if (res == null) {
            result();
        } else if (res instanceof Integer integer) {
            result(integer);
        } else if (res instanceof Double double1) {
            result(double1);
        } else if (res instanceof String string) {
            result(string);
        } else if (res instanceof Long long1) {
            result(long1);
        } else if (res instanceof byte[] bytes) {
            result(bytes);
        } else if (res instanceof Boolean boolean1) {
            result(boolean1 ? 1 : 0);
        }
    }
}
