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
 * NetCDF filtering infrastructure
 *
 * <h2>Filter splitting and translation</h2>
 *
 * <p>The {@code filter} portion of the package focuses on analyzing the original GeoTools filter and separating the
 * dimension-based predicates from the residual post-filter. The main classes involved are:
 *
 * <ul>
 *   <li>{@link org.geotools.imageio.netcdf.slice.filter.DimensionFilterSplitter} is the main entry point. It
 *       coordinates the split process and returns a {@link org.geotools.imageio.netcdf.slice.filter.SplitFilterResult}
 *       containing the recognized time/elevation/additional filters plus the residual post-filter.
 *   <li>{@link org.geotools.imageio.netcdf.slice.filter.FilterSplittingVisitor} walks the original GeoTools filter tree
 *       and decides which predicates are pushdown-safe and which must remain post-processed.
 *   <li>{@link org.geotools.imageio.netcdf.slice.filter.PreFilterDimensionExtractor} converts the recognized pre-filter
 *       subtree into dimension-specific {@link org.geotools.imageio.netcdf.slice.filter.DimensionFilter} objects.
 *   <li>{@link org.geotools.imageio.netcdf.slice.filter.SplitFilterResult} is the transport object carrying the outcome
 *       of this phase.
 * </ul>
 */
package org.geotools.imageio.netcdf.slice.filter;
