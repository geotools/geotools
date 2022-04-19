/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import java.util.ArrayList;
import java.util.List;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;

public abstract class JDBCJsonArrayContainsOnlineTest extends JDBCTestSupport {

    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);

    protected void checkFunction(String json, String pointer, Object expected, int countResult)
            throws Exception {

        ContentFeatureSource featureSource = dataStore.getFeatureSource(tname("jsontest"));
        Function function =
                ff.function(
                        "jsonArrayContains",
                        ff.property(json),
                        ff.literal(pointer),
                        ff.literal(expected));
        Filter filter = ff.equals(function, ff.literal(true));
        try (SimpleFeatureIterator iterator = featureSource.getFeatures(filter).features()) {
            List<SimpleFeature> features = new ArrayList<>();
            while (iterator.hasNext()) {
                features.add(iterator.next());
            }
            assertEquals(features.size(), countResult);
        }
    }
}
