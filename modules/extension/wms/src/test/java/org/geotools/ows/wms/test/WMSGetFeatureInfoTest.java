package org.geotools.ows.wms.test;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.geotools.api.referencing.FactoryException;
import org.geotools.geometry.Position2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.map.WMSCoverageReader;
import org.geotools.ows.wms.map.WMSCoverageReaderTest;
import org.geotools.ows.wms.map.WMSLayer;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.internal.util.collections.Sets;

public class WMSGetFeatureInfoTest {

    protected static final Logger LOGGER = Logging.getLogger(WMS1_3_0_Test.class);

    public WMSGetFeatureInfoTest() {}
    // this tests checks the GEOS-9770 bug, WMS cascaded layer does the proper transformations to i,
    // j params
    // it uses the same data of the issue

    @Test
    public void testProperTransformationsToGetXYinImageCoordinates()
            throws FactoryException, ServiceException, IOException {
        URL brunar130 = WMSCoverageReaderTest.class.getResource("brunar130.xml");
        MockHttpClient client = new MockHttpClient() {

            @Override
            public HTTPResponse get(URL url) throws IOException {
                if (url.getQuery().contains("GetCapabilities")) {
                    return new MockHttpResponse(brunar130, "text/xml");
                } else if (url.getQuery().contains("GetFeatureInfo")) {
                    Map<String, String> params = parseParams(url.getQuery());

                    // Expected position request
                    if (!Double.valueOf(params.get("I")).equals(Double.valueOf(107))
                            || !Double.valueOf(params.get("J")).equals(Double.valueOf(2))) {
                        // this will cause the test to fail
                        throw new IllegalArgumentException();
                    } else {
                        return new MockHttpResponse(brunar130, "application/vnd.ogc.gml");
                    }
                }
                throw new IllegalArgumentException(
                        "Don't know how to handle a get request over " + url.toExternalForm());
            }
        };
        WebMapServer serverWithMockedClient = new WebMapServer(new URL("http://geoserver.org/geoserver/wms"), client);
        HashMap<String, CRSEnvelope> BBOXES = new HashMap<>();
        // given the bbox, and i, j params

        CRSEnvelope generalEnvelope = new CRSEnvelope("EPSG:4326", -180, -90, 180, 90); // $NON-NLS-1$
        BBOXES.put("EPSG:4326", generalEnvelope);
        ReferencedEnvelope requestBBox = new ReferencedEnvelope(
                7291710.72, 7294004.4799999995, 832252.7999999999, 834546.5599999999, CRS.decode("EPSG:3006"));

        Position2D pointInImage = new Position2D(107, 2);
        Layer layer = new Layer("CV100Coberta_Terrestre");
        layer.setSrs(Sets.newSet("EPSG:3006"));
        layer.setBoundingBoxes(BBOXES);
        // a random layer to perform the getFeatureInfoRequest
        WMSLayer wmsCopy = new WMSLayerTest(
                new WebMapServer(brunar130),
                layer,
                new WMSCoverageReader(serverWithMockedClient, getLayer(serverWithMockedClient, "GEOS9770")));

        // test trigger
        try {
            wmsCopy.getFeatureInfo(
                    requestBBox, 512, 512, (int) pointInImage.x, (int) pointInImage.y, "application/vnd.ogc.gml", 1);
        } catch (Exception e) {
            Assert.fail("getFeatureInfo request with wrong I,J reprojected params ");
        }
    }

    private Layer getLayer(WebMapServer server, String layerName) {
        for (Layer layer : server.getCapabilities().getLayerList()) {
            if (layerName.equals(layer.getName())) {
                return layer;
            }
        }
        throw new IllegalArgumentException("Could not find layer " + layerName);
    }

    /*
       Test Class Extending WMLayer that allows to create a WMSLayer Object passing a reader
    */
    static class WMSLayerTest extends WMSLayer {
        public WMSLayerTest(WebMapServer wms, Layer layer, WMSCoverageReader reader) {
            super(wms, layer);
            this.reader = reader;
        }
    }

    Map<String, String> parseParams(String query) {

        List<NameValuePair> params = URLEncodedUtils.parse(query, StandardCharsets.UTF_8);
        Map<String, String> result = new HashMap<>();
        for (Object param : params) {
            NameValuePair pair = (NameValuePair) param;
            result.put(pair.getName().toUpperCase(), pair.getValue());
        }
        return result;
    }
}
