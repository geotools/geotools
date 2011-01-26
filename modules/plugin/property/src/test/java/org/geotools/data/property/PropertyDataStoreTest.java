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
package org.geotools.data.property;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

/**
 * Test functioning of PropertyDataStore.
 * 
 * @author Jody Garnett, Refractions Research Inc.
 * @source $URL$
 */
public class PropertyDataStoreTest extends TestCase {
    PropertyDataStore store;
    
    static FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory(null);

    /**
     * Constructor for SimpleDataStoreTest.
     * @param arg0
     */
    public PropertyDataStoreTest(String arg0) {
        super(arg0);
    }
    protected void setUp() throws Exception {
        File dir = new File(".", "propertyTestData" );
        dir.mkdir();
               
        File file = new File( dir ,"road.properties");
        if( file.exists()){
            file.delete();
        }        
        BufferedWriter writer = new BufferedWriter( new FileWriter( file ) );
        writer.write("_=id:Integer,name:String"); writer.newLine();
        writer.write("fid1=1|jody"); writer.newLine();
        writer.write("fid2=2|brent"); writer.newLine();
        writer.write("fid3=3|dave"); writer.newLine();
        writer.write("fid4=4|justin");
        writer.close();
        
        file = new File( dir ,"dots.in.name.properties");
        if( file.exists()){
            file.delete();
        }        
        writer = new BufferedWriter( new FileWriter( file ) );
        writer.write("_=id:Integer,name:String"); writer.newLine();
        writer.write("fid1=1|jody"); writer.newLine();
        writer.write("fid2=2|brent"); writer.newLine();
        writer.write("fid3=3|dave"); writer.newLine();
        writer.write("fid4=4|justin");
        writer.close();
        
        store = new PropertyDataStore( dir );
        super.setUp();
    }
    protected void tearDown() throws Exception {
        File dir = new File( "propertyTestData" );
        File list[]=dir.listFiles();
        for( int i=0; i<list.length;i++){
            list[i].delete();
        }
        dir.delete();
        super.tearDown();                
    }

    public void testGetNames() {
        String names[] = store.getTypeNames();
        Arrays.sort(names);
        assertEquals( 2, names.length );
        assertEquals( "dots.in.name", names[0] );
        assertEquals( "road", names[1] );              
    }

    public void testGetSchema() throws IOException {
        SimpleFeatureType type = store.getSchema( "road" );
        assertNotNull( type );
        assertEquals( "road", type.getTypeName() );
        assertEquals( "propertyTestData", type.getName().getNamespaceURI().toString() );
        assertEquals( 2, type.getAttributeCount() );
        
        AttributeDescriptor id = type.getDescriptor(0);        
        AttributeDescriptor name = type.getDescriptor(1);
        
        assertEquals( "id", id.getLocalName() );
        assertEquals( "class java.lang.Integer", id.getType().getBinding().toString() );
                
        assertEquals( "name", name.getLocalName() );
        assertEquals( "class java.lang.String", name.getType().getBinding().toString() );                        
    }
    public void testGetFeaturesFeatureTypeFilterTransaction1() throws Exception {
        SimpleFeatureType type = store.getSchema( "road" );
        Query roadQuery = new DefaultQuery("road");
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader( roadQuery, Transaction.AUTO_COMMIT );
        int count = 0;
        try {
            while( reader.hasNext() ){
                reader.next();
                count++;
            }
        }
        finally {
            reader.close();
        }
        assertEquals( 4, count );
        
        Filter selectFid1;
        
        selectFid1 = ff.id( Collections.singleton( ff.featureId("fid1") ) );
        reader = store.getFeatureReader( new DefaultQuery("road", selectFid1 ), Transaction.AUTO_COMMIT );
        assertEquals( 1, count( reader ) );
        
        Transaction transaction = new DefaultTransaction();
        reader = store.getFeatureReader( roadQuery, transaction );
        assertEquals( 4, count( reader ));
        
        reader = store.getFeatureReader( roadQuery, transaction );
        List list = new ArrayList();
        try {
            while( reader.hasNext() ){
                list.add( reader.next().getID() );
            }
        }
        finally {
            reader.close();
        }
        assertEquals( "[fid1, fid2, fid3, fid4]", list.toString() );        
    }
    /*
     * Test for  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(String)
     */
    public void testGetFeatureReaderString() throws NoSuchElementException, IOException, IllegalAttributeException {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = store.getFeatureReader("road");
        int count = 0;
        try {
            while( reader.hasNext() ){
                reader.next();                
                count++;
            }
        }
        finally {
            reader.close();
        }
        assertEquals( 4, count );
    }
    private int count(  FeatureReader<SimpleFeatureType, SimpleFeature> reader ) throws Exception {
        int count = 0;
        try {
            while( reader.hasNext() ){
                reader.next();
                count++;
            }
        }
        finally {
            reader.close();
        }
        return count;        
    }    
    private int count( String typeName ) throws Exception {
        return count( store.getFeatureReader( typeName ) );                
    }
    
    public void testWriterSkipThrough() throws Exception {
        PropertyFeatureWriter writer = (PropertyFeatureWriter)
            store.getFeatureWriter("road");
            
        File in = writer.read;
        File out = writer.write;
        
        int count = 0;
        while( writer.hasNext() ){
            writer.next();
            count++;
        }
        assertEquals( 4, count );
        assertTrue( in.exists() );
        assertTrue( out.exists() );
        writer.close();
        assertTrue( in.exists() );        
        
        assertEquals( 4, count( "road" ) );
    }
    public void testWriterChangeName() throws Exception{
        PropertyFeatureWriter writer = (PropertyFeatureWriter)
            store.getFeatureWriter("road");
            
        int count = 0;
        while( writer.hasNext() ){
            SimpleFeature f = writer.next();
            f.setAttribute(1,"name "+(count+1));
            writer.write();
            count++;
        }                
        writer.close();        
        assertEquals( 4, count );
        assertEquals( 4, count( "road" ));                
    }
    public void testWriterChangeFirstName() throws Exception{
        PropertyFeatureWriter writer = (PropertyFeatureWriter)
                    store.getFeatureWriter("road");
        SimpleFeature f;
        f = writer.next();
        f.setAttribute(1,"changed");
        writer.write();
        writer.close();                           
        assertEquals( 4, count( "road" ));    
    }
    public void testWriterChangeLastName() throws Exception{
        PropertyFeatureWriter writer = (PropertyFeatureWriter)
                    store.getFeatureWriter("road");
        SimpleFeature f;
        writer.next();
        writer.next();
        writer.next();        
        f = writer.next();
        f.setAttribute(1,"changed");
        writer.write();
        writer.close();                           
        assertEquals( 4, count( "road" ));    
    }    
    public void testWriterChangeAppend() throws Exception{
        PropertyFeatureWriter writer = (PropertyFeatureWriter)
                    store.getFeatureWriter("road");
        SimpleFeature f;
        writer.next();
        writer.next();
        writer.next();
        writer.next();
        assertFalse( writer.hasNext() );
        f = writer.next();
        assertNotNull( f );
        f.setAttribute(0,new Integer(-1));        
        f.setAttribute(1,"new");
        writer.write();
        writer.close();
        assertEquals( 5, count( "road" ));    
    }
    public void testWriterAppendLastNull() throws Exception{
        FeatureWriter<SimpleFeatureType, SimpleFeature> writer = (FeatureWriter)
                    store.getFeatureWriterAppend("road", Transaction.AUTO_COMMIT);
        SimpleFeature f;
        assertFalse( writer.hasNext() );
        f = writer.next();
        assertNotNull( f );
        f.setAttribute(0,new Integer(-1));        
        f.setAttribute(1,null); // this made the datastore break
        writer.write();
        writer.close();
        assertEquals( 5, count( "road" ));    
    }
    public void testWriterChangeRemoveFirst() throws Exception{
        PropertyFeatureWriter writer = (PropertyFeatureWriter)
                    store.getFeatureWriter("road");
        
        writer.next();
        writer.remove();
        writer.close();
        assertEquals( 3, count( "road" ));    
    }
    public void testWriterChangeRemoveLast() throws Exception{
        PropertyFeatureWriter writer = (PropertyFeatureWriter)
                    store.getFeatureWriter("road");
        
        writer.next();
        writer.next();
        writer.next();
        writer.remove();
        writer.close();
        assertEquals( 3, count( "road" ));    
    }
    public void testWriterChangeRemoveAppend() throws Exception{
        PropertyFeatureWriter writer = (PropertyFeatureWriter)
                    store.getFeatureWriter("road");
        SimpleFeature f;
        writer.next();
        writer.next();
        writer.next();
        writer.next();        
                
        assertFalse( writer.hasNext() );
        f = writer.next();
        assertNotNull( f );
        f.setAttribute(0,new Integer(-1));        
        f.setAttribute(1,"new");
        writer.remove();
        writer.close();
        assertEquals( 4, count( "road" ));                    
    }
    public void testWriterChangeIgnoreAppend() throws Exception{
        PropertyFeatureWriter writer = (PropertyFeatureWriter)
                    store.getFeatureWriter("road");
        SimpleFeature f;
        writer.next();
        writer.next();
        writer.next();
        writer.next();
        assertFalse( writer.hasNext() );
        f = writer.next();
        assertNotNull( f );
        f.setAttribute(0,new Integer(-1));        
        f.setAttribute(1,"new");        
        writer.close();
        assertEquals( 4, count( "road" ));                    
    }
    
    public void testGetFeatureSource() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource( "road" );
        SimpleFeatureCollection features = road.getFeatures();
        SimpleFeatureIterator reader = features.features();
        List list = new ArrayList();
        try {
            while( reader.hasNext() ){
                list.add( reader.next().getID() );                
            }
        } finally {
            reader.close();
        }
        assertEquals( "[fid1, fid2, fid3, fid4]", list.toString() );
        assertEquals( 4, road.getCount(Query.ALL) );
        assertEquals( null, road.getBounds(Query.ALL) );
        assertEquals( 4, features.size() );
        assertTrue( features.getBounds().isNull() );
        assertEquals( 4, features.size() );
                
    }
    private void dir( File file ){
        File dir;
        if( file.isDirectory() ){
            dir = file;
        }
        else{
            dir = file.getParentFile();
        }
        if( dir != null){
            String ls[] = dir.list();
            System.out.println( "Directory "+dir );
            for( int i=0; i<ls.length;i++){
                System.out.println( ls[i] );
            }
        }
    }

    public void testTransactionIndependence() throws Exception {
        SimpleFeatureType ROAD = store.getSchema( "road" );
        SimpleFeature chrisFeature =
            SimpleFeatureBuilder.build(ROAD, new Object[]{ new Integer(5), "chris"}, "fid5" );
        
        SimpleFeatureStore roadAuto = (SimpleFeatureStore) store.getFeatureSource("road");
        
        SimpleFeatureStore roadFromClient1 = (SimpleFeatureStore) store.getFeatureSource("road");
        Transaction transaction1 = new DefaultTransaction("Transaction Used by Client 1");
        roadFromClient1.setTransaction( transaction1 );
        
        SimpleFeatureStore roadFromClient2 = (SimpleFeatureStore) store.getFeatureSource("road");
        Transaction transaction2 = new DefaultTransaction("Transaction Used by Client 2");
        roadFromClient2.setTransaction( transaction2 );

        FilterFactory2 ff = (FilterFactory2) CommonFactoryFinder.getFilterFactory(null);
        Filter selectFid1 = ff.id( Collections.singleton( ff.featureId("fid1") ));
        Filter selectFid2 = ff.id( Collections.singleton( ff.featureId("fid2") ));        
        
            
        // Before we edit everything should be the same
        assertEquals( "auto before", 4, roadAuto.getFeatures().size() );
        assertEquals( "client 1 before", 4, roadFromClient1.getFeatures().size() );
        assertEquals( "client 2 before", 4, roadFromClient2.getFeatures().size() );

        // Remove Feature with Fid1
        roadFromClient1.removeFeatures( selectFid1 ); // road1 removes fid1 on t1
        
        assertEquals( "auto after client 1 removes fid1", 4, roadAuto.getFeatures().size() );
        assertEquals( "client 1 after client 1 removes fid1", 3, roadFromClient1.getFeatures().size() );
        assertEquals( "client 2 after client 1 removes fid1", 4, roadFromClient2.getFeatures().size() );               
        
        
        roadFromClient2.addFeatures( DataUtilities.collection( chrisFeature )); // road2 adds fid5 on t2    
        assertEquals( "auto after client 1 removes fid1 and client 2 adds fid5", 4, roadAuto.getFeatures().size() );
        assertEquals( "client 1 after client 1 removes fid1 and client 2 adds fid5", 3, roadFromClient1.getFeatures().size() );
        assertEquals( "cleint 2 after client 1 removes fid1 and client 2 adds fid5", 5, roadFromClient2.getFeatures().size() );        

        transaction1.commit();
        assertEquals( "auto after client 1 commits removal of fid1 (client 2 has added fid5)", 3, roadAuto.getFeatures().size() );
        assertEquals( "client 1 after commiting removal of fid1 (client 2 has added fid5)", 3, roadFromClient1.getFeatures().size() );
        assertEquals( "client 2 after client 1 commits removal of fid1 (client 2 has added fid5)", 4, roadFromClient2.getFeatures().size() );                
            
        transaction2.commit();
        assertEquals( "auto after client 2 commits addition of fid5 (fid1 previously removed)", 4, roadAuto.getFeatures().size() );
        assertEquals( "client 1 after client 2 commits addition of fid5 (fid1 previously removed)", 4, roadFromClient1.getFeatures().size() );
        assertEquals( "client 2 after commiting addition of fid5 (fid1 previously removed)", 4, roadFromClient2.getFeatures().size() );
    }
    
    public void testUseExistingFid() throws Exception {
        SimpleFeatureType ROAD = store.getSchema( "road" );
        SimpleFeature chrisFeature = SimpleFeatureBuilder.build(ROAD, new Object[]{ new Integer(5), "chris"}, "fid5" );
        chrisFeature.getUserData().put(Hints.USE_PROVIDED_FID, Boolean.TRUE);
        
        SimpleFeatureStore roadAuto = (SimpleFeatureStore) store.getFeatureSource("road");
        List<FeatureId> fids = roadAuto.addFeatures(DataUtilities.collection(chrisFeature));
        
        // checke the id was preserved
        assertEquals(1, fids.size());
        FeatureId fid = SimpleFeatureBuilder.createDefaultFeatureIdentifier("fid5");
        assertTrue(fids.contains(fid));
        
        // manually check the feature with the proper id is actually there
        SimpleFeatureIterator it = roadAuto.getFeatures(ff.id(Collections.singleton(fid))).features();
        assertTrue(it.hasNext());
        SimpleFeature sf = it.next();
        it.close();
        assertEquals(fid, sf.getIdentifier());
    }
}
