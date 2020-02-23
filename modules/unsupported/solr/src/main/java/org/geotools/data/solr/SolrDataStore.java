/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.comparators.ComparatorChain;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrQuery.ORDER;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.request.LukeRequest;
import org.apache.solr.client.solrj.response.LukeResponse;
import org.apache.solr.client.solrj.response.LukeResponse.FieldInfo;
import org.apache.solr.client.solrj.response.LukeResponse.FieldTypeInfo;
import org.geotools.data.Query;
import org.geotools.data.solr.SolrUtils.ExtendedFieldSchemaInfo;
import org.geotools.data.store.ContentDataStore;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.FilterCapabilities;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.util.factory.Hints;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * Datastore implementation for SOLR document <br>
 * The types provided from the datastore are obtained querying with distinct a specific SOLR field
 */
public class SolrDataStore extends ContentDataStore {

    // Url of SOLR server
    private URL url;

    // Controlls how documents are mapped to layers
    private SolrLayerMapper layerMapper;

    // Types that the datastore provides obtained
    // Dependent on doc loader being used
    private List<Name> nativeTypeNames;

    // Attributes present in SOLR schema
    private ArrayList<SolrAttribute> solrAttributes = new ArrayList<SolrAttribute>();

    // SOLR uuid attributes
    private SolrAttribute pk = null;

    // Attributes configurations of the store entries
    private Map<String, SolrLayerConfiguration> solrConfigurations =
            new ConcurrentHashMap<String, SolrLayerConfiguration>();

    HttpSolrClient solrServer;

    // feature types build using the provided indexes configuration
    private final Map<String, SimpleFeatureType> defaultFeatureTypes = new HashMap<>();

    public SolrDataStore(URL url, SolrLayerMapper layerMapper, IndexesConfig indexesConfig) {
        this(url, layerMapper);
        // build the feature types based on the provided indexes configuration
        indexesConfig
                .getIndexesNames()
                .forEach(
                        indexName -> {
                            // get from Apache Solr the index schema and retrieve its attributes
                            List<SolrAttribute> solrAttributes = getSolrAttributes(indexName);
                            // build the feature type using the index configuration
                            SimpleFeatureType defaultFeatureType =
                                    indexesConfig.buildFeatureType(indexName, solrAttributes);
                            defaultFeatureTypes.put(indexName, defaultFeatureType);
                        });
    }

    /**
     * Create the data store, using the {@link FieldLayerMapper}.
     *
     * @param url the URL of SOLR server
     * @param field SOLR field to query to obtain the store types
     */
    public SolrDataStore(URL url, String field) {
        this(url, new FieldLayerMapper(field));
    }

    /**
     * Creates the datastore.
     *
     * @param url The URL of SOLR server
     * @param layerMapper The document loader.
     */
    public SolrDataStore(URL url, SolrLayerMapper layerMapper) {
        // TODO: make connection timeouts configurable
        this.url = url;
        this.layerMapper = layerMapper;
        this.solrServer =
                new HttpSolrClient.Builder()
                        .withBaseSolrUrl(url.toString())
                        .allowCompression(true)
                        .withConnectionTimeout(10000)
                        .withSocketTimeout(10000)
                        .build();
        this.solrServer.setFollowRedirects(true);
    }

    /**
     * Retrieve SOLR attribute for specific type <br>
     * Two SOLR LukeRequest are needed to discover SOLR fields and theirs schema for dynamic and
     * static kinds. <br>
     * For each discovered field a SOLR request is needed to verify if the field has no values in
     * the actual type, this information will be stored in {@link SolrAttribute#setEmpty}. <br>
     * SolrJ not extracts information about uniqueKey so custom class {@link
     * ExtendedFieldSchemaInfo} is used. <br>
     * MultiValued SOLR field is mapped as String type
     *
     * @param layerName the type to use to query the SOLR field {@link SolrDataStore#field}
     * @see {@link SolrUtils#decodeSolrFieldType}
     * @see {@link ExtendedFieldSchemaInfo#ExtendedFieldSchemaInfo}
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

                    FieldTypeInfo fty = processSchema.getFieldTypeInfo(type);
                    if (fty != null) {
                        String solrTypeName = fty.getClassName();
                        Class<?> objType = SolrUtils.decodeSolrFieldType(solrTypeName);
                        if (objType != null) {
                            ExtendedFieldSchemaInfo extendedFieldSchemaInfo =
                                    new SolrUtils.ExtendedFieldSchemaInfo(
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
                            at.setEmpty(fieldInfo.getDocs() == 0);
                            solrAttributes.add(at);
                        } else {
                            if (LOGGER.isLoggable(Level.FINE)) {
                                LOGGER.log(
                                        Level.FINE,
                                        "Skipping attribute "
                                                + fty.getName()
                                                + " as we don't know how to map its type to a java object "
                                                + fty.getClassName());
                            }
                        }
                    }
                }
                // Reorder fields: empty after
                List<BeanComparator> sortFields =
                        Arrays.asList(new BeanComparator("empty"), new BeanComparator("name"));
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
            if (nativeTypeNames == null || nativeTypeNames.isEmpty()) {
                List<Name> temp = new ArrayList<>();
                for (String name : layerMapper.createTypeNames(solrServer)) {
                    temp.add(new NameImpl(namespaceURI, name));
                }
                nativeTypeNames = temp;
            }
        } catch (Exception ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        }
        Set<Name> names = new TreeSet<>(nativeTypeNames);
        // also pick the configured layers
        for (SolrLayerConfiguration conf : solrConfigurations.values()) {
            String name = conf.getLayerName();
            names.add(new NameImpl(namespaceURI, name));
        }

        return new ArrayList<>(names);
    }

    @Override
    protected ContentFeatureSource createFeatureSource(ContentEntry entry) throws IOException {
        ContentEntry type = ensureEntry(entry.getName());
        return new SolrFeatureSource(type, defaultFeatureTypes);
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

    /** Gets the attributes configuration for the types in this datastore */
    public Map<String, SolrLayerConfiguration> getSolrConfigurations() {
        return solrConfigurations;
    }

    /** Add the type configuration to this datastore */
    public void setSolrConfigurations(SolrLayerConfiguration configuration) {
        entries.remove(new NameImpl(namespaceURI, configuration.getLayerName()));
        this.solrConfigurations.put(configuration.getLayerName(), configuration);
    }

    /** Get the url of SOLR server */
    public URL getUrl() {
        return url;
    }

    /**
     * Gets the document loader controlling how documents are mapped to layers from the solr index.
     */
    public SolrLayerMapper getLayerMapper() {
        return layerMapper;
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
     * @see {@link Hints#VIRTUAL_TABLE_PARAMETERS}
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
                        query.addSort(
                                sort.getPropertyName().getPropertyName(),
                                sort.getSortOrder().equals(SortOrder.ASCENDING)
                                        ? ORDER.asc
                                        : ORDER.desc);
                    } else {
                        naturalSortOrder =
                                sort.getSortOrder().equals(SortOrder.ASCENDING)
                                        ? ORDER.asc
                                        : ORDER.desc;
                    }
                }
            }

            // Always add natural sort by PK to support pagination
            query.addSort(getPrimaryKey(featureType.getTypeName()).getName(), naturalSortOrder);

            // Encode OGC filer
            FilterToSolr f2s = initializeFilterToSolr(featureType);
            String fq = layerMapper.prepareFilterQuery(featureType);
            Filter simplified = SimplifyingFilterVisitor.simplify(q.getFilter(), featureType);
            String ffq = f2s.encodeToString(simplified);
            if (ffq != null && !ffq.isEmpty()) {
                fq = fq != null ? fq + " AND " + ffq : ffq;
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
     * Create a group by field Solr query
     *
     * @param visitor UniqueVisitor with group settings
     * @return Solr query
     */
    protected SolrQuery selectUniqueValues(
            SimpleFeatureType featureType, Query q, UniqueVisitor visitor) {
        SolrQuery query = select(featureType, q);
        // normal fields empty
        query.setFields(new String[] {});
        // set group data
        query.setParam("group", true);
        PropertyName pname = (PropertyName) visitor.getExpression();
        query.setParam("group.field", pname.getPropertyName());
        query.setParam("group.limit", "0");
        return query;
    }

    /**
     * Builds the SolrJ count query with support of limit/offset, OGC filter encoding and viewParams
     * <br>
     * Currently only additional "q" and "fq" SOLR parameters can be passed using viewParams, this
     * conditions are added in AND with others
     *
     * @param featureType the feature type to query
     * @param q the OGC query to translate in SOLR request
     * @see {@link Hints#VIRTUAL_TABLE_PARAMETERS}
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
            String fq = layerMapper.prepareFilterQuery(featureType);
            String ffq = f2s.encodeToString(q.getFilter());
            if (ffq != null && !ffq.isEmpty()) {
                fq = fq != null ? fq + " AND " + ffq : ffq;
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
            Map<String, String> parameters =
                    (Map<String, String>) hints.get(Hints.VIRTUAL_TABLE_PARAMETERS);
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

    HttpSolrClient getSolrServer() {
        return solrServer;
    }

    @Override
    public void dispose() {
        try {
            solrServer.close();
        } catch (IOException ex) {
            LOGGER.log(Level.SEVERE, ex.getMessage(), ex);
        } finally {
            super.dispose();
        }
    }
}
