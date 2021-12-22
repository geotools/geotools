/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.ows.wmts.online;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Set;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.WMTSSpecification;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.map.WMTSCoverageReader;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

public class LantmaterietWMTSServerOnlineTest extends WMTSOnlineTestCase {

    private URL server;

    private URL serverWithStyle;

    @Override
    public void setUpInternal() throws Exception {
        this.server =
                new URL(
                        "https://api.lantmateriet.se/open/topowebb-ccby/v1/wmts/token/8d61b10d-e93b-3c04-b4ae-4f4bdd1afe1b/?request=getcapabilities&service=wmts");
        this.serverWithStyle = new URL("https://www.basemap.at/wmts/1.0.0/WMTSCapabilities.xml");
    }

    @Test
    public void testGetTiles()
            throws IOException, ServiceException, FactoryException, TransformException {
        WebMapTileServer wmts = new WebMapTileServer(server);

        WMTSCapabilities capabilities = wmts.getCapabilities();

        CoordinateReferenceSystem crs = CRS.decode("EPSG:3857");
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(
                        181896.32913603852,
                        1086312.9422175875,
                        6091282.433471196,
                        7689478.305598114,
                        crs);

        // envelope to request tiles for 5th zoom level
        ReferencedEnvelope re1 =
                new ReferencedEnvelope(
                        -9772.986997677013,
                        3743054.020110313,
                        6674950.119413431,
                        1.042777712652142E7,
                        crs);
        Set<Tile> responses =
                issueTileRequest(wmts, capabilities, envelope, re1, crs, "topowebb_nedtonad");
        assertFalse(responses.isEmpty());
        for (Tile response : responses) {
            // checking the identifier
            assertTrue(response.getId().startsWith("wmts_5"));
            re1.contains(response.getExtent().toBounds(re1.getCoordinateReferenceSystem()));
        }
        // envelope to request tiles for 7th zoom level
        ReferencedEnvelope re2 =
                new ReferencedEnvelope(
                        381146.49290940526,
                        1441515.582157366,
                        7403037.65074037,
                        9279451.154294366,
                        crs);
        Set<Tile> responses2 =
                issueTileRequest(wmts, capabilities, envelope, re2, crs, "topowebb_nedtonad");
        for (Tile response : responses2) {
            // checking the identifier
            assertTrue(response.getId().startsWith("wmts_7"));
            re2.contains(response.getExtent().toBounds(re2.getCoordinateReferenceSystem()));
        }
    }

    @Test
    public void testWMTSCoverageReader()
            throws IOException, ServiceException, FactoryException, TransformException {
        WebMapTileServer wmts = new WebMapTileServer(server);
        WMTSCapabilities capabilities = wmts.getCapabilities();
        CoordinateReferenceSystem crs = CRS.decode("EPSG:3857");
        // layer bboxes
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(
                        181896.32913603852,
                        1086312.9422175875,
                        6091282.433471196,
                        7689478.305598114,
                        crs);

        // first envelope 5th zoom level
        ReferencedEnvelope re1 =
                new ReferencedEnvelope(
                        -9772.986997677013,
                        3743054.020110313,
                        6674950.119413431,
                        1.042777712652142E7,
                        crs);
        RenderedImage ri1 =
                getRenderImageResult(wmts, capabilities, envelope, re1, "topowebb_nedtonad");
        File img1 = new File(getClass().getResource("wmtsTestResultZoom5.png").getFile());
        ImageAssert.assertEquals(img1, ri1, 50);
        // second envelope 7th zoomLevel
        ReferencedEnvelope re2 =
                new ReferencedEnvelope(
                        381146.49290940526,
                        1441515.582157366,
                        7403037.65074037,
                        9279451.154294366,
                        crs);
        RenderedImage ri2 =
                getRenderImageResult(wmts, capabilities, envelope, re2, "topowebb_nedtonad");
        File img2 = new File(getClass().getResource("wmtsTestResultZoom7.png").getFile());
        ImageAssert.assertEquals(img2, ri2, 100);
    }

    @Test
    public void testGetTilesWithStylePlaceHolder()
            throws IOException, ServiceException, FactoryException, TransformException {
        WebMapTileServer wmts = new WebMapTileServer(serverWithStyle);

        WMTSCapabilities capabilities = wmts.getCapabilities();

        CoordinateReferenceSystem crs = CRS.decode("EPSG:3857");

        // envelope to request tiles for 5th zoom level
        ReferencedEnvelope re1 = new ReferencedEnvelope(977650, 1913530, 5838030, 6281290, crs);
        Set<Tile> responses = issueTileRequest(wmts, capabilities, re1, re1, crs, "bmapgelaende");
        assertFalse(responses.isEmpty());
        for (Tile response : responses) {
            // checking the identifier
            assertTrue(response.getId().startsWith("wmts_7"));
            re1.contains(response.getExtent().toBounds(re1.getCoordinateReferenceSystem()));
        }
    }

    @Test
    public void testWMTSCoverageReaderWithStylePlaceholder()
            throws IOException, ServiceException, FactoryException, TransformException {
        WebMapTileServer wmts = new WebMapTileServer(serverWithStyle);

        WMTSCapabilities capabilities = wmts.getCapabilities();

        CoordinateReferenceSystem crs = CRS.decode("EPSG:3857");

        // envelope to request tiles for 5th zoom level
        ReferencedEnvelope re1 = new ReferencedEnvelope(977650, 1913530, 5838030, 6281290, crs);
        RenderedImage ri = getRenderImageResult(wmts, capabilities, re1, re1, "geolandbasemap");
        File img = new File(getClass().getResource("stylePlaceHolderResult.png").getFile());
        ImageAssert.assertEquals(img, ri, 100);
    }

    @Test
    public void testReproject()
            throws IOException, ServiceException, FactoryException, URISyntaxException {
        WebMapTileServer wmts = new WebMapTileServer(serverWithStyle);
        WMTSCapabilities capabilities = wmts.getCapabilities();
        CoordinateReferenceSystem crs = CRS.decode("EPSG:2056");
        // layer bboxes
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(
                        2698313.7525878195,
                        3348542.311391488,
                        1135039.4713045221,
                        1476372.4030413276,
                        crs);

        ReferencedEnvelope requested1 =
                new ReferencedEnvelope(
                        2788966.1644620905,
                        3258069.540350589,
                        1182531.426718924,
                        1429299.3484102695,
                        crs);
        RenderedImage ri =
                getRenderImageResult(wmts, capabilities, envelope, requested1, "bmaporthofoto30cm");
        URL url = getClass().getResource("wmts-rep1.png");
        ImageAssert.assertEquals(new File(url.toURI()), ri, 100);

        ReferencedEnvelope requested2 =
                new ReferencedEnvelope(
                        3025961.099155759,
                        3260512.787100008,
                        1210934.1701809228,
                        1334318.1310265958,
                        crs);
        RenderedImage ri2 =
                getRenderImageResult(wmts, capabilities, envelope, requested2, "bmaporthofoto30cm");
        url = getClass().getResource("wmts-rep2.png");
        ImageAssert.assertEquals(new File(url.toURI()), ri2, 100);
    }

    @Test
    public void testGetTilesReproject()
            throws IOException, ServiceException, FactoryException, TransformException {
        WebMapTileServer wmts = new WebMapTileServer(serverWithStyle);

        WMTSCapabilities capabilities = wmts.getCapabilities();

        CoordinateReferenceSystem crs = CRS.decode("EPSG:2056");
        // layer bboxes
        ReferencedEnvelope envelope =
                new ReferencedEnvelope(
                        2698313.7525878195,
                        3348542.311391488,
                        1135039.4713045221,
                        1476372.4030413276,
                        crs);

        ReferencedEnvelope requested1 =
                new ReferencedEnvelope(
                        2788966.1644620905,
                        3258069.540350589,
                        1182531.426718924,
                        1429299.3484102695,
                        crs);
        Set<Tile> responses =
                issueTileRequest(
                        wmts, capabilities, envelope, requested1, crs, "bmaporthofoto30cm");
        assertFalse(responses.isEmpty());
        for (Tile response : responses) {
            // checking the identifier
            assertTrue(response.getId().startsWith("wmts_8"));
            ReferencedEnvelope nativeEnv = response.getExtent();
            assertEquals("EPSG:3857", CRS.toSRS(nativeEnv.getCoordinateReferenceSystem()));
            ReferencedEnvelope env =
                    response.getExtent().transform(requested1.getCoordinateReferenceSystem(), true);
            requested1.contains(env.toBounds(requested1.getCoordinateReferenceSystem()));
        }
    }

    @Test
    public void testRenderImageWithDifferentCRSDefinition()
            throws IOException, ServiceException, FactoryException, URISyntaxException {
        // tests that the server supported srs is selected when requesting tiles with
        // same CRS but different srs definition eg. using EPSG instead of urn:ogc:def:crs:EPSG::
        try {
            System.setProperty("org.geotools.referencing.forceXY", "true");
            CRS.reset("all");
            WebMapTileServer wmts = new WebMapTileServer(server);

            WMTSCapabilities capabilities = wmts.getCapabilities();
            // server supports urn:ogc:def:crs:EPSG::3006
            CoordinateReferenceSystem crs = CRS.decode("EPSG:3006");
            // layer bboxes
            ReferencedEnvelope envelope =
                    new ReferencedEnvelope(
                            1200000.0000000005,
                            2994304.0000000005,
                            4305696.000000001,
                            8500000.000000002,
                            crs);

            ReferencedEnvelope requested =
                    new ReferencedEnvelope(
                            662730.6807799754,
                            1131834.056668474,
                            6168587.230596287,
                            6637690.606484786,
                            crs);
            WMTSCoverageReader reader =
                    wmtsCoverageReader(wmts, capabilities, envelope, "topowebb_nedtonad");
            RenderedImage ri = getRenderImageResult(reader, requested);
            WMTSSpecification.GetMultiTileRequest request =
                    (WMTSSpecification.GetMultiTileRequest) reader.getTileRequest();
            String requestSRS = CRS.toSRS(request.getCrs());
            assertEquals("urn:ogc:def:crs:EPSG::3006", requestSRS);
            WMTSLayer layer = capabilities.getLayer("topowebb_nedtonad");
            assertTrue(layer.getSrs().contains(requestSRS));
            URL url = getClass().getResource("different_srs_def.png");
            ImageAssert.assertEquals(new File(url.toURI()), ri, 100);
        } finally {
            System.clearProperty("org.geotools.referencing.forceXY");
        }
    }

    public void cleanUp() {
        System.clearProperty("org.geotools.referencing.forceXY");
    }
}
