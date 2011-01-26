/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
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
package org.geotools.data.edigeo;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataStore;
import org.geotools.data.DataAccessFactory.Param;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

import junit.framework.TestCase;

public class EdigeoDataStoreFactoryTest extends TestCase {
	private EdigeoDataStoreFactory dsFactory ;
	private Map<String, Serializable> params;
	
	@Before protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
		dsFactory = new EdigeoDataStoreFactory();
		params = new HashMap<String, Serializable>();
		params.put(EdigeoDataStoreFactory.PARAM_PATH.key, EdigeoTestUtils.fileName("E000AB01.THF"));
		params.put(EdigeoDataStoreFactory.PARAM_OBJ.key, "COMMUNE_id");
	}
	
	@After protected void tearDown() throws Exception {
		dsFactory = null;
		super.tearDown();
	}
	
	@Test public void testGetDisplayName() {
		assertEquals("EdigeoDataStore", dsFactory.getDisplayName());
	}
	
	@Test public void testGetDescription() {
        assertEquals("EDIGéO format files (*.thf)",dsFactory.getDescription());
    }
	
	@Test public void testGetParametersInfo() {
		Param[] param = dsFactory.getParametersInfo();
		assertNotNull(param);		
	}
	
	@Test public void testGetImplementationHints() {
		assertNotNull(dsFactory.getImplementationHints());
	}
	
	public void testCreateDataStore() {
		DataStore ds = null;
		try {
			ds = dsFactory.createDataStore(params);
			SimpleFeatureType schema = ds.getSchema("E000AB01") ;
			List<AttributeDescriptor> attributes = schema.getAttributeDescriptors();
	        assertEquals("Number of Attributes", 3, attributes.size());
		} catch (IOException e) {
			fail(e.getMessage());
		}
		assertNotNull(ds);
        assertEquals(true, ds.getClass() == EdigeoDataStore.class);
	}
}
