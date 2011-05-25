/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.validation;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DefaultRepository;
import org.geotools.data.FeatureSource;
import org.geotools.data.Repository;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.Name;

/**
 * Validator<br>
 * @author bowens<br>
 * Created Jun 25, 2004<br>
 *
 * @source $URL$
 * @version <br>
 * 
 * <b>Puropse:</b><br>
 * <p>
 * Provides access to feature validation and integrity validation.<br>
 * 
 * 
 * </p>
 * 
 * <b>Description:</b><br>
 * <p>
 * Feature validation will iterate over a feature features and test all features
 * that have a validation test specific to them in the validation processor
 * passed in.<br>
 * 
 * Integrity validation will iterate over all data stores passed in through the
 * stores map and run the tests associated with each store.
 * </p>
 * 
 * <b>Usage:</b><br>
 * <p>
 * DOCUMENT ME!!
 * </p>
 */
public class Validator
{
	/** Standard logging instance for class */
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
			"org.vfny.geoserver.responses");
	
	private ValidationProcessor validationProcessor;
	
	private DefaultRepository repository;
	
	
	
	
	/**
	 * Validator Constructor
	 * 
	 */
	public Validator(Repository repository, ValidationProcessor processor)
	{
		this.repository = (DefaultRepository) repository;
		validationProcessor = processor;//new ValidationProcessor();
	}

	
	/**
	 * <b>featureValidation Purpose:</b> <br>
	 * <p>
	 * DOCUMENT ME!!
	 * </p>
	 * 
	 * <b>Description:</b><br>
	 * <p>
	 * Feature validation will iterate over a feature features and test all features
	 * that have a validation test specific to them in the validation processor
	 * passed in.
	 * </p>
	 * 
	 * Author: bowens<br>
	 * Created on: Jun 25, 2004<br>
	 * @param dsid
	 * @param features
	 * @throws IOException
	 * @throws Exception
	 */
	public void featureValidation(String dsid, SimpleFeatureCollection features, ValidationResults results )
		throws IOException, Exception//, WfsTransactionException 
	{
		//LOGGER.finer("FeatureValidation called on "+dsid+":"+type.getTypeName() ); 
    
		if (validationProcessor == null)
		{
			LOGGER.warning("ValidationProcessor unavailable");
			return;
		}
		
		/** Set up our validation results */
		try {
			validationProcessor.runFeatureTests(dsid, features, results);
		} catch (Exception badIdea) {
			// ValidationResults should have handled stuff, will redesign :-)
			throw new DataSourceException("Validation Failed", badIdea);
		}
	}



	/**
	 * <b>getFeatureValidationResults Purpose:</b> <br>
	 * <p>
	 * DOCUMENT ME!!
	 * </p>
	 * 
	 * <b>Description:</b><br>
	 * <p>
	 * DOCUMENT ME!!
	 * </p>
	 * 
	 * Author: bowens<br>
	 * Created on: Jun 26, 2004<br>
	 * @param failed
	 */
	private ValidationResults makeFeatureValidationResults(final Map failed)
	{
		ValidationResults results = new ValidationResults() 
		{
				String name;
				String description;
				public void setValidation(Validation validation) {
					name = validation.getName();
					description = validation.getDescription();
				}                
				public void error(SimpleFeature feature, String message) {
					LOGGER.warning(name + ": " + message + " (" + description
						+ ")");
					failed.put(feature.getID(),
						name + ": " + message + " " + "(" + description + ")");
				}
				public void warning(SimpleFeature feature, String message) {
					LOGGER.warning(name + ": " + message + " (" + description
						+ ")");
				}
		};
		return results;
	}


	/**
	 * Integrity validation will iterate over all data stores passed in through the
 	 * stores map and run the tests associated with each store.
	 * </p>
	 * 
	 * Author: bowens<br>
	 * Created on: Jun 26, 2004<br>
	 * @param featureStores Map of required FeatureStores by typeRef (dataStoreId:typeName)
	 * @param bBox
	 * @throws IOException
	 * @throws Exception
	 */
	public void integrityValidation(Map<Name,FeatureSource<?,?>> featureStores, ReferencedEnvelope bBox, ValidationResults results )
		throws IOException, Exception// WfsTransactionException 
	{
		//Data catalog = request.getWFS().getData();
		//ValidationProcessor validation = request.getValidationProcessor();
		if( validationProcessor == null ) {
			LOGGER.warning( "Validation Processor unavaialble" );
			return;
		}
		LOGGER.finer( "Required to validate "+featureStores.size()+" typeRefs" );
		LOGGER.finer( "within "+bBox );
		
		// go through each typeName passed in through stores
		// and ask what we need to check
		Set typeRefs = new HashSet();
		for( Name name : featureStores.keySet() ){
			String typeRef = typeRef( name );
			typeRefs.add( typeRef );
        
			Set dependencies = validationProcessor.getDependencies( typeRef );
			LOGGER.finer( "typeRef "+typeRef+" requires "+dependencies);
			if (dependencies != null && dependencies.size() > 0)
				typeRefs.addAll( dependencies ); 
		}

		// Grab a source for each typeName we need to check
		// Grab from the provided stores - so we check against
		// the transaction 
		//
		Map sources = new HashMap();

		for (Iterator i = typeRefs.iterator(); i.hasNext();) {
			String typeRef = (String) i.next();
			LOGGER.finer("Searching for required typeRef: " + typeRef );
			
			/** This checks to see if we have already loaded in any feature stores.
			 * They can be loaded already if we are in a transaction operation. If
			 * this is for the "do it" button, there will be no feature stores
			 * already loaded and thus we always hit the 'else' statement.*/
			if (featureStores.containsKey( typeRef )) // if it was passed in through stores
			{
				LOGGER.finer(" found required typeRef: " + typeRef +" (it was already loaded)");                
				sources.put( typeRef, featureStores.get(typeRef));
			} 
			else // if we have to go get it (ie. it is a dependant for a test)
			{
				// These will be using Transaction.AUTO_COMMIT
				// this is okay as they were not involved in our
				// Transaction...
				LOGGER.finer(" could not find typeRef: " + typeRef +" (we will now load it)");
				String split[] = typeRef.split(":");
				String dataStoreId = split[0];
				String typeName = split[1];
				LOGGER.finer(" going to look for dataStoreId:"+dataStoreId+" and typeName:"+typeName );                
            	
				// FeatureTypeInfo meta = catalog.getFeatureTypeInfo(typeName);
				LOGGER.finer(" loaded required typeRef: " + typeRef );				
				FeatureSource<?,?> source = repository.source( dataStoreId, typeName );				                
				sources.put( typeRef, source );                                                
			}
		}
		LOGGER.finer( "Total of "+sources.size()+" SimpleFeatureSource marshalled for testing" );
		final Map failed = new TreeMap();
		
		try {
			//should never be null, but confDemo is giving grief, and I 
			//don't want transactions to mess up just because validation 
			//stuff is messed up. ch
			LOGGER.finer("Runing integrity tests using validation validationProcessor ");
			validationProcessor.runIntegrityTests(featureStores.keySet(), sources, bBox, results);        	
		} 
		catch (Exception badIdea) 
		{
			badIdea.printStackTrace();
			// ValidationResults should of handled stuff will redesign :-)
			throw new DataSourceException("Validation Failed", badIdea);
		}
		if (failed.isEmpty()) 
		{
			LOGGER.finer( "All validation tests passed" );            
			return; // everything worked out
		}
		
		/** One or more of the tests failed if we reached here, so dump out
		 * the information from teh tests that failed. */
		LOGGER.finer( "Validation fail - marshal result for transaction document" );
		StringBuffer message = new StringBuffer();
		for (Iterator i = failed.entrySet().iterator(); i.hasNext();) 
		{
			Map.Entry entry = (Map.Entry) i.next();
			message.append(entry.getKey());
			message.append(" failed test ");
			message.append(entry.getValue());
			message.append("\n");
		}		
		//throw new Exception("Validation - " + message.toString());
		//throw new WfsTransactionException(message.toString(), "validation");
	}

	    /** Convert a Name to a type reference (namespace ":" name) */
	    protected String typeRef( Name name ){
	        return name.getNamespaceURI()+":"+name.getLocalPart();
	    }

	/**
	 * <b>getIntegrityValidationResults Purpose:</b> <br>
	 * <p>
	 * Gets the validation results for Integrity tests.
	 * </p>
	 * 
	 * <b>Description:</b><br>
	 * <p>
	 * DOCUMENT ME!!
	 * </p>
	 * 
	 * Author: bowens<br>
	 * Created on: Jun 26, 2004<br>
	 * @param failed the map of failed features
	 */
	private ValidationResults makeIntegrityValidationResults(final Map failed)
	{
		ValidationResults results = new ValidationResults() 
		{
				String name;
				String description;
		
				public void setValidation(Validation validation) {
					name = validation.getName();
					description = validation.getDescription();
				}
		
				public void error(SimpleFeature feature, String message) {
					LOGGER.warning(name + ": " + message + " (" + description
						+ ")");
					if (feature == null) {
						failed.put("ALL",
								name + ": " + message + " " + "(" + description + ")");                        
					} else {
					failed.put(feature.getID(),
						name + ": " + message + " " + "(" + description + ")");
					}
				}
		
				public void warning(SimpleFeature feature, String message) {
					LOGGER.warning(name + ": " + message + " (" + description
						+ ")");
				}
		};
		return results;
	}
	
}
