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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import org.geotools.api.data.Query;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.coverage.io.CoverageSource.AdditionalDomain;
import org.geotools.coverage.io.CoverageSource.DomainType;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.coverage.io.catalog.CoverageSlicesCatalog.SliceProvider;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.imageio.netcdf.VariableAdapter;
import org.geotools.imageio.netcdf.cv.CoordinateVariable;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter;

/**
 * A {@link SliceProvider} implementation that exposes the slices of a single NetCDF variable as queryable catalog
 * entries.
 *
 * <p>This class coordinates the full query-to-slice workflow. A {@link Query} is first translated into a
 * {@link NetCDFSliceQuery}, which separates recognized dimension constraints from generic post-filters. Filter
 * recognition is delegated to {@link DimensionFilterSplitter}, which in turn relies on a
 * NetCDFPreFilterDimensionExtractor to identify filters targeting time, elevation, and additional NetCDF dimensions.
 *
 * <p>The resulting {@link NetCDFSliceQuery} is converted into a domain of selected dimension indices, and a
 * {@link NetCDFSliceIterator} walks the matching tuples to build {@link org.geotools.coverage.io.catalog.CoverageSlice}
 * instances. This allows recognized dimension predicates to be evaluated up front, while any residual filter is applied
 * in memory as a post-filter during iteration.
 */
public class NetCDFSliceProvider implements SliceProvider {

    // The variable adapter to read the variable metadata and create features for the slices
    private final VariableAdapter adapter;

    // The schema of the features created for the slices
    private final SimpleFeatureType schema;

    // ImageIndex starts from 0 for the first variable in the NetCDF file,
    // so we need to know the offset of this variable within the global imageIndex sequence.
    private final int startIndex;
    private final ReferencedEnvelope bounds;
    private final String timeAttribute;
    private final String elevationAttribute;
    private final List<AdditionalDomainBinding> additionalBindings;

    private final DimensionIndexesContext dimensionsContext;

    /** Builds a slice provider for a NetCDF variable and its indexed dimension context. */
    public NetCDFSliceProvider(
            VariableAdapter adapter,
            SimpleFeatureType schema,
            int startIndex,
            ReferencedEnvelope bounds,
            DimensionIndexesContext context) {
        this.adapter = Objects.requireNonNull(adapter, "adapter");
        this.schema = Objects.requireNonNull(schema, "schema");
        this.startIndex = startIndex;
        this.bounds = bounds;
        this.timeAttribute = adapter.getTimeAttributeName();
        this.elevationAttribute = adapter.getElevationAttributeName();
        this.additionalBindings = initAdditionalBindings(adapter);
        this.dimensionsContext = context;
    }

    /** Returns an iterator over slices matching the provided query. */
    @Override
    public Iterator<CoverageSlice> iterate(Query query) {
        final NetCDFSliceQuery slicesQuery =
                NetCDFSliceQuery.fromQuery(query, adapter, timeAttribute, elevationAttribute, additionalBindings);
        return new NetCDFSliceIterator(this, slicesQuery);
    }

    /** Counts the slices matching the provided query. */
    @Override
    public int count(Query query) {
        NetCDFSliceQuery slicesQuery =
                NetCDFSliceQuery.fromQuery(query, adapter, timeAttribute, elevationAttribute, additionalBindings);

        int count = 0;
        NetCDFSliceIterator it = new NetCDFSliceIterator(this, slicesQuery);
        while (it.hasNext()) {
            it.next();
            count++;
        }
        return count;
    }

    /**
     * Returns the bounds of the underlying variable slices. Note that all the slices of a Variable share the same
     * spatial bounds, so we can return the variable bounds directly without looking at the query.
     */
    @Override
    public ReferencedEnvelope bounds(Query query) throws IOException {
        if (query == null || query.getFilter() == null || query.getFilter().equals(Filter.INCLUDE)) {
            return bounds;
        }
        NetCDFSliceQuery slicesQuery =
                NetCDFSliceQuery.fromQuery(query, adapter, timeAttribute, elevationAttribute, additionalBindings);
        Iterator<CoverageSlice> it = new NetCDFSliceIterator(this, slicesQuery);
        if (it.hasNext()) {
            return bounds;
        } else {
            // if no slice matches the query, we can return an empty envelope with the correct CRS
            ReferencedEnvelope emptyBounds = new ReferencedEnvelope(bounds.getCoordinateReferenceSystem());
            emptyBounds.setToNull();
            return emptyBounds;
        }
    }

    /** Returns the variable adapter handled by this provider. */
    public VariableAdapter getAdapter() {
        return adapter;
    }

    /** Returns the feature type used for slice features. */
    public SimpleFeatureType getSchema() {
        return schema;
    }

    /** Returns the starting global image index for this variable. */
    public int getStartIndex() {
        return startIndex;
    }

    DimensionIndexesContext getDimensionIndexesContext() {
        return dimensionsContext;
    }

    List<AdditionalDomainBinding> getAdditionalDomainBindings() {
        return additionalBindings;
    }

    /** Initializes the bindings between additional domains and logical dimensions. */
    private static List<AdditionalDomainBinding> initAdditionalBindings(VariableAdapter adapter) {
        final List<AdditionalDomainBinding> bindings = new ArrayList<>();
        final List<AdditionalDomain> additionalDomains = adapter.getAdditionalDomains();
        if (additionalDomains == null) {
            return bindings;
        }
        for (int i = 0; i < additionalDomains.size(); i++) {
            final AdditionalDomain domain = additionalDomains.get(i);
            final int logicalDimension = i + 2;
            final int actualDimension = adapter.getNDimensionIndex(logicalDimension);
            if (actualDimension < 0) {
                continue;
            }
            final String attr = adapter.getAdditionalDomainAttributeName(domain.getName());
            bindings.add(new AdditionalDomainBinding(domain.getName(), attr, logicalDimension, domain.getType()));
        }
        return bindings;
    }

    /** Associates an additional domain with its attribute name and logical dimension index. */
    public record AdditionalDomainBinding(
            String domainName, String attributeName, int logicalDimension, DomainType type) {}

    /** Per-variable context holding the available dimension lookups. */
    public static final class DimensionIndexesContext {
        private NetCDFDimensionIndexes.TimeAxisLookup time;
        private NetCDFDimensionIndexes.NumericAxisLookup elevation;
        private final List<NetCDFDimensionIndexes.AxisLookup> additional = new ArrayList<>();

        /** Returns the temporal lookup, if available. */
        public NetCDFDimensionIndexes.TimeAxisLookup getTime() {
            return time;
        }

        /** Sets the temporal lookup. */
        public void setTime(NetCDFDimensionIndexes.TimeAxisLookup time) {
            this.time = time;
        }

        /** Returns the elevation lookup, if available. */
        public NetCDFDimensionIndexes.NumericAxisLookup getElevation() {
            return elevation;
        }

        /** Sets the elevation lookup. */
        public void setElevation(NetCDFDimensionIndexes.NumericAxisLookup elevation) {
            this.elevation = elevation;
        }

        /** Returns the additional-dimension lookups. */
        public List<NetCDFDimensionIndexes.AxisLookup> getAdditional() {
            return additional;
        }

        /** Adds a lookup for an additional dimension. */
        public void addAdditional(NetCDFDimensionIndexes.AxisLookup index) {
            additional.add(index);
        }

        public DimensionIndexesContext(VariableAdapter adapter) throws IOException {
            if (adapter.getTemporalDomain() != null) {
                CoordinateVariable<Date> timeCv = adapter.getTemporalDomain().getAdaptee();
                time = NetCDFDimensionIndexes.forTime(timeCv);
            }

            if (adapter.getVerticalDomain() != null) {
                elevation = NetCDFDimensionIndexes.forVertical(adapter.getVerticalDomain());
            }

            if (adapter.getAdditionalDomains() != null) {
                for (AdditionalDomain domain : adapter.getAdditionalDomains()) {
                    VariableAdapter.UnidataAdditionalDomain ud = (VariableAdapter.UnidataAdditionalDomain) domain;
                    if (domain.getType() == DomainType.DATE) {
                        addAdditional(NetCDFDimensionIndexes.forDateAdditional(ud));
                    } else if (domain.getType() == DomainType.NUMBER) {
                        addAdditional(NetCDFDimensionIndexes.forNumericAdditional(ud));
                    } else {
                        addAdditional(null);
                    }
                }
            }
        }
    }
}
