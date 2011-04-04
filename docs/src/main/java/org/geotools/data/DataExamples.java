package org.geotools.data;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;

public class DataExamples {

// alter start
SimpleFeatureSource alter(SimpleFeatureCollection collection, String typename,
        SimpleFeatureType featureType, final List<AttributeDescriptor> newTypes) {
    
    try {
        
        // Create target schema
        SimpleFeatureTypeBuilder buildType = new SimpleFeatureTypeBuilder();
        buildType.init(featureType);
        buildType.setName(typename);
        buildType.addAll(newTypes);
        
        final SimpleFeatureType schema = buildType.buildFeatureType();
        // Configure memory datastore
        final MemoryDataStore memory = new MemoryDataStore();
        memory.createSchema(schema);
        
        collection.accepts(new FeatureVisitor() {
            public void visit(Feature feature) {
                SimpleFeatureBuilder builder = new SimpleFeatureBuilder(schema);
                
                builder.init((SimpleFeature) feature);
                for (AttributeDescriptor descriptor : newTypes) {
                    builder.add(DataUtilities.defaultValue(descriptor));
                }
                
                SimpleFeature newFeature = builder.buildFeature(feature.getIdentifier().getID());
                memory.addFeature(newFeature);
            }
        }, null);
        
        return memory.getFeatureSource(typename);
        
    } catch (Exception e) {
        e.printStackTrace();
    }
    return null;
}

// alter end

// exportToShapefile start
DataStore exportToShapefile(MemoryDataStore memory, String typeName, File directory)
        throws IOException {
    // existing feature source from MemoryDataStore
    SimpleFeatureSource featureSource = memory.getFeatureSource(typeName);
    SimpleFeatureType ft = featureSource.getSchema();
    
    String fileName = ft.getTypeName();
    File file = new File(directory, fileName + ".shp");
    
    Map<String, java.io.Serializable> creationParams = new HashMap<String, java.io.Serializable>();
    creationParams.put("url", DataUtilities.fileToURL(file));
    
    FileDataStoreFactorySpi factory = FileDataStoreFinder.getDataStoreFactory("shp");
    DataStore dataStore = factory.createNewDataStore(creationParams);
    
    dataStore.createSchema(ft);
    
    // The following workaround to write out the prj is no longer needed
    // ((ShapefileDataStore)dataStore).forceSchemaCRS(ft.getCoordinateReferenceSystem());
    
    SimpleFeatureStore featureStore = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
    
    Transaction t = new DefaultTransaction();
    try {
        SimpleFeatureCollection collection = featureSource.getFeatures(); // grab all features
        featureStore.addFeatures(collection);
        t.commit(); // write it out
    } catch (IOException eek) {
        eek.printStackTrace();
        try {
            t.rollback();
        } catch (IOException doubleEeek) {
            // rollback failed?
        }
    } finally {
        t.close();
    }
    return dataStore;
}

// exportToShapefile end

private void fixWFSTypeName(SimpleFeatureType ft) {
    // fixWFSTypeName start
    StringBuilder builder = new StringBuilder(ft.getTypeName());
    for (int i = 0; i < builder.length(); i++) {
        if (!Character.isLetterOrDigit(builder.charAt(i))) {
            builder.setCharAt(i, '_');
        }
    }
    String fileName = builder.toString();
    // fixWFSTypeName end
}

// exportToShapefile2 start
DataStore exportToShapefile2(MemoryDataStore memory, String typeName, File directory)
        throws IOException {
    // existing feature source from MemoryDataStore
    SimpleFeatureSource featureSource = memory.getFeatureSource(typeName);
    SimpleFeatureType ft = featureSource.getSchema();
    
    String fileName = ft.getTypeName();
    File file = new File(directory, fileName + ".shp");
    
    Map<String, java.io.Serializable> creationParams = new HashMap<String, java.io.Serializable>();
    creationParams.put("url", DataUtilities.fileToURL(file));
    
    FileDataStoreFactorySpi factory = FileDataStoreFinder.getDataStoreFactory("shp");
    DataStore dataStore = factory.createNewDataStore(creationParams);
    
    dataStore.createSchema(ft);
    
    SimpleFeatureStore featureStore = (SimpleFeatureStore) dataStore.getFeatureSource(typeName);
    
    Transaction t = new DefaultTransaction();
    try {
        SimpleFeatureCollection collection = featureSource.getFeatures(); // grab all features
        
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = dataStore.getFeatureWriter(
                typeName, t);
        
        SimpleFeatureIterator iterator = collection.features();
        SimpleFeature feature;
        try {
            while (iterator.hasNext()) {
                feature = iterator.next();
                
                // Step1: create a new empty feature on each call to next
                SimpleFeature aNewFeature = (SimpleFeature) writer.next();
                // Step2: copy the values in
                aNewFeature.setAttributes(feature.getAttributes());
                // Step3: write out the feature
                writer.write();
            }
            
        } catch (IOException eek) {
            eek.printStackTrace();
            try {
                t.rollback();
            } catch (IOException doubleEeek) {
                // rollback failed?
            }
        }
    } finally {
        t.close();
    }
    return dataStore;
}

// exportToShapefile2 end
}
