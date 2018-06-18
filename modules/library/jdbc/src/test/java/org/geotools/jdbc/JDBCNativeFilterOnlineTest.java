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
package org.geotools.jdbc;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.NativeFilter;
import org.opengis.filter.spatial.BBOX;

public abstract class JDBCNativeFilterOnlineTest extends JDBCTestSupport {

    protected final FilterFactory2 filterFactory = CommonFactoryFinder.getFilterFactory2(null);

    public void testNativeFilterExecution() throws Exception {
        // build the filter that will be send to the database
        BBOX boundingBoxFilter =
                filterFactory.bbox(
                        tname("location"), -5, -5, 5, 5, DefaultGeographicCRS.WGS84.toString());
        Filter filter = filterFactory.and(boundingBoxFilter, getNativeFilter());
        // retrieve the features that match the filter above
        ContentFeatureSource featureSource =
                dataStore.getFeatureSource(tname("gt_jdbc_test_measurements"));
        assertThat(featureSource, notNullValue());
        SimpleFeatureIterator iterator = featureSource.getFeatures(filter).features();
        List<SimpleFeature> features = new ArrayList<>();
        while (iterator.hasNext()) {
            features.add(iterator.next());
        }
        iterator.close();
        // check that we retrieved the necessary features
        assertThat(features.size(), is(1));
        assertThat(features.get(0).getAttribute(tname("code")), is("#2"));
    }

    /**
     * Builds a native filter that consider only the measurement of type temperature or type wind,
     * and with a value greater than 15:
     *
     * <p>{@code (TYPE = 'temperature' OR TYPE = 'wind') AND value > 15 }.
     */
    protected abstract NativeFilter getNativeFilter();
}
