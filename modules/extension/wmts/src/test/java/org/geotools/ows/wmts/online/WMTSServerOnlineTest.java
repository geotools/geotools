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

import static org.junit.Assert.*;

import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.media.jai.Interpolation;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.image.test.ImageAssert;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.map.WMTSCoverageReader;
import org.geotools.ows.wmts.map.WMTSMapLayer;
import org.geotools.ows.wmts.model.*;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.junit.Before;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.TransformException;

public class WMTSServerOnlineTest {

    private URL server;

    private URL serverWithStyle;

    @Before
    public void setUpInternal() throws MalformedURLException {
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
        RenderedImage extent = getRenderImageResult(wmts, capabilities, re1, re1, "geolandbasemap");
        File img = new File(getClass().getResource("stylePlaceHolderResult.png").getFile());
        ImageAssert.assertEquals(img, extent, 100);
    }

    private Set<Tile> issueTileRequest(
            WebMapTileServer wmts,
            WMTSCapabilities capabilities,
            ReferencedEnvelope bboxes,
            ReferencedEnvelope requested,
            CoordinateReferenceSystem crs,
            String layerName)
            throws ServiceException {
        WMTSLayer layer = capabilities.getLayer(layerName);
        assertNotNull(layer);
        GetTileRequest request = wmts.createGetTileRequest();
        layer.setBoundingBoxes(new CRSEnvelope(bboxes));
        layer.setSrs(layer.getBoundingBoxes().keySet());
        request.setLayer(layer);

        request.setRequestedWidth(800);
        request.setRequestedHeight(400);
        request.setCRS(crs);
        request.setRequestedBBox(requested);

        return wmts.issueRequest(request);
    }

    private RenderedImage getRenderImageResult(
            WebMapTileServer wmts,
            WMTSCapabilities capabilities,
            ReferencedEnvelope bboxes,
            ReferencedEnvelope requested,
            String layerName)
            throws IOException {

        WMTSLayer layer = capabilities.getLayer(layerName);
        assertNotNull(layer);
        layer.setBoundingBoxes(new CRSEnvelope(bboxes));
        Set<String> srs = new HashSet<>();
        for (String s : layer.getBoundingBoxes().keySet()) {
            srs.add(layer.getBoundingBoxes().get(s).getEPSGCode());
        }
        layer.setSrs(srs);
        WMTSMapLayer mapLayer = new WMTSMapLayer(wmts, layer);
        WMTSCoverageReader wmtsReader = mapLayer.getReader();
        Rectangle rectangle = new Rectangle(0, 0, 768, 589);
        GridGeometry2D grid = new GridGeometry2D(new GridEnvelope2D(rectangle), requested);
        final Parameter<Interpolation> readInterpolation =
                (Parameter<Interpolation>) AbstractGridFormat.INTERPOLATION.createValue();
        readInterpolation.setValue(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        Parameter<GridGeometry2D> readGG =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        readGG.setValue(grid);

        return wmtsReader
                .read(new GeneralParameterValue[] {readGG, readInterpolation})
                .getRenderedImage();
    }
}
