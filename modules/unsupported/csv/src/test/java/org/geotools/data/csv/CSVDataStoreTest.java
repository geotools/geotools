package org.geotools.data.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
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
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Point;

/**
 * Test the CSVDataStore
 *
 *
 *
 * @source $URL$
 */
public class CSVDataStoreTest {

    private static final String TYPE_NAME = "locations";
    private static final String TEST_FILE_NAME = TYPE_NAME + ".csv";
    private static final String NUMBER_COL = "NUMBER";
    private static final String CITY_COL = "CITY";

    private File testFile;

    /**
     * Create a CSV file for the rest of the tests to use. 
     * The features have 3 attributes, but "coordinate" is broken out into separate LAT/LON columns in the file.
     */
    @Before
    public void createTestFile() throws IOException {
        File tempDir = new File(new File("./target"), "/csvDataStoreTest");
        if (!tempDir.exists()) {
            tempDir.mkdir();
        }
        this.testFile = new File(tempDir, TEST_FILE_NAME);
        if (!this.testFile.exists()) {
            this.testFile.createNewFile();
        }
        BufferedWriter writer = new BufferedWriter( new FileWriter( this.testFile ) );
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
    
    @After
    public void deleteTestFile() {
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
        assertTrue( this.testFile.exists() );        
        DataStore csv = new CSVDataStore( this.testFile );
        return csv;
    }
    
    @Test
    public void testFactory() throws FileNotFoundException, IOException {
        Map<String, Serializable> params = new HashMap<String, Serializable>();
        params.put("file", this.testFile);
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
        SimpleFeatureCollection matches = rows.getFeatures(this.newTestFilter());
        List<String> filteredCities = this.filteredCities();
        int count = 0;
        SimpleFeatureIterator iter = matches.features();
        while(iter.hasNext()) {
            SimpleFeature f = iter.next();
            filteredCities.contains(f.getAttribute(CITY_COL));
            count++;
        }
        iter.close();
        assertEquals(3, count);
        assertEquals(3, matches.size());
    }

    /**
     * Test FeatureStore/Writer update
     */
    @Test
    public void testUpdate() throws FileNotFoundException, IOException {
        DataStore csv = this.getDataStore();
        SimpleFeatureStore rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        rows.modifyFeatures(NUMBER_COL, 333, this.newTestFilter());
        
        // re-open
        csv = this.getDataStore();
        rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        SimpleFeatureCollection features = rows.getFeatures();
        assertEquals(9, features.size());
        SimpleFeatureIterator cursor = features.features();
        List<String> filteredCities = this.filteredCities();
        while (cursor.hasNext()) {
            SimpleFeature feature = cursor.next();
            if (filteredCities.contains(feature.getAttribute(CITY_COL))) {
                assertEquals("333", feature.getAttribute(NUMBER_COL));
            } else {
                assertNotSame("333", feature.getAttribute(NUMBER_COL));
            }
        }
        cursor.close();
    }
    
    @Test
    public void testUpdateAll() throws FileNotFoundException, IOException {
        DataStore csv = this.getDataStore();
        SimpleFeatureStore rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        rows.modifyFeatures(NUMBER_COL, 333, Filter.INCLUDE);
        
        // re-open
        csv = this.getDataStore();
        rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        SimpleFeatureCollection features = rows.getFeatures();
        assertEquals(9, features.size());
        SimpleFeatureIterator cursor = features.features();
        while (cursor.hasNext()) {
            SimpleFeature feature = cursor.next();
            assertEquals("333", feature.getAttribute(NUMBER_COL));
        }
        cursor.close();
    }
    
    @Test
    public void testUpdateNone() throws FileNotFoundException, IOException {
        DataStore csv = this.getDataStore();
        SimpleFeatureStore rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        rows.modifyFeatures(NUMBER_COL, 333, Filter.EXCLUDE);
        
        // re-open
        csv = this.getDataStore();
        rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        SimpleFeatureCollection features = rows.getFeatures();
        assertEquals(9, features.size());
        SimpleFeatureIterator cursor = features.features();
        while (cursor.hasNext()) {
            SimpleFeature feature = cursor.next();
            assertNotSame("333", feature.getAttribute(NUMBER_COL));
        }
        cursor.close();
    }
    
    private Filter newTestFilter() {
        // Filter collects entries with number > 500
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
        return ff.greater(ff.property(NUMBER_COL), ff.literal(500));
    }
    
    private List<String> filteredCities() {
        return Arrays.asList("Lausanne", "Victoria", "Cape Town");
    }
    
    private Point newPoint(Coordinate coords) {
        return JTSFactoryFinder.getGeometryFactory(null).createPoint(coords);
    }

    /**
     * Test remove
     */
    @Test
    public void testRemove() throws FileNotFoundException, IOException {
        DataStore csv = this.getDataStore();
        SimpleFeatureStore rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        rows.removeFeatures(this.newTestFilter());

        // re-open
        csv = this.getDataStore();
        rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        SimpleFeatureCollection features = rows.getFeatures();
        assertEquals(6, features.size());
    }

    @Test
    public void testRemoveAll() throws FileNotFoundException, IOException {
        DataStore csv = this.getDataStore();
        SimpleFeatureStore rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        rows.removeFeatures(Filter.INCLUDE);

        // re-open
        csv = this.getDataStore();
        rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        SimpleFeatureCollection features = rows.getFeatures();
        assertEquals(0, features.size());
    }

    /**
     * Test add
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testAdd() throws FileNotFoundException, IOException {
        DataStore csv = this.getDataStore();
        SimpleFeatureStore rows = (SimpleFeatureStore) csv.getFeatureSource(TYPE_NAME);
        SimpleFeatureType featureType = csv.getSchema(TYPE_NAME);
        DefaultFeatureCollection toAdd = new DefaultFeatureCollection("test", featureType);
        SimpleFeature newFeature = new SimpleFeatureImpl(new Object[] {this.newPoint(new Coordinate(12, 34)), "Manhattan Beach", 444}, featureType, new FeatureIdImpl("test"), false);
        toAdd.add(newFeature);
        rows.addFeatures(toAdd);

        // re-open
        csv = this.getDataStore();
        rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        SimpleFeatureCollection features = rows.getFeatures();
        assertEquals(10, features.size());
    }
    
    /**
     * Test add more than one.
     * @throws IOException 
     * @throws FileNotFoundException 
     */
    @Test
    public void testAddSeveral() throws FileNotFoundException, IOException {
        DataStore csv = this.getDataStore();
        SimpleFeatureStore rows = (SimpleFeatureStore) csv.getFeatureSource(TYPE_NAME);
        SimpleFeatureType featureType = csv.getSchema(TYPE_NAME);
        DefaultFeatureCollection toAdd = new DefaultFeatureCollection("test", featureType);
        SimpleFeature newFeature = new SimpleFeatureImpl(new Object[] {this.newPoint(new Coordinate(12, 34)), "Manhattan Beach", 444}, featureType, new FeatureIdImpl("test1"), false);
        toAdd.add(newFeature);
        newFeature = new SimpleFeatureImpl(new Object[] {this.newPoint(new Coordinate(56, 78)), "Hermosa Beach", 555}, featureType, new FeatureIdImpl("test2"), false);
        toAdd.add(newFeature);
        newFeature = new SimpleFeatureImpl(new Object[] {this.newPoint(new Coordinate(90, 12)), "Redondo Beach", 666}, featureType, new FeatureIdImpl("test3"), false);
        toAdd.add(newFeature);
        rows.addFeatures(toAdd);

        // re-open
        csv = this.getDataStore();
        rows = (SimpleFeatureStore) csv.getFeatureSource( TYPE_NAME );
        SimpleFeatureCollection features = rows.getFeatures();
        assertEquals(12, features.size());
    }
    
}
