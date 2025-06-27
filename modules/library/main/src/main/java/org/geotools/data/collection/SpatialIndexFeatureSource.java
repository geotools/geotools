/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.collection;

import java.awt.RenderingHints;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.FeatureListener;
import org.geotools.api.data.Query;
import org.geotools.api.data.QueryCapabilities;
import org.geotools.api.data.ResourceInfo;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.filter.expression.PropertyName;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.spatial.BinarySpatialOperator;
import org.geotools.api.filter.spatial.Contains;
import org.geotools.api.filter.spatial.Crosses;
import org.geotools.api.filter.spatial.DWithin;
import org.geotools.api.filter.spatial.Equals;
import org.geotools.api.filter.spatial.Intersects;
import org.geotools.api.filter.spatial.Overlaps;
import org.geotools.api.filter.spatial.Touches;
import org.geotools.api.filter.spatial.Within;
import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.data.store.ReTypingFeatureCollection;
import org.geotools.data.store.ReprojectingFeatureCollection;
import org.geotools.feature.collection.MaxSimpleFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/**
 * A FeatureSource using a spatial index to hold on to features and serve them up for fast display.
 *
 * <p>This is a port of Andrea's CachingFeatureSource (which is slightly more compliced and rebuilds the cache as an
 * origional feature source changes). Our implementation here knows up front that the features are in memory and does
 * its best to take advantage of the fact. A caching feature source for fast data access.
 *
 * <p>Please note that this FeatureSource is strictly "read-only" and thus does not support feature events.
 */
public class SpatialIndexFeatureSource implements SimpleFeatureSource {
    SpatialIndexFeatureCollection contents;

    private static final Set<Class> supportedFilterTypes = new HashSet<>(Arrays.asList(
            BBOX.class,
            Contains.class,
            Crosses.class,
            DWithin.class,
            Equals.class,
            Intersects.class,
            Overlaps.class,
            Touches.class,
            Within.class));

    public SpatialIndexFeatureSource(SpatialIndexFeatureCollection original) {
        this.contents = original;
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {}

    @Override
    public void removeFeatureListener(FeatureListener listener) {}

    @Override
    public DataStore getDataStore() {
        return null; // not applicable
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return contents.getBounds();
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return getFeatures(query).getBounds();
    }

    @Override
    public int getCount(Query query) throws IOException {
        return getFeatures(query).size();
    }

    @Override
    public SimpleFeatureType getSchema() {
        return contents.getSchema();
    }

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return contents;
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        Query query = new Query(getSchema().getName().getLocalPart(), filter);
        return getFeatures(query);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        Envelope bounds = getEnvelope(query.getFilter());
        return getFeatureCollection(query, bounds);
    }

    @SuppressWarnings("UnusedVariable") // revisit, parameter bounds is not used
    private SimpleFeatureCollection getFeatureCollection(Query query, Envelope bounds) throws IOException {
        query = DataUtilities.resolvePropertyNames(query, getSchema());
        final int offset = query.getStartIndex() != null ? query.getStartIndex() : 0;
        if (offset > 0 && query.getSortBy() == null) {
            if (!getQueryCapabilities().supportsSorting(query.getSortBy())) {
                throw new IllegalStateException("Feature source does not support this sorting "
                        + "so there is no way a stable paging (offset/limit) can be performed");
            }
            Query copy = new Query(query);
            copy.setSortBy(SortBy.NATURAL_ORDER);
            query = copy;
        }
        SimpleFeatureCollection collection;
        // step one filter
        if (query.getFilter() != null && query.getFilter().equals(Filter.EXCLUDE)) {
            return new EmptyFeatureCollection(getSchema());
        }
        if (query.getFilter() != null && query.getFilter().equals(Filter.INCLUDE)) {
            collection = contents;
        }
        if (query.getFilter() != null && query.getFilter().equals(Filter.INCLUDE)) {
            collection = contents;
        } else {
            collection = contents.subCollection(query.getFilter());
        }
        // step two: reproject
        if (query.getCoordinateSystemReproject() != null) {
            collection = new ReprojectingFeatureCollection(collection, query.getCoordinateSystemReproject());
        }
        // step two sort! (note this makes a sorted copy)
        if (query.getSortBy() != null && query.getSortBy().length != 0) {
            SimpleFeature[] array = collection.toArray(new SimpleFeature[collection.size()]);
            // Arrays sort is stable (not resorting equal elements)
            for (SortBy sortBy : query.getSortBy()) {
                Comparator<SimpleFeature> comparator = DataUtilities.sortComparator(sortBy);
                Arrays.sort(array, comparator);
            }
            ArrayList<SimpleFeature> list = new ArrayList<>(Arrays.asList(array));
            collection = new ListFeatureCollection(getSchema(), list);
        }

        // step three skip to start and return max number of fetaures
        if (offset > 0 || !query.isMaxFeaturesUnlimited()) {
            long max = Long.MAX_VALUE;
            if (!query.isMaxFeaturesUnlimited()) {
                max = query.getMaxFeatures();
            }
            collection = new MaxSimpleFeatureCollection(collection, offset, max);
        }
        // step four - retyping
        // (It would be nice to do this earlier so as to not have all the baggage
        // of unneeded attributes)
        if (query.getPropertyNames() != Query.ALL_NAMES) {
            // rebuild the type and wrap the reader
            SimpleFeatureType schema = collection.getSchema();
            SimpleFeatureType target = SimpleFeatureTypeBuilder.retype(schema, query.getPropertyNames());
            if (!target.equals(schema)) {
                collection = new ReTypingFeatureCollection(collection, target);
            }
        }
        return collection;
    }

    Envelope getEnvelope(Filter filter) {
        Envelope result = new Envelope();
        if (filter instanceof And) {
            Envelope bounds = new Envelope();
            for (Filter f : ((And) filter).getChildren()) {
                Envelope e = getEnvelope(f);
                if (e == null) {
                    return null;
                } else {
                    bounds.expandToInclude(e);
                }
            }
            result = bounds;
        } else if (filter instanceof BinarySpatialOperator) {
            BinarySpatialOperator gf = (BinarySpatialOperator) filter;
            if (supportedFilterTypes.contains(gf.getClass())) {
                Expression lg = gf.getExpression1();
                Expression rg = gf.getExpression2();
                if (lg instanceof Literal) {
                    Geometry g = (Geometry) ((Literal) lg).getValue();
                    if (rg instanceof PropertyName) {
                        result = g.getEnvelopeInternal();
                    }
                } else if (rg instanceof Literal) {
                    Geometry g = (Geometry) ((Literal) rg).getValue();
                    if (lg instanceof PropertyName) {
                        result = g.getEnvelopeInternal();
                    }
                }
            }
        }
        return result;
    }

    @Override
    public ResourceInfo getInfo() {
        return null;
    }

    @Override
    public Name getName() {
        return contents.getSchema().getName();
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        return new QueryCapabilities() {
            @Override
            public boolean isOffsetSupported() {
                return true;
            }
        };
    }

    @Override
    public Set<RenderingHints.Key> getSupportedHints() {
        return new HashSet<>();
    }
}
