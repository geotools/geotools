/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, David Zwiers
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
package org.geotools.data.wfs.v1_0_0;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.logging.Level;
import java.util.logging.Logger;

import junit.framework.TestCase;

import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Envelope;

/**
 * <p> 
 * DOCUMENT ME!
 * </p>
 * @author dzwiers
 *
 *
 * @source $URL$
 */
public class WFSDataStoreReadTest extends TestCase {
    
    public WFSDataStoreReadTest(){
        Logger.global.setLevel(Level.SEVERE);
    }
    
    public void testEmpty(){/**/}

    public static WFS_1_0_0_DataStore getDataStore(URL server) throws IOException{
        try{
        Map m = new HashMap();
        m.put(WFSDataStoreFactory.URL.key,server);
        m.put(WFSDataStoreFactory.TIMEOUT.key,new Integer(10000)); // not debug
        m.put(WFSDataStoreFactory.TIMEOUT.key,new Integer(1000000)); //for debug
        return (WFS_1_0_0_DataStore)(new WFSDataStoreFactory()).createDataStore(m);

        }catch(java.net.SocketException se){
            se.printStackTrace();
            return null;
        }
    }
        
    public static void doFeatureType(URL url,boolean get, boolean post, int i) throws IOException, SAXException{
    	if( url == null) return;
        try{
        WFS_1_0_0_DataStore wfs = getDataStore(url);
        System.out.println("FeatureTypeTest + "+url);
        assertNotNull("No featureTypes",wfs.getTypeNames());
        
        String typeName = wfs.getTypeNames()[i];
        assertNotNull("Null featureType in ["+i+"]",typeName);
        
        //System.out.println("FT name = "+typeName);
        if(get){
            // get
            SimpleFeatureType ft = wfs.getSchemaGet(typeName);
            assertNotNull("GET DescribeFeatureType for "+typeName+" was null",ft);
            assertTrue("GET "+typeName+" must have 1 geom and atleast 1 other attribute -- fair assumption",ft.getGeometryDescriptor()!=null && ft.getAttributeDescriptors()!=null && ft.getAttributeCount()>0);
        }
        if(post){
            // post
            SimpleFeatureType ft = wfs.getSchemaPost(typeName);
            assertNotNull("POST DescribeFeatureType for "+typeName+" resulted in null",ft);
            assertTrue("POST "+typeName+" must have 1 geom and atleast 1 other attribute -- fair assumption",ft.getGeometryDescriptor()!=null && ft.getAttributeDescriptors()!=null && ft.getAttributeCount()>0);
        }
        }catch(java.net.SocketException se){
            se.printStackTrace();
        }
    }
    
    public static void doFeatureReader(URL url, boolean get, boolean post, int i) throws NoSuchElementException, IOException, IllegalAttributeException, SAXException{
    	if( url == null) return;
    	try{
        System.out.println("FeatureReaderTest + "+url);
        WFS_1_0_0_DataStore wfs = getDataStore(url);
        assertNotNull("No featureTypes",wfs.getTypeNames());
        assertNotNull("Null featureType in [0]",wfs.getTypeNames()[i]);
        Query query = new DefaultQuery(wfs.getTypeNames()[i]);
        
        if(post){
        // 	post
             FeatureReader<SimpleFeatureType, SimpleFeature> ft = wfs.getFeatureReaderPost(query,Transaction.AUTO_COMMIT);
            assertNotNull("FeatureType was null",ft);
            assertTrue("must have 1 feature -- fair assumption",ft.hasNext() && ft.getFeatureType()!=null && ft.next()!=null);
            // disable for now
//            assertNotNull("CRS missing ",ft.getFeatureType().getDefaultGeometry().getCoordinateSystem());
            ft.close();
        }
        if(get){
        // 	get
             FeatureReader<SimpleFeatureType, SimpleFeature> ft = wfs.getFeatureReaderGet(query,Transaction.AUTO_COMMIT);
            assertNotNull("FeatureType was null",ft);
            assertTrue("must have 1 feature -- fair assumption",ft.hasNext() && ft.getFeatureType()!=null && ft.next()!=null);
            // disable for now
//            assertNotNull("CRS missing ",ft.getFeatureType().getDefaultGeometry().getCoordinateSystem());
            ft.close();}
        }catch(java.net.SocketException se){
            se.printStackTrace();
        }
    }
    
    public static void doFeatureReaderWithQuery(URL url, boolean get, boolean post, int i) throws NoSuchElementException, IllegalAttributeException, IOException, SAXException{
    	if( url == null) return;
    	try{
        System.out.println("FeatureReaderWithFilterTest + "+url);
        WFS_1_0_0_DataStore wfs = getDataStore(url);
        assertNotNull("No featureTypes",wfs.getTypeNames());
        assertNotNull("Null featureType in [0]",wfs.getTypeNames()[i]);
        SimpleFeatureType ft = wfs.getSchema(wfs.getTypeNames()[i]);
        // take atleast attributeType 3 to avoid the undeclared one .. inherited optional attrs
        
        String[] props;
        props = new String[] {ft.getGeometryDescriptor().getLocalName()};
        
        DefaultQuery query = new DefaultQuery(ft.getTypeName());
        query.setPropertyNames(props);
        String fid=null;
        if(get){
            // 	get
             FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderGet(query,Transaction.AUTO_COMMIT);
            try{
                assertNotNull("FeatureType was null",ft);
                
                SimpleFeatureType featureType = fr.getFeatureType();
                if( ft.getAttributeCount()>1 ){
                    assertEquals("Query must restrict feature type to only having 1 AttributeType", 1, featureType.getAttributeCount() );
                }
                assertTrue("must have 1 feature -- fair assumption",fr.hasNext() && featureType!=null );
                SimpleFeature feature = fr.next();
                featureType=feature.getFeatureType();
                if( ft.getAttributeCount()>1 ){
                    assertEquals("Query must restrict feature type to only having 1 AttributeType", 1, featureType.getAttributeCount() );
                }
                assertNotNull( "must have 1 feature ", feature);
                fid=feature.getID();
                int j=0;while(fr.hasNext()){ 
                    fr.next();
                    j++;
                }
                System.out.println(j+" Features");
            }finally{
                fr.close();
            }
        }if(post){
            // 	post

             FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderPost(query,Transaction.AUTO_COMMIT);
            try{
                assertNotNull("FeatureType was null",ft);
                SimpleFeatureType featureType = fr.getFeatureType();
                if( ft.getAttributeCount()>1 ){
                    assertEquals("Query must restrict feature type to only having 1 AttributeType", 1, featureType.getAttributeCount() );
                }
                assertTrue("must have 1 feature -- fair assumption",fr.hasNext() && featureType!=null );
                SimpleFeature feature = fr.next();
                featureType=feature.getFeatureType();
                if( ft.getAttributeCount()>1 ){
                    assertEquals("Query must restrict feature type to only having 1 AttributeType", 1, featureType.getAttributeCount() );
                }
                assertNotNull( "must have 1 feature ", feature);
                fid=feature.getID();
                int j=0;while(fr.hasNext()){ 
                    fr.next();
                    j++;
                }
                System.out.println(j+" Features");
            }finally{
                fr.close();
            }
        }

        // test fid filter 
        query.setFilter(FilterFactoryFinder.createFilterFactory().createFidFilter(fid));
        if( get ){
             FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderGet(query,Transaction.AUTO_COMMIT);
            try{
                assertNotNull("FeatureType was null",ft);
                int j=0;while(fr.hasNext()){ assertEquals(fid,fr.next().getID());j++;}
                assertEquals( 1,j );
            }finally{
                fr.close();
            }
        }if (post){
             FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderPost(query,Transaction.AUTO_COMMIT);
            try{
                assertNotNull("FeatureType was null",ft);
                int j=0;while(fr.hasNext()){ assertEquals(fid,fr.next().getID());j++;}
                assertEquals( 1,j );
            }finally{
                fr.close();
            }
        }
        }catch(java.net.SocketException se){
            se.printStackTrace();
        }
        
    }
       /** Request a subset of available properties 
     * @throws IllegalFilterException */
    public static void doFeatureReaderWithBBox(URL url, boolean get, boolean post, int i, Envelope bbox) throws NoSuchElementException, IllegalAttributeException, IOException, SAXException, IllegalFilterException{
        if( url == null ) return; // test disabled (must be site specific)                
        try{
        System.out.println("FeatureReaderWithFilterTest + "+url);
        WFS_1_0_0_DataStore wfs = getDataStore(url);
        assertNotNull("No featureTypes",wfs.getTypeNames());
        
        String typeName = wfs.getTypeNames()[i];
        assertNotNull("Null featureType in [0]",typeName);
        SimpleFeatureType featureType = wfs.getSchema(typeName);
        
        // take atleast attributeType 3 to avoid the undeclared one .. inherited optional attrs
        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
        DefaultQuery query = new DefaultQuery(featureType.getTypeName());
        PropertyName theGeom = ff.property( featureType.getGeometryDescriptor().getName() );
        Filter filter = ff.bbox( theGeom, bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(), bbox.getMaxY(), "EPSG:4326" );
                
        query.setFilter( filter );
        //query.setMaxFeatures(3);
        if(get){
            //  get
            FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderGet(query,Transaction.AUTO_COMMIT);
            assertNotNull("GET "+typeName+" FeatureType was null",featureType);
            assertTrue("GET "+typeName+ " must have 1 feature -- fair assumption",fr.hasNext() && fr.getFeatureType()!=null && fr.next()!=null);
            int j=0;while(fr.hasNext()){fr.next();j++;}
            System.out.println("bbox selected "+j+" Features");
            fr.close();
        }if(post){
            //  post

            FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderPost(query,Transaction.AUTO_COMMIT);
            assertNotNull("POST "+typeName+"FeatureType was null",featureType);
            assertTrue("POST "+typeName+"must have 1 feature -- fair assumption",fr.hasNext() && fr.getFeatureType()!=null && fr.next()!=null);
            int j=0;while(fr.hasNext()){fr.next();j++;}
            System.out.println("bbox selected "+j+" Features");
            fr.close();
        }
        }catch(java.net.SocketException se){
            se.printStackTrace();
        }
    }
}
