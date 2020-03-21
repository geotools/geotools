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

import java.util.UUID;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.BaseFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * Abstract base class to ease the implementation of a streaming processing collection, that is, one
 * that tries to compute the results on the fly as the iterator is traversed.
 *
 * <p>Besides the few methods that the implementor actually needs to override it suggested to
 * consider overriding also the followings to get extra performance gains:
 *
 * <ul>
 *   {@link #subCollection(Filter)} {@link #sort(SortBy)}
 * </ul>
 *
 * @author Andrea Aime - GeoSolutions
 * @param <T>
 * @param <F>
 */
public abstract class ProcessingCollection<T extends FeatureType, F extends Feature>
        extends BaseFeatureCollection<T, F> {

    public ProcessingCollection() {
        this.id = getClass().getSimpleName() + "-" + UUID.randomUUID().toString();
    }

    /** Streams out the output features */
    @Override
    public abstract FeatureIterator<F> features();

    /**
     * The bounds of features in the output. If the bounds are not known in advance once can call
     * the getFeatureBounds() which will build it from the features as they are returned from the
     * feature iterator.
     */
    @Override
    public abstract ReferencedEnvelope getBounds();

    /**
     * Builds once and for all the target feature type. The results are available by calling
     * getSchema()
     */
    protected abstract T buildTargetFeatureType();

    /**
     * The number of features in the output. If the size is not known in advance once can call the
     * getFeatureCount() which will count the features as they are returned from the feature
     * iterator.
     */
    @Override
    public abstract int size();

    @Override
    public T getSchema() {
        if (schema == null) {
            schema = buildTargetFeatureType();
        }
        return schema;
    }
}
