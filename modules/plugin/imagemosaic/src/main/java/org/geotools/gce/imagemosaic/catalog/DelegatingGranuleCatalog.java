/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import org.geotools.coverage.grid.io.footprint.MultiLevelROI;
import org.geotools.coverage.grid.io.footprint.MultiLevelROIProvider;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.util.factory.Hints;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.geometry.BoundingBox;

class DelegatingGranuleCatalog extends GranuleCatalog {

    GranuleCatalog adaptee;

    public DelegatingGranuleCatalog(GranuleCatalog adaptee) {
        super(adaptee.getHints(), adaptee.getConfigurations());
        this.adaptee = adaptee;
    }

    @Override
    public void addGranules(
            String typeName, Collection<SimpleFeature> granules, Transaction transaction)
            throws IOException {
        adaptee.addGranules(typeName, granules, transaction);
    }

    @Override
    public void computeAggregateFunction(Query q, FeatureCalc function) throws IOException {
        adaptee.computeAggregateFunction(q, function);
    }

    @Override
    public void createType(String namespace, String typeName, String typeSpec)
            throws IOException, SchemaException {
        adaptee.createType(namespace, typeName, typeSpec);
    }

    @Override
    public void createType(SimpleFeatureType featureType) throws IOException {
        adaptee.createType(featureType);
    }

    @Override
    public void createType(String identification, String typeSpec)
            throws SchemaException, IOException {
        adaptee.createType(identification, typeSpec);
    }

    @Override
    public void dispose() {
        adaptee.dispose();
        if (multiScaleROIProvider != null) {
            multiScaleROIProvider.dispose();
            multiScaleROIProvider = null;
        }
    }

    @Override
    public BoundingBox getBounds(String typeName) {
        return adaptee.getBounds(typeName);
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q) throws IOException {
        return adaptee.getGranules(q);
    }

    @Override
    public int getGranulesCount(Query q) throws IOException {
        return adaptee.getGranulesCount(q);
    }

    @Override
    public void getGranuleDescriptors(Query q, GranuleCatalogVisitor visitor) throws IOException {
        adaptee.getGranuleDescriptors(q, visitor);
    }

    @Override
    public QueryCapabilities getQueryCapabilities(String typeName) {
        return adaptee.getQueryCapabilities(typeName);
    }

    @Override
    public SimpleFeatureType getType(String typeName) throws IOException {
        return adaptee.getType(typeName);
    }

    @Override
    public void removeType(String typeName) throws IOException {
        adaptee.removeType(typeName);
    }

    @SuppressWarnings("deprecation")
    @Override
    public int removeGranules(Query query) {
        return adaptee.removeGranules(query);
    }

    @Override
    public String[] getTypeNames() {
        return adaptee.getTypeNames();
    }

    @Override
    public void drop() throws IOException {
        adaptee.drop();
    }

    @Override
    protected String getParentLocation() {
        return adaptee.getParentLocation();
    }

    @Override
    public void addGranule(String typeName, SimpleFeature granule, Transaction transaction)
            throws IOException {
        adaptee.addGranule(typeName, granule, transaction);
    }

    @Override
    public BoundingBox getBounds(String typeName, Transaction t) {
        return adaptee.getBounds(typeName, t);
    }

    @Override
    public SimpleFeatureCollection getGranules(Query q, Transaction t) throws IOException {
        return adaptee.getGranules(q, t);
    }

    @Override
    public int removeGranules(Query query, Transaction transaction) {
        return adaptee.removeGranules(query, transaction);
    }

    @Override
    protected Query mergeHints(Query q) {
        return adaptee.mergeHints(q);
    }

    @Override
    public void setMultiScaleROIProvider(MultiLevelROIProvider footprintProvider) {
        adaptee.setMultiScaleROIProvider(footprintProvider);
    }

    @Override
    protected MultiLevelROI getGranuleFootprint(SimpleFeature sf) {
        return adaptee.getGranuleFootprint(sf);
    }

    @Override
    public List<File> getFootprintFiles(SimpleFeature sf) throws IOException {
        return adaptee.getFootprintFiles(sf);
    }

    @Override
    protected CatalogConfigurationBeans getConfigurations() {
        return adaptee.getConfigurations();
    }

    @Override
    public Hints getHints() {
        return adaptee.getHints();
    }

    public GranuleCatalog getAdaptee() {
        return adaptee;
    }
}
