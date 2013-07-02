/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;
import java.util.Collection;
import java.util.Map.Entry;

import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.MultiValuedFilter.MatchAction;
import org.opengis.filter.spatial.BBOX;
import org.opengis.geometry.BoundingBox;

/**
 * A granule catalog wrapper making sure certain hints are always set in the Query
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 */
public class HintedGranuleCatalog implements GranuleCatalog {

    FilterFactory2 ff;

    GranuleCatalog delegate;

    Hints hints;

    public HintedGranuleCatalog(GranuleCatalog delegate, Hints hints) {
        this.delegate = delegate;
        this.hints = hints;
        this.ff = CommonFactoryFinder.getFilterFactory2(hints);
    }
    
    /**
     * Merges the wrapper hints with the query ones, making sure not to overwrite the query ones
     * 
     * @param q
     * @return
     */
    private Query mergeHints(Query q) {
        Query clone = new Query(q);
        Hints hints = clone.getHints();
        if (hints == null || hints.isEmpty()) {
            clone.setHints(this.hints);
        } else {
            for (Entry<Object, Object> entry : this.hints.entrySet()) {
                if (!hints.containsKey(entry.getKey())) {
                    hints.put(entry.getKey(), entry.getValue());
                }
            }
        }

        return clone;
    }

    @Override
    public Collection<GranuleDescriptor> getGranules(String typeName, BoundingBox envelope)
            throws IOException {
        BBOX bbox = ff.bbox(ff.property(""), envelope, MatchAction.ANY);
        Query q = new Query(typeName, bbox);
        q.setHints(hints);
        return delegate.getGranules(q);
    }

    @Override
    public Collection<GranuleDescriptor> getGranules(Query q) throws IOException {
        return delegate.getGranules(mergeHints(q));
    }

    @Override
    public Collection<GranuleDescriptor> getGranules(String typeName) throws IOException {
        Query q = new Query(typeName);
        q.setHints(hints);
        return delegate.getGranules(q);
    }

    @Override
    public void getGranules(String typeName, BoundingBox envelope, GranuleCatalogVisitor visitor)
            throws IOException {
        BBOX bbox = ff.bbox(ff.property(""), envelope, MatchAction.ANY);
        Query q = new Query(typeName, bbox);
        q.setHints(hints);
        delegate.getGranules(q, visitor);
    }

    @Override
    public void getGranules(Query q, GranuleCatalogVisitor visitor) throws IOException {
        delegate.getGranules(mergeHints(q), visitor);
    }

    @Override
    public void dispose() {
        delegate.dispose();
    }

    @Override
    public void addGranule(String typeName, SimpleFeature granule, Transaction transaction)
            throws IOException {
        delegate.addGranule(typeName, granule, transaction);
    }

    @Override
    public void addGranules(String typeName, Collection<SimpleFeature> granules,
            Transaction transaction) throws IOException {
        delegate.addGranules(typeName, granules, transaction);
    }

    @Override
    public void createType(String namespace, String typeName, String typeSpec) throws IOException,
            SchemaException {
        delegate.createType(namespace, typeName, typeSpec);
    }

    @Override
    public void createType(SimpleFeatureType featureType) throws IOException {
        delegate.createType(featureType);
    }

    @Override
    public void createType(String identification, String typeSpec) throws SchemaException,
            IOException {
        delegate.createType(identification, typeSpec);
    }

    @Override
    public SimpleFeatureType getType(String typeName) throws IOException {
        return delegate.getType(typeName);
    }

    @Override
    public int removeGranules(Query query) {
        return delegate.removeGranules(mergeHints(query));
    }

    @Override
    public BoundingBox getBounds(String typeName) {
        return delegate.getBounds(typeName);
    }

    @Override
    public void computeAggregateFunction(Query q, FeatureCalc function) throws IOException {
        delegate.computeAggregateFunction(mergeHints(q), function);
    }

    @Override
    public QueryCapabilities getQueryCapabilities(String typeName) {
        return delegate.getQueryCapabilities(typeName);
    }

    @Override
    public String[] getTypeNames() {
        return delegate.getTypeNames();
    }

    @Override
    public String toString() {
        return "HintedGranuleCatalog [delegate=" + delegate + ", hints=" + hints + "]";
    }

}
