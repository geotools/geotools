/* GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2010-2014, Open Source Geospatial Foundation (OSGeo)
 *
 * This file is hereby placed into the Public Domain. This means anyone is
 * free to do whatever they wish with this file. Use it well and enjoy!
 */
package org.geotools.tutorial.csv;

import static org.junit.Assert.assertTrue;

import com.csvreader.CsvReader;
import java.io.File;
import java.io.FileReader;
import java.io.Serializable;
import java.net.URL;
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
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.identity.FeatureId;

public class CSVTest {

    @Test
    public void test() throws Exception {
        List<String> cities = new ArrayList<>();
        URL url = CSVTest.class.getResource("locations.csv");
        File file = new File(url.toURI());
        try (FileReader reader = new FileReader(file)) {
            CsvReader locations = new CsvReader(reader);
            locations.readHeaders();
            while (locations.readRecord()) {
                cities.add(locations.get("CITY"));
            }
        }
        assertTrue(cities.contains("Victoria"));
    }

    // locations.csv end

    @Test
    public void example1() throws Exception {
        System.out.println("example1 start\n");
        URL url = CSVTest.class.getResource("locations.csv");
        File file = new File(url.toURI());

        // example1 start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);

        DataStore store = DataStoreFinder.getDataStore(params);

        String names[] = store.getTypeNames();
        System.out.println("typenames: " + names.length);
        System.out.println("typename[0]: " + names[0]);
        // example1 end
        System.out.println("\nexample1 end\n");
    }

    @Test
    public void example2() throws Exception {
        System.out.println("example2 start\n");
        URL url = CSVTest.class.getResource("locations.csv");
        File file = new File(url.toURI());
        // example2 start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        SimpleFeatureType type = store.getSchema("locations");

        System.out.println("featureType  name: " + type.getName());
        System.out.println("featureType count: " + type.getAttributeCount());

        System.out.println("featuretype attributes list:");
        // access by list
        for (AttributeDescriptor descriptor : type.getAttributeDescriptors()) {
            System.out.print("  " + descriptor.getName());
            System.out.print(
                    " (" + descriptor.getMinOccurs() + "," + descriptor.getMaxOccurs() + ",");
            System.out.print((descriptor.isNillable() ? "nillable" : "manditory") + ")");
            System.out.print(" type: " + descriptor.getType().getName());
            System.out.println(" binding: " + descriptor.getType().getBinding().getSimpleName());
        }
        // access by index
        AttributeDescriptor attributeDescriptor = type.getDescriptor(0);
        System.out.println("attribute 0    name: " + attributeDescriptor.getName());
        System.out.println("attribute 0    type: " + attributeDescriptor.getType().toString());
        System.out.println("attribute 0 binding: " + attributeDescriptor.getType().getBinding());

        // access by name
        AttributeDescriptor cityDescriptor = type.getDescriptor("CITY");
        System.out.println("attribute 'CITY'    name: " + cityDescriptor.getName());
        System.out.println("attribute 'CITT'    type: " + cityDescriptor.getType().toString());
        System.out.println("attribute 'CITY' binding: " + cityDescriptor.getType().getBinding());

        // default geometry
        GeometryDescriptor geometryDescriptor = type.getGeometryDescriptor();
        System.out.println("default geom    name: " + geometryDescriptor.getName());
        System.out.println("default geom    type: " + geometryDescriptor.getType().toString());
        System.out.println("default geom binding: " + geometryDescriptor.getType().getBinding());
        System.out.println(
                "default geom     crs: "
                        + CRS.toSRS(geometryDescriptor.getCoordinateReferenceSystem()));

        // example2 end
        System.out.println("\nexample2 end\n");
    }

    @Test
    public void example3() throws Exception {
        System.out.println("example3 start\n");
        URL url = CSVTest.class.getResource("locations.csv");
        File file = new File(url.toURI());
        // example3 start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore datastore = DataStoreFinder.getDataStore(params);

        Query query = new Query("locations");

        System.out.println("open feature reader");
        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                datastore.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            int count = 0;
            while (reader.hasNext()) {
                SimpleFeature feature = reader.next();
                System.out.println("  " + feature.getID() + " " + feature.getAttribute("CITY"));
                count++;
            }
            System.out.println("close feature reader");
            System.out.println("read in " + count + " features");
        } finally {
            reader.close();
        }
        // example3 end
        System.out.println("\nexample3 end\n");
    }

    @Test
    public void example4() throws Exception {
        System.out.println("example4 start\n");
        URL url = CSVTest.class.getResource("locations.csv");
        File file = new File(url.toURI());

        // example4 start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory();

        Set<FeatureId> selection = new HashSet<>();
        selection.add(ff.featureId("locations.7"));

        Filter filter = ff.id(selection);
        Query query = new Query("locations", filter);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader =
                store.getFeatureReader(query, Transaction.AUTO_COMMIT);

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

    @Test
    public void example5() throws Exception {
        System.out.println("example5 start\n");
        URL url = CSVTest.class.getResource("locations.csv");
        File file = new File(url.toURI());
        // example5 start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        SimpleFeatureSource featureSource = store.getFeatureSource("locations");

        Filter filter = CQL.toFilter("CITY = 'Denver'");
        SimpleFeatureCollection features = featureSource.getFeatures(filter);
        System.out.println("found :" + features.size() + " feature");
        SimpleFeatureIterator iterator = features.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                Geometry geometry = (Geometry) feature.getDefaultGeometry();
                System.out.println(feature.getID() + " default geometry " + geometry);
            }
        } catch (Throwable t) {
            iterator.close();
        }

        // example5 end
        System.out.println("\nexample5 end\n");
    }

    @Test
    public void example6() throws Exception {
        System.out.println("example6 start\n");
        URL url = CSVTest.class.getResource("locations.csv");
        File file = new File(url.toURI());

        // example6 start
        Map<String, Serializable> params = new HashMap<>();
        params.put("file", file);
        DataStore store = DataStoreFinder.getDataStore(params);

        SimpleFeatureSource featureSource = store.getFeatureSource("locations");
        SimpleFeatureCollection featureCollection = featureSource.getFeatures();

        List<String> list = new ArrayList<>();
        try (SimpleFeatureIterator features = featureCollection.features(); ) {
            while (features.hasNext()) {
                list.add(features.next().getID());
            }
        } // try-with-resource will call features.close()

        System.out.println("           List Contents: " + list);
        System.out.println("    FeatureSource  count: " + featureSource.getCount(Query.ALL));
        System.out.println("    FeatureSource bounds: " + featureSource.getBounds(Query.ALL));
        System.out.println("FeatureCollection   size: " + featureCollection.size());
        System.out.println("FeatureCollection bounds: " + featureCollection.getBounds());

        // Load into memory!
        DefaultFeatureCollection collection = DataUtilities.collection(featureCollection);
        System.out.println("         collection size: " + collection.size());
        // example6 end
        System.out.println("\nexample6 end\n");
    }
}
