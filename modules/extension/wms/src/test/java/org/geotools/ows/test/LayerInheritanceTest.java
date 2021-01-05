/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.test;

import java.io.File;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.StyleImpl;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.xml.WMSSchema;
import org.geotools.test.TestData;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.handlers.DocumentHandler;
import org.junit.Assert;
import org.junit.Test;

public class LayerInheritanceTest {

    @Test
    public void testInheritCapabilities() throws Exception {

        File getCaps = TestData.file(this, "inheritCap.xml");
        URL getCapsURL = getCaps.toURI().toURL();

        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
        Object object = DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);

        SchemaFactory.getInstance(WMSSchema.NAMESPACE);

        Assert.assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        // Get first test layer
        Layer layer = capabilities.getLayerList().get(0);
        Assert.assertNotNull(layer);
        Assert.assertEquals(3, layer.getDimensions().size());
        Assert.assertEquals("ISO8601", layer.getDimension("time").getUnits());

        // Get next test layer, it's nested 3 deep
        List<Layer> allLayers = capabilities.getLayerList();

        layer = allLayers.get(2);
        Assert.assertNotNull(layer);
        Assert.assertNotNull(layer.getParent());
        Assert.assertEquals(3, layer.getDimensions().size());

        // Should be false by default since not specified in layer or ancestors
        Assert.assertFalse(layer.isQueryable());
        Assert.assertEquals(layer.getTitle(), "Coastlines");

        // Should be 5 total after accumulating all ancestors
        Assert.assertEquals(5, layer.getSrs().size());
        Assert.assertTrue(layer.getSrs().contains("EPSG:26906"));
        Assert.assertTrue(layer.getSrs().contains("EPSG:26905"));
        Assert.assertTrue(layer.getSrs().contains("EPSG:4326"));
        Assert.assertTrue(layer.getSrs().contains("AUTO:42003"));
        Assert.assertTrue(layer.getSrs().contains("AUTO:42005"));

        // 2 total, this layer plus top most layer
        Assert.assertEquals(layer.getStyles().size(), 2);
        Assert.assertTrue(layer.getStyles().contains(new StyleImpl("TestStyle")));
        Assert.assertTrue(layer.getStyles().contains(new StyleImpl("default")));

        // Next test layer, nested 3 deep but different path
        layer = capabilities.getLayerList().get(4);
        Assert.assertNotNull(layer);
        Assert.assertNotNull(layer.getParent());
        Assert.assertEquals(3, layer.getDimensions().size());
        Assert.assertEquals(1, layer.getExtents().size());
        Assert.assertEquals(layer.getExtent("time").getName(), "time");

        // Should be true by default since inherited from parent
        Assert.assertEquals(layer.getName(), "RTOPO");
        Assert.assertTrue(layer.isQueryable());
    }
}
