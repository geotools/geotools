/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.postgis.ps.PostGISPSTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

/**
 * Subclass of {@link PostGISJsonWriterOnlineTest} uses prepared statements for INSERT and UPDATE.
 *
 * @author awaterme
 */
public class PostGISJsonWriterPSOnlineTest extends PostGISJsonWriterOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new PostGISJsonTestSetup(new PostGISPSTestSetup());
    }
}
