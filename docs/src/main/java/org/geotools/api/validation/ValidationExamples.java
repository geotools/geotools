package org.geotools.api.validation;

import java.io.File;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.validation.DefaultFeatureResults;
import org.geotools.validation.ValidationProcessor;
import org.geotools.validation.spatial.IsValidGeometryValidation;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

@SuppressWarnings("unused")
public class ValidationExamples {

private void validationExample() throws Exception{
    SimpleFeatureCollection roadFeatures = null;
    SimpleFeatureCollection riverFeatures = null;

    // validationExample start
    MemoryDataStore store;
    store = new MemoryDataStore();
    store.addFeatures( roadFeatures );
    store.addFeatures( riverFeatures );
    
    //
    // SETUP
    //
    ValidationProcessor processor = new ValidationProcessor(); 

    // normally you load definition from file
    // Here we are doing it by hand    
    IsValidGeometryValidation geom = new IsValidGeometryValidation();
    geom.setName( "IsValidGeometry");
    geom.setDescription("IsValid geomtry check");
    geom.setTypeRef("*"); // works on any feature type
    processor.addValidation( geom );

    //
    // TESTING
    //
    
    // Create a ValidationResults callback object to receive 
    // any warnings or errors
    //
    // Normally you implement this as a callback to your application;
    // here we will use a default implementation here that adds results to a list
    DefaultFeatureResults results = new DefaultFeatureResults();

    // test a featureCollection 
    processor.runFeatureTests( "dataStoreId", roadFeatures, results);

    // and check the results
    System.out.println("Found "+ results.error.size() + " failires" );
    // validationExample end
}

private void validationExample2() throws Exception {
    File pluginDirectory = null;
    File testSuiteDirectory = null;
    SimpleFeatureSource lakesFeatureSource = null;
    SimpleFeatureSource streamsFeatureSource = null;

    // validationExample2 start

    // set up a validation processor using two directories of
    // configuraiton files
    ValidationProcessor processor = new ValidationProcessor(); 
    processor.load( pluginDirectory, testSuiteDirectory );

    // normally you load definition from file
    // it will load all the files in the provided directories
    processor.load( pluginDirectory, testSuiteDirectory );

    DefaultFeatureResults results = new DefaultFeatureResults();

        // To run integrity tests (that compare several featureSources we need
    // to make a Map of FeatureSources.
    // the *key* is called the "typeRef" and will be used by test suites
    // to refer to look up a featureSource as needed
    Map<String,SimpleFeatureSource> map = new HashMap<String,SimpleFeatureSource>();
    
    // register in map so validation processor can find it
    map.put( "LAKES:lakes",  lakesFeatureSource );
    map.put( "STREAMS:streams", streamsFeatureSource );
    // optional shortlist of layers to check (these are usually the layers your
    // user modified)
    Set<Name> check = new HashSet<Name>();
    check.add( new NameImpl("LAKES:lakes"));
    
    processor.runIntegrityTests( check, map, null, results);

    // and check the results
    System.out.println("Found "+ results.error.size() + " failires" );
    // validationExample2 end
}
}
