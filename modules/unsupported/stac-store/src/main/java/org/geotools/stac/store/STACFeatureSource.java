/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.stac.store;

import static org.geotools.referencing.crs.DefaultGeographicCRS.WGS84;

import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.MaxFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.geojson.PagingFeatureCollection;
import org.geotools.data.simple.FilteringSimpleFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.feature.visitor.MaxVisitor;
import org.geotools.feature.visitor.MinVisitor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.stac.client.STACClient;
import org.geotools.stac.client.STACConformance;
import org.geotools.stac.client.SearchQuery;
import org.geotools.util.DateRange;
import org.locationtech.jts.geom.Envelope;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.And;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.opengis.filter.spatial.BBOX;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.TransformException;

public class STACFeatureSource extends ContentFeatureSource {

    static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2();

    /**
     * Used to cut eventual "out of world" coordinates that the remote server could not appreciate
     */
    private static final ReferencedEnvelope WORLD =
            new ReferencedEnvelope(-180, 180, -90, 90, WGS84);

    private final STACClient client;

    public STACFeatureSource(ContentEntry entry, STACClient client) {
        super(entry, Query.ALL);
        this.client = client;
    }

    @Override
    public STACDataStore getDataStore() {
        return (STACDataStore) super.getDataStore();
    }

    @Override
    protected QueryCapabilities buildQueryCapabilities() {
        return new QueryCapabilities() {

            @Override
            public boolean isOffsetSupported() {
                return false;
            }

            @Override
            public boolean supportsSorting(SortBy... sortAttributes) {
                // we really need to check the capabilities here
                return false;
            }
        };
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        // the STAC API does not offer a fast way to retrieve bounds based on a query,
        // it only reports the bounds of a full collection
        if (!query.equals(Query.ALL)) return null;

        String typeName = getCollectionId(query);
        // maybe we should we have a way to retrieve a single collection
        return client.getCollections().stream()
                .filter(c -> typeName.equals(c.getId()))
                .map(c -> c.getBounds())
                .findFirst()
                .orElse(null);
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        SearchQuery searchQuery = getSearchQuery(query, true);
        // if the query cannot be fully translated, we cannot do an efficient count
        if (searchQuery == null) return -1;

        Integer fc = getCountFromClient(searchQuery);
        if (fc != null) {
            int hardLimit = getDataStore().getHardLimit();
            if (hardLimit > 0) return Math.min(hardLimit, fc);
            else return fc;
        }

        // no fast way to count, give up, most catalogs have too many features
        return -1;
    }

    private Integer getCountFromClient(SearchQuery searchQuery) throws IOException {
        // try to get the "matched" header from the GeoJSON collection, if available
        SimpleFeatureCollection fc = client.search(searchQuery, getDataStore().getSearchMode());
        // if there was a "next" link, the reader returns a paging collection
        if (fc instanceof PagingFeatureCollection) {
            return Optional.ofNullable(((PagingFeatureCollection) fc).getMatched()).orElse(-1);
        }
        // otherwise everything is in memory
        if (fc instanceof ListFeatureCollection) {
            return fc.size();
        }
        return null;
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        SearchQuery sq = getSearchQuery(Query.ALL, false);
        sq.setLimit(getDataStore().getFetchSize());
        SimpleFeatureCollection fc = client.search(sq, getDataStore().getSearchMode());
        SimpleFeatureType rawSchema = fc.getSchema();

        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(rawSchema);
        tb.setName(entry.getTypeName());
        return tb.buildFeatureType();
    }

    /**
     * Maps the GeoTools {@link Query} to a {@link SearchQuery}.
     *
     * @param q The GeoTools query
     * @param strict If strict is true, will return null if the query cannot be fully mapped into
     *     the SearchQuery
     * @return
     */
    private SearchQuery getSearchQuery(Query q, boolean strict) {
        SearchQuery sq = new SearchQuery();
        sq.setCollections(Arrays.asList(getCollectionId(q)));

        // the limit in STAC-API is a suggestion on how many features to return to the first
        // page, not an actual limit
        sq.setLimit(Math.min(q.getMaxFeatures(), getDataStore().getFetchSize()));

        // if the server supports fields selection, use it
        if (q.getPropertyNames() != null && !q.getProperties().isEmpty()) {
            if (STACConformance.FIELDS.matches(client.getLandingPage().getConformance())) {
                List<String> fields =
                        Arrays.stream(q.getPropertyNames())
                                .map(n -> n.equals("geometry") ? n : "properties." + n)
                                .collect(Collectors.toList());
                fields.add("type"); // some servers would even remove this field if not included!
                fields.add("id"); // and this as well
                fields.add(
                        "-links"); // some servers like to return the links no matter what instead
                sq.setFields(fields);

            } else if (strict) return null;
        }

        // not supported yet, but could be added
        if (q.getSortBy() != null && q.getSortBy() != SortBy.UNSORTED) {
            if (STACConformance.SORT.matches(client.getLandingPage().getConformance())) {
                List<org.geotools.stac.client.SortBy> sorts =
                        Arrays.stream(q.getSortBy())
                                .map(sb -> toSTACSort(sb))
                                .collect(Collectors.toList());
                sq.setSortBy(sorts);
            }
        }

        // no plans to support this
        if (q.getJoins() != null && !q.getJoins().isEmpty() && strict) return null;

        if (q.getFilter() != null && q.getFilter() != Filter.INCLUDE) {
            Filter simplified = SimplifyingFilterVisitor.simplify(q.getFilter());

            // see if we can use the simple space/time filtering, should be better implemented
            // across the board
            if (!encodeSimpleFilter(simplified, sq)) {
                // we might encode in CQL, but need to turn the CQL conformance classes into filter
                // capabilities --> for later
                if (strict) return null;
            }
        }

        return sq;
    }

    private org.geotools.stac.client.SortBy toSTACSort(SortBy sort) {
        return new org.geotools.stac.client.SortBy(
                sort.getPropertyName().getPropertyName(),
                sort.getSortOrder() == SortOrder.DESCENDING
                        ? org.geotools.stac.client.SortBy.Direction.desc
                        : org.geotools.stac.client.SortBy.Direction.asc);
    }

    private boolean encodeSimpleFilter(Filter filter, SearchQuery sq) {
        try {
            if (filter instanceof BBOX) {
                encodeBBOX((BBOX) filter, sq);
                return true;
            } else if (isTimeFilter(filter)) {
                encodeTimeFilter(filter, sq);
                return true;
            } else if (filter instanceof And) {
                And and = (And) filter;
                List<Filter> children = and.getChildren();
                if (children.size() != 2) return false;
                if (children.get(0) instanceof BBOX && isTimeFilter(children.get(1))) {
                    encodeBBOX((BBOX) children.get(0), sq);
                    encodeTimeFilter(children.get(1), sq);
                    return true;
                } else if (children.get(1) instanceof BBOX && isTimeFilter(children.get(0))) {
                    encodeBBOX((BBOX) children.get(1), sq);
                    encodeTimeFilter(children.get(0), sq);
                    return true;
                }
            }
        } catch (FactoryException | TransformException e) {
            throw new RuntimeException("Failed to setup the bbox filter", e);
        }

        return false;
    }

    private void encodeTimeFilter(Filter filter, SearchQuery sq) {
        TimeRangeVisitor visitor = new TimeRangeVisitor();
        DateRange range = (DateRange) filter.accept(visitor, null);
        if (TimeRangeVisitor.DATE_NEGATIVE_INFINITE.equals(range.getMinValue())) {
            // should not get here, but just in case, don't encode anything
            if (TimeRangeVisitor.DATE_POSITIVE_INFINITE.equals(range.getMaxValue())) return;
            else sq.setDatetime("../" + toISO(range.getMaxValue()));
        } else if (TimeRangeVisitor.DATE_POSITIVE_INFINITE.equals(range.getMaxValue())) {
            sq.setDatetime(toISO(range.getMinValue()) + "/..");
        } else if (range.getMinValue().equals(range.getMaxValue())) {
            // some servers need nanosecond precision for match, use the smallest range available
            long next = range.getMinValue().getTime() + 1;
            sq.setDatetime(toISO(range.getMinValue()) + "/" + toISO(new Date(next)));
        } else {
            sq.setDatetime(toISO(range.getMinValue()) + "/" + toISO(range.getMaxValue()));
        }
    }

    private String toISO(Date value) {
        return DateTimeFormatter.ISO_INSTANT.format(value.toInstant());
    }

    private boolean isTimeFilter(Filter filter) {
        TimeRangeVisitor visitor = new TimeRangeVisitor();
        DateRange range = (DateRange) filter.accept(visitor, null);
        return range != null
                && !range.isEmpty()
                && !TimeRangeVisitor.INFINITY.equals(range)
                && visitor.isExact();
    }

    private void encodeBBOX(BBOX filter, SearchQuery sq)
            throws TransformException, FactoryException {
        Envelope box = ReferencedEnvelope.reference(filter.getBounds()).transform(WGS84, true);
        box = box.intersection(WORLD);
        sq.setBbox(new double[] {box.getMinX(), box.getMinY(), box.getMaxX(), box.getMaxY()});
    }

    private String getCollectionId(Query query) {
        return Optional.ofNullable(query).map(q -> q.getTypeName()).orElse(entry.getTypeName());
    }

    @Override
    protected boolean canFilter() {
        return true;
    }

    @Override
    protected boolean canSort() {
        return true;
    }

    @Override
    protected boolean canRetype() {
        return true;
    }

    @Override
    protected boolean canLimit() {
        return true;
    }

    @Override
    @SuppressWarnings("PMD.CloseResource")
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        SearchQuery sq = getSearchQuery(query, true);
        boolean postFilter = false;
        if (sq == null) {
            postFilter = true;
            Query full = new Query(query);
            full.setPropertyNames((String[]) null);
            sq = getSearchQuery(full, false);
        }

        SimpleFeatureCollection fc = client.search(sq, getDataStore().getSearchMode());
        SimpleFeatureReader result = new CollectionReader(fc);

        if (postFilter) result = new FilteringSimpleFeatureReader(result, query.getFilter());

        // handle max feature and hard limit
        int max = Math.min(query.getMaxFeatures(), getDataStore().getHardLimit());
        if (max < Query.DEFAULT_MAX)
            result = DataUtilities.simple(new MaxFeatureReader<>(result, max));

        return result;
    }

    @Override
    protected boolean handleVisitor(Query query, FeatureVisitor visitor) throws IOException {
        if (visitor instanceof MinVisitor
                && ((MinVisitor) visitor).getExpression() instanceof PropertyName) {
            String pn = ((PropertyName) ((MinVisitor) visitor).getExpression()).getPropertyName();
            ((MinVisitor) visitor).setValue(getExtremeValue(query, pn, SortOrder.ASCENDING));
            return true;
        } else if (visitor instanceof MaxVisitor
                && ((MaxVisitor) visitor).getExpression() instanceof PropertyName) {
            String pn = ((PropertyName) ((MaxVisitor) visitor).getExpression()).getPropertyName();
            ((MaxVisitor) visitor).setValue(getExtremeValue(query, pn, SortOrder.DESCENDING));
            return true;
        }

        return super.handleVisitor(query, visitor);
    }

    private Object getExtremeValue(Query query, String pn, SortOrder direction) throws IOException {
        Query q = new Query(query);
        q.setPropertyNames(pn);
        q.setSortBy(FF.sort(pn, direction));
        q.setMaxFeatures(1);
        ContentFeatureCollection fc = getFeatures(q);
        SimpleFeature feature = DataUtilities.first(fc);
        if (feature == null) return null;
        return feature.getAttribute(pn);
    }
}
