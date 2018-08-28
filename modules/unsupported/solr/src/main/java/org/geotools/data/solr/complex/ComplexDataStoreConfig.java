/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.solr.complex;

import java.net.URL;
import java.util.List;
import org.geotools.data.complex.config.SourceDataStore;
import org.geotools.data.solr.IndexesConfig;

/** Helper class holds the data source configuration provided by App-Schema. */
public class ComplexDataStoreConfig extends SourceDataStore {

    // Apache Solr indexes configuration
    private final IndexesConfig indexesConfig = new IndexesConfig();

    // HTTP URL for the Apache Solr instance
    private URL url;

    /**
     * Adds to the indexes configuration a geometry field specification.
     *
     * @param indexName name of the Apache Solr index
     * @param attributeName index attribute name
     * @param srid the SRID of the geometry, e.g. EPSG:4326
     * @param type type of the geometry, e.g. POINT
     * @param isDefault TRUE fi this is the feature type default geometry
     */
    public void addGeometry(
            String indexName, String attributeName, String srid, String type, String isDefault) {
        indexesConfig.addGeometry(indexName, attributeName, srid, type, isDefault);
    }

    /**
     * Add the attributes that App-Schema is interested in.
     *
     * @param indexName name of the Apache Solr index
     * @param attributes list of the attributes names App-Schema is interested in
     */
    public void addAttributes(String indexName, List<String> attributes) {
        indexesConfig.addAttributes(indexName, attributes);
    }

    /**
     * Return the Apache Sorl indexes configuration.
     *
     * @return the Apache Sorl indexes configuration
     */
    public IndexesConfig getIndexesConfig() {
        return indexesConfig;
    }

    /**
     * Returns the HTTP URL of the Apache Solr instance.
     *
     * @return the HTTP URL of the Apache Sorl instance
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Sets the HTTP URL of the target Apache Solr instance.
     *
     * @param url HTTP URL of the target Apache Solr instance
     */
    public void setUrl(String url) {
        try {
            this.url = new URL(url);
        } catch (Exception exception) {
            throw new RuntimeException(String.format("Error parsing URL '%s'.", url), exception);
        }
    }
}
