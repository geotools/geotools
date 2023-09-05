/*
 *    GeoTools Sample code and Tutorials by Open Source Geospatial Foundation, and others
 *    https://docs.geotools.org
 *
 *    To the extent possible under law, the author(s) have dedicated all copyright
 *    and related and neighboring rights to this software to the public domain worldwide.
 *    This software is distributed without any warranty.
 *
 *    You should have received a copy of the CC0 Public Domain Dedication along with this
 *    software. If not, see <http://creativecommons.org/publicdomain/zero/1.0/>.
 */
package org.geotools.data;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.geotools.api.data.DataStore;
import org.geotools.api.data.DataStoreFinder;
import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.FileDataStore;
import org.geotools.api.data.FileDataStoreFactorySpi;
import org.geotools.api.data.FileDataStoreFinder;
import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.api.feature.Property;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.filter.Filter;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.data.shapefile.ShapefileDumper;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.SchemaException;

public class ShapefileExample {

    public void use() throws Exception {
        // start use
        File file = new File("example.shp");
        Map<String, Object> map = new HashMap<>();
        map.put("url", file.toURI().toURL());

        DataStore dataStore = DataStoreFinder.getDataStore(map);
        String typeName = dataStore.getTypeNames()[0];

        FeatureSource<SimpleFeatureType, SimpleFeature> source =
                dataStore.getFeatureSource(typeName);
        Filter filter = Filter.INCLUDE; // ECQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)")

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(filter);
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            while (features.hasNext()) {
                SimpleFeature feature = features.next();
                System.out.print(feature.getID());
                System.out.print(": ");
                System.out.println(feature.getDefaultGeometryProperty().getValue());
            }
        }
        // end use
    }

    public void create() throws Exception {
        // start create
        FileDataStoreFactorySpi factory = new ShapefileDataStoreFactory();

        File file = new File("my.shp");
        Map<String, ?> map = Collections.singletonMap("url", file.toURI().toURL());

        DataStore myData = factory.createNewDataStore(map);
        SimpleFeatureType featureType =
                DataUtilities.createType(
                        "my", "geom:Point,name:String,age:Integer,description:String");
        myData.createSchema(featureType);
        // end create
    }

    public void read() throws Exception {
        // start read
        File file = new File("my.shp");
        FileDataStore myData = FileDataStoreFinder.getDataStore(file);
        SimpleFeatureSource source = myData.getFeatureSource();
        SimpleFeatureType schema = source.getSchema();

        Query query = new Query(schema.getTypeName());
        query.setMaxFeatures(1);

        FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures(query);
        try (FeatureIterator<SimpleFeature> features = collection.features()) {
            while (features.hasNext()) {
                SimpleFeature feature = features.next();
                System.out.println(feature.getID() + ": ");
                for (Property attribute : feature.getProperties()) {
                    System.out.println("\t" + attribute.getName() + ":" + attribute.getValue());
                }
            }
        }
        // end read
    }

    public void dump() throws Exception {
        // start dumper
        ShapefileDumper dumper = new ShapefileDumper(new File("./target/demo"));
        // optiona, set a target charset (ISO-8859-1 is the default)
        dumper.setCharset(Charset.forName("ISO-8859-15"));
        // split when shp or dbf reaches 100MB
        int maxSize = 100 * 1024 * 1024;
        dumper.setMaxDbfSize(maxSize);
        dumper.setMaxDbfSize(maxSize);
        // actually dump data
        SimpleFeatureCollection fc = getFeatureCollection();
        dumper.dump(fc);
        // end dumper
    }

    private SimpleFeatureCollection getFeatureCollection() throws SchemaException {
        SimpleFeatureType featureType =
                DataUtilities.createType(
                        "my", "geom:Point,name:String,age:Integer,description:String");
        return new ListFeatureCollection(featureType);
    }
}
