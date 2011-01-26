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

import javax.xml.namespace.QName;

import org.geotools.se.v1_1.SE;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class SEMockData {

    static Element trim(Document document, Node parent) {
        Element trim = element(SE.Trim, document, parent);
        Element strValue = element(SE.StringValue, document, trim);
        
        strValue.appendChild(document.createTextNode("foobarxyz"));
        trim.setAttribute("stripOffPosition", "trailing");
        trim.setAttribute("stripOffChar", "xyz");
        
        return trim;
    }
    
    static Element formatDate(Document document, Node parent) {
        Element formatDate = element(SE.FormatDate, document, parent);
        element(SE.DateValue, document, formatDate)
            .appendChild(document.createTextNode("1981-06-20"));
        element(SE.Pattern, document, formatDate)
            .appendChild(document.createTextNode("yyyy/MM/dd"));
        return formatDate;
    }
    
    static Element changeCase(Document document, Node parent, boolean lower) {
        Element changeCase = element(SE.ChangeCase, document, parent);
        element(SE.StringValue, document, changeCase).appendChild(document.createTextNode("hElLo"));
        changeCase.setAttribute("direction", lower ? "toLower" : "toUpper");
        return changeCase;
    }
    
    static Element concatenate(Document document, Node parent) {
        Element concat = element(SE.Concatenate, document, parent);
        element(SE.StringValue, document, concat).appendChild(document.createTextNode("one"));
        element(SE.StringValue, document, concat).appendChild(document.createTextNode("two"));
        element(SE.StringValue, document, concat).appendChild(document.createTextNode("three"));
        return concat;
    }
    
    static Element stringPosition(Document document, Node parent) {
        Element strPosition = element(SE.StringPosition, document, parent);
        element(SE.LookupString, document, strPosition).appendChild(document.createTextNode("l"));
        element(SE.StringValue, document, strPosition).appendChild(document.createTextNode("hello"));
        strPosition.setAttribute("searchDirection", "backToFront");
       
        return strPosition;
    }
    
    static Element stringLength(Document document, Node parent) {
        Element stringLength = element(SE.StringLength, document, parent);
        element(SE.StringValue, document, stringLength).appendChild(document.createTextNode("hello"));
        
        return stringLength;
    }
    
    static Element substring(Document document, Node parent) {
        Element stringLength = element(SE.Substring, document, parent);
        element(SE.StringValue, document, stringLength).appendChild(document.createTextNode("hello"));
        element(SE.Position, document, stringLength).appendChild(document.createTextNode("2"));
        element(SE.Length, document, stringLength).appendChild(document.createTextNode("3"));
        return stringLength;
    }
    
    static Element element(QName name, Document document, Node parent) {
        Element element = document.createElementNS(name.getNamespaceURI(), name.getLocalPart());

        if (parent != null) {
            parent.appendChild(element);
        }

        return element;
    }

    static Element element(QName name, Document document, Node parent, String text) {
        Element element = element(name, document, parent);

        if (text != null) {
            element.appendChild(document.createTextNode(text));
        }

        return element;
    }
}
