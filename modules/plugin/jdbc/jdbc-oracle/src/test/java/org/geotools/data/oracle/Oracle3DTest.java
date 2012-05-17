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
package org.geotools.data.oracle;

import org.geotools.jdbc.JDBC3DTest;
import org.geotools.jdbc.JDBC3DTestSetup;

public class Oracle3DTest extends JDBC3DTest {

    @Override
    protected JDBC3DTestSetup createTestSetup() {
        return new Oracle3DTestSetup();
    }

    /**
     * This test is overriden to disable it, since it is a known issue.
     * The issue is that the Oracle driver writes Rectangles as Oracle SDO Rectangle structures, which don't preserve 3D
     * See GEOT-4133
     */
    @Override
    public void testCreateSchemaAndInsertPolyRectangle() throws Exception {
    }
    
}
