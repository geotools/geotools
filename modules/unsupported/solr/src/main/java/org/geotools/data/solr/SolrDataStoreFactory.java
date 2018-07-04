/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Map;
import org.geotools.data.AbstractDataStoreFactory;
import org.geotools.data.DataStore;
import org.geotools.data.solr.SolrLayerMapper.Type;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.util.KVP;

/** Implementation of DataStoreFactory for SOLR server */
public class SolrDataStoreFactory extends AbstractDataStoreFactory {

    /** Url to the SOLR server core */
    public static final Param URL =
            new Param(
                    "solr_url",
                    URL.class,
                    "Url to a SOLR server CORE",
                    true,
                    "http://localhost:8080/solr/collection1",
                    new KVP(Param.LEVEL, "user"));

    /** Document loading strategy */
    public static final Param LAYER_MAPPER =
            new Param(
                    "layer_mapper",
                    String.class,
                    "Controls how documents in the solr index are mapped to layers"
                            + SolrLayerMapper.Type.values(),
                    false,
                    Type.FIELD.name(),
                    new KVP(Param.LEVEL, "user", Param.DEPRECATED, true));

    /** SOLR field that holds the layers names, used by {@link SolrLayerMapper.Type#FIELD}. */
    public static final Param FIELD =
            new Param(
                    "layer_name_field",
                    String.class,
                    "Field used in SOLR that holds the layer names",
                    false,
                    "",
                    new KVP(Param.LEVEL, "user", Param.DEPRECATED, true));

    /** Field that holds the namespace */
    public static final Param NAMESPACE =
            new Param("namespace", String.class, "Namespace prefix", false, "solr");

    @Override
    public DataStore createDataStore(Map<String, Serializable> params) throws IOException {
        URL url = (URL) URL.lookUp(params);
        String namespace = (String) NAMESPACE.lookUp(params);
        String mapperName = (String) LAYER_MAPPER.lookUp(params);
        String fieldName = (String) FIELD.lookUp(params);

        SolrLayerMapper.Type mapperType = SolrLayerMapper.Type.SINGLE;

        // if field name is populated, than the store is of type FIELD (deprecated)
        if (fieldName != null && !fieldName.isEmpty()) {
            // create the layer mapper (FIELD is the default)
            mapperType = SolrLayerMapper.Type.FIELD;
            if (mapperName != null) {
                mapperType = SolrLayerMapper.Type.valueOf(mapperName.toUpperCase());
            }
        }

        SolrLayerMapper mapper = mapperType.createMapper(params);

        SolrDataStore store = new SolrDataStore(url, mapper);
        store.setNamespaceURI(namespace);
        store.setFilterFactory(CommonFactoryFinder.getFilterFactory(null));
        return store;
    }

    @Override
    public DataStore createNewDataStore(Map<String, Serializable> params) throws IOException {
        return createDataStore(params);
    }

    @Override
    public String getDescription() {
        return "Connect to SOLR server (HTTP) and extract features";
    }

    @Override
    public String getDisplayName() {
        return "SOLR";
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return null;
    }

    @Override
    public Param[] getParametersInfo() {
        return new Param[] {URL, LAYER_MAPPER, FIELD, NAMESPACE};
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public boolean canProcess(Map params) {
        if (!super.canProcess(params)) {
            return false; // fail basic param check
        }
        return true;
    }
}
