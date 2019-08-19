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
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;
import org.geotools.data.complex.IndexesTest;
import org.geotools.data.complex.TestFeatureSource;
import org.junit.Test;
import org.opengis.filter.And;
import org.opengis.filter.BinaryLogicOperator;
import org.opengis.filter.Filter;

/**
 * Tests org.geotools.data.complex.filter.IndexCombinedFilterTransformerVisitor
 *
 * @author Fernando Miño, Geosolutions
 */
public class IndexCombinedFilterTransformerVisitorTest extends IndexesTest {

    @Test
    public void testVisitor() {
        try (TestFeatureSource fsource =
                new TestFeatureSource(
                        "/test-data/index/",
                        "stationsIndexed.xml",
                        "http://www.stations.org/1.0",
                        "stationsIndexed")) {
            Filter filter = partialIndexedFilter_2idxfilterResults();
            List<Filter> indexedFilters =
                    Arrays.asList(new Filter[] {totallyIndexedFilter(), totallyIndexedFilter2()});
            IndexCombinedFilterTransformerVisitor visitor =
                    new IndexCombinedFilterTransformerVisitor(
                            (BinaryLogicOperator) partialIndexedFilter_2idxfilterResults(),
                            indexedFilters,
                            buildIdInExpression(
                                    Arrays.asList(new String[] {"st.3", "st.2"}),
                                    fsource.getMappedSource().getMapping()));
            Filter ultimateFilter = (Filter) filter.accept(visitor, ff);
            assertNotEquals(filter, ultimateFilter);
            assertTrue(ultimateFilter instanceof And);
            And mainAnd = (And) ultimateFilter;
            assertEquals(
                    mainAnd.getChildren().get(0),
                    buildIdInExpression(
                            Arrays.asList(new String[] {"st.3", "st.2"}),
                            fsource.getMappedSource().getMapping()));
        }
    }
}
