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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.geotools.data.util.FeatureStreams;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;

/**
 * Tests TotalIndexedMappingFeatureIterator use case
 *
 * @author Fernando Miño, Geosolutions
 */
public class TotalIndexedMappingFeatureIteratorTest extends IndexesTest {

    @Test
    public void testTotalInstance() throws IOException {
        try (TestFeatureSource fsource =
                new TestFeatureSource(
                        "/test-data/index/",
                        "stationsIndexed.xml",
                        "http://www.stations.org/1.0",
                        "stationsIndexed")) {
            FeatureCollection<FeatureType, Feature> fcoll =
                    fsource.getMappedSource().getFeatures(this.totalIndexedFilterCase());
            FeatureIterator<Feature> iterator = fcoll.features();
            assertTrue(iterator instanceof TotalIndexedMappingFeatureIterator);
            List<Feature> features =
                    FeatureStreams.toFeatureStream(fcoll).collect(Collectors.toList());
            assertEquals(features.size(), 4);
            assertTrue(checkExists(features, "st.1"));
            assertTrue(checkExists(features, "st.2"));
            assertTrue(checkExists(features, "st.10"));
            assertTrue(checkExists(features, "st.11"));
        }
    }

    @Test
    public void testGetFid() throws IOException {
        try (TestFeatureSource fsource =
                new TestFeatureSource(
                        "/test-data/index/",
                        "stationsIndexed.xml",
                        "http://www.stations.org/1.0",
                        "stationsIndexed")) {
            FeatureCollection<FeatureType, Feature> fcoll =
                    fsource.getMappedSource().getFeatures(this.totalIndexedFilterCase());
            FeatureIterator<Feature> iterator = fcoll.features();
            assertTrue(iterator instanceof TotalIndexedMappingFeatureIterator);
            TotalIndexedMappingFeatureIterator titer =
                    (TotalIndexedMappingFeatureIterator) iterator;
            assertEquals("ID", titer.getFidAttrMap().getIndexField());
        }
    }

    /** Should returns 1, 2, 10, 12(11 on index) */
    private Filter totalIndexedFilterCase() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        List<Filter> filters =
                Arrays.asList(
                        ff.or(
                                ff.equals(ff.property(this.attName), ff.literal("station11")),
                                ff.equals(ff.property(this.attId), ff.literal("st.1"))),
                        ff.or(
                                ff.equals(ff.property(this.attName), ff.literal("station10")),
                                ff.equals(ff.property(this.attId), ff.literal("st.2"))));
        Filter filter = ff.or(filters);
        return filter;
    }

    /**
     * Helper method that checks that a feature that matches the provided id exists in the list of
     * provided features.
     */
    private boolean checkExists(List<Feature> features, String id) {
        for (Feature feature : features) {
            if (feature.getIdentifier().getID().equals(id)) {
                // we found a match
                return true;
            }
        }
        // found not match
        return false;
    }
}
