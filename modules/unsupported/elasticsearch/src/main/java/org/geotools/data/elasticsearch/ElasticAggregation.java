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
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
class ElasticAggregation {

    private List<Map<String, Object>> buckets;

    public List<Map<String, Object>> getBuckets() {
        return buckets;
    }

    public void setBuckets(List<Map<String, Object>> buckets) {
        this.buckets = buckets;
    }

    @Override
    public String toString() {
        return "ElasticAggregation[numBuckets=" + (buckets != null ? buckets.size() : 0) + "]";
    }
}
