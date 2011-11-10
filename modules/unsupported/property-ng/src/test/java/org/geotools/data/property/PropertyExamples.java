package org.geotools.data.property;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.Geometry;

/**
 * 
 *
 * @source $URL$
 */
public class PropertyExamples {

    static File directory;

    public static void main(String[] args) {
        File tmp = null;
        try {
            tmp = File.createTempFile("example", "");
            boolean exists = tmp.exists();
            if (exists) {
                System.err.println("Removing tempfile " + tmp);
                tmp.delete();
            }
            boolean created = tmp.mkdirs();
            if (!created) {
                System.err.println("Could not create " + tmp);
                System.exit(1);
            }
            File example = new File(tmp, "example.properties");

            BufferedWriter writer = new BufferedWriter(new FileWriter(example));            
            writer.write("_=id:Integer,name:String,geom:Point");
            writer.newLine();
            writer.write("fid1=1|jody garnett|POINT(0 0)");
            writer.newLine();
            writer.write("fid2=2|brent|POINT(10 10)");
            writer.newLine();
            writer.write("fid3=3|dave|POINT(20 20)");
            writer.newLine();
            writer.write("fid4=4|justin deolivera|POINT(30 30)");
            writer.newLine();
            writer.close();

            directory = tmp;
            try {
                featureStoreExample();
                transactionExample();
                removeAllExample();
                appendContent();
            } catch (Throwable t) {
                t.printStackTrace();
                System.exit(1);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            File list[] = tmp.listFiles();
            for (int i = 0; i < list.length; i++) {
                list[i].delete();
                System.out.println("remove " + list[i]);
            }
            tmp.delete();
            System.out.println("remove " + tmp);
        }
    }

    private static void featureStoreExample() throws Exception {
        System.out.println("featureStoreExample start\n");
        // featureStoreExample start
        PropertyDataStore datastore = new PropertyDataStore(directory);
        SimpleFeatureSource featureSource = datastore
                .getFeatureSource("example");
        if (!(featureSource instanceof SimpleFeatureStore)) {
            throw new IllegalStateException("Modification not supported");
        }
        SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;

        // featureStoreExample end
        System.out.println("\nfeatureStoreExample end\n");
    }

    private static void transactionExample() throws Exception {
        System.out.println("transactionExample start\n");
        // transactionExample start
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("directory", directory);
        DataStore store = DataStoreFinder.getDataStore(params);

        Transaction t1 = new DefaultTransaction("transaction 1");
        Transaction t2 = new DefaultTransaction("transactoin 2");

        SimpleFeatureType type = store.getSchema("example");
        SimpleFeatureStore featureStore = (SimpleFeatureStore) store
                .getFeatureSource("example");
        SimpleFeatureStore featureStore1 = (SimpleFeatureStore) store
                .getFeatureSource("example");
        SimpleFeatureStore featureStore2 = (SimpleFeatureStore) store
                .getFeatureSource("example");

        featureStore1.setTransaction(t1);
        featureStore2.setTransaction(t2);
        
        System.out.println("Step 1");
        System.out.println("------");
        System.out.println("start     auto-commit: "+DataUtilities.fidSet(featureStore.getFeatures()) );
        System.out.println("start              t1: "+DataUtilities.fidSet(featureStore1.getFeatures()) );
        System.out.println("start              t2: "+DataUtilities.fidSet(featureStore2.getFeatures()) );
        
        // select feature to remove
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        Filter filter1 = ff.id(Collections.singleton(ff.featureId("fid1")));
        featureStore1.removeFeatures(filter1); // road1 removes fid1 on t1
        
        System.out.println();
        System.out.println("Step 2 transaction 1 removes feature 'fid1'");
        System.out.println("------");
        System.out.println("t1 remove auto-commit: "+DataUtilities.fidSet(featureStore.getFeatures()) );
        System.out.println("t1 remove          t1: "+DataUtilities.fidSet(featureStore1.getFeatures()) );
        System.out.println("t1 remove          t2: "+DataUtilities.fidSet(featureStore2.getFeatures()) );
 
        // new feature to add!
        SimpleFeature feature = SimpleFeatureBuilder.build(type, new Object[] {
                5, "chris", null }, "fid5");
        feature.getUserData().put(Hints.USE_PROVIDED_FID,true);
        feature.getUserData().put(Hints.PROVIDED_FID, "fid5");
        
        SimpleFeatureCollection collection = DataUtilities.collection(feature);
        featureStore2.addFeatures(collection);

        System.out.println();
        System.out.println("Step 3 transaction 2 adds a new feature '"+feature.getID()+"'");
        System.out.println("------");
        System.out.println("t2 add    auto-commit: "+DataUtilities.fidSet(featureStore.getFeatures()) );
        System.out.println("t2 add             t1: "+DataUtilities.fidSet(featureStore1.getFeatures()) );
        System.out.println("t1 add             t2: "+DataUtilities.fidSet(featureStore2.getFeatures()) );

        // commit transaction one
        t1.commit();
        
        System.out.println();
        System.out.println("Step 4 transaction 1 commits the removal of feature 'fid1'");
        System.out.println("------");
        System.out.println("t1 commit auto-commit: "+DataUtilities.fidSet(featureStore.getFeatures()) );
        System.out.println("t1 commit          t1: "+DataUtilities.fidSet(featureStore1.getFeatures()) );
        System.out.println("t1 commit          t2: "+DataUtilities.fidSet(featureStore2.getFeatures()) );

        // commit transaction two
        t2.commit();

        System.out.println();
        System.out.println("Step 5 transaction 2 commits the addition of '"+feature.getID()+"'");
        System.out.println("------");
        System.out.println("t2 commit auto-commit: "+DataUtilities.fidSet(featureStore.getFeatures()) );
        System.out.println("t2 commit          t1: "+DataUtilities.fidSet(featureStore1.getFeatures()) );
        System.out.println("t2 commit          t2: "+DataUtilities.fidSet(featureStore2.getFeatures()) );

        t1.close();
        t2.close();
        store.dispose(); // clear out any listeners
        // transactionExample end
        System.out.println("\ntransactionExample end\n");
    }

    private static void removeAllExample() throws Exception {
        System.out.println("removeAllExample start\n");
        // removeAllExample start
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("directory", directory);
        DataStore store = DataStoreFinder.getDataStore(params);

        Transaction t = new DefaultTransaction("transaction");
        try {
            FeatureWriter<SimpleFeatureType, SimpleFeature> writer = store
                    .getFeatureWriter("example", Filter.INCLUDE, t);

            SimpleFeature feature;
            try {
                while (writer.hasNext()) {
                    feature = writer.next();
                    System.out.println("remove " + feature.getID());
                    writer.remove(); // marking contents for removal
                }
            } finally {
                writer.close();
            }
            System.out.println("commit " + t); // only now are the contents
                                               // removed
            t.commit();
        } catch (Throwable eek) {
            t.rollback();
        } finally {
            t.close();
            store.dispose();
        }
        // removeAllExample end
        System.out.println("\nremoveAllExample end\n");
    }

    private void replaceAll() throws Exception {
        System.out.println("replaceAll start\n");
        // replaceAll start
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("directory", directory);
        DataStore store = DataStoreFinder.getDataStore(params);

        final SimpleFeatureType type = store.getSchema("example");
        final FeatureWriter<SimpleFeatureType, SimpleFeature> writer;
        SimpleFeature f;
        SimpleFeatureCollection collection = FeatureCollections.newCollection();
        f = SimpleFeatureBuilder
                .build(type, new Object[] { 1, "jody" }, "fid1");
        collection.add(f);
        f = SimpleFeatureBuilder.build(type, new Object[] { 2, "brent" },
                "fid3");
        collection.add(f);
        f = SimpleFeatureBuilder
                .build(type, new Object[] { 3, "dave" }, "fid3");
        collection.add(f);
        f = SimpleFeatureBuilder.build(type, new Object[] { 4, "justin" },
                "fid4");
        collection.add(f);

        writer = store.getFeatureWriter("road", Transaction.AUTO_COMMIT);
        try {
            // remove all features
            while (writer.hasNext()) {
                writer.next();
                writer.remove();
            }
            // copy new features in
            SimpleFeatureIterator iterator = collection.features();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                SimpleFeature newFeature = writer.next(); // new blank feature
                newFeature.setAttributes(feature.getAttributes());
                writer.write();
            }
        } finally {
            writer.close();
        }
        // replaceAll end
        System.out.println("\nreplaceAll end\n");
    }

    private static void appendContent() throws Exception {
        System.out.println("copyContent start\n");
        // copyContent start
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("directory", directory);
        DataStore store = DataStoreFinder.getDataStore(params);

        FeatureReader<SimpleFeatureType,SimpleFeature> reader;
        FeatureWriter<SimpleFeatureType,SimpleFeature> writer;
        SimpleFeature feature, newFeature;
        
        SimpleFeatureType type = store.getSchema( "example" );
        SimpleFeatureType type2 = DataUtilities.createType( "duplicate", "id:Integer,geom:Geometry,name:String" );
        
        Query query = new Query( type.getTypeName(), Filter.INCLUDE );
        reader = store.getFeatureReader( query, Transaction.AUTO_COMMIT );
        store.createSchema( type2 );
        writer = store.getFeatureWriterAppend( "duplicate", Transaction.AUTO_COMMIT );
        try {
            while (reader.hasNext()) {
                feature = reader.next();
                newFeature = writer.next();
                newFeature.setAttribute("id", feature.getAttribute("id"));
                newFeature.setAttribute("geom", feature.getAttribute("geom") );
                newFeature.setAttribute("name", feature.getAttribute("name"));
                writer.write();
            }
        }
        finally {
            reader.close();
            writer.close();
        }
        
        // copyContent end
        System.out.println("\ncopyContent end\n");

    }
}
