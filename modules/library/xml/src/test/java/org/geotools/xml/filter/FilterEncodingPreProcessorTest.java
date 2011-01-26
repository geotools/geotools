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
package org.geotools.xml.filter;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.Filters;
import org.geotools.xml.XMLHandlerHints;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.identity.FeatureId;


public class FilterEncodingPreProcessorTest extends TestCase {
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testNOTFids() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        // not id filter does not actually have a valid encoding 
        Filter filter = factory.not( factory.id( Collections.singleton( factory.featureId(fid1) ) ) );

        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        Filters.accept(filter, visitor);

        assertEquals(filter, visitor.getFilter());
        
        assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());

        //Test MEDIUM compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        Filters.accept(filter, visitor);
        
        assertEquals(Filter.INCLUDE, visitor.getFilter());
        assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());
        assertTrue(visitor.requiresPostProcessing());

        //Test HIGH compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        Filters.accept(filter, visitor);

        assertEquals(Filter.INCLUDE, visitor.getFilter());
        assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());
        assertTrue(visitor.requiresPostProcessing());
    }

    public void testNOTANDFids() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        
        Filter fidFilter = factory.not( factory.id( Collections.singleton( factory.featureId(fid1) ) ) );
        
         PropertyIsNull nullFilter = factory.isNull( factory.property("name"));

        Filter filter = factory.and(nullFilter,fidFilter);

        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        Filters.accept(filter, visitor);
        
        assertEquals(filter, visitor.getFilter());
        assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());
        assertFalse(visitor.requiresPostProcessing());


        //Test MEDIUM compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        Filters.accept(filter, visitor);
        
        assertEquals(nullFilter, visitor.getFilter());
        assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());
        assertTrue(visitor.requiresPostProcessing());

        //Test HIGH compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        Filters.accept(filter, visitor);

        assertEquals(nullFilter, visitor.getFilter());
        assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());
        assertTrue(visitor.requiresPostProcessing());
    }
    
    Id createFidFilter(String... fid){
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        Set<FeatureId> set;
        if( fid == null || fid.length == 0){
            set = Collections.emptySet();
        }
        else {
            set = new HashSet<FeatureId>();
            for( String f : fid ){
                set.add( factory.featureId(f));
            }
        }
        return factory.id( set );
    }

    public void testStraightANDFids() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.id(Collections.singleton( factory.featureId(fid1)));
        filter = factory.and( filter, factory.id(Collections.singleton( factory.featureId(fid2))));

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        Filters.accept(filter, visitor);
        
        assertEquals(filter, visitor.getFilter());

        Set<FeatureId> empty = Collections.emptySet();
        Filter fidFilter = factory.id( empty );
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());


        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        Filters.accept(fidFilter, visitor);

        // anding 2 different fids results in nothing.
        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        Filters.accept(fidFilter, visitor);

        // anding 2 different fids results in nothing.
        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test and same fid
        filter = factory.and( createFidFilter(fid1), createFidFilter(fid1));

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        Filters.accept(filter, visitor);


        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter(fid1);
        assertEquals(createFidFilter(fid1), visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        Filters.accept(filter, visitor);


        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());
    }

    public void testMixedAND() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.or( createFidFilter(fid1), createFidFilter(fid2));
        Filter nullFilter = factory.isNull(factory.property("att"));

        filter = factory.and( filter, nullFilter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        Filters.accept(filter, visitor);


        assertEquals(filter, visitor.getFilter());

        Filter fidFilter = createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        Filters.accept(filter, visitor);


        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter(fid1,fid2);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        Filters.accept(filter, visitor);


        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter(fid1,fid2);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());
    }

    public void testStraightOrFids() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.or( createFidFilter(fid1),createFidFilter(fid2));

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        Filters.accept(filter, visitor);


        assertEquals(filter, visitor.getFilter());

        Filter fidFilter = createFidFilter(null);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        Filters.accept(filter, visitor);


        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter(fid1,fid2);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        Filters.accept(filter, visitor);


        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter(fid1,fid2);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());
    }

    public void testMixedOr() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.or( createFidFilter(fid1), createFidFilter(fid2));
        Filter nullFilter = factory.isNull(factory.property("att"));

        filter = factory.or( filter, nullFilter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        Filters.accept(filter, visitor);


        assertEquals(filter, visitor.getFilter());

        Filter fidFilter = createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        Filters.accept(filter, visitor);


        assertEquals(nullFilter, visitor.getFilter());
        fidFilter = createFidFilter(fid1,fid2);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);

        try {
            Filters.accept(filter, visitor);

            fail("This is not a legal filter for this compliance level");
        } catch (UnsupportedFilterException e) {
            // good
        }
    }

    /**
     * Tests the following filter:
     *
     * and {
     *                 nullFilter
     *                 or{
     *                         fidFilter
     *                         nullFilter
     *                 }
     * }
     *
     * for medium it should end up as:
     *
     *         and{
     *                 nullFilter
     *                 nullFilter
     *         }
     *
     * and the fids should be included.
     *
     * @throws Exception
     */
    public void testMixedAndOr() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        Filter nullFilter1 = factory.isNull(factory.property("att"));

        Filter filter = factory.or( createFidFilter(fid1),nullFilter1);
        Filter nullFilter2 = factory.isNull(factory.property("name"));

        filter = factory.and( nullFilter2,filter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        Filters.accept(filter, visitor);


        assertEquals(filter, visitor.getFilter());

        Filter fidFilter = createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        Filters.accept(filter, visitor);


        assertEquals( factory.and( nullFilter1,nullFilter2), visitor.getFilter());
        fidFilter = createFidFilter(fid1);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertTrue(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);

        try {
            Filters.accept(filter, visitor);

            fail("This is not a legal filter for this compliance level");
        } catch (UnsupportedFilterException e) {
            // good
        }
    }

    public void testStrictlyLegalFilter() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        Filter nullFilter1 = factory.isNull(factory.property("att"));

        Filter nullFilter2 = factory.isNull(factory.property("name"));
        Filter compareFilter = factory.equals(factory.property("name"), factory.literal(3));
        
        Filter filter = factory.and( nullFilter2,nullFilter1);
        filter = factory.not(filter);
        filter = factory.or(filter,compareFilter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        Filters.accept(filter, visitor);


        assertEquals(filter, visitor.getFilter());
        assertEquals(createFidFilter(), visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        Filters.accept(filter, visitor);


        assertEquals(filter, visitor.getFilter());
        assertEquals(createFidFilter(), visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        //Test High level compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        Filters.accept(filter, visitor);


        assertEquals(filter, visitor.getFilter());
        assertEquals(createFidFilter(), visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());
    }
}
