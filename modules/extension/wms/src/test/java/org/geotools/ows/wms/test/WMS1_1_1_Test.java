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
import org.geotools.ows.wms.StyleImpl;
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
public class WMS1_1_1_Test {
    protected static final Logger LOGGER = Logging.getLogger(WMS1_1_1_Test.class);

    public WMS1_1_1_Test() throws Exception {}

    @Test
    public void testCreateParser() throws Exception {
        try {
            WMSCapabilities capabilities = createCapabilities("1.1.1Capabilities.xml");

            assertEquals(capabilities.getVersion(), "1.1.1");
            assertEquals(capabilities.getService().getName(), "OGC:WMS");
            assertEquals(capabilities.getService().getTitle(), "Microsoft TerraServer Map Server");
            assertEquals(
                    capabilities.getService().get_abstract(),
                    "WMT Map Server maintained by Microsoft Corporation.  Data returned provided by US Geological Survey.  Contact: tbarclay@microsoft.com or gylee@usgs.gov.");
            assertEquals(capabilities.getService().getOnlineResource(), new URL("http://terraservice.net/"));

            String[] keywords = {"USGS", "DOQ", "DRG", "Topographic", "UrbanArea", "Urban Areas"};

            for (int i = 0; i < capabilities.getService().getKeywordList().length; i++) {
                assertEquals(capabilities.getService().getKeywordList()[i], keywords[i]);
            }

            assertEquals(capabilities.getService().getContactInformation().getIndividualName(), "Some guy");
            assertEquals(
                    capabilities
                            .getService()
                            .getContactInformation()
                            .getOrganisationName()
                            .toString(),
                    "Some company");
            assertEquals(
                    capabilities
                            .getService()
                            .getContactInformation()
                            .getPositionName()
                            .toString(),
                    "Researcher");
            assertEquals(
                    capabilities
                            .getService()
                            .getContactInformation()
                            .getContactInfo()
                            .getPhone()
                            .getVoices()
                            .iterator()
                            .next(),
                    "+1 555 555 5555");
            assertEquals(
                    capabilities
                            .getService()
                            .getContactInformation()
                            .getContactInfo()
                            .getPhone()
                            .getFacsimiles()
                            .iterator()
                            .next(),
                    "+1 555 555 5556");
            assertEquals(
                    capabilities
                            .getService()
                            .getContactInformation()
                            .getContactInfo()
                            .getAddress()
                            .getPostalCode(),
                    "11111");
            assertEquals(
                    capabilities
                            .getService()
                            .getContactInformation()
                            .getContactInfo()
                            .getAddress()
                            .getAdministrativeArea()
                            .toString(),
                    "CA");
            assertEquals(
                    capabilities
                            .getService()
                            .getContactInformation()
                            .getContactInfo()
                            .getAddress()
                            .getCity()
                            .toString(),
                    "San Francisco");
            assertEquals(
                    capabilities
                            .getService()
                            .getContactInformation()
                            .getContactInfo()
                            .getAddress()
                            .getCountry()
                            .toString(),
                    "USA");
            assertEquals(
                    capabilities
                            .getService()
                            .getContactInformation()
                            .getContactInfo()
                            .getAddress()
                            .getDeliveryPoints()
                            .iterator()
                            .next(),
                    "555 Street St.");
            assertEquals(
                    capabilities
                            .getService()
                            .getContactInformation()
                            .getContactInfo()
                            .getAddress()
                            .getElectronicMailAddresses()
                            .iterator()
                            .next(),
                    "email@domain.com");

            assertEquals(
                    capabilities.getRequest().getGetCapabilities().getFormats().get(0), "application/vnd.ogc.wms_xml");
            assertEquals(
                    capabilities.getRequest().getGetCapabilities().getGet(),
                    new URL("http://terraservice.net/ogccapabilities.ashx"));
            assertEquals(
                    capabilities.getRequest().getGetCapabilities().getPost(),
                    new URL("http://terraservice.net/ogccapabilities.ashx"));

            assertEquals(capabilities.getRequest().getGetMap().getFormats().get(0), "image/jpeg");
            assertEquals(
                    capabilities.getRequest().getGetMap().getGet(), new URL("http://terraservice.net/ogcmap.ashx"));

            assertNull(capabilities.getRequest().getGetFeatureInfo());

            assertEquals(capabilities.getLayerList().size(), 4);

            Layer layer = capabilities.getLayerList().get(0);
            assertNotNull(layer);
            assertNull(layer.getName());
            assertEquals(layer.getTitle(), "Microsoft TerraServer Map Server");
            assertEquals(layer.getSrs().size(), 1);
            assertTrue(layer.getSrs().contains("EPSG:4326"));

            validateBoundingBox(layer.getLatLonBoundingBox(), -168.67, 17.84, -65.15, 71.55);

            assertNull(layer.getParent());
            assertEquals(layer.getBoundingBoxes().size(), 0);
            assertEquals(layer.getStyles().size(), 0);

            layer = capabilities.getLayerList().get(1);
            assertEquals(layer.getName(), "DOQ");
            assertEquals(layer.getTitle(), "USGS Digital Ortho-Quadrangles");
            // changed expected to 14 to account for inherited srs
            assertEquals(layer.getSrs().size(), 14);
            // Added additional check to test for inherited srs
            assertTrue(layer.getSrs().contains("EPSG:4326"));
            assertTrue(layer.getSrs().contains("EPSG:26905"));
            assertTrue(layer.getSrs().contains("EPSG:26920"));
            assertEquals(layer.getBoundingBoxes().size(), 13);
            CRSEnvelope bbox = layer.getBoundingBoxes().get("EPSG:26905");
            assertNotNull(bbox);
            assertEquals(bbox.getEPSGCode(), "EPSG:26905");
            assertEquals(bbox.getMinX(), 552600.0, 0.0);
            assertEquals(bbox.getMinY(), 6540200.0, 0.0);
            assertEquals(bbox.getMaxX(), 670200.0, 0.0);
            assertEquals(bbox.getMaxY(), 6794800.0, 0.0);
            assertEquals(10, layer.getScaleDenominatorMin(), 0);
            assertEquals(10000, layer.getScaleDenominatorMax(), 0);

            bbox = layer.getBoundingBoxes().get("EPSG:26920");
            assertNotNull(bbox);
            assertEquals(bbox.getEPSGCode(), "EPSG:26920");
            assertEquals(bbox.getMinX(), 181800.0, 0.0);
            assertEquals(bbox.getMinY(), 1985200.0, 0.0);
            assertEquals(bbox.getMaxX(), 269400.0, 0.0);
            assertEquals(bbox.getMaxY(), 2048600.0, 0.0);

            // Changed expected value, no duplicates allowed by spec
            assertEquals(layer.getStyles().size(), 18);
            assertTrue(layer.getStyles().contains(new StyleImpl("UTMGrid")));
            assertTrue(layer.getStyles().contains(new StyleImpl("GeoGrid_Cyan")));
            assertTrue(layer.getStyles().contains(new StyleImpl("GeoGrid_Black")));
            assertTrue(layer.getStyles().contains(new StyleImpl("GeoGrid_Gray")));
            assertTrue(layer.getStyles().contains(new StyleImpl("GeoGrid_White")));

            StyleImpl utmGrid = layer.getStyles().get(0);
            assertEquals(utmGrid.getName(), "UTMGrid");
            assertEquals(utmGrid.getAbstract().toString(), "Display grid lines in Goldenrod on the DOQ image");
            assertEquals(utmGrid.getTitle().toString(), "Goldedrod Grid Lines on UTM coordinates");

            assertFalse(layer.isQueryable());

            // Added test to verify inheritance, should be same as previous llbbox
            validateBoundingBox(layer.getLatLonBoundingBox(), -168.67, 17.84, -65.15, 71.55);

            layer = capabilities.getLayerList().get(2);
            assertNotNull(layer);
            assertEquals(layer.getName(), "DRG");
            assertEquals(layer.getTitle(), "USGS Raster Graphics (Topo Maps)");
            // Added test to verify inheritance, should be same as previous llbbox
            validateBoundingBox(layer.getLatLonBoundingBox(), -168.67, 17.84, -65.15, 71.55);

            assertEquals(50, layer.getScaleDenominatorMin(), 0);

            layer = capabilities.getLayerList().get(3);
            assertNotNull(layer);
            assertEquals(layer.getName(), "UrbanArea");
            assertEquals(layer.getTitle(), "USGS Urban Areas Ortho-Imagery");
            // Added test to verify inheritance, should be same as previous llbbox
            validateBoundingBox(layer.getLatLonBoundingBox(), -168.67, 17.84, -65.15, 71.55);
            assertEquals(50000, layer.getScaleDenominatorMax(), 0);

        } catch (java.net.ConnectException ce) {
            if (ce.getMessage().indexOf("timed out") > 0) {
                LOGGER.warning("Unable to test - timed out: " + ce);
            } else {
                throw ce;
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
    /* (non-Javadoc)
     * @see org.geotools.data.wms.test.WMS1_0_0Test#checkProperties(java.util.Properties)
     */
    protected void checkProperties(Properties properties) {
        assertEquals(properties.get("VERSION"), "1.1.1");
        assertEquals(properties.get("REQUEST"), "GetCapabilities");
        assertEquals(properties.get("SERVICE"), "WMS");
    }
}
