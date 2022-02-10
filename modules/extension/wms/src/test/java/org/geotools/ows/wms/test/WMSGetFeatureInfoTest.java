package org.geotools.ows.wms.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.logging.Logger;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.map.WMSCoverageReader;
import org.geotools.ows.wms.map.WMSLayer;
import org.geotools.referencing.CRS;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
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

    public WMSGetFeatureInfoTest() {}
    //this tests checks the GEOS-9770 bug, WMS cascaded layer does the proper transformations to i, j params
    //it uses the same data of the issue
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
        WMSLayer wmsTest = new WMSLayer(new WebMapServer(testURL), layer);
        WMSLayer wmsCopy = new WMSLayer(new WebMapServer(testURL), layer);

        // mock WMSLayer reader object to verify later on the getFeatureInfo of WMSCoverage call
        // argument
        WMSCoverageReader mockReader = Mockito.mock(WMSCoverageReader.class);
        when(mockReader.initMapRequest(any(), anyInt(), anyInt(), any()))
                .thenAnswer(
                        invocation ->
                                wmsCopy.getReader()
                                        .initMapRequest(
                                                requestBBox,
                                                256,
                                                256,
                                                invocation.getArgument(3, Color.class)));
        when(mockReader.getRequestdEnvelopeCRS())
                .thenAnswer(inv -> wmsCopy.getReader().getRequestdEnvelopeCRS());
        wmsTest.setReader(mockReader);

        // test trigger
        wmsTest.getFeatureInfo(
                requestBBox,
                512,
                512,
                Double.valueOf(pointInImage.x).intValue(),
                Double.valueOf(pointInImage.y).intValue(),
                "application/vnd.ogc.gml",
                1);

        // assertion
        // this is a point expected for that getInfoRequest with the correct transformations
        DirectPosition2D pointInWorld = new DirectPosition2D(7293995.52, 832732.1599999999);
        verify(mockReader, times(1)).getFeatureInfo(argCaptor.capture(), any(), anyInt(), any());
        Assert.assertEquals(pointInWorld, argCaptor.getValue());
    }
}
