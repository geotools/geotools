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
import junit.framework.TestCase;
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

public class WMSComplexTypesTest extends TestCase {

    // GEOS-8477
    public void testLegendURLType() throws Exception {

        File getCaps = TestData.file(this, "1.1.0CapabilitiesInvalid.xml");
        URL getCapsURL = getCaps.toURI().toURL();
        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
        Object object = null;

        try {
            object = DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);
        } catch (NullPointerException e) {
            fail("Parsing document with invalid xlink URL should not cause NPE");
        }

        assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        assertEquals(capabilities.getVersion(), "1.1.0");
        assertEquals(capabilities.getService().getName(), "OGC:WMS");
        assertEquals(capabilities.getService().getTitle(), "GMap WMS Demo Server");
        // invalid xlink URL will cause most online resources to be null
        assertNull(capabilities.getService().getOnlineResource());

        assertEquals(
                capabilities.getRequest().getGetCapabilities().getFormats().get(0),
                "application/vnd.ogc.wms_xml");

        Layer topLayer = (Layer) capabilities.getLayerList().get(0);
        assertNotNull(topLayer);
        assertNull(topLayer.getParent());
        assertEquals(topLayer.getTitle(), "GMap WMS Demo Server");
        assertEquals(topLayer.getSrs().size(), 4);

        Attribution attribution = topLayer.getAttribution();
        assertNotNull(attribution);
        assertEquals(attribution.getTitle(), "test");
        // invalid xlink URL will cause most online resources to be null
        assertNull(attribution.getOnlineResource());

        LogoURL logoURL = attribution.getLogoURL();
        assertNotNull(logoURL);
        assertEquals(logoURL.getFormat(), "image/png");
        // invalid xlink URL will cause most online resources to be null
        assertNull(logoURL.getOnlineResource());
        assertEquals(logoURL.getHeight(), 100);
        assertEquals(logoURL.getWidth(), 100);

        Layer layer = capabilities.getLayerList().get(1);
        assertEquals(layer.getParent(), topLayer);
        assertFalse(layer.isQueryable());
        assertEquals(layer.getName(), "bathymetry");
        assertEquals(layer.getTitle(), "Elevation/Bathymetry");

        attribution = layer.getAttribution();
        assertNotNull(attribution);
        assertEquals(attribution.getTitle(), "test");

        // invalid xlink URL will cause most online resources to be null
        assertNull(attribution.getOnlineResource());
        logoURL = attribution.getLogoURL();
        assertNull(logoURL);

        MetadataURL metadataURL = layer.getMetadataURL().get(0);
        // invalid xlink URL will cause most online resources to be null
        assertNull(metadataURL.getUrl());

        StyleImpl style = layer.getStyles().get(0);
        assertNotNull(style);
        Object legendURL = style.getLegendURLs().get(0);
        // invalid xlink URL will cause most online resources to be null
        assertNull(legendURL);
    }
}
