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

import java.util.HashMap;
import javax.xml.namespace.QName;
import net.opengis.wfs20.QueryType;
import net.opengis.wfs20.Wfs20Factory;
import org.custommonkey.xmlunit.SimpleNamespaceContext;
import org.custommonkey.xmlunit.XMLAssert;
import org.custommonkey.xmlunit.XMLUnit;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v2_0.FES;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortOrder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WfsQueryTypeTest extends WFSTestSupport {

    public void testEncode() throws Exception {
        QName typeName = new QName("http://www.test.com/query", "theType");
        QueryType query = Wfs20Factory.eINSTANCE.createQueryType();
        query.getTypeNames().add(typeName);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        query.getSortBy().add(ff.sort("myProperty", SortOrder.ASCENDING));

        Document doc = encode(query, WFS.Query);

        Element root = doc.getDocumentElement();
        String attr = root.getAttribute("typeNames");
        assertNotNull(attr);

        String tmp = typeName.getLocalPart();

        assertFalse(attr.startsWith("[{"));
        assertTrue(attr.indexOf(tmp) != -1);
        assertEquals(attr.length(), attr.indexOf(tmp) + tmp.length()); // 8 == ":theType".length

        HashMap<String, String> m = new HashMap<String, String>();
        m.put("wfs", WFS.NAMESPACE);
        m.put("fes", FES.NAMESPACE);
        XMLUnit.setXpathNamespaceContext(new SimpleNamespaceContext(m));
        XMLAssert.assertXpathExists("//wfs:Query", doc);
        XMLAssert.assertXpathExists("//wfs:Query/fes:SortBy", doc);
        XMLAssert.assertXpathExists("//wfs:Query/fes:SortBy/fes:SortProperty", doc);
        XMLAssert.assertXpathExists(
                "//wfs:Query/fes:SortBy/fes:SortProperty/fes:ValueReference", doc);
        XMLAssert.assertXpathEvaluatesTo(
                "myProperty", "//wfs:Query/fes:SortBy/fes:SortProperty/fes:ValueReference", doc);
        XMLAssert.assertXpathEvaluatesTo(
                "ASC", "//wfs:Query/fes:SortBy/fes:SortProperty/fes:SortOrder", doc);
    }
}
