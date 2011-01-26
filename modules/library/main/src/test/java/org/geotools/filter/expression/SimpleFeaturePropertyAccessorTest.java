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
package org.geotools.filter.expression;

import junit.framework.TestCase;

import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class SimpleFeaturePropertyAccessorTest extends TestCase {

	SimpleFeatureType type;
	SimpleFeature feature;
	PropertyAccessor accessor = SimpleFeaturePropertyAccessorFactory.ATTRIBUTE_ACCESS;
	
	protected void setUp() throws Exception {
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		
		typeBuilder.setName( "test" );
		typeBuilder.setNamespaceURI( "http://www.geotools.org/test" );
		typeBuilder.add( "foo", Integer.class );
		typeBuilder.add( "bar", Double.class );
		
		type = (SimpleFeatureType) typeBuilder.buildFeatureType();
		
		SimpleFeatureBuilder builder = new SimpleFeatureBuilder(type);
		builder.add( new Integer( 1 ) );
		builder.add( new Double( 2.0 ) );

		feature = (SimpleFeature) builder.buildFeature( "fid" );
		accessor = SimpleFeaturePropertyAccessorFactory.ATTRIBUTE_ACCESS;
	}
	
	
	public void testCanHandle() {
		assertTrue( accessor.canHandle( feature, "foo", null ) );
		assertTrue( accessor.canHandle( feature, "bar", null ) );
		
		assertFalse( accessor.canHandle( feature, "illegal", null ) );
	}
	
	public void testCanHandleType() {
		assertTrue( accessor.canHandle( type, "foo", null ) );
		assertTrue( accessor.canHandle( type, "bar", null ) );
		
		assertFalse( accessor.canHandle( type, "illegal", null ) );
	}
	
	public void testGet() {
		assertEquals( new Integer( 1 ), accessor.get( feature, "foo", null ) );
		assertEquals( new Double( 2.0 ), accessor.get( feature, "bar", null ) );
		assertEquals( "fid", SimpleFeaturePropertyAccessorFactory.FID_ACCESS.get( feature, "@id", null) );
		assertEquals( "fid", SimpleFeaturePropertyAccessorFactory.FID_ACCESS.get( feature, "@gml:id", null) );
                assertFalse( accessor.canHandle( feature, "illegal", null ) );
		assertNull( accessor.get( feature, "illegal", null ) );
	}
	
	public void testGetType() {
		assertEquals( type.getDescriptor( "foo" ), accessor.get( type, "foo", null ) );
		assertEquals( type.getDescriptor( "bar" ), accessor.get( type, "bar", null ) );
		assertNull( accessor.get( type, "illegal", null ) );
	}
	
	public void testSet() {
		try {
			accessor.set( feature, "foo", new Integer( 2 ), null );
		} catch (IllegalAttributeException e) {
			fail();
		}
		assertEquals( new Integer( 2 ), accessor.get( feature, "foo", null ) );
		
		try {
			accessor.set( feature, "bar", new Double( 1.0 ), null );
		} catch (IllegalAttributeException e) {
			fail();
		}
		assertEquals( new Double( 1.0 ), accessor.get( feature, "bar", null ) );
		try {
			accessor.set( feature, "@id", "fid2", null );
			fail( "Should have thrown exception trying to set fid" );
		}
		catch( IllegalAttributeException e ) {
		}
	}
	
	public void testSetType() {
		try {
			accessor.set( type, "foo", new Object(), null );
			fail( "trying to set attribute type should have thrown exception" );
		} catch (IllegalAttributeException e) {}
		
		
	}
	
	public void testGetAnyGeometry() throws Exception {
	    SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
	    tb.setName( "test");
	    tb.add("g1", Point.class );
	    tb.add("g2", Point.class );
	    tb.setDefaultGeometry( "g1" );
	    
	    SimpleFeatureType type = tb.buildFeatureType();
	    
	    SimpleFeatureBuilder b = new SimpleFeatureBuilder(type);
	    b.set("g1", null );
	    
	    Point p = new GeometryFactory().createPoint( new Coordinate(0,0));
	    b.set("g2", p );
	    SimpleFeature feature = b.buildFeature(null);
	    
	    assertNull( feature.getDefaultGeometry() );
	    assertEquals(p, SimpleFeaturePropertyAccessorFactory
	            .DEFAULT_GEOMETRY_ACCESS.get( feature, "", Geometry.class ));
	    
	}
	
}
