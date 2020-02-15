/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;

/**
 * Reasonable default implementation for subCollection making use of parent {@link
 * SimpleFeatureCollection#features()} and provided Filter.
 *
 * <p>This is only a reasonable implementation and is not optimal. It is recommended that
 * implementors construct a new {@link Query} and use {@link
 * SimpleFeatureSource#getFeatures(Query)}.
 *
 * @author Jody Garnett, Refractions Research, Inc.
 */
public class SubFeatureCollection extends BaseSimpleFeatureCollection {

    /** Filter */
    protected Filter filter;

    /** Original Collection */
    protected SimpleFeatureCollection collection;

    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    public SubFeatureCollection(SimpleFeatureCollection collection) {
        this(collection, Filter.INCLUDE);
    }
    /** @param collection Collection or AbstractFeatureCollection */
    public SubFeatureCollection(SimpleFeatureCollection collection, Filter subfilter) {
        super(collection.getSchema());

        if (subfilter == null) {
            subfilter = Filter.INCLUDE;
        }
        if (subfilter.equals(Filter.EXCLUDE)) {
            throw new IllegalArgumentException(
                    "A subcollection with Filter.EXCLUDE would be empty");
        }
        if (collection instanceof SubFeatureCollection) {
            SubFeatureCollection filtered = (SubFeatureCollection) collection;
            if (subfilter.equals(Filter.INCLUDE)) {
                this.collection = filtered.collection;
                this.filter = filtered.filter();
            } else {
                this.collection = filtered.collection;
                this.filter = ff.and(filtered.filter(), subfilter);
            }
        } else {
            this.collection = collection;
            this.filter = subfilter;
        }
    }

    public SimpleFeatureIterator features() {
        return new FilteringSimpleFeatureIterator(collection.features(), filter());
    }

    public int size() {
        int count = 0;
        SimpleFeatureIterator i = features();
        try {
            while (i.hasNext()) {
                i.next();
                count++;
            }
        } finally {
            i.close();
        }
        return count;
    }
    /**
     * Generate filter to use for content, makes use of {@link #createFilter()} if needed.
     *
     * @return Filter to use for content
     */
    protected Filter filter() {
        if (filter == null) {
            filter = createFilter();
        }
        return filter;
    }

    /** Override to implement subsetting */
    protected Filter createFilter() {
        return Filter.INCLUDE;
    }

    public SimpleFeatureCollection subCollection(Filter filter) {
        if (filter.equals(Filter.INCLUDE)) {
            return this;
        }
        if (filter.equals(Filter.EXCLUDE)) {
            return new EmptyFeatureCollection(schema);
        }
        return new SubFeatureCollection(this, filter);
    }

    public SimpleFeatureCollection sort(SortBy order) {
        return new SubFeatureList(collection, filter, order);
    }

    public String getID() {
        return collection.getID();
    }

    /** Calculates the bounds of the features without caching. */
    @Override
    public ReferencedEnvelope getBounds() {
        return DataUtilities.bounds(this);
    }
}
