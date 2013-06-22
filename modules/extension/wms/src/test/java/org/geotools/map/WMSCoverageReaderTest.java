package org.geotools.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.util.ParameterParser;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.MockHttpClient;
import org.geotools.data.ows.MockHttpResponse;
import org.geotools.data.wms.WebMapServer;
import org.geotools.factory.Hints;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class WMSCoverageReaderTest {

    Map<String, String> parseParams(String query) {
        ParameterParser pp = new ParameterParser();
        List params = pp.parse(query, '&');
        Map<String, String> result = new HashMap<String, String>();
        for (Iterator it = params.iterator(); it.hasNext();) {
            NameValuePair pair = (NameValuePair) it.next();
            result.put(pair.getName().toUpperCase(), pair.getValue());
        }
        return result;
    };
    
    @Before
    public void setup() {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "http");
        CRS.reset("all");
    }
    
    @After
    public void teardown() {
        System.clearProperty("org.geotools.referencing.forceXY");
        Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "");
        CRS.reset("all");
    }

    @Test
    public void test4326wms13() throws Exception {
        WMSCoverageReader reader = getReader4326wms13();

        // build a getmap request and check it
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope worldEnvelope = new ReferencedEnvelope(-180, 180, -90, 90, wgs84);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 180, 90), worldEnvelope);
        final Parameter<GridGeometry2D> ggParam = (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D
                .createValue();
        ggParam.setValue(gg);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] { ggParam });
        assertTrue(CRS.equalsIgnoreMetadata(wgs84, coverage.getCoordinateReferenceSystem()));
        assertEquals(worldEnvelope, new ReferencedEnvelope(coverage.getEnvelope()));
    }
    
    @Test
    public void test4326wms13RequestFlipped() throws Exception {
        WMSCoverageReader reader = getReader4326wms13();

        // build a getmap request and check it
        CoordinateReferenceSystem wgs84 = CRS.decode("urn:ogc:def:crs:EPSG::4326", true);
        ReferencedEnvelope worldEnvelope = new ReferencedEnvelope(-90, 90, -180, 180, wgs84);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 180, 90), worldEnvelope);
        final Parameter<GridGeometry2D> ggParam = (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D
                .createValue();
        ggParam.setValue(gg);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] { ggParam });
        assertTrue(CRS.equalsIgnoreMetadata(wgs84, coverage.getCoordinateReferenceSystem()));
        assertEquals(worldEnvelope, new ReferencedEnvelope(coverage.getEnvelope()));
    }
    
    @Test
    public void test4326wms13RequestFlippedStandardEPSG() throws Exception {
        // reset the CRS system to its defaults
        System.clearProperty("org.geotools.referencing.forceXY");
        Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "");
        CRS.reset("all");
        
        WMSCoverageReader reader = getReader4326wms13();

        // build a getmap request and check it
        CoordinateReferenceSystem wgs84 = CRS.decode("urn:ogc:def:crs:EPSG::4326", true);
        ReferencedEnvelope worldEnvelope = new ReferencedEnvelope(-90, 90, -180, 180, wgs84);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 180, 90), worldEnvelope);
        final Parameter<GridGeometry2D> ggParam = (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D
                .createValue();
        ggParam.setValue(gg);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] { ggParam });
        assertTrue(CRS.equalsIgnoreMetadata(wgs84, coverage.getCoordinateReferenceSystem()));
        assertEquals(worldEnvelope, new ReferencedEnvelope(coverage.getEnvelope()));
    }

    private WMSCoverageReader getReader4326wms13() throws IOException, ServiceException,
            MalformedURLException {
        // prepare the responses
        MockHttpClient client = new MockHttpClient() {

            public HTTPResponse get(URL url) throws IOException {
                if (url.getQuery().contains("GetCapabilities")) {
                    URL caps130 = WMSCoverageReaderTest.class.getResource("caps130.xml");
                    return new MockHttpResponse(caps130, "text/xml");
                } else if (url.getQuery().contains("GetMap")
                        && url.getQuery().contains("world4326")) {
                    Map<String, String> params = parseParams(url.getQuery());
                    assertEquals("1.3.0", params.get("VERSION"));
                    assertEquals("-90.0,-180.0,90.0,180.0", params.get("BBOX"));
                    assertEquals("EPSG:4326", params.get("CRS"));
                    URL world = WMSCoverageReaderTest.class.getResource("world.png");
                    return new MockHttpResponse(world, "image/png");
                } else {
                    throw new IllegalArgumentException(
                            "Don't know how to handle a get request over " + url.toExternalForm());
                }
            }

        };
        // setup the reader
        WebMapServer server = new WebMapServer(new URL("http://geoserver.org/geoserver/wms"),
                client);
        WMSCoverageReader reader = new WMSCoverageReader(server, getLayer(server, "world4326"));
        return reader;
    }

    @Test
    public void testCrs84wms13() throws Exception {
        // prepare the responses
        MockHttpClient client = new MockHttpClient() {

            public HTTPResponse get(URL url) throws IOException {
                if (url.getQuery().contains("GetCapabilities")) {
                    URL caps130 = WMSCoverageReaderTest.class.getResource("caps130_crs84.xml");
                    return new MockHttpResponse(caps130, "text/xml");
                } else if (url.getQuery().contains("GetMap") && url.getQuery().contains("world84")) {
                    Map<String, String> params = parseParams(url.getQuery());
                    assertEquals("1.3.0", params.get("VERSION"));
                    assertEquals("CRS:84", params.get("CRS"));
                    assertEquals("-180.0,-90.0,180.0,90.0", params.get("BBOX"));
                    URL world = WMSCoverageReaderTest.class.getResource("world.png");
                    return new MockHttpResponse(world, "image/png");
                } else {
                    throw new IllegalArgumentException(
                            "Don't know how to handle a get request over " + url.toExternalForm());
                }
            }

        };
        WebMapServer server = new WebMapServer(new URL("http://geoserver.org/geoserver/wms"),
                client);
        WMSCoverageReader reader = new WMSCoverageReader(server, getLayer(server, "world84"));

        // setup the request and check it
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope worldEnvelope = new ReferencedEnvelope(-180, 180, -90, 90, wgs84);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 180, 90), worldEnvelope);
        final Parameter<GridGeometry2D> ggParam = (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D
                .createValue();
        ggParam.setValue(gg);

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] { ggParam });
        assertTrue(CRS.equalsIgnoreMetadata(wgs84, coverage.getCoordinateReferenceSystem()));
        assertEquals(worldEnvelope, new ReferencedEnvelope(coverage.getEnvelope()));
    }

    private Layer getLayer(WebMapServer server, String layerName) {
        for (Layer layer : server.getCapabilities().getLayerList()) {
            if (layerName.equals(layer.getName())) {
                return layer;
            }
        }
        throw new IllegalArgumentException("Could not find layer " + layerName);
    }
}
