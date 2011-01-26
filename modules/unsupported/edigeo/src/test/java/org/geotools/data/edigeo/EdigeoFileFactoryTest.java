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

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class EdigeoFileFactoryTest extends TestCase {

	private File f = null;

	/**
	 * DOCUMENT ME!
	 * 
	 * @param args
	 *            DOCUMENT ME!
	 * 
	 * @throws Exception
	 *             DOCUMENT ME!
	 */
	public static void main(java.lang.String[] args) throws Exception {
		junit.textui.TestRunner.run(new TestSuite(EdigeoFileFactoryTest.class));
	}

	@Before
	protected void setUp() throws Exception {
		// TODO Auto-generated method stub
		super.setUp();
	}

	@After
	protected void tearDown() throws Exception {
		// TODO Auto-generated method stub
		if (f != null) {
			f.delete();
			f = null;
		}
		super.tearDown();
	}

	@Test
	public void testSetFile() {
		// Test existing file
		try {
			assertTrue("E000AB01.THF is an existing file",EdigeoFileFactory.setFile(
					EdigeoTestUtils.fileName("E000AB01.THF"), "thf", true).exists());
		} catch (FileNotFoundException e) {
			assertFalse(e.getMessage(),true);
		}
		
		// Test file is a directory
		try {
			EdigeoFileFactory.setFile(EdigeoTestUtils.fileName(""), "THF", true);
			assertFalse(true);
		} catch (FileNotFoundException e) {
			assertTrue(e.getMessage(), true);
		}
		
		// Test with an unexisting file
		try {
			EdigeoFileFactory.setFile(EdigeoTestUtils.fileName("unexistingFile.THF"), "THF", true);
			assertFalse(true);
		} catch (FileNotFoundException e) {
			assertTrue(e.getMessage(), true);
		}
		
		// Test bad extension file
		try {
			EdigeoFileFactory.setFile(EdigeoTestUtils.fileName("E000AB01"), "test", true);
			assertFalse(true);
		} catch (FileNotFoundException e) {
			assertTrue(e.getMessage(), true);
		}
		
	}
}

