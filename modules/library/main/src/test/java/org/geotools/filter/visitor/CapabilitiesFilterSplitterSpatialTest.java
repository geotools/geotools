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

import org.junit.Test;
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

/**
 * 
 * @author Jesse
 * @author ported from PostPreProcessFilterSplittingVisitor at 2.5.2 by Gabriel Roldan
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/test/java/org/geotools/filter/visitor/CapabilitiesFilterSplitterSpatialTest.java $
 */
@SuppressWarnings({"nls", "unchecked"})
public class CapabilitiesFilterSplitterSpatialTest extends AbstractCapabilitiesFilterSplitterTests {

    private Geometry geom = new MockGeometryImpl();
    
    @Test
	public void testBBOX() throws Exception{
		// JE:  this test fails but I am not sure if it is a bug or expected behaviour  
		// I wrote this test so it may be correct but it maybe wrong.  Someone that knows should look at this.
		Filter f = ff.bbox(geomAtt, 10, 10, 20, 20, "");
		runTest(f, newCapabilities(BBOX.class), geomAtt);
	}

    @Test
    public void testBEYOND() throws Exception{
		Filter f = ff.beyond(geomAtt, geom, 10, "");
		runTest(f, newCapabilities(Beyond.class), geomAtt);
	}

    @Test
    public void testCONTAINS() throws Exception{
        Filter f = ff.contains(geomAtt, geom);
        runTest(f, newCapabilities(Contains.class), geomAtt);
	}

    @Test
	public void testCROSSES() throws Exception{
        Filter f = ff.crosses(geomAtt, geom);
        runTest(f, newCapabilities(Crosses.class), geomAtt);
	}

    @Test
	public void testDISJOINT() throws Exception{
        Filter f = ff.disjoint(geomAtt, geom);
        runTest(f, newCapabilities(Disjoint.class), geomAtt);
	}

    @Test
	public void tesDWITHINt() throws Exception{
        Filter f = ff.dwithin(geomAtt, geom, 10, "");
        runTest(f, newCapabilities(DWithin.class), geomAtt);
	}

    @Test
	public void testEQUALS() throws Exception{
        Filter f = ff.equals(geomAtt, geom);
        runTest(f, newCapabilities(Equals.class), geomAtt);
	}

    @Test
	public void testINTERSECTS() throws Exception{
        Filter f = ff.intersects(geomAtt, geom);
        runTest(f, newCapabilities(Intersects.class), geomAtt);
	}

    @Test
	public void testOVERLAPS() throws Exception{
        Filter f = ff.overlaps(geomAtt, geom);
        runTest(f, newCapabilities(Overlaps.class), geomAtt);
	}

    @Test
	public void testTOUCHES() throws Exception{
        Filter f = ff.touches(geomAtt, geom);
        runTest(f, newCapabilities(Touches.class), geomAtt);		
	}

    @Test
	public void testWITHIN() throws Exception{
        Filter f = ff.within(geomAtt, geom);
        runTest(f, newCapabilities(Within.class), geomAtt);
	}

}
