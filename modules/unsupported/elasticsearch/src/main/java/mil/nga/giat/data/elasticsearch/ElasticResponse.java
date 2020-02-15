/*
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElasticResponse {

    @JsonProperty("hits")
    private ElasticResults results;

    @JsonProperty("aggregations")
    private Map<String,ElasticAggregation> aggregations;

    @JsonProperty("_scroll_id")
    private String scrollId;

    public ElasticResults getResults() {
        return results;
    }

    public Map<String,ElasticAggregation> getAggregations() {
        return aggregations;
    }

    public String getScrollId() {
        return scrollId;
    }

    @JsonIgnore
    public List<ElasticHit> getHits() {
        final List<ElasticHit> hits;
        if (results != null) {
            hits = results.getHits();
        } else {
            hits = new ArrayList<>();
        }
        return hits;
    }

    public int getNumHits() {
        final int numHits;
        if (results != null) {
            numHits = results.getHits().size();
        } else {
            numHits = 0;
        }
        return numHits;
    }

    public long getTotalNumHits() {
        final long total;
        if (results != null && results.getTotal() != null) {
            total = results.getTotal();
        } else {
            total = 0L;
        }
        return total;
    }

    public float getMaxScore() {
        final float maxScore;
        if (results != null && results.getMaxScore() != null) {
            maxScore = results.getMaxScore();
        } else {
            maxScore = 0f;
        }
        return maxScore;
    }

    @Override
    public String toString() {
        return "ElasticResponse[total=" +
                getTotalNumHits() +
                ", hits=" + getNumHits() +
                ", aggregations=" + aggregations +
                ", scrollId=" + scrollId +
                ", maxScore=" + getMaxScore() +
                "]";
    }

}
