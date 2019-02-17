/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
 *
 */

package org.geotools.brewer.styling.filter;

import org.geotools.brewer.styling.builder.Builder;
import org.geotools.factory.CommonFactoryFinder;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

/** FilterBuilder acting as a simple wrapper around an Expression. */
public class FilterBuilder implements Builder<Filter> {
    protected Filter filter; // placeholder just to keep us going right now
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory2(null);
    protected boolean unset = false;
    protected Builder<? extends Filter> delegate = null;

    public FilterBuilder() {
        reset();
    }

    public FilterBuilder(Filter filter) {
        reset(filter);
    }

    /** Build the expression. */
    public Filter build() {
        if (unset) {
            return null;
        }
        return filter;
    }

    public FilterBuilder reset() {
        this.delegate = null;
        this.filter = org.opengis.filter.Filter.EXCLUDE;
        this.unset = false;
        return this;
    }

    public FilterBuilder reset(Filter filter) {
        if (filter == null) {
            return unset();
        }
        this.filter = filter;
        this.unset = false;
        return this;
    }

    public FilterBuilder unset() {
        this.unset = true;
        this.delegate = null;
        this.filter = Filter.EXCLUDE;
        return this;
    }
}
