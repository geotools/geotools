package org.geotools.map;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import java.net.URL;
import java.util.Set;

import org.geotools.data.wmts.WebMapTileServer;
import org.geotools.data.wmts.client.WMTSTileService;
import org.geotools.data.wmts.model.WMTSLayer;
import org.geotools.data.wmts.request.AbstractGetTileRequest;
import org.geotools.data.wmts.request.GetTileRequest;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.geotools.tile.Tile.RenderState;
import org.junit.Ignore;
import org.junit.Test;

/**
 * 
 * @author Christian Marsch
 */
@Ignore("Do not know what to do when creating an online test")
public class WMTSCoverageReaderOnlineTest {

	/**
	 * Testing the behavior of {@link WMTSCoverageReader} when accessing a layer
	 * only providing image/jpeg.<br>
	 * This test ensures the {@link AbstractGetTileRequest#FORMAT} was set in
	 * {@link WMTSCoverageReader#initTileRequest(ReferencedEnvelope, int, int, String)}
	 * and is delegated by {@link AbstractGetTileRequest#getTiles()} to
	 * {@link WMTSTileService}.
	 */
	@Test
	public void testKVPInitMapRequest_JPEGFormatOnly() throws Exception {
		WebMapTileServer server = new WebMapTileServer(
				new URL("https://gibs.earthdata.nasa.gov/wmts/epsg4326/best/wmts.cgi"));
		WMTSLayer layer = (WMTSLayer) server.getCapabilities().getLayer("VIIRS_CityLights_2012");
		WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
		ReferencedEnvelope bbox = new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
		int width = 400;
		int height = 200;
		ReferencedEnvelope grid = wcr.initTileRequest(bbox, width, height, null);
		assertNotNull(grid);
		GetTileRequest mapRequest = wcr.getTileRequest();
		mapRequest.setCRS(grid.getCoordinateReferenceSystem());
		Set<Tile> responses = wcr.wmts.issueRequest(mapRequest);
		assertFalse("At least one response must be done", responses.isEmpty());
		Tile firstTile = responses.stream().findFirst().get();
		firstTile.getBufferedImage();
		assertNotEquals(RenderState.INVALID, firstTile.getRenderState());
	}

	/**
	 * Test case for server described in ticket GEOT-6018.
	 */
	@Test
	public void testKVPInitMapRequest_PNGJPEGFormatOnly() throws Exception {
		WebMapTileServer server = new WebMapTileServer(
				new URL("https://sampleserver6.arcgisonline.com/arcgis/rest/services/Toronto/ImageServer/WMTS"));
		WMTSLayer layer = (WMTSLayer) server.getCapabilities().getLayer("Toronto");
		WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
		ReferencedEnvelope bbox = new ReferencedEnvelope(-8837500.0 - 300.0, -8837500.0 + 300.0, 5405478 - 300.0,
				5405478 + 300.0, CRS.decode("EPSG:3857"));
		int width = 400;
		int height = 200;
		ReferencedEnvelope grid = wcr.initTileRequest(bbox, width, height, null);
		assertNotNull(grid);
		GetTileRequest mapRequest = wcr.getTileRequest();
		mapRequest.setCRS(grid.getCoordinateReferenceSystem());
		Set<Tile> responses = wcr.wmts.issueRequest(mapRequest);
		assertFalse("At least one response must be done", responses.isEmpty());
		Tile firstTile = responses.stream().findFirst().get();
		firstTile.getBufferedImage();
		assertNotEquals(RenderState.INVALID, firstTile.getRenderState());
	}
}
