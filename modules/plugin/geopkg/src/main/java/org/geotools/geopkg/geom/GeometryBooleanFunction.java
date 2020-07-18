/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
 * An sqlite function that operates on a Geopackage Geometry BLOB and returns a boolean
 *
 * @author Andrea Aime
 */
public abstract class GeometryBooleanFunction extends Function {

    public abstract boolean execute(GeoPkgGeomReader reader) throws IOException, SQLException;

    @Override
    protected void xFunc() throws SQLException {
        try {
            result(execute(new GeoPkgGeomReader(value_blob(0))) ? 1 : 0);
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}
