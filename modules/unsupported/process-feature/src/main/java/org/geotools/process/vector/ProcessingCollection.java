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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.BaseFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

/**
 * Abstract base class to ease the implementation of a streaming processing collection, that is, one
 * that tries to compute the results on the fly as the iterator is traversed.
 * 
 * Besides the few methods that the implementor actually needs to override it suggested to consider
 * overriding also the followings to get extra performance gains:
 * <ul>
 * {@link #subCollection(Filter)} {@link #sort(SortBy)}
 * </ul>
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 * @param <T>
 * @param <F>
 */
public abstract class ProcessingCollection<T extends FeatureType, F extends Feature>
        extends BaseFeatureCollection<T, F> {

    public ProcessingCollection() {
        this.id = getClass().getSimpleName() + "-" + UUID.randomUUID().toString();
    }

    /**
     * Streams out the output features
     */
    @Override
    public abstract FeatureIterator<F> features();

    @Override
    public void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException {
        FeatureIterator<F> iterator = null;
        float size = progress != null ? size() : 0;
        if (progress == null) {
            progress = new NullProgressListener();
        }
        try {
            float position = 0;
            progress.started();
            iterator = features();
            while (!progress.isCanceled() && iterator.hasNext()) {
                try {
                    F feature = iterator.next();
                    visitor.visit(feature);
                    if (size > 0) {
                        progress.progress(position++ / size);
                    }
                } catch (Exception erp) {
                    progress.exceptionOccurred(erp);
                    throw new IOException("Error occurred while iterating over features", erp);
                }
            }
        } finally {
            progress.complete();
            if( iterator != null ){
            	iterator.close();
            }
        }

    }

    /**
     * The bounds of features in the output. If the bounds are not known in advance once can call the
     * getFeatureBounds() which will build it from the features as they are returned from the feature
     * iterator.
     */
    @Override
    public abstract ReferencedEnvelope getBounds();

    /**
     * Builds once and for all the target feature type. The results are available by calling getSchema()
     * 
     * @return
     */
    protected abstract T buildTargetFeatureType();

    /**
     * The number of features in the output. If the size is not known in advance once can call the
     * getFeatureCount() which will count the features as they are returned from the feature
     * iterator.
     */
    @Override
    public abstract int size();

    /**
     * Convenience method that counts features by traversing the feature iterator.
     * 
     * @return number of features using {@link #features()}
     * @deprecated Use {@link DataUtilities#count(org.geotools.feature.FeatureCollection)
     */
    protected int getFeatureCount() {
        return DataUtilities.count( this );
    }
    
    /**
     * Utility to get all the features to implement the toArray methods
     * @deprecated Use {@link DataUtilities#list(org.geotools.feature.FeatureCollection)}
     */
    protected List<F> toList() {
        return DataUtilities.list( this );
    }
    /**
     * Convenience method that computes the feature bounds by traversing the feature iterator.
     * 
     * @return bounds calculated using {@link #features()}
     * @deprecated Use {@link DataUtilities#bounds(FeatureIterator)
     */
    protected ReferencedEnvelope getFeatureBounds() {
        return DataUtilities.bounds( features() );
    }

    @Override
    public T getSchema() {
        if(schema == null) {
            schema = buildTargetFeatureType();
        }        
        return schema;
    }

}
