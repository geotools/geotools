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

import org.geotools.styling.ChannelSelection;


public class SLDChannelSectionBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(ChannelSelection.class, new SLDChannelSelectionBinding(styleFactory).getType());
    }

    public void testRGB() throws Exception {
        SLDMockData.channelSelectionRGB(document, document);

        ChannelSelection cs = (ChannelSelection) parse();
        assertNotNull(cs);

        assertEquals(cs.getRGBChannels()[0].getChannelName(), "Red");
        assertEquals(cs.getRGBChannels()[1].getChannelName(), "Green");
        assertEquals(cs.getRGBChannels()[2].getChannelName(), "Blue");
    }
    
    public void testGray() throws Exception {
        SLDMockData.channelSelectionGray(document, document);

        ChannelSelection cs = (ChannelSelection) parse();
        assertNotNull(cs);

        assertEquals(cs.getGrayChannel().getChannelName(), "Gray");
    }
}
