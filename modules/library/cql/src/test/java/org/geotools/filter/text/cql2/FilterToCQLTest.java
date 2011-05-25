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
package org.geotools.filter.text.cql2;


import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.filter.Filter;

/** 
 * FilterToCQLTest
 * 
 * Unit test for FilterToCQL
 * 
 * @author Johann Sorel
 *
 *
 * @source $URL$
 */
public class FilterToCQLTest{

    FilterToCQL toCQL = new FilterToCQL();
    

    @Test
    public void testSample() throws Exception {
        Filter filter = CQL.toFilter(FilterCQLSample.LESS_FILTER_SAMPLE);
        
        String output = filter.accept( toCQL, null ).toString();
        Assert.assertNotNull( output );
        Assert.assertEquals( FilterCQLSample.LESS_FILTER_SAMPLE, output );
    }
    /* NOT (ATTR1 BETWEEN 10 AND 20) */
    @Test
    public void testNotBetween() throws Exception {
        cqlTest( "NOT (ATTR1 BETWEEN 10 AND 20)" );
    }
    /* ((ATTR1 < 10 AND ATTR2 < 2) OR ATTR3 > 10) */
    @Test
    public void testANDOR() throws Exception {
        cqlTest( "((ATTR1 < 10 AND ATTR2 < 2) OR ATTR3 > 10)" );
    }
    /** (ATTR1 > 10 OR ATTR2 < 2) */
    @Test
    public void testOR() throws Exception {
        cqlTest( "(ATTR1 > 10 OR ATTR2 < 2)" );
    }

    @Test
    public void testLike() throws Exception {
        cqlTest( "ATTR1 LIKE '%ABC%'" );
    }

    @Test
    public void testBbox() throws Exception {
    	cqlTest( "BBOX(the_geom, 10.0,20.0,30.0,40.0)" );
	}
    
    @Test 
    public void testAfter() throws Exception{
    	
    	cqlTest("attr AFTER 2006-12-31T01:30:00Z");
    }
    
    @Test 
    public void testBefore() throws Exception{
    	
    	cqlTest("attr BEFORE 2006-12-31T01:30:00Z");
    }

    @Test
    public void testBeforeAndAfter() throws Exception{
    	
    	cqlTest("(dateAttr AFTER 2006-10-10T01:30:00Z AND dateAttr BEFORE 2010-12-31T01:30:00Z)");
    }

    /**
     * FIXME This require an implementation. The "and" filter should be modified to take into account that 
     * a filter with dates should be transformed in a "during" predicate.
     * 
     * @throws Exception
     */
    @Ignore
    public void testDuring() throws Exception{
    	
    	cqlTest("dateAttr DURING 2006-10-10T01:30:00Z/2010-12-31T01:30:00Z");
    }
    
    
    
    protected void cqlTest( String cql ) throws Exception {
        Filter filter = CQL.toFilter(cql);
        Assert.assertNotNull( cql + " parse", filter );
        
        String output = filter.accept( toCQL, null ).toString();
        Assert.assertNotNull( cql + " encode", output );
        Assert.assertEquals( cql, cql,output );        
    }
    
    
    
}
