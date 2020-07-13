/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbtiles;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Collectors;

/** Maps to the JSON structure describing a layer of vector tiles */
class VectorLayerMetadata {

    String id;
    String description;

    @JsonProperty("minzoom")
    Integer minZoom;

    @JsonProperty("maxzoom")
    Integer maxZoom;

    Map<String, String> fields;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMinZoom() {
        return minZoom;
    }

    public void setMinZoom(Integer minZoom) {
        this.minZoom = minZoom;
    }

    public Integer getMaxZoom() {
        return maxZoom;
    }

    public void setMaxZoom(Integer maxZoom) {
        this.maxZoom = maxZoom;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }

    /**
     * Returns the fields with their type parsed to a binding class (java own String, Double,
     * Boolean)
     */
    public LinkedHashMap<String, Class> getFieldBindings() {
        return fields.entrySet()
                .stream()
                .collect(
                        Collectors.toMap(
                                e -> e.getKey(),
                                e -> toBinding(e.getValue()),
                                (v1, v2) -> v1,
                                LinkedHashMap::new));
    }

    /** Tolerant type mapper, maps Number to Double, Doolean to boolean, any other name to String */
    protected Class toBinding(String typeName) {
        if ("Number".equals(typeName)) {
            return Number.class;
        } else if ("Boolean".equals(typeName)) {
            return Boolean.class;
        } else {
            return String.class;
        }
    }
}
