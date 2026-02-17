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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.coverage.io.catalog.CoverageSlice;
import org.geotools.imageio.netcdf.VariableAdapter;
import org.geotools.imageio.netcdf.slice.filter.DimensionFilter;

/**
 * Iterates over NetCDF slices by combining dimension-based index selections with feature creation and optional
 * post-filter evaluation.
 *
 * <p>The iterator walks the cartesian product of selected dimension indices, resolves them into NetCDF split indices,
 * and produces {@link CoverageSlice} instances backed by {@link SimpleFeature}.
 */
final class NetCDFSliceIterator implements Iterator<CoverageSlice> {
    private final NetCDFSliceProvider variableSliceProvider;
    private final NetCDFSliceQuery slicesQuery;
    private final SliceIndexDomain sliceIndexDomain;
    private final IndexTupleIterator tupleIterator;
    private CoverageSlice next;

    /** Iterates over all combinations of selected indices across dimensions. */
    static final class IndexTupleIterator {
        private final int[][] selectedIndices;
        private final int[] offsets;
        private boolean hasNext = true;

        /** Initializes the iterator with selected indices per dimension. */
        IndexTupleIterator(int[][] selectedIndices) {
            this.selectedIndices = selectedIndices;
            this.offsets = new int[selectedIndices.length];
            for (int[] c : selectedIndices) {
                if (c.length == 0) {
                    hasNext = false;
                    break;
                }
            }
        }

        /** Returns true if more tuples are available. */
        boolean hasNext() {
            return hasNext;
        }

        /** Returns the next tuple in the cartesian product. */
        int[] next() {
            if (!hasNext) {
                throw new NoSuchElementException();
            }
            int[] tuple = new int[selectedIndices.length];
            for (int i = 0; i < selectedIndices.length; i++) {
                tuple[i] = selectedIndices[i][offsets[i]];
            }
            advanceOffset();
            return tuple;
        }

        /** Advances offsets to the next combination. */
        private void advanceOffset() {
            for (int i = 0; i < offsets.length; i++) {
                offsets[i]++;
                if (offsets[i] < selectedIndices[i].length) {
                    return;
                }
                offsets[i] = 0;
            }
            hasNext = false;
        }
    }

    /** Defines the logical dimensions and their selected indices. */
    static class SliceIndexDomain {
        private final int[] dimensions;
        private final int[][] selectedIndices;

        /**
         * Converts the recognized dimension filters into a {@link SliceIndexDomain}.
         *
         * <p>The returned domain contains the logical dimensions involved in the query and, for each of them, the
         * concrete indices selected on the corresponding NetCDF axis.
         */
        SliceIndexDomain(
                NetCDFSliceQuery sliceQuery,
                VariableAdapter adapter,
                NetCDFSliceProvider.DimensionIndexesContext context,
                List<NetCDFSliceProvider.AdditionalDomainBinding> additionalBindings) {

            DimensionFilterIndexResolver resolver = new DimensionFilterIndexResolver();

            List<Integer> logicalDimensions = new ArrayList<>();
            List<int[]> selected = new ArrayList<>();

            if (adapter.getNDimensionIndex(VariableAdapter.T) >= 0) {
                logicalDimensions.add(VariableAdapter.T);
                selected.add(resolver.resolveTime(sliceQuery.time(), context.getTime()));
            }

            if (adapter.getNDimensionIndex(VariableAdapter.Z) >= 0) {
                logicalDimensions.add(VariableAdapter.Z);
                selected.add(resolver.resolveNumeric(sliceQuery.elevation(), context.getElevation()));
            }
            Map<Integer, DimensionFilter> additional = sliceQuery.additional();
            for (NetCDFSliceProvider.AdditionalDomainBinding binding : additionalBindings) {
                if (adapter.getNDimensionIndex(binding.logicalDimension()) < 0) {
                    continue;
                }

                DimensionFilter filter = additional.getOrDefault(binding.logicalDimension(), DimensionFilter.ALL);

                NetCDFDimensionIndexes.AxisLookup axis = getAdditionalAxis(context, binding.logicalDimension());

                logicalDimensions.add(binding.logicalDimension());
                selected.add(resolver.resolveAdditional(filter, axis));
            }

            dimensions = logicalDimensions.stream().mapToInt(Integer::intValue).toArray();
            selectedIndices = selected.toArray(new int[selected.size()][]);
        }

        private static NetCDFDimensionIndexes.AxisLookup getAdditionalAxis(
                NetCDFSliceProvider.DimensionIndexesContext context, int logicalDimension) {
            int additionalIndex = logicalDimension - 2;
            if (additionalIndex < 0
                    || additionalIndex >= context.getAdditional().size()) {
                return null;
            }
            return context.getAdditional().get(additionalIndex);
        }

        /** Returns the total number of index combinations. */
        long size() {
            long result = 1L;
            for (int[] values : selectedIndices) {
                result *= values.length;
            }
            return result;
        }

        /** Expands a compact tuple into a full split index aligned with logical dimensions. */
        int[] toSplitIndex(int[] compactTuple) {
            int[] splitIndex = new int[dimensions.length == 0 ? 0 : maxLogicalDimension(dimensions) + 1];
            Arrays.fill(splitIndex, -1);
            for (int i = 0; i < dimensions.length; i++) {
                splitIndex[dimensions[i]] = compactTuple[i];
            }
            return splitIndex;
        }

        /** Returns the maximum logical dimension index. */
        private static int maxLogicalDimension(int[] logicalDimensions) {
            int max = -1;
            for (int logical : logicalDimensions) {
                if (logical > max) {
                    max = logical;
                }
            }
            return max;
        }
    }
    /** Builds an iterator over slices using the provided query and index domain. */
    NetCDFSliceIterator(NetCDFSliceProvider variableSliceProvider, NetCDFSliceQuery slicesQuery) {
        this.variableSliceProvider = variableSliceProvider;
        this.slicesQuery = slicesQuery;
        this.sliceIndexDomain = new SliceIndexDomain(
                slicesQuery,
                variableSliceProvider.getAdapter(),
                variableSliceProvider.getDimensionIndexesContext(),
                variableSliceProvider.getAdditionalDomainBindings());
        this.tupleIterator = new IndexTupleIterator(sliceIndexDomain.selectedIndices);
    }

    @Override
    public boolean hasNext() {
        if (next != null) {
            return true;
        }
        while (tupleIterator.hasNext()) {
            int[] compactTuple = tupleIterator.next();
            int[] splitIndex = sliceIndexDomain.toSplitIndex(compactTuple);

            VariableAdapter adapter = variableSliceProvider.getAdapter();
            int localImageIndex = adapter.getLocalImageIndex(splitIndex);
            int globalImageIndex = variableSliceProvider.getStartIndex() + localImageIndex;
            SimpleFeature feature =
                    adapter.createFeatureForSplitIndex(splitIndex, globalImageIndex, variableSliceProvider.getSchema());
            if (slicesQuery.hasPostFilter() && !slicesQuery.postFilter().evaluate(feature)) {
                continue;
            }
            next = new CoverageSlice(feature);
            return true;
        }
        return false;
    }

    @Override
    public CoverageSlice next() {
        if (!hasNext()) {
            throw new NoSuchElementException();
        }
        final CoverageSlice result = next;
        next = null;
        return result;
    }
}
