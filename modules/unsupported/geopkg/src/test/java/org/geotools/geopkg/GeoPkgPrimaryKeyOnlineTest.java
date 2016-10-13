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

import org.geotools.jdbc.JDBCPrimaryKeyOnlineTest;
import org.geotools.jdbc.JDBCPrimaryKeyTestSetup;

/**
 * 
 *
 * @source $URL$
 */
public class GeoPkgPrimaryKeyOnlineTest extends JDBCPrimaryKeyOnlineTest {

    @Override
    protected JDBCPrimaryKeyTestSetup createTestSetup() {
        return new GeoPkgPrimaryKeyTestSetup();
    }
    
    @Override
    public void testSequencedPrimaryKey() throws Exception {
        //SQLite does not do sequences
    }

    @Override
    public void testMultiColumnPrimaryKey() throws Exception {
       //TODO: find out why this multikey only has one column when queried
    }
    
}
