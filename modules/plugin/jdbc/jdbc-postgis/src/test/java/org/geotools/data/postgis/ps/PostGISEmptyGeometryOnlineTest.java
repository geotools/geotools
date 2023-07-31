/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis.ps;

import static org.junit.Assert.assertTrue;

import org.geotools.data.postgis.PostGISEmptyGeometryTestSetup;
import org.geotools.jdbc.JDBCEmptyGeometryOnlineTest;
import org.geotools.jdbc.JDBCEmptyGeometryTestSetup;
import org.locationtech.jts.geom.Geometry;

public class PostGISEmptyGeometryOnlineTest extends JDBCEmptyGeometryOnlineTest {

    @Override
    protected JDBCEmptyGeometryTestSetup createTestSetup() {
        return new PostGISEmptyGeometryTestSetup(new PostGISPSTestSetup());
    }

    @Override
    protected void assertEmptyGeometry(Geometry geometry) {
        // strict, we insert an empty geometry and want one back, not a null
        assertTrue(geometry.isEmpty());
    }
}
