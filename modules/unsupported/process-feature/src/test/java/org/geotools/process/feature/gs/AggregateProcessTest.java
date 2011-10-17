package org.geotools.process.feature.gs;


import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.EnumSet;
import java.util.Set;

import org.geotools.data.DataStore;
import org.geotools.data.property.PropertyDataStore;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.process.feature.gs.AggregateProcess.AggregationFunction;
import org.geotools.process.feature.gs.AggregateProcess.Results;
import org.geotools.test.TestData;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 *
 * @source $URL$
 */
public class AggregateProcessTest {
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
        SimpleFeatureSource source = bugs.getFeatureSource("bugsites");

        Set<AggregationFunction> functions = EnumSet.of(AggregationFunction.Sum);
        
        Results result = AggregateProcess.process( source.getFeatures(), "cat",  functions, true, null );
        assertTrue( result.sum > 0 );
    }

}
