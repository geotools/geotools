/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016 Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2016 Boundless Spatial
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
package org.geotools.ysld.parse;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.styling.StyleBuilder;
import org.geotools.styling.StyleFactory;
import org.opengis.filter.FilterFactory;

/**
 * Container class for instances of {@link StyleFactory}, {@link StyleBuilder}, and {@link
 * FilterFactory} used when parsing
 */
public class Factory {
    StyleFactory style;

    StyleBuilder styleBuilder;

    FilterFactory filter;

    public Factory() {
        this(CommonFactoryFinder.getStyleFactory(), CommonFactoryFinder.getFilterFactory());
    }

    public Factory(StyleFactory style, FilterFactory filter) {
        this.style = style;
        this.styleBuilder = new StyleBuilder(style, filter);
        this.filter = filter;
    }
}
