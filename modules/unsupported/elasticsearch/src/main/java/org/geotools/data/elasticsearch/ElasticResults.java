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
import tools.jackson.databind.annotation.JsonDeserialize;

@SuppressWarnings("unused")
@JsonIgnoreProperties(ignoreUnknown = true)
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
