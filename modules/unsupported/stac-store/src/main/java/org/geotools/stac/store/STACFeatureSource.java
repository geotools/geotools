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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.MaxFeatureReader;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ReTypeFeatureReader;
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
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.stac.client.CQL2Conformance;
import org.geotools.stac.client.FilterLang;
import org.geotools.stac.client.STACClient;
import org.geotools.stac.client.STACConformance;
import org.geotools.stac.client.SearchQuery;
import org.geotools.util.DateRange;
import org.geotools.util.factory.Hints;
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
    protected void addHints(Set<Hints.Key> hints) {
        hints.add(Hints.FEATURE_DETACHED);
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
        Pair<SearchQuery, Filter> pair = getSearchQuery(query, true);
        // if the query cannot be fully translated, we cannot do an efficient count
        if (pair == null) return -1;

        Integer fc = getCountFromClient(pair.getKey());
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
        SearchQuery sq = getSearchQuery(Query.ALL, false).getKey();

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
     * @return A pair with a SearchQuery and a post filter, or null if strict is true and part of
     *     the query could not be mapped
     */
    private Pair<SearchQuery, Filter> getSearchQuery(Query q, boolean strict) {
        SearchQuery sq = new SearchQuery();
        sq.setCollections(Arrays.asList(getCollectionId(q)));

        // the limit in STAC-API is a suggestion on how many features to return to the first
        // page, not an actual limit
        sq.setLimit(Math.min(q.getMaxFeatures(), getDataStore().getFetchSize()));

        // not supported yet, but could be added
        List<String> conformance = client.getLandingPage().getConformance();
        if (q.getSortBy() != null && q.getSortBy() != SortBy.UNSORTED) {
            if (STACConformance.SORT.matches(conformance)) {
                List<org.geotools.stac.client.SortBy> sorts =
                        Arrays.stream(q.getSortBy())
                                .map(sb -> toSTACSort(sb))
                                .collect(Collectors.toList());
                sq.setSortBy(sorts);
            }
        }

        // no plans to support this
        if (q.getJoins() != null && !q.getJoins().isEmpty() && strict) return null;

        Filter post = Filter.INCLUDE;
        if (q.getFilter() != null && q.getFilter() != Filter.INCLUDE) {
            Filter simplified = SimplifyingFilterVisitor.simplify(q.getFilter());

            // see if we can use the simple space/time filtering, should be better implemented
            // across the board
            if (!encodeSimpleFilter(simplified, sq)) {
                if (!STACConformance.FILTER.matches(conformance)) return null;

                CQL2PostPreFilterSplitter splitter = new CQL2PostPreFilterSplitter(conformance);
                simplified.accept(splitter, null);
                Filter pre = splitter.getFilterPre();
                post = splitter.getFilterPost();

                if (strict && !Filter.INCLUDE.equals(post)) return null;
                if (setupFilterLanguage(sq, conformance)) {
                    sq.setFilter(pre);
                }
            }
        }

        // if the server supports fields selection, use it
        String[] propertyNames = q.getPropertyNames();
        if (propertyNames != null && !q.getProperties().isEmpty()) {
            if (STACConformance.FIELDS.matches(conformance)) {
                Set<String> nameList = new LinkedHashSet<>(Arrays.asList(propertyNames));
                if (post != null && !Filter.INCLUDE.equals(post)) {
                    FilterAttributeExtractor fex = new FilterAttributeExtractor();
                    post.accept(fex, null);
                    nameList.addAll(fex.getAttributeNameSet());
                }
                List<String> fields =
                        nameList.stream()
                                .map(n -> n.equals("geometry") ? n : "properties." + n)
                                .collect(Collectors.toList());
                fields.add("type"); // some servers would even remove this field if not included!
                fields.add("id"); // and this as well
                fields.add(
                        "-links"); // some servers like to return the links no matter what instead
                sq.setFields(fields);

            } else if (strict) return null;
        }

        return Pair.of(sq, post);
    }

    /**
     * Sets up a filter encoding language that's supported on both ends. If both text and JSON are
     * supported, choose based on the type of request, GET vsPOST.
     *
     * @param sq
     * @param conformance
     * @return True if a supported language was set up, otherwise not
     */
    private boolean setupFilterLanguage(SearchQuery sq, List<String> conformance) {
        boolean text = CQL2Conformance.TEXT.matches(conformance);
        boolean json = CQL2Conformance.JSON.matches(conformance);

        if (text && json) {
            if (getDataStore().getSearchMode().equals(STACClient.SearchMode.GET))
                sq.setFilterLang(FilterLang.CQL2_TEXT);
            else sq.setFilterLang(FilterLang.CQL2_JSON);
        } else if (text) {
            sq.setFilterLang(FilterLang.CQL2_TEXT);
        } else if (json) {
            sq.setFilterLang(FilterLang.CQL2_JSON);
        }

        return sq.getFilterLang() != null;
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
        Pair<SearchQuery, Filter> pair = getSearchQuery(query, false);
        SearchQuery sq;
        Filter postFilter;
        if (pair == null) {
            sq = getSearchQuery(Query.ALL, false).getKey();
            postFilter = query.getFilter();
        } else {
            sq = pair.getKey();
            postFilter = pair.getValue();
        }

        SimpleFeatureCollection fc = client.search(sq, getDataStore().getSearchMode(), getSchema());
        SimpleFeatureReader result = new CollectionReader(fc);

        // the hard limit must be applied before filtering, or we'll fetch more features
        int hardLimit = getDataStore().getHardLimit();
        if (hardLimit > 0) {
            result = DataUtilities.simple(new MaxFeatureReader<>(result, hardLimit));
        }

        // handle post filtering, if the full filter could not be encoded
        if (!Filter.INCLUDE.equals(postFilter))
            result = new FilteringSimpleFeatureReader(result, query.getFilter());

        // handle property selection, if it was not fully encoded in the query, or a post
        // filter required more properties than strictly necessary.
        if (query.getProperties() != null
                && !query.getProperties().isEmpty()
                && (sq.getFields() == null || !Filter.INCLUDE.equals(postFilter))) {
            SimpleFeatureType targetType =
                    SimpleFeatureTypeBuilder.retype(
                            getSchema(), Arrays.asList(query.getPropertyNames()));
            result = DataUtilities.simple(new ReTypeFeatureReader(result, targetType, false));
        }

        // handle max feature from the query
        int max = query.getMaxFeatures();
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
