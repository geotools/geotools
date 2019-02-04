/*
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown=true)
public class ElasticResults {

    @JsonDeserialize(using = TotalDeserializer.class)
    private Long total;

    @JsonProperty("max_score")
    private Float maxScore;

    private List<ElasticHit> hits;

    public Long getTotal() {
        return total;
    }

    public Float getMaxScore() {
        return maxScore;
    }

    public List<ElasticHit> getHits() {
        return hits;
    }

}
