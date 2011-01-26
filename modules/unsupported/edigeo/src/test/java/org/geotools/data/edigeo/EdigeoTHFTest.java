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

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;

public class EdigeoTHFTest extends TestCase {
	private EdigeoTHF eThf ;
	
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		eThf = new EdigeoTHF(EdigeoTestUtils.fileName("E000AB01.THF"));
	}
	
	@After
	protected void tearDown() throws Exception {
		eThf = null;
		super.tearDown();
	}
	
	@Test
	public void testReadTHFile() {
		HashMap<String, String> thfValue = null;
		String fname = "EDAB01SE";
		try {
			thfValue = eThf.readTHFile();
			// Edigeo file name for this Edigeo dataset
			assertEquals(fname, thfValue.get("genfname"));
			assertEquals(fname, thfValue.get("geofname"));
			assertEquals(fname, thfValue.get("dicfname"));
			assertEquals(fname, thfValue.get("scdfname"));
			// Vector file number
			assertEquals("4", thfValue.get("nbvec"));
			// Vector file name
			assertEquals("EDAB01T1", thfValue.get("vecfname_T1"));
			assertEquals("EDAB01T2", thfValue.get("vecfname_T2"));
			assertEquals("EDAB01T3", thfValue.get("vecfname_T3"));
			assertEquals("EDAB01S1", thfValue.get("vecfname_S1"));
		} catch (FileNotFoundException e) {
			assertFalse(e.getMessage(), true);
		}
		thfValue = null;
		fname = null;
	}
	
}
