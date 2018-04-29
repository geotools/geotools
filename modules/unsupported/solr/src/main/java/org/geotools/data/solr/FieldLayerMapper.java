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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.FacetParams;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeatureType;

/** Uses a field of the index to classify documents into feature types. */
public class FieldLayerMapper implements SolrLayerMapper {

    static Logger LOGGER = Logging.getLogger(FieldLayerMapper.class);

    String field;

    public FieldLayerMapper(String field) {
        this.field = field;
    }

    public String getField() {
        return field;
    }

    @Override
    public List<String> createTypeNames(HttpSolrClient solrServer) throws Exception {
        List<String> names = new ArrayList<>();

        SolrQuery query = new SolrQuery();
        query.setQuery("*:*");
        query.addFacetField(field);
        query.setFacet(true);
        query.setFacetMinCount(1);
        query.setFacetSort(FacetParams.FACET_SORT_INDEX);
        query.setRows(0);
        query.setParam("omitHeader", true);

        QueryResponse rsp = solrServer.query(query);
        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.log(Level.FINE, "SOLR query done: " + query.toString());
        }
        List<Count> uniqueFacetFields = rsp.getFacetFields().get(0).getValues();
        for (Count field : uniqueFacetFields) {
            names.add(field.getName());
        }
        return names;
    }

    @Override
    public String prepareFilterQueryForSchema() {
        return this.field + ":*";
    }

    @Override
    public String prepareFilterQuery(SimpleFeatureType featureType) {
        return this.field + ":" + featureType.getTypeName();
    }
}
