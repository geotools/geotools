/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import static org.junit.Assert.assertEquals;

import java.sql.Connection;
import org.geotools.jdbc.JDBCGeometryOnlineTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.junit.Test;

public class PostgisGeometryOnlineTest extends JDBCGeometryOnlineTest {

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        return new PostgisGeometryTestSetup(new PostGISTestSetup());
    }

    @Override
    public void testLinearRing() throws Exception {
        // linear ring type is not a supported type in postgis
    }

    @Test
    public void testDimensionFromFirstGeometry() throws Exception {
        try (Connection cx = dataStore.getDataSource().getConnection()) {
            PostGISDialect dialect = (PostGISDialect) dataStore.getSQLDialect();
            assertEquals((Integer) 0, dialect.getDimensionFromFirstGeo("public", "dim_point", "geom", cx));
            assertEquals((Integer) 1, dialect.getDimensionFromFirstGeo("public", "dim_line", "geom", cx));
            assertEquals((Integer) 2, dialect.getDimensionFromFirstGeo("public", "dim_polygon", "geom", cx));
            assertEquals((Integer) 1, dialect.getDimensionFromFirstGeo("public", "dim_collection", "geom", cx));
        }
    }
}
