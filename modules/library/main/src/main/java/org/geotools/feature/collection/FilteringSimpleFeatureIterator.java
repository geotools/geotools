/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.FilteringFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

/**
 * Extension of {@link FilteringFeatureIterator} that type narrows to {@link SimpleFeature}.
 * 
 * @author Justin Deoliveira, OpenGeo
 *
 */
public class FilteringSimpleFeatureIterator extends FilteringFeatureIterator<SimpleFeature> 
    implements SimpleFeatureIterator {

    public FilteringSimpleFeatureIterator(SimpleFeatureIterator delegate, Filter filter) {
        super(delegate, filter);
    }

}
