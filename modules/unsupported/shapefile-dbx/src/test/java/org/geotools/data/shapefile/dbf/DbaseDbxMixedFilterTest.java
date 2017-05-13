package org.geotools.data.shapefile.dbf;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.Query;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.data.shapefile.TestCaseSupport;
import org.geotools.data.shapefile.dbf.index.DbxDbaseFileIndexer;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.geometry.jts.JTS;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.simple.SimpleFeature;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.Polygon;

public class DbaseDbxMixedFilterTest extends TestCaseSupport {
    
    final static String FILTERTEST_SHPFILE = "filter-mixed/dbxtest.shp";
    
    private List<ShapefileDataStore> stores = new ArrayList<ShapefileDataStore>();
    
    @Before
    public void setup() throws IOException {
        
        for (String shapeFileName : new String[]{ FILTERTEST_SHPFILE }) {
            
            File shpFile = copyShapefiles(shapeFileName);
            URL url = shpFile.toURI().toURL();
            
            // open the test shapefile
            ShapefileDataStore store = new ShapefileDataStore(url);
            
            String fname = shpFile.getPath();
            int spos = fname.lastIndexOf('.');
            File dbaseFile = new File(fname.substring(0, spos) + ".dbf");
            
            // create the related DBX-index.
            DbxDbaseFileIndexer indexer = new DbxDbaseFileIndexer(dbaseFile, store.getCharset(), store.getTimeZone(), false);
            indexer.buildIndex(null, 1024*1024, new NullProgressListener());
            
            stores.add(store);
        }
    }
    
    @After
    public void tearDown() throws Exception {
        
        for (ShapefileDataStore store : stores) {
            if (store != null) {
                store.dispose();
            }
        }
        stores.clear();
        
        super.tearDown();
    }
    
    private int testFeatures(ShapefileDataStore store, String filter, int resultCount, int[] recordIds) throws Exception {
        int featureCount = 0;
        
        Query query = new Query("", ECQL.toFilter(filter));
        assertNotNull(query);
        
        Map<Integer,Integer> recordMap = new HashMap<Integer,Integer>();
        if (recordIds!=null && recordIds.length>0) for (int recordId : recordIds) recordMap.put(recordId, recordId);
        
        SimpleFeatureSource featureSource = store.getFeatureSource(store.getTypeNames()[0]);
        SimpleFeatureCollection featureCollection = featureSource.getFeatures(query);
        SimpleFeatureIterator featureIterator = featureCollection.features();
        
        try {
            while (featureIterator.hasNext()) {
                SimpleFeature feature = featureIterator.next();
                
                if (recordMap.size()>0) {
                    int id = Integer.parseInt(feature.getProperty("F_NUM").getValue().toString());
                    if (recordMap.size()>0 && !recordMap.containsKey(id)) Assert.fail("Feature "+ id + " not fetched!");
                }
                featureCount++;
            }
        } finally {
            featureIterator.close();
        }
        assertTrue("Invalid number of features fetched!", featureCount == resultCount);
        return featureCount;
    }
    
    @Test
    public void testShapeFileWithDbaseIndex() throws Exception {
        
        double size = 0.0000001;
        Coordinate coordinate = new Coordinate(-2.185971907671144, 42.58693305554461); 
        Envelope envelope = new Envelope(coordinate.x-size, coordinate.x+size, coordinate.y-size, coordinate.y+size);
        Polygon bbox = JTS.toGeometry(envelope);
        bbox.setSRID(4326);
        
        for (ShapefileDataStore store : stores) {
            
            testFeatures(store, "F_NUM=0", 1, new int[]{0});
            
            testFeatures(store, "F_NUM=1 OR F_NUM=9", 2, new int[]{1,9});
            
            testFeatures(store, "F_NUM>=8", 2, new int[]{8,9});
            
            testFeatures(store, "(F_NUM<>5 OR F_NUM=0) AND F_NUM<=100*4", 9, new int[]{0,1,2,3,4,6,7,8,9});
            
            testFeatures(store, "F_INT>=-16 AND F_INT<=-15", 2, new int[]{5,6});
            
            testFeatures(store, "F_DOUBLE=34.3567", 1, new int[]{6});
            
            testFeatures(store, "F_TEXT=''", 1, new int[]{2});
            
            testFeatures(store, "F_TEXT<>''", 9, new int[]{0,1,3,4,5,6,7,8,9});
            
            testFeatures(store, "F_TEXT='Record N1 with "+Character.toString((char)241)+"'", 1, new int[]{1});
            
            testFeatures(store, "F_TEXT='Record N1 with "+Character.toString((char)241)+"' OR F_TEXT='Record N0' OR F_TEXT like 'Record N6'", 3, new int[]{0,1,6});
            
            testFeatures(store, "F_TEXT like 'Record N%'", 9, new int[]{0,1,3,4,5,6,7,8,9});
            
            testFeatures(store, "F_BOOL=False", 10, new int[]{0,1,2,3,4,5,6,7,8,9});
            
            testFeatures(store, "F_DATE=2015-08-24", 2, new int[]{4,9});
            
            testFeatures(store, "WITHIN(the_geom,"+bbox.toText()+")", 1, new int[]{0});
            
            testFeatures(store, "WITHIN(the_geom,"+bbox.toText()+") OR F_NUM=9", 2, new int[]{0,9});
            
            testFeatures(store, "WITHIN(the_geom,"+bbox.toText()+") AND WITHIN(the_geom,"+bbox.toText()+")", 1, new int[]{0});
            
            testFeatures(store, "WITHIN(the_geom,"+bbox.toText()+") AND F_NUM=9", 0, null);
        }
    }
}
