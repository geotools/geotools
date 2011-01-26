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
package org.geotools.data.postgis.ps;

import org.geotools.data.postgis.PostgisUDTTestSetup;
import org.geotools.jdbc.JDBCUDTTest;
import org.geotools.jdbc.JDBCUDTTestSetup;

public class PostgisUDTTest extends JDBCUDTTest {

    @Override
    protected JDBCUDTTestSetup createTestSetup() {
        return new PostgisUDTTestSetup(new PostGISPSTestSetup());
    }

}
