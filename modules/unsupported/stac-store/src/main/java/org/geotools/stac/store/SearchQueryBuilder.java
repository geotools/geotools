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

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.tuple.Pair;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.And;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.sort.SortBy;
import org.geotools.api.filter.sort.SortOrder;
import org.geotools.api.filter.spatial.BBOX;
import org.geotools.api.filter.temporal.BinaryTemporalOperator;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.stac.client.CQL2Conformance;
import org.geotools.stac.client.FilterLang;
import org.geotools.stac.client.STACClient;
import org.geotools.stac.client.STACConformance;
import org.geotools.stac.client.SearchQuery;
import org.geotools.util.DateRange;
import org.locationtech.jts.geom.Envelope;

/** Builds a {@link org.geotools.stac.client.SearchQuery} from a GeoTools Query */
class SearchQueryBuilder {

    static final FilterFactory FF = CommonFactoryFinder.getFilterFactory();

    /**
     * Used to cut eventual "out of world" coordinates that the remote server could not appreciate
     */
    private static final ReferencedEnvelope WORLD =
            new ReferencedEnvelope(-180, 180, -90, 90, WGS84);

    private static final String TYPE = "type";
    private static final String ID = "id";
    private static final String BBOX = "bbox";
    private static final String GEOMETRY = "geometry";

    private final SimpleFeatureType schema;
    private final STACDataStore store;

    public SearchQueryBuilder(SimpleFeatureType schema, STACDataStore store) {
        this.schema = schema;
        this.store = store;
    }

    /**
     * Maps the GeoTools {@link Query} to a {@link SearchQuery}.
     *
     * @param q The GeoTools query the SearchQuery
     * @return A pair with a SearchQuery and a post filter, or null if strict is true and part of
     *     the query could not be mapped
     */
    public Pair<SearchQuery, Filter> toSearchQuery(Query q, boolean strict) {
        SearchQuery sq = new SearchQuery();
        sq.setCollections(Arrays.asList(getCollectionId(q)));

        // the limit in STAC-API is a suggestion on how many features to return to the first
        // page, not an actual limit
        sq.setLimit(Math.min(q.getMaxFeatures(), store.getFetchSize()));

        // not supported yet, but could be added
        List<String> conformance = store.getClient().getLandingPage().getConformance();
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
            try {
                if (!encodeSimpleFilter(simplified, sq)) {
                    if (!STACConformance.FILTER.matches(conformance)) return null;

                    // CQL2 works better with explicit temporal filters
                    TemporalFilterVisitor temporalReplacer = new TemporalFilterVisitor(schema);
                    Filter adapted = (Filter) simplified.accept(temporalReplacer, null);

                    // try harder to use bbox and datetime, leaving simpler filters for CQL2
                    // as implementations out there have all sorts of weaknesses
                    Filter leftover = adapted;
                    if (adapted instanceof And) {
                        List<BBOX> bbox = new ArrayList<>();
                        List<BinaryTemporalOperator> time = new ArrayList<>();
                        List<Filter> other = new ArrayList<>();
                        for (Filter filter : ((And) adapted).getChildren()) {
                            if (filter instanceof BBOX) {
                                bbox.add((BBOX) filter);
                            } else if (filter instanceof BinaryTemporalOperator) {
                                time.add((BinaryTemporalOperator) filter);
                            } else {
                                other.add(filter);
                            }
                        }

                        // did we get at least one simple filter, but no duplicates?
                        if (time.size() <= 1
                                && bbox.size() <= 1
                                && time.size() + bbox.size() <= 2
                                && time.size() + bbox.size() > 0) {
                            if (!time.isEmpty()) encodeTimeFilter(time.get(0), sq);
                            if (!bbox.isEmpty()) encodeBBOX(bbox.get(0), sq);
                            if (other.isEmpty()) {
                                leftover = Filter.EXCLUDE;
                            } else if (other.size() == 1) {
                                leftover = other.get(0);
                            } else {
                                leftover = FF.and(other);
                            }
                        }
                    }

                    if (!Filter.EXCLUDE.equals(leftover)) {
                        CQL2PostPreFilterSplitter splitter =
                                new CQL2PostPreFilterSplitter(conformance);
                        leftover.accept(splitter, null);
                        Filter pre = splitter.getFilterPre();
                        post = splitter.getFilterPost();

                        if (strict && !Filter.INCLUDE.equals(post)) return null;
                        if (setupFilterLanguage(sq, conformance)) {
                            sq.setFilter(pre);
                        }
                    }
                }
            } catch (FactoryException | TransformException e) {
                throw new RuntimeException("Failed to setup the " + BBOX + " filter", e);
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
                                .map(n -> propertyToField(n))
                                .map(n -> n.replace('/', '.'))
                                .collect(Collectors.toList());
                fields.add(TYPE); // some servers would even remove this field if not included!
                fields.add(ID); // and this as well
                fields.add("-" + BBOX); // no need for the top level bbox
                if (nameList.stream().noneMatch(n -> n.startsWith("properties"))) {
                    fields.add("-properties");
                }
                if (fields.stream().noneMatch(n -> n.equals(STACFeatureSource.ASSETS))) {
                    // assets is large, remove if not requested. There migth be properties
                    // inside assets, it's better to say "assets.my.properties,-assets" to
                    // ensure the other assets properties are not returned
                    fields.add("-assets");
                }
                fields.add("-links"); // some servers like to return the links no matter what

                sq.setFields(fields);

            } else if (strict) return null;
        }

        return Pair.of(sq, post);
    }

    /**
     * Most properties are found under "properties" but a few are top level. They need different
     * paths in the JSON structure.
     */
    private static String propertyToField(String n) {
        return n.equals(GEOMETRY)
                        || n.equals(STACFeatureSource.ASSETS)
                        || n.startsWith(STACFeatureSource.ASSETS + "/")
                ? n
                : "properties." + n;
    }

    private String getCollectionId(Query query) {
        if (query != null && query.getTypeName() != null) return query.getTypeName();
        return schema.getTypeName();
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
            if (store.getSearchMode().equals(STACClient.SearchMode.GET))
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

    private boolean encodeSimpleFilter(Filter filter, SearchQuery sq)
            throws FactoryException, TransformException {

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
}
