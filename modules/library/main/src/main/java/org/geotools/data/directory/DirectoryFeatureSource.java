/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.directory;

import java.awt.RenderingHints.Key;
import java.io.IOException;
import java.util.Set;
import org.geotools.data.DataAccess;
import org.geotools.data.FeatureListener;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.ResourceInfo;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

public class DirectoryFeatureSource implements SimpleFeatureSource {
    SimpleFeatureSource fsource;

    public DirectoryFeatureSource(SimpleFeatureSource delegate) {
        this.fsource = delegate;
    }

    @Override
    public void addFeatureListener(FeatureListener listener) {
        fsource.addFeatureListener(listener);
    }

    @Override
    public ReferencedEnvelope getBounds() throws IOException {
        return fsource.getBounds();
    }

    @Override
    public ReferencedEnvelope getBounds(Query query) throws IOException {
        return fsource.getBounds(query);
    }

    @Override
    public int getCount(Query query) throws IOException {
        return fsource.getCount(query);
    }

    @Override
    public DataAccess<SimpleFeatureType, SimpleFeature> getDataStore() {
        // this is done on purpose to avoid crippling the shapefile renderer optimizations
        return fsource.getDataStore();
    }

    @Override
    public SimpleFeatureCollection getFeatures() throws IOException {
        return fsource.getFeatures();
    }

    @Override
    public SimpleFeatureCollection getFeatures(Filter filter) throws IOException {
        return fsource.getFeatures(filter);
    }

    @Override
    public SimpleFeatureCollection getFeatures(Query query) throws IOException {
        return fsource.getFeatures(query);
    }

    @Override
    public ResourceInfo getInfo() {
        return fsource.getInfo();
    }

    @Override
    public Name getName() {
        return fsource.getName();
    }

    @Override
    public QueryCapabilities getQueryCapabilities() {
        return fsource.getQueryCapabilities();
    }

    @Override
    public SimpleFeatureType getSchema() {
        return fsource.getSchema();
    }

    @Override
    public Set<Key> getSupportedHints() {
        return fsource.getSupportedHints();
    }

    @Override
    public void removeFeatureListener(FeatureListener listener) {
        fsource.removeFeatureListener(listener);
    }

    public SimpleFeatureSource unwrap() {
        return fsource;
    }
}
