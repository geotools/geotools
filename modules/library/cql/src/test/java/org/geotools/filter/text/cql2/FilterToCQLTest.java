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


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.temporal.AnyInteracts;
import org.opengis.filter.temporal.Begins;
import org.opengis.filter.temporal.BegunBy;
import org.opengis.filter.temporal.EndedBy;
import org.opengis.filter.temporal.Ends;
import org.opengis.filter.temporal.Meets;
import org.opengis.filter.temporal.MetBy;
import org.opengis.filter.temporal.OverlappedBy;
import org.opengis.filter.temporal.TContains;
import org.opengis.filter.temporal.TEquals;
import org.opengis.filter.temporal.TOverlaps;

/** 
 * FilterToCQLTest
 * 
 * Unit test for {@link FilterToCQL}
 * 
 * @author Johann Sorel
 *
 *
 *
 * @source $URL$
 */
public class FilterToCQLTest{

    @Test
    public void testSample() throws Exception {
        Filter filter = CQL.toFilter(FilterCQLSample.LESS_FILTER_SAMPLE);
        FilterToCQL toCQL = new FilterToCQL();
        
        String output = filter.accept( toCQL, null ).toString();
        Assert.assertNotNull( output );
        Assert.assertEquals( FilterCQLSample.LESS_FILTER_SAMPLE, output );
    }
    
    @Test
    public void testNotBetween() throws Exception {
        cqlTest( "NOT (ATTR1 BETWEEN 10 AND 20)" );
    }
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
    	
    	cqlTest("attr AFTER 2006-12-31T01:30:00");
    }
    
    @Test 
    public void testBefore() throws Exception{
    	
    	cqlTest("attr BEFORE 2006-12-31T01:30:00");
    }

    @Test
    public void testBeforeAndAfter() throws Exception{
    	
    	cqlTest("(dateAttr AFTER 2006-10-10T01:30:00 AND dateAttr BEFORE 2010-12-31T01:30:00)");
    }

    @Test
    public void testDuring() throws Exception{
    	
    	cqlTest("dateAttr DURING 2006-10-10T01:30:00/2010-12-31T01:30:00");
    }
    
    @Test (expected=UnsupportedOperationException.class) 
    public void testEndedByUnsuported() throws Exception{
    	
    	
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	EndedBy filter = ff.endedBy(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }

    @Test (expected=UnsupportedOperationException.class) 
    public void testEndsUnsuported() throws Exception{
    	
    	
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	Ends filter = ff.ends(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }

    @Test (expected=UnsupportedOperationException.class) 
    public void testMeetsUnsuported() throws Exception{
    	
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	Meets filter = ff.meets(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }

    @Test (expected=UnsupportedOperationException.class) 
    public void testMetByUnsuported() throws Exception{
    	
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	MetBy filter = ff.metBy(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }

    @Test (expected=UnsupportedOperationException.class) 
    public void testOverlappedByUnsuported() throws Exception{
    	
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	OverlappedBy filter = ff.overlappedBy(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }

    @Test (expected=UnsupportedOperationException.class) 
    public void testTContainsUnsuported() throws Exception{
    	
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	TContains filter = ff.tcontains(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }

    @Test (expected=UnsupportedOperationException.class) 
    public void testTEqualsUnsuported() throws Exception{
    	
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	TEquals filter = ff.tequals(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }


    @Test (expected=UnsupportedOperationException.class) 
    public void testTOverlapsUnsuported() throws Exception{
    	
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	TOverlaps filter = ff.toverlaps(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }

    protected Literal newSampleDate() throws ParseException{

    	SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date dateTime = dateFormatter.parse("2006-11-30T01:30:00");
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);

    	return ff.literal(dateTime);
    }

    @Test (expected=UnsupportedOperationException.class) 
    public void testBeginsUnsuported() throws Exception{
    	
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	Begins filter = ff.begins(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }

    @Test (expected=UnsupportedOperationException.class) 
    public void testBegunByUnsuported() throws Exception{
    	
    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	BegunBy filter = ff.begunBy(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }
    
    @Test (expected=UnsupportedOperationException.class) 
    public void testAnyInteractsUnsuported() throws Exception{

    	FilterFactory ff = CommonFactoryFinder.getFilterFactory((Hints) null);
    	AnyInteracts filter = ff.anyInteracts(ff.property("date"), newSampleDate());
    	
        FilterToCQL toCQL = new FilterToCQL();
        filter.accept( toCQL, null ).toString();
    }

    @Test
    public void testIntersects() throws Exception{
    	
    	cqlTest("INTERSECTS(the_geom, POINT (1 2))");
    }

    protected void cqlTest( String cql ) throws Exception {
        Filter filter = CQL.toFilter(cql);
        Assert.assertNotNull( filter );
        
        FilterToCQL toCQL = new FilterToCQL();
        String output = filter.accept( toCQL, null ).toString();
        Assert.assertNotNull( output );
        Assert.assertEquals( cql,output );        
    }
    
}
