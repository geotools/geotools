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
package org.geotools.wfs.v2_0;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;

import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.xml.Encoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

public class WFSFeatureCollectionEncodingTest extends TestCase {

    FeatureCollectionType fc;
    
    @Override
    protected void setUp() throws Exception {
        fc = WfsFactory.eINSTANCE.createFeatureCollectionType();
        FeatureCollection features = new DefaultFeatureCollection(null,null);
        
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( "feature" );
        tb.setNamespaceURI( "http://geotools.org");
        tb.add( "geometry", Point.class );
        tb.add( "integer", Integer.class );
        
        SimpleFeatureBuilder b = new SimpleFeatureBuilder( tb.buildFeatureType() );
        b.add( new GeometryFactory().createPoint( new Coordinate( 0, 0 ) ) );
        b.add( 0 );
        features.add( b.buildFeature( "zero" ) );
        
        b.add( new GeometryFactory().createPoint( new Coordinate( 1, 1 ) ) );
        b.add( 1 );
        features.add( b.buildFeature( "one" ) );
        
        fc.getFeature().add( features );
    }
    
    public void testEncodeFeatureCollection() throws Exception {
        Encoder e = encoder();
        e.getNamespaces().declarePrefix( "geotools", "http://geotools.org");
        e.setIndenting(true);
        
        Document d = e.encodeAsDOM( fc, WFS.FeatureCollection );
        TransformerFactory.newInstance().newTransformer().transform(
            new DOMSource(d), new StreamResult(System.out));
        
        assertEquals( 2, d.getElementsByTagName( "wfs:member" ).getLength() );
        assertEquals( 2, d.getElementsByTagName( "gml:Point" ).getLength() );
        assertEquals( 2, d.getElementsByTagName( "gml:pos" ).getLength() );
        assertEquals( 0, d.getElementsByTagName( "gml:coord" ).getLength() );
        
        assertEquals( 2, d.getElementsByTagName( "geotools:feature" ).getLength() );
        assertNotNull( ((Element)d.getElementsByTagName( "geotools:feature").item( 0 )).getAttribute("gml:id") );
    }
    
    Encoder encoder() {
        return new Encoder(new org.geotools.wfs.v2_0.WFSConfiguration());
    }
}
