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

import org.picocontainer.MutablePicoContainer;
import javax.xml.namespace.QName;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/ogc:ExpressionType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType abstract="true" mixed="true" name="ExpressionType"/&gt;
 *
 *          </code>
 *         </pre>
 * </p>
 *
 * @generated
 *
 * @source $URL$
 */
public class OGCExpressionTypeBinding extends AbstractComplexBinding {
    FilterFactory filterFactory;

    public OGCExpressionTypeBinding(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.ExpressionType;
    }

    /**
     * <!-- begin-user-doc -->
     * This stratagy object is a NOP
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
        return Expression.class;
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
     * This binding simply looks for any text in the node and turns it into
     * a literal expression. If differnt behaviour is required by a sub binding
     * then they should ovveride.
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        return filterFactory.literal(value);
    }
}
