package org.geotools.api;

import java.net.URI;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.cql2.CQL;
import org.opengis.feature.simple.SimpleFeatureType;

public class DataStoreExamples {

DataStore dataStore = null;
SimpleFeatureSource featureSource = null;

void exampleInfo() {
    // exampleInfo start
    ServiceInfo info = dataStore.getInfo();
    
    // Human readable name and description
    String title = info.getTitle();
    String text = info.getDescription();
    
    // keywords (dublin core keywords like a web page)
    Set<String> keywords = info.getKeywords();
    
    // formal metadata
    URI publisher = info.getPublisher(); // authority publishing data
    URI schema = info.getSchema(); // used for data conforming to a standard
    URI source = info.getSource(); // location where information is published from
    
    // exampleInfo end
}

void exampleCreateSchema() throws Exception {
    // exampleCreateSchema start
    SimpleFeatureType schema  = DataUtilities.createType("LINE", "centerline:LineString,name:\"\",id:0");

    dataStore.createSchema( schema );
    // exampleCreateSchema end
    
}

void exampleAllCount() throws Exception {
    // all start
    int count = featureSource.getCount( Query.ALL );
    if( count == -1 ){
        count = featureSource.getFeatures().size();
    }
    // all end
}

void exampleQueryCount() throws Exception {
    // count start
    Query query = new Query( "typeName", CQL.toFilter("REGION = 3") );
    int count = featureSource.getCount( query );
    if( count == -1 ){
        count = featureSource.getFeatures( query ).size();
    }
    // count end
}

}
