/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.memory;

import org.geotools.data.DataTestCase;
import org.geotools.data.DefaultQuery;
import org.geotools.data.Query;

/**
 * @author Frank Gasdorf, fgdrf@users.sourceforge.net
 *
 *
 * @source $URL$
 */
public class MemoryDataStoreBoundsTest extends DataTestCase {
    MemoryDataStore data;
    
	public MemoryDataStoreBoundsTest(String name) {
		super(name);
	}

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        data = new MemoryDataStore();
        data.addFeatures(roadFeatures);
    }

    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        data = null;
        super.tearDown();
    }

	public void testGetBounds() throws Exception {
        // the Bounds of the queried features should be equal to the bounding 
        // box of the road2 feature, because of the road2 FID filter  
        Query query = new DefaultQuery("road", rd2Filter);
        assertEquals(roadFeatures[1].getBounds(), data.getBounds(query));
    }

}
