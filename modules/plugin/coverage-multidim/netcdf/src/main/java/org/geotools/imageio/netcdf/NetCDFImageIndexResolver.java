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
package org.geotools.imageio.netcdf;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.geotools.util.Utilities;

/**
 * Resolves a global image index into the owning variable and its logical non-spatial dimension indexes.
 *
 * <p><b>Example</b>
 *
 * <p>Consider a dataset with two variables:
 *
 * <ul>
 *   <li><b>surface_temperature</b> with 3 time steps
 *   <li><b>air_temperature</b> with 3 time steps and 2 elevation levels
 * </ul>
 *
 * The resulting flattened image indexes could be:
 *
 * <pre>
 * Index  Variable             Time index   Elevation index
 * -------------------------------------------------------
 *   0    surface_temperature      0              -
 *   1    surface_temperature      1              -
 *   2    surface_temperature      2              -
 *
 *   3    air_temperature          0              0
 *   4    air_temperature          0              1
 *   5    air_temperature          1              0
 *   6    air_temperature          1              1
 *   7    air_temperature          2              0
 *   8    air_temperature          2              1
 * </pre>
 *
 * Given a global image index, this resolver identifies:
 *
 * <ul>
 *   <li>the corresponding variable (e.g. {@code air_temperature})
 *   <li>the logical indexes along non-spatial dimensions (e.g. time=1, elevation=0)
 * </ul>
 *
 * For example:
 *
 * <ul>
 *   <li>index 1 -> surface_temperature, time=1
 *   <li>index 6 -> air_temperature, time=1, elevation=1
 * </ul>
 */
public final class NetCDFImageIndexResolver {

    /**
     * Represents the result of resolving a variable selection into index positions.
     *
     * <p>This encapsulates the target variable name and the corresponding index for each dimension, forming a fully
     * specified coordinate within the variable, identifying a 2D(xy) slice of the multidimensional hypercube.
     */
    public record Slice2DIndex(String variableName, int[] indexes) {

        public int getNIndex(int n) {
            return indexes[n];
        }

        public int getNCount() {
            return indexes.length;
        }
    }

    /**
     * Associates a variable with the contiguous range of global image indexes it occupies.
     *
     * <p>Each variable contributes a block of indexes in the flattened image index space; this class records the start
     * (inclusive) and end (exclusive) of that block, allowing fast resolution of a global index to its owning variable.
     *
     * <p>Instances are typically organized in a sorted collection and used by {@link NetCDFImageIndexResolver} to
     * locate the variable corresponding to a given index with O(log n) lookup . Based on the above example:
     *
     * <ul>
     *   <li>{@code surface_temperature → [0, 3)}
     *   <li>{@code air_temperature → [3, 9)}
     * </ul>
     */
    static final class VariableIndexRange {
        private final VariableAdapter variableAdapter;
        private final int startIndexInclusive;
        private final int endIndexExclusive;

        VariableIndexRange(VariableAdapter variableAdapter, int startIndexInclusive) {
            this.variableAdapter = variableAdapter;
            this.startIndexInclusive = startIndexInclusive;
            this.endIndexExclusive = startIndexInclusive + variableAdapter.getNumberOfSlices();
        }

        int toLocalIndex(int imageIndex) {
            return imageIndex - startIndexInclusive;
        }

        int getSliceCount() {
            return endIndexExclusive - startIndexInclusive;
        }
    }

    // The list of ranges of all the Variables
    private final List<VariableIndexRange> ranges;

    // The global imageIndex where each variable starts (following same order of variables)
    // i.e. 0 for surface_temperature, 3 for air_temperature.
    // These will be used with a binarySearch to identify the variable given a global index.
    private final int[] startIndexes;
    private final int totalImageCount;

    /**
     * Build the ImageIndexResolver on top of the available VariableAdapters, provided in the same order they get
     * discovered from the originating NetCDF.
     */
    public NetCDFImageIndexResolver(List<VariableAdapter> variableAdapters) {
        Utilities.ensureNonNull("variableAdapters", variableAdapters);

        List<VariableIndexRange> variableIndexRanges = new ArrayList<>(variableAdapters.size());
        List<Integer> starts = new ArrayList<>(variableAdapters.size());

        int offset = 0;
        for (VariableAdapter adapter : variableAdapters) {
            if (adapter == null) {
                continue;
            }
            VariableIndexRange variableIndexRange = new VariableIndexRange(adapter, offset);
            variableIndexRanges.add(variableIndexRange);
            starts.add(offset);
            offset += variableIndexRange.getSliceCount();
        }

        this.ranges = Collections.unmodifiableList(variableIndexRanges);
        this.startIndexes = new int[starts.size()];
        for (int i = 0; i < starts.size(); i++) {
            this.startIndexes[i] = starts.get(i);
        }
        this.totalImageCount = offset;
    }

    /** Resolve a global imageIndex to the corresponding Variable and the underlying dimension indices. */
    public Slice2DIndex resolve(int imageIndex) {
        VariableIndexRange variableIndexRange = toVariableIndexRange(imageIndex);
        int localImageIndex = variableIndexRange.toLocalIndex(imageIndex);
        int[] indexes = variableIndexRange.variableAdapter.splitIndex(localImageIndex);
        return new Slice2DIndex(variableIndexRange.variableAdapter.getName(), indexes);
    }

    private VariableIndexRange toVariableIndexRange(int imageIndex) {
        if (imageIndex < 0 || imageIndex >= totalImageCount) {
            throw new IndexOutOfBoundsException(
                    "Invalid imageIndex " + imageIndex + ", valid range is [0.." + (totalImageCount - 1) + "]");
        }

        int idx = Arrays.binarySearch(startIndexes, imageIndex);
        if (idx >= 0) {
            return ranges.get(idx);
        }

        // imageIndex falls inside the range started by the previous entry
        int insertionPoint = -(idx + 1);
        return ranges.get(insertionPoint - 1);
    }
}
