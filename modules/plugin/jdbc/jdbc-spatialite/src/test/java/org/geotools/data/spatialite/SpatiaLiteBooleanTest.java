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
package org.geotools.data.spatialite;

import org.geotools.jdbc.JDBCBooleanTest;
import org.geotools.jdbc.JDBCBooleanTestSetup;

public class SpatiaLiteBooleanTest extends JDBCBooleanTest {

    @Override
    protected JDBCBooleanTestSetup createTestSetup() {
        return new SpatiaLiteBooleanTestSetup();
    }
    
    //sqlite does not do booleans, they are actually just varchars
    // TODO: figure out a way to this and reenable these tests 
    @Override
    public void testGetFeatures() throws Exception {
        
    }
    
    @Override
    public void testGetSchema() throws Exception {
        
    }

}
