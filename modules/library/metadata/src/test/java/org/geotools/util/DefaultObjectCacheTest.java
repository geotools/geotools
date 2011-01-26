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
package org.geotools.util;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests the {@link DefaultObjectCache} with simple tests.
 *
 * @author Cory Horner
 *
 * @source $URL$
 */
public final class DefaultObjectCacheTest {
    /**
     * Tests with two values.
     */
    @Test
    public void testSimple() {
        Integer  key1 = 1;
        Integer  key2 = 2;
        String value1 = new String("value 1");
        String value2 = new String("value 2");

        ObjectCache cache = new DefaultObjectCache();
        assertNotNull(cache);
        assertEquals(null, cache.get(key1));

        cache.writeLock(key1);
        cache.put(key1, value1);
        cache.writeUnLock(key1);
        assertEquals(value1, cache.get(key1));
        assertEquals(null,   cache.get(key2));
        
        //this is 2 because a call to get adds and item to the 
        //cache
        assertEquals(2, cache.getKeys().size());
        assertTrue(cache.getKeys().contains(key1));
    }
    
    /**
     * Tests remove function
     */
    @Test
    public void testRemove(){
    	Integer key1 = 1;
    	Integer key2 = 2;
    	String value1 = new String("value 1");
    	String value2 = new String("value 2");
    	
    	ObjectCache cache = new DefaultObjectCache();
    	assertNotNull(cache);
    	assertEquals(null, cache.get(key1));
    	assertEquals(1, cache.getKeys().size());
    	
//    	try{
//    		cache.writeLock(key1);
    		cache.remove(key1);
//    	}finally{
//    		cache.writeUnLock(key1);
//    	}
    	
    	assertEquals(0, cache.getKeys().size());
    	
    	cache.put(key1, value1);
    	assertEquals(1, cache.getKeys().size());
    	cache.remove(key1);
    	assertEquals(0, cache.getKeys().size());
    }
}
