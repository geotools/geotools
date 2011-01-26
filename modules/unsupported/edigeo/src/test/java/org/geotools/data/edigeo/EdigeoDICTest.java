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
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class EdigeoDICTest extends TestCase {
	private EdigeoDIC eDic ;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		eDic = new EdigeoDIC(EdigeoTestUtils.fileName("EDAB01SE.DIC"));
	}
	
	@After
	protected void tearDown() throws Exception {
		eDic = null;
		super.tearDown();
	}
	
	@Test
	public void testReadDICFile() {
		HashMap<String,String> attIds = null;
		HashMap<String, HashMap<String,String>> attribut = null;
		
		try {
			attIds = new EdigeoSCD(EdigeoTestUtils.fileName("EDAB01SE.SCD")).readSCDFile("PTCANV_id");
			attribut = eDic.readDICFile(attIds);
			
			// PTCAN_id attribute not precoded
			assertTrue("IDU_id attribute values should not be precoded", attribut.get("IDU_id").get("precoded").equals("false"));
			assertTrue("ORI_id attribute values should not be precoded", attribut.get("ORI_id").get("precoded").equals("false"));
			
			// PTCAN_id attribute precoded
			assertTrue("CAN_id attribute values should be precoded", attribut.get("CAN_id").get("precoded").equals("true"));
			assertTrue("MAP_id attribute values should be precoded", attribut.get("MAP_id").get("precoded").equals("true"));
			assertTrue("PALT_id attribute values should be precoded", attribut.get("PALT_id").get("precoded").equals("true"));
			assertTrue("SYM_id attribute values should be precoded", attribut.get("SYM_id").get("precoded").equals("true"));
			assertTrue("PPLN_id attribute values should be precoded", attribut.get("PPLN_id").get("precoded").equals("true"));
			
			// Check some precoded values for different PTCANV_id attributes.
			assertEquals("precoded value 03 should be equal to \"Point de canevas d'ensemble borne\" for SYM_id attribute " +
					"of PTCANV_id Edigeo object","Point de canevas d'ensemble borne", attribut.get("SYM_id").get("03"));
			assertEquals("precoded value 05 should be equal to \"Piquet\" for MAP_id attribute " +
					"of PTCANV_id Edigeo object","Piquet", attribut.get("MAP_id").get("05"));
			assertEquals("precoded value 05 should be equal to \"Departement\" for CAN_id attribute " +
					"of PTCANV_id Edigeo object","Departement", attribut.get("CAN_id").get("05"));
			
		} catch (IOException e) {
			assertFalse(e.getMessage(), true);
		}
		
		attIds = null;
		attribut = null;
	}
	
}
