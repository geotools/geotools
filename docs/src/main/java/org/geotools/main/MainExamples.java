package org.geotools.main;

import org.geotools.data.DataUtilities;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Main examples used for sphinx documentation.-+*
 * 
 * @author Jody Garnett
 */
public class MainExamples {

void exampleDataUtilities() throws Exception {
    FeatureCollection<SimpleFeatureType, SimpleFeature> collection = null;
    
    // exampleDataUtilities start
    SimpleFeatureCollection features = DataUtilities.simple(collection);
    // exampleDataUtilities end
}

// exampleRetype start
void exampleRetype() throws Exception {
    SimpleFeatureType origional = DataUtilities.createType("LINE", "centerline:LineString,name:\"\",id:0");
    SimpleFeatureType modified = DataUtilities.createSubType(origional, new String[]{"centerline"});
    
    SimpleFeature feature = DataUtilities.template( origional );
    
    SimpleFeature changed = DataUtilities.reType( modified, feature);
}
// exampleRetype end

void exampleIterator() throws Exception {
    SimpleFeatureCollection featureCollection = null;
    // exampleIterator start
    SimpleFeatureIterator iterator = featureCollection.features();
    try {
        while( iterator.hasNext() ){
            SimpleFeature feature = iterator.next();
            // process feature
        }
    }
    finally {
        iterator.close();
    }
    // exampleIterator end
}
}
