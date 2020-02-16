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
import java.util.Map;

@SuppressWarnings("unused")
class ElasticMappings {

    private Map<String, Mapping> mappings;

    public Map<String, Mapping> getMappings() {
        return mappings;
    }

    public void setMappings(Map<String, Mapping> mappings) {
        this.mappings = mappings;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Mapping {

        private Map<String, Object> properties;

        public Map<String, Object> getProperties() {
            return properties;
        }
    }

    public static class Untyped {

        private Mapping mappings;

        public Mapping getMappings() {
            return mappings;
        }
    }
}
