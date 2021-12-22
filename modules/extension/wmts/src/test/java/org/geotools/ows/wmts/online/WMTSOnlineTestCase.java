package org.geotools.ows.wmts.online;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import javax.media.jai.Interpolation;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.map.WMTSCoverageReader;
import org.geotools.ows.wmts.map.WMTSMapLayer;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.parameter.Parameter;
import org.geotools.test.OnlineTestCase;
import org.geotools.tile.Tile;
import org.opengis.parameter.GeneralParameterValue;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public abstract class WMTSOnlineTestCase extends OnlineTestCase {

    @Override
    protected String getFixtureId() {
        return "wmts-online";
    }

    @Override
    protected Properties createExampleFixture() {
        return new Properties();
    }

    protected Set<Tile> issueTileRequest(
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
        return request.getTiles();
    }

    protected RenderedImage getRenderImageResult(
            WebMapTileServer wmts,
            WMTSCapabilities capabilities,
            ReferencedEnvelope bboxes,
            ReferencedEnvelope requested,
            String layerName)
            throws IOException {

        WMTSCoverageReader wmtsReader = wmtsCoverageReader(wmts, capabilities, bboxes, layerName);
        Rectangle rectangle = new Rectangle(0, 0, 768, 589);
        GridGeometry2D grid = new GridGeometry2D(new GridEnvelope2D(rectangle), requested);

        return wmtsReader.read(getGeneralParameterValues(grid)).getRenderedImage();
    }

    protected RenderedImage getRenderImageResult(
            WMTSCoverageReader wmtsReader, ReferencedEnvelope requested) throws IOException {

        Rectangle rectangle = new Rectangle(0, 0, 768, 589);
        GridGeometry2D grid = new GridGeometry2D(new GridEnvelope2D(rectangle), requested);

        return wmtsReader.read(getGeneralParameterValues(grid)).getRenderedImage();
    }

    protected WMTSCoverageReader wmtsCoverageReader(
            WebMapTileServer wmts,
            WMTSCapabilities capabilities,
            ReferencedEnvelope bboxes,
            String layerName) {
        WMTSLayer layer = capabilities.getLayer(layerName);
        assertNotNull(layer);
        layer.setBoundingBoxes(new CRSEnvelope(bboxes));
        WMTSMapLayer mapLayer = new WMTSMapLayer(wmts, layer);
        return mapLayer.getReader();
    }

    protected GeneralParameterValue[] getGeneralParameterValues(GridGeometry2D grid) {
        final Parameter<Interpolation> readInterpolation =
                (Parameter<Interpolation>) AbstractGridFormat.INTERPOLATION.createValue();
        readInterpolation.setValue(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        Parameter<GridGeometry2D> readGG =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        readGG.setValue(grid);
        return new GeneralParameterValue[] {readGG, readInterpolation};
    }
}
