package org.geotools.process.feature.gs;

import static org.junit.Assert.*;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.NameImpl;
import org.geotools.process.Process;
import org.geotools.process.Processors;
import org.geotools.process.feature.gs.AggregateProcess.AggregationFunction;
import org.geotools.process.feature.gs.AggregateProcess.Results;
import org.geotools.test.TestData;
import org.geotools.util.KVP;
import org.geotools.util.NullProgressListener;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class FeatureGSProcessFactoryFactoryTest {

    DataStore bugs;
    
    @Before
    public void setup() throws IOException {
        File file = TestData.file(this, null );
        bugs = new PropertyDataStore( file );
    }
    @After
    public void tearDown(){
        bugs.dispose();
    }
    
    @Test
    public void testSum() throws Exception {
        String typeName = bugs.getTypeNames()[0];
        SimpleFeatureSource source = bugs.getFeatureSource( typeName );
        
        Map<String,Object> input = new KVP(
                "features", source.getFeatures(),
                "aggregationAttribute","cat",
                "function",EnumSet.of(AggregationFunction.Sum),
                "singlePass", true);
        
        NameImpl name = new NameImpl("gs","Aggregate");
        Process process = Processors.createProcess( name );
        assertNotNull("aggregateProcess not found", process);
        NullProgressListener monitor = new NullProgressListener();
        Map<String, Object> output = process.execute(input, monitor );
        
        Results result = (Results) output.get("result");
        assertTrue( result.sum > 0 );
    }
    
}
