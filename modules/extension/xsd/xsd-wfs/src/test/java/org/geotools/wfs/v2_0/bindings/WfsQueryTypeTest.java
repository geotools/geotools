/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.wfs.v2_0.bindings;

import javax.xml.namespace.QName;
import net.opengis.wfs20.QueryType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WfsQueryTypeTest extends WFSTestSupport {

    public void testEncode() throws Exception {
        QName typeName = new QName("http://www.test.com/query", "theType");
        QueryType query = Wfs20Factory.eINSTANCE.createQueryType();
        query.getTypeNames().add(typeName);

        Document doc = encode(query, WFS.Query);
        Element root = doc.getDocumentElement();
        String attr = root.getAttribute("typeNames");
        assertNotNull(attr);

        String tmp = typeName.getLocalPart();

        assertFalse(attr.startsWith("[{"));
        assertTrue(attr.indexOf(tmp) != -1);
        assertEquals(attr.length(), attr.indexOf(tmp) + tmp.length()); // 8 == ":theType".length
    }
}
