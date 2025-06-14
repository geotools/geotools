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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.metadata.citation.Address;
import org.geotools.data.ows.OperationType;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wmts.WMTSSpecification;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Test;
import org.xml.sax.SAXException;

public class WMTSCapabilitiesTest {

    static final Logger LOGGER = Logging.getLogger(WMTSCapabilitiesTest.class);

    protected WMTSSpecification spec;

    public WMTSCapabilitiesTest() throws Exception {
        this.spec = new WMTSSpecification();
    }

    @Test
    public void testGetVersion() {
        Assert.assertEquals(spec.getVersion(), "1.0.0");
    }

    protected void checkProperties(Properties properties) {
        Assert.assertEquals(properties.getProperty("REQUEST"), "GetCapabilities");
        Assert.assertEquals(properties.getProperty("VERSION"), "1.0.0");
    }

    @Test
    public void testCreateParser() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("GeoServer_2.2.x/1.0.0/GetCapabilities.xml");
        try {
            Assert.assertEquals("1.0.0", capabilities.getVersion());
            Assert.assertEquals("OGC WMTS", capabilities.getService().getName());
            Assert.assertEquals(
                    "Web Map Tile Service - GeoWebCache",
                    capabilities.getService().getTitle());

            for (int i = 0; i < capabilities.getService().getKeywordList().length; i++) {
                Assert.assertEquals(
                        capabilities.getService().getKeywordList()[i], "OpenGIS WMS Web Map Server".split(" ")[i]);
            }

            WMTSRequest request = capabilities.getRequest();

            OperationType getTile = request.getGetTile();
            Assert.assertNotNull(getTile);

            Assert.assertEquals(110, capabilities.getLayerList().size());

            List<WMTSLayer> layers = capabilities.getLayerList();
            WMTSLayer l0 = layers.get(0);

            Assert.assertEquals("OML_Foreshore", l0.getTitle());
            Assert.assertNull(l0.getParent());
            // case should not matter
            Assert.assertEquals(3, l0.getBoundingBoxes().size());

            Assert.assertEquals(2, l0.getTileMatrixLinks().size());
            TileMatrixSetLink tmsl0 = l0.getTileMatrixLinks().get("EPSG:4326");
            Assert.assertNotNull(tmsl0);
            Assert.assertEquals("EPSG:4326", tmsl0.getIdentifier());
            List<TileMatrixLimits> tmLimits = tmsl0.getLimits();
            Assert.assertNotNull(tmLimits);
            Assert.assertEquals("Bad size: TileMatrixLimits", 22, tmLimits.size());
            TileMatrixLimits tml3 = tmLimits.get(3);
            Assert.assertEquals(1, tml3.getMinrow());
            Assert.assertEquals(1, tml3.getMaxrow());
            Assert.assertEquals(7, tml3.getMincol());
            Assert.assertEquals(7, tml3.getMaxcol());

            Assert.assertEquals("b_road", layers.get(1).getTitle());
            Assert.assertEquals("meridian:b_road", layers.get(1).getName());
            Assert.assertEquals("b_road_polyline", layers.get(20).getTitle());
            Assert.assertEquals("meridian:b_road_polyline", layers.get(20).getName());

            CRSEnvelope bbox = layers.get(1).getBoundingBoxes().get("EPSG:4326");
            Assert.assertNotNull(bbox);
        } catch (Exception e) {
            Logger.getGlobal().log(Level.INFO, "", e);
            if (e.getMessage() != null && e.getMessage().indexOf("timed out") > 0) {
                LOGGER.warning("Unable to test - timed out: " + e);
            } else {
                throw e;
            }
        }
    }

    /**
     * A Layer might have a MatrixSetLink with an identifier that isn't within the MatrixSet's. Such a link should be
     * removed.
     */
    @Test
    public void testMissingMatrixSetLink() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("nasa.getcapa.xml");
        WMTSLayer layer = capabilities.getLayer("Blue_Marble_Extended");
        TileMatrixSetLink link = layer.getTileMatrixLinks().get("1.5km");
        Assert.assertNull("1.5km isn't defined as a MatrixSet and should be omitted.", link);
    }

    @Test
    public void testParser2() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("admin_ch.getcapa.xml");
        try {
            Assert.assertEquals("1.0.0", capabilities.getVersion());

            WMTSService service = (WMTSService) capabilities.getService();
            Assert.assertEquals("OGC WMTS", service.getName());
            Assert.assertEquals("WMTS BGDI", service.getTitle());

            String[] keywordList = service.getKeywordList();
            Assert.assertNotNull(keywordList);
            Assert.assertEquals("Switzerland", keywordList[0]);
            Assert.assertEquals("Web Map Service", keywordList[1]);

            WMTSRequest request = capabilities.getRequest();

            OperationType getTile = request.getGetTile();
            Assert.assertNotNull(getTile);

            Assert.assertEquals(306, capabilities.getLayerList().size());

            List<WMTSLayer> layers = capabilities.getLayerList();
            WMTSLayer l0 = layers.get(0);

            Assert.assertEquals("ch.are.agglomerationen_isolierte_staedte", l0.getName());
            Assert.assertNull(l0.getParent());
            Assert.assertTrue(l0.getSrs().contains("EPSG:2056")); // case should not
            // matter

            Assert.assertNotNull("Missing dimensions", l0.getDimensions());
            Assert.assertEquals("Bad dimensions size", 1, l0.getDimensions().size());
            String dimName = l0.getDimensions().keySet().iterator().next();
            Assert.assertTrue("Bad dimension name (Time!=" + dimName + ")", "Time".equalsIgnoreCase(dimName));

            Assert.assertNotNull(l0.getTileMatrixLinks());
            Assert.assertEquals(1, l0.getTileMatrixLinks().keySet().size());
            Assert.assertEquals(
                    "2056_26", l0.getTileMatrixLinks().keySet().iterator().next());
            Assert.assertEquals(
                    "2056_26",
                    l0.getTileMatrixLinks().values().iterator().next().getIdentifier());
            Assert.assertEquals(
                    0,
                    l0.getTileMatrixLinks()
                            .values()
                            .iterator()
                            .next()
                            .getLimits()
                            .size());

            Assert.assertEquals(12, capabilities.getMatrixSets().size());
            Assert.assertEquals("2056_17", capabilities.getMatrixSets().get(0).getIdentifier());
            Assert.assertEquals(
                    18, capabilities.getMatrixSets().get(0).getMatrices().size());
            Assert.assertEquals(
                    14285750.5715,
                    capabilities.getMatrixSets().get(0).getMatrices().get(0).getDenominator(),
                    0d);

        } catch (Exception e) {
            Logger.getGlobal().log(Level.INFO, "", e);
            if (e.getMessage() != null && e.getMessage().indexOf("timed out") > 0) {
                LOGGER.warning("Unable to test - timed out: " + e);
            } else {
                throw e;
            }
        }
    }

    @Test
    public void testParser3() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("nasa.getcapa.xml");
        try {
            Assert.assertEquals("1.0.0", capabilities.getVersion());

            WMTSService service = (WMTSService) capabilities.getService();
            Assert.assertEquals("OGC WMTS", service.getName());
            Assert.assertEquals("NASA Global Imagery Browse Services for EOSDIS", service.getTitle());

            String[] keywordList = service.getKeywordList();
            Assert.assertNotNull(keywordList);
            Assert.assertEquals("World", keywordList[0]);
            Assert.assertEquals("Global", keywordList[1]);

            WMTSRequest request = capabilities.getRequest();

            OperationType getTile = request.getGetTile();
            Assert.assertNotNull(getTile);

            Assert.assertEquals(520, capabilities.getLayerList().size());

            List<WMTSLayer> layers = capabilities.getLayerList();
            WMTSLayer l0 = layers.get(0);

            Assert.assertEquals("AMSR2_Snow_Water_Equivalent", l0.getName());
            Assert.assertNull(l0.getParent());

            // assertTrue(l0.getSrs().contains("urn:ogc:def:crs:OGC:2:84")); //
            // case should not matter
            Assert.assertTrue(l0.getSrs().contains("CRS:84"));

            Assert.assertNotNull("Missing dimensions", l0.getDimensions());
            Assert.assertEquals("Bad dimensions size", 1, l0.getDimensions().size());
            String dimName = l0.getDimensions().keySet().iterator().next();
            Assert.assertTrue("Bad dimension name (Time!=" + dimName + ")", "Time".equalsIgnoreCase(dimName));

            CRSEnvelope bbox = layers.get(1).getBoundingBoxes().get("CRS:84");
            Assert.assertNotNull(bbox);

        } catch (Exception e) {
            Logger.getGlobal().log(Level.INFO, "", e);
            if (e.getMessage() != null && e.getMessage().indexOf("timed out") > 0) {
                LOGGER.warning("Unable to test - timed out: " + e);
            } else {
                throw e;
            }
        }
    }

    @Test
    public void testParserOLSample() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("ol.getcapa.xml");
        try {
            Assert.assertEquals("1.0.0", capabilities.getVersion());

            WMTSService service = (WMTSService) capabilities.getService();
            Assert.assertEquals("Koordinates Labs", service.getTitle());

            List<TileMatrixSet> matrixSets = capabilities.getMatrixSets();
            Assert.assertNotNull(matrixSets);
            Assert.assertFalse(matrixSets.isEmpty());

            Assert.assertEquals(
                    "urn:ogc:def:wkss:OGC:1.0:GoogleMapsCompatible",
                    matrixSets.get(0).getWellKnownScaleSet());

        } catch (Exception e) {
            // a standard catch block shared with the other tests
            if (e.getMessage() != null && e.getMessage().indexOf("timed out") > 0) {
                LOGGER.warning("Unable to test - timed out: " + e);
            } else {
                throw e;
            }
        }
    }

    // This particular capabilities response does not contain a ServiceProvider section
    // Make sure things still work
    @Test
    public void testParser4() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("geodata.nationaalgeoregister.nl.xml");
        try {
            Assert.assertEquals("1.0.0", capabilities.getVersion());

            WMTSService service = (WMTSService) capabilities.getService();
            Assert.assertEquals("OGC WMTS", service.getName());
            Assert.assertEquals("Web Map Tile Service", service.getTitle());

            WMTSRequest request = capabilities.getRequest();

            OperationType getTile = request.getGetTile();
            Assert.assertNotNull(getTile);

            Assert.assertEquals(44, capabilities.getLayerList().size());

            List<WMTSLayer> layers = capabilities.getLayerList();
            WMTSLayer l0 = layers.get(0);

            Assert.assertEquals("brtachtergrondkaart", l0.getName());
            Assert.assertTrue(l0.getSrs().contains("EPSG:28992")); // case

        } catch (Exception e) {
            // a standard catch block shared with the other tests
            if (e.getMessage() != null && e.getMessage().indexOf("timed out") > 0) {
                LOGGER.warning("Unable to test - timed out: " + e);
            } else {
                throw e;
            }
        }
    }

    @Test
    public void testParseWithIncompleteAddressMetadata() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("vienna_capa.xml");
        WMTSService service = (WMTSService) capabilities.getService();
        Address address = service.getContactInformation().getContactInfo().getAddress();
        Assert.assertNull(address.getAdministrativeArea());
        Assert.assertNull(address.getPostalCode());
        Assert.assertNotNull(address.getCity());
        Assert.assertNotNull(address.getCountry());
        Assert.assertNotNull(address.getElectronicMailAddresses());
    }

    /**
     * The section OperationsMetadata of GetCapabilities is optional. WMTSCapabilities used as argument for
     * WebMapTileServer shouldn't make a NullPointerException.
     */
    @Test
    public void testMissingOperationsMetadata() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("linz_basemaps_capabilities.xml");
        WebMapTileServer tileServer = new WebMapTileServer(
                new URL("https://basemaps.linz.govt.nz/v1/tiles/WMTSCapabilities.xml"), capabilities);
        Assert.assertNotNull(tileServer);
    }

    /**
     * Testing WMTSCapabilities parseLayer and it's support for missing Title, multiple Title elements with different
     * xml:lang, and multiple abstracts.
     */
    @Test
    public void testMultilanguageCapabilities() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("multilang.getcapa.xml");
        WMTSLayer layer = capabilities.getLayer("plz");
        Assert.assertEquals("Post Code", layer.getTitle());

        WMTSLayer layer2 = capabilities.getLayer("isolines");
        Assert.assertEquals("isolines", layer2.getTitle());

        WMTSLayer layer3 = capabilities.getLayer("overlay-all");
        Assert.assertEquals("Map overlay with all layers.", layer3.get_abstract());
    }

    protected WebMapTileServer getCustomWMS(URL featureURL) throws SAXException, URISyntaxException, IOException {
        return new WebMapTileServer(featureURL);
    }
}
