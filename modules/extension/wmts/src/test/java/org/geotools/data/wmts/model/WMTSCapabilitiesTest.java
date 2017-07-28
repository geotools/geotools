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
package org.geotools.data.wmts.model;

import org.geotools.data.wmts.model.WMTSLayer;
import org.geotools.data.wmts.model.WMTSCapabilities;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.OperationType;
import org.geotools.data.wmts.request.GetTileRequest;
import org.geotools.test.TestData;
import org.geotools.wmts.WMTSConfiguration;
import org.geotools.xml.Parser;
import org.xml.sax.SAXException;

import junit.framework.TestCase;
import net.opengis.wmts.v_1.CapabilitiesType;
import org.geotools.data.wmts.WMTSSpecification;
import org.geotools.data.wmts.WebMapTileServer;
import org.geotools.data.wmts.model.TileMatrixLimits;
import org.geotools.data.wmts.model.TileMatrixSetLink;

/**
 *
 *
 *
 * @source $URL$
 */
public class WMTSCapabilitiesTest extends TestCase {
    protected URL server;

    protected WMTSSpecification spec;

    public WMTSCapabilitiesTest() throws Exception {
        this.spec = new WMTSSpecification();
        this.server = new URL("http://maps.boundlessgeo-dev.com/geoserver/gwc/service/wmts?");
    }

    public void testGetVersion() {
        assertEquals(spec.getVersion(), "1.0.0");
    }

    
    protected void checkProperties(Properties properties) {
        assertEquals(properties.getProperty("REQUEST"), "GetCapabilities");
        assertEquals(properties.getProperty("VERSION"), "1.0.0");
    }

    public void testCreateParser() throws Exception {
        WMTSCapabilities capabilities = createCapabilities(
                "GeoServer_2.2.x/1.0.0/GetCapabilities.xml");
        try {
            assertEquals("1.0.0", capabilities.getVersion());
            assertEquals("OGC WMTS", capabilities.getService().getName());
            assertEquals("Web Map Tile Service - GeoWebCache",
                    capabilities.getService().getTitle());

            for (int i = 0; i < capabilities.getService().getKeywordList().length; i++) {
                assertEquals(capabilities.getService().getKeywordList()[i],
                        "OpenGIS WMS Web Map Server".split(" ")[i]);
            }

            WMTSRequest request = capabilities.getRequest();

            assertEquals(request.getGetFeatureInfo().getGet(),
                    new URL("http://astun-desktop:8080/geoserver/gwc/service/wmts?"));
            OperationType getTile = request.getGetTile();
            assertNotNull(getTile);

            assertEquals(110,capabilities.getLayerList().size());

            Layer[] layers = (Layer[]) capabilities.getLayerList()
                    .toArray(new Layer[capabilities.getLayerList().size()]);

            WMTSLayer l0 = (WMTSLayer)layers[0];
            assertEquals("OML_Foreshore", l0.getTitle());
            assertNull(l0.getParent());
            assertTrue(l0.getSrs().contains("urn:ogc:def:crs:EPSG::4326")); // case should not matter
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
            
            assertEquals("b_road",layers[1].getTitle());
            assertEquals("meridian:b_road", layers[1].getName() );
            assertEquals("b_road_polyline",layers[20].getTitle());
            assertEquals("meridian:b_road_polyline",layers[20].getName());

            CRSEnvelope bbox = (CRSEnvelope) layers[1].getBoundingBoxes().get("EPSG:4326");
            assertNotNull(bbox);
        } catch (Exception e) {
            e.printStackTrace();
            if ((e.getMessage() != null) && e.getMessage().indexOf("timed out") > 0) {
                System.err.println("Unable to test - timed out: " + e);
            } else {
                throw (e);
            }
        }
    }

    public void testParser2() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("admin_ch.getcapa.xml");
        try {
            assertEquals("1.0.0", capabilities.getVersion());

            WMTSService service = (WMTSService)capabilities.getService();
            assertEquals("OGC WMTS", service.getName());
            assertEquals("WMTS BGDI", service.getTitle());

            String[] keywordList = service.getKeywordList();
            assertNotNull(keywordList);
            assertEquals("Switzerland", keywordList[0]);
            assertEquals("Web Map Service", keywordList[1]);

            WMTSRequest request = capabilities.getRequest();

            OperationType getTile = request.getGetTile();
            assertNotNull(getTile);

            assertEquals(306,capabilities.getLayerList().size());

            Layer[] layers = (Layer[]) capabilities.getLayerList()
                    .toArray(new Layer[capabilities.getLayerList().size()]);

            WMTSLayer l0 = (WMTSLayer)layers[0];

            assertEquals("ch.are.agglomerationen_isolierte_staedte", l0.getName());
            assertNull(l0.getParent());
            assertTrue(l0.getSrs().contains("urn:ogc:def:crs:EPSG::2056")); // case should not matter
            assertTrue(l0.getSrs().contains("EPSG:2056")); // case should not matter

            assertNotNull("Missing dimensions", l0.getDimensions());
            assertEquals("Bad dimensions size", 1, l0.getDimensions().size());
            String dimName = l0.getDimensions().keySet().iterator().next();
            assertTrue("Bad dimension name (Time!="+dimName+")", "Time".equalsIgnoreCase(dimName));

            assertNotNull(l0.getTileMatrixLinks());
            assertEquals(1,l0.getTileMatrixLinks().keySet().size());
            assertEquals("2056_26", l0.getTileMatrixLinks().keySet().iterator().next());
            assertEquals("2056_26", l0.getTileMatrixLinks().values().iterator().next().getIdentifier());
            assertEquals(0, l0.getTileMatrixLinks().values().iterator().next().getLimits().size());

            assertEquals(12, capabilities.getMatrixSets().size());
            assertEquals("2056_17", capabilities.getMatrixSets().get(0).getIdentifier());
            assertEquals(18, capabilities.getMatrixSets().get(0).getMatrices().size());
            assertEquals(14285750.5715, capabilities.getMatrixSets().get(0).getMatrices().get(0).getDenominator());

            CRSEnvelope bbox = (CRSEnvelope) layers[1].getBoundingBoxes().get("EPSG:4326");
            assertNotNull(bbox);


        } catch (Exception e) {
            e.printStackTrace();
            if ((e.getMessage() != null) && e.getMessage().indexOf("timed out") > 0) {
                System.err.println("Unable to test - timed out: " + e);
            } else {
                throw (e);
            }
        }
    }

    public void testParser3() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("nasa.getcapa.xml");
        try {
            assertEquals("1.0.0", capabilities.getVersion());

            WMTSService service = (WMTSService)capabilities.getService();
            assertEquals("OGC WMTS", service.getName());
            assertEquals("NASA Global Imagery Browse Services for EOSDIS", service.getTitle());

            String[] keywordList = service.getKeywordList();
            assertNotNull(keywordList);
            assertEquals("World", keywordList[0]);
            assertEquals("Global", keywordList[1]);

            WMTSRequest request = capabilities.getRequest();

            OperationType getTile = request.getGetTile();
            assertNotNull(getTile);

            assertEquals(519,capabilities.getLayerList().size());

            Layer[] layers = (Layer[]) capabilities.getLayerList()
                    .toArray(new Layer[capabilities.getLayerList().size()]);

            Layer l0 = layers[0];

            assertEquals("AMSR2_Snow_Water_Equivalent", l0.getName());
            assertNull(l0.getParent());
            
            //assertTrue(l0.getSrs().contains("urn:ogc:def:crs:OGC:2:84")); // case should not matter
            assertTrue(l0.getSrs().contains("CRS:84"));

            assertNotNull("Missing dimensions", l0.getDimensions());
            assertEquals("Bad dimensions size", 1, l0.getDimensions().size());
            String dimName = l0.getDimensions().keySet().iterator().next();
            assertTrue("Bad dimension name (Time!="+dimName+")", "Time".equalsIgnoreCase(dimName));

            CRSEnvelope bbox = (CRSEnvelope) layers[1].getBoundingBoxes().get("EPSG:4326");
            assertNotNull(bbox);


        } catch (Exception e) {
            e.printStackTrace();
            if ((e.getMessage() != null) && e.getMessage().indexOf("timed out") > 0) {
                System.err.println("Unable to test - timed out: " + e);
            } else {
                throw (e);
            }
        }
    }

    public void testCreateGetTileRequest() throws Exception {
        try {
            WebMapTileServer wmts = new WebMapTileServer(server);
            WMTSCapabilities caps = wmts.getCapabilities();
            GetTileRequest request = wmts.createGetTileRequest();
            assertNotNull(request);
//            request.setFormat("image/jpeg");
            //System.out.println(request.getFinalURL().toExternalForm());

//            assertTrue(request.getFinalURL().toExternalForm().indexOf("jpeg") >= 0);
        } catch (java.net.ConnectException ce) {
            if (ce.getMessage().indexOf("timed out") > 0) {
                System.err.println("Unable to test - timed out: " + ce);
            } else {
                throw (ce);
            }
        }
    }

    public void testCreateGetFeatureInfoRequest() throws Exception {
        /*
         * TODO FIX THIS try{ URL featureURL = new URL("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?VERSION=1.1.0&REQUEST=GetCapabilities");
         * WebMapServer wms = getCustomWMS(featureURL); WMSCapabilities caps = wms.getCapabilities(); assertNotNull(caps);
         * assertNotNull(caps.getRequest().getGetFeatureInfo());
         * 
         * GetMapRequest getMapRequest = wms.createGetMapRequest();
         * 
         * List layers = Arrays.asList(WMSUtils.getNamedLayers(caps)); List simpleLayers = new ArrayList(); Iterator iter = layers.iterator(); while
         * (iter.hasNext()) { Layer layer = (Layer) iter.next(); SimpleLayer sLayer = new SimpleLayer(layer.getName(), ""); simpleLayers.add(sLayer);
         * List styles = layer.getStyles(); if (styles.size() == 0) { sLayer.setStyle(""); continue; } Random random = new Random(); int randomInt =
         * random.nextInt(styles.size()); sLayer.setStyle((String) styles.get(randomInt)); } getMapRequest.setLayers(simpleLayers);
         * 
         * getMapRequest.setSRS("EPSG:4326"); getMapRequest.setDimensions("400", "400"); getMapRequest.setFormat("image/png");
         * 
         * getMapRequest.setBBox("-114.01268,59.4596930,-113.26043,60.0835794"); URL url2 = getMapRequest.getFinalURL();
         * 
         * GetFeatureInfoRequest request = wms.createGetFeatureInfoRequest(getMapRequest); request.setQueryLayers(WMSUtils.getQueryableLayers(caps));
         * request.setQueryPoint(200, 200); request.setInfoFormat(caps.getRequest().getGetFeatureInfo().getFormatStrings()[0]);
         * 
         * System.out.println(request.getFinalURL());
         * 
         * GetFeatureInfoResponse response = (GetFeatureInfoResponse) wms.issueRequest(request); System.out.println(response.getContentType());
         * assertTrue( response.getContentType().indexOf("text/plain") != -1 ); BufferedReader in = new BufferedReader(new
         * InputStreamReader(response.getInputStream())); String line;
         * 
         * boolean textFound = false; while ((line = in.readLine()) != null) { System.out.println(line); if
         * (line.indexOf("Wood Buffalo National Park") != -1) { textFound = true; } } assertTrue(textFound); } catch(java.net.ConnectException ce){
         * if(ce.getMessage().indexOf("timed out")>0){ System.err.println("Unable to test - timed out: "+ce); } else{ throw(ce); } }
         */
    }

    /**
     * @param featureURL
     * @throws IOException
     * @throws URISyntaxException
     * @throws SAXException
     */
    protected WebMapTileServer getCustomWMS(URL featureURL)
            throws SAXException, URISyntaxException, IOException {
        return new WebMapTileServer(featureURL);
    }

    protected WMTSCapabilities createCapabilities(String capFile) throws Exception {
        try {
            File getCaps = TestData.file(null, capFile);
            assertNotNull(getCaps);

            Parser parser = new Parser(new WMTSConfiguration());

            Object object = parser.parse(new FileReader(getCaps));
            assertTrue("Capabilities failed to parse " + object.getClass(), object instanceof CapabilitiesType);

            WMTSCapabilities capabilities = new WMTSCapabilities((CapabilitiesType) object);
            return capabilities;
        } catch (java.net.ConnectException ce) {
            if (ce.getMessage().indexOf("timed out") > 0) {
                System.err.println("Unable to test - timed out: " + ce);
                return null;
            } else {
                throw (ce);
            }
        }
    }

}
