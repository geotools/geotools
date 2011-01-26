/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import org.geotools.filter.FilterCapabilities;
import org.opengis.filter.Filter;
import org.opengis.filter.spatial.BBOX;
import org.opengis.filter.spatial.Beyond;
import org.opengis.filter.spatial.Contains;
import org.opengis.filter.spatial.Crosses;
import org.opengis.filter.spatial.DWithin;
import org.opengis.filter.spatial.Disjoint;
import org.opengis.filter.spatial.Equals;
import org.opengis.filter.spatial.Intersects;
import org.opengis.filter.spatial.Overlaps;
import org.opengis.filter.spatial.Touches;
import org.opengis.filter.spatial.Within;
import org.opengis.geometry.Geometry;

public class PostPreProcessFilterSplittingVisitorSpatialTest extends AbstractPostPreProcessFilterSplittingVisitorTests {

    private Geometry geom = new MockGeometryImpl();
    
	protected void setUp() throws Exception {
		super.setUp();
        
	}
	
	public void testBBOX() throws Exception{
		// JE:  this test fails but I am not sure if it is a bug or expected behaviour  
		// I wrote this test so it may be correct but it maybe wrong.  Someone that knows should look at this.
		Filter f = ff.bbox(geomAtt, 10, 10, 20, 20, "");
		runTest(f, new FilterCapabilities(BBOX.class), geomAtt);
	}
	public void testBEYOND() throws Exception{
		Filter f = ff.beyond(geomAtt, geom, 10, "");
		runTest(f, new FilterCapabilities(Beyond.class), geomAtt);
	}
	public void testCONTAINS() throws Exception{
        Filter f = ff.contains(geomAtt, geom);
        runTest(f, new FilterCapabilities(Contains.class), geomAtt);
	}
	public void testCROSSES() throws Exception{
        Filter f = ff.crosses(geomAtt, geom);
        runTest(f, new FilterCapabilities(Crosses.class), geomAtt);
	}
	public void testDISJOINT() throws Exception{
        Filter f = ff.disjoint(geomAtt, geom);
        runTest(f, new FilterCapabilities(Disjoint.class), geomAtt);
	}
	public void tesDWITHINt() throws Exception{
        Filter f = ff.dwithin(geomAtt, geom, 10, "");
        runTest(f, new FilterCapabilities(DWithin.class), geomAtt);
	}
	public void testEQUALS() throws Exception{
        Filter f = ff.equals(geomAtt, geom);
        runTest(f, new FilterCapabilities(Equals.class), geomAtt);
	}
	public void testINTERSECTS() throws Exception{
        Filter f = ff.intersects(geomAtt, geom);
        runTest(f, new FilterCapabilities(Intersects.class), geomAtt);
	}
	public void testOVERLAPS() throws Exception{
        Filter f = ff.overlaps(geomAtt, geom);
        runTest(f, new FilterCapabilities(Overlaps.class), geomAtt);
	}
	public void testTOUCHES() throws Exception{
        Filter f = ff.touches(geomAtt, geom);
        runTest(f, new FilterCapabilities(Touches.class), geomAtt);		
	}
	public void testWITHIN() throws Exception{
        Filter f = ff.within(geomAtt, geom);
        runTest(f, new FilterCapabilities(Within.class), geomAtt);
	}

}
