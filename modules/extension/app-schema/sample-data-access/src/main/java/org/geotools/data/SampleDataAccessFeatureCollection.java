/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

/**
 * Collection of features from a {@link SampleDataAccess}.
 * 
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 * 
 * 
 *
 * @source $URL$
 * @since 2.6
 */
@SuppressWarnings("serial")
public class SampleDataAccessFeatureCollection extends ArrayList<Feature> implements
        FeatureCollection<FeatureType, Feature> {

    /**
     * @see org.geotools.feature.FeatureCollection#accepts(org.opengis.feature.FeatureVisitor,
     *      org.opengis.util.ProgressListener)
     */
    public void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException {
        for (Feature feature : this) {
            visitor.visit(feature);
        }
    }

    /**
     * Get an iterator over the features.
     * 
     * @see org.geotools.feature.FeatureCollection#features()
     */
    public FeatureIterator<Feature> features() {
        return new SampleDataAccessFeatureIterator(iterator());
    }

    /**
     * Not yet implemented.
     * 
     * @see org.geotools.feature.FeatureCollection#getBounds()
     */
    public ReferencedEnvelope getBounds() {
        // FIXME implement this
        return null;
    }

    /**
     * Not yet implemented.
     * 
     * @see org.geotools.feature.FeatureCollection#getID()
     */
    public String getID() {
        // FIXME implement this
        return null;
    }

    /**
     * Return type of features.
     * 
     * @see org.geotools.feature.FeatureCollection#getSchema()
     */
    public FeatureType getSchema() {
        return SampleDataAccessData.MAPPEDFEATURE_TYPE;
    }

    /**
     * Unsupported operation.
     * 
     * @see org.geotools.feature.FeatureCollection#sort(org.opengis.filter.sort.SortBy)
     */
    public FeatureCollection<FeatureType, Feature> sort(SortBy order) {
        throw new UnsupportedOperationException();
    }

    /**
     * Unsupported operation.
     * 
     * @see org.geotools.feature.FeatureCollection#subCollection(org.opengis.filter.Filter)
     */
    public FeatureCollection<FeatureType, Feature> subCollection(Filter filter) {
        throw new UnsupportedOperationException();
    }

}
