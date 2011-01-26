package org.geotools.geojson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

//import org.apache.commons.io.output.NullOutputStream;
import org.geotools.data.FeatureSource;
//import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geojson.feature.FeatureJSON;
import org.geotools.geojson.geom.GeometryJSON;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;

public class Benchmark {

    public static void main(String[] args) throws Exception {
        benchmarkFeatureCollectionParse(new File("/Users/jdeolive/texas_roads.json"));
        //benchmarkFeatureCollectionParse(new File("/Users/jdeolive/world_borders.json"));
        //FeatureSource data = loadData();

        //bencharkGeometryEncode(data);
        //bencharkGeometryEncode(data);
        //bencharkGeometryEncode(data);
    }
    
    static void benchmarkFeatureCollectionParse(File source) throws Exception {
        BufferedReader reader = new BufferedReader(new FileReader(source));
        FeatureJSON fjson = new FeatureJSON();
        FeatureIterator<SimpleFeature> it = fjson.streamFeatureCollection(reader);
        int count = 0;
        
        long t1 = System.currentTimeMillis();
        while(it.hasNext()) {
//            SimpleFeature f = it.next();
//            System.out.println(f.getID());
//            System.out.println(f.getDefaultGeometry());
//            
//            for (int i = 0; i < f.getAttributeCount(); i++) {
//                System.out.println("\t" + f.getFeatureType().getDescriptor(i).getLocalName() + ": " + f.getAttribute(i));
//            }
            if (count++ % 100000 == 0) {
                System.out.println(count);
            }
            
        }
        long t2 = System.currentTimeMillis();
        System.out.println(t2-t1);
        it.close();
        reader.close();
    }
    
    static void bencharkGeometryEncode(FeatureSource data) throws Exception {
        GeometryJSON gjson = new GeometryJSON();
        OutputStream out = System.out;/*new NullOutputStream();*/
        Writer writer = new OutputStreamWriter(out);
        
        FeatureCollection features = data.getFeatures();
        FeatureIterator it = features.features();

        long t1 = System.currentTimeMillis();
        while(it.hasNext()) {
            SimpleFeature f = (SimpleFeature) it.next();
            gjson.write((Geometry) f.getDefaultGeometry(), writer);
        }
        
        writer.flush();
        
        long t2 = System.currentTimeMillis();
        System.out.println(t2-t1);
        features.close(it);
    }

    static FeatureSource loadData() throws Exception {
        /*ShapefileDataStore ds = new ShapefileDataStore(new File("/Users/jdeolive/Downloads/data/world_borders.shp").toURL());
        return ds.getFeatureSource();*/
        return null;
    }
}
