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

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import junit.framework.TestCase;
import junit.framework.TestSuite;

public class EdigeoParserTest extends TestCase {
	
	private EdigeoParser ep = null;
	
	/**
     * DOCUMENT ME!
     *
     * @param args DOCUMENT ME!
     *
     * @throws Exception DOCUMENT ME!
     */
    public static void main(java.lang.String[] args) throws Exception {
        junit.textui.TestRunner.run(new TestSuite(EdigeoParserTest.class));
    }
    
	@Before
	protected void setUp() throws Exception {
		super.setUp();
		ep = new EdigeoParser(new File(EdigeoTestUtils.fileName("E000AB01.THF")));
	}
	
	@After
	protected void tearDown() throws Exception {
		ep.close();
		super.tearDown();
	}
	
	/*
     * Class under test for boolean readLine()
     */
    @Test
	public void testReadLine() {
        assertTrue (ep.readLine());
        assertEquals("E000AB01.THF", ep.getValue("BOMT"));
        assertTrue(ep.readLine());
        assertTrue(ep.readLine());
        assertEquals("GTS", ep.getValue("RTYSA"));
    }
    
    
}
