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
import org.geotools.ows.wms.xml.Dimension;
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
                capabilities.getRequest().getGetCapabilities().getFormats().get(0), "application/vnd.ogc.wms_xml");

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
    public void testMissingFormat() throws Exception {

        File getCaps = TestData.file(this, "1.1.0CapabilitiesMissingFormat.xml");
        URL getCapsURL = getCaps.toURI().toURL();
        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
        if (!hints.containsKey(DocumentFactory.VALIDATION_HINT)) {
            // Removing validation to match WMSGetCapabilitiesResponse behavior
            hints.put(DocumentFactory.VALIDATION_HINT, Boolean.FALSE);
        }
        Object object = DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);

        Assert.assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        Layer topLayer = capabilities.getLayerList().get(0);
        Assert.assertNotNull(topLayer);
        Attribution attribution = topLayer.getAttribution();
        Assert.assertNotNull(attribution);
        LogoURL logoURL = attribution.getLogoURL();
        Assert.assertEquals(
                logoURL.getOnlineResource().toString(), "http://www.osgeo.org/sites/all/themes/osgeo/logo.png");
        Assert.assertNull(logoURL.getFormat());
        Assert.assertEquals(logoURL.getHeight(), 100);
        Assert.assertEquals(logoURL.getHeight(), 100);
        Assert.assertEquals(logoURL.getWidth(), 100);

        StyleImpl style = topLayer.getStyles().get(0);
        Assert.assertNotNull(style);
        Object legendURL = style.getLegendURLs().get(0);
        Assert.assertEquals(legendURL, "http://www.example.com/legend.png");
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

    /**
     * Runs a test that should make te WMS capabilities parsing fail, because some given dimension has the 'units'
     * attribute missing.
     *
     * @throws Exception in case loading of test data fails.
     */
    @Test
    public void testDimensionMissingUnits() throws Exception {

        File getCaps = TestData.file(this, "1.3.0Capabilities_Dimensions_missingUnits.xml");
        URL getCapsURL = getCaps.toURI().toURL();
        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());

        Exception exception = null;
        try {
            DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);
            Assert.fail("Parsing the document should fail, because a layer dimension is missing a unit");
        } catch (Exception e) {
            exception = e;
        }

        Assert.assertNotNull(
                "Capabilities should fail to parse, because a layer dimension is missing a unit.", exception);
    }

    /**
     * Run some basic tests on WMS dimensions. Various questions should be answered:
     *
     * <p>
     *
     * <ul>
     *   <li>root layer has no dimensions
     *   <li>for given dimensions, 'units' attribute must not be missing
     *   <li>'units' values can be empty: GEOS-7632
     *   <li>some basic 'units' values tests
     *   <li>'unitSymbols' is optional
     *   <li>'default' is optional
     *   <li>some basic 'default' values tests
     * </ul>
     *
     * @throws Exception in case loading of test data fails.
     */
    @Test
    public void testDimensionUnits() throws Exception {

        File getCaps = TestData.file(this, "1.3.0Capabilities_Dimensions.xml");
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

        // root layer
        Layer layer = capabilities.getLayerList().get(0);
        Assert.assertNotNull(layer);
        Map<String, Dimension> layerDimensions = layer.getDimensions();
        Assert.assertTrue("Root layer has no dimensions", layerDimensions.isEmpty());

        // radar layer
        // ===========
        layer = capabilities.getLayerList().get(1);
        Assert.assertNotNull(layer);
        // time dimension
        Dimension dimension = layer.getDimension("time");
        Assert.assertNotNull(dimension);
        // unit
        String units = dimension.getUnits();
        Assert.assertNotNull("Units must not be missing", units);
        Assert.assertEquals("Unit of dimension time should be ISO8601.", "ISO8601", units);
        // unit symbol
        Assert.assertNull("Unit symbol should be null", dimension.getUnitSymbol());

        // NWP layer : check time dimension
        // ================================
        layer = capabilities.getLayerList().get(2);
        Assert.assertNotNull(layer);

        // time dimension
        dimension = layer.getDimension("time");
        Assert.assertNotNull(dimension);
        // default value
        Assert.assertEquals(
                "Default value should be 'current'",
                "current",
                dimension.getExtent().getDefaultValue());
        // unit
        units = dimension.getUnits();
        Assert.assertNotNull("Units must not be missing", units);
        Assert.assertEquals("Unit of dimension time should be ISO8601.", "ISO8601", units);
        // unit symbol
        Assert.assertEquals("Unit symbol should be 't'", "t", dimension.getUnitSymbol());

        // ref time dimension
        dimension = layer.getDimension("REFERENCE_TIME");
        Assert.assertNotNull(dimension);
        // unit
        units = dimension.getUnits();
        Assert.assertNotNull("Units must not be missing", units);
        Assert.assertEquals("Unit of dimension time should be ISO8601.", "ISO8601", units);
        // unit symbol
        Assert.assertEquals("Unit symbol should be 't'", "t", dimension.getUnitSymbol());

        // elevation dimension:
        // GEOS-7632: units must not be missing, but can be empty
        dimension = layer.getDimension("ELEVATION");
        Assert.assertNotNull(dimension);
        // unit
        units = dimension.getUnits();
        Assert.assertNotNull("Units must not be missing", units);
        Assert.assertEquals("Unit should be empty .", "", units);
        // unit symbol
        Assert.assertEquals("Unit symbol should be 'm'", "m", dimension.getUnitSymbol());
    }
}
