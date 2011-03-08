package org.geotools.data.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Point;

public class CSVDataStoreTest {

    @Test
    public void load() throws Exception {
        File file = TestData.file( this, "locations.csv");
        assertTrue( file.exists() );
        
        CSVDataStore csv  = new CSVDataStore( file );
        
        String[] names = csv.getTypeNames();
        assertEquals( 1, names.length );
        
        String typeName = names[0];
        assertEquals( "locations", typeName);
        
        SimpleFeatureType schema = csv.getSchema( typeName );
        assertEquals( 3, schema.getAttributeCount() );
        assertTrue( schema.getGeometryDescriptor().getType().getBinding().isAssignableFrom( Point.class));
        
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
            String expected = "locations.4";
            System.out.println( found );
            assertTrue( expected, found.contains( expected));
        }
        finally {
            cursor.close();
        }
    }
    
}