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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 *
 * @author jamesm
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/library/main/src/test/java/org/geotools/data/DefaultQueryTest.java $
 */
public class QueryTest extends TestCase {
    
    public QueryTest(java.lang.String testName) {
        super(testName);
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(QueryTest.class);
        return suite;
    }
    
    public void testFullConstructor(){
        Query query = new Query("mytype", Filter.INCLUDE, 10, new String[] {"foo"}, "myquery");
        assertNotNull(query);
    }
    
    /** Test of getPropertyNames method, of class org.geotools.data.Query. */
    public void testPropertyNames() {
        System.out.println("testPropertyNames");
        Query query = new Query();
        assertNull(query.getPropertyNames());
        query.setPropertyNames(new String[]{"foo","bar"});
        String names[] = query.getPropertyNames();
        assertNotNull(names);
        assertEquals("foo",names[0]);
        List list = Arrays.asList(names);
        query.setPropertyNames(list);
        names = query.getPropertyNames();
        
        assertEquals("bar",names[1]);
                
        //test compatibility with getProperties method
        List<PropertyName> properties2 = query.getProperties();
        assertNotNull(properties2);
        assertEquals("foo", properties2.get(0).getPropertyName());
        assertEquals("bar", properties2.get(1).getPropertyName());
        
        query.setPropertyNames(Query.ALL_NAMES);
        assertNull(query.getPropertyNames());
        
        query = new Query( "Test", Filter.INCLUDE, new String[]{"foo", "wibble"});
        assertNotNull(query.getPropertyNames());
    }
    
    /** Test of set/getProperties method, of class org.geotools.data.Query. */
    public void testProperties() {
        final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
         
        System.out.println("testProperties");
        Query query = new Query();
        assertNull(query.getProperties());
        List<PropertyName> properties = new ArrayList<PropertyName>();
        
        NamespaceSupport nsContext = new NamespaceSupport();
        nsContext.declarePrefix("foo", "FooNamespace");
        
        PropertyName fooProp = ff.property("foo", nsContext);
        PropertyName barProp = ff.property("bar", nsContext);
        properties.add(fooProp);
        properties.add(barProp);
        
        query.setProperties(properties);
        
        List<PropertyName> properties2 = query.getProperties();
        assertNotNull(properties);
        assertEquals(fooProp,properties2.get(0));
        assertEquals(barProp,properties2.get(1));
        assertEquals(nsContext,properties2.get(0).getNamespaceContext());
        
        //test compatibility with getPropertyNames method
        String[] names = query.getPropertyNames();  
        assertEquals("foo",names[0]);
        assertEquals("bar",names[1]);
        
        query.setProperties(Query.ALL_PROPERTIES);
        assertNull(query.getProperties());
        
        query = new Query( "Test", Filter.INCLUDE, properties);
        assertNotNull(query.getProperties());
        
    }
    
    
    
    /** Test of retrieveAllProperties method, of class org.geotools.data.Query. */
    public void testRetrieveAllProperties() {
        System.out.println("testRetrieveAllProperties");
        
        Query query = new Query();
        assertTrue(query.retrieveAllProperties());
        
        query.setPropertyNames(new String[]{"foo","bar"});
        assertFalse(query.retrieveAllProperties());
        
        query.setPropertyNames(Query.ALL_NAMES);
        assertTrue(query.retrieveAllProperties());
        
        query.setProperties(Query.ALL_PROPERTIES);
        assertTrue(query.retrieveAllProperties());
        
        query.setPropertyNames(new String[]{"foo","bar"});
        query.setProperties(Query.ALL_PROPERTIES);
        assertTrue(query.retrieveAllProperties());
    }
    
    /** Test of getMaxFeatures method, of class org.geotools.data.Query. */
    public void testMaxFeatures() {
        System.out.println("testMaxFeatures");
        Query query = new Query();
        assertEquals(Query.DEFAULT_MAX, query.getMaxFeatures());
        
        query.setMaxFeatures(5);
        assertEquals(5,query.getMaxFeatures());
    }
    
    
    
    /** Test of getFilter method, of class org.geotools.data.Query. */
    public void testFilter() {
        System.out.println("testGetFilter");
        Query query = new Query();
        query.setFilter(Filter.EXCLUDE);
        assertEquals(Filter.EXCLUDE, query.getFilter());
        
        query = new Query( "test", Filter.INCLUDE);
        assertEquals(Filter.INCLUDE, query.getFilter());
    }
    
    
    
    /** Test of getTypeName method, of class org.geotools.data.Query. */
    public void testTypeName() {
        Query query = new Query();
        assertNull(query.getTypeName());
        
        query.setTypeName("foobar");
        assertEquals("foobar", query.getTypeName());
        
        query = new Query("mytype", Filter.EXCLUDE);
        assertEquals("mytype", query.getTypeName());
    }
    
    
    
    /** Test of getHandle method, of class org.geotools.data.Query. */
    public void testHandle() {
        System.out.println("testGetHandle");
        Query query = new Query();
        assertNull(query.getHandle());
        query.setHandle("myquery");
        assertEquals("myquery", query.getHandle());
    }
    
    
    
    /** Test of getVersion method, of class org.geotools.data.Query. */
    public void testVersion() {
        System.out.println("testGetVersion");
        Query query = new Query();
        assertNull( query.getVersion() );
    }
    
    /** Test of toString method, of class org.geotools.data.Query. */
    public void testToString() {
        System.out.println("testToString");
        Query query = new Query();
        assertNotNull(query.toString());
        
        query.setHandle("myquery");
        assertNotNull(query.toString());
        
        query.setFilter(Filter.EXCLUDE);
        assertNotNull(query.toString());
        
        query.setPropertyNames(new String[]{"foo", "bar"});
        assertNotNull(query.toString());
        
    }
    
    
}
