/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation; version 2.1 of
 * the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
 * even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.ows.wmts.model;

import static org.geotools.ows.wmts.WMTSTestUtils.createCapabilities;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import junit.framework.TestCase;
import org.geotools.data.ows.OperationType;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wmts.WMTSSpecification;
import org.geotools.ows.wmts.WebMapTileServer;
import org.xml.sax.SAXException;

public class WMTSCapabilitiesTest extends TestCase {

    protected WMTSSpecification spec;

    public WMTSCapabilitiesTest() throws Exception {
        this.spec = new WMTSSpecification();
    }

    public void testGetVersion() {
        assertEquals(spec.getVersion(), "1.0.0");
    }

    protected void checkProperties(Properties properties) {
        assertEquals(properties.getProperty("REQUEST"), "GetCapabilities");
        assertEquals(properties.getProperty("VERSION"), "1.0.0");
    }

    public void testCreateParser() throws Exception {
        WMTSCapabilities capabilities =
                createCapabilities("GeoServer_2.2.x/1.0.0/GetCapabilities.xml");
        try {
            assertEquals("1.0.0", capabilities.getVersion());
            assertEquals("OGC WMTS", capabilities.getService().getName());
            assertEquals(
                    "Web Map Tile Service - GeoWebCache", capabilities.getService().getTitle());

            for (int i = 0; i < capabilities.getService().getKeywordList().length; i++) {
                assertEquals(
                        capabilities.getService().getKeywordList()[i],
                        "OpenGIS WMS Web Map Server".split(" ")[i]);
            }

            WMTSRequest request = capabilities.getRequest();

            OperationType getTile = request.getGetTile();
            assertNotNull(getTile);

            assertEquals(110, capabilities.getLayerList().size());

            List<WMTSLayer> layers = capabilities.getLayerList();
            WMTSLayer l0 = layers.get(0);

            assertEquals("OML_Foreshore", l0.getTitle());
            assertNull(l0.getParent());
            assertTrue(
                    l0.getSrs().contains("urn:ogc:def:crs:EPSG::4326")); // case should not matter
            assertEquals(4, l0.getBoundingBoxes().size());

            assertEquals(2, l0.getTileMatrixLinks().size());
            TileMatrixSetLink tmsl0 = l0.getTileMatrixLinks().get("EPSG:4326");
            assertNotNull(tmsl0);
            assertEquals("EPSG:4326", tmsl0.getIdentifier());
            List<TileMatrixLimits> tmLimits = tmsl0.getLimits();
            assertNotNull(tmLimits);
            assertEquals("Bad size: TileMatrixLimits", 22, tmLimits.size());
            TileMatrixLimits tml3 = tmLimits.get(3);
            assertEquals(1, tml3.getMinrow());
            assertEquals(1, tml3.getMaxrow());
            assertEquals(7, tml3.getMincol());
            assertEquals(7, tml3.getMaxcol());

            assertEquals("b_road", layers.get(1).getTitle());
            assertEquals("meridian:b_road", layers.get(1).getName());
            assertEquals("b_road_polyline", layers.get(20).getTitle());
            assertEquals("meridian:b_road_polyline", layers.get(20).getName());

            CRSEnvelope bbox = (CRSEnvelope) layers.get(1).getBoundingBoxes().get("EPSG:4326");
            assertNotNull(bbox);
        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            if ((e.getMessage() != null) && e.getMessage().indexOf("timed out") > 0) {
                // System.err.println("Unable to test - timed out: " + e);
            } else {
                throw (e);
            }
        }
    }

    public void testParser2() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("admin_ch.getcapa.xml");
        try {
            assertEquals("1.0.0", capabilities.getVersion());

            WMTSService service = (WMTSService) capabilities.getService();
            assertEquals("OGC WMTS", service.getName());
            assertEquals("WMTS BGDI", service.getTitle());

            String[] keywordList = service.getKeywordList();
            assertNotNull(keywordList);
            assertEquals("Switzerland", keywordList[0]);
            assertEquals("Web Map Service", keywordList[1]);

            WMTSRequest request = capabilities.getRequest();

            OperationType getTile = request.getGetTile();
            assertNotNull(getTile);

            assertEquals(306, capabilities.getLayerList().size());

            List<WMTSLayer> layers = capabilities.getLayerList();
            WMTSLayer l0 = layers.get(0);

            assertEquals("ch.are.agglomerationen_isolierte_staedte", l0.getName());
            assertNull(l0.getParent());
            assertTrue(l0.getSrs().contains("urn:ogc:def:crs:EPSG::2056")); // case
            // should
            // not
            // matter
            assertTrue(l0.getSrs().contains("EPSG:2056")); // case should not
            // matter

            assertNotNull("Missing dimensions", l0.getDimensions());
            assertEquals("Bad dimensions size", 1, l0.getDimensions().size());
            String dimName = l0.getDimensions().keySet().iterator().next();
            assertTrue(
                    "Bad dimension name (Time!=" + dimName + ")", "Time".equalsIgnoreCase(dimName));

            assertNotNull(l0.getTileMatrixLinks());
            assertEquals(1, l0.getTileMatrixLinks().keySet().size());
            assertEquals("2056_26", l0.getTileMatrixLinks().keySet().iterator().next());
            assertEquals(
                    "2056_26", l0.getTileMatrixLinks().values().iterator().next().getIdentifier());
            assertEquals(0, l0.getTileMatrixLinks().values().iterator().next().getLimits().size());

            assertEquals(12, capabilities.getMatrixSets().size());
            assertEquals("2056_17", capabilities.getMatrixSets().get(0).getIdentifier());
            assertEquals(18, capabilities.getMatrixSets().get(0).getMatrices().size());
            assertEquals(
                    14285750.5715,
                    capabilities.getMatrixSets().get(0).getMatrices().get(0).getDenominator());

            CRSEnvelope bbox = (CRSEnvelope) layers.get(1).getBoundingBoxes().get("EPSG:4326");
            assertNotNull(bbox);

        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            if ((e.getMessage() != null) && e.getMessage().indexOf("timed out") > 0) {
                // System.err.println("Unable to test - timed out: " + e);
            } else {
                throw (e);
            }
        }
    }

    public void testParser3() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("nasa.getcapa.xml");
        try {
            assertEquals("1.0.0", capabilities.getVersion());

            WMTSService service = (WMTSService) capabilities.getService();
            assertEquals("OGC WMTS", service.getName());
            assertEquals("NASA Global Imagery Browse Services for EOSDIS", service.getTitle());

            String[] keywordList = service.getKeywordList();
            assertNotNull(keywordList);
            assertEquals("World", keywordList[0]);
            assertEquals("Global", keywordList[1]);

            WMTSRequest request = capabilities.getRequest();

            OperationType getTile = request.getGetTile();
            assertNotNull(getTile);

            assertEquals(519, capabilities.getLayerList().size());

            List<WMTSLayer> layers = capabilities.getLayerList();
            WMTSLayer l0 = layers.get(0);

            assertEquals("AMSR2_Snow_Water_Equivalent", l0.getName());
            assertNull(l0.getParent());

            // assertTrue(l0.getSrs().contains("urn:ogc:def:crs:OGC:2:84")); //
            // case should not matter
            assertTrue(l0.getSrs().contains("CRS:84"));

            assertNotNull("Missing dimensions", l0.getDimensions());
            assertEquals("Bad dimensions size", 1, l0.getDimensions().size());
            String dimName = l0.getDimensions().keySet().iterator().next();
            assertTrue(
                    "Bad dimension name (Time!=" + dimName + ")", "Time".equalsIgnoreCase(dimName));

            CRSEnvelope bbox = (CRSEnvelope) layers.get(1).getBoundingBoxes().get("EPSG:4326");
            assertNotNull(bbox);

        } catch (Exception e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            if ((e.getMessage() != null) && e.getMessage().indexOf("timed out") > 0) {
                // System.err.println("Unable to test - timed out: " + e);
            } else {
                throw (e);
            }
        }
    }

    public void testParserOLSample() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("ol.getcapa.xml");
        try {
            assertEquals("1.0.0", capabilities.getVersion());

            WMTSService service = (WMTSService) capabilities.getService();
            assertEquals("Koordinates Labs", service.getTitle());

            List<TileMatrixSet> matrixSets = capabilities.getMatrixSets();
            assertNotNull(matrixSets);
            assertFalse(matrixSets.isEmpty());

            assertEquals(
                    "urn:ogc:def:wkss:OGC:1.0:GoogleMapsCompatible",
                    matrixSets.get(0).getWellKnownScaleSet());

        } catch (Exception e) {
            // a standard catch block shared with the other tests
            if ((e.getMessage() != null) && e.getMessage().indexOf("timed out") > 0) {
                // System.err.println("Unable to test - timed out: " + e);
            } else {
                throw (e);
            }
        }
    }

    protected WebMapTileServer getCustomWMS(URL featureURL)
            throws SAXException, URISyntaxException, IOException {
        return new WebMapTileServer(featureURL);
    }
}
