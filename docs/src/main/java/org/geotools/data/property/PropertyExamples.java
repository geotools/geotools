package org.geotools.data.property;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.cql2.CQLException;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.Geometry;

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
                example1();
                example2();
                example3();
                example4();
                example5();
                example6();
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
            }
            tmp.delete();
        }
    }

    private static void example1() throws IOException {
        System.out.println("example1 start\n");
        // example1 start
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("directory", directory);
        DataStore store = DataStoreFinder.getDataStore(params);

        String names[] = store.getTypeNames();
        System.out.println("typenames: " + names.length);
        System.out.println("typename[0]: " + names[0]);
        // example1 end
        System.out.println("\nexample1 end\n");
    }

    private static void example2() throws IOException {
        System.out.println("example2 start\n");
        // example2 start
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("directory", directory);
        DataStore store = DataStoreFinder.getDataStore(params);

        SimpleFeatureType type = store.getSchema("example");

        System.out.println("       typeName: " + type.getTypeName());
        System.out.println("           name: " + type.getName());
        System.out.println("attribute count: " + type.getAttributeCount());

        AttributeDescriptor id = type.getDescriptor(0);
        System.out.println("attribute 'id'    name:" + id.getName());
        System.out.println("attribute 'id'    type:" + id.getType().toString());
        System.out.println("attribute 'id' binding:"
                + id.getType().getDescription());

        AttributeDescriptor name = type.getDescriptor("name");
        System.out.println("attribute 'name'    name:" + name.getName());
        System.out.println("attribute 'name' binding:"
                + name.getType().getBinding());
        // example2 end
        System.out.println("\nexample2 end\n");
    }

    private static void example3() throws IOException {
        System.out.println("example3 start\n");
        // example3 start
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("directory", directory);
        DataStore datastore = DataStoreFinder.getDataStore(params);

        Query query = new Query("example");
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = datastore
                .getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                System.out.println("feature " + count + ": " + feature.getID());
                count++;
            }
            System.out.println("read in " + count + " features");
        } finally {
            reader.close();
        }
        // example3 end
        System.out.println("\nexample3 end\n");
    }

    private static void example4() throws IOException {
        System.out.println("example4 start\n");
        // example4 start
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("directory", directory);
        DataStore store = DataStoreFinder.getDataStore(params);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Set<FeatureId> selection = new HashSet<FeatureId>();
        selection.add(ff.featureId("fid1"));

        Filter filter = ff.id(selection);
        Query query = new Query("example", filter);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader = store
                .getFeatureReader(query, Transaction.AUTO_COMMIT);

        try {
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                System.out.println("feature " + feature.getID());

                for (Property property : feature.getProperties()) {
                    System.out.print("\t");
                    System.out.print(property.getName());
                    System.out.print(" = ");
                    System.out.println(property.getValue());
                }
            }
        } finally {
            reader.close();
        }
        // example4 end
        System.out.println("\nexample4 end\n");
    }

    private static void example5() throws IOException, CQLException {
        System.out.println("example5 start\n");
        // example5 start
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("directory", directory);
        DataStore store = DataStoreFinder.getDataStore(params);

        SimpleFeatureSource featureSource = store.getFeatureSource("example");

        Filter filter = CQL.toFilter("name = 'dave'");
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        System.out.println("found :" + features.size() + " feature");
        SimpleFeatureIterator iterator = features.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                System.out.println(feature.getID() + " location " + geometry);
            }
        } catch (Throwable t) {
            iterator.close();
        }

        // example5 end
        System.out.println("\nexample5 end\n");
    }

    private static void example6() throws IOException, CQLException {
        System.out.println("example6 start\n");
        // example6 start
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("directory", directory);
        DataStore store = DataStoreFinder.getDataStore(params);

        SimpleFeatureSource featureSource = store.getFeatureSource("example");
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();
        SimpleFeatureIterator features = featureCollection.features();
        List<String> list = new ArrayList<String>();
        try {
            while (features.hasNext()) {
                list.add(features.next().getID());
            }
        } finally {
            features.close();
        }
        System.out.println("  contents:" + list);
        System.out.println("     count:" + featureSource.getCount(Query.ALL));
        System.out.println("    bounds:" + featureSource.getBounds(Query.ALL));
        System.out.println("      size:" + featureCollection.size());
        System.out.println("    bounds:" + featureCollection.getBounds());
        System.out.println("collection: "
                + DataUtilities.collection(featureCollection).size());

        // example6 end
        System.out.println("\nexample6 end\n");
    }
}
