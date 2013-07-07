package org.geotools.data.transform;

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.geotools.data.CollectionFeatureReader;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.transform.Definition;
import org.geotools.data.transform.TransformFactory;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.text.cql2.CQL;
import org.geotools.filter.text.ecql.ECQL;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.io.WKTReader;

public class TransformFeatureStoreTest extends AbstractTransformTest {
    
    WKTReader wkt = new WKTReader();
    
    @Before
    public void setupWriteTest() throws IOException {
        // clone the file to avoid issues with write tests
        new File("./target/ ").mkdirs();
        File source = new File("./src/test/resources/org/geotools/data/transform/states.properties");
        File target = new File("./target/transform/states.properties");
        target.delete();
        FileUtils.copyFile(source, target);

        PropertyDataStore pds = new PropertyDataStore(new File("./target/transform"));
        STATES = pds.getFeatureSource("states");
    }

    SimpleFeatureSource transformWithPartialTransform() throws Exception {
        List<Definition> definitions = new ArrayList<Definition>();
        definitions.add(new Definition("geom", ECQL.toExpression("buffer(the_geom, 1)")));
        definitions.add(new Definition("name", ECQL.toExpression("strToLowercase(state_name)")));
        definitions.add(new Definition("total", ECQL.toExpression("male + female")));
        definitions.add(new Definition("people", ECQL.toExpression("persons")));

        SimpleFeatureSource transformed = TransformFactory.transform(STATES, "bstates", definitions);
        return transformed;
    }

    @Test
    public void testFeatureStore() throws Exception {
        // these are easy, selection and renaming, they can be inverted
        assertTrue(transformWithSelection() instanceof SimpleFeatureStore);
        assertTrue(transformWithRename() instanceof SimpleFeatureStore);
        assertTrue(transformWithPartialTransform() instanceof SimpleFeatureStore);
        // this one does not have any definition that can be inverted
        assertTrue(transformWithExpressions() instanceof SimpleFeatureSource);
    }

    @Test
    public void testDeleteOnSelection() throws Exception {
        Filter f = CQL.toFilter("state_name = 'Delaware'");
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithSelection();
        transformed.removeFeatures(f);

        // check it has been deleted
        int size = STATES.getCount(Query.ALL);
        assertEquals(9, size);

        // check it's not possible to get at it anymore
        assertEquals(0, STATES.getFeatures(f).size());
        assertEquals(0, transformed.getFeatures(f).size());
    }

    @Test
    public void testDeleteOnRename() throws Exception {
        Filter f = CQL.toFilter("name = 'Delaware'");
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithRename();
        transformed.removeFeatures(f);

        // check it has been deleted
        int size = STATES.getCount(Query.ALL);
        assertEquals(9, size);

        // check it's not possible to get at it anymore
        assertEquals(0, STATES.getFeatures(f).size());
        assertEquals(0, transformed.getFeatures(f).size());
    }

    @Test
    public void testDeleteOnTransform() throws Exception {
        Filter f = CQL.toFilter("name = 'delaware'");
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithPartialTransform();
        transformed.removeFeatures(f);

        // check it has been deleted
        int size = STATES.getCount(Query.ALL);
        assertEquals(9, size);

        // check it's not possible to get at it anymore
        assertEquals(0, STATES.getFeatures(f).size());
        assertEquals(0, transformed.getFeatures(f).size());
        
        // now remove everything else
        f = CQL.toFilter("people = total");
        transformed.removeFeatures(f);
        assertEquals(0, STATES.getFeatures(Query.ALL).size());
        assertEquals(0, transformed.getFeatures(Query.ALL).size());
    }
    
    @Test 
    public void testInsertOnSelection() throws Exception {
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithSelection();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(transformed.getSchema());
        fb.add(wkt.read("POINT (-120 40)").buffer(3));
        fb.add("baloon");
        fb.add(1);
        SimpleFeature sf = fb.buildFeature("states_mini.11");
        
        // add the feature
        transformed.addFeatures(DataUtilities.collection(sf));
        
        // check it's there
        int size = STATES.getCount(Query.ALL);
        assertEquals(11, size);
        
        assertEquals(1, STATES.getFeatures(new Query(null, CQL.toFilter("state_name = 'baloon'"))).size());
        assertEquals(1, transformed.getFeatures(new Query(null, CQL.toFilter("state_name = 'baloon'"))).size());
    }
    
    @Test 
    public void testInsertOnRename() throws Exception {
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithRename();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(transformed.getSchema());
        fb.add(wkt.read("POINT (-120 40)").buffer(3));
        fb.add("baloon");
        fb.add(1);
        SimpleFeature sf = fb.buildFeature("states_mini.11");
        
        // add the feature
        transformed.addFeatures(DataUtilities.collection(sf));
        
        // check it's there
        int size = STATES.getCount(Query.ALL);
        assertEquals(11, size);
        
        assertEquals(1, STATES.getFeatures(new Query(null, CQL.toFilter("state_name = 'baloon'"))).size());
        assertEquals(1, transformed.getFeatures(new Query(null, CQL.toFilter("name = 'baloon'"))).size());
    }
    
    @Test 
    public void testInsertOnTransform() throws Exception {
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithPartialTransform();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(transformed.getSchema());
        fb.add(wkt.read("POINT (-120 40)").buffer(3));
        fb.add("baloon");
        fb.add(1);
        fb.add(1);
        SimpleFeature sf = fb.buildFeature("states_mini.11");
        
        // add the feature
        transformed.addFeatures(DataUtilities.collection(sf));
        
        // check it's there
        int size = STATES.getCount(Query.ALL);
        assertEquals(11, size);
        
        // the name won't be preserved since it's transformed, we use the population instead
        assertEquals(1, STATES.getFeatures(new Query(null, CQL.toFilter("persons = 1"))).size());
        assertEquals(1, transformed.getFeatures(new Query(null, CQL.toFilter("people = 1"))).size());
    }
    
    @Test 
    public void testSetOnSelection() throws Exception {
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithSelection();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(transformed.getSchema());
        fb.add(wkt.read("POINT (-120 40)").buffer(3));
        fb.add("baloon");
        fb.add(1);
        SimpleFeature sf = fb.buildFeature("states_mini.11");
        
        // set the feature
        transformed.setFeatures(new CollectionFeatureReader(new SimpleFeature[] {sf}));
        
        // check it's there and it's the only one
        int size = STATES.getCount(Query.ALL);
        assertEquals(1, size);
        
        assertEquals(1, STATES.getFeatures(new Query(null, CQL.toFilter("state_name = 'baloon'"))).size());
        assertEquals(1, transformed.getFeatures(new Query(null, CQL.toFilter("state_name = 'baloon'"))).size());
    }
    
    @Test 
    public void testSetOnRename() throws Exception {
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithRename();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(transformed.getSchema());
        fb.add(wkt.read("POINT (-120 40)").buffer(3));
        fb.add("baloon");
        fb.add(1);
        SimpleFeature sf = fb.buildFeature("states_mini.11");
        
        // set the feature
        transformed.setFeatures(new CollectionFeatureReader(new SimpleFeature[] {sf}));
        
        // check it's there
        int size = STATES.getCount(Query.ALL);
        assertEquals(1, size);
        
        assertEquals(1, STATES.getFeatures(new Query(null, CQL.toFilter("state_name = 'baloon'"))).size());
        assertEquals(1, transformed.getFeatures(new Query(null, CQL.toFilter("name = 'baloon'"))).size());
    }
    
    @Test 
    public void testSetOnTransform() throws Exception {
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithPartialTransform();
        SimpleFeatureBuilder fb = new SimpleFeatureBuilder(transformed.getSchema());
        fb.add(wkt.read("POINT (-120 40)").buffer(3));
        fb.add("baloon");
        fb.add(1);
        fb.add(1);
        SimpleFeature sf = fb.buildFeature("states_mini.11");
        
        // set the feature
        transformed.setFeatures(new CollectionFeatureReader(new SimpleFeature[] {sf}));
        
        // check it's there
        int size = STATES.getCount(Query.ALL);
        assertEquals(1, size);
        
        // the name won't be preserved since it's transformed, we use the population instead
        assertEquals(1, STATES.getFeatures(new Query(null, CQL.toFilter("persons = 1"))).size());
        assertEquals(1, transformed.getFeatures(new Query(null, CQL.toFilter("people = 1"))).size());
    }
    
    @Test
    public void testUpdateOnSelection() throws Exception {
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithSelection();
        
        // modify the feature
        transformed.modifyFeatures("persons", 0, CQL.toFilter("state_name = 'Illinois'"));
        
        // check it has been modified
        assertEquals(1, STATES.getFeatures(new Query(null, CQL.toFilter("persons = 0"))).size());
        SimpleFeatureCollection rfc = transformed.getFeatures(new Query(null, CQL.toFilter("persons = 0")));
        assertEquals(1, rfc.size());
        
        // double check the features themselves
        SimpleFeatureIterator fi = rfc.features();
        try {
            assertTrue(fi.hasNext());
            SimpleFeature sf = fi.next();
            assertEquals("Illinois", sf.getAttribute("state_name"));
        } finally {
            fi.close();
        }
    }
    
    @Test
    public void testUpdateOnRename() throws Exception {
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithRename();
        
        // modify the feature
        transformed.modifyFeatures("people", 0, CQL.toFilter("name = 'Illinois'"));
        
        // check it has been modified
        assertEquals(1, STATES.getFeatures(new Query(null, CQL.toFilter("persons = 0"))).size());
        SimpleFeatureCollection rfc = transformed.getFeatures(new Query(null, CQL.toFilter("people = 0")));
        assertEquals(1, rfc.size());
        
        // double check the features themselves
        SimpleFeatureIterator fi = rfc.features();
        try {
            assertTrue(fi.hasNext());
            SimpleFeature sf = fi.next();
            assertEquals("Illinois", sf.getAttribute("name"));
        } finally {
            fi.close();
        }
    }
    
    @Test
    public void testUpdateOnTransform() throws Exception {
        SimpleFeatureStore transformed = (SimpleFeatureStore) transformWithPartialTransform();
        
        // modify the feature
        transformed.modifyFeatures("people", 0, CQL.toFilter("name = 'illinois'"));
        
        // check it has been modified
        assertEquals(1, STATES.getFeatures(new Query(null, CQL.toFilter("persons = 0"))).size());
        SimpleFeatureCollection rfc = transformed.getFeatures(new Query(null, CQL.toFilter("people = 0")));
        assertEquals(1, rfc.size());
        
        // double check the features themselves
        SimpleFeatureIterator fi = rfc.features();
        try {
            assertTrue(fi.hasNext());
            SimpleFeature sf = fi.next();
            assertEquals("illinois", sf.getAttribute("name"));
        } finally {
            fi.close();
        }
    }
    
}
