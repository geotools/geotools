/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.vector;

import org.geotools.data.DataUtilities;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.collection.SortedSimpleFeatureCollection;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * The simple feature version of {@link ProcessingCollection}. Please see the base class for further
 * information on how to implement a proper streaming processing collection on top of this base
 * class
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
public abstract class SimpleProcessingCollection extends
        ProcessingCollection<SimpleFeatureType, SimpleFeature> implements SimpleFeatureCollection {

    @Override
    public SimpleFeatureCollection sort(SortBy order) {
        return new SortedSimpleFeatureCollection(this, new SortBy[] { order });
    }

    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        return DataUtilities.simple(super.subCollection(filter));
    }
}
