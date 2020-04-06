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

import java.util.List;
import java.util.Map;

public class MetricGeoHashGrid extends GeoHashGrid {

    public static final String DEFAULT_METRIC_KEY = "metric";

    private static final int METRIC_KEY_INDEX = 0;

    private static final int VALUE_KEY_INDEX = 1;

    private String metricKey = DEFAULT_METRIC_KEY;

    private String valueKey = GeoHashGrid.VALUE_KEY;

    @Override
    public void setParams(List<String> params) {
        if (null != params) {
            if (params.size() >= 1) {
                metricKey = params.get(METRIC_KEY_INDEX);
            }
            if (params.size() >= 2) {
                valueKey = params.get(VALUE_KEY_INDEX);
            }
        }
    }

    @Override
    public Number computeCellValue(Map<String, Object> bucket) {
        return super.pluckMetricValue(bucket, metricKey, valueKey);
    }

    public String getMetricKey() {
        return metricKey;
    }

    public String getValueKey() {
        return valueKey;
    }
}
