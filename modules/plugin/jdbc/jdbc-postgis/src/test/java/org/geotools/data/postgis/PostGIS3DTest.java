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
package org.geotools.data.postgis;

import org.geotools.jdbc.JDBC3DTest;
import org.geotools.jdbc.JDBC3DTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class PostGIS3DTest extends JDBC3DTest {

    @Override
    protected JDBC3DTestSetup createTestSetup() {
        return new PostGIS3DTestSetup();
    }
    
    @Override
    public void testCreateSchemaAndInsertPolyRectangle() throws Exception {
        // does not work now, see https://jira.codehaus.org/browse/GEOT-4163
    }
    
    @Override
    public void testCreateSchemaAndInsertPolyRectangleWithHole() throws Exception {
        // does not work now, see https://jira.codehaus.org/browse/GEOT-4163    
    }
    
    @Override
    public void testCreateSchemaAndInsertPolyTriangle() throws Exception {
        // does not work now, see https://jira.codehaus.org/browse/GEOT-4163    
    }
    
    @Override
    public void testCreateSchemaAndInsertPolyWithHoleCW() throws Exception {
        // does not work now, see https://jira.codehaus.org/browse/GEOT-4163
    }
    
}
