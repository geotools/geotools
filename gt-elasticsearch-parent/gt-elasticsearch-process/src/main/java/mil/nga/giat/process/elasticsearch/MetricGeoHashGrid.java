/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.process.elasticsearch;

import java.util.List;
import java.util.Map;

public class MetricGeoHashGrid extends GeoHashGrid {

    public final static int METRIC_KEY_INDEX = 0;
    public final static int VALUE_KEY_INDEX = 1;

    public final static String DEFAULT_METRIC_KEY = "metric";

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
    public Number computeCellValue(Map<String,Object> bucket) {
        return super.pluckMetricValue(bucket, metricKey, valueKey);
    }

    public String getMetricKey() {
        return metricKey;
    }

    public String getValueKey() {
        return valueKey;
    }
}
