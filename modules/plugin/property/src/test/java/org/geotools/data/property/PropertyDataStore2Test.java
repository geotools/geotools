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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.Hints;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.JTSFactoryFinder;
import org.geotools.referencing.CRS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

/**
 * Test non functionality of PropertyDataStore (problems and enhancements requested through JIRA).
 * 
 * @author Jody Garnett (LISAsoft)
 *
 *
 * @source $URL$
 */
public class PropertyDataStore2Test {
    PropertyDataStore store;
    
    PropertyDataStore sridStore;

    private PropertyDataStore unusalStore;

    @Before
    public void setUp() throws Exception {
        File dir = new File(".", "propertyTestData" );
        dir.mkdir();
               
        File file = new File( dir ,"road.properties");
        if( file.exists()){
            file.delete();
        }        
        BufferedWriter writer = new BufferedWriter( new FileWriter( file ) );
        writer.write("_=id:Integer,*geom:Geometry,name:String"); writer.newLine();
        writer.write("fid1=1|LINESTRING(0 0,10 10)|jody"); writer.newLine();
        writer.write("fid2=2|LINESTRING(20 20,30 30)|brent"); writer.newLine();
        writer.write("fid3=3|LINESTRING(5 0, 5 10)|dave"); writer.newLine();
        writer.write("fid4=4|LINESTRING(0 5, 5 0, 10 5, 5 10, 0 5)|justin");
        writer.close();
        store = new PropertyDataStore( dir );
        
        // Create a similar data store but with srid in the geometry column
        File dir2 = new File(".", "propertyTestData2");
        dir2.mkdir();
        File file2 = new File(dir2, "road2.properties");
        if (file2.exists()) {
            file2.delete();
        }
        BufferedWriter writer2 = new BufferedWriter(new FileWriter(file2));
        writer2.write("_=id:Integer,geom:Geometry:srid=4283");
        writer2.newLine();
        writer2.write("fid1=1|LINESTRING(0 0,10 10)");
        writer2.newLine();
        writer2.write("fid2=2|LINESTRING(20 20,30 30)");
        writer2.newLine();
        writer2.write("fid3=3|LINESTRING(5 0, 5 10)");
        writer2.newLine();
        writer2.write("fid4=4|LINESTRING(0 5, 5 0, 10 5, 5 10, 0 5)");
        writer2.close();
        sridStore = new PropertyDataStore(dir2);

        // Create a similar data store with various unusal problems
        File dir3 = new File(".", "propertyTestData3");
        dir3.mkdir();
        File file3 = new File( dir3 ,"unusual.properties");
        if( file3.exists()){
            file3.delete();
        }
        
        BufferedWriter writer3 = new BufferedWriter( new FileWriter( file3 ) );
        writer3.write("_=id:Integer,*geom:Geometry,data:String"); writer3.newLine();
        writer3.write("fid1=1|LINESTRING(0 0,10 10)|feeling a bit \\|broken up"); writer3.newLine();
        writer3.write("fid2=2|\\\nLINESTRING(20 20,30 30)|\\\nnew line\\\ndiff friendly");writer3.newLine();
        writer3.close();
        unusalStore = new PropertyDataStore( dir3 );
        
        // listFileContents( file3 );
    }
    
    @After
    public void tearDown() throws Exception {
        store.dispose();
        File dir = new File( "propertyTestData" );
        for( File file : dir.listFiles()){
            file.delete();
        }
        dir.delete();

        sridStore.dispose();
        dir = new File( "propertyTestData2" );
        for( File file : dir.listFiles()){
            file.delete();
        }
        dir.delete();

        unusalStore.dispose();
        dir = new File( "propertyTestData3" );
        for( File file : dir.listFiles()){
            file.delete();
        }
        dir.delete();
    }
    
    /**
     * Test CRS being passed into Geometry user data.
     * 
     * @throws Exception
     */
    @Test
    public void testCRS() throws Exception {
        SimpleFeatureSource road = sridStore.getFeatureSource("road2");
        SimpleFeatureCollection features = road.getFeatures();
        assertEquals(4, features.size());

        SimpleFeature feature;
        Geometry geom;
        Property prop;
        GeometryType geomType;
        SimpleFeatureIterator iterator = features.features();
        while (iterator.hasNext()) {
            feature = iterator.next();
            prop = feature.getProperty("geom");
            assertTrue(prop.getType() instanceof GeometryType);
            geomType = (GeometryType) prop.getType();

            Object val = prop.getValue();
            assertTrue(val != null && val instanceof Geometry);
            geom = (Geometry) val;

            Object userData = geom.getUserData();
            assertTrue(userData != null && userData instanceof CoordinateReferenceSystem);
            // ensure the same CRS is passed on to userData for encoding
            assertEquals(userData, geomType.getCoordinateReferenceSystem());
        }
    }
    @Test
    public void unusual() throws Exception {
        SimpleFeatureSource road = unusalStore.getFeatureSource( "unusual" );
        assertEquals( 3, road.getSchema().getAttributeCount() );

        SimpleFeatureCollection features = road.getFeatures();
        assertFalse( features.isEmpty() );
        
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        
        SimpleFeatureCollection select = road.getFeatures( ff.id( ff.featureId("fid1")));
        SimpleFeatureIterator iterator = select.features();
        SimpleFeature feature1 = iterator.next();
        assertNotNull( feature1 );
        iterator.close();

        select = road.getFeatures( ff.id( ff.featureId("fid2")));
        iterator = select.features();
        SimpleFeature feature2 = iterator.next();
        assertNotNull( feature2 );
        iterator.close();
        
        // Encode |
        assertEquals("feeling a bit |broken up", feature1.getAttribute("data"));
        
        // Encode newline; but still respect trim of white space
        assertTrue( feature2.getAttribute("geom") instanceof LineString );
        assertEquals("\nnew line\ndiff friendly", feature2.getAttribute("data"));
    }
    @Test

    public void testSimple() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource( "road" );
        SimpleFeatureCollection features = road.getFeatures();
        
        //assertEquals( 1, features.getFeatureType().getAttributeCount() );
        assertEquals( 4, features.size() );
    }
    @Test

    public void testQuery() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource( "road" );
                
        Query query = new Query( "road", Filter.INCLUDE,
                new String[]{ "name" } );
        
        SimpleFeatureCollection features = road.getFeatures( query );
        assertEquals( 4, features.size() );
        //assertEquals( 1, features.getFeatureType().getAttributeCount() );
    }
    @Test
    public void testQueryReproject() throws Exception {
        CoordinateReferenceSystem world = CRS.decode("EPSG:4326"); // world lon/lat
        CoordinateReferenceSystem local = CRS.decode("EPSG:3005"); // british columbia
        
        
        SimpleFeatureSource road = store.getFeatureSource( "road" );
        SimpleFeatureType origionalType = road.getSchema();
        
        Query query = new Query( "road", Filter.INCLUDE,
                new String[]{ "geom", "name" } );
        
        query.setCoordinateSystem( local ); // FROM
        query.setCoordinateSystemReproject( world ); // TO
                
        SimpleFeatureCollection features = road.getFeatures( query );
        SimpleFeatureType resultType = features.getSchema();
        
        
        assertNotNull( resultType );
        assertNotSame( resultType, origionalType );

        assertEquals( world, resultType.getCoordinateReferenceSystem() );

        GeometryDescriptor geometryDescriptor = resultType.getGeometryDescriptor();        
        assertTrue( Geometry.class.isAssignableFrom( geometryDescriptor.getType().getBinding() ) );
    }
    @Test
    public void testUnusalRoundTrip() throws Throwable {

        File target = new File( "propertyTestData3/trip.properties" );
        if( target.exists() ){
            boolean deleted = target.delete();
            assertTrue( "unable to delete "+target.getAbsolutePath(), deleted );
        }
        assertFalse( "trip.properties should not exist yet", target.exists() );
        
        
        SimpleFeatureType schema = DataUtilities.createType("trip", "point:Point::srid=4326,text:String, number:Integer");
        SimpleFeatureBuilder builder = new SimpleFeatureBuilder( schema );
        GeometryFactory gf = JTSFactoryFinder.getGeometryFactory();
        
        List<SimpleFeature> list = new ArrayList<SimpleFeature>();
        SimpleFeature feature;
        
        feature = builder.buildFeature("trip1", new Object[]{gf.createPoint( new Coordinate(0,0)), "hello world", 1 });
        feature.getUserData().put( Hints.USE_PROVIDED_FID, true );
        list.add( feature);
        
        feature = builder.buildFeature("trip2", new Object[]{gf.createPoint( new Coordinate(0,0)), "test if | chracter handling", 2 });
        feature.getUserData().put( Hints.USE_PROVIDED_FID, true );
        list.add( feature );
        
        feature= builder.buildFeature("trip3", new Object[]{gf.createPoint( new Coordinate(0,0)), "test of\n multi-line handling", 3 });
        feature.getUserData().put( Hints.USE_PROVIDED_FID, true );
        list.add( feature );

        feature = builder.buildFeature("trip4", new Object[]{gf.createPoint( new Coordinate(0,0)), "    test of\n whitespace handling", 4 });
        feature.getUserData().put( Hints.USE_PROVIDED_FID, true );
        list.add( feature );
        
        feature = builder.buildFeature("trip5", new Object[]{gf.createPoint( new Coordinate(0,0)), "test encoding does not get confused over \\n newline and \\twhite space and \\| other markers", 5 });
        feature.getUserData().put( Hints.USE_PROVIDED_FID, true );
        list.add( feature );
        
        feature = builder.buildFeature("trip6", new Object[]{gf.createPoint( new Coordinate(0,0)), "How well can we encode 1\\2?", 5 });
        feature.getUserData().put( Hints.USE_PROVIDED_FID, true );
        list.add( feature );
        
        ListFeatureCollection features = new ListFeatureCollection(schema, list );
        
        unusalStore.createSchema( schema );
        SimpleFeatureStore trip = (SimpleFeatureStore) unusalStore.getFeatureSource("trip");
        
        trip.addFeatures( features );
        assertTrue( "trip.properties created", target.exists() );
        
        // listFileContents(target);
        
        assertEquals("stored", list.size(), trip.getCount(Query.ALL));
        
        final Map<String,SimpleFeature> cache = new HashMap<String,SimpleFeature>();
        SimpleFeatureCollection readFeatures = trip.getFeatures();
        
        FeatureVisitor cacheResults = new FeatureVisitor() {
            @Override
            public void visit(Feature f) {
                SimpleFeature feature = (SimpleFeature) f;
                cache.put( feature.getID(), feature );
            }
        };
        readFeatures.accepts( cacheResults, null );
        assertEquals( "restored", list.size(), cache.size() );
        assertEquals( "hello world", cache.get("trip1").getAttribute("text"));
        assertEquals( "test if | chracter handling", cache.get("trip2").getAttribute("text"));
        assertEquals( "test of\n multi-line handling", cache.get("trip3").getAttribute("text"));
        assertEquals( "    test of\n whitespace handling", cache.get("trip4").getAttribute("text"));
        
        // need some regex magic to make this work
        //assertEquals( "test encoding does not get confused over \\n newline and \\twhite space and \\| other markers", cache.get("trip5").getAttribute("text"));
        assertEquals( "How well can we encode 1\\2?", cache.get("trip6").getAttribute("text"));
    }

    private void listFileContents(File target) throws FileNotFoundException, IOException {
        System.out.println("Contents of "+target );
        if( target.exists() ){
            BufferedReader reader = new BufferedReader( new FileReader( target ) );
            String line;
            while( (line = reader.readLine()) != null ){
                System.out.println( line );
            }
            reader.close();
        }
        else {
            System.out.println(" ... does not exist");
        }
    }
}
