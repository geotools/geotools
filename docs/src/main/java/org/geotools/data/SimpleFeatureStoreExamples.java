package org.geotools.data;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

@SuppressWarnings("unused")
public class SimpleFeatureStoreExamples {
DataStore dataStore = null;

String typeName;

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
