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
package org.geotools.ows.wms.xml.test;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.xml.WMSSchema;
import org.geotools.test.TestData;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.SchemaFactory;
import org.junit.Assert;
import org.junit.Test;

public class WMSSchema_StyleAbstractTest {

    @Test
    public void testGetCapabilities() throws Exception {

        File getCaps = TestData.file(this, "1.3.0Capabilities_StyleAbstractTest.xml");
        URL getCapsURL = getCaps.toURI().toURL();

        Object object = DocumentFactory.getInstance(getCapsURL.openStream(), null, Level.WARNING);

        SchemaFactory.getInstance(WMSSchema.NAMESPACE);

        Assert.assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        Layer Layer_with_Abstract_in_Style = capabilities.getLayerList().get(1);
        Assert.assertEquals(Layer_with_Abstract_in_Style.getName(), "Layer_with_Abstract_in_Style");
        Assert.assertEquals(Layer_with_Abstract_in_Style.getTitle(), "Layer with Abstract in Style");
        Assert.assertEquals(
                "http://www.osgeo.org/sites/all/themes/osgeo/logo.png",
                Layer_with_Abstract_in_Style.getStyles().get(0).getLegendURLs().get(0));

        Layer Layer_with_empty_Abstract_in_Style = capabilities.getLayerList().get(2);
        Assert.assertEquals(Layer_with_empty_Abstract_in_Style.getName(), "Layer_with_empty_Abstract_in_Style");
        Assert.assertEquals(Layer_with_empty_Abstract_in_Style.getTitle(), "Layer with empty Abstract in Style");
        Assert.assertEquals(
                "http://www.osgeo.org/sites/all/themes/osgeo/logo.png",
                Layer_with_empty_Abstract_in_Style.getStyles()
                        .get(0)
                        .getLegendURLs()
                        .get(0));
    }
}
