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
 * Tests PartialIndexedMappingFeatureIterator
 *
 * @author Fernando Miño, Geosolutions
 */
public class PartialIndexedMappingFeatureIteratorTest extends IndexesTest {

    @Test
    public void testPartialInstance() throws IOException {
        try (TestFeatureSource fsource =
                new TestFeatureSource(
                        "/test-data/index/",
                        "stationsIndexed.xml",
                        "http://www.stations.org/1.0",
                        "stationsIndexed")) {
            FeatureCollection<FeatureType, Feature> fcoll =
                    fsource.getMappedSource()
                            .getFeatures(this.partialIndexedFilter_2idxfilterResults());
            FeatureIterator<Feature> iterator = fcoll.features();
            assertTrue(iterator instanceof PartialIndexedMappingFeatureIterator);
            List<Feature> features =
                    FeatureStreams.toFeatureStream(fcoll).collect(Collectors.toList());
            assertEquals(features.size(), 6);
            assertEquals(features.get(0).getIdentifier().getID(), "st.1");
            assertEquals(features.get(1).getIdentifier().getID(), "st.2");
            assertEquals(features.get(2).getIdentifier().getID(), "st.5");
            assertEquals(features.get(3).getIdentifier().getID(), "st.6");
            assertEquals(features.get(4).getIdentifier().getID(), "st.10");
            assertEquals(features.get(5).getIdentifier().getID(), "st.11");
        }
    }

    /** Should returns 1, 2, 5, 6, 10, 12(11 on index) */
    @Override
    protected Filter partialIndexedFilter_2idxfilterResults() {
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        List<Filter> filters =
                Arrays.asList(
                        ff.or(
                                ff.equals(ff.property(this.attName), ff.literal("station11")),
                                ff.equals(ff.property(this.attId), ff.literal("st.1"))),
                        ff.like(ff.property(attLocationName), "*America*"),
                        ff.or(
                                ff.equals(ff.property(this.attName), ff.literal("station10")),
                                ff.equals(ff.property(this.attId), ff.literal("st.2"))));
        Filter filter = ff.or(filters);
        return filter;
    }
}
