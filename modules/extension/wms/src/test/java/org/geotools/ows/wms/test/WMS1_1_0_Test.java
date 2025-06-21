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

/**
 * @author Richard Gould
 * @author ian
 */
public class WMS1_1_0_Test {
    protected static final Logger LOGGER = Logging.getLogger(WMS1_1_0_Test.class);

    public WMS1_1_0_Test() throws Exception {}

    /* (non-Javadoc)
     * @see org.geotools.data.wms.test.WMS1_0_0Test#checkProperties(java.util.Properties)
     */

    protected void checkProperties(Properties properties) {
        assertEquals(properties.get("REQUEST"), "GetCapabilities");
        assertEquals(properties.get("VERSION"), "1.1.0");
        assertEquals(properties.get("SERVICE"), "WMS");
    }

    @Test
    public void testCreateParser() throws Exception {
        WMSCapabilities capabilities = createCapabilities("1.1.0Capabilities.xml");
        try {
            assertEquals(capabilities.getVersion(), "1.1.0");
            assertEquals(capabilities.getService().getName(), "OGC:WMS");
            assertEquals(capabilities.getService().getTitle(), "GMap WMS Demo Server");
            assertNotNull(capabilities.getService().get_abstract());
            assertEquals(
                    capabilities.getService().getOnlineResource(),
                    new URL("http://dev1.dmsolutions.ca/cgi-bin/mswms_gmap?"));
            assertNull(capabilities.getService().getKeywordList());

            assertEquals(
                    capabilities.getRequest().getGetCapabilities().getFormats().size(), 1);
            assertEquals(
                    capabilities.getRequest().getGetCapabilities().getFormats().get(0), "application/vnd.ogc.wms_xml");
            assertEquals(
                    capabilities.getRequest().getGetCapabilities().getGet(),
                    new URL("http://dev1.dmsolutions.ca/cgi-bin/mswms_gmap?"));
            assertEquals(
                    capabilities.getRequest().getGetCapabilities().getPost(),
                    new URL("http://dev1.dmsolutions.ca/cgi-bin/mswms_gmap?"));

            assertEquals(capabilities.getRequest().getGetMap().getFormats().size(), 7);
            assertEquals(capabilities.getRequest().getGetMap().getFormats().get(0), "image/gif");
            assertEquals(capabilities.getRequest().getGetMap().getFormats().get(3), "image/wbmp");
            assertEquals(capabilities.getRequest().getGetMap().getFormats().get(6), "image/tiff");
            assertEquals(
                    capabilities.getRequest().getGetMap().getGet(),
                    new URL("http://dev1.dmsolutions.ca/cgi-bin/mswms_gmap?"));
            assertEquals(
                    capabilities.getRequest().getGetMap().getPost(),
                    new URL("http://dev1.dmsolutions.ca/cgi-bin/mswms_gmap?"));

            assertEquals(
                    capabilities.getRequest().getGetFeatureInfo().getFormats().size(), 3);
            assertEquals(
                    capabilities.getRequest().getGetFeatureInfo().getFormats().get(0), "text/plain");
            assertEquals(
                    capabilities.getRequest().getGetFeatureInfo().getFormats().get(1), "text/html");
            assertEquals(
                    capabilities.getRequest().getGetFeatureInfo().getFormats().get(2), "application/vnd.ogc.gml");

            assertEquals(capabilities.getLayerList().size(), 12);

            Layer layer = capabilities.getLayerList().get(0);
            assertNull(layer.getParent());
            assertEquals(layer.getName(), "DEMO");
            assertEquals(layer.get_abstract(), "Abstract Test");
            String[] keywords = layer.getKeywords();
            assertNotNull(keywords);
            assertEquals(keywords.length, 2);
            assertEquals(keywords[0], "word1");
            assertEquals(keywords[1], "word2");
            assertEquals(layer.getTitle(), "GMap WMS Demo Server");
            assertEquals(layer.getSrs().size(), 4);
            assertTrue(layer.getSrs().contains("EPSG:42304"));
            assertTrue(layer.getSrs().contains("EPSG:42101"));
            assertTrue(layer.getSrs().contains("EPSG:4269"));
            assertTrue(layer.getSrs().contains("EPSG:4326"));

            CRSEnvelope llbbox = layer.getLatLonBoundingBox();
            validateBoundingBox(llbbox, -172.367, 35.6673, -11.5624, 83.8293);

            assertEquals(layer.getBoundingBoxes().size(), 1);
            assertNotNull(layer.getBoundingBoxes().get("EPSG:42304"));

            Layer layer2 = capabilities.getLayerList().get(1);
            assertEquals(layer2.getParent(), layer);
            assertEquals(layer2.getName(), "bathymetry");
            assertEquals(layer2.getTitle(), "Elevation/Bathymetry");
            assertTrue(layer2.getSrs().contains("EPSG:42304"));
            assertFalse(layer2.isQueryable());

            layer2 = capabilities.getLayerList().get(2);
            assertEquals(layer2.getParent(), layer);
            assertEquals(layer2.getName(), "land_fn");
            assertEquals(layer2.getTitle(), "Foreign Lands");

            validateBoundingBox(layer2.getLatLonBoundingBox(), -178.838, 31.8844, 179.94, 89.8254);

            assertTrue(layer2.getSrs().contains("EPSG:42304"));
            assertFalse(layer2.isQueryable());
            assertNotNull(layer2.getBoundingBoxes().get("EPSG:42304"));

            layer2 = capabilities.getLayerList().get(3);
            assertEquals(layer2.getParent(), layer);
            assertEquals(layer2.getName(), "park");
            assertEquals(layer2.getTitle(), "Parks");

            validateBoundingBox(layer2.getLatLonBoundingBox(), -173.433, 41.4271, -13.3643, 83.7466);

            assertTrue(layer2.getSrs().contains("EPSG:42304"));
            assertTrue(layer2.isQueryable());
            assertNotNull(layer2.getBoundingBoxes().get("EPSG:42304"));

            layer2 = capabilities.getLayerList().get(11);
            assertEquals(layer2.getParent(), layer);
            assertEquals(layer2.getName(), "grid");
            assertEquals(layer2.getTitle(), "Grid");

            llbbox = layer2.getLatLonBoundingBox();
            validateBoundingBox(llbbox, -178.838, 31.8844, 179.94, 89.8254);

            assertTrue(layer2.getSrs().contains("EPSG:42304"));
            assertFalse(layer2.isQueryable());
            assertNotNull(layer2.getBoundingBoxes().get("EPSG:42304"));
        } catch (Exception e) {
            if (e.getMessage().indexOf("timed out") > 0) {
                LOGGER.warning("Unable to test - timed out: " + e);
            } else {
                throw e;
            }
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
