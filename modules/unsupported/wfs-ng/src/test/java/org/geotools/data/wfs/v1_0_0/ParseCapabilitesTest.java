/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.v1_0_0;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.data.ows.WFSCapabilities;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.function.FilterFunction_distance;
import org.geotools.filter.function.FilterFunction_geometryType;
import org.geotools.filter.function.math.FilterFunction_abs;
import org.geotools.xml.DocumentFactory;

/**
 * Test the ability to read a capabilities document.  The emphasis is in making sure that 
 * the Function expressions are constructed correctly.
 * 
 * @author Jesse
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/wfs/src/test/java/org/geotools/data/wfs/v1_0_0/ParseCapabilitesTest.java $
 */
public class ParseCapabilitesTest extends TestCase {

	protected void setUp() throws Exception {
		super.setUp();
	}

	public void testNoSupportedFunctionExpression() throws Exception {
		Map hints=new HashMap();
		InputStream in = TestData.openStream("xml/capabilities/WFSGetCapsNoFunctionExpressions.xml");
        WFSCapabilities obj=(WFSCapabilities) DocumentFactory.getInstance(in, hints, Level.WARNING);
        FilterCapabilities filterCapabilities = obj.getFilterCapabilities();
		assertFalse(filterCapabilities.supports(FilterCapabilities.FUNCTIONS));
		
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_DISJOINT));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_EQUALS));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_DWITHIN));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_BEYOND));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_INTERSECT));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_TOUCHES));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_CROSSES));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_WITHIN));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_CONTAINS));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_OVERLAPS));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_BBOX));
		
		assertTrue(filterCapabilities.supports(FilterCapabilities.LOGICAL));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SIMPLE_ARITHMETIC));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SIMPLE_COMPARISONS));

		assertTrue(filterCapabilities.supports(FilterCapabilities.BETWEEN));
		assertTrue(filterCapabilities.supports(FilterCapabilities.NULL_CHECK));
		assertTrue(filterCapabilities.supports(FilterCapabilities.LIKE));
		
	}
	
	public void testSomeSupportedFunctionExpression() throws Exception {
		Map hints=new HashMap();
		InputStream in = TestData.openStream("xml/capabilities/WFSGetCapsSomeFunctionExpressions.xml");
        WFSCapabilities obj=(WFSCapabilities) DocumentFactory.getInstance(in, hints, Level.WARNING);
        FilterCapabilities filterCapabilities = obj.getFilterCapabilities();
        
		assertTrue("supports functions", filterCapabilities.supports(FilterCapabilities.FUNCTIONS));
		
		assertTrue(filterCapabilities.supports(FilterFunction_distance.class));
		assertFalse(filterCapabilities.supports(FilterFunction_abs.class));
		assertFalse(filterCapabilities.supports(FilterFunction_geometryType.class));

		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_DISJOINT));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_EQUALS));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_DWITHIN));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_BEYOND));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_INTERSECT));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_TOUCHES));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_CROSSES));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_WITHIN));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_CONTAINS));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_OVERLAPS));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_BBOX));
		
		assertTrue(filterCapabilities.supports(FilterCapabilities.LOGICAL));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SIMPLE_ARITHMETIC));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SIMPLE_COMPARISONS));

		assertTrue(filterCapabilities.supports(FilterCapabilities.BETWEEN));
		assertTrue(filterCapabilities.supports(FilterCapabilities.NULL_CHECK));
		assertTrue(filterCapabilities.supports(FilterCapabilities.LIKE));
	}
	
	public void testGeoserverSupportedFunctionExpression() throws Exception {
		Map hints=new HashMap();
		InputStream in = TestData.openStream("xml/capabilities/WFSGetCapsGeoserverFunctionExpressions.xml");
        WFSCapabilities obj=(WFSCapabilities) DocumentFactory.getInstance(in, hints, Level.WARNING);
        FilterCapabilities filterCapabilities = obj.getFilterCapabilities();
        
		boolean supports = filterCapabilities.supports(FilterCapabilities.FUNCTIONS);
        assertTrue("supports functions", supports);
		
		assertTrue("supports distance", filterCapabilities.supports(FilterFunction_distance.class));
		assertTrue("supports filter", filterCapabilities.supports(FilterFunction_abs.class));
		assertTrue("supports geometry", filterCapabilities.supports(FilterFunction_geometryType.class));

		assertTrue("supports disjoint", filterCapabilities.supports(FilterCapabilities.SPATIAL_DISJOINT));
		assertTrue("supports equals", filterCapabilities.supports(FilterCapabilities.SPATIAL_EQUALS));
		assertTrue("supports dwithin", filterCapabilities.supports(FilterCapabilities.SPATIAL_DWITHIN));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_BEYOND));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_INTERSECT));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_TOUCHES));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_CROSSES));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_WITHIN));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_CONTAINS));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_OVERLAPS));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SPATIAL_BBOX));
		
		assertTrue(filterCapabilities.supports(FilterCapabilities.LOGICAL));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SIMPLE_ARITHMETIC));
		assertTrue(filterCapabilities.supports(FilterCapabilities.SIMPLE_COMPARISONS));

		assertTrue(filterCapabilities.supports(FilterCapabilities.BETWEEN));
		assertTrue(filterCapabilities.supports(FilterCapabilities.NULL_CHECK));
		assertTrue(filterCapabilities.supports(FilterCapabilities.LIKE));
	}

}
