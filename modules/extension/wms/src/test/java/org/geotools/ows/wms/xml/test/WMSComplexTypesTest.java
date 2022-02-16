/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms.xml.test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.StyleImpl;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.xml.Attribution;
import org.geotools.ows.wms.xml.LogoURL;
import org.geotools.ows.wms.xml.MetadataURL;
import org.geotools.ows.wms.xml.WMSSchema;
import org.geotools.test.TestData;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.handlers.DocumentHandler;
import org.junit.Assert;
import org.junit.Test;

public class WMSComplexTypesTest {

    // GEOS-8477
    @Test
    public void testLegendURLType() throws Exception {

        File getCaps = TestData.file(this, "1.1.0CapabilitiesInvalid.xml");
        URL getCapsURL = getCaps.toURI().toURL();
        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
        Object object = null;

        try {
            object = DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);
        } catch (NullPointerException e) {
            Assert.fail("Parsing document with invalid xlink URL should not cause NPE");
        }

        Assert.assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        Assert.assertEquals(capabilities.getVersion(), "1.1.0");
        Assert.assertEquals(capabilities.getService().getName(), "OGC:WMS");
        Assert.assertEquals(capabilities.getService().getTitle(), "GMap WMS Demo Server");
        // invalid xlink URL will cause most online resources to be null
        Assert.assertNull(capabilities.getService().getOnlineResource());

        Assert.assertEquals(
                capabilities.getRequest().getGetCapabilities().getFormats().get(0),
                "application/vnd.ogc.wms_xml");

        Layer topLayer = capabilities.getLayerList().get(0);
        Assert.assertNotNull(topLayer);
        Assert.assertNull(topLayer.getParent());
        Assert.assertEquals(topLayer.getTitle(), "GMap WMS Demo Server");
        Assert.assertEquals(topLayer.getSrs().size(), 4);

        Attribution attribution = topLayer.getAttribution();
        Assert.assertNotNull(attribution);
        Assert.assertEquals(attribution.getTitle(), "test");
        // invalid xlink URL will cause most online resources to be null
        Assert.assertNull(attribution.getOnlineResource());

        LogoURL logoURL = attribution.getLogoURL();
        Assert.assertNotNull(logoURL);
        Assert.assertEquals(logoURL.getFormat(), "image/png");
        // invalid xlink URL will cause most online resources to be null
        Assert.assertNull(logoURL.getOnlineResource());
        Assert.assertEquals(logoURL.getHeight(), 100);
        Assert.assertEquals(logoURL.getWidth(), 100);

        Layer layer = capabilities.getLayerList().get(1);
        Assert.assertEquals(layer.getParent(), topLayer);
        Assert.assertFalse(layer.isQueryable());
        Assert.assertEquals(layer.getName(), "bathymetry");
        Assert.assertEquals(layer.getTitle(), "Elevation/Bathymetry");

        attribution = layer.getAttribution();
        Assert.assertNotNull(attribution);
        Assert.assertEquals(attribution.getTitle(), "test");

        // invalid xlink URL will cause most online resources to be null
        Assert.assertNull(attribution.getOnlineResource());
        logoURL = attribution.getLogoURL();
        Assert.assertNull(logoURL);

        MetadataURL metadataURL = layer.getMetadataURL().get(0);
        // invalid xlink URL will cause most online resources to be null
        Assert.assertNull(metadataURL.getUrl());

        StyleImpl style = layer.getStyles().get(0);
        Assert.assertNotNull(style);
        Object legendURL = style.getLegendURLs().get(0);
        // invalid xlink URL will cause most online resources to be null
        Assert.assertNull(legendURL);
    }

    @Test
    public void testEmptyOnlineResourceServiceDef() throws Exception {

        File getCaps = TestData.file(this, "1.3.0Capabilities_EmptyOnlineResource.xml");
        URL getCapsURL = getCaps.toURI().toURL();
        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
        Object object = null;

        try {
            object = DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);
        } catch (NullPointerException e) {
            Assert.fail("Parsing document with empty xlink URL should not cause NPE");
        }

        Assert.assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        Assert.assertEquals(capabilities.getVersion(), "1.3.0");
        Assert.assertEquals(capabilities.getService().getName(), "WMS");
        Assert.assertEquals(capabilities.getService().getTitle(), "World Map");
        // empty Service OnlineResource element should be null, preventing npe
        Assert.assertNull(capabilities.getService().getOnlineResource());
    }
}
