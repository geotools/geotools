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

import junit.framework.TestCase;
import org.geotools.filter.CompareFilter;
import org.geotools.filter.FidFilter;
import org.geotools.filter.Filter;
import org.geotools.filter.FilterFactory;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.FilterType;
import org.geotools.filter.NullFilter;
import org.geotools.xml.XMLHandlerHints;


public class FilterEncodingPreProcessorTest extends TestCase {
    protected void setUp() throws Exception {
        super.setUp();
    }

    public void testNOTFids() throws Exception {
        FilterFactory factory = FilterFactoryFinder
            .createFilterFactory();
        String fid1 = "FID.1";
        Filter filter = factory.createFidFilter(fid1).not();

        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor);

        assertEquals(filter, visitor.getFilter());
        assertEquals(factory.createFidFilter(), visitor.getFidFilter());

        //Test MEDIUM compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor);

        assertEquals(Filter.INCLUDE, visitor.getFilter());
        assertEquals(factory.createFidFilter(), visitor.getFidFilter());
        assertTrue(visitor.requiresPostProcessing());

        //Test HIGH compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor);

        assertEquals(Filter.INCLUDE, visitor.getFilter());
        assertEquals(factory.createFidFilter(), visitor.getFidFilter());
        assertTrue(visitor.requiresPostProcessing());
    }

    public void testNOTANDFids() throws Exception {
        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        String fid1 = "FID.1";
        Filter fidFilter = factory.createFidFilter(fid1).not();
        NullFilter nullFilter = factory.createNullFilter();
        nullFilter.nullCheckValue(factory.createAttributeExpression("name"));

        Filter filter = nullFilter.and(fidFilter);

        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor);

        assertEquals(filter, visitor.getFilter());
        assertEquals(factory.createFidFilter(), visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());


        //Test MEDIUM compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor);

        assertEquals(nullFilter, visitor.getFilter());
        assertEquals(factory.createFidFilter(), visitor.getFidFilter());
        assertTrue(visitor.requiresPostProcessing());

        //Test HIGH compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor);

        assertEquals(nullFilter, visitor.getFilter());
        assertEquals(factory.createFidFilter(), visitor.getFidFilter());
        assertTrue(visitor.requiresPostProcessing());
    }

    public void testStraightANDFids() throws Exception {
        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.createFidFilter(fid1)
                               .and(factory.createFidFilter(fid2));

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor);

        assertEquals(filter, visitor.getFilter());

        FidFilter fidFilter = factory.createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());


        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor);

        // anding 2 different fids results in nothing.
        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = factory.createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor);

        // anding 2 different fids results in nothing.
        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = factory.createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test and same fid
        filter = factory.createFidFilter(fid1).and(factory.createFidFilter(fid1));

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor);

        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = factory.createFidFilter();
        fidFilter.addFid(fid1);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor);

        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());
    }

    public void testMixedAND() throws Exception {
        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.createFidFilter(fid1)
                               .or(factory.createFidFilter(fid2));
        NullFilter nullFilter = factory.createNullFilter();
        nullFilter.nullCheckValue(factory.createAttributeExpression("att"));

        filter = filter.and(nullFilter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor);

        assertEquals(filter, visitor.getFilter());

        FidFilter fidFilter = factory.createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor);

        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = factory.createFidFilter();
        fidFilter.addFid(fid1);
        fidFilter.addFid(fid2);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor);

        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = factory.createFidFilter();
        fidFilter.addFid(fid1);
        fidFilter.addFid(fid2);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());
    }

    public void testStraightOrFids() throws Exception {
        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.createFidFilter(fid1)
                               .or(factory.createFidFilter(fid2));

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor);

        assertEquals(filter, visitor.getFilter());

        FidFilter fidFilter = factory.createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor);

        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = factory.createFidFilter();
        fidFilter.addFid(fid1);
        fidFilter.addFid(fid2);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor);

        assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = factory.createFidFilter();
        fidFilter.addFid(fid1);
        fidFilter.addFid(fid2);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());
    }

    public void testMixedOr() throws Exception {
        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.createFidFilter(fid1)
                               .or(factory.createFidFilter(fid2));
        NullFilter nullFilter = factory.createNullFilter();
        nullFilter.nullCheckValue(factory.createAttributeExpression("att"));

        filter = filter.or(nullFilter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor);

        assertEquals(filter, visitor.getFilter());

        FidFilter fidFilter = factory.createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor);

        assertEquals(nullFilter, visitor.getFilter());
        fidFilter = factory.createFidFilter();
        fidFilter.addFid(fid1);
        fidFilter.addFid(fid2);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);

        try {
            filter.accept(visitor);
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
        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        String fid1 = "FID.1";
        NullFilter nullFilter1 = factory.createNullFilter();
        nullFilter1.nullCheckValue(factory.createAttributeExpression("att"));

        Filter filter = factory.createFidFilter(fid1).or(nullFilter1);
        NullFilter nullFilter2 = factory.createNullFilter();
        nullFilter2.nullCheckValue(factory.createAttributeExpression("name"));

        filter = nullFilter2.and(filter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor);

        assertEquals(filter, visitor.getFilter());

        FidFilter fidFilter = factory.createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor);

        assertEquals(nullFilter1.and(nullFilter2), visitor.getFilter());
        fidFilter = factory.createFidFilter();
        fidFilter.addFid(fid1);
        assertEquals(fidFilter, visitor.getFidFilter());
        assertTrue(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);

        try {
            filter.accept(visitor);
            fail("This is not a legal filter for this compliance level");
        } catch (UnsupportedFilterException e) {
            // good
        }
    }

    public void testStrictlyLegalFilter() throws Exception {
        FilterFactory factory = FilterFactoryFinder.createFilterFactory();
        NullFilter nullFilter1 = factory.createNullFilter();
        nullFilter1.nullCheckValue(factory.createAttributeExpression("att"));

        NullFilter nullFilter2 = factory.createNullFilter();
        nullFilter2.nullCheckValue(factory.createAttributeExpression("name"));

        CompareFilter compareFilter = factory.createCompareFilter(FilterType.COMPARE_EQUALS);
        compareFilter.addLeftValue(factory.createAttributeExpression("name"));
        compareFilter.addRightValue(factory.createLiteralExpression(3));

        Filter filter = nullFilter2.and(nullFilter1);
        filter = filter.not();
        filter = filter.or(compareFilter);

        //		 Test Low compliance
        FilterEncodingPreProcessor visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor);

        assertEquals(filter, visitor.getFilter());

        FidFilter fidFilter = factory.createFidFilter();
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor);

        assertEquals(filter, visitor.getFilter());
        assertEquals(fidFilter, visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());

        //Test High level compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor);

        assertEquals(filter, visitor.getFilter());
        assertEquals(factory.createFidFilter(), visitor.getFidFilter());
        assertFalse(visitor.requiresPostProcessing());
    }
}
