/*
 *    GeoTools - The Open Source Java GIS Tookit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This file is hereby placed into the Public Domain. This means anyone is
 *    free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.demo.example;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.Identifier;
import org.opengis.filter.spatial.Intersects;

import com.vividsolutions.jts.geom.Envelope;

public class WFSExample {
	/**
	 * Before running this application please install and start geoserver on your local machine.
	 * @param args
	 */
	public static void main( String[] args ){
		String getCapabilities =
			"http://localhost:8080/geoserver/wfs?service=WFS&request=GetCapabilities";
		if( args.length != 0 ){
			getCapabilities = args[0];
		}		
		try {
			supressInfo();
			dataAccess( getCapabilities );
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static void supressInfo(){
		org.geotools.util.logging.Logging.getLogger("org.geotools.gml").setLevel( Level.SEVERE );
		org.geotools.util.logging.Logging.getLogger("net.refractions.xml").setLevel( Level.SEVERE);
	}
	
	public static void dataAccess( String getCapabilities ) throws Exception {
		// Step 1 - connection parameters
		//
		Map connectionParameters = new HashMap();
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities );
		
		// Step 2 - connection
		DataStore data = DataStoreFinder.getDataStore( connectionParameters );
		
		// Step 3 - discouvery
		String typeNames[] = data.getTypeNames();
		String typeName = typeNames[0];
		SimpleFeatureType schema = data.getSchema( typeName );
		System.out.println( "Schema Attributes:"+schema.getAttributeCount() );
		
		// Step 4 - target
		SimpleFeatureSource source = data.getFeatureSource( typeName );
		System.out.println( "Metadata Bounds:"+ source.getBounds() );

		// Step 5 - query
		String geomName = schema.getGeometryDescriptor().getLocalName();
		Envelope bbox = new Envelope( -100.0, -70, 25, 40 );
		
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( GeoTools.getDefaultHints() );
		Object polygon = JTS.toGeometry( bbox );
        Intersects filter = ff.intersects( ff.property( geomName ), ff.literal( polygon ) );
		
		Query query = new DefaultQuery( typeName, filter, new String[]{ geomName } );
		SimpleFeatureCollection features = source.getFeatures( query );

		ReferencedEnvelope bounds = new ReferencedEnvelope();
		Iterator<SimpleFeature> iterator = features.iterator();
		try {
			while( iterator.hasNext() ){
				Feature feature = (Feature) iterator.next();				
				bounds.include( feature.getBounds() );
			}
			System.out.println( "Calculated Bounds:"+ bounds );
		}
		finally {
			features.close( iterator );
		}
	}

	public static void dataUpdate( String getCapabilities ) throws Exception {
		// Step 1 - connection parameters
		//
		Map connectionParameters = new HashMap();
		connectionParameters.put("WFSDataStoreFactory:GET_CAPABILITIES_URL", getCapabilities );
		
		// Step 2 - connection
		DataStore data = DataStoreFinder.getDataStore( connectionParameters );
		
		// Step 3 - discouvery
		String typeNames[] = data.getTypeNames();
		String typeName = typeNames[0];
		SimpleFeatureType schema = data.getSchema( typeName );
		System.out.println( "Schema Attributes:"+schema.getAttributeCount() );
		
		// Step 4 - target
		SimpleFeatureSource source = data.getFeatureSource( typeName );
		System.out.println( "Metadata Bounds:"+ source.getBounds() );

		// Step 5 - query
		FilterFactory ff = CommonFactoryFinder.getFilterFactory( GeoTools.getDefaultHints() );
		
		DefaultQuery query = new DefaultQuery( typeName, Filter.INCLUDE );
		query.setMaxFeatures(2);
		SimpleFeatureCollection features = source.getFeatures( query );

		String fid = null;
		Iterator<SimpleFeature> iterator = features.iterator();
		try {
			while( iterator.hasNext() ){
				SimpleFeature feature = (SimpleFeature) iterator.next();
				fid = feature.getID();
			}
		}
		finally {
			features.close( iterator );
		}
		// step 6 modify
		Transaction t = new DefaultTransaction();

		SimpleFeatureStore store = (SimpleFeatureStore) source;
		store.setTransaction( t );
		Set<Identifier> ids = new HashSet<Identifier>();
		ids.add( ff.featureId(fid) );
		Filter filter = ff.id( ids );
		try {
			store.removeFeatures( filter );
		}
		finally {
			t.rollback();
		}
	}

}
