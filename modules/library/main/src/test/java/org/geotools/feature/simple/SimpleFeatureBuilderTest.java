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
package org.geotools.feature.simple;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class SimpleFeatureBuilderTest extends TestCase {

	SimpleFeatureBuilder builder;
	
	protected void setUp() throws Exception {
		SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
		typeBuilder.setName( "test" );
		typeBuilder.add( "point", Point.class );
		typeBuilder.add( "integer", Integer.class );
		typeBuilder.add( "float", Float.class );
		
		SimpleFeatureType featureType = typeBuilder.buildFeatureType();
		
		builder = new SimpleFeatureBuilder(featureType);
		builder.setValidating(true);
	}
	
	public void testSanity() throws Exception {
		GeometryFactory gf = new GeometryFactory();
		builder.add( gf.createPoint( new Coordinate( 0, 0 ) ) );
		builder.add( new Integer( 1 ) );
		builder.add( new Float( 2.0 ) );
		
		SimpleFeature feature = builder.buildFeature( "fid" );
		assertNotNull( feature );
		
		assertEquals( 3, feature.getAttributeCount() );
		
		assertTrue( gf.createPoint( new Coordinate( 0, 0) ).equals( (Geometry) feature.getAttribute( "point" ) ) );
		assertEquals( new Integer( 1 ) , feature.getAttribute( "integer" ) );
		assertEquals( new Float( 2.0 ) , feature.getAttribute( "float" ) );
	}
	
	public void testTooFewAttributes() throws Exception {
	    GeometryFactory gf = new GeometryFactory();
        builder.add( gf.createPoint( new Coordinate( 0, 0 ) ) );
        builder.add( new Integer( 1 ) );
        
        SimpleFeature feature = builder.buildFeature( "fid" );
        assertNotNull( feature );
        
        assertEquals( 3, feature.getAttributeCount() );
        
        assertTrue( gf.createPoint( new Coordinate( 0, 0) ).equals( (Geometry) feature.getAttribute( "point" ) ) );
        assertEquals( new Integer( 1 ) , feature.getAttribute( "integer" ) );
        assertNull( feature.getAttribute( "float" ) );
	}
	
	public void testSetSequential() throws Exception {
        GeometryFactory gf = new GeometryFactory();
        builder.set( "point", gf.createPoint( new Coordinate( 0, 0 ) ) );
        builder.set( "integer", new Integer( 1 ) );
        builder.set( "float",  new Float( 2.0 ) );
        
        SimpleFeature feature = builder.buildFeature( "fid" );
        assertNotNull( feature );
        
        assertEquals( 3, feature.getAttributeCount() );
        
        assertTrue( gf.createPoint( new Coordinate( 0, 0) ).equals( (Geometry) feature.getAttribute( 0 ) ) );
        assertEquals( new Integer( 1 ) , feature.getAttribute( 1 ) );
        assertEquals( new Float( 2.0 ) , feature.getAttribute( 2 ) );
	}
	
	public void testSetNonSequential() throws Exception {
	    GeometryFactory gf = new GeometryFactory();
	    builder.set( "float",  new Float( 2.0 ) );
	    builder.set( "point", gf.createPoint( new Coordinate( 0, 0 ) ) );
        builder.set( "integer", new Integer( 1 ) );
        
        SimpleFeature feature = builder.buildFeature( "fid" );
        assertNotNull( feature );
        
        assertEquals( 3, feature.getAttributeCount() );
        
        assertTrue( gf.createPoint( new Coordinate( 0, 0) ).equals( (Geometry) feature.getAttribute( 0 ) ) );
        assertEquals( new Integer( 1 ) , feature.getAttribute( 1 ) );
        assertEquals( new Float( 2.0 ) , feature.getAttribute( 2 ) );
	}
	
	public void testSetTooFew() throws Exception {
	    builder.set("integer", new Integer(1));
	    SimpleFeature feature = builder.buildFeature( "fid" );
        assertNotNull( feature );
        
        assertEquals( 3, feature.getAttributeCount() );
        
        assertNull( feature.getAttribute( 0 ) );
        assertEquals( new Integer( 1 ) , feature.getAttribute( 1 ) );
        assertNull( feature.getAttribute( 2 ) );
	}
	
	public void testConverting() throws Exception {
	    builder.set( "integer", "1" );
	    SimpleFeature feature = builder.buildFeature("fid");
	    
	    try {
	        builder.set( "integer", "foo" );    
	        fail( "should have failed" );
	    }
	    catch( Exception e ) {}
	    
	}
	
	public void testCreateFeatureWithLength() throws Exception {

	    SimpleFeatureTypeBuilder builder=new SimpleFeatureTypeBuilder(); //$NON-NLS-1$
        builder.setName("test");
        builder.length(5).add("name", String.class);
        
        SimpleFeatureType featureType = builder.buildFeatureType();
        SimpleFeature feature = SimpleFeatureBuilder.build( featureType, new Object[]{"Val"}, "ID" );
        
        assertNotNull(feature);
        
        try{
            feature = SimpleFeatureBuilder.build( featureType, new Object[]{"Longer Than 5"}, "ID" );
            feature.validate();
            fail("this should fail because the value is longer than 5 characters");
        }catch (Exception e) {
            // good
	    }
    } 
	
	public void testCreateFeatureWithRestriction() throws Exception {
	    FilterFactory fac = CommonFactoryFinder.getFilterFactory(null);

	    String attributeName = "string";
	    PropertyIsEqualTo filter = fac.equals(fac.property("."), fac.literal("Value"));

	    SimpleFeatureTypeBuilder builder=new SimpleFeatureTypeBuilder(); //$NON-NLS-1$
	    builder.setName("test");
	    builder.restriction(filter).add(attributeName, String.class);

	    SimpleFeatureType featureType = builder.buildFeatureType();
	    SimpleFeature feature = SimpleFeatureBuilder.build( featureType, new Object[]{"Value"}, "ID" );

	    assertNotNull(feature);
	    
	    try {
	        SimpleFeature sf = SimpleFeatureBuilder.build( featureType, new Object[]{"NotValue"}, "ID" );
	        sf.validate();
	       fail( "PropertyIsEqualTo filter should have failed");
	    }
	    catch( Exception e ) {
	        //good
	    }
	    
    }
}
