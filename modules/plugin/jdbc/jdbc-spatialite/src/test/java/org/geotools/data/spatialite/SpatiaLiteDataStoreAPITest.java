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

import java.io.IOException;
import java.util.NoSuchElementException;

import org.geotools.feature.IllegalAttributeException;
import org.geotools.jdbc.JDBCDataStoreAPITest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;



public class SpatiaLiteDataStoreAPITest extends JDBCDataStoreAPITest {

    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new SpatiaLiteDataStoreAPITestSetup();
    }
    
    @Override
    public void testTransactionIsolation() throws Exception {
        //super.testTransactionIsolation();
        //JD: In order to allow multiple connections from the same thread (which this test requires) 
        // we need to put the database in read_uncommitted mode, which means transaction isolation 
        // can not be achieved
    }
    
    @Override
    public void testGetFeatureReaderFilterTransaction() throws NoSuchElementException, IOException,
            IllegalAttributeException {
        //super.testGetFeatureReaderFilterTransaction();
    }

}
