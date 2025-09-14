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

import java.io.Serial;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Describes an Elasticsearch layer configuration as set of {@link ElasticAttribute} */
public class ElasticLayerConfiguration implements Serializable {

    @Serial
    private static final long serialVersionUID = 1838874365349725912L;

    /** Key to identify the Elasticsearch layer configuration. */
    public static final String KEY = "ElasticLayerConfiguration";

    private final String docType;

    private String layerName;

    private final List<ElasticAttribute> attributes;

    public ElasticLayerConfiguration(String docType) {
        this.docType = docType;
        this.layerName = docType;
        this.attributes = new ArrayList<>();
    }

    public ElasticLayerConfiguration(ElasticLayerConfiguration other) {
        this(other.docType);
        setLayerName(other.layerName);
        for (final ElasticAttribute attribute : other.attributes) {
            attributes.add(new ElasticAttribute(attribute));
        }
    }

    public String getDocType() {
        return docType;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }

    public List<ElasticAttribute> getAttributes() {
        return attributes;
    }
}
