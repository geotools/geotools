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
package org.geotools.jdbc;

/**
 * Tests the ability of the datastore to cope with 3D data
 *
 * @author Andrea Aime - OpenGeo
 * @author Martin Davis - OpenGeo
 */
public abstract class JDBCCompound3DOnlineTest extends JDBCGeneric3DOnlineTest {

    protected abstract JDBCCompound3DTestSetup createTestSetup();

    @Override
    protected int getEpsgCode() {
        return 7415;
    }

    @Override
    protected String getLine3d() {
        return "lineCompound3d";
    }

    @Override
    protected String getPoint3d() {
        return "pointCompound3d";
    }

    @Override
    protected String getPoly3d() {
        return "polyCompound3d";
    }
}
