/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.jdbc;

import org.geotools.data.DataUtilities;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.opengis.feature.Feature;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * 
 *
 * @source $URL$
 */
public abstract class JDBCPrimaryKeyFinderTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCPrimaryKeyFinderTestSetup createTestSetup();
    
    @Override
    protected void connect() throws Exception {
        super.connect();
        dataStore.setDatabaseSchema(null);
    }
    
    public void testSequencedPrimaryKey() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("seqtable"));
        
        assertEquals( 1, fs.getPrimaryKey().getColumns().size() );
        assertTrue( fs.getPrimaryKey().getColumns().get(0) instanceof SequencedPrimaryKeyColumn );
        
        ContentFeatureCollection features = fs.getFeatures();
        assertPrimaryKeyValues(features, 3);
        addFeature(fs.getSchema(),fs);
        assertPrimaryKeyValues(features,4);
    }

    public void testAssignedSinglePKeyView() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("assignedsinglepk"));
        
        assertEquals( 1, fs.getPrimaryKey().getColumns().size() );
        assertTrue( fs.getPrimaryKey().getColumns().get(0) instanceof NonIncrementingPrimaryKeyColumn );
        
        FeatureCollection features = fs.getFeatures();
        assertPrimaryKeyValues(features, 3);
    }
    
    public void testAssignedMultiPKeyView() throws Exception {
        JDBCFeatureStore fs = (JDBCFeatureStore) dataStore.getFeatureSource(tname("assignedmultipk"));
        
        assertEquals( 2, fs.getPrimaryKey().getColumns().size() );
        assertTrue( fs.getPrimaryKey().getColumns().get(0) instanceof NonIncrementingPrimaryKeyColumn );
        assertTrue( fs.getPrimaryKey().getColumns().get(1) instanceof NonIncrementingPrimaryKeyColumn );
        
        FeatureIterator i = fs.getFeatures().features();
        
        for ( int j = 1; i.hasNext(); j++ ) {
            SimpleFeature f = (SimpleFeature) i.next();
            
            assertEquals( tname("assignedmultipk") + "." + j + "." + (j + 1) , f.getID() );
        }
        
        i.close();
    }
    
    protected void addFeature( SimpleFeatureType featureType, JDBCFeatureStore features ) throws Exception {
        SimpleFeatureBuilder b = new SimpleFeatureBuilder( featureType );
        b.add("four");
        b.add( new GeometryFactory().createPoint( new Coordinate(4,4) ) );
        
        SimpleFeature f = b.buildFeature(null); 
        features.addFeatures( DataUtilities.collection( f ) );
        
        //pattern match to handle the multi primary key case
        assertTrue(((String)f.getUserData().get( "fid" )).matches( tname(featureType.getTypeName()) + ".4(\\..*)?"));
    }
    
    protected void assertPrimaryKeyValues( final FeatureCollection features, int count ) throws Exception {
        assertFeatureIterator(1,count,features.features(),new SimpleFeatureAssertion() {
            public int toIndex(SimpleFeature feature) {
                return Integer.parseInt(feature.getIdentifier().getID().split("\\.",2)[1]);
            }

            public void check(int index, SimpleFeature feature) {
                assertEquals( tname(features.getSchema().getName().getLocalPart()) + "." + index , feature.getIdentifier().getID() );
            }
        });
    }

}
