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

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import javax.xml.namespace.QName;
import net.opengis.wfs20.QueryType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v2_0.FES;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.wfs.v2_0.WFSTestSupport;
import org.junit.Test;
import org.geotools.api.filter.FilterFactory2;
import org.geotools.api.filter.sort.SortOrder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class WfsQueryTypeTest extends WFSTestSupport {

    @Override
    protected Map<String, String> getNamespaces() {
        return namespaces(Namespace("wfs", WFS.NAMESPACE), Namespace("fes", FES.NAMESPACE));
    }

    @Test
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
        assertNotEquals(attr.indexOf(tmp), -1);
        assertEquals(attr.length(), attr.indexOf(tmp) + tmp.length()); // 8 == ":theType".length

        assertThat(doc, hasXPath("//wfs:Query", notNullValue(String.class)));
        assertThat(doc, hasXPath("//wfs:Query/fes:SortBy", notNullValue(String.class)));
        assertThat(
                doc,
                hasXPath("//wfs:Query/fes:SortBy/fes:SortProperty", notNullValue(String.class)));
        assertThat(
                doc,
                hasXPath(
                        "//wfs:Query/fes:SortBy/fes:SortProperty/fes:ValueReference",
                        notNullValue(String.class)));
        assertThat(
                doc,
                hasXPath(
                        "//wfs:Query/fes:SortBy/fes:SortProperty/fes:ValueReference",
                        equalTo("myProperty")));
        assertThat(
                doc,
                hasXPath("//wfs:Query/fes:SortBy/fes:SortProperty/fes:SortOrder", equalTo("ASC")));
    }
}
