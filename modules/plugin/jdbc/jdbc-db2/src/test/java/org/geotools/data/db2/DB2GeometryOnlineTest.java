/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General  License for more details.
 */
package org.geotools.data.db2;

import org.geotools.jdbc.JDBCGeometryOnlineTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;

/** @source $URL$ */
public class DB2GeometryOnlineTest extends JDBCGeometryOnlineTest {

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        return new DB2GeometryTestSetup();
    }

    public void testLinearRing() throws Exception {
        assertEquals(LineString.class, checkGeometryType(LinearRing.class));
    }
}
