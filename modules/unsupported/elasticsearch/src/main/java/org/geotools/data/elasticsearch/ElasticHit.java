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

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
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
    private Map<String, Object> source;

    @JsonProperty("fields")
    private Map<String, List<Object>> fields;

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
