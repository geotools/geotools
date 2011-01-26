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
package org.geotools.gml3.v3_2;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;

import org.apache.commons.io.FileUtils;
import org.eclipse.xsd.XSDSchema;
import org.geotools.data.DataUtilities;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.xml.Parser;
import org.opengis.feature.simple.SimpleFeature;
import org.w3c.dom.Document;

import com.vividsolutions.jts.geom.Point;

public class GMLParsingTest extends TestCase {

    public void testGML() throws Exception {
        XSDSchema gml = GML.getInstance().getSchema();
        assertFalse( gml.getTypeDefinitions().isEmpty() );
    }
    
    public void testParseFeatureCollection() throws Exception {
        File schema = File.createTempFile("test", "xsd");
        schema.deleteOnExit();
        FileUtils.copyURLToFile(getClass().getResource("test.xsd"), schema);
        
        Document dom = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(
            getClass().getResourceAsStream( "test.xml" )   
        );
        URL schemaURL = DataUtilities.fileToURL( schema.getAbsoluteFile() );        
        dom.getDocumentElement().setAttribute( "xsi:schemaLocation", "http://www.geotools.org/test " + schemaURL.getFile() );
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        TransformerFactory.newInstance().newTransformer().transform( 
            new DOMSource( dom ), new StreamResult( out ) );
        
        GMLConfiguration config = new GMLConfiguration();
        Parser p = new Parser( config );
        Object o = p.parse( new ByteArrayInputStream( out.toByteArray() ) );
        assertTrue( o instanceof FeatureCollection );
        
        FeatureCollection features = (FeatureCollection) o;
        assertEquals( 3, features.size() );
        
        FeatureIterator fi = features.features();
        
        for ( int i = 0; i < 3; i++ ) {
            assertTrue( fi.hasNext() );
            
            SimpleFeature f = (SimpleFeature) fi.next();
            assertTrue( f.getDefaultGeometry() instanceof Point );
        
            Point point = (Point) f.getDefaultGeometry();
            assertEquals( i/1d, point.getX(), 0.1 );
            assertEquals( i/1d, point.getX(), 0.1 );
            
            assertEquals( i, f.getAttribute( "count" ) );
        }
        features.close( fi );
    }
}
