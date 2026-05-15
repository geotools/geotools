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
/**
 * NetCDF slice-query infrastructure used by the GeoTools NetCDF reader to translate catalog-style
 * {@link org.geotools.api.data.Query} objects into NetCDF dimension selections and, ultimately, into readable 2D
 * slices.
 *
 * <p>Queries containing time, elevation, and additional dimensions must be translated into ImageIO {@code imageIndex}
 * values, each representing a 2D slice of the underlying NetCDF variable. These {@code imageIndex} values are then
 * resolved into the corresponding NetCDF multidimensional indices (time, elevation, and other dimensions), allowing the
 * reader to access the correct portion of the NetCDF data.
 *
 * <p>The code in this package is organized around a staged pipeline:
 *
 * <ol>
 *   <li>A GeoTools filter is analyzed and split into:
 *       <ul>
 *         <li>dimension-backed predicates that can be resolved directly against NetCDF axes
 *         <li>a residual post-filter that must still be evaluated in memory on produced features
 *       </ul>
 *   <li>The recognized dimension predicates are translated into
 *       {@link org.geotools.imageio.netcdf.slice.filter.DimensionFilter} instances for time, elevation, and additional
 *       dimensions.
 *   <li>Those dimension filters are resolved to concrete NetCDF axis indices by consulting per-variable lookup
 *       structures built from underlying NetCDF coordinate variables.
 *   <li>The matching logical dimension tuples are iterated, converted to NetCDF split indices, mapped to image indexes,
 *       and exposed as {@link org.geotools.coverage.io.catalog.CoverageSlice}s.
 * </ol>
 *
 * <h2>Filter splitting and translation</h2>
 *
 * <p>See the {@code filter} portion of the package for the logic that walks the original GeoTools filter tree using the
 * {@link org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter}.
 *
 * <p>{@link org.geotools.imageio.netcdf.slice.NetCDFDimensionBindingResolver} centralizes the mapping between feature
 * attributes and NetCDF logical dimensions by answering which attribute corresponds to time, elevation, or a given
 * additional dimension.
 *
 * <h2>Slice query planning and execution</h2>
 *
 * <p>The {@code slice} portion of the package turns the split result into actual slice iteration:
 *
 * <ul>
 *   <li>{@link org.geotools.imageio.netcdf.slice.NetCDFSliceProvider} is the
 *       {@link org.geotools.coverage.io.catalog.CoverageSlicesCatalog.SliceProvider} implementation for a single NetCDF
 *       variable. It is the main runtime bridge between {@link org.geotools.coverage.io.catalog.CoverageSlicesCatalog}
 *       queries and NetCDF slice access.
 *   <li>{@link org.geotools.imageio.netcdf.slice.NetCDFSliceQuery} is the preprocessed query representation. It stores
 *       the recognized dimension filters and the residual post-filter, and prepares the logical dimension domain to
 *       iterate.
 *   <li>{@link org.geotools.imageio.netcdf.slice.NetCDFSliceIterator} iterates the cartesian product of the selected
 *       dimension indices, builds the corresponding features, and applies the residual post-filter when needed.
 *   <li>{@link org.geotools.imageio.netcdf.NetCDFImageIndexResolver} resolves a global image index into the owning
 *       variable and its logical non-spatial dimension indices, allowing the reader to move between flattened image
 *       indexes and multidimensional NetCDF coordinates.
 *   <li>{@link org.geotools.imageio.netcdf.slice.NetCDFDimensionIndexes} builds and exposes lookup structures for
 *       temporal, elevation, and additional dimensions. It provides regular-axis and array-backed implementations,
 *       exact-match support, and lower/upper bound search methods used by range filters.
 *   <li>{@link org.geotools.imageio.netcdf.slice.DimensionFilterIndexResolver} takes a
 *       {@link org.geotools.imageio.netcdf.slice.filter.DimensionFilter} together with one of the lookup types from
 *       {@link org.geotools.imageio.netcdf.slice.NetCDFDimensionIndexes} and resolves it into the exact set of matching
 *       axis indices.
 * </ul>
 *
 * <h2>Class interactions</h2>
 *
 * <p>The main interaction flow is:
 *
 * <pre>
 * Query
 *   -> NetCDFSliceProvider (query)
 *   -> NetCDFSliceQuery
 *   -> DimensionFilterSplitter
 *   -> SplitFilterResult
 *   -> NetCDFSliceQuery.toDomain(...)
 *   -> DimensionFilterIndexResolver
 *   -> NetCDFDimensionIndexes lookup objects
 *   -> NetCDFSliceIterator
 *   -> CoverageSlice / SimpleFeature
 * </pre>
 */
package org.geotools.imageio.netcdf.slice;
