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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;

import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.impl.HttpSolrServer;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureSource;
import org.geotools.data.FilteringFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.ReTypeFeatureReader;
import org.geotools.data.ResourceInfo;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.AttributeTypeBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Feature source for SOLR datastore
 */
public class SolrFeatureSource extends ContentFeatureSource {

    /**
     * Creates the new SOLR feature store.
     * 
     * @param entry the datastore entry.
     */
    public SolrFeatureSource(ContentEntry entry) {
        super(entry, Query.ALL);
    }

    @Override
    public SolrDataStore getDataStore() {
        return (SolrDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        CoordinateReferenceSystem flatCRS = CRS.getHorizontalCRS(getSchema()
                .getCoordinateReferenceSystem());
        ReferencedEnvelope bounds = new ReferencedEnvelope(flatCRS);
        SolrDataStore store = getDataStore();
        Filter[] split = splitFilter(query.getFilter(), this);
        Filter preFilter = split[0];
        Filter postFilter = split[1];
        DefaultQuery preQuery = new DefaultQuery(query);
        preQuery.setFilter(preFilter);
        SolrQuery q = getDataStore().select(getSchema(), preQuery);
        if (getDataStore().getLogger().isLoggable(Level.FINE)) {
            getDataStore().getLogger().log(Level.FINE, q.toString());
        }
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        try {
            reader = new SolrFeatureReader(getSchema(), store.getSolrServer(), q,
                    this.getDataStore());
            // if post filter, wrap it
            if (postFilter != null && postFilter != Filter.INCLUDE) {
                reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader,
                        postFilter);
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
                DefaultQuery preQuery = new DefaultQuery(query);
                preQuery.setFilter(preFilter);
                SolrQuery q = store.count(getSchema(), preQuery);
                if (store.getLogger().isLoggable(Level.FINE)) {
                    store.getLogger().log(Level.FINE, q.toString());
                }
                HttpSolrServer server = store.getSolrServer();
                QueryResponse rsp = server.query(q);
                count = rsp.getResults().size();
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
            DefaultQuery preQuery = new DefaultQuery(query);
            preQuery.setFilter(preFilter);

            // Build the feature type returned by this query. Also build an eventual extra feature
            // type
            // containing the attributes we might need in order to evaluate the post filter
            SimpleFeatureType[] types = buildQueryAndReturnFeatureTypes(getSchema(),
                    query.getPropertyNames(), postFilter);
            SimpleFeatureType querySchema = types[0];
            SimpleFeatureType returnedSchema = types[1];

            SolrQuery q = store.select(querySchema, preQuery);
            if (store.getLogger().isLoggable(Level.FINE)) {
                store.getLogger().log(Level.FINE, q.toString());
            }
            reader = new SolrFeatureReader(querySchema, store.getSolrServer(), q, store);
            if (postFilter != null && postFilter != Filter.INCLUDE) {
                reader = new FilteringFeatureReader<SimpleFeatureType, SimpleFeature>(reader,
                        postFilter);
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

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        tb.setName(entry.getName());
        SolrLayerConfiguration layerConfiguration = getDataStore().getSolrConfigurations().get(
                entry.getTypeName());
        String pkField = null;
        if (layerConfiguration != null) {
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
                                att = ab.buildDescriptor(attribute.getName(),
                                        ab.buildGeometryType());
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
                    tb.add(att);
                }
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

    private SimpleFeatureType[] buildQueryAndReturnFeatureTypes(SimpleFeatureType featureType,
            String[] propertyNames, Filter filter) {

        SimpleFeatureType[] types = null;
        if (propertyNames == Query.ALL_NAMES) {
            return new SimpleFeatureType[] { featureType, featureType };
        } else {
            SimpleFeatureType returnedSchema = SimpleFeatureTypeBuilder.retype(featureType,
                    propertyNames);
            SimpleFeatureType querySchema = returnedSchema;

            if (filter != null && !filter.equals(Filter.INCLUDE)) {
                FilterAttributeExtractor extractor = new FilterAttributeExtractor(featureType);
                filter.accept(extractor, null);

                String[] extraAttributes = extractor.getAttributeNames();
                if (extraAttributes != null && extraAttributes.length > 0) {
                    List<String> allAttributes = new ArrayList<String>(Arrays.asList(propertyNames));
                    for (String extraAttribute : extraAttributes) {
                        if (!allAttributes.contains(extraAttribute))
                            allAttributes.add(extraAttribute);
                    }
                    String[] allAttributeArray = allAttributes.toArray(new String[allAttributes
                            .size()]);
                    querySchema = SimpleFeatureTypeBuilder.retype(getSchema(), allAttributeArray);
                }
            }
            types = new SimpleFeatureType[] { querySchema, returnedSchema };
        }
        return types;
    }

    private Filter[] splitFilter(Filter original, FeatureSource source) {
        Filter[] split = new Filter[2];
        if (original != null) {
            SolrFeatureSource featureSource = (SolrFeatureSource) source;
            ;
            PostPreProcessFilterSplittingVisitor splitter = new PostPreProcessFilterSplittingVisitor(
                    getDataStore().getFilterCapabilities(), featureSource.getSchema(), null);
            original.accept(splitter, null);
            split[0] = splitter.getFilterPre();
            split[1] = splitter.getFilterPost();
        }
        return split;
    }

}
