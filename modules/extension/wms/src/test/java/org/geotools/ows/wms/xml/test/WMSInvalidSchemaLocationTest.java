/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.xml.WMSSchema;
import org.geotools.test.TestData;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.handlers.DocumentHandler;

public class WMSInvalidSchemaLocationTest extends TestCase {

    public void testGetCapabilities() throws Exception {
        // pointing to a file that has an invalid schema location reference
        File getCaps = TestData.file(this, "mapserverSchemaExtension.xml");
        URL getCapsURL = getCaps.toURI().toURL();

        // disable schema validation to match the WebMapServer behavior (the caps documented
        // tested is indeed not schema valid)
        Map<String, Object> hints = new HashMap<>();
        hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
        hints.put(DocumentFactory.VALIDATION_HINT, Boolean.FALSE);
        // used to blow up here due to the invalid schema reference (port -1 was chosen as a
        // suitably
        // invalid local url for tests, the real world case was an actual server that would never
        // respond
        // to the GetSchemaExtension request, forcing it to time out
        Object object = DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);

        assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

        WMSCapabilities capabilities = (WMSCapabilities) object;

        Layer Layer_with_Abstract_in_Style = (Layer) capabilities.getLayerList().get(1);
        assertEquals(Layer_with_Abstract_in_Style.getName(), "states");
        assertEquals(Layer_with_Abstract_in_Style.getTitle(), "states");
    }
}
