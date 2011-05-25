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

import java.net.URL;
import java.util.List;

import javax.xml.namespace.QName;

import net.opengis.ows10.KeywordsType;
import net.opengis.ows10.Ows10Factory;
import net.opengis.ows10.WGS84BoundingBoxType;
import net.opengis.wfs.FeatureTypeListType;
import net.opengis.wfs.FeatureTypeType;
import net.opengis.wfs.OperationType;
import net.opengis.wfs.OperationsType;
import net.opengis.wfs.OutputFormatListType;

import org.geotools.ows.OWS;
import org.geotools.test.TestData;
import org.geotools.wfs.WFS;
import org.geotools.wfs.WFSTestSupport;
import org.geotools.xml.Binding;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * Unit test suite for {@link FeatureTypeListTypeBinding}
 * 
 * @author Gabriel Roldan
 * @version $Id: FeatureTypeListTypeBindingTest.java 27749 2007-11-05 09:51:33Z
 *          groldan $
 * @since 2.5.x
 *
 * @source $URL$
 */
public class FeatureTypeListTypeBindingTest extends WFSTestSupport {
    public FeatureTypeListTypeBindingTest() {
        super(WFS.FeatureTypeListType, FeatureTypeListType.class, Binding.OVERRIDE);
    }

    public void testEncode() throws Exception {
        FeatureTypeListType ftl = factory.createFeatureTypeListType();
        {
            OperationsType ops = factory.createOperationsType();
            ops.getOperation().add(OperationType.DELETE_LITERAL);
            ops.getOperation().add(OperationType.INSERT_LITERAL);
            ops.getOperation().add(OperationType.QUERY_LITERAL);
            ftl.setOperations(ops);
        }
        {
            FeatureTypeType ft = factory.createFeatureTypeType();
            ft.setName(new QName("http://www.openplans.org/topp", "Type1"));
            ft.setTitle("Title1");
            ft.setAbstract("Abstract1");
            KeywordsType kwd = Ows10Factory.eINSTANCE.createKeywordsType();
            kwd.getKeyword().add("keword1");
            kwd.getKeyword().add("keword2");
            ft.getKeywords().add(kwd);
            ft.setDefaultSRS("urn:ogc:crs:EPSG:6.7:4326");
            ft.getOtherSRS().add("urn:ogc:crs:EPSG:6.7:23030");
            ft.setOperations(factory.createOperationsType());
            OperationsType operations = ft.getOperations();
            List operationList = operations.getOperation();
            operationList.add(OperationType.QUERY_LITERAL);
            operationList.add(OperationType.INSERT_LITERAL);
            operationList.add(OperationType.UPDATE_LITERAL);
            operationList.add(OperationType.DELETE_LITERAL);
            OutputFormatListType outputFormat = factory.createOutputFormatListType();
            outputFormat.getFormat().add("GML2");
            ft.setOutputFormats(outputFormat);
            WGS84BoundingBoxType bbox = Ows10Factory.eINSTANCE.createWGS84BoundingBoxType();
            bbox.setCrs("urn:ogc:crs:EPSG:6.7:4326");
            ft.getWGS84BoundingBox().add(bbox);
            ftl.getFeatureType().add(ft);
        }

        final Document dom = encode(ftl, WFS.FeatureTypeList);
        final Element root = dom.getDocumentElement();

        assertName(WFS.FeatureTypeList, root);
        assertOperations(root);
        Element ft = getElementByQName(root, new QName(WFS.NAMESPACE, "FeatureType"));
        assertNotNull(ft);
        assertFeatureType(ft);        
    }

    private void assertFeatureType(Element ft) {
        Element typeName = getElementByQName(ft, new QName(WFS.NAMESPACE, "Name"));
        assertEquals("Type1", typeName.getFirstChild().getNodeValue());
        Element title = getElementByQName(ft, new QName(WFS.NAMESPACE, "Title"));
        assertEquals("Title1", title.getFirstChild().getNodeValue());
        Element abstract_ = getElementByQName(ft, new QName(WFS.NAMESPACE, "Abstract"));
        assertEquals("Abstract1", abstract_.getFirstChild().getNodeValue());
        Element keywords = getElementByQName(ft, OWS.Keywords);
        assertNotNull(keywords);
        assertEquals(2, getElementsByQName(keywords, new QName(OWS.NAMESPACE, "Keyword")).getLength());
        
        Element defaultCrs = getElementByQName(ft, new QName(WFS.NAMESPACE, "DefaultSRS"));
        assertEquals("urn:ogc:crs:EPSG:6.7:4326", defaultCrs.getFirstChild().getNodeValue());
        Element otherSrs = getElementByQName(ft, new QName(WFS.NAMESPACE, "OtherSRS"));
        assertEquals("urn:ogc:crs:EPSG:6.7:23030", otherSrs.getFirstChild().getNodeValue());
        
        Element operations = getElementByQName(ft, new QName(WFS.NAMESPACE, "Operations"));
        assertNotNull(operations);
        NodeList ops = getElementsByQName(operations, new QName(WFS.NAMESPACE, "Operation"));
        assertEquals(4, ops.getLength());
        assertEquals("Query", ops.item(0).getFirstChild().getNodeValue());
        assertEquals("Insert", ops.item(1).getFirstChild().getNodeValue());
        assertEquals("Update", ops.item(2).getFirstChild().getNodeValue());
        assertEquals("Delete", ops.item(3).getFirstChild().getNodeValue());
        
        Element outputFormats = getElementByQName(ft, new QName(WFS.NAMESPACE, "OutputFormats"));
        assertNotNull(outputFormats);
        NodeList formats = getElementsByQName(outputFormats, new QName(WFS.NAMESPACE, "Format"));
        assertEquals(1, formats.getLength());
        assertEquals("GML2", formats.item(0).getFirstChild().getNodeValue());
    }

    private void assertOperations(final Element root) {
        Element operations = getElementByQName(root, new QName(WFS.NAMESPACE, "Operations"));
        assertNotNull(operations);
        NodeList ops = getElementsByQName(operations, new QName(WFS.NAMESPACE, "Operation"));
        assertEquals(3, ops.getLength());
        assertEquals("Delete", ops.item(0).getFirstChild().getNodeValue());
        assertEquals("Insert", ops.item(1).getFirstChild().getNodeValue());
        assertEquals("Query", ops.item(2).getFirstChild().getNodeValue());
    }

    public void testParse() throws Exception {
        final URL resource = TestData.getResource(this, "FeatureTypeListTypeBindingTest.xml");
        buildDocument(resource);

        final Object parsed = parse(WFS.FeatureTypeList);
        assertNotNull(parsed);
        assertTrue(parsed instanceof FeatureTypeListType);

        final FeatureTypeListType ftl = (FeatureTypeListType) parsed;

        List operations = ftl.getOperations().getOperation();
        assertEquals(4, operations.size());
        assertSame(OperationType.DELETE_LITERAL, operations.get(0));
        assertSame(OperationType.INSERT_LITERAL, operations.get(1));
        assertSame(OperationType.QUERY_LITERAL, operations.get(2));
        assertSame(OperationType.GET_GML_OBJECT_LITERAL, operations.get(3));

        assertEquals(1, ftl.getFeatureType().size());
        FeatureTypeType ft = (FeatureTypeType) ftl.getFeatureType().get(0);
        assertEquals(new QName("http://www.openplans.org/topp", "name1"), ft.getName());
        assertEquals("title1", ft.getTitle());
        assertEquals(1, ft.getKeywords().size());
        KeywordsType kw = (KeywordsType) ft.getKeywords().get(0);
        assertEquals(2, kw.getKeyword().size());
        assertEquals("urn:ogc:crs:EPSG:6.7:4326", ft.getDefaultSRS());
        assertEquals(1, ft.getOtherSRS().size());
        assertEquals("urn:ogc:crs:EPSG:6.7:23030", ft.getOtherSRS().get(0));
        assertEquals(1, ft.getOutputFormats().getFormat().size());
        assertEquals("GML2", ft.getOutputFormats().getFormat().get(0));
        assertNotNull(ft.getWGS84BoundingBox());
    }
}
