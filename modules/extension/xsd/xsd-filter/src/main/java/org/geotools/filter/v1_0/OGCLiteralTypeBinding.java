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
package org.geotools.filter.v1_0;

import java.util.Calendar;
import java.util.TimeZone;

import javax.xml.namespace.QName;

import org.geotools.util.Converters;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;
import org.geotools.xml.Text;
import org.geotools.xml.impl.DatatypeConverterImpl;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Literal;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * Binding object for the type http://www.opengis.net/ogc:LiteralType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="LiteralType"&gt;
 *      &lt;xsd:complexContent mixed="true"&gt;
 *          &lt;xsd:extension base="ogc:ExpressionType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:any minOccurs="0"/&gt;
 *              &lt;/xsd:sequence&gt;
 *          &lt;/xsd:extension&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 *
 *
 * @source $URL$
 */
public class OGCLiteralTypeBinding extends AbstractComplexBinding {
    private FilterFactory factory;

    public OGCLiteralTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.LiteralType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public int getExecutionMode() {
        return OVERRIDE;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return Literal.class;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {
    }

    /**
     * <!-- begin-user-doc -->
     * Just pass on emeded value as is.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        //number of possibilities here since single child is of type any

        //1. has child elements
        if (!node.getChildren().isEmpty()) {
            Object childValue = node.getChildValue(0);
            if(childValue instanceof Text){
                childValue = ((Text)childValue).getValue();
            }
            return factory.literal(childValue);
        }

        //2. no child elements, just return the text if any
        return factory.literal(value);
    }

    public Element encode(Object object, Document document, Element value)
        throws Exception {
        Literal literal = (Literal) object;

        Object unconvertedValue = literal.getValue();
        if (unconvertedValue != null) {
            // use converter api to sreialize
            String textValue = Converters.convert(unconvertedValue, String.class);
            value.appendChild(document.createTextNode(textValue));
        }

        return value;
    }
}
