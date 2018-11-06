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
package org.geotools.ows.wms.xml.test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import junit.framework.TestCase;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.xml.Attribution;
import org.geotools.ows.wms.xml.LogoURL;
import org.geotools.ows.wms.xml.WMSSchema;
import org.geotools.test.TestData;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.handlers.DocumentHandler;
import org.geotools.xml.schema.Schema;

public class WMSSchemaTest extends TestCase {

    public void testSchema() throws URISyntaxException {
        Schema v1 = SchemaFactory.getInstance(new URI("http://www.opengis.net/wms"));
        assertNotNull(v1);
        Schema v2 = WMSSchema.getInstance();
        assertNotNull(v2);
        assertEquals(v1, v2);
    }

    public void testGetCapabilities() throws Exception {

        File getCaps = TestData.file(this, "1.3.0Capabilities.xml");
        URL getCapsURL = getCaps.toURI().toURL();

        Object object = DocumentFactory.getInstance(getCapsURL.openStream(), null, Level.WARNING);

        Schema schema = WMSSchema.getInstance();
        SchemaFactory.getInstance(WMSSchema.NAMESPACE);

        assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        assertEquals(capabilities.getVersion(), "1.3.0");
        assertEquals(capabilities.getService().getName(), "WMS");
        assertEquals(capabilities.getService().getTitle(), "World Map");
        assertEquals(capabilities.getService().get_abstract(), "None");
        assertEquals(
                capabilities.getService().getOnlineResource(), new URL("http://www2.demis.nl"));

        assertEquals(capabilities.getService().getLayerLimit(), 40);
        assertEquals(capabilities.getService().getMaxWidth(), 2000);
        assertEquals(capabilities.getService().getMaxHeight(), 2000);

        assertEquals(
                capabilities.getRequest().getGetCapabilities().getFormats().get(0), "text/xml");
        assertEquals(
                capabilities.getRequest().getGetCapabilities().getGet(),
                new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
        assertEquals(
                capabilities.getRequest().getGetCapabilities().getPost(),
                new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));

        assertEquals(capabilities.getRequest().getGetMap().getFormats().size(), 5);
        assertEquals(capabilities.getRequest().getGetMap().getFormats().get(0), "image/gif");
        assertEquals(capabilities.getRequest().getGetMap().getFormats().get(1), "image/png");
        assertEquals(capabilities.getRequest().getGetMap().getFormats().get(2), "image/jpeg");
        assertEquals(capabilities.getRequest().getGetMap().getFormats().get(3), "image/bmp");
        assertEquals(capabilities.getRequest().getGetMap().getFormats().get(4), "image/swf");
        assertEquals(
                capabilities.getRequest().getGetMap().getGet(),
                new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));

        assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().size(), 4);
        assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().get(0), "text/xml");
        assertEquals(
                capabilities.getRequest().getGetFeatureInfo().getFormats().get(1), "text/plain");
        assertEquals(
                capabilities.getRequest().getGetFeatureInfo().getFormats().get(2), "text/html");
        assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().get(3), "text/swf");
        assertEquals(
                capabilities.getRequest().getGetFeatureInfo().getGet(),
                new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));

        Layer topLayer = (Layer) capabilities.getLayerList().get(0);
        assertNotNull(topLayer);
        assertNull(topLayer.getParent());
        assertFalse(topLayer.isQueryable());
        assertEquals(topLayer.getTitle(), "World Map");
        assertEquals(topLayer.getSrs().size(), 1);
        assertTrue(topLayer.getSrs().contains("CRS:84"));

        CRSEnvelope llbbox = topLayer.getLatLonBoundingBox();
        assertNotNull(llbbox);
        assertEquals(llbbox.getMinX(), -180, 0.0);
        assertEquals(llbbox.getMaxX(), 180, 0.0);
        assertEquals(llbbox.getMinY(), -90, 0.0);
        assertEquals(llbbox.getMaxY(), 90, 0.0);

        assertEquals(topLayer.getBoundingBoxes().size(), 1);

        CRSEnvelope bbox = (CRSEnvelope) topLayer.getBoundingBoxes().get("CRS:84");
        assertNotNull(bbox);
        assertEquals(bbox.getEPSGCode(), "CRS:84");
        assertEquals(bbox.getMinX(), -184, 0.0);
        assertEquals(bbox.getMaxX(), 180, 0.0);
        assertEquals(bbox.getMinY(), -90.0000000017335, 0.0);
        assertEquals(bbox.getMaxY(), 90, 0.0);

        Layer layer = (Layer) capabilities.getLayerList().get(1);
        assertEquals(layer.getParent(), topLayer);
        assertTrue(layer.isQueryable());
        assertEquals(layer.getName(), "Bathymetry");
        assertEquals(layer.getTitle(), "Bathymetry");
        assertEquals(layer.getMetadataURL().get(0).getUrl().toString(), "http://www.example.com/?");
        assertEquals(layer.getMetadataURL().get(0).getFormat(), "text/html");
        assertEquals(layer.getMetadataURL().get(0).getType(), "FGDC");
        assertEquals(
                layer.getStyles().get(0).getLegendURLs().get(0),
                "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");

        // Added test for Attribution parameter
        Attribution attribution = layer.getAttribution();
        assertNotNull(attribution);
        assertEquals(attribution.getTitle(), "test");
        assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        LogoURL logoURL = attribution.getLogoURL();
        assertNotNull(logoURL);
        assertEquals(logoURL.getFormat(), "image/png");
        assertEquals(
                logoURL.getOnlineResource().toString(),
                "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");
        assertEquals(logoURL.getHeight(), 100);
        assertEquals(logoURL.getWidth(), 100);

        // Added test to verify inheritance, should be same as previous llbbox
        llbbox = layer.getLatLonBoundingBox();
        assertNotNull(llbbox);
        assertEquals(llbbox.getMinX(), -180, 0.0);
        assertEquals(llbbox.getMaxX(), 180, 0.0);
        assertEquals(llbbox.getMinY(), -90, 0.0);
        assertEquals(llbbox.getMaxY(), 90, 0.0);

        bbox = (CRSEnvelope) layer.getBoundingBoxes().get("CRS:84");
        assertNotNull(bbox);
        assertEquals(bbox.getEPSGCode(), "CRS:84");
        assertEquals(bbox.getMinX(), -180, 0.0);
        assertEquals(bbox.getMaxX(), 180, 0.0);
        assertEquals(bbox.getMinY(), -90, 0.0);
        assertEquals(bbox.getMaxY(), 90, 0.0);

        assertEquals(capabilities.getLayerList().size(), 21);

        layer = (Layer) capabilities.getLayerList().get(2);
        assertEquals(layer.getParent(), topLayer);
        assertTrue(layer.isQueryable());
        assertEquals(layer.getName(), "Countries");
        assertEquals(layer.getTitle(), "Countries");

        attribution = layer.getAttribution();
        assertNotNull(attribution);
        assertEquals(attribution.getTitle(), "test");
        assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        logoURL = attribution.getLogoURL();
        assertNull(logoURL);

        layer = (Layer) capabilities.getLayerList().get(3);
        assertEquals(layer.getParent(), topLayer);
        assertTrue(layer.isQueryable());
        assertEquals(layer.getName(), "Topography");
        assertEquals(layer.getTitle(), "Topography");

        attribution = layer.getAttribution();
        assertNotNull(attribution);
        assertEquals(attribution.getTitle(), "test");
        assertNull(attribution.getOnlineResource());
        logoURL = attribution.getLogoURL();
        assertNull(logoURL);

        layer = (Layer) capabilities.getLayerList().get(4);
        assertEquals(layer.getParent(), topLayer);
        assertFalse(layer.isQueryable());
        assertEquals(layer.getName(), "Hillshading");
        assertEquals(layer.getTitle(), "Hillshading");

        attribution = layer.getAttribution();
        assertNotNull(attribution);
        assertNull(attribution.getTitle());
        assertNotNull(attribution.getOnlineResource());
        assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        logoURL = attribution.getLogoURL();
        assertNotNull(logoURL);
        assertEquals(logoURL.getFormat(), "image/png");
        assertEquals(
                logoURL.getOnlineResource().toString(),
                "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");
        assertEquals(logoURL.getHeight(), 0);
        assertEquals(logoURL.getWidth(), 0);

        layer = (Layer) capabilities.getLayerList().get(20);
        assertEquals(layer.getParent(), topLayer);
        assertTrue(layer.isQueryable());
        assertEquals(layer.getName(), "Ocean features");
        assertEquals(layer.getTitle(), "Ocean features");
        attribution = layer.getAttribution();
        assertNull(attribution);

        // Added test to verify inheritance, should be same as previous llbbox
        llbbox = layer.getLatLonBoundingBox();
        assertNotNull(llbbox);
        assertEquals(llbbox.getMinX(), -180, 0.0);
        assertEquals(llbbox.getMaxX(), 180, 0.0);
        assertEquals(llbbox.getMinY(), -90, 0.0);
        assertEquals(llbbox.getMaxY(), 90, 0.0);

        bbox = (CRSEnvelope) layer.getBoundingBoxes().get("CRS:84");
        assertNotNull(bbox);
        assertEquals(bbox.getEPSGCode(), "CRS:84");
        assertEquals(bbox.getMinX(), -180, 0.0);
        assertEquals(bbox.getMaxX(), 179.999420166016, 0.0);
        assertEquals(bbox.getMinY(), -62.9231796264648, 0.0);
        assertEquals(bbox.getMaxY(), 68.6906585693359, 0.0);
    }

    public void testGetCapabilities110() throws Exception {

        File getCaps = TestData.file(this, "1.1.0Capabilities.xml");
        URL getCapsURL = getCaps.toURI().toURL();
        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
        Object object = DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);

        assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        assertEquals(capabilities.getVersion(), "1.1.0");
        assertEquals(capabilities.getService().getName(), "OGC:WMS");
        assertEquals(capabilities.getService().getTitle(), "GMap WMS Demo Server");
        assertTrue(
                capabilities
                        .getService()
                        .get_abstract()
                        .contains("This demonstration server was setup by DM Solutions Group"));
        assertEquals(
                capabilities.getService().getOnlineResource(),
                new URL("http://dev1.dmsolutions.ca/cgi-bin/mswms_gmap?"));

        assertEquals(
                capabilities.getRequest().getGetCapabilities().getFormats().get(0),
                "application/vnd.ogc.wms_xml");

        Layer topLayer = (Layer) capabilities.getLayerList().get(0);
        assertNotNull(topLayer);
        assertNull(topLayer.getParent());
        assertFalse(topLayer.isQueryable());
        assertEquals(topLayer.getTitle(), "GMap WMS Demo Server");
        assertEquals(topLayer.getSrs().size(), 4);

        // Added test for Attribution parameter
        Attribution attribution = topLayer.getAttribution();
        assertNotNull(attribution);
        assertEquals(attribution.getTitle(), "test");
        assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        LogoURL logoURL = attribution.getLogoURL();
        assertNotNull(logoURL);
        assertEquals(logoURL.getFormat(), "image/png");
        assertEquals(
                logoURL.getOnlineResource().toString(),
                "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");
        assertEquals(logoURL.getHeight(), 100);
        assertEquals(logoURL.getWidth(), 100);

        Layer layer = (Layer) capabilities.getLayerList().get(1);
        assertEquals(layer.getParent(), topLayer);
        assertFalse(layer.isQueryable());
        assertEquals(layer.getName(), "bathymetry");
        assertEquals(layer.getTitle(), "Elevation/Bathymetry");

        attribution = layer.getAttribution();
        assertNotNull(attribution);
        assertEquals(attribution.getTitle(), "test");
        assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        logoURL = attribution.getLogoURL();
        assertNull(logoURL);

        layer = (Layer) capabilities.getLayerList().get(2);
        assertEquals(layer.getParent(), topLayer);
        assertFalse(layer.isQueryable());
        assertEquals(layer.getName(), "land_fn");
        assertEquals(layer.getTitle(), "Foreign Lands");

        attribution = layer.getAttribution();
        assertNotNull(attribution);
        assertEquals(attribution.getTitle(), "test");
        assertNull(attribution.getOnlineResource());
        logoURL = attribution.getLogoURL();
        assertNull(logoURL);

        layer = (Layer) capabilities.getLayerList().get(3);
        assertEquals(layer.getParent(), topLayer);
        assertTrue(layer.isQueryable());
        assertEquals(layer.getName(), "park");
        assertEquals(layer.getTitle(), "Parks");

        attribution = layer.getAttribution();
        assertNotNull(attribution);
        assertNull(attribution.getTitle());
        assertNotNull(attribution.getOnlineResource());
        assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        logoURL = attribution.getLogoURL();
        assertNotNull(logoURL);
        assertEquals(logoURL.getFormat(), "image/png");
        assertEquals(
                logoURL.getOnlineResource().toString(),
                "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");
        assertEquals(logoURL.getHeight(), 0);
        assertEquals(logoURL.getWidth(), 0);

        layer = (Layer) capabilities.getLayerList().get(4);
        assertEquals(layer.getParent(), topLayer);
        assertFalse(layer.isQueryable());
        assertEquals(layer.getName(), "drain_fn");
        assertEquals(layer.getTitle(), "Water");
        attribution = layer.getAttribution();
        assertNull(attribution);
    }
}
