/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.io.StringReader;
import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import net.opengis.wfs20.QueryExpressionTextType;
import net.opengis.wfs20.Wfs20Factory;
import org.geotools.wfs.v2_0.WFS;
import org.geotools.xsd.AbstractComplexEMFBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Binding object for the type http://www.opengis.net/wfs/2.0:QueryExpressionTextType.
 *
 * <p>
 *
 * <pre>
 *  <code>
 *  &lt;xsd:complexType mixed="true" name="QueryExpressionTextType"&gt;
 *      &lt;xsd:choice&gt;
 *          &lt;xsd:any maxOccurs="unbounded" minOccurs="0" namespace="##other" processContents="skip"/&gt;
 *          &lt;xsd:any maxOccurs="unbounded" minOccurs="0"
 *              namespace="##targetNamespace" processContents="skip"/&gt;
 *      &lt;/xsd:choice&gt;
 *      &lt;xsd:attribute name="returnFeatureTypes"
 *          type="wfs:ReturnFeatureTypesListType" use="required"/&gt;
 *      &lt;xsd:attribute name="language" type="xsd:anyURI" use="required"/&gt;
 *      &lt;xsd:attribute default="false" name="isPrivate" type="xsd:boolean"/&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *   </code>
 * </pre>
 *
 * @generated
 */
public class QueryExpressionTextTypeBinding extends AbstractComplexEMFBinding {

    NamespaceSupport namespaceContext;

    public QueryExpressionTextTypeBinding(Wfs20Factory factory, NamespaceSupport namespaceContext) {
        super(factory, QueryExpressionTextType.class);
        this.namespaceContext = namespaceContext;
    }
    /** @generated */
    public QName getTarget() {
        return WFS.QueryExpressionTextType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        // parsing handled by QueryExpressionTextDelegate
        return null;
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        Element e = super.encode(object, document, value);

        QueryExpressionTextType qe = (QueryExpressionTextType) object;
        if (!qe.isIsPrivate()) {
            // include the query text

            // this is a hack, but we need to build up a dom with namespaces without actually
            // having them declared by the expression text, so we first parse the query
            // expression with a sax handler that can transform to a namespace aware dom
            // using the current namespace context
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setNamespaceAware(true);

            ConvertToDomHandler h =
                    new ConvertToDomHandler(
                            dbf.newDocumentBuilder().newDocument(), namespaceContext);

            SAXParser saxp = SAXParserFactory.newInstance().newSAXParser();
            saxp.parse(new InputSource(new StringReader(qe.getValue())), h);

            Document d = h.getDocument();
            e.appendChild(document.importNode(d.getDocumentElement(), true));
        }

        return e;
    }
}
