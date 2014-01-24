/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2013, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.Rectangle2D;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.resources.coverage.FeatureUtilities;
import org.geotools.util.Utilities;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.geometry.BoundingBox;

import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.index.ItemVisitor;
import com.vividsolutions.jts.index.strtree.STRtree;

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
 * @version 10.0
 *
 * @source $URL$
 */
@SuppressWarnings("unused")
class STRTreeGranuleCatalog extends GranuleCatalog {
	
	/** Logger. */
	final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(STRTreeGranuleCatalog.class);

	private static class JTSIndexVisitorAdapter  implements ItemVisitor {

		private GranuleCatalogVisitor adaptee;
		
		private Filter filter;

                private int maxGranules=-1;
                
                private int granuleIndex=0;

		/**
		 * @param indexLocation
		 */
		public JTSIndexVisitorAdapter(final GranuleCatalogVisitor adaptee) {
			this(adaptee,(Query)null);
		}
		
		public JTSIndexVisitorAdapter(final GranuleCatalogVisitor adaptee, Query q) {
			this.adaptee=adaptee;
			this.filter=q==null?Query.ALL.getFilter():q.getFilter();
                        this.maxGranules= q.getMaxFeatures();		
		}
		/**
		 * @param indexLocation
		 */
		public JTSIndexVisitorAdapter(final GranuleCatalogVisitor adaptee, Filter filter) {
			this.adaptee=adaptee;
			this.filter=filter==null?Query.ALL.getFilter():filter;
		}

		/*
		 * (non-Javadoc)
		 * 
		 * @see
		 * com.vividsolutions.jts.index.ItemVisitor#visitItem(java.lang.Object)
		 */
		public void visitItem(Object o) {
		    if(maxGranules>0&&granuleIndex>maxGranules){
		        return; //Skip
		    }
			if(o instanceof GranuleDescriptor){
				final GranuleDescriptor g = (GranuleDescriptor) o;
				final SimpleFeature originator = g.getOriginator();
				if(originator!=null&&filter.evaluate(originator)){
				    adaptee.visit(g,null);
				    granuleIndex++;
				}
				return;
			}
			throw new IllegalArgumentException("Unable to visit provided item"+o);

		}

	}

	private GranuleCatalog wrappedCatalogue;
	
	private String typeName;
	
	public STRTreeGranuleCatalog(final Properties params, DataStoreFactorySpi spi, final Hints hints) {
	    super(hints);
	        Utilities.ensureNonNull("params", params);
	        this.wrappedCatalogue = new GTDataStoreGranuleCatalog(params, false, spi,hints);
	        this.typeName =  (String)params.get("TypeName");
	        if(typeName==null){
	            ((GTDataStoreGranuleCatalog)wrappedCatalogue).typeNames.iterator().next();
	        }
	}
	



        /** The {@link STRtree} index. */
	private STRtree index;

	private final ReadWriteLock rwLock= new ReentrantReadWriteLock(true);

	/**
	 * Constructs a {@link STRTreeGranuleCatalog} out of a {@link FeatureCollection}.
	 * @param readLock 
	 * 
	 * @param features
	 * @throws IOException
	 */
	private void checkIndex(Lock readLock) throws IOException {
		final Lock writeLock=rwLock.writeLock();
		try{
			// upgrade the read lock to write lock
			readLock.unlock();
			writeLock.lock();
			
			// do your thing
			if (index == null) {
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine("No index exits and we create a new one.");
				createIndex();
			} else if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Index does not need to be created...");
			
		}finally{
			// get read lock again
			readLock.lock();
			// leave write lock
			writeLock.unlock();
			
		}

	}

	/**
	 * This method shall only be called when the <code>indexLocation</code> is of protocol <code>file:</code>
	 */
	private void createIndex() {
		
		Iterator<GranuleDescriptor> it=null;
		final Collection<GranuleDescriptor> features=new ArrayList<GranuleDescriptor>();
		//
		// Load tiles informations, especially the bounds, which will be
		// reused
		//
		try{

			wrappedCatalogue.getGranuleDescriptors(new Query(typeName), new GranuleCatalogVisitor() {
                            
                            @Override
                            public void visit(GranuleDescriptor granule, Object o) {
                                features.add(granule);
                                
                            }
                        });
			if (features == null) 
				throw new NullPointerException(
						"The provided SimpleFeatureCollection is null, it's impossible to create an index!");
	
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Index Loaded");
			
			//load the feature from the shapefile and create JTS index
			it = features.iterator();
			if (!it.hasNext()) 
				throw new IllegalArgumentException(
						"The provided SimpleFeatureCollection  or empty, it's impossible to create an index!");
			
			// now build the index
			// TODO make it configurable as far the index is involved
			STRtree tree = new STRtree();
			long size=0;
			while (it.hasNext()) {
				final GranuleDescriptor granule = it.next();
				final ReferencedEnvelope env=ReferencedEnvelope.reference(granule.getGranuleBBOX());
				final Geometry g = (Geometry)FeatureUtilities.getPolygon(
						new Rectangle2D.Double(env.getMinX(),env.getMinY(),env.getWidth(),env.getHeight()),0);
				tree.insert(g.getEnvelopeInternal(), granule);
			}
			
			// force index construction --> STRTrees are built on first call to
			// query
			tree.build();
			
			// save the soft reference
			index=tree;
		}
		catch (Throwable e) {
			throw new  IllegalArgumentException(e);
		}
		
	}

	/* (non-Javadoc)
	 * @see org.geotools.gce.imagemosaic.FeatureIndex#findFeatures(com.vividsolutions.jts.geom.Envelope)
	 */
	@SuppressWarnings("unchecked")
	public List<GranuleDescriptor> getGranules(final BoundingBox envelope) throws IOException {
		Utilities.ensureNonNull("envelope",envelope);
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			checkIndex(lock);
			return index.query(ReferencedEnvelope.reference(envelope));
		}finally{
			lock.unlock();
		}			
	}
	
	/* (non-Javadoc)
	 * @see org.geotools.gce.imagemosaic.FeatureIndex#findFeatures(com.vividsolutions.jts.geom.Envelope, com.vividsolutions.jts.index.ItemVisitor)
	 */
	public void getGranules(final BoundingBox envelope, final GranuleCatalogVisitor visitor) throws IOException {
		Utilities.ensureNonNull("envelope",envelope);
		Utilities.ensureNonNull("visitor",visitor);
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			
			checkIndex(lock);
			
			index.query(ReferencedEnvelope.reference(envelope), new JTSIndexVisitorAdapter(visitor));
		}finally{
			lock.unlock();
		}				
		

	}

    public void dispose() {
        final Lock l = rwLock.writeLock();
        try {
            l.lock();

            // original index
            if (wrappedCatalogue != null) {
                try {
                    wrappedCatalogue.dispose();
                } catch (Exception e) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
                }
            }
            if(multiScaleROIProvider != null) {
                multiScaleROIProvider.dispose();
            }
        } finally {
            index = null;
            multiScaleROIProvider = null;
            l.unlock();

        }
    }

	@SuppressWarnings("unchecked")
	public SimpleFeatureCollection getGranules(Query q) throws IOException {
	        q=mergeHints(q);
		Utilities.ensureNonNull("q",q);
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			
			// get filter and check bbox
			final Filter filter= q.getFilter();	
			// try to combine the index bbox with the one that may come from the query.
			ReferencedEnvelope requestedBBox=extractAndCombineBBox(filter);
			
			// load what we need to load
			checkIndex(lock);
			final List<GranuleDescriptor> features= index.query(requestedBBox);			
			final ListFeatureCollection retVal= new ListFeatureCollection(wrappedCatalogue.getType(typeName));
			final int maxGranules= q.getMaxFeatures();
			int numGranules=0;
			for(GranuleDescriptor g :features)
			{       
			        // check how many tiles we are returning
			        if(maxGranules>0&&numGranules>=maxGranules)
			            break;
				final SimpleFeature originator = g.getOriginator();
				if(originator!=null&&filter.evaluate(originator))
					retVal.add(originator);
			}
			return retVal;
		}finally{
			lock.unlock();
		}	
	}

	private ReferencedEnvelope extractAndCombineBBox(Filter filter) {
		final Utils.BBOXFilterExtractor bboxExtractor = new Utils.BBOXFilterExtractor();
		filter.accept(bboxExtractor, null);
		ReferencedEnvelope requestedBBox=bboxExtractor.getBBox();
		BoundingBox bbox = wrappedCatalogue.getBounds(typeName);
		// add eventual bbox from the underlying index to constrain search
		if(requestedBBox!=null){
			// intersection
			final Envelope intersection = requestedBBox.intersection(ReferencedEnvelope.reference(bbox));
			
			// create intersection
			final ReferencedEnvelope referencedEnvelope= new ReferencedEnvelope(intersection,bbox.getCoordinateReferenceSystem());
		}
		else{
		    return ReferencedEnvelope.reference(bbox);
		}
		return requestedBBox;
	}

	public List<GranuleDescriptor> getGranules() throws IOException {
		return getGranules(this.getBounds(typeName));
	}

	public void getGranuleDescriptors(Query q, GranuleCatalogVisitor visitor)
			throws IOException {
		Utilities.ensureNonNull("q",q);
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			
			// get filter and check bbox
			final Filter filter= q.getFilter();			
			ReferencedEnvelope requestedBBox=extractAndCombineBBox(filter);
			
			// get filter and check bbox
			checkIndex(lock);
                        index.query(requestedBBox,new JTSIndexVisitorAdapter(visitor,q));
			
		}finally{
			lock.unlock();
		}	
	}

	public BoundingBox getBounds(String typeName) {
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			
			return wrappedCatalogue.getBounds(typeName);
			
		}finally{
			lock.unlock();
		}			
	}

	/**
	 * @throws IllegalStateException
	 */
	private void checkStore() throws IllegalStateException {
		if(wrappedCatalogue==null)
			throw new IllegalStateException("The underlying store has already been disposed!");
	}

	@Override
	public SimpleFeatureType getType(final String typeName) throws IOException {
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			return this.wrappedCatalogue.getType(typeName);
		}finally{
			lock.unlock();
		}
	}

	@Override
    public String[] getTypeNames() {
        return typeName != null ? new String[]{typeName} : null;
    }


    public void computeAggregateFunction(Query query, FeatureCalc function) throws IOException {
                query=mergeHints(query);
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			wrappedCatalogue.computeAggregateFunction(query, function);
		}finally{
			lock.unlock();
		}		
		
	}
	public QueryCapabilities getQueryCapabilities() {
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			
			return wrappedCatalogue.getQueryCapabilities(typeName);
		
		}finally{
			lock.unlock();
		}	
	}

    @Override
    public int getGranulesCount(Query q) throws IOException {
        return wrappedCatalogue.getGranulesCount(mergeHints(q));
    }


    @Override
    public void addGranule(String typeName, SimpleFeature granule, Transaction transaction)
            throws IOException {
        throw new UnsupportedOperationException("Unsupported operation");
        
    }


    @Override
    public void addGranules(String typeName, Collection<SimpleFeature> granules,
            Transaction transaction) throws IOException {
        throw new UnsupportedOperationException("Unsupported operation");
        
    }


    @Override
    public void createType(String namespace, String typeName, String typeSpec) throws IOException,
            SchemaException {
        throw new UnsupportedOperationException("Unsupported operation");
        
    }


    @Override
    public void createType(SimpleFeatureType featureType) throws IOException {
        throw new UnsupportedOperationException("Unsupported operation");
    }


    @Override
    public void createType(String identification, String typeSpec) throws SchemaException,
            IOException {
        throw new UnsupportedOperationException("Unsupported operation");
        
    }


    @Override
    public QueryCapabilities getQueryCapabilities(String typeName) {
        throw new UnsupportedOperationException("Unsupported operation");
    }


    @Override
    public int removeGranules(Query query) {
        throw new UnsupportedOperationException("Unsupported operation");
    }

    @Override
    public void removeType(String typeName) throws IOException {
        final Lock lock=rwLock.readLock();
        try{
                lock.lock();
                checkStore();
                this.wrappedCatalogue.removeType(typeName);
        }finally{
                lock.unlock();
        }        
    }
}

