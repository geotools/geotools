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
 * Test for ID Predicate
 * <pre>
 * &lt id predicate &gt ::= "ID" [ "NOT" ] "IN " "(" &lt id value &gt {,&lt id value &gt } ")"
 * 
 * Sample: ID IN ( 'states.1', 'states.2', 'states.3')
 * </pre>
 *
 * @author Mauricio Pazos (Axios Engineering)
 * @since 2.6
 *
 * @source $URL$
 */
public class ECQLIDPredicateTest {
    
    
    /**
     * Sample: ID IN( 'states.1', 'states.2', 'states.3'
     * </pre>
     * @throws Exception
     */
    @Test
    public void filterIdWithListOfIdValues() throws Exception {

        final String strId1 = "states.1";
        final String strId2 = "states.2";
        final String strId3 = "states.3";
        Filter filter = ECQL.toFilter("ID IN ('" + strId1 + "','" + strId2
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
     * Sample: ID NOT IN( 'states.1', 'states.2', 'states.3')
     * </pre>
     * @throws Exception
     */
    @Test
    public void notFilterId() throws Exception {

        Filter filter;
        
        final String strId1 = "states.1";
        final String strId2 = "states.2";
        final String strId3 = "states.3";
        filter = ECQL.toFilter("NOT ID IN ('" + strId1 + "','" + strId2
                + "', '" + strId3 + "')");
        
        Assert.assertNotNull(filter);
        Assert.assertTrue("Not filter was expected",  filter instanceof Not);
        
        Not notFilter = (Not) filter;
        filter = notFilter.getFilter();

        Id filterId = (Id) filter;
        Set<?> resultIdentifiers = filterId.getIDs();
        Assert.assertTrue("one id in filter Id was expected",
                resultIdentifiers.size() == 3);

        Assert.assertTrue(strId1 + " was expected", resultIdentifiers.contains(strId1));

        Assert.assertTrue(strId2 + " was expected", resultIdentifiers.contains(strId2));

        Assert.assertTrue(strId3 + " was expected", resultIdentifiers.contains(strId3));
    }

    @Test
    public void notIdUsingIntegerValues() throws Exception {

        Filter filter;
        
        final String strId1 = "1";
        final String strId2 = "2";
        final String strId3 = "3";
        filter = ECQL.toFilter("NOT ID IN (" + strId1 + "," + strId2
                + ", " + strId3 + ")");
        
        Assert.assertNotNull(filter);
        Assert.assertTrue("Not filter was expected",  filter instanceof Not);
        
        Not notFilter = (Not) filter;
        filter = notFilter.getFilter();

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
     * 
     * Samples:
     * <ul>
     * <li>ID IN( '15521.3566' )</li>
     * <li>ID IN( 'fid-_df58120_11814e5d8b3__7ffb')</li>
     * <li>ID IN ('states.1')</li>
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
        Filter filter = ECQL.toFilter("ID IN (" + strId + ")");
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
        String strId = "ID 15521.3566"; // should be ID IN( '15521.3566')
        ECQL.toFilter(strId);
    }
}
