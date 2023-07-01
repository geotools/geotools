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
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.xml.XMLHandlerHints;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.filter.Id;
import org.geotools.api.filter.PropertyIsNull;
import org.geotools.api.filter.identity.FeatureId;

public class FilterEncodingPreProcessorTest {
    @Before
    public void setUp() throws Exception {}

    @Test
    public void testNOTFids() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        // not id filter does not actually have a valid encoding
        Filter filter = factory.not(factory.id(Collections.singleton(factory.featureId(fid1))));

        FilterEncodingPreProcessor visitor =
                new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilter());

        Assert.assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());

        // Test MEDIUM compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor, null);

        Assert.assertEquals(Filter.INCLUDE, visitor.getFilter());
        Assert.assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());
        Assert.assertTrue(visitor.requiresPostProcessing());

        // Test HIGH compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor, null);

        Assert.assertEquals(Filter.INCLUDE, visitor.getFilter());
        Assert.assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());
        Assert.assertTrue(visitor.requiresPostProcessing());
    }

    @Test
    public void testNOTANDFids() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";

        Filter fidFilter = factory.not(factory.id(Collections.singleton(factory.featureId(fid1))));

        PropertyIsNull nullFilter = factory.isNull(factory.property("name"));

        Filter filter = factory.and(nullFilter, fidFilter);

        FilterEncodingPreProcessor visitor =
                new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilter());
        Assert.assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test MEDIUM compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor, null);

        Assert.assertEquals(nullFilter, visitor.getFilter());
        Assert.assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());
        Assert.assertTrue(visitor.requiresPostProcessing());

        // Test HIGH compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor, null);

        Assert.assertEquals(nullFilter, visitor.getFilter());
        Assert.assertTrue(visitor.getFidFilter().getIdentifiers().isEmpty());
        Assert.assertTrue(visitor.requiresPostProcessing());
    }

    Id createFidFilter(String... fid) {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        Set<FeatureId> set;
        if (fid == null || fid.length == 0) {
            set = Collections.emptySet();
        } else {
            set = new HashSet<>();
            for (String f : fid) {
                set.add(factory.featureId(f));
            }
        }
        return factory.id(set);
    }

    @Test
    public void testStraightANDFids() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.id(Collections.singleton(factory.featureId(fid1)));
        filter = factory.and(filter, factory.id(Collections.singleton(factory.featureId(fid2))));

        // Test Low compliance
        FilterEncodingPreProcessor visitor =
                new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilter());

        Set<FeatureId> empty = Collections.emptySet();
        Filter fidFilter = factory.id(empty);
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        fidFilter.accept(visitor, null);

        // anding 2 different fids results in nothing.
        Assert.assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter();
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        fidFilter.accept(visitor, null);

        // anding 2 different fids results in nothing.
        Assert.assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter();
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test and same fid
        filter = factory.and(createFidFilter(fid1), createFidFilter(fid1));

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor, null);

        Assert.assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter(fid1);
        Assert.assertEquals(createFidFilter(fid1), visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor, null);

        Assert.assertEquals(Filter.EXCLUDE, visitor.getFilter());
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());
    }

    @Test
    public void testMixedAND() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.or(createFidFilter(fid1), createFidFilter(fid2));
        Filter nullFilter = factory.isNull(factory.property("att"));

        filter = factory.and(filter, nullFilter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor =
                new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilter());

        Filter fidFilter = createFidFilter();
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor, null);

        Assert.assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter(fid1, fid2);
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor, null);

        Assert.assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter(fid1, fid2);
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());
    }

    @Test
    public void testStraightOrFids() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.or(createFidFilter(fid1), createFidFilter(fid2));

        // Test Low compliance
        FilterEncodingPreProcessor visitor =
                new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilter());

        Filter fidFilter = createFidFilter((String[]) null);
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor, null);

        Assert.assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter(fid1, fid2);
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor, null);

        Assert.assertEquals(Filter.EXCLUDE, visitor.getFilter());
        fidFilter = createFidFilter(fid1, fid2);
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());
    }

    @Test
    public void testMixedOr() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        String fid2 = "FID.2";
        Filter filter = factory.or(createFidFilter(fid1), createFidFilter(fid2));
        Filter nullFilter = factory.isNull(factory.property("att"));

        filter = factory.or(filter, nullFilter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor =
                new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilter());

        Filter fidFilter = createFidFilter();
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor, null);

        Assert.assertEquals(nullFilter, visitor.getFilter());
        fidFilter = createFidFilter(fid1, fid2);
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);

        try {
            filter.accept(visitor, null);

            Assert.fail("This is not a legal filter for this compliance level");
        } catch (UnsupportedFilterException e) {
            // good
        }
    }

    /**
     * Tests the following filter:
     *
     * <p>and { nullFilter or{ fidFilter nullFilter } }
     *
     * <p>for medium it should end up as:
     *
     * <p>and{ nullFilter nullFilter }
     *
     * <p>and the fids should be included.
     */
    @Test
    public void testMixedAndOr() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        String fid1 = "FID.1";
        Filter nullFilter1 = factory.isNull(factory.property("att"));

        Filter filter = factory.or(createFidFilter(fid1), nullFilter1);
        Filter nullFilter2 = factory.isNull(factory.property("name"));

        filter = factory.and(nullFilter2, filter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor =
                new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilter());

        Filter fidFilter = createFidFilter();
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor, null);

        Assert.assertEquals(factory.and(nullFilter1, nullFilter2), visitor.getFilter());
        fidFilter = createFidFilter(fid1);
        Assert.assertEquals(fidFilter, visitor.getFidFilter());
        Assert.assertTrue(visitor.requiresPostProcessing());

        // Test High compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);

        try {
            filter.accept(visitor, null);

            Assert.fail("This is not a legal filter for this compliance level");
        } catch (UnsupportedFilterException e) {
            // good
        }
    }

    @Test
    public void testStrictlyLegalFilter() throws Exception {
        FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2(null);
        Filter nullFilter1 = factory.isNull(factory.property("att"));

        Filter nullFilter2 = factory.isNull(factory.property("name"));
        Filter compareFilter = factory.equals(factory.property("name"), factory.literal(3));

        Filter filter = factory.and(nullFilter2, nullFilter1);
        filter = factory.not(filter);
        filter = factory.or(filter, compareFilter);

        // Test Low compliance
        FilterEncodingPreProcessor visitor =
                new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_LOW);
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilter());
        Assert.assertEquals(createFidFilter(), visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test Medium level compliance.
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_MEDIUM);
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilter());
        Assert.assertEquals(createFidFilter(), visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());

        // Test High level compliance
        visitor = new FilterEncodingPreProcessor(XMLHandlerHints.VALUE_FILTER_COMPLIANCE_HIGH);
        filter.accept(visitor, null);

        Assert.assertEquals(filter, visitor.getFilter());
        Assert.assertEquals(createFidFilter(), visitor.getFidFilter());
        Assert.assertFalse(visitor.requiresPostProcessing());
    }
}
