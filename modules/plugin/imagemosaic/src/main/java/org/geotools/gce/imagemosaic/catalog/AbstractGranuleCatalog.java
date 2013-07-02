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
package org.geotools.gce.imagemosaic.catalog;

import java.io.IOException;
import java.util.Collection;

import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;

/**
 * 
 *
 * @source $URL$
 */
public class AbstractGranuleCatalog implements GranuleCatalog {

    public void addGranule(final String typeName, SimpleFeature granule, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("This Catalog does not support adding granules ");
    }

    public void addGranules(final String typeName, Collection<SimpleFeature> granules, Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("This Catalog does not support adding granules ");
    }

//    public void addGranule(SimpleFeature granule, Transaction transaction) throws IOException {
//        throw new UnsupportedOperationException("This Catalog does not support adding granules ");
//    }
//
//    public void addGranules(Collection<SimpleFeature> granules, Transaction transaction) throws IOException {
//        throw new UnsupportedOperationException("This Catalog does not support adding granules ");
//    }
    
    public void computeAggregateFunction(Query q, FeatureCalc function) throws IOException {
        throw new UnsupportedOperationException("This Catalog does not support Aggregate function");
    }

    public void createType(String namespace, String typeName, String typeSpec) throws IOException, SchemaException {
        throw new UnsupportedOperationException("This Catalog does not support FeatureTypes creation");
    }

    public void createType(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("This Catalog does not support FeatureTypes creation");
    }

    public void createType(String identification, String typeSpec) throws SchemaException, IOException {
        throw new UnsupportedOperationException("This Catalog does not support FeatureTypes creation");
    }

    public void dispose() {
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public BoundingBox getBounds(final String typeName) {
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

//    public BoundingBox getBounds(){
//        throw new UnsupportedOperationException("This Catalog does not support this method");
//    }
//
//    public Collection<GranuleDescriptor> getGranules(BoundingBox envelope) throws IOException{
//        throw new UnsupportedOperationException("This Catalog does not support this method");
//    }

    public Collection<GranuleDescriptor> getGranules(final String typeName, BoundingBox envelope) throws IOException {
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public Collection<GranuleDescriptor> getGranules(Query q) throws IOException{
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }
//
//    public  Collection<GranuleDescriptor> getGranules() throws IOException{
//        throw new UnsupportedOperationException("This Catalog does not support this method");
//    }
//
//    public  void getGranules(BoundingBox envelope, GranuleCatalogVisitor visitor) throws IOException{
//        throw new UnsupportedOperationException("This Catalog does not support this method");
//    }
//
    public  void getGranules(Query q, GranuleCatalogVisitor visitor) throws IOException{
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public Collection<GranuleDescriptor> getGranules(final String typeName) throws IOException{
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public  void getGranules(final String typeName, BoundingBox envelope, GranuleCatalogVisitor visitor) throws IOException{
        throw new UnsupportedOperationException("This Catalog does not support this method");
    }

    public QueryCapabilities getQueryCapabilities(final String typeName) {
        return null;
    }

//    public QueryCapabilities getQueryCapabilities(){
//        return null;
//    }
//
//    public SimpleFeatureType getType() throws IOException {
//        return null;
//    }

    public SimpleFeatureType getType(final String typeName) throws IOException {
        return null;
    }

    public int removeGranules(Query query) {
        throw new UnsupportedOperationException("This Catalog does not support removing granules");        
    }

    public String[] getTypeNames() {
        throw new UnsupportedOperationException("This Catalog does not support removing granules");
    }
}
