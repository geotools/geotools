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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;

public class NestedAggGeoHashGrid extends GeoHashGrid {

    private static final Logger LOGGER = Logging.getLogger(NestedAggGeoHashGrid.class);

    private static final int NESTED_KEY_INDEX = 0;

    private static final int METRIC_KEY_INDEX = 1;

    private static final int VALUE_KEY_INDEX = 2;

    private static final int SELECTION_STRATEGY_INDEX = 3;

    private static final int RASTER_STRATEGY_INDEX = 4;

    private static final int TERMS_MAP_INDEX = 5;

    static final String SELECT_LARGEST = "largest";

    static final String SELECT_SMALLEST = "smallest";

    static final String RASTER_FROM_VALUE = "value";

    static final String RASTER_FROM_KEY = "key";

    static final String DEFAULT_AGG_KEY = "nested";

    static final String DEFAULT_METRIC_KEY = "";

    private String nestedAggKey = DEFAULT_AGG_KEY;

    private String metricKey = DEFAULT_METRIC_KEY;

    private String valueKey = GeoHashGrid.VALUE_KEY;

    private String selectionStrategy = SELECT_LARGEST;

    private String rasterStrategy = RASTER_FROM_VALUE;

    private Map<String, Integer> termsMap = null;

    @Override
    public void setParams(List<String> params) {
        if (null != params) {
            if (params.size() < 5) {
                LOGGER.warning("Parameters list does not contain required length; you provided "
                        + params.size()
                        + ", expecting: 5 or more");
                throw new IllegalArgumentException();
            }
            nestedAggKey = params.get(NESTED_KEY_INDEX);
            metricKey = params.get(METRIC_KEY_INDEX);
            valueKey = params.get(VALUE_KEY_INDEX);
            switch (params.get(SELECTION_STRATEGY_INDEX)) {
                case SELECT_SMALLEST:
                    selectionStrategy = params.get(SELECTION_STRATEGY_INDEX);
                    break;
                case SELECT_LARGEST:
                    selectionStrategy = params.get(SELECTION_STRATEGY_INDEX);
                    break;
                default:
                    LOGGER.warning("Unexpected buckets selection strategy parameter; you provided "
                            + params.get(SELECTION_STRATEGY_INDEX)
                            + ", defaulting to: "
                            + selectionStrategy);
            }
            switch (params.get(RASTER_STRATEGY_INDEX)) {
                case RASTER_FROM_VALUE:
                    rasterStrategy = params.get(RASTER_STRATEGY_INDEX);
                    break;
                case RASTER_FROM_KEY:
                    rasterStrategy = params.get(RASTER_STRATEGY_INDEX);
                    break;
                default:
                    LOGGER.warning("Unexpected raster strategy parameter; you provided "
                            + params.get(RASTER_STRATEGY_INDEX)
                            + ", defaulting to: "
                            + rasterStrategy);
            }
            if (rasterStrategy.equals(RASTER_FROM_KEY) && params.size() >= 6) {
                termsMap = new HashMap<>();
                String[] terms = params.get(TERMS_MAP_INDEX).split(";");
                for (String term : terms) {
                    String[] keyValueSplit = term.split(":");
                    if (keyValueSplit.length != 2) {
                        LOGGER.warning("Term " + term + " does not contain required format <key>:<value>");
                        throw new IllegalArgumentException();
                    }
                    termsMap.put(keyValueSplit[0], Integer.valueOf(keyValueSplit[1]));
                }
            }
        }
    }

    @Override
    public Number computeCellValue(Map<String, Object> geogridBucket) {
        List<Map<String, Object>> aggBuckets = super.pluckAggBuckets(geogridBucket, nestedAggKey);
        Number rasterValue = 0;
        switch (selectionStrategy) {
            case SELECT_SMALLEST:
                rasterValue = selectSmallest(aggBuckets);
                break;
            case SELECT_LARGEST:
                rasterValue = selectLargest(aggBuckets);
                break;
        }
        return rasterValue;
    }

    Number selectLargest(List<Map<String, Object>> buckets) {
        String largestKey = pluckBucketName(buckets.get(0));
        Number largestValue = super.pluckMetricValue(buckets.get(0), metricKey, valueKey);
        for (Map<String, Object> bucket : buckets) {
            Number value = super.pluckMetricValue(bucket, metricKey, valueKey);
            if (value.doubleValue() > largestValue.doubleValue()) {
                largestKey = super.pluckBucketName(bucket);
                largestValue = value;
            }
        }
        return bucketToRaster(largestKey, largestValue);
    }

    Number selectSmallest(List<Map<String, Object>> buckets) {
        String smallestKey = pluckBucketName(buckets.get(0));
        Number smallestValue = super.pluckMetricValue(buckets.get(0), metricKey, valueKey);
        for (Map<String, Object> bucket : buckets) {
            Number value = super.pluckMetricValue(bucket, metricKey, valueKey);
            if (value.doubleValue() < smallestValue.doubleValue()) {
                smallestKey = super.pluckBucketName(bucket);
                smallestValue = value;
            }
        }
        return bucketToRaster(smallestKey, smallestValue);
    }

    Number bucketToRaster(String key, Number value) {
        Number rasterValue = value;
        if (rasterStrategy.equals(RASTER_FROM_KEY)) {
            if (null != termsMap) {
                if (termsMap.containsKey(key)) {
                    rasterValue = termsMap.get(key);
                } else {
                    LOGGER.warning("Cannot convert key (String) to raster value, mapping does not contain key "
                            + key
                            + ". Add key to terms_map argument to resolve.");
                    throw new IllegalArgumentException();
                }
            } else {
                try {
                    rasterValue = Double.valueOf(key);
                } catch (NumberFormatException nfe) {
                    LOGGER.warning("Cannot convert key (String) to raster value, key, "
                            + key
                            + ", is not a number. Use terms_map argument to map Strings to Numbers.");
                    throw new IllegalArgumentException();
                }
            }
        }
        return rasterValue;
    }

    public String getNestedAggKey() {
        return nestedAggKey;
    }

    public String getMetricKey() {
        return metricKey;
    }

    public String getValueKey() {
        return valueKey;
    }

    public String getSelectionStrategy() {
        return selectionStrategy;
    }

    public String getRasterStrategy() {
        return rasterStrategy;
    }

    public Map<String, Integer> getTermsMap() {
        return termsMap;
    }
}
