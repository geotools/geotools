/**
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticAggregation {

    private List<Map<String,Object>> buckets;

    public List<Map<String, Object>> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Map<String, Object>> buckets) {
        this.buckets = buckets;
    }

    @Override
    public String toString() {
        return new StringBuilder("ElasticAggregation[numBuckets=")
                .append(buckets != null ? buckets.size() : 0)
                .append("]").toString();
    }

}
