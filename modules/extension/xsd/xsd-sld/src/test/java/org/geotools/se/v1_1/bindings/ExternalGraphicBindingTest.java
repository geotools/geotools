/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.se.v1_1.bindings;


import org.geotools.se.v1_1.SE;
import org.geotools.se.v1_1.SETestSupport;
import org.geotools.styling.ExternalGraphic;
import org.geotools.xlink.XLINK;
import org.w3c.dom.Element;

/**
 * @author Sebastian Graca, ISPiK S.A.
 */
public class ExternalGraphicBindingTest extends SETestSupport {
    public void testOnlineResource() throws Exception {
        document.appendChild(document.createElementNS(SE.NAMESPACE, "ExternalGraphic"));

        Element r = document.createElementNS(SE.NAMESPACE, "OnlineResource");
        r.setAttributeNS(XLINK.NAMESPACE, "href", SETestSupport.class.getResource("inlineContent-image.png").toString());

        Element f = document.createElementNS(SE.NAMESPACE, "Format");
        f.appendChild(document.createTextNode("image/png"));

        document.getDocumentElement().appendChild(r);
        document.getDocumentElement().appendChild(f);

        ExternalGraphic externalGraphic = (ExternalGraphic) parse();
        assertNotNull(externalGraphic);

        assertEquals(SETestSupport.class.getResource("inlineContent-image.png"), externalGraphic.getLocation());
        assertEquals("image/png", externalGraphic.getFormat());
        assertNull(externalGraphic.getInlineContent());
    }

    public void testInlineContent() throws Exception {
        document.appendChild(document.createElementNS(SE.NAMESPACE, "ExternalGraphic"));

        Element c = document.createElementNS(SE.NAMESPACE, "InlineContent");
        c.setAttributeNS(SE.NAMESPACE, "encoding", "base64");
        c.appendChild(document.createTextNode("iVBORw0KGgoAAAANSUhEUgAAABAAAAAQCAIAAACQkWg2AAAAK3RFWHRDcmVhdGlvbiBUaW1lAFd0IDE0IHdyeiAyMDEwIDEyOjA2OjAyICswMTAweoAlkgAAAAd0SU1FB9oJDgo6HdmGt90AAAAJcEhZcwAACxIAAAsSAdLdfvwAAAAEZ0FNQQAAsY8L/GEFAAABfklEQVR42mP8//8/AwYACn779o2bmxtTiokBG9iwboOfq9+1K9eI0vDp06cZk2e8ffp2zuQ5mPZj0bB963aGLwxTI6YdO3QMiyX/UcHz58/tLeyXZ674O+NfgVthWnTaz58/kRWg27B8yXKu31yeGp6MjIwpVimnjp86fvg4Tic9ffp0zfI1ZU7lfBx8QK6WpFasSdzMiTN//fqFRQPQusULFsvzyFsqWMIFw4zDHt5+uH/Xfiwarl27tnj+4nSzdDYWNrigtIB0iF5IZ1MnMOhQNACNnztjrq64roWiJQMqCDUO+/D6w/YN21E0XLp06fD+w3l2+cjGwy1Jt82YO3Pe+/fvoRqAfupq6bKVs7VQsMAa8REmEf8+/V04fSFUw/Hjxy+cvZBklgwMSqwagIGWYpm6cOEiYDCyAI2fPnG6mrD6n39/Lj25xIADAB325/vvVQtXsXz58uX7z+8vfr7I2ZrNgBew87HfvnebERg+X79+/ffvHwMRgJWVFQBa4Mt756r78AAAAABJRU5ErkJggg=="));

        Element f = document.createElementNS(SE.NAMESPACE, "Format");
        f.appendChild(document.createTextNode("image/png"));

        document.getDocumentElement().appendChild(c);
        document.getDocumentElement().appendChild(f);

        ExternalGraphic externalGraphic = (ExternalGraphic) parse();
        assertNotNull(externalGraphic);

        assertEquals("image/png", externalGraphic.getFormat());
        assertImagesEqual(getReferenceImage("inlineContent-image.png"), externalGraphic.getInlineContent());
        assertNull(externalGraphic.getLocation());
    }

    public void testInlineContentWithINvalidData() throws Exception {
        document.appendChild(document.createElementNS(SE.NAMESPACE, "ExternalGraphic"));

        Element c = document.createElementNS(SE.NAMESPACE, "InlineContent");
        c.setAttributeNS(SE.NAMESPACE, "encoding", "base64");
        c.appendChild(document.createTextNode("PD94bWwgdmVyc2lvbj0iMS4wIiBlbmNvZGluZz0iVVRGLTgiPz4NCg=="));

        Element f = document.createElementNS(SE.NAMESPACE, "Format");
        f.appendChild(document.createTextNode("image/png"));

        document.getDocumentElement().appendChild(c);
        document.getDocumentElement().appendChild(f);

        ExternalGraphic externalGraphic = (ExternalGraphic) parse();
        assertNotNull(externalGraphic);

        assertEquals("image/png", externalGraphic.getFormat());
        assertNotNull(externalGraphic.getInlineContent());
        assertEquals(1, externalGraphic.getInlineContent().getIconWidth());
        assertEquals(1, externalGraphic.getInlineContent().getIconHeight());
        assertNull(externalGraphic.getLocation());
    }
}
