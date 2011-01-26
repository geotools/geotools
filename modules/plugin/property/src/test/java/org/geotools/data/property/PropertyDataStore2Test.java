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

import junit.framework.TestCase;

import org.geotools.data.DefaultQuery;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.referencing.CRS;
import org.opengis.feature.Property;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.feature.type.GeometryType;
import org.opengis.filter.Filter;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Geometry;

/**
 * Test non functionality of PropertyDataStore.
 * 
 * @author Jody Garnett, Refractions Research Inc.
 * @source $URL$
 */
public class PropertyDataStore2Test extends TestCase {
    PropertyDataStore store;
    
    PropertyDataStore sridStore;
    /**
     * Constructor for SimpleDataStoreTest.
     * @param arg0
     */
    public PropertyDataStore2Test(String arg0) {
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

        super.setUp();
    }
    protected void tearDown() throws Exception {
        File dir = new File( "propertyTestData" );
        File list[]=dir.listFiles();
        for( int i=0; i<list.length;i++){
            list[i].delete();
        }
        dir.delete();
        
        dir = new File( "propertyTestData2" );
        File list2[] = dir.listFiles();
        for( int i=0; i<list2.length;i++){
            list2[i].delete();
        }
        dir.delete();
        
        super.tearDown();                
    }
    
    /**
     * Test CRS being passed into Geometry user data.
     * 
     * @throws Exception
     */
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
      
    public void testSimple() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource( "road" );
        SimpleFeatureCollection features = road.getFeatures();
        
        //assertEquals( 1, features.getFeatureType().getAttributeCount() );
        assertEquals( 4, features.size() );
    }
    public void testQuery() throws Exception {
        SimpleFeatureSource road = store.getFeatureSource( "road" );
                
        DefaultQuery query = new DefaultQuery( "road", Filter.INCLUDE,
                new String[]{ "name" } );
        
        SimpleFeatureCollection features = road.getFeatures( query );
        assertEquals( 4, features.size() );
        //assertEquals( 1, features.getFeatureType().getAttributeCount() );
    }
    
    public void testQueryReproject() throws Exception {
        CoordinateReferenceSystem world = CRS.decode("EPSG:4326"); // world lon/lat
        CoordinateReferenceSystem local = CRS.decode("EPSG:3005"); // british columbia
        
        
        SimpleFeatureSource road = store.getFeatureSource( "road" );
        SimpleFeatureType origionalType = road.getSchema();
        
        DefaultQuery query = new DefaultQuery( "road", Filter.INCLUDE,
                new String[]{ "geom", "name" } );
        
        query.setCoordinateSystem( local ); // FROM
        query.setCoordinateSystemReproject( world ); // TO
                
        SimpleFeatureCollection features = road.getFeatures( query );
        SimpleFeatureType resultType = features.getSchema();
        
        
        assertNotNull( resultType );
        assertNotSame( resultType, origionalType );

        assertEquals( world, resultType.getCoordinateReferenceSystem() );

        GeometryDescriptor geometryDescriptor = resultType.getGeometryDescriptor();        
    }
}
