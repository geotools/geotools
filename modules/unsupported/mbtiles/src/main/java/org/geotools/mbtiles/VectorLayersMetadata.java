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
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/** Maps to the JSON layers metadata object contained in the json key of the metadata table */
class VectorLayersMetadata {

    static final ObjectMapper MAPPER =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

    static VectorLayersMetadata parseMetadata(String json) throws IOException {
        return (VectorLayersMetadata) MAPPER.readValue(json, VectorLayersMetadata.class);
    }

    @JsonProperty("vector_layers")
    List<VectorLayerMetadata> layers = new ArrayList<>();

    /** The list of layers contained in the JSON description. */
    public List<VectorLayerMetadata> getLayers() {
        return layers;
    }

    public void setLayers(List<VectorLayerMetadata> layers) {
        this.layers = layers;
    }

    /** Returns the {@link VectorLayerMetadata} list as a map keyed from the layer id */
    public LinkedHashMap<String, VectorLayerMetadata> getLayersMap() {
        return layers.stream()
                .collect(
                        Collectors.toMap(
                                l -> l.getId(), l -> l, (l1, l2) -> l1, LinkedHashMap::new));
    }
}
