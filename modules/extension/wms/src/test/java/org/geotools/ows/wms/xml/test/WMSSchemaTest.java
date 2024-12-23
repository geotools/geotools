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
import org.junit.Assert;
import org.junit.Test;

public class WMSSchemaTest {

    @Test
    public void testSchema() throws URISyntaxException {
        Schema v1 = SchemaFactory.getInstance(new URI("http://www.opengis.net/wms"));
        Assert.assertNotNull(v1);
        Schema v2 = WMSSchema.getInstance();
        Assert.assertNotNull(v2);
        Assert.assertEquals(v1, v2);
    }

    @Test
    public void testGetCapabilities() throws Exception {

        File getCaps = TestData.file(this, "1.3.0Capabilities.xml");
        URL getCapsURL = getCaps.toURI().toURL();

        Object object = DocumentFactory.getInstance(getCapsURL.openStream(), null, Level.WARNING);

        SchemaFactory.getInstance(WMSSchema.NAMESPACE);

        Assert.assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        Assert.assertEquals(capabilities.getVersion(), "1.3.0");
        Assert.assertEquals(capabilities.getService().getName(), "WMS");
        Assert.assertEquals(capabilities.getService().getTitle(), "World Map");
        Assert.assertEquals(capabilities.getService().get_abstract(), "None");
        Assert.assertEquals(capabilities.getService().getOnlineResource(), new URL("http://www2.demis.nl"));

        Assert.assertEquals(capabilities.getService().getLayerLimit(), 40);
        Assert.assertEquals(capabilities.getService().getMaxWidth(), 2000);
        Assert.assertEquals(capabilities.getService().getMaxHeight(), 2000);

        Assert.assertEquals(
                capabilities.getRequest().getGetCapabilities().getFormats().get(0), "text/xml");
        Assert.assertEquals(
                capabilities.getRequest().getGetCapabilities().getGet(),
                new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
        Assert.assertEquals(
                capabilities.getRequest().getGetCapabilities().getPost(),
                new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));

        Assert.assertEquals(capabilities.getRequest().getGetMap().getFormats().size(), 5);
        Assert.assertEquals(capabilities.getRequest().getGetMap().getFormats().get(0), "image/gif");
        Assert.assertEquals(capabilities.getRequest().getGetMap().getFormats().get(1), "image/png");
        Assert.assertEquals(capabilities.getRequest().getGetMap().getFormats().get(2), "image/jpeg");
        Assert.assertEquals(capabilities.getRequest().getGetMap().getFormats().get(3), "image/bmp");
        Assert.assertEquals(capabilities.getRequest().getGetMap().getFormats().get(4), "image/swf");
        Assert.assertEquals(
                capabilities.getRequest().getGetMap().getGet(),
                new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));

        Assert.assertEquals(
                capabilities.getRequest().getGetFeatureInfo().getFormats().size(), 4);
        Assert.assertEquals(
                capabilities.getRequest().getGetFeatureInfo().getFormats().get(0), "text/xml");
        Assert.assertEquals(
                capabilities.getRequest().getGetFeatureInfo().getFormats().get(1), "text/plain");
        Assert.assertEquals(
                capabilities.getRequest().getGetFeatureInfo().getFormats().get(2), "text/html");
        Assert.assertEquals(
                capabilities.getRequest().getGetFeatureInfo().getFormats().get(3), "text/swf");
        Assert.assertEquals(
                capabilities.getRequest().getGetFeatureInfo().getGet(),
                new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));

        Layer topLayer = capabilities.getLayerList().get(0);
        Assert.assertNotNull(topLayer);
        Assert.assertNull(topLayer.getParent());
        Assert.assertFalse(topLayer.isQueryable());
        Assert.assertEquals(topLayer.getTitle(), "World Map");
        Assert.assertEquals(topLayer.getSrs().size(), 1);
        Assert.assertTrue(topLayer.getSrs().contains("CRS:84"));

        CRSEnvelope llbbox = topLayer.getLatLonBoundingBox();
        Assert.assertNotNull(llbbox);
        Assert.assertEquals(llbbox.getMinX(), -180, 0.0);
        Assert.assertEquals(llbbox.getMaxX(), 180, 0.0);
        Assert.assertEquals(llbbox.getMinY(), -90, 0.0);
        Assert.assertEquals(llbbox.getMaxY(), 90, 0.0);

        Assert.assertEquals(topLayer.getBoundingBoxes().size(), 1);

        CRSEnvelope bbox = topLayer.getBoundingBoxes().get("CRS:84");
        Assert.assertNotNull(bbox);
        Assert.assertEquals(bbox.getEPSGCode(), "CRS:84");
        Assert.assertEquals(bbox.getMinX(), -184, 0.0);
        Assert.assertEquals(bbox.getMaxX(), 180, 0.0);
        Assert.assertEquals(bbox.getMinY(), -90.0000000017335, 0.0);
        Assert.assertEquals(bbox.getMaxY(), 90, 0.0);

        Layer layer = capabilities.getLayerList().get(1);
        Assert.assertEquals(layer.getParent(), topLayer);
        Assert.assertTrue(layer.isQueryable());
        Assert.assertEquals(layer.getName(), "Bathymetry");
        Assert.assertEquals(layer.getTitle(), "Bathymetry");
        Assert.assertEquals(layer.getMetadataURL().get(0).getUrl().toString(), "http://www.example.com/?");
        Assert.assertEquals(layer.getMetadataURL().get(0).getFormat(), "text/html");
        Assert.assertEquals(layer.getMetadataURL().get(0).getType(), "FGDC");
        Assert.assertEquals(
                layer.getStyles().get(0).getLegendURLs().get(0),
                "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");

        // Added test for Attribution parameter
        Attribution attribution = layer.getAttribution();
        Assert.assertNotNull(attribution);
        Assert.assertEquals(attribution.getTitle(), "test");
        Assert.assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        LogoURL logoURL = attribution.getLogoURL();
        Assert.assertNotNull(logoURL);
        Assert.assertEquals(logoURL.getFormat(), "image/png");
        Assert.assertEquals(
                logoURL.getOnlineResource().toString(), "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");
        Assert.assertEquals(logoURL.getHeight(), 100);
        Assert.assertEquals(logoURL.getWidth(), 100);

        // Added test to verify inheritance, should be same as previous llbbox
        llbbox = layer.getLatLonBoundingBox();
        Assert.assertNotNull(llbbox);
        Assert.assertEquals(llbbox.getMinX(), -180, 0.0);
        Assert.assertEquals(llbbox.getMaxX(), 180, 0.0);
        Assert.assertEquals(llbbox.getMinY(), -90, 0.0);
        Assert.assertEquals(llbbox.getMaxY(), 90, 0.0);

        bbox = layer.getBoundingBoxes().get("CRS:84");
        Assert.assertNotNull(bbox);
        Assert.assertEquals(bbox.getEPSGCode(), "CRS:84");
        Assert.assertEquals(bbox.getMinX(), -180, 0.0);
        Assert.assertEquals(bbox.getMaxX(), 180, 0.0);
        Assert.assertEquals(bbox.getMinY(), -90, 0.0);
        Assert.assertEquals(bbox.getMaxY(), 90, 0.0);

        Assert.assertEquals(capabilities.getLayerList().size(), 21);

        layer = capabilities.getLayerList().get(2);
        Assert.assertEquals(layer.getParent(), topLayer);
        Assert.assertTrue(layer.isQueryable());
        Assert.assertEquals(layer.getName(), "Countries");
        Assert.assertEquals(layer.getTitle(), "Countries");

        attribution = layer.getAttribution();
        Assert.assertNotNull(attribution);
        Assert.assertEquals(attribution.getTitle(), "test");
        Assert.assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        logoURL = attribution.getLogoURL();
        Assert.assertNull(logoURL);

        layer = capabilities.getLayerList().get(3);
        Assert.assertEquals(layer.getParent(), topLayer);
        Assert.assertTrue(layer.isQueryable());
        Assert.assertEquals(layer.getName(), "Topography");
        Assert.assertEquals(layer.getTitle(), "Topography");

        attribution = layer.getAttribution();
        Assert.assertNotNull(attribution);
        Assert.assertEquals(attribution.getTitle(), "test");
        Assert.assertNull(attribution.getOnlineResource());
        logoURL = attribution.getLogoURL();
        Assert.assertNull(logoURL);

        layer = capabilities.getLayerList().get(4);
        Assert.assertEquals(layer.getParent(), topLayer);
        Assert.assertFalse(layer.isQueryable());
        Assert.assertEquals(layer.getName(), "Hillshading");
        Assert.assertEquals(layer.getTitle(), "Hillshading");

        attribution = layer.getAttribution();
        Assert.assertNotNull(attribution);
        Assert.assertNull(attribution.getTitle());
        Assert.assertNotNull(attribution.getOnlineResource());
        Assert.assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        logoURL = attribution.getLogoURL();
        Assert.assertNotNull(logoURL);
        Assert.assertEquals(logoURL.getFormat(), "image/png");
        Assert.assertEquals(
                logoURL.getOnlineResource().toString(), "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");
        Assert.assertEquals(logoURL.getHeight(), 0);
        Assert.assertEquals(logoURL.getWidth(), 0);

        layer = capabilities.getLayerList().get(20);
        Assert.assertEquals(layer.getParent(), topLayer);
        Assert.assertTrue(layer.isQueryable());
        Assert.assertEquals(layer.getName(), "Ocean features");
        Assert.assertEquals(layer.getTitle(), "Ocean features");
        attribution = layer.getAttribution();
        Assert.assertNull(attribution);

        // Added test to verify inheritance, should be same as previous llbbox
        llbbox = layer.getLatLonBoundingBox();
        Assert.assertNotNull(llbbox);
        Assert.assertEquals(llbbox.getMinX(), -180, 0.0);
        Assert.assertEquals(llbbox.getMaxX(), 180, 0.0);
        Assert.assertEquals(llbbox.getMinY(), -90, 0.0);
        Assert.assertEquals(llbbox.getMaxY(), 90, 0.0);

        bbox = layer.getBoundingBoxes().get("CRS:84");
        Assert.assertNotNull(bbox);
        Assert.assertEquals(bbox.getEPSGCode(), "CRS:84");
        Assert.assertEquals(bbox.getMinX(), -180, 0.0);
        Assert.assertEquals(bbox.getMaxX(), 179.999420166016, 0.0);
        Assert.assertEquals(bbox.getMinY(), -62.9231796264648, 0.0);
        Assert.assertEquals(bbox.getMaxY(), 68.6906585693359, 0.0);
    }

    @Test
    public void testGetCapabilities110() throws Exception {

        File getCaps = TestData.file(this, "1.1.0Capabilities.xml");
        URL getCapsURL = getCaps.toURI().toURL();
        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
        Object object = DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);

        Assert.assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        Assert.assertEquals(capabilities.getVersion(), "1.1.0");
        Assert.assertEquals(capabilities.getService().getName(), "OGC:WMS");
        Assert.assertEquals(capabilities.getService().getTitle(), "GMap WMS Demo Server");
        Assert.assertTrue(capabilities
                .getService()
                .get_abstract()
                .contains("This demonstration server was setup by DM Solutions Group"));
        Assert.assertEquals(
                capabilities.getService().getOnlineResource(),
                new URL("http://dev1.dmsolutions.ca/cgi-bin/mswms_gmap?"));

        Assert.assertEquals(
                capabilities.getRequest().getGetCapabilities().getFormats().get(0), "application/vnd.ogc.wms_xml");

        Layer topLayer = capabilities.getLayerList().get(0);
        Assert.assertNotNull(topLayer);
        Assert.assertNull(topLayer.getParent());
        Assert.assertFalse(topLayer.isQueryable());
        Assert.assertEquals(topLayer.getTitle(), "GMap WMS Demo Server");
        Assert.assertEquals(topLayer.getSrs().size(), 4);

        // Added test for Attribution parameter
        Attribution attribution = topLayer.getAttribution();
        Assert.assertNotNull(attribution);
        Assert.assertEquals(attribution.getTitle(), "test");
        Assert.assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        LogoURL logoURL = attribution.getLogoURL();
        Assert.assertNotNull(logoURL);
        Assert.assertEquals(logoURL.getFormat(), "image/png");
        Assert.assertEquals(
                logoURL.getOnlineResource().toString(), "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");
        Assert.assertEquals(logoURL.getHeight(), 100);
        Assert.assertEquals(logoURL.getWidth(), 100);

        Layer layer = capabilities.getLayerList().get(1);
        Assert.assertEquals(layer.getParent(), topLayer);
        Assert.assertFalse(layer.isQueryable());
        Assert.assertEquals(layer.getName(), "bathymetry");
        Assert.assertEquals(layer.getTitle(), "Elevation/Bathymetry");

        attribution = layer.getAttribution();
        Assert.assertNotNull(attribution);
        Assert.assertEquals(attribution.getTitle(), "test");
        Assert.assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        logoURL = attribution.getLogoURL();
        Assert.assertNull(logoURL);

        layer = capabilities.getLayerList().get(2);
        Assert.assertEquals(layer.getParent(), topLayer);
        Assert.assertFalse(layer.isQueryable());
        Assert.assertEquals(layer.getName(), "land_fn");
        Assert.assertEquals(layer.getTitle(), "Foreign Lands");

        attribution = layer.getAttribution();
        Assert.assertNotNull(attribution);
        Assert.assertEquals(attribution.getTitle(), "test");
        Assert.assertNull(attribution.getOnlineResource());
        logoURL = attribution.getLogoURL();
        Assert.assertNull(logoURL);

        layer = capabilities.getLayerList().get(3);
        Assert.assertEquals(layer.getParent(), topLayer);
        Assert.assertTrue(layer.isQueryable());
        Assert.assertEquals(layer.getName(), "park");
        Assert.assertEquals(layer.getTitle(), "Parks");

        attribution = layer.getAttribution();
        Assert.assertNotNull(attribution);
        Assert.assertNull(attribution.getTitle());
        Assert.assertNotNull(attribution.getOnlineResource());
        Assert.assertEquals(attribution.getOnlineResource().toString(), "http://www.example.com");
        logoURL = attribution.getLogoURL();
        Assert.assertNotNull(logoURL);
        Assert.assertEquals(logoURL.getFormat(), "image/png");
        Assert.assertEquals(
                logoURL.getOnlineResource().toString(), "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");
        Assert.assertEquals(logoURL.getHeight(), 0);
        Assert.assertEquals(logoURL.getWidth(), 0);

        layer = capabilities.getLayerList().get(4);
        Assert.assertEquals(layer.getParent(), topLayer);
        Assert.assertFalse(layer.isQueryable());
        Assert.assertEquals(layer.getName(), "drain_fn");
        Assert.assertEquals(layer.getTitle(), "Water");
        attribution = layer.getAttribution();
        Assert.assertNull(attribution);
    }
}
