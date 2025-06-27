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
package org.geotools.ows.wms.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.xml.WMSSchema;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.handlers.DocumentHandler;
import org.junit.Assert;
import org.junit.Test;

/** @author rgould */
public class WMS1_3_0_Test {

    protected static final Logger LOGGER = Logging.getLogger(WMS1_3_0_Test.class);

    public WMS1_3_0_Test() throws Exception {}

    protected void checkProperties(Properties properties) {
        assertEquals(properties.get("VERSION"), "1.3.0");
        assertEquals(properties.get("REQUEST"), "GetCapabilities");
        assertEquals(properties.get("SERVICE"), "WMS");
    }

    @Test
    public void testCreateParser() throws Exception {
        try {
            WMSCapabilities capabilities = createCapabilities("1.3.0Capabilities.xml");

            assertNotNull(capabilities);

            assertEquals(capabilities.getVersion(), "1.3.0");
            assertEquals(capabilities.getService().getName(), "WMS");
            assertEquals(capabilities.getService().getTitle(), "World Map");
            assertEquals(capabilities.getService().get_abstract(), "None");
            assertEquals(capabilities.getService().getOnlineResource(), new URL("http://www2.demis.nl"));

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

            assertEquals(
                    capabilities.getRequest().getGetFeatureInfo().getFormats().size(), 4);
            assertEquals(
                    capabilities.getRequest().getGetFeatureInfo().getFormats().get(0), "text/xml");
            assertEquals(
                    capabilities.getRequest().getGetFeatureInfo().getFormats().get(1), "text/plain");
            assertEquals(
                    capabilities.getRequest().getGetFeatureInfo().getFormats().get(2), "text/html");
            assertEquals(
                    capabilities.getRequest().getGetFeatureInfo().getFormats().get(3), "text/swf");
            assertEquals(
                    capabilities.getRequest().getGetFeatureInfo().getGet(),
                    new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));

            Layer topLayer = capabilities.getLayerList().get(0);
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

            CRSEnvelope bbox = topLayer.getBoundingBoxes().get("CRS:84");
            assertNotNull(bbox);
            assertEquals(bbox.getEPSGCode(), "CRS:84");
            assertEquals(bbox.getMinX(), -184, 0.0);
            assertEquals(bbox.getMaxX(), 180, 0.0);
            assertEquals(bbox.getMinY(), -90.0000000017335, 0.0);
            assertEquals(bbox.getMaxY(), 90, 0.0);

            Layer layer = capabilities.getLayerList().get(1);
            assertEquals(layer.getParent(), topLayer);
            assertTrue(layer.isQueryable());
            assertEquals(layer.getName(), "Bathymetry");
            assertEquals(layer.getTitle(), "Bathymetry");

            // Added test to verify inheritance, should be same as previous llbbox
            llbbox = topLayer.getLatLonBoundingBox();
            assertNotNull(llbbox);
            assertEquals(llbbox.getMinX(), -180, 0.0);
            assertEquals(llbbox.getMaxX(), 180, 0.0);
            assertEquals(llbbox.getMinY(), -90, 0.0);
            assertEquals(llbbox.getMaxY(), 90, 0.0);

            bbox = layer.getBoundingBoxes().get("CRS:84");
            assertNotNull(bbox);
            assertEquals(bbox.getEPSGCode(), "CRS:84");
            assertEquals(bbox.getMinX(), -180, 0.0);
            assertEquals(bbox.getMaxX(), 180, 0.0);
            assertEquals(bbox.getMinY(), -90, 0.0);
            assertEquals(bbox.getMaxY(), 90, 0.0);

            assertEquals(capabilities.getLayerList().size(), 21);

            layer = capabilities.getLayerList().get(20);
            assertEquals(layer.getParent(), topLayer);
            assertTrue(layer.isQueryable());
            assertEquals(layer.getName(), "Ocean features");
            assertEquals(layer.getTitle(), "Ocean features");

            // Added test to verify inheritance, should be same as previous llbbox
            llbbox = topLayer.getLatLonBoundingBox();
            assertNotNull(llbbox);
            assertEquals(llbbox.getMinX(), -180, 0.0);
            assertEquals(llbbox.getMaxX(), 180, 0.0);
            assertEquals(llbbox.getMinY(), -90, 0.0);
            assertEquals(llbbox.getMaxY(), 90, 0.0);

            bbox = layer.getBoundingBoxes().get("CRS:84");
            assertNotNull(bbox);
            assertEquals(bbox.getEPSGCode(), "CRS:84");
            assertEquals(bbox.getMinX(), -180, 0.0);
            assertEquals(bbox.getMaxX(), 179.999420166016, 0.0);
            assertEquals(bbox.getMinY(), -62.9231796264648, 0.0);
            assertEquals(bbox.getMaxY(), 68.6906585693359, 0.0);
        } catch (java.net.ConnectException ce) {
            if (ce.getMessage().indexOf("timed out") > 0) {
                LOGGER.warning("Unable to test - timed out: " + ce);
            } else {
                throw ce;
            }
        }
    }

    @Test
    public void testGEOT4706() {
        try {
            WMSCapabilities capabilities = createCapabilities("envitia-OGC.xml");

            assertNotNull(capabilities);

            assertEquals(capabilities.getVersion(), "1.3.0");
            assertEquals(capabilities.getService().getName(), "WMS");

            Layer topLayer = capabilities.getLayerList().get(0);
            assertNotNull(topLayer);
            assertNull(topLayer.getParent());
            assertFalse(topLayer.isQueryable());
            assertEquals(topLayer.getTitle(), "MapRite");
            assertEquals(topLayer.getSrs().size(), 1);
            assertTrue(topLayer.getSrs().contains("EPSG:27700"));
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    @Test
    public void testGEOT7718() {
        try {
            WMSCapabilities capabilities = createCapabilities("ign.xml");

            assertNotNull(capabilities);

            assertEquals(capabilities.getVersion(), "1.3.0");
            assertEquals(capabilities.getService().getName(), "WMS");

            Layer topLayer = capabilities.getLayerList().get(0);
            assertNotNull(topLayer);
            assertNull(topLayer.getParent());
            assertFalse(topLayer.isQueryable());
            assertEquals("WMS layers", topLayer.getTitle());
            assertEquals(192, topLayer.getSrs().size());
            assertTrue(topLayer.getSrs().contains("EPSG:2154"));
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
        }
    }

    protected WMSCapabilities createCapabilities(String capFile) throws Exception {
        try {
            File getCaps = TestData.file(this, capFile);
            URL getCapsURL = getCaps.toURI().toURL();
            Map<String, Object> hints = new HashMap<>();
            hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
            Object object = DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);

            SchemaFactory.getInstance(WMSSchema.NAMESPACE);

            Assert.assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

            WMSCapabilities capabilities = (WMSCapabilities) object;
            return capabilities;
        } catch (java.net.ConnectException ce) {
            if (ce.getMessage().indexOf("timed out") > 0) {
                // System.err.println("Unable to test - timed out: " + ce);
                return null;
            } else {
                throw ce;
            }
        }
    }

    protected void validateBoundingBox(CRSEnvelope llbbox, double minX, double minY, double maxX, double maxY) {
        assertNotNull(llbbox);
        assertEquals(llbbox.getMinX(), minX, 0.0);
        assertEquals(llbbox.getMinY(), minY, 0.0);
        assertEquals(llbbox.getMaxX(), maxX, 0.0);
        assertEquals(llbbox.getMaxY(), maxY, 0.0);
    }
}
