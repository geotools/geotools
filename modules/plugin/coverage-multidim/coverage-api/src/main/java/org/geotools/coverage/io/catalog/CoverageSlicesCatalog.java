/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.catalog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.logging.Logger;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.geometry.BoundingBox;
import org.geotools.data.DataUtilities;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Before GT 35.x version this class relied on an underlying {@code DataStore} (H2/PostGIS/...) to persist and query
 * slice metadata (time/elevation/other dimensions) and the corresponding {@code imageIndex}.
 *
 * <p>Current implementation delegates query planning and slice iteration to a {@link SliceProvider}. That can compute
 * matches on-the-fly (e.g. using a deterministic Var/T/Z -> imageIndex mapping).
 */
public class CoverageSlicesCatalog {

    /** Logger. */
    static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(CoverageSlicesCatalog.class);

    public static final String IMAGE_INDEX_ATTR = "imageindex";

    private final Map<String, CoverageSlicesContext> contextsByTypeName = new LinkedHashMap<>();

    public CoverageSlicesCatalog() {}

    public String[] getTypeNames() {
        return contextsByTypeName.keySet().toArray(new String[0]);
    }

    /**
     * Resolver that can translate GeoTools {@link Query} objects into a stream of matching slices.
     *
     * <p>Implementations are expected to:
     *
     * <ul>
     *   <li>Recognize TIME/ELEVATION (and other dimension) constraints in {@code Query#getFilter()}
     *   <li>Compute the matching tuples and their {@code imageIndex} without materializing all slices
     *   <li>Build {@link CoverageSlice} instances (or at least their originator {@link SimpleFeature}) lazily
     * </ul>
     *
     * It matches the FeatureSource behavior by returning an Iterator to iterate over the CoverageSlices resulting from
     * a specific Query.
     */
    public interface SliceProvider {

        /** Return the SliceIterator object to iterate on the CoverageSlices resulting from the provided Query. */
        Iterator<CoverageSlice> iterate(Query query) throws IOException;

        /**
         * Optional fast-path for counts. Implementations may return {@code -1} to indicate that the catalog should fall
         * back to counting by iteration.
         */
        default int count(Query query) throws IOException {
            return -1;
        }

        /**
         * Optional fast-path for bounds. Implementations may return {@code null} to indicate that the catalog should
         * fall back to computing bounds by iteration.
         */
        default ReferencedEnvelope bounds(Query query) throws IOException {
            return null;
        }
    }

    /**
     * A CoverageSlices context holds the typeName and underlying schema of a coverage, plus a SliceProvider instance to
     * extract CoverageSlices from that schema via a provided query.
     */
    public static final class CoverageSlicesContext {
        private final String typeName;
        private final SimpleFeatureType schema;
        private SliceProvider sliceProvider;

        public CoverageSlicesContext(SimpleFeatureType schema) {
            this.schema = Objects.requireNonNull(schema, "schema");
            this.typeName = schema.getTypeName();
        }

        public void setSliceProvider(SliceProvider sliceProvider) {
            this.sliceProvider = sliceProvider;
        }

        public String getTypeName() {
            return typeName;
        }

        public SimpleFeatureType getSchema() {
            return schema;
        }

        public SliceProvider getSliceProvider() {
            return sliceProvider;
        }
    }

    private CoverageSlicesContext context(String typeName) {
        return contextsByTypeName.get(typeName);
    }

    /**
     * Streams matching {@link CoverageSlice}s without materializing them into a list.
     *
     * <p>This is the preferred entry point for callers that want to avoid eagerly loading all slices.
     */
    public Iterator<CoverageSlice> iterateGranules(final Query q) throws IOException {
        Query query = prepareQuery(q);
        return context(requiredTypeName(query)).getSliceProvider().iterate(query);
    }
    /**
     * Returns the number of granules matching the provided query without loading them all. Falls back to iteration if
     * the underlying store cannot compute counts.
     */
    public int getCount(final Query q) throws IOException {
        Query query = prepareQuery(q);
        SliceProvider provider = context(requiredTypeName(query)).getSliceProvider();

        int fast = provider.count(query);
        if (fast >= 0) {
            return fast;
        }

        int count = 0;
        Iterator<CoverageSlice> it = provider.iterate(query);
        while (it.hasNext()) {
            it.next();
            count++;
        }

        return count;
    }

    /**
     * Returns bounds for granules matching the provided query without loading them all. Falls back to
     * feature-collection bounds if the underlying store can't compute it directly.
     */
    public ReferencedEnvelope getBounds(final Query q) throws IOException {
        Query query = prepareQuery(q);
        SliceProvider provider = context(requiredTypeName(query)).getSliceProvider();

        ReferencedEnvelope fast = provider.bounds(query);
        if (fast != null) {
            return fast;
        }

        ReferencedEnvelope envelope = null;
        Iterator<CoverageSlice> it = provider.iterate(query);
        while (it.hasNext()) {
            CoverageSlice slice = it.next();
            SimpleFeature feature = slice.getOriginator();
            if (feature == null) {
                continue;
            }
            BoundingBox bbox = feature.getBounds();
            if (bbox == null) {
                continue;
            }
            ReferencedEnvelope bounds = new ReferencedEnvelope(bbox);
            if (envelope == null) {
                envelope = bounds;
            } else {
                envelope.expandToInclude(bounds);
            }
        }

        return envelope;
    }

    private Query prepareQuery(Query q) throws IOException {
        Query query = (q == null) ? null : new Query(q);

        if (query == null || query.getTypeName() == null || query.getTypeName().isEmpty()) {
            if (contextsByTypeName.size() == 1) {
                String onlyTypeName = contextsByTypeName.keySet().iterator().next();
                Query newQuery = new Query(onlyTypeName);
                return query != null ? DataUtilities.mixQueries(query, newQuery, query.getHandle()) : newQuery;
            }
            throw new IOException(
                    "Query typename is required for multi-typename CoverageSlicesCatalog. Known typenames: "
                            + contextsByTypeName.keySet());
        }
        return query;
    }

    public SimpleFeatureType getSchema(final String typeName) throws IOException {
        CoverageSlicesContext context = context(typeName);
        return context != null ? context.getSchema() : null;
    }

    public void computeAggregateFunction(Query query, FeatureCalc function) throws IOException {
        Iterator<CoverageSlice> it = iterateGranules(query);
        while (it.hasNext()) {
            CoverageSlice slice = it.next();
            SimpleFeature feature = slice.getOriginator();
            function.visit(feature);
        }
    }

    private String requiredTypeName(Query query) throws IOException {
        String typeName = query.getTypeName();
        if (typeName == null || typeName.isEmpty()) {
            throw new IOException("Missing typename in query");
        }
        return typeName;
    }

    public List<CoverageSlice> getGranules(final Query q) throws IOException {
        List<CoverageSlice> out = new ArrayList<>();
        Iterator<CoverageSlice> it = iterateGranules(q);
        while (it.hasNext()) {
            out.add(it.next());
        }
        return out;
    }

    public void registerContext(CoverageSlicesContext context) {
        contextsByTypeName.put(context.getTypeName(), context);
    }
}
