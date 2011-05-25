/*
 *    GeoTools - The Open Source Java GIS Toolkit
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

package org.geotools.data.postgis;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import junit.framework.AssertionFailedError;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.RegfuncFilterFactoryImpl;
import org.geotools.filter.RegisteredFunction;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.expression.Expression;

/**
 * Full test of registered function support, using filter queries to call a function on the database
 * side.
 * 
 * <p>
 * 
 * See {@link AbstractRegfuncPostgisOnlineTestCase} for a description of this test fixture.
 * 
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class RegFuncPostgisDataStoreOnlineTest extends AbstractRegfuncPostgisOnlineTestCase {

    /**
     * Test that using no filter results in features for all four rows being returned.
     * 
     * @throws Exception
     */
    public void testNoFilter() throws Exception {
        runFilterTest(Filter.INCLUDE, new int[] { 3, 27, 41, 93 });
    }

    /**
     * Test that a filter for descriptions containing "basalt" results in features for the two rows
     * whose descriptions contain "basalt". Note that this is a case-insensitive condition.
     * 
     * @throws Exception
     */
    public void testBasaltFilter() throws Exception {
        runFilterTest(buildFilter("basalt"), new int[] { 3, 27 });
    }

    /**
     * Test that a filter for descriptions containing "GRAVEL" results in a feature for the one row
     * whose description contains "GRAVEL" (case-insensitive).
     * 
     * @throws Exception
     */
    public void testGravelFilter() throws Exception {
        runFilterTest(buildFilter("GRAVEL"), new int[] { 93 });
    }

    /**
     * Test that a filter for descriptions containing "chocolate" returns no features.
     * 
     * @throws Exception
     */
    public void testChocolateFilter() throws Exception {
        runFilterTest(buildFilter("chocolate"), new int[] {});
    }

    /**
     * Test that the test is working by ensuring that specifying the wrong features causes a test
     * failure.
     * 
     * @throws Exception
     */
    public void testBasaltFilterWrongFeatures() throws Exception {
        // true if test unexpectedly passes
        boolean failed = false;
        try {
            // should expect ids 3 and 27
            runFilterTest(buildFilter("basalt"), new int[] { 27 });
            // cannot fail() here as would be caught
            failed = true;
        } catch (AssertionFailedError e) {
            // success
        }
        if (failed) {
            fail();
        }
    }

    /**
     * Test that a filter query issued.
     * 
     * @param filter
     *                filter to be passed in the query to determine the subset of features to be
     *                returned, or null for all features
     * @param expectedFeatureIds
     *                integer id for returned features, matching the expected row ids
     * @throws Exception
     */
    private void runFilterTest(Filter filter, int[] expectedFeatureIds) throws Exception {
        DataStore datastore = null;
        try {
            datastore = DataStoreFinder.getDataStore(getParams());
            assertNotNull(datastore);
            Query query = new DefaultQuery(TEST_TABLE_NAME, filter);
            FeatureReader<SimpleFeatureType, SimpleFeature> reader = null;
            try {
                /*
                 * List of all the integer feature ids seen in the features returned for this query.
                 */
                List<Integer> featureIds = new ArrayList<Integer>();
                reader = datastore.getFeatureReader(query, Transaction.AUTO_COMMIT);
                for (SimpleFeature feature = null; reader.hasNext();) {
                    feature = reader.next();
                    /*
                     * Convert long feature id of the form test_table_name.1234 to the numeric id
                     * used when creating the row. This relies on the behaviour of the postgis fid
                     * mapper.
                     */
                    Integer id = Integer
                            .valueOf(feature.getID().replace(TEST_TABLE_NAME + ".", ""));
                    featureIds.add(id);
                }
                /*
                 * The query succeeded as expected if and only if the sorted lists of returned and
                 * expected ids are equal. The expected ids are converted to a List of Integers to
                 * leverage Collections comparison.
                 */
                assertEquals(sortedList(expectedFeatureIds), sorted(featureIds));
            } finally {
                if (reader != null) {
                    reader.close();
                }
            }
        } finally {
            if (datastore != null) {
                datastore.dispose();
            }
        }

    }

    /**
     * Create a {@link Filter} that selects features from the test table whose description contains
     * the text specified in the argument (case-insensitive).
     * 
     * @param descriptionContainsText
     *                text that description column must contain
     * @return filter to select features containing text in their description
     */
    private Filter buildFilter(String descriptionContainsText) {
        FilterFactory filterFactory = new RegfuncFilterFactoryImpl();
        // Programmatically build a filter like this fragment from an OGC WFS 1.1.0 Query:
        //
        // <ogc:Filter>
        // <ogc:PropertyIsEqualTo>
        // <ogc:Function name="contains_text">
        // <ogc:PropertyName>
        // gsml:specification/gsml:GeologicUnit/gml:description
        // </ogc:PropertyName>
        // <ogc:Literal>basalt</ogc:Literal>
        // </ogc:Function>
        // <ogc:Literal>1</ogc:Literal>
        // </ogc:PropertyIsEqualTo>
        // </ogc:Filter>
        //
        // Now step by step:
        //
        // Build <ogc:PropertyName>
        Expression property = filterFactory.property(DESCRIPTION_COLUMN_NAME);
        // Build <ogc:Literal>basalt</ogc:Literal> equivalent
        Expression string = filterFactory.createLiteralExpression(descriptionContainsText);
        // Build <ogc:Function name="contains_text"> equivalent and set its arguments
        Expression function = filterFactory.function(TEST_FUNCTION_NAME, property, string);
        // Sanity check
        assertEquals(RegisteredFunction.class, function.getClass());
        // Build <ogc:Literal>1</ogc:Literal>
        Expression one = filterFactory.createLiteralExpression(TEST_FUNCTION_RETURN_TRUE);
        // Build <ogc:PropertyIsEqualTo>, set its arguments, and return it
        return filterFactory.equals(function, one);
    }

    /**
     * Return a sorted copy of a list.
     * 
     * @param a
     *                list to be sorted
     * @return a sorted copy of the input list
     */
    private List sorted(List a) {
        List b = new ArrayList(a);
        Collections.sort(b);
        return b;
    }

    /**
     * Convert an int array into a sorted list of Integer.
     * 
     * @param a
     *                array of int to be sorted
     * @return sorted list of Integer, copied from input
     */
    private List/* <Integer> */sortedList(int[] a) {
        List b = new ArrayList(a.length);
        for (int i = 0; i < a.length; i++) {
            b.add(new Integer(a[i]));
        }
        return sorted(b);
    }

}
