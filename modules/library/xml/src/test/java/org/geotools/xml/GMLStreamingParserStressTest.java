/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xml;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.util.logging.Level;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.data.FeatureReader;
import org.geotools.xml.gml.FCBuffer;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;


/**
 * <p>
 * DOCUMENT ME!
 * </p>
 * @
 *
 * @author dzwiers www.refractions.net
 *
 * @source $URL$
 */
public class GMLStreamingParserStressTest extends TestCase {

//    public void testOSDNFFeatures() throws SAXException, IOException {
//         FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
//        try {
//
//            String path = "/home/dzwiers/testData/sample-master-map.xml";
//            File f = new File(path);
//            URI u = f.toURI();
//
//            XMLSAXHandler.setLogLevel(Level.FINEST);
//            XSISAXHandler.setLogLevel(Level.FINEST);
//            XMLElementHandler.setLogLevel(Level.FINEST);
//            XSIElementHandler.setLogLevel(Level.FINEST);
//            fr = FCBuffer.getFeatureReader(u,10,100000);
//            
//            assertNotNull("FeatureReader missing", fr);
//            
//            int i=0;
//            for(;fr.hasNext();i++){
//                System.out.println(fr.next());
//            }
//            
//            assertTrue("# features = "+i,i==70);
//            
//        } catch (Throwable e) {
//            e.printStackTrace();
//            fail(e.toString());
//        } finally {
//            if(fr!=null){
//                ((FCBuffer)fr).stop();
//            }
//        }
//    }

    public void skippedtestGTRoadsFeatures() throws IOException {
    	if(!TestData.isExtensiveTest())
    		return;
         FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
        try {

            String path = "xml/geoserver/roads.xml";
            File f = TestData.copy(this,path);
            TestData.copy(this,"xml/wfs/WFS-basic.xsd");
            TestData.copy(this,"xml/geoserver/roadSchema.xsd");
            URI u = f.toURI();

            XMLSAXHandler.setLogLevel(Level.FINEST);
            XSISAXHandler.setLogLevel(Level.FINEST);
            XMLElementHandler.setLogLevel(Level.FINEST);
            XSIElementHandler.setLogLevel(Level.FINEST);
            fr = FCBuffer.getFeatureReader(u,10,10000);
            
            assertNotNull("FeatureReader missing", fr);
            
            int i=0;
            for(;fr.hasNext();i++){
//                System.out.println(fr.next());
                fr.next();
            }
            
            assertTrue("# features = "+i,i==70);
            
        } catch (Throwable e) {
            e.printStackTrace();
            fail(e.toString());
        } finally {
            if(fr!=null){
                ((FCBuffer)fr).close();
            }
        }
    }
    
    public void testFMERoadsFeatures() throws IOException {
    	if(!TestData.isExtensiveTest())
    		return;
         FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
        try {
            String path = "xml/fme/roads/roads.xml";
            File f = TestData.copy(this,path);
            TestData.copy(this,"xml/fme/roads/roads.xsd");
            URI u = f.toURI();

            fr = FCBuffer.getFeatureReader(u,10,10000);
            
            assertNotNull("FeatureReader missing", fr);
            
            int i=0;
            for(;fr.hasNext();i++){
//                System.out.println(fr.next());
                fr.next();
            }
            
            assertTrue("# features "+i,i>20);
            System.out.println("\n # Features = "+i);
            
        } catch (Throwable e) {
            e.printStackTrace();
            fail(e.toString());
        } finally {
            if(fr!=null){
                ((FCBuffer)fr).close();
            }
        }
    }
    
    public void testFMELakesFeatures() throws IOException {
    	if(!TestData.isExtensiveTest())
    		return;
         FeatureReader<SimpleFeatureType, SimpleFeature> fr = null;
        try {
            String path = "xml/fme/lakes/lakes.xml";
            File f = TestData.copy(this,path);
            TestData.copy(this,"xml/fme/lakes/lakes.xsd");
            URI u = f.toURI();

            fr = FCBuffer.getFeatureReader(u,10,10000);
            
            assertNotNull("FeatureReader missing", fr);
            
            int i=0;
            for(;fr.hasNext();i++){
//                System.out.println(fr.next());
                fr.next();
            }
            
            assertTrue("# features"+i,i>20);
            System.out.println("\n # Features = "+i);
            
        } catch (Throwable e) {
            e.printStackTrace();
            fail(e.toString());
        } finally {
            if(fr!=null){
                ((FCBuffer)fr).close();
            }
        }
    }
    
    public void testFME2StreamsFeatures() throws IOException {
    	if(!TestData.isExtensiveTest())
    		return;
         FeatureReader<SimpleFeatureType, SimpleFeature> fr1 = null;
         FeatureReader<SimpleFeatureType, SimpleFeature> fr2 = null;
        try {
            String path = "xml/fme/lakes/lakes.xml";
            File f = TestData.copy(this,path);
            TestData.copy(this,"xml/fme/lakes/lakes.xsd");
            URI u1 = f.toURI();

            path = "xml/fme/roads/roads.xml";
            f = TestData.copy(this,path);
            TestData.copy(this,"xml/geoserver/roadSchema.xsd");
            URI u2 = f.toURI();

            fr1 = FCBuffer.getFeatureReader(u1,10,10000);
            fr2 = FCBuffer.getFeatureReader(u2,10,10000);

            assertNotNull("FeatureReader missing", fr1);
            assertNotNull("FeatureReader missing", fr2);
            
            boolean cont = true;
            int count1,count2;
            count1 = count2 = 0;
            while(cont){
                cont = false;
                for(int i=0;i<10 && fr1.hasNext();i++){
                    SimpleFeature ftr = fr1.next();
                    assertTrue("Feature Null",ftr!=null);
//                    System.out.println(ftr);
                    cont = true;
                    count1++;
                }
                for(int i=0;i<10 && fr2.hasNext();i++){
                    SimpleFeature ftr = fr2.next();
                    assertTrue("Feature Null",ftr!=null);
//                    System.out.println(ftr);
                    cont = true;
                    count2++;
                }
            }
            assertTrue("Must have used both readers",(count1>20 && count2>20));
            System.out.println("\n# Features: "+count1+" , "+count2);
            
        } catch (Throwable e) {
            e.printStackTrace();
            fail(e.toString());
        } finally {
            if(fr1!=null){
                ((FCBuffer)fr1).close();
            }
            if(fr2!=null){
                ((FCBuffer)fr2).close();
            }
        }
    }
}
