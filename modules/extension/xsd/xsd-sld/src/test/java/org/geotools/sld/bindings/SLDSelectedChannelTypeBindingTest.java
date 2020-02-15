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
import org.geotools.filter.v1_0.OGC;
import org.geotools.styling.SelectedChannelType;
import org.junit.Test;
import org.w3c.dom.Element;

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
        assertEquals(channelType.getChannelName().evaluate(null, String.class), "SomeName");

        assertNotNull(channelType.getContrastEnhancement());
    }

    /** Test Expression evaluation on SourceChannelName */
    @Test
    public void testChannelNameExpression() throws Exception {
        final String b1 = "B1";
        document.appendChild(document.createElementNS(SLD.NAMESPACE, "GreenChannel"));

        Element name = document.createElementNS(SLD.NAMESPACE, "SourceChannelName");
        Element expression = document.createElementNS(OGC.NAMESPACE, OGC.Function.getLocalPart());
        expression.setAttribute("name", "env");
        name.appendChild(expression);
        Element envName = document.createElementNS(OGC.NAMESPACE, OGC.Literal.getLocalPart());
        envName.appendChild(document.createTextNode(b1));
        Element envDefaultValue =
                document.createElementNS(OGC.NAMESPACE, OGC.Literal.getLocalPart());
        envDefaultValue.appendChild(document.createTextNode("1"));
        expression.appendChild(envName);
        expression.appendChild(envDefaultValue);

        Element contrast = document.createElementNS(SLD.NAMESPACE, "ContrastEnhancement");

        document.getDocumentElement().appendChild(name);
        document.getDocumentElement().appendChild(contrast);

        SelectedChannelType channelType = (SelectedChannelType) parse();
        assertNotNull(channelType);
        assertNotNull(channelType.getChannelName());

        // check default value
        EnvFunction.removeLocalValue(b1);
        assertEquals(channelType.getChannelName().evaluate(null, Integer.class).intValue(), 1);
        // check env variable
        EnvFunction.setLocalValue(b1, "12");
        assertEquals(channelType.getChannelName().evaluate(null, Integer.class).intValue(), 12);
        EnvFunction.removeLocalValue(b1);

        assertNotNull(channelType.getContrastEnhancement());
    }
}
