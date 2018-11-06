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
package org.geotools.ows.bindings;

import net.opengis.ows10.GetCapabilitiesType;
import org.geotools.ows.OWSTestSupport;
import org.geotools.xsd.Binding;
import org.geotools.xsd.ows.OWS;

public class GetCapabilitiesTypeBindingTest extends OWSTestSupport {
    public void testType() throws Exception {
        assertEquals(GetCapabilitiesType.class, binding(OWS.GetCapabilitiesType).getType());
    }

    public void testExecutionMode() throws Exception {
        assertEquals(Binding.OVERRIDE, binding(OWS.GetCapabilitiesType).getExecutionMode());
    }

    public void testParse() throws Exception {
        String xml =
                "<ows:GetCapabilities xmlns:ows=\"http://www.opengis.net/ows\" version=\"1.1.0\">"
                        + "<ows:AcceptVersions>"
                        + "<ows:Version>1.0.0</ows:Version>"
                        + "</ows:AcceptVersions>"
                        + "</ows:GetCapabilities>";

        buildDocument(xml);

        GetCapabilitiesType getCaps = (GetCapabilitiesType) parse();
        assertNotNull(getCaps);

        assertEquals(1, getCaps.getAcceptVersions().getVersion().size());
    }
}
