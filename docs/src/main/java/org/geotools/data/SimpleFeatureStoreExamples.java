package org.geotools.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryBuilder;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

@SuppressWarnings("unused")
public class SimpleFeatureStoreExamples {
DataStore dataStore = null;

String typeName;

private void addFeaturesExample() throws Exception {
    // addExample start
    SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource( typeName );
    
    SimpleFeatureType featureType = store.getSchema();
    
    SimpleFeatureBuilder build = new SimpleFeatureBuilder(featureType);
    GeometryBuilder geom = new GeometryBuilder();
    
    List<SimpleFeature> list = new ArrayList<SimpleFeature>();
    list.add( build.buildFeature("fid1", new Object[]{ geom.point(1,1), "hello" } ) );
    list.add( build.buildFeature("fid2", new Object[]{ geom.point(2,3), "martin" } ) );
    SimpleFeatureCollection collection = new ListFeatureCollection(featureType, list);

    Transaction transaction = new DefaultTransaction("Add Example");
    store.setTransaction( transaction );
    try {
        store.addFeatures( collection );
        transaction.commit(); // actually writes out the features in one go
    }
    catch( Exception eek){
        transaction.rollback();
    }
    // addExample end
}

private void addFeatureIdExample(SimpleFeatureCollection collection ) throws Exception {
    // addFeatureIdExample start
    Transaction transaction = new DefaultTransaction("Add Example");
    SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
    store.setTransaction(transaction);
    try {
        List<FeatureId> added = store.addFeatures( collection );
        System.out.println( added ); // prints out the temporary feature ids
        
        transaction.commit();
        System.out.println( added ); // prints out the final feature ids
        
        Set<FeatureId> selection = new HashSet<FeatureId>( added );
        FilterFactory ff = CommonFactoryFinder.getFilterFactory();
        Filter selected = ff.id( selection ); // filter selecting all the features just added
    }
    catch( Exception problem){
        transaction.rollback();
        throw problem;
    }
    // addFeatureIdExample end
}

private void addFeaturesEvents(SimpleFeatureCollection collection ) throws Exception {
    // addEvents start
    Transaction transaction = new DefaultTransaction("Add Example");
    SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
    store.setTransaction(transaction);

    class CommitListener implements FeatureListener {
        public void changed(FeatureEvent featureEvent) {
            if (featureEvent instanceof BatchFeatureEvent ){
                BatchFeatureEvent batchEvent = (BatchFeatureEvent) featureEvent;
                
                System.out.println( "area changed:" + batchEvent.getBounds() );
                System.out.println( "created fids:" + batchEvent.fids );
            }
            else {
                System.out.println( "bounds:" + featureEvent.getBounds() );
                System.out.println( "change:" + featureEvent.filter );
            }
        }
    }
    CommitListener listener = new CommitListener();
    store.addFeatureListener( listener );
    try {
        List<FeatureId> added = store.addFeatures( collection );
        transaction.commit();
    }
    catch( Exception problem){
        transaction.rollback();
        throw problem;
    }
    // addEvents end
}
private void removeExample() throws Exception {
    // removeExample start
    Transaction transaction = new DefaultTransaction("removeExample");
    SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
    store.setTransaction(transaction);
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
    Filter filter = ff.id(Collections.singleton(ff.featureId("fred")));
    try {
        store.removeFeatures(filter);
        transaction.commit();
    } catch (Exception eek) {
        transaction.rollback();
    }
    // removeExample end
}
private void removeExample2() throws Exception {
    // removeExample2 start
    Transaction transaction = new DefaultTransaction("removeExample");
    SimpleFeatureStore store = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
    store.setTransaction(transaction);
    
    FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
    Filter filter = ff.id(Collections.singleton(ff.featureId("fred")));
    try {
        final Set<FeatureId> removed = new HashSet<FeatureId>();
        SimpleFeatureCollection collection = store.getFeatures( new Query( typeName, filter, Query.NO_NAMES ));
        collection.accepts( new FeatureVisitor(){
            public void visit(Feature feature) {
                removed.add( feature.getIdentifier() );
            }
        }, null );
        store.removeFeatures(filter);
        transaction.commit();
    } catch (Exception eek) {
        transaction.rollback();
    }
    // removeExample2 end
}


}
