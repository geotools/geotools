package org.geotools.ows.wms.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.geotools.geometry.DirectPosition2D;
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
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.internal.util.collections.Sets;
import org.opengis.referencing.FactoryException;

public class WMSGetFeatureInfoTest {

    protected static final Logger LOGGER = Logging.getLogger(WMS1_3_0_Test.class);

    @Captor ArgumentCaptor<DirectPosition2D> argCaptor;

    private WebMapServer serverWithMockedClient;

    public WMSGetFeatureInfoTest() {}
    //this tests checks the GEOS-9770 bug, WMS cascaded layer does the proper transformations to i, j params
    //it uses the same data of the issue
    @Before
    public void setUp() throws Exception {
        MockHttpClient client =
                new MockHttpClient() {

                    @Override
                    public HTTPResponse get(URL url) throws IOException {
                        if (url.getQuery().contains("GetCapabilities")) {
                            URL brunar130 =
                                    WMSCoverageReaderTest.class.getResource("brunar130.xml");
                            return new MockHttpResponse(brunar130, "text/xml");
                        } else {
                            throw new IllegalArgumentException(
                                    "Don't know how to handle a get request over "
                                            + url.toExternalForm());
                        }
                    }
                };
        serverWithMockedClient =
                new WebMapServer(new URL("http://geoserver.org/geoserver/wms"), client);
    }


    @Test
    public void testProperTransformationsToGetXYinImageCoordinates()
            throws FactoryException, ServiceException, IOException {
        MockitoAnnotations.initMocks(this);
        HashMap<String, CRSEnvelope> BBOXES = new HashMap<>();
        // given the bbox, and i, j params
        URL testURL =
                new URL(
                        "http://carto.icv.gva.es/arcgis/services/mapabase_topografico/cv100_mapabase/MapServer/WMSServer?service=wms&request=getcapabilities");

        CRSEnvelope generalEnvelope =
                new CRSEnvelope("EPSG:4326", -180, -90, 180, 90); // $NON-NLS-1$
        BBOXES.put("EPSG:4326", generalEnvelope);
        ReferencedEnvelope requestBBox =
                new ReferencedEnvelope(
                        7291710.72,
                        7294004.4799999995,
                        832252.7999999999,
                        834546.5599999999,
                        CRS.decode("EPSG:3006"));

        DirectPosition2D pointInImage = new DirectPosition2D(107, 2);
        Layer layer = new Layer("CV100Coberta_Terrestre");
        layer.setSrs(Sets.newSet("EPSG:3006"));
        layer.setBoundingBoxes(BBOXES);

        // mock WMSLayer reader object to verify later on the getFeatureInfo of WMSCoverage call
        // argument
        WMSCoverageReader mockReader = Mockito.mock(WMSCoverageReader.class);
        //a wmsLayer with a real WMSCoverageReader
        WMSLayer wmsCopy = new WMSLayerTest(new WebMapServer(testURL), layer, new WMSCoverageReader(serverWithMockedClient, getLayer(serverWithMockedClient, "GEOS9770")));
        //a wmsLayer with a mocked WMSCoverageReader
        WMSLayer wmsTest = new WMSLayerTest(new WebMapServer(testURL), layer, mockReader);

        // test trigger
        try {
            wmsCopy.getFeatureInfo(
                    requestBBox,
                    512,
                    512,
                    Double.valueOf(pointInImage.x).intValue(),
                    Double.valueOf(pointInImage.y).intValue(),
                    "application/vnd.ogc.gml",
                    1);
        } catch(IOException e) {
            //doNothing
        }
        when(mockReader.getRequestdEnvelopeCRS())
                .thenAnswer(inv -> wmsCopy.getReader().getRequestdEnvelopeCRS());

        when(mockReader.getMapRequest())
                .thenAnswer(inv -> wmsCopy.getLastGetMap());
        try {
            wmsTest.getFeatureInfo(
                    requestBBox,
                    512,
                    512,
                    Double.valueOf(pointInImage.x).intValue(),
                    Double.valueOf(pointInImage.y).intValue(),
                    "application/vnd.ogc.gml",
                    1);
        } catch(IOException e) {
            //doNothing
        }

        // assertion
        // this is a point expected for that getInfoRequest with the correct transformations
        DirectPosition2D pointInWorld = new DirectPosition2D(7293995.52, 832732.1599999999);
        verify(mockReader, times(1)).getFeatureInfo(argCaptor.capture(), any(), anyInt(), any());
        Assert.assertEquals(pointInWorld, argCaptor.getValue());
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
    class WMSLayerTest extends WMSLayer {
        public WMSLayerTest(WebMapServer wms, Layer layer, WMSCoverageReader reader) {
            super(wms, layer);
            this.reader = reader;
        }
    }
}
