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
package org.geotools.wfs.bindings;

import java.math.BigInteger;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.wfs.QueryType;
import net.opengis.wfs.XlinkPropertyNameType;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.v1_1.OGC;
import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.Id;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Unit test suite for {@link QueryTypeBinding}
 * 
 * @author Justin Deoliveira
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.5.x
 * @source $URL:
 *         http://svn.geotools.org/geotools/trunk/gt/modules/extension/xsd/wfs/src/test/java/org/geotools/wfs/bindings/QueryTypeBindingTest.java $
 */
public class QueryTypeBindingTest extends WFSTestSupport {
    public QueryTypeBindingTest() {
        super(WFS.QueryType, QueryType.class, Binding.OVERRIDE);
    }

    public void testParse() throws Exception {
        final URL resource = TestData.getResource(this, "QueryTypeBindingTest.xml");
        buildDocument(resource);

        final Object parsed = parse(WFS.Query);
        assertTrue((parsed == null) ? "null" : parsed.getClass().toString(),
                parsed instanceof QueryType);

        QueryType query = (QueryType) parsed;
        List typeNames = query.getTypeName();
        assertEquals(1, typeNames.size());

        QName typeName = new QName("http://www.geotools.org/test", "testType1");
        Object parsedName = typeNames.get(0);
        assertEquals(typeName, parsedName);
        assertEquals("testHandle", query.getHandle());
        assertEquals("HEAD", query.getFeatureVersion());
        assertEquals("urn:x-ogc:def:crs:EPSG:6.11.2:4326", query.getSrsName().toString());

        assertEquals(2, query.getPropertyName().size());
        assertEquals(2, query.getXlinkPropertyName().size());
        assertEquals(2, query.getFunction().size());

        assertEquals("property1", query.getPropertyName().get(0));
        assertEquals("property2", query.getPropertyName().get(1));

        XlinkPropertyNameType xlink;
        xlink = (XlinkPropertyNameType) query.getXlinkPropertyName().get(0);
        assertEquals("gt:propertyA/gt:propertyB", xlink.getValue());
        assertEquals("*", xlink.getTraverseXlinkDepth());
        assertEquals(BigInteger.valueOf(10), xlink.getTraverseXlinkExpiry());

        xlink = (XlinkPropertyNameType) query.getXlinkPropertyName().get(1);
        assertEquals("gt:propertyC/gt:propertyD", xlink.getValue());
        assertEquals("1", xlink.getTraverseXlinkDepth());
        assertNull(xlink.getTraverseXlinkExpiry());

        Function function;
        function = (Function) query.getFunction().get(0);
        assertNotNull(function);
        assertEquals("max", function.getName());

        function = (Function) query.getFunction().get(1);
        assertNotNull(function);
        assertEquals("min", function.getName());

        assertTrue(query.getFilter() instanceof Id);
        assertEquals(1, query.getSortBy().size());
        assertTrue(query.getSortBy().get(0) instanceof SortBy);
    }

    @SuppressWarnings("unchecked")
    public void testEncode() throws Exception {
        final QueryType query = buildTestQuery();

        final Document dom = encode(query, WFS.Query);
        final Element root = dom.getDocumentElement();

        assertName(WFS.Query, root);
        assertEquals(8, root.getChildNodes().getLength());
        
        assertEquals("typeName1", root.getAttribute("typeName"));

        assertEquals(2, getElementsByQName(dom, WFS.PropertyName).getLength());
        assertEquals(2, getElementsByQName(dom, WFS.XlinkPropertyName).getLength());
        assertEquals(2, getElementsByQName(dom, OGC.Function).getLength());

        assertEquals("property1", getElementValueByQName(dom, WFS.PropertyName, 0));
        assertEquals("property2", getElementValueByQName(dom, WFS.PropertyName, 1));

        assertEquals("propertyA/propertyB", getElementValueByQName(dom, WFS.XlinkPropertyName, 0));
        assertEquals("propertyC/propertyD", getElementValueByQName(dom, WFS.XlinkPropertyName, 1));

        assertNotNull(getElementByQName(dom, OGC.Filter));
        assertNotNull(getElementByQName(dom, OGC.FeatureId));

        assertNotNull(getElementByQName(dom, OGC.SortBy));
    }

    /**
     * Builds a {@link QueryType} instance equivalent to the test query in the
     * file <code>test-data/QueryTypeBinding.xml</code> in the
     * <code>org.geotools.wfs.bindings</code> package of the test resources.
     * 
     * @return
     */
    @SuppressWarnings("unchecked")
    private QueryType buildTestQuery() {
        FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

        final Function function1 = ff.function("MAX", new Expression[] { ff.literal(1),
                ff.literal(2) });
        final Function function2 = ff.function("MIN", new Expression[] { ff.literal(1),
                ff.literal(2) });

        final XlinkPropertyNameType xlinkPropertyName1 = factory.createXlinkPropertyNameType();
        xlinkPropertyName1.setTraverseXlinkExpiry(BigInteger.valueOf(10));
        xlinkPropertyName1.setTraverseXlinkDepth("*");
        xlinkPropertyName1.setValue("propertyA/propertyB");

        final XlinkPropertyNameType xlinkPropertyName2 = factory.createXlinkPropertyNameType();
        xlinkPropertyName2.setTraverseXlinkDepth("1");
        xlinkPropertyName2.setValue("propertyC/propertyD");

        // The first property is a Choice[0..*] of PropertyName,
        // XLinkPropertyName and Function
        QueryType query = factory.createQueryType();
        query.setTypeName(Collections.singletonList("typeName1"));
        query.getPropertyName().add("property1");
        query.getXlinkPropertyName().add(xlinkPropertyName1);
        query.getFunction().add(function1);
        query.getPropertyName().add("property2");
        query.getXlinkPropertyName().add(xlinkPropertyName2);
        query.getFunction().add(function2);

        query.setFilter(ff.id(Collections.singleton(ff.featureId("fid"))));
        query.getSortBy().add(ff.sort("sortProperty", SortOrder.DESCENDING));

        return query;
    }
}
