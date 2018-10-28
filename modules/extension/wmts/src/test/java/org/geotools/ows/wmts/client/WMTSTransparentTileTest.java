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
package org.geotools.ows.wmts.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.Layer;
import org.geotools.map.MapContent;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.renderer.GTRenderer;
import org.geotools.renderer.lite.StreamingRenderer;
import org.geotools.swing.SingleLayerMapContent;
import org.geotools.test.OnlineTestCase;
import org.geotools.tile.util.AsyncTileLayer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Ludovic Pecquot (E-IS) on 21/12/2017.
 * @link {org.geotools.tile.util.TileLayer}
 */
public class WMTSTransparentTileTest extends OnlineTestCase {

    private WMTSTileService service;

    @Before
    public void setup() throws Exception {
        service = createKVPService();
    }

    @Override
    protected void tearDownInternal() throws Exception {
        service = null;
    }

    @Override
    protected String getFixtureId() {
        return "wmts";
    }

    private WMTSTileService createKVPService() throws Exception {
        try {
            URL capaResource =
                    getClass()
                            .getClassLoader()
                            .getResource("test-data/geosolutions_getcapa_kvp.xml");
            assertNotNull("Can't find KVP getCapa file", capaResource);
            File capaFile = new File(capaResource.toURI());
            WMTSCapabilities capa = WMTSTileFactory4326Test.createCapabilities(capaFile);

            String baseURL = "http://demo.geo-solutions.it/geoserver/gwc/service/wmts";

            WMTSLayer layer = capa.getLayer("unesco:Unesco_point");
            TileMatrixSet matrixSet = capa.getMatrixSet("EPSG:900913");
            assertNotNull(layer);
            assertNotNull(matrixSet);

            return new WMTSTileService(baseURL, WMTSServiceType.KVP, layer, null, matrixSet);

        } catch (URISyntaxException ex) {
            fail(ex.getMessage());
            return null;
        }
    }

    @Test
    public void testTransparentTile() {

        Layer layer = new AsyncTileLayer(service); // or new TileLayer(service);

        MapContent map = new SingleLayerMapContent(layer);

        GTRenderer renderer = new StreamingRenderer();
        renderer.setMapContent(map);

        ReferencedEnvelope mapBounds = service.getBounds();
        double heightToWidth = mapBounds.getSpan(1) / mapBounds.getSpan(0);
        int imageWidth = 600;
        Rectangle imageBounds =
                new Rectangle(0, 0, imageWidth, (int) Math.round(imageWidth * heightToWidth));
        map.getViewport().setScreenArea(imageBounds);
        map.getViewport().setBounds(mapBounds);

        BufferedImage image =
                new BufferedImage(
                        imageBounds.width, imageBounds.height, BufferedImage.TYPE_INT_RGB);
        Graphics2D gr = image.createGraphics();

        // set background color for assertion
        gr.setPaint(Color.RED);
        gr.fill(imageBounds);

        // render tiles
        renderer.paint(gr, imageBounds, mapBounds);

        // get first pixel
        int argb = image.getRGB(0, 0);
        boolean hasAlphaChannel = image.getAlphaRaster() != null;
        Color c = new Color(argb, hasAlphaChannel);
        Assert.assertEquals("Color should be RED (background)", Color.RED, c);

        // get a rendered pixel
        argb = image.getRGB(69, 49);
        c = new Color(argb, hasAlphaChannel);
        Assert.assertEquals("Color should be black", Color.BLACK, c);

        // get another rendered pixel
        argb = image.getRGB(154, 64);
        c = new Color(argb, hasAlphaChannel);
        Assert.assertEquals("Color should be very dark red", new Color(44, 0, 0), c);
    }
}
