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

import org.geotools.filter.function.EnvFunction;
import org.geotools.styling.ChannelSelection;

public class SLDChannelSectionBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(
                ChannelSelection.class, new SLDChannelSelectionBinding(styleFactory).getType());
    }

    public void testRGB() throws Exception {
        SLDMockData.channelSelectionRGB(document, document);

        ChannelSelection cs = (ChannelSelection) parse();
        assertNotNull(cs);

        assertEquals(cs.getRGBChannels()[0].getChannelName().evaluate(null, String.class), "Red");
        assertEquals(cs.getRGBChannels()[1].getChannelName().evaluate(null, String.class), "Green");
        assertEquals(cs.getRGBChannels()[2].getChannelName().evaluate(null, String.class), "Blue");
    }

    /** Test Expression ENV function on ChannelSelection */
    public void testRGBExpression() throws Exception {
        SLDMockData.channelSelectionExpression(document, document);

        ChannelSelection cs = (ChannelSelection) parse();
        assertNotNull(cs);

        // test default value: 1
        EnvFunction.removeLocalValue("B1");
        assertEquals(
                cs.getRGBChannels()[0].getChannelName().evaluate(null, Integer.class).intValue(),
                1);
        // test ENV variable B1:20
        EnvFunction.setLocalValue("B1", "20");
        assertEquals(
                cs.getRGBChannels()[0].getChannelName().evaluate(null, Integer.class).intValue(),
                20);
        EnvFunction.removeLocalValue("B1");

        assertEquals(
                cs.getRGBChannels()[1].getChannelName().evaluate(null, Integer.class).intValue(),
                2);
        assertEquals(
                cs.getRGBChannels()[2].getChannelName().evaluate(null, Integer.class).intValue(),
                3);
    }

    public void testGray() throws Exception {
        SLDMockData.channelSelectionGray(document, document);

        ChannelSelection cs = (ChannelSelection) parse();
        assertNotNull(cs);

        assertEquals(cs.getGrayChannel().getChannelName().evaluate(null, String.class), "Gray");
    }
}
