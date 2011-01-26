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
import org.geotools.styling.ExternalGraphic;
import org.geotools.xlink.XLINK;


public class SLDExternalGraphicBindingTest extends SLDTestSupport {
    public void testType() throws Exception {
        assertEquals(ExternalGraphic.class, new SLDExternalGraphicBinding(null).getType());
    }

    public void testNormal() throws Exception {
        document.appendChild(document.createElementNS(SLD.NAMESPACE, "ExternalGraphic"));

        Element r = document.createElementNS(SLD.NAMESPACE, "OnlineResource");
        r.setAttributeNS(XLINK.NAMESPACE, "href", getClass().getResource("eclipse.png").toString());

        Element f = document.createElementNS(SLD.NAMESPACE, "Format");
        f.appendChild(document.createTextNode("image/png"));

        document.getDocumentElement().appendChild(r);
        document.getDocumentElement().appendChild(f);

        ExternalGraphic externalGraphic = (ExternalGraphic) parse();
        assertNotNull(externalGraphic);

        assertEquals(getClass().getResource("eclipse.png"), externalGraphic.getLocation());
        assertEquals("image/png", externalGraphic.getFormat());
    }
}
