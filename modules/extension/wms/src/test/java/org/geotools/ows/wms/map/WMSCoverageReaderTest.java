package org.geotools.ows.wms.map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import org.apache.hc.core5.net.URLEncodedUtils;
import org.apache.hc.core5.http.NameValuePair;
import org.geotools.api.parameter.GeneralParameterValue;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.Hints;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class WMSCoverageReaderTest {

    public static final String STRING_VP = "STRING";
    public static final String NUMBER_VP = "NUMBER";
    public static final String LIST_VP = "LIST";
    public static final String NUMBER_LIST_VP = "NUMBER_LIST";
    public static final String CSV_VP = "CSV";
    public static final String BOOLEAN_VP = "BOOLEAN";
    public static final String ESCAPED_CHARACTERS = "ESCAPED_CHARACTERS";

    Map<String, String> parseParams(String query) {

        List<NameValuePair> params = URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
        Map<String, String> result = new HashMap<>();
        for (Object param : params) {
            NameValuePair pair = (NameValuePair) param;
            result.put(pair.getName().toUpperCase(), pair.getValue());
        }
        return result;
    }

    @Before
    public void setup() {
        System.setProperty("org.geotools.referencing.forceXY", "true");
        Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "http");
        CRS.reset("all");
    }

    @AfterClass
    public static void teardown() {
        System.clearProperty("org.geotools.referencing.forceXY");
        Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "");
        CRS.reset("all");
    }

    @Test
    public void testWithVendorParameters() throws Exception {
        Map<String, String> vendorParameters = Map.of(
                STRING_VP, "5,10,20,5,1,3,1,,,,1",
                NUMBER_VP, "-95.0",
                NUMBER_LIST_VP, "-90.0,-180.0,90.0,180.0",
                LIST_VP, "LIST_1,LIST_2,LIST_3,LIST_4,LIST_5,LIST_6",
                CSV_VP, "5,10,20,5,1,3,1,,,,1",
                BOOLEAN_VP, "TRUE",
                ESCAPED_CHARACTERS, "POPULATION > 100000");

        // prepare the responses
        MockHttpClient client = new MockHttpClient() {

            @Override
            public HTTPResponse get(URL url) throws IOException {
                if (url.getQuery().contains("GetCapabilities")) {
                    URL caps130 = WMSCoverageReaderTest.class.getResource("caps130.xml");
                    return new MockHttpResponse(caps130, "text/xml");
                } else if (url.getQuery().contains("GetMap") && url.getQuery().contains("world4326")) {
                    Map<String, String> params = parseParams(url.getQuery());

                    // Standard checks for parameters to see if any are lost
                    assertEquals("1.3.0", params.get("VERSION"));
                    assertEquals("-90.0,-180.0,90.0,180.0", params.get("BBOX"));
                    assertEquals("EPSG:4326", params.get("CRS"));

                    // Checks to see if vendor parameters are passed
                    assertEquals(vendorParameters.get(STRING_VP), params.get(STRING_VP));
                    assertEquals(vendorParameters.get(NUMBER_VP), params.get(NUMBER_VP));
                    assertEquals(vendorParameters.get(NUMBER_LIST_VP), params.get(NUMBER_LIST_VP));
                    assertEquals(vendorParameters.get(LIST_VP), params.get(LIST_VP));
                    assertEquals(vendorParameters.get(CSV_VP), params.get(CSV_VP));
                    assertEquals(vendorParameters.get(BOOLEAN_VP), params.get(BOOLEAN_VP));
                    assertEquals(vendorParameters.get(ESCAPED_CHARACTERS), params.get(ESCAPED_CHARACTERS));

                    URL world = WMSCoverageReaderTest.class.getResource("world.png");
                    return new MockHttpResponse(world, "image/png");
                } else {
                    throw new IllegalArgumentException(
                            "Don't know how to handle a get request over " + url.toExternalForm());
                }
            }
        };
        WebMapServer server = new WebMapServer(new URL("http://geoserver.org/geoserver/wms"), client);
        Layer layer = getLayer(server, "world4326");

        // build a getmap request and check it
        CoordinateReferenceSystem wgs84 = CRS.decode("urn:ogc:def:crs:EPSG::4326", true);
        ReferencedEnvelope worldEnvelope = new ReferencedEnvelope(-90, 90, -180, 180, wgs84);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 180, 90), worldEnvelope);
        final Parameter<GridGeometry2D> ggParam =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParam.setValue(gg);

        layer.setVendorParameters(vendorParameters);

        WMSCoverageReader reader = new WMSCoverageReader(server, layer);
        reader.read(new GeneralParameterValue[] {ggParam});
    }

    @Test
    public void test4326wms13() throws Exception {
        WMSCoverageReader reader = getReader4326wms13();

        // build a getmap request and check it
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope worldEnvelope = new ReferencedEnvelope(-180, 180, -90, 90, wgs84);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 180, 90), worldEnvelope);
        final Parameter<GridGeometry2D> ggParam =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParam.setValue(gg);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggParam});
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
        final Parameter<GridGeometry2D> ggParam =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParam.setValue(gg);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggParam});
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
        final Parameter<GridGeometry2D> ggParam =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParam.setValue(gg);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggParam});
        assertTrue(CRS.equalsIgnoreMetadata(wgs84, coverage.getCoordinateReferenceSystem()));
        assertEquals(worldEnvelope, new ReferencedEnvelope(coverage.getEnvelope()));
    }

    private WMSCoverageReader getReader4326wms13() throws IOException, ServiceException, MalformedURLException {
        // prepare the responses
        MockHttpClient client = new MockHttpClient() {

            @Override
            public HTTPResponse get(URL url) throws IOException {
                if (url.getQuery().contains("GetCapabilities")) {
                    URL caps130 = WMSCoverageReaderTest.class.getResource("caps130.xml");
                    return new MockHttpResponse(caps130, "text/xml");
                } else if (url.getQuery().contains("GetMap") && url.getQuery().contains("world4326")) {
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
        WebMapServer server = new WebMapServer(new URL("http://geoserver.org/geoserver/wms"), client);
        WMSCoverageReader reader = new WMSCoverageReader(server, getLayer(server, "world4326"));
        return reader;
    }

    @Test
    public void testGetMapNoContentType() throws Exception {
        // reset the CRS system to its defaults
        System.clearProperty("org.geotools.referencing.forceXY");
        Hints.putSystemDefault(Hints.FORCE_AXIS_ORDER_HONORING, "");
        CRS.reset("all");

        // prepare the responses
        final AtomicBoolean disposeCalled = new AtomicBoolean(false);
        MockHttpClient client = new MockHttpClient() {

            @Override
            public HTTPResponse get(URL url) throws IOException {
                if (url.getQuery().contains("GetCapabilities")) {
                    URL caps130 = WMSCoverageReaderTest.class.getResource("caps130.xml");
                    return new MockHttpResponse(caps130, "text/xml");
                } else if (url.getQuery().contains("GetMap") && url.getQuery().contains("world4326")) {
                    Map<String, String> params = parseParams(url.getQuery());
                    assertEquals("1.3.0", params.get("VERSION"));
                    assertEquals("-90.0,-180.0,90.0,180.0", params.get("BBOX"));
                    assertEquals("EPSG:4326", params.get("CRS"));
                    URL world = WMSCoverageReaderTest.class.getResource("world.png");
                    return new MockHttpResponse(world, null) {
                        @Override
                        public void dispose() {
                            disposeCalled.set(true);
                            super.dispose();
                        }
                    };
                } else {
                    throw new IllegalArgumentException(
                            "Don't know how to handle a get request over " + url.toExternalForm());
                }
            }
        };
        // setup the reader
        WebMapServer server = new WebMapServer(new URL("http://geoserver.org/geoserver/wms"), client);
        WMSCoverageReader reader = new WMSCoverageReader(server, getLayer(server, "world4326"));

        // build a getmap request and check it
        CoordinateReferenceSystem wgs84 = CRS.decode("urn:ogc:def:crs:EPSG::4326", true);
        ReferencedEnvelope worldEnvelope = new ReferencedEnvelope(-90, 90, -180, 180, wgs84);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 180, 90), worldEnvelope);
        final Parameter<GridGeometry2D> ggParam =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParam.setValue(gg);
        try {
            reader.read(new GeneralParameterValue[] {ggParam});
            fail("Should have thrown an exception, the GetMap content type was null");
        } catch (Exception e) {
            // it's fine
        }

        assertTrue(disposeCalled.get());
    }

    @Test
    public void test4326wms11() throws Exception {
        WMSCoverageReader reader = getReader4326wms11();
        GeneralBounds original = reader.getOriginalEnvelope();
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        assertTrue(CRS.equalsIgnoreMetadata(wgs84, original.getCoordinateReferenceSystem()));

        // build a getmap request and check it
        ReferencedEnvelope worldEnvelope = new ReferencedEnvelope(-180, 180, -90, 90, wgs84);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 180, 90), worldEnvelope);
        final Parameter<GridGeometry2D> ggParam =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParam.setValue(gg);
        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggParam});
        assertTrue(CRS.equalsIgnoreMetadata(wgs84, coverage.getCoordinateReferenceSystem()));
        assertEquals(worldEnvelope, new ReferencedEnvelope(coverage.getEnvelope()));
    }

    private WMSCoverageReader getReader4326wms11() throws IOException, ServiceException, MalformedURLException {
        // prepare the responses
        MockHttpClient client = new MockHttpClient() {

            @Override
            public HTTPResponse get(URL url) throws IOException {
                if (url.getQuery().contains("GetCapabilities")) {
                    URL caps130 = WMSCoverageReaderTest.class.getResource("caps110.xml");
                    return new MockHttpResponse(caps130, "text/xml");
                } else if (url.getQuery().contains("GetMap") && url.getQuery().contains("world4326")) {
                    Map<String, String> params = parseParams(url.getQuery());
                    assertEquals("1.1.0", params.get("VERSION"));
                    assertEquals("-180.0,-90.0,180.0,90.0", params.get("BBOX"));
                    assertEquals("EPSG:4326", params.get("SRS"));
                    URL world = WMSCoverageReaderTest.class.getResource("world.png");
                    return new MockHttpResponse(world, "image/png");
                } else {
                    throw new IllegalArgumentException(
                            "Don't know how to handle a get request over " + url.toExternalForm());
                }
            }
        };
        // setup the reader
        WebMapServer server = new WebMapServer(new URL("http://geoserver.org/geoserver/wms"), client);
        WMSCoverageReader reader = new WMSCoverageReader(server, getLayer(server, "world4326"));
        return reader;
    }

    @Test
    public void testCrs84wms13() throws Exception {
        // prepare the responses
        MockHttpClient client = new MockHttpClient() {

            @Override
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
        WebMapServer server = new WebMapServer(new URL("http://geoserver.org/geoserver/wms"), client);
        WMSCoverageReader reader = new WMSCoverageReader(server, getLayer(server, "world84"));

        // setup the request and check it
        CoordinateReferenceSystem wgs84 = CRS.decode("EPSG:4326", true);
        ReferencedEnvelope worldEnvelope = new ReferencedEnvelope(-180, 180, -90, 90, wgs84);
        GridGeometry2D gg = new GridGeometry2D(new GridEnvelope2D(0, 0, 180, 90), worldEnvelope);
        final Parameter<GridGeometry2D> ggParam =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        ggParam.setValue(gg);

        GridCoverage2D coverage = reader.read(new GeneralParameterValue[] {ggParam});
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
