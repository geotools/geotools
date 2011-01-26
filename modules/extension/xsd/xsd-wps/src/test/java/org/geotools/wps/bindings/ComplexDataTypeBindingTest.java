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
package org.geotools.wps.bindings;

import net.opengis.wfs.FeatureCollectionType;
import net.opengis.wps10.ComplexDataType;

import org.geotools.feature.FeatureCollection;
import org.geotools.wps.WPS;
import org.geotools.wps.WPSTestSupport;

import com.vividsolutions.jts.geom.Polygon;

public class ComplexDataTypeBindingTest extends WPSTestSupport {

    public void testParsePolygon() throws Exception {
        String xml = 
          "<wps:ComplexData xmlns:wps='http://www.opengis.net/wps/1.0.0'>" + 
            "<gml:Polygon xmlns:gml='http://www.opengis.net/gml'>" +
              "<gml:exterior>" + 
                "<gml:LinearRing>" + 
                  "<gml:posList>1 1 2 1 2 2 1 2 1 1</gml:posList>" + 
                "</gml:LinearRing>" + 
              "</gml:exterior>" + 
            "</gml:Polygon>" +
          "</wps:ComplexData>";
        buildDocument(xml);
       
        Object o = parse(WPS.ComplexDataType);
        assertTrue( o instanceof ComplexDataType );
        
        ComplexDataType data = (ComplexDataType) o;
        assertEquals( 1, data.getData().size() );
        
        assertTrue( data.getData().get( 0 ) instanceof Polygon );
    }
    
    public void testParseFeatureCollection() throws Exception {
        String xml = 
          "<wps:ComplexData xmlns:wps='http://www.opengis.net/wps/1.0.0'>" + 
            "<wfs:FeatureCollection xmlns='http://www.opengis.net/wfs' " +
                "xmlns:wfs='http://www.opengis.net/wfs' xmlns:geotools='http://geotools.org' " +
                "xmlns:gml='http://www.opengis.net/gml'>" +
                "<gml:boundedBy>" + 
                  "<gml:Envelope srsName='urn:x-ogc:def:crs:EPSG:4326'>" + 
                    "<gml:lowerCorner>0 0</gml:lowerCorner>" + 
                    "<gml:upperCorner>1 1</gml:upperCorner>" + 
                  "</gml:Envelope>" + 
                "</gml:boundedBy>" + 
                "<gml:featureMember>" + 
                  "<geotools:feature fid='feature.1'>" + 
                     "<geotools:geometry>" + 
                       "<gml:LineString>" + 
                          "<gml:posList>0 0 1 1</gml:posList>" + 
                       "</gml:LineString>" +
                     "</geotools:geometry>" +
                     "<geotools:integer>1</geotools:integer>" + 
                  "</geotools:feature>" + 
                "</gml:featureMember>" + 
            "</wfs:FeatureCollection>" + 
          "</wps:ComplexData>";
        buildDocument(xml);
       
        Object o = parse(WPS.ComplexDataType);
        assertTrue( o instanceof ComplexDataType );
        
        ComplexDataType data = (ComplexDataType) o;
        assertEquals( 1, data.getData().size() );
        
        assertTrue( data.getData().get( 0 ) instanceof FeatureCollectionType );
        FeatureCollectionType fc = (FeatureCollectionType) data.getData().get( 0 );

        assertEquals( 1, fc.getFeature().size() );
        FeatureCollection features = (FeatureCollection) fc.getFeature().get( 0 );
        assertEquals( 1, features.size() );
    
    }
}
