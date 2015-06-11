/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.simple.FilteringSimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.filter.visitor.PostPreProcessFilterSplittingVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;

public class MongoFeatureSource extends ContentFeatureSource {

    static Logger LOG = Logging.getLogger("org.geotools.data.mongodb");

    final DBCollection collection;

    CollectionMapper mapper;

    public MongoFeatureSource(ContentEntry entry, Query query, DBCollection collection) {
        super(entry, query);
        this.collection = collection;
        initMapper();
    }

    final void initMapper() {
        // use schema with mapping info if it exists
        SimpleFeatureType type = entry.getState(null).getFeatureType();
        setMapper(type != null ? new MongoSchemaMapper(type) : new MongoInferredMapper());
    }

    public DBCollection getCollection() {
        return collection;
    }

    public CollectionMapper getMapper() {
        return mapper;
    }

    public void setMapper(CollectionMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        SimpleFeatureType type = mapper.buildFeatureType(entry.getName(), collection);
        getDataStore().getSchemaStore().storeSchema(type);
        return type;
    }

    @Override
    public MongoDataStore getDataStore() {
        return (MongoDataStore) super.getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        // TODO: crs?
        FeatureReader<SimpleFeatureType, SimpleFeature> r = getReader(query);
        try {
            ReferencedEnvelope e = new ReferencedEnvelope();
            if (r.hasNext()) {
                e.init(r.next().getBounds());
            }
            while (r.hasNext()) {
                e.include(r.next().getBounds());
            }
            return e;
        } finally {
            r.close();
        }
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        Filter f = query.getFilter();
        if (isAll(f)) {
            LOG.fine("count(all)");
            return (int) collection.count();
        }

        Filter[] split = splitFilter(f);
        if (!isAll(split[1])) {
            return -1;
        }

        DBObject q = toQuery(f);
        if (LOG.isLoggable(Level.FINE)) {
            LOG.fine("count(" + q + ")");
        }
        return (int) collection.count(q);
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {

        List<Filter> postFilter = new ArrayList<Filter>();
        DBCursor cursor = toCursor(query, postFilter);
        SimpleFeatureReader r = new MongoFeatureReader(cursor, this);

        if (!postFilter.isEmpty()) {
            r = new FilteringSimpleFeatureReader(r, postFilter.get(0));
        }
        return r;
    }

    @Override
    protected boolean canOffset() {
        return true;
    }

    @Override
    protected boolean canLimit() {
        return true;
    }

    @Override
    protected boolean canRetype() {
        return true;
    }

    @Override
    protected boolean canSort() {
        return true;
    }

    @Override
    protected boolean canFilter() {
        return true;
    }

    DBCursor toCursor(Query q, List<Filter> postFilter) {
        DBObject query = new BasicDBObject();

        Filter f = q.getFilter();
        if (!isAll(f)) {
            Filter[] split = splitFilter(f);
            query = toQuery(split[0]);
            if (!isAll(split[1])) {
                postFilter.add(split[1]);
            }
        }

        DBCursor c;
        if (q.getPropertyNames() != Query.ALL_NAMES) {
            BasicDBObject keys = new BasicDBObject();
            for (String p : q.getPropertyNames()) {
                keys.put(mapper.getPropertyPath(p), 1);
            }
            if (!keys.containsField(mapper.getGeometryPath())) {
                keys.put(mapper.getGeometryPath(), 1);
            }
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine(String.format("find(%s, %s)", query, keys));
            }
            c = collection.find(query, keys);
        } else {
            if (LOG.isLoggable(Level.FINE)) {
                LOG.fine(String.format("find(%s)", query));
            }
            c = collection.find(query);
        }

        if (q.getStartIndex() != null && q.getStartIndex() != 0) {
            c = c.skip(q.getStartIndex());
        }
        if (q.getMaxFeatures() != Integer.MAX_VALUE) {
            c = c.limit(q.getMaxFeatures());
        }

        if (q.getSortBy() != null) {
            BasicDBObject orderBy = new BasicDBObject();
            for (SortBy sortBy : q.getSortBy()) {
                String propName = sortBy.getPropertyName().getPropertyName();
                orderBy.append(propName, sortBy.getSortOrder() == SortOrder.ASCENDING ? 1 : -1);
            }
            c = c.sort(orderBy);
        }

        return c;
    }

    DBObject toQuery(Filter f) {
        if (isAll(f)) {
            return new BasicDBObject();
        }

        return (DBObject) f.accept(new FilterToMongo(mapper), null);
    }

    boolean isAll(Filter f) {
        return f == null || f == Filter.INCLUDE;
    }

    @SuppressWarnings("deprecation")
    Filter[] splitFilter(Filter f) {
        PostPreProcessFilterSplittingVisitor splitter = new PostPreProcessFilterSplittingVisitor(
                getDataStore().getFilterCapabilities(), null, null);
        f.accept(splitter, null);
        return new Filter[] { splitter.getFilterPre(), splitter.getFilterPost() };
    }

}
