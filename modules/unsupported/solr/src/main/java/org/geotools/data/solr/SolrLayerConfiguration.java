/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.solr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/** Describes a SOLR layer configuration as set of {@link SolrAttribute} */
public class SolrLayerConfiguration implements Serializable {

    private static final long serialVersionUID = 1838874365349725912L;

    /** A KEY to identify SOLR layer configuration */
    public static final String KEY = "SolrLayerConfiguration";

    /**
     * A KEY to store PK field name into "user data" of feature type
     *
     * @see {@link SolrFeatureSource#buildFeatureType}
     */
    public static final String ID = "id_field";

    private String layerName;

    private List<SolrAttribute> attributes;

    public SolrLayerConfiguration() {
        attributes = new ArrayList<SolrAttribute>();
    }

    public SolrLayerConfiguration(ArrayList<SolrAttribute> attributes) {
        this.attributes = attributes;
    }

    public List<SolrAttribute> getAttributes() {
        return attributes;
    }

    public void setAttributes(List<SolrAttribute> attributes) {
        this.attributes = attributes;
    }

    public String getLayerName() {
        return layerName;
    }

    public void setLayerName(String layerName) {
        this.layerName = layerName;
    }
}
