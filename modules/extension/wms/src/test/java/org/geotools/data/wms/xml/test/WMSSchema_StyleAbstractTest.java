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
package org.geotools.data.wms.xml.test;

import java.io.File;
import java.net.URL;
import java.util.logging.Level;

import junit.framework.TestCase;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.xml.WMSSchema;
import org.geotools.test.TestData;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.schema.Schema;

/**
 * 
 *
 * @source $URL$
 */
public class WMSSchema_StyleAbstractTest extends TestCase {
    

	public void testGetCapabilities() throws Exception {
		
		File getCaps = TestData.file(this, "1.3.0Capabilities_StyleAbstractTest.xml");
        URL getCapsURL = getCaps.toURI().toURL();

		Object object = DocumentFactory.getInstance(getCapsURL.openStream(), null, Level.WARNING);

        Schema schema = WMSSchema.getInstance();
		SchemaFactory.getInstance(WMSSchema.NAMESPACE);
				
		assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);
		
		WMSCapabilities capabilities = (WMSCapabilities) object;
		
		Layer Layer_with_Abstract_in_Style = (Layer) capabilities.getLayerList().get(1);
		assertEquals(Layer_with_Abstract_in_Style.getName(), "Layer_with_Abstract_in_Style");
		assertEquals(Layer_with_Abstract_in_Style.getTitle(), "Layer with Abstract in Style");
		assertEquals("http://www.osgeo.org/sites/all/themes/osgeo/logo.png", Layer_with_Abstract_in_Style.getStyles().get(0).getLegendURLs().get(0));
		
		Layer Layer_with_empty_Abstract_in_Style = (Layer) capabilities.getLayerList().get(2);
		assertEquals(Layer_with_empty_Abstract_in_Style.getName(), "Layer_with_empty_Abstract_in_Style");
		assertEquals(Layer_with_empty_Abstract_in_Style.getTitle(), "Layer with empty Abstract in Style");
		assertEquals("http://www.osgeo.org/sites/all/themes/osgeo/logo.png", Layer_with_empty_Abstract_in_Style.getStyles().get(0).getLegendURLs().get(0));
		
	}
}
