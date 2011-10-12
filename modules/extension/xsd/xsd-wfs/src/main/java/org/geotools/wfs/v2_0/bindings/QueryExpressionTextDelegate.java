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

import java.util.ArrayList;

import javax.xml.namespace.QName;

import net.opengis.wfs20.QueryExpressionTextType;
import net.opengis.wfs20.Wfs20Factory;

import org.geotools.wfs.v2_0.WFS;
import org.geotools.xml.ParserDelegate;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.NamespaceSupport;

public class QueryExpressionTextDelegate extends CopyingHandler implements ParserDelegate {

    static QName QueryExpressionText = new QName(WFS.NAMESPACE, "QueryExpressionText");
    
    QueryExpressionTextType result;

    public QueryExpressionTextDelegate(NamespaceSupport context) {
        super(context);
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

        if (QueryExpressionText.getLocalPart().equals(localName)) {
            //root element
            result = Wfs20Factory.eINSTANCE.createQueryExpressionTextType();
            result.setLanguage(attributes.getValue("language"));
            if (attributes.getValue("isPrivate") != null) {
                String isPrivate = attributes.getValue("isPrivate");
                result.setIsPrivate("true".equalsIgnoreCase(isPrivate) || "1".equals(isPrivate));
            }
            
            result.setReturnFeatureTypes(new ArrayList<QName>());
            for (String returnType : attributes.getValue("returnFeatureTypes").split(" +")) {
                QName typeName = null;
                String[] split = returnType.split(":");
                if (split.length == 1) {
                    typeName = new QName(split[0]);
                }
                else {
                    String prefix = split[0];
                    typeName = new QName(namespaceContext.getURI(prefix), split[1], prefix);
                }
                result.getReturnFeatureTypes().add(typeName);
            }
        }
        else {
            super.startElement(uri, localName, qName, attributes);
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (QueryExpressionText.getLocalPart().equals(localName)) {
            result.setValue(buffer.toString());
        }
        else {
            super.endElement(uri, localName, qName);
        }
    }
    
    public boolean canHandle(QName elementName) {
        return QueryExpressionText.equals(elementName);
    }

    public Object getParsedObject() {
        return result;
    }
}
