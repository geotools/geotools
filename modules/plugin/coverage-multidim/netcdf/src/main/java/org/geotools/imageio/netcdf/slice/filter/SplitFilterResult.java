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
package org.geotools.imageio.netcdf.slice.filter;

import java.util.Collections;
import java.util.List;
import org.geotools.api.filter.Filter;

/** Holds the recognized dimension filters and the residual post-filter. */
public record SplitFilterResult(
        DimensionFilter time,
        DimensionFilter elevation,
        List<DimensionFilter.AdditionalDimensionFilter> additional,
        Filter postFilter) {
    /** Builds a split result with normalized default values. */
    public SplitFilterResult(
            DimensionFilter time,
            DimensionFilter elevation,
            List<DimensionFilter.AdditionalDimensionFilter> additional,
            Filter postFilter) {
        this.time = time == null ? DimensionFilter.ALL : time;
        this.elevation = elevation == null ? DimensionFilter.ALL : elevation;
        this.additional = additional == null ? Collections.emptyList() : additional;
        this.postFilter = postFilter == null ? Filter.INCLUDE : postFilter;
    }
}
