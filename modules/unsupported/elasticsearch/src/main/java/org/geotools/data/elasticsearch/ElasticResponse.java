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
package org.geotools.data.elasticsearch;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
public class ElasticResponse {

    @JsonProperty("hits")
    private ElasticResults results;

    @JsonProperty("aggregations")
    private Map<String, ElasticAggregation> aggregations;

    @JsonProperty("_scroll_id")
    private String scrollId;

    public ElasticResults getResults() {
        return results;
    }

    public Map<String, ElasticAggregation> getAggregations() {
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
        return "ElasticResponse[total="
                + getTotalNumHits()
                + ", hits="
                + getNumHits()
                + ", aggregations="
                + aggregations
                + ", scrollId="
                + scrollId
                + ", maxScore="
                + getMaxScore()
                + "]";
    }
}
