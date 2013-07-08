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
package org.geotools.jdbc;

import java.util.Arrays;
import java.util.List;

/**
 * 
 * @author r0bb3n
 * 
 */
public abstract class JDBCTypeNamesTest extends JDBCTestSupport {
	
	
	@Override
    protected abstract JDBCTypeNamesTestSetup createTestSetup();

	
	public void testTypeNames() throws Exception{

		String[] typeNamesArr = dataStore.getTypeNames();
		assertNotNull("no types found", typeNamesArr);
		List<String> typeNames = Arrays.asList(typeNamesArr);
		assertFalse("no types found", typeNames.isEmpty());
		
		List<String> expected = ((JDBCTypeNamesTestSetup)setup).getExpectedTypeNames();
		
//		assertEquals("number of types unexpected ", expected.size(), typeNames.size());
		
		for (String expectedType : expected) {
			String tn = tname(expectedType);
			assertTrue("type not returned by database: " + tn, typeNames.contains(tn));
		}
		
	}

}
