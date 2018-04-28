/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2016, Open Source Geospatial Foundation (OSGeo)
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeatureType;

/** Maps the solr index as a single layer. */
public class SingleLayerMapper implements SolrLayerMapper {

    static final Logger LOGGER = Logging.getLogger(SingleLayerMapper.class);

    @Override
    public List<String> createTypeNames(HttpSolrClient solr) throws Exception {
        // try to parse the url and return the name of the core
        try {
            URL url = new URL(solr.getBaseURL());
            String[] path = url.getPath() != null ? url.getPath().split("/") : null;
            if (path != null && path.length > 0) {
                String last = path[path.length - 1];
                if (!last.trim().isEmpty()) {
                    return Arrays.asList(last);
                }
            }
        } catch (MalformedURLException e) {
            LOGGER.log(Level.FINE, "unable to parse solr url: " + solr.getBaseURL(), e);
        }

        // default
        return Arrays.asList("index");
    }

    @Override
    public String prepareFilterQueryForSchema() {
        return null;
    }

    @Override
    public String prepareFilterQuery(SimpleFeatureType featureType) {
        return null;
    }
}
