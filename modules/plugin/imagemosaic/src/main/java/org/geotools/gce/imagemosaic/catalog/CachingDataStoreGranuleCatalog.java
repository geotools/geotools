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
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.AbstractFeatureVisitor;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.util.DefaultProgressListener;
import org.geotools.util.SoftValueHashMap;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.geometry.BoundingBox;

/**
 * This class simply builds an SRTREE spatial index in memory for fast indexed
 * geometric queries.
 * 
 * <p>
 * Since the {@link ImageMosaicReader} heavily uses spatial queries to find out
 * which are the involved tiles during mosaic creation, it is better to do some
 * caching and keep the index in memory as much as possible, hence we came up
 * with this index.
 * 
 * @author Simone Giannecchini, S.A.S.
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 * @since 2.5
 *
 * @source $URL$
 */
class CachingDataStoreGranuleCatalog extends GranuleCatalog {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(CachingDataStoreGranuleCatalog.class);

    private final GTDataStoreGranuleCatalog adaptee;
    
    private final SoftValueHashMap<String, GranuleDescriptor> descriptorsCache= new SoftValueHashMap<String, GranuleDescriptor>();
    
   
    /**
     * 
     * @param adaptee
     * @param hints
     */
    public CachingDataStoreGranuleCatalog(GTDataStoreGranuleCatalog adaptee) {
        super(null);
        this.adaptee=adaptee;
    }

    @Override
    public void addGranules(String typeName, Collection<SimpleFeature> granules,
            Transaction transaction) throws IOException {
        adaptee.addGranules(typeName, granules, transaction);        
    }

    @Override
    public void computeAggregateFunction(Query q, FeatureCalc function) throws IOException {
        adaptee.computeAggregateFunction(q, function);
        
    }

    @Override
    public void createType(String namespace, String typeName, String typeSpec) throws IOException,
            SchemaException {
        adaptee.createType(namespace, typeName, typeSpec);
        
    }

    @Override
    public void createType(SimpleFeatureType featureType) throws IOException {
        adaptee.createType(featureType);
        
    }

    @Override
    public void createType(String identification, String typeSpec) throws SchemaException,
            IOException {
        adaptee.createType(identification, typeSpec);
        
    }

    @Override
    public void dispose() {
        adaptee.dispose();
        
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
    public void getGranuleDescriptors(final Query q, final GranuleCatalogVisitor visitor) throws IOException {

        final SimpleFeatureCollection features = adaptee.getGranules(q);
        if (features == null)
            throw new NullPointerException(
                    "The provided SimpleFeatureCollection is null, it's impossible to create an index!");

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("Index Loaded");

        // visiting the features from the underlying store
        final DefaultProgressListener listener = new DefaultProgressListener();
        features.accepts(new AbstractFeatureVisitor() {
            public void visit(Feature feature) {
                if (feature instanceof SimpleFeature) {
                    // get the feature
                    final SimpleFeature sf = (SimpleFeature) feature;
                    final GranuleDescriptor granule;

                    // caching by granule's location
//                    synchronized (descriptorsCache) {
                        String featureId = sf.getID();
                        if(descriptorsCache.containsKey(featureId)){
                            granule=descriptorsCache.get(featureId);
                        } else{
                            // create the granule descriptor
                            granule= new GranuleDescriptor(
                                            sf,
                                            adaptee.suggestedRasterSPI,
                                            adaptee.pathType,
                                            adaptee.locationAttribute,
                                            adaptee.parentLocation,
                                            adaptee.heterogeneous, 
                                            adaptee.hints); // retain hints since this may contain a reader or anything
                            descriptorsCache.put(featureId, granule);
                        }

                        visitor.visit(granule, null);
    
                        // check if something bad occurred
                        if (listener.isCanceled() || listener.hasExceptions()) {
                            if (listener.hasExceptions()) {
                                throw new RuntimeException(listener.getExceptions().peek());
                            } else {
                                throw new IllegalStateException("Feature visitor for query " + q
                                        + " has been canceled");
                            }
                        }
//                    }
                }
            }
        }, listener);
        
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
    public int removeGranules(Query query) {
        final int val= adaptee.removeGranules(query);
        // clear cache if needed 
        // TODO this can be optimized further filtering out elements using the Query's Filter
        if(val>=1){
            descriptorsCache.clear();
        }
        
        return val;
    }

    @Override
    public String[] getTypeNames() {
        return adaptee.getTypeNames();
    }

    /**
     * @return the adaptee
     */
    public GranuleCatalog getAdaptee() {
        return adaptee;
    }
}

