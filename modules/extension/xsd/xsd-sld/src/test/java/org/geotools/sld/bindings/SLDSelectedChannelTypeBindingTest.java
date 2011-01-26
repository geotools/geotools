/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.sld.bindings;

import org.w3c.dom.Element;
import org.geotools.styling.SelectedChannelType;


public class SLDSelectedChannelTypeBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(SelectedChannelType.class, new SLDSelectedChannelTypeBinding(null).getType());
    }

    public void testNormal() throws Exception {
        document.appendChild(document.createElementNS(SLD.NAMESPACE, "GreenChannel"));

        Element name = document.createElementNS(SLD.NAMESPACE, "SourceChannelName");
        name.appendChild(document.createTextNode("SomeName"));

        Element contrast = document.createElementNS(SLD.NAMESPACE, "ContrastEnhancement");

        document.getDocumentElement().appendChild(name);
        document.getDocumentElement().appendChild(contrast);

        SelectedChannelType channelType = (SelectedChannelType) parse();
        assertNotNull(channelType);
        assertNotNull(channelType.getChannelName());
        assertEquals(channelType.getChannelName(), "SomeName");

        assertNotNull(channelType.getContrastEnhancement());
    }
}
