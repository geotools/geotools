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

import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import junit.framework.TestCase;
import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wfs.WfsFactory;

import org.geotools.data.memory.MemoryDataStore;
import org.geotools.feature.DefaultFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.xml.Encoder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 *
 * @source $URL$
 */
public class WFSFeatureCollectionEncodingTest extends TestCase {

    MemoryDataStore store;

    @Override
    protected void setUp() throws Exception {
        
        store = new MemoryDataStore();
        
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName( "feature" );
        tb.setNamespaceURI( "http://geotools.org");
        tb.add( "geometry", Point.class );
        tb.add( "integer", Integer.class );
        store.createSchema(tb.buildFeatureType());
        
        SimpleFeatureBuilder b = new SimpleFeatureBuilder( store.getSchema("feature") );
        b.add( new GeometryFactory().createPoint( new Coordinate( 0, 0 ) ) );
        b.add( 0 );
        store.addFeature(b.buildFeature( "zero" ));
        
        b.add( new GeometryFactory().createPoint( new Coordinate( 1, 1 ) ) );
        b.add( 1 );
        store.addFeature(b.buildFeature( "one" ));
        
        tb = new SimpleFeatureTypeBuilder();
        tb.setName( "other" );
        tb.setNamespaceURI( "http://geotools.org");
        tb.add( "geometry", Point.class );
        tb.add( "integer", Integer.class );
        store.createSchema(tb.buildFeatureType());
        
        b = new SimpleFeatureBuilder( store.getSchema("other") );
        b.add( new GeometryFactory().createPoint( new Coordinate( 2, 2 ) ) );
        b.add( 2 );
        store.addFeature(b.buildFeature( "two" ));
        
        b.add( new GeometryFactory().createPoint( new Coordinate( 3, 3 ) ) );
        b.add( 3 );
        store.addFeature(b.buildFeature( "three" ));
    }

    public void testEncodeFeatureCollection() throws Exception {
        FeatureCollectionType fc = WfsFactory.eINSTANCE.createFeatureCollectionType();
        FeatureCollection features = store.getFeatureSource("feature").getFeatures();
        fc.getFeature().add( features );

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

    public void testEncodeMultiFeatureCollection() throws Exception {
        FeatureCollectionType fc = WfsFactory.eINSTANCE.createFeatureCollectionType();
        
        fc.getFeature().add( store.getFeatureSource("feature").getFeatures() );
        fc.getFeature().add( store.getFeatureSource("other").getFeatures() );
        
        Encoder e = encoder();
        e.getNamespaces().declarePrefix( "geotools", "http://geotools.org");
        e.setIndenting(true);
        
        Document d = e.encodeAsDOM( fc, WFS.FeatureCollection );
        //TransformerFactory.newInstance().newTransformer().transform(
        //    new DOMSource(d), new StreamResult(System.out));
        
        List<Element> members = getChildElementsByTagName( d.getDocumentElement(), "wfs:member" ); 
        assertEquals( 2, members.size() );

        assertEquals(1, getChildElementsByTagName(members.get(0), "wfs:FeatureCollection").size());

        Element featureCollection = 
            getChildElementsByTagName(members.get(0), "wfs:FeatureCollection").get(0);
        
        assertEquals( 2, getChildElementsByTagName( featureCollection, "wfs:member" ).size() );
        assertEquals( 2, featureCollection.getElementsByTagName("gml:Point" ).getLength() );
        assertEquals( 2, featureCollection.getElementsByTagName("gml:pos" ).getLength() );
        assertEquals( 0, featureCollection.getElementsByTagName("gml:coord" ).getLength() );
        
        assertEquals(1, getChildElementsByTagName(members.get(1), "wfs:FeatureCollection").size());

        featureCollection = 
            getChildElementsByTagName(members.get(1), "wfs:FeatureCollection").get(0);
        
        assertEquals( 2, getChildElementsByTagName( featureCollection, "wfs:member" ).size() );
        assertEquals( 2, featureCollection.getElementsByTagName("gml:Point" ).getLength() );
        assertEquals( 2, featureCollection.getElementsByTagName("gml:pos" ).getLength() );
        assertEquals( 0, featureCollection.getElementsByTagName("gml:coord" ).getLength() );

    }

    List<Element> getChildElementsByTagName(Node e, String name) {
        List<Element> elements = new ArrayList();
        for (int i = 0; i < e.getChildNodes().getLength(); i++) {
            Node n = e.getChildNodes(). item(i);
            if (n instanceof Element && n.getNodeName().equals(name)) {
                elements.add((Element)n);
            }
        }
        return elements;
    }

    Encoder encoder() {
        return new Encoder(new org.geotools.wfs.v2_0.WFSConfiguration());
    }
}
