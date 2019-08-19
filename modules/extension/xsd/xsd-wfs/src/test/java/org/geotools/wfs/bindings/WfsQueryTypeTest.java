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

package org.geotools.wfs.bindings;

import java.util.Collections;
import javax.xml.namespace.QName;
import net.opengis.wfs.QueryType;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.wfs.v1_1.WFS;
import org.geotools.xsd.Binding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WfsQueryTypeTest extends WFSTestSupport {

    public WfsQueryTypeTest() {
        super(WFS.QueryType, QueryType.class, Binding.OVERRIDE);
    }

    @Override
    public void testParse() throws Exception {}

    @Override
    public void testEncode() throws Exception {
        QName typeName = new QName("http://www.test.com/query", "theType");

        QueryType wfsQuery = factory.createQueryType();
        wfsQuery.setTypeName(Collections.singletonList(typeName));

        Document doc = encode(wfsQuery, WFS.Query);
        Element root = doc.getDocumentElement();
        String attr = root.getAttribute("typeName");
        assertNotNull(attr);

        String tmp = ":" + typeName.getLocalPart();

        assertFalse(attr.startsWith("[{"));
        assertTrue(attr.indexOf(tmp) != -1);
        assertEquals(attr.length(), attr.indexOf(tmp) + tmp.length()); // 8 == ":theType".length
    }
}
