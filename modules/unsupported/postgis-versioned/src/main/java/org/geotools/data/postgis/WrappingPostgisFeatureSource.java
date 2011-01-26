/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.io.IOException;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

/**
 * Really delegates everything to a wrapped feature source, but allows to
 * advertise a data store other than the original one
 * 
 * @author aaime
 * @since 2.4
 * 
 *
 * @source $URL$
 */
public class WrappingPostgisFeatureSource implements SimpleFeatureSource {
    SimpleFeatureSource wrapped;

    VersionedPostgisDataStore store;

    public WrappingPostgisFeatureSource(SimpleFeatureSource wrapped,
            VersionedPostgisDataStore store) {
        this.wrapped = wrapped;
        this.store = store;
    }
    
    public ResourceInfo getInfo() {
        return wrapped.getInfo();
    }

    /**
     * @since 2.5
     * @see FeatureSource#getName()
     */
    public Name getName() {
        return wrapped.getName();
    }
    
    public DataStore getDataStore() {
        return store;
    }

    public void addFeatureListener(FeatureListener listener) {
        wrapped.addFeatureListener(listener);
    }

    public void removeFeatureListener(FeatureListener listener) {
        wrapped.removeFeatureListener(listener);
    }

    public ReferencedEnvelope getBounds() throws IOException {
        return wrapped.getBounds();
    }

    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return wrapped.getBounds(query);
    }

    public int getCount(Query query) throws IOException {
        return wrapped.getCount(query);
    }

    public SimpleFeatureCollection getFeatures() throws IOException {
        return wrapped.getFeatures();
    }

    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return wrapped.getFeatures(filter);
    }

    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        return wrapped.getFeatures(query);
    }

    public SimpleFeatureType getSchema() {
        return wrapped.getSchema();
    }
    
    public Set getSupportedHints() {
        return wrapped.getSupportedHints();
    }

    public QueryCapabilities getQueryCapabilities() {
        return wrapped.getQueryCapabilities();
    }
}
