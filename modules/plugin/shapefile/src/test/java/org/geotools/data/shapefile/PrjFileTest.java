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
package org.geotools.data.shapefile;

import java.io.IOException;

import org.geotools.data.shapefile.prj.PrjFileReader;

/**
 * 
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/plugin/shapefile/src/test/java/org/geotools/data/shapefile/PrjFileTest.java $
 * @version $Id$
 * @author Ian Schneider
 * @author James Macgill
 */
public class PrjFileTest extends TestCaseSupport {

    static final String TEST_FILE = "wkt/cntbnd01.prj";

    protected PrjFileReader prj = null;

    public PrjFileTest(String testName) throws IOException {
        super(testName);
    }

    protected void setUp() throws Exception {
//        prj = new PrjFileReader(new ShpFiles(TEST_FILE));
    }

    protected void tearDown() throws Exception {
//        prj.close();
        super.tearDown();
    }

    public void testIgnoreEmptyTestCaseWarning() {
    }
}
