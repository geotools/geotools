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

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.FacetField.Count;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.client.solrj.response.LukeResponse.FieldTypeInfo;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.common.params.FacetParams;
import org.geotools.data.Query;
import org.geotools.data.solr.SolrUtils.ExtendedFieldSchemaInfo;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.NameImpl;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Datastore implementation for SOLR document <br>
 * The types provided from the datastore are obtained querying with distinct a specific SOLR field
 */

public class SolrDataStore extends ContentDataStore {

    // Url of SOLR server
    private URL url;

    // Field to filter to obtain the types that the datastore provides.
    private String field;

    // Types that the datastore provides obtained by differents values of field "field"
    private List<Name> typeNames;

    // Attributes present in SOLR schema
    private ArrayList<SolrAttribute> solrAttributes = new ArrayList<SolrAttribute>();

    // SOLR uuid attributes
    private SolrAttribute pk = null;

    // Attributes configurations of the store entries
    private Map<String, SolrLayerConfiguration> solrConfigurations = new ConcurrentHashMap<String, SolrLayerConfiguration>();

    HttpSolrServer solrServer;

    /**
     * Create the data store
     * 
     * @param url the URL of SOLR server
     * @param field SOLR field to query to obtain the store types
     */
    public SolrDataStore(URL url, String field) {
        this.url = url;
        this.field = field;
        this.solrServer = new HttpSolrServer(url.toString());
        this.solrServer.setAllowCompression(true);
        this.solrServer.setConnectionTimeout(10000);
        this.solrServer.setFollowRedirects(true);
        this.solrServer.setSoTimeout(10000);
    }

    /**
     * Retrieve SOLR attribute for specific type <br/>
     * Two SOLR LukeRequest are needed to discover SOLR fields and theirs schema for dynamic and
     * static kinds. <br/>
     * For each discovered field a SOLR request is needed to verify if the field has no values in
     * the actual type, this information will be stored in {@link SolrAttribute#setEmpty}. <br/>
     * SolrJ not extracts information about uniqueKey so custom class
     * {@link ExtendedFieldSchemaInfo} is used. <br/>
     * MultiValued SOLR field is mapped as String type
     * 
     * @param layerName the type to use to query the SOLR field {@link SolrDataStore#field}
     * 
     * @see {@link SolrUtils#decodeSolrFieldType}
     * @see {@link ExtendedFieldSchemaInfo#ExtendedFieldSchemaInfo}
     * 
     */
    public ArrayList<SolrAttribute> getSolrAttributes(String layerName) {
        if (solrAttributes.isEmpty()) {
            solrAttributes = new ArrayList<SolrAttribute>();
            try {
                LukeRequest lq = new LukeRequest();
                lq.setShowSchema(true);
                LukeResponse processSchema = lq.process(solrServer);

                lq = new LukeRequest();
                lq.setShowSchema(false);
                LukeResponse processField = lq.process(solrServer);
                Map<String, FieldInfo> fis = processField.getFieldInfo();
                SortedSet<String> keys = new TreeSet<String>(fis.keySet());
                for (String k : keys) {
                    FieldInfo fieldInfo = fis.get(k);
                    String name = fieldInfo.getName();
                    String type = fieldInfo.getType();

                    SolrQuery query = new SolrQuery();
                    query.setQuery("*:*");
                    query.setRows(0);
                    query.addFilterQuery(this.field + ":*");
                    if (layerName != null && layerName.isEmpty()) {
                        query.addFilterQuery(name + ":" + layerName);
                    } else {
                        query.addFilterQuery(name + ":*");
                    }
                    QueryResponse rsp = solrServer.query(query);
                    long founds = rsp.getResults().getNumFound();

                    FieldTypeInfo fty = processSchema.getFieldTypeInfo(type);
                    if (fty != null) {
                        String solrTypeName = fty.getClassName();
                        Class<?> objType = SolrUtils.decodeSolrFieldType(solrTypeName);
                        if (objType != null) {
                            ExtendedFieldSchemaInfo extendedFieldSchemaInfo = new SolrUtils.ExtendedFieldSchemaInfo(
                                    processSchema, processField, name);
                            SolrAttribute at = new SolrAttribute(name, objType);
                            at.setSolrType(solrTypeName);
                            if (extendedFieldSchemaInfo.getUniqueKey()) {
                                at.setPk(true);
                                at.setUse(true);
                            }
                            if (extendedFieldSchemaInfo.getMultivalued()
                                    && !Geometry.class.isAssignableFrom(at.getType())) {
                                at.setType(String.class);
                            }
                            at.setEmpty(founds == 0);
                            solrAttributes.add(at);
                        } else {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(Level.FINE, "Skipping attribute " + fty.getName()
                                        + " as we don't know how to map its type to a java object "
                                        + fty.getClassName());
                            }
                        }
                    }
                }
                // Reorder fields: empty after
                List<BeanComparator> sortFields = Arrays.asList(new BeanComparator("empty"),
                        new BeanComparator("name"));
                ComparatorChain multiSort = new ComparatorChain(sortFields);
                Collections.sort(solrAttributes, multiSort);
            } catch (Exception ex) {
                LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
            }
        }
        return solrAttributes;
    }

    @Override
    protected List<Name> createTypeNames() throws IOException {
        try {
            if (typeNames == null || typeNames.isEmpty()) {
                typeNames = new ArrayList<Name>();
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
                    typeNames.add(new NameImpl(namespaceURI, field.getName()));
                }
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        return typeNames;
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        ContentEntry type = ensureEntry(entry.getName());
        return new SolrFeatureSource(type);
    }

    @Override
    public FilterFactory getFilterFactory() {
        return CommonFactoryFinder.getFilterFactory2();
    }

    /**
     * The filter capabilities which reports which spatial operations the underlying SOLR server can
     * handle natively.
     * 
     * @return The filter capabilities, never <code>null</code>.
     */
    public FilterCapabilities getFilterCapabilities() {
        FilterToSolr f2s = new FilterToSolr(null);
        return f2s.getCapabilities();
    }

    /**
     * Gets the attributes configuration for the types in this datastore
     */
    public Map<String, SolrLayerConfiguration> getSolrConfigurations() {
        return solrConfigurations;
    }

    /**
     * Add the type configuration to this datastore
     */
    public void setSolrConfigurations(SolrLayerConfiguration configuration) {
        entries.remove(new NameImpl(namespaceURI, configuration.getLayerName()));
        this.solrConfigurations.put(configuration.getLayerName(), configuration);
    }

    /**
     * Get the url of SOLR server
     */
    public URL getUrl() {
        return url;
    }

    /**
     * Get the field used to filter the types that the datastore provides.
     */
    public String getField() {
        return field;
    }

    /**
     * Gets the primary key attribute a type in this datastore.</br> If the key is not currently
     * available a call to {@link #getSolrAttributes} is needed.
     * 
     * @param layerName the type to use to query the SOLR field {@link SolrDataStore#field}
     */
    public SolrAttribute getPrimaryKey(String layerName) {
        if (pk == null) {
            ArrayList<SolrAttribute> attributes = getSolrAttributes(layerName);
            for (SolrAttribute at : attributes) {
                if (at.isPk()) {
                    pk = at;
                    break;
                }
            }
        }
        return pk;
    }

    /**
     * Builds the SolrJ query with support of subset of fields, limit/offset, sorting, OGC filter
     * encoding and viewParams <br>
     * The SOLR query always need the order by PK field to enable pagination and efficient data
     * retrieving <br>
     * Currently only additional "q" and "fq" SOLR parameters can be passed using vireParams, this
     * conditions are added in AND with others
     * 
     * @param featureType the feature type to query
     * @param q the OGC query to translate in SOLR request
     * 
     * @see {@link Hints#VIRTUAL_TABLE_PARAMETERS}
     * 
     */
    protected SolrQuery select(SimpleFeatureType featureType, Query q) {
        SolrQuery query = new SolrQuery();
        query.setParam("omitHeader", true);
        try {
            // Column names
            if (q.getPropertyNames() != null) {
                for (String prop : q.getPropertyNames()) {
                    query.addField(prop);
                }
            }
            query.setQuery("*:*");

            // Encode limit/offset, if necessary
            if (q.getStartIndex() != null && q.getStartIndex() >= 0) {
                query.setStart(q.getStartIndex());
            }
            if (q.getMaxFeatures() > 0) {
                query.setRows(q.getMaxFeatures());
            }

            // Sort
            ORDER naturalSortOrder = ORDER.asc;
            if (q.getSortBy() != null) {
                for (SortBy sort : q.getSortBy()) {
                    if (sort.getPropertyName() != null) {
                        query.addSort(sort.getPropertyName().getPropertyName(), sort.getSortOrder()
                                .equals(SortOrder.ASCENDING) ? ORDER.asc : ORDER.desc);
                    } else {
                        naturalSortOrder = sort.getSortOrder().equals(SortOrder.ASCENDING) ? ORDER.asc
                                : ORDER.desc;
                    }
                }
            }

            // Always add natural sort by PK to support pagination
            query.addSort(getPrimaryKey(featureType.getTypeName()).getName(), naturalSortOrder);

            // Encode OGC filer
            FilterToSolr f2s = initializeFilterToSolr(featureType);
            String fq = this.field + ":" + featureType.getTypeName();
            Filter simplified = SimplifyingFilterVisitor.simplify(q.getFilter());
            String ffq = f2s.encodeToString(simplified);
            if (ffq != null && !ffq.isEmpty()) {
                fq = fq + " AND " + ffq;
            }
            query.setFilterQueries(fq);

            // Add viewpPrams
            addViewparams(q, query);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return query;
    }

    /**
     * Builds the SolrJ count query with support of limit/offset, OGC filter encoding and viewParams <br>
     * Currently only additional "q" and "fq" SOLR parameters can be passed using viewParams, this
     * conditions are added in AND with others
     * 
     * @param featureType the feature type to query
     * @param q the OGC query to translate in SOLR request
     * 
     * @see {@link Hints#VIRTUAL_TABLE_PARAMETERS}
     * 
     */
    protected SolrQuery count(SimpleFeatureType featureType, Query q) {
        SolrQuery query = new SolrQuery();
        query.setParam("omitHeader", true);
        query.setQuery("*:*");
        query.setFields(this.getPrimaryKey(featureType.getName().getLocalPart()).getName());
        try {

            // Encode limit/offset, if necessary
           
            if (q.getStartIndex() != null && q.getStartIndex() >= 0) {
                query.setStart(q.getStartIndex());
            }
            query.setRows(0);

            // Encode OGC filer
            FilterToSolr f2s = initializeFilterToSolr(featureType);
            String fq = this.field + ":" + featureType.getTypeName();
            String ffq = f2s.encodeToString(q.getFilter());
            if (ffq != null && !ffq.isEmpty()) {
                fq = fq + " AND " + ffq;
            }
            query.setFilterQueries(fq);

            // Add viewparams parameters
            addViewparams(q, query);

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        return query;
    }

    /*
     * Set parameters for OGC filter encoder
     */
    private FilterToSolr initializeFilterToSolr(SimpleFeatureType featureType) {
        FilterToSolr f2s = new FilterToSolr(featureType);
        f2s.setPrimaryKey(this.getPrimaryKey(featureType.getName().getLocalPart()));
        f2s.setFeatureTypeName(featureType.getName().getLocalPart());

        return f2s;
    }

    /*
     * Add viewParams to SOLR query
     */
    private void addViewparams(Query q, SolrQuery query) {
        String qViewParamers = null;
        String fqViewParamers = null;
        Hints hints = q.getHints();
        if (hints != null) {
            Map<String, String> parameters = (Map<String, String>) hints
                    .get(Hints.VIRTUAL_TABLE_PARAMETERS);
            if (parameters != null) {
                for (String param : parameters.keySet()) {
                    // Accepts only q and fq query parameters
                    if (param.equalsIgnoreCase("q")) {
                        qViewParamers = parameters.get(param);
                    }
                    if (param.equalsIgnoreCase("fq")) {
                        fqViewParamers = parameters.get(param);
                    }
                }
            }
        }
        if (qViewParamers != null && !qViewParamers.isEmpty()) {
            query.set("q", query.get("q").concat(" AND ").concat(qViewParamers));
        }
        if (fqViewParamers != null && !fqViewParamers.isEmpty()) {
            query.addFilterQuery(fqViewParamers);
        }
    }

    HttpSolrServer getSolrServer() {
        return solrServer;
    }

    @Override
    public void dispose() {
        try {
            solrServer.shutdown();
        } finally {
            super.dispose();
        }
    }

}
