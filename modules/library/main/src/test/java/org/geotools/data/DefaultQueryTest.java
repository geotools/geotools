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
 *    
 *    Created on August 16, 2003, 5:10 PM
 */
package org.geotools.data;

import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.opengis.filter.Filter;

/**
 *
 * @author jamesm
 * @source $URL$
 */
public class DefaultQueryTest extends TestCase {
    
    public DefaultQueryTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(DefaultQueryTest.class);
        return suite;
    }
    
    public void testFullConstructor(){
        DefaultQuery query = new DefaultQuery("mytype", Filter.INCLUDE, 10, new String[] {"foo"}, "myquery");
        assertNotNull(query);
    }
    
    /** Test of getPropertyNames method, of class org.geotools.data.DefaultQuery. */
    public void testPropertyNames() {
        System.out.println("testPropertyNames");
        DefaultQuery query = new DefaultQuery();
        assertNull(query.getPropertyNames());
        query.setPropertyNames(new String[]{"foo","bar"});
        String names[] = query.getPropertyNames();
        assertNotNull(names);
        assertEquals("foo",names[0]);
        List list = Arrays.asList(names);
        query.setPropertyNames(list);
        names = query.getPropertyNames();
        
        assertEquals("bar",names[1]);
        query.setPropertyNames((List)null);
        assertNull(query.getPropertyNames());
        
        query = new DefaultQuery( "Test", Filter.INCLUDE, new String[]{"foo", "wibble"});
        assertNotNull(query.getPropertyNames());
    }
    
    
    
    /** Test of retrieveAllProperties method, of class org.geotools.data.DefaultQuery. */
    public void testRetrieveAllProperties() {
        System.out.println("testRetrieveAllProperties");
        
        DefaultQuery query = new DefaultQuery();
        assertTrue(query.retrieveAllProperties());
        
        query.setPropertyNames(new String[]{"foo","bar"});
        assertFalse(query.retrieveAllProperties());
        
        query.setPropertyNames((String[])null);
        assertTrue(query.retrieveAllProperties());
        
        query.setPropertyNames(new String[]{"foo","bar"});
        query.setPropertyNames((List)null);
        assertTrue(query.retrieveAllProperties());
    }
    
    /** Test of getMaxFeatures method, of class org.geotools.data.DefaultQuery. */
    public void testMaxFeatures() {
        System.out.println("testMaxFeatures");
        DefaultQuery query = new DefaultQuery();
        assertEquals(Query.DEFAULT_MAX, query.getMaxFeatures());
        
        query.setMaxFeatures(5);
        assertEquals(5,query.getMaxFeatures());
    }
    
    
    
    /** Test of getFilter method, of class org.geotools.data.DefaultQuery. */
    public void testFilter() {
        System.out.println("testGetFilter");
        DefaultQuery query = new DefaultQuery();
        query.setFilter(Filter.EXCLUDE);
        assertEquals(Filter.EXCLUDE, query.getFilter());
        
        query = new DefaultQuery( "test", Filter.INCLUDE);
        assertEquals(Filter.INCLUDE, query.getFilter());
    }
    
    
    
    /** Test of getTypeName method, of class org.geotools.data.DefaultQuery. */
    public void testTypeName() {
        DefaultQuery query = new DefaultQuery();
        assertNull(query.getTypeName());
        
        query.setTypeName("foobar");
        assertEquals("foobar", query.getTypeName());
        
        query = new DefaultQuery("mytype", Filter.EXCLUDE);
        assertEquals("mytype", query.getTypeName());
    }
    
    
    
    /** Test of getHandle method, of class org.geotools.data.DefaultQuery. */
    public void testHandle() {
        System.out.println("testGetHandle");
        DefaultQuery query = new DefaultQuery();
        assertNull(query.getHandle());
        query.setHandle("myquery");
        assertEquals("myquery", query.getHandle());
    }
    
    
    
    /** Test of getVersion method, of class org.geotools.data.DefaultQuery. */
    public void testVersion() {
        System.out.println("testGetVersion");
        DefaultQuery query = new DefaultQuery();
        assertNull( query.getVersion() );
    }
    
    /** Test of toString method, of class org.geotools.data.DefaultQuery. */
    public void testToString() {
        System.out.println("testToString");
        DefaultQuery query = new DefaultQuery();
        assertNotNull(query.toString());
        
        query.setHandle("myquery");
        assertNotNull(query.toString());
        
        query.setFilter(Filter.EXCLUDE);
        assertNotNull(query.toString());
        
        query.setPropertyNames(new String[]{"foo", "bar"});
        assertNotNull(query.toString());
        
    }
    
    
}
