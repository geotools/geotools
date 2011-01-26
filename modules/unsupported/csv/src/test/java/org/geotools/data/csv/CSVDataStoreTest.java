package org.geotools.data.csv;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.File;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.test.TestData;
import org.junit.Test;
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
    }
}