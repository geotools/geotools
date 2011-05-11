package org.geotools.data.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Point;

/**
 * Test the CSVDataStore
 */
public class CSVDataStoreTest {

    private static final String TYPE_NAME = "locations";
    private static final String TEST_FILE_NAME = TYPE_NAME + ".csv";

    private static File testFile;

    /**
     * Create a CSV file for the rest of the tests to use. 
     * The features have 3 attributes, but "coordinate" is broken out into separate LAT/LON columns in the file.
     */
    @BeforeClass
    public static void createTestFile() throws IOException {
        File tempDir = new File(System.getProperty("java.io.tmpdir") + "/csvDataStoreTest");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        testFile = new File(tempDir, TEST_FILE_NAME);
        if (!testFile.exists()) {
            testFile.createNewFile();
        }
        System.out.println("testFile: " + testFile);
        BufferedWriter writer = new BufferedWriter( new FileWriter( testFile ) );
        writer.append("LAT, LON, CITY, NUMBER"); writer.newLine();
        writer.append("46.066667, 11.116667, Trento, 140"); writer.newLine();
        writer.append("44.9441, -93.0852, St Paul, 125"); writer.newLine();
        writer.append("13.752222, 100.493889, Bangkok, 150"); writer.newLine();
        writer.append("45.420833, -75.69, Ottawa, 200"); writer.newLine();
        writer.append("44.9801, -93.251867, Minneapolis, 350"); writer.newLine();
        writer.append("46.519833, 6.6335, Lausanne, 560"); writer.newLine();
        writer.append("48.428611, -123.365556, Victoria, 721"); writer.newLine();
        writer.append("-33.925278, 18.423889, Cape Town, 550"); writer.newLine();
        writer.append("-33.859972, 151.211111, Sydney, 436"); writer.newLine();
        writer.close();
    }
    
    @AfterClass
    public static void deleteTestFile() {
        testFile.delete();
    }

    /**
     * Test basic loading/reading of CSV file.
     */
    @Test
    public void testLoad() throws Exception {
        DataStore csv = this.getDataStore();
        
        String[] names = csv.getTypeNames();
        assertEquals( 1, names.length );
        
        String typeName = names[0];
        assertEquals( TYPE_NAME, typeName);
        
        SimpleFeatureType schema = csv.getSchema( typeName );
        assertEquals( 3, schema.getAttributeCount() );
        assertTrue( schema.getGeometryDescriptor().getType().getBinding().isAssignableFrom( Point.class ));
        
        SimpleFeatureSource rows = csv.getFeatureSource( typeName );
        assertNotNull( rows );
        int count = rows.getCount(Query.ALL);
        assertEquals( 9, count );
        
        ReferencedEnvelope bounds = rows.getBounds();
        assertFalse( bounds.isEmpty() || bounds.isNull() );
        
        SimpleFeatureCollection features = rows.getFeatures();
        SimpleFeatureIterator cursor = features.features();
        Set<String> found = new HashSet<String>();
        try {
            while( cursor.hasNext()){
                SimpleFeature feature = cursor.next();
                found.add( feature.getID() );
            }
            String expected = TYPE_NAME + ".4";
            System.out.println( found );
            assertTrue( expected, found.contains( expected ));
        }
        finally {
            cursor.close();
        }
    }

    private DataStore getDataStore() throws FileNotFoundException, IOException {
        assertTrue( testFile.exists() );        
        DataStore csv = new CSVDataStore( testFile );
        return csv;
    }
    
    @Test
    public void testFactory() throws FileNotFoundException, IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("file", testFile);
        DataStore ds = new CSVDataStoreFactory().createDataStore(params);
        assertNotNull(ds);
        assertEquals("locations", ds.getNames().get(0).getLocalPart());
        assertEquals("locations", ds.getTypeNames()[0]);
    }
    
    /**
     * Test basic filtering.
     */
    @Test
    public void testFilter() throws FileNotFoundException, IOException {
        DataStore csv = this.getDataStore();
        SimpleFeatureSource rows = csv.getFeatureSource( TYPE_NAME );
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        // Filter collects entries with number < 200
        SimpleFeatureCollection matches = rows.getFeatures(ff.less(ff.property("NUMBER"), ff.literal(200)));
        List<String> filteredCities = Arrays.asList("Trento", "St Paul", "Bangkok");
        int count = 0;
        SimpleFeatureIterator iter = matches.features();
        while(iter.hasNext()) {
            SimpleFeature f = iter.next();
            filteredCities.contains(f.getAttribute("CITY"));
            count++;
        }
        iter.close();
        assertEquals(3, count);
        assertEquals(3, matches.size());
    }

    /**
     * Test FeatureStore/Writer
     */
    @Ignore
    @Test
    public void testUpdate() throws FileNotFoundException, IOException {
        DataStore csv = this.getDataStore();
        SimpleFeatureStore rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        rows.modifyFeatures("NUMBER", 333, Filter.INCLUDE);
        SimpleFeatureCollection features = rows.getFeatures();
        SimpleFeatureIterator cursor = features.features();
        while (cursor.hasNext()) {
            assertEquals(333, cursor.next().getAttribute("NUMBER"));
        }
        cursor.close();
    }

}