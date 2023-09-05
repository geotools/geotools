/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.complex.filter;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.data.complex.TestFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.test.AppSchemaTestSupport;
import org.junit.Test;

/**
 * Tests IndexedFilterDetectorVisitor
 *
 * @author Fernando Miño, Geosolutions
 */
public class IndexedFilterDetectorVisitorTest extends AppSchemaTestSupport {

    @Test
    public void testPartialIndexedFilter() {
        try (TestFeatureSource fsource =
                new TestFeatureSource(
                        "/test-data/index/",
                        "stationsIndexed.xml",
                        "http://www.stations.org/1.0",
                        "stationsIndexed")) {
            IndexedFilterDetectorVisitor visitor =
                    new IndexedFilterDetectorVisitor(fsource.getMappedSource().getMapping());
            Filter filter = partialIndexedFilter();
            filter.accept(visitor, null);
            assertEquals(visitor.getIndexedFilters().get(0), totallyIndexedFilter());
            assertEquals(visitor.getParentLogicOperator(), partialIndexedFilter());
        }
    }

    @Test
    public void testPartialIndexedFilter_with2idxSubfilters() {
        try (TestFeatureSource fsource =
                new TestFeatureSource(
                        "/test-data/index/",
                        "stationsIndexed.xml",
                        "http://www.stations.org/1.0",
                        "stationsIndexed")) {
            IndexedFilterDetectorVisitor visitor =
                    new IndexedFilterDetectorVisitor(fsource.getMappedSource().getMapping());
            Filter filter = partialIndexedFilter_2idxfilterResults();
            filter.accept(visitor, null);
            assertEquals(2, visitor.getIndexedFilters().size());
            assertEquals(
                    visitor.getParentLogicOperator(), partialIndexedFilter_2idxfilterResults());
        }
    }

    private Filter partialIndexedFilter() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Filter filter =
                ff.and(
                        totallyIndexedFilter(),
                        ff.like(ff.property("st:location/st:name"), "*fer*"));
        return filter;
    }

    private Filter totallyIndexedFilter() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Filter filter =
                ff.or(
                        ff.equals(ff.property("st:Station"), ff.literal("st.1")),
                        ff.like(ff.property("st:Station/st:name"), "*fer*"));
        return filter;
    }

    private Filter totallyIndexedFilter2() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Filter filter =
                ff.or(
                        ff.equals(ff.property("st:Station/st:name"), ff.literal("fer")),
                        ff.like(ff.property("st:Station/st:name"), "*mariela*"));
        return filter;
    }

    private Filter partialIndexedFilter_2idxfilterResults() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        List<Filter> filters =
                Arrays.asList(
                        totallyIndexedFilter(),
                        ff.like(ff.property("st:location/st:name"), "*fer*"),
                        totallyIndexedFilter2());
        Filter filter = ff.and(filters);
        return filter;
    }
}
