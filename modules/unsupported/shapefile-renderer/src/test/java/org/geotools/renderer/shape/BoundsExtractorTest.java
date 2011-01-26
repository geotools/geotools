/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.shape;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.opengis.feature.simple.SimpleFeatureType;

public class BoundsExtractorTest extends TestCase {
	private SimpleFeatureType schema;
	protected void setUp() throws Exception {
		schema=DataUtilities.createType("type","the_geom:Geometry,name:String");
	}
	public void testBBoxFilter() throws Exception {
//		FilterFactory factory=FilterFactoryFinder.createFilterFactory();
//		
//		GeometryFilter filter=factory.createGeometryFilter(org.geotools.filter.Filter.GEOMETRY_BBOX);
//
//		testFilter(factory, filter);
	}
//	public void testCONTAINSFilter() throws Exception {
//		FilterFactory factory=FilterFactoryFinder.createFilterFactory();
//		
//		GeometryFilter filter=factory.createGeometryFilter(org.geotools.filter.Filter.GEOMETRY_CONTAINS);
//
//		testFilter(factory, filter);
//	}
//	public void testCROSSESFilter() throws Exception {
//		FilterFactory factory=FilterFactoryFinder.createFilterFactory();
//		
//		GeometryFilter filter=factory.createGeometryFilter(org.geotools.filter.Filter.GEOMETRY_CROSSES);
//
//		testFilter(factory, filter);
//	}
//	public void testDWITHINFilter() throws Exception {
//		FilterFactory factory=FilterFactoryFinder.createFilterFactory();
//		
//		GeometryFilter filter=factory.createGeometryFilter(org.geotools.filter.Filter.GEOMETRY_DWITHIN);
//
//		testFilter(factory, filter);
//	}
//	public void testEQUALSFilter() throws Exception {
//		FilterFactory factory=FilterFactoryFinder.createFilterFactory();
//		
//		GeometryFilter filter=factory.createGeometryFilter(org.geotools.filter.Filter.GEOMETRY_EQUALS);
//
//		testFilter(factory, filter);
//	}
//	public void testINTERSECTSFilter() throws Exception {
//		FilterFactory factory=FilterFactoryFinder.createFilterFactory();
//		
//		GeometryFilter filter=factory.createGeometryFilter(org.geotools.filter.Filter.GEOMETRY_INTERSECTS);
//
//		testFilter(factory, filter);
//	}
//	public void testOVERLAPSFilter() throws Exception {
//		FilterFactory factory=FilterFactoryFinder.createFilterFactory();
//		
//		GeometryFilter filter=factory.createGeometryFilter(org.geotools.filter.Filter.GEOMETRY_OVERLAPS);
//
//		testFilter(factory, filter);
//	}
//	public void testTOUCHESFilter() throws Exception {
//		FilterFactory factory=FilterFactoryFinder.createFilterFactory();
//		
//		GeometryFilter filter=factory.createGeometryFilter(org.geotools.filter.Filter.GEOMETRY_TOUCHES);
//
//		testFilter(factory, filter);
//	}
//	public void testWITHINFilter() throws Exception {
//		FilterFactory factory=FilterFactoryFinder.createFilterFactory();
//		
//		GeometryFilter filter=factory.createGeometryFilter(org.geotools.filter.Filter.GEOMETRY_WITHIN);
//
//		testFilter(factory, filter);
//	}
//	
//	private void testFilter(FilterFactory factory, GeometryFilter filter) throws IllegalFilterException {
//		Envelope bbox=new Envelope (10,100,10,100);
//		BBoxExpression bb = factory.createBBoxExpression(bbox);
//		
//		String geomName=schema.getDefaultGeometry().getName();
//		AttributeExpression attr = factory.createAttributeExpression(geomName);
//		
//		filter.addLeftGeometry(attr);
//		filter.addRightGeometry(bb);
//
//		GeometryFilter filter2=factory.createGeometryFilter(org.geotools.filter.Filter.GEOMETRY_BBOX);
//		filter2.addLeftGeometry(attr);
//		Coordinate[] coords=new Coordinate[]{new Coordinate(0,0),new Coordinate(30,0),new Coordinate(25,30)};
//		Geometry geom=new GeometryFactory().createLineString(coords);
//		filter2.addRightGeometry(factory.createLiteralExpression(geom));
//
//		/*
//		 * Extract bounds from F^F2    
//		 */
//		
//		BoundsExtractor extractor=new BoundsExtractor(new Envelope(-10,40, -10,40));
//		filter.and(filter2).accept(extractor);
//		assertEquals(new Envelope(10,30,10,30), extractor.getIntersection());
//
//		/*
//		 * Extract bounds from F|F2    
//		 */
//		extractor=new BoundsExtractor(new Envelope(-10,40, -10,40));
//		filter.or(filter2).accept(extractor);
//		assertEquals(new Envelope(0,40,0,40), extractor.getIntersection());
//		
//		/*
//		 * Extract bounds from ~F    
//		 */
//		extractor=new BoundsExtractor(new Envelope(-10,40, -10,40));
//		filter.not().accept(extractor);
//		assertEquals(new Envelope(-10,40,-10,40), extractor.getIntersection());		
//		
//		/*
//		 * Extract bounds from ~(F^F2)    
//		 */
//		extractor=new BoundsExtractor(new Envelope(-10,40, -10,40));
//		filter.and(filter2).not().accept(extractor);
//		assertEquals(new Envelope(-10,40,-10,40), extractor.getIntersection());
//
//		/*
//		 * Extract bounds from ~(~(F|F2))    
//		 */
//		extractor=new BoundsExtractor(new Envelope(-10,40, -10,40));
//		filter.or(filter2).not().not().accept(extractor);
//		assertEquals(new Envelope(0,40,0,40), extractor.getIntersection());
//		
//		/*
//		 * Extract bounds from ~(F|F2)^F2)=(F|F2|F2)=F|F2 but since F contains F2 ~(F1|F2) is always false so 
//                 * 
//		 */
//		extractor=new BoundsExtractor(new Envelope(-10,40, -10,40));
//		Filter newF=filter.or(filter2).not().and(filter2).not();
//		newF.accept(extractor);
//		assertTrue(new Envelope(0,40,0,40).contains(extractor.getIntersection()));
//		
//		/*
//		 * Disjoint case
//		 * |--| |--|
//		 * |  | |  |
//		 * |--| |--|
//		 */
//		extractor=new BoundsExtractor(-10,0,-10,0);
//		filter.accept(extractor);
//		assertTrue(extractor.getIntersection().isNull());
//	
//		/*
//		 * Contains case
//		 * |------|
//		 * | |--| |
//		 * | |  | |
//		 * | |--| |
//		 * |------|
//		 */
//		extractor=new BoundsExtractor(new Envelope(0,110, 0,110));
//		filter.accept(extractor);
//		assertEquals(new Envelope(10,100,10,100), extractor.getIntersection());
//
//		/*
//		 *  |------|
//		 *|---|    |
//		 *| | |    |
//		 *|---|    |
//		 *  |------|
//		 */
//		extractor=new BoundsExtractor(new Envelope(0,20, 20,90));
//		filter.accept(extractor);
//		assertEquals(new Envelope(10,20,20,90), extractor.getIntersection());
//
//		/*
//		 *|---|
//		 *| |-|----|
//		 *| | |    |
//		 *| | |    |
//		 *| | |    |
//		 *| |-|----|
//		 *|---|
//		 */
//		extractor=new BoundsExtractor(new Envelope(0,20, 0,110));
//		filter.accept(extractor);
//		assertEquals(new Envelope(10,20,10,100), extractor.getIntersection());
//
//		/*
//		 *    |---|
//		 *  |-|---|-|
//		 *  | |---| |
//		 *  |       |
//		 *  |       |
//		 *  |-------|
//		 *     
//		 */
//		extractor=new BoundsExtractor(new Envelope(20,90, 90,110));
//		filter.accept(extractor);
//		assertEquals(new Envelope(20,90,90,100), extractor.getIntersection());
//
//		/*
//		 *|-----------|
//		 *| |-------| |
//		 *|-----------|
//		 *  |       |
//		 *  |       |
//		 *  |-------|
//		 *     
//		 */
//		extractor=new BoundsExtractor(new Envelope(0,110, 90,110));
//		filter.accept(extractor);
//		assertEquals(new Envelope(10,100,90,100), extractor.getIntersection());
//
//		/*
//		 *         
//		 *  |-------|
//		 *  |     |---|
//		 *  |     | | |
//		 *  |     |---|
//		 *  |-------|
//		 *     
//		 */
//		extractor=new BoundsExtractor(new Envelope(90,110, 20,90));
//		filter.accept(extractor);
//		assertEquals(new Envelope(90,100,20,90), extractor.getIntersection());
//
//		/*
//		 *        |---|
//		 *  |-----|-| |
//		 *  |     | | |
//		 *  |     | | |
//		 *  |-----|-| |
//		 *        |---|
//		 */
//		extractor=new BoundsExtractor(new Envelope(90,100, 0,110));
//		filter.accept(extractor);
//		assertEquals(new Envelope(90,100,10,100), extractor.getIntersection());
//
//		/*
//		 *             
//		 *  |-------|  
//		 *  |       |  
//		 *  | |---| |  
//		 *  |-|---|-|  
//		 *    |---|    
//		 */
//		extractor=new BoundsExtractor(new Envelope(20,90, 0,20));
//		filter.accept(extractor);
//		assertEquals(new Envelope(20,90,10,20), extractor.getIntersection());
//		
//
//		/*
//		 *             
//		 *  |-------|  
//		 *  |       |  
//		 *|-----------|
//		 *| |-------| |
//		 *|---------- |
//		 */
//		extractor=new BoundsExtractor(new Envelope(0,110, 0,20));
//		filter.accept(extractor);
//		assertEquals(new Envelope(10,100,10,20), extractor.getIntersection());
//
//		/*
//		 *             
//		 *  |-------|  
//		 *|-----------|
//		 *|-----------|
//		 *  |-------| 
//		 *
//		 */
//		extractor=new BoundsExtractor(new Envelope(0,110, 20,90));
//		filter.accept(extractor);
//		assertEquals(new Envelope(10,100,20,90), extractor.getIntersection());
//
//		/*
//		 *    |---|    
//		 *  |-|---|-|  
//		 *  | |   | |  
//		 *  | |   | |   
//		 *  |-|---|-|  
//		 *    |---|    
//		 */
//		extractor=new BoundsExtractor(new Envelope(20,90,0,110));
//		filter.accept(extractor);
//		assertEquals(new Envelope(20,90,10,100), extractor.getIntersection());
//		
//	}

}
