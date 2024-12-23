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
package org.geotools.ows.wmts.online;

import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.Map;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.NoSuchAuthorityCodeException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.DelegateHTTPClient;
import org.geotools.http.HTTPClientFinder;
import org.geotools.http.HTTPResponse;
import org.geotools.map.MapContent;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.map.WMTSMapLayer;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.lite.RendererUtilities;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.test.OnlineTestCase;
import org.junit.Assert;
import org.junit.Test;

/** @author ian */
public class WMTSMapLayerOnlineTest extends OnlineTestCase {

    protected CountHttpGetClient httpClient;

    protected URL serverURL;

    protected WMTSMapLayer kvpMapLayer;

    @Override
    protected String getFixtureId() {
        return "wmts";
    }

    @Override
    protected void setUpInternal() throws Exception {
        serverURL = new URL(fixture.getProperty("kvp_server"));
        httpClient = new CountHttpGetClient();
        WebMapTileServer server = new WebMapTileServer(serverURL, httpClient);
        WMTSLayer wlayer = server.getCapabilities().getLayer("topp:states");
        assertNotNull(wlayer);
        kvpMapLayer = new WMTSMapLayer(server, wlayer);
    }

    /** Test method for {@link WMTSMapLayer#getBounds()}. */
    @Test
    public void testGetBounds() throws FactoryException {
        ReferencedEnvelope env = kvpMapLayer.getBounds();
        checkEnv(env);
    }

    /** */
    void checkEnv(ReferencedEnvelope env) throws FactoryException {
        assertEquals("wrong CRS", "EPSG:3857", CRS.lookupIdentifier(env.getCoordinateReferenceSystem(), true));
        assertEquals(env.getMinimum(0), -1.3885038382923e7, 0.001);
        assertEquals(env.getMinimum(1), 2870337.130793, 0.001);
        assertEquals(env.getMaximum(0), -7455049.489182421, 0.001);
        assertEquals(env.getMaximum(1), 6338174.055756185, 0.001);
    }

    /** Test method for {@link WMTSMapLayer#getCoordinateReferenceSystem()}. */
    @Test
    public void testGetCoordinateReferenceSystem() throws FactoryException {

        assertEquals("wrong CRS", "EPSG:3857", CRS.lookupIdentifier(kvpMapLayer.getCoordinateReferenceSystem(), true));
    }

    /** Test if our http client is used, and how many calls. */
    @Test
    public void testCountHttpGetCount() {
        StreamingRenderer renderer = new StreamingRenderer();
        MapContent mapContent = new MapContent();
        mapContent.addLayer(kvpMapLayer);
        renderer.setMapContent(mapContent);
        BufferedImage image = new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB);

        Rectangle paintArea = new Rectangle(0, 0, 100, 100);
        AffineTransform transform = RendererUtilities.worldToScreenTransform(kvpMapLayer.getBounds(), paintArea);
        renderer.paint(image.createGraphics(), paintArea, kvpMapLayer.getBounds(), transform);
        Assert.assertEquals(2, httpClient.count);
    }

    /**
     * Test method for
     * {@link WMTSMapLayer#isNativelySupported(org.geotools.api.referencing.crs.CoordinateReferenceSystem)}.
     */
    @Test
    public void testIsNativelySupported() throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        assertTrue(kvpMapLayer.isNativelySupported(crs));
        crs = CRS.decode("epsg:3857");
        // Sort out web mercator lookup
        // assertTrue(restMapLayer.isNativelySupported(crs));
        // assertTrue(kvpMapLayer.isNativelySupported(crs));
        crs = CRS.decode("epsg:27700");
        // assertFalse(restMapLayer.isNativelySupported(crs));
        assertFalse(kvpMapLayer.isNativelySupported(crs));
    }

    static class CountHttpGetClient extends DelegateHTTPClient {

        int count;

        public CountHttpGetClient() {
            super(HTTPClientFinder.createClient());
        }

        @Override
        public HTTPResponse get(URL url) throws IOException {
            count++;
            return delegate.get(url);
        }

        @Override
        public HTTPResponse get(URL url, Map<String, String> headers) throws IOException {
            count++;
            return delegate.get(url, headers);
        }
    }
}
