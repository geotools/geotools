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
import java.net.URI;
import java.util.logging.Level;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.geotools.TestData;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.opengis.feature.simple.SimpleFeature;


/**
 *
 *
 * @source $URL$
 */
public class GMLInheritanceTest extends TestCase {

    
    public void testNestedFeature() throws Throwable {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        SAXParser parser = spf.newSAXParser();

        String path = "xml/sample/nestedFeatures.xml";
        File f = TestData.copy(this,path);
        TestData.copy(this,"xml/sample/nestedFeatures.xsd");
        URI u = f.toURI();

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u,null);
        XMLSAXHandler.setLogLevel(Level.FINEST);
        XSISAXHandler.setLogLevel(Level.FINEST);
        XMLElementHandler.setLogLevel(Level.FINEST);
        XSIElementHandler.setLogLevel(Level.FINEST);

        parser.parse(f, xmlContentHandler);

        Object doc = xmlContentHandler.getDocument();
        assertNotNull("Document missing", doc);
//            System.out.println(doc);
        
        checkFeatureCollection((SimpleFeatureCollection)doc);
    }
    public void testMultiInheritance() throws Throwable {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        spf.setValidating(false);

        SAXParser parser = spf.newSAXParser();

        String path = "xml/sample/multiInheritance.xml";
        File f = TestData.copy(this,path);
        TestData.copy(this,"xml/sample/multiInheritance.xsd");
        URI u = f.toURI();

        XMLSAXHandler xmlContentHandler = new XMLSAXHandler(u,null);
        XMLSAXHandler.setLogLevel(Level.FINEST);
        XSISAXHandler.setLogLevel(Level.FINEST);
        XMLElementHandler.setLogLevel(Level.FINEST);
        XSIElementHandler.setLogLevel(Level.FINEST);

        parser.parse(f, xmlContentHandler);

        Object doc = xmlContentHandler.getDocument();
        assertNotNull("Document missing", doc);
//            System.out.println(doc);
        
        checkFeatureCollection((SimpleFeatureCollection)doc);
    }
    
    private void checkFeatureCollection(SimpleFeatureCollection doc){
               
        //remaining slot (s) should be feature(s)
        assertTrue("Requires atleast one feature",doc.size()>0);  //bbox + feature
        SimpleFeatureIterator i = doc.features();
        int j = 1;
        while(i.hasNext()){
            SimpleFeature ft = i.next();
            assertNotNull("Feature #"+j+" is null",ft);
//            assertNotNull("Feature #"+j+" missing crs ",ft.getFeatureType().getDefaultGeometry().getCoordinateSystem());
//            System.out.println("Feature "+j+" : "+ft);
            j++;
        }
        assertEquals( 2, j );
        // System.out.println("Found "+j+" Features");
    }
}
