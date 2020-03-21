/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.process.elasticsearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeatureType;

class TestUtil {

    public static SimpleFeatureCollection createAggregationFeatures(
            List<Map<String, Object>> data) {
        final SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName("testType");
        builder.add("_aggregation", HashMap.class);
        builder.add("aString", String.class);
        final SimpleFeatureType featureType = builder.buildFeatureType();
        final DefaultFeatureCollection collection = new DefaultFeatureCollection();
        final SimpleFeatureBuilder featureBuilder = new SimpleFeatureBuilder(featureType);
        data.forEach(
                item -> {
                    item.keySet().forEach(key -> featureBuilder.set(key, item.get(key)));
                    collection.add(featureBuilder.buildFeature(null));
                });
        return collection;
    }

    public static Map<String, Object> createDocCountBucket(String bucketName, int docCount) {
        Map<String, Object> bucket = new HashMap<>();
        bucket.put(GeoHashGrid.BUCKET_NAME_KEY, bucketName);
        bucket.put(GeoHashGrid.DOC_COUNT_KEY, docCount);
        return bucket;
    }

    public static Map<String, Object> createMetricBucket(
            int docCount, String metricName, String valueName, int value) {
        Map<String, Object> metric = new HashMap<>();
        metric.put(valueName, value);

        Map<String, Object> bucket = createDocCountBucket("grid_cell_name", docCount);
        bucket.put(metricName, metric);

        return bucket;
    }

    public static List<Map<String, Object>> createBuckets(int[] values) {
        List<Map<String, Object>> buckets = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            buckets.add(createDocCountBucket(Integer.toString(i), values[i]));
        }
        return buckets;
    }

    public static Map<String, Object> createAggBucket(String aggName, int[] values) {
        int totalDocCount = 0;
        List<Map<String, Object>> buckets = new ArrayList<>();
        for (int i = 0; i < values.length; i++) {
            totalDocCount += values[i];
            buckets.add(createDocCountBucket(Integer.toString(i), values[i]));
        }

        Map<String, Object> aggResults = new HashMap<>();
        aggResults.put(GeoHashGrid.BUCKETS_KEY, buckets);

        Map<String, Object> bucket = createDocCountBucket("grid_cell_name", totalDocCount);
        bucket.put(aggName, aggResults);

        return bucket;
    }
}
