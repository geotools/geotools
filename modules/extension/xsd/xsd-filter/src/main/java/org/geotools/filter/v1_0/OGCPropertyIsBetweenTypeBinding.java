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
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.expression.Expression;
import org.geotools.xml.AbstractComplexBinding;
import org.geotools.xml.ElementInstance;
import org.geotools.xml.Node;


/**
 * Binding object for the type http://www.opengis.net/ogc:PropertyIsBetweenType.
 *
 * <p>
 *        <pre>
 *         <code>
 *  &lt;xsd:complexType name="PropertyIsBetweenType"&gt;
 *      &lt;xsd:complexContent&gt;
 *          &lt;xsd:extension base="ogc:ComparisonOpsType"&gt;
 *              &lt;xsd:sequence&gt;
 *                  &lt;xsd:element ref="ogc:expression"/&gt;
 *                  &lt;xsd:element name="LowerBoundary" type="ogc:LowerBoundaryType"/&gt;
 *                  &lt;xsd:element name="UpperBoundary" type="ogc:UpperBoundaryType"/&gt;
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
 * @source $URL$
 */
public class OGCPropertyIsBetweenTypeBinding extends AbstractComplexBinding {
    private FilterFactory factory;

    public OGCPropertyIsBetweenTypeBinding(FilterFactory factory) {
        this.factory = factory;
    }

    /**
     * @generated
     */
    public QName getTarget() {
        return OGC.PropertyIsBetweenType;
    }

    /**
     * <!-- begin-user-doc -->
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Class getType() {
        return PropertyIsBetween.class;
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
     * <!-- end-user-doc -->
     *
     * @generated modifiable
     */
    public Object parse(ElementInstance instance, Node node, Object value)
        throws Exception {
        Expression e = (Expression) node.getChildValue(0);
        Expression l = (Expression) node.getChildValue(1);
        Expression u = (Expression) node.getChildValue(2);

        return factory.between(e, l, u);
    }

    public Object getProperty(Object object, QName name)
        throws Exception {
        PropertyIsBetween between = (PropertyIsBetween) object;

        //&lt;xsd:element ref="ogc:expression"/&gt;
        if (OGC.expression.equals(name)) {
            return between.getExpression();
        }

        //&lt;xsd:element name="LowerBoundary" type="ogc:LowerBoundaryType"/&gt;
        if ("LowerBoundary".equals(name.getLocalPart())) {
            return between.getLowerBoundary();
        }

        //&lt;xsd:element name="UpperBoundary" type="ogc:UpperBoundaryType"/&gt;
        if ("UpperBoundary".equals(name.getLocalPart())) {
            return between.getUpperBoundary();
        }

        return null;
    }
}
