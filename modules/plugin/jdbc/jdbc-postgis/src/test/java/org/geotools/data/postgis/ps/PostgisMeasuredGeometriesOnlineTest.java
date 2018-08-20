/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.postgis.PostgisMeasuredGeometriesTestSetup;
import org.geotools.jdbc.JDBCMeasuredGeometriesOnlineTest;

/** Execute measured geometries tests with aPostGIS database. */
public final class PostgisMeasuredGeometriesOnlineTest extends JDBCMeasuredGeometriesOnlineTest {

    @Override
    protected PostgisMeasuredGeometriesTestSetup createTestSetup() {
        return new PostgisMeasuredGeometriesTestSetup(new PostGISPSTestSetup());
    }
}
