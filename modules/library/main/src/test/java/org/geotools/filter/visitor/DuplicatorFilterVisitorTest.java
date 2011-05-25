/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.filter.visitor;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;


/**
 * Unit test for DuplicatorFilterVisitor.
 *
 * @author Cory Horner, Refractions Research Inc.
 *
 * @source $URL$
 */
public class DuplicatorFilterVisitorTest extends TestCase {
    FilterFactory fac;

    public DuplicatorFilterVisitorTest(String testName) {
        super(testName);
    }

    protected void setUp() throws Exception {
        fac = CommonFactoryFinder.getFilterFactory(null);
    }
    
    public void testLogicFilterDuplication() throws IllegalFilterException {
        List filters = new ArrayList();
        // create a filter
        Filter filter1 = fac.greater(fac.literal(2), fac.literal(1));
        filters.add(filter1);
        Filter filter2 = fac.greater(fac.literal(4), fac.literal(3));
        filters.add(filter2);

        And oldFilter = fac.and(filters);
        //duplicate it
    	DuplicatingFilterVisitor visitor = new DuplicatingFilterVisitor((FilterFactory2) fac);
    	Filter newFilter = (Filter) oldFilter.accept(visitor, null);

    	//compare it
    	assertNotNull(newFilter);
    	//TODO: a decent comparison
    }
    
}
