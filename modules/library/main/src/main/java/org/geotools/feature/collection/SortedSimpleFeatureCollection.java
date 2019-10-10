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
 */
package org.geotools.feature.collection;

import java.io.IOException;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.sort.SortedFeatureIterator;
import org.geotools.util.factory.Hints;
import org.opengis.filter.sort.SortBy;

/**
 * A wrapper that will sort a feature collection using a size sensitive algorithm, in main memory
 * for small collections, using secondary memory otherwise. The threshold is defined by the {@link
 * Hints#MAX_MEMORY_SORT} feature count
 *
 * @author Andrea Aime - GeoSolutions
 */
public class SortedSimpleFeatureCollection extends DecoratingSimpleFeatureCollection {

    private SortBy[] sort;

    public SortedSimpleFeatureCollection(SimpleFeatureCollection delegate, SortBy[] sort) {
        super(delegate);
        this.sort = sort;
    }

    @Override
    public SimpleFeatureIterator features() {
        try {
            SimpleFeatureIterator features = delegate.features();
            // sort if necessary
            if (sort != null) {
                features = new SortedFeatureIterator(features, getSchema(), sort, -1);
            }
            return features;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
