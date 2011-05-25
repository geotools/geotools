/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wms.test;

import java.net.MalformedURLException;
import java.net.URL;

import junit.framework.TestCase;

import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * This test case assume you have a default geoserver installed
 * on 127.0.0.1 (ie localhost).
 * 
 * @author Jody Garnett
 *
 *
 * @source $URL$
 */
public class LocalGeoServerOnlineTest extends TestCase {
    static private WebMapServer wms;
    static private WMSCapabilities capabilities;
    static private URL serverURL;
    static {
        try {
            serverURL = new URL("http://127.0.0.1:8080/geoserver/wms?SERVICE=WMS&");
        } catch (MalformedURLException e) {
            serverURL = null;
        };
    }
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        if( wms == null ){
            // do setup once!
            if( serverURL != null ){
                try {
                    wms = new WebMapServer( serverURL );
                    capabilities = wms.getCapabilities();
                }
                catch (Exception eek){
                    serverURL = null;
                    throw eek;
                }
            }
        }        
    }
        
    public void testLocalGeoServer(){
        assertNotNull( wms );
        assertNotNull( capabilities );
        Layer root = capabilities.getLayer();
        assertNotNull( root );
        assertNull( "root layer does not have a name", root.getName() );
        assertNotNull( "title", root.getTitle() );
    }

    public void testStates(){
        Layer states = find( "topp:states" );
        assertNotNull( states );  
        
        ResourceInfo info = wms.getInfo( states );
        assertNotNull( info );
        assertEquals( states.getTitle(), info.getTitle() );
        
        ReferencedEnvelope bounds = info.getBounds();
        assertNotNull( bounds );
        assertFalse( bounds.isEmpty() );
    }
    private Layer find( String name ){
        for( Layer layer : capabilities.getLayerList() ){
            if( name.equals( layer.getName() )){
                return layer;
            }
        }
        return null;
    }
    
    public void testServiceInfo(){
        ServiceInfo info = wms.getInfo();
        assertNotNull( info );
        
        assertEquals( serverURL, wms.getCapabilities().getRequest().getGetCapabilities().getGet() );
        assertEquals( "My GeoServer WMS", info.getTitle() );
        
        assertNotNull( info.getDescription() );        
    }
    
    public void testResourceInfo(){
        
    }
}
