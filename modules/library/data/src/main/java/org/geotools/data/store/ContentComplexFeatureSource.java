/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2012, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.store;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.Set;

import org.geotools.data.DataAccess;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

public abstract class ContentComplexFeatureSource implements FeatureSource<FeatureType, Feature> {

    /**
     * The query defining the feature source
     */
    protected Query query;

    protected ContentComplexFeatureSource(Query query) {
        this.query = query;
    }

    @Override
    public Name getName() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ResourceInfo getInfo() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public DataAccess<FeatureType, Feature> getDataStore() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public FeatureType getSchema() {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public int getCount(Query query) throws IOException {
        throw new RuntimeException("Not implemented");
    }

    @Override
    public Set<Key> getSupportedHints() {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Convenience method for joining a query with the defining query of the feature source.
     */
    protected Query joinQuery(Query query) {
        // join the queries
        return DataUtilities.mixQueries(this.query, query, null);
    }
}
