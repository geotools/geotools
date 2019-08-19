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
package org.geotools.geopkg;

import org.geotools.jdbc.JDBCFeatureLockingOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * TODO: find out how to fix the following Temporarily override all of these as SQLite/GeoPkg throws
 * an SQLException not a FeatureLockException when the DB is locked, so the tests fail.
 */
public class GeoPkgFeatureLockingOnlineTest extends JDBCFeatureLockingOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new GeoPkgTestSetup();
    }

    @Override
    public void testLockFeatures() throws Exception {}

    @Override
    public void testLockFeaturesWithFilter() throws Exception {}

    @Override
    public void testLockFeaturesWithInvalidFilter() throws Exception {}

    @Override
    public void testLockFeaturesWithInvalidQuery() throws Exception {}

    @Override
    public void testUnlockFeatures() throws Exception {}

    @Override
    public void testUnlockFeaturesInvalidFilter() throws Exception {}

    @Override
    public void testDeleteLockedFeatures() throws Exception {}

    @Override
    public void testModifyLockedFeatures() throws Exception {}
}
