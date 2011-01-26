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
package org.geotools.data.db2;

import org.geotools.jdbc.JDBCFeatureSourceTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


public class DB2FeatureSourceTest extends JDBCFeatureSourceTest {
    protected JDBCTestSetup createTestSetup() {
        return new DB2TestSetup();
    }

    protected boolean areCRSEqual(CoordinateReferenceSystem crs1, CoordinateReferenceSystem crs2) {
    	if (crs1==null && crs2==null)
    		return true;
    	
    	if (crs1==null ) return false;
    	if (crs2==null ) return false;
    	
    	String name1 =crs1.getName().toString();
    	String name2 =crs2.getName().toString();
    	
    	return (name1.contains("WGS") && name1.contains("84") &&
    		name2.contains("WGS") && name2.contains("84"));
   	}
    
    
}
