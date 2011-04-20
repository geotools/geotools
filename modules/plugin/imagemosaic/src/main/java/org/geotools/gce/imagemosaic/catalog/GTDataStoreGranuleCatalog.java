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
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.QueryCapabilities;
import org.geotools.data.Transaction;
import org.geotools.data.postgis.PostgisNGDataStoreFactory;
import org.geotools.data.postgis.PostgisNGJNDIDataStoreFactory;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;
import org.geotools.feature.collection.AbstractFeatureVisitor;
import org.geotools.feature.visitor.FeatureCalc;
import org.geotools.filter.visitor.DefaultFilterVisitor;
import org.geotools.gce.imagemosaic.GranuleDescriptor;
import org.geotools.gce.imagemosaic.ImageMosaicReader;
import org.geotools.gce.imagemosaic.PathType;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.DefaultProgressListener;
import org.geotools.util.Utilities;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;
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
	 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/plugin/imagemosaic/src/main/java/org/geotools/gce/imagemosaic/RasterManager.java $
 */
class GTDataStoreGranuleCatalog extends AbstractGranuleCatalog {
	
	/** Logger. */
	final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GTDataStoreGranuleCatalog.class);

	/**
     * UTC timezone to serve as reference
     */
    static final TimeZone UTC_TZ = TimeZone.getTimeZone("UTC");
    
	final static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( GeoTools.getDefaultHints() );
	
	/**
	 * Extracts a bbox from a filter in case there is at least one.
	 * 
	 * I am simply looking for the BBOX filter but I am sure we could
	 * use other filters as well. I will leave this as a todo for the moment.
	 * 
	 * @author Simone Giannecchini, GeoSolutions SAS.
	 * @todo TODO use other spatial filters as well
	 */
	@SuppressWarnings("deprecation")
	static class BBOXFilterExtractor extends DefaultFilterVisitor{

		public ReferencedEnvelope getBBox() {
			return bbox;
		}
		private ReferencedEnvelope bbox;
		@Override
		public Object visit(BBOX filter, Object data) {
			final ReferencedEnvelope bbox= new ReferencedEnvelope(
					filter.getMinX(),
					filter.getMinY(),
					filter.getMaxX(),
					filter.getMaxY(),
					null);
			if(this.bbox!=null)
				this.bbox=(ReferencedEnvelope) this.bbox.intersection(bbox);
			else
				this.bbox=bbox;
			return super.visit(filter, data);
		}
		
	}
	
	
	private DataStore tileIndexStore;

	private String typeName;

	private String geometryPropertyName;

	private ReferencedEnvelope bounds;

	private DataStoreFactorySpi spi;

	private PathType pathType;

	private String locationAttribute;

	private ImageReaderSpi suggestedSPI;

	private String parentLocation;
	
	private boolean heterogeneous;

	public GTDataStoreGranuleCatalog(
			final Map<String, Serializable> params, 
			final boolean create, 
			final DataStoreFactorySpi spi) {
		Utilities.ensureNonNull("params",params);
		Utilities.ensureNonNull("spi",spi);
		this.spi=spi;
		
		try{

			this.pathType=(PathType) params.get("PathType");
			this.locationAttribute=(String)params.get("LocationAttribute");
			final String temp=(String)params.get("SuggestedSPI");
			this.suggestedSPI=temp!=null?(ImageReaderSpi) Class.forName(temp).newInstance():null;
			this.parentLocation=(String)params.get("ParentLocation");
			Object heterogen = params.get("Heterogeneous");
			if (heterogen != null){
			    this.heterogeneous = ((Boolean) heterogen).booleanValue();
			}
			
			// creating a store, this might imply creating it for an existing underlying store or 
			// creating a brand new one
			if(!create)
				tileIndexStore =spi.createDataStore(params);
			else
			{
				// this works only with the shapefile datastore, not with the others
				// therefore I try to catch the error to try and use themethdo without *New*
				try{
					tileIndexStore =  spi.createNewDataStore(params);
				}catch (UnsupportedOperationException e) {
					tileIndexStore =  spi.createDataStore(params);
				}
			}

			
			// is this a new store? If so we do not set any properties
			if(create)
				return;
				
			// if this is not a new store let's extract basic properties from it
			if(spi instanceof PostgisNGJNDIDataStoreFactory||spi instanceof PostgisNGDataStoreFactory){
				String typeName = FilenameUtils.getBaseName(FilenameUtils.getPathNoEndSeparator(this.parentLocation));
				//if (typeName != null){
				//    typeName = typeName.toLowerCase();
				//}
               extractBasicProperties(typeName);
			} else {
				extractBasicProperties(null);
			}
		}
		catch (Throwable e) {
			try {
				if(tileIndexStore!=null)
					tileIndexStore.dispose();
			} catch (Throwable e1) {
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE, e1.getLocalizedMessage(), e1);
			}
			finally{
				tileIndexStore=null;
			}	

			throw new  IllegalArgumentException(e);
		}
		
	}

	/**
	 * If the underlying store has been disposed we throw an {@link IllegalStateException}.
	 * <p>
	 * We need to arrive here with at least a read lock!
	 * @throws IllegalStateException in case the underlying store has been disposed. 
	 */
	private void checkStore()throws IllegalStateException{
		if (tileIndexStore == null) {
			throw new IllegalStateException("The index store has been disposed already.");
		}
	}
	
        private void extractBasicProperties(String typeName) throws IOException{
    
            final String[] typeNames = tileIndexStore.getTypeNames();
            if (typeNames==null||typeNames.length <= 0)
                throw new IllegalArgumentException(
                        "BBOXFilterExtractor::extractBasicProperties(): Problems when opening the index,"
                                + " no typenames for the schema are defined");
    
            if (typeName == null) {
                typeName = typeNames[0];
                if (LOGGER.isLoggable(Level.WARNING))
                    LOGGER.warning("BBOXFilterExtractor::extractBasicProperties(): passed typename is null, using: "
                            + typeName);
            }
    
            // loading all the features into memory to build an in-memory index.
            for (String type : typeNames) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("BBOXFilterExtractor::extractBasicProperties(): Looking for type \'"
                            + typeName + "\' in DataStore:getTypeNames(). Testing: \'" + type + "\'.");
                if (type.equalsIgnoreCase(typeName)) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine("BBOXFilterExtractor::extractBasicProperties(): SUCCESS -> type \'"
                                + typeName + "\' is equalsIgnoreCase() to \'" + type + "\'.");
                    this.typeName = type;
                    break;
                }
            }
    
            final SimpleFeatureSource featureSource = tileIndexStore
                    .getFeatureSource(this.typeName);
            if (featureSource != null)
                bounds = featureSource.getBounds();
            else
                throw new IOException(
                        "BBOXFilterExtractor::extractBasicProperties(): unable to get a featureSource for the qualified name"
                                + this.typeName);

            final FeatureType schema = featureSource.getSchema();
            if (schema != null) {
                geometryPropertyName = schema.getGeometryDescriptor().getLocalName();
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.fine("BBOXFilterExtractor::extractBasicProperties(): geometryPropertyName is set to \'"
                            + geometryPropertyName + "\'.");

            } else {
                throw new IOException(
                        "BBOXFilterExtractor::extractBasicProperties(): unable to get a schema from the featureSource");
            }
    
        }
		
	private final ReadWriteLock rwLock= new ReentrantReadWriteLock(true);

	/* (non-Javadoc)
	 * @see org.geotools.gce.imagemosaic.FeatureIndex#findFeatures(com.vividsolutions.jts.geom.Envelope)
	 */
	public List<GranuleDescriptor> getGranules(final BoundingBox envelope) throws IOException {
		Utilities.ensureNonNull("envelope",envelope);
		final Query q = new Query(typeName);
		Filter filter = ff.bbox( ff.property( geometryPropertyName ), ReferencedEnvelope.reference(envelope) );
		q.setFilter(filter);
	    return getGranules(q);	
		
	}
	
	/* (non-Javadoc)
	 * @see org.geotools.gce.imagemosaic.FeatureIndex#findFeatures(com.vividsolutions.jts.geom.Envelope, com.vividsolutions.jts.index.ItemVisitor)
	 */
	public void  getGranules(final BoundingBox envelope, final GranuleCatalogVisitor visitor) throws IOException {
		Utilities.ensureNonNull("envelope",envelope);
		final Query q = new Query(typeName);
		Filter filter = ff.bbox( ff.property( geometryPropertyName ), ReferencedEnvelope.reference(envelope) );
		q.setFilter(filter);
	    getGranules(q,visitor);			
		

	}

	public void dispose() {
		final Lock l=rwLock.writeLock();
		try{
			l.lock();
			try {
				if(tileIndexStore!=null)
					tileIndexStore.dispose();
			} catch (Throwable e) {
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			}
			finally{
				tileIndexStore=null;
			}	
						
		}finally{
			
			l.unlock();
		
		}
		
		
	}

	public int removeGranules(final Query query) {
		Utilities.ensureNonNull("query",query);
		final Lock lock=rwLock.writeLock();
		try{
			lock.lock();
			// check if the index has been cleared
			checkStore();		
			
			SimpleFeatureStore fs=null;
			try{
				// create a writer that appends this features
				fs = (SimpleFeatureStore) tileIndexStore.getFeatureSource(typeName);
				final int retVal=fs.getCount(query);
				fs.removeFeatures(query.getFilter());
				
				//update bounds
				bounds=tileIndexStore.getFeatureSource(typeName).getBounds();
				
				return retVal;
				
			}
			catch (Throwable e) {
				if(LOGGER.isLoggable(Level.SEVERE))
					LOGGER.log(Level.SEVERE,e.getLocalizedMessage(),e);
				return -1;
			}
			// do your thing
		}finally{
			lock.unlock();
		}			
	}

	public void addGranule(final SimpleFeature granule, final Transaction transaction) throws IOException {
		addGranules(Collections.singleton(granule),transaction);
	}
	
	public void addGranules(final Collection<SimpleFeature> granules, final Transaction transaction) throws IOException {
		Utilities.ensureNonNull("granuleMetadata",granules);
		final Lock lock=rwLock.writeLock();
		try{
			lock.lock();
			// check if the index has been cleared
			checkStore();
			
			
			FeatureWriter<SimpleFeatureType, SimpleFeature> fw =null;
			try{
				// create a writer that appends this features
				fw = tileIndexStore.getFeatureWriterAppend(typeName,transaction);

				//add them all
				for(SimpleFeature f:granules){
					
					// create a new feature
					final SimpleFeature feature = fw.next();
					
					// get attributes and copy them over
					for(int i=f.getAttributeCount()-1;i>=0;i--){
						Object attribute = f.getAttribute(i);
						
						
						// special case for postgis
						if(spi instanceof PostgisNGJNDIDataStoreFactory||spi instanceof PostgisNGDataStoreFactory)
						{
							final AttributeDescriptor descriptor = tileIndexStore.getSchema(typeName).getDescriptor(i);
							if(descriptor.getType().getBinding().equals(String.class))
							{
								// escape the string correctly
								attribute=((String) attribute).replace("\\", "\\\\");
							}

							if(descriptor.getType().getBinding().equals(Date.class))
							{
								// escape the date correctly
								Calendar cal = Calendar.getInstance(UTC_TZ);
								cal.setTime(((Date) attribute));
								attribute=cal.getTime();
							}

						}
						
						feature.setAttribute(i, attribute);
					}
					
					//write down
					fw.write();

				}
			}
			catch (Throwable e) {
				if(LOGGER.isLoggable(Level.SEVERE))
					LOGGER.log(Level.SEVERE,e.getLocalizedMessage(),e);
			}finally{
				if(fw!=null)
					fw.close();
			}
			
			// do your thing
			
			//update bounds
			bounds=tileIndexStore.getFeatureSource(typeName).getBounds(Query.ALL);
		}finally{
			lock.unlock();
		}	
		
	}

	public void  getGranules(final Query q, final GranuleCatalogVisitor visitor)
	throws IOException {
		Utilities.ensureNonNull("q",q);

		final Lock lock=rwLock.readLock();
		try{
			lock.lock();		
			checkStore();
			
			//
			// Load tiles informations, especially the bounds, which will be
			// reused
			//

			final SimpleFeatureSource featureSource = tileIndexStore.getFeatureSource(this.typeName);
			if (featureSource == null) 
				throw new NullPointerException(
						"The provided SimpleFeatureSource is null, it's impossible to create an index!");			
			final SimpleFeatureCollection features = featureSource.getFeatures( q );
			if (features == null) 
				throw new NullPointerException(
						"The provided SimpleFeatureCollection is null, it's impossible to create an index!");
	
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Index Loaded");
						
			
			//load the feature from the underlying datastore as needed
			final SimpleFeatureIterator it = features.features();
			try{
				if (!it.hasNext()) {
					if(LOGGER.isLoggable(Level.FINE))
						LOGGER.fine("The provided SimpleFeatureCollection  or empty, it's impossible to create an index!");
					return ;
						
				}	
			}finally{
				it.close();
			}
			
			final DefaultProgressListener listener= new DefaultProgressListener();
			features.accepts( new AbstractFeatureVisitor(){
			    public void visit( Feature feature ) {
			        if(feature instanceof SimpleFeature)
			        {
			        	final SimpleFeature sf= (SimpleFeature) feature;
						// create the granule descriptor
						final GranuleDescriptor granule= new GranuleDescriptor(
								sf,
								suggestedSPI,
								pathType,
								locationAttribute,
								parentLocation,
								heterogeneous);
			        	visitor.visit(granule, null);
			        	
			        	// check if something bad occurred
			        	if(listener.isCanceled()||listener.hasExceptions()){
			        	    if(listener.hasExceptions())
			        	        throw new RuntimeException(listener.getExceptions().peek());
			        	    else
			        	        throw new IllegalStateException("Feature visitor for query "+q+" has been canceled");
			        	}
			        }
			    }            
			}, listener);

		}
		catch (Throwable e) {
			final IOException ioe= new  IOException();
			ioe.initCause(e);
			throw ioe;
		}
		finally{
			lock.unlock();

		}
	}

	public List<GranuleDescriptor> getGranules(final Query q) throws IOException {
		Utilities.ensureNonNull("q",q);

		FeatureIterator<SimpleFeature> it=null;
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();		
			checkStore();
			
			//
			// Load tiles informations, especially the bounds, which will be
			// reused
			//
			final SimpleFeatureSource featureSource = tileIndexStore.getFeatureSource(this.typeName);
			if (featureSource == null) 
				throw new NullPointerException(
						"The provided SimpleFeatureSource is null, it's impossible to create an index!");			
			final SimpleFeatureCollection features = featureSource.getFeatures( q );
			if (features == null) 
				throw new NullPointerException(
						"The provided SimpleFeatureCollection is null, it's impossible to create an index!");
	
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.fine("Index Loaded");
						
			
			//load the feature from the underlying datastore as needed
			it = features.features();
			if (!it.hasNext()) {
				if(LOGGER.isLoggable(Level.FINE))
					LOGGER.fine("The provided SimpleFeatureCollection  or empty, it's impossible to create an index!");
				return Collections.emptyList();
					
			}
			
			// now build the index
			// TODO make it configurable as far the index is involved
			final ArrayList<GranuleDescriptor> retVal= new ArrayList<GranuleDescriptor>(features.size());
			while (it.hasNext()) {
				// get the feature
				final SimpleFeature sf = it.next();
				
				// create the granule descriptor
				final GranuleDescriptor granule= new GranuleDescriptor(
						sf,
						suggestedSPI,
						pathType,
						locationAttribute,
						parentLocation, 
						heterogeneous);
				retVal.add(granule);
			}
			return retVal;

		}
		catch (Throwable e) {
			throw new  IllegalArgumentException(e);
		}
		finally{
			lock.unlock();
			if(it!=null)
				// closing he iterator to free some resources.
    			it.close();

		}
	}

	public Collection<GranuleDescriptor> getGranules()throws IOException {
		return getGranules(getBounds());
	}

	public BoundingBox getBounds() {
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
				
			return bounds;
		}finally{
			lock.unlock();
		}
	}

	public void createType(String namespace, String typeName, String typeSpec) throws IOException, SchemaException {
		Utilities.ensureNonNull("typeName",typeName);
		Utilities.ensureNonNull("typeSpec",typeSpec);
		final Lock lock=rwLock.writeLock();
		try{
			lock.lock();
			checkStore();
			
			final SimpleFeatureType featureType= DataUtilities.createType(namespace, typeName, typeSpec);
			tileIndexStore.createSchema(featureType);
			extractBasicProperties(featureType.getTypeName());
		}finally{
			lock.unlock();
		}			

		
	}

	public void createType(SimpleFeatureType featureType) throws IOException {
		Utilities.ensureNonNull("featureType",featureType);
		final Lock lock=rwLock.writeLock();
		try{
			lock.lock();
			checkStore();

			tileIndexStore.createSchema(featureType);
			extractBasicProperties(featureType.getTypeName());
		}finally{
			lock.unlock();
		}				
		
	}

	public void createType(String identification, String typeSpec) throws SchemaException, IOException {
		Utilities.ensureNonNull("typeSpec",typeSpec);
		Utilities.ensureNonNull("identification",identification);
		final Lock lock=rwLock.writeLock();
		try{
			lock.lock();
			checkStore();
			final SimpleFeatureType featureType= DataUtilities.createType(identification, typeSpec);
			tileIndexStore.createSchema(featureType);
			extractBasicProperties(featureType.getTypeName());
		}finally{
			lock.unlock();
		}			
		
	}

	public SimpleFeatureType getType() throws IOException {
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			
			return tileIndexStore.getSchema(typeName);
		}finally{
			lock.unlock();
		}			
		
	}

	public void computeAggregateFunction(Query query, FeatureCalc function) throws IOException {
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			SimpleFeatureSource fs = tileIndexStore.getFeatureSource(query.getTypeName());
				
			if(fs instanceof ContentFeatureSource)
				((ContentFeatureSource)fs).accepts(query, function, null);
			else
			{
				final SimpleFeatureCollection collection = fs.getFeatures(query);
				collection.accepts(function, null);
				
			}
		}finally{
			lock.unlock();
		}		
		
	}

	public QueryCapabilities getQueryCapabilities() {
		final Lock lock=rwLock.readLock();
		try{
			lock.lock();
			checkStore();
			
			return tileIndexStore.getFeatureSource(typeName).getQueryCapabilities();
		} catch (IOException e) {
			if(LOGGER.isLoggable(Level.INFO))
				LOGGER.log(Level.INFO,"Unable to collect QueryCapabilities",e);
			return null;
		}finally{
			lock.unlock();
		}	
	}

}

