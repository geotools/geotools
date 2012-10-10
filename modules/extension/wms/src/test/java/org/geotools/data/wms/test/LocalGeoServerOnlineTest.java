/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2012, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * This test case assume you have a default GeoServer 2.2 installed on 127.0.0.1 (ie localhost).
 * <p>
 * This is being used to look at WMS 1.1.1 vs WMS 1.3.0 compatibility issues for uDig.
 * <p>
 * 
 * <pre><code>
 * &lt;Layer queryable="1"&gt;
 * &lt;Name&gt;nurc:Img_Sample&lt;/Name&gt;
 *     &lt;Title&gt;North America sample imagery&lt;/Title&gt;
 *     &lt;Abstract/&gt;
 *     &lt;KeywordList&gt;
 *     &lt;Keyword&gt;WCS&lt;/Keyword&gt;
 *     &lt;Keyword&gt;worldImageSample&lt;/Keyword&gt;
 *     &lt;Keyword&gt;worldImageSample_Coverage&lt;/Keyword&gt;
 *     &lt;/KeywordList&gt;
 *     &lt;CRS&gt;EPSG:4326&lt;/CRS&gt;
 *     &lt;CRS&gt;CRS:84&lt;/CRS&gt;
 *     &lt;EX_GeographicBoundingBox&gt;
 *         &lt;westBoundLongitude&gt;-130.85168&lt;/westBoundLongitude&gt;
 *         &lt;eastBoundLongitude&gt;-62.0054&lt;/eastBoundLongitude&gt;
 *         &lt;southBoundLatitude&gt;20.7052&lt;/southBoundLatitude&gt;
 *         &lt;northBoundLatitude&gt;54.1141&lt;/northBoundLatitude&gt;
 *     &lt;/EX_GeographicBoundingBox&gt;
 *     &lt;BoundingBox CRS="CRS:84" minx="-130.85168" miny="20.7052" maxx="-62.0054" maxy="54.1141"/&gt;
 *     &lt;BoundingBox CRS="EPSG:4326" minx="20.7052" miny="-130.85168" maxx="54.1141" maxy="-62.0054"/&gt;
 *     &lt;Style&gt;
 *     &lt;Name&gt;raster&lt;/Name&gt;
 *     &lt;Title&gt;Default Raster&lt;/Title&gt;
 *     &lt;Abstract&gt;
 *     A sample style that draws a raster, good for displaying imagery
 *     &lt;/Abstract&gt;
 *     &lt;LegendURL width="20" height="20"&gt;
 *         &lt;Format&gt;image/png&lt;/Format&gt;
 *         &lt;OnlineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="http://localhost:8080/geoserver/ows?service=WMS&request=GetLegendGraphic&format=image%2Fpng&width=20&height=20&layer=Img_Sample"/&gt;
 *     &lt;/LegendURL&gt;
 *     &lt;/Style&gt;
 * &lt;/Layer&gt;
 * </code></pre>
 * 
 * </p>
 * 
 * @author Jody Garnett
 * @source $URL$
 */
public class LocalGeoServerOnlineTest extends TestCase {
    static private String LOCAL_GEOSERVER = "http://127.0.0.1:8080/geoserver/ows?SERVICE=WMS&";

    static private WebMapServer wms;

    static private WMSCapabilities capabilities;

    static private URL serverURL;
    static {
        try {
            serverURL = new URL(LOCAL_GEOSERVER);
        } catch (MalformedURLException e) {
            serverURL = null;
        }
        ;
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        System.setProperty("org.geotools.referencing.forceXY", "true"); //$NON-NLS-1$ //$NON-NLS-2$
        if (wms == null) {
            // do setup once!
            if (serverURL != null) {
                try {
                    wms = new WebMapServer(serverURL);
                    capabilities = wms.getCapabilities();
                } catch (Exception eek) {
                    serverURL = null;
                    throw eek;
                }
            }
        }
    }

    public void testLocalGeoServer() {
        assertNotNull(wms);
        assertNotNull(capabilities);

        assertEquals("Version Negotiation", "1.3.0", capabilities.getVersion());
        Layer root = capabilities.getLayer();
        assertNotNull(root);
        assertNull("root layer does not have a name", root.getName());
        assertNotNull("title", root.getTitle());
    }

    public void testStates() {
        Layer states = find("topp:states");
        assertNotNull(states);

        ResourceInfo info = wms.getInfo(states);
        assertNotNull(info);
        assertEquals(states.getTitle(), info.getTitle());

        ReferencedEnvelope bounds = info.getBounds();
        assertNotNull(bounds);
        assertFalse(bounds.isEmpty());
    }

    private Layer find(String name, WMSCapabilities caps) {
        for (Layer layer : caps.getLayerList()) {
            if (name.equals(layer.getName())) {
                return layer;
            }
        }
        return null;
    }

    private Layer find(String name) {
        return find(name, capabilities);
    }

    public void testServiceInfo() {
        ServiceInfo info = wms.getInfo();
        assertNotNull(info);

        assertEquals(serverURL, wms.getCapabilities().getRequest().getGetCapabilities().getGet());
        assertEquals("GeoServer Web Map Service", info.getTitle());

        assertNotNull(info.getDescription());
    }

    String axisName(CoordinateReferenceSystem crs, int dimension ){
        return crs.getCoordinateSystem().getAxis(dimension).getName().getCode();
    }
    
    public void testImgSample130() {
        Layer img_sample = find( "nurc:Img_Sample");
        assertNotNull("Img_Sample layer found", img_sample );
        CRSEnvelope latLon = img_sample.getLatLonBoundingBox();
        assertEquals( "LatLonBoundingBox axis 0 name", "Geodetic longitude", axisName( latLon.getCoordinateReferenceSystem(), 0) );
        assertEquals( "LatLonBoundingBox axis 0 name", "Geodetic latitude", axisName( latLon.getCoordinateReferenceSystem(), 1) );
        
        CRSEnvelope bounds = img_sample.getBoundingBoxes().get("EPSG:4326");
        assertEquals( "EPSG:4326 axis 0 name", "Geodetic latitude", axisName( bounds.getCoordinateReferenceSystem(), 0) );
        assertEquals( "EPSG:4326 axis 1 name", "Geodetic longitude", axisName( bounds.getCoordinateReferenceSystem(), 1) );
        
        assertEquals("axis order 0 min", latLon.getMinimum(1), bounds.getMinimum(0));
        assertEquals("axis order 1 min", latLon.getMinimum(0), bounds.getMinimum(1));
        assertEquals("axis order 1 max", latLon.getMaximum(0), bounds.getMaximum(1));
        assertEquals("axis order 1 min", latLon.getMaximum(1), bounds.getMaximum(0));
    }
    
    public void testImageSample111() throws Exception {
        WebMapServer wms111 = new WebMapServer( new URL(serverURL +"&VERSION=1.1.1") );
        WMSCapabilities caps = wms111.getCapabilities();
        assertEquals( "1.1.1", caps.getVersion() );
        
        Layer img_sample = find("nurc:Img_Sample",caps );
        assertNotNull("Img_Sample layer found", img_sample );
        CRSEnvelope latLon = img_sample.getLatLonBoundingBox();
        assertEquals( "LatLonBoundingBox axis 0 name", "Geodetic longitude", axisName( latLon.getCoordinateReferenceSystem(), 0) );
        assertEquals( "LatLonBoundingBox axis 1 name", "Geodetic latitude", axisName( latLon.getCoordinateReferenceSystem(), 1) );
      
        CRSEnvelope bounds = img_sample.getBoundingBoxes().get("EPSG:4326");
        assertEquals( "EPSG:4326 axis 0 name", "Geodetic longitude", axisName( bounds.getCoordinateReferenceSystem(), 0) );
        assertEquals( "EPSG:4326 axis 1 name", "Geodetic latitude", axisName( bounds.getCoordinateReferenceSystem(), 1) );
        
        assertEquals("axis order 0 min", latLon.getMinimum(0), bounds.getMinimum(0));
        assertEquals("axis order 1 min", latLon.getMinimum(1), bounds.getMinimum(1));
        assertEquals("axis order 1 max", latLon.getMaximum(0), bounds.getMaximum(0));
        assertEquals("axis order 1 min", latLon.getMaximum(1), bounds.getMaximum(1));
        
        
        
        
        
    }
}
