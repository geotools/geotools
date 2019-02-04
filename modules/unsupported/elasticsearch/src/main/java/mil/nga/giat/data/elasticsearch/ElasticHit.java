/*
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file.
 */
package mil.nga.giat.data.elasticsearch;

import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown=true)
class ElasticHit {

    @JsonProperty("_index")
    private String index;

    @JsonProperty("_type")
    private String type;

    @JsonProperty("_id")
    private String id;

    @JsonProperty("_score")
    private Float score;

    @JsonProperty("_source")
    private Map<String,Object> source;

    @JsonProperty("fields")
    private Map<String,List<Object>> fields;

    public String getIndex() {
        return index;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public Float getScore() {
        return score;
    }

    public Map<String, Object> getSource() {
        return source;
    }

    public Map<String, List<Object>> getFields() {
        return fields;
    }

    public List<Object> field(String name) {
        return this.fields != null ? this.fields.get(name) : null;
    }

}
