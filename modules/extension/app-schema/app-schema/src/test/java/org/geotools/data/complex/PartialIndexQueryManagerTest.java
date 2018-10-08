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
package org.geotools.data.complex;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.Query;
import org.geotools.data.complex.IndexQueryManager.PartialIndexQueryManager;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.feature.FeatureCollection;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/**
 * Tests PartialIndexQueryManager
 *
 * @author Fernando Miño, Geosolutions
 */
public class PartialIndexQueryManagerTest extends IndexesTest {

    @Test
    public void testIndexQuery() throws IOException, URISyntaxException {
        try (TestFeatureSource fsource =
                new TestFeatureSource(
                        "/test-data/index/",
                        "stationsIndexed.xml",
                        "http://www.stations.org/1.0",
                        "stationsIndexed")) {
            Filter filter = partialIndexedFilter_2idxfilterResults();
            Query query = new Query("stationsIndexed", filter);
            PartialIndexQueryManager piqm =
                    new PartialIndexQueryManager(fsource.getMappedSource().getMapping(), query);
            Query indexQuery = piqm.getIndexQuery();
            assertNotNull(indexQuery);
            // Check expected indexOnlyFilter:
            List<Filter> filters = Arrays.asList(totallyIndexedFilter(), totallyIndexedFilter2());
            Filter expectedFilter = ff.and(filters);
            assertTrue(expectedFilter.equals(indexQuery.getFilter()));

            // check build new combined query:
            Query combQuery = piqm.buildCombinedQuery(indexFeatureCollection());
            assertNotNull(combQuery);
            Filter ultimateFilter = combQuery.getFilter();
            assertNotEquals(filter, ultimateFilter);
            assertTrue(ultimateFilter instanceof And);
            And mainAnd = (And) ultimateFilter;
            assertEquals(
                    mainAnd.getChildren().get(0),
                    buildIdInExpression(
                            Arrays.asList(new String[] {"st.2", "st.3"}),
                            fsource.getMappedSource().getMapping()));
        }
    }

    private FeatureCollection<? extends FeatureType, ? extends Feature> indexFeatureCollection()
            throws IOException, URISyntaxException {
        Map<String, Serializable> params = new HashMap<>();
        File dir = new File(getClass().getResource("/test-data/index/").toURI());
        PropertyDataStore datastore = new PropertyDataStore(dir);
        FilterFactory ff1 = datastore.getFilterFactory();
        Query query =
                new Query(
                        "stationsIndex",
                        ff1.or(
                                ff1.equals(ff1.property("NAME"), ff1.literal("station2")),
                                ff1.equals(ff1.property("NAME"), ff1.literal("station3"))));
        return datastore.getFeatureSource("stationsIndex").getFeatures(query);
    }
}
