/*
 *    GeoTools - The Open Source Java GIS Tookit
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
package org.geotools.filter.text.ecql;

import java.util.Set;

import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.filter.Filter;
import org.opengis.filter.Id;
import org.opengis.filter.Not;

/**
 * 
 * Test for ID Predicate
 * <pre>
 * &lt id predicate &gt ::= [ "NOT" ] "IN " "(" &lt id value &gt {,&lt id value &gt } ")"
 * 
 * Sample: IN ( 'states.1', 'states.2', 'states.3')
 * </pre>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 * @source $URL$
 */
public class ECQLIDPredicateTest {
    
    /**
     * Sample: IN( 'states.1', 'states.2', 'states.3'
     * </pre>
     * @throws Exception
     */
    @Test
    public void filterIdWithListOfIdValues() throws Exception {

        final String strId1 = "states.1";
        final String strId2 = "states.2";
        final String strId3 = "states.3";
        Filter filter = ECQL.toFilter("IN ('" + strId1 + "','" + strId2
                + "', '" + strId3 + "')");
        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof Id);

        Id filterId = (Id) filter;
        Set<?> resultIdentifiers = filterId.getIDs();
        Assert.assertTrue("one id in filter Id was expected",
                resultIdentifiers.size() == 3);

        Assert.assertTrue(strId1 + " was expected", resultIdentifiers.contains(strId1));

        Assert.assertTrue(strId2 + " was expected", resultIdentifiers.contains(strId2));

        Assert.assertTrue(strId3 + " was expected", resultIdentifiers.contains(strId3));
    }
    
    /**
     * <pre>
     * Sample: NOT IN( 'states.1', 'states.2', 'states.3')
     * </pre>
     * @throws Exception
     */
    @Test
    public void notFilterId() throws Exception {

        Filter filter;
        
        final String strId1 = "states.1";
        final String strId2 = "states.2";
        final String strId3 = "states.3";
        filter = ECQL.toFilter("NOT IN ('" + strId1 + "','" + strId2
                + "', '" + strId3 + "')");
        
        Assert.assertNotNull(filter);
        Assert.assertTrue("Not filter was expected",  filter instanceof Not);
        
        Assert.assertTrue(filter instanceof Not);
        Not notFilter = (Not) filter;
        filter = notFilter.getFilter();

        Assert.assertTrue(filter instanceof Id);        
        Id filterId = (Id) filter;
        
        Set<?> resultIdentifiers = filterId.getIDs();
        Assert.assertTrue("one id in filter Id was expected",
                resultIdentifiers.size() == 3);

        Assert.assertTrue(strId1 + " was expected", resultIdentifiers.contains(strId1));

        Assert.assertTrue(strId2 + " was expected", resultIdentifiers.contains(strId2));

        Assert.assertTrue(strId3 + " was expected", resultIdentifiers.contains(strId3));
    }

    @Test
    public void idUsingIntegerValues() throws Exception {

        Filter filter;
        
        final String strId1 = "1";
        final String strId2 = "2";
        final String strId3 = "3";
        filter = ECQL.toFilter("IN (" + strId1 + "," + strId2
                + ", " + strId3 + ")");
        
        Id filterId = (Id) filter;
        Set<?> resultIdentifiers = filterId.getIDs();
        Assert.assertTrue("one id in filter Id was expected",
                resultIdentifiers.size() == 3);

        Assert.assertTrue(strId1 + " was expected", resultIdentifiers.contains(strId1));

        Assert.assertTrue(strId2 + " was expected", resultIdentifiers.contains(strId2));

        Assert.assertTrue(strId3 + " was expected", resultIdentifiers.contains(strId3));
    }

    @Test
    public void IdUsingDateValues() throws Exception {

        Filter filter;
        
        final String date1 = "2010-01-01";
        final String date2 = "2010-02-02";
        final String date3 = "2010-03-03";
        filter = ECQL.toFilter("IN ('" + date1 + "','" + date2+ "', '" + date3 + "')");
        
        Assert.assertNotNull(filter);
        Assert.assertTrue("Not filter was expected",  filter instanceof Id);

        Id filterId = (Id) filter;

        Set<?> resultIdentifiers = filterId.getIDs();
        Assert.assertTrue("one id in filter Id was expected",
                resultIdentifiers.size() == 3);

        Assert.assertTrue(date1 + " was expected", resultIdentifiers.contains(date1));

        Assert.assertTrue(date2 + " was expected", resultIdentifiers.contains(date2));

        Assert.assertTrue(date3 + " was expected", resultIdentifiers.contains(date3));
    }

    @Test
    public void IdUsingTimeStamp() throws Exception {

        Filter filter;
        
        final String timeStamp1 = "2010-01-01 00:01:01";
        final String timeStamp2 = "2010-02-02 00:01:01";
        final String timeStamp3 = "2010-03-03 00:01:01";
        filter = ECQL.toFilter("IN ('" + timeStamp1 + "','" + timeStamp2+ "', '" + timeStamp3 + "')");
        
        Assert.assertNotNull(filter);
        Assert.assertTrue("Not filter was expected",  filter instanceof Id);

        Id filterId = (Id) filter;

        Set<?> resultIdentifiers = filterId.getIDs();
        Assert.assertTrue("one id in filter Id was expected",
                resultIdentifiers.size() == 3);

        Assert.assertTrue(timeStamp1 + " was expected", resultIdentifiers.contains(timeStamp1));

        Assert.assertTrue(timeStamp2 + " was expected", resultIdentifiers.contains(timeStamp2));

        Assert.assertTrue(timeStamp3 + " was expected", resultIdentifiers.contains(timeStamp3));
    }
    
    /**
     * <pre>
     * 
     * Samples:
     * <ul>
     * <li>IN( '15521.3566' )</li>
     * <li>IN( 'fid-_df58120_11814e5d8b3__7ffb')</li>
     * <li>IN ('states.1')</li>
     * </ul> 
     * </pre>
     * @throws Exception
     */
    @Test 
    public void filterIdSimple() throws Exception {
        assertFilterId("15521.3566");
        assertFilterId("fid-_df58120_11814e5d8b3__7ffb");
        assertFilterId("states.1");
    }

    /**
     * Test the id Predicate 
     * @throws CQLException
     */
    private void assertFilterId(final String idValue) throws CQLException {

        String strId = "'"+ idValue + "'";
        Filter filter = ECQL.toFilter("IN (" + strId + ")");
        Assert.assertNotNull(filter);
        Assert.assertTrue(filter instanceof Id);

        Id filterId = (Id) filter;
        Set<?>  idSet = filterId.getIDs();
        Assert.assertTrue("one id in filter Id was expected", idSet.size() == 1);
        Assert.assertTrue(idValue + "was expected", idSet.contains(idValue));
    }
    
    /**
     * Test for Syntax error exception
     * @throws CQLException 
     */
    @Test(expected = CQLException.class)
    public void filterIdSyntaxError() throws CQLException {
        String strId = "IN 15521.3566"; // should be IN( '15521.3566')
        ECQL.toFilter(strId);
    }
    
    

    /** 
     * deprecated syntax (TODO  it should be unsupported in the next version)
     * 
     * This test produces the following warning in the log:
     * "WARNING: ID IN (...) is a deprecated syntax, you should use IN (...) "
     * 
     */
    @Test
    public void deprecatedSyntax() throws Exception{

    	Filter filter = ECQL.toFilter("ID IN ('river.1', 'river.2')");
    
    	Assert.assertTrue(filter instanceof Id);
    }
}
