package org.geotools.data.spatialite;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.geotools.data.DataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.spatial.BBOX;

public class ScalabilityBenchmark {

    public static void main(String[] args) throws IOException, InterruptedException, ExecutionException {
        
        Map<Serializable, Object> params = new HashMap<Serializable, Object>();
        params.put(JDBCDataStoreFactory.DATABASE.key, "/home/aaime/devel/gisData/foss4g2009/data/TIGER-2008/48_TEXAS/texas.sqlite");
        params.put(JDBCDataStoreFactory.DBTYPE.key, "spatialite");
        
        DataStore sl = new SpatiaLiteDataStoreFactory().createDataStore(params);
        System.out.println(Arrays.toString(sl.getTypeNames())); 
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        BBOX bbox = ff.bbox(ff.property(""), -98.682214632889,29.437545158775,-98.579577078201,29.520737707985, "EPSG:4326");
        final SimpleFeatureCollection fc = sl.getFeatureSource("roads").getFeatures(bbox);
        
        ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(8);
        
        List<Future<Void>> futures = new ArrayList<Future<Void>>();
        for (int i = 0; i < 8; i++) {
            Future<Void> future = newFixedThreadPool.submit(new Callable<Void>() {
                
                @Override
                public Void call() throws Exception {
                    for (int j = 0; j < 100; j++) {
                        fc.accepts(new FeatureVisitor() {
                            
                            @Override
                            public void visit(Feature feature) {
                                
                                
                            }
                        }, null);
                        System.out.println(Thread.currentThread().getName() + " " + fc.size());
                    }

                    return null;
                }
            });
            futures.add(future);
        }
        for (Future<Void> future : futures) {
            future.get();
        }
        newFixedThreadPool.shutdown();
        
    }
}
