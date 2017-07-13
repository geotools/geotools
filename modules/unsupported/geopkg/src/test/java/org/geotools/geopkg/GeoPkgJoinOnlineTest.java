/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCJoinOnlineTest;
import org.geotools.jdbc.JDBCJoinTestSetup;

public class GeoPkgJoinOnlineTest extends JDBCJoinOnlineTest {

    @Override
    protected JDBCJoinTestSetup createTestSetup() {
        return new GeoPkgJoinTestSetup();
    }

    @Override
    public void testSimpleJoin() throws Exception {
        // can't open two data sources from same geopackage
       
    }

    @Override
    public void testSimpleJoinOnPrimaryKey() throws Exception {
     // can't open two data sources from same geopackage
    }

    @Override
    public void testSimpleJoinWithFilterCount() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testJoinMoreThanTwo() throws Exception {
        // TODO Auto-generated method stub
       
    }

    @Override
    public void testJoinSchema() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testSimpleJoinWithSort() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testSpatialJoin() throws Exception {
        // TODO Auto-generated method stub
      //not a supported filter
    }

    @Override
    public void testSimpleJoinInvertedAliases() throws Exception {
        // TODO Auto-generated method stub
       
    }

    @Override
    public void testSimpleJoinWithFilter() throws Exception {
        // TODO Auto-generated method stub
        
    }

    @Override
    public void testSimpleJoinWithLimitOffset() throws Exception {
        // TODO Auto-generated method stub
        
    }
    
}
