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

import javax.xml.namespace.QName;
import org.geotools.filter.Filters;
import org.geotools.gml3.bindings.GML3EncodingUtils;
import org.geotools.xsd.AbstractComplexBinding;
import org.geotools.xsd.ElementInstance;
import org.geotools.xsd.Node;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.picocontainer.MutablePicoContainer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * Binding object for the type http://www.opengis.net/ogc:PropertyNameType.
 *
 * <p>
 *
 * <pre>
 *         <code>
 *  &lt;xsd:complexType name="PropertyNameType"&gt;
 *      &lt;xsd:complexContent mixed="true"&gt;
 *          &lt;xsd:extension base="ogc:ExpressionType"/&gt;
 *      &lt;/xsd:complexContent&gt;
 *  &lt;/xsd:complexType&gt;
 *
 *          </code>
 *         </pre>
 *
 * @generated
 */
public class OGCPropertyNameTypeBinding extends AbstractComplexBinding {
    protected FilterFactory2 factory;

    /** parser namespace mappings */
    protected NamespaceSupport namespaceSupport;

    public OGCPropertyNameTypeBinding(FilterFactory2 factory, NamespaceSupport namespaceSupport) {
        this.factory = factory;
        this.namespaceSupport = namespaceSupport;
    }

    /** @generated */
    @Override
    public QName getTarget() {
        return OGC.PropertyNameType;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public int getExecutionMode() {
        return AFTER;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Class getType() {
        return PropertyName.class;
    }

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public void initialize(ElementInstance instance, Node node, MutablePicoContainer context) {}

    /**
     *
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    @Override
    public Object parse(ElementInstance instance, Node node, Object value) throws Exception {
        Expression expression = (Expression) value;
        String xpath = Filters.asString(expression);

        // if null returned, assume empty string == default geometry
        if (xpath == null) {
            xpath = "";
        }

        return factory.property(xpath, GML3EncodingUtils.copyNamespaceSupport(namespaceSupport));
    }

    @Override
    public Element encode(Object object, Document document, Element value) throws Exception {
        PropertyName propertyName = (PropertyName) object;

        if (propertyName.getPropertyName() != null) {
            value.appendChild(document.createTextNode(propertyName.getPropertyName()));
        }

        return value;
    }
}
