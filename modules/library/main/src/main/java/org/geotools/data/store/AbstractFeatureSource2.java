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
package org.geotools.data.store;

import java.io.IOException;
import java.util.Collections;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public abstract class AbstractFeatureSource2 implements FeatureSource<SimpleFeatureType, SimpleFeature> {

	/** The logger for the data module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data");
    
    /**
     * the type entry
     */
	protected ActiveTypeEntry entry;
	
	public AbstractFeatureSource2( ActiveTypeEntry entry ) {
		this.entry = entry;
	}
	
	public DataStore getDataStore() {
		return entry.parent;
	}

	public void addFeatureListener(FeatureListener listener) {
		entry.listenerManager.addFeatureListener( this, listener );
	}

	public void removeFeatureListener(FeatureListener listener) {
		entry.listenerManager.removeFeatureListener( this, listener );
	}

	public FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatures(Query query) throws IOException {
		 SimpleFeatureType featureType = entry.getFeatureType();
		 
		 Filter filter = query.getFilter();
		 if (filter == null) {
            throw new NullPointerException("getFeatureReader requires Filter: "
                + "did you mean Filter.INCLUDE?");
         }
         String propertyNames[] = query.getPropertyNames();
         
         if ( filter == Filter.EXCLUDE || filter.equals( Filter.EXCLUDE )) {
             //return new EmptyFeatureReader(featureType);
             return new EmptyFeatureCollection( featureType );
         }
         //GR: allow subclases to implement as much filtering as they can,
         //by returning just it's unsupperted filter
//         filter = getUnsupportedFilter( filter);
//         if(filter == null){
//             throw new NullPointerException("getUnsupportedFilter shouldn't return null. Do you mean Filter.INCLUDE?");
//         }
         
         //filter
         FeatureCollection<SimpleFeatureType, SimpleFeature> features = getFeatures( filter );
         
         //retyping
         if( propertyNames != null || query.getCoordinateSystem() != null ){
             try {
                 SimpleFeatureType target = 
                	 DataUtilities.createSubType( featureType, propertyNames, query.getCoordinateSystem() );
                 if ( !featureType.equals( target ) ) {
                	 LOGGER.fine("Recasting feature type to subtype by using a ReTypeFeatureReader");
                	 features = new ReTypingFeatureCollection(features, target); 
                 }
             } catch (SchemaException e) {
                 LOGGER.log( Level.FINEST, e.getMessage(), e);
                 throw new DataSourceException( "Could not create Feature Type for query", e );

             }
         }
       
         //reprojection 
         if ( query.getCoordinateSystemReproject() != null ) {
        	 if ( query.getCoordinateSystem() != null ) {
        		 features = reproject( features, query.getCoordinateSystem(), query.getCoordinateSystemReproject() );
        	 }
        	 else {
        		 features = new ReprojectingFeatureCollection( 
        			features, query.getCoordinateSystemReproject()	 
        		 );
        	}
         }
         
         //max feature cap
         if (query.getMaxFeatures() != Query.DEFAULT_MAX) {
             features = new MaxFeaturesFeatureCollection<SimpleFeatureType, SimpleFeature>( features, query.getMaxFeatures() );
         }
         
         return features;
	}

	public FeatureCollection<SimpleFeatureType, SimpleFeature> getFeatures(Filter filter) throws IOException {
        //filter
        if ( filter != null && !filter.equals( Filter.INCLUDE ) ) {
            return new FilteringFeatureCollection<SimpleFeatureType, SimpleFeature>( getFeatures() , filter );
        }
        
        return getFeatures();
	}

	public SimpleFeatureType getSchema() {
		return entry.getFeatureType();
	}

	public ReferencedEnvelope getBounds() throws IOException {
		return getFeatures().getBounds();
	}

	public ReferencedEnvelope getBounds(Query query) throws IOException {
		return getFeatures( query ).getBounds();
	}

	public int getCount(Query query) throws IOException {
		return getFeatures( query ).size();
	}

	/**
	 * Template method for reprojecting a feature collection from a source crs to a target crs.
	 * <p>
	 * Subclasses may override, the default implementation wraps <param>features</param> in a 
	 * {@link ReprojectingFeatureCollection}.
	 * </p>
	 * @param features The feature collection to be reprojected.
	 * 
	 * @return the reprojected feature collection.
	 */
	protected FeatureCollection<SimpleFeatureType, SimpleFeature> reproject( FeatureCollection<SimpleFeatureType, SimpleFeature> features, CoordinateReferenceSystem source, CoordinateReferenceSystem target ) {
		return new ReprojectingFeatureCollection( features, source, target );
	}
	
	/**
	* By default, no Hints are supported
	*/
	public Set getSupportedHints() {
	       return Collections.EMPTY_SET;
	}
}
