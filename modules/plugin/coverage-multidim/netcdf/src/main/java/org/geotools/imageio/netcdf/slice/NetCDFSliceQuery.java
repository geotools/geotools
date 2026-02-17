/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.imageio.netcdf.slice;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.geotools.api.data.Query;
import org.geotools.api.filter.Filter;
import org.geotools.api.filter.IncludeFilter;
import org.geotools.filter.visitor.SimplifyingFilterVisitor;
import org.geotools.imageio.netcdf.VariableAdapter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter;
import org.geotools.imageio.netcdf.slice.filter.SplitFilterResult;

/**
 * Represents a preprocessed query over NetCDF slices, separating dimension-backed filters from the residual
 * post-filter.
 *
 * <p>The recognized constraints are stored as {@link DimensionFilter} instances for time, elevation and additional
 * dimensions. They can later be resolved to concrete axis indices before iterating the matching slice tuples.
 */
record NetCDFSliceQuery(
        DimensionFilter time, DimensionFilter elevation, Map<Integer, DimensionFilter> additional, Filter postFilter) {

    /** Builds a slice query with resolved dimension filters and optional post-filter. */
    NetCDFSliceQuery(
            DimensionFilter time,
            DimensionFilter elevation,
            Map<Integer, DimensionFilter> additional,
            Filter postFilter) {
        this.time = time == null ? DimensionFilter.ALL : time;
        this.elevation = elevation == null ? DimensionFilter.ALL : elevation;
        this.additional = additional == null ? Map.of() : Map.copyOf(additional);
        this.postFilter = postFilter == null ? Filter.INCLUDE : postFilter;
    }

    /**
     * Preprocesses the original query by simplifying the GeoTools filter and splitting recognized dimension predicates
     * from the residual post-filter.
     */
    static NetCDFSliceQuery fromQuery(
            Query query,
            VariableAdapter adapter,
            String timeAttribute,
            String elevationAttribute,
            List<NetCDFSliceProvider.AdditionalDomainBinding> additionalBindings) {

        Filter raw = query != null && query.getFilter() != null ? query.getFilter() : Filter.INCLUDE;
        Filter simplified = SimplifyingFilterVisitor.simplify(raw);

        SplitFilterResult split =
                splitFilter(simplified, adapter, timeAttribute, elevationAttribute, additionalBindings);

        return new NetCDFSliceQuery(
                split.time(), split.elevation(), toAdditionalMap(split.additional()), split.postFilter());
    }

    /** Returns true when a residual in-memory post-filter must still be evaluated. */
    boolean hasPostFilter() {
        return postFilter != null && postFilter != Filter.INCLUDE && !(postFilter instanceof IncludeFilter);
    }

    /**
     * Recognizes filters on time, elevation and additional dimensions, and splits them from the residual post-filter.
     */
    private static SplitFilterResult splitFilter(
            Filter filter,
            VariableAdapter adapter,
            String timeAttribute,
            String elevationAttribute,
            List<NetCDFSliceProvider.AdditionalDomainBinding> additionalBindings) {

        DimensionBindingResolver resolver =
                new NetCDFDimensionBindingResolver(adapter, timeAttribute, elevationAttribute, additionalBindings);
        DimensionFilterSplitter splitter = new DimensionFilterSplitter(resolver);
        return splitter.split(filter);
    }

    private static Map<Integer, DimensionFilter> toAdditionalMap(
            List<DimensionFilter.AdditionalDimensionFilter> filters) {
        if (filters == null || filters.isEmpty()) {
            return Map.of();
        }

        Map<Integer, DimensionFilter> map = new LinkedHashMap<>();
        for (DimensionFilter.AdditionalDimensionFilter filter : filters) {
            map.put(filter.dimensionIndex(), filter.filter());
        }
        return map;
    }
}
