/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms.test;

import java.awt.image.BufferedImage;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import javax.imageio.ImageIO;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.StyleImpl;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WMSUtils;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.ows.wms.response.GetMapResponse;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @author Richard Gould */
public class WebMapServerOnlineTest extends WMSOnlineTestSupport {
    /*
     * Class under test for void WebMapServer(URL)
     */
    @Test
    public void testWebMapServerURL() throws Exception {
        WebMapServer wms = new WebMapServer(serverURL);

        Assert.assertNotNull(wms.getCapabilities());
    }

    @Test
    public void testGetCapabilities() throws Exception {
        WebMapServer wms = new WebMapServer(serverURL);

        Assert.assertNotNull(wms.getCapabilities());
    }

    @Test
    public void testIssueGetMapRequest() throws Exception {
        WebMapServer wms = new WebMapServer(serverURL);

        WMSCapabilities capabilities = wms.getCapabilities();

        GetMapRequest request = wms.createGetMapRequest();

        request.setVersion("1.1.1");

        Layer[] layers = WMSUtils.getNamedLayers(capabilities);
        Iterator iter = Arrays.asList(layers).iterator();
        int count = -1;
        while (iter.hasNext()) {

            Layer layer = (Layer) iter.next();
            count++;
            if (count >= 5) {
                break;
            }

            List styles = layer.getStyles();

            if (styles.isEmpty()) {
                request.addLayer(layer);
                continue;
            }

            Random random = new Random();
            int randomInt = random.nextInt(styles.size());

            request.addLayer(layer, (StyleImpl) styles.get(randomInt));
        }

        request.setSRS("EPSG:4326");
        request.setDimensions("400", "400");

        String format = "image/gif";
        List<String> formats = wms.getCapabilities().getRequest().getGetMap().getFormats();
        if (!formats.contains("image/gif")) {
            format = formats.get(0);
        }
        request.setFormat(format);

        request.setBBox("366800,2170400,816000,2460400");

        GetMapResponse response = wms.issueRequest(request);

        Assert.assertEquals(format, response.getContentType());

        BufferedImage image = ImageIO.read(response.getInputStream());
        Assert.assertEquals(image.getHeight(), 400);
    }

    @Test
    public void testGetEnvelope() throws Exception {
        WebMapServer wms = new WebMapServer(serverURL);

        WMSCapabilities caps = wms.getCapabilities();

        Layer layer = null;
        for (Layer l : caps.getLayerList()) {
            if ("topp:states".equalsIgnoreCase(l.getName())) {
                layer = l;
            }
        }
        assertNotNull(layer);
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");

        GeneralEnvelope envelope = wms.getEnvelope(layer, crs);
        double tolerance = 0.000000001;

        //        minx="-172.367" miny="35.6673" maxx="-11.5624" maxy="83.8293" />
        Assert.assertEquals(24.955967, envelope.getMinimum(0), tolerance);
        Assert.assertEquals(-124.731422, envelope.getMinimum(1), tolerance);
        Assert.assertEquals(49.371735, envelope.getMaximum(0), tolerance);
        Assert.assertEquals(-66.969849, envelope.getMaximum(1), tolerance);

        crs = CRS.decode("EPSG:2163");
        envelope = wms.getEnvelope(layer, crs);

        tolerance = 0.001; // metres
        //   -2636513,-2629385 : 3430696,1390944
        Assert.assertEquals(-2495667.977, envelope.getMinimum(0), tolerance);
        Assert.assertEquals(-2223677.196, envelope.getMinimum(1), tolerance);
        Assert.assertEquals(3291070.610, envelope.getMaximum(0), tolerance);
        Assert.assertEquals(959189.331, envelope.getMaximum(1), tolerance);
    }

    @Test
    public void testServiceExceptions() throws Exception {
        WebMapServer wms = new WebMapServer(serverURL);
        GetMapRequest request = wms.createGetMapRequest();
        request.addLayer("NoLayer", "NoStyle");
        GetMapResponse resp = wms.issueRequest(request);
        assertTrue(resp.getContentType().contains("text/xml"));
    }
}
