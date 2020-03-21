/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *     (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.stream.Collectors;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.ResourceInfo;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.feature.visitor.NearestVisitor;
import org.geotools.feature.visitor.UniqueVisitor;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.SortByImpl;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** Feature source for SOLR datastore */
public class SolrFeatureSource extends ContentFeatureSource {

    /** Used to store native solr type for geometry attributes */
    public static final String KEY_SOLR_TYPE = "solr_type";

    protected SimpleDateFormat dateFormatUTC = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    // feature types build based ont he provided indexes configuration
    private final Map<String, SimpleFeatureType> defaultFeatureTypes;

    /**
     * Creates the new SOLR feature store.
     *
     * @param entry the datastore entry.
     */
    public SolrFeatureSource(ContentEntry entry) {
        this(entry, Collections.emptyMap());
    }

    public SolrFeatureSource(
            ContentEntry entry, Map<String, SimpleFeatureType> defaultFeatureTypes) {
        super(entry, Query.ALL);
        this.defaultFeatureTypes = defaultFeatureTypes;
    }

    @Override
    public SolrDataStore getDataStore() {
        return (SolrDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        CoordinateReferenceSystem flatCRS =
                CRS.getHorizontalCRS(getSchema().getCoordinateReferenceSystem());
        ReferencedEnvelope bounds = new ReferencedEnvelope(flatCRS);
        SolrDataStore store = getDataStore();
        Filter[] split = splitFilter(query.getFilter(), this);
        Filter preFilter = split[0];
        Filter postFilter = split[1];
        Query preQuery = new Query(query);
        preQuery.setFilter(preFilter);
        SolrQuery q = getDataStore().select(getSchema(), preQuery);
        if (getDataStore().getLogger().isLoggable(Level.FINE)) {
            getDataStore().getLogger().log(Level.FINE, q.toString());
        }
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        try {
            reader =
                    new SolrFeatureReader(
                            getSchema(), store.getSolrServer(), q, this.getDataStore());
            // if post filter, wrap it
            if (postFilter != null && postFilter != Filter.INCLUDE) {
                reader =
                        new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(
                                reader, postFilter);
            }
            try {
                if (reader.hasNext()) {
                    SimpleFeature f = reader.next();
                    bounds.init(f.getBounds());
                    while (reader.hasNext()) {
                        f = reader.next();
                        bounds.include(f.getBounds());
                    }
                }
            } finally {
                reader.close();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return bounds;
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        int count = 0;
        try {
            SolrDataStore store = getDataStore();
            Filter[] split = splitFilter(query.getFilter(), this);
            Filter preFilter = split[0];
            Filter postFilter = split[1];
            if (postFilter != null && postFilter != Filter.INCLUDE) {
                // grab a reader
                FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(query);
                try {
                    while (reader.hasNext()) {
                        reader.next();
                        count++;
                    }
                } finally {
                    reader.close();
                }
                return count;
            } else {
                Query preQuery = new Query(query);
                preQuery.setFilter(preFilter);
                SolrQuery q = store.count(getSchema(), preQuery);
                if (store.getLogger().isLoggable(Level.FINE)) {
                    store.getLogger().log(Level.FINE, q.toString());
                }
                @SuppressWarnings("PMD.CloseResource") // not managed here
                HttpSolrClient server = store.getSolrServer();
                QueryResponse rsp = server.query(q);
                count =
                        Long.valueOf(rsp.getResults().getNumFound() - rsp.getResults().getStart())
                                .intValue();
                // Manage max manually
                if (query.getMaxFeatures() > 0 && query.getMaxFeatures() < Integer.MAX_VALUE) {
                    if (count > query.getMaxFeatures()) {
                        count = query.getMaxFeatures();
                    }
                }
            }
        } catch (Throwable e) { // NOSONAR
            if (e instanceof Error) {
                throw (Error) e;
            } else {
                throw (IOException) new IOException().initCause(e);
            }
        }
        return count;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        try {
            SolrDataStore store = getDataStore();
            Filter[] split = splitFilter(query.getFilter(), this);
            Filter preFilter = split[0];
            Filter postFilter = split[1];
            Query preQuery = new Query(query);
            preQuery.setFilter(preFilter);

            // Build the feature type returned by this query. Also build an eventual extra feature
            // type
            // containing the attributes we might need in order to evaluate the post filter
            SimpleFeatureType[] types =
                    buildQueryAndReturnFeatureTypes(
                            getSchema(), query.getPropertyNames(), postFilter);
            SimpleFeatureType querySchema = types[0];
            SimpleFeatureType returnedSchema = types[1];

            SolrQuery q = store.select(querySchema, preQuery);
            if (store.getLogger().isLoggable(Level.FINE)) {
                store.getLogger().log(Level.FINE, q.toString());
            }
            reader = new SolrFeatureReader(querySchema, store.getSolrServer(), q, store);
            if (postFilter != null && postFilter != Filter.INCLUDE) {
                reader =
                        new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(
                                reader, postFilter);
                if (!returnedSchema.equals(querySchema))
                    reader = new ReTypeFeatureReader(reader, returnedSchema);
            }
        } catch (Throwable e) {
            if (e instanceof Error) {
                throw (Error) e;
            } else {
                throw (IOException) new IOException().initCause(e);
            }
        }
        return reader;
    }

    /**
     * Returns a List with distinct-unique values
     *
     * @param visitor with unique field setting
     * @return List with distinct unique values
     */
    protected List<String> getUniqueScalarList(Query query, UniqueVisitor visitor)
            throws IOException {
        List<String> values;
        try {
            SolrDataStore store = getDataStore();
            Filter[] split = splitFilter(query.getFilter(), this);
            Filter preFilter = split[0];
            Query preQuery = new Query(query);
            preQuery.setFilter(preFilter);
            // set start and maz results in query
            preQuery.setStartIndex(visitor.getStartIndex());
            preQuery.setMaxFeatures(visitor.getMaxFeatures());

            @SuppressWarnings("PMD.CloseResource") // not managed here
            HttpSolrClient solrServer = store.getSolrServer();
            SolrQuery q = store.selectUniqueValues(getSchema(), preQuery, visitor);
            QueryResponse rsp = solrServer.query(q);
            values =
                    rsp.getGroupResponse()
                            .getValues()
                            .stream()
                            .filter(g -> g.getName().equals(visitor.getExpression().toString()))
                            .flatMap(gr -> gr.getValues().stream())
                            .map(g -> g.getGroupValue())
                            .collect(Collectors.toList());

        } catch (Throwable e) {
            if (e instanceof Error) {
                throw (Error) e;
            } else {
                throw (IOException) new IOException().initCause(e);
            }
        }
        return values;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        tb.setName(entry.getName());
        SolrLayerConfiguration layerConfiguration =
                getDataStore().getSolrConfigurations().get(entry.getTypeName());
        String pkField = null;
        if (layerConfiguration != null) {
            String defaultGeometryName = null;
            for (SolrAttribute attribute : layerConfiguration.getAttributes()) {
                if (attribute.isUse()) {
                    AttributeDescriptor att = null;
                    if (attribute.isPk()) {
                        pkField = attribute.getName();
                    }
                    if (Geometry.class.isAssignableFrom(attribute.getType())) {
                        // add the attribute as a geometry
                        Integer srid = attribute.getSrid();
                        try {
                            if (srid != null) {
                                ab.setCRS(CRS.decode("EPSG:" + srid));
                                ab.setName(attribute.getName());
                                ab.setBinding(attribute.getType());
                                att =
                                        ab.buildDescriptor(
                                                attribute.getName(), ab.buildGeometryType());
                                if (attribute.isDefaultGeometry() != null
                                        && attribute.isDefaultGeometry()) {
                                    defaultGeometryName = attribute.getName();
                                }
                            }
                        } catch (Exception e) {
                            String msg = "Error occured determing srid for " + attribute.getName();
                            getDataStore().getLogger().log(Level.WARNING, msg, e);
                        }

                    } else {
                        ab.setName(attribute.getName());
                        ab.setBinding(attribute.getType());
                        att = ab.buildDescriptor(attribute.getName(), ab.buildType());
                    }

                    if (att != null) {
                        // store the native solr type
                        att.getUserData().put(KEY_SOLR_TYPE, attribute.getSolrType());
                        tb.add(att);
                    }
                }
            }
            if (defaultGeometryName != null) {
                tb.setDefaultGeometry(defaultGeometryName);
            }
        } else {
            // if a default feature type is available return it
            SimpleFeatureType defaultFeatureType = defaultFeatureTypes.get(entry.getTypeName());
            if (defaultFeatureType != null) {
                // default feature type build based on the provided indexes configuration
                return defaultFeatureType;
            }
        }
        final SimpleFeatureType ft = tb.buildFeatureType();
        if (pkField != null) {
            ft.getUserData().put(SolrLayerConfiguration.ID, pkField);
        }
        return ft;
    }

    @Override
    protected boolean canFilter() {
        return true;
    }

    @Override
    protected boolean canRetype() {
        return true;
    }

    @Override
    protected boolean canOffset() {
        return true;
    }

    @Override
    protected boolean canSort() {
        return true;
    }

    @Override
    protected boolean canLimit() {
        return true;
    }

    @Override
    public ResourceInfo getInfo() {
        return super.getInfo();
    }

    private SimpleFeatureType[] buildQueryAndReturnFeatureTypes(
            SimpleFeatureType featureType, String[] propertyNames, Filter filter) {

        SimpleFeatureType[] types = null;
        if (propertyNames == Query.ALL_NAMES) {
            return new SimpleFeatureType[] {featureType, featureType};
        } else {
            SimpleFeatureType returnedSchema =
                    SimpleFeatureTypeBuilder.retype(featureType, propertyNames);
            SimpleFeatureType querySchema = returnedSchema;

            if (filter != null && !filter.equals(Filter.INCLUDE)) {
                FilterAttributeExtractor extractor = new FilterAttributeExtractor(featureType);
                filter.accept(extractor, null);

                String[] extraAttributes = extractor.getAttributeNames();
                if (extraAttributes != null && extraAttributes.length > 0) {
                    List<String> allAttributes =
                            new ArrayList<String>(Arrays.asList(propertyNames));
                    for (String extraAttribute : extraAttributes) {
                        if (!allAttributes.contains(extraAttribute))
                            allAttributes.add(extraAttribute);
                    }
                    String[] allAttributeArray =
                            allAttributes.toArray(new String[allAttributes.size()]);
                    querySchema = SimpleFeatureTypeBuilder.retype(getSchema(), allAttributeArray);
                }
            }
            types = new SimpleFeatureType[] {querySchema, returnedSchema};
        }
        return types;
    }

    private Filter[] splitFilter(Filter original, FeatureSource source) {
        Filter[] split = new Filter[2];
        if (original != null) {
            SolrFeatureSource featureSource = (SolrFeatureSource) source;
            PostPreProcessFilterSplittingVisitor splitter =
                    new PostPreProcessFilterSplittingVisitor(
                            getDataStore().getFilterCapabilities(),
                            featureSource.getSchema(),
                            null);
            original.accept(splitter, null);
            split[0] = splitter.getFilterPre();
            split[1] = splitter.getFilterPost();
        }
        return split;
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        // UniqueVisitor handling:
        if (visitor instanceof UniqueVisitor) {
            handleUniqueVisitor(query, (UniqueVisitor) visitor);
            return true;
        }

        // Don't do the following shortcuts if we don't request all features as that
        // might introduce subtle bugs.
        if (query.getMaxFeatures() != Integer.MAX_VALUE) {
            return false;
        }

        SortBy sortBy;

        if (visitor instanceof MinVisitor) {
            // Get Minimum value
            MinVisitor minVisitor = (MinVisitor) visitor;
            List<Expression> exprs = minVisitor.getExpressions();
            if (exprs.size() != 1 || !(exprs.get(0) instanceof PropertyName)) {
                return false;
            }

            PropertyName propName = (PropertyName) exprs.get(0);
            sortBy = new SortByImpl(propName, SortOrder.ASCENDING);
        } else if (visitor instanceof MaxVisitor) {
            // Get Maximum Value
            MaxVisitor maxVisitor = (MaxVisitor) visitor;
            List<Expression> exprs = maxVisitor.getExpressions();
            if (exprs.size() != 1 || !(exprs.get(0) instanceof PropertyName)) {
                return false;
            }

            PropertyName propName = (PropertyName) exprs.get(0);
            sortBy = new SortByImpl(propName, SortOrder.DESCENDING);
        } else if (visitor instanceof NearestVisitor) {
            NearestVisitor nearestVisitor = (NearestVisitor) visitor;
            Expression exp = nearestVisitor.getExpression();

            if (!(exp instanceof PropertyName)) {
                return false;
            }

            PropertyName propName = (PropertyName) exp;

            if (!(nearestVisitor.getValueToMatch() instanceof Date)) {
                return false;
            }

            FilterFactory factory = CommonFactoryFinder.getFilterFactory(null);

            // Sort by difference from getValueToMatch, should return closest value
            // at the top of the sort
            PropertyName expr =
                    factory.property(
                            "abs(ms("
                                    + dateFormatUTC.format(nearestVisitor.getValueToMatch())
                                    + ","
                                    + propName.getPropertyName()
                                    + "))");

            sortBy = new SortByImpl(expr, SortOrder.ASCENDING);

        } else {
            // Otherwise let the caller iterate through the entire collection
            return false;
        }

        Query newQuery = new Query(query);
        newQuery.setSortBy(new SortBy[] {sortBy});

        // We set up the sortBy where we only need a single value instead of the
        // entire collection.
        newQuery.setMaxFeatures(1);

        try (FeatureReader<SimpleFeatureType, SimpleFeature> reader = getReader(newQuery)) {
            while (reader.hasNext()) {
                visitor.visit(reader.next());
            }
        }

        return true;
    }

    /** Process UniqueVisitor with group on solr query */
    private void handleUniqueVisitor(Query query, UniqueVisitor visitor) throws IOException {
        visitor.setValue(getUniqueScalarList(query, visitor));
    }
}
