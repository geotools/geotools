/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.map;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.net.URL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.test.OnlineTestCase;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @author ian */
public class WMTSMapLayerTest extends OnlineTestCase {

    private URL serverURL;

    private URL restWMTS;

    private WMTSMapLayer kvpMapLayer;

    private WMTSMapLayer restMapLayer;

    @Override
    protected void setUpInternal() throws Exception {
        serverURL = new URL(fixture.getProperty("kvp_server"));
        WebMapTileServer server = new WebMapTileServer(serverURL);
        WMTSLayer wlayer = (WMTSLayer) server.getCapabilities().getLayer("topp:states");

        kvpMapLayer = new WMTSMapLayer(server, wlayer);
        restWMTS = new URL(fixture.getProperty("rest_server"));
        WebMapTileServer server2 = new WebMapTileServer(restWMTS);
        WMTSLayer w2layer = (WMTSLayer) server2.getCapabilities().getLayer("topp:states");
        restMapLayer = new WMTSMapLayer(server2, w2layer);
    }

    /** Test method for {@link WMTSMapLayer#getBounds()}. */
    @Test
    public void testGetBounds() throws FactoryException {
        ReferencedEnvelope env = kvpMapLayer.getBounds();
        checkEnv(env);
        env = restMapLayer.getBounds();
        // work out how to make MapProxy set bounds to layer size
        // checkEnv(env);
    }

    /** */
    private void checkEnv(ReferencedEnvelope env) throws FactoryException {
        assertEquals(
                "wrong CRS",
                "EPSG:3857",
                CRS.lookupIdentifier(env.getCoordinateReferenceSystem(), true));
        assertEquals(env.getMinimum(0), -1.3885038382923e7, 0.001);
        assertEquals(env.getMinimum(1), 2870337.130793, 0.001);
        assertEquals(env.getMaximum(0), -7455049.489182421, 0.001);
        assertEquals(env.getMaximum(1), 6338174.055756185, 0.001);
    }

    /** Test method for {@link WMTSMapLayer#getCoordinateReferenceSystem()}. */
    @Test
    public void testGetCoordinateReferenceSystem() throws FactoryException {

        assertEquals(
                "wrong CRS",
                "EPSG:3857",
                CRS.lookupIdentifier(kvpMapLayer.getCoordinateReferenceSystem(), true));
        assertEquals(
                "wrong CRS",
                "EPSG:4326",
                CRS.lookupIdentifier(restMapLayer.getCoordinateReferenceSystem(), true));
    }

    /** Test method for {@link WMTSMapLayer#getLastGetMap()}. */
    @Test
    public void testGetLastGetMap() {
        StreamingRenderer renderer = new StreamingRenderer();
        MapContent mapContent = new MapContent();
        mapContent.addLayer(kvpMapLayer);
        renderer.setMapContent(mapContent);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

        Rectangle paintArea = new Rectangle(0, 0, 100, 100);
        AffineTransform transform =
                RendererUtilities.worldToScreenTransform(kvpMapLayer.getBounds(), paintArea);
        renderer.paint(image.createGraphics(), paintArea, kvpMapLayer.getBounds(), transform);
        assertNotNull(kvpMapLayer.getLastGetMap());
    }

    /**
     * Test method for {@link
     * WMTSMapLayer#isNativelySupported(org.opengis.referencing.crs.CoordinateReferenceSystem)}.
     */
    @Test
    public void testIsNativelySupported() throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        assertTrue(restMapLayer.isNativelySupported(crs));
        assertTrue(kvpMapLayer.isNativelySupported(crs));
        crs = CRS.decode("epsg:3857");
        // Sort out web mercator lookup
        // assertTrue(restMapLayer.isNativelySupported(crs));
        // assertTrue(kvpMapLayer.isNativelySupported(crs));
        crs = CRS.decode("epsg:27700");
        // assertFalse(restMapLayer.isNativelySupported(crs));
        assertFalse(kvpMapLayer.isNativelySupported(crs));
    }

    @Override
    protected String getFixtureId() {
        return "wmts";
    }
}
